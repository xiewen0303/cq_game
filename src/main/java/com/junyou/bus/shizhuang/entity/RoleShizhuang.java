package com.junyou.bus.shizhuang.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_shizhuang")
public class RoleShizhuang extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("shizhuang_id")
	private Integer shizhuangId;

	@Column("level")
	private Integer level;

	@Column("is_show")
	private Integer isShow;
	
	@Column("expire_time")
	private Long expireTime;//过期时间


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

	public Integer getShizhuangId(){
		return shizhuangId;
	}

	public  void setShizhuangId(Integer shizhuangId){
		this.shizhuangId = shizhuangId;
	}

	public Integer getLevel(){
		return level;
	}

	public  void setLevel(Integer level){
		this.level = level;
	}

	public Integer getIsShow(){
		return isShow;
	}

	public  void setIsShow(Integer isShow){
		this.isShow = isShow;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}
	
	public Long getExpireTime() {
		return expireTime;
	}
	
	public boolean isExpire(Long cur){
		if(expireTime == null || expireTime == 0){
			return false;
		}
		return expireTime <= cur;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public RoleShizhuang copy(){
		RoleShizhuang result = new RoleShizhuang();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setShizhuangId(getShizhuangId());
		result.setLevel(getLevel());
		result.setIsShow(getIsShow());
		result.setExpireTime(getExpireTime());
		return result;
	}
}
