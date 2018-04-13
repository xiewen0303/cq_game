package com.junyou.cmd;

/**
 * java内部指令(内部指令全部用int并且是负数)
 * 
 * @author DaoZheng Yuan 2014-11-14 上午8:09:08
 */
public class InnerCmdType {

	// -----------node-control-----------------
	/** 角色进入游戏前节点处理 **/
	public static final short NODE_INIT_IN = -1;

	/** 角色退出游戏前节点处理 **/

	public static final short NODE_INIT_OUT = -2;

	// -----------client-io-----------------
	/** 角色进入游戏前数据处理 **/
	public static final short BUS_INIT_IN = -3;
	/** 角色退出游戏前数据处理 **/
	public static final short BUS_INIT_OUT = -4;

	// -----------recharge-----------------
	/** 充值进入角色账户 **/
	public static final short REV_RECHARGE = -5;

	/** 原来的sync2db **/
	public static final short SYNC_CMD = -50;

	// -----------io-manage-----------------
	/** 最终所有输出给客户端的指令 **/
	public static final short IO_MSG_DISPATHCE_CMD = -100;

	// ------------进度-----------------
	/** 游戏进度 */
	public static final short GAME_JINDU = -149;

	// -----------AI相关-----------------
	/** ai处理 **/
	public static final short AI_HANDLE = -150;
	/** ai生产 **/
	public static final short AI_PRODUCE = -151;
	/** ai回收 **/
	public static final short AI_RETRIEVE = -152;
	/** ai消失 **/
	public static final short AI_DISAPPEAR = -153;
	/** ai死亡物品掉落 **/
	public static final short AI_GOODS_DROP = -154;
	/** ai死亡经验掉落 **/
	public static final short AI_EXP_DROP = -155;
	/** ai自动巡逻 **/
	public static final short AI_AUTO_XUNLOU = -156;
	/** 宠物ai处理 **/
	public static final short AI_PET_HANDLE = -157;
	/** 仙宫巡逻AI **/
	public static final short AI_XIANGONG_XUNLUO = -158;
	/** 塔防怪物AI **/
	public static final short AI_TAFANG_MONSTER = -159;
	/** 塔防NPCAI **/
	public static final short AI_TAFANG_NPC = -160;

	// -----------------stagecontroll-----------------
	/** 异常内部下线 **/
	public static final short EXIT_STAGE = -200;

	// -----------------stage场景内部指令-----------------
	/** 进入场景 **/
	public static final short S_Enter_Stage_cmd = -300;
	/** 离开场景 **/
	public static final short S_Leave_Stage_cmd = -301;
	/** 离开场景标识状态 **/
	public static final short leaveStage_cmd = -302;
	/** 内部切换场景 **/
	public static final short S_Change_Stage_cmd = -303;
	/** 增加经验 */
	public static final short INNER_ADD_EXP = -304;
	/** 杀怪推送经验玉经验 */
	public static final short TUISONG_EXPEXP = -305;
	/** 跨服拾取数值道具 */
	public static final short TAKE_GOODS_KUAFU = -306;
	/** PK物品掉落 */
	public static final short PK_DROP_GOODS = -307;
	/** 杀怪 */
	public static final short KILL_MONSTOR = -308;
	/** 血蓝回复 **/
	public static final short HPMP_REVERT = -309;
	/** 先离开完成后,再进入新场景 **/
	public static final short S_LEAVE_AFTER_ENTER_CMD = -310;
	/** 物品掉落 **/
	public static final short DROP_GOODS = -311;
	/** 物品消失 **/
	public static final short GOODS_DISAPPEAR = -312;
	/** 内部申请切换场景 **/
	public static final short S_APPLY_CHANGE_STAGE = -313;
	/** 内部申请离开场景 **/
	public static final short S_APPLY_LEAVE_STAGE = -314;
	/** 角色进入场景后处理业务 **/
	public static final short ENTER_STAGE_LATER = -315;
	/** 野外BOSS被击杀 推送前端关闭面板 **/
	public static final short CLOSE_BOSS_HURT_RANK = -316;
	/** 战斗检查状态 */
	public static final short INNER_FIGHT_STATE_CHECK = -317;
	/** 时间增加怒气 */
	public static final short TIME_ADD_NUQI = -318;
	/** 大招定时 */
	public static final short NUQI_SKILL_SCHEDULE = -319;
	/** buff触发 **/
	public static final short INNER_BUFF_TRIGGER = -320;
	/** buff结束 **/
	public static final short INNER_BUFF_END = -321;
	/** 清空怒气 **/
	public static final short INNER_CLEAR_NUQI = -322;
	/** 内部回城复活 */
	public static final short INNER_TOWN_REVIVE = -323;
	/** 内部释放技能 **/
	public static final short INNER_SKILL_SINGLE_FIRE = -324;
	/** 领地战增加经验真气 */
	public static final short INNER_TERRITORY_ADD_EXP_ZHENQI = -325;
	/** BUFF触发技能 **/
	public static final short INNER_BUFF_SKILL_FIRE = -326;

	/** 热发布活动地图创建 */
	public static final short INNER_REFB_ACTIVE_MAP_CREATE = -327;
	/** 热发布活动地图结束 */
	public static final short INNER_REFB_ACTIVE_MAP_OVER = -328;
	
	
	//-----------------------场景属性(-390~-399)---------------------------
	/**角色属性变动*/
	public static final short INNER_CHANGE_ROLE_ATT = -399;
	
	//-----------------跨服指令--------------------------
	/**跨服活动开始*/
	
	/** 争霸赛增加经验真气 */
	public static final short INNER_HCZBS_ADD_EXP_ZHENQI = -238;

	// -----------------跨服指令--------------------------
	/** 跨服活动开始 */

	public static final short KUAFU_ACTIVE_START = -400;
	/** 跨服活动结束 */
	public static final short KUAFU_ACTIVE_END = -401;
	/** 进跨服 **/
	public static final short INNER_KUAFU_ENTER = -402;
	/** 退出跨服 **/
	public static final short INNER_KUAFU_LEAVE = -403;
	/** 跨服离线指令(第一步，离开地图) **/
	public static final short KUAFU_OFFLINE1 = -404;
	public static final short INNER_SC_KUAFU_COPY = -405;
	/** 通用离开副本回到正常场景 **/
	public static final short INNER_T_LEAVEL_COPY = -406;
	/** 第一次连接时接收信息 **/
	public static final short KUAFU_RECEIVE_INFO_FROM_LOCAL = -407;
	/** 主动离开跨服 **/
	public static final short KUAFU_QUIT = -408;
	/** 清理玩家跨服数据 **/
	public static final short CLEAR_KUAFU_DATA = -409;
	/** 接收跨服邮件 **/
	public static final short INNER_KF_EMAIL = -410;
	/** 接收跨服发送给多客户端的信息 **/
	public static final short INNER_KF_TO_MANY_CLIENT = -411;
	/** 跨服心跳指令 */
	public static final short SOU2KF_CMD = -412;
	/** 服务器和跨服服务器通讯使用的默认指令 */
	public static final short DEFAULT_CMD = -413;
	/** 复活药复活通知跨服执行复活操作 */
	public static final short KF_PROP_REVIVE = -414;
	/** 接收跨服发送给单客户端的信息 **/
	public static final short INNER_KF_TO_ONE_CLIENT = -415;
	/** 跨服接收到血量回复 **/
	public static final short INNER_KF_HUIFU_HP = -416;
	/** 角色心跳指令 **/
	public static final short INNER_ROLE_XT = -420;

	// -----------------Role--------------------------
	/** 沉迷指令 **/
	public static final short CHENMIN_CMD = -500;
	/** 半沉迷指令 **/
	public static final short HALF_CHENMIN_CMD = -501;
	/** 角色等级变化（服务器内部处理一些属性等） **/
	public static final short LEVEL_UPGRADE = -502;

	/** 角色PK值变化（增加和减少） **/
	public static final short ADD_PK_CHANGE = -503;
	/** 角色PK灰名时间 **/
	public static final short PK_HUI = -504;
	/** 角色增加PK值 **/
	public static final short ROLE_PK = -505;
	/** 角色灰名消失定时 **/
	public static final short HUI_XS = -506;
	/** 角色红名减少定时 **/
	public static final short HONGMING_DERC = -507;
	/** 玩家队伍变化 **/
	public static final short TEAM_CHANGE = -508;
	/** VIP限时到期 */
	public static final short VIP_TIME_OUT = -509;
	/** 角色VIP等级变化（服务器内部处理一些属性等） **/
	public static final short VIP_LEVEL_UPGRADE = -510;
	/** 战力变更保存 */
	public static final short ZPLUS_CHANGE_SAVE = -511;
	/** 角色灰名处理 **/
	public static final short ROLE_HUIMING = -512;
	// ----------------装备(-621~-649)--------------------------
	/** 换装 */
	public static final short INNER_CHANGE_EQUIP = -621;
	/** 换装耐久度消耗 */
	public static final short EQUIP_NJD_CONSUME = -622;
	/** 修改身上是否有可消耗的耐久度装备 */
	public static final short MODIFY_DURABILITY_FLAG = -623;
	/** 御剑换装 */
	public static final short INNER_CHANGE_ZUOQI_EQUIP = -624;
	/** 翅膀换装 */
	public static final short INNER_CHANGE_CHIBANG_EQUIP = -625;
	/** 糖宝换装 */
	public static final short INNER_CHANGE_TANGBAO_EQUIP = -626;
	/** 天工换装 */
	public static final short INNER_CHANGE_TIANGONG_EQUIP = -627;
	/** 天裳换装 */
	public static final short INNER_CHANGE_TIANSHANG_EQUIP = -628;
	/** 器灵换装 */
	public static final short INNER_CHANGE_QILING_EQUIP = -629;
	/** 天羽换装 */
	public static final short INNER_CHANGE_TIANYU_EQUIP = -630;
	/** 宠物换装 */
	public static final short INNER_CHANGE_CHONGWU_EQUIP = -631;
	/** 神器换装 */
	public static final short INNER_CHANGE_SHENQI_EQUIP = -632;
	/** 新圣剑换装 */
	public static final short INNER_CHANGE_WUQI_EQUIP = -633;
	

