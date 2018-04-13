package com.junyou.bus.kfjingji.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("kuafu_jingji")
public class KuafuJingji extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("change_count")
	private Integer changeCount;

	@Column("cd_time")
	private Long cdTime;

	@Column("tiaozhan_count")
	private Integer tiaozhanCount;

	@Column("rank")
	private Integer rank;
	
	@Column("last_rank")
	private Integer lastRank;

	@Column("gift")
	private Integer gift;

	@Column("fight1")
	private Integer fight1;

	@Column("fight2")
	private Integer fight2;

	@Column("fight3")
	private Integer fight3;

	@Column("fight4")
	private Integer fight4;

	@Column("update_time")
	private Long updateTime;
	
	@EntityField
	private long targetRole;
	@EntityField
	private int targetRank;
	@EntityField
	private boolean watching;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getChangeCount(){
		return changeCount;
	}

	public  void setChangeCount(Integer changeCount){
		this.changeCount = changeCount;
	}

	public Long getCdTime(){
		return cdTime;
	}

	public  void setCdTime(Long cdTime){
		this.cdTime = cdTime;
	}

	public Integer getTiaozhanCount(){
		return tiaozhanCount;
	}

	public  void setTiaozhanCount(Integer tiaozhanCount){
		this.tiaozhanCount = tiaozhanCount;
	}

	public Integer getRank(){
		return rank;
	}

	public  void setRank(Integer rank){
		this.rank = rank;
	}

	public Integer getGift(){
		return gift;
	}

	public  void setGift(Integer gift){
		this.gift = gift;
	}

	public Integer getFight1(){
		return fight1;
	}

	public  void setFight1(Integer fight1){
		this.fight1 = fight1;
	}

	public Integer getFight2(){
		return fight2;
	}

	public  void setFight2(Integer fight2){
		this.fight2 = fight2;
	}

	public Integer getFight3(){
		return fight3;
	}

	public  void setFight3(Integer fight3){
		this.fight3 = fight3;
	}

	public Integer getFight4(){
		return fight4;
	}

	public  void setFight4(Integer fight4){
		this.fight4 = fight4;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Integer getLastRank() {
		return lastRank;
	}

	public void setLastRank(Integer lastRank) {
		this.lastRank = lastRank;
	}

	public long getTargetRole() {
		return targetRole;
	}

	public void setTargetRole(long targetRole) {
		this.targetRole = targetRole;
	}
	
	public int getTargetRank() {
		return targetRank;
	}

	public void setTargetRank(int targetRank) {
		this.targetRank = targetRank;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public boolean isWatching() {
		return watching;
	}

	public void setWatching(boolean watching) {
		this.watching = watching;
	}

	public KuafuJingji copy(){
		KuafuJingji result = new KuafuJingji();
		result.setUserRoleId(getUserRoleId());
		result.setChangeCount(getChangeCount());
		result.setCdTime(getCdTime());
		result.setTiaozhanCount(getTiaozhanCount());
		result.setRank(getRank());
		result.setGift(getGift());
		result.setFight1(getFight1());
		result.setFight2(getFight2());
		result.setFight3(getFight3());
		result.setFight4(getFight4());
		result.setUpdateTime(getUpdateTime());
		result.setLastRank(getLastRank());
		return result;
	}
}
