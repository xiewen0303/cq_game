package com.junyou.bus.role.entity;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("tangbao")
public class Tangbao extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("zzdan")
	private Integer zzdan;

	@Column("czdan")
	private Integer czdan;
	
	@Column("eat_info")
	private String eatInfo;
	
	@Column("progress")
	private Integer progress;
	
	@EntityField
	private Map<String,Integer> eatInfoMap;
	
	@EntityField
	private Map<String,Long> eatAttribute;
	
	@EntityField
	private Object[] msgData;
	
	@EntityField
	private static int maxCount;
	
	
	

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getZzdan(){
		return zzdan;
	}

	public  void setZzdan(Integer zzdan){
		this.zzdan = zzdan;
	}

	public Integer getCzdan(){
		return czdan;
	}

	public  void setCzdan(Integer czdan){
		this.czdan = czdan;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public Map<String, Long> getEatAttribute() {
		return eatAttribute;
	}

	public void setEatAttribute(Map<String, Long> eatAttribute) {
		this.eatAttribute = eatAttribute;
	}

	public String getEatInfo() {
		return eatInfo;
	}

	public void setEatInfo(String eatInfo) {
		this.eatInfo = eatInfo;
	}

	public Integer getProgress() {
		return progress;
	}
	/**
	 * 糖宝是否已激活
	 * @return
	 */
	public boolean isActive(){
		return progress < 0; 
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}
	
	/**
	 * 增加进度
	 * @return	是否已激活
	 */
	public void addProgress() {
		this.progress += 1;
		if(progress >= maxCount){
			progress = -1;
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getEatInfoMap() {
		if(eatInfoMap == null){
			if(ObjectUtil.strIsEmpty(eatInfo)){
				eatInfoMap = new HashMap<>();
			}else{
				eatInfoMap = (Map<String,Integer>)JSONObject.parse(eatInfo);
			}
		}
		return eatInfoMap;
	}

	public void setEatInfoMap(Map<String, Integer> eatInfoMap) {
		this.eatInfoMap = eatInfoMap;
	}

	public Tangbao copy(){
		Tangbao result = new Tangbao();
		result.setUserRoleId(getUserRoleId());
		result.setZzdan(getZzdan());
		result.setCzdan(getCzdan());
		result.setEatAttribute(getEatAttribute());
		result.setEatInfo(getEatInfo());
		result.setEatInfoMap(getEatInfoMap());
		result.setProgress(getProgress());
		return result;
	}

	public static void setMaxCount(int maxCount) {
		Tangbao.maxCount = maxCount;
	}

	/**
	 * 重新检测
	 * @return
	 */
	public boolean reCheck() {
		if(progress >= maxCount){
			progress = -1;
			return true;
		}
		return false;
	}

	public Object[] getMsgData() {
		if(msgData == null){
			msgData = new Object[]{0,progress};
		}else{
			msgData[1] = progress;
		}
		return msgData;
	}
	
	
	
}
