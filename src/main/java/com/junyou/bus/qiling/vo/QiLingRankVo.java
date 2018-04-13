package com.junyou.bus.qiling.vo;

import java.io.Serializable;

import com.junyou.public_.rank.vo.BaseRank;
import com.junyou.public_.rank.vo.IQiLingRankVo;
import com.kernel.data.dao.IEntity;
public class QiLingRankVo extends BaseRank implements Serializable , IEntity,IQiLingRankVo{
	private static final long serialVersionUID = 1L;
	
	private Integer rank;
	
	private Long userRoleId;
	
	private Integer configId;
	
	private String name;

	private Integer qilingLevel;

	private String guildName;
	
	private Integer zplus;
	
	public Integer getZplus() {
		return zplus;
	}

	public void setZplus(Integer zplus) {
		this.zplus = zplus;
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

	
	public void setQiLingLevel(Integer qiLingLevel) {
		this.qilingLevel = qiLingLevel;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return  getUserRoleId();
	}

	
	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	@Override
	public IEntity copy() {
		QiLingRankVo copy=new QiLingRankVo();
		copy.setUserRoleId(getUserRoleId());
		copy.setRank(getRank());
		copy.setName(getName());
		copy.setConfigId(getConfigId());
		copy.setQiLingLevel(getQiLingLevel());
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
	public Integer getQiLingLevel() {
		return qilingLevel;
	}

	@Override
	public String getUncertain() {
		return  getQiLingLevel().toString();
	}
	
	@Override
	public Object[] getOutData() {
		return new Object[]{
				getConfigId(),
				getUserRoleId(),
				getName(),
				getRank(),
				getQiLingLevel(),
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
