package org.cloud.framework.protocol;

import org.cloud.framework.model.JobAndTrigger;
import org.cloud.framework.model.Result;
import org.quartz.JobDataMap;

/**
 * @Description：任务调度服务
 * @Modified By
 **/
public interface IJobService{


	/**
	 * 新建任务
	 * 
	 * @param quartz
	 * @return
	 */
	Result save(JobAndTrigger quartz, JobDataMap jobDataMap);

	/**
	 * 获取任务列表
	 * 
	 * @param jobName
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Result list( String jobName,
			Integer pageNo, Integer pageSize);

	/**
	 * 触发任务
	 * 
	 * @param quartz
	 * @param response
	 * @return
	 */
	Result trigger(JobAndTrigger quartz);

	/**
	 * 停止任务
	 * 
	 * @param quartz
	 * @param response
	 * @return
	 */
	Result pause(JobAndTrigger quartz);

	/**
	 * 恢复任务
	 * 
	 * @param quartz
	 * @param response
	 * @return
	 */
	Result resume(JobAndTrigger quartz);

	/**
	 * 移除任务
	 * 
	 * @param quartz
	 * @param response
	 * @return
	 */
	Result remove(JobAndTrigger quartz);

}
