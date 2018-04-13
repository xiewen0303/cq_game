package com.junyou.gameconfig.map.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.export.teleport.EndTimeCheckRule;
import com.junyou.gameconfig.export.teleport.ICheckRule;
import com.junyou.gameconfig.export.teleport.MinLevelCheckRule;
import com.junyou.gameconfig.export.teleport.StartTimeCheckRule;
import com.junyou.gameconfig.export.teleport.TeleportChecker;
import com.junyou.gameconfig.map.configure.util.DiTuConfigUtil;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;

/**
 * 地图信息配置
 *@author  DaoZheng Yuan
 *@created 2013-10-16上午10:18:19
 */
@Component
public class DiTuConfigExportService extends AbsClasspathConfigureParser {

	private Map<Integer, MapLineConfig> configs;
	private Map<Integer, DiTuConfig> dituConfigs;
	//新手出生地图id
	private static Integer BIRTH_MAP_ID;
	//竞技场小黑屋地图
	private static Integer JINGJI_SAFE_MAP_ID;
	//配置的第一个主城 当死亡找不到地图时异常处理的 
	private static Integer FIRST_MAIN_MAP_ID;
	
	private static final String SPLIT_CHAR = "\\|";
	private static final String SUB_SPLIT_CHAR = ":";
	
	/**
	 * 配置名
	 */
	private String configureName = "DiTuBiao.jat";
	
	protected String getConfigureName() {
		return configureName;
	}
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, MapLineConfig> configs = new HashMap<>();
		Map<Integer, DiTuConfig> dituConfigs = new HashMap<>();
		//地图全局临时变量
		Map<Integer, Integer> mapIds = new HashMap<>();
		
