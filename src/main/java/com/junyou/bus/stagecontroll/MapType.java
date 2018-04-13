package com.junyou.bus.stagecontroll;

import java.util.HashSet;
import java.util.Set;


public class MapType {
	
	/**
	 * 普通地图
	 */
	public static final Integer NORMAL_MAP = 1;
	
	/**
	 * 单人副本地图
	 */
	public static final Integer FUBEN_MAP = 2;
	
	/**
	 * 跨服副本地图
	 */
	public static final Integer KUAFU_FUBEN_MAP = 3;
	
	/**
	 * 竞技场安全地图
	 */
	public static final Integer JINGJI_SAFE_MAP = 4;
	
	/**
	 * 跨服安全地图
	 */
	public static final Integer KUAFU_SAFE_MAP = 5;

	/**
	 * 单人守护副本地图
	 */
	public static final Integer SAVE_FUBEN_MAP = 6;
	
	/**
	 * 多人副本地图
	 */
	public static final Integer MORE_FUBEN_MAP = 7;
	
	/**
	 * 新手任务安全地图
	 */
	public static final Integer NEW_SAFE_MAP = 8;
	
	/**
	 *  爬塔副本地图
	 */
	public static final Integer PATA_FUBEN_MAP = 9;
	
	/**
	 * 阵营战
	 */
	public static final Integer CAMP_WAR_MAP = 10;
	
	/**
	 * 仙宫探宝地图
	 */
	public static final Integer XIANGONG_TANBAO_MAP = 11;
	/**
	 * 温泉
	 */
	public static final Integer WENQUAN_MAP_TYPE = 13;
	/**
	 * 热发布活动地图
	 */
	public static final Integer REFB_ACTIVE_MAP_TYPE = 14;
	/**
	 * 皇城争霸赛
	 */
	public static final Integer HC_ZBS_MAP_TYPE = 15;
	/**
	 * 勇闯七杀
	 */
	public static final Integer QISHA_MAP_TYPE = 16;
	/**
	 * 跨服个人竞技场副本地图
	 */
	public static final Integer KUAFU_AREBA_SINGLE = 17;
	/**
	 * 混沌战场
	 */
	public static final Integer HUNDUN_MAP_TYPE = 18;
	
	/**
	 * 八卦副本地图
	 */
	public static final Integer BAGUA_FUBEN_MAP = 19;
	/**
	 * 神魔战场地图
	 */
	public static final Integer SHENMO_FUBEN_MAP = 20;
	
	/**
	 * 塔防副本地图
	 */
	public static final Integer TAFANG_FUBEN_MAP = 21;
	/**
	 * 云浮剑冢副本
	 */
	public static final Integer JIANZHONG_FUBEN_MAP = 22;
	
	/**
	 * 埋骨副本地图
	 */
	public static final Integer MAIGU_FUBEN_MAP = 23;
	
	/**
	 * 封神之战竞技场安全地图
	 */
	public static final Integer KF_JINGJI_SAFE_MAP = 24;
	/**
	 *  门派炼狱boss
	 */
	public static final Integer GUILD_LIANYU_BOSS = 25;
	/**
	 * 跨服boss地图
	 */
	public static final Integer KUAFUBOSS_FUBEN_MAP = 26;
	/**
	 * 跨服大乱斗地图
	 */
	public static final Integer KUAFUDALUANDOU_FUBEN_MAP = 27;
	/**
	 * 跨服群仙宴地图
	 */
	public static final Integer KUAFUQUNXIANYAN_FUBEN_MAP = 28;
	
    /**
     * 五行副本
     */
    public static final Integer WUXING_FUBEN_MAP = 29;

    /**
     * 五行技能副本(镇妖塔)
     */
    public static final Integer WUXING_SKILL_FUBEN_MAP = 30;
   
    /**
     * 跨服巅峰之战地图
     */
    public static final Integer KUAFU_DIANFENG_MAP = 31;
	
    /**
     * 五行试炼副本
     */
    public static final Integer WUXING_SHILIAN_FUBEN_MAP = 32;
    
    /**
     * 心魔副本
     */
    public static final Integer XINMO_FUBEN_MAP = 33;
    
    /**
     * 心魔深渊副本
     */
    public static final Integer XINMO_SHENYUAN_FUBEN_MAP = 34;
    
    /**
     * 心魔斗场副本
     */
    public static final Integer XINMO_DOUCHANG_FUBEN_MAP = 35;
    
    /**
     * 跨服云宫之巅地图
     */
    public static final Integer KUAFU_YUNGONG_MAP = 36;
    /**
     * 魔宫猎焰地图
     */
    public static final Integer MOGONGLIEYAN_MAP = 37;
    /**
     * 云瑶晶脉地图
     */
    public static final Integer YUNYAOJINGMAI_MAP = 38;
    /**
     * 个人Boss
     */
    public static final Integer PERSONAL_BOSS = 39;
    
	private static Set<Integer> publicFubenTypes = new HashSet<>();
	static{
		publicFubenTypes.add(XIANGONG_TANBAO_MAP);
		publicFubenTypes.add(CAMP_WAR_MAP);
		publicFubenTypes.add(WENQUAN_MAP_TYPE);
		publicFubenTypes.add(REFB_ACTIVE_MAP_TYPE);
		publicFubenTypes.add(HC_ZBS_MAP_TYPE);
		publicFubenTypes.add(QISHA_MAP_TYPE);
		publicFubenTypes.add(HUNDUN_MAP_TYPE);
		publicFubenTypes.add(SHENMO_FUBEN_MAP);
		publicFubenTypes.add(MOGONGLIEYAN_MAP);
		publicFubenTypes.add(YUNYAOJINGMAI_MAP);
	}
	
	/**
	 * 是否离线可保存地图
	 */
	public static boolean usedForOfflineSave(Integer mapType){
		if( NORMAL_MAP.equals(mapType)){
			return true;
		}
		return false;
	}

	/**
	 * 是否是安全地图
	 * @param mapType
	 * @return
	 */
	public static boolean isSafeMap(Integer mapType){
		return JINGJI_SAFE_MAP.equals(mapType) || KUAFU_SAFE_MAP.equals(mapType) || NEW_SAFE_MAP.equals(mapType) || KF_JINGJI_SAFE_MAP.equals(mapType);
	}
	
	/**
	 * 是否是副本地图
	 * @param mapType
	 * @return
	 */
	public static boolean isFubenMap(Integer mapType){
		return     FUBEN_MAP.equals(mapType)        || SAVE_FUBEN_MAP.equals(mapType)         || GUILD_LIANYU_BOSS.equals(mapType) 
		        || PATA_FUBEN_MAP.equals(mapType)   || TAFANG_FUBEN_MAP.equals(mapType)       || JIANZHONG_FUBEN_MAP.equals(mapType)
		        || WUXING_FUBEN_MAP.equals(mapType) || WUXING_SKILL_FUBEN_MAP.equals(mapType) || WUXING_SHILIAN_FUBEN_MAP.equals(mapType)
		        || XINMO_FUBEN_MAP.equals(mapType)  || XINMO_SHENYUAN_FUBEN_MAP.equals(mapType)|| XINMO_DOUCHANG_FUBEN_MAP.equals(mapType)
		        || PERSONAL_BOSS.equals(mapType);
	}
	
	
	public static boolean isPublicFubenMap(Integer mapType){
		return publicFubenTypes.contains(mapType);
	}
	
	public static boolean isRefbActiveMap(Integer mapType){
		return REFB_ACTIVE_MAP_TYPE.equals(mapType);
	}
}
