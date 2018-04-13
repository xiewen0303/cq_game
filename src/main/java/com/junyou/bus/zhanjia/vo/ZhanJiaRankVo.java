package com.junyou.bus.zhanjia.vo;

import java.io.Serializable;

import com.junyou.public_.rank.vo.BaseRank;
import com.junyou.public_.rank.vo.IZhanJiaRankVo;
import com.kernel.data.dao.IEntity;
public class ZhanJiaRankVo extends BaseRank implements Serializable , IEntity,IZhanJiaRankVo{
	private static final long serialVersionUID = 1L;
	
	private Integer rank;
	
	private Long userRoleId;
	
	private Integer configId;
	
	private String name;

	private Integer xianjianLevel;

	private Integer level;
	
	private Integer zplus;
	
	private String guildName;
	
	public Integer getZplus() {
		return zplus;
	}

	public void setZplus(Integer zplus) {
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

	public Integer getXianjianLevel() {
		return xianjianLevel;
	}

	public void setXianjianLevel(Integer xianjianLevel) {
		this.xianjianLevel = xianjianLevel;
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
		ZhanJiaRankVo copy=new ZhanJiaRankVo();
		copy.setUserRoleId(getUserRoleId());
		copy.setRank(getRank());
		copy.setName(getName());
		copy.setConfigId(getConfigId());
		copy.setXianjianLevel(getXianjianLevel());
		copy.setLevel(getLevel());
		copy.setGuildName(getGuildName());
		return copy;
	}

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
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
		return  getXianjianLevel().toString();
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

}
