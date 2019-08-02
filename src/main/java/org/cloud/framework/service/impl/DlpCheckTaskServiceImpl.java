package org.cloud.framework.service.impl;

import com.alibaba.druid.util.JdbcUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cloud.framework.mapper.*;
import org.cloud.framework.model.*;
import org.cloud.framework.service.IDlpCheckRecordService;
import org.cloud.framework.service.IDlpCheckTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cloud.framework.utils.DateUtils;
import org.cloud.framework.utils.DbConnection;
import org.cloud.framework.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LKS
 * @since 2019-07-08
 */
@Service
public class DlpCheckTaskServiceImpl extends ServiceImpl<DlpCheckTaskMapper, DlpCheckTask> implements IDlpCheckTaskService {

	@Autowired
	private DlpCheckTaskMapper dlpCheckTaskMapper;

	@Autowired
	private DlpCorpusMapper dlpCorpusMapper;

	@Autowired
	private DlpDepartmentMapper dlpDepartmentMapper;

	@Autowired
	private SystemUserMapper systemUserMapper;

	@Autowired
	private DlpModelMapper dlpModelMapper;

	@Autowired
	private DlpCheckRecordMapper dlpCheckRecordMapper;

	@Override
	public Map<String, Integer> getCheckNums() {
		int totalCheckFilesNum = 0;
		int totalTaskNum = 0;
		int totalWarnFilesNum = 0;
		int realWarnFilesNum = 0;

		Map<String, Integer> map = new HashMap<>();
		// 查询所有的语料
//		List<DlpCorpus> dlpCorpuses = dlpCorpusMapper.selectList(null);
//		if(null == dlpCorpuses) {
//			map.put("totalCorpusNum", 0);
//		} else {
//			map.put("totalCorpusNum", dlpCorpuses.size());
//		}

		// 查询所有检查任务
		QueryWrapper<DlpCheckRecord> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("check_status", 0);
		List<DlpCheckRecord> records = dlpCheckRecordMapper.selectList(queryWrapper);
		if(null == records || records.size() == 0) {
			map.put("totalCheckFilesNum", 0);
			map.put("totalTaskNum", 0);
			map.put("totalWarnFilesNum", 0);
			return map;
		}
		totalCheckFilesNum = records.size();

		// 预警总数量
		QueryWrapper<DlpCheckRecord> queryWrapper2 = new QueryWrapper<>();
		queryWrapper2.eq("check_status", 2);
		List<DlpCheckRecord> records2 = dlpCheckRecordMapper.selectList(queryWrapper2);

		totalWarnFilesNum = records2.size();

		// 预警总数量
		QueryWrapper<DlpCheckRecord> queryWrapper3 = new QueryWrapper<>();
		queryWrapper3.eq("review_status", 2);
		List<DlpCheckRecord> records3 = dlpCheckRecordMapper.selectList(queryWrapper3);

		realWarnFilesNum = records3.size();


		map.put("totalCheckFilesNum", totalCheckFilesNum);
		map.put("totalTaskNum", totalTaskNum);
		map.put("totalWarnFilesNum", totalWarnFilesNum);
		map.put("realWarnFilesNum", realWarnFilesNum);

		return map;
	}

