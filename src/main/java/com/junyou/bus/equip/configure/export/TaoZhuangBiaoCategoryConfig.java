package com.junyou.bus.equip.configure.export;

import java.util.HashMap;
import java.util.Map;

import com.junyou.utils.common.ObjectUtil;


/**
 * 套装总表 
 * @author LiuYu
 * @date 2015-5-30 下午5:08:47
 */
public class TaoZhuangBiaoCategoryConfig {

	private int maxCount;//套装最大数量

	private Map<Integer,TaoZhuangBiaoConfig> configs = new HashMap<>();
	 
	public TaoZhuangBiaoConfig getConfigByCount(int count) {
		if(count > maxCount){
			count = maxCount;
		}
		return configs.get(count);
	}

	public void addConfig(TaoZhuangBiaoConfig config) {
		if(config.getCount() > maxCount){
			maxCount = config.getCount();
		}
		configs.put(config.getCount(), config);
	}

	public void init(){
		TaoZhuangBiaoConfig last = null;
		for (int i = 1; i <= maxCount; i++) {
			if(configs.containsKey(i)){
				Map<String,Long> att = null;
				if(last != null){
					att = new HashMap<String, Long>(last.getAttrs());
				}
				last = configs.get(i);
				if(att != null){
					//叠加属性
					ObjectUtil.longMapAdd(att, last.getAttrs());
					last.setAttrs(att);
				}
			}else if(last != null){
				configs.put(i, last);
			}
		}
	}
}
