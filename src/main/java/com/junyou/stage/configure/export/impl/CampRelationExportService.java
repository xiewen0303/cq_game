package com.junyou.stage.configure.export.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

/**
 *  阵营配置
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-4 上午11:34:05
 */
@Component
public class CampRelationExportService extends AbsClasspathConfigureParser{
	
	/**
	 * 配置名
	 */
	private String configureName = "ZhenYingGuanXiBiao.jat";
	
	private static final Integer GUANXI_NEUTRAL = 0;//中立
	private static final Integer GUANXI_FRIEND = 1;//友好
	private static final Integer GUANXI_ENEMY = 2;//敌对
	
	private Map<Integer, EnemyCampsConfig> configs;
	
	@Override
	protected String getConfigureName() {
		
		return configureName;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		Map<Integer, EnemyCampsConfig> configs = new HashMap<>();
		
		Object[] datas = GameConfigUtil.getResource(data);
		
		//关系个数
		int guanXiIds = datas.length;
		
		//解数据
		for (Object obj:datas) {
			
			Map<Object, Object> tmp = (Map<Object, Object>)obj;
			
			EnemyCampsConfig config = new EnemyCampsConfig();
			Integer id = CovertObjectUtil.object2int(tmp.get("id"));
			config.setId(id);
			
			for(int i = 0 ; i < guanXiIds ; i++){
				
				Integer guanXi = CovertObjectUtil.object2int(tmp.get("vs" + i));//两者之间的关系 
				config.add(i ,guanXi);
			}
			
			configs.put(config.getId(), config);
		}
		this.configs = configs;
	}
	
	/**
	 * 获取敌对阵营ids
	 * @param camp
	 * @return
	 */
	public List<Integer> getEnemyCamps(Integer camp) {
		EnemyCampsConfig config = configs.get(camp);
		if(config == null){
			ChuanQiLog.debug("EnemyCampsConfig is null camp:"+camp);
			return null;
		}
		
		List<Integer> campIds = null;
		Map<Integer, Integer> gxMap = config.getGuanXiMap();
		if(gxMap != null && gxMap.size() > 0){
			campIds = new ArrayList<>();
			
			for (Integer campId : gxMap.keySet()) {
				Integer tmpGx = gxMap.get(campId);
				
				if(GUANXI_ENEMY.equals(tmpGx)){
					campIds.add(campId);
				}
			}
			
			if(campIds.size() == 0){
				campIds = null;
			}
		}
		
		return campIds;
	}
	
	/**
	 * 敌对关系
	 */
	public boolean isEnemy(String id, String compareId) {
		EnemyCampsConfig config = configs.get(id);
		
		Integer guanxi = config.getGuanXiMap().get(compareId);
		
		return GUANXI_ENEMY.equals(guanxi);
	}

	/**
	 * 中立关系 
	 */
	public boolean isNeutral(String id, String compareId) {
		EnemyCampsConfig config = configs.get(id);
		
		Integer guanxi = config.getGuanXiMap().get(compareId);
		
		return GUANXI_NEUTRAL.equals(guanxi);
	}
	
	/**
	 * 友好关系 
	 */
	public boolean isFriend(String id, String compareId) {
		
		EnemyCampsConfig config = configs.get(id);
		
		Integer guanxi = config.getGuanXiMap().get(compareId);
		
		return GUANXI_FRIEND.equals(guanxi);
	}
}