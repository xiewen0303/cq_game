package com.junyou.bus.kuafu_boss.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class KuafuBossChengZhangConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, KuafuBossChengZhangConfig> configs = null;

	/**
	 * configFileName
	 */
	private String configureName = "KuaFuBossChengZhang.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		if(data==null){
			return;
		}

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, KuafuBossChengZhangConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				KuafuBossChengZhangConfig config = createKuafuBossChengZhangConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
	}

	public KuafuBossChengZhangConfig createKuafuBossChengZhangConfig(
			Map<String, Object> tmp) {
		KuafuBossChengZhangConfig config = new KuafuBossChengZhangConfig();

		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setBossId(CovertObjectUtil.object2String(tmp.get("bossid")));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	public String getBossIdById(int id){
		KuafuBossChengZhangConfig config = configs.get(id);
		if(config==null){
			return null;
		}else{
			return config.getBossId();
		}
	}
}
