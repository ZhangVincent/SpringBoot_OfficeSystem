package com.zkp.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkp.auth.service.SysMenuService;
import com.zkp.auth.service.SysRoleService;
import com.zkp.auth.service.SysUserService;
import com.zkp.common.exception.MyException;
import com.zkp.common.jwt.JwtHelper;
import com.zkp.common.result.Result;
import com.zkp.common.result.ResultCodeEnum;
import com.zkp.common.utils.MD5;
import com.zkp.model.system.SysRole;
import com.zkp.model.system.SysUser;
import com.zkp.vo.system.LoginVo;
import com.zkp.vo.system.RouterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 登录
     * 注意：这里正确运行的代码是200，需要在前端对应位置修改正确
     * <p>
     * 重写登录方法，获取登录信息，判断用户是否存在，密码正确，状态是否被禁用，封装成token返回
     */
    @ApiOperation(value = "登录验证")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("token", "admin");
//        return Result.ok(map);

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, loginVo.getUsername());
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        if (sysUser == null) {
            throw new MyException(ResultCodeEnum.FAIL.getCode(), "用户不存在！");
        }
        if (!sysUser.getPassword().equals(MD5.encrypt(loginVo.getPassword()))) {
            throw new MyException(ResultCodeEnum.FAIL.getCode(), "密码不正确！");
        }
        if (sysUser.getStatus() == 0) {
            throw new MyException(ResultCodeEnum.FAIL.getCode(), "用户被禁用");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", JwtHelper.createToken(sysUser.getId(), sysUser.getUsername()));
        return Result.ok(map);
    }

    /**
     * 获取用户信息
     * 按照对应格式配置
     * <p>
     * 完善用户信息获取方法
     */
    @ApiOperation(value = "用户信息查询")
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
//        Long userId = 2L;
        SysUser sysUser = sysUserService.getById(userId);

        Map<String, Object> roleMap = sysRoleService.findRoleByUserId(userId);
        List<SysRole> assignRoleList = (List<SysRole>) roleMap.get("assignRoleList");
        String roles = assignRoleList.stream().filter(c -> !StringUtils.isEmpty(c.getRoleName())).map(c -> c.getRoleName()).collect(Collectors.joining("','"));
        System.out.println(roles);
        System.out.println("['" + roles + "']");

        // 查找可操作的菜单
        List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);
        // 查找可操作的按钮
        List<String> permsList = sysMenuService.findUserPermsListByUserId(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("name", sysUser.getUsername());
//        map.put("roles","[admin]");
        map.put("roles", "['" + roles + "']");
        map.put("introduction", "I am a administrator");
        // 用户头像
        if (!StringUtils.isEmpty(sysUser.getHeadUrl())) {
            map.put("avatar", sysUser.getHeadUrl());
        } else {
            map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        }
//        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        // 用户可以操作的菜单
        map.put("routers", routerList);
// 用户可以操作的按钮
        map.put("buttons", permsList);
        return Result.ok(map);
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }
}
