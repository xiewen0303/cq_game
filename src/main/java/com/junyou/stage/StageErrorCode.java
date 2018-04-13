package com.junyou.stage;

public class StageErrorCode {
	
	
	//你不是GM，无法使用GM功能
	public static final Integer NO_GM_QUANXIAN = 31610;
	//GM传送无法到进入此地图
	public static final Integer GM_CANNOT_TELEPORT_THIS_MAP = 31611;
	//GM传送坐标错误
	public static final Integer GM_TELEPORT_POINT_ERROR = 31612;
	//GM传送场景错误
	public static final Integer GM_TELEPORT_STAGE_ERROR = 31613;
	//GM无法传送到玩家副本
	public static final Integer GM_CANNOT_TELEPORT_TO_PLAYER_COPY = 31614;
	//GM传送目标玩家错误
	public static final Integer GM_TELEPORT_TARGER_ROLE_ERROR = 31615;
	//GM传送目标玩家不在线
	public static final Integer GM_TELEPORT_TARGER_ROLE_NOT_ONLINE = 31616;
	//对方不存在
	public static final Integer TARGET_ROLE_IS_NULL = 31619;
	//对方是GM
	public static final Integer TARGET_ROLE_IS_GM = 31620;
	//理由为空
	public static final Integer REASONS_IS_NULL = 31621;
	
	
	//  进入地图需要需要消耗的游戏币不足够,不能传送
	public static final Integer TELEPORT_MONEY_ERROR = 200201;
	// 进入地图需要需要的等级不足够,不能传送
	public static final Integer TELEPORT_MIN_LEVEL_ERROR = 200202;
	// 地图找不到了,不能传送
	public static final Integer TELEPORT_ERROR = 200203;
	// 你的等级大于进入地图最大等级,不能传送
	public static final Integer TELEPORT_MAX_LEVEL_ERROR = 200204;
	// 进入地图需要拥有物品不足,不能传送
	public static final Integer TELEPORT_MUST_ITEM_ERROR = 200205;
	// 进入地图需要消耗的物品不足,不能传送
	public static final Integer TELEPORT_NEED_ITEM_ERROR = 200206;
	 // 进入地图需要官职太低,不能传送
	public static final Integer TELEPORT_GZ_ERROR = 200207;
	// 进入地图的时间还没到,不能进入
	public static final Integer TELEPORT_START_TIME_ERROR = 200208;
	// 进入地图的时间已过,不能进入
	public static final Integer TELEPORT_END_TIME_ERROR = 200209;
	// 进入地图需要的VIP等级高于你的等级,不能进入
	public static final Integer TELEPORT_VIP_ERROR = 200210;
	// 进入地图需要在公会内
	public static final Integer TELEPORT_GUILD_ERROR = 200211;
	// 您已在本地图了
	public static final Integer IN_CUR_MAP = 200212;
	// 死亡状态不能传送
	public static final Integer TELEPORT_IN_DEAD_ERROR = 200213;
	//你当前在副本中，操作不被允许
	public static final Integer IN_FUBEN_ERROR = 200214;
	//进入地图需要需要消耗的礼券不足够,不能传送
	public static final Integer TELEPORT_LIQUAN_ERROR = 200220;
	
	public static final Integer NO_JB = 20014;
	 
	/**
	 * 您现在不能传送
	 */
	public static final Integer IN_BOOTH = 200216;
	/**
	 * 摆摊状态不可传送
	 */
	public static final Integer BAITAN_NOT_CHUANGSONG = 200217;
	/**
	 * 您今日的飞鞋次数已用完,成为VIP后可以无限飞
	 */
	public static final Integer TELEPORT_FX_ERROR = 200215;

	public static final Integer TELEPORT_TIME_ERROR = 490007;
	
	
	 
	
	
	public static final Integer TELEPORT_LINE_ERROR = 490008;
	
	public static final Integer TELEPORT_SAME_LINE_ERROR = 490009;
	
	public static final Integer TELEPORT_IN_FIGHTING_ERROR = 490010;
	

	public static final Integer TELEPORT_NO_TARGET_ERROR = 490012;//传送的目标地图找不到
	public static final Integer TELEPORT_SAME_ERROR = 490013;//你已经目标地图内，不需要传送
	public static final Integer TELEPORT_IN_DAZUO_ERROR = 490014;//打坐不能传送
	
	/**
	 *  对不起不能传送,请完成当前主线任务后再传送
	 */
	public static final Integer TASK_MIAN_TESHU = 490021;
	
	
	/**
	 * 您还不能拾取这件物品
	 */
	public static final Integer NO_PICK_UP = 20028;

	/**
	 * 没有找到等级对应的挂机地图
	 */
	public static final Integer NOT_LEVEL = 13508;
	
	/**
	 * 抱歉，您今天的挂机时间不够，不能进入地图
	 */
	public static final Integer NOT_TIME = 13509;
	
	/**
	 * 590200 兜率宫击杀全民奖励
	 */
	public static final Integer ALL_ROLE_ACTIVITY_TITLE = 590200;
	
	/**
	 * 590201 恭喜英雄在{0}活动中，打败了{1}，奖励以下物品，请及时领取奖励  
	 */
	public static final Integer ALL_ROLE_ACTIVITY_CONTENT = 590201;
	
	public static final Object TELEPORT_ERROR_OBJ = new Object[]{0,TELEPORT_ERROR};
	public static final Object IN_CUR_MAP_OBJ = new Object[]{0,IN_CUR_MAP};
	public static final Object IN_FUBEN_ERROR_OBJ = new Object[]{0,IN_FUBEN_ERROR};
	/**
	 * 传送需要VIP等级不够
	 */
	public static final Object VIP_ERROR_OBJ = new Object[]{0,200218};
}
