package com.junyou.bus.lianyuboss.util;

import java.util.Comparator;

import com.junyou.bus.lianyuboss.entity.GuildBossLianyu;

public class LianyuBossComparator implements Comparator<GuildBossLianyu>  {

	@Override
	public int compare(GuildBossLianyu o1, GuildBossLianyu o2) {
		if(o1.getConfigId()>o2.getConfigId()){
			return -1;
		}else if(o1.getConfigId().intValue()==o2.getConfigId()){
			if(o1.getCreateTime().getTime()<o2.getCreateTime().getTime()){
				return -1;
			}else if(o1.getCreateTime().getTime()>o2.getCreateTime().getTime()){
				return 1;
			}else{
				return 0 ;
			}
		}
		return 1;
	}

}
