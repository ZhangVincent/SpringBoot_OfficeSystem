package com.zkp.auth.utils;

import com.zkp.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuHelper {
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        List<SysMenu> list = new ArrayList<>();
        for (SysMenu sm : sysMenuList) {
            if (sm.getParentId() == 0) {
                list.add(findChildren(sm, sysMenuList));
            }
        }
        return list;
    }

    private static SysMenu findChildren(SysMenu root, List<SysMenu> sysMenuList) {
        root.setChildren(new ArrayList<>());
        for (SysMenu ch : sysMenuList) {
            if (ch.getParentId() == root.getId()) {
                root.getChildren().add(findChildren(ch, sysMenuList));
            }
        }
        return root;
    }
}
