package com.junyou.bus.platform.common.entity;
import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_platform_info")
public class RolePlatformInfo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("update_time")
	private Long updateTime;

	@Column("platform_id")
	private String platformId;

	@Column("gift_state")
	private Integer giftState;

	@Column("gift_state_standby")
	private Integer giftStateStandby;

	@Column("create_time")
	private Timestamp createTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public String getPlatformId(){
		return platformId;
	}

	public  void setPlatformId(String platformId){
		this.platformId = platformId;
	}

	public Integer getGiftState(){
		return giftState;
	}

	public  void setGiftState(Integer giftState){
		this.giftState = giftState;
	}

	public Integer getGiftStateStandby(){
		return giftStateStandby;
	}

	public  void setGiftStateStandby(Integer giftStateStandby){
		this.giftStateStandby = giftStateStandby;
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

	public RolePlatformInfo copy(){
		RolePlatformInfo result = new RolePlatformInfo();
		result.setUserRoleId(getUserRoleId());
		result.setUpdateTime(getUpdateTime());
		result.setPlatformId(getPlatformId());
		result.setGiftState(getGiftState());
		result.setGiftStateStandby(getGiftStateStandby());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
