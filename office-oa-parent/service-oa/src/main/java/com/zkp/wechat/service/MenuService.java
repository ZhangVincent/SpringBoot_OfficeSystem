package com.zkp.wechat.service;

import com.zkp.model.wechat.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zkp.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author zkp
 * @since 2023-04-28
 */
public interface MenuService extends IService<Menu> {

    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();
}
