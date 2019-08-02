package org.cloud.framework.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import org.cloud.framework.utils.StringUtils;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author LKS
 * @since 2019-07-09
 */
@TableName("system_log")
public class SystemLog extends Model<SystemLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日志类型（info-正常日志 error-错误日志）
     */
    private String type;

    /**
     * 日志标题
     */
    private String title;

    /**
     * 请求地址
     */
    @TableField("remote_addr")
    private String remoteAddr;

    /**
     * URL
     */
    @TableField("request_uri")
    private String requestUri;

    /**
     * 提交方式(get/post)
     */
    private String method;

    /**
     * 提交参数
     */
    private String params;

    /**
     * 异常信息
     */
    private String exception;

    /**
     * 超时时间
     */
    private String timeout;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;

    /**
     * 操作人
     */
    @TableField("create_user")
    private String createUser;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SystemLog{" +
        ", id=" + id +
        ", type=" + type +
        ", title=" + title +
        ", remoteAddr=" + remoteAddr +
        ", requestUri=" + requestUri +
        ", method=" + method +
        ", params=" + params +
        ", exception=" + exception +
        ", timeout=" + timeout +
        ", createTime=" + createTime +
        ", createUser=" + createUser +
        "}";
    }
    /**
     * 设置请求参数
     * @param paramMap
     */
    public void setMapToParams(Map<String, String[]> paramMap) {
        if (paramMap == null){
            return;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramMap).entrySet()){
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
        }
        this.params = params.toString();
    }
}
