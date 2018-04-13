package com.junyou.bus.leihao.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("refabu_leihao")
public class RefabuLeihao extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("sub_id")
	private Integer subId;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("yb_count")
	private Integer ybCount;

	@Column("pick_str")
	private String pickStr;
	
	@Column("update_time")
	private Long updateTime;

	@Column("yb_day_count")
	private Integer ybDayCount;
	
	@Column("yb_day_id")
	private String ybDayId;
	

	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getYbCount(){
		return ybCount;
	}

	public  void setYbCount(Integer ybCount){
		this.ybCount = ybCount;
	}

	public String getPickStr(){
		return pickStr;
	}

	public  void setPickStr(String pickStr){
		this.pickStr = pickStr;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
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

	
	
	public Integer getYbDayCount() {
		return ybDayCount;
	}

	public void setYbDayCount(Integer ybDayCount) {
		this.ybDayCount = ybDayCount;
	}

	public String getYbDayId() {
		return ybDayId;
	}

	public void setYbDayId(String ybDayId) {
		this.ybDayId = ybDayId;
	}

	public RefabuLeihao copy(){
		RefabuLeihao result = new RefabuLeihao();
		result.setId(getId());
		result.setSubId(getSubId());
		result.setUserRoleId(getUserRoleId());
		result.setYbCount(getYbCount());
		result.setPickStr(getPickStr());
		result.setUpdateTime(getUpdateTime());
		result.setYbDayCount(getYbDayCount());
		result.setYbDayId(getYbDayId());
		return result;
	}
}
