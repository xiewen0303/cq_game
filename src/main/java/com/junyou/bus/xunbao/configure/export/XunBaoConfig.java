package com.junyou.bus.xunbao.configure.export;

import java.util.HashMap;
import java.util.Map;
import com.kernel.data.dao.AbsVersion;
 
/**
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-2-5 下午2:22:27
 */
public class XunBaoConfig extends AbsVersion {
	private int id;
	
	private String name;
	
	private Integer mincishu;

	private Integer maxcishu;
	
	private int allOdds;

	private Map<Object[],Integer> goodsOdds =new HashMap<Object[], Integer>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMincishu() {
		return mincishu;
	}

	public void setMincishu(Integer mincishu) {
		this.mincishu = mincishu;
	}

	public Integer getMaxcishu() {
		return maxcishu;
	}

	public void setMaxcishu(Integer maxcishu) {
		this.maxcishu = maxcishu;
	}
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<Object[], Integer> getGoodsOdds() {
		return goodsOdds;
	}

	public void addGoodsOdds(Object[] objects, int dropOdds) {
		goodsOdds.put(objects, dropOdds);
	}

	public int getAllOdds() {
		return allOdds;
	}

	public void setAllOdds(int allOdds) {
		this.allOdds = allOdds;
	}
	
}
