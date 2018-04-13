/**
 * 
 */
package com.junyou.stage;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-24上午10:35:47
 */
public class StageCommands {

	/**
	 * 场景相关
	 */
	public static final String INNER_ENTER_STAGE = "S:ENTER";
	
	public static final String INNER_DROP_KF_JF = "s:d_kfjf";
	public static final String INNER_PK_DROP_GOODS = "S:PK_DROP_GOODS";
	public static final String INNER_WAR_PK_DROP_GOODS = "S:WAR_PK_DROP_GOODS";
	public static final String INNER_GOODS_DISAPPEAR = "S:GOODS_DIS";
	public static final String ENTER_STAGE = "21000";
	// 怪物掉落金币
	public static final String MONSTER_DROP_MONEY = "23006";
	
	/**
	 * 被踢换地图
	 */
	public static final String T_OUT_MAP = "20050";
	
	/**
	 *  拉取多个角色战力
	 */
	public static final String LQ_MANY_ZL = "12023";
	/**
	 * 杀怪推送经验玉经验
	 */
	public static final String TUISONG_EXPEXP = "bus:tttsongExp";
	
	/**
	 * 血蓝真元回复
	 */
	public static final String INNER_HP_REVERT = "HP_REVERT";
	public static final String INNER_MP_REVERT = "MP_REVERT";
	public static final String INNER_ZY_REVERT = "ZY_REVERT";
	
	/**
	 * 土城高经验
	 */
	public static final String INNER_TC_EXP = "TC_EXP_REVERT";
	
	
	/**
	 * 副本相关
	 */
	public static final String INNER_FUBEN_EXPIRE_CHECK = "S:EXPIRE_FB";
	public static final String INNER_FUBEN_CHELLENGE_OVER = "S:OVER_FB";
	public static final String INNER_FUBEN_FORCE_LEAVE = "S:FORCE_LEAVE_FB";
	public static final String INNER_APPLY_LEAVE_FUBEN = "SC:APPLY_LEAVE_FUBEN";
	public static final String STATE_FINISH_DAY_FUBEN = "S:FINISH_DAY_FUBEN";
	public static final String STATE_FINISH_PUBLIC_FUBEN = "S:FINISH_PUBLIC_FUBEN";
	public static final String LEAVE_FUBEN = "50504";
	public static final String FUBEN_TYPE = "50506";
	
	public static final String INNER_STAGE_LEAVE_FUBEN = "S:STAGE_LEAVE_FUBEN";
	
	/**
	 * 塔防副本
	 */
	public static final String INNER_TF_FUBEN_EXPIRE_CHECK = "S:EXPIRE_TF_FB";
	public static final String INNER_TF_FUBEN_CHELLENGE_OVER = "S:OVER_TF_FB";
	public static final String INNER_TF_FUBEN_FORCE_LEAVE = "S:FORCE_LEAVE_TF_FB";
	public static final String STATE_FINISH_TF_FUBEN = "S:FINISH_TF_FUBEN";
	public static final String INNER_APPLY_LEAVE_TF_FUBEN = "SC:APPLY_LEAVE_TF_FUBEN";
	public static final String LEAVE_TF_FUBEN = "84004";
	
	public static final String TF_SHUAXIN_GUAIWU = "S:SHUA_GUAI";//刷怪
	
	public static final String YI_DONG_GUAIWU = "S:YI_DONG_GUAIWU";
	
	public static final String TF_SHUA_GUAI = "84002";
	
	public static final String TF_BO_CI = "84006";//向客服端推送当前波次
	
	public static final String INNER_STAGE_LEAVE_TF_FUBEN = "S:STAGE_LEAVE_TF_FUBEN";
	/**
	 * 获取BOSS信息
	 */
	public static final String GET_BOSS_INFO = "86500";
	
	/**
	 * 定时刷新怪物
	 */
	public static final String INNER_REFRESH_MONSTER = "REFRESH_MONSTER";
	public static final String INNER_CLEAR_REFRESH_MONSTER = "CLEAR_REFRESH_MONSTER";
	public static final String INNER_SEND_EMAIL_REF_MONSTER = "SEND_EMAIL_REF_MONSTER";
	
