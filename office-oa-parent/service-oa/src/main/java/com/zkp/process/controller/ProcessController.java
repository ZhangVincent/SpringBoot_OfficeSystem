package com.zkp.process.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkp.common.result.Result;
import com.zkp.process.service.ProcessService;
import com.zkp.vo.process.ProcessQueryVo;
import com.zkp.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 审批类型 前端控制器
 * </p>
 *
 * @author zkp
 * @since 2023-04-25
 */
@Api(tags = "审批流管理")
@SuppressWarnings({"unchecked", "rawtypes"})
@RestController
@RequestMapping("/admin/process")
public class ProcessController {
    @Autowired
    private ProcessService processService;

    @PreAuthorize("hasAuthority('bnt.process.list')")
    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page, @PathVariable Long limit, ProcessQueryVo processQueryVo) {
        Page<ProcessVo> processPage = new Page<>(page, limit);
        Page<ProcessVo> pageModel = processService.selectPage(processPage, processQueryVo);
        return Result.ok(pageModel);
    }
}

