package com.junyou.bus.xunbao.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("xun_bao")
public class XunBao extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("find_count")
	private Integer findCount;

	@Column("find_jf")
	private Integer findJf;

	@Column("find_versions")
	private String findVersions;

	@Column("find_last_time")
	private Long findLastTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getFindCount(){
		return findCount;
	}

	public  void setFindCount(Integer findCount){
		this.findCount = findCount;
	}

	public Integer getFindJf(){
		return findJf;
	}

	public  void setFindJf(Integer findJf){
		this.findJf = findJf;
	}

	public String getFindVersions(){
		return findVersions;
	}

	public  void setFindVersions(String findVersions){
		this.findVersions = findVersions;
	}

	public Long getFindLastTime(){
		return findLastTime;
	}

	public  void setFindLastTime(Long findLastTime){
		this.findLastTime = findLastTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public XunBao copy(){
		XunBao result = new XunBao();
		result.setUserRoleId(getUserRoleId());
		result.setFindCount(getFindCount());
		result.setFindJf(getFindJf());
		result.setFindVersions(getFindVersions());
		result.setFindLastTime(getFindLastTime());
		return result;
	}
}