	// ---------------背包(-670~-689)--------------------
	/** 拾取物品进背包 */
	public static final short TAKE_GOODS = -670;
	/** 格位开启属性变化 */
	public static final short INNER_SLOT_ATTRS = -671;

	// ---------------公会(-710~-759)--------------------
	/** 公会（添加公会内可收到聊天信息成员） **/
	public static final short GUILD_MEMBER_ADD_CHAT = -710;
	/** 公会（清除公会内可收到聊天信息成员） **/
	public static final short GUILD_MEMBER_REMOVE_CHAT = -711;
	/** 公会（解散公会） **/
	public static final short GUILD_MEMBER_CLEAR = -712;
	/** 通知场景公会变化 */
	public static final short GUILD_CHANGE = -713;
	/** 通知场景公会等级变化 */
	public static final short GUILD_LEVEL_CHANGE = -714;

	// ---------------镖车(-740-749)--------------------
	/** 创建镖车 **/
	public final static short S_CREATE_BIAOCHE = -740;
	/** 镖车心跳 **/
	public final static short S_BIAOCHE_HEART = -741;
	/** 镖车死亡（劫镖通知BUS处理） **/
	public final static short B_BIAOCHE_DEAD = -742;
	/** 镖车清除 **/
	public final static short S_BIAOCHE_CLEAN = -743;
	/** 镖车过期，清除状态 **/
	public final static short B_BIAOCHE_STATUS = -744;

	// ---------------好友(-760~-770)--------------------
	/** 好友 */
	public static final short ADD_ENEMY = -760;

	// ------------技能(-771~-789)------------------------
	/** 学习技能 */
	public static final short SKILL_ADD_SKILL = -771;
	/** 技能被使用，通知增加熟练度 */
	public static final short SKILL_USE_SKILL = -772;
	/** 学习被动技能 */
	public static final short SKILL_ADD_BEIDONG_SKILL = -773;
	/** 学习门派技能 */
	public static final short SKILL_ADD_GUILD_SKILL = -774;
	/** 主动技能战力变化 */
	public static final short SKILL_ZPLUS_CHANGE = -775;
	/** 切换技能 */
	public static final short ROLE_CHANGE_SKILL = -776;
	/** 通知场景取消技能熟练度通知*/
	public static final short ROLE_CANCEL_NOTICESKILLS = -777;
	/** 通知场景增加技能熟练度通知 */
	public static final short ROLE_ADD_NOTICESKILLS = -778;
	/** 删除技能 */
	public static final short SKILL_DELETE_SKILL = -779;
	/** 学习糖宝被动技能 */
	public static final short SKILL_ADD_TANGBAO_SKILL = -780;
	// ---------------副本(-790~-809)--------------------
	/** 通知BUS业务退出副本 **/
	public static final short B_EXIT_FUBEN = -790;
	/** 副本完成（标记为可领取奖励状态） **/
	public static final short S_FUBEN_FINISH = -791;
	/** 通知场景退出副本 **/
	public static final short S_EXIT_FUBEN = -792;
	/** 通知场景副本已完成 **/
	public static final short S_FUBEN_OVER = -793;
	/** 通知业务副本扫荡结束 **/
	public static final short B_FINISH_SAODANG_FUBEN = -794;
	/** 通知业务层场景已退出副本 **/
	public static final short HAS_EXIT_FUBEN = -795;
	/** 守护副本完成 */
	public static final short SHOUHU_FUBEN_FINISH = -796;
	/** 同步守护副本进度 */
	public static final short SYN_SHOUHU_FUBEN_GUAN = -797;
	/** 通知多人副本场景退出 **/
	public static final short S_EXIT_MORE_FUBEN = -798;
	/** 通知多人副本场景完成 **/
	public static final short S_MORE_OVER = -799;
	/** 通知BUS业务退出多人副本 **/
	public static final short B_EXIT_MORE_FUBEN = -800;
	/** 多人副本完成（标记为副本完成状态） **/
	public static final short S_MORE_FUBEN_FINISH = -801;
	/** 多人副本进入状态 */
	public static final short B_ENTER_MORE_FUBEN = -802;
	/** 请求创建多人副本队伍 */
	public static final short APPLY_CREATE_MORE_FUBEN_TEAM = -803;
	/** 请求幻境历练副本队伍信息 */
	public static final short MORE_FUBEN_TEAM_INFO = -804;
	/** 多人副本离线业务 */
	public static final short MORE_FUBEN_OFFLINE_HANDLE = -805;
	/** 多人副本申请加入队伍 */
	public static final short MORE_FUBEN_APPLY_ENTER_TEAM = -806;
	/** 多人副本退出副本 */
	public static final short MORE_FUBEN_EXIT_FUBEN = -807;
	/** 多人副本踢出副本 */
	public static final short MORE_FUBEN_KICK_FUBEN = -808;
	/** 多人副本队长变更战力需求 */
	public static final short MORE_FUBEN_CHANGE_ZPLUS = -809;
 
 
	// ---------------打坐跳闪(-810~-815)--------------------
	/** 打坐奖励处理 */
	public static final short DAZUO_AWARD = -810;
	/** 切换到双休状态 */
	public static final short DAZUO_2_SX = -811;
	/** 添加跳闪值 */
	public static final short ADD_TIAOSHAN = -812;
	/** 跳闪值状态 */
	public static final short STAGE_TIAOSHAN = -813;
	/** 打坐增加经验真气值 */
	public static final short INNER_DAZUO_AWARD = -814;
	/** 内部取消打坐 */
	public static final short INNER_DAZUO_CANCEL = -815;

	// ------------------竞技(-816~-820)------------
	/** 挑战开始通知场景进入小黑屋 */
	public static final short ENTER_SAFE_MAP = -816;
	/** 已进入安全场景，通知业务可以开始战斗 */
	public static final short ENTER_SAFE_SUCCESS_START_FIGHT = -817;
	/** 挑战开始通知场景进入小黑屋(封神之战) */
	public static final short ENTER_SAFE_MAP_KF = -818;
	/** 已进入安全场景，通知业务可以开始战斗(封神之战) */
	public static final short ENTER_SAFE_SUCCESS_START_FIGHT_KF = -819;
	// ------------------坐骑(-821~-830)------------
	/** 坐骑属性变化 */
	public static final short INNER_ZUOQI_CHANGE = -821;
	/** 坐骑状态信息变化 */
	public static final short ZUOQI_STATE = -822;
	/** 坐骑外显变化信息 */
	public static final short INNER_SHOW_UPDATE = -823;
	/** 通知bus业务持久化坐骑状态 */
	public static final short ZUOQI_BUS_STATE = -824;
	/** 初始化场景里面的坐骑信息 */
	public static final short INNER_ZUOQI_ADD = -825;
	/** 等级变化刷新坐骑属性 */
	public static final short INNER_ZUOQI_REFRESH = -826;
	/** 坐骑战斗力变化 */
	public static final short INNER_ZUOQI_ZPLUS_CHANGE = -827;

	// -----------------------道具(-831~-850)--------------------
	/** 血包效果生效 */
	public static final short PROP_EFFECT_HP = -831;
	/** 多倍经验到期 */
	public static final short PROP_EFFECT_EXP_OVER = -832;
	/** 通知跨服道具生效 */
	public static final short PROP_EFFECT_NOTICE_KUAFU = -833;

	// ---------------------领地战(-851~-870)-------------------
	/** 增加经验真气 */
	public static final short TERRITORY_ADD_EXP_ZHENQI = -851;
	/** 领地战结果产生 */
	public static final short TERRITORY_HAS_WINNER = -852;
	/** 领地战开始 */
	public static final short TERRITORY_START = -853;
	/** 领地战活动结束 */
	public static final short TERRITORY_ACTIVE_END = -854;
	/** 领地战扛旗者死亡 */
	public static final short TERRITORY_FLAG_OWNER_DEAD = -855;
	/** 领地战怪物死亡 */
	public static final short TERRITORY_MONSTER_DEAD = -856;
	/** 领地战杀人获得帮贡 */
	public static final short TERRITORY_ADD_BANGGONG = -857;
	/** 领地战同步旗子位子 */
	public static final short TERRITORY_SYN_FLAG = -858;
	/** 变更掌门 :让位 **/
	public final static short TERRITORY_LEADER_OFF = -859;
	/** 变更掌门 :上位 **/
	public final static short TERRITORY_LEADER_ON = -860;

