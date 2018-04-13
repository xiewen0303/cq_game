package com.junyou.bus.jewel.entity;
import java.io.Serializable;
import java.sql.Timestamp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_jewel")
public class RoleJewel extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("data")
	private String data;

	@Column("create_time")
	private Timestamp createTime;

	@EntityField
	private Object[] allData;  //add

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public String getData(){
		return data;
	}

	public  void setData(String data){
		this.data = data;
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

	public RoleJewel copy(){
		RoleJewel result = new RoleJewel();
		result.setUserRoleId(getUserRoleId());
		result.setData(getData());
		result.setCreateTime(getCreateTime());
		return result;
	}
	
	//*****************add****************
	public Object[] getAllData() {
		if(allData==null){
			if(this.data!=null || !"".equals(this.data)){
				allData = JSONArray.parseObject(this.data, new TypeReference<Object[]>() {});
			}
		}
		return allData;
	}
	public void setAllData(Object[] allData) {
		this.allData = allData;
	}
	
}
