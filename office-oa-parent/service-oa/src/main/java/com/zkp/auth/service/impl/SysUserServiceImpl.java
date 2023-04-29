package com.zkp.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkp.auth.mapper.SysUserMapper;
import com.zkp.auth.service.SysDeptService;
import com.zkp.auth.service.SysPostService;
import com.zkp.auth.service.SysUserService;
import com.zkp.model.system.SysUser;
import com.zkp.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zkp
 * @since 2023-04-17
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysPostService sysPostService;

    @Override
    public boolean updateStatus(Long id, Integer status) {
        SysUser sysUser = this.getById(id);
        if (status==1){
            sysUser.setStatus(1);
        }else {
            sysUser.setStatus(0);
        }
        return this.updateById(sysUser);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username);
        return this.getOne(qw);
    }

    @Override
    public Map<String,Object> getCurrentUser() {
        SysUser sysUser = baseMapper.selectById(LoginUserInfoHelper.getUserId());
        //SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
        //SysPost sysPost = sysPostService.getById(sysUser.getPostId());
        Map<String, Object> map = new HashMap<>();
        map.put("name", sysUser.getName());
        map.put("phone", sysUser.getPhone());
        //map.put("deptName", sysDept.getName());
        //map.put("postName", sysPost.getName());
        return map;
    }
}