	/**
	 * 节日刷新怪物
	 */
	public static final String INNER_JIERI_MONSTER = "JIERI_MONSTER";
	public static final String INNER_CLEAR_JIERI_MONSTER = "CLEAR_JIERI_MONSTER";
	
	
	/**
	 *  刷新出新的高级怪
	 */
	public static final String NOTIFY_REFRESH_MONSTER = "11101";
	
	/**
	 * 通用的文本通道替换规则
	 */
//	public static final String NOTIFY_CMD = "11202";
	/**
	 * 通用的文本通道替换规则
	 */
	public static final String NOTIFY_GOODS_CMD = "11101";
	
	
	/**
	 * 爬塔副本
	 */
	public static final String INNER_PATA_EXPIRE_CHECK = "S:EXPIRE_PT";
	public static final String INNER_PATA_CHELLENGE_OVER = "S:OVER_PT";
	public static final String INNER_PATA_FORCE_LEAVE = "S:FORCE_LEAVE_PT";
	public static final String INNER_PATA_CHECK_LEAVE = "S:LEAVE_CHECK_PT";
	public static final String INNER_APPLY_PATA_LEAVE_FUBEN = "SC:APPLY_LEAVE_PATA_FUBEN";
	public static final String FINISH_PATA_STATE = "S:FINISH_PATA_STATE";
	public static final String PATA_FUBEN_TONGGUAN_FAIL = "50603";
	
	/**
	 * 武魂
	 */
	public static final String INNER_ROLE_WUHUN_CHANGE = "S:ROLE_WUHUN_CHANGE";
	
	/**
	 * 武魂迭代
	 */
	public static final String INNER_ROLE_WUHUN_DD_CHANGE = "S:ROLE_WUHUN_DD_CHANGE";
	/**
	 * 场景：武魂启明变化
	 */
	public static final String INNER_ROLE_WUHUN_QM_CHANGE = "S:ROLE_WUHUN_QM_CHANGE";
	/**
	 * 坐骑
	 */
	public static final String INNER_ROLE_ZUOQI_CHANGE = "S:ROLE_ZUOQI_CHANGE";
	
	/**
	 * 转生真龙
	 */
	public static final String INNER_ROLE_ZHUANSHENG_CHANGE = "S:ROLE_ZHUANSHENG_CHANGE";
	
	/**
	 * 暗器变化
	 */
	public static final String INNER_ROLE_KNIVES_CHANGE = "S:ROLE_KNIVES_CHANGE";
	
	/**
	 * PK值变化
	 */
	public static final String INNER_ROLE_PK_CHANGE = "S:PK_VALUE";
	public static final String INNER_ROLE_PK_HUI_CHANGE = "S:PK_HUI";
	
	/**
	 * VIP变化
	 */
	public static final String INNER_VIP_CHANGE = "vip_change";
	
	/**
	 * 通知主角PK值变化
	 */
	public static final String PK_VALUE_CHANGE = "40601";
	public static final String PK_HUI_CHANGE = "40602";
	
	/**
	 * aoi信息推送
	 * */
	public static final String AOI_ROLES_LEAVE = "24001";
	
	public static final String AOI_HD_ROLE_ENTER = "23007";
	public static final String AOI_HD_ROLE_LEAVE = "24007";
	
	public static final String AOI_PETS_ENTER = "23003";
	
	public static final String AOI_BAITAN_ENTER = "23008";
	public static final String AOI_BAITAN_LEAVE = "24008";
	
	public static final String AOI_TAFANG_PAOTAI_ENTER = "23009";
	
	public static final String AOI_COLLECT_ENTER = "23004";
	public static final String AOI_GOODS_ENTER = "23005";
	public static final String AOI_DIAOXIANG_ENTER = "23007";
	public static final String AOI_BIAOCHE_ENTER = "23006";
	public static final String AOI_TRAP_ENTER = "42040";
	public static final String AOI_PARTITE_ENTER = "80201";
	
