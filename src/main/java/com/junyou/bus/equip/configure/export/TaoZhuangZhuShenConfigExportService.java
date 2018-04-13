package com.junyou.bus.equip.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu 2015-11-2 下午2:29:27
 */
@Service
public class TaoZhuangZhuShenConfigExportService extends
		AbsClasspathConfigureParser {
	private String configureName = "TaoZhuangZhuShen.jat";
	private Map<Integer, TaoZhuangZhuShenConfig> configs;
	private int maxLevel;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		configs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				TaoZhuangZhuShenConfig config = createTaoZhuangZhuShenConfig(tmp);
				configs.put(config.getLevel(), config);
				if(config.getLevel()>maxLevel){
					maxLevel = config.getLevel();
				}
			}
		}
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	private TaoZhuangZhuShenConfig createTaoZhuangZhuShenConfig(
			Map<String, Object> tmp) {
		TaoZhuangZhuShenConfig config = new TaoZhuangZhuShenConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		config.setOdds(CovertObjectUtil.object2int(tmp.get("odds")));
		config.setAddodds(CovertObjectUtil.object2int(tmp.get("addodds")));
		config.setZsxs(CovertObjectUtil.object2Float(tmp.get("zsxs")));
		config.setTzxs(CovertObjectUtil.object2Float(tmp.get("tzxs")));
		config.setSjxs(CovertObjectUtil.object2Float(tmp.get("sjxs")));
		config.setNeeditem(CovertObjectUtil.object2String(tmp.get("needitem")));
		config.setNeednum(CovertObjectUtil.object2int(tmp.get("neednum")));
		config.setNeedmoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		return config;

	}

	public TaoZhuangZhuShenConfig loadByLevel(int level) {
		return configs.get(level);
	}

}
