package com.junyou.bus.daytask.configure;

 
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 任务总环数 
 *
 * @author wind
 * @date 2015-03-16 16:09:39
 */
@Component
public class RiChangJiangLiConfigExportService extends AbsClasspathConfigureParser {
	 
	
	/**
	  * configFileName
	 */
	private String configureName = "RiChangJiangLi.jat";
	
	private Map<Integer,RiChangJiangLiConfig> jiangLiConfigs = null;
	
	private Map<BeginEnd,Integer> levelLoopIds =null;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		jiangLiConfigs = new HashMap<Integer, RiChangJiangLiConfig>();
		levelLoopIds =new HashMap<>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				RiChangJiangLiConfig config = createRiChangJiangLiConfig(tmp);
				jiangLiConfigs.put(config.getId(), config);			
				levelLoopIds.put(new BeginEnd(config.getMinlevel(), config.getMaxlevel(),config.getType()), config.getId());
			}
		}
	}
	
	public RiChangJiangLiConfig createRiChangJiangLiConfig(Map<String, Object> tmp) {
		RiChangJiangLiConfig config = new RiChangJiangLiConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setMaxcount(CovertObjectUtil.object2int(tmp.get("maxcount")));
											
		config.setMaxlevel(CovertObjectUtil.object2int(tmp.get("maxlevel")));
										 
		String itemsIdStr = CovertObjectUtil.object2String(tmp.get("jiangitem"));
		config.setAwards(analysis2Map(itemsIdStr));
											
		config.setMinlevel(CovertObjectUtil.object2int(tmp.get("minlevel")));
		
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		
		config.setJiangmoney(CovertObjectUtil.object2int(tmp.get("jiangmoney")));
		
		config.setJiangexp(CovertObjectUtil.object2int(tmp.get("jiangexp")));
		
		config.setJiangzhen(CovertObjectUtil.object2int(tmp.get("jiangzhen")));
		
		config.setJianggongxian(CovertObjectUtil.object2int(tmp.get("jianggongxian")));
		
		return config;
	}
	
	
	private Map<String,Integer> analysis2Map(String itemsIdStr){
		
		Map<String,Integer> awards =null;
		if(itemsIdStr != null &&!"".equals(itemsIdStr) && !"0".equals(itemsIdStr.trim())){
			
			String[] items = itemsIdStr.split("\\|");
			awards = new HashMap<>();
			for (String itemsStr : items) {
				String[] datas = itemsStr.split(":");
				awards.put(datas[0],CovertObjectUtil.object2int(datas[1]));
			}
		}
		return awards;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public RiChangJiangLiConfig loadById(int id){
		return jiangLiConfigs.get(id);
	}

	public Integer getConfigIdByLevel(int level,int type) {
		for (Entry<BeginEnd,Integer> element : levelLoopIds.entrySet()) {
			BeginEnd beginEnd  = element.getKey();
			if( beginEnd.isContain(level, type)){
				return element.getValue();
			}
		}
		ChuanQiLog.error("获取配置信息有误：className:"+this.getClass().getSimpleName() +"\t level:"+level+"\ttype:"+type);
		
		return null;
	}
}