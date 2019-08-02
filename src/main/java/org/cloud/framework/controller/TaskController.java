package org.cloud.framework.controller;

import com.alibaba.fastjson.JSON;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.cloud.framework.model.*;
import org.cloud.framework.protocol.impl.SearchDataReadService;
import org.cloud.framework.protocol.impl.SearchDataWriteService;
import org.cloud.framework.service.*;
import org.cloud.framework.utils.*;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author wgsh
 * @Date wgshb on 2019/7/12 15:32
 */
@RequestMapping("/task")
@Controller
public class TaskController extends BaseContoroller {

	@Autowired
	private SearchDataReadService searchDataReadService;

	@Autowired
	private IDlpCheckTaskService dlpCheckTaskService;

	@Autowired
	private SearchDataWriteService searchDataWriteService;

	@Autowired
	private ISystemUserService systemUserService;

	@Autowired
	private IDlpDepartmentService dlpDepartmentService;

	@Autowired
	private IDlpCheckRecordService dlpCheckRecordService;

	@Autowired
	private IDlpModelService dlpModelService;

	@Autowired
	@Qualifier("Scheduler")
	private Scheduler scheduler;

	@Value("${data.upload.file}")
	private String dataUploadFile;

	public final static Map<String, String> taskStatusMap = new HashMap<String, String>();
	static {
		taskStatusMap.put("0", "未开始");
		taskStatusMap.put("1", "检查中");
		taskStatusMap.put("2", "检查完成");
	}

	private Map<String, String> dataSourceTypeMap;
	private Map<String, String> dataSourceTypeReverseMap;

	public Map<String, String> getStaticDataSourceTypeMap() {
		List<DlpSourceType> list = searchDataReadService.getDataSourceTypeList();
		Map<String, String> map = new HashMap<>();
		for(DlpSourceType sourceType : list) {
			map.put(sourceType.getCode() + "", sourceType.getName());
		}
		return map;
	}
	public Map<String, String> getStaticDataSourceTypeReverseMap() {
		List<DlpSourceType> list = searchDataReadService.getDataSourceTypeList();
		Map<String, String> map = new HashMap<>();
		for(DlpSourceType sourceType : list) {
			map.put(sourceType.getName(), sourceType.getCode() + "");
		}
		return map;
	}

	/**
	 * 查询所有的数据类型
	 * @return
	 */
	@RequestMapping("/getDataSourceTypeMap")
	@ResponseBody
	public String getDataSourceTypeMap(HttpServletResponse response) throws Exception {
		List<DlpSourceType> list = searchDataReadService.getDataSourceTypeList();
		List<LinkedHashMap<String, Object>> result = new ArrayList<>();
		for(DlpSourceType sourceType : list) {
			LinkedHashMap<String, Object> temp = new LinkedHashMap<String, Object>();
			temp.put("key", sourceType.getCode());
			temp.put("value", sourceType.getName());
			result.add(temp);
		}

		return JSON.toJSONString(result);
	}

	/**
	 * 获取预警任务列表
	 */
	@RequestMapping("/getCheckTaskName")
	@ResponseBody
	public void getCheckTaskName(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Integer id = Integer.parseInt(request.getParameter("id"));
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("name", dlpCheckTaskService.getById(id).getName());
		String resultString = formatToJson(data);
		super.pringWriterToPage(resultString, "application/json", response);
	}

