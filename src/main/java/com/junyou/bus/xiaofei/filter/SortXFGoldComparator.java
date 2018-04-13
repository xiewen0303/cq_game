package com.junyou.bus.xiaofei.filter;

import java.util.Comparator;

import com.junyou.bus.xiaofei.entity.RefabuXiaofei;

/**
 * 根据消费元宝排序实现
 *@author  ZHONGDIAN
 *@created 2012-3-19下午1:25:32
 */
public class SortXFGoldComparator implements Comparator<RefabuXiaofei>{

	@Override
	public int compare(RefabuXiaofei xiaofei1, RefabuXiaofei xiaofei2) {
		
		if(xiaofei1.getXfGold() < xiaofei2.getXfGold()){
			return 1;
		}else if(xiaofei1.getXfGold().intValue() == xiaofei2.getXfGold().intValue()){
			int i = xiaofei1.getUpdateTime().compareTo(xiaofei2.getUpdateTime());
			if(i > 0 ){
				return 1;
			}else{
				return -1;
			}
		}else{
			return -1;
		}
	}

}
