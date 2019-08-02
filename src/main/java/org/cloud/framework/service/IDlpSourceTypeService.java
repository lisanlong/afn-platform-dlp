package org.cloud.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.framework.model.DlpSourceType;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LKS
 * @since 2019-07-08
 */
public interface IDlpSourceTypeService extends IService<DlpSourceType> {

	/**
	 * 根据数据源 code 获取数据的 name
	 * @param id
	 * @return
	 */
	String getSourceTypeNameBySourceCode(Integer code);
}
