package com.junyou.bus.xiulianzhilu.entity;
import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.junyou.utils.json.JsonUtils;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_xiulian_task")
public class RoleXiulianTask extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("day_task")
	private String dayTask;
	@Column("leiji_task")
	private String leijiTask;
	@Column("complete_task_id")
	private String completeTaskId;

	@Column("update_time")
	private Long updateTime;

	@Column("create_time")
	private Long createTime;
	
	@EntityField
	private JSONObject dayJson;
	@EntityField
	private JSONObject leijiJson;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}


	public String getDayTask() {
		return dayTask;
	}

	public void setDayTask(String dayTask) {
		this.dayTask = dayTask;
	}

	public String getLeijiTask() {
		return leijiTask;
	}

	public void setLeijiTask(String leijiTask) {
		this.leijiTask = leijiTask;
	}

	public String getCompleteTaskId() {
		return completeTaskId;
	}

	public void setCompleteTaskId(String completeTaskId) {
		this.completeTaskId = completeTaskId;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
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

	public RoleXiulianTask copy(){
		RoleXiulianTask result = new RoleXiulianTask();
		result.setUserRoleId(getUserRoleId());
		result.setCompleteTaskId(getCompleteTaskId());
		result.setCreateTime(getCreateTime());
		result.setDayTask(getDayTask());
		result.setLeijiTask(getLeijiTask());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
	
 //*****************************myself*********************************
	public JSONObject getDayJson() {
		if(dayJson != null){
			return dayJson;
		}
		dayJson = JsonUtils.getJSONObj(this.dayTask);
	   return dayJson;
	}
	public void setDayJson(JSONObject json) {
		this.dayJson = json;
		this.dayTask = json == null?"":json.toString();
	} 
	
	public JSONObject getLeijiJson() {
		if(leijiJson != null){
			return leijiJson;
		}
		leijiJson = JsonUtils.getJSONObj(this.leijiTask);
		return leijiJson;
	}
	public void setLeijiJson(JSONObject json) {
		this.leijiJson = json;
		this.leijiTask = json == null?"":json.toString();
	} 
	
	
}
