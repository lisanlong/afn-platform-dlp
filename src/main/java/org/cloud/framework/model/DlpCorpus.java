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
 * @since 2019-07-25
 */
@TableName("dlp_corpus")
public class DlpCorpus extends Model<DlpCorpus> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 密级编号
     */
    private String cnum;

    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件大小
     */
    @TableField("file_size")
    private String fileSize;

    /**
     * 文件类型
     */
    @TableField("file_type")
    private String fileType;

    /**
     * 语料文件相对路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 文件正文
     */
    private String content;

    /**
     * 特征词
     */
    private String feature;

    /**
     * 0:未发布；1已发布
     */
    @TableField("is_published")
    private Integer isPublished;

    /**
     * 是否停用，0否1是
     */
    @TableField("is_stoped")
    private Integer isStoped;

    /**
     * 创建人
     */
    @TableField("create_uid")
    private Integer createUid;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnum() {
        return cnum;
    }

    public void setCnum(String cnum) {
        this.cnum = cnum;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public Integer getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Integer isPublished) {
        this.isPublished = isPublished;
    }

    public Integer getIsStoped() {
        return isStoped;
    }

    public void setIsStoped(Integer isStoped) {
        this.isStoped = isStoped;
    }

    public Integer getCreateUid() {
        return createUid;
    }

    public void setCreateUid(Integer createUid) {
        this.createUid = createUid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DlpCorpus{" +
        ", id=" + id +
        ", cnum=" + cnum +
        ", fileName=" + fileName +
        ", fileSize=" + fileSize +
        ", fileType=" + fileType +
        ", filePath=" + filePath +
        ", content=" + content +
        ", feature=" + feature +
        ", isPublished=" + isPublished +
        ", isStoped=" + isStoped +
        ", createUid=" + createUid +
        ", createTime=" + createTime +
        "}";
    }
}
