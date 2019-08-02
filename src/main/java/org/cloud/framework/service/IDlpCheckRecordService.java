package org.cloud.framework.service;

import org.cloud.framework.model.DlpCheckRecord;
import com.baomidou.mybatisplus.extension.service.IService;

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
public interface IDlpCheckRecordService extends IService<DlpCheckRecord> {

	/**
	 * 根据任务 id 获取对应的检查记录的集合
	 * @param id
	 * @return
	 */
	List<DlpCheckRecord> getCheckRecordByTaskId(Integer id);

	/**
	 * 查询近 7 日的预警数量+检查总数量统计
	 * @param lastWeekList
	 * @return
	 */
    Map<String, List<String>> getWarnAndTotalTrend(List<String> lastWeekList);

	/**
	 * 获取今日预警数量
	 * @param todayList
	 * @return
	 */
	Map<String, List<String>> getTodayWarnNum(List<String> todayList);

}