	// -------------------定时活动(-871~-900)----------------------
	/** 打坐活动开始 */
	public static final short DINGSHI_DAZUO_START = -871;
	/** 打坐活动结束 */
	public static final short DINGSHI_DAZUO_STOP = -872;
	/** 打怪活动开始 */
	public static final short DINGSHI_KILL_MONSTER_START = -873;
	/** 打怪活动结束 */
	public static final short DINGSHI_KILL_MONSTER_STOP = -874;
	/** 定时刷怪 */
	public static final short DINGSHI_REFRESH_BOSS = -875;
	/** 回收定时刷出的野怪 */
	public static final short RETRIEVE_BOSS = -876;
	/** 修改对怪物的伤害值 */
	public static final short MODIFY_HURT_VALUE = -877;
	/** 排行刷新 */
	public static final short INNER_DSBOSS_RANK = -878;
	/** 场景进入定时刷怪 */
	public static final short INNER_DS_MONSTER = -879;
	/** 场景进入定时刷怪(boss) */
	public static final short INNER_DS_BOSS_MONSTER = -880;
	/** 答题活动开始 */
	public static final short DINGSHI_DATI_STATRT = -881;
	/** 答题活动结束 */
	public static final short DINGSHI_DATI_STOP = -882;
	/** 押镖活动开始 */
	public static final short DINGSHI_YABIAO_MONSTER_START = -883;
	/** 押镖活动结束 */
	public static final short DINGSHI_YABIAO_MONSTER_STOP = -884;
	/** 开启野外boss排行榜 */
	public static final short OPEN_BOSS_RANK = -885;
	/** 定时宝箱采集活动开始 */
	public static final short DINGSHI_COLLECT_BOX_START = -886;
	/** 定时宝箱采集活动结束 */
	public static final short DINGSHI_COLLECT_BOX_STOP = -887;
	/** 场景宝箱采集活动开始 */
	public static final short COLLECT_BOX_START = -888;
	/** 场景宝箱采集活动结束 */
	public static final short COLLECT_BOX_STOP = -889;
	/** 场景定时刷入箱子 */
	public static final short DINGSHI_FLUSH_BOX = -890;
	/** 箱子回收 */
	public static final short DINGSHI_CLEAR_ALL_BOX = -891;
	/** 阵营战开始 */
	public static final short DINGSHI_CAMP_WAR_START = -892;
	/** 探宝箱子刷新 */
	public static final short DINGSHI_TANBAO_REFRESH_BOX = -893;
	/** 仙宫探宝活动开始 */
	public static final short DINGSHI_XIANGONG_START = -894;
	/** 仙宫探宝活动结束 */
	public static final short DINGSHI_XIANGONG_STOP = -895;
	/** 仙宫探宝活动初始化 */
	public static final short DINGSHI_XIANGONG_INIT = -896;
	/** 增加探宝积分 */
	public static final short ADD_TANBAO_SCORE = -897;
	/** 定时增加探宝经验 */
	public static final short DINGSHI_TANBAO_EXP_ADD = -898;
	/** 领地战活动开启 */
	public static final short DINGSHI_TERRITORY_START = -899;
	/** 温泉活动开启 */
	public static final short DINGSHI_WENQUAN_START = -900;

	// ------------------ 翅膀(-901~-920)----------------------
	/** 翅膀属性变化 */
	public static final short INNER_CHIBANG_CHANGE = -901;
	/** 初始化场景里面的翅膀信息 */
	public static final short INNER_CHIBANG_ADD = -902;
	/** 场景内部翅膀变化 */
	public static final short INNER_CHIBANG_STATE = -903;
	/** 翅膀形象变化 */
	public static final short CHIBANG_SHOW_UPDATE = -904;
	/** 翅膀战斗力变化 */
	public static final short INNER_ZPLUS_CHIBANG = -905;

	// -----------------糖宝(-921~-930)--------------------------
	/** 主人属性变更 */
	public static final short PET_OWNER_ATTRIBUTE_CHANGE = -921;
	/** 糖宝基本属性变更 */
	public static final short PET_BASE_ATTRIBUTE_CHANGE = -922;
	/** 新增糖宝 */
	public static final short PET_ADD_PET = -923;
	/** 糖宝进度 */
	public static final short PET_ADD_PROGRESS = -924;
	/** 糖宝属性刷新 */
	public static final short PET_ATTRIBUTE_REFRESH = -925;

	// ----------------副本后续(-931~-999)------------------
	/** 队长更改幻境历练副本队伍是否满员自动挑战 */
    public static final short MORE_FUBEN_SET_AUTO = -931;
    /** （副本预进入请求）通知队伍里所有的人在指定时间戳后进入副本 */
    public static final short MORE_FUBEN_PRE_START = -932;
    /** 成员准备 */
    public static final short MORE_FUBEN_PREPARE = -933;
    /** 请求成员数据 */
    public static final short MORE_FUBEN_ASK_ROLE_DATA = -934;
    /** 发送成员数据 */
    public static final short MORE_FUBEN_SEND_ROLE_DATA = -935;
    /** 离开多人队伍 */
    public static final short MORE_FUBEN_LEAVE_TEAM = -936;
    /** 进入跨服 */
    public static final short MORE_FUBEN_ENTER_KUAFU = -937;
    /** 通知跨服进入场景 */
    public static final short MORE_FUBEN_CAN_ENTER_STAGE = -938;
    /** 离开多人副本 */
    public static final short MORE_FUBEN_LEAVE_FUBEN = -939;
    /** 离开多人副本(跨服) */
    public static final short MORE_FUBEN_LEAVE_FUBEN_KUAFU = -940;
    /** 进入多人副本失败,通知原服 */
    public static final short MORE_FUBEN_ENTER_FAIL = -941;
    /** 退出爬塔副本 */
    public static final short HAS_EXIT_PATA_FUBEN = -942;
    /** 爬塔副本完成 */
    public static final short PATA_FINISH = -943;
    /** 爬塔副本失败 */
    public static final short PATA_FAIL = -944;
    /** 跨服通知原服多人本队伍创建（进入）成功 */
    public static final short MORE_FUBEN_TEAM_SUCCESS = -945;
    /** 多人副本异步离线业务 */
    public static final short MORE_FUBEN_OFFLINE_HANDLE_SYN = -946;
    /** 处理过期队伍 */
    public static final short HANDLE_MORE_FUBEN_TIME_OUT_TEAM = -947;
    /** 玩家已在跨服已经切场景后再次向源服告知 标记状态（容错） */
    public static final short MORE_FUBEN_AFTER_ENTER_STAGE = -948;
    /** 与源服状态同步 */
    public static final short MORE_FUBEN_START_SYNC = -949;
    /** 与源服状态同步 */
    public static final short MORE_FUBEN_SYNC = -950;
    /** 与源服状态同步 */
    public static final short MORE_FUBEN_REMOVE_FROM_KUAFU = -951;
    
    //-------------------------五行副本(-952~-954)--------------------//
    /** 服务器内部处理玩家成功离开五行副本 **/
    public static final short B_EXIT_WUXING_FUBEN = -952;
    /** 五行副本业务完成（标记为可领取奖励状态） **/
    public static final short S_WUXING_FUBEN_FINISH = -953;

    // -------------------------五行技能副本(-954~-955)--------------------//
    /** 服务器内部处理玩家成功离开五行技能副本 **/
    public static final short B_EXIT_WUXING_SKILL_FUBEN = -954;
    /** 五行技能副本业务完成（标记为可领取奖励状态） **/
    public static final short S_WUXING_SKILL_FUBEN_FINISH = -955;
    
    // -------------------------五行试炼副本(-956~-955)--------------------//
    /** 五行试炼副本通关完成(在时间结束前,玩家没有退出副本,则表示通关完成) **/
    public static final short S_WUXING_SHILIAN_FUBEN_FINISH = -956;
    /** 服务器内部处理玩家成功离开五行试炼副本 **/
    public static final short B_EXIT_WUXING_SHILIAN_FUBEN = -957;

	// ----------------神器(-1000~-1010)------------------
	/** 玩家神器穿脱 */
	public static final short SHENQI_REFRESH = -1000;
	/** 神器激活 */
	public static final short SHENQI_ACTIVATE = -1001;
	/** 神器攻击 */
	public static final short SHENQI_ATTACK = -1002;
	/** 神器进阶属性变化 */
	public static final short SHENQI_JINJIE_ATTR_CHANGE = -1003;
	/** 神器格位开启信息推送 */
	public static final short SHENQI_EQUIP_GEWEI_CHANGE = -1004;
	// ----------------阵营战(-1011~-1020)------------------
	/** 阵营战开始 **/
	public final static short S_CAMP_WAR_START = -1011;
	/** 阵营战强制退出 **/
	public final static short S_LEVEL_CAMP = -1012;
	/** 阵营战定时加经验 **/
	public final static short S_CAMP_ADD_EXP = -1013;
	/** 阵营战中，添加积分值 **/
	public final static short S_CAMP_DEAD = -1014;
	/** 阵营战活动结束后，统计排名结算奖励 **/
	public final static short S_CAMP_RANK = -1015;
	/** 阵营战随机复活 **/
	public final static short S_CAMP_FUHUO = -1016;
	/** 领地战复活 **/
	public final static short S_TERRITORY_FUHUO = -1017;
	/** 皇城争霸赛复活 **/
	public final static short S_HCZBS_FUHUO = -1018;

	/** 特殊指令，永远放在内部指令最后面 **/
	public final static short INNER_TS_SERVER = -32767;
	// ------------------仙剑(-1021~-1030)----------------------
	/** 仙剑属性变化 */
	public static final short INNER_XIANJIAN_CHANGE = -1021;
	/** 初始化场景里面的仙剑信息 */
	public static final short INNER_XIANJIAN_ADD = -1022;
	/** 场景内部仙剑变化 */
	public static final short INNER_XIANJIAN_STATE = -1023;
	/** 仙剑形象变化 */
	public static final short XIANJIAN_SHOW_UPDATE = -1024;
	/** 仙剑战斗力变化 */
	public static final short INNER_ZPLUS_XIANJIAN = -1025;
	// ------------------战甲(-1021~-1030)----------------------
	/** 仙剑属性变化 */
	public static final short INNER_ZHANJIA_CHANGE = -1031;
	/** 初始化场景里面的仙剑信息 */
	public static final short INNER_ZHANJIA_ADD = -1032;
	/** 场景内部仙剑变化 */
	public static final short INNER_ZHANJIA_STATE = -1033;
	/** 仙剑形象变化 */
	public static final short ZHANJIA_SHOW_UPDATE = -1034;
	/** 仙剑战斗力变化 */
	public static final short INNER_ZPLUS_ZHANJIA = -1035;

