package org.cloud.framework.protocol.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.exception.TikaException;
import org.cloud.framework.model.DlpCheckTask;
import org.cloud.framework.model.DlpClassify;

// import com.bestdata.nlp.Datanlp;
// import com.bestdata.nlp.corpus.dependency.CoNll.CoNLLSentence;
// import com.bestdata.nlp.model.crf.CRFLexicalAnalyzer;
// import com.bestdata.nlp.seg.common.Term;
// import com.google.common.collect.Maps;

import org.cloud.framework.model.DlpCorpus;
import org.cloud.framework.model.DlpCword;
import org.cloud.framework.model.DlpModel;
import org.cloud.framework.model.DlpTerm;
import org.cloud.framework.protocol.INlpService;
import org.cloud.framework.utils.TikaUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//import com.bestdata.nlp.classify.test.Classify4Web;
//import com.bestdata.nlp.util.EquipmentTypeUtil;
//import com.bestdata.nlp.util.FileUtils;
//import com.bestdata.nlp.util.TermUtil;

import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class NlpServiceImpl implements INlpService {
 

	@Value("${data.upload.file}")
	private String uploadDir;
	
	@Value("${dlp.corpus.train}")
	private String trainDir;
	
	@Value("${dlp.corpus.model}")
	private String modelDir;  
	@Override
	public String getFileContent(String filePath) {
		try {
			return TikaUtil.getParseText(new File(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@Override
	public boolean classifyCreate(DlpClassify dlpClassify) {
		String cnum = trainDir+"/"+dlpClassify.getCnum();
		File f = new File(cnum);
		if(!f.exists()||!f.isDirectory()) {
			return f.mkdir();
		} 
		return true;
	}

	@Override
	public boolean termReload(List<DlpTerm> termList) {
		List<String> list = new ArrayList<String>();
			for(DlpTerm dt: termList) {
				list.add(dt.getName());
			}
		try {
			//TermUtil.init().write(list);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean cwordReload(List<DlpCword> cwordList) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for(DlpCword dc: cwordList) {
			map.put(dc.getWord(),dc.getCnum());
		}
		try {
			//EquipmentTypeUtil.init().write(map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	@Override
	public boolean corpusExtract(DlpCorpus corpus) {
		String source  = uploadDir+"/"+corpus.getFilePath();
		String target = trainDir+"/"+corpus.getCnum()+"/"+corpus.getFileName().substring(0, corpus.getFileName().lastIndexOf("."))+".txt";
		// 处理文本
//		fileName = UUIDUtil.generate(true) + ".txt";
		try {
			TikaUtil.parseTextToFile(new File(source),new File(target));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

	@Override
	public void modelClear(DlpModel model) {
		//FileUtils.deleteFile(new File(modelDir+"/"+model.getName()+".model"));
	}
	
	@Override
	public Map<String, Object> modelTrain(DlpModel model) {
		return null;
	}
	@Override
	public Map<String, Object> checkTask(DlpCheckTask task) {

		return null;
	}
	@Override
	public Map<String, Object> onlineTextPredict(String text, DlpModel model) {
		String modelPath =  modelDir+"/"+model.getName()+".model";
		//Classify4Web classify4Web = new Classify4Web(modelPath);
	//	classify4Web.loadModel();
		String labels  = model.getLabels();
		if(StringUtils.isNotEmpty(labels)) {
		//	classify4Web.setFilterLabs(Arrays.asList(model.getLabels().split(";")));
		}
		Map<String, Object>  result =  new HashMap<String, Object>();
		try {
		//	result = classify4Web.proResult(text);
			result.put("status","OK");
		} catch (Exception e) {
			result.put("status","ERROR");
			result.put("msg","内容检测异常");
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Map<String, Object> onlineFilePredict(String FilePath, DlpModel model) {
		
		String modelPath =  modelDir+"/"+model.getName()+".model";
		//Classify4Web classify4Web = new Classify4Web(modelPath);
		//classify4Web.loadModel();
		String labels  = model.getLabels();
		if(StringUtils.isNotEmpty(labels)) {
		//	classify4Web.setFilterLabs(Arrays.asList(model.getLabels().split(";")));
		}
		Map<String, Object>  result =  new HashMap<String, Object>();
		try {
			String text = TikaUtil.getParseText(new File(FilePath));
			//result = classify4Web.proResult(text);
			result.put("status","OK");
		} catch (IOException e) {
			result.put("status","ERROR");
			result.put("msg","内容检测异常");
			e.printStackTrace();
		} catch (TikaException e) {
			result.put("status","ERROR");
			result.put("msg","文件解析异常");
			e.printStackTrace();
		}
//		result.put("labels",Set<String>);
//		result.put("maxlabel","秘密");
//		result.put("predictType","预测方式，0模型预测；1触发词匹配");
//		result.put("wordWeight",Map<"特征词、触发词", "权重 ">);
//		result.put("maxSentences",new String[] {"特征句子"});
		return result;
	}


	

// 
 
}
