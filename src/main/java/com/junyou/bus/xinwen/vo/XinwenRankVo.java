package com.junyou.bus.xinwen.vo;

import java.io.Serializable;

import com.junyou.public_.rank.vo.BaseRank;
import com.junyou.public_.rank.vo.IXinwenRankVo;
import com.kernel.data.dao.IEntity;
public class XinwenRankVo extends BaseRank implements Serializable , IEntity,IXinwenRankVo{
	private static final long serialVersionUID = 1L;
	
	private Integer rank;
	
	private Long userRoleId;

	private Integer configId;
	
	private String name;

	private Integer xinwenLevel;

	private Integer cengLevel;
	
	private Integer level;
	
	private Integer zplus;
	
	private int zhanjiaLevel;

	private int zhanjiaZplus;
	
	private String guildName;
	
	public int getZhanjiaLevel() {
		return zhanjiaLevel;
	}
	public void setZhanjiaLevel(int zhanjiaLevel) {
		this.zhanjiaLevel = zhanjiaLevel;
	}
	public int getZhanjiaZplus() {
		return zhanjiaZplus;
	}
	public void setZhanjiaZplus(int zhanjiaZplus) {
		this.zhanjiaZplus = zhanjiaZplus;
	}
	
	public Integer getZplus() {
		return zplus;
	}
	 
	public Integer getXinwenLevel() {
		return xinwenLevel;
	}
	public void setXinwenLevel(Integer xinwenLevel) {
		this.xinwenLevel = xinwenLevel;
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
		XinwenRankVo copy=new XinwenRankVo();
		copy.setUserRoleId(getUserRoleId());
		copy.setRank(getRank());
		copy.setName(getName());
		copy.setConfigId(getConfigId());
		copy.setXinwenLevel(getTangBaoXinwenLevel());
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
	public String getUncertain() {
		return  new StringBuffer()
		  		.append(getTangBaoXinwenLevel().toString())
		  		.append(",")
		  		.append(getCengLevel())
		  		.append(",")
		  		.append(getZhanjiaLevel())
		  		.append(",")
		  		.append(getZhanjiaZplus())
				.toString() ;//     getTangBaoXinwenLevel().toString()+","+getCengLevel();
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
	@Override
	public Integer getTangBaoXinwenLevel() {
		return xinwenLevel;
	}

}
