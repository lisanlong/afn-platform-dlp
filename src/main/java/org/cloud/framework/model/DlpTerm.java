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
@TableName("dlp_term")
public class DlpTerm extends Model<DlpTerm> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *  名称 
     */
    private String name;

    /**
     *  术语分类 
     */
    @TableField("termclass_id")
    private Integer termclassId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTermclassId() {
        return termclassId;
    }

    public void setTermclassId(Integer termclassId) {
        this.termclassId = termclassId;
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
        return "DlpTerm{" +
        ", id=" + id +
        ", name=" + name +
        ", termclassId=" + termclassId +
        ", isPublished=" + isPublished +
        ", isStoped=" + isStoped +
        ", createUid=" + createUid +
        ", createTime=" + createTime +
        "}";
    }
}
