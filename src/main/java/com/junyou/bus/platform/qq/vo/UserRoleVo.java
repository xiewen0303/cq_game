package com.junyou.bus.platform.qq.vo;

import java.io.Serializable;

import com.kernel.data.dao.IEntity;

/**
 * 
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-2-28 下午4:56:57
 */
public class UserRoleVo implements Serializable,IEntity{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Integer configId;
	
	private String name;
	
	private Integer level; 
	
	private Long lastOfflineTime; //最后在线时间
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
 

	public Long getLastOfflineTime() {
		return lastOfflineTime;
	}

	public void setLastOfflineTime(Long lastOfflineTime) {
		this.lastOfflineTime = lastOfflineTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	@Override
	public IEntity copy() {
		UserRoleVo vo = new UserRoleVo();
		vo.setId(getId());
		vo.setName(getName());
		vo.setLevel(getLevel()); 
		vo.setLastOfflineTime(getLastOfflineTime());
		return vo;
	}

}
