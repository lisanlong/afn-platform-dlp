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
@TableName("dlp_cword")
public class DlpCword extends Model<DlpCword> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *  密级词 
     */
    private String word;

    /**
     *  密级编号 
     */
    private String cnum;

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
     *  创建人 
     */
    @TableField("create_uid")
    private Integer createUid;

    /**
     *  创建时间 
     */
    @TableField("create_time")
    private String createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getCnum() {
        return cnum;
    }

    public void setCnum(String cnum) {
        this.cnum = cnum;
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
        return "DlpCword{" +
        ", id=" + id +
        ", word=" + word +
        ", cnum=" + cnum +
        ", isPublished=" + isPublished +
        ", isStoped=" + isStoped +
        ", createUid=" + createUid +
        ", createTime=" + createTime +
        "}";
    }
}
