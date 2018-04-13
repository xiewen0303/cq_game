package com.junyou.stage.model.element.monster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地区首领BOSS管理 
 *@author  DaoZheng Yuan
 *@created 2013-7-26下午2:09:04
 */
public class BossManage {

	private static Map<String,BossVo> bossMap = new HashMap<String, BossVo>();
	
	
	/**
	 * 地区首领加入管理 
	 * @param monsterId
	 */
	public static void addBoss(String monsterId,String mapId,int x,int y,int produceDelay){
		BossVo bossVo = bossMap.get(monsterId);
		
		if(bossVo != null){
			bossLiveById(monsterId);
		}else{
			bossVo = new BossVo(monsterId, mapId, x, y,produceDelay);
			bossMap.put(monsterId, bossVo);
		}
	}
	
	/**
	 * 是否是首次,刷新出来
	 * @param monsterId
	 * @return
	 */
	public static boolean isFirstRefresh(String monsterId){
		BossVo bossVo = bossMap.get(monsterId);
		if(bossVo == null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 地区首领死亡
	 * @param monsterId
	 * @param roleId
	 * @param roleName
	 * @param configId
	 */
	public static void bossDeadById(String monsterId,String roleId,String roleName,int configId){
		BossVo bossVo = bossMap.get(monsterId);
		if(bossVo != null){
			bossVo.changeDeadState(roleId, roleName, configId);
		}
	}
	
	/**
	 * 地区首领复活
	 * @param monsterId
	 */
	public static void bossLiveById(String monsterId){
		BossVo bossVo = bossMap.get(monsterId);
		if(bossVo != null){
			bossVo.changeLiveState();
		}
	}
	
	/**
	 * 获取指定地区首领数据
	 * @param monsterIds
	 * @return
	 */
	public static Object getBossesData(Object[] monsterIds){
		if(bossMap.size() == 0){
			return null;
		}
		
		if(monsterIds == null || monsterIds.length == 0){
			return null;
		}
		
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < monsterIds.length; i++) {
			Object monsterId = monsterIds[i];
			BossVo bossVo = bossMap.get(monsterId.toString());
			
			if(bossVo != null){
				list.add(bossVo.getMsgData());
			}
		}
		
		if(list.size() == 0){
			return null;
		}
		
		return list.toArray();
	}
}
