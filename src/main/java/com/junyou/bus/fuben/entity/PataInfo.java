package com.junyou.bus.fuben.entity;
import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.junyou.constants.GameConstants;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("pata_info")
public class PataInfo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("max_ceng")
	private Integer maxCeng;

	@Column("count")
	private Integer count;

	@Column("buy_count")
	private Integer buyCount;

	@Column("yuanzhu_count")
	private Integer yuanzhuCount;

	@Column("best_time")
	private String bestTime;

	@Column("update_time")
	private Long updateTime;
	
	@EntityField
	private JSONObject bestTimeMap;
	
	@EntityField
	private int state = GameConstants.FUBEN_STATE_READY;
	
	@EntityField
	private int enterCeng;
	
	@EntityField
	private long enterTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getMaxCeng(){
		return maxCeng;
	}

	public  void setMaxCeng(Integer maxCeng){
		this.maxCeng = maxCeng;
	}

	public Integer getCount(){
		return count;
	}

	public  void setCount(Integer count){
		this.count = count;
	}

	public Integer getBuyCount(){
		return buyCount;
	}

	public  void setBuyCount(Integer buyCount){
		this.buyCount = buyCount;
	}

	public Integer getYuanzhuCount(){
		return yuanzhuCount;
	}

	public  void setYuanzhuCount(Integer yuanzhuCount){
		this.yuanzhuCount = yuanzhuCount;
	}

	public String getBestTime(){
		return bestTime;
	}

	public  void setBestTime(String bestTime){
		this.bestTime = bestTime;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}
	
	public JSONObject getBestTimeMap() {
		if(bestTimeMap == null){
			if(ObjectUtil.strIsEmpty(bestTime)){
				bestTimeMap = new JSONObject();
			}else{
				bestTimeMap = JSONObject.parseObject(bestTime);
			}
		}
		return bestTimeMap;
	}
	
	public Integer getCengBestTime(String cengId){
		return getBestTimeMap().getInteger(cengId);
	}

	public void setBestTimeMap(JSONObject bestTimeMap) {
		this.bestTimeMap = bestTimeMap;
	}
	
	public void changeBestTime(String ceng,int time){
		getBestTimeMap().put(ceng, time);
		bestTime = bestTimeMap.toJSONString();
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public int getEnterCeng() {
		return enterCeng;
	}

	public void setEnterCeng(int enterCeng) {
		this.enterCeng = enterCeng;
	}

	public long getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(long enterTime) {
		this.enterTime = enterTime;
	}

	public PataInfo copy(){
		PataInfo result = new PataInfo();
		result.setUserRoleId(getUserRoleId());
		result.setMaxCeng(getMaxCeng());
		result.setCount(getCount());
		result.setBuyCount(getBuyCount());
		result.setYuanzhuCount(getYuanzhuCount());
		result.setBestTime(getBestTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
