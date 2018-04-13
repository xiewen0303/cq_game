package com.junyou.bus.jingji.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.jingji.entity.RongYuDuiHuaniConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class RongYuDuiHuanConfigExportService extends AbsClasspathConfigureParser{

	private String configureName = "RongYuDuiHuan.jat";
	private Map<String, RongYuDuiHuaniConfig> configs;
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<String, RongYuDuiHuaniConfig> configs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				RongYuDuiHuaniConfig config = createRongYuDuiHuaniConfig(tmp);
				configs.put(config.getItemId(), config);
				//注册需要解析的配置
				GoodsConfigChecker.registCheck(config);
			}
		}
		this.configs = configs;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	private RongYuDuiHuaniConfig createRongYuDuiHuaniConfig(Map<String, Object> tmp){
		RongYuDuiHuaniConfig config = new RongYuDuiHuaniConfig();
		config.setItemId(CovertObjectUtil.object2String(tmp.get("id")));
		config.setNeedLevel(CovertObjectUtil.object2int(tmp.get("needlevel")));
		config.setNeedRongyu(CovertObjectUtil.object2int(tmp.get("needrongyu")));
		config.setMaxCount(CovertObjectUtil.object2int(tmp.get("maxcishu")));
		return config;
	}
	
	public RongYuDuiHuaniConfig loadByItemId(String itemId){
		return configs.get(itemId);
	}
	
	
}
