package com.junyou.bus.kaifuactivity.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

public class QiriLevel extends AbsVersion implements Serializable,IEntity {

	private static final long serialVersionUID = 1L;
		
	private Long id;
	
	private Integer configId;
	
	private Integer maxNumber;
	
	private Integer lingquNumber;
	
	private Timestamp createTime;
	
	private Integer subId;
	
	public Integer getSubId() {
		return subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Integer getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}
	public Integer getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(Integer maxNumber) {
		this.maxNumber = maxNumber;
	}
	public Integer getLingquNumber() {
		return lingquNumber;
	}

	public void setLingquNumber(Integer lingquNumber) {
		this.lingquNumber = lingquNumber;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Override	
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return getId();
	}
	
	public QiriLevel copy(){
		QiriLevel result = new QiriLevel();
		result.setId(getId());
		result.setConfigId(getConfigId());
		result.setMaxNumber(getMaxNumber());
		result.setLingquNumber(getLingquNumber());
		result.setCreateTime(getCreateTime());
		result.setSubId(getSubId());
		return result;
	}


}
