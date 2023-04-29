package com.zkp.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkp.model.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author zkp
 * @since 2023-04-17
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findMenuListByUserId(Long userId);
}
