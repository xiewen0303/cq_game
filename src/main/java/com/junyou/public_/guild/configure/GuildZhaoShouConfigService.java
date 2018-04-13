package com.junyou.public_.guild.configure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.public_.guild.entity.GuildZhaoShouConfig;
import com.junyou.utils.common.CovertObjectUtil;
/**
 * 门派招收条件
 * @author LiuYu
 * @date 2015-1-21 下午4:24:18
 */
@Service
public class GuildZhaoShouConfigService extends AbsClasspathConfigureParser{
	private final String configureName = "MenPaiZhaoShouTiaoJianBiao.jat";
	private ConcurrentMap<Integer, GuildZhaoShouConfig> configs;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		ConcurrentMap<Integer, GuildZhaoShouConfig> configs = new ConcurrentHashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				GuildZhaoShouConfig config = createGuildZhaoShouConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
		this.configs = configs;
	}
	
	private GuildZhaoShouConfig createGuildZhaoShouConfig(Map<String, Object> tmp){
		GuildZhaoShouConfig config = new GuildZhaoShouConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setValue(CovertObjectUtil.object2int(tmp.get("level")));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public GuildZhaoShouConfig getGuildZhaoShouConfig(int id){
		return configs.get(id);
	}
}