	public static final String AOI_MONSTERS_LEAVE = "24002";
	public static final String AOI_PETS_LEAVE = "24003";
	public static final String AOI_COLLECT_LEAVE = "24004";
	public static final String AOI_GOODS_LEAVE = "24005";
	public static final String AOI_BIAOCHE_LEAVE = "24006";
	public static final String AOI_DIAOXIANG_LEAVE = "24007";
	public static final String AOI_TRAP_LEAVE = "42042";
	public static final String AOI_CRYSTAL_LEAVE = "80202";
	
	/**
	 * 战斗基本行为
	 * */
	public static final String SKILL_FIRE_REBOUND = "42006";
	public static final String INNER_SKILL_FIRE_REBOUND = "S:42006";
	public static final String INNER_SKILL_FIRE = "s:innser_skill_fire";
	public static final String INNER_SKILL_SINGLE_FIRE = "s:innser_skill_s_fire";
	public static final String SKILL_FS_TELEPORT_FIRE = "42002";//法师瞬移技能回指令
	public static final String SKILL_XIXUE_FIRE = "42003";//玩家释放完技能后的吸血业务
	public static final String ZY_CHANGE = "12010";
	public static final String FIGHT_STATE_START = "40501";
	public static final String FIGHT_STATE_END = "40502";
	public static final String ADD_BUFF = "42030";
	public static final String REMOVE_BUFF = "42031";
	public static final String TRIGGER_BUFF = "42032";
	public static final String INNER_FIGHT_STATE_CHECK = "fight_state_check";
//	public static final String INNER_FIGHT_STATE_CHECK = "S:FIGHT_STATE_CHECK";
	public static final String BATTLE_MODE = "40600";
	
	public static final String INNER_FABAO_SKILL_READY_FIRE = "S:FABAO_READY_FIGHT";
	public static final String INNER_FABAO_SKILL_FIRE = "S:FABAO_FIGHT";
	public static final String INNER_FABAO_SKILL_FIRE_2 = "S:FABAO_FIGHT2";
	public static final String FABAO_SKILL_FIRE = "41001";
	public static final String FABAO_HP_MP_CHANGE = "41011";
	public static final String FABAO_ATTACK_STATE = "41013";
	
	//服务端推送其他客户端或者怪物死亡的信息
	public static final String NO_ROLE_DEAD = "43004";
	
	public static final String FABAO_READY_FIGHT = "41012";
	public static final String FABAO_REVIVE_MONEY = "41005";
	public static final String FABAO_DEAD = "41004";
	public static final String FABAO_REVIVE = "41006";
	public static final String NEW_PLAYER_REVIVE = "43002";

	public static final String INNER_APPLY_CHANGE_MAP = "SC:APPLY_CHANG_MAP";
	
	public static final String INNER_RECOVER_TILI = "S:RECOVER_T";
	
	/**
	 *  请求兼听其他角色魔法变化
	 */
	public static final String QUERY_OTHER_MP = "12200";
	
	/**
	 * buff相关
	 * */
	public static final String INNER_BUFF_END = "BUFF:END";
	public static final String INNER_BUFF_TRIGGER = "BUFF:TRIGGER";
	public static final String INNER_BUFF_ONLINE_CHECK = "BUFF:ONLINE_CHECK";
	
	/**
	 * 剧情BUFF相关
	 */
	public static final String JUNQIN_START = "-600";
	public static final String JUNQIN_END = "-601";
	
	
	public static final String ROLE_ATTRIBUTE = "110000";

	/**
	 * ai相关
	 * */
	public static final String INNER_AI_ADVANCED_1 = "AI:ADVANCED_1";//高级AI
	public static final String INNER_AI_ADVANCED_2 = "AI:ADVANCED_2";//高级AI
	public static final String INNER_AI_ADVANCED_3 = "AI:ADVANCED_3";//高级AI
	/**
	 * 塔防ai相关
	 * */
	public static final String TF_INNER_AI_DEFAULT_HANDLE = "TF_AI:HANDLE";
	public static final String TF_INNER_AI_RETRIEVE = "TF_AI:RETRIEVE";
	public static final String TF_INNER_AI_PRODUCE = "TF_AI:PRODUCE";
	public static final String TF_INNER_AI_DISAPPEAR = "TF_AI:DISAPPEAR";
	
