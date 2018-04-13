package com.junyou.stage.configure.export.impl;

import java.util.ArrayList;
import java.util.List;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 地图非战斗资源配置表 
 *
 * @author DaoZheng Yuan
 * @date 2013-10-26 16:52:02
 */
public class ZiYuanConfig extends AbsVersion{
	
	private Integer id;
	
	private String name;
	
	private int type;
	
	private String mapId;
	
	private List<Integer[]> zuobiao;
	
	private Integer collectTime;

	private Integer refreshTime;

	private String collectItem;
	
	private Integer collectItemCount;
	
	private Integer delaTime;
	
	private int delaNumber;
	
	private boolean isNotice;
	
	private Object[] successMsg;
	
	public int getDelaNumber() {
		return delaNumber;
	}

	public void setDelaNumber(int delaNumber) {
		this.delaNumber = delaNumber;
	}

	public boolean isNotice() {
		return isNotice;
	}

	public void setNotice(boolean isNotice) {
		this.isNotice = isNotice;
	}

	public Integer getDelaTime() {
		return delaTime;
	}

	public void setDelaTime(Integer delaTime) {
		this.delaTime = delaTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	
	public String getCollectItem() {
		return collectItem;
	}

	public void setCollectItem(String collectItem) {
		this.collectItem = collectItem;
	}

	
	public Integer getCollectItemCount() {
		return collectItemCount;
	}

	public void setCollectItemCount(Integer collectItemCount) {
		this.collectItemCount = collectItemCount;
	}

	public Integer getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Integer collectTime) {
		this.collectTime = collectTime * 1000;
	}

	public Integer getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(Integer refreshTime) {
		this.refreshTime = refreshTime;
	}

	public List<Integer[]> getZuobiao() {
		List<Integer[]> tmp = null;
		if(zuobiao != null && zuobiao.size() > 0){
			tmp = new ArrayList<Integer[]>();
			tmp.addAll(zuobiao);
			return tmp;
		}else{
			return null;
		}
	}

	public void setZuobiao(List<Integer[]> zuobiao) {
		this.zuobiao = zuobiao;
	}

	public Object[] getSuccessMsg() {
		if(successMsg == null){
			successMsg = new Object[]{1,collectTime};
		}
		return successMsg;
	}

	

}
