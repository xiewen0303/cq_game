package com.junyou.bus.lianchong.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_lianchong")
public class RoleLianchong extends AbsVersion implements Serializable, IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;
	@Column("active_end_time")
	private Long activeEndTime;
	@Column("user_role_id")
	private Long userRoleId;

	@Column("day_recharge")
	private Integer dayRecharge;

	@Column("day_reward")
	private Integer dayReward;

	@Column("update_time")
	private Long updateTime;

	@Column("gold_reward_info")
	private String goldRewardInfo;

	@Column("create_time")
	private Timestamp createTime;

	@Column("sub_id")
	private Integer subId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getDayRecharge() {
		return dayRecharge;
	}

	public void setDayRecharge(Integer dayRecharge) {
		this.dayRecharge = dayRecharge;
	}

	public Integer getDayReward() {
		return dayReward;
	}

	public void setDayReward(Integer dayReward) {
		this.dayReward = dayReward;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	public Long getActiveEndTime(){
		return activeEndTime;
	}

	public  void setActiveEndTime(Long activeEndTime){
		this.activeEndTime = activeEndTime;
	}

	public String getGoldRewardInfo() {
		//新数据都会在goldRewardInfoList
		if(this.goldRewardInfoList!=null){
			return this.goldRewardInfoList.toString();
		}
		return goldRewardInfo;
	}

	public void setGoldRewardInfo(String goldRewardInfo) {
		this.goldRewardInfo = goldRewardInfo;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getSubId() {
		return subId;
	}

	public void setSubId(Integer subId) {
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

	public RoleLianchong copy() {
		RoleLianchong result = new RoleLianchong();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setDayRecharge(getDayRecharge());
		result.setDayReward(getDayReward());
		result.setUpdateTime(getUpdateTime());
		result.setGoldRewardInfo(getGoldRewardInfo());
		result.setCreateTime(getCreateTime());
		result.setSubId(getSubId());
		result.setActiveEndTime(getActiveEndTime());
		return result;
	}
	// *****************************myself*********************************
	/**
	 * [ [int(今天是否完成0|1), int(excel行id）,int(完成天数),[领取过的奖励3,5,7 ] ],... ] 
	 */
	@EntityField
	private List<JSONArray> goldRewardInfoList;

	public List<JSONArray> getGoldRewardInfoList() {   
		if (goldRewardInfoList != null) {
			return goldRewardInfoList;
		}
		this.goldRewardInfoList  =  JSON.parseObject(this.goldRewardInfo,new TypeReference<List<JSONArray>>(){});
		return this.goldRewardInfoList;
	}
	/**
	 * 添加单个
	 * @param json
	 */
	public void addGoldRewardInfoArray(JSONArray json) {
		if(json==null || json.size()==0){return;}
		if(this.goldRewardInfoList==null){
			this.goldRewardInfoList = new ArrayList<>();
		}
		this.goldRewardInfoList.add(json);
	}
	/**
	 * 获取对应的某个档位的数据
	 * [int(今天是否完成0|1), int(excel行id）,int(完成天数),[领取过的奖励3,5,7 ] ]
	 */

	public JSONArray getRewardArrayById(int configId){
		if(this.getGoldRewardInfoList()==null){return null;}
		for (JSONArray jsonArray : goldRewardInfoList) {
			if(jsonArray.getIntValue(1)==configId){
				return jsonArray;
			}
		}
		return null;
	}
	/**
	 * 数据有变化直接更新字段
	 */
	@Deprecated
	public void updateGoldRewardInfo(){
		if(this.goldRewardInfoList!=null){
			 this.goldRewardInfo = this.goldRewardInfoList.toString();
		}
	}
	
	
}
