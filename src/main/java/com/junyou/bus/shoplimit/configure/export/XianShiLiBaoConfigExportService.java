package com.junyou.bus.shoplimit.configure.export;

 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 限时礼包
 */
@Component
public class XianShiLiBaoConfigExportService extends AbsClasspathConfigureParser  {
	
	private Map<Integer,XianShiLiBaoConfig> configs = null;
	
	/**
	  * configFileName
	 */
	private String configureName = "xianshilibaobiao.jat";
	private int minConfigId;
	private int maxConfigId;
	private int totalTime;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		configs = new HashMap<Integer, XianShiLiBaoConfig>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				XianShiLiBaoConfig config = createXianShiLiBaoConfig(tmp);
				totalTime += config.getTime();
				configs.put(config.getId(), config);
				if(minConfigId == 0 || minConfigId > config.getId()){
					minConfigId = config.getId();
				}
				if(maxConfigId < config.getId()){
					maxConfigId = config.getId();
				}
			}
		}
	}
	
	public XianShiLiBaoConfig createXianShiLiBaoConfig(Map<String, Object> tmp) {
		XianShiLiBaoConfig config = new XianShiLiBaoConfig();	
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		if(config.getId() <0 ){
			return config;
		}
		
		config.setJiangli(CovertObjectUtil.object2Map(tmp.get("jiangli")));
		config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("cost")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("time"))  *60*1000 );
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setTotalTime((totalTime + CovertObjectUtil.object2int(tmp.get("time")) *60*1000));
		
		return config;
	}
	
	public List<String> getConsumeIds(List<String> id1s) {
		List<String> result = new ArrayList<>();
		if(id1s != null){
			for (String id1 : id1s) {
				List<String> ids = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(id1);
				result.addAll(ids);
			}
		}
		return result; 
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public XianShiLiBaoConfig loadById(int id){
		return configs.get(id);
	}
	
	public int getMinConfigId() {
		return minConfigId;
	}
	
	public int getMaxConfigId() {
		return maxConfigId;
	}

	public Map<Integer,XianShiLiBaoConfig> loadAll() {
	 return configs;
	}
}