	@Override
	public Map<String, List<String>> getCheckTrend(List<String> lastWeekList) {
		Map<String, List<String>> result = new HashMap<>();
		List<String> keyList = new ArrayList<>();
		List<String> valList = new ArrayList<>();

		for(String day : lastWeekList) {
//			Map<String, String> checkTrends = new HashMap<>();
			QueryWrapper<DlpCheckTask> queryWrapper = new QueryWrapper<>();
			String fromdate = day + " 00:00:00";
			String enddate = day + " 23:59:59";
			queryWrapper.ge("create_time", fromdate);
			queryWrapper.le("create_time", enddate);
			List<DlpCheckTask> dlpCheckTasks = dlpCheckTaskMapper.selectList(queryWrapper);
			if(null == dlpCheckTasks || dlpCheckTasks.size() == 0) {
				keyList.add(DateUtils.getFormatDate(day));
				valList.add("0");
				continue;
			}
			// int totalCheckFilesNum = 0;
			// int totalWarnFilesNum = 0;
			// for(DlpCheckTask task : dlpCheckTasks) {
			// 	totalCheckFilesNum += task.getFileTotal();
			// 	totalWarnFilesNum += task.getWarnFileTotal();
			// }
			// if(totalCheckFilesNum == 0) {
			// 	checkTrends.put(DateUtils.getFormatDate(day), "0");
			// 	continue;
			// }
			// String rate = 1 - new BigDecimal((float)totalWarnFilesNum/totalCheckFilesNum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
			// checkTrends.put(DateUtils.getFormatDate(day), rate);

			int totalWarnFilesNum = 0;
			for(DlpCheckTask task : dlpCheckTasks) {
				totalWarnFilesNum += (null==task.getWarnFileTotal()?0:task.getWarnFileTotal());
			}
			keyList.add(DateUtils.getFormatDate(day));
			valList.add(totalWarnFilesNum + "");

		}
		result.put("keys", keyList);
		result.put("values", valList);
		return result;
	}

	@Override
	public Map<String, List<String>> getDepartmentSummary() {
		List<DlpDepartment> dlpDepartments = dlpDepartmentMapper.selectList(null);
		Map<String, List<String>> result = new HashMap<>();
		List<String> keyList = new ArrayList<>();
		List<String> valList = new ArrayList<>();
		List<String> warnList = new ArrayList<>();
		for(DlpDepartment department : dlpDepartments) {
			int totalCheckFilesNum = 0;
			int totalWarnFilesNum = 0;
			// 根据部门 id 查询对应的用户
			List<SystemUser> users = systemUserMapper.selectList(new QueryWrapper<SystemUser>().eq("dpt_id", department.getId()));
			for(SystemUser user : users) {

				/*// 根据用户 id 查询对应的执行的任务
				List<DlpCheckTask> dlpCheckTasks = dlpCheckTaskMapper.selectList(new QueryWrapper<DlpCheckTask>().eq("create_uid", user.getId()));
				if(null == dlpCheckTasks || dlpCheckTasks.size() == 0) {
					totalCheckFilesNum += 0;
					totalWarnFilesNum += 0;
					continue;
				}
				for(DlpCheckTask task : dlpCheckTasks) {
					totalCheckFilesNum += (null==task.getFileTotal()?0:task.getFileTotal());
				}
				for(DlpCheckTask task : dlpCheckTasks) {
					totalWarnFilesNum += (null==task.getWarnFileTotal()?0:task.getWarnFileTotal());
				}*/
				// 所有的检查的记录
				List<DlpCheckRecord> records = dlpCheckRecordMapper.selectList(new QueryWrapper<DlpCheckRecord>().eq("create_uid", user.getId()).ne("check_status", 0));
				if(null == records || records.size() == 0) {
					totalCheckFilesNum += 0;
					continue;
				}
				totalCheckFilesNum += records.size();

			}

			for(SystemUser user : users) {

				// 所有的预警的记录
				List<DlpCheckRecord> records = dlpCheckRecordMapper.selectList(new QueryWrapper<DlpCheckRecord>().eq("create_uid", user.getId()).eq("check_status", 2));
				if(null == records || records.size() == 0) {
					totalWarnFilesNum += 0;
					continue;
				}
				totalWarnFilesNum += records.size();

			}

			keyList.add(department.getName());
			valList.add(totalCheckFilesNum + "");
			warnList.add(totalWarnFilesNum + "");

		}

		result.put("keys", keyList);
		result.put("values", valList);
		result.put("warnValues", warnList);
		return result;
	}

