package com.junyou.bus.fuben.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("jianzhong_fuben")
public class JianzhongFuben extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("max_kill_monster")
	private Integer maxKillMonster;

	@Column("max_jingqi")
	private Integer maxJingqi;

	@Column("state")
	private Integer state;

	@Column("update_time")
	private Long updateTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getMaxKillMonster(){
		return maxKillMonster;
	}

	public  void setMaxKillMonster(Integer maxKillMonster){
		this.maxKillMonster = maxKillMonster;
	}

	public Integer getMaxJingqi(){
		return maxJingqi;
	}

	public  void setMaxJingqi(Integer maxJingqi){
		this.maxJingqi = maxJingqi;
	}

	public Integer getState(){
		return state;
	}

	public  void setState(Integer state){
		this.state = state;
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

	public JianzhongFuben copy(){
		JianzhongFuben result = new JianzhongFuben();
		result.setUserRoleId(getUserRoleId());
		result.setMaxKillMonster(getMaxKillMonster());
		result.setMaxJingqi(getMaxJingqi());
		result.setState(getState());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
