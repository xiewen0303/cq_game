package com.junyou.public_.guild.entity;
import java.io.Serializable;

import com.junyou.public_.guild.util.GuildOutPutUtil;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("guild_member")
public class GuildMember extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("guild_id")
	private Long guildId;

	@Column("postion")
	private Integer postion;
	
	@Column("today_gongxian")
	private Long todayGongxian;

	@Column("cur_gongxian")
	private Long curGongxian;

	@Column("total_gongxian")
	private Long totalGongxian;

	@Column("box_state")
	private Integer boxState;

	@Column("update_time")
	private Long updateTime;

	@Column("enter_time")
	private Long enterTime;

	@EntityField
	private Long offlineTime;
	
	@EntityField
	private boolean online;
	
	@EntityField
	private long zplus;
	
	@EntityField
	private int configId;
	
	@EntityField
	private int level;
	
	@EntityField
	private int vip;
	
	@EntityField
	private String name;
	
	@EntityField
	private Object[] memberVo;

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Long getGuildId(){
		return guildId;
	}

	public  void setGuildId(Long guildId){
		this.guildId = guildId;
	}

	public Integer getPostion(){
		return postion;
	}

	public  void setPostion(Integer postion){
		this.postion = postion;
		if(memberVo != null){
			memberVo[7] = postion;
		}
	}

	public Long getCurGongxian(){
		return curGongxian;
	}

	public  void setCurGongxian(Long curGongxian){
		this.curGongxian = curGongxian;
	}

	public Long getTotalGongxian(){
		return totalGongxian;
	}

	public  void setTotalGongxian(Long totalGongxian){
		this.totalGongxian = totalGongxian;
		if(memberVo != null){
			memberVo[6] = totalGongxian;
		}
	}

	public Integer getBoxState(){
		return boxState;
	}

	public  void setBoxState(Integer boxState){
		this.boxState = boxState;
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
	
	public Long getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(Long offlineTime) {
		this.offlineTime = offlineTime;
	}

	public boolean isOnline() {
		return online;
	}

	public Long getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(Long enterTime) {
		this.enterTime = enterTime;
	}

	public void setOnline(boolean online) {
		this.online = online;
		if(memberVo != null){
			memberVo[8] = online ? 0 : offlineTime;
		}
	}

	public long getZplus() {
		return zplus;
	}

	public void setZplus(Long zplus) {
		this.zplus = zplus;
		if(memberVo != null){
			memberVo[4] = zplus;
		}
	}

	public Long getTodayGongxian() {
		return todayGongxian;
	}

	public void setTodayGongxian(Long todayGongxian) {
		this.todayGongxian = todayGongxian;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
		if(memberVo != null){
			memberVo[5] = configId;
		}
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
		if(memberVo != null){
			memberVo[3] = level;
		}
	}

	public int getVip() {
		return vip;
	}

	public void setVip(Integer vip) {
		this.vip = vip;
		if(memberVo != null){
			memberVo[2] = vip;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		if(memberVo != null){
			memberVo[1] = name;
		}
	}
	
	public Object[] getMemberVo() {
		if(memberVo == null){
			memberVo = GuildOutPutUtil.getMemberVo(this);
		}
		return memberVo;
	}

	public GuildMember copy(){
		GuildMember result = new GuildMember();
		result.setUserRoleId(getUserRoleId());
		result.setGuildId(getGuildId());
		result.setPostion(getPostion());
		result.setCurGongxian(getCurGongxian());
		result.setTotalGongxian(getTotalGongxian());
		result.setBoxState(getBoxState());
		result.setUpdateTime(getUpdateTime());
		result.setOfflineTime(getOfflineTime());
		result.setOnline(isOnline());
		result.setZplus(getZplus());
		result.setConfigId(getConfigId());
		result.setLevel(getLevel());
		result.setVip(getVip());
		result.setName(getName());
		result.setTodayGongxian(getTodayGongxian());
		return result;
	}
	public void addGongxian(int gongxian){
		setTotalGongxian(totalGongxian + gongxian);
		setCurGongxian(curGongxian + gongxian);
		setTodayGongxian(todayGongxian + gongxian);
	}
}
