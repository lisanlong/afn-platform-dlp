package org.cloud.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.cloud.framework.model.DlpModel;
import org.cloud.framework.model.DlpSourceType;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LKS
 * @since 2019-07-08
 */
@Mapper
public interface DlpSourceTypeMapper extends BaseMapper<DlpSourceType> {
}
