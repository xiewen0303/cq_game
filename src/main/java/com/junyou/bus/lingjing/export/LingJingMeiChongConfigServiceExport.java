package com.junyou.bus.lingjing.export;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.lingjing.entity.LingJingShuXingCengConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 灵境每重表
 * @author LiuYu
 * 2015-6-29 上午10:03:10
 */
@Service
public class LingJingMeiChongConfigServiceExport extends AbsClasspathConfigureParser {

	private String configureName = "LingJingMeiChongBiao.jat";
	
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
				int rank = CovertObjectUtil.object2int(tmp.get("rank"));
				if(rank < 1){
					continue;
				}
				LingJingShuXingCengConfig config = lingJingShuXingConfigServiceExport.getCengConfig(rank);
				if(config == null){
					ChuanQiLog.error("灵境配置异常。{}重没有配置。",rank);
				}
				Map<String,Integer> items = ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("jiangitem")));
				config.setItems(items);
				
				//注册需要解析的配置
				GoodsConfigChecker.registCheck(config);
			}
		}
	}
	

	@Override
	protected String getConfigureName() {
		return configureName;
	}
}
