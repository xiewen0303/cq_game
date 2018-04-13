package com.junyou.bus.kfjingji.configure;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-10-26 下午4:57:58
 */
@Service
public class KfJingjiConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "KuaFuPaiMingZaXiang.jat";
	private KfJingjiConfig config;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				KfJingjiConfig config = createKfJingjiConfig(tmp);
				if(config != null){
					this.config = config;
					break;
				}
			}
		}
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	private KfJingjiConfig createKfJingjiConfig(Map<String, Object> tmp){
		KfJingjiConfig config = new KfJingjiConfig();
		config.setCdGold(CovertObjectUtil.object2int(tmp.get("gold1")));
		config.setChangeGold(CovertObjectUtil.object2int(tmp.get("gold2")));
		config.setCishu(CovertObjectUtil.object2int(tmp.get("cishu")));
		config.setCishuGold(CovertObjectUtil.object2int(tmp.get("gold")));
		config.setFightCd(CovertObjectUtil.object2int(tmp.get("time1")));
		config.setMaxCd(CovertObjectUtil.object2int(tmp.get("time2")));
		config.setFreeChange(CovertObjectUtil.object2int(tmp.get("huancishu")));
		config.setLoseExp(CovertObjectUtil.object2int(tmp.get("jiangitem3")));
		config.setLoseShenhun(CovertObjectUtil.object2int(tmp.get("jiangitem4")));
		config.setWinExp(CovertObjectUtil.object2int(tmp.get("jiangitem1")));
		config.setWinShenhun(CovertObjectUtil.object2int(tmp.get("jiangitem2")));
		return config;
	}
	public KfJingjiConfig loadConfig(){
		return config;
	}
}
