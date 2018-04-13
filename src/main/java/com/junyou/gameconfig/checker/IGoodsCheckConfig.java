package com.junyou.gameconfig.checker;

import java.util.List;
import java.util.Map;

/**
 * 需要进行物品检测的配置
 * @author LiuYu
 * @date 2015-1-30 下午4:37:35
 */
public interface IGoodsCheckConfig {
	/**
	 * 获取配置名
	 * @return
	 */
	public String getConfigName();
	/**
	 * 需要检测的物品集合
	 * @return
	 */
	public List<Map<String,Integer>> getCheckMap();
}
