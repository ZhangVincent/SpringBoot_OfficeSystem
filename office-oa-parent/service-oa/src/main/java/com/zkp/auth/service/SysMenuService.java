package com.zkp.auth.service;

import com.zkp.model.system.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zkp.vo.system.AssginMenuVo;
import com.zkp.vo.system.RouterVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author zkp
 * @since 2023-04-17
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();

    boolean removeMenuById(Serializable id);

    List<SysMenu> findSysMenuById(Long roleId);

    boolean doAssign(AssginMenuVo assginMenuVo);

    List<RouterVo> findUserMenuListByUserId(Long userId);

    List<String> findUserPermsListByUserId(Long userId);
}
