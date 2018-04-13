package com.junyou.bus.lj.entity;
 
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_lj")
public class RoleLj extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;
	@Column("level")
	private Integer level;
	@Column("exp")
	private Integer exp;
	@Column("lblq")
	private String lblq; //领取状态
	@Column("last_modify_time")
	private Long lastModifyTime;
	@Column("type_count")	
	private String typeCount;
	
	@EntityField
	private Set<Integer> lblqs = new HashSet<>();
	@EntityField
	private Map<Integer,Integer> typeCounts = new HashMap<>();
	
	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}
 
	public String getLblq() {
		StringBuffer sb = new StringBuffer();
		if(!ObjectUtil.isEmpty(lblqs)){
			for (Integer id : lblqs) {
				sb.append(id).append(",");
			}
			this.lblq = sb.toString();
		}
		return lblq;
	}

	public void setLblq(String lblq) {
		this.lblq = lblq;
		
		if(!ObjectUtil.isEmpty(lblq)){
			lblqs = new HashSet<>();
			for (String e : lblq.split(",")) {
				lblqs.add(CovertObjectUtil.object2int(e));
			}
		}
	}
	
	public Set<Integer> getLblqs(){
		return lblqs;
	}
	
	public void addLblq(int id) {
		if(ObjectUtil.isEmpty(lblqs)){
			lblqs = new HashSet<Integer>();
		}
		lblqs.add(id);
	}

	public Long getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Long lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getTypeCount() {
		if(!ObjectUtil.isEmpty(typeCounts)){
			this.typeCount = JSONObject.toJSONString(typeCounts);
		}
		return typeCount;
	}

	public void setTypeCount(String typeCount) {
		this.typeCount = typeCount;
		if(!ObjectUtil.isEmpty(typeCount)){
			this.typeCounts = JSONObject.parseObject(this.typeCount,new TypeReference<Map<Integer,Integer>>(){});
		}
	}

	public Map<Integer, Integer> getTypeCounts() {
		return typeCounts;
	}

	public RoleLj copy(){
		RoleLj result = new RoleLj();
		result.setUserRoleId(getUserRoleId());
		result.setTypeCount(getTypeCount());
		result.setLastModifyTime(getLastModifyTime());
		result.setLblq(getLblq());
		result.setExp(getExp());
		result.setLevel(getLevel());
		return result;
	}



	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}
}