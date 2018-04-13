package com.junyou.gameconfig.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.configure.ClientTimeScope;
import com.junyou.configure.ClientTimeScopeNode;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.export.teleport.ICheckRule;
import com.junyou.gameconfig.export.teleport.MaxLevelCheckRule;
import com.junyou.gameconfig.export.teleport.MustItemCheckRule;
import com.junyou.gameconfig.export.teleport.TeleportChecker;
import com.junyou.gameconfig.export.teleport.TimeScopeCheckRule;
import com.junyou.gameconfig.export.teleport.UseItemCheckRule;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

public class TeleportConfigExportService extends AbsClasspathConfigureParser{
	
	@Override
	protected String getConfigureName() {
		return "teleport.jat";
	}
	
	private Map<String, WaypointTeleportConfig> configs;
	private Map<String, NpcTeleportConfig> npcs;

	protected void configureDataResolve(byte[] data) {
		
		Map<Object,Object> teleportConfigMap = GameConfigUtil.getResourceMap(data);
		configs = new HashMap<>();
		npcs = new HashMap<>();
		
		Object[] shoesTeleport = (Object[])teleportConfigMap.get("fx");
		createShoesTeleport(shoesTeleport);
		
		Object[] waypointTeleport = (Object[])teleportConfigMap.get("wayPoint");
		createWaypointTeleport(waypointTeleport);
		
		Object[] npcTeleport = (Object[])teleportConfigMap.get("npcTeleport");
		createNpcTeleport(npcTeleport);
		
	}
	

	/**
	 * 创建飞鞋传送
	 */
	private void createShoesTeleport(Object[] shoesTeleport) {
		// TODO Auto-generated method stub
	}

	/**
	 * 创建传送点传送
	 */
	@SuppressWarnings("unchecked")
	private void createWaypointTeleport(Object[] waypointTeleport) {
		// TODO Auto-generated method stub
		
		if(null != waypointTeleport){
			for(Object tmp : waypointTeleport){
				Map<String, Object> waypointTeleportMap = (Map<String, Object>)tmp;
				
				String id = (String)waypointTeleportMap.get("id");
				String mapId = (String)waypointTeleportMap.get("toMap");
				int x = (Integer)waypointTeleportMap.get("toX");
				int y = (Integer)waypointTeleportMap.get("toY");
				
				WaypointTeleportConfig entity = new WaypointTeleportConfig();
				entity.setId(id);
				entity.setMapId(mapId);
				entity.setX(x);
				entity.setY(y);
				//money	maxlevel minlevel mustitem useitem completetasks countryoffice clan vip	actime	camp
				TeleportChecker checker = createChecker((Map<String,Object>)waypointTeleportMap.get("limit"));
				entity.setTeleportChecker(checker);
				configs.put(entity.getId(), entity);
			}
		}
		
	}
	