	// -------------------灵火----------------------
	/** 灵火属性变化 */
	public static final short INNER_LINGHUO_CHANGE = -1041;
	// -------------------灵境(-1050~-1059)--------------------
	/** 灵境属性变化 */
	public static final short INNER_LINGJING_CHANGE = -1050;

	// -------------------温泉----------------------
	/** 温泉开始 */
	public static final short WENQUAN_START = -1061;
	/** 温泉结束 */
	public static final short WENQUAN_END = -1062;
	/** 温泉定时加经验 **/
	public final static short WENQUAN_ADD_EXP = -1063;
	/** 温泉进入高倍区 **/
	public final static short WENQUAN_GO_HIGH_AREA = -1064;

	// -------------------活跃度----------------------
	/** 参与1次幻境历练 */
	public static final short INNER_HUOYUEDU_HUANJING = -1060;
	/** 参与1次八卦 */
	public static final short INNER_HUOYUEDU_BAGUA = -1065;
	/** 参与1次埋骨之地 */
	public static final short INNER_HUOYUEDU_MAIGU = -1066;
	/**抛指令方式统计活跃度(非跨服) */
	public static final short INNER_HUOYUEDU_COUNT = -1067;
	/**其他模块需要通过指令的方式统计活跃度 */
	public static final short INNER_HUOYUEDU = -1068;

	// -------------------称号----------------------
	/** 称号过期 */
	public static final short INNER_CHENGHAO_EXPIRE = -1070;
	/** 玩家称号穿脱 */
	public static final short INNER_CHENGHAO_REFRESH = -1071;
	/** 称号激活 */
	public static final short INNER_CHENGHAO_ACTIVATE = -1072;
	/** 称号消失 */
	public static final short INNER_CHENGHAO_REMOVE = -1073;

	// *********************成就***********************
	/** 成就属性变化 */
	public static final short INNET_CHENGJIU_CHANGE = -1080;

	public static final short CHENGJIU_CHARGE = -1081;
	//*********************台湾*********************
	/**绑定平台ID*/
	public static final short TAIWAN_PF_BANGDING = -1082;
	// ********************腾讯**********************
	/** 联盟罗盘 */
	public static final short TENCENT_LUOPANLM_LOGIN_CHUAN = -1089;
	public static final short INNER_TENCENT_LM_LUOPAN = -1090;
	public static final short TENCENT_LUOPAN_TONGYONG = -1091;
	public static final short TENCENT_GAOQIAN_LOG = -1092;
	public static final short TENCENT_REGISTER_LUOPAN = -1093;
	public static final short TENCENT_RECHAGE_LUOPAN = -1094;
	public static final short TENCENT_LUOPANLM_ZHUCE = -1095;
	public static final short TENCENT_VIA_USER = -1096;
	public static final short TENCENT_LUOPAN_OSS_ZHUCE = -1097;
	public static final short TENCENT_LUOPAN_OSS_LOGIN = -1098;
	public static final short TENCENT_LUOPAN_OSS_XIAOFEI = -1099;
	public static final short TENCENT_LUOPANLM_LOGIN = -1100;
	public static final short TENCENT_LUOPANLM_OSS_CHARGE = -1088;
	
	// ------------------ 妖神(-1101~-1110)----------------------
	/** 妖神属性变化 */
	public static final short INNER_YAOSHEN_ATTR_CHANGE = -1101;
	/** 妖神外显变化 */
	public static final short INNER_YAOSHEN_SHOW_CHANGE = -1102;
	/** 妖神魔纹属性变化 */
	public static final short INNER_YAOSHEN_MOWEN_CHANGE = -1103;
	/** 妖神魂魄属性变化 */
	public static final short INNER_YAOSHEN_HUNPO_CHANGE = -1104;
	/**魄神属性变化 */
	public static final short INNER_YAOSHEN_HUNPO_POSHEN_CHANGE = -1105;

	// ------------------ 宝石(-1111~-1115)----------------------
	/** 宝石属性变化 */
	public static final short INNER_JEWEL_ATTR_CHANGE = -1111;

	// --------------------时装(-1116~-1120)------------------
	/** 场景切换时装激活属性 */
	public static final short INNER_CHANGE_SHIZHUANG_ACTIVE_ATT = -1116;
	/** 场景切换时装升级属性 */
	public static final short INNER_CHANGE_SHIZHUANG_SHENGJI_ATT = -1117;
	/** 通知场景切换时装 */
	public static final short INNER_CHANGE_SHIZHUANG = -1118;
	/** 限时时装到期 */
	public static final short SHIZHUANG_EXPIRE = -1119;
	/** 场景切换时装进阶属性 */
	public static final short INNER_CHANGE_SHIZHUANG_JINJIE_ATT = -1120;
	// ------------------ 宠物(-1121~-1130)----------------------
	/** 宠物属性变化 */
	public static final short CHONGWU_ATTR_CHANGE = -1121;
	/** 宠物增加经验 */
	public static final short CHONGWU_ADD_EXP = -1122;
    /** 宠物增加经验 */
    public static final short CHONGWU_SHOW_UPDATE = -1123;
    /** 宠物技能属性变化 */
    public static final short CHONGWU_Skill_ATTR_CHANGE = -1124;
	// -------------------争霸赛(-1112~-1115,-1130~-1140)----------------------
	/** 争霸赛开始 */
	public static final short ZHENGBASAI_START = -1112;
	/** 争霸赛活动开启 */
	public static final short DINGSHI_ZHENGBASAI_START = -1113;
	/** 变更掌门 :让位 **/
	public final static short HCZBS_LEADER_OFF = -1114;
	/** 变更掌门 :上位 **/
	public final static short HCZBS_LEADER_ON = -1115;
	/** 爭霸賽結束 **/
	public final static short HCZBS_ACTIVE_END = -1130;
	/** 增加经验真气 */
	public static final short HCZBS_ADD_EXP_ZHENQI = -1131;
	/** 领地战结果产生 */
	public static final short HCZBS_HAS_WINNER = -1132;
	/** 争霸赛怪物死亡 */
	public static final short HCZBS_MONSTER_DEAD = -1133;
	/** 争霸赛扛旗者死亡 */
	public static final short HCZBS_FLAG_OWNER_DEAD = -1134;
	/** 争霸赛同步旗子位子 */
	public static final short HCZBS_SYN_FLAG = -1135;
	/** 争霸赛获得帮贡 */
	public static final short HCZBS_ADD_BANGGONG = -1136;
	/** 通知门主，仙侣切换外显并且处理门主buff切换 */
	public static final short HCZBS_WINNER_LEADER_CHANGE_CLOTHES = -1137;

	//------------------ 跨服充值活动-1140~-1145)----------------------
	/**跨服充值活动*/
	public static final short KUAFU_CHARGE_RANK = -1140;
	/**跨服充值活动结束定时器*/
	public static final short KUAFU_CHARGE_RANK_END_SCHEDULE = -1141;
	//------------------ 欢乐卡牌活动-1146~-1150)----------------------
	/**欢乐卡牌*/
	public static final short HAPPY_CARD_CHARGE = -1146;
	//---------------------结婚(-1151~-1160)-------------------
	/**定时增加缘分、亲密度*/
	public static final short MARRY_SCHEDULE_ADD = -1151;
	/**定时离婚*/
	public static final short MARRY_SCHEDULE_DIVOCRE = -1152;
	/**配偶变化*/
	public static final short MARRY_CHANGE_PEIOU = -1153;
	/**信物变化*/
	public static final short MARRY_CHANGE_XINWU = -1154;
	
	//---------------------改名通知(-1161~-1180)-------------------
	/**改名通知跨服充值缓存更新*/
	public static final short MODIFY_NAME_EVENT_1 = -1161;
	/**温泉排行榜缓存更新*/
	public static final short MODIFY_NAME_EVENT_2 = -1162;
	/**公会成员名字*/
	public static final short MODIFY_NAME_EVENT_3 = -1163;
	/**配偶名字*/
	public static final short MODIFY_NAME_EVENT_4 = -1164;
	/**改名通知单服充值缓存更新*/
	public static final short MODIFY_NAME_EVENT_5 = -1165;
	/**改名通知跨服消费缓存更新*/
	public static final short MODIFY_NAME_EVENT_6 = -1166;
	//-----------------------七杀(-1181~-1185)------------------------
	/** 勇闯七杀活动开启 */
	public static final short DINGSHI_QISHA_START = -1181;
	/**勇闯七杀活动结束*/
	public static final short DINGSHI_QISHA_END = -1182;
	/**勇闯七杀增加经验*/
	public static final short QISHA_EXP_ADD = -1183;
	/** 场景进入定时刷怪(公会伤害排行BOSS) */
	public static final short INNER_DS_GUILD_HURT_MONSTER = -1184;
	/**七杀BOSS死亡*/
	public static final short QISHA_BOSS_DEAD = -1185;
	//---------------------混沌战场(-1186~-1200)----------------------
	/**混沌战场活动开启 */
	public static final short DINGSHI_HUNDUN_START = -1186;
	/**混沌战场活动结束*/
	public static final short DINGSHI_HUNDUN_END = -1187;
	/**混沌战场定时检测*/
	public static final short DINGSHI_HUNDUN_CHECK = -1188;
	/**混沌战场定时加经验*/
	public static final short DINGSHI_HUNDUN_ADD_EXP = -1189;
	
