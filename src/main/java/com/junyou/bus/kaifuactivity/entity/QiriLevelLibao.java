package com.junyou.bus.kaifuactivity.entity;

import com.kernel.data.dao.IEntity;
import com.kernel.data.dao.AbsVersion;
import java.io.Serializable;
import java.sql.Timestamp;

public class QiriLevelLibao extends AbsVersion implements Serializable,IEntity {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Long userRoleId;
	
	private Integer userLevel;
	
	private Integer levelStatus;
	
	private Timestamp createTime;
	
	private Timestamp updateTime;
	
	private Integer subId;
	
	public Integer getSubId() {
		return subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public Integer getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	public Integer getLevelStatus() {
		return levelStatus;
	}

	public void setLevelStatus(Integer levelStatus) {
		this.levelStatus = levelStatus;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	@Override	
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return getId();
	}
	
	public QiriLevelLibao copy(){
		QiriLevelLibao result = new QiriLevelLibao();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setUserLevel(getUserLevel());
		result.setLevelStatus(getLevelStatus());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		result.setSubId(getSubId());
		return result;
	}


}
