package org.cloud.framework.utils;

import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * txt文件处理
 * 
 * @author vimes
 * 
 */
public class TxtReadUtil {

	public static List<HashMap<String, String>> txt2StringForPos(InputStream aInputStream) {
		List<HashMap<String, String>> r = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> mp = null;
		String encoding = "utf-8";
		try {
			InputStreamReader read = new InputStreamReader(aInputStream, encoding);// 考虑到编码格式
			BufferedReader br = new BufferedReader(read);// 构造一个BufferedReader类来读取文件
			String s = null;
			// 使用readLine方法，一次读一行
			while ((s = br.readLine()) != null) {
				String wordcolor = s.split(" ")[0];
				String color = s.split(" ")[1];
				String code = s.split(" ")[2];
				String name = s.split(" ")[s.split(" ").length - 1];
				mp = new HashMap<String, String>();
				mp.put("code", code);
				mp.put("name", name);
				mp.put("wordcolor", wordcolor);
				mp.put("color", color);
				r.add(mp);
//				System.out.println(color + " " + code + " " + name);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * 读取txt文件的内容
	 * 
	 * @param file
	 *            想要读取的文件对象
	 * @return 返回文件内容
	 */
	public static List<HashMap<String, String>> txt2String(File file) {
		List<HashMap<String, String>> r = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> mp = null;
		String encoding = "utf-8";
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					file), encoding);// 考虑到编码格式
			BufferedReader br = new BufferedReader(read);// 构造一个BufferedReader类来读取文件
			String s = null;
			// 使用readLine方法，一次读一行
			while ((s = br.readLine()) != null) {
				String nameEn = s.split(" ")[0];
				String nameCn = s.split(" ")[s.split(" ").length - 1];
				mp = new HashMap<String, String>();
				mp.put("nameCn", nameCn);
				mp.put("nameEn", nameEn);
				r.add(mp);
//				System.out.println(code + " " + name);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			InputStream aInputStream = TxtReadUtil.class.getResourceAsStream("/config/concept.txt");
			File file = ResourceUtils.getFile("classpath:config/concept.txt");
			List<HashMap<String, String>> r = TxtReadUtil.txt2String(file);
			System.out.println(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*String aa = "{\"basic_words\":[\"去年\"],\"byte_length\":4,\"byte_offset\":0,\"formal\":\"\",\"item\":\"去年\",\"loc_details\":[],\"ne\":\"TIME\",\"pos\":\"\",\"uri\":\"\"}";
		Map data = (Map) JSON.parse(aa);
		System.out.println(data.get("item"));
		System.out.println(data.get("pos"));*/
	}

}
