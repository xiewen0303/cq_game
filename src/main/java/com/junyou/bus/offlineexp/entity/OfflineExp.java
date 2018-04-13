package com.junyou.bus.offlineexp.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("offline_exp")
public class OfflineExp extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("offline_total")
	private Long offlineTotal;

	@Column("offline_exp")
	private Long offlineExp;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Long getOfflineTotal(){
		return offlineTotal;
	}

	public  void setOfflineTotal(Long offlineTotal){
		this.offlineTotal = offlineTotal;
	}

	public Long getOfflineExp(){
		if(offlineExp == null){
			return 0l;
		}
		return offlineExp;
	}

	public  void setOfflineExp(Long offlineExp){
		this.offlineExp = offlineExp;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public OfflineExp copy(){
		OfflineExp result = new OfflineExp();
		result.setUserRoleId(getUserRoleId());
		result.setOfflineTotal(getOfflineTotal());
		result.setOfflineExp(getOfflineExp());
		return result;
	}
}
