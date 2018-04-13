package com.junyou.bus.tianyu.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("tian_yu_info")
public class TianYuInfo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("skill_ids")
	private String skillIds;

	@Column("qnd_count")
	private Integer qndCount;

	@Column("czdCount")
	private Integer czdcount;

	@Column("tianyu_level")
	private Integer tianyuLevel;

	@Column("show_id")
	private Integer showId;

	@Column("is_get_on")
	private Integer isGetOn;

	@Column("zfz_val")
	private Integer zfzVal;

	@Column("zplus")
	private Long zplus;

	@Column("last_sj_time")
	private Long lastSjTime;

	@Column("update_time")
	private Timestamp updateTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public String getSkillIds(){
		return skillIds;
	}

	public  void setSkillIds(String skillIds){
		this.skillIds = skillIds;
	}

	public Integer getQndCount(){
		return qndCount;
	}

	public  void setQndCount(Integer qndCount){
		this.qndCount = qndCount;
	}

	public Integer getCzdcount(){
		return czdcount;
	}

	public  void setCzdcount(Integer czdcount){
		this.czdcount = czdcount;
	}

	public Integer getTianyuLevel(){
		return tianyuLevel;
	}

	public  void setTianyuLevel(Integer tianyuLevel){
		this.tianyuLevel = tianyuLevel;
	}

	public Integer getShowId(){
		return showId;
	}

	public  void setShowId(Integer showId){
		this.showId = showId;
	}

	public Integer getIsGetOn(){
		return isGetOn;
	}

	public  void setIsGetOn(Integer isGetOn){
		this.isGetOn = isGetOn;
	}

	public Integer getZfzVal(){
		return zfzVal;
	}

	public  void setZfzVal(Integer zfzVal){
		this.zfzVal = zfzVal;
	}

	public Long getZplus(){
		return zplus;
	}

	public  void setZplus(Long zplus){
		this.zplus = zplus;
	}

	public Long getLastSjTime(){
		return lastSjTime;
	}

	public  void setLastSjTime(Long lastSjTime){
		this.lastSjTime = lastSjTime;
	}

	public Timestamp getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Timestamp updateTime){
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

	public TianYuInfo copy(){
		TianYuInfo result = new TianYuInfo();
		result.setUserRoleId(getUserRoleId());
		result.setSkillIds(getSkillIds());
		result.setQndCount(getQndCount());
		result.setCzdcount(getCzdcount());
		result.setTianyuLevel(getTianyuLevel());
		result.setShowId(getShowId());
		result.setIsGetOn(getIsGetOn());
		result.setZfzVal(getZfzVal());
		result.setZplus(getZplus());
		result.setLastSjTime(getLastSjTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
