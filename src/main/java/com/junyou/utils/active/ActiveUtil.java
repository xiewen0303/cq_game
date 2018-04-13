package com.junyou.utils.active;

import java.util.HashMap;
import java.util.Map;

/**
 * 活动工具
 */
public class ActiveUtil {
	private static ActiveUtil activeUtil = new ActiveUtil();
	private boolean canUseFX = true;
	private boolean canChangeGuild = true;
	private boolean isTerritory = false;
	private int killBei = 0;
	private int yabiaoBei = 1;//押镖经验倍率
	private boolean isWenquan = false;
	private boolean isHcZBS = false; //皇城争霸赛(云宫之战)
	private boolean isKfYunGong = false; //跨服云宫之巅
	private Map<Integer,Float> dazuoMap = new HashMap<>();
	
	
	
	/**是否争霸赛*/
	public static boolean isHcZBS() {
		return  activeUtil.isHcZBS;
	}
	public static void setHcZBS(boolean isHcZBS) {
		activeUtil.isHcZBS = isHcZBS;
	}
	
	/**是否是跨服云宫之巅*/
	public static boolean isKfYunGong() {
        return activeUtil.isKfYunGong;
    }
	
    public static void setKfYunGong(boolean isKfYunGong) {
        activeUtil.isKfYunGong = isKfYunGong;
    }
    
    /**是否可以使用飞鞋*/
	public static boolean isCanUseFX() {
		return activeUtil.canUseFX;
	}
	public static void setCanUseFX(boolean canUseFX) {
		activeUtil.canUseFX = canUseFX;
	}
	/**是否可以变更公会、变更职位*/
	public static boolean isCanChangeGuild() {
		return activeUtil.canChangeGuild;
	}
	public static void setCanChangeGuild(boolean canChangeGuild) {
		activeUtil.canChangeGuild = canChangeGuild;
	}
	/**是否是领地战*/
	public static boolean isTerritory() {
		return activeUtil.isTerritory;
	}
	public static void setTerritory(boolean isTerritory) {
		activeUtil.isTerritory = isTerritory;
	}
	public static boolean isWenquan() {
		return activeUtil.isWenquan;
	}
	public static void setWenquan(boolean isWenquan) {
		activeUtil.isWenquan = isWenquan;
	}
	/**当前杀怪几倍经验奖励*/
	public static int getKillBei() {
		return activeUtil.killBei;
	}
	public static void setKillBei(int killBei) {
		activeUtil.killBei = killBei;
	}
	/**获取当前地图打坐几倍奖励*/
	public static Float getDazuoBei(Integer mapId) {
		Float bei = activeUtil.dazuoMap.get(mapId);
		if(bei == null){
			return 1f;
		}
		return bei;
	}
	public static void setDazuoMap(Integer map,Float bei) {
		activeUtil.dazuoMap.put(map, bei);
	}
	public static void clearDazuoMap(){
		activeUtil.dazuoMap.clear();
	}
	
	public static int getYabiaoBei() {
		return activeUtil.yabiaoBei;
	}
	public static void setYabiaoBei(int yabiaoBei) {
		activeUtil.yabiaoBei = yabiaoBei;
	}
	
}
