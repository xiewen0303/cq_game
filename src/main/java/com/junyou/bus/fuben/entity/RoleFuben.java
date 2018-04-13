package com.junyou.bus.fuben.entity;
import java.io.Serializable;

import org.springframework.context.annotation.ComponentScan.Filter;

import com.junyou.constants.GameConstants;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_fuben")
public class RoleFuben extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("fuben_id")
	private Integer fubenId;

	@Column("type")
	private Integer type;

	@Column("state")
	private Integer state;

	@Column("saodang_start")
	private Long saodangStart = 0l;

	@Column("saodang_ids")
	private String saodangIds;
	
	@EntityField
	private int cd;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getFubenId(){
		return fubenId;
	}

	public  void setFubenId(Integer fubenId){
		this.fubenId = fubenId;
	}

	public Integer getState(){
		return state;
	}

	public  void setState(Integer state){
		this.state = state;
	}

	public Long getSaodangStart(){
		return saodangStart;
	}
	
	public Long getSaodangEnd(){
		return saodangStart + cd;
	}

	public  void setSaodangStart(Long saodangStart){
		this.saodangStart = saodangStart;
	}

	public String getSaodangIds(){
		return saodangIds;
	}

	public  void setSaodangIds(String saodangIds){
		this.saodangIds = saodangIds;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public int getCd() {
		return cd;
	}

	public void setCd(int cd) {
		this.cd = cd;
	}

	public RoleFuben copy(){
		RoleFuben result = new RoleFuben();
		result.setUserRoleId(getUserRoleId());
		result.setFubenId(getFubenId());
		result.setState(getState());
		result.setSaodangStart(getSaodangStart());
		result.setSaodangIds(getSaodangIds());
		result.setType(getType());
		result.setCd(getCd());
		return result;
	}
}
