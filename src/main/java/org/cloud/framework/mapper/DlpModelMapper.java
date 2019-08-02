package org.cloud.framework.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.cloud.framework.model.DlpModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
public interface DlpModelMapper extends BaseMapper<DlpModel> {
	/**
	 * 查询当前的所有的任务中包含的所有的DlpModel对象
	 * @return
	 */
	@Select("SELECT * FROM dlp_model WHERE id in (SELECT DISTINCT(model_id) FROM dlp_check_task);")
	List<DlpModel> selectTaskModelList();
}
