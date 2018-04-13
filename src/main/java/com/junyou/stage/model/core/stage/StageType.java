package com.junyou.stage.model.core.stage;

import java.util.HashSet;
import java.util.Set;


/**
 * 场景类型
 * @author DaoZheng Yuan
 * 2015年2月5日 上午9:54:47
 */
public enum StageType {

	/**
	 * 正常开放场景
	 */
	NOMAL(false)
	
	/**
	 * 日常副本场景
	 */
	,FUBEN_RC(true)
	/**
	 * 日常副本--云浮剑冢
	 */
	,FUBEN_JIANZHONG(true)
	
	/**
	 * 竞技场安全场景(小黑屋)
	 */
	,SAFE_STAGE(true)
	
	/**
	 * 竞技场安全场景(封神之战小黑屋)
	 */
	,KF_JINGJI_STAGE(true)
	
	/**
	 * 跨服副本
	 */
	,KUAFU_FUBEN(true)
	
	/**
	 * 跨服安全场景(小黑屋)
	 */
	,KUAFU_SAFE_STAGE(true)
	
	/**
	 * 领地战场景
	 */
	,TERRITORY_WAR(false)
	/**
	 * 皇城争霸赛
	 */
	,HCZBS_WAR(true)
	/**
	 * 多人副本场景
	 */
	,MORE_FUBEN(true)
	
	/**
	 * 守护副本场景
	 */
	,SAVE_FB(true)
	
	/**
	 * 爬塔副本场景
	 */
	,FUBEN_PATA(true)
	/**
	 * 门派boss个人副本场景
	 */
	,GUILD_LIANYU_BOSS(true)
	
	/**
	 * 初始化场景小黑屋
	 */
	,START_GAME_SAFE_STAGE(false)
	/**
	 * 阵营战
	 */
	,CAMP(true)
	/**
	 * 仙宫探宝
	 */
	,XIANGONG(true)
		/**
	 * 温泉
	 */
	,WENQUAN(true)
	/**
	 * 热发布活动地图
	 */
	,REFB_ACTIVE(true)
	/**
	 * 勇闯七杀
	 */
	,QISHA(true)
			/**
	 * 跨服个人竞技
	 */
	,KUAFU_ARENA_SINGLE(true)
	/**
	 * 混沌战场
	 */
	,CHAOS(true)
		/**
	 * 八卦阵副本
	 */
	,BAGUA_FUBEN(true)
	/**
	 * 神魔战场场景
	 */
	,SHENMO_FUBEN(true)
	/**
	 * 塔防副本
	 */
	,TAFANG_FUBEN(true)
		/**
	 * 埋骨之地副本
	 */
	,MAIGU_FUBEN(true)
	/**
	 * 跨服boss
	 */
	,KUAFUBOSS(true)
	/**
	 * 跨服大乱斗
	 */
	,KUAFUDALUANDOU(true)
	/**
	 * 跨服群仙宴
	 */
	,KUAFUQUNXIANYAN(true)
	/**
     * 五行副本场景
     */
    , FUBEN_WUXING(true)
    /**
     * 五行技能副本场景
     */
    , FUBEN_WUXING_SKILL(true)
    /**
     * 跨服巅峰之战
     */
    ,KUAFUDIANFENG(true)
    /**
     * 五行试炼副本场景
     */
    , FUBEN_WUXING_SHILIAN(true)
    /**
     * 心魔副本场景
     */
    , FUBEN_XINMO(true)
    /**
     * 心魔深渊副本场景
     */
    , FUBEN_XINMO_SHENYUAN(true)
    /**
     * 心魔斗场副本场景
     */
    , FUBEN_XINMO_DOUCHANG(true)
    /**
     * 跨服云宫之巅
     */
    ,KUAFU_YUNGONG(true)
    /**
     * 魔宫猎焰公共场景
     */
    ,MGLY_STAGE(true)
    /**
     * 云瑶晶脉公共场景
     */
    ,YYJM_STAGE(true)
    /**
     * 个人Boss
     */
    ,PERSONAL_BOSS_FUBEN(true)
	;
	private final boolean isCopy;
	
	private static Set<StageType> publicFubenTypes = new HashSet<>();
	static{
		publicFubenTypes.add(XIANGONG);
		publicFubenTypes.add(CAMP);
		publicFubenTypes.add(WENQUAN);
		publicFubenTypes.add(REFB_ACTIVE);
		publicFubenTypes.add(HCZBS_WAR);
		publicFubenTypes.add(QISHA);
		publicFubenTypes.add(CHAOS);
		publicFubenTypes.add(SHENMO_FUBEN);
		publicFubenTypes.add(KUAFUBOSS);
		publicFubenTypes.add(KUAFUDALUANDOU);
		publicFubenTypes.add(KUAFUDIANFENG);
		publicFubenTypes.add(MGLY_STAGE);
		publicFubenTypes.add(YYJM_STAGE);
	}
	
	/**
	 *	是否是副本地图
	 * @author LiuYu
	 * @date 2015-3-9 下午4:13:53
	 */
	private StageType(boolean isCopy){
		this.isCopy = isCopy;
	}
	
	public boolean isCopy() {
		return isCopy;
	}
	
	/**
	 * 是否是安全场景(小黑屋)
	 * @param stageType
	 * @return
	 */
	public static boolean isSafeStage(StageType stageType){
		return SAFE_STAGE.equals(stageType) || KUAFU_SAFE_STAGE.equals(stageType) || START_GAME_SAFE_STAGE.equals(stageType);
	}
	/**
	 * 是否是个人副本
	 * @param stageTpye
	 * @return
	 */
	public static boolean isSingleFuben(StageType stageType){
		return       FUBEN_RC.equals(stageType)             ||             SAVE_FB.equals(stageType) 
		        ||   GUILD_LIANYU_BOSS.equals(stageType)    ||             FUBEN_PATA.equals(stageType) 
		        ||   TAFANG_FUBEN.equals(stageType)         ||             FUBEN_JIANZHONG.equals(stageType)
		        ||   FUBEN_WUXING.equals(stageType)         ||             FUBEN_WUXING_SKILL.equals(stageType)
		        ||   FUBEN_WUXING_SHILIAN.equals(stageType) ||             FUBEN_XINMO.equals(stageType)
		        ||   FUBEN_XINMO_SHENYUAN.equals(stageType) ||             FUBEN_XINMO_DOUCHANG.equals(stageType)
		        ||   PERSONAL_BOSS_FUBEN.equals(stageType);
	}
	
	/**
	 * 是否是公共副本
	 * @param stageType
	 * @return
	 */
	public static boolean isPublicFuben(StageType stageType){
		return publicFubenTypes.contains(stageType);
	}
	
	/**
	 * 是否是热发布副本地图
	 * @param stageType
	 * @return
	 */
	public static boolean isRefbActiveFuben(StageType stageType){
		return REFB_ACTIVE.equals(stageType);
	}
}
