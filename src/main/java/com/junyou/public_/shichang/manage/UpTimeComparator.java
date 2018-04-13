package com.junyou.public_.shichang.manage;

import java.util.Comparator;

/**
 * 时间排序
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-14 下午6:15:09
 */
public class UpTimeComparator implements Comparator<PaiMai> {

	@Override
	public int compare(PaiMai o1, PaiMai o2) {
		if(o1.getSellTime() > o2.getSellTime()){
			return -1;
		}else if(o1.getSellTime() < o2.getSellTime()){
			return 1;
		} 
		return 0; 
	}
}
