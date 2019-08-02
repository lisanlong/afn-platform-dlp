package org.cloud.framework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description：任务类
 * @Modified By
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobAndTrigger {

	/**
	 * 任务名称
	 */
	private String jobName;

	/**
	 * 任务分组
	 */
	private String jobGroup;

	/**
	 * 任务描述
	 */
	private String description;

	/**
	 * 执行类
	 */
	private String jobClassName;

	/**
	 * 执行时间
	 */
	private String cronExpression;

	/**
	 * 任务名称
	 */
	private String triggerName;

	/**
	 * 任务状态
	 */
	private String triggerState;

	/**
	 * 任务名称 用于修改
	 */
	private String oldJobName;

	/**
	 * 任务分组 用于修改
	 */
	private String oldJobGroup;
}
