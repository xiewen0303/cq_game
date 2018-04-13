package com.junyou.bus.kaifuactivity.configure.export;

import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;

/**
 * 武器阶排行
 */
public class KaiFuWuQiConfig {
	
	private Map<String, GoodsConfigureVo> jiangitem;
	private Object[] itemClientMap;
	private String emailItem; 
	private Integer id;
	private Integer min;
	private Integer max;
	private Integer yuJianLevel;
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

	public Integer getYuJianLevel() {
		return yuJianLevel;
	}

	public void setYuJianLevel(Integer yuJianLevel) {
		this.yuJianLevel = yuJianLevel;
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
