package com.junyou.bus.qipan.configure.export;

import java.util.List;
import java.util.Map;

/**
 * 
 * @description 七日开服活动配置表 (全民修仙)
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class QiPanConfig {
	
	private Integer id;
	private Integer maxGe;//最大格数
	private Integer maxCiShu;//每日最多获得次数
	private Map<Integer, Float> zhuanMap;//转盘MAP
	private Map<Integer, Object[]> geWeiMap;//格位MAP<格位,object[]{物品ID,数量}>
	private List<Object[]> clientGw;//客服端需要
	private List<Object[]> clinetZM;//客户端需要
	
	public List<Object[]> getClientGw() {
		return clientGw;
	}
	public void setClientGw(List<Object[]> clientGw) {
		this.clientGw = clientGw;
	}
	public List<Object[]> getClinetZM() {
		return clinetZM;
	}
	public void setClinetZM(List<Object[]> clinetZM) {
		this.clinetZM = clinetZM;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMaxGe() {
		return maxGe;
	}
	public void setMaxGe(Integer maxGe) {
		this.maxGe = maxGe;
	}
	public Integer getMaxCiShu() {
		return maxCiShu;
	}
	public void setMaxCiShu(Integer maxCiShu) {
		this.maxCiShu = maxCiShu;
	}
	public Map<Integer, Float> getZhuanMap() {
		return zhuanMap;
	}
	public void setZhuanMap(Map<Integer, Float> zhuanMap) {
		this.zhuanMap = zhuanMap;
	}
	public Map<Integer, Object[]> getGeWeiMap() {
		return geWeiMap;
	}
	public void setGeWeiMap(Map<Integer, Object[]> geWeiMap) {
		this.geWeiMap = geWeiMap;
	}
	
	
}