	/**
	 * 获取预警任务列表
	 */
	@RequestMapping("/getWarnList")
	@ResponseBody
	public void getWarnList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Integer pageNum = Integer.parseInt(request.getParameter("pageNum"));
		Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		// 创建时间降序
		queryParams.put("^,create_time","desc");
		queryParams.put("=,task_status","2");
		queryParams.put("!=,warn_file_total","0");
		List<DlpCheckTask> dlpCheckTaskList = searchDataReadService.getDlpCheckTaskList(queryParams,pageNum, pageSize);
		Integer count = searchDataReadService.getDlpCheckTaskCount(queryParams);
		List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> dataMap = null;
		for(DlpCheckTask ds:dlpCheckTaskList){
			dataMap = new HashMap<String, Object>();
			dataMap.put("id", ds.getId());
			dataMap.put("name", ds.getName());
			SystemUser user = systemUserService.getById(ds.getCreateUid());
			dataMap.put("createUser", user.getUsername());
			dataMap.put("createTime", ds.getCreateTime());
			dataMap.put("warnFileTotal", ds.getWarnFileTotal());
			dataMap.put("checkProcess", ds.getCheckTotal() + "/" + ds.getFileTotal());

			dataList.add(dataMap);
		}
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("data",dataList);
		data.put("count",(count + pageSize - 1) / pageSize);
		String resultString = formatToJson(data);
		super.pringWriterToPage(resultString, "application/json", response);
	}

	/**
	 * 下载记录表
	 */
	@RequestMapping("/downloadResult")
	@ResponseBody
	public void downloadResult(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// 拼装查询参数, 类似分页的结果进行返回
		HashMap<String, Object> queryParams = new HashMap<>();
		Integer taskId = Integer.parseInt(request.getParameter("id"));
//		List<DlpCheckRecord> records = dlpCheckRecordService.getCheckRecordByTaskId(id);
		// 查询当前的用户对应的记录表
		queryParams.put("=,create_uid", request.getSession().getAttribute("userid"));
		queryParams.put("=,task_id",taskId);
		queryParams.put("^,origin_label","asc");
		queryParams.put("^,filename","asc");

		Integer count = searchDataReadService.getDlpCheckRecordCount(queryParams);
		List<DlpCheckRecord> records = searchDataReadService.getDlpCheckRecordList(queryParams, 1, count);

		List<DlpClassify> classifies = searchDataReadService.getDlpClassify();
		Properties labels = new Properties();
		if(null != classifies && classifies.size() > 0) {
			for(DlpClassify classify :classifies) {
				labels.put(classify.getCnum(), classify.getName());
			}

			if(records.size() > 0) {
				for(DlpCheckRecord record : records) {
					if(!StringUtils.isEmpty(record.getOriginLabel())) {
						record.setOriginLabel((String) labels.get(record.getOriginLabel()));
					}
					if(!StringUtils.isEmpty(record.getCheckLabel())) {
						record.setCheckLabel((String) labels.get(record.getCheckLabel()));
					}
					if(!StringUtils.isEmpty(record.getReviewLabel())) {
						record.setReviewLabel((String) labels.get(record.getReviewLabel()));
					}
				}
			}
		}

		DlpCheckTask task = dlpCheckTaskService.getById(taskId);
		// 创建人
		Integer createUid = task.getCreateUid();
		SystemUser user = systemUserService.getById(createUid);

		String fname = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());// Excel文件名
		OutputStream os = response.getOutputStream();// 取得输出流
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename="
				+ fname + ".xls"); // 设定输出文件头,该方法有两个参数，分别表示应答头的名字和值。
		response.setContentType("application/vnd.ms-excel");
		try {
			// 导出到 excel
			POIUtils.createFixationSheet(os, records, task, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载任务报表
	 */
	@RequestMapping("/downloadReport")
	@ResponseBody
	public void downloadReport(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// 拼装查询参数, 类似分页的结果进行返回
		HashMap<String, Object> queryParams = new HashMap<>();
		Integer taskId = Integer.parseInt(request.getParameter("id"));
//		List<DlpCheckRecord> records = dlpCheckRecordService.getCheckRecordByTaskId(id);
		// 查询当前的用户对应的记录表
		queryParams.put("=,create_uid", request.getSession().getAttribute("userid"));
		queryParams.put("=,task_id",taskId);
		queryParams.put("^,origin_label","asc");
		queryParams.put("^,filename","asc");

		int totalWarnNum = 0;
		int realWarnNum = 0;
		int realQualifiedNum = 0;
		int qualifiedNum = 0;


		Integer count = searchDataReadService.getDlpCheckRecordCount(queryParams);
		List<DlpCheckRecord> records = searchDataReadService.getDlpCheckRecordList(queryParams, 1, count);
		List<DlpCheckRecord> warnList = new ArrayList<>();
		List<DlpClassify> classifies = searchDataReadService.getDlpClassify();
		// 存放所有的密级的集合...
		Properties labels = new Properties();
		if(null != classifies && classifies.size() > 0) {
			for(DlpClassify classify :classifies) {
				labels.put(classify.getCnum(), classify.getName());
			}

			if(records.size() > 0) {
				for(DlpCheckRecord record : records) {
					if(!StringUtils.isEmpty(record.getOriginLabel())) {
						record.setOriginLabel((String) labels.get(record.getOriginLabel()));
					}
					if(!StringUtils.isEmpty(record.getCheckLabel())) {
						record.setCheckLabel((String) labels.get(record.getCheckLabel()));
					}
					if(!StringUtils.isEmpty(record.getReviewLabel())) {
						record.setReviewLabel((String) labels.get(record.getReviewLabel()));
					}
					if(record.getCheckStatus() == 2) {
					    totalWarnNum ++;
//					    warnList.add(record);
                    }
					if(record.getCheckStatus() == 1) {
						qualifiedNum ++;
                    }
					if(record.getReviewStatus() == 2) {
					    realWarnNum ++;
                    }
					if(record.getReviewStatus() == 1) {
						realQualifiedNum ++;
                    }
				}
			}
		}

		DlpCheckTask task = dlpCheckTaskService.getById(taskId);
		// 创建人
		Integer createUid = task.getCreateUid();
		SystemUser user = systemUserService.getById(createUid);

		String reportTime = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒").format(new Date());
		Map<String,Object> dataMap = new HashMap<>();
		dataMap.put("taskName", task.getName());
//		dataMap.put("reportTime", reportTime);
//		dataMap.put("deptName", dlpDepartmentService.getById(user.getDptId()).getName());
		dataMap.put("username", user.getUsername());
		dataMap.put("checkTime", task.getCreateTime());
		dataMap.put("totalCheckNum", records.size());
		dataMap.put("totalWarnNum", totalWarnNum);
		dataMap.put("realWarnNum", realWarnNum);
		dataMap.put("qualifiedNum", realQualifiedNum + qualifiedNum);
		dataMap.put("rangeCheck", formatCheckRange(searchDataReadService.getChinLabelsByModelId(task.getModelId())));

		List<HashMap<String, Object>> recordList = new ArrayList<>();
		if(records.size() == 0) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("index", "无");
			map.put("filename", "无");
			map.put("orginLabel", "无");
			map.put("checkLabel", "无");
			map.put("reviewLabel", "无");
			map.put("fileOwner", "无");
			map.put("fileDept", "无");
			recordList.add(map);
		} else {
			for(DlpCheckRecord record : records) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("index", records.indexOf(record) + 1);
				map.put("filename", record.getFileName());
				map.put("orginLabel", record.getOriginLabel());

				map.put("checkLabel", record.getCheckLabel() == null?"":record.getCheckLabel());
//				map.put("checkLabel", record.getCheckLabel());

				map.put("reviewLabel", ""==record.getReviewLabel()?"待鉴定":record.getReviewLabel());
				map.put("fileOwner", "");
				map.put("fileDept", "");

				recordList.add(map);
			}
		}

		dataMap.put("recordList", recordList);

		String fname = task.getName() + "_保密检查报告(" + reportTime +")";// 报告名称
		OutputStream os = response.getOutputStream();// 取得输出流
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String((fname + ".doc").getBytes("gb2312"), "ISO8859-1")); // 设定输出文件头,该方法有两个参数，分别表示应答头的名字和值。
		response.setContentType("application/msword");
		try {
			DocUtils docUtils = new DocUtils();
			// 导出到 word
			docUtils.createDocByFos(dataMap, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String formatCheckRange(List<String> chinLabelsByModelId) {
		if(null == chinLabelsByModelId || chinLabelsByModelId.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(String label : chinLabelsByModelId) {
			if(chinLabelsByModelId.indexOf(label) == (chinLabelsByModelId.size() - 1)) {
				sb.append(label);
			} else {
				sb.append(label + " ");
			}
		}
		return sb.toString();
	}

	/**
	 * 删除任务
	 */
	@RequestMapping("/delCheckTask")
	@ResponseBody
	public void delCheckTask(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String ids = request.getParameter("id");
		JobAndTrigger trigger = new JobAndTrigger();
		Map<String, String> result = new HashMap<>();
		if(!ids.contains(",")) {
			try {
				DlpCheckTask dlpCheckTask = searchDataReadService.findDlpCheckTask(Integer.parseInt(ids));
				trigger.setJobName("job"+dlpCheckTask.getId());
				trigger.setJobGroup("jobGroup"+dlpCheckTask.getName());
				remove(trigger, response);
				searchDataWriteService.deleteDlpCheckTaskById(Integer.parseInt(ids));
				result.put("code", "1");
			} catch (NumberFormatException e) {
				result.put("code", "0");
				e.printStackTrace();
			}
		} else {
			try {
				for(String idStr : Arrays.asList(ids.split(","))) {
					DlpCheckTask dlpCheckTask = searchDataReadService.findDlpCheckTask(Integer.parseInt(idStr));
					trigger.setJobName("job"+dlpCheckTask.getId());
					trigger.setJobGroup("jobGroup"+dlpCheckTask.getName());
					remove(trigger, response);
					searchDataWriteService.deleteDlpCheckTaskById(Integer.parseInt(idStr));
				}
				result.put("code", "1");
			} catch (NumberFormatException e) {
				result.put("code", "0");
				e.printStackTrace();
			}
		}

		String resultString = formatToJson(result);
		super.pringWriterToPage(resultString, "application/json", response);
	}

	/**
	 * 获取所有的任务
	 */
	@RequestMapping("/getAllTaskList")
	@ResponseBody
	private String getAllTaskList() {
		List<LinkedHashMap<String, Object>> result = searchDataReadService.getAllTaskList();
		return JSON.toJSONString(result);
	}

	/**
	 * 获取任务列表
	 */
	@RequestMapping("/getTaskList")
	@ResponseBody
	public void getTaskList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		dataSourceTypeMap = getStaticDataSourceTypeMap();
		Integer pageNum = Integer.parseInt(request.getParameter("pageNum"));
		Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		// 创建时间降序
		queryParams.put("^,create_time","desc");
		queryParams.put("=,create_uid", request.getSession().getAttribute("userid"));
		List<DlpCheckTask> dlpCheckTaskList = searchDataReadService.getDlpCheckTaskList(queryParams,pageNum, pageSize);
		Integer count = searchDataReadService.getDlpCheckTaskCount(queryParams);
		List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> dataMap = null;
		for(DlpCheckTask ds:dlpCheckTaskList){
			dataMap = new HashMap<String, Object>();
			dataMap.put("id", ds.getId());
			dataMap.put("name", ds.getName());

			Integer code = searchDataReadService.getDataSourceById(ds.getDatasourceId()).getSourceType();
			String sourceType = searchDataReadService.getSourceTypeNameBySourceCode(code);
			dataMap.put("sourceType", sourceType);
			dataMap.put("taskStatus", taskStatusMap.get(ds.getTaskStatus() + ""));
			dataMap.put("createTime", ds.getCreateTime());
			dataMap.put("checkProcess", ds.getCheckTotal() + "/" + ds.getFileTotal());
			dataList.add(dataMap);
		}
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("data",dataList);
		data.put("count",(count + pageSize - 1) / pageSize);
		String resultString = formatToJson(data);
		super.pringWriterToPage(resultString, "application/json", response);
	}
	/**
	 * 获取预警记录列表
	 */
	@RequestMapping("/getWarnRecordList")
	@ResponseBody
	public void getWarnRecordList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		dataSourceTypeMap = getStaticDataSourceTypeMap();
		Integer pageNum = Integer.parseInt(request.getParameter("pageNum"));
		Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));

