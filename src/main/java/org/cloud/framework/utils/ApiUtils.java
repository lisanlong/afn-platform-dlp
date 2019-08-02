package org.cloud.framework.utils;

import java.io.Serializable;

/**
 * api接口返回实体类
 */
public class ApiUtils implements Serializable{

    //响应码规范
    public static final String RESPONSE_SUCCESS = "000000#响应成功";
    public static final String PARAMETER_ERROR = "000012#参数错误";
    public static final String RESPONSE_ERROR = "000014#响应失败";

    //响应码
    private String code;
    //相应内容
    private String message;
    //响应时间
    private String responseTime;
    //返回数据
    private Object data;

    public ApiUtils(String codeMessage, Object data) {
        code = codeMessage.split("#")[0];
        message = codeMessage.split("#")[1];
        responseTime = DateUtils.getDateTime();
        this.data = data;
    }



    /**
     * @param args
     */
    public static void main(String[] args) {
        ApiUtils aApiUtils = new ApiUtils("111111@信息有误", "2222");
        System.out.println("aApiUtils=" + aApiUtils);
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
