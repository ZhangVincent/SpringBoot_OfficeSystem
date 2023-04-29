package com.zkp.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zkp.model.system.SysUser;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zkp
 * @since 2023-04-17
 */
public interface SysUserService extends IService<SysUser> {

    boolean updateStatus(Long id, Integer status);

    SysUser getUserByUsername(String username);

    Map<String,Object> getCurrentUser();
}
