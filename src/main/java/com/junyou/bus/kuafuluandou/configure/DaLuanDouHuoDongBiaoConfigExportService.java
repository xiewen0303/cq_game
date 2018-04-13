package com.junyou.bus.kuafuluandou.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 跨服大乱斗活动表 
 *
 * @author ZHONGDIAN
 * @date 2016-02-17 15:15:29
 */
@Component
public class DaLuanDouHuoDongBiaoConfigExportService extends AbsClasspathConfigureParser {
	
	private Map<Integer, DaLuanDouHuoDongBiaoConfig> configs = new HashMap<>();
	
	/**
	  * configFileName
	 */
	private String configureName = "DaLuanDouHuoDongBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				DaLuanDouHuoDongBiaoConfig config = createDaLuanDouHuoDongBiaoConfig(tmp);
								
				configs.put(config.getId(), config);
			}
		}
	}
	
	public DaLuanDouHuoDongBiaoConfig createDaLuanDouHuoDongBiaoConfig(Map<String, Object> tmp) {
		DaLuanDouHuoDongBiaoConfig config = new DaLuanDouHuoDongBiaoConfig();	
							
		config.setPretime(CovertObjectUtil.object2String(tmp.get("pretime")));
											
		config.setEndtime(CovertObjectUtil.object2String(tmp.get("endtime")));
											
		config.setNeedlevel(CovertObjectUtil.object2int(tmp.get("needlevel")));
											
		config.setMianban(CovertObjectUtil.object2String(tmp.get("mianban")));
											
		config.setRes(CovertObjectUtil.object2String(tmp.get("res")));
											
		config.setStarttime(CovertObjectUtil.object2String(tmp.get("starttime")));
											
		config.setData1(CovertObjectUtil.object2int(tmp.get("data1")));
											
		config.setData2(CovertObjectUtil.object2String(tmp.get("data2")));
											
		config.setData3(CovertObjectUtil.object2String(tmp.get("data3")));
											
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
											
		config.setNeedzplus(CovertObjectUtil.object2int(tmp.get("needzplus")));
											
		config.setGonggao2(CovertObjectUtil.object2String(tmp.get("gonggao2")));
											
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setGonggao3(CovertObjectUtil.object2String(tmp.get("gonggao3")));
											
		config.setGonggao1(CovertObjectUtil.object2String(tmp.get("gonggao1")));
											
		config.setMap(CovertObjectUtil.object2int(tmp.get("map")));
											
		config.setRes1(CovertObjectUtil.object2String(tmp.get("res1")));
											
		config.setWeek(CovertObjectUtil.object2int(tmp.get("week")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public DaLuanDouHuoDongBiaoConfig loadById(Integer id){
		return configs.get(id);
	}
}