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
@TableName("dlp_classify")
public class DlpClassify extends Model<DlpClassify> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *  密级名称 
     */
    private String name;

    /**
     * 优先级(1开始，数字越小优先级越高)
     */
    @TableField("use_level")
    private Integer useLevel;

    /**
     *  密级编号 
     */
    private String cnum;

    /**
     *  说明
     */
    private String detail;

    /**
     * 是否停用，0否1是
     */
    @TableField("is_stoped")
    private Integer isStoped;

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

    public Integer getUseLevel() {
        return useLevel;
    }

    public void setUseLevel(Integer useLevel) {
        this.useLevel = useLevel;
    }

    public String getCnum() {
        return cnum;
    }

    public void setCnum(String cnum) {
        this.cnum = cnum;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getIsStoped() {
        return isStoped;
    }

    public void setIsStoped(Integer isStoped) {
        this.isStoped = isStoped;
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
        return "DlpClassify{" +
        ", id=" + id +
        ", name=" + name +
        ", useLevel=" + useLevel +
        ", cnum=" + cnum +
        ", detail=" + detail +
        ", isStoped=" + isStoped +
        ", createTime=" + createTime +
        "}";
    }
}
