package com.junyou.bus.huoyuedu.entity;
import java.io.Serializable;
import java.sql.Timestamp;

import com.alibaba.fastjson.JSONObject;
import com.junyou.utils.json.JsonUtils;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_huoyuedu")
public class RoleHuoyuedu extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("activity_count")
	private String activityCount;

	@Column("award")
	private Integer award;

	@Column("update_time")
	private Long updateTime;

	@Column("create_time")
	private Timestamp createTime;
	
	@EntityField
	private JSONObject json;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public String getActivityCount(){
		return activityCount;
	}

	public  void setActivityCount(String activityCount){
		this.activityCount = activityCount;
	}

	public Integer getAward(){
		return award;
	}

	public  void setAward(Integer award){
		this.award = award;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
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

	public RoleHuoyuedu copy(){
		RoleHuoyuedu result = new RoleHuoyuedu();
		result.setUserRoleId(getUserRoleId());
		result.setActivityCount(getActivityCount());
		result.setAward(getAward());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		return result;
	}
	
 //*****************************myself*********************************
	public JSONObject getJson() {
		if(json != null){
			return json;
		}
		json = JsonUtils.getJSONObj(this.activityCount);
	   return json;
	}
	public void setJson(JSONObject json) {
		this.json = json;
		this.activityCount = json == null?"":json.toString();
	} 
	
	
	
	
	
	
}
