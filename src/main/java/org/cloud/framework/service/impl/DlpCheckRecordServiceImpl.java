package org.cloud.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cloud.framework.model.DlpCheckRecord;
import org.cloud.framework.mapper.DlpCheckRecordMapper;
import org.cloud.framework.model.DlpCheckTask;
import org.cloud.framework.model.DlpClassify;
import org.cloud.framework.protocol.impl.SearchDataReadService;
import org.cloud.framework.service.IDlpCheckRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cloud.framework.utils.DateUtils;
import org.cloud.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LKS
 * @since 2019-07-08
 */
@Service
public class DlpCheckRecordServiceImpl extends ServiceImpl<DlpCheckRecordMapper, DlpCheckRecord> implements IDlpCheckRecordService {

	@Autowired
	private DlpCheckRecordMapper dlpCheckRecordMapper;
	@Autowired
	private SearchDataReadService searchDataReadService;

	@Override
	public List<DlpCheckRecord> getCheckRecordByTaskId(Integer id) {

		QueryWrapper<DlpCheckRecord> queryWrapper = new QueryWrapper<DlpCheckRecord>().eq("task_id", id);
		List<DlpCheckRecord> records = dlpCheckRecordMapper.selectList(queryWrapper);
		Properties labels = new Properties();
		List<DlpClassify> classifies = searchDataReadService.getDlpClassify();
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

		return records;
	}

	@Override
	public Map<String, List<String>> getWarnAndTotalTrend(List<String> lastWeekList) {
		Map<String, List<String>> result = new HashMap<>();
		List<String> keyList = new ArrayList<>();
		List<String> valList = new ArrayList<>();
		List<String> warnList = new ArrayList<>();
		// 检查的总数量
		for(String day : lastWeekList) {
			String fromdate = day + " 00:00:00";
			String enddate = day + " 23:59:59";
			QueryWrapper<DlpCheckRecord> queryWrapper = new QueryWrapper<>();
			queryWrapper.ge("create_time", fromdate);
			queryWrapper.le("create_time", enddate);
			// 已经检查的数量
			queryWrapper.ne("check_status", 0);
			List<DlpCheckRecord> records = dlpCheckRecordMapper.selectList(queryWrapper);
			if(null == records || records.size() == 0) {
				keyList.add(DateUtils.getFormatDate(day));
				valList.add("0");
				continue;
			}

			int totalCheckFilesNum = records.size();
			keyList.add(DateUtils.getFormatDate(day));
			valList.add(totalCheckFilesNum + "");

		}
		for(String day : lastWeekList) {
			String fromdate = day + " 00:00:00";
			String enddate = day + " 23:59:59";
			// 预警总数量
			QueryWrapper<DlpCheckRecord> queryWrapper2 = new QueryWrapper<>();
			queryWrapper2.ge("create_time", fromdate);
			queryWrapper2.le("create_time", enddate);
			// 已经检查的数量
			queryWrapper2.eq("check_status", 2);

			List<DlpCheckRecord> records2 = dlpCheckRecordMapper.selectList(queryWrapper2);
			if(null == records2 || records2.size() == 0) {
				warnList.add("0");
				continue;
			}

			int totalWarnFilesNum = records2.size();
			warnList.add(totalWarnFilesNum + "");
		}


		result.put("keys", keyList);
		result.put("values", valList);
		result.put("warnValues", warnList);

		return result;
	}

	@Override
	public Map<String, List<String>> getTodayWarnNum(List<String> todayList) {
		Map<String, List<String>> result = new HashMap<>();
		List<String> keyList = new ArrayList<>();
		List<String> warnList = new ArrayList<>();
		for(String day : todayList) {
			String fromdate = day + " 00:00:00";
			String enddate = day + " 23:59:59";
			// 预警总数量
			QueryWrapper<DlpCheckRecord> queryWrapper2 = new QueryWrapper<>();
			queryWrapper2.ge("create_time", fromdate);
			queryWrapper2.le("create_time", enddate);
			// 已经检查的数量
			queryWrapper2.eq("check_status", 2);

			List<DlpCheckRecord> records2 = dlpCheckRecordMapper.selectList(queryWrapper2);
			if(null == records2 || records2.size() == 0) {
				warnList.add("0");
				continue;
			}

			int totalWarnFilesNum = records2.size();
			warnList.add(totalWarnFilesNum + "");
		}


		result.put("keys", keyList);
		result.put("warnValues", warnList);

		return result;
	}

}