		for (Object obj:dataList) {
			Map<Object, Object> tmp = (Map<Object, Object>)obj;
			if (null != tmp) {
				int mapId = CovertObjectUtil.object2int(tmp.get("id"));
				if("".equals(mapId) || mapId == 0){
					continue;
				}
				
				mapIds.put(mapId, mapId);
				
				DiTuConfig diTuConfig = new DiTuConfig();
				diTuConfig.setId(mapId);
				
				int type = CovertObjectUtil.object2int(tmp.get("type"));
				diTuConfig.setType(type);
				//新手出生地图id设置
				if(DiTuConfigUtil.isBirthMap(type)){
					BIRTH_MAP_ID = mapId;
				}else if(DiTuConfigUtil.isSafeMap(type)){
					JINGJI_SAFE_MAP_ID = mapId;
				}
				//配置的第一个主城
				if(FIRST_MAIN_MAP_ID == null && DiTuConfigUtil.isMainMap(type)){
					FIRST_MAIN_MAP_ID = mapId;
				}
				
				diTuConfig.setIsHuiChen(CovertObjectUtil.object2boolean(tmp.get("huicheng")));
				diTuConfig.setName(CovertObjectUtil.object2String(tmp.get("name")));
				Integer reviveMapId = CovertObjectUtil.object2Integer(tmp.get("map"));
				if(reviveMapId == null || reviveMapId < 1){
					diTuConfig.setReviveMapId(mapId);
				}else{
					diTuConfig.setReviveMapId(reviveMapId);
				}
				//是否不可骑乘
				diTuConfig.setNoRideAble(CovertObjectUtil.object2boolean(tmp.get("ridable")));
				//是否不可以使用随机石
				diTuConfig.setNoRandom(CovertObjectUtil.object2boolean(tmp.get("randomable")));
				//是否随机出生 
				diTuConfig.setRandomBirth(CovertObjectUtil.object2boolean(tmp.get("random")));
				//不可使用回城石
				diTuConfig.setNoHuichengable(CovertObjectUtil.object2boolean(tmp.get("huichengable")));
				
				//镖车是否可以进入
				diTuConfig.setBiaoCheCanEnter(CovertObjectUtil.object2boolean(tmp.get("biaoche")));
				//攻城战期间杀人是否不加PK值
				diTuConfig.setGcWarNoAddPk(CovertObjectUtil.object2boolean(tmp.get("gczpk")));
				//杀死后地图不加PK值,死亡后不掉落物品(活动期间活动地图)
				diTuConfig.setNoPkValue(CovertObjectUtil.object2boolean(tmp.get("pk1")));
				//杀死后是否不增加PK值(全时段全地图)
				diTuConfig.setAddPk(!CovertObjectUtil.object2boolean(tmp.get("killer")));
				
				//非定时活动经验和掉落
				diTuConfig.setExpNoBv(CovertObjectUtil.object2Float(tmp.get("expBl")));
				diTuConfig.setNoDrop(CovertObjectUtil.object2boolean(tmp.get("dropBl")));
				
				//是否有新手保护
				diTuConfig.setNewProtect(CovertObjectUtil.object2boolean(tmp.get("baohu")));
				
				//地图是否无法PK
				diTuConfig.setSafeMap(CovertObjectUtil.object2boolean(tmp.get("safe")));
				
				//地图是否可摆摊
				diTuConfig.setBaitan(CovertObjectUtil.object2int(tmp.get("baitan")));
				
				//是否启动时直接创建地图     0或者不填 启动创建  1 不启动创建（当有玩家第一次进入时再创建）
				diTuConfig.setBootNoInit(CovertObjectUtil.object2boolean(tmp.get("chuangjian")));
				
				diTuConfig.setMode(CovertObjectUtil.object2int(tmp.get("mode")));
				//GM是否可以通过GM传送进入
				diTuConfig.setGmMove(CovertObjectUtil.object2boolean(tmp.get("gmmove")));
				//随机坐标
				String randomZb = CovertObjectUtil.object2String(tmp.get("zuobiao"));
				if(!CovertObjectUtil.isEmpty(randomZb)){
					List<int[]> randomPoints = new ArrayList<int[]>();
					
					String[] tmpZb = randomZb.split(SPLIT_CHAR);
					
					for (String rzb : tmpZb) {
						String[] tmpRzb = rzb.split(SUB_SPLIT_CHAR);
							randomPoints.add(new int[]{Integer.parseInt(tmpRzb[0]),Integer.parseInt(tmpRzb[1])});
					}
					
					if(randomPoints.size() > 0){
						diTuConfig.setRandomPoints(randomPoints);
					}
				}
				
				//出生随机坐标
				String birthRandomZb = CovertObjectUtil.object2String(tmp.get("zuobiao1"));
				if(!CovertObjectUtil.isEmpty(birthRandomZb)){
					List<int[]> randomPoints = new ArrayList<int[]>();
					
					String[] tmpZb = birthRandomZb.split(SPLIT_CHAR);
					
					for (String rzb : tmpZb) {
						String[] tmpRzb = rzb.split(SUB_SPLIT_CHAR);
						
						randomPoints.add(new int[]{Integer.parseInt(tmpRzb[0]),Integer.parseInt(tmpRzb[1])});
					}
					
					if(randomPoints.size() > 0){
						diTuConfig.setBirthRandomPoints(randomPoints);
					}
				}
				
				
				//地图分线信息
				MapLineConfig mapLineConfig = mapLineAnalysis(tmp);
				if(mapLineConfig != null){
					configs.put(mapLineConfig.getMapId(), mapLineConfig);
					diTuConfig.setMapLineConfig(mapLineConfig);
				}
				
				//是否可呼救
				diTuConfig.setHelp(CovertObjectUtil.object2boolean(tmp.get("hujiu")));
				//设置复活模式
				diTuConfig.setFuhuoType(CovertObjectUtil.object2int(tmp.get("fuhuo")));
				//地图传送验证条件
				teleportAnalysis(tmp,diTuConfig);
				
				dituConfigs.put(diTuConfig.getId(), diTuConfig);
				
				printDebug(diTuConfig);
			}
		}
		this.configs = configs;
		this.dituConfigs = dituConfigs;
	}
	
	/**
	 * 打印可呼救的地图
	 * @param diTuConfig
	 */
	private void printDebug(DiTuConfig diTuConfig){
		if(GameServerContext.getGameAppConfig().isDebug()){
			
			if(diTuConfig.isHelp() && ("3059".equals(diTuConfig.getId()) || "3060".equals(diTuConfig.getId()) || "3061".equals(diTuConfig.getId())) ){
				System.out.println("Warning map help mijing mapName[mapId]:"+diTuConfig.getName()+"["+diTuConfig.getId()+"]");
			}
		}
	}

	
	/**
	 * 地图分线解析
	 * @param tmp
	 * @return
	 */
	private MapLineConfig mapLineAnalysis(Map<Object, Object> tmp){
		Integer defaultLine = CovertObjectUtil.object2int(tmp.get("tacitopenvalue"));
		if(defaultLine > 0){
			
			MapLineConfig mapLineConfig = new MapLineConfig();
			
			mapLineConfig.setMapId(CovertObjectUtil.object2int(tmp.get("id")));
			mapLineConfig.setDefaultLine(defaultLine);
			mapLineConfig.setMaxLine(CovertObjectUtil.object2int(tmp.get("maxlinevalue")));
			mapLineConfig.setMaxLoad(CovertObjectUtil.object2int(tmp.get("tacitmaxpeople")));
			if(mapLineConfig.getMaxLoad() < 50){
				System.out.println("警告:地图Id:"+mapLineConfig.getMapId()+"单线最大人数小于50人,策划配置的是:"+mapLineConfig.getMaxLoad());
			}
			int rotateincrement = CovertObjectUtil.object2int(tmp.get("rotateincrement"));
			if(rotateincrement < 1){
				ChuanQiLog.error("警告：地图id:{}配置的增容小于1，策划配置的是:{}",mapLineConfig.getMapId(),rotateincrement);
				rotateincrement = 1;
			}
			mapLineConfig.setMaxLoadIncrease(rotateincrement);
			
			return mapLineConfig;
		}else{
			return null;
		}
	}
	
	
	/**
	 * 创建传送条件
	 */
	private void teleportAnalysis(Map<Object,Object> tmp,DiTuConfig diTuConfig) {
		if(null != tmp){
			TeleportChecker checker = null;
			Integer type = CovertObjectUtil.object2int(tmp.get("entertype"));
			if(type == 1){
				checker = new TeleportChecker();
				//角色最小等级
				Integer minLevel = CovertObjectUtil.object2int(tmp.get("data1"));
				if(minLevel > 0){
					ICheckRule checkRule = new MinLevelCheckRule(minLevel);
					checker.addRule(checkRule);
				}
			}else if(type == 2){//指定时间内可进入
				checker = new TeleportChecker();
				String startTime = CovertObjectUtil.object2String(tmp.get("data1"));
				String endTime = CovertObjectUtil.object2String(tmp.get("data2"));
				long time1 = DatetimeUtil.parseDateMillTime(startTime,DatetimeUtil.FORMART6);
				long time2 = DatetimeUtil.parseDateMillTime(endTime,DatetimeUtil.FORMART6);
				if(time1 > 0){
					ICheckRule checkRule = new StartTimeCheckRule(time1);
					checker.addRule(checkRule);
				}
				if(time2 > 0){
					ICheckRule checkRule = new EndTimeCheckRule(time1);
					checker.addRule(checkRule);
					diTuConfig.setKickTime(time2);
				}
			}
			
			if(checker != null && checker.ruleSize() > 0){
				diTuConfig.setTeleportChecker(checker);
			}
		}
		
	}
	

	/**
	 * 根据地图加载地图信息
	 * @param mapId
	 * @return
	 */
	public DiTuConfig loadDiTu(int mapId){
		return dituConfigs.get(mapId);
	}
	
	public DiTuConfig loadBirthDiTu(){
		return dituConfigs.get(BIRTH_MAP_ID);
	}
	
	public DiTuConfig loadFirstMainDiTu(){
		return dituConfigs.get(FIRST_MAIN_MAP_ID);
	}
	/**
	 * 获取竞技场安全地图配置
	 * @return
	 */
	public DiTuConfig loadSafeDiTu(){
		return dituConfigs.get(JINGJI_SAFE_MAP_ID);
	}
	
	/**
	 * 根据地图加载分线信息
	 * @param mapId
	 * @return
	 */
	public MapLineConfig loadMapLineByMapId(Integer mapId) {
		return configs.get(mapId);
	}

	
	/**
	 * 加载所有地图分线信息
	 * @return
	 */
	public List<MapLineConfig> loadMapLineAll() {
		return new ArrayList<MapLineConfig>(configs.values());
	}
	
}
