package com.junyou.bus.rolebusiness.vo;

import java.io.Serializable;

import com.junyou.public_.rank.vo.BaseRank;
import com.junyou.public_.rank.vo.IFightingRankVo;
import com.kernel.data.dao.IEntity;
public class FightingRankVo extends BaseRank implements Serializable,IEntity, IFightingRankVo{

	private static final long serialVersionUID = 1L;
	
	private Integer rank;
	
	private Long userRoleId;
	
	private Integer configId;
	
	private String name;

	private Integer fighting;
	
	private String  userGuildName;
	
	private Integer level;
	
	private Integer chibang_level;
	
	private Integer wiQiLevel;
	
	public Integer getWiQiLevel() {
		return wiQiLevel;
	}

	public void setWiQiLevel(Integer wiQiLevel) {
		this.wiQiLevel = wiQiLevel;
	}

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
	
	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setUserGuildName(String userGuildName) {
		this.userGuildName = userGuildName;
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

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}
 
	public void setFighting(Integer fighting) {
		this.fighting = fighting;
	}

	@Override
	public Long getUserRoleId() {
		return userRoleId;
	}

	@Override
	public Integer getRank() {
		return rank;
	}

	@Override
	public Integer getConfigId() {
		return configId;
	}

	@Override
	public Integer getFighting() {
		return fighting;
	}

	@Override
	public String getPirmaryKeyName() {
		
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return getUserRoleId();
	}

	@Override
	public IEntity copy() {
		FightingRankVo copy=new FightingRankVo();
		copy.setUserRoleId(getUserRoleId());
		copy.setRank(getRank());
		copy.setName(getName());
		copy.setConfigId(getConfigId());
		copy.setFighting(getFighting());
		return copy;
	}

	@Override
	public String getUncertain() {
		return  getFighting().toString();
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
				getWiQiLevel()
		};
	}

	@Override
	public String getUserGuildName() {
		return userGuildName;
	}

	@Override
	public Integer getLevel() {
		return level;
	}

}
