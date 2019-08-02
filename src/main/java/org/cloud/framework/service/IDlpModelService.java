package org.cloud.framework.service;

import org.cloud.framework.model.DlpModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LKS
 * @since 2019-07-08
 */
public interface IDlpModelService extends IService<DlpModel> {

	/**
	 * 获取所有的测试模型
	 * @return
	 */
	List<LinkedHashMap<String, Object>> getAllModel();

	/**
	 * 根据模型获取对应的密级范围
	 */
	List<String> getSelectedLabels(String version);

	/**
	 * 文件预测
	 * @param fileUrl
	 * @param version
	 * @return
	 */
	Map<String, Object> filePredict(String fileUrl, String version);

	/**
	 * 文本预测
	 * @param textArea
	 * @param version
	 * @return
	 */
	Map<String, Object> textPredict(String textArea, String version);
}