	//-----------------------发红包(-1190~-1195)------------------------
	/**发红包*/
	public static final short HONGBAO_SEND = -1190;
	
	//---------------------跨服竞技(-2001~--2050)-------------------
	public final static short KUAFU_ARENA_1V1_MATCH = -2001;
	/**
	 * 跨服单人竞技场 匹配成功
	 */
	public final static short KUAFU_ARENA_1V1_MATCH_SUCCESS = -2002;
	/**
	 * 跨服单人竞技场 发起取消匹配请求
	 */
	public final static short KUAFU_ARENA_1V1_CANCEL_MATCH = -2003;
	/**
	 * 跨服单人竞技场 取消匹配操作成功
	 */
	public final static short KUAFU_ARENA_1V1_CANCEL_MATCH_RESULT = -2004;
	/**
	 * 跨服单人竞技场进入场景
	 */
	public static final short KUAFU_ARENA_1V1_ENTER_STAGE = -2005;
	/** 发送成员数据 */
	public static final short KUAFU_ARENA_1V1_SEND_ROLE_DATA = -2006;
	/** 进入跨服单人竞技场失败,通知原服 */
	public static final short KUAFU_ARENA_1V1_ENTER_STAGE_FAIL = -2007;
	/** 跨服单人竞技场死亡处理 */
	public static final short KUAFU_ARENA_1V1_DEAD_HANDLE = -2008;
	/** 跨服单人竞技场结算*/
	public static final short KUAFU_ARENA_1V1_CALC_RESULT = -2009;
	/** 跨服发到源服 通知玩家离开副本*/
	public static final short KUAFU_ARENA_1V1_LEAVE_FB = -2010;
	/** 源服发到跨服 通知玩家离开副本*/
	public static final short KUAFU_ARENA_1V1_EXIT_FB_TO_KUAFU = -2011;
//	/** 源服发到跨服 玩家下线*/
//	public static final short KUAFU_ARENA_1V1_OFFLINE_TO_KUAFU = -2012;
	/** 跨服里开始战斗*/
	public static final short KUAFU_ARENA_1V1_START_TO_KUAFU = -2013;
	/** 跨服强制结束*/
	public static final short KUAFU_ARENA_1V1_FORCE_END = -2014;
	/** 匹配失败*/
	public static final short KUAFU_ARENA_1V1_MATCH_FAIL = -2015;
	/** 匹配超时容错*/
	public static final short KUAFU_ARENA_1V1_MATCH_TIME_OUT = -2016;
	/** 准备超时容错*/
	public static final short KUAFU_ARENA_1V1_PREPARE_TIME_OUT = -2017;
	/** 开始战斗*/
	public static final short KUAFU_ARENA_1V1_PK_START = -2018;
	/** 绑定serverId*/
	public static final short BIND_ROLE_ID_SERVERID = -2019;
	/** 进入小黑屋*/
	public static final short KUAFU_ARENA_1V1_ENTER_XIAOHEIWU = -2020;
	/** 强制踢人*/
	public static final short KUAFU_ARENA_1V1_KICK_MEMBER = -2021;
	/**2周清job*/
	public static final short KUAFU_ARENA_1V1_2WEEK_JOB = -2022;
//	/**2周清job*/
//	public static final short KUAFU_ARENA_1V1_CLEAN_REDIS_RANK = -2023;
	/** 解绑绑定serverId*/
	public static final short CLEAR_ROLE_KUAFU = -2024;
	/**2周后,1v1延迟5分钟 发送邮件奖励 **/
    public static final short KUAFU_ARENA_1V1_SEND_EMIAL_REWARD = -2025;
	
	// ------------------ 神器洗练(-1200~-1205)----------------------
	/** 神器洗练属性变化 */
	public static final short INNER_SHENQI_WASH_ATTR_CHANGE = -1200;
	// ------------------ 八卦阵----------------------
	/** 通知八卦副本场景退出 **/
	public static final short BAGUA_SELF_LEAVE_FUBEN = -1210;
	/** 通知八卦副本场景完成 **/
	public static final short BAGUA_FUBEN_FINISH = -1211;
	/** 通知BUS业务退出八卦副本 **/
	public static final short BAGUA_FORCE_EXIT_FUBEN = -1212;
	/** 八卦副本完成（标记为副本完成状态） **/
	public static final short BAGUA_FUBEN_FINISH_HANDLE = -1213;
	/** 八卦副本进入状态 */
	public static final short BAGUA_AFTER_DJS = -1214;
	/** 请求创建八卦副本队伍 */
	public static final short BAGUA_CREATE_TEAM_KF = -1215;
	/** 请求八卦副本队伍信息 */
	public static final short BAGUA_SELECT_TEAM = -1216;
	/** 八卦副本离线业务 */
	public static final short BAGUA_FUBEN_OFFLINE_HANDLE = -1217;
	/** 八卦副本申请加入队伍 */
	public static final short BAGUA_FUBEN_APPLY_ENTER_TEAM = -1218;
	/** 八卦副本退出副本 */
	public static final short BAGUA_EXIT_FUBEN = -1219;
	/** 八卦副本踢出副本 */
	public static final short BAGUA_KICK = -1220;
	/** 八卦副本队长变更战力需求 */
	public static final short BAGUA_CHANGE_STRENGTH = -1221;
	/** 队长更改八卦副本队伍是否满员自动挑战 */
	public static final short BAGUA_CHANGE_TEAM_AUTO_START = -1222;
	/** （副本预进入请求）通知队伍里所有的人在指定时间戳后进入副本 */
	public static final short BAGUA_START_TEAM = -1223;
	/** 成员准备 */
	public static final short BAGUA_CHANGE_PREPARE_STATUS = -1224;
	/** 请求成员数据 */
	public static final short BAGUA_FUBEN_ASK_ROLE_DATA = -1225;
	/** 发送成员数据 */
	public static final short BAGUA_SEND_ROLE_DATA = -1226;
	/** 离开多人队伍 */
	public static final short BAGUA_LEAVE_TEAM = -1227;
	/** 进入跨服 */
	public static final short BAGUA_ENTER_XIAO_HEI_WU = -1228;
	/** 通知跨服进入场景 */
	public static final short BAGUA_ENTER_FUBEN = -1229;
	/** 离开八卦副本 */
	public static final short BAGUA_LEAVE_FUBEN_YF = -1230;
	/** 离开八卦副本(跨服) */
	public static final short BAGUA_EXIT_KUAFU = -1231;
	/** 进入八卦副本失败,通知原服 */
	public static final short BAGUA_ENTER_FUBEN_FAIL = -1232;
	/** 跨服通知原服八卦本队伍创建（进入）成功 */
	public static final short BAGUA_JOIN_TEAM_SUCCESS = -1233;
	/** 八卦副本异步离线业务 */
	public static final short BAGUA_FUBEN_OFFLINE_HANDLE_SYN = -1234;
	/** 八卦副本进入真门 */
	public static final short BAGUA_ENTER_DOOR = -1235;
	/** 八卦副本进入真门 */
	public static final short BAGUA_ENTER_DOOR_KF = -1236;
	/** 处理过期队伍 */
	public static final short HANDLE_BAGUA_FUBEN_TIME_OUT_TEAM = -1237;
	// ------------------ 平台相关(-1250~-1260)----------------------
	/**请求360V计划用户信息 */
	public static final short INNER_360_VPLAN_INFO = -1250;
	
	// ------------------ 器灵(-1261~-1276)----------------------
	/** 器灵属性变化 */
	public static final short INNER_QILING_CHANGE = -1261;
	/** 初始化场景里面的器灵信息 */
	public static final short INNER_QILING_ADD = -1262;
	/** 场景内部器灵变化 */
	public static final short INNER_QILING_STATE = -1263;
	/**器灵形象变化 */
	public static final short QILING_SHOW_UPDATE = -1264;
	/**器灵战斗力变化 */
	public static final short INNER_ZPLUS_QILING = -1265;
	
