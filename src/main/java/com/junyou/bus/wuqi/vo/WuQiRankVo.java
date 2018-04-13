package com.junyou.bus.wuqi.vo;

import java.io.Serializable;

import com.junyou.public_.rank.vo.BaseRank;
import com.junyou.public_.rank.vo.IWuqiRankVo;
import com.kernel.data.dao.IEntity;

public class WuQiRankVo extends BaseRank implements Serializable , IEntity,IWuqiRankVo{
	private static final long serialVersionUID = 1L;
	
	private Integer rank;
	
	private Long userRoleId;
	
	private Integer configId;
	
	private String name;

	private Integer wuqiLevel;

	private Integer level;
	
	private Long zplus;
	
	private String guildName;
	
	public Long getZplus() {
		return zplus;
	}

	public void setZplus(Long zplus) {
		this.zplus = zplus;
	}

	public Integer getRank() {
		return rank;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
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



	public void setZuoqiLevel(Integer wuqiLevel) {
		this.wuqiLevel = wuqiLevel;
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
		WuQiRankVo copy=new WuQiRankVo();
		copy.setUserRoleId(getUserRoleId());
		copy.setRank(getRank());
		copy.setName(getName());
		copy.setConfigId(getConfigId());
		copy.setZuoqiLevel(getZuoqiLevel());
		copy.setLevel(getLevel());
		copy.setGuildName(getGuildName());
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
	public Integer getZuoqiLevel() {
		return wuqiLevel;
	}

	@Override
	public String getUncertain() {
		return  getZuoqiLevel().toString();
	}
	
	@Override
	public Object[] getOutData() {
		return new Object[]{
				getConfigId(),
				getUserRoleId(),
				getName(),
				getRank(),
				getZuoqiLevel(),
				getZplus(),//战斗力
				getUncertain(),
				null,
				getGuildName(),
				getVipLevel()
		};
	}


	@Override
	public String getUserGuildName() {
		return null;
	}

}
