package com.junyou.gameconfig.map.configure.util;

import java.util.ArrayList;
import java.util.List;

public class DiTuConfigUtil {

	/*
	 * 	0	出生地图
		1	普通地图
		2	副本地图
		3	活动地图
		4	主城地图
		5	BOSS房间
		9	跨服战地图
		10	小黑屋
	*/

	public static final int BRITH_MAP_TYPE = 0;
	public static final int NOMAL_MAP_TYPE = 1;
	public static final int COPY_MAP_TYPE = 2;
	public static final int HUDONG_MAP_TYPE = 3;
	public static final int MAIN_MAP_TYPE = 4;
	
	/**
	 * 跨服地图
	 */
	public static final int KUAFU_MAP_TYPE = 9;
	/**
	 * 竞技场安全地图
	 */
	public static final int JINGJI_SAFE_MAP_TYPE = 10;
	/**
	 * 阵营战
	 */
	public static final int CAMP_MAP_TYPE = 11;
	/**
	 * 仙宫夺宝
	 */
	public static final int XIANGONG_MAP_TYPE = 12;
	/**
	 * 温泉
	 */
	public static final int WENQUAN_MAP_TYPE = 13;
	/**
	 * 热发布活动地图
	 */
	public static final int REFB_ACTIVE_MAP_TYPE = 14;
	/**
	 * 争霸赛之云宫之战  
	 */
	public static final int HC_ZBS_MAP_TYPE = 15;
	/**
	 * 勇闯七杀
	 */
	public static final int QISHA_MAP_TYPE = 16;
	/**
	 * 跨服个人竞技
	 */
	public static final int KUAFU_SINGLE_ARENA = 17;
	/**
	 * 混沌战场
	 */
	public static final int CHAOS_MAP_TYPE = 18;
	
	/**
	 * 剧情地图
	 */
	public static final int PLOT_MAP_TYPE = 40;
	
	/**野外场景地图类型*/
	public static List<Integer> AOI_STAGE_TYPES = new ArrayList<>();
	static{
		AOI_STAGE_TYPES.add(BRITH_MAP_TYPE);
		AOI_STAGE_TYPES.add(NOMAL_MAP_TYPE);
		AOI_STAGE_TYPES.add(HUDONG_MAP_TYPE);
		AOI_STAGE_TYPES.add(MAIN_MAP_TYPE);
		AOI_STAGE_TYPES.add(5);
		AOI_STAGE_TYPES.add(6);
		AOI_STAGE_TYPES.add(8);
		AOI_STAGE_TYPES.add(HC_ZBS_MAP_TYPE);
		AOI_STAGE_TYPES.add(PLOT_MAP_TYPE);
	}
	
	/**
	 * 当前地图类型是否可以创建野外场景
	 * @param mapType
	 * @return
	 */
	public static boolean isCanCreateAoiStage(Integer mapType){
		return AOI_STAGE_TYPES.contains(mapType);
	}
	
	/**
	 * 是否是出生地图
	 * @param type
	 * @return true:是
	 */
	public static boolean isBirthMap(int type){
		return type == BRITH_MAP_TYPE;
	}
	
	/**
	 * 是否是跨服地图
	 * @param type
	 * @return
	 */
	public static boolean isKuaFuMap(int type){
		return type == KUAFU_MAP_TYPE;
	}
	/**
	 * 是否是安全地图
	 * @param type
	 * @return
	 */
	public static boolean isSafeMap(int type){
		return type == JINGJI_SAFE_MAP_TYPE;
	}
	
	/**
	 * 是否是主城地图
	 * @param type
	 * @return true:是
	 */
	public static boolean isMainMap(int type){
		return type == MAIN_MAP_TYPE;
	}
	
	/**
	 * 是否是公共地图 
	 * @param type
	 * @return
	 */
	public static boolean isPublicMap(int type){
		return type == NOMAL_MAP_TYPE || type == MAIN_MAP_TYPE;
	}
}
