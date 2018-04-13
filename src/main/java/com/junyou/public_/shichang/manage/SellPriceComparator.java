package com.junyou.public_.shichang.manage;

import java.util.Comparator;

/**
 * 单价排序
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-14 下午6:15:09
 */
public class SellPriceComparator implements Comparator<PaiMai> {
	
	private int order; 
	
	public SellPriceComparator(int order) {
		this.order = order;
	}

	@Override
	public int compare(PaiMai o1, PaiMai o2) {
		float f1 = o1.getPrice()/(float)o1.getCount();
		float f2 = o2.getPrice()/(float)o2.getCount();
		if(f1 > f2){
			return 1*order;
		}else if(f1 < f2){
			return -1*order;
		}
			 
		return 0; 
	}
}
