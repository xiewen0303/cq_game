package com.junyou.bus.yaoshen.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_yaoshen")
public class RoleYaoshen extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("is_yaoshen_show")
	private Integer isYaoshenShow;

	@Column("jie_level")
	private Integer jieLevel;

	@Column("ceng_level")
	private Integer cengLevel;

	@Column("qnd_num")
	private Integer qndNum;

	@Column("czd_num")
	private Integer czdNum;
	
	@Column("update_time")
	private Long updateTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getIsYaoshenShow(){
		return isYaoshenShow;
	}

	public  void setIsYaoshenShow(Integer isYaoshenShow){
		this.isYaoshenShow = isYaoshenShow;
	}

	public Integer getJieLevel(){
		return jieLevel;
	}

	public  void setJieLevel(Integer jieLevel){
		this.jieLevel = jieLevel;
	}

	public Integer getCengLevel(){
		return cengLevel;
	}

	public  void setCengLevel(Integer cengLevel){
		this.cengLevel = cengLevel;
	}

	public Integer getQndNum(){
		return qndNum;
	}

	public  void setQndNum(Integer qndNum){
		this.qndNum = qndNum;
	}

	public Integer getCzdNum(){
		return czdNum;
	}

	public  void setCzdNum(Integer czdNum){
		this.czdNum = czdNum;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public RoleYaoshen copy(){
		RoleYaoshen result = new RoleYaoshen();
		result.setUserRoleId(getUserRoleId());
		result.setIsYaoshenShow(getIsYaoshenShow());
		result.setJieLevel(getJieLevel());
		result.setCengLevel(getCengLevel());
		result.setQndNum(getQndNum());
		result.setCzdNum(getCzdNum());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
