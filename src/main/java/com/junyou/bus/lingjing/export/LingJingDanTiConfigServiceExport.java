package com.junyou.bus.lingjing.export;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.lingjing.entity.LingJingShuXingCengConfig;
import com.junyou.bus.lingjing.entity.LingJingShuXingConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 灵境单体表
 * @author LiuYu
 * 2015-6-29 上午10:03:10
 */
@Service
public class LingJingDanTiConfigServiceExport extends AbsClasspathConfigureParser {

	private String configureName = "LingJingDanTiBiao.jat";
	
	@Autowired
	private LingJingShuXingConfigServiceExport lingJingShuXingConfigServiceExport;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				int type = CovertObjectUtil.object2int(tmp.get("type"));
				if(type < 1){
					continue;
				}
				for (int i = 1; i <= lingJingShuXingConfigServiceExport.getMaxRank(); i++) {
					LingJingShuXingCengConfig cengConfig = lingJingShuXingConfigServiceExport.getCengConfig(i);
					if(cengConfig == null){
						ChuanQiLog.error("灵境配置异常。{}重没有配置属性。",i);
					}
					LingJingShuXingConfig config = cengConfig.getConfig(type);
					if(config == null){
						ChuanQiLog.error("灵境配置异常。{}重{}类型没有配置属性。",i,type);
					}
					config.setNeed(CovertObjectUtil.obj2long(tmp.get("need" + i)));
				}
			}
		}
	}
	

	@Override
	protected String getConfigureName() {
		return configureName;
	}
}
