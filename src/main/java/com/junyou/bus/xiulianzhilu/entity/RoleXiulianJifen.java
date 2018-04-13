package com.junyou.bus.xiulianzhilu.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_xiulian_jifen")
public class RoleXiulianJifen extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;
	@Column("can_use_jifen")
	private Integer canUseJifen;
	@Column("all_jifen")
	private Integer allJifen;
	@Column("jackpot_level")
	private Integer jackpotLevel;//当前奖池等级
	@Column("exchange_id")
	private String exchangeId;//已兑换ID
	@Column("day_status")
	private String dayStatus;//每日任务领取状态
	@Column("create_time")
	private Long createTime;
	@Column("update_time")
	private Long updateTime;
	@Column("chenghao_status")
	private Integer chenghaoStatus;//称号状态



	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getCanUseJifen() {
		return canUseJifen;
	}

	public void setCanUseJifen(Integer canUseJifen) {
		this.canUseJifen = canUseJifen;
	}

	public Integer getAllJifen() {
		return allJifen;
	}

	public void setAllJifen(Integer allJifen) {
		this.allJifen = allJifen;
	}

	public Integer getJackpotLevel() {
		return jackpotLevel;
	}

	public void setJackpotLevel(Integer jackpotLevel) {
		this.jackpotLevel = jackpotLevel;
	}

	public String getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}

	public String getDayStatus() {
		return dayStatus;
	}

	public void setDayStatus(String dayStatus) {
		this.dayStatus = dayStatus;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public Integer getChenghaoStatus() {
		return chenghaoStatus;
	}

	public void setChenghaoStatus(Integer chenghaoStatus) {
		this.chenghaoStatus = chenghaoStatus;
	}

	public RoleXiulianJifen copy(){
		RoleXiulianJifen result = new RoleXiulianJifen();
		result.setUserRoleId(getUserRoleId());
		result.setAllJifen(getAllJifen());
		result.setCanUseJifen(getCanUseJifen());
		result.setCreateTime(getCreateTime());
		result.setDayStatus(getDayStatus());
		result.setExchangeId(getExchangeId());
		result.setJackpotLevel(getJackpotLevel());
		result.setUpdateTime(getUpdateTime());
		result.setChenghaoStatus(getChenghaoStatus());
		return result;
	}
}
