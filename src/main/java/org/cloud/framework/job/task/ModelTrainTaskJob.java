package org.cloud.framework.job.task;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import org.cloud.framework.model.DlpModel;
import org.cloud.framework.service.IDlpModelService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

//import com.bestdata.nlp.classify.test.Classify4Web;
//import com.bestdata.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModelTrainTaskJob  implements Job,Serializable {

		private static final long serialVersionUID = 1L;
		
		@Value("${dlp.corpus.train}")
		private String trainDir;  
		@Value("${dlp.corpus.model}")
		private String modelDir;  
		
	    @Autowired
	    private IDlpModelService dlpModelService;
	    
	    /**
	     * JobExecutionContext:JobDataMap<"data",Map<"modelId",Integer>>
	     */
		@SuppressWarnings("unchecked")
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {

			/**注入jobService 执行相关业务操作*/
			//log.info("模型训练任务 执行开始=========================");
			JobDataMap data = context.getJobDetail().getJobDataMap();
		    Map<String,Integer> datamap = (Map<String, Integer>) data.get("data");
		    DlpModel dlpModel= dlpModelService.getById(datamap.get("modelId"));
		    String  version =  dlpModel.getVersion();
		    String filterLabs = dlpModel.getLabels();
		  //  String modelname = "model_v"+version+"_"+DateUtils.dateTime();
		    
			//Classify4Web cs = new Classify4Web(modelDir+"/"+modelname+".model");
			//cs.setParams(trainDir, modelDir+"/"+modelname+".train");
//			cs.setExcuteType("wordCount");
		//	cs.setFilterLabs(Arrays.asList(filterLabs.split(";")));
			try {
				//dlpModel.setName(modelname);
				dlpModel.setModelStatus(1);
				//dlpModel.setStartTime(DateUtils.dateTimeFormate());
				dlpModelService.saveOrUpdate(dlpModel);
				
				//log.info("训练预料处理开始！");
				
			//	cs.trainFileProcess();
				dlpModel.setModelStatus(2);
				dlpModelService.saveOrUpdate(dlpModel);
				//log.info("训练预料处理结束！");
				
			//	cs.train();
				dlpModel.setModelStatus(3);
				dlpModelService.saveOrUpdate(dlpModel);
				//log.info("模型训练结束！");
				
			} catch (Exception e) {
			//	log.error("模型训练异常！模型名称："+modelname);
				e.printStackTrace();
				dlpModel.setModelStatus(4);
				
			}
			//dlpModel.setEndTime(DateUtils.dateTimeFormate());
			dlpModelService.saveOrUpdate(dlpModel);
			
			//log.info("模型训练任务  执行结束=========================");
		}

	}
