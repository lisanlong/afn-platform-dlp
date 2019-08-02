package org.cloud.framework.job.task;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.cloud.framework.model.DlpCheckTask;
import org.cloud.framework.model.DlpDataSource;
import org.apache.commons.lang.StringUtils;
import org.cloud.framework.model.DlpCheckRecord;
import org.cloud.framework.model.DlpModel;
import org.cloud.framework.service.IDlpCheckRecordService;
import org.cloud.framework.service.IDlpCheckTaskService;
import org.cloud.framework.service.IDlpDataSourceService;
import org.cloud.framework.service.IDlpModelService;
import org.cloud.framework.utils.TikaUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.bestdata.nlp.classify.test.Classify4Web;
//import com.bestdata.util.DateUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class DlpCheckTaskJob  implements Job,Serializable {

		private static final long serialVersionUID = 1L;
		
		@Value("${data.upload.file}")
		private String uploadDir;
	    
		@Value("${dlp.corpus.model}")
		private String modelDir;  
		
		@Autowired
	    private IDlpCheckTaskService dlpCheckTaskService;
	    @Autowired
	    private IDlpCheckRecordService dlpCheckRecordService;
	    @Autowired
	    private IDlpDataSourceService dlpDataSourceService;
	    
	    @Autowired
	    private IDlpModelService dlpModelService;
	    
	    
	    /**
	     * JobExecutionContext:JobDataMap<"data",Map<"taskId",Integer>>
	     */
		@SuppressWarnings("unchecked")
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {

			/**注入jobService 执行相关业务操作*/
		//	log.info("文件检查任务 执行开始=========================");
			JobDataMap data = context.getJobDetail().getJobDataMap();
		    Map<String,Integer> datamap = (Map<String, Integer>) data.get("data");
		    
		    DlpCheckTask dlpCheckTask= dlpCheckTaskService.getById(datamap.get("taskId")); 
		   // log.info("文件检查任务 ID："+dlpCheckTask.getId());
		    
		    DlpModel dlpModel= dlpModelService.getById(dlpCheckTask.getModelId()); 
		    String  modelname =  dlpModel.getName();
			QueryWrapper<DlpCheckRecord> queryWrapper = new QueryWrapper<DlpCheckRecord>();
			
			queryWrapper.eq("task_id", dlpCheckTask.getId()).eq("check_status", 0);
			
			List<DlpCheckRecord> list = dlpCheckRecordService.list(queryWrapper);
			
			Map<String, Object> result =null;
			int predictType=0;
			String maxlabel="";
			
			//dlpCheckTask.setStartTime(DateUtils.dateTimeFormate());
			dlpCheckTask.setTaskStatus(1);
			dlpCheckTaskService.saveOrUpdate(dlpCheckTask);
			
			
			DlpDataSource dlpDataSource = dlpDataSourceService.getById(dlpCheckTask.getDatasourceId());
			String rootpath = uploadDir;
			if(dlpDataSource.getSourceType()==1) {//文件夹路径
				rootpath = dlpCheckTask.getFilePath();
			}
			//Classify4Web cs = new Classify4Web(modelDir+"/"+modelname+".model");
		//	cs.loadModel();
			String labels  = dlpModel.getLabels();
			if(StringUtils.isNotEmpty(labels)) {
			//	cs.setFilterLabs(Arrays.asList(labels.split(";")));
			}
//			cs.setExcuteType("wordCount");
			int checkTotal=0;
			int warnTotal=0;
			List<DlpCheckRecord> recordList = new ArrayList<DlpCheckRecord>();
			for(DlpCheckRecord record :list) {
				record.setCheckStatus(3);
				try {
					String source = rootpath+"/"+record.getFileDiskPath();
					String sourceText = TikaUtil.getParseText(new File(source));
					//result = cs.proLabel(sourceText);
					if(null!=result&&result.size()>1) {
						predictType = (int) result.get("predictType");
						maxlabel = (String) result.get("maxProLabel");
						boolean b = record.getOriginLabel().equals(maxlabel);
						record.setCheckLabel(maxlabel);
						record.setPredictType(predictType);
						record.setCheckStatus(b?1:2);
						warnTotal = b?warnTotal:warnTotal+1;
					}
				} catch (Exception e) {
				//	log.error("文件解析异常");
					record.setCheckDetail("文件解析异常");
					e.printStackTrace();
				}
//				record.setTaskId(dlpCheckTask.getId());
	//			record.setOriginLabel(originLabel);
				//record.setCheckTime(DateUtils.dateTimeFormate());
				recordList.add(record);
				checkTotal++;
				if(checkTotal%20==0) {
					dlpCheckRecordService.saveOrUpdateBatch(recordList);
					recordList = new ArrayList<DlpCheckRecord>();
					
					dlpCheckTask.setCheckTotal(checkTotal);
					dlpCheckTask.setWarnFileTotal(warnTotal);
					dlpCheckTaskService.saveOrUpdate(dlpCheckTask);
				}
			}
			if(recordList.size()>0) {
				dlpCheckRecordService.saveOrUpdateBatch(recordList);
			}
			//dlpCheckTask.setEndTime(DateUtils.dateTimeFormate());
			dlpCheckTask.setTaskStatus(2);
			dlpCheckTask.setCheckTotal(checkTotal);
			dlpCheckTask.setWarnFileTotal(warnTotal);
			dlpCheckTaskService.saveOrUpdate(dlpCheckTask);
			//log.info("文件检查任务 执行结束===============ID:"+dlpCheckTask.getId());

		}
	}
