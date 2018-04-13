package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.Map;

/**
 * 微端登录奖励物品
 * @description 
 * @author Hanchun
 * @email 279444454@qq.com
 * @date 2015-4-21 下午1:55:32
 */
public class WeiDuanPublicConfig extends AdapterPublicConfig{
	
	private Map<String, Integer> jiangitem;//微端登录奖励物品

	public Map<String, Integer> getJiangitem() {
		return jiangitem;
	}

	public void setJiangitem(Map<String, Integer> jiangitem) {
		this.jiangitem = jiangitem;
	}


}
