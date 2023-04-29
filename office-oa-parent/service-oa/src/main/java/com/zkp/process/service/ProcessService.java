package com.zkp.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zkp.model.process.Process;
import com.zkp.vo.process.ApprovalVo;
import com.zkp.vo.process.ProcessFormVo;
import com.zkp.vo.process.ProcessQueryVo;
import com.zkp.vo.process.ProcessVo;

import java.util.Map;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author zkp
 * @since 2023-04-25
 */
public interface ProcessService extends IService<Process> {

    Page<ProcessVo> selectPage(Page<ProcessVo> page, ProcessQueryVo processQueryVo);

    void deployByZip(String deployPath);

    void startUp(ProcessFormVo processFormVo);

    Page<ProcessVo> findPending(Page<Process> pageParam);

    Map<String,Object> show(Long id);

    void approve(ApprovalVo approvalVo);

    IPage<ProcessVo> findProcessed(Page<Process> pageParam);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);
}
