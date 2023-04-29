package com.zkp.process.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkp.common.result.Result;
import com.zkp.model.process.ProcessTemplate;
import com.zkp.process.service.ProcessTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p>
 * 审批模板 前端控制器
 * </p>
 *
 * @author zkp
 * @since 2023-04-24
 */
@Api(value = "审批模板", tags = "审批模板")
@RestController
@RequestMapping("/admin/process/processTemplate")
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessTemplateController {
    @Autowired
    private ProcessTemplateService processTemplateService;

    @PreAuthorize("hasAuthority('bnt.processTemplate.list')")
    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page, @PathVariable Long limit) {
        Page<ProcessTemplate> pageParam = new Page<>(page, limit);
        Page<ProcessTemplate> pageModel = processTemplateService.selectPage(pageParam);
        return Result.ok(pageModel);
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.list')")
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        ProcessTemplate processTemplate = processTemplateService.getById(id);
        return Result.ok(processTemplate);
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.add')")
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody ProcessTemplate processTemplate) {
        boolean b = processTemplateService.save(processTemplate);
        return b ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.update')")
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody ProcessTemplate processTemplate) {
        boolean b = processTemplateService.updateById(processTemplate);
        return b ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.remove')")
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean b = processTemplateService.removeById(id);
        return b ? Result.ok() : Result.fail();
    }

    // TODO 这里，跳转页面点击保存会报错，提示没有权限
    @PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation(value = "上传流程定义")
    @PostMapping("/uploadProcessDefinition")
    public Result uploadProcessDefinition(MultipartFile file) throws FileNotFoundException {
        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        System.out.println(path);
        String fileName = file.getOriginalFilename();
        File tempFile = new File(path + "/processes/");
        if (!tempFile.exists()){
            tempFile.mkdir();
        }
        File iFile = new File(path + "/processes/" + fileName);
        try {
            file.transferTo(iFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("上传失败");
        }

        HashMap<String, Object> map = new HashMap<>();
        //根据上传地址后续部署流程定义，文件名称为流程定义的默认key
        map.put("processDefinitionPath", "processes/" + fileName);
        map.put("processDefinitionKey", fileName.substring(0, fileName.lastIndexOf(".")));
        return Result.ok(map);
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.publish')")
    @ApiOperation(value = "发布")
    @GetMapping("/publish/{id}")
    public Result publish(@PathVariable Long id) {
        processTemplateService.publish(id);
        return Result.ok();
    }
}

