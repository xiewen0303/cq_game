package com.junyou.bus.maigu.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.bus.maigu.configure.ZuDuiShouHhuGuaiWuConfig;
import com.junyou.bus.maigu.configure.constants.MaiguConstant;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class ZuDuiShouHhuGuaiWuConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, ZuDuiShouHhuGuaiWuConfig> configs = null;

	private Map<String, Integer> allMonsters = null;

	/**
	 * configFileName
	 */
	private String configureName = "ZuDuiShouHhuGuaiWu.jat";

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		// 配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);

		Object[] dataList = GameConfigUtil.getResource(data);
		configs = new HashMap<Integer, ZuDuiShouHhuGuaiWuConfig>();
		allMonsters = new HashMap<String, Integer>();
		for (Object obj : dataList) {

			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ZuDuiShouHhuGuaiWuConfig config = createZuDuiShouHhuGuaiWuConfig(tmp);
				configs.put(config.getBoshu(), config);
				List<Map<String, Integer>> monsters = config.getMonsters();
				for (Map<String, Integer> e : monsters) {
					for (String monsterId : e.keySet()) {
						Integer amount = allMonsters.get(monsterId);
						if (amount == null) {
							allMonsters.put(monsterId, e.get(monsterId)
									* MaiguConstant.POINT_NUM);
						} else {
							allMonsters.put(monsterId, e.get(monsterId)
									* MaiguConstant.POINT_NUM + amount);
						}
					}
				}
			}
		}
	}

	public ZuDuiShouHhuGuaiWuConfig createZuDuiShouHhuGuaiWuConfig(
			Map<String, Object> tmp) {
		ZuDuiShouHhuGuaiWuConfig config = new ZuDuiShouHhuGuaiWuConfig();
		config.setBoshu(CovertObjectUtil.object2int(tmp.get("boshu")));
		List<Map<String, Integer>> monsters = new ArrayList<Map<String, Integer>>();
		String monsterStr = CovertObjectUtil.object2String(tmp.get("monster"));
		String[] monsterStrArray = monsterStr.split("\\|");
		for (int i = 0; i < monsterStrArray.length; i++) {
			String info = monsterStrArray[i];
			String[] infoArray = info.split(":");
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put(infoArray[0], Integer.parseInt(infoArray[1]));
			monsters.add(map);
		}
		config.setMonsters(monsters);
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public ZuDuiShouHhuGuaiWuConfig getConfig(int boshu) {
		return configs.get(boshu);
	}

	public ReadOnlyMap<String, Integer> getAllMonsters() {
		return new ReadOnlyMap<>(allMonsters);
	}

}
