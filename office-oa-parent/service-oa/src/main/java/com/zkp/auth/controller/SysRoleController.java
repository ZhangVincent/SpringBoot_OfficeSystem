package com.zkp.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkp.auth.service.SysRoleService;
import com.zkp.common.result.Result;
import com.zkp.model.system.SysRole;
import com.zkp.vo.system.AssginRoleVo;
import com.zkp.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation(value = "获取全部角色列表")
    @GetMapping("getAll")
    public Result findAll() {
        List<SysRole> sysRoleList = sysRoleService.list();
        return Result.ok(sysRoleList);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation(value = "根据id查询")
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    /**
     * @param page           当前页
     * @param limit          每页多少条数据
     * @param sysRoleQueryVo 传送到后端的条件对象
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation(value = "条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page, @PathVariable Long limit, SysRoleQueryVo sysRoleQueryVo) {
        Page<SysRole> sysRolePage = new Page<>(page, limit);
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        // springframework的StringUtils.isEmpty用来判断是否不为空且不为''
        if (!StringUtils.isEmpty(roleName)) {
            queryWrapper.like(SysRole::getRoleName, roleName);
        }
        Page<SysRole> pageModel = sysRoleService.page(sysRolePage, queryWrapper);
        return Result.ok(pageModel);
    }

    /**
     * @param sysRole
     * @return 添加操作
     * @Validated 数据格式校验，如有异常会抛出统一处理
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation(value = "添加角色")
    @PostMapping("save")
    public Result save(@RequestBody @Validated SysRole sysRole) {
//    public Result save(@RequestBody SysRole sysRole) {
        boolean b = sysRoleService.save(sysRole);
        return b ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation(value = "修改角色")
    @PutMapping("update")
    public Result update(@RequestBody SysRole sysRole) {
        boolean b = sysRoleService.updateById(sysRole);
        return b ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation(value = "根据id删除角色")
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable Long id) {
        boolean b = sysRoleService.removeById(id);
        return b ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation(value = "批量删除角色")
    @DeleteMapping("batchDelete")
    public Result batchDelete(@RequestBody List<Long> ids) {
        boolean b = sysRoleService.removeByIds(ids);
        return b ? Result.ok() : Result.fail();
    }

    // TODO 这里，点击跳转页面会直接报错，提示没有权限
    @ApiOperation(value = "根据用户ID获取角色数据")
    @GetMapping("toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId) {
        Map<String, Object> map = sysRoleService.findRoleByUserId(userId);
        return Result.ok(map);
    }

    @ApiOperation(value = "为用户分配角色数据")
    @PostMapping("doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo) {
        boolean b = sysRoleService.doAssign(assginRoleVo);
        return b ? Result.ok() : Result.fail();
    }

}
