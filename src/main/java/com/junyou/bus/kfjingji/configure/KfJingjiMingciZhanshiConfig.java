package com.junyou.bus.kfjingji.configure;

import java.util.List;

import com.junyou.utils.collection.ReadOnlyList;

/**
 * @author LiuYu
 * 2015-10-26 下午4:16:12
 */
public class KfJingjiMingciZhanshiConfig {
	private int paiming;
	private List<int[]> other;//对手
	public int getPaiming() {
		return paiming;
	}
	public void setPaiming(int paiming) {
		this.paiming = paiming;
	}
	public List<int[]> getOther() {
		return other;
	}
	public void setOther(List<int[]> other) {
		this.other = new ReadOnlyList<>(other);
	}
	
}
