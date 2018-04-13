package com.junyou.bus.resource.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.resource.entity.ZiYuanJiaGeConfig;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 资源价格表
 * @author LiuYu
 * 2015-7-8 下午7:02:54
 */
@Service
public class ZiYuanJiaGeConfigExportService extends AbsClasspathConfigureParser {

	private Map<Integer,ZiYuanJiaGeConfig> configs = null;
	/**
	  * configFileName
	 */
	private String configureName = "ZiYuanJiaGeBiao.jat";
	
	private int minLevel;
	private int maxLevel;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		Map<Integer,ZiYuanJiaGeConfig> configs = new HashMap<>();
		int minLevel = -1;
		int maxLevel = 0;
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZiYuanJiaGeConfig config = createZiYuanJiaGeConfig(tmp);
				
				int level = config.getLevel();
				if(minLevel < 0){//初始化
					minLevel = level;
					maxLevel = level;
					configs.put(level, config);
				}else{
					for (; maxLevel < level; ) {
						maxLevel++;
						configs.put(maxLevel, config);
					}
				}
			}
		}
		this.configs = configs;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
	}
	
	public ZiYuanJiaGeConfig createZiYuanJiaGeConfig(Map<String, Object> tmp) {
		ZiYuanJiaGeConfig config = new ZiYuanJiaGeConfig();	
		config.setLevel(CovertObjectUtil.object2int(tmp.get("levelqj")));
		config.setExp(CovertObjectUtil.object2int(tmp.get("dhexp")));
		config.setMoney(CovertObjectUtil.object2int(tmp.get("dhmoney")));
		config.setRongyu(CovertObjectUtil.object2int(tmp.get("dhrongyu")));
		config.setZhenqi(CovertObjectUtil.object2int(tmp.get("dhzhenqi")));
		config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		config.setNeedGold(CovertObjectUtil.object2int(tmp.get("needgold")));
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ZiYuanJiaGeConfig loadByLevel(int level){
		if(level < minLevel){
			level = minLevel;
		}else if(level > maxLevel){
			level = maxLevel;
		}
		return configs.get(level);
	}
}
