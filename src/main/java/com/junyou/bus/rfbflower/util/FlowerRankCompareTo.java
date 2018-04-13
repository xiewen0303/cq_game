package com.junyou.bus.rfbflower.util;

import java.util.Comparator;
/**
 * @author lxn
 *
 */
public class FlowerRankCompareTo implements Comparator<FlowerCharmRank>{

	@Override
	public int compare(FlowerCharmRank o1, FlowerCharmRank o2) {
		if(o1.getCharmValue()>o2.getCharmValue()){
			return -1;
		}else if(o1.getCharmValue()<o2.getCharmValue()){
			return 1;
		}else{
			if(o1.getCharmValue()==o2.getCharmValue()){
				if(o1.getShangBangTime()<o2.getShangBangTime()){
					return -1;
				}else{
					return 1;
				}
			}
		}
		
		return 0;
	}

}
