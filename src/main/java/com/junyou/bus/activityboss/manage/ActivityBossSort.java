package com.junyou.bus.activityboss.manage;

import java.util.Comparator;

/**
 * 活动boss排序
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-29 下午1:38:48
 */
public class ActivityBossSort implements Comparator<BossHurtRank> {

	@Override
	public int compare(BossHurtRank arg0, BossHurtRank arg1) {
		if(arg0.getHurt() < arg1.getHurt()){
			return 1;
		}else if(arg0.getHurt() > arg1.getHurt()){
			return -1;
		}
		return 0;
	}
	
}
