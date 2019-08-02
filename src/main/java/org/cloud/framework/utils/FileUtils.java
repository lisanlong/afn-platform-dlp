package org.cloud.framework.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class FileUtils {

	public static void main(String[] args) {
	}

	/**
	 * 复制文件到目标目录
	 */
	public static void copyDirectory2TargetDirectory(String fromPath, String targetPath) {
		//初始化文件复制
		File file1=new File(fromPath);
		//把文件里面内容放进数组
		File[] fs=file1.listFiles();
		//初始化文件粘贴
		File file2=new File(targetPath);
		//判断是否有这个文件有不管没有创建
		if(!file2.exists()){
			file2.mkdirs();
		}
		//遍历文件及文件夹
		for (File f : fs) {
			if(f.isFile()){
				//文件
				copySingleFile(f.getPath(),targetPath+"\\"+f.getName()); //调用文件拷贝的方法
			}else if(f.isDirectory()){
				//文件夹
				copyDirectory2TargetDirectory(f.getPath(),targetPath+"\\"+f.getName());//继续调用复制方法      递归的地方,自己调用自己的方法,就可以复制文件夹的文件夹了
			}
		}
	}

	private static void copySingleFile(String src, String to) {
		File resfile = new File(src);
		File tfile = new File(to);
		FileInputStream fis=null;
		FileOutputStream fos=null;
		FileChannel in=null;
		FileChannel out = null;
		try {
			fis = new FileInputStream(resfile);
			fos = new FileOutputStream(tfile);
			in = fis.getChannel();// 得到对应的文件通道
			out= fos.getChannel();// 得到对应的文件通道
			long start = System.currentTimeMillis();
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
			long end = System.currentTimeMillis();
			System.out.println("运行时间："+(start-end)+"毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				in.close();
				fos.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取压缩文件内的文件的总数
	 * @param destFile
	 * @return
	 */
	public static String getTotalCkeckNum(File destFile) {
		// 1. 解压
		String targetPath = destFile.getAbsolutePath().substring(0, destFile.getAbsolutePath().lastIndexOf("."));
		unZip(destFile, targetPath);
		// 2. 获取文件内的文件的数量
		Integer totalFileOnly = getTotalFileOnly(targetPath);
		// 3. 删除临时文件
		deleteFile(new File(targetPath));
		return totalFileOnly + "";
	}

	public static void deleteFile(File file) {
		if (file.exists()) {//判断文件是否存在
			if (file.isFile()) {//判断是否是文件
				file.delete();//删除文件
			} else if (file.isDirectory()) {//否则如果它是一个目录
				File[] files = file.listFiles();//声明目录下所有的文件 files[];
				for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件
					deleteFile(files[i]);//把每个文件用这个方法进行迭代
				}
				file.delete();//删除文件夹
			}
		} else {
			System.out.println("所删除的文件不存在");
		}
	}

	public static Integer getTotalFileOnly(String targetPath) {
		int total = 0;
		File uploadDirectory = new File(targetPath);
		if(!uploadDirectory.exists() || uploadDirectory.listFiles().length == 0) {
			return total;
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
			return total;
		}

		for(File dir : files) {
			if(dir.isFile()) {
				continue;
			}
			File[] files1 = dir.listFiles();
			for(File corpus : files1) {
				if(corpus.isDirectory()) {
					continue;
				}
				total++;
			}
		}
		return total;
	}

	public static class Txt{
		/**
		 * 读取文件内容------txt中一行为一条数据库数据
		 * @param inPath
		 * @throws Exception 
		 */
		public static List<Map<String,Object>> readTxtFile(String inPath,String rowRegex,String fieldRegex) throws Exception {
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			FileInputStream fis = null;
			InputStreamReader isr = null;
			BufferedReader br = null;
			String str = "";
			fis = new FileInputStream(inPath);
			isr = new InputStreamReader(fis,"utf-8");
			br = new BufferedReader(isr);
			Map<String, String> toMap =null;
			Map<String, Object> map =null;
			while ((str = br.readLine()) != null) {
				if(str.trim().equals("")){
					continue;
				}
				String[] splitRow = splitRow(str, rowRegex);
				map = new HashMap<String, Object>();
				for (int i = 0; i < splitRow.length; i++) {
					 toMap = fieldToMap(splitRow[i], fieldRegex);
					 if(!toMap.isEmpty()){
						 map.putAll(toMap);
					 }
				}
				listMap.add(map);
			}
			close(fis);
			close(isr);
			close(isr);
			return listMap;
		}
		/**
		 * 获取txt文件字段，类型是一行一条数据
		 * @param inPath
		 * @param rowRegex
		 * @param fieldRegex
		 * @return
		 * @throws Exception
		 */
		public static List<Map<String,String>>getFieldName(String inPath,String rowRegex,String fieldRegex) throws Exception{
			List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
			FileInputStream fis = null;
			InputStreamReader isr = null;
			BufferedReader br = null;
			String str = "";
			fis = new FileInputStream(inPath);
			isr = new InputStreamReader(fis,"utf-8");
			br = new BufferedReader(isr);
			Map<String, String> toMap =null;
			Map<String, String> map =null;
			Map<String, String> map2 =null;
			while ((str = br.readLine()) != null) {
				String[] splitRow = splitRow(str, rowRegex);
				map = new HashMap<String, String>();
				for (int i = 0; i < splitRow.length; i++) {
					 toMap = fieldToMap(splitRow[i], fieldRegex);
					 if(!toMap.isEmpty()){
						 map.putAll(toMap);
					 }
				}
				for (String key : map.keySet()) {
					map2 = new HashMap<String, String>();
					map2.put("fieldName", key);
					listMap.add(map2);
				}
				break;
			}
			close(fis);
			close(isr);
			close(isr);
			return listMap;
		}
		/**
		 * 读取txt字段------txt中多行为一条数据库数据，行空格为一条数据库数据
		 * @param inPath
		 * @throws Exception 
		 */
		public static List<Map<String,String>> getFieldName(String inPath,String fieldRegex) throws Exception {
			List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
			FileInputStream fis = null;
			InputStreamReader isr = null;
			BufferedReader br = null;
			String str = "";
			fis = new FileInputStream(inPath);
			isr = new InputStreamReader(fis,"utf-8");
			br = new BufferedReader(isr);
			Map<String, String> toMap = null;
			Map<String, String> map2 = null;
			while ((str = br.readLine()) != null) {
				if(str.equals("")){
					break;
				}else{
					toMap=new HashMap<String, String>();
					toMap = fieldToMap(str, fieldRegex);
				}
				for (String key : toMap.keySet()) {
					map2 = new HashMap<String,String>();
					map2.put("fieldName", key);
					listMap.add(map2);
				}
			}
			close(fis);
			close(isr);
			close(isr);
			return listMap;
		}
		/**
		 * 获取txt内容------txt中多行为一条数据库数据，行空格为一条数据库数据
		 * @param inPath
		 * @throws Exception 
		 */
		public static List<Map<String,Object>> readTxtFile(String inPath,String fieldRegex) throws Exception {
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			FileInputStream fis = null;
			InputStreamReader isr = null;
			BufferedReader br = null;
			String str = "";
			fis = new FileInputStream(inPath);
			isr = new InputStreamReader(fis,"utf-8");
			br = new BufferedReader(isr);
			Map<String, String> toMap = null;
			Map<String, Object> map = new HashMap<String,Object>();
			while ((str = br.readLine()) != null) {
				if(str.equals("")){
					if(!map.isEmpty()){
						listMap.add(map);
					}
					map = new HashMap<String,Object>();
				}else{
					toMap = fieldToMap(str, fieldRegex);
					if(!toMap.isEmpty()){
						map.putAll(toMap);
					}
				}
			}
			close(fis);
			close(isr);
			close(isr);
			return listMap;
		}
		/**
		 * 一行一条数据库数据  分割
		 * @param str
		 * @param regex
		 * @return
		 */
		public static String []splitRow(String str,String regex){
			if(str == null || str.equals("")){
				return new String[0];
			}
			return str.split(regex);
		}
		/**
		 * 字段分割
		 * @param str
		 * @param regex
		 * @return
		 */
		public static Map<String, String> fieldToMap(String str,String regex){
			Map<String, String> map = new HashMap<String,String>();
			if(str == null){
				return map;
			}
			String[] split = str.split(regex);
			if(split.length==1 && str.matches("^.*"+regex+".*$")){
				map.put(split[0], "");
				return map;
			}
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < split.length; i++) {
				if(i==split.length-1){
					sb.append(split[i]);
				}else{
					sb.append(split[i]+regex);
				}
				
			}
			map.put(split[0],sb.toString());
			return map;
		}
		/**
		 * 读取json数据
		 * @param inPath
		 * @return
		 * @throws Exception
		 */
		public static List<Map<String,Object>> readJsonFile(String inPath) throws Exception {
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			StringBuffer sb = readJsonToString(inPath);
			listMap=jsonToMap(sb.toString().trim());
			return listMap;
		}
		/**
		 * 获取json字段
		 * @param inPath
		 * @return
		 * @throws Exception
		 */
		public static List<Map<String,String>> getJsonFieldName(String inPath) throws Exception {
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			List<Map<String,String>> listField = new ArrayList<Map<String,String>>();
			StringBuffer sb = readJsonToString(inPath);
			listMap=jsonToMap(sb.toString().trim());
			Map<String,String> mapf=null;
			if(!listMap.isEmpty()){
				Map<String, Object> map = listMap.get(0);
				for (String key : map.keySet()) {
					mapf=new HashMap<String,String>();
					mapf.put("fieldName", key);
					listField.add(mapf);
				}
			}
			return listField;
		}
		/**
		 * 读取json数据
		 * @param inPath
		 * @return
		 * @throws Exception
		 */
		public static StringBuffer readJsonToString(String inPath) throws Exception {
			FileInputStream fis = null;
			InputStreamReader isr = null;
			BufferedReader br = null;
			String str = "";
			StringBuffer sb = new StringBuffer();
			fis = new FileInputStream(inPath);
			isr = new InputStreamReader(fis,"utf-8");
			br = new BufferedReader(isr);
			while ((str = br.readLine()) != null) {
				if(!str.trim().equals("")){
					sb.append(str);
				}
			}
			close(fis);
			close(isr);
			close(isr);
			return sb;
		}
		/**
		 * 转list<map>
		 * @param str
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		public static List<Map<String,Object>> jsonToMap(String str){
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = null;
			JSONArray jsonArr = JSONArray.fromObject(str);
			for (int i = 0; i < jsonArr.size(); i++) {
				map = new HashMap<String,Object>();
				JSONObject jsonObj = jsonArr.getJSONObject(i);
				Iterator iterator = jsonObj.keys();
				String key = null;
				String value = null;
				while (iterator.hasNext()) {
					key = (String) iterator.next();
					value = jsonObj.getString(key);
					map.put(key, value);
				}
				listMap.add(map);
			}
			return listMap;
		}
		public static String[] getJsonField(String strt){
			return null;
		}
	}
	/**
	 * xml内部类
	 */
	public static class Xml{
		/**
		 * 加载文件
		 * @param filePath
		 * @return
		 * @throws DocumentException
		 */
		public static  Document getDocument(String filePath) throws DocumentException{
	        File file = new File(filePath);
	        SAXReader saxReader = new SAXReader();
	        Document doc = saxReader.read(file);
	        return doc;
	    }
		/**
		 * 获取第三层标签students->students->id/name/...
		 * <students>
		 * 		<student>
		 * 			<id></id>
		 * 			<name></name>
		 * 		</student>
		 * </students>
		 * @param doc
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public static String[] getLables(Document doc){
			Element root = doc.getRootElement();
			List<Element> elements = root.elements();
			String []lable = null;
			if(elements.size()!=0){
				Element element = elements.get(0);
				List<Element> elements2 = element.elements();
				lable = new String[elements2.size()];
				for (int i = 0; i < elements2.size(); i++) {
					lable[i]=elements2.get(i).getName();
				}
			}else{
				return new String[0];
			}
	       return lable;
		}
		/**
		 * 获取xml字段
		 * @param doc
		 * @param elementPath
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public static List<Map<String,String>> getFieldName(Document doc,String elementPath){
			List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
			Map<String,String> map=null;
			List<Element> selectNodes = doc.selectNodes(elementPath);
			for (Element element : selectNodes) {
				List<Element> elements = element.elements();
				for (Element element2 : elements) {
					map=new HashMap<String, String>();
					map.put("fieldName", element2.getQualifiedName());
					listMap.add(map);
				}
				break;
			}
			return listMap;
		}
		@SuppressWarnings("unchecked")
		public static List<Map<String,Object>> readXml(String filePath,String elementPath) throws DocumentException{
			Document doc = getDocument(filePath);
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			Map<String,Object> map=null;
			List<Element> selectNodes = doc.selectNodes(elementPath);
			for (Element element : selectNodes) {
				List<Element> elements = element.elements();
				map=new HashMap<String, Object>();
				for (Element element2 : elements) {
					map.put(element2.getQualifiedName(), element2.getText());
				}
				listMap.add(map);
			}
			return listMap;
		}
		
		@SuppressWarnings("unchecked")
		public static String getNodeText(Document doc,String elementPath){
			Element element = selectSingleNode(doc, elementPath);
			if(element == null){
				return null;
			}
			List<Element> elements = element.elements();
			if(elements.size()==0){
				return  element.getText();
			}else{
				return element.asXML();
			}
		}

		public static Element selectSingleNode(Document doc, String elementPath) {
			Node node = doc.selectSingleNode(elementPath);
			return node == null ? null : (Element) node;
		}

	}
	/**
	 * 关闭
	 * @param auto
	 */
	public static void close(AutoCloseable auto) {
		if (auto != null)
			try {
				auto.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public static String trim(String str){
		if(str == null){
			return "";
		}
		if(str.equals("")){
			return str;
		}
		str = str.replaceAll("\\s+", "");
		return str;
	}
	public static void show(List<Map<String, Object>> listMap){
		for (int i = 0; i < listMap.size(); i++) {
			for (String key : listMap.get(i).keySet()) {
				System.out.print(key+":"+listMap.get(i).get(key)+"	");
			}
			System.out.println();
		}
	}


	/**

	 * zip解压

	 * @param srcFile        zip源文件

	 * @param destDirPath     解压后的目标文件夹

	 * @throws RuntimeException 解压失败会抛出运行时异常

	 */

	public static void unZip(File srcFile, String destDirPath) throws RuntimeException {

		long start = System.currentTimeMillis();

		// 判断源文件是否存在

		if (!srcFile.exists()) {

			throw new RuntimeException(srcFile.getPath() + "所指文件不存在");

		}

		// 开始解压

		ZipFile zipFile = null;

		try {
			//
			zipFile = new ZipFile(srcFile, Charset.forName("GBK"));

			Enumeration<?> entries = zipFile.entries();

			while (entries.hasMoreElements()) {

				ZipEntry entry = (ZipEntry) entries.nextElement();

				System.out.println("解压" + entry.getName());

				// 如果是文件夹，就创建个文件夹

				if (entry.isDirectory()) {

					String dirPath = destDirPath + "/" + entry.getName();

					File dir = new File(dirPath);

					dir.mkdirs();

				} else {

					// 如果是文件，就先创建一个文件，然后用io流把内容copy过去

					File targetFile = new File(destDirPath + "/" + entry.getName());

					// 保证这个文件的父文件夹必须要存在

					if(!targetFile.getParentFile().exists()){

						targetFile.getParentFile().mkdirs();

					}

					targetFile.createNewFile();

					// 将压缩文件内容写入到这个文件中

					InputStream is = zipFile.getInputStream(entry);

					FileOutputStream fos = new FileOutputStream(targetFile);

					int len;

					byte[] buf = new byte[1024];

					while ((len = is.read(buf)) != -1) {

						fos.write(buf, 0, len);

					}

					// 关流顺序，先打开的后关闭

					fos.close();

					is.close();

				}

			}

			long end = System.currentTimeMillis();

			System.out.println("解压完成，耗时：" + (end - start) +" ms");

		} catch (Exception e) {

			throw new RuntimeException("unzip error from ZipUtils", e);

		} finally {

			if(zipFile != null){

				try {

					zipFile.close();

				} catch (IOException e) {

					e.printStackTrace();

				}

			}

		}

	}
}
