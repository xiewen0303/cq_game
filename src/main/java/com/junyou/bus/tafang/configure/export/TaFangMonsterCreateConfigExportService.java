package com.junyou.bus.tafang.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.tafang.configure.TaFangMonsterCreateConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-10-9 下午6:29:44
 */
@Service
public class TaFangMonsterCreateConfigExportService extends AbsClasspathConfigureParser {
	
	private String configureName = "TaFangGuaiWuShuaXin.jat";
	
	private Map<Integer, TaFangMonsterCreateConfig> configs;
	private static int maxLevel = 30;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Map<Integer, TaFangMonsterCreateConfig> tempConfigs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				TaFangMonsterCreateConfig config = createTaFangMonsterCreateConfig(tmp);
				tempConfigs.put(config.getId(), config);
			}
		}
		
		this.configs = tempConfigs;
	}
	
	private TaFangMonsterCreateConfig createTaFangMonsterCreateConfig(Map<String, Object> tmp){
		TaFangMonsterCreateConfig config = new TaFangMonsterCreateConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		String[] start = CovertObjectUtil.object2String(tmp.get("startzb")).split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		config.setStart(new int[]{CovertObjectUtil.object2int(start[0]),CovertObjectUtil.object2int(start[1])});
		String[] stop = CovertObjectUtil.object2String(tmp.get("stopzb")).split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		config.setStop(new int[]{CovertObjectUtil.object2int(stop[0]),CovertObjectUtil.object2int(stop[1])});
		Map<Integer, List<String>> monsterMap = new HashMap<>();
		for (int i = 1; i <= maxLevel; i++) {
			String monsters = CovertObjectUtil.obj2StrOrNull(tmp.get("monster"+i));
			if(monsters != null){
				List<String> list = new ArrayList<>();
				for (String monster : monsters.split(GameConstants.CONFIG_SPLIT_CHAR)) {
					if(monster != null && monster.length() > 0){
						String[] info = monster.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
						if(info != null && info.length > 1){
							for (int j = 0; j < CovertObjectUtil.object2int(info[1]); j++) {
								list.add(info[0]);
							}
						}
					}
				}
				monsterMap.put(i, list);
			}
		}
		config.setMonsters(monsterMap);
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public TaFangMonsterCreateConfig getConfigById(int id){
		if(configs == null){
			return null;
		}
		return configs.get(id);
	}
	
	public List<TaFangMonsterCreateConfig> getConfigs(){
		if(configs == null){
			return null;
		}
		return new ArrayList<>(configs.values());
	}

	public int getMaxLevel() {
		return maxLevel;
	}

}