	@Override
	public List<Map<String, Object>> getVersionUsageRate() {
		// 查询检查任务包含的所有的 DlpModel 对象
		List<DlpModel> dlpModels = dlpModelMapper.selectTaskModelList();
		// 存放键值对的集合
		List<Map<String, Object>> list = new ArrayList<>();
		// 临时集合
		Map<String, Object> result = new HashMap<>();
		int totalSum = 0;
		for(DlpModel dlpModel : dlpModels) {
			int totalCheckFilesNum = 0;
			// 根据模型 id 查询对应的数量
			List<DlpCheckTask> dlpCheckTasks = dlpCheckTaskMapper.selectList(new QueryWrapper<DlpCheckTask>().eq("model_id", dlpModel.getId()));
			if(null == dlpCheckTasks || dlpCheckTasks.size() == 0) {
				totalCheckFilesNum += 0;
				continue;
			}
			for(DlpCheckTask task : dlpCheckTasks) {
				totalCheckFilesNum += (null==task.getFileTotal()?0:task.getFileTotal());
			}
			totalSum += totalCheckFilesNum;
			result.put(dlpModel.getVersion() + "", totalCheckFilesNum + "");
		}
		int percentSum = 0;
		for(DlpModel dlpModel : dlpModels) {
			Map<String, Object> map = new HashMap<>();
			int targetNum = Integer.parseInt((String) result.get(dlpModel.getVersion()));
			int percent =  Integer.parseInt(NumberUtils.getRatePercent(targetNum, totalSum, 0));
			if(dlpModels.indexOf(dlpModel) == (dlpModels.size() - 1)) {
				percent = 100 - percentSum;
			}
			percentSum += percent;
			map.put("name", "V" + dlpModel.getVersion() + ".0");
			map.put("value", percent);
			list.add(map);
		}

		return list;
	}

	@Override
	public Map<String, List<String>> getTaskTrend(List<String> lastWeekList) {
		Map<String, List<String>> result = new HashMap<>();
		List<String> keyList = new ArrayList<>();
		List<String> valList = new ArrayList<>();
		for(String day : lastWeekList) {
			QueryWrapper<DlpCheckTask> queryWrapper = new QueryWrapper<>();
			String fromdate = day + " 00:00:00";
			String enddate = day + " 23:59:59";
			queryWrapper.ge("create_time", fromdate);
			queryWrapper.le("create_time", enddate);
			List<DlpCheckTask> dlpCheckTasks = dlpCheckTaskMapper.selectList(queryWrapper);
			if(null == dlpCheckTasks || dlpCheckTasks.size() == 0) {
				keyList.add(DateUtils.getFormatDate(day));
				valList.add("0");
				continue;
			}

			int totalCheckFilesNum = 0;
			for(DlpCheckTask task : dlpCheckTasks) {
				totalCheckFilesNum += (null==task.getFileTotal()?0:task.getFileTotal());
			}
			keyList.add(DateUtils.getFormatDate(day));
			valList.add(totalCheckFilesNum + "");
		}
		result.put("keys", keyList);
		result.put("values", valList);

		return result;
	}

	@Override
	public HashMap<String, Object> checkSecDataSrc(String usernmae, String password, String url, String sourceType) {
		HashMap<String, Object> result = new HashMap<>();
		Connection conn  = getConnection(url,usernmae,password,sourceType);
		if(conn==null){
			result.put("code", "0");
		}else{
			result.put("code", "1");
		}
		JdbcUtils.close(conn);
		return result;
	}

	public Connection getConnection(String url, String user, String password, String type){
		Connection conn = null;
		try {
			if(type.equalsIgnoreCase("mysql")){
				conn  = DbConnection.MySQL.getConnection(url, user, password);
			} else if(type.equalsIgnoreCase("oracle")){
				conn  = DbConnection.Oracle.getConnection(url, user, password);
			} else if(type.equalsIgnoreCase("sqlserver")){
				conn  = DbConnection.SQLServer.getConnection(url, user, password);
			}
            /*if(type.equals("access")){
                conn  = DbConnection.Access.getConnection(url,coding, user, password);
            }*/
		} catch (Exception e) {
		}
		return conn;
	}
}