	public static final String TF_INNER_AI_ADVANCED_1 = "TF_AI:ADVANCED_1";//高级AI
	public static final String TF_INNER_AI_ADVANCED_2 = "TF_AI:ADVANCED_2";//高级AI
	public static final String TF_INNER_AI_ADVANCED_3 = "TF_AI:ADVANCED_3";//高级AI

	/**
	 * 场景内聊天相关
	 * */
	public static final String CHAT_NEARBY = "11000";
	public static final String CHAT_PM = "11001";
	public static final String CHAT_ERROR_MSG = "11111";
	
	/**
	 * 世界
	 */
	public static final String WORLD_MSG = "11004";
	
	/**
	 * 怪物AI触发头顶说放
	 */
	public static final String MONSTER_AI_TALK = "11200";
	
	
	/**
	 * 角色基本行为
	 * */
	public static final String BEHAVIOR_ACTION = "40200";
	
	/**
	 * 场景与业务同步相关
	 * */
	public static final String INNER_ADD_SKILL = "S:ADD_SKILL";
	public static final String INNER_XIANFAJINGJIE = "s:total_level";
	public static final String XIANFAJINGJIE = "42406";
	
	public static final String INNER_CHANGE_EQUIP = "S:CHANGE_EQUIP";
	public static final String INNER_ROLE_CHANGE_NAME = "name_change";
	public static final String INNER_ROLE_CHANGE_JOB = "job_change";
	
	public static final String CHANGE_EQUIPS = "31002";
	public static final String TUO_EQUIPS = "31003";
	
	public static final String GOODS_PICKUP = "30103";
	public static final String INNER_ADD_GOODS = "BUS:ADD_GOODS";
	public static final String INNER_STAGE_KUAFU_ADD = "s_add_m";
	
	public static final String ADD_ENEMY_BUS = "BUS:51005";
	public static final String ADD_CHENGJIU_TUISONG = "CHENGJIU:51006";
	
	/**
	 * 官职变化 通知场景
	 */
	public static final String INNER_GUAN_ZHI_CHANGE = "s:66001";
	/**
	 * 官印变化 通知场景
	 */
	public static final String INNER_GUAN_YI_CHANGE = "s:66003";
	
	public static final String TF_INNER_AI_DEAD_EXP_DROP = "TF_DROP:3";//塔防怪物经验掉落
	public static final String INNER_ADD_EXP = "add_exp";
	
	public static final String HP_MP_PROP_CHANGE = "12300";
	public static final String MANY_BEI_EXP = "12301";
	
	
	/**
	 * 塔防副本累计经验变化
	 */
	public static final String TAFANG_ADD_EXP = "84003";
	
	/**
	 * 怪物死亡掉落经验
	 */
	public static final String MONSETER_DEAD_ADD_EXP = "43005";
	
	/**
	 * 43006 服务端单独推送客户被击杀时，爆出了哪些装备
	 */
	public static final String PK_DIAOLUO_GOODS = "43006";
	
	/**
	 * 业务杀怪统计
	 */
	public static final String INNER_KILL_MONSTER = "BUS:43000";
	/**
	 * 杀怪成就推送
	 */
	public static final String MONSTER_CHENGJIU = "CHENGJIU:43001";
	/**
	 * 54205 采集矿之后更新个人采集信息
	 */
	public static final String ROLE_KZ_COLLECT = "54205";
	/**
	 * 54206 服务端推送行会矿已被采集
	 */
	public static final String ROLE_KZ_GUILD_COLLECT = "54206";
	
	/**
	 * 好友模块死亡处理
	 */
	public static final String FRIEND_DEAD_HADNLE = "F:DEAD";
	
	/**
	 * 查看目标属性
	 */
	public static final String INNER_TARGET_ATTRIBUTE = "S:12020";
	public static final String TARGET_ATTRIBUTE = "12020";
	public static final String TARGET_ATTRIBUTE_FAIL = "12022";
	
