package org.cloud.framework.mapper;

import org.apache.ibatis.annotations.Param;
import org.cloud.framework.model.DlpCheckRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LKS
 * @since 2019-07-17
 */
public interface DlpCheckRecordMapper extends BaseMapper<DlpCheckRecord> {

    void saveDlpCheckRecordBatch(@Param("list") List<DlpCheckRecord> subList);
}
