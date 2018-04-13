package com.junyou.public_.guild.configure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.public_.guild.entity.GuildDuihuanConfig;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class GuildDuihuanConfigService extends AbsClasspathConfigureParser{
	private final String configureName = "MenPaiDuiHuanBiao.jat";
	private ConcurrentMap<String, GuildDuihuanConfig> configs;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		ConcurrentMap<String, GuildDuihuanConfig> configs = new ConcurrentHashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				GuildDuihuanConfig config = createGuildDuihuanConfig(tmp);
				configs.put(config.getGoodsId(), config);
			}
		}
		this.configs = configs;
	}
	
	private GuildDuihuanConfig createGuildDuihuanConfig(Map<String, Object> tmp){
		GuildDuihuanConfig config = new GuildDuihuanConfig();
		config.setGoodsId(CovertObjectUtil.object2String(tmp.get("sellid")));
		config.setMaxCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setNeedGong(CovertObjectUtil.object2int(tmp.get("needgong")));
		config.setNeedLevel(CovertObjectUtil.object2int(tmp.get("needlevel")));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public GuildDuihuanConfig getGuildDuihuanConfig(String id){
		return configs.get(id);
	}
}
