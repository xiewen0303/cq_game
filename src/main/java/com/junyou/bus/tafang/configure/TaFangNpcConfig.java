package com.junyou.bus.tafang.configure;

import java.util.List;

import com.junyou.utils.collection.ReadOnlyList;

/**
 * @author LiuYu
 * 2015-10-12 上午10:18:53
 */
public class TaFangNpcConfig {
	private int id;
	private String npcId;
	private int[] zuobiao;
	private String item;
	private int itemCount;
	private List<Integer> attLines;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNpcId() {
		return npcId;
	}
	public void setNpcId(String npcId) {
		this.npcId = npcId;
	}
	public int[] getZuobiao() {
		return zuobiao;
	}
	public void setZuobiao(int[] zuobiao) {
		this.zuobiao = zuobiao;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public List<Integer> getAttLines() {
		return attLines;
	}
	public void setAttLines(List<Integer> attLines) {
		this.attLines = new ReadOnlyList<>(attLines);
	}
	
	
}
