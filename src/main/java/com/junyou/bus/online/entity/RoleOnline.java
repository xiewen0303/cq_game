package com.junyou.bus.online.entity;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_online")
public class RoleOnline extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;
	
	@Column("total_online_time")
	private Long totalOnlineTime;

	@Column("online_time")
	private Long onlineTime;

	@Column("login_time")
	private Long loginTime;

	@Column("state")
	private Integer state;

	@Column("goods1")
	private String goods1;

	@Column("count1")
	private Integer count1;

	@Column("goods2")
	private String goods2;

	@Column("count2")
	private Integer count2;

	@Column("goods3")
	private String goods3;

	@Column("count3")
	private Integer count3;

	@Column("goods4")
	private String goods4;

	@Column("count4")
	private Integer count4;
	
	@Column("online_awards_data")
	private String onlineAwardsData;
	
	@EntityField
	private Map<Integer,Integer> onlineAwards = new HashMap<>();
	
	private void clearGoods(){
		goods1 = "";
		goods2 = "";
		goods3 = "";
		goods4 = "";
		count1 = 0;
		count2 = 0;
		count3 = 0;
		count4 = 0;
	}
	 
	public Map<Integer, Integer> getOnlineAwards() {
		return onlineAwards;
	}


	public void setOnlineAwards(Map<Integer, Integer> onlineAwards) {
		this.onlineAwards = onlineAwards;
	}

	public String getOnlineAwardsData() {
		return JSONObject.toJSONString(onlineAwards);
	}

	public void setOnlineAwardsData(String onlineAwardsData) {
		this.onlineAwardsData = onlineAwardsData;
		
		if(!CovertObjectUtil.isEmpty(onlineAwardsData)){
			this.onlineAwards = JSONObject.parseObject(onlineAwardsData,new TypeReference<Map<Integer,Integer>>(){});
		}
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Long getTotalOnlineTime() {
		return totalOnlineTime;
	}

	public void setTotalOnlineTime(Long totalOnlineTime) {
		this.totalOnlineTime = totalOnlineTime;
	}

	public Long getOnlineTime(){
		return onlineTime;
	}

	public  void setOnlineTime(Long onlineTime){
		this.onlineTime = onlineTime;
	}

	public Long getLoginTime(){
		return loginTime;
	}

	public  void setLoginTime(Long loginTime){
		this.loginTime = loginTime;
	}

	public Integer getState(){
		return state;
	}

	public  void setState(Integer state){
		this.state = state;
		if(state == 0){//重置领取状态，清空已兑换奖励
			clearGoods();
		}
	}

	public String getGoods1(){
		return goods1;
	}

	public  void setGoods1(String goods1){
		this.goods1 = goods1;
	}

	public Integer getCount1(){
		return count1;
	}

	public  void setCount1(Integer count1){
		this.count1 = count1;
	}

	public String getGoods2(){
		return goods2;
	}

	public  void setGoods2(String goods2){
		this.goods2 = goods2;
	}

	public Integer getCount2(){
		return count2;
	}

	public  void setCount2(Integer count2){
		this.count2 = count2;
	}

	public String getGoods3(){
		return goods3;
	}

	public  void setGoods3(String goods3){
		this.goods3 = goods3;
	}

	public Integer getCount3(){
		return count3;
	}

	public  void setCount3(Integer count3){
		this.count3 = count3;
	}

	public String getGoods4(){
		return goods4;
	}

	public  void setGoods4(String goods4){
		this.goods4 = goods4;
	}

	public Integer getCount4(){
		return count4;
	}

	public  void setCount4(Integer count4){
		this.count4 = count4;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleOnline copy(){
		RoleOnline result = new RoleOnline();
		result.setUserRoleId(getUserRoleId());
		result.setTotalOnlineTime(getTotalOnlineTime());
		result.setOnlineTime(getOnlineTime());
		result.setLoginTime(getLoginTime());
		result.setState(getState());
		result.setGoods1(getGoods1());
		result.setCount1(getCount1());
		result.setGoods2(getGoods2());
		result.setCount2(getCount2());
		result.setGoods3(getGoods3());
		result.setCount3(getCount3());
		result.setGoods4(getGoods4());
		result.setCount4(getCount4());
		result.setOnlineAwardsData(getOnlineAwardsData());
		return result;
	}
}
