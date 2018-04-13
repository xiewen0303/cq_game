package com.junyou.bus.marry.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_marry_info")
public class RoleMarryInfo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("state")
	private Integer state;

	@Column("target_role_id")
	private Long targetRoleId;

	@Column("yuanfen")
	private Integer yuanfen;

	@Column("love_val")
	private Long loveVal;

	@Column("xinwu")
	private Integer xinwu;

	@Column("lf_level")
	private Integer lfLevel;

	@Column("marry_time")
	private Long marryTime;

	@Column("update_time")
	private Long updateTime;

	@Column("xinqing")
	private String xinqing;
	
	@EntityField
	private Object[] targetInfo;//配偶信息，防止反复查库
	
	@EntityField
	private Integer qiuhunXinwu;//求婚信物，为null或小于1则不在求婚中
	
	@EntityField
	private boolean askDivorce;//是否请求和平分手
	
	@EntityField
	private boolean openDinghun;
	
	@EntityField
	private boolean openChange;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getState(){
		return state;
	}

	public  void setState(Integer state){
		this.state = state;
	}

	public Long getTargetRoleId(){
		return targetRoleId;
	}

	public  void setTargetRoleId(Long targetRoleId){
		this.targetRoleId = targetRoleId;
	}

	public Integer getYuanfen(){
		return yuanfen;
	}

	public  void setYuanfen(Integer yuanfen){
		this.yuanfen = yuanfen;
	}

	public Long getLoveVal(){
		return loveVal;
	}

	public  void setLoveVal(Long loveVal){
		this.loveVal = loveVal;
	}

	public Integer getXinwu(){
		return xinwu;
	}

	public  void setXinwu(Integer xinwu){
		this.xinwu = xinwu;
	}

	public Integer getLfLevel(){
		return lfLevel;
	}

	public  void setLfLevel(Integer lfLevel){
		this.lfLevel = lfLevel;
	}

	public Long getMarryTime(){
		return marryTime;
	}

	public  void setMarryTime(Long marryTime){
		this.marryTime = marryTime;
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

	public Object[] getTargetInfo() {
		return targetInfo;
	}

	public void setTargetInfo(Object[] targetInfo) {
		this.targetInfo = targetInfo;
	}

	public Integer getQiuhunXinwu() {
		return qiuhunXinwu;
	}

	public void setQiuhunXinwu(Integer qiuhunXinwu) {
		this.qiuhunXinwu = qiuhunXinwu;
	}

	public boolean isAskDivorce() {
		return askDivorce;
	}

	public void setAskDivorce(boolean askDivorce) {
		this.askDivorce = askDivorce;
	}

	public boolean isOpenDinghun() {
		return openDinghun;
	}

	public void setOpenDinghun(boolean openDinghun) {
		this.openDinghun = openDinghun;
	}

	public boolean isOpenChange() {
		return openChange;
	}

	public void setOpenChange(boolean openChange) {
		this.openChange = openChange;
	}

	public String getXinqing() {
		return xinqing;
	}

	public void setXinqing(String xinqing) {
		this.xinqing = xinqing;
	}

	public RoleMarryInfo copy(){
		RoleMarryInfo result = new RoleMarryInfo();
		result.setUserRoleId(getUserRoleId());
		result.setState(getState());
		result.setTargetRoleId(getTargetRoleId());
		result.setYuanfen(getYuanfen());
		result.setLoveVal(getLoveVal());
		result.setXinwu(getXinwu());
		result.setLfLevel(getLfLevel());
		result.setMarryTime(getMarryTime());
		result.setUpdateTime(getUpdateTime());
		result.setXinqing(getXinqing());
		return result;
	}
}
