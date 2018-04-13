package com.junyou.bus.daytask.entity;
import java.io.Serializable;

import com.junyou.bus.task.entity.AbsTask;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.IEntity;

@Table("task_day")
public class TaskDay extends AbsTask implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;
	
	@Column("id")
	private Long id; //主键ID
	
	@Column("task_day_type")
	private int taskDayType; 
	
	@Column("user_role_id")
	private Long userRoleId;

	@Column("task_id")
	private Integer taskId;

	@Column("times")
	private Integer times;

	@Column("last_opt_time")
	private Long lastOptTime;

	@Column("kill_count")
	private Integer killCount;

	@Column("loop_id")
	private Integer loopId;
	
	@Column("status")
	private Integer status;//任务完成状态
	@Column("renwu_count")
	private Integer renwuCount;
	
	@EntityField
	private String monsterId; // 怪物配置Id
	
	@EntityField
	private int needKillCount;// 需要击杀的数量
	
	@EntityField
	private int maxTimes = 0;// 日常最多可完成的次数
	
	@EntityField
	private boolean addListener = false;
	  
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTaskDayType() {
		return taskDayType;
	}

	public void setTaskDayType(int taskDayType) {
		this.taskDayType = taskDayType;
	}

	public int getMaxTimes() {
		return maxTimes;
	}

	public void setMaxTimes(int maxTimes) {
		this.maxTimes = maxTimes;
	}

	public String getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(String monsterId) {
		this.monsterId = monsterId;
	}

	public int getNeedKillCount() {
		return needKillCount;
	}

	public void setNeedKillCount(int needKillCount) {
		this.needKillCount = needKillCount;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getTaskId(){
		return taskId;
	}

	public  void setTaskId(Integer taskId){
		this.taskId = taskId;
	}

	public Integer getTimes(){
		return times;
	}

	public  void setTimes(Integer times){
		this.times = times;
	}

	public Long getLastOptTime(){
		return lastOptTime;
	}

	public  void setLastOptTime(Long lastOptTime){
		this.lastOptTime = lastOptTime;
	}

	public Integer getKillCount(){
		return killCount;
	}

	public  void setKillCount(Integer killCount){
		this.killCount = killCount;
	}

	public Integer getLoopId(){
		return loopId;
	}

	public  void setLoopId(Integer loopId){
		this.loopId = loopId;
	}
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public TaskDay copy(){
		TaskDay result = new TaskDay();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setTaskId(getTaskId());
		result.setTimes(getTimes());
		result.setLastOptTime(getLastOptTime());
		result.setKillCount(getKillCount());
		result.setLoopId(getLoopId());
		result.setMonsterId(getMonsterId());
		result.setNeedKillCount(getNeedKillCount());
		result.setMaxTimes(getMaxTimes());
		result.setTaskDayType(getTaskDayType());
		result.setStatus(getStatus());
		result.setRenwuCount(getRenwuCount());
		return result;
	}
	
	public Integer getRenwuCount() {
		return renwuCount;
	}

	public void setRenwuCount(Integer renwuCount) {
		this.renwuCount = renwuCount;
	}

	public boolean isAddListener() {
		return addListener;
	}

	public void setAddListener(boolean addListener) {
		this.addListener = addListener;
	}

	@Override
	public String getTargetId() {
		return monsterId;
	}
	
}
