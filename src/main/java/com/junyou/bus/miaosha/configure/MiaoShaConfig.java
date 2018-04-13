package com.junyou.bus.miaosha.configure;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2016-3-4 下午3:45:42
 */
public class MiaoShaConfig {
	private int id;
	private MiaoShaTime time;
	private int gold;
	private Map<MiaoShaGoodsVo,Integer> drop;
	private int total;
	private Map<String,Integer> first;
	private Map<String,Integer> top10;
	private Map<String,Integer> join;
	private String jipinGoods;
	private Object[] clientVo;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public MiaoShaTime getTime() {
		return time;
	}
	public void setTime(MiaoShaTime time) {
		this.time = time;
		time.setId(getId());
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public Map<MiaoShaGoodsVo, Integer> getDrop() {
		return drop;
	}
	public void setDrop(Map<MiaoShaGoodsVo, Integer> drop) {
		this.drop = new ReadOnlyMap<>(drop);
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Map<String, Integer> getFirst() {
		return first;
	}
	public void setFirst(Map<String, Integer> first) {
		this.first = new ReadOnlyMap<>(first);
	}
	public Map<String, Integer> getTop10() {
		return top10;
	}
	public void setTop10(Map<String, Integer> top10) {
		this.top10 = new ReadOnlyMap<>(top10);
	}
	public Map<String, Integer> getJoin() {
		return join;
	}
	public void setJoin(Map<String, Integer> join) {
		this.join = new ReadOnlyMap<>(join);
	}
	public String getJipinGoods() {
		return jipinGoods;
	}
	public void setJipinGoods(String jipinGoods) {
		this.jipinGoods = jipinGoods;
	}
	public Object[] getClientVo() {
		return clientVo;
	}
	public void setClientVo(Object[] clientVo) {
		this.clientVo = clientVo;
	}
	
}
