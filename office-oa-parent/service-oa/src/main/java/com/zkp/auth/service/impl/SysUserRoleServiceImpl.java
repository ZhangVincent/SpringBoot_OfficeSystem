package com.zkp.auth.service.impl;

import com.zkp.model.system.SysUserRole;
import com.zkp.auth.mapper.SysUserRoleMapper;
import com.zkp.auth.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 用户角色 服务实现类
 * </p>
 *
 * @author zkp
 * @since 2023-04-17
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
}
