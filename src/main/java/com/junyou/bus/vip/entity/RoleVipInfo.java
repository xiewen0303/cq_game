package com.junyou.bus.vip.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_vip_info")
public class RoleVipInfo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("vip_exp")
	private Long vipExp;

	@Column("vip_level")
	private Integer vipLevel;

	@Column("week_gift")
	private Integer weekGift;

	@Column("level_gift")
	private Integer levelGift;

	@Column("expire_time")
	private Long expireTime;

	@Column("next_week_time")
	private Long nextWeekTime;
	
	@Column("vip_id")
	private Integer vipId;
	


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Long getVipExp(){
		return vipExp;
	}

	public  void setVipExp(Long vipExp){
		this.vipExp = vipExp;
	}

	public Integer getVipLevel(){
		return vipLevel;
	}

	public  void setVipLevel(Integer vipLevel){
		this.vipLevel = vipLevel;
	}

	public Integer getWeekGift(){
		return weekGift;
	}

	public  void setWeekGift(Integer weekGift){
		this.weekGift = weekGift;
	}

	public Integer getLevelGift(){
		return levelGift;
	}

	public  void setLevelGift(Integer levelGift){
		this.levelGift = levelGift;
	}

	public Long getExpireTime(){
		return expireTime;
	}

	public  void setExpireTime(Long expireTime){
		this.expireTime = expireTime;
	}

	public Long getNextWeekTime(){
		return nextWeekTime;
	}

	public  void setNextWeekTime(Long nextWeekTime){
		this.nextWeekTime = nextWeekTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public Integer getVipId() {
		if(vipId == null){
			vipId = 0;
		}
		return vipId;
	}

	public void setVipId(Integer vipId) {
		this.vipId = vipId;
	}

	public RoleVipInfo copy(){
		RoleVipInfo result = new RoleVipInfo();
		result.setUserRoleId(getUserRoleId());
		result.setVipExp(getVipExp());
		result.setVipLevel(getVipLevel());
		result.setWeekGift(getWeekGift());
		result.setLevelGift(getLevelGift());
		result.setExpireTime(getExpireTime());
		result.setNextWeekTime(getNextWeekTime());
		result.setVipId(getVipId());
		return result;
	}
}
