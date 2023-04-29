package com.zkp.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkp.model.process.ProcessRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 审批记录 Mapper 接口
 * </p>
 *
 * @author zkp
 * @since 2023-04-25
 */
@Mapper
public interface ProcessRecordMapper extends BaseMapper<ProcessRecord> {

}