	public static final String INNER_TARGET_HUOBAN_ATTRIBUTE = "S:12021";
	public static final String TARGET_HUOBAN_ATTRIBUTE = "12021";
	public static final String TARGET_STAGE_INFO = "25001";
	
	/**
	 * 好友模块：通知关注人，角色切换场景
	 * [mapId]
	 * */
	public static final String FRIEND_MAP_CHANGE = "F:51014";
	
	/**
	 * 公会模块：通知在线人员，角色切换场景
	 */
	public static final String GUILD_MAP_CHANGE = "F:53032";
	public static final String GUILD_ROLE_DEAD = "G:ROLE_DEAD";
	public static final String GUILD_ROLE_BEAT = "G:ROLE_BEAT";
	
	/**
	 * 队伍
	 */
	public static final String INNER_TEAM_CHANGE = "s:team_change";
	
	/**
	 * 队伍信息变化
	 */
	public static final String TEAM_INFO_CHANGE = "12007";
	
	public static final String TEAM_HP_MP_CHANGE = "52007";
	public static final String TEAM_LEVEL_UPGRADE = "52008";
	public static final String TEAM_CHANGE_NOTIFY = "52010";
	public static final String TEAM_MEMBER_KILLED = "52013";
	public static final String TEAM_CLOTHES_CHANGE = "52014";
	public static final String TEAM_MAP_CHANGE = "52012";
	public static final String TEAM_CUR_STAGE_MEMBER_INFO = "52009";
	public static final String TEAM_TARGET_POSITION = "52015";
	public static final String INNER_TEAM_TARGET_POSITION = "S:52015";
	public static final String TEAM_STAGE_CHANGE = "P:STAGE_CHANGE";
	
	
	public static final String INNER_STAGE_FIGHTER_CHANGE = "S:fighter_change";
	public static final String INNER_BUS_FIGHTER_CHANGE = "B:fighter_change";
	
	/**
	 * 更改公会
	 */
	public static final String INNER_GUILD_CHANGE = "s:guild_change";
	public static final String GUILD_CHANG = "53104";
	
	
	/**
	 * 物品数量变化
	 */
	public static final String GOODS_CHANGE = "30104";
	
	
	/**
	 * 陷阱相关
	 * **/
	public static final String INNER_AI_TRAP_ACTIVE_HANDLE = "trap:1";
	public static final String INNER_TRAP_DISAPPEAR = "trap:dis";
	
	public static final String EMPLOYEE_ATTRIBUTE = "12010";
	
	/**
	 * 上坐骑
	 */
	public static final String RIDE_ZUOQI = "40400";
	/**
	 * 下坐骑
	 */
	public static final String UNDER_ZUOQI = "40401";
	
	public static final String INNER_ZUOQI_STATE_CHANGE = "zq_state";
	public static final String INNER_ZUOQI_ATTR_CHANGE = "zq_att";
	
	/**
	 * 激活头衔（通知场景激活后永久附加属性）
	 */
	public static final String ACTIVATED_ROLE_TITLE_STATE = "s:activated_add_title";
	
	/**
	 * 启动头衔（通知场景启动后附加属性）
	 */
	public static final String USR_ROLE_TITLE_STATE = "s:usr_add_title";
	
	/**
	 * 取消头衔（通知场景取消启动后的附加属性）
	 */
	public static final String CANCEL_ROLE_TITLE_STATE = "s:cancel_title";
	
	/**
	 * 设置黄钻信息
	 */
	public static final String SET_YELLOW_INFO = "s:set_yellow_info";
	/**
	 * 雕像
	 */
	public static final String INNER_STAGE_DX_CHANGE = "dx_stage_change";
	
	/**
	 * 离开队伍
	 */
	public final static String EXIT_TEAM = "52006";
	
	
	public final static String START_COLLECT = "40410";//请求开始采集
	public final static String COMPLETE_COLLECT = "40411";//请求采集完成
	
	public final static String CHANGE_QSQH = "12008";//推送客户端全身强化配置id
}