package com.junyou.bus.platform.common.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_360_vplan")
public class Role360Vplan extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("tg_gift")
	private Integer tgGift;

	@Column("day_gift")
	private Integer dayGift;

	@Column("day_gift_time")
	private Long dayGiftTime;

	@Column("upgrade_gift")
	private Integer upgradeGift;

	@Column("consume_total_gold")
	private Integer consumeTotalGold;

	@Column("consume_end_time")
	private Long consumeEndTime;

	@Column("consume_gift")
	private Integer consumeGift;

	@Column("create_time")
	private Timestamp createTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getTgGift(){
		return tgGift;
	}

	public  void setTgGift(Integer tgGift){
		this.tgGift = tgGift;
	}

	public Integer getDayGift(){
		return dayGift;
	}

	public  void setDayGift(Integer dayGift){
		this.dayGift = dayGift;
	}

	public Long getDayGiftTime(){
		return dayGiftTime;
	}

	public  void setDayGiftTime(Long dayGiftTime){
		this.dayGiftTime = dayGiftTime;
	}

	public Integer getUpgradeGift(){
		return upgradeGift;
	}

	public  void setUpgradeGift(Integer upgradeGift){
		this.upgradeGift = upgradeGift;
	}

	public Integer getConsumeTotalGold(){
		return consumeTotalGold;
	}

	public  void setConsumeTotalGold(Integer consumeTotalGold){
		this.consumeTotalGold = consumeTotalGold;
	}

	public Long getConsumeEndTime(){
		return consumeEndTime;
	}

	public  void setConsumeEndTime(Long consumeEndTime){
		this.consumeEndTime = consumeEndTime;
	}

	public Integer getConsumeGift(){
		return consumeGift;
	}

	public  void setConsumeGift(Integer consumeGift){
		this.consumeGift = consumeGift;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public Role360Vplan copy(){
		Role360Vplan result = new Role360Vplan();
		result.setUserRoleId(getUserRoleId());
		result.setTgGift(getTgGift());
		result.setDayGift(getDayGift());
		result.setDayGiftTime(getDayGiftTime());
		result.setUpgradeGift(getUpgradeGift());
		result.setConsumeTotalGold(getConsumeTotalGold());
		result.setConsumeEndTime(getConsumeEndTime());
		result.setConsumeGift(getConsumeGift());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
