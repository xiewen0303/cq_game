package com.junyou.bus.activityboss.manage;

 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.junyou.bus.activityboss.util.ActivityBossUtil;
import com.junyou.constants.GameConstants;

/**
 * 活动boss管理器
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-25 下午2:56:18
 */
public class ActivityBossManage {
	
	/**
	 * 野外boss活动配置Id = 各个线上的boss数据
	 */
	private static Map<String,List<ActivityBoss>> idBosses = new ConcurrentHashMap<>(); 
	
	/**
	 * bossMonsterId =  ActivityBoss
	 */
	private static Map<Long,ActivityBoss> bossMonster = new ConcurrentHashMap<>();
	
	/**
	 *map_configId = ActivityBossInfo 记录是否杀死对应的boss
	 */
	private static Map<String,ActivityBossInfo> abInfos = new ConcurrentHashMap<>();
	
	/**
	 * 登陆时初始化对应的数据
	 * @param abInfos
	 */
	public static void initABInfos(Map<String,ActivityBossInfo> abInfoTemps){
		if(abInfoTemps != null) {
			abInfos.putAll(abInfoTemps);
		}
	}
	
	

	
	/**
	 * 刷出怪物
	 * @param bossInfo
	 */
	public static void addActivityBossInfo(int configId,int line,long endTime,int mapId){
		String key = ActivityBossUtil.getKey(mapId, configId);
		ActivityBossInfo abinfo =  abInfos.get(key);
		
		if(abinfo == null){
			abinfo = new ActivityBossInfo(configId,mapId);
			abInfos.put(key, abinfo);
		}
		
		BossKillInfo bkInfo = new BossKillInfo();
		bkInfo.setEndTime(endTime);
		bkInfo.setLine(line);
		bkInfo.setState(GameConstants.BOSS_NO_KILL);
		
		abinfo.addBossKillInfo(bkInfo);
	}
	
	public static ActivityBoss getBoss(long monsterId){
		return bossMonster.get(monsterId);
	}
	
	/**
	 * 是否还存在定时活动boss
	 * @param monsterId
	 * @return
	 */
	public static boolean isBossMonster(long monsterId){
		return bossMonster.containsKey(monsterId);
	}
	
	/**
	 * 添加进入场景的野外boss
	 * @param aBoss
	 */
	public static void add(ActivityBoss aBoss){
		bossMonster.put(aBoss.getId(), aBoss);
		
		List<ActivityBoss> aBosses = idBosses.get(aBoss.getDsMonsterConfigId());
		if(aBosses == null){
			aBosses = new ArrayList<>();
			idBosses.put(aBoss.getDsMonsterConfigId(), aBosses);
		}
		aBosses.add(aBoss);
	}
	
	
	/**
	 * 移出索引
	 * @param monsterId
	 */
	public static ActivityBoss remove(long monsterId) {
		ActivityBoss activityBoss = bossMonster.remove(monsterId);
		if(activityBoss == null) {
			return null;
		}
		
		List<ActivityBoss> activitys = idBosses.get(activityBoss.getDsMonsterConfigId());
		activitys.remove(activityBoss);
		return null;
	}
	
	/**
	 * 修改bossState 防止停服怪没被杀死
	 * @param dsMonsterCfgId
	 * @param line
	 */
	public static void modifyBossState(String dsMonsterCfgId,int line,int state,String roleName){
		ActivityBossInfo  info = abInfos.get(dsMonsterCfgId);
		if(info != null){
			BossKillInfo bossKillInfo = info.getBossKillInfo(line);
			bossKillInfo.setState(state);
			bossKillInfo.setRoleName(roleName);
		}
	}
	

	/**
	 * 野外boss活动配置Id = 各个线上的boss数据
	 */
	public static Map<String, List<ActivityBoss>> getIdBosses() {
		return idBosses;
	}

	public static Map<String, ActivityBossInfo> getAbInfos() {
		return abInfos;
	}  
	
	public static ActivityBossInfo getActivityBossInfo(String map_configId) {
		return abInfos.get(map_configId);
	}
}
