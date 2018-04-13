package com.junyou.bus.resource.entity;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_resource_back")
public class RoleResourceBack extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("status")
	private String status;

	@Column("update_time")
	private Long updateTime;
	
	@Column("type_update_time")
	private String typeUpdateTime;
	
	@Column("accept_update_time")
	private String acceptUpdateTime;
	
	@EntityField
	private Map<String,Map<String,Map<String,Integer>>> infoMap;//key:类型 value：(key:第几天,value:(key:奖励类型，value:奖励值))
	
	@EntityField
	private Map<String,Long> updateMap;//key:类型 value：更新时间
	
	@EntityField
	private Map<String,Long> acceptUpdateMap;//key:类型 value：更新时间
	
	public String getAcceptUpdateTime() {
		return acceptUpdateTime;
	}

	public void setAcceptUpdateTime(String acceptUpdateTime) {
		this.acceptUpdateTime = acceptUpdateTime;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public String getStatus(){
		return status;
	}

	public  void setStatus(String status){
		this.status = status;
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

	@SuppressWarnings("unchecked")
	public Map<String,Long> getAcceptUpdateTimeMap() {
		if(acceptUpdateMap == null){
			if(ObjectUtil.strIsEmpty(acceptUpdateTime)){
				acceptUpdateMap = new HashMap<>();
			}else{
				acceptUpdateMap = JSONObject.parseObject(acceptUpdateTime,Map.class);
			}
		}
		return acceptUpdateMap;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String,Map<String,Map<String,Integer>>> getInfoMap() {
		if(infoMap == null){
			if(ObjectUtil.strIsEmpty(status)){
				infoMap = new HashMap<>();
			}else{
				infoMap = (Map<String,Map<String,Map<String,Integer>>>) JSON.parse(status);
			}
		}
		return infoMap;
	}

	/**
	 * 状态有变化
	 */
	public void changeState() {
		status = JSON.toJSONString(infoMap,SerializerFeature.DisableCircularReferenceDetect);
		typeUpdateTime = JSON.toJSONString(updateMap);
	}
	
	/**
	 * 领奖更新
	 */
	public void changeTypeUpdateTime() {
		acceptUpdateTime = JSONObject.toJSONString(acceptUpdateMap);
	}
	
	/**
	 * 时间有变化
	 */
	public void changeTime() {
		typeUpdateTime = JSON.toJSONString(updateMap);
	}

	public Map<String, Long> getUpdateMap() {
		if(updateMap == null){
			if(ObjectUtil.strIsEmpty(typeUpdateTime) || "null".equals(typeUpdateTime)){
				updateMap = new HashMap<>();
			}else{
				updateMap = (Map<String, Long>) JSON.parse(typeUpdateTime);
			}
		}
		return updateMap;
	}

	public String getTypeUpdateTime() {
		return typeUpdateTime;
	}

	public void setTypeUpdateTime(String typeUpdateTime) {
		this.typeUpdateTime = typeUpdateTime;
	}

	public RoleResourceBack copy(){
		RoleResourceBack result = new RoleResourceBack();
		result.setUserRoleId(getUserRoleId());
		result.setStatus(getStatus());
		result.setUpdateTime(getUpdateTime());
		result.setTypeUpdateTime(getTypeUpdateTime());
		result.setAcceptUpdateTime(getAcceptUpdateTime());
		return result;
	}
}
