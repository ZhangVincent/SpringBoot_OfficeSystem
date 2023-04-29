package com.zkp.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkp.model.process.ProcessTemplate;
import com.zkp.model.process.ProcessType;
import com.zkp.process.mapper.ProcessTemplateMapper;
import com.zkp.process.service.ProcessService;
import com.zkp.process.service.ProcessTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkp.process.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author zkp
 * @since 2023-04-24
 */
@Service
public class ProcessTemplateServiceImpl extends ServiceImpl<ProcessTemplateMapper, ProcessTemplate> implements ProcessTemplateService {

    @Autowired
    private ProcessTemplateMapper processTemplateMapper;

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private ProcessService processService;

    @Override
    public Page<ProcessTemplate> selectPage(Page<ProcessTemplate> pageParam) {
        Page<ProcessTemplate> processTemplatePage = baseMapper.selectPage(pageParam, null);
        List<ProcessTemplate> templateList = processTemplatePage.getRecords();
        for (ProcessTemplate processTemplate : templateList) {
            LambdaQueryWrapper<ProcessType> queryWrapper = new LambdaQueryWrapper<ProcessType>().eq(ProcessType::getId, processTemplate.getProcessTypeId());
            ProcessType processType = processTypeService.getOne(queryWrapper);
            processTemplate.setProcessTypeName(processType.getName());
        }
//        processTemplatePage.setRecords(templateList);
        return processTemplatePage;
    }

    @Transactional
    @Override
    public void publish(Long id) {
        ProcessTemplate processTemplate = this.getById(id);
        processTemplate.setStatus(1);
        processTemplateMapper.updateById(processTemplate);

        //优先发布在线流程设计
        if(!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())) {
            processService.deployByZip(processTemplate.getProcessDefinitionPath());
        }
    }
}
