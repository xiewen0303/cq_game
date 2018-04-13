package com.junyou.bus.role.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.junyou.constants.GameConstants;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Primary;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

/**
 * 玩家角色Entity
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-11-28 下午4:54:46
 */
@Table("user_role")
public class UserRole extends AbsVersion implements Serializable, IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Primary
	@Column("id")
	private Long id;

	@Column("config_id")
	private Integer configId;

	@Column("user_id")
	private String userId;

	@Column("name")
	private String name;

	@Column("exp")
	private Long exp = 0l;

	@Column("zhenqi")
	private Long zhenqi = 0l;

	@Column("level")
	private Integer level;

	@Column("create_time")
	private Timestamp createTime;

	@Column("online_time")
	private Long onlineTime;

	@Column("offline_time")
	private Long offlineTime;

	@Column("is_fangchenmi")
	private Integer isFangchenmi;

	@Column("chenmi_add_online")
	private Integer chenmiAddOnline;

	@Column("chenmi_add_offline")
	private Integer chenmiAddOffline;

	@Column("server_id")
	private String serverId;

	@Column("last_login_ip")
	private String lastLoginIp;

	@Column("is_cm")
	private Integer isCm;

	@Column("is_gm")
	private Integer isGm;

	@Column("user_type")
	private Integer userType;

	@Column("once_data")
	private Integer onceData;

	@Column("is_auto_create")
	private Integer isAutoCreate;

	@Column("phone_reward_status")
	private Integer phoneRewardStatus;

	@Column("last_modify_name_time")
	private Long lastModifyNameTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getExp() {
		return exp;
	}

	/**
	 * 玩家经验,默认为0
	 * 
	 * @param exp
	 */
	public void setExp(Long exp) {
		this.exp = exp;
	}

	public int getLevel() {
		return level;
	}

	public Integer getIsAutoCreate() {
		return isAutoCreate;
	}

	public void setIsAutoCreate(Integer isAutoCreate) {
		this.isAutoCreate = isAutoCreate;
	}

	/**
	 * 不填,默认为1
	 * 
	 * @param level
	 *            玩家等级
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Long onlineTime) {
		this.onlineTime = onlineTime;
	}

	public Long getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(Long offlineTime) {
		this.offlineTime = offlineTime;
	}

	/**
	 * 是否为防沉迷玩家
	 * 
	 * @return true:沉迷玩家（未成年）
	 */
	public boolean isFangchenmi() {
		return isFangchenmi == GameConstants.IS_FANGCHENMI;
	}

	/**
	 * 不填,默认为0（非沉迷）
	 * 
	 * @param isFangchenmi是否防沉迷
	 *            （0非沉迷，1沉迷）
	 */
	public void setIsFangchenmi(Integer isFangchenmi) {
		this.isFangchenmi = isFangchenmi;
	}

	public Integer getChenmiAddOnline() {
		return chenmiAddOnline;
	}

	public void setChenmiAddOnline(Integer chenmiAddOnline) {
		this.chenmiAddOnline = chenmiAddOnline;
	}

	public Integer getChenmiAddOffline() {
		return chenmiAddOffline;
	}

	public void setChenmiAddOffline(Integer chenmiAddOffline) {
		this.chenmiAddOffline = chenmiAddOffline;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	/**
	 * 是否为重名玩家
	 * 
	 * @return true:非重名
	 */
	public boolean isCm() {
		return isCm == GameConstants.NOT_IS_CM;
	}

	/**
	 * 不填,默认为0（非重名）
	 * 
	 * @param isCm
	 *            （0非重名，1重名）
	 */
	public void setIsCm(Integer isCm) {
		this.isCm = isCm;
	}

	/**
	 * 不填,默认为0（非GM）
	 * 
	 * @param isGm
	 *            （0非GM，1GM）
	 */
	public void setIsGm(Integer isGm) {
		this.isGm = isGm;
	}

	public Integer getUserType() {
		return userType;
	}

	/**
	 * 不填,默认为0（正常玩家）
	 * 
	 * @param isGm
	 *            （0正常玩家，1托，2内部人员）
	 */
	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	public Integer getIsFangchenmi() {
		return isFangchenmi;
	}

	public Integer getIsCm() {
		return isCm;
	}

	public Integer getIsGm() {
		return isGm;
	}

	@Override
	public Long getPrimaryKeyValue() {
		return getId();
	}

	public Integer getOnceData() {
		return onceData;
	}

	public void setOnceData(Integer onceData) {
		this.onceData = onceData;
	}

	public Long getZhenqi() {
		return zhenqi == null ? 0 : zhenqi;
	}

	public void setZhenqi(Long zhenqi) {
		this.zhenqi = zhenqi;
	}

	public Integer getPhoneRewardStatus() {
		return phoneRewardStatus;
	}

	public void setPhoneRewardStatus(Integer phoneRewardStatus) {
		this.phoneRewardStatus = phoneRewardStatus;
	}

	public Long getLastModifyNameTime() {
		return lastModifyNameTime;
	}

	public void setLastModifyNameTime(Long lastModifyNameTime) {
		this.lastModifyNameTime = lastModifyNameTime;
	}

	/**
	 * 是否为GM角色
	 * 
	 * @return true:是
	 */
	public boolean isGm() {
		return isGm == GameConstants.IS_GM;
	}

	public UserRole copy() {
		UserRole result = new UserRole();
		result.setId(getId());
		result.setConfigId(getConfigId());
		result.setUserId(getUserId());
		result.setName(getName());
		result.setExp(getExp());
		result.setLevel(getLevel());
		result.setCreateTime(getCreateTime());
		result.setOnlineTime(getOnlineTime());
		result.setOfflineTime(getOfflineTime());
		result.setIsFangchenmi(isFangchenmi);
		result.setChenmiAddOnline(getChenmiAddOnline());
		result.setChenmiAddOffline(getChenmiAddOffline());
		result.setServerId(getServerId());
		result.setLastLoginIp(getLastLoginIp());
		result.setIsCm(isCm);
		result.setIsGm(isGm);
		result.setUserType(getUserType());
		result.setOnceData(getOnceData());
		result.setZhenqi(getZhenqi());
		result.setIsAutoCreate(getIsAutoCreate());
		result.setPhoneRewardStatus(getPhoneRewardStatus());
		result.setLastModifyNameTime(getLastModifyNameTime());
		return result;
	}
}
