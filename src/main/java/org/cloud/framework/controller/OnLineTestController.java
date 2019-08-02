package org.cloud.framework.controller;

import com.alibaba.fastjson.JSON;
import org.apache.tika.exception.TikaException;
import org.cloud.framework.model.DlpClassify;
import org.cloud.framework.model.DlpModel;
import org.cloud.framework.protocol.INlpService;
import org.cloud.framework.protocol.ISearchDataReadService;
import org.cloud.framework.service.IDlpModelService;
import org.cloud.framework.utils.NumberUtils;
import org.cloud.framework.utils.StringUtils;
import org.cloud.framework.utils.TikaUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author wgsh
 * @Date wgshb on 2019/7/11 14:54
 */
@Controller
@RequestMapping("/onLineTest")
public class OnLineTestController extends BaseContoroller {

	@Autowired
	private IDlpModelService dlpModelService;
	@Autowired
	private ISearchDataReadService searchDataReadService;
	@Autowired
	private INlpService nlpService;

	@Value("${data.upload.file}")
	private String dataUploadFile;

	/**
	 * 根据检测结果获取对应的检测的名称
	 */
	@RequestMapping("/getCheckResult")
	@ResponseBody
	private String getCheckResult(HttpServletRequest request) {
		String label = request.getParameter("label");
		DlpClassify classify = searchDataReadService.getDlpClassifyByCnum(label);
		return classify.getName();
	}

	/**
	 * 文件检测
	 */
	@RequestMapping("/filePredict")
	@ResponseBody
	private String filePredict(HttpServletRequest request) throws IOException, TikaException {
		String fileUrl = request.getParameter("fileUrl");
		String id = request.getParameter("id");
		DlpModel dlpModel = searchDataReadService.findDlpModel(Integer.parseInt(id));
		Map<String, Object> result = dlpModelService.filePredict(dataUploadFile + fileUrl, dlpModel.getVersion());
		ArrayList<String> maxSentences = (ArrayList<String>) result.get("maxSentences");

		if(maxSentences == null || maxSentences.size() == 0) {
			result.put("content", "");
		}
		StringBuilder sb = new StringBuilder();
		for(String sen : maxSentences) {
//			sen = sen.replace("　","").replace(" ", "");
			if(!StringUtils.isEmpty(sen.trim()) && maxSentences.indexOf(sen) != maxSentences.size() - 1) {
				sb.append((maxSentences.indexOf(sen) + 1) + ". " + sen.replace("　","").replace(" ", "") + "\n");
			} else if(!StringUtils.isEmpty(sen.trim()) && maxSentences.indexOf(sen) == maxSentences.size() - 1) {
				sb.append((maxSentences.indexOf(sen) + 1) + ". " + sen.replace("　","").replace(" ", ""));
			}
		}

		String orignContent = TikaUtil.getParseText(new File(dataUploadFile + fileUrl));
		for(String sen : maxSentences) {
//			sen = sen.replace("　","").replace(" ", "");
			if(!StringUtils.isEmpty(sen.replace("　","").replace(" ", "").trim())) {

				orignContent = orignContent.replace(sen, "<strong  style=\"color: red\">"+sen+"</strong>");
			}
		}
		result.put("formatContent", orignContent);
		System.out.println("==================================");
		System.out.println(orignContent);


		result.put("content", sb.toString());

		return JSON.toJSONString(result);
	}

	/**
	 * 文件检测
	 */
	@RequestMapping("/textPredict")
	@ResponseBody
	private String textPredict(HttpServletRequest request) {
		String orignContent = request.getParameter("textArea");
		String id = request.getParameter("id");
		DlpModel dlpModel = searchDataReadService.findDlpModel(Integer.parseInt(id));
		Map<String, Object> stringObjectMap = dlpModelService.textPredict(orignContent, dlpModel.getVersion());

//		orignContent = orignContent.replace("　","").replace(" ", "");
		Map<String, Object> result = new HashMap<>();
		ArrayList<String> maxSentences = (ArrayList<String>) stringObjectMap.get("maxSentences");
		if(maxSentences == null || maxSentences.size() == 0) {
			result.put("content", "");
		}
		for(String sen : maxSentences) {
//			sen = sen.replace("　","").replace(" ", "");
			if(!StringUtils.isEmpty(sen.replace("　","").replace(" ", "").trim())) {

				orignContent = orignContent.replace(sen, "<strong  style=\"color: red\">"+sen+"</strong>");
			}
		}
		result.put("content", orignContent);
		result.putAll(stringObjectMap);

		return JSON.toJSONString(result);
	}

	/**
	 * 文件上传
	 */
	@RequestMapping("/upload")
	@ResponseBody
	private String upload(@RequestParam(value="file",required = false)MultipartFile file, HttpServletRequest request) throws IOException {
		Map<String, String> result = new HashMap<>();
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
//				result.put("fileUrl", destFile.getAbsolutePath());
				result.put("fileUrl", File.separator + newName);
				result.put("originalFilename", originalFilename.substring(0, originalFilename.lastIndexOf(".")));
				result.put("fileSize", NumberUtils.formatFileSize(destFile.length()));
				if(originalFilename.contains(".")) {
					result.put("fileType", originalFilename.substring(originalFilename.lastIndexOf(".") + 1));
				}
 			}
		} else {
			result.put("code", "0");
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 根据模型获取对应的密级范围
	 */
	@RequestMapping("/getSelectedLabels")
	@ResponseBody
	private String getSelectedLabels(HttpServletRequest request) {
		String id = request.getParameter("id");
		List<String> result = dlpModelService.getSelectedLabels(id);
		return JSON.toJSONString(result);
	}

	/**
	 * 获取所有的模型
	 * @return
	 */
	@RequestMapping("/getAllModel")
	@ResponseBody
	private String getAllModel() {
		List<LinkedHashMap<String, Object>> result = dlpModelService.getAllModel();
		return JSON.toJSONString(result);
	}

	//全文查看
	@RequestMapping("seeFileTest")
	@ResponseBody
	public void seeFileTest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String filePath = request.getParameter("filePath");
		String content = "";
		if(filePath!=null && !filePath.equals("")){
			File f = new File(filePath);
			if(f.exists()){
				content = nlpService.getFileContent(f.getAbsolutePath());
			}
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", "true");
		map.put("content", content);
		String resultString = formatToJson(map);
		super.pringWriterToPage(resultString, "application/json", response);
	}

}
