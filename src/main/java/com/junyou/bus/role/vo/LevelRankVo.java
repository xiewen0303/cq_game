package com.junyou.bus.role.vo;

import java.io.Serializable;

import com.junyou.public_.rank.vo.BaseRank;
import com.junyou.public_.rank.vo.ILevelRankVo;
import com.kernel.data.dao.IEntity;

/**
 * 为等级榜服务的VO
 * */
public class LevelRankVo extends BaseRank implements Serializable,IEntity,ILevelRankVo{

	private static final long serialVersionUID = 1L;
	
	private Integer rank;
	
	private Long userRoleId;
	
	private Integer configId;
	
	private String name;

	private Integer level;
	
	private Integer fighting;
	
	private String  userGuildName;
	
	private Integer chibang_level;
	
	private Integer wuQiLevel;
	
	public Integer getChibang_level() {
		return chibang_level;
	}

	public void setChibang_level(Integer chibang_level) {
		this.chibang_level = chibang_level;
	}

	public Integer getZuoqi_level() {
		return zuoqi_level;
	}

	public void setZuoqi_level(Integer zuoqi_level) {
		this.zuoqi_level = zuoqi_level;
	}

	private Integer zuoqi_level;

	public void setFighting(Integer fighting) {
		this.fighting = fighting;
	}

	public void setUserGuildName(String userGuildName) {
		this.userGuildName = userGuildName;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
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

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return  getUserRoleId();
	}

	@Override
	public IEntity copy() {
		LevelRankVo copy=new LevelRankVo();
		copy.setUserRoleId(getUserRoleId());
		copy.setRank(getRank());
		copy.setName(getName());
		copy.setConfigId(getConfigId());
		copy.setLevel(getLevel());
		return copy;
	}

	@Override
	public Long getUserRoleId() {
		return userRoleId;
	}

	@Override
	public Integer getConfigId() {
		return configId;
	}

	@Override
	public String getUncertain() {
		
		return  getLevel().toString();
	}

	@Override
	public Object[] getOutData() {
		return new Object[]{
				getConfigId(),
				getUserRoleId(),
				getName(),
				getRank(),
				getUserGuildName(),
				getLevel(),
				getFighting(),
				getZuoqi_level(),
				getChibang_level(),
				null,
				getVipLevel(),
				getWuQiLevel()
		};
	}

	public Object getWuQiLevel() {
		return wuQiLevel;
	}
	
	public void setWuQiLevel(Integer wuQiLevel) {
		this.wuQiLevel = wuQiLevel;
	}

	@Override
	public String getUserGuildName() {
		return userGuildName;
	}

	@Override
	public Integer getFighting() {
		return fighting;
	}
}
