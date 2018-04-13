package com.junyou.public_.guild.configure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.public_.guild.entity.GuildGeLouConfig;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-7-14 下午4:28:46
 */
@Service
public class GuildGeLouConfigService extends AbsClasspathConfigureParser{

	private final String configureName = "MenPaiGeLou.jat";
	private ConcurrentMap<Integer, GuildGeLouConfig> configs;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		ConcurrentMap<Integer, GuildGeLouConfig> configs = new ConcurrentHashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				GuildGeLouConfig config = createGuildGeLouConfig(tmp);
				configs.put(config.getLevel(), config);
			}
		}
		this.configs = configs;
	}
	
	private GuildGeLouConfig createGuildGeLouConfig(Map<String, Object> tmp){
		GuildGeLouConfig config = new GuildGeLouConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		config.setNeedMoney(CovertObjectUtil.obj2long(tmp.get("needmoney")));
		config.setNeedItem1(CovertObjectUtil.object2int(tmp.get("needpai1")));
		config.setNeedItem2(CovertObjectUtil.object2int(tmp.get("needpai2")));
		config.setNeedItem3(CovertObjectUtil.object2int(tmp.get("needpai3")));
		config.setNeedItem4(CovertObjectUtil.object2int(tmp.get("needpai4")));
		config.setNeedGuildLevel(CovertObjectUtil.object2int(tmp.get("needqizhi")));
		config.setMaxLevel(CovertObjectUtil.object2int(tmp.get("maxlevel")));
		return config;
	}
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public GuildGeLouConfig getConfigByLevel(int level){
		return configs.get(level);
	}

}
