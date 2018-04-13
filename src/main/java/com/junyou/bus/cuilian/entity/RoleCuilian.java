package com.junyou.bus.cuilian.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_cuilian")
public class RoleCuilian extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("money_times")
	private Integer moneyTimes;

	@Column("last_money_time")
	private Long lastMoneyTime;

	@Column("bgold_times")
	private Integer bgoldTimes;

	@Column("last_bgold_time")
	private Long lastBgoldTime;

	@Column("gold_times")
	private Integer goldTimes;

	@Column("last_gold_time")
	private Long lastGoldTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getMoneyTimes(){
		return moneyTimes;
	}

	public  void setMoneyTimes(Integer moneyTimes){
		this.moneyTimes = moneyTimes;
	}

	public Long getLastMoneyTime(){
		return lastMoneyTime;
	}

	public  void setLastMoneyTime(Long lastMoneyTime){
		this.lastMoneyTime = lastMoneyTime;
	}

	public Integer getBgoldTimes(){
		return bgoldTimes;
	}

	public  void setBgoldTimes(Integer bgoldTimes){
		this.bgoldTimes = bgoldTimes;
	}

	public Long getLastBgoldTime(){
		return lastBgoldTime;
	}

	public  void setLastBgoldTime(Long lastBgoldTime){
		this.lastBgoldTime = lastBgoldTime;
	}

	public Integer getGoldTimes(){
		return goldTimes;
	}

	public  void setGoldTimes(Integer goldTimes){
		this.goldTimes = goldTimes;
	}

	public Long getLastGoldTime(){
		return lastGoldTime;
	}

	public  void setLastGoldTime(Long lastGoldTime){
		this.lastGoldTime = lastGoldTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleCuilian copy(){
		RoleCuilian result = new RoleCuilian();
		result.setUserRoleId(getUserRoleId());
		result.setMoneyTimes(getMoneyTimes());
		result.setLastMoneyTime(getLastMoneyTime());
		result.setBgoldTimes(getBgoldTimes());
		result.setLastBgoldTime(getLastBgoldTime());
		result.setGoldTimes(getGoldTimes());
		result.setLastGoldTime(getLastGoldTime());
		return result;
	}
}
