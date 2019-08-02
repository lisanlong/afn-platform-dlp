package org.cloud.framework.utils;

import com.alibaba.fastjson.JSONReader;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

/**
 * JSON文件管理
 */
public class JSONFileUtil {

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {

//        Map<String,Object> result = new LinkedHashMap<>();
//
//        HashMap<String,Object> explain = new HashMap<>();
//        explain.put("batchNumber","T1001");
//        explain.put("batchType","insert"); //batchType:insert/update/delete
//        explain.put("databaseEn","qklw");
//        explain.put("dataCount",100);
//        explain.put("createTime","2019-04-24 15:15:15");
//        explain.put("createUser","anhuifeng");
//        result.put("EXPLAIN", explain);
//
//        List<HashMap> list = new ArrayList<>();
//        HashMap<String,Object> map = null;
//        for (int i = 0; i < 100; i++) {
//            map = new HashMap<>();
//            map.put("title","TsAGI历史上\"的百年纪念：弹道导弹Bulava-30");
//            map.put("cover","/upload/imgs/tsagi.com/image/4e375780c9ecbf711f78c394e35eedc0.jpeg");
//            map.put("content","<p style=\\\"text-align:center\\\"><span style=\\\"color:red\\\">————————————————————本文来源于国外网站,以下是机器翻译内容,向下阅读可查看原文————————————————————</span></p>    <div class=\\\"news-detail\\\">   <p><img src=\\\"/upload/imgs/tsagi.com/image/4e375780c9ecbf711f78c394e35eedc0.jpeg\\\"></p>   <p>国内固体燃料弹道导弹Bulava-30首次发射于2005年9月27日在白海水域发射。  导弹成功获得并击中目标。  </p>   <p>莫斯科热能技术研究所于1998年开始研制导弹。此前，该研究所开发了一种陆基导弹，即Topol-M。  该研究所的设计者面临着开发一种新的先进导弹的挑战，该导弹将部署在Borei级潜艇上。  经过几年的计算和研究，结果产生了一个近      <nobr>       37吨，      </nobr>      <nobr>       12英尺      </nobr>三级Bulava-30，能够携带6到10枚弹头，并且可以在8000公里的距离内进行单独的引导。  </p>   <p>Zhukovsky中央航空动力学研究所（TsAGI）的专家参与了这个项目，为确保水下发射提供科学支持。  TsAGI的Bulava工作周期持续了数年，于2012年结束。在此期间，科学家们开发了高效的水下发射方法，并就水中最有利的运动模式及其向大气中的释放提出了建议。  全面的发射测试验证了该研究所的理论和实验研究的结果。  </p>   <p>目前，Bulava-30已有32次发射，其中25次成功。  </p>      <div class=\\\"fb-like\\\" data-send=\\\"true\\\" data-layout=\\\"button_count\\\" data-width=\\\"235\\\" data-show-faces=\\\"false\\\"></p>   </p> <p style=\\\"text-align: center;\\\"><span>————————————————————原文————————————————————</span></p><p style=\\\"text-align: center;\\\">TsAGI centenary in history: ballistic missile Bulava-30</p><div class=\\\"news-detail\\\">   <p><img src=\\\"/upload/imgs/tsagi.com/image/4e375780c9ecbf711f78c394e35eedc0.jpeg\\\"></p>  <p>The domestic solid-fuel ballistic missile Bulava-30 inaugural launch took place inSeptember27, 2005 inthe waters ofthe White Sea. The missile successfully acquired and hit its target.</p>  <p>The Moscow Institute ofThermal Technology started development ofthe missile in1998. Prior, the Institute had developed aland-based missile, the Topol-M. The institute designers were challenged todevelop anew advanced missile tobedeployed onBorei class submarines. The result ofseveral years ofcalculations and studies resulted inanalmost   <nobr>    37-ton,   </nobr>   <nobr>    12-foot   </nobr> three-stage Bulava-30, able tocarry six toten warheads with individual guidance toadistance of8000km.</p>  <p>Specialists ofthe Zhukovsky Central AeroHydrodynamic Institute (TsAGI) participated inthis project giving scientific support toensure its underwater launch. The Bulava work cycle atTsAGI lasted for several years and ended in2012. During this time, scientists developed highly efficient methods for the underwater launch and made recommendations onits most favorable motion modes inwater and its release into the atmosphere. Full-scale launch tests verified the results ofthe Institute’s theoretical and experimental research. </p>  <p>Atpresent, there have been 32launches ofthe Bulava-30, 25ofwhich have been successful.</p>       <div class=\\\"fb-like\\\" data-send=\\\"true\\\" data-layout=\\\"button_count\\\" data-width=\\\"235\\\" data-show-faces=\\\"false\\\"></p>   </p>");
//            list.add(map);
//        }
//        result.put("RECORDS", list);
//
//        String jsonString = JSON.toJSONString(result);
//        JSONFileUtil.createJsonFile(jsonString, "/Users/anhuifeng/Documents/json", "agency");

        //解析JSON文件
//        File file = new File("/Users/anhuifeng/Documents/json/bs_ts.json");
//        String fileContent = org.apache.commons.io.FileUtils.readFileToString(file);
//        JSONObject aJSONObject = JSON.parseObject(fileContent);
//
//        Map<String, Object> explainObj = JSONObject.toJavaObject(aJSONObject.getJSONObject("EXPLAIN"), Map.class);
//        System.out.println("batchNumber=" + explainObj.get("batchNumber"));
//        System.out.println("dataCount=" + explainObj.get("dataCount"));
//        System.out.println("databaseEn=" + explainObj.get("databaseEn"));
//        System.out.println("batchType=" + explainObj.get("batchType"));
//        System.out.println("createUser=" + explainObj.get("createUser"));
//        System.out.println("createTime=" + explainObj.get("createTime"));
//
//        JSONArray recordsObj = aJSONObject.getJSONArray("RECORDS");
//        for (int i=0;i<recordsObj.size();i++) {
//            String title = recordsObj.getJSONObject(i).getString("title");
//            String cover = recordsObj.getJSONObject(i).getString("cover");
//            String summary = recordsObj.getJSONObject(i).getString("summary");
//
//            System.out.println("title=" + title);
//            System.out.println("cover=" + cover);
//            System.out.println("summary=" + summary);
//        }

        //解析大JSON文件
//        File fileBig = new File("/Users/anhuifeng/Downloads/perio_artical_001.json");
//        JSONReader reader=new JSONReader(new FileReader(file));

        JSONFileUtil aJSONFileUtil = new JSONFileUtil();
        aJSONFileUtil.ReadWithFastJson();

    }

