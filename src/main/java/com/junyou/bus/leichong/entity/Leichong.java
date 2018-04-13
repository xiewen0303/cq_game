package com.junyou.bus.leichong.entity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("leichong")
public class Leichong extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

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
	@Column("day_recharge_val")
	private Integer dayRechargeVal;
	@Column("day_recharge_time")
	private Long dayRechargeTime;
	@Column("recharge_day")
	private String rechargeDay;
	@Column("day_lingqu")
	private String dayLingqu;
	@Column("buqian_count")
	private Integer buqianCount;


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

	public Integer getRechargeVal(){
		return rechargeVal;
	}

	public  void setRechargeVal(Integer rechargeVal){
		this.rechargeVal = rechargeVal;
	}
	
	public Map<Integer,Integer> getLingquFl53(){
		if(!ObjectUtil.isEmpty(this.lingquStatus)){
			return JSONObject.parseObject(this.lingquStatus,new TypeReference<Map<Integer,Integer>>(){});
		}
		return null;
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

	public Integer getDayRechargeVal() {
		return dayRechargeVal;
	}

	public void setDayRechargeVal(Integer dayRechargeVal) {
		this.dayRechargeVal = dayRechargeVal;
	}


	public Long getDayRechargeTime() {
		return dayRechargeTime;
	}

	public void setDayRechargeTime(Long dayRechargeTime) {
		this.dayRechargeTime = dayRechargeTime;
	}

	public String getRechargeDay() {
		return rechargeDay;
	}

	public void setRechargeDay(String rechargeDay) {
		this.rechargeDay = rechargeDay;
	}

	public String getDayLingqu() {
		return dayLingqu;
	}

	public void setDayLingqu(String dayLingqu) {
		this.dayLingqu = dayLingqu;
	}


	public Integer getBuqianCount() {
		return buqianCount;
	}

	public void setBuqianCount(Integer buqianCount) {
		this.buqianCount = buqianCount;
	}

	public Leichong copy(){
		Leichong result = new Leichong();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setRechargeVal(getRechargeVal());
		result.setLingquStatus(getLingquStatus());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		result.setSubId(getSubId());
		result.setRechargeDay(getRechargeDay());
		result.setDayRechargeTime(getDayRechargeTime());
		result.setDayRechargeVal(getDayRechargeVal());
		result.setDayLingqu(getDayLingqu());
		result.setBuqianCount(getBuqianCount());
		return result;
	}
}
