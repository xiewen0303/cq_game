package com.junyou.bus.tafang.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.tafang.configure.TaFangNpcConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-10-9 下午6:29:44
 */
@Service
public class TaFangNpcConfigExportService extends AbsClasspathConfigureParser {
	
	private String configureName = "TaFangZhaoHuan.jat";
	
	private Map<Integer, TaFangNpcConfig> configs;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Map<Integer, TaFangNpcConfig> tempConfigs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				TaFangNpcConfig config = createTaFangNpcConfig(tmp);
				tempConfigs.put(config.getId(), config);
			}
		}
		
		this.configs = tempConfigs;
	}
	
	private TaFangNpcConfig createTaFangNpcConfig(Map<String, Object> tmp){
		TaFangNpcConfig config = new TaFangNpcConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setNpcId(CovertObjectUtil.object2String(tmp.get("npcid")));
		String[] zb = CovertObjectUtil.object2String(tmp.get("zuobiao")).split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		if(zb != null && zb.length > 1){
			config.setZuobiao(new int[]{CovertObjectUtil.object2int(zb[0]),CovertObjectUtil.object2int(zb[1])});
		}
		config.setItem(CovertObjectUtil.obj2StrOrNull(tmp.get("needitem")));
		config.setItemCount(CovertObjectUtil.object2int(tmp.get("count")));
		String[] xianlu = CovertObjectUtil.object2String(tmp.get("xianlu")).split(GameConstants.CONFIG_SPLIT_CHAR);
		if(xianlu != null && xianlu.length > 0){
			List<Integer> attLines = new ArrayList<>();
			for (String line : xianlu) {
				attLines.add(CovertObjectUtil.object2int(line));
			}
			config.setAttLines(attLines);
		}
		
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public TaFangNpcConfig getConfig(int id){
		if(configs == null){
			return null;
		}
		return configs.get(id);
	}
	
}