    /**
     * FastJson逐行解析json
     * @author drlyee
     * @date 2015-02-10
     */
    public void ReadWithFastJson() throws FileNotFoundException {
//        String jsonString = "{\"array\":[1,2,3],\"arraylist\":[{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"}],\"object\":{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},\"string\":\"HelloWorld\"}";

        // 如果json数据以形式保存在文件中，用FileReader进行流读取！！
        // path为json数据文件路径！！
//         JSONReader reader = new JSONReader(new FileReader("/Users/anhuifeng/Downloads/perio_artical_001.json"));
         JSONReader reader = new JSONReader(new FileReader("/Users/anhuifeng/Documents/json/bs_ts.json"));

        // 为了直观，方便运行，就用StringReader做示例！
//        JSONReader reader = new JSONReader(new StringReader(jsonString));
        reader.startObject();
        System.out.println("start fastjson");
        while (reader.hasNext()) {
            String key = reader.readString();
            System.out.println("key " + key);
            if (key.equals("RECORDS")) {
                reader.startArray();
                System.out.println("start " + key);
                while (reader.hasNext()) {
                    reader.startObject();
                    System.out.println("start arraylist item");
                    while (reader.hasNext()) {
                        String arrayListItemKey = reader.readString();
                        String arrayListItemValue = reader.readObject().toString();
                        System.out.println("key " + arrayListItemKey);
                        System.out.println("value " + arrayListItemValue);
                    }
                    reader.endObject();
                    System.out.println("end arraylist item");
                }
                reader.endArray();
                System.out.println("end " + key);
            }else if (key.equals("EXPLAIN")){
                reader.startObject();
                System.out.println("start object item");
                while (reader.hasNext()) {
                    String objectKey = reader.readString();
                    String objectValue = reader.readObject().toString();
                    System.out.println("key " + objectKey);
                    System.out.println("value " + objectValue);
                }
                reader.endObject();
                System.out.println("end object item");
            }
        }
        reader.endObject();
        reader.close();
        System.out.println("end fastjson");
    }


    /**
     * 生成.json格式文件
     */
    public static boolean createJsonFile(String jsonString, String filePath, String fileName) {
        // 标记文件生成是否成功
        boolean flag = true;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".json";

        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();

            if(jsonString.indexOf("'")!=-1){
                //将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
                jsonString = jsonString.replaceAll("'", "\\'");
            }
            if(jsonString.indexOf("\"")!=-1){
                //将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
                jsonString = jsonString.replaceAll("\"", "\\\"");
            }

            if(jsonString.indexOf("\r\n")!=-1){
                //将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
                jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
            }
            if(jsonString.indexOf("\n")!=-1){
                //将换行转换一下，因为JSON串中字符串不能出现显式的换行
                jsonString = jsonString.replaceAll("\n", "\\u000a");
            }

            // 格式化json字符串
//            jsonString = JsonFormatTool.formatJson(jsonString);

            //求优雅输出
            ObjectMapper mapper = new ObjectMapper();
            Object obj = mapper.readValue(jsonString, Object.class);
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);

            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        // 返回是否成功的标记
        return flag;
    }

}
