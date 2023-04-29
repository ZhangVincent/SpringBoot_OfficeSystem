package com.zkp.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zkp.model.process.ProcessType;

import java.util.List;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author zkp
 * @since 2023-04-24
 */
public interface ProcessTypeService extends IService<ProcessType> {

    List<ProcessType> findProcessType();
}
