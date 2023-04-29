package com.zkp.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkp.auth.mapper.SysRoleMenuMapper;
import com.zkp.auth.utils.MenuHelper;
import com.zkp.common.exception.MyException;
import com.zkp.common.result.ResultCodeEnum;
import com.zkp.model.system.SysMenu;
import com.zkp.auth.mapper.SysMenuMapper;
import com.zkp.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkp.model.system.SysRoleMenu;
import com.zkp.vo.system.AssginMenuVo;
import com.zkp.vo.system.MetaVo;
import com.zkp.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author zkp
 * @since 2023-04-17
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> sysMenuList = this.list();
        if (CollectionUtils.isEmpty(sysMenuList)) return null;
        List<SysMenu> result = MenuHelper.buildTree(sysMenuList);
        return result;
    }

    @Override
    public boolean removeMenuById(Serializable id) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id);
        int count = this.count(queryWrapper);
        if (count > 0) {
            throw new MyException(ResultCodeEnum.FAIL);
        }
        return this.removeById(id);
    }

    @Override
    public List<SysMenu> findSysMenuById(Long roleId) {
        List<SysMenu> sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));

        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuMapper.selectList(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        List<Long> menuIds = sysRoleMenuList.stream().map(c -> c.getMenuId()).collect(Collectors.toList());
        sysMenuList.stream().forEach(menu -> {
            if (menuIds.contains(menu.getId())) {
                menu.setSelect(true);
            } else {
                menu.setSelect(false);
            }
        });

        return MenuHelper.buildTree(sysMenuList);
    }

    @Override
    public boolean doAssign(AssginMenuVo assginMenuVo) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, assginMenuVo.getRoleId());
        sysRoleMenuMapper.delete(queryWrapper);
        for (Long id : assginMenuVo.getMenuIdList()) {
            if (!StringUtils.isEmpty(id)) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setMenuId(id);
                sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
                int insert = sysRoleMenuMapper.insert(sysRoleMenu);
                if (insert <= 0) return false;
            }
        }
        return true;
    }

    @Override
    public List<RouterVo> findUserMenuListByUserId(Long userId) {
        List<SysMenu> sysMenuList = null;
        // 约定id为1的是管理员
        if (userId == 1) {
            sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1).orderByAsc(SysMenu::getSortValue));
        } else {
            sysMenuList = sysMenuMapper.findMenuListByUserId(userId);
        }
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);
        List<RouterVo> routerVoList = this.buildMenus(sysMenuTreeList);
        return routerVoList;
    }

    /**
     * 将菜单列表转化成前端的路由列表
     * @param sysMenuTreeList
     * @return
     */
    private List<RouterVo> buildMenus(List<SysMenu> sysMenuTreeList) {
        List<RouterVo> routers = new ArrayList<>();
        for (SysMenu menu : sysMenuTreeList) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            List<SysMenu> children = menu.getChildren();
            //如果当前是菜单，需将按钮对应的路由加载出来，如：“角色授权”按钮对应的路由在“系统管理”下面
            if (menu.getType().intValue() == 1) {
                List<SysMenu> hiddenMenuList = children.stream().filter(item -> !StringUtils.isEmpty(item.getComponent())).collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    // 隐藏路由
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routers.add(hiddenRouter);
                }
            } else {
                if (!CollectionUtils.isEmpty(children)) {
                    if (children.size() > 0) {
                        router.setAlwaysShow(true);
                    }
                    router.setChildren(buildMenus(children));
                }
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
//        String routerPath = "/" + menu.getPath();
//        if(menu.getParentId().intValue() != 0) {
//            routerPath = menu.getPath();
//        }
//        return routerPath;
        return menu.getParentId().intValue() != 0 ? menu.getPath() : "/" + menu.getPath();
    }

    @Override
    public List<String> findUserPermsListByUserId(Long userId) {
        //超级管理员admin账号id为：1
        List<SysMenu> sysMenuList = null;
        if (userId == 1) {
            sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));
        } else {
            sysMenuList = sysMenuMapper.findMenuListByUserId(userId);
        }
        List<String> permsList = sysMenuList.stream().filter(item -> item.getType() == 2).map(item -> item.getPerms()).collect(Collectors.toList());
        return permsList;
    }

}
