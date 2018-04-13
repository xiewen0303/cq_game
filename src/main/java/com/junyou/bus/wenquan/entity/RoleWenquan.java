package com.junyou.bus.wenquan.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_wenquan")
public class RoleWenquan extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("gold_times")
	private Integer goldTimes;

	@Column("gold_update_time")
	private Long goldUpdateTime;

	@Column("money_times")
	private Integer moneyTimes;

	@Column("money_update_time")
	private Long moneyUpdateTime;
	
	@Column("play_times")
	private Integer playTimes;
	
	@Column("play_update_time")
	private Long playUpdateTime;
	
	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getGoldTimes(){
		return goldTimes;
	}

	public  void setGoldTimes(Integer goldTimes){
		this.goldTimes = goldTimes;
	}

	public Long getGoldUpdateTime(){
		return goldUpdateTime;
	}

	public  void setGoldUpdateTime(Long goldUpdateTime){
		this.goldUpdateTime = goldUpdateTime;
	}

	public Integer getMoneyTimes(){
		return moneyTimes;
	}

	public  void setMoneyTimes(Integer moneyTimes){
		this.moneyTimes = moneyTimes;
	}

	public Long getMoneyUpdateTime(){
		return moneyUpdateTime;
	}

	public  void setMoneyUpdateTime(Long moneyUpdateTime){
		this.moneyUpdateTime = moneyUpdateTime;
	}

	public Integer getPlayTimes() {
		return playTimes;
	}

	public void setPlayTimes(Integer playTimes) {
		this.playTimes = playTimes;
	}

	public Long getPlayUpdateTime() {
		return playUpdateTime;
	}

	public void setPlayUpdateTime(Long playUpdateTime) {
		this.playUpdateTime = playUpdateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleWenquan copy(){
		RoleWenquan result = new RoleWenquan();
		result.setUserRoleId(getUserRoleId());
		result.setGoldTimes(getGoldTimes());
		result.setGoldUpdateTime(getGoldUpdateTime());
		result.setMoneyTimes(getMoneyTimes());
		result.setMoneyUpdateTime(getMoneyUpdateTime());
		result.setPlayTimes(getPlayTimes());
		result.setPlayUpdateTime(getPlayUpdateTime());
		return result;
	}
}
