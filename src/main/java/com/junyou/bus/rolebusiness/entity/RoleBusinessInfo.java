package com.junyou.bus.rolebusiness.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_business_info")
public class RoleBusinessInfo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	//pk
	@Column("pk_val")
	private Integer pkVal = 0;//pk值
	@Column("hui_time")
	private Long huiTime = 0l;//灰名时间戳
	@Column("hm_time")
	private Long hmTime = 0l;//红名时间戳
	@Column("hui_start_time")
	private Long huiStartTime = 0l;//灰名开始时间
	@Column("hm_start_time")
	private Long hmStartTime = 0l;//红名开始时间
	@Column("cur_fighter")
	private Long curFighter;//当前战斗力
	@Column("max_fighter")
	private Long maxFighter;//最大战斗力
	@Column("zhenqi")
	private Long zhenqi = 0l;//真气
	@Column("ronglu_val")
	private Integer rongluValue;//熔炉值
	@Column("xuantie_val")
	private Integer xuanTieValue;//玄铁值
	@Column("jump_val")
	private Integer jumpVal; //跳闪值
	@Column("rongyu")
	private Integer rongyu = 0;//荣誉值
	@Column("last_yh_time") 
	private Long lastYhTime = 0l;//最后吆喝的时间
	@Column("user_type_time") 
	private Long userTypeTime = 0l;//用户类型到期时间
	@Column("xiuwei")
	private Long xiuwei = 0l;//修为值

	public Long getLastYhTime() {
		return lastYhTime;
	}

	public void setLastYhTime(Long lastYhTime) {
		this.lastYhTime = lastYhTime;
	}

	/**
	 * 跳闪值
	 * @return
	 */
	public Integer getJumpVal() {
		return jumpVal;
	}

	public void setJumpVal(Integer jumpVal) {
		this.jumpVal = jumpVal;
	}

	
	public Long getCurFighter() {
		return curFighter;
	}

	public void setCurFighter(Long curFighter) {
		this.curFighter = curFighter;
	}

	public Long getMaxFighter() {
		return maxFighter == null ? 0 : maxFighter;
	}

	public void setMaxFighter(Long maxFighter) {
		this.maxFighter = maxFighter;
	}

	public Long getHuiStartTime() {
		return huiStartTime;
	}

	public void setHuiStartTime(Long huiStartTime) {
		this.huiStartTime = huiStartTime;
	}

	public Long getHmStartTime() {
		return hmStartTime;
	}

	public void setHmStartTime(Long hmStartTime) {
		this.hmStartTime = hmStartTime;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public int getPkVal(){
		return pkVal.intValue();
	}

	public  void setPkVal(Integer pkVal){
		this.pkVal = pkVal;
	}

	public Long getHuiTime(){
		return huiTime;
	}

	public  void setHuiTime(Long huiTime){
		this.huiTime = huiTime;
	}

	public Long getHmTime(){
		return hmTime;
	}

	public  void setHmTime(Long hmTime){
		this.hmTime = hmTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public Long getZhenqi() {
		return zhenqi;
	}

	public void setZhenqi(Long zhenqi) {
		this.zhenqi = zhenqi;
	}

	public Integer getRongluValue() {
		return rongluValue;
	}

	public void setRongluValue(Integer rongluValue) {
		this.rongluValue = rongluValue;
	}
	public Integer getXuanTieValue() {
		return xuanTieValue;
	}
	
	public void setXuanTieValue(Integer xuanTieValue) {
		this.xuanTieValue = xuanTieValue;
	}

	public Integer getRongyu() {
		return rongyu;
	}

	public void setRongyu(Integer rongyu) {
		this.rongyu = rongyu;
	}

	public Long getUserTypeTime() {
		return userTypeTime;
	}

	public void setUserTypeTime(Long userTypeTime) {
		this.userTypeTime = userTypeTime;
	}

	public Long getXiuwei() {
		return xiuwei;
	}

	public void setXiuwei(Long xiuwei) {
		if(xiuwei != null){
			this.xiuwei = xiuwei;
		}
	}

	public RoleBusinessInfo copy(){
		RoleBusinessInfo result = new RoleBusinessInfo();
		result.setUserRoleId(getUserRoleId());
		result.setPkVal(getPkVal());
		result.setHuiTime(getHuiTime());
		result.setHmTime(getHmTime());
		result.setHuiStartTime(getHuiStartTime());
		result.setHmStartTime(getHmStartTime());
		result.setCurFighter(getCurFighter());
		result.setMaxFighter(getMaxFighter());
		result.setZhenqi(getZhenqi());
		result.setRongluValue(getRongluValue());
		result.setRongyu(getRongyu());
		result.setJumpVal(getJumpVal());
		result.setLastYhTime(getLastYhTime());
		result.setXuanTieValue(getXuanTieValue());
		result.setUserTypeTime(getUserTypeTime());
		result.setXiuwei(getXiuwei());
		return result;
	}
	
	
}