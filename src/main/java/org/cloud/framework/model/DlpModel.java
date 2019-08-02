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
@TableName("dlp_model")
public class DlpModel extends Model<DlpModel> {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 模型名称
	 */
	private String name;

	/**
	 * 预测分类类别（密级范围）
	 */
	private String labels;

	/**
	 * 版本号
	 */
	private String version;

	/**
	 * 处理进度（0：未处理；1：文本解析2：模型训练3：训练结束）
	 */
	@TableField("model_status")
	private Integer modelStatus;

	/**
	 * 训练开始时间
	 */
	@TableField("start_time")
	private String startTime;

	/**
	 * 训练结束时间
	 */
	@TableField("end_time")
	private String endTime;

	/**
	 * 模型说明
	 */
	private String detail;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getModelStatus() {
		return modelStatus;
	}

	public void setModelStatus(Integer modelStatus) {
		this.modelStatus = modelStatus;
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
		return "DlpModel{" + ", id=" + id + ", name=" + name + ", labels=" + labels + ", version=" + version
				+ ", modelStatus=" + modelStatus + ", startTime=" + startTime + ", endTime=" + endTime + ", detail="
				+ detail + ", isStoped=" + isStoped + ", createUid=" + createUid + ", createTime=" + createTime + "}";
	}
}
