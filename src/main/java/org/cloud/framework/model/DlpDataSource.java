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
 * @since 2019-07-09
 */
@TableName("dlp_data_source")
public class DlpDataSource extends Model<DlpDataSource> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *  数据源名称 
     */
    private String name;

    /**
     *  数据源描述 
     */
    private String detail;

    /**
     *  excel/mysql/oracle/sqlServer/xml/zip/FTP 
     */
    @TableField("source_type")
    private Integer sourceType;

    /**
     *  链接地址 
     */
    private String url;

    /**
     *  用户名 
     */
    private String user;

    /**
     *  密码 
     */
    private String password;

    /**
     * 是否停用，0否1是
     */
    @TableField("is_stoped")
    private Integer isStoped;

    /**
     *  编码 
     */
    private String coding;

	/**
	 * 创建时间
	 * @return
	 *
	 */
	@TableField("create_time")
	private String createTime;

	/**
	 * 创建人
	 * @return
	 */
	@TableField("create_uid")
	private Integer createUid;

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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIsStoped() {
        return isStoped;
    }

    public void setIsStoped(Integer isStoped) {
        this.isStoped = isStoped;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

	@Override
	public String toString() {
		return "DlpDataSource{" +
				"id=" + id +
				", name='" + name + '\'' +
				", detail='" + detail + '\'' +
				", sourceType='" + sourceType + '\'' +
				", url='" + url + '\'' +
				", user='" + user + '\'' +
				", password='" + password + '\'' +
				", isStoped=" + isStoped +
				", coding='" + coding + '\'' +
				", createTime='" + createTime + '\'' +
				", createUid=" + createUid +
				'}';
	}
}
