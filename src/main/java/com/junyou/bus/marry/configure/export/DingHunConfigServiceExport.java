package com.junyou.bus.marry.configure.export;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.marry.entity.DingHunConfig;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-8-10 下午10:28:32
 */
@Service
public class DingHunConfigServiceExport extends AbsClasspathConfigureParser {

	private DingHunConfig config;
	/**
	 * configFileName
	 */
	private String configureName = "JieHunYuanFen.jat";
	
	@Override
	protected void configureDataResolve(byte[] data) {
		ConfigMd5SignManange.addConfigSign(configureName, data);

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				config = createDingHunConfig(tmp);
				break;//配置只解第一行
			}
		}
	}
	
	private DingHunConfig createDingHunConfig(Map<String, Object> tmp){
		DingHunConfig config = new DingHunConfig();
		config.setCostMoney(CovertObjectUtil.object2int(tmp.get("dhmoney")));
		config.setMaxYF(CovertObjectUtil.object2int(tmp.get("yfzmax")));
		config.setItemId1(CovertObjectUtil.obj2StrOrNull(tmp.get("needitem")));
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setMoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		config.setYfAdd(CovertObjectUtil.object2int(tmp.get("yfzone")));
		config.setItemGold(CovertObjectUtil.object2int(tmp.get("needgold")));
		config.setItemBGold(CovertObjectUtil.object2int(tmp.get("needbgold")));
		config.setQxCost(CovertObjectUtil.object2int(tmp.get("qxmoney")));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public DingHunConfig getConfig(){
		return config;
	}
}
