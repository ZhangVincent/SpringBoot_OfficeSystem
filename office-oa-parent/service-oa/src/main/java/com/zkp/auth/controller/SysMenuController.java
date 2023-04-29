package com.zkp.auth.controller;


import com.zkp.auth.service.SysMenuService;
import com.zkp.common.result.Result;
import com.zkp.model.system.SysMenu;
import com.zkp.vo.system.AssginMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author zkp
 * @since 2023-04-17
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @ApiOperation(value = "获取菜单")
    @GetMapping("findNodes")
    public Result findNodes(){
        List<SysMenu> sysMenu = sysMenuService.findNodes();
        return Result.ok(sysMenu);
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.add')")
    @ApiOperation(value = "新增菜单")
    @PostMapping("save")
    public Result save(@RequestBody SysMenu sysMenu){
        boolean b = sysMenuService.save(sysMenu);
        return b?Result.ok(): Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.update')")
    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public Result update(@RequestBody SysMenu sysMenu){
        boolean b = sysMenuService.updateById(sysMenu);
        return b?Result.ok(): Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.remove')")
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean b = sysMenuService.removeMenuById(id);
        return b?Result.ok(): Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.assignRole')")
    @ApiOperation(value = "根据角色id获取菜单")
    @GetMapping("toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId){
        List<SysMenu> list= sysMenuService.findSysMenuById(roleId);
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.assignRole')")
    @ApiOperation(value = "给角色分配菜单")
    @PostMapping("doAssign")
    public Result doAssign(@RequestBody AssginMenuVo assginMenuVo){
        boolean b = sysMenuService.doAssign(assginMenuVo);
        return b?Result.ok(): Result.fail();
    }
}

