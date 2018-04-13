package com.junyou.bus.jingji.export;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.jingji.entity.PaiMingZaXiangConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class PaiMingZaXiangConfigExportService extends AbsClasspathConfigureParser{

	private String configureName = "PaiMingZaXiang.jat";
	private static PaiMingZaXiangConfig config;
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				config = createPaiMingZaXiangConfig(tmp);
				return;
			}
		}
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	private PaiMingZaXiangConfig createPaiMingZaXiangConfig(Map<String, Object> tmp){
		PaiMingZaXiangConfig config = new PaiMingZaXiangConfig();
		config.setBuyCountGold(CovertObjectUtil.object2int(tmp.get("gold")));
		config.setClearCdGold(CovertObjectUtil.object2int(tmp.get("gold1")));
		config.setGailv1(CovertObjectUtil.object2Float(tmp.get("gailv1")));
		config.setGailv2(CovertObjectUtil.object2Float(tmp.get("gailv2")));
		config.setGuwuBingGold(CovertObjectUtil.object2int(tmp.get("needgold1")));
		config.setGuwuCount(CovertObjectUtil.object2int(tmp.get("cishu1")));
		config.setGuwuCount2(CovertObjectUtil.object2int(tmp.get("cishu2")));
		config.setGuwuGold(CovertObjectUtil.object2int(tmp.get("needgold")));
		config.setLoseExp(CovertObjectUtil.object2int(tmp.get("jiangitem3")));
		config.setLoseRongyu(CovertObjectUtil.object2int(tmp.get("jiangitem4")));
		config.setMaxTime(CovertObjectUtil.object2int(tmp.get("time2")) * 1000);
		config.setTime(CovertObjectUtil.object2int(tmp.get("time1")) * 1000);
		config.setWinExp(CovertObjectUtil.object2int(tmp.get("jiangitem1")));
		config.setWinRongyu(CovertObjectUtil.object2int(tmp.get("jiangitem2")));
		config.setMaxTzCount(CovertObjectUtil.object2int(tmp.get("cishu")));
		config.setGuwuAdd(CovertObjectUtil.object2int(tmp.get("zhanli")));
		return config;
	}
	
	public PaiMingZaXiangConfig getConfig(){
		return config;
	}
	
	public static int getGuwuAdd(){
		if(config == null){
			return 1;
		}
		return config.getGuwuAdd();
	}
}
