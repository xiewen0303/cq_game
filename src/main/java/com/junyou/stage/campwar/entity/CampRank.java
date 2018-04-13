package com.junyou.stage.campwar.entity;

import java.io.Serializable;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

/**
 * 阵营排行
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-4-7 下午2:23:38 
 */
public class CampRank extends AbsVersion implements Serializable,IEntity {

	private static final long serialVersionUID = 1L;
	private Long userRoleId;
	private String name;
	private Integer orderNum;
	private int jifen;
	private int camp;
	private Long updateTime;
	
	public Long getUpdateTime() {
		return updateTime == null ? 0 : updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public int getCamp() {
		return camp;
	}

	public void setCamp(int camp) {
		this.camp = camp;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public int getJifen() {
		return jifen;
	}

	public void setJifen(int jifen) {
		this.jifen = jifen;
	}
	
	public Integer[] getOutData(){
		return new Integer[]{
				getJifen()
				,getOrderNum()};
	}

	@Override
	public String getPirmaryKeyName() {
		return null;
	}

	@Override
	public Long getPrimaryKeyValue() {
		return null;
	}

	@Override
	public IEntity copy() {
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
