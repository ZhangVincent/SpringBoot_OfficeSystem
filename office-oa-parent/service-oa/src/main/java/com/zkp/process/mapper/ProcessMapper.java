package com.zkp.process.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkp.model.process.Process;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkp.vo.process.ProcessQueryVo;
import com.zkp.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 审批类型 Mapper 接口
 * </p>
 *
 * @author zkp
 * @since 2023-04-25
 */
@Mapper
public interface ProcessMapper extends BaseMapper<Process> {
    Page<ProcessVo> selectPage(Page<ProcessVo> page, @Param("vo") ProcessQueryVo processQueryVo);
}