	//---------------------神魔战场1(-2001~--2050)-------------------
	public final static short KUAFU_ARENA_4V4_MATCH = -1301;
	/**
	 * 跨服单人竞技场 匹配成功
	 */
	public final static short KUAFU_ARENA_4V4_MATCH_SUCCESS = -1302;
	/**
	 * 跨服单人竞技场 发起取消匹配请求
	 */
	public final static short KUAFU_ARENA_4V4_CANCEL_MATCH = -1303;
	/**
	 * 跨服单人竞技场 取消匹配操作成功
	 */
	public final static short KUAFU_ARENA_4V4_CANCEL_MATCH_RESULT = -1304;
	/**
	 * 跨服单人竞技场进入场景
	 */
	public static final short KUAFU_ARENA_4V4_ENTER_STAGE = -1305;
	/** 发送成员数据 */
	public static final short KUAFU_ARENA_4V4_SEND_ROLE_DATA = -1306;
	/** 进入跨服单人竞技场失败,通知原服 */
	public static final short KUAFU_ARENA_4V4_ENTER_STAGE_FAIL = -1307;
//	/** 跨服单人竞技场死亡处理 */
//	public static final short KUAFU_ARENA_1V1_DEAD_HANDLE = -2008;
//	/** 跨服单人竞技场结算*/
//	public static final short KUAFU_ARENA_1V1_CALC_RESULT = -2009;
	/** 跨服发到源服 通知玩家离开副本*/
	public static final short KUAFU_ARENA_4V4_LEAVE_FB = -1310;
	/** 源服发到跨服 通知玩家离开副本*/
	public static final short KUAFU_ARENA_4V4_EXIT_FB_TO_KUAFU = -1311;
	/** 源服发到跨服 玩家下线*/
	public static final short KUAFU_ARENA_4V4_OFFLINE_TO_KUAFU = -1312;
	/** 跨服里开始战斗*/
	public static final short KUAFU_ARENA_4V4_START_TO_KUAFU = -1313;
	/** 跨服强制结束*/
	public static final short KUAFU_ARENA_4V4_FORCE_END = -1314;
	/** 匹配失败*/
	public static final short KUAFU_ARENA_4V4_MATCH_FAIL = -1315;
	/** 匹配超时容错*/
	public static final short KUAFU_ARENA_4V4_MATCH_TIME_OUT = -1316;
	/** 准备超时容错*/
	public static final short KUAFU_ARENA_4V4_PREPARE_TIME_OUT = -1317;
	/** 开始战斗*/
	public static final short KUAFU_ARENA_4V4_PK_START = -1318;
//	/** 绑定serverId*/
//	public static final short BIND_ROLE_ID_SERVERID = -2019;
	/** 进入小黑屋*/
	public static final short KUAFU_ARENA_4V4_ENTER_XIAOHEIWU = -1320;
//	/** 强制踢人*/
//	public static final short KUAFU_ARENA_1V1_KICK_MEMBER = -2021;
	/**2周清job*/
	public static final short KUAFU_ARENA_4V4_2WEEK_JOB = -1322;
//	/**2周清job*/
//	public static final short KUAFU_ARENA_4V4_CLEAN_REDIS_RANK = -1323;
	/**2周后,4v4延迟5分钟 发送邮件奖励 **/
    public static final short KUAFU_ARENA_4V4_SEND_EMIAL_REWARD = -1325;
	
	/** 当超时的时候 从已匹配进的队伍中删除 */
	public static final short KUAFU_ARENA_4V4_REMOVE_REQUEST = -1324;
	/** 解散队伍 */
	public static final short KUAFU_ARENA_4V4_DISPOSE_TEAM = -1326;	
	/** 结算*/
	public static final short KUAFU_ARENA_4V4_CALC_RESULT = -1327;
	public final static short KUAFU_ARENA_4V4_TEAM_MATCH = -1328;
	/** 匹配超时容错*/
	public static final short KUAFU_ARENA_4V4_TEAM_MATCH_DJS = -1329;
	//--------------------神魔战场2(-1277~-1290)---------------------------
	/**增加积分*/
	public static final short SHENMO_ADD_SHUIJING_SCORE = -1277;
	/**活动结束定时*/
	public static final short SHENMO_ACTIVE_STOP = -1278;
	/**活动结束踢出所有玩家*/
	public static final short SHENMO_KICK_ALL = -1279;
	/**定时通知玩家当前双方积分情况*/
	public static final short SHENMO_SCHEDULE_NOTICE_SCORE = -1280;
	public static final short SHENMO_AUTO_FUHUO = -1281;
	public static final short SHENMO_ESCAPE = -1282;

	//---------------------塔防副本(-1291~-1300)---------------------------
	/**击杀怪物增加经验*/
	public static final short TAFANG_ADD_EXP = -1291;
	/**刷怪*/
	public static final short TAFANG_SHUAXIN_GUAIWU = -1292;
	
	//--------------------妖神魂魄(-1400~-1409)---------------------------
	/**副本时间到，通知业务层结算 **/
	public static final short B_EXIT_JIANZHONG_FUBEN = -1400;
	/** 通知场景强制退出副本，再次回收 **/
	public static final short S_FORCE_EXIT_FUBEN = -1401;
	/** 通知场景副本时间到，已完成 **/
	public static final short S_JIANZHONG_FUBEN_OVER = -1403;
	/** 通知业务层场景已退出副本 **/
	public static final short HAS_EXIT_JIANZHONG_FUBEN = -1404; 
	/** 通知场景退出塔防副本 **/
	public static final short S_EXIT_TAFANG_FUBEN = -1405;
	
	//--------------------妖神魔印(-1410~-1419)---------------------------
	/**魔印属性变化 */
	public static final short INNER_YAOSHEN_MOYIN_ATTR_CHANGE = -1410;
	//--------------------成神(-1420~-1425)---------------------------
	/**成神属性变化 */
	public static final short INNER_CHENG_SHEN_SJ_ATTR_CHANGE = -1420;
	
	// ------------------ 埋骨之地(-1510~-1539)----------------------
	/** 通知埋骨副本场景退出 **/
	public static final short MAIGU_SELF_LEAVE_FUBEN = -1510;
	/** 通知埋骨副本场景完成 **/
	public static final short MAIGU_FUBEN_FINISH = -1511;
	/** 通知BUS业务退出埋骨副本 **/
	public static final short MAIGU_FORCE_EXIT_FUBEN = -1512;
	/** 埋骨副本完成（标记为副本完成状态） **/
	public static final short MAIGU_FUBEN_FINISH_HANDLE = -1513;
	/** 埋骨副本进入状态 */
	public static final short MAIGU_AFTER_DJS = -1514;
	/** 请求创建埋骨副本队伍 */
	public static final short MAIGU_CREATE_TEAM_KF = -1515;
	/** 请求埋骨副本队伍信息 */
	public static final short MAIGU_SELECT_TEAM = -1516;
	/** 埋骨副本离线业务 */
	public static final short MAIGU_FUBEN_OFFLINE_HANDLE = -1517;
	/** 埋骨副本申请加入队伍 */
	public static final short MAIGU_FUBEN_APPLY_ENTER_TEAM = -1518;
	/** 埋骨副本退出副本 */
	public static final short MAIGU_EXIT_FUBEN = -1519;
	/** 埋骨副本踢出副本 */
	public static final short MAIGU_KICK = -1520;
	/** 埋骨副本队长变更战力需求 */
	public static final short MAIGU_CHANGE_STRENGTH = -1521;
	/** 队长更改埋骨副本队伍是否满员自动挑战 */
	public static final short MAIGU_CHANGE_TEAM_AUTO_START = -1522;
	/** （副本预进入请求）通知队伍里所有的人在指定时间戳后进入副本 */
	public static final short MAIGU_START_TEAM = -1523;
	/** 成员准备 */
	public static final short MAIGU_CHANGE_PREPARE_STATUS = -1524;
	/** 请求成员数据 */
	public static final short MAIGU_FUBEN_ASK_ROLE_DATA = -1525;
	/** 发送成员数据 */
	public static final short MAIGU_SEND_ROLE_DATA = -1526;
	/** 离开多人队伍 */
	public static final short MAIGU_LEAVE_TEAM = -1527;
	/** 进入跨服 */
	public static final short MAIGU_ENTER_XIAO_HEI_WU = -1528;
	/** 通知跨服进入场景 */
	public static final short MAIGU_ENTER_FUBEN = -1529;
	/** 离开埋骨副本 */
	public static final short MAIGU_LEAVE_FUBEN_YF = -1530;
	/** 离开埋骨副本(跨服) */
	public static final short MAIGU_EXIT_KUAFU = -1531;
	/** 进入埋骨副本失败,通知原服 */
	public static final short MAIGU_ENTER_FUBEN_FAIL = -1532;
	/** 跨服通知原服埋骨本队伍创建（进入）成功 */
	public static final short MAIGU_JOIN_TEAM_SUCCESS = -1533;
	/** 埋骨副本异步离线业务 */
	public static final short MAIGU_FUBEN_OFFLINE_HANDLE_SYN = -1534;
	/** 处理过期队伍 */
	public static final short HANDLE_MAIGU_FUBEN_TIME_OUT_TEAM = -1535;

	
	// ------------------ 转生(-1540~-1545)----------------------
	/**转生等级变化*/
	public static final short ZHUANSHENG_LEVEL_CHANGE = -1540;
	

	// ------------------ 糖宝心纹(-1546~-1550)----------------------
	/**糖宝心纹属性变化 */
	public static final short INNER_TANGBAO_XINWEN_ATTR_CHANGE = -1546;

	//------------------ 单服充值活动-1550~-1155)----------------------
	/**单服充值活动*/
	public static final short DANFU_CHARGE_RANK = -1550;
	/**单服充值活动结束定时器*/
	public static final short DANFU_CHARGE_RANK_END_SCHEDULE = -1551; 
	
	// ------------------ 通天之路(-1556~-1558)----------------------
	/**通天之路获得属性 */
	public static final short TONGTIAN_ROAD_GET_ATTR = -1556;
	// ------------------ 门派炼狱boss(-1559~-1568)----------------------
	/**炼狱boss完成 --场景层处理*/
	public static final short LIANYU_BOSS_FINISH_2_STAGE = -1559;
	/**炼狱boss完成 --业务层处理*/
	public static final short LIANYU_BOSS_FINISH_2_BUS = -1560;
	/**退出炼狱副本 */
	public static final short LIANYU_BOSS_EXIT = -1561;

	//--------------------妖神附魔(-1569~-1574)---------------------------
	/**魔印属性变化 */
	public static final short INNER_YAOSHEN_HUMO_ATTR_CHANGE = -1569;
	
