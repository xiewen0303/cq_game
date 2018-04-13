package com.junyou.bus.tanbao.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;


/**
 * @author LiuYu
 * 2015-6-17 下午10:15:14
 */
@Service
public class TanBaoExpConfigExportService extends AbsClasspathConfigureParser{
	/**
	 * 配置名
	 */
	private String configureName = "DuoBaoJingYan.jat";
	
	private Map<Integer, Integer> configs;
	
	private int minLevel;
	private int maxLevel;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] listData = GameConfigUtil.getResource(data);
		Map<Integer, Integer> configs = new HashMap<>();
		int minLevel = Integer.MAX_VALUE;
		int maxLevel = 0;
		for (Object obj:listData) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			int level = CovertObjectUtil.object2int(tmp.get("level"));
			int exp = CovertObjectUtil.object2int(tmp.get("exp"));
			if(minLevel > level){
				minLevel = level;
			}
			if(maxLevel < level){
				maxLevel = level;
			}
			configs.put(level, exp);
		}
		this.configs = configs;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public int getExp(int level){
		if(level < minLevel){
			level = minLevel;
		}else if(level > maxLevel){
			level = maxLevel;
		}
		return configs.get(level);
	}
	
	
}
