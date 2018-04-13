package com.junyou.bus.kaifuactivity.configure.export;

import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;

/**
 * 
 * @description 七日开服活动配置表 (全民修仙)
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class KaiFuYaoShenMoYinConfig {
	
	private Map<String, GoodsConfigureVo> jiangitem;
	private Object[] itemClientMap;
	private String emailItem; 

	private Integer id;

	private Integer min;
	private Integer max;
	private Integer moYinLevel;
	public Integer getMoYinLevel() {
		return moYinLevel;
	}

	public void setMoYinLevel(Integer moYinLevel) {
		this.moYinLevel = moYinLevel;
	}

	private String showitem;

	private String des;
	
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}


	public String getShowitem() {
		return showitem;
	}

	public void setShowitem(String showitem) {
		this.showitem = showitem;
	}

	public Map<String, GoodsConfigureVo> getJiangitem() {
		return jiangitem;
	}

	public void setJiangitem(Map<String, GoodsConfigureVo> jiangitem) {
		this.jiangitem = jiangitem;
	}

	public Object[] getItemClientMap() {
		
		return itemClientMap;
	}
	public void setItemClientMap(Object[] itemClientMap) {
		this.itemClientMap = itemClientMap;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmailItem() {
		return emailItem;
	}

	public void setEmailItem(String emailItem) {
		this.emailItem = emailItem;
	}
	
}