	// ------------------ 跨服世界boss(-1570~-1579)----------------------
	/**跨服boss活动开始 */
	public static final short DINGSHI_KUAFUBOSS_ACTIVE_START = -1570;
	/**跨服boss活动结束 */
	public static final short DINGSHI_KUAFUBOSS_ACTIVE_END = -1571;
	/**通知跨服boss活动开始 */
	public static final short DINGSHI_KUAFUBOSS_ACTIVE_START_KUAFU = -1572;
	/** 跨服boss加经验定时**/
	public final static short KUAFUBOSS_ADD_EXP_DINGSHI = -1573;
	/** 发送成员数据 */
	public static final short KUAFUBOSS_SEND_ROLE_DATA = -1574;
	public static final short KUAFUBOSS_AUTO_FUHUO = -1575;
	/** 进入小黑屋*/
	public static final short KUAFUBOSS_ENTER_XIAOHEIWU = -1576;
	/** 进入跨服boss失败*/
	public static final short KUAFUBOSS_ENTER_FAIL = -1577;
	/** 跨服boss奖励邮件*/
	public static final short KUAFUBOSS_REWARD_MAIL = -1578;
	/** 跨服boss退出*/
	public static final short KUAFUBOSS_EXIT = -1579;
	/** 排行刷新 */
	public static final short KUAFU_BOSS_RANK = -1580;
	/** 跨服boss强踢人*/
	public final static short KUAFUBOSS_FORCE_KICK = -1581;
	/** 退出副本*/
	public static final short KUAFUBOSS_LEAVE_FB = -1582;
	/** boss被击杀*/
	public static final short KUAFUBOSS_DEAD = -1583;
	/** 跨服boss复活*/
	public static final short KUAFUBOSS_FUHUO = -1584;
	
	// ------------------ 画卷(-1601~-1610)----------------------
	/** 画卷属性变化 */
	public static final short HUAJUAN_ATTR_CHANGE = -1601;
	
	// ------------------ 跨服大乱斗(-1610~-1649)----------------------
	/** 发送成员数据 */
	public static final short KUAFULUANDOU_SEND_ROLE_DATA = -1614;
	public static final short KUAFULUANDOU_AUTO_FUHUO = -1615;
	/** 进入小黑屋*/
	public static final short KUAFULUANDOU_ENTER_XIAOHEIWU = -1616;
	/** 进入跨服大乱斗失败*/
	public static final short KUAFULUANDOU_ENTER_FAIL = -1617;
	/** 跨服大乱斗退出*/
	public static final short KUAFULUANDOU_EXIT = -1619;
	/** 排行刷新 */
	public static final short KUAFU_LUANDOU_RANK = -1620;
	/** 跨服大乱斗强踢人*/
	public final static short KUAFULUANDOU_FORCE_KICK = -1621;
	/** 退出副本*/
	public static final short KUAFULUANDOU_LEAVE_FB = -1622;
	/** 跨服大乱斗复活*/
	public static final short KUAFULUANDOU_FUHUO = -1624;
	/**增加积分*/
	public static final short KUAFULUANDOU_ADD_JIFEN = -1625;
	/**取消无敌*/
	public static final short KUAFULUANDOU_QUXIAO_WUDI = -1626;
	
   // ------------------ 巅峰之战(-1627~-)----------------------
	/** 巅峰之战发送成员数据 */
    public static final short KUAFU_DIANFENG_SEND_ROLE_DATA = -1627;
    /** 进入巅峰之战小黑屋*/
    public static final short KUAFU_DIANFENG_ENTER_XIAOHEIWU = -1628;
    /** 进入巅峰之战失败*/
    public static final short KUAFU_DIANFENG_ENTER_FAIL = -1629;
    /** 巅峰之战小场战斗开始  **/
    public static final short KUAFU_DIANFENG_START_PK = -1630;
    /** 巅峰之战小场战斗死亡  **/
    public static final short KUAFU_DIANFENG_DEATH_PK = -1631;
    /** 巅峰之战小场战斗结束  **/
    public static final short KUAFU_DIANFENG_END_PK = -1632;
    /** 巅峰之战小场战斗结果展示  **/
    public static final short KUAFU_DIANFENG_RESULT_SHOW = -1633;
    /** 巅峰之战强踢  **/
    public static final short KUAFU_DIANFENG_FORCE_KICK = -1634;
    /** 巅峰之战退出场景  **/
    public static final short KUAFU_DIANFENG_EXIT_STAGE = -1635;
    /** 巅峰之战下线处理  **/
    public static final short KUAFU_DIANFENG_OFFINE = -1636;
    /** 巅峰之战保存到跨服数据  **/
    public static final short KUAFU_DIANFENG_SAVE_DATA = -1637;
    /** 巅峰之战插入Redis数据  **/
    public static final short KUAFU_DIANFENG_INSERT_DATA = -1638;
    /** 巅峰之战更新Redis数据  **/
    public static final short KUAFU_DIANFENG_UPDATE_DATA = -1639;
    /** 巅峰之战通知源服比赛开始  **/
    public static final short KUAFU_DIANFENG_NOTICE = -1640;
    
	// ------------------ 天羽(-1700~-1750)----------------------
	/** 天羽属性变化 */
	public static final short INNER_TIANYU_CHANGE = -1700;
	/** 初始化场景里面的天羽信息 */
	public static final short INNER_TIANYU_ADD = -1701;
	/** 场景内部天羽变化 */
	public static final short INNER_TIANYU_STATE = -1702;
	/**天羽形象变化 */
	public static final short TIANYU_SHOW_UPDATE = -1703;
	/**天羽战斗力变化 */
	public static final short INNER_ZPLUS_TIANYU = -1704;

	//-------------------转职（-1751~-1800）-------------------------------
	/**转职成功通知场景*/
	public static final short INNER_ROLE_CHANGE_JOB = -1751;
	//-------------------神武（-1801~-1850）-----------------------------------
	public static final short ZHUANGBEI_FUHOU = -1801;
	
	// ------------------ 跨服群仙宴(-1850~-1899)----------------------
	/** 跨服群仙宴加经验定时**/
	public final static short KUAFUQUNXIANYAN_ADD_EXP_DINGSHI = -1850;
	/** 发送成员数据 */
	public static final short KUAFUQUNXIANYAN_SEND_ROLE_DATA = -1851;
	public static final short KUAFUQUNXIANYAN_AUTO_FUHUO = -1852;
	/** 进入小黑屋*/
	public static final short KUAFUQUNXIANYAN_ENTER_XIAOHEIWU = -1853;
	/** 进入跨服群仙宴失败*/
	public static final short KUAFUQUNXIANYAN_ENTER_FAIL = -1854;
	/** 跨服群仙宴奖励邮件*/
	public static final short KUAFUQUNXIANYAN_REWARD_MAIL = -1855;
	/** 跨服群仙宴退出*/
	public static final short KUAFUQUNXIANYAN_EXIT = -1856;
	/** 排行刷新 */
	public static final short KUAFU_QUNXIANYAN_RANK = -1857;
	/** 跨服群仙宴强踢人*/
	public final static short KUAFUQUNXIANYAN_FORCE_KICK = -1858;
	/** 退出副本*/
	public static final short KUAFUQUNXIANYAN_LEAVE_FB = -1859;
	/** 跨服群仙宴复活*/
	public static final short KUAFUQUNXIANYAN_FUHUO = -1860;
	/**增加积分*/
	public static final short KUAFUQUNXIANYAN_ADD_JIFEN = -1861;
	/**死亡积分处理*/
	public static final short KUAFUQUNXIANYAN_DEAD_JIFEN = -1862;
	/**刷资源*/
	public static final short KUAFUQUNXIANYAN_ZIYUAN = -1863;
	/**请求排行榜*/
	public static final short KUAFUQUNXIANYAN_GET_RANK = -1864;
	/**采集奖励*/
	public static final short KUAFUQUNXIANYAN_CJ_ITEM = -1865;
	/**采集奖励*/
	public static final short KUAFUQUNXIANYAN_JIESUAN_EMAIL = -1866;
	
	//--------------------------五行---------------------------
	/** 五行属性变化 */
	public static final short INNER_WUXING_CHANGE = -1900;
	/** 五行附体修改 */
	public static final short WUXING_FUTI_CHARGE = -1901;
	/** 五行技能属性变化 */
	public static final short INNER_WX_SKILL_CHARGE = -1902;
	/** 五行精魄属性变化 */
	public static final short INNER_WX_JP_CHARGE = -1903;
	
