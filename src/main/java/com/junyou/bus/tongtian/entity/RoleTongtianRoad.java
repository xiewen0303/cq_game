package com.junyou.bus.tongtian.entity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_tongtian_road")
public class RoleTongtianRoad extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("value")
	private Integer value;

	@Column("position")
	private Integer position;

	@Column("attribute")
	private String attribute;

	@Column("update_time")
	private Long updateTime;

	@Column("create_time")
	private Timestamp createTime;

	@EntityField
	private Map<String, Long> attrMap = new HashMap<>();
	@EntityField
	private Map<String, Long> clientMap = new HashMap<>();
	
	public Map<String, Long> getClientMap() {
		
		return clientMap;
	}
	public void setClientMap(Map<String, Long> clientMap) {
		this.clientMap = clientMap;
	}
	
	public Map<String, Long> getAttrMap() {
		if(attrMap.isEmpty()){
			if(this.attribute!=null && !"".equals(this.attribute)){
				attrMap = JSONArray.parseObject(this.attribute, new TypeReference<Map<String, Long>>() {});	
			}
		}
		return attrMap;
	}
	public void setAttrMap(Map<String, Long> attrMap) {
		this.attrMap = attrMap;
		this.attribute  = JSON.toJSONString(this.attrMap);//更新
	}
	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getValue(){
		return value;
	}

	public  void setValue(Integer value){
		this.value = value;
	}

	public Integer getPosition(){
		return position;
	}

	public  void setPosition(Integer position){
		this.position = position;
	}

	public String getAttribute(){
		return attribute;
	}

	public  void setAttribute(String attribute){
		this.attribute = attribute;
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

	public RoleTongtianRoad copy(){
		RoleTongtianRoad result = new RoleTongtianRoad();
		result.setUserRoleId(getUserRoleId());
		result.setValue(getValue());
		result.setPosition(getPosition());
		result.setAttribute(getAttribute());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
