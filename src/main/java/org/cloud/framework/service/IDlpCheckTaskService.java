package org.cloud.framework.service;

import org.cloud.framework.model.DlpCheckTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
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
public interface IDlpCheckTaskService extends IService<DlpCheckTask> {

	/**
	 * 查询检查的总数：包含（总文件数、总语料数、总任务数、总预警文件数）
	 * @return
	 */
	Map<String, Integer> getCheckNums();

	/**
	 * 获取近一周的检查的正确率
	 * @param lastWeekList
	 * @return
	 */
	Map<String, List<String>> getCheckTrend(List<String> lastWeekList);

	/**
	 * 按照部门统计文件检查总数
	 * @return
	 */
	Map<String, List<String>> getDepartmentSummary();

	/**
	 * 按照预料版本统计文件检查总数
	 * @return
	 */
	List<Map<String, Object>> getVersionUsageRate();

	/**
	 * 近 7 日的任务数统计
	 * @return
	 * @param lastWeekList
	 */
	Map<String, List<String>> getTaskTrend(List<String> lastWeekList);

	/**
	 * 检查数据库的连通性
	 * @param usernmae
	 * @param password
	 * @param url
	 * @param sourceType
	 * @return
	 */
	HashMap<String, Object> checkSecDataSrc(String usernmae, String password, String url, String sourceType);
}
