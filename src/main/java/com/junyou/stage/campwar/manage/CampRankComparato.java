package com.junyou.stage.campwar.manage;

import java.util.Comparator;

import com.junyou.stage.campwar.entity.CampRank;

/**
 * 阵营战排行数据（排序）
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-4-7 下午12:43:16 
 */
public class CampRankComparato implements Comparator<CampRank>{
	
	@Override
	public int compare(CampRank vo1, CampRank vo2) {
		
		if(vo2.getJifen() > vo1.getJifen()){
			return 1;
		}else if(vo2.getJifen() == vo1.getJifen() && vo2.getUpdateTime() > vo1.getUpdateTime()){
			return 1;
		}else if(vo2.getJifen() == vo1.getJifen() && vo2.getUpdateTime() == vo1.getUpdateTime()){
			return 0;
		}else{
			return -1;
		}
	}
}
