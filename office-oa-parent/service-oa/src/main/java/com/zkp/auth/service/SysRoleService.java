package com.zkp.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zkp.model.system.SysRole;
import com.zkp.vo.system.AssginRoleVo;
import org.springframework.stereotype.Service;

import java.util.Map;

//@Service
public interface SysRoleService extends IService<SysRole> {

    Map<String, Object> findRoleByUserId(Long userId);

    boolean doAssign(AssginRoleVo assginRoleVo);
}
