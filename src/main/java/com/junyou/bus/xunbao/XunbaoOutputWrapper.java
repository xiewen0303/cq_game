package com.junyou.bus.xunbao;

import java.util.List;

import com.junyou.bus.xunbao.entity.XunBaoBag;
import com.junyou.bus.xunbao.entity.XunBaoLog;

public class XunbaoOutputWrapper {

	public static Object[] getXunbaoInfo(List<XunBaoLog> xunbaoLogs ,int bagCount){
		Object[] logs = null;
		if(xunbaoLogs != null && xunbaoLogs.size() > 0){
			logs = new Object[xunbaoLogs.size()];
			
			int index = 0;
			for (XunBaoLog xunbaoLog : xunbaoLogs) {
				logs[index++] = new Object[]{xunbaoLog.getRoleName(),xunbaoLog.getGoodsId(),xunbaoLog.getGoodsCount()};
			}
		}
		
		return new Object[]{logs,bagCount};
	}
	
	
	public static Object[] getXunbaoData(List<XunBaoBag> xunbaoBags){
		Object[] bags = null;
		if(xunbaoBags != null && xunbaoBags.size() > 0){
			bags = new Object[xunbaoBags.size()];
			
			int index = 0;
			for (XunBaoBag xunbaoBag : xunbaoBags) {
				bags[index++] = new Object[]{xunbaoBag.getId(),xunbaoBag.getGoodsId(),xunbaoBag.getGoodsCount()};
			}
		}
		
		return bags;
	}
	
}
