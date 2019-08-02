package org.cloud.framework.protocol.impl;

import java.util.List;

import org.cloud.framework.model.JobAndTrigger;
import org.cloud.framework.model.Result;
import org.cloud.framework.protocol.IJobService;
import org.cloud.framework.service.IJobAndTriggerService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JobService implements IJobService {

	private static Logger log = LoggerFactory.getLogger(JobService.class);

	/**
	 * 加入Qulifier注解，通过名称注入bean
	 * 
	 */
	@Autowired
	@Qualifier("Scheduler")
	private Scheduler scheduler;

	@Autowired
	private IJobAndTriggerService iJobAndTriggerService;

	/**
	 *	 新建任务
	 * 
	 * @param quartz
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result save(JobAndTrigger quartz, JobDataMap jobDataMap) {
		log.info("新增任务");
		try {
			/**
			 * 获取Scheduler实例、废弃、使用自动注入的scheduler、否则spring的service将无法注入 Scheduler scheduler =
			 * StdSchedulerFactory.getDefaultScheduler(); 如果是修改 ,展示旧的任务
			 */
			if (quartz.getOldJobGroup() != null) {
				JobKey key = new JobKey(quartz.getOldJobName(), quartz.getOldJobGroup());
				scheduler.deleteJob(key);
			}

			Class cls = Class.forName(quartz.getJobClassName());
			cls.newInstance();

			/*** 构建job信息 */
			JobBuilder jobBuilder = JobBuilder.newJob(cls);
			if(null!=jobDataMap) {
				jobBuilder.setJobData(jobDataMap);
			}
			
			JobDetail job  = jobBuilder.withIdentity(quartz.getJobName(), quartz.getJobGroup())
					.withDescription(quartz.getDescription()).build();
			/*** 触发时间点 */
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity("trigger" + quartz.getJobName(), quartz.getJobGroup()).startNow()
					.withSchedule(cronScheduleBuilder).build();

			/**
			 * 	交由Scheduler安排触发
			 **/
			scheduler.scheduleJob(job, trigger);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
		return Result.ok();
	}

	/**
	 * 获取任务列表
	 * 
	 * @param jobName
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	public Result list(String jobName, Integer pageNo, Integer pageSize) {
		log.info("任务列表");
		List<JobAndTrigger> list = iJobAndTriggerService.listQuartzEntity(jobName, pageNo, pageSize);
		return Result.ok(list);
	}

	/**
	 * 触发任务
	 * 
	 * @param quartz
	 * @param response
	 * @return
	 */
	@Override
	public Result trigger(JobAndTrigger quartz) {
		log.info("触发任务");
		try {
			JobKey key = new JobKey(quartz.getJobName(), quartz.getJobGroup());
			scheduler.triggerJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return Result.error();
		}
		return Result.ok();
	}

	/**
	 * 停止任务
	 * 
	 * @param quartz
	 * @param response
	 * @return
	 */
	@Override
	public Result pause(JobAndTrigger quartz) {
		log.info("停止任务");
		try {
			JobKey key = new JobKey(quartz.getJobName(), quartz.getJobGroup());
			scheduler.pauseJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return Result.error();
		}
		return Result.ok();
	}

	/**
	 * 恢复任务
	 * 
	 * @param quartz
	 * @param response
	 * @return
	 */
	@Override
	public Result resume(JobAndTrigger quartz) {
		log.info("恢复任务");
		try {
			JobKey key = new JobKey(quartz.getJobName(), quartz.getJobGroup());
			scheduler.resumeJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return Result.error();
		}
		return Result.ok();
	}

	/**
	 * 移除任务
	 * 
	 * @param quartz
	 * @param response
	 * @return
	 */
	@Override
	public Result remove(JobAndTrigger quartz) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(quartz.getJobName(), quartz.getJobGroup());

			/*** 停止触发器 */
			scheduler.pauseTrigger(triggerKey);

			/** 移除触发器 */
			scheduler.unscheduleJob(triggerKey);

			/*** 删除任务 */
			scheduler.deleteJob(JobKey.jobKey(quartz.getJobName(), quartz.getJobGroup()));
			System.out.println("removeJob:" + JobKey.jobKey(quartz.getJobName()));
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
		return Result.ok();
	}

}
