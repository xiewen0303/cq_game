package com.junyou.bus.map.entity;

import java.util.List;

import com.junyou.utils.collection.ReadOnlyList;


/**
 * @author LiuYu
 * 2015-7-28 上午10:44:17
 */
public class ActiveMapConfig {
	private Integer id;
	private String info;
	private String bg;
	private String beizhu;
	private Integer mapId;
	private String version;
	private Long startTime;
	private Long endTime;
	private List<Integer> subMapIds;
	private Object[] clientData;
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getBg() {
		return bg;
	}
	public void setBg(String bg) {
		this.bg = bg;
	}
	public Integer getMapId() {
		return mapId;
	}
	public void setMapId(Integer mapId) {
		this.mapId = mapId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public String getBeizhu() {
		return beizhu;
	}
	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}
	public List<Integer> getSubMapIds() {
		return subMapIds;
	}
	public void setSubMapIds(List<Integer> subMapIds) {
		this.subMapIds = new ReadOnlyList<>(subMapIds);
	}
	public Object[] getClientData() {
		if(clientData == null){
			clientData = new Object[]{bg,info,beizhu};
		}
		return clientData;
	}
	
}
