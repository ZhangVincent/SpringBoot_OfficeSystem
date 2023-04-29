package com.zkp.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkp.model.process.ProcessTemplate;
import com.zkp.model.process.ProcessType;
import com.zkp.process.mapper.ProcessTypeMapper;
import com.zkp.process.service.ProcessTemplateService;
import com.zkp.process.service.ProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author zkp
 * @since 2023-04-24
 */
@Service
public class ProcessTypeServiceImpl extends ServiceImpl<ProcessTypeMapper, ProcessType> implements ProcessTypeService {

    @Autowired
    private ProcessTemplateService processTemplateService;

    @Override
    public List<ProcessType> findProcessType() {
        List<ProcessType> processTypes = baseMapper.selectList(null);
        for (ProcessType processType:processTypes){
            LambdaQueryWrapper<ProcessTemplate> queryWrapper = new LambdaQueryWrapper<ProcessTemplate>().eq(ProcessTemplate::getProcessTypeId, processType.getId());
            List<ProcessTemplate> processTemplates = processTemplateService.list(queryWrapper);
            processType.setProcessTemplateList(processTemplates);
        }
        return processTypes;
    }
}
