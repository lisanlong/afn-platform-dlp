package org.cloud.framework.protocol;

import java.util.Map;
import java.util.List;

import org.cloud.framework.model.DlpCheckTask;
import org.cloud.framework.model.DlpClassify;
import org.cloud.framework.model.DlpCorpus;
import org.cloud.framework.model.DlpCword;
import org.cloud.framework.model.DlpModel;
import org.cloud.framework.model.DlpTerm;

public interface INlpService {
	/**
	 * 	文件内容浏览 
	 * @param text
	 * @return
	 */
	String  getFileContent(String filePath);
	/**
	 * 	密级创建 
	 * @param text
	 * @return
	 */
	boolean  classifyCreate(DlpClassify dlpClassify);
	/**
	 * 	术语发布 
	 * @param text
	 * @return
	 */
	boolean  termReload(List<DlpTerm> termList);
	/**
	 * 	触发词发布 
	 * @param text
	 * @return
	 */
	boolean  cwordReload(List<DlpCword> cwordList);
    /**
     * 	语料发布 
     * @param text
     * @return
     */
	boolean corpusExtract(DlpCorpus corpus);
    /**
     * 	模型训练
     * @param model
     * @return
     */
    Map<String, Object> modelTrain(DlpModel model);
    /**
     * 
     *	 模型清空
     * @param model
     * @return
     */
    void modelClear(DlpModel model);
    /**
     *	执行检测 任务
     * @param task
     * @return
     */
    Map<String, Object> checkTask(DlpCheckTask task);
    
	/**
	 * 	在线文本测试
	 * @param text
	 * @param model
	 * @return
	 * Map<"labels",Set<String>>;
     * 	<"maxlabel","秘密">;
     * 	<"predictType","预测方式，0模型预测；1触发词匹配">;
     * 	<"wordWeight",Map<"特征词、触发词", "权重 ">>;
     * 	<"maxSentences",new String[] {"特征句子"}>;
	 */
    Map<String, Object> onlineTextPredict(String text,DlpModel model);
    
    /**
     * 	在线文件测试 
     * @param FilePath
     * @param model
     * @return
     * Map<"labels",Set<String>>;
     * 	<"maxlabel","秘密">;
     * 	<"predictType","预测方式，0模型预测；1触发词匹配">;
     * 	<"wordWeight",Map<"特征词、触发词", "权重 ">>;
     * 	<"maxSentences",new String[] {"特征句子"}>;
     */
    Map<String, Object> onlineFilePredict(String FilePath,DlpModel model);
    
    
    
    
    //
}
