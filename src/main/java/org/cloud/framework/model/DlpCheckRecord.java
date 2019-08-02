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
 * @since 2019-07-19
 */
@TableName("dlp_check_record")
public class DlpCheckRecord extends Model<DlpCheckRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *  文件第三方ID 
     */
    @TableField("file_id")
    private String fileId;

    /**
     *  文件条码ID 
     */
    @TableField("file_bar_code")
    private String fileBarCode;

    /**
     *  文件名称 
     */
    @TableField("file_name")
    private String fileName;

    /**
     *  文件来源类型，1刻录，2打印 
     */
    @TableField("file_source")
    private Integer fileSource;

    /**
     *  文件类型 
     */
    @TableField("file_type")
    private Integer fileType;

    /**
     *  文件大小 
     */
    @TableField("file_size")
    private String fileSize;

    /**
     *  文件在第三方系统存放位置 
     */
    @TableField("file_disk_path")
    private String fileDiskPath;

    /**
     *  原密级 
     */
    @TableField("origin_label")
    private String originLabel;

    /**
     *  文件正文 
     */
    private String content;

    /**
     *  特征词 
     */
    private String feature;

    /**
     *  预测方式，0模型预测；1触发词匹配 
     */
    @TableField("predict_type")
    private Integer predictType;

    /**
     *  密级触发词 
     */
    private String cwords;

    /**
     *  文件提交时间 
     */
    @TableField("create_time")
    private String createTime;

    /**
     *  文件提交人 
     */
    @TableField("create_uid")
    private Integer createUid;

    /**
     *  文件提交人部门 
     */
    @TableField("dpt_id")
    private String dptId;

    /**
     *  任务ID 
     */
    @TableField("task_id")
    private Integer taskId;

    /**
     *  检查时间 
     */
    @TableField("check_time")
    private String checkTime;

    /**
     *  检查状态，0尚未检查，1正常，2预警 3，未知
     */
    @TableField("check_status")
    private Integer checkStatus;

    /**
     *  预测密级 
     */
    @TableField("check_label")
    private String checkLabel;

    /**
     *  检查结果说明 
     */
    @TableField("check_detail")
    private String checkDetail;

    /**
     * 人工标注结果
     */
    @TableField("review_label")
    private String reviewLabel;

    /**
     *  人工校核，0尚未查实，1查实取消，2查实确认 
     */
    @TableField("review_status")
    private Integer reviewStatus;

    /**
     *  审核人ID 
     */
    @TableField("review_uid")
    private Integer reviewUid;

    /**
     *  审核时间 
     */
    @TableField("review_time")
    private String reviewTime;

    /**
     *  审核结果说明 
     */
    @TableField("review_detail")
    private String reviewDetail;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileBarCode() {
        return fileBarCode;
    }

    public void setFileBarCode(String fileBarCode) {
        this.fileBarCode = fileBarCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileSource() {
        return fileSource;
    }

    public void setFileSource(Integer fileSource) {
        this.fileSource = fileSource;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileDiskPath() {
        return fileDiskPath;
    }

    public void setFileDiskPath(String fileDiskPath) {
        this.fileDiskPath = fileDiskPath;
    }

    public String getOriginLabel() {
        return originLabel;
    }

    public void setOriginLabel(String originLabel) {
        this.originLabel = originLabel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Integer getPredictType() {
        return predictType;
    }

    public void setPredictType(Integer predictType) {
        this.predictType = predictType;
    }

    public String getCwords() {
        return cwords;
    }

    public void setCwords(String cwords) {
        this.cwords = cwords;
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

    public String getDptId() {
        return dptId;
    }

    public void setDptId(String dptId) {
        this.dptId = dptId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckLabel() {
        return checkLabel;
    }

    public void setCheckLabel(String checkLabel) {
        this.checkLabel = checkLabel;
    }

    public String getCheckDetail() {
        return checkDetail;
    }

    public void setCheckDetail(String checkDetail) {
        this.checkDetail = checkDetail;
    }

    public String getReviewLabel() {
        return reviewLabel;
    }

    public void setReviewLabel(String reviewLabel) {
        this.reviewLabel = reviewLabel;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Integer getReviewUid() {
        return reviewUid;
    }

    public void setReviewUid(Integer reviewUid) {
        this.reviewUid = reviewUid;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getReviewDetail() {
        return reviewDetail;
    }

    public void setReviewDetail(String reviewDetail) {
        this.reviewDetail = reviewDetail;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DlpCheckRecord{" +
        ", id=" + id +
        ", fileId=" + fileId +
        ", fileBarCode=" + fileBarCode +
        ", fileName=" + fileName +
        ", fileSource=" + fileSource +
        ", fileType=" + fileType +
        ", fileSize=" + fileSize +
        ", fileDiskPath=" + fileDiskPath +
        ", originLabel=" + originLabel +
        ", content=" + content +
        ", feature=" + feature +
        ", predictType=" + predictType +
        ", cwords=" + cwords +
        ", createTime=" + createTime +
        ", createUid=" + createUid +
        ", dptId=" + dptId +
        ", taskId=" + taskId +
        ", checkTime=" + checkTime +
        ", checkStatus=" + checkStatus +
        ", checkLabel=" + checkLabel +
        ", checkDetail=" + checkDetail +
        ", reviewLabel=" + reviewLabel +
        ", reviewStatus=" + reviewStatus +
        ", reviewUid=" + reviewUid +
        ", reviewTime=" + reviewTime +
        ", reviewDetail=" + reviewDetail +
        "}";
    }
}
