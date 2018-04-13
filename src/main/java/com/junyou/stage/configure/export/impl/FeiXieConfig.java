package com.junyou.stage.configure.export.impl;

import java.util.List;

import com.junyou.utils.lottery.Lottery;

/**
 * 
 * @description 飞鞋表 
 *
 * @author DaoZhen Yuan
 * @date 2015-03-11 15:45:46
 */
public class FeiXieConfig {

	private String id;

	private boolean isFree;

	private Integer mapId;

	/**
	 * 坐标集
	 */
	private List<int[]> zuobiaos;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 是否是免费飞
	 * @return true:是
	 */
	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	public Integer getMapId() {
		return mapId;
	}

	public void setMapId(Integer mapId) {
		this.mapId = mapId;
	}

	public List<int[]> getZuobiaos() {
		return zuobiaos;
	}
	
	/**
	 * 随机获取一个飞鞋坐标点
	 * @return
	 */
	public int[] getZuobiaoByLottery(){
		if(zuobiaos == null || zuobiaos.size() == 0){
			return null;
		}else if(zuobiaos.size() == 1){
			return zuobiaos.get(0);
		}else{
			int randomValue = Lottery.roll(zuobiaos.size());
			return zuobiaos.get(randomValue);
		}
	}

	public void setZuobiaos(List<int[]> zuobiaos) {
		this.zuobiaos = zuobiaos;
	}
}
