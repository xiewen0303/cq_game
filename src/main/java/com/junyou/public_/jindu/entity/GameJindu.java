package com.junyou.public_.jindu.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("game_jindu")
public class GameJindu extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("time")
	private Long time;

	@Column("game_role")
	private Integer gameRole;

	@Column("create_role")
	private Integer createRole;

	@Column("first_task")
	private Integer firstTask;
	
	@Column("game_role_other")
	private Integer gameRoleOther;
	
	@Column("create_role_other")
	private Integer createRoleOther;
	
	@Column("first_task_other")
	private Integer firstTaskOther;

	public Long getTime(){
		return time;
	}

	public  void setTime(Long time){
		this.time = time;
	}

	public Integer getGameRole(){
		return gameRole;
	}

	public  void setGameRole(Integer gameRole){
		this.gameRole = gameRole;
	}

	public Integer getCreateRole(){
		return createRole;
	}

	public  void setCreateRole(Integer createRole){
		this.createRole = createRole;
	}

	public Integer getFirstTask(){
		return firstTask;
	}

	public  void setFirstTask(Integer firstTask){
		this.firstTask = firstTask;
	}

	@Override
	public String getPirmaryKeyName() {
		return "time";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return time;
	}

	public Integer getGameRoleOther() {
		return gameRoleOther;
	}

	public void setGameRoleOther(Integer gameRoleOther) {
		this.gameRoleOther = gameRoleOther;
	}

	public Integer getCreateRoleOther() {
		return createRoleOther;
	}

	public void setCreateRoleOther(Integer createRoleOther) {
		this.createRoleOther = createRoleOther;
	}

	public Integer getFirstTaskOther() {
		return firstTaskOther;
	}

	public void setFirstTaskOther(Integer firstTaskOther) {
		this.firstTaskOther = firstTaskOther;
	}

	public GameJindu copy(){
		GameJindu result = new GameJindu();
		result.setTime(getTime());
		result.setGameRole(getGameRole());
		result.setCreateRole(getCreateRole());
		result.setFirstTask(getFirstTask());
		result.setCreateRoleOther(getCreateRoleOther());
		result.setFirstTaskOther(getFirstTaskOther());
		result.setGameRoleOther(getGameRoleOther());
		return result;
	}
}
