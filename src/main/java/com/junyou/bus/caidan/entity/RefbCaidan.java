package com.junyou.bus.caidan.entity;
import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.junyou.constants.GameConstants;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("refb_caidan")
public class RefbCaidan extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("sub_id")
	private Integer subId;

	@Column("jifen")
	private Integer jifen;

	@Column("time")
	private Integer time;

	@Column("lucky")
	private Integer lucky;

	@Column("update_time")
	private Long updateTime;

	@EntityField
	private JSONObject infoJson;

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

	public Integer getJifen(){
		return jifen;
	}

	public  void setJifen(Integer jifen){
		this.jifen = jifen;
	}
	public void addJifen(Integer jifen) {
		this.jifen += jifen;
	}
	public void delJifen(Integer jifen) {
		this.jifen -= jifen;
	}
	public Integer getTime(){
		return time;
	}
	public void addTime() {
		this.time++;
	}
	public  void setTime(Integer time){
		this.time = time;
	}

	public Integer getLucky(){
		return lucky;
	}

	public  void setLucky(Integer lucky){
		this.lucky = lucky;
	}
	public void addLucky(Integer lucky) {
		this.lucky += lucky;
		if(this.lucky > GameConstants.MAX_LUCKY){
			this.lucky = GameConstants.MAX_LUCKY;
		}
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public String getDanInfo() {
		return getInfoJson().toString();
	}

	public void setDanInfo(String danInfo) {
		if(!ObjectUtil.strIsEmpty(danInfo)){
			infoJson = JSON.parseObject(danInfo);
		}else{
			infoJson = new JSONObject();
		}
	}
	
	public JSONObject getInfoJson() {
		if(infoJson == null){
			infoJson = new JSONObject();
		}
		return infoJson;
	}

	public void setInfoJson(JSONObject infoJson) {
		this.infoJson = infoJson;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RefbCaidan copy(){
		RefbCaidan result = new RefbCaidan();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setSubId(getSubId());
		result.setJifen(getJifen());
		result.setTime(getTime());
		result.setLucky(getLucky());
		result.setUpdateTime(getUpdateTime());
		result.setInfoJson(getInfoJson());
		return result;
	}
}
