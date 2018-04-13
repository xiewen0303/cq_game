package com.junyou.bus.qisha.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.qisha.configure.QiShaBossConfig;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-8-10 下午10:28:32
 */
@Service
public class QiShaBossConfigServiceExport extends AbsClasspathConfigureParser {

	/**
	 * configFileName
	 */
	private String configureName = "QiShaJiangLi.jat";
	private Map<String,QiShaBossConfig> configs;
	private List<String> bossIds;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		ConfigMd5SignManange.addConfigSign(configureName, data);

		Object[] dataList = GameConfigUtil.getResource(data);
		Map<String,QiShaBossConfig> configs = new HashMap<>();
		List<String> bossIds = new ArrayList<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				QiShaBossConfig config = createQiShaBossConfig(tmp);
				configs.put(config.getMonsterId(), config);
				bossIds.add(config.getMonsterId());
				GoodsConfigChecker.registCheck(config);
			}
		}
		this.configs = configs;
		this.bossIds = bossIds;
	}
	
	private QiShaBossConfig createQiShaBossConfig(Map<String, Object> tmp){
		QiShaBossConfig config = new QiShaBossConfig();
		config.setShunxu(CovertObjectUtil.object2int(tmp.get("shunxu")));
		config.setMonsterId(CovertObjectUtil.obj2StrOrNull(tmp.get("id")));
		String killItemStr = CovertObjectUtil.object2String(tmp.get("jiangitem"));
		config.setKillItemStr(killItemStr);
		Map<String,Integer> killItems = ConfigAnalysisUtils.getConfigMap(killItemStr);
		config.setKillItems(killItems);
		String hurtItemStr = CovertObjectUtil.object2String(tmp.get("jiangitem2"));
		config.setHurtItemStr(hurtItemStr);
		Map<String,Integer> hurtItems = ConfigAnalysisUtils.getConfigMap(hurtItemStr);
		config.setHurtItems(hurtItems);
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public List<String> getBossIds(){
		return bossIds;
	}
	
	public QiShaBossConfig getConfigById(String monsterId){
		return configs.get(monsterId);
	}
}
