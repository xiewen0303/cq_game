package com.junyou.bus.platform.qq.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_qq_tgp")
public class RoleQqTgp extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("znl_val")
	private Integer znlVal;

	@Column("hd_count")
	private Integer hdCount;

	@Column("duihuan_count")
	private Integer duihuanCount;

	@Column("update_time")
	private Long updateTime;

	@Column("create_time")
	private Long createTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getZnlVal(){
		return znlVal;
	}

	public  void setZnlVal(Integer znlVal){
		this.znlVal = znlVal;
	}

	public Integer getHdCount(){
		return hdCount;
	}

	public  void setHdCount(Integer hdCount){
		this.hdCount = hdCount;
	}

	public Integer getDuihuanCount(){
		return duihuanCount;
	}

	public  void setDuihuanCount(Integer duihuanCount){
		this.duihuanCount = duihuanCount;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Long createTime){
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

	public RoleQqTgp copy(){
		RoleQqTgp result = new RoleQqTgp();
		result.setUserRoleId(getUserRoleId());
		result.setZnlVal(getZnlVal());
		result.setHdCount(getHdCount());
		result.setDuihuanCount(getDuihuanCount());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
