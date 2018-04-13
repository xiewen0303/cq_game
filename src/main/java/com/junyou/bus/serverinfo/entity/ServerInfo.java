package com.junyou.bus.serverinfo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Primary;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("server_info")
public class ServerInfo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;
		
	@Primary
	@Column("id")
	private Integer id;

	@Column("start_time")
	private Timestamp startTime;

	@Column("hefu_time")
	private Timestamp hefuTime;

	//平台id标识 
	@Column("platform_id")
	private String platformId;
	
	//平台服务器id (pps 1服 就是1)
	@Column("pt_server_id")
	private String ptServerId;
	
	@Column("prefix_id")
	private String prefixId;

	@Column("version")
	private String version;

	@Column("hefu_times")
	private Integer hefuTimes = 0;

	@Column("stop_times")
	private Integer stopTimes;

	@Column("send_cm_email")
	private Integer sendCmEmail;
	
	/**
	 * 是否需要更新 为空时不需要更新,不为空表示需要更新,实际值为主键类型
	 */
	@EntityField
	private int updateState = -1;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getHefuTime() {
		return hefuTime;
	}

	public void setHefuTime(Timestamp hefuTime) {
		this.hefuTime = hefuTime;
	}
	public String getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}
	public Integer getSendCmEmail() {
		return sendCmEmail;
	}

	public void setSendCmEmail(Integer sendCmEmail) {
		this.sendCmEmail = sendCmEmail;
	}
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	public Integer getHefuTimes() {
		return hefuTimes;
	}

	public void setHefuTimes(Integer hefuTimes) {
		this.hefuTimes = hefuTimes;
	}
	public Integer getStopTimes() {
		return stopTimes;
	}

	public void setStopTimes(Integer stopTimes) {
		this.stopTimes = stopTimes;
	}
	
	/**
	 * 停机次数加1
	 */
	public void addOneStopTimes(int type){
		this.updateState = type;
		this.stopTimes = getStopTimes() + 1;
	}
	
	/**
	 * 更新完成(状态改成不需要更新)
	 */
	public void updateFinishState(){
		this.updateState = -1;
	}
	
	/**
	 * 获取是否更新的状态
	 * @return
	 */
	public Integer getUpdateStage(){
		return this.updateState;
	}
	
	
	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getPtServerId() {
		return ptServerId;
	}

	public void setPtServerId(String ptServerId) {
		this.ptServerId = ptServerId;
	}

	/**
	 * 是否需要更新
	 * @return true:需要,false:不需要
	 */
	public boolean isUpdate() {
		return updateState == -1 ? false : true;
	}

	@Override	
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return getId().longValue();
	}
	
	public ServerInfo copy(){
		ServerInfo result = new ServerInfo();
		result.setId(getId());
		result.setStartTime(getStartTime());
		result.setHefuTime(getHefuTime());
		result.setPlatformId(getPlatformId());
		result.setPtServerId(getPtServerId());
		result.setPrefixId(getPrefixId());
		result.setSendCmEmail(getSendCmEmail());
		result.setVersion(getVersion());
		result.setHefuTimes(getHefuTimes());
		result.setStopTimes(getStopTimes());
		return result;
	}


}