	//-------------------------首冲返利----------------------------
	public static final short INNER_FIRST_CHARGER_REBATE = -1904;
    //--------------------------糖宝五行---------------------------
    /** 糖宝五行属性变化 */
    public static final short INNER_TB_WUXING_CHANGE = -1905;
    /** 糖宝五行技能属性变化 */
    public static final short INNER_TB_WX_SKILL_CHARGE = -1906;
    //--------------------------心魔---------------------------
    /** 心魔属性变化 */
    public static final short INNER_XINMO_CHANGE = -1907;
    /** 增加心魔凝神需要的心神力 */
    public static final short INNER_ADD_XINMO_EXP = -1908;
    //-------------------------心魔炼丹----------------------
    /** 定时产生丹药 **/
    public static final short XM_LIANDAN_DINGSHI = -1909;
    //-------------------------心魔-魔神----------------------
    /** 心魔-魔神属性变化 */
    public static final short INNER_XM_MOSHEN_CHARGE = -1910;
    /** 心魔-魔神噬体属性变化 */
    public static final short INNER_XM_MOSHEN_SHITI_CHARGE = -1911;
    /** 心魔-魔神噬体CD结束,解体 **/
    public static final short XM_MOSHEN_SHITI_CD = -1912;
    //-------------------------心魔-副本----------------------
    /** 心魔-副本减少腐化度 **/
    public static final short XM_FUBEN_CUT_FUHUA = -1913;
    /** 心魔-副本挑战通关完成 **/
    public static final short S_XINMO_FUBEN_FINISH = -1914;
    /** 心魔-副本成功离开 **/
    public static final short B_EXIT_XM_FUBEN = -1915;
    //-------------------------心魔-魔神技能----------------------
    /** 心魔-魔神技能属性变化 */
    public static final short INNER_XM_SKILL_CHARGE = -1916;
    //-------------------------心魔深渊-副本----------------------
    /** 心魔深渊-副本冷却 **/
    public static final short XM_SHENYUAN_FUBEN_COLING = -1917;
    /** 心魔深渊-副本挑战通关完成 **/
    public static final short S_XM_SHENYUAN_FUBEN_FINISH = -1918;
    /** 心魔深渊-副本成功离开 **/
    public static final short B_XM_SHENYUAN_FUBEN_EXIT = -1919;
    //-------------------------心魔-洗练----------------------
    /** 心魔-洗练属性变化 */
    public static final short INNER_XM_XILIAN_CHARGE = -1920;
    //-------------------------心魔斗场-副本----------------------
    /** 心魔斗场-副本挑战通关完成 **/
    public static final short S_XM_DOUCHANG_FUBEN_FINISH = -1921;
    /** 心魔斗场-离开副本,发送奖励 **/
    public static final short B_XM_DOUCHANG_FUBEN_EXIT = -1922;
    /** 心魔斗场击杀怪物,增加buff效果和记录杀怪数量 */
    public static final short I_XM_DOUCHANG_ADD_BUFF = -1923;
    //-----------------------画卷2场景属性刷新-------------------
    public static final short I_HUANJUAN2_CHARGE = -1924;
    //----------------------灵火祝福场景属性刷新------------------
    public static final short I_LINGHUO_BLESS_CHARGE = -1925;
    
    //-----------------------begin跨服-云宫之巅(-1926~-1948)begin----------------------
    
    //------------------------------源服执行--------------------------//
    /** 进入准备阶段定时器 **/
    public static final short KUAFU_YUNGONG_READY = -1926;
    /** 进入开始阶段定时器 **/
    public static final short KUAFU_YUNGONG_START = -1927;
    /** 进入结束阶段定时器 **/
    public static final short KUAFU_YUNGONG_END = -1928;
    
    // ----------------------------跨服转发到源服执行------------------//
    /** 进入云宫之巅失败*/
    public static final short KUAFU_YUNGONG_ENTER_FAIL = -1929;
    /** 进入云宫之巅小黑屋*/
    public static final short KUAFU_YUNGONG_ENTER_XIAOHEIWU = -1930;
    /** 退出云宫之巅小黑屋*/
    public static final short KUAFU_YUNGONG_EXIT_XIAOHEIWU = -1931;
    /** 源服更新拔旗消耗 **/
    public static final short KUAFU_YUNGONG_QIZI_CONSUME = -1932;
    /** 源服更新活动结果数据 */
    public static final short KUAFU_YUNGONG_UPDATE_RESULT = -1933;
    /** 源服同步跨服活动进度 **/
    public static final short KUAFU_YUNGONG_ACTIVITY_END = -1934;
    /** 源服增加经验真气 */
    public static final short KUAFU_YUNGONG_ADD_EXP_ZQ = -1935;
    /** 源服发送邮件奖励 **/
    public static final short KUAFU_YUNGONG_SEND_EMAIL_REWARD = -1948;

    // ----------------------------源服转发到跨服执行------------------//
    /** 发送成员数据到跨服 */
    public static final short KUAFU_YUNGONG_SEND_ROLE_DATA = -1936;
    /** 通知跨服开始采集旗子 **/
    public static final short I_KUAFU_YUNGONG_COLLECT = -1937;
    /** 通知跨服开始拔起旗子 **/
    public static final short I_KUAFU_YUNGONG_PULL = -1938;
    /** 通知跨服场景中门主，仙侣切换外显 */
    public static final short I_KUAFU_YUNGONG_CHANGE_CLOTHES = -1939;
    /** 场景中玩家复活 **/
    public static final short I_KUAFU_YUNGONG_AUTO_FUHUO = -1940;
    /** 请求退出场景 **/
    public static final short I_KUAFU_YUNGONG_EXIT = -1941;
    
    // ----------------------------跨服执行--------------------------//
    /** 产生占领公会 **/
    public static final short I_KUAFU_YUNGONG_HAS_WINNER = -1942;
    /** 同步旗子坐标 */
    public static final short I_KUAFU_YUNGONG_SYN_QIZI = -1943;
    /** 增加经验和真气 */
    public static final short I_KUAFU_YUNGONG_ADD_EXP_ZQ = -1944;
    /** 活动场景清人 **/
    public static final short I_KUAFU_YUNGONG_CLEAR = -1945;
    /** 活动结束 **/
    public static final short I_KUAFU_YUNGONG_OVER = -1946;
    /** 旗子占领状态更新:1.扛旗者被杀;2.图腾被其他公会消灭;3.扛旗者下线或者换线 **/
    public static final short I_KUAFU_YUNGONG_OWNER_UPDATE = -1947;
    
    //-----------------------end跨服-云宫之巅(-1926~-1948)end----------------------
    
    //---------------------------魔宫猎焰MGLY-----------------------------------------
    /** 定时刷场景boss怪物 */
    public static final short I_MGLY_DS_REFRESH_BOSS = -1949;
    /** 定时增加经验真气 */
    public static final short I_MGLY_ADD_EXP_ZHENQI = -1950;
    /** 定时减少御魔值 */
    public static final short I_MGLY_CUT_YUMO_VAL = -1951;
    /** 更新御魔值 */
    public static final short I_MGLY_UPDATE_YUMO_VAL = -1952;
    /** 场景中玩家死亡处理 */
    public static final short I_MGLY_ROLE_DEAD = -1953;
    /** 场景中怪物死亡处理 */
    public static final short I_MGLY_MONSTER_DEAD = -1954;
    /** 内部请求场景退出 */
    public static final short I_MGLY_EXIT_STAGE = -1955;
    /** 场景中延迟强踢处理 */
    public static final short I_MGLY_DELAY_EXIT = -1956;
    
    //------------------------------仙器觉醒----------------------------------------
    /** 仙洞属性变化 */
    public static final short INNER_XIANDONG_CHANGE = -1957;
    /** 仙器觉醒属性变化 */
    public static final short INNER_XIANQIJUEXING_CHANGE = -1958;
    
    //------------------------------仙器副本-云瑶晶脉----------------------------------------
    /** 请求退出云瑶晶脉副本场景 **/
    public static final short INNER_YYJM_EXIT_STAGE = -1959;
    /** 云瑶晶脉副本场景结束清人 **/
    public static final short INNER_YYJM_FORCE_KICK = -1960;
    
    //------------------------------仙缘飞化----------------------------------------
    /** 仙缘飞化属性变更 */
    public static final short INNER_XIANYUAN_FEIHUA_CHANGE=-1961;
    
  //------------------------------套装象位---------------------------------------
    /** 套装象位属性变更 */
    public static final short INNER_XIANGWEI_CHANGE= -2051;
    
    //------------------ 跨服消费活动-1965~-1970)----------------------
  	/**跨服充值活动*/
  	public static final short KUAFU_XIAOFEI_RANK = -1965;
  	
  //-------------------个人boss(-2051~-2075)----------------------
  	public static final short PERSONAL_BOSS_RESULT = -2053;
  	public static final short PERSONAL_BOSS_KILL = -2054;
  	public static final short PERSONAL_BOSS_KICK = -2055;
  	/**4593  退出副本*/
	public static final short PERSONAL_BOSS_LEAVE = -2056;
    /** boss积分属性变更 */
    public static final short INNER_BOSS_JIFEN_CHANGE=-2057;
	
    
    
	// ------------------圣剑(-2100~-2150)------------
	/** 新圣剑属性变化 */
	public static final short INNER_WUQI_CHANGE = -2100;
//	/** 坐骑状态信息变化 */
//	public static final short WUQI_STATE = -2101;
	/** 新圣剑外显变化信息 */
	public static final short WUQI_SHOW_UPDATE = -2102;
	
	//TODO  三个内部属性修改
//	/** 通知bus业务持久化坐骑状态 */
//	public static final short WUQI_BUS_STATE = -2103;
//	
//	/** 初始化场景里面的坐骑信息 */
//	public static final short INNER_WUQI_ADD = -2104;
	
	/** 等级变化刷新新圣剑属性 */
	public static final short INNER_WUQI_REFRESH = -2105;
	/** 新圣剑战斗力变化 */
	public static final short INNER_WUQI_ZPLUS_CHANGE = -2106;
	
	// ------------------支线(-2151~-2160)------------
	/**抛指令方式统计支线 */
	public static final short INNER_BRANCH_COUNT = -2151;
	/**激活支线*/
	public static final short INNER_BRANCH_ACTIVITY = -2152;
	/**等级升级触发*/
	public static final short INNER_ROLE_LEVEL_UP = -2153;
	
	//--------------------星空宝藏-------------------
	public static final short INNER_XKBZ_XIAOFEI_BGOLD = -2164;
	
	//------------------修炼任务(-2161~-2170)-------------------------
	/**任务进度变化*/
	public static final short INNER_XIULIAN_TASK_CHARGE = -2161;
	/**任务进度变化特殊*/
	public static final short INNER_XIULIAN_TASK_CHARGE_TS = -2162;
	
	/**
	 * 定时活动公告
	 */
	public static final short DINGSHI_ACTIVE_NOTICE = -2163;
}