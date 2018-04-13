package com.junyou.bus.platform.common.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_37_vplan")
public class Role37Vplan extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("day_gift_state")
	private Integer dayGiftState;

	@Column("day_gift_uptime")
	private Long dayGiftUptime;

	@Column("level_gift")
	private Integer levelGift;

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

	public Integer getDayGiftState(){
		return dayGiftState;
	}

	public  void setDayGiftState(Integer dayGiftState){
		this.dayGiftState = dayGiftState;
	}

	public Long getDayGiftUptime(){
		return dayGiftUptime;
	}

	public  void setDayGiftUptime(Long dayGiftUptime){
		this.dayGiftUptime = dayGiftUptime;
	}

	public Integer getLevelGift(){
		return levelGift;
	}

	public  void setLevelGift(Integer levelGift){
		this.levelGift = levelGift;
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

	public Role37Vplan copy(){
		Role37Vplan result = new Role37Vplan();
		result.setUserRoleId(getUserRoleId());
		result.setDayGiftState(getDayGiftState());
		result.setDayGiftUptime(getDayGiftUptime());
		result.setLevelGift(getLevelGift());
		result.setConsumeTotalGold(getConsumeTotalGold());
		result.setConsumeEndTime(getConsumeEndTime());
		result.setConsumeGift(getConsumeGift());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
