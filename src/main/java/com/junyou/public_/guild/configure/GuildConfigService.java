package com.junyou.public_.guild.configure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.public_.guild.entity.GuildConfig;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class GuildConfigService extends AbsClasspathConfigureParser{
	private final String configureName = "MenPaiShengJiBiao.jat";
	private ConcurrentMap<Integer, GuildConfig> configs;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		ConcurrentMap<Integer, GuildConfig> configs = new ConcurrentHashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				GuildConfig config = createGuildConfig(tmp);
				configs.put(config.getLevel(), config);
			}
		}
		this.configs = configs;
	}
	
	private GuildConfig createGuildConfig(Map<String, Object> tmp){
		GuildConfig config = new GuildConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		config.setNeedItem1(CovertObjectUtil.object2int(tmp.get("needpai1")));
		config.setNeedItem2(CovertObjectUtil.object2int(tmp.get("needpai2")));
		config.setNeedItem3(CovertObjectUtil.object2int(tmp.get("needpai3")));
		config.setNeedItem4(CovertObjectUtil.object2int(tmp.get("needpai4")));
		config.setMaxCount(CovertObjectUtil.object2int(tmp.get("superiorlimit")));
		Map<String,Long> att = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttribute(att);
		config.setAddExp(CovertObjectUtil.object2int(tmp.get("addexp")));
		config.setAddZhenqi(CovertObjectUtil.object2int(tmp.get("addzhenqi")));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public GuildConfig getGuildConfig(int level){
		return configs.get(level);
	}
}
