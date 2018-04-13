package com.junyou.bus.yabiao.configure.export;

import java.util.Map;

public class BiaoCheShuaXinConfig{
	private Integer id;
	private Integer rarelevel;
	
	private Integer total;
	private Map<Integer,Integer> map;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRarelevel() {
		return rarelevel;
	}

	public void setRarelevel(Integer rarelevel) {
		this.rarelevel = rarelevel;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Map<Integer, Integer> getMap() {
		return map;
	}

	public void setMap(Map<Integer, Integer> map) {
		this.map = map;
	}
	
}
