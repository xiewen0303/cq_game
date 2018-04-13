package com.junyou.bus.stagecontroll;

import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.stage.export.RoleStageWrapper;

public class LoginClientVo {

	private RoleStageWrapper roleStage;
	
	private RoleState roleState;
	
	private RoleWrapper loginRole;
	
	private DiTuConfig diTuConfig;
	
	private Object setting;
	
	private int vipLevel;
	
	//外显衣服
	private Object[] clothIds;
	//技能列表
	private Object skillList;
	
	//金币
	private long jb;
	//元宝
	private long yb;
	//绑定元宝
	private long bindYb;
	//账号内没领取的元宝
	private int recevieYb;
	
	//魂石
	private int hunValue;
	
	//官职等级
	private int gzLevel = 0;
	//官印等级
	private int gyLevel = 0;
	
	private String guildId;
	
	private String guildName;
	
	private int pkValue;//PK值
	
	private String teamId;
	
	private int shenQiJiFen;//神器积分
	
	private boolean isHuiMin;//是否是灰名
	
	private int fxCount;//飞鞋次数
	
	private int qsqhId;//全身强化配置id
	
	private String titleRes;//称号的动画res
	
	public String getTitleRes() {
		return titleRes;
	}
	public void setTitleRes(String titleRes) {
		this.titleRes = titleRes;
	}
	public int getFxCount() {
		return fxCount;
	}
	public void setFxCount(int fxCount) {
		this.fxCount = fxCount;
	}
	public int getRecevieYb() {
		return recevieYb;
	}
	public void setRecevieYb(int recevieYb) {
		this.recevieYb = recevieYb;
	}
	/**
	 * 是否是灰名
	 * @return
	 */
	public boolean isHuiMin() {
		return isHuiMin;
	}
	public void setHuiMin(boolean isHuiMin) {
		this.isHuiMin = isHuiMin;
	}
	public int getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	public String getGuildId() {
		return guildId;
	}
	public void setGuildId(String guildId) {
		this.guildId = guildId;
	}
	public String getGuildName() {
		return guildName;
	}
	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}
	public RoleState getRoleState() {
		return roleState;
	}
	public void setRoleState(RoleState roleState) {
		this.roleState = roleState;
	}
	public Object[] getClothIds() {
		return clothIds;
	}
	public void setClothIds(Object[] clothIds) {
		this.clothIds = clothIds;
	}
	public Object getSkillList() {
		return skillList;
	}
	public void setSkillList(Object skillList) {
		this.skillList = skillList;
	}
	public RoleStageWrapper getRoleStage() {
		return roleStage;
	}
	public void setRoleStage(RoleStageWrapper roleStage) {
		this.roleStage = roleStage;
	}
	
	public RoleWrapper getLoginRole() {
		return loginRole;
	}
	public void setLoginRole(RoleWrapper loginRole) {
		this.loginRole = loginRole;
	}
	
	public DiTuConfig getDiTuConfig() {
		return diTuConfig;
	}
	public void setDiTuConfig(DiTuConfig diTuConfig) {
		this.diTuConfig = diTuConfig;
	}
	
	
	
	public Object getSetting() {
		return setting;
	}
	public void setSetting(Object setting) {
		this.setting = setting;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public long getJb() {
		return jb;
	}
	public void setJb(long jb) {
		this.jb = jb;
	}
	public long getYb() {
		return yb;
	}
	public void setYb(long yb) {
		this.yb = yb;
	}
	public long getBindYb() {
		return bindYb;
	}
	public void setBindYb(long bindYb) {
		this.bindYb = bindYb;
	}
	public int getHunValue() {
		return hunValue;
	}
	public void setHunValue(int hunValue) {
		this.hunValue = hunValue;
	}
	public int getGzLevel() {
		return gzLevel;
	}
	public void setGzLevel(int gzLevel) {
		this.gzLevel = gzLevel;
	}
	public int getGyLevel() {
		return gyLevel;
	}
	public void setGyLevel(int gyLevel) {
		this.gyLevel = gyLevel;
	}
	public int getPkValue() {
		return pkValue;
	}
	public void setPkValue(int pkValue) {
		this.pkValue = pkValue;
	}
	public int getShenQiJiFen() {
		return shenQiJiFen;
	}
	public void setShenQiJiFen(int shenQiJiFen) {
		this.shenQiJiFen = shenQiJiFen;
	}
	public int getQsqhId() {
		return qsqhId;
	}
	public void setQsqhId(int qsqhId) {
		this.qsqhId = qsqhId;
	}
}