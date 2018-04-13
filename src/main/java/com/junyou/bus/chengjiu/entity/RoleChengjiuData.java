package com.junyou.bus.chengjiu.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_chengjiu_data")
public class RoleChengjiuData extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("update_time")
	private Timestamp updateTime;

	@Column("create_time")
	private Timestamp createTime;

	@Column("day_task_count")
	private Integer dayTaskCount;

	@Column("guild_task_count")
	private Integer guildTaskCount;

	@Column("gouyu_fuben_count")
	private Integer gouyuFubenCount;

	@Column("dihuo_fuben_count")
	private Integer dihuoFubenCount;

	@Column("snasheng_fuben_count")
	private Integer snashengFubenCount;

	@Column("xianmobang_count")
	private Integer xianmobangCount;

	@Column("cheng_biao_count")
	private Integer chengBiaoCount;

	@Column("kill_boss_count")
	private Integer killBossCount;

	@Column("xizao_feizao_count")
	private Integer xizaoFeizaoCount;

	@Column("shichang_chushou_count")
	private Integer shichangChushouCount;

	@Column("danri_xiaofei_number")
	private Integer danriXiaofeiNumber;

	@Column("danci_recharge_number")
	private Integer danciRechargeNumber;

	@Column("leiji_recharge_number")
	private Integer leijiRechargeNumber;

	@Column("leiji_xunbao_count")
	private Integer leijiXunbaoCount;

	@Column("leiji_qiandao_count")
	private Integer leijiQiandaoCount;

	@Column("kill_monter_count")
	private Integer killMonterCount;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Timestamp getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Timestamp updateTime){
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	public Integer getDayTaskCount(){
		return dayTaskCount;
	}

	public  void setDayTaskCount(Integer dayTaskCount){
		this.dayTaskCount = dayTaskCount;
	}

	public Integer getGuildTaskCount(){
		return guildTaskCount;
	}

	public  void setGuildTaskCount(Integer guildTaskCount){
		this.guildTaskCount = guildTaskCount;
	}

	public Integer getGouyuFubenCount(){
		return gouyuFubenCount;
	}

	public  void setGouyuFubenCount(Integer gouyuFubenCount){
		this.gouyuFubenCount = gouyuFubenCount;
	}

	public Integer getDihuoFubenCount(){
		return dihuoFubenCount;
	}

	public  void setDihuoFubenCount(Integer dihuoFubenCount){
		this.dihuoFubenCount = dihuoFubenCount;
	}

	public Integer getSnashengFubenCount(){
		return snashengFubenCount;
	}

	public  void setSnashengFubenCount(Integer snashengFubenCount){
		this.snashengFubenCount = snashengFubenCount;
	}

	public Integer getXianmobangCount(){
		return xianmobangCount;
	}

	public  void setXianmobangCount(Integer xianmobangCount){
		this.xianmobangCount = xianmobangCount;
	}

	public Integer getChengBiaoCount(){
		return chengBiaoCount;
	}

	public  void setChengBiaoCount(Integer chengBiaoCount){
		this.chengBiaoCount = chengBiaoCount;
	}

	public Integer getKillBossCount(){
		return killBossCount;
	}

	public  void setKillBossCount(Integer killBossCount){
		this.killBossCount = killBossCount;
	}

	public Integer getXizaoFeizaoCount(){
		return xizaoFeizaoCount;
	}

	public  void setXizaoFeizaoCount(Integer xizaoFeizaoCount){
		this.xizaoFeizaoCount = xizaoFeizaoCount;
	}

	public Integer getShichangChushouCount(){
		return shichangChushouCount;
	}

	public  void setShichangChushouCount(Integer shichangChushouCount){
		this.shichangChushouCount = shichangChushouCount;
	}

	public Integer getDanriXiaofeiNumber(){
		return danriXiaofeiNumber;
	}

	public  void setDanriXiaofeiNumber(Integer danriXiaofeiNumber){
		this.danriXiaofeiNumber = danriXiaofeiNumber;
	}

	public Integer getDanciRechargeNumber(){
		return danciRechargeNumber;
	}

	public  void setDanciRechargeNumber(Integer danciRechargeNumber){
		this.danciRechargeNumber = danciRechargeNumber;
	}

	public Integer getLeijiRechargeNumber(){
		return leijiRechargeNumber;
	}

	public  void setLeijiRechargeNumber(Integer leijiRechargeNumber){
		this.leijiRechargeNumber = leijiRechargeNumber;
	}

	public Integer getLeijiXunbaoCount(){
		return leijiXunbaoCount;
	}

	public  void setLeijiXunbaoCount(Integer leijiXunbaoCount){
		this.leijiXunbaoCount = leijiXunbaoCount;
	}

	public Integer getLeijiQiandaoCount(){
		return leijiQiandaoCount;
	}

	public  void setLeijiQiandaoCount(Integer leijiQiandaoCount){
		this.leijiQiandaoCount = leijiQiandaoCount;
	}

	public Integer getKillMonterCount(){
		return killMonterCount;
	}

	public  void setKillMonterCount(Integer killMonterCount){
		this.killMonterCount = killMonterCount;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleChengjiuData copy(){
		RoleChengjiuData result = new RoleChengjiuData();
		result.setUserRoleId(getUserRoleId());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		result.setDayTaskCount(getDayTaskCount());
		result.setGuildTaskCount(getGuildTaskCount());
		result.setGouyuFubenCount(getGouyuFubenCount());
		result.setDihuoFubenCount(getDihuoFubenCount());
		result.setSnashengFubenCount(getSnashengFubenCount());
		result.setXianmobangCount(getXianmobangCount());
		result.setChengBiaoCount(getChengBiaoCount());
		result.setKillBossCount(getKillBossCount());
		result.setXizaoFeizaoCount(getXizaoFeizaoCount());
		result.setShichangChushouCount(getShichangChushouCount());
		result.setDanriXiaofeiNumber(getDanriXiaofeiNumber());
		result.setDanciRechargeNumber(getDanciRechargeNumber());
		result.setLeijiRechargeNumber(getLeijiRechargeNumber());
		result.setLeijiXunbaoCount(getLeijiXunbaoCount());
		result.setLeijiQiandaoCount(getLeijiQiandaoCount());
		result.setKillMonterCount(getKillMonterCount());
		return result;
	}
}
