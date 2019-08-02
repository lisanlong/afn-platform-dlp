package org.cloud.framework.utils;

import java.io.*;
import java.util.*;

public class TxtUtil {
	
	public static void writeTxt(String filePath, List<Map<String,Object>>listMap,String regex) {
	    File f = new File(filePath);
	    OutputStreamWriter writer = null;
	    BufferedWriter bw = null;
	    try {
	        OutputStream os = new FileOutputStream(f,true);
	        writer = new OutputStreamWriter(os);
	        bw = new BufferedWriter(writer);
	        
	       for (String key : listMap.get(0).keySet()) {//表头
	    	   bw.write(key+regex);
	       }
	       bw.newLine();
	       for (int i = 1; i < listMap.size(); i++) {
	        	for (String key : listMap.get(i).keySet()) {
	        		bw.write(listMap.get(i).get(key).toString()+regex);
				}
	        	if(i%10000==0){
					System.out.println("===="+i+"/"+listMap.size()+"=====");	
				}
		        bw.newLine();
			}
	        bw.flush();
	        if(f.exists()){
	            f.delete();
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            bw.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	public static List<Map<String,Object>> readTxt(String path,int startLine,int pageSize,String regex) throws IOException{
		List<Map<String,Object>> l = new ArrayList<Map<String,Object>>();
		FileInputStream inputStream = null;
		Scanner sc = null;
		int index = 0;//记录总点数
		String[] head = null;
		String[] split = null;
		StringBuffer sb = new StringBuffer();
		try {
		    inputStream = new FileInputStream(path);
		    sc = new Scanner(inputStream, "UTF-8");
		    Map<String,Object> m = null;
		    while (sc.hasNextLine()) {
				index = index + 1;
				m = new HashMap<String, Object>();
				String line = sc.nextLine();
				// 第index行
				if (index == 1) {// 表头
					head = line.split(regex);
				} else {
					if (index - 1 >= startLine) {
						split = line.split(regex);
						for (int i = 0; i < head.length; i++) {
							if(i<split.length){
								char[] charArray = split[i].toCharArray();
								sb = new StringBuffer();
								for (char c : charArray) {
									if (!(c < 0x9 || c > 0x9 && c < 0xA || c > 0xA && c < 0xD || c > 0xD && c  
											< 0x20 || c > 0xD7FF && c < 0xE000 || c > 0xFFFD)){//下面了允许的字符范围
										sb.append(c);
										
									}
								}
								m.put(head[i], sb.toString());
							}else{
								m.put(head[i], "");
							}
						}
						l.add(m);
						if (l.size() >= pageSize) {
							break;
						}
					}
					if (index % 10000 == 0) {
						System.out.println("读取行数："+index );
					}
				}
			}
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}
		return l;
	}
	public static int getTxtCount(String path) throws IOException{
		FileInputStream inputStream = null;
		Scanner sc = null;
		int index = 0;//记录总点数
		try {
		    inputStream = new FileInputStream(path);
		    sc = new Scanner(inputStream, "UTF-8");
//		    Map<String,Object> m = null;
		    while (sc.hasNextLine()) {
				index = index + 1;
				sc.nextLine();
			}
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}
		return index-1;
	}
	public static List<Map<String,String>> getTxtColumn(String path,String regex) throws IOException{
		List<Map<String,String>> l = new ArrayList<Map<String,String>>();
		FileInputStream inputStream = null;
		Scanner sc = null;
		int index = 0;//记录总点数
		try {
		    inputStream = new FileInputStream(path);
		    sc = new Scanner(inputStream, "UTF-8");
		    Map<String,String> m = null;
		    while (sc.hasNextLine()) {
				index = index + 1;
				if(index == 1){
					String nextLine = sc.nextLine();
					String[] split = nextLine.split(regex);
					for (int i = 0; i < split.length; i++) {
						m = new HashMap<String, String>();
						m.put("fieldName", split[i]);
						l.add(m);
					}
					break;
				}
				
			}
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}
		return l;
	}
	
	public static void main(String[] args) throws IOException {
		//String filePath = "C:\\Users\\Administrator\\Desktop\\长期保存\\123.txt";
		//List<Map<String, Object>> readTxt = readTxt(filePath,122,3,"\t");
		/*
		for (Map<String, Object> map : readTxt) {
			for (String key : map.keySet()) {
				System.out.print(key+":"+map.get(key)+"\t");
			}
			System.out.println();
		}
		int txtCount = getTxtCount(filePath);
		List<Map<String, String>> txtColumn = getTxtColumn(filePath, "\t");
		for (Map<String, String> map : txtColumn) {
			for (String key : map.keySet()) {
				System.out.println(map.get(key));
			}
		}
		
		System.out.println(txtCount);
		*/
		long start = System.currentTimeMillis();
		String str="911	2016 年俄罗斯十大航天热点新闻	周生东	航天新闻	俄新社梳理了2016年发生的俄罗斯十大航天新闻	2017-01-05	ZRHTKX201701	中国航天员科研训练中心	强静	中国航天员科研训练中心	载人航天快讯	特稿 : 俄新社评出\" 2016 年俄罗斯十大航天热点新闻\"  [俄新社 2016年12月15日] 2016 年＀俄罗斯航天领域经历了一系列的成功＀也经受了失败＀并且从发射次数来说＀从 1999 年开始至今＀这是首次落后于美国和中国. 俄新社梳理了 2016 年发生的俄罗斯十大航天新闻＀ 1. \"东方\"发射场首次成功发射 2016年4月28日是一个值得庆祝的日子＀位于俄罗斯远东地区阿穆尔州新建的\"东方\"发射场完成了运载火箭发射的首秀.\"联盟\" -2.1a 号运载火箭原本于4月27日发射＀但由于自动装置出现技术故障＀因此＀发射日期推迟至4月28日.该运载火箭安装有新型推进器＀成功将\" Аист-2Д \"航天器,\"罗蒙诺索夫\"科研卫星以及\" SamSat-218Д \"纳米卫星送入预定轨道. 2. 俄罗斯和欧盟首次联合研制并发射火星探测器 为了寻找火星上的生命＀俄罗斯与欧盟历史上第一次开始合作＀并通过\"质子\"-M运载火箭于2016年3月14日从拜科努尔发射场发射\"Trace Gas Orbiter\"＀简称TGO＀火箭探索航天器＀并成功进入飞往火星的预定轨道.该航天器的主要任务是证明火星大气层是否有甲烷气体＀并由此来确定火星过去或者现在生命体的存在.而Schiaparelli火星着陆器的主要任务是检验2020年开始的第二次火星之旅的关键技术.2016年10月16日＀TGO进入椭圆形飞行轨道＀但是＀Schiaparelli火星着陆器却未能按规定模式着陆火星表面＀着陆前地球与之失去了约 50 秒的联系.欧航局代表称＀着陆器比预期提前打开降落伞＀其制动发动机工作时间小于规定时间.最终＀欧航局证实＀着陆器在着陆时坠毁. 3＀ 2016年9 S7航 1995 年＀主要用于太平洋赤道海上发射商业卫星等＀总部设在瑞士的伯尔尼. 2010 年改组后＀ 95% 的股权属于 EOL ＀ Energia Overseas Limited ＀--俄罗斯火箭航天集团公司旗下的子公司下属公司＀ 3% 的股权属于美国的波音公司＀ 2% 的股权属于挪威的 Aker Solution 公司. 4＀ 2016 ~ 2025 年俄罗斯联邦航天计划>于2016年3月17日 2025 年之前＀俄罗斯拟建成由 70 颗航天器组成的民用卫星集群. 2016~2025 年航天计划的财政支出约为 2.1 万亿卢布＀但后来缩减为 1.4 万亿卢布.俄罗斯航天集团公司多次强调＀新的联邦航天计划完全保存了基本的空间探索项目＀包括科研基础项目.该航天计划还预留了方案＀如果经济条件允许的话＀ 2021 年之后国家财政会增加约 1150 亿卢布的支出. 5. 完 - 凯利于2015年3月27 TMA-16M 号载人飞船发射升空＀在国际空间站上共驻留了 340 个昼夜＀绕地球 5440 圈＀完成了 400 多项科学试验＀并于2016年3月2 TMA-18M 载人飞船顺利返回地面. 美国\"财富\"杂志将两位航天员列入世界上 50 位最有影响的人物＀他们位于第 22 位.俄罗斯航天集团公司和美国航天局多次称＀此次考察与国际空间站整个计划一样＀世界政治局势复杂化以及对俄罗斯的制裁＀但这没有影响俄美空间项目的合作. 6 . 俄罗斯\"进步\" MS-04 号货运飞船坠毁 俄罗斯\"进步\" MS-04 号货运飞船于2016年12月1 盟U 60 ~ 70 公里处的无人山区＀飞船的大部分在坠落时在大气层中烧毁. 俄罗斯航天专家推测＀可能由于第三级火箭发动机出现故障而引起这次发射事故. 其中一种说法称＀火箭在组装时出现了质量问题＀燃烧室烧蚀造成焊接裂缝.另一种说法是＀火箭的控制系统可能发出了第三级火箭发动机关闭指令＀导致\"进步\" MS-04 号飞船提前分离＀但未进入预设飞行轨道而导致坠毁.俄罗斯航天集团公司称＀事故调查委员会要在2016年12月20 7＀ 1 年之久. 在与国际空间站连接时＀飞船的重量为14.4吨＀如果飞往月球时＀重量可达19吨＀＀返回舱的重量为9吨.飞船长为6.1米.降落时的额定过载为3G＀发射时使用\"安加拉\"-A5B型重型运载火箭系统. 8. 配备了俄罗斯火箭发动机的\"安塔瑞斯\"运载火箭首次发射成功 配备了两个俄罗斯一级发动机 RD-181 的美国\"安塔瑞斯\"运载火箭于2016年10月18 -230 \"＀\" Аntares -230\"＀＀曾多次推迟发射.在2014年10 RD-181 火箭发动机＀俄罗斯专家用了不到1年时间完成了研制生产许可＀并开始向美国供货.2014年12 9. 成功举办\"人类飞天55周年纪念\"活动 \"抬起你的头\"成为2016年激励俄罗斯年轻一代的座右铭＀今年的航天庆祝活动主要是围绕着\"纪念人类第一次飞天\"开展的＀在标语的旁边＀加加林面露微笑. 55年前＀1961年4月12日＀尤里 - 加加林第一次代表人类进入太空＀这开启了一个新的时代＀他的名字也永远写入了人类发展史上.加加林在绕地球一圈后＀乘返回舱经过浓密大气层＀因为当时还没有软着陆系统＀因此加加林是在距地面几千米的高空通过降落伞返回地面的. 10 ＀航天发射次数创历史新低 今年是俄罗斯第一次失去了发射次数排名第一的地位＀排在中国和美国之后.因为＀考虑更多的是建立轨道卫星集群而减少了发射次数.美国在 1999 年超过了俄罗斯＀当时＀美国进行了 31 次发射＀俄罗斯完成了 30 次＀而中国仅进行了 4 次.但是2000年俄罗斯完成了39次发射＀而美国完成了28次＀中国为5次. 2015 年俄罗斯29次＀美国是20次＀中国为19次.＀周生东＀		";
		char[] charArray = str.toCharArray();
		System.out.println(charArray.length);
		StringBuffer sb = new StringBuffer();
		for (char c : charArray) {
			if (!(c < 0x9 || c > 0x9 && c < 0xA || c > 0xA && c < 0xD || c > 0xD && c  
					< 0x20 || c > 0xD7FF && c < 0xE000 || c > 0xFFFD)){//下面了允许的字符范围
				sb.append(c);
				
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(start-end);
		//System.out.println(str);
		System.out.println(sb);
	}
}