	/**
	 * 创建传送验证器
	 */
	private TeleportChecker createChecker(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
		TeleportChecker checker = new TeleportChecker();
		
		Integer maxLevel = (Integer)map.get("maxlevel");
		if(null != maxLevel){
			ICheckRule checkRule = new MaxLevelCheckRule(maxLevel);
			checker.addRule(checkRule);
		}
		
		String mustItem = (String)map.get("mustitem");
		if(null != mustItem){
			ICheckRule checkRule = new MustItemCheckRule(mustItem);
			checker.addRule(checkRule);
		}
		
		String useItem = (String)map.get("useitem");
		if(null != useItem){
			ICheckRule checkRule = new UseItemCheckRule(useItem);
			checker.addRule(checkRule);
		}
		
		Object[] timeScope = (Object[])map.get("actime");
		if(null != timeScope){
			
			ClientTimeScope scope = new ClientTimeScope();
			for(Object node : timeScope){
				
				ClientTimeScopeNode scopeNode = new ClientTimeScopeNode();
				Object[] nodeData = (Object[])node;
				
				//年
				Object[] yearData = (Object[])nodeData[0];
				if(null != yearData && yearData.length > 0){
					List<Integer> years = new ArrayList<Integer>();
					for(Object tmp : yearData){
						String tmpData = String.valueOf(tmp);
						if(tmpData.equals("*")){
							years.add(ClientTimeScopeNode.VALUE_WILDCARD);
						}else{
							years.add(Integer.parseInt(tmpData));
						}
					}
					scopeNode.setYears(years);
				}
				
				//月
				Object[] monthData = (Object[])nodeData[1];
				if(null != monthData && monthData.length > 0){
					List<Integer> months = new ArrayList<Integer>();
					for(Object tmp : monthData){
						String tmpData = String.valueOf(tmp);
						if(tmpData.equals("*")){
							months.add(ClientTimeScopeNode.VALUE_WILDCARD);
						}else{
							months.add(Integer.parseInt(tmpData));
						}
					}
					scopeNode.setMonths(months);
				}
				
				//日
				Object[] dayData = (Object[])nodeData[2];
				if(null != dayData && dayData.length > 0){
					List<Integer> days = new ArrayList<Integer>();
					for(Object tmp : dayData){
						String tmpData = String.valueOf(tmp);
						if(tmpData.equals("*")){
							days.add(ClientTimeScopeNode.VALUE_WILDCARD);
						}else{
							days.add(Integer.parseInt(tmpData));
						}
					}
					scopeNode.setDays(days);
				}
				
				//星期
				Object[] weekData = (Object[])nodeData[3];
				if(null != weekData && weekData.length > 0){
					List<Integer> weeks = new ArrayList<Integer>();
					for(Object tmp : yearData){
						String tmpData = String.valueOf(tmp);
						if(tmpData.equals("*")){
							weeks.add(ClientTimeScopeNode.VALUE_WILDCARD);
						}else{
							weeks.add(Integer.parseInt(tmpData));
						}
					}
					scopeNode.setWeeks(weeks);
				}
				
				//时间
				Object[] datetimeData = (Object[])nodeData[4];
				if(datetimeData.length > 0){
					//开始时间
					Object[] beginDatatime = (Object[])datetimeData[0];
					Integer beginHour = (Integer)beginDatatime[0];
					Integer beginMin = (Integer)beginDatatime[1];
					scopeNode.setBeginHour(beginHour);
					scopeNode.setBeginMin(beginMin);
					
				}
				//结束时间
				if(datetimeData.length > 1){
					//开始时间
					Object[] endDatatime = (Object[])datetimeData[1];
					Integer endHour = (Integer)endDatatime[0];
					Integer endMin = (Integer)endDatatime[1];
					scopeNode.setEndHour(endHour);
					scopeNode.setEndMin(endMin);
				}
				
				scope.addTimeScopeNode(scopeNode);
			}
			
			checker.addRule(new TimeScopeCheckRule(scope));
			
		}
		
		if(checker.ruleSize() == 0){
			return null;
		}
		
		return checker;
	}

	/**
	 * 创建npc传送
	 */
	@SuppressWarnings("unchecked")
	private void createNpcTeleport(Object[] npcTeleport) {
		// TODO Auto-generated method stub
		
		if(null != npcTeleport){
			for(Object tmp : npcTeleport){
				Map<String, Object> npcTeleportMap = (Map<String, Object>)tmp;
				
				String id = CovertObjectUtil.object2String(npcTeleportMap.get("id"));
				String mapId = (String)npcTeleportMap.get("mapid");
				int x = (Integer)npcTeleportMap.get("x");
				int y = (Integer)npcTeleportMap.get("y");
				
				NpcTeleportConfig entity = new NpcTeleportConfig();
				entity.setId(id);
				entity.setMapId(mapId);
				entity.setX(x);
				entity.setY(y);
				
				//money	maxlevel	minlevel	mustitem	useitem	completetasks	countryoffice	clan	vip	actime	camp
				TeleportChecker checker = createChecker((Map<String,Object>)npcTeleportMap.get("limit"));
				entity.setTeleportChecker(checker);
				npcs.put(entity.getId(), entity);
			}
		}
	}

	public NpcTeleportConfig loadNpcTeleport(String npcTeleportId) {
		return npcs.get(npcTeleportId);
	}

	public WaypointTeleportConfig loadWaypointTeleport(String waypointId) {
		return configs.get(waypointId);
	}
	

}
