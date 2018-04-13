package com.junyou.bus.xunbao.configure.export;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 寻宝配置表 
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-2-5 下午2:35:42
 */
@Component
public class XunBaoConfig2ExportService extends AbsClasspathConfigureParser {
	 

	
	
	/** TODO  换成热发布的
	  * configFileName
	 */
	private String configureName = "XunBaoBiao2.jat";
	
	private Map<Integer,XunBaoConfig> xunBaoConfigs = new HashMap<Integer, XunBaoConfig>();
	

	
	private int defautId;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data); 
		
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		defautId = 0; 
		
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				XunBaoConfig config = createXunBaoConfig(tmp);
							
				xunBaoConfigs.put(config.getId(), config);  
				
				if(defautId!=0){
					defautId = config.getId();
				}
			}
		}
	}
	
	public XunBaoConfig createXunBaoConfig(Map<String, Object> tmp) {
		int  allOdds=0;
		XunBaoConfig config = new XunBaoConfig();	
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
//		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setMincishu(CovertObjectUtil.object2int(tmp.get("mincishu")));
		config.setMaxcishu(CovertObjectUtil.object2int(tmp.get("maxcishu")));
		
		int i=1;
		while(i<100){
			
			String drop = CovertObjectUtil.object2String(tmp.get(GameConstants.DROP+i));
			Integer dropCount=CovertObjectUtil.object2int(tmp.get(GameConstants.DROP+i+GameConstants.COUNT));
			
			if("".equals(drop) || dropCount == 0){
				break;
			}
			int dropOdds=CovertObjectUtil.object2int(tmp.get(GameConstants.DROP+i+GameConstants.ODDS));
			allOdds+=dropOdds;
			int index = drop.indexOf(GameConstants.ZB_FLAG);
			boolean isZB= index != -1 ? true:false;
			
			config.addGoodsOdds(new Object[]{drop,dropCount,isZB},dropOdds);  
			
			i++;
		}
		
		config.setAllOdds(allOdds);
							
		return config;
	} 
	 
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public Collection<XunBaoConfig> loadAllConfig(){
		return  xunBaoConfigs.values();
	}

	public XunBaoConfig getXunBaoIdByCount(int times) {
		for (XunBaoConfig  config : xunBaoConfigs.values()) {
			if(config.getMincishu()<=times && times<=config.getMaxcishu()){
				return config;
			}
		}
		
		return xunBaoConfigs.get(defautId);
	}
	
	public XunBaoConfig getXunBaoById(int id){
		return xunBaoConfigs.get(id);
	}
}