package org.cloud.framework.service;

import java.util.List;

import org.cloud.framework.model.JobAndTrigger;

/**
 * @Description
 * @Modified By
 **/
public interface IJobAndTriggerService {

    //public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize);

    /**
     * 	查询定时任务实体类
     * @param jobName
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<JobAndTrigger> listQuartzEntity(String jobName,Integer pageNo,Integer pageSize);

    /**
     *	 查总的任务数量
     * @param quartz
     * @return
     */
    Long listQuartzEntityNum(JobAndTrigger quartz);

}
