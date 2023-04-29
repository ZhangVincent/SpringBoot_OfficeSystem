package com.zkp.auth.service.impl;

import com.zkp.auth.service.SysMenuService;
import com.zkp.auth.service.SysUserService;
import com.zkp.common.exception.MyException;
import com.zkp.common.result.ResultCodeEnum;
import com.zkp.model.system.SysUser;
import com.zkp.security.custom.CustomUser;
import com.zkp.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * spring security业务对象，根据用户名查询用户信息，封装后返回自定义的客户对象
 */
//@Component
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getUserByUsername(username);
        // 需要在service中新增对应方法
        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new MyException(ResultCodeEnum.ACCOUNT_STOP);
        }

        List<String> userPermsList = sysMenuService.findUserPermsListByUserId(sysUser.getId());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String perm : userPermsList) {
            authorities.add(new SimpleGrantedAuthority(perm.trim()));
        }
        return new CustomUser(sysUser, authorities);
    }
}