//		Integer taskId = Integer.parseInt(request.getParameter("taskId"));
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		// 创建时间降序
		queryParams.put("^,task_id","desc");
		queryParams.put("^,origin_label","asc");
		queryParams.put("^,filename","asc");
		queryParams.put("=,create_uid", request.getSession().getAttribute("userid"));
        if(!StringUtils.isEmpty(request.getParameter("taskId"))) {
            queryParams.put("=,task_id",Integer.parseInt(request.getParameter("taskId")));
        }
		if(!StringUtils.isEmpty(request.getParameter("reviewStatus"))) {
            queryParams.put("=,review_status",Integer.parseInt(request.getParameter("reviewStatus")));
            queryParams.put("=,check_status",2);
        } else if(!StringUtils.isEmpty(request.getParameter("checkStatus"))){
            queryParams.put("=,check_status",Integer.parseInt(request.getParameter("checkStatus")));
        } else {
			queryParams.put("!,check_status",0);
		}
		if(!StringUtils.isEmpty(request.getParameter("taskId"))) {
			queryParams.put("=,task_id",Integer.parseInt(request.getParameter("taskId")));
		}
		List<DlpCheckRecord> dlpCheckTaskList = searchDataReadService.getDlpCheckRecordList(queryParams,pageNum, pageSize);
		Integer count = searchDataReadService.getDlpCheckRecordCount(queryParams);
		List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> dataMap = null;
		// 获取所有的密级的集合
		Map<String, Integer> classifyList = getAllClassifyUserLevelList();
		for(DlpCheckRecord ds:dlpCheckTaskList){
			dataMap = new HashMap<String, Object>();
			dataMap.put("id", ds.getId());
			dataMap.put("fileName", ds.getFileName());
			dataMap.put("taskName", dlpCheckTaskService.getById(ds.getTaskId()).getName());
			if(null!=ds.getOriginLabel()){
				dataMap.put("originLabel", searchDataReadService.getDlpClassifyByCnum(ds.getOriginLabel()).getName());
			}
			if(null!=ds.getCheckLabel() && null != searchDataReadService.getDlpClassifyByCnum(ds.getCheckLabel())) {
				dataMap.put("checkLabel", searchDataReadService.getDlpClassifyByCnum(ds.getCheckLabel()).getName());
			}
			dataMap.put("predictType", ds.getPredictType());
			dataMap.put("createTime", ds.getCreateTime());
			if(null != searchDataReadService.getDlpClassifyByCnum(ds.getReviewLabel())) {
				dataMap.put("reviewLabel", searchDataReadService.getDlpClassifyByCnum(ds.getReviewLabel()).getName());
			} else {
				dataMap.put("reviewLabel", "");
			}
			dataMap.put("createUser", systemUserService.getById(ds.getCreateUid()).getUsername());
			if((ds.getCheckStatus() == 2 && ds.getReviewStatus() != 1) || ds.getReviewStatus() == 2) {
				if(classifyList.get(ds.getOriginLabel()) > classifyList.get(ds.getCheckLabel())) {
					dataMap.put("status" , "<b style=\"color:red\">偏高</b>");
				} else if(classifyList.get(ds.getOriginLabel()) < classifyList.get(ds.getCheckLabel())) {
					dataMap.put("status" , "<b style=\"color:purple\">偏低</b>");
				}

            } else {
                dataMap.put("status" , "正常");
            }

			dataMap.put("checkStatus", ds.getCheckStatus());
			dataList.add(dataMap);
		}
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("data",dataList);
		data.put("count",(count + pageSize - 1) / pageSize);
		String resultString = formatToJson(data);
		super.pringWriterToPage(resultString, "application/json", response);
	}

	/**
	 * 获取密级的编号与优先级的集合
	 * @return
	 */
	private Map<String, Integer> getAllClassifyUserLevelList() {
		Map<String, Integer> classifies = new HashMap<>();
		List<DlpClassify> classifyList = searchDataReadService.getDlpClassify();
		if(null != classifyList && classifyList.size() > 0) {
			for(DlpClassify classify : classifyList) {
				classifies.put(classify.getCnum(), classify.getUseLevel());
			}
		}
		return classifies;
	}

	/**
	 * 获取密级的编号与优先级的集合
	 * @return
	 */
	private Map<String, String> getAllClassifyNameCnumList() {
		Map<String, String> classifies = new HashMap<>();
		List<DlpClassify> classifyList = searchDataReadService.getDlpClassify();
		if(null != classifyList && classifyList.size() > 0) {
			for(DlpClassify classify : classifyList) {
				classifies.put(classify.getName(), classify.getCnum());
			}
		}
		return classifies;
	}

	/**
	 * 获取目标记录的所有的标记
	 */
	@RequestMapping("/getTargetLabelsByRecordId")
	@ResponseBody
	public void getTargetLabelsByRecordId(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Integer id = Integer.parseInt(request.getParameter("id"));
		Map<String, Object> data = searchDataReadService.findDlpCheckRecord(id);
		String resultString = formatToJson(data);
		super.pringWriterToPage(resultString, "application/json", response);
	}

	/**
	 * 获取记录详情
	 */
	@RequestMapping("/getCheckRecordById")
	@ResponseBody
	public void getCheckRecordById(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Integer id = Integer.parseInt(request.getParameter("id"));
		Map<String, Object> data = searchDataReadService.findDlpCheckRecord(id);
		String resultString = formatToJson(data);
		super.pringWriterToPage(resultString, "application/json", response);
	}

	/**
	 * 标记预警任务
	 */
	@RequestMapping("/recordReview")
	@ResponseBody
	public void recordReview(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Integer id = Integer.parseInt(request.getParameter("id"));
		String reviewResult = request.getParameter("reviewResult");
		// 获取当前的登录用户
		Integer userid = (Integer) request.getSession().getAttribute("userid");
		Map<String, Object> data = searchDataWriteService.recordReview(id,reviewResult,userid);
		String resultString = formatToJson(data);
		super.pringWriterToPage(resultString, "application/json", response);
	}


	/**
	 * 添加或修改任务
	 */
	@RequestMapping("/addOrEditTask")
	@ResponseBody
	public void addOrEditTask(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, String> data = new HashMap<>();
		String name = request.getParameter("name");
		String detail = request.getParameter("detail");
		String datasourceId = request.getParameter("datasourceId");
		String filePath = request.getParameter("filePath");
		String modelId = request.getParameter("modelId");
		String cronExpression = request.getParameter("cronExpression");
		String totalCheckFile = request.getParameter("totalCheckFile");

		DlpCheckTask task = new DlpCheckTask();

		task.setName(name);
		task.setDetail(detail);
		task.setDatasourceId(Integer.parseInt(datasourceId));
		task.setFilePath(filePath);
		task.setModelId(modelId);
		task.setFileTotal(Integer.parseInt(totalCheckFile));
		task.setCreateUid((Integer) request.getSession().getAttribute("userid"));
		task.setTaskStatus(0);
		task.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		task.setCronExpression(cronExpression);

		searchDataWriteService.saveDlpCheckTask(task);


		// 拼装 job 对象
		JobAndTrigger trigger = new JobAndTrigger();
		// jobName = task.id
		trigger.setJobName("job"+task.getId());
		trigger.setJobGroup("jobGroup"+task.getName());
		trigger.setDescription(task.getName() + "创建了...");
		trigger.setJobClassName("org.cloud.framework.job.task.DlpCheckTaskJob");
		trigger.setCronExpression(cronExpression);

		save(trigger, task);

		// 解析文件夹到数据库
		if(searchDataReadService.getDataSourceById(task.getDatasourceId()).getSourceType() == 0) {
			unZip2TargetDirectory(task.getId() + "", task, request);
		} else if(searchDataReadService.getDataSourceById(task.getDatasourceId()).getSourceType() == 1) {
			parseUploadFile2Database(task.getId() + "", task.getFilePath(), request,task);
		}

		String resultString = formatToJson(data);
		super.pringWriterToPage(resultString, "application/json", response);
	}

	/**
	 * 任务启动
	 */
	@RequestMapping("/startCheckTask")
	@ResponseBody
	public void startCheckTask(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String ids = request.getParameter("id");
		JobAndTrigger trigger = new JobAndTrigger();
		Map<String, String> result = new HashMap<>();
		if(!ids.contains(",")) {
			try {
				DlpCheckTask dlpCheckTask = searchDataReadService.findDlpCheckTask(Integer.parseInt(ids));
				trigger.setJobName("job"+dlpCheckTask.getId());
				trigger.setJobGroup("jobGroup"+dlpCheckTask.getName());
//				trigger.setCronExpression(dlpCheckTask.getCronExpression());
				trigger(trigger);
				result.put("code", "1");
			} catch (NumberFormatException e) {
				result.put("code", "0");
				e.printStackTrace();
			}
		} else {
			try {
				for(String idStr : Arrays.asList(ids.split(","))) {
					DlpCheckTask dlpCheckTask = searchDataReadService.findDlpCheckTask(Integer.parseInt(idStr));
					trigger.setJobName("job"+dlpCheckTask.getId());
					trigger.setJobGroup("jobGroup"+dlpCheckTask.getName());
//					trigger.setCronExpression(dlpCheckTask.getCronExpression());
					trigger(trigger);
				}
				result.put("code", "1");
			} catch (NumberFormatException e) {
				result.put("code", "0");
				e.printStackTrace();
			}
		}

		String resultString = formatToJson(result);
		super.pringWriterToPage(resultString, "application/json", response);
	}

	/**
	 * 解压缩到指定的文件夹
	 * @param dlpCheckTask
	 */
	private void unZip2TargetDirectory(String taskId, DlpCheckTask dlpCheckTask, HttpServletRequest request) {
		String zipPath = dataUploadFile + File.separator + dlpCheckTask.getFilePath();
		File zipFile = new File(zipPath);
		String targetPath = zipPath.substring(0, zipPath.lastIndexOf("."));
		FileUtils.unZip(zipFile, targetPath);
		parseUploadFile2Database(taskId, targetPath,request,dlpCheckTask);
	}

	/**
	 * 解析指定的文件夹的内容到数据库
	 * @param
	 * @return
	 */
	private List<DlpCheckRecord> parseUploadFile2Database(String taskId, String checkPath, HttpServletRequest request,DlpCheckTask dlpCheckTask) {
		List<DlpCheckRecord> records = new ArrayList<>();
		File uploadDirectory = new File(checkPath);
		if(!uploadDirectory.exists() || uploadDirectory.listFiles().length == 0) {
			return records;
		}
		File[] files = uploadDirectory.listFiles();
		boolean dirFlag = false;
		for(File f : files) {
			if(f.isDirectory()) {
				dirFlag = true;
				break;
			}
		}
		if(!dirFlag) {
			// 目录结构不符合要求，返回!
			return records;
		}

		// 中文的集合
		List<String> goodLabels = searchDataReadService.getChinLabelsByModelId(searchDataReadService.findDlpCheckTask(Integer.parseInt(taskId)).getModelId());


		dataSourceTypeReverseMap = getStaticDataSourceTypeReverseMap();
		String deptId = searchDataReadService.getDlpDepartmentIdByLoginUser((Integer) request.getSession().getAttribute("userid"));
		Integer sourceType = searchDataReadService.getDataSourceById(dlpCheckTask.getDatasourceId()).getSourceType();
		Map<String, String> nameCnumList = getAllClassifyNameCnumList();

		for(File dir : files) {

			if(dir.isFile() || !goodLabels.contains(dir.getName())) {
				continue;
			}
			File[] files1 = dir.listFiles();
			for(File corpus : files1) {
				if(corpus.isDirectory()) {
					continue;
				}

				DlpCheckRecord record = new DlpCheckRecord();

				record.setFileName(corpus.getName());
				record.setFileType(Integer.parseInt(dataSourceTypeReverseMap.get(corpus.getName().substring(corpus.getName().lastIndexOf(".") + 1))));
				record.setFileSize("" + corpus.length());
				record.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				record.setCreateUid((Integer) request.getSession().getAttribute("userid"));
				record.setTaskId(dlpCheckTask.getId());
				// 设置原始的密级查询 classify 表
				record.setOriginLabel(nameCnumList.get(dir.getName()));
				// 设置存放路径...
				if(sourceType == 1) { // 文件夹形式的数据源
					if(checkPath.endsWith(File.separator)) {
						checkPath = checkPath.substring(0, checkPath.length() - 1);
					}
					record.setFileDiskPath(corpus.getAbsolutePath().substring(checkPath.length() + 1));
				} else {
					record.setFileDiskPath(corpus.getAbsolutePath().substring(dataUploadFile.length() + 1));
				}
				record.setDptId(deptId);

//				searchDataWriteService.saveDlpCheckRecord(record);
				records.add(record);
			}


		}

		Long beginTime = System.currentTimeMillis();
		// 批量插入数据...
		if(records.size() > 0) {

			int batchNum = 100;
			int n = records.size();
			int subIndex = 0;
			for(int i = 0; i <= n; i++) {
				if(i > 0 && i % batchNum == 0 || i == n) {
					searchDataWriteService.saveDlpCheckRecordBatch(records.subList(subIndex, i), batchNum);
					subIndex = i;
				}
			}
			System.out.println("插入完成, 耗时: " + (System.currentTimeMillis() - beginTime) + " SSS");


		}

		return null;

	}

	/**
	 * 任务暂停
	 */
	@RequestMapping("/stopCheckTask")
	@ResponseBody
	public void stopCheckTask(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String ids = request.getParameter("id");
		JobAndTrigger trigger = new JobAndTrigger();
		Map<String, String> result = new HashMap<>();
		if(!ids.contains(",")) {
			try {
				DlpCheckTask dlpCheckTask = searchDataReadService.findDlpCheckTask(Integer.parseInt(ids));
				trigger.setJobName("job"+dlpCheckTask.getId());
				trigger.setJobGroup("jobGroup"+dlpCheckTask.getName());
				trigger.setCronExpression(dlpCheckTask.getCronExpression());
				pause(trigger, response);
				result.put("code", "1");
			} catch (NumberFormatException e) {
				result.put("code", "0");
				e.printStackTrace();
			}
		} else {
			try {
				for(String idStr : Arrays.asList(ids.split(","))) {
					DlpCheckTask dlpCheckTask = searchDataReadService.findDlpCheckTask(Integer.parseInt(idStr));
					trigger.setJobName("job"+dlpCheckTask.getId());
					trigger.setJobGroup("jobGroup"+dlpCheckTask.getName());
					trigger.setCronExpression(dlpCheckTask.getCronExpression());
					pause(trigger, response);
				}
				result.put("code", "1");
			} catch (NumberFormatException e) {
				result.put("code", "0");
				e.printStackTrace();
			}
		}

		String resultString = formatToJson(result);
		super.pringWriterToPage(resultString, "application/json", response);
	}

	/**
	 * 暂停任务调度
	 * @param quartz
	 * @param response
	 * @return
	 */
	public Result pause(JobAndTrigger quartz, HttpServletResponse response) {
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
	 * 启动调度任务
	 * @param quartz
	 * @param
	 * @return
	 */
	public Result trigger(JobAndTrigger quartz) {
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
	 * 移除调度任务
	 * @param quartz
	 * @param response
	 * @return
	 */
	public Result remove(JobAndTrigger quartz, HttpServletResponse response) {
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

	/**
	 * 新建调度任务
	 * @param
	 * @param quartz
	 * @param task
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void save(JobAndTrigger quartz, DlpCheckTask task) {
		try {
			JobDataMap jobDataMap = new JobDataMap();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("taskId", task.getId());
			jobDataMap.put("data", map);
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
			JobDetail job = JobBuilder.newJob(cls).setJobData(jobDataMap).withIdentity(quartz.getJobName(), quartz.getJobGroup())
					.withDescription(quartz.getDescription()).build();
			/*** 触发时间点 */
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity("trigger" + quartz.getJobName(), quartz.getJobGroup()).startNow()
					.withSchedule(cronScheduleBuilder).build();

			/**
			 * *交由Scheduler安排触发
			 **/
			scheduler.scheduleJob(job, trigger);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件上传
	 */
	@RequestMapping("/upload")
	@ResponseBody
	private String upload(@RequestParam(value="file",required = false) MultipartFile file, HttpServletRequest request) throws IOException {
	    Integer modelId = Integer.parseInt(request.getParameter("modelId"));

		Map<String, Object> result = new HashMap<>();
		if(null != file) {
			String originalFilename = file.getOriginalFilename();
			File f = new File(dataUploadFile);
			if(!f.exists()) {
				f.mkdir();
			}
			if(!StringUtils.isEmpty(originalFilename)) {
				// 文件名重新命名
				String newName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + originalFilename.substring(originalFilename.lastIndexOf("."));
				// 创建文件
				File destFile = new File(dataUploadFile + File.separator + newName);
				// 写入到服务器中
				file.transferTo(destFile);
				result.put("code", "1");
				result.put("fileUrl", newName);
				result.put("originalFilename", originalFilename.substring(0, originalFilename.lastIndexOf(".")));
				result.put("fileSize", NumberUtils.formatFileSize(destFile.length()));
				if(originalFilename.contains(".")) {
					result.put("fileType", originalFilename.substring(originalFilename.lastIndexOf(".") + 1));
				}
				if(destFile.getAbsolutePath().endsWith(".zip")) {
					result.put("totalCheckNum", searchDataReadService.getTotalCkeckNum(modelId + "", destFile) + "");
					result.put("checkRange", JSON.toJSONString(searchDataReadService.getTargetCheckRange(modelId + "", destFile)));
				}
			}
		} else {
			result.put("code", "0");
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 获取文件夹的文件的数量,同时复制文件到上传目录...
	 */
	@RequestMapping("/getTotalCheckFile")
	@ResponseBody
	private void getTotalCheckFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<>();
		String uploadDirectory = request.getParameter("uploadDirectory");
		String modelId = request.getParameter("modelId");
		File file = new File(uploadDirectory);
		if(!file.exists()) {
			result.put("code","文件目录不存在!!!");
			return;
		} else {
			List<String> badInfos = searchDataReadService.findBadInfo(modelId, uploadDirectory);
			if(badInfos.size() > 0) {
				result.put("errMsg", CollectionUtils.list2Str(badInfos));
			}
			result.put("code", searchDataReadService.getTotalFileOnly(modelId, uploadDirectory));
			result.put("checkRange", JSON.toJSONString(getGoodCheckRange(modelId + "", uploadDirectory)));
		}

		String resultString = formatToJson(result);
		super.pringWriterToPage(resultString, "application/json", response);

	}

	public List<String> getGoodCheckRange(String modelId, String targetPath) {
		List<String> result = new ArrayList<>();
		// 中文的集合
		List<String> goodLabels = searchDataReadService.getChinLabelsByModelId(modelId);

		File uploadDirectory = new File(targetPath);
		if(!uploadDirectory.exists() || uploadDirectory.listFiles().length == 0) {
			return result;
		}
		File[] files = uploadDirectory.listFiles();
		for(File f : files) {
			if(goodLabels.contains(f.getName())) {
				result.add(f.getName());
			}
		}

		return result;
	}



	/*==========================================================数据源=========================================================================*/

	/**
	 * 获取所有的数据源的集合
	 */
	@RequestMapping("/getAllDatasources")
	@ResponseBody
	public String getAllDatasources(HttpServletRequest request, HttpServletResponse response) throws Exception{
		/*dataSourceTypeMap = getStaticDataSourceTypeMap();
		List<LinkedHashMap> result = new ArrayList<>();
		Iterator iter = dataSourceTypeMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			LinkedHashMap map = new LinkedHashMap();
			map.put("key", entry.getKey());
			map.put("value", entry.getValue());
			result.add(map);
		}*/
		dataSourceTypeMap = getStaticDataSourceTypeMap();
		List<DlpDataSource> list = searchDataReadService.getDlpDataSource();
		List<Object> result = new ArrayList<>();
		if(list == null || list.size() == 0) {
			return "0";
		}
		for(DlpDataSource ds : list) {
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("key", ds.getId());
			temp.put("value",ds.getName());
			result.add(temp);
		}
		String resultString = formatToJson(result);
		return resultString;
	}

	/**
	 * 获取所有的数据源的集合
	 */
	@RequestMapping("/getSourceTypeBySourceId")
	@ResponseBody
	public String getSourceTypeBySourceId(HttpServletRequest request, HttpServletResponse response) {
		Integer id = Integer.parseInt(request.getParameter("id"));
		Integer code = searchDataReadService.getDataSourceById(id).getSourceType();
		// String name = searchDataReadService.getSourceTypeNameBySourceCode(Integer.parseInt(code));
		return code + "";
	}



	/**
	 * 删除数据源
	 */
	@RequestMapping("/delDataSource")
	@ResponseBody
	public void delDataSource(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String ids = request.getParameter("id");
		Map<String, String> result = new HashMap<>();
		if(!ids.contains(",")) {
			try {
				searchDataWriteService.deleteDlpDataSource(Integer.parseInt(ids));
				result.put("code", "1");
			} catch (NumberFormatException e) {
				result.put("code", "0");
				e.printStackTrace();
			}
		} else {
			try {
				for(String idStr : Arrays.asList(ids.split(","))) {
					searchDataWriteService.deleteDlpDataSource(Integer.parseInt(idStr));
				}
				result.put("code", "1");
			} catch (NumberFormatException e) {
				result.put("code", "0");
				e.printStackTrace();
			}
		}

		String resultString = formatToJson(result);
		super.pringWriterToPage(resultString, "application/json", response);
	}

	/**
	 * 测试数据源连通性
	 */
	@RequestMapping("/checkSecDataSrc")
	@ResponseBody
	public void checkSecDataSrc(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String usernmae = request.getParameter("username");
		String password = request.getParameter("password");
		String url = request.getParameter("url");
		String sourceType = request.getParameter("sourceType");

		HashMap<String, Object> data = dlpCheckTaskService.checkSecDataSrc(usernmae, password, url, sourceType);

		String resultString = formatToJson(data);
		super.pringWriterToPage(resultString, "application/json", response);
	}

	/**
	 * 查询任务
	 */
	@RequestMapping("/getDatabase")
	@ResponseBody
	public void getDatabase(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String id = request.getParameter("id");
		DlpDataSource dlpDataSource = searchDataReadService.findDlpDataSource(Integer.parseInt(id));
		String resultString = formatToJson(dlpDataSource);
		super.pringWriterToPage(resultString, "application/json", response);
	}

	/**
	 * 添加或修改数据源
	 */
	@RequestMapping("/addOrEditDatasource")
	@ResponseBody
	public void addOrEditDatasource(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, String> data = new HashMap<>();
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String detail = request.getParameter("detail");
		String isStoped = request.getParameter("isStoped");
		String usernmae = request.getParameter("username");
		String password = request.getParameter("password");
		String url = request.getParameter("url");
		Integer sourceType = Integer.parseInt(request.getParameter("sourceType"));
		DlpDataSource dataSource = new DlpDataSource();
		if(!StringUtils.isEmpty(id) && !"undefined".equalsIgnoreCase(id)) {
			dataSource.setId(Integer.parseInt(id));
		}
		dataSource.setName(name);
		dataSource.setIsStoped(Integer.parseInt(isStoped));
		dataSource.setUrl(url);
		dataSource.setUser(usernmae);
		dataSource.setPassword(password);
		dataSource.setSourceType(sourceType);
		dataSource.setDetail(detail);
		dataSource.setCreateUid((Integer) request.getSession().getAttribute("userid"));
		if(StringUtils.isEmpty(id) || "undefined".equalsIgnoreCase(id)) {
			searchDataWriteService.saveDlpDataSource(dataSource);
		} else {
			searchDataWriteService.editDlpDataSource(dataSource);
		}

		String resultString = formatToJson(data);
		super.pringWriterToPage(resultString, "application/json", response);
	}


	/**
	 * 获取数据源列表
	 */
	@RequestMapping("/getDataSourceList")
	@ResponseBody
	public void getDataSourceList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		dataSourceTypeMap = getStaticDataSourceTypeMap();
		Integer pageNum = Integer.parseInt(request.getParameter("pageNum"));
		Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("^,id","asc");
		List<DlpDataSource> dlpDataSourceList = searchDataReadService.getDataSourceList(queryParams,pageNum, pageSize);
		Integer count = searchDataReadService.getDlpDataSourceCount(queryParams);
		List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> dataMap = null;
		for(DlpDataSource ds:dlpDataSourceList){
			dataMap = new HashMap<String, Object>();
			dataMap.put("id", ds.getId());
			dataMap.put("name", ds.getName());
			dataMap.put("detail", ds.getDetail());
			dataMap.put("sourceType", dataSourceTypeMap.get(ds.getSourceType() + ""));
			dataMap.put("url", ds.getUrl());
			dataMap.put("user", ds.getUser());
			dataMap.put("password", ds.getPassword());
			dataMap.put("isStoped", ds.getIsStoped());
			dataMap.put("coding", ds.getCoding());
			dataMap.put("createTime", ds.getCreateTime());
			SystemUser user = systemUserService.getById(ds.getCreateUid());
			dataMap.put("createUser", user.getUsername());
			dataList.add(dataMap);
		}
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("data",dataList);
		data.put("count",(count + pageSize - 1) / pageSize);
		String resultString = formatToJson(data);
		super.pringWriterToPage(resultString, "application/json", response);
	}

}
