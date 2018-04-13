package com.junyou.bus.xunbao.entity;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("refb_xunbao")
public class RefbXunbao extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("find_count")
	private Integer findCount;

	@Column("sub_id")
	private Integer subId;

	@Column("qmjl_lingqu_status")
	private String qmjlLingquStatus;

	@Column("update_time")
	private Long updateTime;

	@EntityField
	private Map<Integer, Integer> lingquStatusMap;
	
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

	public Integer getFindCount(){
		return findCount;
	}

	public  void setFindCount(Integer findCount){
		this.findCount = findCount;
	}

	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	public String getQmjlLingquStatus(){
		return qmjlLingquStatus;
	}

	public  void setQmjlLingquStatus(String qmjlLingquStatus){
		this.qmjlLingquStatus = qmjlLingquStatus;
		if(!CovertObjectUtil.isEmpty(qmjlLingquStatus)){
			lingquStatusMap = JSONObject.parseObject(qmjlLingquStatus,Map.class);
		}
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Map<Integer, Integer> getLingquStatusMap(){
		return lingquStatusMap == null ? new HashMap<Integer, Integer>():lingquStatusMap;
	}
	
	public void setLingquStatusMap(Map<Integer,Integer> lingquStatusMap) {
		String qmjlLingquStatus = "";
		if(lingquStatusMap != null && lingquStatusMap.size() > 0){
			qmjlLingquStatus = JSONObject.toJSONString(lingquStatusMap);
			this.lingquStatusMap = lingquStatusMap;
		}
		this.qmjlLingquStatus = qmjlLingquStatus;
	}
	
	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RefbXunbao copy(){
		RefbXunbao result = new RefbXunbao();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setFindCount(getFindCount());
		result.setSubId(getSubId());
		result.setQmjlLingquStatus(getQmjlLingquStatus());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
