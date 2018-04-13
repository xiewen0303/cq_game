package com.junyou.bus.territory.entity;
import java.io.Serializable;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("territory_day_reward")
public class TerritoryDayReward extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("map_id")
	private Integer mapId;

	@Column("update_time")
	private Long updateTime;


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getMapId(){
		return mapId;
	}

	public  void setMapId(Integer mapId){
		this.mapId = mapId;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public TerritoryDayReward copy(){
		TerritoryDayReward result = new TerritoryDayReward();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setMapId(getMapId());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
