package com.junyou.public_.shichang.manage;

import java.util.Comparator;

/**
 * 总价格排序
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-14 下午6:15:09
 */
public class PriceComparator implements Comparator<PaiMai> {
	
	private int order; 
	
	public PriceComparator(int order) {
		this.order = order;
	}



	@Override
	public int compare(PaiMai o1, PaiMai o2) {
		if(o1.getPrice().intValue() > o2.getPrice().intValue()){
			return 1*order;
		}else if(o1.getPrice().intValue() < o2.getPrice().intValue()){
			return -1*order;
		} 
		return 0;
	}
}
