package com.zkp.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkp.auth.mapper.SysRoleMapper;
import com.zkp.auth.mapper.SysUserRoleMapper;
import com.zkp.auth.service.SysRoleService;
import com.zkp.model.system.SysRole;
import com.zkp.model.system.SysUser;
import com.zkp.model.system.SysUserRole;
import com.zkp.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public Map<String, Object> findRoleByUserId(Long userId) {
        List<SysRole> sysRoleList = this.list();

        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId);
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectList(queryWrapper);
        List<Long> existRoleIdList = sysUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());
        List<SysRole> assignRoleList = new ArrayList<>();
        for (SysRole role : sysRoleList) {
            if (existRoleIdList.contains(role.getId())) {
                assignRoleList.add(role);
            }
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("assignRoleList", assignRoleList);
        map.put("allRolesList", sysRoleList);
        return map;
    }

    @Override
    public boolean doAssign(AssginRoleVo assginRoleVo) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, assginRoleVo.getUserId());
        sysUserRoleMapper.delete(queryWrapper);
        for (Long id : assginRoleVo.getRoleIdList()) {
            if (!StringUtils.isEmpty(id)) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(assginRoleVo.getUserId());
                sysUserRole.setRoleId(id);
                int i = sysUserRoleMapper.insert(sysUserRole);
                if (i <= 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
