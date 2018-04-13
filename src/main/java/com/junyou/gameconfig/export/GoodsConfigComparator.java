package com.junyou.gameconfig.export;

import java.util.Comparator;

import com.junyou.gameconfig.goods.configure.export.GoodsConfig;

public class GoodsConfigComparator implements Comparator<GoodsConfig>{
	private static GoodsConfigComparator comparator = new GoodsConfigComparator();
	public static GoodsConfigComparator getInstance(){
		return comparator;
	}
	
	@Override
	public int compare(GoodsConfig vo1, GoodsConfig vo2) {
		//限时的在前，非限时的在后
		if(vo1.getDurationDay() > 0 && vo2.getDurationDay() < 1){
			return 1;
		}else if(vo2.getDurationDay() > 0 && vo1.getDurationDay() < 1){
			return -1;
		}

		//绑定的在前，非绑定的在后
		if(vo1.getBangding() > vo2.getBangding()){
			return -1;
		}else if(vo1.getBangding() == vo2.getBangding()){
			return 0;
		}else{
			return 1;
		}
	}

}
