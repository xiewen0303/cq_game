package com.junyou.bus.miaosha.entity;
import java.io.Serializable;

import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("refb_miaosha")
public class RefbMiaosha extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("sub_id")
	private Integer subId;

	@Column("end_time")
	private Long endTime;
	
	@Column("box_id")
	private Integer boxId;
	
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

	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	public Long getEndTime(){
		return endTime;
	}

	public  void setEndTime(Long endTime){
		this.endTime = endTime;
	}

	public Integer getBoxId() {
		if(GameSystemTime.getSystemMillTime() > endTime){
			return 0;
		}
		return boxId;
	}

	public void setBoxId(Integer boxId) {
		this.boxId = boxId;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}
	
	public boolean isCanBuy(long endTime){
		return endTime > this.endTime;
	}

	public RefbMiaosha copy(){
		RefbMiaosha result = new RefbMiaosha();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setSubId(getSubId());
		result.setEndTime(getEndTime());
		result.setBoxId(getBoxId());
		return result;
	}
}
