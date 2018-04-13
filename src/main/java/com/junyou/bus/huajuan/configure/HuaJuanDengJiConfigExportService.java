package com.junyou.bus.huajuan.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class HuaJuanDengJiConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, HuaJuanDengJiConfig> configs = null;
	private List<HuaJuanDengJiConfig> configList = null;
	private int minExp = Integer.MAX_VALUE;
	private int minLevel = 0;
	private int maxExp = 0;
	private int maxLevel = 0;

	/**
	 * configFileName
	 */
	private String configureName = "HuaJuanDengJi.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, HuaJuanDengJiConfig>();
		configList = new ArrayList<HuaJuanDengJiConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				HuaJuanDengJiConfig config = createHuaJuanDengJiConfig(tmp);
				if(config.getNeedexp()==0){
					continue;
				}
				configs.put(config.getLevel(), config);
				configList.add(config);
				if (config.getNeedexp() > maxExp) {
					maxExp = config.getNeedexp();
					maxLevel = config.getLevel();
				}
				if (config.getNeedexp() < minExp) {
					minExp = config.getNeedexp();
					minLevel = config.getLevel();
				}
			}
		}
	}

	public HuaJuanDengJiConfig createHuaJuanDengJiConfig(Map<String, Object> tmp) {
		HuaJuanDengJiConfig config = new HuaJuanDengJiConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		config.setNeedexp(CovertObjectUtil.object2int(tmp.get("needexp")));
		config.setPercentage(CovertObjectUtil.object2int(tmp.get("plus")));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public HuaJuanDengJiConfig loadByLevel(Integer level) {
		return configs.get(level);
	}

	public int calcLevel(int exp) {
		if (exp < minExp) {
			return minLevel;
		}
		if (exp >= maxExp) {
			return maxLevel;
		}
		for (int i = 0; i < configList.size() - 1; i++) {
			if (configList.get(i + 1).getNeedexp() > exp
					&& exp >= configList.get(i).getNeedexp()) {
				return configList.get(i + 1).getLevel();
			}
		}
		throw new RuntimeException("error exp =" + exp);
	}

	public int getMinExp() {
		return minExp;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public int getMaxExp() {
		return maxExp;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

}
