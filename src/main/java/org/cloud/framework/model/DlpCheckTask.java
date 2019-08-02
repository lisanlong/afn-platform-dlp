package org.cloud.framework.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LKS
 * @since 2019-07-17
 */
@TableName("dlp_check_task")
public class DlpCheckTask extends Model<DlpCheckTask> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	/**
     *  模型id 
     */
    @TableField("model_id")
    private String modelId;

    /**
     *  任务名称 
     */
    private String name;

    /**
     *  数据源ID 
     */
    @TableField("datasource_id")
    private Integer datasourceId;

    /**
     *  预测文件路径 
     */
    @TableField("file_path")
    private String filePath;

    /**
     *  检查开始时间 
     */
    @TableField("start_time")
    private String startTime;

    /**
     *  检查结束时间 
     */
    @TableField("end_time")
    private String endTime;

    /**
     *  任务执行状态，0未开始，1检查中，2检查完成 
     */
    @TableField("task_status")
    private Integer taskStatus;

    /**
     *  检查文件总数 
     */
    @TableField("file_total")
    private Integer fileTotal;

    /**
     * 已检查数量
     */
    @TableField("check_total")
    private Integer checkTotal;

    /**
     *  预警文件总数 
     */
    @TableField("warn_file_total")
    private Integer warnFileTotal;

    /**
     *  任务描述 
     */
    private String detail;

    /**
     *  创建时间 
     */
    @TableField("create_time")
    private String createTime;

    /**
     *  创建人 
     */
    @TableField("create_uid")
    private Integer createUid;

    /**
     * 定期执行表达式
     * @return
     */
    @TableField("cron_expression")
    private String cronExpression;


    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Integer datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getFileTotal() {
        return fileTotal;
    }

    public void setFileTotal(Integer fileTotal) {
        this.fileTotal = fileTotal;
    }

    public Integer getCheckTotal() {
        return checkTotal;
    }

    public void setCheckTotal(Integer checkTotal) {
        this.checkTotal = checkTotal;
    }

    public Integer getWarnFileTotal() {
        return warnFileTotal;
    }

    public void setWarnFileTotal(Integer warnFileTotal) {
        this.warnFileTotal = warnFileTotal;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateUid() {
        return createUid;
    }

    public void setCreateUid(Integer createUid) {
        this.createUid = createUid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DlpCheckTask{" +
        ", id=" + id +
        ", modelId=" + modelId +
        ", name=" + name +
        ", datasourceId=" + datasourceId +
        ", filePath=" + filePath +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", taskStatus=" + taskStatus +
        ", fileTotal=" + fileTotal +
        ", checkTotal=" + checkTotal +
        ", warnFileTotal=" + warnFileTotal +
        ", detail=" + detail +
        ", createTime=" + createTime +
        ", createUid=" + createUid +
        "}";
    }
}
