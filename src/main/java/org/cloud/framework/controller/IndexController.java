package org.cloud.framework.controller;

import com.alibaba.fastjson.JSON;
import org.cloud.framework.service.IDlpCheckRecordService;
import org.cloud.framework.service.IDlpCheckTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 首页控制器
 * @Author wgsh
 * @Date wgshb on 2019/7/9 17:59
 */
@Controller
@RequestMapping("/index")
public class IndexController extends BaseContoroller {

	@Autowired
	private IDlpCheckTaskService dlpCheckTaskService;
	@Autowired
	private IDlpCheckRecordService dlpCheckRecordService;

	/**
	 * 查询首页相关的数量
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/getCheckNums")
	@ResponseBody
	public String getCheckNums(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Integer> map = dlpCheckTaskService.getCheckNums();
		/*String result = formatToJson(map);
		super.pringWriterToPage(result, "application/json", response);*/
		return JSON.toJSONString(map);
	}

	/**
	 * 查询近 7 日的预警数量统计
	 */
	@RequestMapping("/getCheckTrend")
	@ResponseBody
	public String getCheckTrend() {
		Map<String, List<String>> map = dlpCheckTaskService.getCheckTrend(getLastWeekList());
		return JSON.toJSONString(map);
	}

	/**
	 * 查询近 7 日的预警数量+检查总数量统计
	 */
	@RequestMapping("/getWarnAndTotalTrend")
	@ResponseBody
	public String getWarnAndTotalTrend() {
		Map<String, List<String>> map = dlpCheckRecordService.getWarnAndTotalTrend(getLastWeekList());
		return JSON.toJSONString(map);
	}

	@RequestMapping("/getTodayWarnNum")
	@ResponseBody
	public String getTodayWarnNum() {
		Map<String, List<String>> map = dlpCheckRecordService.getTodayWarnNum(getTodayList());
		return JSON.toJSONString(map);
	}

	/**
	 * 部门统计查询
	 */
	@RequestMapping("/getDepartmentSummary")
	@ResponseBody
	public String getDepartmentSummary() {
		Map<String, List<String>> map = dlpCheckTaskService.getDepartmentSummary();
		return JSON.toJSONString(map);
	}

	/**
	 * 版本使用率查询
	 */
	@RequestMapping("/getVersionUsageRate")
	@ResponseBody
	public String getVersionUsageRate() {
		List<Map<String, Object>> map = dlpCheckTaskService.getVersionUsageRate();
		return JSON.toJSONString(map);
	}

	/**
	 * 近 7 日任务统计
	 */
	@RequestMapping("/getTaskTrend")
	@ResponseBody
	public String getTaskTrend() {
		Map<String, List<String>> map = dlpCheckTaskService.getTaskTrend(getLastWeekList());
		return JSON.toJSONString(map);
	}



}
