package com.junyou.bus.yaoshen.vo;

import java.io.Serializable;

import com.junyou.public_.rank.vo.BaseRank;
import com.junyou.public_.rank.vo.IYaoShenHunpoRankVo;
import com.kernel.data.dao.IEntity;
public class YaoShenHunpoRankVo extends BaseRank implements Serializable , IEntity,IYaoShenHunpoRankVo{
	private static final long serialVersionUID = 1L;
	
	private Integer rank;
	
	private Long userRoleId;

	private Integer configId;
	
	private String name;

	private Integer yaoshenLevel;

	private Integer cengLevel;
	
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



	public void setYaoshenLevel(Integer yaoshenLevel) {
		this.yaoshenLevel = yaoshenLevel;
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
		YaoShenHunpoRankVo copy=new YaoShenHunpoRankVo();
		copy.setUserRoleId(getUserRoleId());
		copy.setRank(getRank());
		copy.setName(getName());
		copy.setConfigId(getConfigId());
		copy.setYaoshenLevel(getYaoshenLevel());
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
	public Integer getYaoshenLevel() {
		return yaoshenLevel;
	}

	@Override
	public String getUncertain() {
		return  getYaoshenLevel().toString()+","+getCengLevel();
	}
	
	@Override
	public Object[] getOutData() {
		return new Object[]{
				getConfigId(),
				getUserRoleId(),
				getName(),
				getRank(),
				getLevel(),
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
	
	public Integer getCengLevel() {
		return cengLevel;
	}

	public void setCengLevel(Integer cengLevel) {
		this.cengLevel = cengLevel;
	}

}
