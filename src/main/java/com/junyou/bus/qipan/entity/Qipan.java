package com.junyou.bus.qipan.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("qipan")
public class Qipan extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("qipan_id")
	private Integer qipanId;

	@Column("qipan_step")
	private Integer qipanStep;

	@Column("yihuo_count")
	private Integer yihuoCount;

	@Column("count")
	private Integer count;

	@Column("recharge_val")
	private Integer rechargeVal;

	@Column("lingqu_status")
	private String lingquStatus;

	@Column("create_time")
	private Timestamp createTime;

	@Column("update_time")
	private Timestamp updateTime;

	@Column("sub_id")
	private Integer subId;


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

	public Integer getQipanId(){
		return qipanId;
	}

	public  void setQipanId(Integer qipanId){
		this.qipanId = qipanId;
	}

	public Integer getQipanStep(){
		return qipanStep;
	}

	public  void setQipanStep(Integer qipanStep){
		this.qipanStep = qipanStep;
	}

	public Integer getYihuoCount(){
		return yihuoCount;
	}

	public  void setYihuoCount(Integer yihuoCount){
		this.yihuoCount = yihuoCount;
	}

	public Integer getCount(){
		return count;
	}

	public  void setCount(Integer count){
		this.count = count;
	}

	public Integer getRechargeVal(){
		return rechargeVal;
	}

	public  void setRechargeVal(Integer rechargeVal){
		this.rechargeVal = rechargeVal;
	}

	public String getLingquStatus(){
		return lingquStatus;
	}

	public  void setLingquStatus(String lingquStatus){
		this.lingquStatus = lingquStatus;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Timestamp updateTime){
		this.updateTime = updateTime;
	}

	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public Qipan copy(){
		Qipan result = new Qipan();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setQipanId(getQipanId());
		result.setQipanStep(getQipanStep());
		result.setYihuoCount(getYihuoCount());
		result.setCount(getCount());
		result.setRechargeVal(getRechargeVal());
		result.setLingquStatus(getLingquStatus());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		result.setSubId(getSubId());
		return result;
	}
}
