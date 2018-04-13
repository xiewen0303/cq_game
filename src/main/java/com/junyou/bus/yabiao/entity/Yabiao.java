package com.junyou.bus.yabiao.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("yabiao")
public class Yabiao extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("yb_times")
	private Integer ybTimes;

	@Column("jb_times")
	private Integer jbTimes;
	
	@Column("refresh_times")
	private Integer refreshTimes;
	
	@Column("current_biao_che")
	private Integer currentBiaoChe;

	@Column("update_time")
	private Long updateTime;

	@Column("create_time")
	private Timestamp createTime;
	@Column("renwu_count")
	private Integer renwuCount;
	
	public Integer getRenwuCount() {
		return renwuCount;
	}

	public void setRenwuCount(Integer renwuCount) {
		this.renwuCount = renwuCount;
	}

	@EntityField
	private int bcId;//镖车Id
	@EntityField
	private int state;//镖车状态
	
	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getRefreshTimes() {
		return refreshTimes;
	}

	public void setRefreshTimes(Integer refreshTimes) {
		this.refreshTimes = refreshTimes;
	}

	/**
	 * 获取押镖次数
	 * @return
	 */
	public Integer getYbTimes(){
		return ybTimes;
	}

	/**
	 * 获取劫镖次数
	 * @param ybTimes
	 */
	public  void setYbTimes(Integer ybTimes){
		this.ybTimes = ybTimes;
	}

	/**
	 * 获取劫镖次数
	 * @return
	 */
	public Integer getJbTimes(){
		return jbTimes;
	}

	public Integer getCurrentBiaoChe() {
		return currentBiaoChe;
	}

	public void setCurrentBiaoChe(Integer currentBiaoChe) {
		this.currentBiaoChe = currentBiaoChe;
	}

	/**
	 * 设置劫镖次数
	 * @param jbTimes
	 */
	public  void setJbTimes(Integer jbTimes){
		this.jbTimes = jbTimes;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	/**
	 * 获取正在进行的镖车Id
	 * @return
	 */
	public int getBcId() {
		return bcId;
	}

	public void setBcId(int bcId) {
		this.bcId = bcId;
	}

	/**
	 * 获取正在进行的镖车状态（默认为0：未接）
	 * @return
	 */
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public Yabiao copy(){
		Yabiao result = new Yabiao();
		result.setUserRoleId(getUserRoleId());
		result.setYbTimes(getYbTimes());
		result.setJbTimes(getJbTimes());
		result.setCurrentBiaoChe(getCurrentBiaoChe());
		result.setRefreshTimes(getRefreshTimes());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		result.setRenwuCount(getRenwuCount());
		return result;
	}
}
