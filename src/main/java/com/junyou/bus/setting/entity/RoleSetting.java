package com.junyou.bus.setting.entity;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_setting")
public class RoleSetting extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("quickbar_info")
	private String quickbarInfo;

	@Column("guaji_info")
	private String guajiInfo;
	
	@Column("system_info")
	private String systemInfo;

	@EntityField
	private Map<String, Map<String, Object>> qbInfoMap;
	
	@EntityField
	private Map<String, Object> gjInfoMap;
	
	@EntityField
	private Map<String, Object> sysInfoMap;

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}
	
	/**
	 * 获取快捷键
	 * @return 不会返回null
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, Object>> getQuickbarInfoMap(){
		if(qbInfoMap != null && qbInfoMap.size() > 0){
			return qbInfoMap;
		}
		if(quickbarInfo == null || quickbarInfo.isEmpty()){
			qbInfoMap = new HashMap<>();
		}else{
			qbInfoMap = (Map<String, Map<String, Object>>) JSON.parse(quickbarInfo);
		}
		
		return qbInfoMap;
	}
	/**
	 * 获取挂机设置
	 * @return 不会返回null
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getGuajiInfoMap(){
		if(gjInfoMap != null && gjInfoMap.size() > 0){
			return gjInfoMap;
		}
		if(guajiInfo == null || guajiInfo.isEmpty()){
			gjInfoMap = new HashMap<>();
		}else{
			gjInfoMap = (Map<String, Object>) JSON.parse(guajiInfo);
		}
		
		return gjInfoMap;
	}
	/**
	 * 获取系统设置
	 * @return 不会返回null
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSystemInfoMap(){
		if(sysInfoMap != null && sysInfoMap.size() > 0){
			return sysInfoMap;
		}
		if(systemInfo == null || systemInfo.isEmpty()){
			sysInfoMap = new HashMap<>();
		}else{
			sysInfoMap = (Map<String, Object>) JSON.parse(systemInfo);
		}
		
		return sysInfoMap;
	}
	
	private String getQuickbarInfo() {
		return quickbarInfo;
	}

	public void setQuickbarInfo(String quickbarInfo) {
		this.quickbarInfo = quickbarInfo;
	}

	public String getGuajiInfo() {
		return guajiInfo;
	}

	public void setGuajiInfo(String guajiInfo) {
		this.guajiInfo = guajiInfo;
	}

	public String getSystemInfo() {
		return systemInfo;
	}

	public void setSystemInfo(String systemInfo) {
		this.systemInfo = systemInfo;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleSetting copy(){
		RoleSetting result = new RoleSetting();
		result.setUserRoleId(getUserRoleId());
		result.setQuickbarInfo(getQuickbarInfo());
		result.setGuajiInfo(getGuajiInfo());
		result.setSystemInfo(getSystemInfo());
		return result;
	}
}
