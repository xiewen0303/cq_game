package com.junyou.bus.jingji.entity;
import java.io.Serializable;

import com.junyou.bus.jingji.util.JingJiOutWarpper;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.RandomUtil;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_jingji")
public class RoleJingji extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("rank")
	private Integer rank;

	@Column("last_rank")
	private Integer lastRank;

	@Column("update_time")
	private Long updateTime;

	@Column("gift")
	private Integer gift;
	
	@Column("name")
	private String name;
	
	@Column("level")
	private int level;
	
	@Column("zplus")
	private long zplus;
	
	@Column("config_id")
	private int configId;
	
	@Column("zuoqi")
	private int zuoqi;
	
	@Column("tz_count")
	private int tzCount;
	
	@Column("cd_time")
	private Long cdTime;
	
	@Column("chibang")
	private Integer chibang;

	/**
	 * 默认0是正常的，1隐藏
	 */
	@Column("used")
	private Integer used;

	@Column("historyrank")
	private String historyrank;

	@Column("lastrefreshtime")
	private Long lastrefreshtime;

	@EntityField
	private int state;
	
	@EntityField
	private int targetRank;
	
	@EntityField
	private boolean isWatching;
	
	@EntityField
	private long fightTime;
	
	
	@EntityField
	private Object[] info;

	public Long getLastrefreshtime() {
		return lastrefreshtime;
	}

	public void setLastrefreshtime(Long lastrefreshtime) {
		this.lastrefreshtime = lastrefreshtime;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getRank(){
		return rank;
	}

	public  void setRank(Integer rank){
		this.rank = rank;
	}

	public Integer getLastRank(){
		return lastRank;
	}

	public  void setLastRank(Integer lastRank){
		this.lastRank = lastRank;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Integer getGift(){
		return gift;
	}

	public  void setGift(Integer gift){
		this.gift = gift;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}
	

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
		fightTime = GameSystemTime.getSystemMillTime();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getZplus() {
		return zplus;
	}

	public void setZplus(long zplus) {
		this.zplus = zplus;
	}
	

	public int getTzCount() {
		return tzCount;
	}

	public void setTzCount(int tzCount) {
		this.tzCount = tzCount;
	}

	public Long getCdTime() {
		return cdTime;
	}

	public void setCdTime(Long cdTime) {
		this.cdTime = cdTime;
	}

	public int getTargetRank() {
		return targetRank;
	}

	public void setTargetRank(int targetRank) {
		this.targetRank = targetRank;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public int getZuoqi() {
		return zuoqi;
	}

	public void setZuoqi(int zuoqi) {
		this.zuoqi = zuoqi;
	}

	public Integer getChibang() {
		return chibang;
	}

	public void setChibang(Integer chibang) {
		this.chibang = chibang;
	}
	
	public long getFightTime() {
		return fightTime;
	}

	public RoleJingji copy(){
		RoleJingji result = new RoleJingji();
		result.setUserRoleId(getUserRoleId());
		result.setRank(getRank());
		result.setLastRank(getLastRank());
		result.setUpdateTime(getUpdateTime());
		result.setGift(getGift());
		result.setState(getState());
		result.setName(getName());
		result.setLevel(getLevel());
		result.setZplus(getZplus());
		result.setTzCount(getTzCount());
		result.setCdTime(getCdTime());
		result.setZuoqi(getZuoqi());
		result.setConfigId(getConfigId());
		result.setChibang(getChibang());
		result.setUsed(getUsed());
		result.setLastrefreshtime(getLastrefreshtime());
		result.setHistoryrank(getHistoryrank());
		return result;
	}

	public Object[] getInfo() {
		if(info == null){
			info = JingJiOutWarpper.getRoleJingjiVo(this);
		}else{
			long tempZplus = getZplus();
			if(tempZplus == 0){
				tempZplus = RandomUtil.getIntRandomValue(1000, 8000);
			}
			info[1] = getName();
			info[2] = getLevel();
			info[3] = tempZplus;
			info[4] = getRank();
			info[5] = getConfigId();
			info[6] = getZuoqi();
		}
		return info;
	}

	public boolean isWatching() {
		return isWatching;
	}

	public void setWatching(boolean isFighting) {
		this.isWatching = isFighting;
	}

	public Integer getUsed() {
		return used;
	}

	public void setUsed(Integer used) {
		this.used = used;
	}

	public String getHistoryrank() {
		return historyrank;
	}

	public void setHistoryrank(String historyrank) {
		this.historyrank = historyrank;
	}
}
