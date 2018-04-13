package com.junyou.public_.guild.configure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.public_.guild.entity.GuildQuanXianConfig;
import com.junyou.utils.common.CovertObjectUtil;
@Service
public class GuildQuanXianConfigService extends AbsClasspathConfigureParser{
	private final String configureName = "GongHuiQuanXianBiao.jat";
	private ConcurrentMap<Integer, GuildQuanXianConfig> configs;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		ConcurrentMap<Integer, GuildQuanXianConfig> configs = new ConcurrentHashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				GuildQuanXianConfig config = createGuildQuanXianConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
		this.configs = configs;
	}
	
	private GuildQuanXianConfig createGuildQuanXianConfig(Map<String, Object> tmp){
		GuildQuanXianConfig config = new GuildQuanXianConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("job")));
		config.setPostMaxCount(CovertObjectUtil.object2int(tmp.get("jobnum")));
		config.setAppoint(CovertObjectUtil.object2boolean(tmp.get("change")));
		config.setExpel(CovertObjectUtil.object2boolean(tmp.get("expel")));
		config.setRecruit(CovertObjectUtil.object2boolean(tmp.get("recruit")));
		config.setSetNotice(CovertObjectUtil.object2boolean(tmp.get("revise")));
		config.setReviseterm(CovertObjectUtil.object2boolean(tmp.get("reviseterm")));
		config.setGuildUp(CovertObjectUtil.object2boolean(tmp.get("menpaiup")));
		config.setSchoolUp(CovertObjectUtil.object2boolean(tmp.get("gelouup")));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public GuildQuanXianConfig getGuildQuanXianConfig(int id){
		return configs.get(id);
	}
}
