package com.junyou.bus.suoyaota.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("refb_cangbaoge")
public class RefbSuoyaota extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;
	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("sub_id")
	private Integer subId;

	@Column("cur_ceng")
	private Integer curCeng;

	@Column("cur_lucky")
	private Integer curLucky;

	@Column("update_time")
	private Long updateTime;
	
	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	public Integer getCurCeng(){
		return curCeng;
	}

	public  void setCurCeng(Integer curCeng){
		this.curCeng = curCeng;
	}

	public Integer getCurLucky(){
		return curLucky;
	}

	public  void setCurLucky(Integer curLucky){
		this.curLucky = curLucky;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RefbSuoyaota copy(){
		RefbSuoyaota result = new RefbSuoyaota();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setSubId(getSubId());
		result.setCurCeng(getCurCeng());
		result.setCurLucky(getCurLucky());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
