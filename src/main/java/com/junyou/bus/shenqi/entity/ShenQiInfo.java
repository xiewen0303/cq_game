package com.junyou.bus.shenqi.entity;
import java.io.Serializable;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("shen_qi_info")
public class ShenQiInfo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("shen_qi_id")
	private Integer shenQiId;

	@Column("attr_map")
	private String attrMap;

	@EntityField
	private Map<String, Long>  freeMap; //洗练属性临时存储下,保存的时候入库
	public Map<String, Long> getFreeMap() {
		return freeMap;
	}
	public void setFreeMap(Map<String, Long> freeMap) {
		this.freeMap = freeMap;
	}
	@EntityField
	private Map<String, Long> currentInfoMap; //数据访问
	public Map<String, Long> getCurrentInfoMap() {
		if(currentInfoMap==null){
			if(this.getAttrMap()!=null && this.getAttrMap().length()>0){
				currentInfoMap  = JSON.parseObject(this.getAttrMap(), new TypeReference<Map<String, Long>>() {});
			}
		}
		return currentInfoMap;
	}
	public void setCurrentInfoMap(Map<String, Long> currentInfoMap) {
		this.currentInfoMap = currentInfoMap;
	}
	
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

	public Integer getShenQiId(){
		return shenQiId;
	}

	public  void setShenQiId(Integer shenQiId){
		this.shenQiId = shenQiId;
	}

	public String getAttrMap(){
		return attrMap;
	}

	public  void setAttrMap(String attrMap){
		this.attrMap = attrMap;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public ShenQiInfo copy(){
		ShenQiInfo result = new ShenQiInfo();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setShenQiId(getShenQiId());
		result.setAttrMap(getAttrMap());
		return result;
	}
}
