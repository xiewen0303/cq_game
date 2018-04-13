package com.junyou.bus.platform.qq.entity;
import java.io.Serializable;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_platform_qq_hz")
public class RolePlatformQqHz extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("xinshou_gift")
	private Integer xinshouGift;

	@Column("meiri_gift_update_time")
	private Long meiriGiftUpdateTime;

	@Column("chengzhang_picked_gift")
	private String chengzhangPickedGift;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getXinshouGift(){
		return xinshouGift;
	}

	public  void setXinshouGift(Integer xinshouGift){
		this.xinshouGift = xinshouGift;
	}

	public Long getMeiriGiftUpdateTime() {
		return meiriGiftUpdateTime;
	}

	public void setMeiriGiftUpdateTime(Long meiriGiftUpdateTime) {
		this.meiriGiftUpdateTime = meiriGiftUpdateTime;
	}

	public String getChengzhangPickedGift(){
		return chengzhangPickedGift;
	}

	public  void setChengzhangPickedGift(String chengzhangPickedGift){
		this.chengzhangPickedGift = chengzhangPickedGift;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RolePlatformQqHz copy(){
		RolePlatformQqHz result = new RolePlatformQqHz();
		result.setUserRoleId(getUserRoleId());
		result.setXinshouGift(getXinshouGift());
		result.setMeiriGiftUpdateTime(getMeiriGiftUpdateTime());
		result.setChengzhangPickedGift(getChengzhangPickedGift());
		return result;
	}
}
