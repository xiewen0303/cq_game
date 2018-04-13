package com.junyou.bus.daytask.configure; 
import org.springframework.stereotype.Component;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil; 
import com.junyou.utils.lottery.RandomUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @description 日常任务表 
 *
 * @author wind
 * @date 2015-03-16 15:11:33
 */
@Component
public class RiChangRenWuConfigExportService extends AbsClasspathConfigureParser {
	
	/**
	  * configFileName
	 */
	private String configureName = "RiChangRenWu.jat";

	private Map<Integer,RiChangRenWuConfig> datas = null;
	private Map<BeginEnd,List<Integer>> idLevels = null;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		datas = new HashMap<>();
		idLevels = new HashMap<>();
		 
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				RiChangRenWuConfig config = createRiChangRenWuConfig(tmp);
				datas.put(config.getId(), config);
				 
				initIdLevels(config);
			}
		}
	}
	
	private void initIdLevels(RiChangRenWuConfig config){
		BeginEnd beginEnd = new BeginEnd(config.getMinlevel(), config.getMaxlevel(),config.getType());
		List<Integer> taskIds = idLevels.get(beginEnd);
		if(taskIds == null){
			taskIds = new ArrayList<>();
			idLevels.put(beginEnd, taskIds);
		}
		taskIds.add(config.getId());
	}
	
	public RiChangRenWuConfig createRiChangRenWuConfig(Map<String, Object> tmp) {
		RiChangRenWuConfig config = new RiChangRenWuConfig();	
			
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
											
		config.setMinlevel(CovertObjectUtil.object2int(tmp.get("minlevel")));
											
		config.setMaxlevel(CovertObjectUtil.object2int(tmp.get("maxlevel")));
		
		config.setMonster(CovertObjectUtil.object2String(tmp.get("monster"))); 
		
		config.setMap(CovertObjectUtil.object2int(tmp.get("map"))); 
	
		config.setNum(CovertObjectUtil.object2int(tmp.get("num")));
		
		config.setJiangmoney(CovertObjectUtil.object2int(tmp.get("jiangmoney")));
		
		config.setJiangexp(CovertObjectUtil.object2int(tmp.get("jiangexp")));
		
		config.setJiangzhen(CovertObjectUtil.object2int(tmp.get("jiangzhen")));
		
		config.setJianggongxian(CovertObjectUtil.object2int(tmp.get("jianggongxian")));
		
		String itemsIdStr = CovertObjectUtil.object2String(tmp.get("jiangitem"));
		 
		Map<String,Integer> awards = analysis2Map(itemsIdStr);
		config.setAwards(awards);
											
		config.setFinish(CovertObjectUtil.object2int(tmp.get("finish")));
											
		config.setFly(CovertObjectUtil.object2int(tmp.get("fly"))==1);
		
		config.setZuoBiao(CovertObjectUtil.object2String(tmp.get("zuobiao")));
		
		return config;
	}
	
	private Map<String,Integer> analysis2Map(String itemsIdStr){
		
		Map<String,Integer> awards =null;
		if(itemsIdStr != null &&!"".equals(itemsIdStr.trim()) && !"0".equals(itemsIdStr.trim())){
			
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
	
	public RiChangRenWuConfig loadById(int id){
		return datas.get(id);
	}
	
	public RiChangRenWuConfig randomConfig(int level,int type){
		List<Integer> taskIds = getIdBy(level,type);
		
		if(taskIds != null && taskIds.size()> 0){
			
			int index = RandomUtil.getIntRandomValue(taskIds.size());
			int taskId = taskIds.get(index);
			return datas.get(taskId);
		}

		ChuanQiLog.error("获取任务配置异常,level:"+level+"\t type:"+type);
		return null;
	}
	
	public List<Integer> getIdBy(int level,int type){
		for (Entry<BeginEnd,List<Integer>> element : idLevels.entrySet()) {
			
			BeginEnd beginEnd = element.getKey();
			
			if(beginEnd.isContain(level,type)){
				return element.getValue();
			}
		}
		return null;
	}
}