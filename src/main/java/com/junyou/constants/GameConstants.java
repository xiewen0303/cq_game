package com.junyou.constants;

import io.netty.util.internal.SystemPropertyUtil;

import com.junyou.bus.email.utils.EmailUtil;

/**
 * 游戏全局常量类
 * 
 * @author DaoZheng Yuan 2014年11月26日 下午3:44:55
 */
public class GameConstants {
	// ----------------------通用-------------------------
	/** 数据库多条件查询数值类型拼接符 */
	public static final String SQL_WHERE_INT_JOIN = ",";
	/** 数据库多条件查询字符串类型拼接符 */
	public static final String SQL_WHERE_STR_JOIN = "','";
	/** 压缩的打包数据(建议150bytes以上数据使用) **/
	public static final int PACK_BYTES = 200;
	/** 一天的时间毫秒数 */
	public static final int DAY_TIME = 24 * 60 * 60 * 1000;
	/** 项目中系统使用的默认roleid */
	public static final Long DEFAULT_ROLE_ID = -10000L;
	public static final String DEFAULT_ROLE_STAGEID = "no stage";
	/** 通用配置1级分割符	"|" */
	public static String CONFIG_SPLIT_CHAR = "\\|";
	/** 通用配置2级分割符	":" */
	public static String CONFIG_SUB_SPLIT_CHAR = ":";
	/** 星期分隔符 */
	public static String CONFIG_WEEK_SPLIT_CHAR = "\\|";
	/** 布尔型true的int值:1 */
	public static int BOOLEAN_TRUE_TO_INT = 1;
	/** 布尔型false的int值:0 */
	public static int BOOLEAN_FALSE_TO_INT = 0;
	/** spring定时容错时间 */
	public static long SPRING_DINGSHI_ERRER_TIME = 20000;
	/** spring定时容错时间（短时间定时容错） */
	public static long SPRING_DINGSHI_ERRER_TIME_SHORT = 500;
	/**伤害排行最大条数**/
	public static int MAX_HURT_RANK = 10;
	/** 禁止他人加我为好友 */
	public static String SYSTEM_SETTING_TYPE_FIREND = "25";
	/** 禁止他人对我发起交易 */
	public static String SYSTEM_SETTING_TYPE_TRADE = "26";
	/** 禁止他人对我私聊 */
	public static String SYSTEM_SETTING_TYPE_CHAT = "27";
	/** 禁止他人邀请我进公会 */
	public static String SYSTEM_SETTING_TYPE_GUILD = "28";
	/** 禁止他人邀请我进队伍 */
	public static String SYSTEM_SETTING_TYPE_TEAM = "29";

	// -----------------------协议解析常量[amf协议中常量设置]--------------------------
	/*** flash安全请求使用的会话key */
	public static final String FLASH_SAFE_KEY = "policy-file-request";
	/*** 未解析的流数据缓存key */
	public static final String REMAIN_BUF_KEY = "remain_buf_key";
	/*** amf解析对象的缓存key */
	public static final String AMF3INPUT_KEY = "amf3input_key";
	/*** amf加密解密对象会话key */
	public static final String AMFENCRYPT_KEY = "amfencrypt_key";
	/*** 特殊的字符 */
	public static final String SERVER_MANAGE_KEY = "8576a59173b5b61f";
	/*** 平台参数传过来的参数 */
	public static final String PLATFORM_PARAM_COMPONENT_NAME = "platform_params";
	/***聚享游玩家第一次创角登陆 */
	public static final String JUXIANGYOU_ROLE_FIRST_LOGIN = "juxiangyou_role_first_login";
	/*** 反外挂常量 */
	public static final String PING_COMPONENT_NAME = "PING";
	/*** hw */
	public static final String LOCAL_XML_HW = "-hw";
	/*** 被其他人挤下线 */
	public static final int JING_OFFLINE = 0;
	/*** 服务端检测到用户加速 */
	public static final int SERVER_JS_OFFLINE = 1;
	/*** GM封号 */
	public static final int GM_OFFLINE = 2;

	// ---------------------AccountType
	// start------------------------------------
	/** 物品购买数量最大上限值：1万 **/
	public static final int GOODS_BUG_MAX_COUNT = 10000;
	/** 元宝 最大值 100亿 **/
	public static final long YB_MAX = 10000000000L;
	/** 数值类型最大值（整型） **/
	public static final int MAX_VALUE = 2000000000;
	// ---------------------JobType------------------------------------
	/** 男主角 */
	public static final byte JOB_BZH = 0;
	/** 女主角 **/
	public static final byte JOB_HQG = 1;
	/** 人皇(轩辕朗) **/
	public static final byte JOB_SQM = 2;
	/** 魔灵(夏紫熏) **/
	public static final byte JOB_ZXQX = 3;
	
	/**男*/
	public static final byte SEX_MAN = 1;
	/**女*/
	public static final byte SEX_WOMAN = 2;

	// ---------------------充值类型------------------------------------------
	/** 玩家正常充值 */
	public static final int PLAYER_RECHARGE = 1;
	/** 内部充值 */
	public static final int INNER_RECHARGE = 2;
	/** 补偿玩家充值 */
	public static final int COMPENSATE_RECHARGE = 3;
	// ------------------------账号类型-----------------------------------------
	/** 不是GM */
	public static final int IS_NOT_GM = 0;
	/** 是GM */
	public static final int IS_GM = 1;
	/** 是指导员 */
	public static final int IS_ZHIDAOYUAN = 2;

	/** 自动创号的玩家 */
	public static final int AUTO_CRAETE_ROLE = 1;
	/** 正常玩家（非自动创号的玩家） */
	public static final int NO_AUTO_CRAETE_ROLE = 0;

	/** 普通玩家 */
	public static final int USER_TYPE_PLAYER = 0;
	/** 托 */
	public static final int USER_TYPE_TUO = 1;
	/** 君游内部人员 */
	public static final int USER_TYPE_JUNYOU_INNER = 2;

	// -------------------------stage-------------------------------------------
	/** 微端常量 */
	public static String WEI_DUAN_VALUE = "weiDuan";

	/** 玩家的阵营为正义（2） **/
	public static final int ROLE_CAMP = 2;

	/** 场景分线数据的缓存主键 **/
	public static final String DATA_CONTAINER_STAGELINE = "stageline";
	/** GM隐身标记 **/
	public static final String COMPONENT_GM_HANLE_YS = "gm_ys";
	/** BusShare标记 **/
	public static final String COMPONENT_BUS_SHARE = "bus_share";
	/** 提供给背包用的锁 **/
	public static final String COMPONENT_BAG_SHARE = "bag_share";
	/** 掉落物品 **/
	public static final String COMPONENT_DROP_GOODS = "drop_goods";
	/** 角色心跳 **/
	public static final String COMPONENT_ROLE_XT = "role_xt";
	/** 角色场景心跳间隔(秒) **/
	public static final int COMPONENT_ROLE_XT_SECOND = 5;
	/** 单休 **/
	public static final String COMPONENT_DAZUO = "dazuo";
	/** 单休转双休 **/
	public static final String COMPONENT_DAZUO_SX = "dazuo_sx";
	/** 跳闪值 **/
	public static final String COMPONENT_JUMP_VAL = "jump_val";
	/** 跳闪状态 **/
	public static final String COMPONENT_JUMP_STAGE = "jump_stage";
	/** 默认 DAO USER_ID KEY **/
	public static final String DEFAULT_IDENTITY_KEY = "cacheAsynLoadKey";
	/** 提供DAO基类标识 **/
	public static final String COMPONENT_DAO_ASYN_LOCK = "aylk";
	/** 出生地图类型 */
	public static final int FIRST_MAP_CONFIG_TYPE = 0;
	/** 公共CD文件 */
	public static final String PUBLIC_CD_MANAGER_FILE_NAME = "public_cd_manager";
	/** 角色死亡强制回城复活 **/
	public static final String COMPONENT_BACK_FUHUO = "role_b_fuhuo";
	/** 场景切换，发送100指令是否清除buff，true:清除 **/
	public static final boolean IS_CLEAR_BUFF = true;
	/** 场景增容最大值 */
	public static final int STAGE_MAX_LOAD = 400;
	/**actity标记 **/
	public static final String COMPONENT_ACTITY_SHARE = "actity_share";
	// --------------------------stage ai---------------------------------
	public static final String COMPONENT_AI_HEARTBEAT = "ai_heartbeat";
	/** 回收 **/
	public static final String COMPONENT_AI_RETRIEVE = "ai_retrieve";
	/** ai生产 **/
	public final static String COMPONENT_PRODUCE_TEAM = "produce_team";
	/** ai自动巡逻 **/
	public final static String COMPONENT_AUTO_TEAM_XUNLUO = "auto_team_xunluo";
	/** ai自动巡逻时间（单位：秒） **/
	public final static int AUTO_TEAM_XUNLUO_SECOND = 3;
	/** 怪物连续巡逻次数常量 **/
	public final static int MONSTER_LX_XL_TIMES = 3;
	/*** 移动最大验证格子数 */
	public static final int MOVE_CHECK_MAX_GZ = 7;
	/*** 跳跃最大验证格子数 */
	public static final int JUMP_CHECK_MAX_GZ = 10;
	/** 场景战斗常量 **/
	public final static String COMPONENT_FIGHT_STATE = "fight_state";

	// ---------------------玩家Role code--------------------------------------
	public static final String COMPONENET_CHENMI = "chenmi";
	/** 非GM玩家 **/
	public final static int NOT_IS_GM = 0;
	/** 非Cm玩家 **/
	public final static int NOT_IS_CM = 0;
	/** 非Del玩家 **/
	public final static int NOT_IS_DEL = 0;
	/** Del玩家 **/
	public final static int IS_DEL = 1;

	/** 非角色最多同时存在角色个数玩家 **/
	public final static int ROLE_COUNT_SIZE = 6;
	/** 所有角色最大角色个数 **/
	public final static int ROLE_All_MAX_SIZE = 20;

	/** 名字长度最少是1个字节 **/
	public final static int ROLE_NAME_MIN_SIZE = 1;
	/** 名字长度最大是20个字节 **/
	public final static int ROLE_NAME_MAX_SIZE = 20;
	/** 名字长度最大是12个字符 **/
	public final static int YUENAN_ROLE_NAME_MAX_SIZE = 12;

	/** 不沉迷 **/
	public final static int NOT_IS_FANGCHENMI = 0;
	/** 沉迷 **/
	public final static int IS_FANGCHENMI = 1;

	/** 游戏中沉迷状态-完全沉迷 **/
	public final static int CHENMI_STATUS = 0;
	/** 游戏中沉迷状态-正常 **/
	public final static int CHENMI_STATUS_ACTIVE = 1;
	/** 游戏中沉迷状态-半沉迷 **/
	public final static int CHENMI_STATUS_HALF = 2;

	/** 沉迷时间(5小时) **/
	public final static int CHENMI_TIME = 18000000;
	//public final static int CHENMI_TIME = 10*60*1000;
	/** 半沉迷时间(3小时) **/
	public final static int HALF_CHENMI_TIME = 10800000;
	//public final static int HALF_CHENMI_TIME = 5*60*1000;
	/** 角色信息模块 */
	public static final String COMPONENET_ROLE_INFO = "roleInfo";

	/** 角色属性模块 */
	public static final String COMPONENET_ROLE_ATTRIBUTE = "roleAttribute_type";
	// /**角色属性模块*/
	// public static final String COMPONENET_ROLE_INFO_ATTRIBUTE =
	// "roleAttribute";
	/** 角色死亡强制回城复活时间 **/
	public final static int ROLE_QZ_FUHUO_SECOND = 35;

	// -----------------------------事件源常量--start----------------------------------------------
	/** 通用事件源类型 **/
	public static final byte[] TONGYONG_EVENT_SOURCE = new byte[0];

	// -----------------------------消息分发常量--start-----------------
	public final static long SEND_ALL_GUID = -10000000000l;

	// --------------------http常量------------------------
	/** http getServerInfoByType = 1 查询服务器在线人数 **/
	public final static int HTTP_SERVER_DATA_TYPE_1 = 1;
	/** http getServerInfoByType = 2 查询服务器版本号 **/
	public final static int HTTP_SERVER_DATA_TYPE_2 = 2;
	/** http getServerInfoByType = 3 查询服务器是否是跨服服务器 **/
	public final static int HTTP_SERVER_DATA_TYPE_3 = 3;
	/** http getServerInfoByType = 4 查询服务器时间 **/
	public final static int HTTP_SERVER_DATA_TYPE_4 = 4;
	/** http getServerInfoByType = 5 查询服务器停机次数 **/
	public final static int HTTP_SERVER_DATA_TYPE_5 = 5;
	/** http getServerInfoByType = 6 查询服务器启动时的时间 **/
	public final static int HTTP_SERVER_DATA_TYPE_6 = 6;
	/** http getServerInfoByType = 7 查询跨服状态 **/
	public final static int HTTP_SERVER_DATA_TYPE_7 = 7;
	/** http getServerInfoByType = 8 查询游戏内存中的允许的最高在线人数 **/
	public final static int HTTP_SERVER_DATA_TYPE_8 = 8;

	// --------------------------任务----------------------
	/** 对话类任务 */
	public final static int TASK_TYPE_TALK = 0;
	/** 杀怪类任务 */
	public final static int TASK_TYPE_KILL = 1;
	/** 采集类任务 */
	public final static int TASK_TYPE_CAIJI = 2;
	/** 前端杀怪类任务 */
	public final static int TASK_TYPE_CLIENT_KILL = 3;
	/** 完成其他任务类任务 */
	public final static int TASK_TYPE_TASK = 4;

	/** 未接 */
	public final static int TASK_WJ_STATE = -1;
	/** 已接未完成 */
	public final static int TASK_RECEIVE_STATE = 0;
	/** 已完成未交付 */
	public final static int TASK_COMPLETE = 1;
	/** 已交付 */
	public final static int TASK_PAY = 2;

	/** 主线任务奖励发放邮件内容 */
	public static final String TASK_EMAIL_CONTENT_CODE = "2510";

	/** 押镖被劫通知：您的美女被{0}玩家劫取，您只获得{1}的奖励 */
	public static final String EMAIL_YABIAO_JIEBIAO_CODE = "2514";
	public static final String EMAIL_YABIAO_JIEBIAO_CODE_TITLE = "2523";

	// ---------------------终身一次数据的位置含义----------------
	/** 角色是否是充值过 */
	public final static int ROLE_HAS_RECHARGE = 0;
	/** 是否领取微端登录奖励 **/
	public final static int RECIVE_WEIDUAN = 1;
	/** 表示玩家是否改过名 **/
	public final static int HAS_MODIFY_NAME = 2;
	/** 是否领取微端2登录奖励 **/
	public final static int RECIVE_WEIDUAN2 = 3;

	// ---------------------邮件---------------------
	/** 邮件已删除 */
	public final static int EMAIL_IS_DEL = 1;
	/** 邮件未删除 */
	public final static int EMAIL_NOT_DEL = 0;
	/** 邮件仓库 */
	public final static int EMAIL_BAK = 2;
	/** 公会仓库邮件 */
	public final static int EMAIL_TYPE_GUILD = -1;
	/** 个人邮件 */
	public final static int EMAIL_TYPE_SINGLE = 0;
	/** 全服邮件 */
	public final static int EMAIL_TYPE_ALL = 1;
	/** 等级邮件 */
	public final static int EMAIL_TYPE_LEVEL = 2;
	/** vip等级邮件 */
	public final static int EMAIL_TYPE_VIP = 3;
//	/** 普通邮件过期时间 */
//	public static final long EMAIL_EXPIRE_TIME = 7l * 24 * 60 * 60 * 1000;
//	/** 带附件的邮件过期时间 */
//	public static final long EMAIL_FUJIAN_EXPIRE_TIME = 30l * 24 * 60 * 60 * 1000;
	/** roleId分隔符 */
	public static final String ROLEID_SPLIT = ",";
	/** code参数连接符 */
	public static final String CODE_JOIN = "ü";
	/** 附件一级分隔符（物品之间） */
	public static final String FUJIAN_SPLIT_ONE = ",";
	/** 附件二级分隔符（id、数量之间） */
	public static final String FUJIAN_SPLIT_TWO = ":";
	/** 最大附件数 */
	public static final int EMAIL_MAX_ITEMS_COUNT = 12;
//	/** 最大邮件数 */
//	public static final int EMAIL_MAX_COUNT = 999;
	/** 后台邮件标题标识 */
	public static final String EMAIL_GMTOOL_TITLE = "systemGm";
	/** 后台邮件锁定的小时数 */
	public static final int EMAIL_GMTOOL_LOCK_HOUR = 24;
	/** PPS-后台邮件锁定的小时数 */
	public static final int EMAIL_GMTOOL_LOCK_HOUR_PPS = 1;
	/** 邮件状态：未读 */
	public final static int EMAIL_STATUS_WEIDU = 1;
	/** 邮件状态：已读 */
	public final static int EMAIL_STATUS_YIDU = 2;
	/** 邮件状态：已领取 */
	public final static int EMAIL_STATUS_YILINGQU = 3;
	/** 后台全服邮件标识 */
	public static final String EMAIL_GMTOOL_STATUS= "gmtools";
	// -----------------------跨服------------------
	/** IP 的 key */
	public static final String IP_KEY = "IP";
	/** 连接组SESSION组的 ID */
	public static final String SESSION_GROUP_ID = "sgid";
	/** session中的roleid */
	public static final String SESSION_KEY_ROLE = "skrp";
	/** webservice的远程地址 */
	public static final String WS_REMOTE_URL = "ws_remote_url";
	/** 源服务器和跨服服务器通讯源服务器的webservice端口的Key */
	public static final String REMOTE_PORT = "RP";
	/** 跨服协议 */
	public static final String PROTOCOL_FST = "fst";
	public static final String PROTOCOL_JAVA = "java";
	/** 跨服场景数据 */
	public static final String COMPONENET_KUAFU_DATA = "KUAFU_STAGE_DATA";
	/** 跨服角色场景数据的缓存主键 **/
	public static final String DATA_CONTAINER_ROLESTAGE = "ROLE_STAGE";
	/** 跨服角色数据的缓存主键 **/
	public static final String DATA_CONTAINER_ROLE = "KUAFU_ROLE";
	// ----------------------公会----------------------
	/*** 公会聊天常量 ***/
	public static final String GUILD_CHAT_COMPONENT_NAME = "GUILD_CHAT";
	/*** 公会成员上线常量 ***/
	public static final String GUILD_CHAT_ROLE_GUILD_MEMBER_MAP = "ROLE_GUILD_MEMBER_MAP";
	/** 会长等级职位 */
	public static final int GUILD_LEADER_POSTION = 1;
	/** 副会等级职位 */
	public static final int GUILD_VIC_LEADER_POSTION = 2;
	/**长老等级职位 */
	public static final int GUILD_ZHANGLAO_LEADER_POSTION = 3;
	/** 成员等级职位 */
	public static final int GUILD_MEMBER_POSTION = 4;
	/** 公会名最短字节数 */
	public static final int GUILD_NAME_MIN_LEN = 3;// 单位字符
	/** 公会名最长字节数 */
	public static final int GUILD_NAME_MAX_LEN = 20;// 单位字符
	/** 公会公告最长字节数 */
	public static final int GUILD_NOTICE_MAX_LEN = 120;// 单位字
	/** 公会最大日志条数 */
	public static final int MAX_LOG = 220;
	/** 公会日志文件 */
	public static final String LOG_FILE_COMPONENET_NAME = "guildLog";
	/** 创建公会 */
	public static final int GUILD_LOG_CREATE_GUILD = 1;
	/** 加入公会 */
	public static final int GUILD_LOG_ENTER_GUILD = 2;
	/** 退出公会 */
	public static final int GUILD_LOG_EXIT_GUILD = 3;
	/** 职业变更 */
	public static final int GUILD_LOG_CHANGE_POSITION = 4;
	/** 转让会长 */
	public static final int GUILD_LOG_ZHUANRANG_LEADER = 5;
	/** 踢出公会 */
	public static final int GUILD_LOG_LET_LEAVE_GUILD = 6;
	/** 公会升级 */
	public static final int GUILD_LOG_GUILD_LEVEL_UP = 7;
	/** 公会捐献 */
	public static final int GUILD_LOG_GUILD_JUANXIAN = 8;
	/** 默认招收条件类型 */
	public static final int GUILD_ZHAOSHOU_TYPE_DEFAULT = 1;
	/** 等级条件 */
	public static final int GUILD_ZHAOSHOU_LEVEL = 1;
	/** vip条件 */
	public static final int GUILD_ZHAOSHOU_VIP = 2;
	/** 需审批 */
	public static final int GUILD_ZHAOSHOU_SHENPI = 3;
	/** 捐献类型：青霜令 */
	public static final int GUILD_JUANXIAN_TYPE_ITEM1 = 0;
	/** 捐献类型：紫电令 */
	public static final int GUILD_JUANXIAN_TYPE_ITEM2 = 1;
	/** 捐献类型：玉露令 */
	public static final int GUILD_JUANXIAN_TYPE_ITEM3 = 2;
	/** 捐献类型：金风令 */
	public static final int GUILD_JUANXIAN_TYPE_ITEM4 = 3;
	/** 捐献类型：银两 */
	public static final int GUILD_JUANXIAN_TYPE_YINLIANG = 4;
	/** 捐献类型：绑元 */
	public static final int GUILD_JUANXIAN_TYPE_BGOLD = 5;
	/** 捐献类型：元宝 */
	public static final int GUILD_JUANXIAN_TYPE_GOLD = 6;
	/** 门派升级公告：恭喜门主{0}把门派旗帜提升到{1}级，一跃成为{1}级门派！真是可喜可贺 */
	public static final int GUILD_LEVEL_UP_NOTICE = 1440;
	/** 阁楼升级公告：门主{0}把门派阁楼提升到{1}级，可以学习更高级技能！ */
	public static final int GUILD_GELOU_UP_NOTICE = 1441;
	/** 捐献公告：感谢门派成员:{0} 为门派捐献了[{1}],数量:{2},获得门贡点：{3} */
	public static final int GUILD_JUANXIAN_NOTICE = 1442;

	// ---------------------队伍--------------------------
	/** 自动同意邀请 */
	public static final int TEAM_AUTO_YAOQING = 1;
	/** 自动同意申请 */
	public static final int TEAM_AUTO_APPLY = 2;

	// -----------------聊天----------------------------------
	/** 聊天字符长度太长 */
	public static final int CHAT_MSG_LEN = 100;
	/** 聊天的公共CD时间 */
	public static final int CHAT_PUBLIC_CD = 2;
	/** 聊天的公共数据字符串 */
	public static final String CHAT_PUBLIC_STR = "p_public_chat_str";
	/** 聊天的非公共数据字符串 */
	public static final String CHAT_NO_PUBLIC_STR = "p_chat_str";
	/** 聊天的容错次数字符串 */
	public static final String CHAT_PUBLIC_TIMES = "p_chat_times";
	/** 非法聊天禁言时间（5分钟） */
	public static final int CHAT_SHUT_UP_FIRST = 5;
	/** 非法聊天禁言时间（1小时） */
	public static final int CHAT_SHUT_UP_SECOND = 60;

	// -------------------封禁------------------------------
	/** 禁言 */
	public static final int JINYAN = 1;
	/** 不禁言 */
	public static final int JINYAN_NO = 0;
	/** 禁言 */
	public static final boolean JINYAN_OUT = true;
	/** 不禁言 */
	public static final boolean JINYAN_NO_OUT = false;
	/** 封号 */
	public static final int FREEZE = 1;
	/** 不封号 */
	public static final int FREEZE_NO = 0;
	/** 禁言类型 */
	public static final int JINFENG_TYPE_JIN = 1;
	/** 解禁类型 */
	public static final int JINFENG_TYPE_JIEJIN = 2;
	/** 封号类型 */
	public static final int JINFENG_TYPE_FENG = 3;
	/** 解封类型 */
	public static final int JINFENG_TYPE_JIEFENG = 4;
	/** 封IP类型 */
	public static final int JINFENG_TYPE_FENGIP = 5;

	// ----------------------物品--------------------------
	/** 物品配置1级分割符 */
	public static String GOODS_CONFIG_SPLIT_CHAR = "\\|";
	/** 物品配置2级分割符 */
	public static String GOODS_CONFIG_SUB_SPLIT_CHAR = ":";
	/** 物品配置Id 喇叭 */
	public static String GOODS_LABA = "laba";
	/** 有效物品 */
	public static final int GOODS_NO_DELETE = 0;
	/** 异常物品 */
	public static final int GOODS_DELETE = 1;
	/** 后台删除物品 */
	public static final int GOODS_GMTOOLS_DELETE = 2;
	/** 提品通常装备类型 */
	public static final int EQUIP_TIPIN_TYPE_NOMAL = 1;
	/** 提品套装类型 */
	public static final int EQUIP_TIPIN_TYPE_TAOZHUANG = 2;
	/** 附属装备提品类型 */
	public static final int EQUIP_TIPIN_TYPE_FUSHU = 3;
	/** 宠物装备提品类型 */
	public static final int EQUIP_TIPIN_TYPE_CHONGWU = 4;
	// ---------------------PK---------------------------
	/** 玩家灰红名 **/
	public static final String COMPONENET_HM = "hongming";
	/** 玩家灰名 **/
	public static final String COMPONENET_HUIM = "huiming";
	/** 玩家灰名消失 **/
	public static final String HUIMING_XIAOSHI = "huiming_xiaoshi";

	// ---------------------技能-------------------------
	/** 主动技能类型 */
	public static int SKILL_TYPE_ZHUDONG = 0;
	/** 被动技能类型 */
	public static int SKILL_TYPE_BEIDONG = 1;
	/** 神兵心法 */
	public static int SKILL_TYPE_XINFA = 2;
	/** 帮派技能 */
	public static int SKILL_TYPE_GUILD = 3;
	/** 妖神技能 */
	public static int SKILL_TYPE_YAOSHEN = 4;
	/** 糖宝技能*/
	public static int SKILL_TYPE_TANGBAO = 5;
	/** 被动技能属性最大条数 */
	public static int BEIDONG_PRO_COUNT = 7;
	/** 技能熟练度上限 */
	public static int SKILL_SHULIAN_MAX = 2100000000;
	/** 怒气BUFFid */
	public static final String NUQI_BUFF_ID = "nuqi_buff";
	/** 怒气BUFFid */
	public static final Long NUQI_BUFF_GUID = -100l;
	/** 怒气定时id */
	public static final String COMPONENT_NUQI_SCHEDULE = "nuqi_schedule";
	/** 通用技能类型 */
	public static final int SKILL_JOB_TYPE_TONGYONG = 5;

	// ----------------------好友--------------------------
	public static final int F_FRIEND = 0; // 好友
	public static final int F_ZUI = 1;// 最近联系人
	public static final int F_COU = 2;// 仇人
	public static final int F_HEI = 3;// 黑名单
	public static final int F_SELECT = 4;// 查询

	public static final String F_SPLIT_CHAR = ",";// 仇人分隔符等

	// 不回复
	public static final int F_NO_HUIFU = -1;// 不回复

	/**
	 * 六种类型的自动回复，见公共配置报
	 */
	public static final int F_HUIFU_1 = 1;
	public static final int F_HUIFU_2 = 2;
	public static final int F_HUIFU_3 = 3;
	public static final int F_HUIFU_4 = 4;
	public static final int F_HUIFU_5 = 5;
	public static final int F_HUIFU_6 = 6;

	// -------------------trade----------------------
	/** 交易元宝锁对象 **/
	public static final byte[] TRADE_YB_LOCK = new byte[0];
	/** 交易模块名称 **/
	public static final String COMPONENET_TREAD_NAME = "trade";
	// /**发起交易时,锁定被交易者(确认被交易者不能接受多个人的交易)**/
	// public static final String COMPONENET_TREAD_TARGET = "trade_target";
	/** 交易开始 **/
	public static final int START_TRADE = 1;
	/** 交易确认1 **/
	public static final int CONFIRM_TRADE = 2;
	/** 知道双方都确认了 **/
	public static final int KNOW_OTHER_CONFIRM_TRADE = 3;
	/** 回馈了双方都确认 **/
	public static final int FREED_BACK_TRADE = 4;
	/** 交易日志类型 **/
	public static final int TRADE_TYPE_LOG = 1;
	/** 交易最大格位数 **/
	public static final int TRADE_SLOT_MAX_COUNT = 6;
	/** 场景同步标示 **/
	public static final String COMPONENT_STAGE_ASYN_LOCK = "stage_syn";

	// ---------------------副本---------------------------

	/** 进入副本状态 **/
	public static final int ENTER_STATE = -1;
	/** 默认状态（退出状态、未领取状态） **/
	public static final int EXIT_STATE = 0;
	/** 可领取奖励（完成了任务，未领取奖励） **/
	public static final int RECEIVE_STATE = 1;
	/** 已领取奖励 **/
	public static final int COMPLETE_STATE = 2;
	/** 副本检测 **/
	public static final String COMPONENT_FUBEN_EXPIRE_CHECK = "fuben_expire_check";
	/** 强制退出 **/
	public static final String COMPONENT_FUBEN_FORCED_LEAVE = "fuben_forced_leave";
	/** 副本扫荡CD */
	// public static final int FUBEN_SAODANG_CD = 120000;
	/** 一键扫荡VIP等级 */
	public static final int FUBEN_SAODANG_VIP = 3;
	/** 瞬间扫荡VIP等级 */
	public static final int FUBEN_SAODANG_SHUNJIAN_VIP = 5;
	/** 准备状态 */
	public static final int FUBEN_STATE_READY = 0;
	/** 战斗中 */
	public static final int FUBEN_STATE_FIGHT = 1;
	/** 副本完成 */
	public static final int FUBEN_STATE_FINISH = 2;
	/** 扫荡中 */
	public static final int FUBEN_STATE_SAODANG = 3;
	/** 单人副本扫荡邮件奖励内容 */
	public static final String FUBEN_SAODANG_EMAIL_CONTENT_CODE = "2707";
	/** 单人副本邮件奖励内容 */
	public static final String FUBEN_EMAIL_CONTENT_CODE = "2708";
	/** 单人副本邮件奖励标题 */
	public static final String FUBEN_EMAIL_CONTENT_CODE_TITLE = "2798";
	/** 副本延迟清场时间，单位毫秒 */
	public static final int EXPIRE_CHECK_INTERVAL = 60000;
	/** 副本扫荡 **/
	public static final String COMPONENT_FUBEN_SAODANG = "fuben_saodang";
	/** 扫荡副本id分隔符 */
	public static final String SAODANG_FUBENIDS_SPLIT = ",";
	/** 每份状态最大记录数 */
	public static final int PER_STATE_MAX = 31;
	/** VIP副本 */
	public static final int FUBEN_TYPE_VIP = 1;
	/** 日常副本 */
	public static final int FUBEN_TYPE_DAY = 2;
	/** 多人副本 */
	public static final int FUBEN_TYPE_MORE = 3;
	/** 守护副本 */
	public static final int FUBEN_TYPE_SHOUHU = 4;
	/** 爬塔副本 */
	public static final int FUBEN_TYPE_PATA = 5;
	/** 八卦副本 */
	public static final int FUBEN_TYPE_BAGUA = 6;
	/** 埋骨之地副本 */
	public static final int FUBEN_TYPE_MAIGU = 7;
	/**门派炼狱boss */
	public static final int FUBEN_GUILD_LIANYU = 8;
	/** 爬塔层信息文件 */
	public static final String PATA_INFO_FILE_NAME = "pata_ceng_info";
	/** 爬塔首次通关公告 */
	public static final String PATA_FIRST_GONGGAO = "2919";
	/** 爬塔最快通关公告 */
	public static final String PATA_FAST_GONGGAO = "2920";
	/** 多人副本邀请间隔(毫秒) */
	public static final long MORE_FUBEN_YAOQING_INTERVAL = 60000;
    /** 五行副本 */
    public static final int FUBEN_TYPE_WUXING = 9;
    /** 五行副本邮件奖励内容 */
    public static final String WUXING_FUBEN_EMAIL_CONTENT_CODE = "7006";
    /** 五行副本邮件奖励标题 */
    public static final String WUXING_FUBEN_EMAIL_TITLE_CODE = "7071";
    /** 五行技能副本 */
    public static final int FUBEN_TYPE_WUXING_SKILL = 10;
    /** 五行试炼副本 */
    public static final int FUBEN_TYPE_WUXING_SHILIAN = 11;

	// ---------------------排行榜-------------------------------
	/** 等级 */
	public static final int LEVEL_TYPE = 1;
	/** 战力 */
	public static final int ZL_TYPE = 2;
	/** 坐骑 */
	public static final int ZQ_TYPE = 3;
	/** 翅膀 */
	public static final int CB_TYPE = 4;
	/** 糖宝仙剑 */
	public static final int TB_XJ_TYPE = 5;
	/** 糖宝战甲 */
	public static final int TB_ZJ_TYPE = 6;
	/**妖神*/
	public static final int YS_TYPE = 7;
	/**妖神魔纹*/
	public static final int YS_MOWEN_TYPE =8;
	/**器灵*/
	public static final int QILING_TYPE =9;
	/**妖神魂魄*/
	public static final int YS_HUNPO_TYPE = 10;
	/**妖神魔印*/
	public static final int YS_MOYIN_TYPE = 11;
	/**糖宝心纹*/
	public static final int TANGBAO_XINWEN_TYPE = 12;
	/** 新圣器 */
	public static final int WQ_TYPE = 13;
	
	/** 拉取排行榜的最大人数 */
	public static final int MAX_RANK_SIZE = 100;
//	/** 排行榜每页显示人数 */
//	public static final int MEIYE_RENSHU = 13;

	// ---------------------日常任务-------------------------------
	/** 日常任务 */
	public static final int TASK_DAY = 1;
	/** 公会任务 */
	public static final int GUILD_DAY = 2;
	/** 日常BOSS任务 */
	public static final int BOSS_DAY = 3;
	/** 日常任务奖励 */
	public static final String DAY_TASK_AWARD = "3033";
	/** 公会日常任务奖励 */
	public static final String DAY_GUILD_AWARD = "3034";
	/** BOSS日常任务奖励 */
	public static final String DAY_BOSS_AWARD = "3046";
	/** 任务奖励 */
	public static final String DAY_TASK_EMAIL_TITLE = "3052";
	/** 今日任务并未全部完成 **/
	public static final int TASK_STATUS_NO = 0;
	/** 今日任务已经全部完成 **/
	public static final int TASK_STATUS_YES = 1;

	// --------------------------竞技场--------------------------
	/** 最大战斗时间 */
	public static final long MAX_FIGHT_TIME = 120000;
	/** 战报一级分隔符 */
	public static final String RESPORT_SPLIT_ONE = "|";
	/** 战报二级分隔符 */
	public static final String RESPORT_SPLIT_TWO = ",";
	/** 竞技属性文件 */
	public static final String JINGJI_ATTRIBUTE_FILE_NAME = "jingjiAtt";
	/** 竞技属性2代文件 */
	public static final String JINGJI_ATTRIBUTE_FILE_NAME_TWO = "jingjiAtt2";
	/** 竞技战报文件 */
	public static final String JINGJI_REPORT_FILE_NAME = "jingjiReport";
	/** 竞技正常状态 */
	public static final int JINGJI_STATE_NORMAL = 0;
	/** 竞技战斗中 */
	public static final int JINGJI_STATE_FIGHT = 1;
	/** 竞技奖励已领取过 */
	public static final int JINGJI_GIFT_RECIVED = 1;
	/** 竞技可以领取 */
	public static final int JINGJI_GIFT_HAVE = 0;
	/** 竞技结果胜利 */
	public static final int JINGJI_ANSWER_WIN = 1;
	/** 竞技结果失败 */
	public static final int JINGJI_ANSWER_LOSE = 2;
	/** 竞技战报最大条数 */
	public static final int JINGJI_REPORT_MAX = 20;
	/** 竞技场战斗状态容错时间 */
	public static final int FIGHT_TIME_OUT = 5000;
	/** 竞技场2-10名公告 */
	public static final int JINGJI_TOP_10_NOTICE = 3280;
	/** 竞技场第一名公告 */
	public static final int JINGJI_FIRST_NOTICE = 3281;

	// ---------------------打坐-------------------------------
	/** 打坐获得奖励的cd */
	public static final int DAZUO_AWARD_CD = 5;
	/** 打坐获得奖励的cd */
	public static final int DAZUO_2_SX_CD = 30;
	/** 跳闪最大值 */
	public static final int TIAOSHAN_MAX_VAL = 5;
	/** 跳闪恢复时间 */
	public static final int JUMP_VAL_CD = 10;
	/** 跳闪时间（毫秒） */
	public static final int JUMP_TIME = 800;

	// -----------------------------VIP--------------------------
	/** VIP模块 */
	public static final String COMPONENET_VIP = "vip";
	/** 特权-副本一键扫荡 */
	public static final String VIP_FUBEN_ALL_SAODANG = "jqfuben1";
	/** 特权-副本CD时长 */
	public static final String VIP_FUBEN_CD_TIME = "jqfuben2";
	/** 特权-守护副本元宝重置次数 */
	public static final String VIP_FUBEN_SHOUHU_BUY = "swcs";
	/** 特权-创建门派 */
	public static final String VIP_GUILD_CREATE = "menpai";
	/** 特权-补签次数 */
	public static final String VIP_ASSIGN_BUQIAN = "buqian";
	/** 特权-两倍离线经验次数 */
	public static final String VIP_OFFLINE_LIXIAN2 = "lixian2";
	/** 特权-三倍离线经验次数 */
	public static final String VIP_OFFLINE_LIXIAN3 = "lixian3";
	/** 特权-无限飞鞋 */
	public static final String VIP_FEIXIE = "feixie";
	/** 特权-打坐经验加成 */
	public static final String VIP_DAZUO_EXP = "dazuo";
	/** 特权-爬塔购买次数 */
	public static final String VIP_PATA_BUY_COUNT = "ptcs";
	/** 特权-塔防经验倍率 */
	public static final String VIP_TAFANG_EXP_RATE = "jiangbs";
    /** 特权-五行副本购买次数 */
    public static final String VIP_WX_FUBEN_BUY_COUNT = "wxcs";
    /** 特权-心魔斗场副本购买次数 */
    public static final String VIP_XM_DOUCHANG_FUBEN_BUY_COUNT = "dccs";

	// -----------------------------市场--------------------------
	/** 时间排序 */
	public static final int PAIMAI_TIMESORT = 0;
	/** 单价上升排序 */
	public static final int PAIMAI_SELL_PRICE_UP = 1;
	/** 单价下降排序 */
	public static final int PAIMAI_SELL_PRICE_DOWN = 2;
	/** 总价上升排序 */
	public static final int PAIMAI_PRICE_UP = 3;
	/** 总价下降排序 */
	public static final int PAIMAI_PRICE_DOWN = 4;
	/** 吆喝时间CD */
	public static final int YH_CD = 30 * 1000;
	/** 市场邮件奖励内容 */
	public static final String PAIMAI_CONTENT_CODE = "3508";
	/** 市场邮件奖励标题 */
	public static final String PAIMAI_CONTENT_CODE_TITLE = "3517";
	// -----------------------------坐骑--------------------------
	/** 上坐骑 */
	public static final int ZQ_UP = 1;
	/** 下坐骑 */
	public static final int ZQ_DOWN = 0;

	// -------------------------福利大厅相关----------------------------
	/** 领取离线奖励消耗元宝数 */
	public static final int OFFLINE_GOLD_CONSUME = 50;
	/** 在线奖励ID上线值 */
	public static final int ROLE_ONLINE_VALUE = 4;
	/** 离线时间达到10分钟可以领取 */
	public static final int OFFLINE_EXP_LESS_TIME = 10;
	/** 离线经验最多达到12小时 */
	public static final int OFFLINE_EXP_TIME_LIMIT = 12;
	/** 日常任务 */
	public static final String RESOURCE_TYPE_DAY_TASK = "1";
	/** 门派任务 */
	public static final String RESOURCE_TYPE_GUILD_TASK = "2";
	/** 守护副本 */
	public static final String RESOURCE_TYPE_SHOUHU_FUBEN = "3";
	/** 日常副本 */
	public static final String RESOURCE_TYPE_DAY_FUBEN = "4";
	/** 答题 */
	public static final String RESOURCE_TYPE_DATI_ACTIVE = "5";
	/** 押镖 */
	public static final String RESOURCE_TYPE_YABIAO_ACTIVE = "6";

	/** 资源最大找回天数 */
	public static final int RESOURCE_BACK_MAX_DAY = 3;
	/** 镖车资源找回的镖车id */
	public static final int RESOURCE_BACK_BIAOCHE_ID = 3;
	/** 资源找回消耗银两 */
	public static final int RESOURCE_BACK_COST_TYPE_YINLIANG = 0;
	/** 资源找回消耗元宝 */
	public static final int RESOURCE_BACK_COST_TYPE_YUANBAO = 1;

	// -------------------------BUFF----------------------------------
	public static final String COMPONENT_BUFF_END = "buff_end";
	public static final String COMPONENT_BUFF_TRIGGER = "buff_trigger";

	// -------------------------土城高经验区----------------------------------
	public static final String COMPONENT_TUCHEN_EXP = "role_tuchen_exp";

	// -------------------------定时加血回蓝----------------------------------
	public static final String COMPONENT_HP = "role_hp_revert";

	// -------------------------怒气----------------------------------
	/** 怒气增加 */
	public static final String COMPONENT_TIME_NUQI = "role_time_nuqi";
	/** 怒气增加时间 */
	public static final int NUQI_ADD_DELAY = 5000;

	// -------------------------领地战----------------------------------------
	/** 模块中文名称 */
	public static final String COMPONENT_TERRITORY_NAME = "领地战";
	/** 领地战加经验真气定时 */
	public static final String COMPONENT_TERRITORY_EXP = "role_territory";
	/** 领地战产生结果 */
	public static final String COMPONENT_TERRITORY_HAS_WINNER = "territory_winner";
	/** 领地战结束 */
	public static final String COMPONENT_TERRITORY_END = "territory_end";
	/** 领地战借宿 */
	public static final String COMPONENT_TERRITORY_FLAG_SYN = "territory_flag_syn";
	// ---------------------MonsterType--------------------------------
	/** 怪物类型，领地战场中雕像怪物 **/
	public static final byte MONSTER_TYPE_TERRITORY = 1;

	// -----------------------皇城争霸赛-------------------------------
	/** 模块中文名称 */
	public static final String COMPONENT_HCZBS_NAME = "云宫之战";
	/** 皇城争霸赛加经验真气定时 */
	public static final String COMPONENT_HCZBS_EXP = "role_hczhengbasai";
	/** 皇城争霸赛结束 */
	public static final String COMPONENT_HCZBS_END = "hczhengbasai_end";
	/** 皇城争霸赛活动id */
	public static final String COMPONENT_HCZBS_ACTIVE_ID = "hczhengbasai_activeid";
	/** 争霸赛产生结果 */
	public static final String COMPONENT_HCZBS_HAS_WINNER = "hczhengbasai_winner";
	/** 争霸赛旗子同步 */
	public static final String COMPONENT_HCZBS_FLAG_SYN = "hczhengbasai_flag_syn";

	/** 怪物类型，皇城争霸赛中雕像怪物 **/
	public static final byte MONSTER_TYPE_HCZBS = 3;

	// ------------------------定时活动-------------------------------------
	/** 定时活动模块前缀 */
	public static final String COMPONENET_DINGSHI_ACTIVE = "dingshi_dazuo_";
	/** 定时活动模块前缀 */
	public static final String COMPONENET_DINGSHI_MONSTER = "dingshi_monster_";
	/** 定时活动模块前缀 */
	public static final String COMPONENET_DINGSHI_BOX = "dingshi_box_";
	/** 定时活动公告*/
	public static final String COMPONENET_DINGSHI_ACTIVE_NOTICE = "dingshi_active_notice_";
	/** 定时活动开服一周内 */
	public static final byte DINGSHI_WEEK_TYPE_KF = 1;
	/** 定时活动合服一周内 */
	public static final byte DINGSHI_WEEK_TYPE_HF = 2;
	/** 定时活动正常周期 */
	public static final byte DINGSHI_WEEK_TYPE_NOMAL = 3;
	/** 打坐多倍经验 */
	public static final int DINGSHI_DAZUO_MANY_BEI = 1;
	/** 打怪多倍经验 */
	public static final int DINGSHI_KILL_MANY_BEI = 2;
	/** 押镖多倍经验 */
	public static final int DINGSHI_YABIAO_MANY_BEI = 6;
	/** 答题 */
	public static final int DINGSHI_DATI = 5;
	/** 野外活动boss */
	public static final int DINGSHI_ACTIVITY_BOSS = 4;
	/** 宝箱采集 */
	public static final int DINGSHI_COLLECT_BOX = 3;
	/** 阵营战 */
	public static final int DINGSHI_CAMP_WAR = 7;
	/** 仙宫探宝 */
	public static final int DINGSHI_XIANGONG_TANBAO = 8;
	/** 领地战 */
	public static final int DINGSHI_TERRITORY = 9;
	/** 温泉 */
	public static final int DINGSHI_WENQUAN = 10;
	/** 争霸赛 */
	public static final int DINGSHI_ZHENGBASAI = 11;
	/** 勇闯七杀 */
	public static final int DINGSHI_QISHA = 12;
	/** 混沌战场 */
	public static final int DINGSHI_HUNDUN = 13;
	/** 跨服boss */
	public static final int DINGSHI_KUAFUBOSS = 14;
	/** 采集宝箱数据Key */
	public static final String COMPONENET_BOX_NAME = "collect_box_";
	/** 阵营战标识 **/
	public static final String COMPONENT_CAMP_WAR = "CAMP_WAR";
	/** 阵营战排行榜数据拉取排名 **/
	public static final int RANK_SIZE = 3;
	/** 添加积分，玩家死亡 **/
	public static final int ROLE_DEAD = 1;
	/** 添加积分，雕像怪物死亡 **/
	public static final int MONSTER_DEAD = 2;
	/** 添加积分，击杀雕像怪物 **/
	public static final int KILL_MONSTER = 3;
	/** 探宝活动模块 */
	public static final String COMPONENET_DINGSHI_TANBAO = "dingshi_tanbao";

	// ---------------------商城-----------------------------------
	/** 热发布商城的出售ID从10000开始配 */
	public static final int RFB_MALL_ID = 10000;

	// ------------------------游戏进度------------------------------------
	/** 游戏进度创角界面 */
	public static final int GAME_JINDU_TYPE_ROLE = 1;
	/** 游戏进度创角 */
	public static final int GAME_JINDU_TYPE_CREATE = 2;
	/** 游戏进度接取第一个任务 */
	public static final int GAME_JINDU_TYPE_FIRST = 3;
	/** 游戏进度创角界面(其他版本) */
	public static final int GAME_JINDU_TYPE_ROLE_OTHER = 4;
	/** 游戏进度创角(其他版本) */
	public static final int GAME_JINDU_TYPE_CREATE_OTHER = 5;
	/** 游戏进度接取第一个任务(其他版本) */
	public static final int GAME_JINDU_TYPE_FIRST_OTHER = 6;

	// ---------------------多人副本---------------------------
	/** 多人副本compoment **/
	public static final String COMPONENT_MORE_FUBEN_FORCED_LEAVE = "more_fuben_forced_leave";
	/** 多人读本定时清除倒计时 */
	public static final int MORE_FUBEN_DJS = 1800;
	/** 多人副本队伍上限 */
	public static final int MORE_FUBEN_LIMIT = 200;
	/** 多人副本完成状态 */
	public static final int MORE_FUBEN_FINISH_STATUS = 1;
	/** 多人副本场景标示 **/
	public static final String COMPONENT_MORE_FUBEN_ASYN_LOCK = "more_fuben";
	/** 多人副本倒计时时间 */
	public static final int MORE_FUBEN_PRE_DJS = 5000;
	/** 多人副本准备倒计时 */
	public static final String MORE_FUBEN_PRE_ENTER = "more_fuben_pre_enter";
	/** 多人副本奖励邮件内容 */
	public static final String MORE_FUBEN_EMAIL_REWARD = "5544";
	/** 多人副本奖励补发邮件内容 */
	public static final String MORE_FUBEN_EMAIL_REWARD_RESEND = "5548";
	/**redis数据key，队伍id*/
	public static final String REDIS_MORE_FUBEN_TEAMID_KEY = "1";
	/**redis数据key，队长名字*/
	public static final String REDIS_MORE_FUBEN_TEAMLEADER_KEY = "2";
	/**redis数据key，副本id*/
	public static final String REDIS_MORE_FUBEN_FUBENID_KEY = "3";
	/**redis数据key，所需战力*/
	public static final String REDIS_MORE_FUBEN_ZPLUS_KEY = "4";
	/**redis数据key，队伍人数*/
	public static final String REDIS_MORE_FUBEN_COUNT_KEY = "5";
	/**redis数据key，serverId*/
	public static final String REDIS_MORE_FUBEN_SERVERID_KEY = "6";
	/**redis数据key，队长Id*/
	public static final String REDIS_MORE_FUBEN_LEADER_KEY = "7";
	/**redis数据key，多人副本唯一id*/
	public static final String REDIS_MORE_FUBEN_TEAM_ID = "more_fuben_id";
	
	public static final String MORE_FUBEN_TIME_OUT_TEAM = "more_fuben_time_out_team";
	
	public static final int MORE_FUBEN_TIME_OUT_TEAM_TIME = 300;
	/** 与源码同步状态的定时器**/
	public static final String COMPONENT_MORE_FUBEN_SYNC = "more_fuben_sync";
	// ----------------------押镖----------------------------------
	/** 镖车标识 **/
	public static final String COMPONENT_BIAOCHE_NMAE = "biaoche_name";

	public static final String COMPONENT_BIAOCHE = "biaoche";
	/** 接受押镖任务状态 **/
	public static final int YJ_YB_STATE = -1;
	/** 未接受押镖任务状态（默认状态） **/
	public static final int WJ_YB_STATE = 0;
	/** 可领取奖励（完成了任务，未领取奖励） **/
	public static final int RECEIVE_YB_STATE = 1;
	/** 已领取奖励 **/
	public static final int COMPLETE_YB_STATE = 2;

	// ------------------------野外boss活动------------------------------------
	public static final String DSBOSS_COMPONENET_NAME = "dsboss";
	/** 定时野外boss活动db使用 */
	public static final byte[] ACTIVITY_BOSS_LOCK = new byte[0];
	/** 野外boss排行刷新时间 */
	public static final int DSBOSS_RANK_DELAY = 5000;
	/** 刷新野外boss排行榜常量 */
	public static final String DSBOSS_REFRESH_RANK = "dsboss_refresh_rank";

	/** 被击杀 */
	public static final int BOSS_KILL = 1;
	/** 回收 */
	public static final int BOSS_RECYCLE = 2;
	/** 没有被杀死(被创建) */
	public static final int BOSS_NO_KILL = 0;

	// -----------------------------翅膀--------------------------
	/** 上翅膀 */
	public static final int CB_UP = 1;
	/** 下翅膀 */
	public static final int CB_DOWN = 0;

	// -----------------------------器灵--------------------------
	/**显示器灵 */
	public static final int Ql_UP = 1;
	/** 不显示 */
	public static final int QI_DOWN = 0;

	// ------------------------糖宝----------------------------------
	/** 宠物模块名 */
	public static final String COMPONENT_NAME_PET = "pet_name";
	/** 糖宝跟随状态 */
	public static final int PET_STATE_NOMAL = 0;
	/** 糖宝固定伤害 */
	public static final String PET_HARM_KEY = "harm";
	/** 跨服糖宝仙剑属性key */
	public static final String KUAFU_PET_XIANJIAN_KEY = "kuafu_pet_xianjian";
	/** 跨服糖宝心纹属性key */
	public static final String KUAFU_PET_XINWEN_KEY = "kuafu_pet_xinwen_key";
	/** 跨服糖宝战甲属性key */
	public static final String KUAFU_PET_ZHANJIA_KEY = "kuafu_pet_zhanjia";
	/** 跨服糖宝天羽属性key */
	public static final String KUAFU_PET_TIANYU_KEY = "kuafu_pet_tianyu";
    /** 跨服糖宝五行属性key */
    public static final String KUAFU_PET_WUXING_KEY = "kuafu_pet_wuxing";
    /** 跨服糖宝五行技能属性key */
    public static final String KUAFU_PET_WUXING_SKILL_KEY = "kuafu_pet_wuxing_skill";
	/**天羽*/
	public static final String KUAFU_TIANYU_INFO_KEY = "kuafu_tianyu";
	// --------------------------答题----------------------------
	/** 答题奖励邮件内容 */
	public static final String DATI_MAIL_MSG_CODE = "2512";
	/** 答题奖励邮件标题 */
	public static final String DATI_MAIL_MSG_CODE_TITLE = "2519";

	// -----------------御剑升阶返利邮件内容-----------------------
	public static final String YUJIAN_FANLI = "3452";
	public static final String YUJIAN_FANLI_TITLE = "3479";
	
	// -----------------坐骑升阶返利邮件内容-----------------------
	public static final String WUQI_FANLI = "3482";
	public static final String WUQI_FANLI_TITLE = "3483";

	// ------------------------寻宝----------------
	public static final String XUNBAO_COMPONENET_NAME = "xunbao";
	/** 全服通知寻宝日志保留条数 */
	public static final int XUNBAO_INFO_MAX_COUNT = 10;
	/** 寻宝锁对象 **/
	public static final byte[] XUNBAO_LOG_LOCK = new byte[0];

	public static final String DROP = "drop";
	public static final String COUNT = "count";
	public static final String ODDS = "odds";
	public static final String ZB_FLAG = "@";
	/** 寻宝类型 */
	public static final int XB_TYPE1 = 1;
	public static final int XB_TYPE2 = 2;
	public static final int XB_TYPE3 = 3;

	// ------------------------热发布活动----------------------
	/** 后台热发布活动的下载目录 */
	public static final String REFABU_DIR_NAME = "activity";
	/** 后台热发布活动的实例业务下载目录（如：首充等） */
	public static final String REFABU_DIR_HANLE_NAME = "gameconfig";
	/** 热发布文件扩展名后缀 */
	public final static String RFB_SUFFIX = ".jat";
	/** 主活动前缀 */
	public final static String ZHU_RFB_PREFIX = "activity_";
	/** 基于具体时间的活动 */
	public static final int RFB_ACTIVE_TIME_TYPE_NOMAL = 0;
	/** 基于星期几的活动 */
	public static final int RFB_ACTIVE_TIME_TYPE_WEEK = 1;
	/** 基于开服天数的活动 */
	public static final int RFB_ACTIVE_TIME_TYPE_KAIFU = 2;
	/** 基于合服天数的活动 */
	public static final int RFB_ACTIVE_TIME_TYPE_HEFU = 3;
	/** 热发布活动地图模块前缀 */
	public static final String COMPONENET_REFB_ACTIVE_MAP = "active_map_";

	// -------------------阵营战----------
	/** 阵营战 **/
	public static final String COMPONENT_CAMP = "con_camp";
	/** 阵营战检测 **/
	public static final String COMPONENT_CAMP_CHECK = "camp_check";
	/** 阵营战定时加经验检测 **/
	public static final String COMPONENT_CAMP_ADD_EXP = "camp_ADD_EXP";
	/** 阵营战活动结束后，统计排名结算奖励 **/
	public static final String COMPONENT_CAMP_RANK = "camp_rank";

	// ---------------------MonsterType--------------------------------
	/** 怪物类型，阵营战场中雕像怪物 **/
	public static final byte CAMP_TYPE = 2;
	/** 门派炼狱boss怪物 **/
	public static final byte LIANYU_BOSS_TYPE = 8;
	/** 门派炼狱角色在攻击 **/
	public static final byte LIANYU_BOSS_ROLE_ATTACK = 1;
	/**  门派炼狱怪物在攻击 **/
	public static final byte LIANYU_BOSS_MONSTER_ATTACK = 2;
	
    /** 金属性怪物 **/
    public static final byte MONSTER_TYPE__WUXING_GOLD = 9;
    /** 木属性怪物 **/
    public static final byte MONSTER_TYPE__WUXING_WOOD = 10;
    /** 土属性怪物 **/
    public static final byte MONSTER_TYPE__WUXING_EARTH = 11;
    /** 水属性怪物 **/
    public static final byte MONSTER_TYPE__WUXING_WATER = 12;
    /** 火属性怪物 **/
    public static final byte MONSTER_TYPE__WUXING_FIRE = 13;
	
	
	// -----------------------------仙剑--------------------------
	/** 上仙剑 */
	public static final int XJ_UP = 1;
	/** 下仙剑 */
	public static final int XJ_DOWN = 0;
	/** 跨服仙剑属性key */
	public static final String KUAFU_XIANJIAN_KEY = "kuafu_xianjian";
	// -----------------------------战甲--------------------------
	/** 上战甲 */
	public static final int ZJ_UP = 1;
	/** 下战甲 */
	public static final int ZJ_DOWN = 0;
	/** 跨服战甲属性key */
	public static final String KUAFU_ZHANJIA_KEY = "kuafu_zhanjia";
	// -----------------------------神器--------------------------
	/** 神器攻击 */
	public static final String COMPONENT_SHENQI_ATTACK = "shenqi_attack";
	/** 跨服神器属性key */
	public static final String KUAFU_SHENQI_KEY = "kuafu_shenqi_xianjian";
	// ----------------神秘商店----------------------
	public static final String SMSD_COMPONENET_NAME = "shenmishangdian";
	/** 神秘商店锁对象 **/
	public static final byte[] SMSD_LOG_LOCK = new byte[0];
	/** 全服通知神秘商店日志保留条数 */
	public static final int SMSD_INFO_MAX_COUNT = 30;
	// ----------------命运转盘----------------------
	public static final String ZP_COMPONENET_NAME = "mingyuanzhuanpan";
	/** 转盘锁对象 **/
	public static final byte[] ZP_LOG_LOCK = new byte[0];
	/** 全服通知转盘日志保留条数 */
	public static final int ZP_INFO_MAX_COUNT = 50;
	// ----------------盗墓手札----------------------
	public static final String DM_COMPONENET_NAME = "daomoshouzha";
	/** 盗墓锁对象 **/
	public static final byte[] DM_LOG_LOCK = new byte[0];
	/** 全服通知转盘日志保留条数 */
	public static final int DM_INFO_MAX_COUNT = 50;
	// ----------------热发布寻宝----------------------
	public static final String XB_COMPONENET_NAME = "refabuxunbao";
	/** 转盘锁对象 **/
	public static final byte[] XB_LOG_LOCK = new byte[0];
	/** 全服通知寻宝日志保留条数 */
	public static final int XB_INFO_MAX_COUNT = 50;
	/**热发布全局寻宝次数锁**/
	public static final byte[] REFB_QUANJU_XUNBAO_LOCK = new byte[0];
	//热发布活动
	/** 活动通用对象 **/
	public static final byte[] ACTIVITY_LOG_LOCK = new byte[0];
	public static final byte[] ACTIVITY_LOG_LOCK_ADD = new byte[0];
	public static final byte[] ACTIVITY_LOG_LOCK_CLEAN = new byte[0];
	public static final String ACTIVITY_COMPONENET_NAME = "refabuactivity";
	// -----------------------周天灵境-------------------------------
	/** 御剑类型 */
	public static final int LINGJING_TYPE_YUJIAN = 1;
	/** 翅膀类型 */
	public static final int LINGJING_TYPE_CHIBANG = 2;
	/** 仙剑类型 */
	public static final int LINGJING_TYPE_XIANJIAN = 3;
	/** 战甲类型 */
	public static final int LINGJING_TYPE_ZHANJIA = 4;
	/** 神器类型 */
	public static final int LINGJING_TYPE_SHENQI = 5;
	/** 等级类型 */
	public static final int LINGJING_TYPE_LEVEL = 6;
	/** 战力类型 */
	public static final int LINGJING_TYPE_ZPLUS = 7;
	/** 全身强化类型 */
	public static final int LINGJING_TYPE_QSQH = 8;
	/** 器灵类型 */
	public static final int LINGJING_TYPE_QILING = 10;
	/** 新圣剑类型 */
	public static final int LINGJING_TYPE_SHENGJIAN = 11;
	
//	/** 最大类型数量 */
//	public static final int LINGJING_MAX_TYPE_INDEX = 31;
//	/** 灵境每重满状态值 */
//	public static final int LINGJING_MAX_STATE = (2 << LINGJING_MAX_TYPE_INDEX - 1) - 1;
	
	/** 突破时奖励邮件code */
	public static final String LINGSHI_TUPO_EMAIL_CODE = "";
	// -----------------------温泉-------------------------------
	/** 温泉结束 */
	public static final String COMPONENT_WENQUAN = "wenquan";
	/** 温泉活动id */
	public static final String COMPONENT_WENQUAN_ACTIVE_ID = "wenquan_activeid";
	/** 温泉定时加经验检测 **/
	public static final String COMPONENT_WENQUAN_ADD_EXP = "wenquan_ADD_EXP";

	// -----------------------称号-------------------------------
	/** 称号过期 */
	public static final String COMPONENT_CHENGHAO_EXPIRE = "chenghao_expire";
	/** 平台称号类型 */
	public static final int CHENGHAO_TYPE_PLATFORM = 1;
	/** 成就称号类型 */
	public static final int CHENGHAO_TYPE_CHENGJIU = 2;
	/** 运营称号类型 */
	public static final int CHENGHAO_TYPE_YUNYING = 3;

	// --------------------------成就类型-----------------------------
	/** 等级 */
	public static final int CJ_LEVEL = 1;
	/** 战力 */
	public static final int CJ_ZPLUS = 2;
	/** 强化总和 */
	public static final int CJ_QIANGHUA = 3;
	/** 紫装个数 */
	public static final int CJ_ZIZHUANG = 4;
	/** 橙装个数 */
	public static final int CJ_CHENGZHUANG = 5;
	/** 套装个数 */
	public static final int CJ_TAOZHUANG = 6;
	/** 御剑等阶 */
	public static final int CJ_ZUOQI = 7;
	/** 翅膀等阶 */
	public static final int CJ_CHIBANG = 8;
	/** 仙剑等阶 */
	public static final int CJ_XIANJIAN = 9;
	/** 战甲等阶 */
	public static final int CJ_ZHANJIA = 10;
	/** 激活神兵个数 */
	public static final int CJ_SHENBING = 11;
	/** 基础技能等级 */
	public static final int CJ_JICHUJINENG = 12;
	/** 被动技能等级 */
	public static final int CJ_BEIDONGJINENG = 13;
	/** 日常任务完成数量 */
	public static final int CJ_DAYTASKCOUNT = 14;
	/** 门派任务完成数量 */
	public static final int CJ_GUILDTASKCOUNT = 15;
	/** 勾玉之诺副本通关次数 */
	public static final int CJ_GOUYUFUBENCOUNT = 16;
	/** 地火噬神副本通关次数 */
	public static final int CJ_DIHUOFUBENCOUNT = 17;
	/** 南无月通关关卡数 */
	public static final int CJ_NANWUYUEFUBENCOUNT = 18;
	/** 三生洗礼3个副本累积通关 */
	public static final int CJ_SANSHENGFUBENCOUNT = 19;
	/** 仙魔榜通关的层数 */
	public static final int CJ_XIANMOBANGCENG = 20;
	/** 仙魔榜通关次数 */
	public static final int CJ_XIANMOBANG = 21;
	/** 护送橙色镖车数量 */
	public static final int CJ_CHENGBIAO = 22;
	/** 累计击杀boss数量 */
	public static final int CJ_YEWAIBOSS = 23;
	/** 答题名次 */
	public static final int CJ_DATI = 24;
	/** 洗澡丢肥皂次数 */
	public static final int CJ_XIZAOFEIZAO = 25;
	/** 市场成功出售道具次数 */
	public static final int CJ_SHICHANGMAI = 26;
	/** 拥有银两 */
	public static final int CJ_YINLIANG = 27;
	/** 单日消费元宝 */
	public static final int CJ_XIAOFEIYUANBAO = 28;
	/** 单次充值元宝数量 */
	public static final int CJ_DANCHONGZHIYUANBAO = 29;
	/** 累计充值元宝数量 */
	public static final int CJ_LEICHONGZHIYUANBAO = 30;
	/** 累计开启帝君宝盒数量 */
	public static final int CJ_TIJUNBAOHE = 31;
	/** 累计签到次数 */
	public static final int CJ_QIANDAOCOUNT = 32;
	/** pk值 */
	public static final int CJ_PKVALUE = 33;
	/** 累计杀怪数量 */
	public static final int CJ_KILLMONTER = 34;
	/** vip */
	public static final int CJ_VIP = 35;
	/** 器灵升阶 */
	public static final int CJ_QILING = 36;
	/** 新圣器升阶 */
	public static final int CJ_WUQI = 37;

	/** 跨服-称号key */
	public static final String KUAFU_CHENGHAO_KEY = "kuafu_chenghao1";
	// -----------------------平台信息-------------------------------
	/** 跨服平台信息key */
	public static final String KUAFU_PLATFORM_INFO_KEY = "kuafu_pingtai";
	/** 原服信息key */
	public static final String KUAFU_SOURCE_SERVER_ID = "source_server_id";
 
	/**跨服时装信息key*/
	public static final String KUAFU_SHIZHUANG_INFO_KEY = "kuafu_shizhuang";
	/**跨服结婚信息key*/
	public static final String KUAFU_MARRY_INFO_KEY = "kuafu_marry";
	/**器灵*/
	public static final String KUAFU_QILING_INFO_KEY = "kuafu_qiling";
	/**五行*/
	public static final String KUAFU_WUXING_INFO_KEY = "kuafu_wuxing_type";
	/**五行*/
	public static final String KUAFU_WUXING_ID_KEY = "kuafu_wuxing_id";
	/**五行*/
	public static final String KUAFU_WUXING_LIST_KEY = "kuafu_wuxing_list";
	/**糖宝五行*/
	public static final String KUAFU_TB_WUXING_LIST_KEY = "kuafu_tangbao_wuxing_list";
	/**跨服需要通知熟练度信息key*/
	public static final String KUAFU_NOTICESKILLS_INFO_KEY = "kuafu_noticeskills";
	/**武器*/
	public static final String KUAFU_WUQI_KEY = "kuafu_wuqi";
	/**超级会员解析配置错误默认用这两个参数**/
	public static final int  ALLRECHARGE =5000;
	public static final int  ONCERECHARGE =3000;
	
	//**********************腾讯任务集市类型************************
	/**已领取*/
	public static final Integer RWJS_WEI_LING_QU = 0;
	/** 未领取 */
	public static final Integer RWJS_YI_LING_QU = 1;
	/** 成功 */
	public static final Integer RWJS_SUCCESS = 0;
	/** 用户尚未在应用内创建角色 */
	public static final Integer RWJS_NOT_ROLE = 1;
	/** 用户尚未完成本步骤 */
	public static final Integer RWJS_UNFINISHED = 2;
	/** 该步骤奖励已发放过 */
	public static final Integer RWJS_CHONG_LINGQU = 3;
	/** 仅需要查询任务步骤是否完成，返回步骤完成状态。 */
	public static final String RWJS_CHECK = "check";
	/** 需要查询任务步骤是否完成，若步骤已完成，直接给用户发货（payitem），并返回发货是否成功。 */
	public static final String RWJS_CHECKAWARD = "check_award";
	/** 直接给给用户发货，开发者返回发货是否成功。 */
	public static final String RWJS_AWARD = "award";

	/** 日常任务 */
	public static final int RWJS_DAY_TASK = 2;
	/** 玩家等级类 */
	public static final int RWJS_ROLE_LEVEL = 3;
	/** 参加一次古神战场 */
	public static final int RWJS_GUSHENG = 5;
	/** 完成押镖任务 */
	public static final int RWJS_YA_BIAO = 6;
	/** 进入多人副本 */
	public static final int RWJS_DUOREN_FUBEN = 7;
	/** 战力达到XX */
	public static final int RWJS_ZHAN_LI = 8;
	/** 坐骑等级达到XX */
	public static final int RWJS_ZUO_QI = 9;
	/** 翅膀等级达到XX */
	public static final int RWJS_CHIBANG = 10;

	/** 妖神形态切换 */
	public static final String YAOSHEN_SHOW_CHANGE = "yaoshen_show_change";

	/** 跨服妖神属性key */
	public static final String KUAFU_YAOSHEN_SHOW = "kuafu_yaoshen_show";
	/** 宝石的10个各位 */
	public static final int JEWEL_GEZI_NUM = 12;
	/** 宝石系统每一个格位有5个孔 */
	public static final int JEWEL_KONG_NUM = 5;
	/** 宝石类型 */
	public static final int JEWEL_TYPE = 70;
 
	
	/**宠物id*/
	public static final String KUAFU_CHONGWU_KEY = "kuafu_CHONGWU_INFO";
	public static final String KUAFU_CHONGWU_KEY_ID = "kuafu_CHONGWU_INFO_ID";
	public static final String KUAFU_CHONGWU_KEY_NAME = "kuafu_CHONGWU_INFO_NAME";
	public static final String KUAFU_CHONGWU_KEY_CONFIG_ID = "kuafu_CHONGWU_INFO_CONFIG_ID";
	public static final String KUAFU_CHONGWU_KEY_SPEED = "kuafu_CHONGWU_INFO_SPEED";
	//----------------------------锁妖塔--------------------------
	/**锁妖塔每次增加祝福值*/
	public static final int SUOYAOTA_ADD_LUCKY = 1;
	/** 锁妖塔邮件code */
	public static final String SUOYAOTA_ENTER_EMAIL_CODE = "15805";
	//----------------------------跨服充值排名--------------------------
	/**跨服充值排名活动结束*/
	public static final String KUAFU_CHARGE_RANK = "kuafu_charge_rank";
	//----------------------------单服充值排名--------------------------
	/**跨服充值排名活动结束*/
	public static final String DANFU_CHARGE_RANK = "danfu_charge_rank";
	//---------------------------结婚------------------------------
	/**未婚*/
	public static final int MARRY_STATE_NO = 0;
	/**订婚*/
	public static final int MARRY_STATE_DING = 1;
	/**结婚*/
	public static final int MARRY_STATE_MARRY = 2;
	/**协议离婚*/
	public static final int MARRY_STATE_DIVORCE = 3;
	/**和平分手*/
	public static final int DIVORCE_HEPING = 0;
	/**强制离婚*/
	public static final int DIVORCE_QIANGZHI = 1;
	/**定时增加缘分、亲密**/
	public static final String COMPONENT_MARRY = "marry_add";
	/**定时离婚**/
	public static final String COMPONENT_DIVORCE = "marry_divrce";
	/**心情最大字数*/
	public static final int XINQING_MAX_LENGTH = 100;
	
	//--------------------------时装------------------------------------
	/**限时时装**/
	public static final String COMPONENT_SHIZHUANG = "shizhuang";
	
	//--------------------------七杀------------------------------------
	/**七杀活动定时**/
	public static final String COMPONENT_QISHA = "qisha";
	/**BOSS未刷新*/
	public static final int BOSS_STATE_NOREFRESH = 1;
	/**BOSS已刷新*/
	public static final int BOSS_STATE_REFRESH = 0;
	/**BOSS已死亡*/
	public static final int BOSS_STATE_DEAD = -1;
	/**七杀活动经验定时**/
	public static final String COMPONENT_QISHA_EXP = "qisha_exp";
	/**七杀BOSS类型*/
	public static final int QISHA_MONSTER_TYPE = 4;
	/**伤害最高邮件code*/
	public static final String QISHA_HURT_CODE = "16354";
	/**击杀邮件code*/
	public static final String QISHA_KILL_CODE = "16355";
	/**邮件标题*/
	public static final String QISHA_KILL_CODE_TITLE = "16356";
	
	//----------------------------附属技能------------------------------
	/**坐骑技能类型*/
	public static final int FUSHU_SKILL_TYPE_ZUOQI = 1;
	/**翅膀技能类型*/
	public static final int FUSHU_SKILL_TYPE_CHIBANG = 2;
	/**天工技能类型*/
	public static final int FUSHU_SKILL_TYPE_TIANGONG = 3;
	/**天裳技能类型*/
	public static final int FUSHU_SKILL_TYPE_TIANSHANG = 4;
	/**器灵技能类型*/
	public static final int FUSHU_SKILL_TYPE_QILING = 5;
	/**天羽技能类型*/
	public static final int FUSHU_SKILL_TYPE_TIANYU = 6;
	/**圣剑技能类型*/
	public static final int FUSHU_SKILL_TYPE_WUQI = 7;
	
	//----------------------------混沌战场-------------------------------
	/**混沌战场定时检测*/
	public static final String COMPONENT_CHAOS_CHECK = "chaos_check";
	/**混沌战定时加经验检测**/
	public static final String COMPONENT_CHAOS_ADD_EXP = "chaos_add_exp";
	/**混沌战活动结束后，统计排名结算奖励，并踢出**/
	public static final String COMPONENT_CHAOS_RANK = "chaos_rank";
	/**排行刷新间隔*/
	public static final long CHAOS_REFRESH_TIME = 1000;
	/**混沌战场最高层*/
	public static final int CHAOS_MAX_CENG = 4;
	/**混沌战场全民奖励邮件*/
	public static final String CHAOS_PUJIANG_GIFT_EMAIL = "6443";
	/**混沌战场全民奖励邮件标题*/
	public static final String CHAOS_PUJIANG_GIFT_EMAIL_TITLE = "6459";
	/**混沌战场排名奖励邮件*/
	public static final String CHAOS_PAIMING_GIFT_EMAIL = "6445";
	/**混沌战场排名奖励邮件标题*/
	public static final String CHAOS_PAIMING_GIFT_EMAIL_TITLE = "6458";
	
	//----------------------------跨服单人竞技场-------------------------------
	/** 跨服单人竞技场进入场景 */
	public static final String COMPONENT_KUAFU_ARENA_ENTER_STAGE = "kuafu_arena_enter_stage";
	/** 跨服单人竞技场副本compoment **/
	public static final String COMPONENT_KUAFU_ARENA_START = "kuafu_arena_start";
	/** 跨服单人竞技场副本compoment **/
	public static final String COMPONENT_KUAFU_ARENA_FORCE_END = "kuafu_arena_force_end";
	/** 跨服单人竞技场副本compoment **/
	public static final String COMPONENT_KUAFU_ARENA_KICK_MEMBER = "kuafu_arena_kick_member";
	/** 跨服单人竞技场副本compoment **/
	public static final String COMPONENT_KUAFU_ARENA_MATCH_TIME_OUT = "kuafu_arena_match_time_out";
	/** 跨服单人竞技场副本compoment **/
	public static final String COMPONENT_KUAFU_ARENA_PREPARE_TIME_OUT = "kuafu_arena_prepare_time_out";
	/** 跨服单人竞技场副本compoment **/
	public static final String COMPONENT_KUAFU_ARENA_2WEEK_JOB = "kuafu_arena_2week_job";
	/** 跨服单人竞技场副本compoment **/
	public static final String KUAFU_ARENA_CLEAN_REDIS_RANK = "kuafu_arena_clean_redis_rank";
	/** 发送排名邮件奖励时间戳:秒 **/
	public static final int SEND_REWARD_TIME = 5 * 60;
	/** 跨服竞技1v1发送邮件奖励compoment **/
	public static final String KUAFU_ARENA_1V1_SEND_EMAIL_REWARD = "kuafu_arena_1v1_send_email_reward";
	/** 跨服竞技4v4发送邮件奖励compoment **/
	public static final String KUAFU_ARENA_4V4_SEND_EMAIL_REWARD = "kuafu_arena_4v4_send_email_reward";
	
	//---------------------------八卦阵-------------------------------
	/** 八卦副本compoment **/
	public static final String COMPONENT_BAGUA_FUBEN_FORCED_LEAVE = "bagua_fuben_forced_leave";
	/** 八卦读本定时清除倒计时 */
	public static final int BAGUA_FUBEN_DJS = 1800;
	/** 八卦副本队伍上限 */
	public static final int BAGUA_FUBEN_LIMIT = 200;
	/** 八卦副本完成状态 */
	public static final int BAGUA_FUBEN_FINISH_STATUS = 1;
	/** 八卦副本场景标示 **/
	public static final String COMPONENT_BAGUA_FUBEN_ASYN_LOCK = "bagua_fuben";
	/** 八卦副本倒计时时间 */
	public static final int BAGUA_FUBEN_DJS_TIME = 5000;
	/** 八卦副本准备倒计时 */
	public static final String BAGUA_DJS = "bagua_djs";
	/** 八卦副本奖励邮件内容 */
	public static final String BAGUA_FUBEN_EMAIL_REWARD = "5544";
	/** 八卦副本奖励补发邮件内容 */
	public static final String BAGUA_FUBEN_EMAIL_REWARD_RESEND = "5548";
	/** 八卦副本奖励补发邮件标题 */
	public static final String FUBEN_EMAIL_REWARD_TITLE = "5552";
	/**redis数据key，队伍id*/
	public static final String REDIS_BAGUA_FUBEN_TEAMID_KEY = "1";
	/**redis数据key，队长名字*/
	public static final String REDIS_BAGUA_FUBEN_TEAMLEADER_KEY = "2";
	/**redis数据key，副本id*/
	public static final String REDIS_BAGUA_FUBEN_FUBENID_KEY = "3";
	/**redis数据key，所需战力*/
	public static final String REDIS_BAGUA_FUBEN_ZPLUS_KEY = "4";
	/**redis数据key，队伍人数*/
	public static final String REDIS_BAGUA_FUBEN_COUNT_KEY = "5";
	/**redis数据key，serverId*/
	public static final String REDIS_BAGUA_FUBEN_SERVERID_KEY = "6";
	/**redis数据key，serverId*/
	public static final String REDIS_BAGUA_FUBEN_ING = "7";
	/**redis数据key，多人副本唯一id*/
	public static final String REDIS_BAGUA_FUBEN_TEAM_ID = "bagua_fuben_id";
	/** 多人副本邀请间隔(毫秒) */
	public static final long BAGUA_FUBEN_YAOQING_INTERVAL = 60000;
	/** 副本延迟清场时间，单位毫秒 */
	public static final int BAGUA_FUBEN_EXPIRE_CHECK_INTERVAL = 60000;
	
	public static final String BAGUA_FUBEN_TIME_OUT_TEAM = "bagua_fuben_time_out_team";
	
	public static final int BAGUA_FUBEN_TIME_OUT_TEAM_TIME = 300;
	
	//---------------------------幸运彩蛋--------------------------------------
	public static final String COMPONENET_CAIDAN_NAME = "caidan";
	/**彩蛋日志最大条数*/
	public static final int CAIDAN_LOG_MAX_COUNT = 20;
	/**每次彩蛋最大数量*/
	public static int MAX_GIFT_SIZE = 16;
	/**最大幸运值*/
	public static int MAX_LUCKY = 100;
	/**砸蛋奖励内容*/
	public static String SEND_EMAIL_CAIDAN_CONTENT = EmailUtil.getCodeEmail("16500");
	/**砸蛋公告*/
	public static int ZADAN_NOTICE = 16516;
	
	//-------------------------神魔战场----------------------------------
	/**系统队伍id*/
	public static final int SHENMO_TEAM_SYSTEM = 0;
	/**神兵队伍id*/
	public static final int SHENMO_TEAM_SHENBING = 1;
	/**血魔队伍id*/
	public static final int SHENMO_TEAM_XUEMO = 2;
	/**神魔战场水晶类型*/
	public static final int SHENMO_TYPE_SHUIJING = 1;
	/**神魔战场BOSS类型*/
	public static final int SHENMO_TYPE_BOSS = 3;
	/**水晶加经验定时*/
	public static final String COMPONENT_SHENMO_ADD_SCORE = "shenmo_score";
	/**神魔战场结束定时*/
	public static final String COMPONENT_SHENMO_GAME_OVER = "shenmo_stop";
	/**踢人定时*/
	public static final String COMPONENT_SHENMO_KICK = "shenmo_kick";
	/** 跨服多人竞技场副本compoment **/
	public static final String COMPONENT_KUAFU_ARENA_4V4_MATCH_TIME_OUT = "kuafu_arena_4v4_match_time_out";
	/** 跨服多人竞技场副本compoment **/
	public static final String COMPONENT_KUAFU_ARENA_4V4_PREPARE_TIME_OUT = "kuafu_arena_4v4_prepare_time_out";
	/** 跨服多人竞技场副本compoment **/
	public static final String COMPONENT_KUAFU_ARENA_4V4_2WEEK_JOB = "kuafu_arena_4v4_2week_job";
	/** 跨服多人竞技场副本compoment **/
	public static final String KUAFU_ARENA_4V4_CLEAN_REDIS_RANK = "kuafu_arena_4v4_clean_redis_rank";
	/**超时的时候，从已匹配进的队伍中删除*/
	public static final String KUAFU_ARENA_4V4_REMOVE_REQUEST = "kuafu_arena_4v4_remove_request";
	/**通知积分变化定时*/
	public static final String COMPONENT_SHENMO_NORICE_SCORE = "shenmo_notice_score";
	/**通知积分变化定时时间:单位毫秒*/
	public static final long SHENMO_NOTICE_SCORE_TIME = 3000;
	/** 进入场景 */
	public static final String COMPONENT_KUAFU_ARENA_4V4_ENTER_STAGE = "kuafu_arena_4v4_enter_stage";
	/** 跨服单人竞技场副本compoment **/
	public static final String COMPONENT_KUAFU_ARENA_4V4_START = "kuafu_arena_4v4_start";
	/** 跨服多人竞技场副本compoment **/
	public static final String COMPONENT_KUAFU_ARENA_4V4_TEAM_MATCH_DJS = "kuafu_arena_4v4_team_match_djs";
	/** 神魔战场 水晶 **/
	public static final byte MONSTER_TYPE_SHUIJING = 5;
	
	//-------------------------------塔防副本---------------------------------
	/**塔防定时刷怪*/
	public static final String COMPONENT_TAFANG_MONSTER = "tafang_monster";
	/**升级塔消耗类型：游戏币*/
	public static final Integer TAFANG_COST_TYPE_MANEY = 0;
	/**升级塔消耗类型：元宝*/
	public static final Integer TAFANG_COST_TYPE_GOLD = 1;
	
	//-------------------------妖神魂魄----------------------------------
	/** 完成妖神魂魄副本定时 **/
	public static final String COMPONENT_HUNPO_FUBEN_FORCE_EXIT = "component_hunpo_fuben_force_exit";
	public static final int HUNPO_FUBEN_ID = 1;
	
	
	//---------------------------埋骨之地-------------------------------
	/** 埋骨副本compoment **/
	public static final String COMPONENT_MAIGU_FUBEN_FORCED_LEAVE = "maigu_fuben_forced_leave";
	/** 埋骨读本定时清除倒计时 */
	public static final int MAIGU_FUBEN_DJS = 1800;
	/** 埋骨副本队伍上限 */
	public static final int MAIGU_FUBEN_LIMIT = 200;
	/** 埋骨副本完成状态 */
	public static final int MAIGU_FUBEN_FINISH_STATUS = 1;
	/** 埋骨副本场景标示 **/
	public static final String COMPONENT_MAIGU_FUBEN_ASYN_LOCK = "maigu_fuben";
	/** 埋骨副本倒计时时间 */
	public static final int MAIGU_FUBEN_DJS_TIME = 5000;
	/** 埋骨副本准备倒计时 */
	public static final String MAIGU_DJS = "bagua_djs";
	/** 埋骨副本奖励邮件内容 */
	public static final String MAIGU_FUBEN_EMAIL_REWARD = "5544";
	/** 埋骨副本奖励补发邮件内容 */
	public static final String MAIGU_FUBEN_EMAIL_REWARD_RESEND = "5548";
	/**redis数据key，队伍id*/
	public static final String REDIS_MAIGU_FUBEN_TEAMID_KEY = "1";
	/**redis数据key，队长名字*/
	public static final String REDIS_MAIGU_FUBEN_TEAMLEADER_KEY = "2";
	/**redis数据key，副本id*/
	public static final String REDIS_MAIGU_FUBEN_FUBENID_KEY = "3";
	/**redis数据key，所需战力*/
	public static final String REDIS_MAIGU_FUBEN_ZPLUS_KEY = "4";
	/**redis数据key，队伍人数*/
	public static final String REDIS_MAIGU_FUBEN_COUNT_KEY = "5";
	/**redis数据key，serverId*/
	public static final String REDIS_MAIGU_FUBEN_SERVERID_KEY = "6";
	/**redis数据key，serverId*/
	public static final String REDIS_MAIGU_FUBEN_ING = "7";
	/**redis数据key，多人副本唯一id*/
	public static final String REDIS_MAIGU_FUBEN_TEAM_ID = "maigu_fuben_id";
	/** 埋骨副本邀请间隔(毫秒) */
	public static final long MAIGU_FUBEN_YAOQING_INTERVAL = 60000;
	/** 埋骨副本延迟清场时间，单位毫秒 */
	public static final int MAIGU_FUBEN_EXPIRE_CHECK_INTERVAL = 60000;
	
	public static final String MAIGU_FUBEN_TIME_OUT_TEAM = "maigu_fuben_time_out_team";
	
	public static final int MAIGU_FUBEN_TIME_OUT_TEAM_TIME = 300;
	
	
	//---------------------------------封神之战------------------------------------
	/**redis数据key，竞技信息角色id*/
	public static final String REDIS_KUAFU_JINGJI_USER_ROLE_ID = "1";
	/**redis数据key，竞技信息排名*/
	public static final String REDIS_KUAFU_JINGJI_RANK = "2";
	/**redis数据key，竞技信息战力*/
	public static final String REDIS_KUAFU_JINGJI_ZPLUS = "3";
	/**redis数据key，竞技信息更新时间*/
	public static final String REDIS_KUAFU_JINGJI_UPDATE_TIME = "8";
	
	/**redis数据key，竞技详情角色名*/
	public static final String REDIS_KUAFU_JINGJI_INFO_NAME = "4";
	/**redis数据key，竞技详情等级*/
	public static final String REDIS_KUAFU_JINGJI_INFO_LEVEL = "5";
	/**redis数据key，竞技详情职业*/
	public static final String REDIS_KUAFU_JINGJI_INFO_CONFIG = "6";
	/**redis数据key，竞技详情御剑*/
	public static final String REDIS_KUAFU_JINGJI_INFO_ZUOQI = "7";
	/**redis数据key，竞技详情翅膀*/
	public static final String REDIS_KUAFU_JINGJI_INFO_CHIBANG = "9";
	
//	/**redis数据key，战报是否被动*/
//	public static final String REDIS_KUAFU_JINGJI_FIGHT_BEI = "1";
//	/**redis数据key，战报角色名*/
//	public static final String REDIS_KUAFU_JINGJI_FIGHT_NAME = "2";
//	/**redis数据key，战报是否战胜*/
//	public static final String REDIS_KUAFU_JINGJI_FIGHT_WIN = "3";
//	/**redis数据key，战报排名*/
//	public static final String REDIS_KUAFU_JINGJI_FIGHT_RANK = "4";
//	/**redis数据key，战报排名变动*/
//	public static final String REDIS_KUAFU_JINGJI_FIGHT_CHANGE = "5";
	/**redis数据key，战报自增id */
	public static final String REDIS_KUAFU_JINGJI_FIGHT_ID = "fight_id";
	/**战报分割符*/
	public static final String REDIS_KUAFU_JINGJI_REPORT_SPLIT = ":";
	/**战报过期时间*/
	public static final int REDIS_KUAFU_JINGJI_FIGHT_EXPIRE_TIME = 24 * 60 * 60;
	/**最大战报数量*/
	public static final int KUAFU_JINGJI_MAX_COUNT = 50;
	//---------------------------------转生------------------------------
	/**兑换经验单位:万*/
	public static final int DUIHUAN_PER_EXP = 10000;
	/**转生*/
	public static final String KUAFU_ZHUANSHENG_INFO_KEY = "kuafu_zhuansheng";
	
	//---------------------------------通用潜能丹------------------------------------
//	[0:int(type0:御剑 1:翅膀 2:仙剑 3:战甲，4：器灵，5：眉心血)
	public static final int COMMON_WANNENG_ZUOQI  = 0;
	public static final int COMMON_WANNENG_CHIBANG  = 1;
	public static final int COMMON_WANNENG_XIANJIAN  = 2;
	public static final int COMMON_WANNENG_ZHANJIA  = 3;
	public static final int COMMON_WANNENG_QILING  = 4;
	public static final int COMMON_WANNENG_TANGBAO  = 5;
	public static final int COMMON_WANNENG_TIANYU  = 6;
	public static final int COMMON_WANNENG_WUQI  = 7;
	//---------------------------------妖神通用潜能丹------------------------------------
//	[0:int(type0:霸体 1:魔纹 2:魂魄 3:魔印)
	public static final int YAOSHEN_WANNENG_BATI  = 0;
	public static final int YAOSHEN_WANNENG_MOWEN  = 1;
	public static final int YAOSHEN_WANNENG_HUNPO  = 2;
	public static final int YAOSHEN_WANNENG_MOYING  = 3;
	
	//--------------------------------探宝--------------------------------------
	public static final int WANG_CHENG_ID = 5;//王城宝藏ID
	public static final int OPEN_ID = 1;//永不关闭的宝藏ID
	public static final int SUCCESS = 1;//成功
	public static final int FAILURE = 0;//失败
	public static final int tbOneCount = -1;//领取1阶奖励需要探索王城的次数
	public static final int tbTwoCount = -2;//领取2阶奖励需要探索王城的次数
	public static final int tbThreeCount = -3;//领取3阶奖励需要探索王城的次数
	
	/************          台湾                            ***********/
	public static final String ZHUBO_TYPE_SHIJIE = "1";//世界频道
	public static final String ZHUBO_TYPE_SILIAO = "2";//私聊频道
	public static final int ZHUBO_SEND_SUCCESS  = 0;//成功
	public static final int ZHUBO_CONTENT_MAX = 1;//发送内容过多
	public static final int ZHUBO_BEI_JINYAN = 2;//主播已被禁言
	public static final int ZHUBO_NOT_ROLE = 3;//角色不存在
	
	/*****************越南************************/
	/** 预充值邮件内容 */
	public static final String YUENAN_RECHARGE_CODE = "17507";
	/** 预充值邮件标题 */
	public static final String YUENAN_RECHARGE_CODE_TITLE = "17508";
	
	/*****************跨服世界boss************************/
	public static final String COMPONENT_KUAFU_BOSS = "compoent_kuafu_boss";
	public static final String COMPONENT_KUAFU_BOSS_ADD_EXP = "compoent_kuafu_boss_add_exp";
	/** 跨服boss排行刷新时间 */
	public static final int KUAFUBOSS_RANK_DELAY = 5000;
	/** 跨服boss排行榜常量 */
	public static final String KUAFUBOSS_REFRESH_RANK = "kuafuboss_refresh_rank";
	public static final String COMPONENT_KUAFU_BOSS_FORCE_KICK = "component_kuafu_boss_force_kick";
	/**跨服大乱斗*/
	public static final String COMPONENT_KUAFU_LUANDOU_FORCE_KICK = "component_kuafu_luandou_force_kick";
	/** 跨服boss排行刷新时间 */
	public static final int KUAFULUANDOU_RANK_DELAY = 10000;
	/** 跨服boss排行榜常量 */
	public static final String KUAFULUANDOU_REFRESH_RANK = "kuafuluandou_refresh_rank";
	/** 跨服大乱斗取消复活无敌常量 */
	public static final String KUAFULUANDOU_QUXIAO_FH = "kuafuluandou_quxiao_fuhou";
	
	/***********************跨服群仙宴*********************************/
	public static final String COMPONENT_KUAFU_QUNXIANYAN = "compoent_kuafu_qunxianyan";
	public static final String COMPONENT_KUAFU_QUNXIANYAN_ADD_EXP = "compoent_kuafu_qunxianyan_add_exp";
	/** 跨服群仙宴排行榜常量 */
	public static final String COMPONENT_KUAFU_QUNXIANYAN_FORCE_KICK = "component_kuafu_qunxianyan_force_kick";
	public static final String KUAFUQUNXIANYAN_REFRESH_RANK = "kuafuquanxianyan_refresh_rank";
	public static final String KUAFUQUNXIANYAN_REFRESH_ZIYUAN = "kuafuquanxianyan_refresh_ziyuan";
	
	
	
	public static int THREAD_NUM = Math.max(1, SystemPropertyUtil.getInt(
            "io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));
	
	/**被杀**/
	public static int KILL_BY_ROLE = 0;
	public static int KILL_BY_MONSTER =1;
	
	//---------------------------------------秒杀--------------------------------------
	/**redis过期时间（秒）*/
	public static final int MIAOSHA_REDIS_EXPIRE_TIME = 12*3600;
	/**奖励邮件*/
	public static final String MIAOSHA_GIFT_EMAIL_CODE = "18206";
	/**奖励邮件标题*/
	public static final String MIAOSHA_GIFT_EMAIL_TITLE = "18211";
	//----------------------------------老玩家回归----------------------------
	/**老玩家回归登陆奖励*/
	public static final String LAOWANJIA_LOGIN = "A";
	/**老玩家回归充值奖励*/
	public static final String LAOWANJIA_RECHARGE = "B";
	//-----------------------------------慧眼识金----------------------
	/**矿石数量*/
	public static final Integer HUIYAN_KUANGSHI_NUMBER = 9;
	/**中奖矿石ID*/
	public static final Integer HUIYAN_ZHONGJIANG_KUANGSHI_ID = 1;
	/**中奖元宝常量*/
	public static final String HUIYAN_ZHONGJIANG_GOLD = "zjGlod";
	/**中奖公告常量*/
	public static final String HUIYAN_ZHONGJIANG_GONGGAO = "zjGongGao";
	/**日志ID*/
	public static final String HYSJ_COMPONENET_NAME = "mingyuanzhuanpan";
	/** 锁对象 **/
	public static final byte[] HYSJ_LOG_LOCK = new byte[0];
	/** 全服通知日志保留条数 */
	public static final int HYSJ_INFO_MAX_COUNT = 50;
	/**级别1全服次数ID*/
	public static final int HYSJ_QUANFU_COUNT_ID_1 = -100;
	/**级别2全服次数ID*/
	public static final int HYSJ_QUANFU_COUNT_ID_2 = -101;
	/**级别3全服次数ID*/
	public static final int HYSJ_QUANFU_COUNT_ID_3 = -102;
	//---------------------------------群仙宴---------------------------
	/**群仙宴资源类型*/
	public static final Integer QUNXIANYAN_ZIYAN_TYPE = 6;
	
    // -----------------------------五行魔神---------------------------
    /** 五行初始等级 */
    public static final Integer WUXING_CHUSHI_LEVEL = 0;
    /** 五行附体时间 */
    public static final String WUXING_FUTI_TIME = "wuxing_futi_time";
    /** 五行金属性 **/
    public static final int WUXING_GOLD = 1;
    /** 五行木属性 **/
    public static final int WUXING_WOOD = 2;
    /** 五行土属性 **/
    public static final int WUXING_EARTH = 3;
    /** 五行水属性 **/
    public static final int WUXING_WATER = 4;
    /** 五行火属性 **/
    public static final int WUXING_FIRE = 5;

    // -----------------------------五行副本---------------------------
    /** 五行副本记录分隔符 **/
    public static final String WUXING_FUBEN_SPLIT_CHAR = ",";

    // -----------------------------五行技能副本---------------------------
    /** 奖励类型:一般奖励 **/
    public static final int WXSKILL_NORMAL_REWARD = 0;
    /** 奖励类型:一般奖励+物品奖励 **/
    public static final int WXSKILL_ITEM_REWARD = 1;
    /** 奖励类型:一般奖励+增加buff奖励 **/
    public static final int WXSKILL_BUFFADD_REWARD = 2;
    /** 奖励类型:一般奖励+减伤buff奖励 **/
    public static final int WXSKILL_BUFFSUB_REWARD = 3;

    // ----------------------------五行魔神精魄------------------------------
    /** 魔神背包类型 **/
    public static final int WX_JP_BAG_TYPE = -1;
    /** 魔神背包孔位 **/
    public static final int WX_JP_BAG_SLOT = -1;
    /** 魔神身上孔未开启 **/
    public static final int WX_JP_SLOT_OPEN_NO = 0;
    /** 魔神身上孔已开启 **/
    public static final int WX_JP_SLOT_OPEN_YES = 1;
    /** 魔神身上的孔位,使用魔神等阶开孔 **/
    public static final int WX_JP_SLOT_OPEN_LEVEL = 1;
    /** 魔神背包的孔位,使用元宝开孔 **/
    public static final int WX_JP_SLOT_OPEN_GOLD = 2;
    /** 魔神普通精魄 **/
    public static final int WX_JP_JINGPO_TYPE = 0;
    /** 魔神经验精魄 **/
    public static final int WX_JP_JINGPO_TYPE_EXP = 1;
    
    // ----------------------------跨服巅峰之战常量---------------------------
    /** 跨服巅峰之战战斗开始常量 */
    public static final String KUAFU_DIANFENG_START_PK = "kuafu_dianfeng_start_pk";
    /** 跨服巅峰之战战斗结束常量 */
    public static final String KUAFU_DIANFENG_END_PK = "kuafu_dianfeng_end_pk";
    /** 跨服巅峰之战战斗结果展示常量 */
    public static final String KUAFU_DIANFENG_RESULT_SHOW = "kuafu_dianfeng_result_show";
    //--------------------------采集----------------------
    public static final long CJ_RONGCUO_TIME =  5000l;
    // ----------------充值转盘----------------------
    public static final String LP_COMPONENET_NAME = "chongzhilunpan";
    /** 转盘锁对象 **/
    public static final byte[] LP_LOG_LOCK = new byte[0];
    /** 全服通知转盘日志保留条数 */
    public static final int LP_INFO_MAX_COUNT = 50;
    //------------------首冲返利--------------------
    /** 活动未完成 **/
    public static final int ACTIVITY_STATUS_NO_FINISH = 0;
    /** 活动完成,未领取奖励 **/
    public static final int ACTIVITY_STATUS_YES_FINISH = 1;
    /** 活动已领取奖励 **/
    public static final int ACTIVITY_STATUS_RECEIVE = 2;
    // -----------------------------糖宝五行魔神---------------------------
    /** 糖宝五行初始等级 */
    public static final Integer TB_WUXING_CHUSHI_LEVEL = 0;
    /** 糖宝五行附体时间 */
    public static final String TB_WUXING_FUTI_TIME = "wuxing_futi_time";
    
    //-----------------------------心魔-天炉炼丹:生产丹药定时器模块名称----------------------------
    public static final String COMPEONENT_DANYAN_DINGSHI_PRODUCE = "compeonent_danyan_dingshi_produce";
    /** 心魔-天炉炼丹线程状态:结束状态 **/
    public static final int XM_LIANDAN_RUNNABLE_STATE_OVER = 0;
    /** 心魔-天炉炼丹线程状态:运行状态 **/
    public static final int XM_LIANDAN__RUNNABLE_STATE_RUN = 1;
    
    //-----------------------------心魔-魔神噬体CD模块名称----------------------------
    public static final String COMPEONENT_XM_SHITI_CD_PRODUCE = "compeonent_xm_shiti_cd_produce";
    
    //---------------------------心魔副本-腐化度模块名称------------------------------------
    /** 定时器模块 **/
    public static final String COMPEONENT_XM_FUHUA_PRODUCE = "compeonent_xm_fuhua_produce";
    /** 腐化度值变化的锁 **/
    public static final String COMPONENT_XM_FUBEN_SHARE = "compeonent_xm_fuhua_share";
    /** 心魔副本-腐化度减少线程状态:结束状态 **/
    public static final int XM_FUHUA_RUNNABLE_STATE_OVER = 0;
    /** 心魔副本-腐化度减少线程状态:运行状态 **/
    public static final int XM_FUHUA_RUNNABLE_STATE_RUN = 1;

    //--------------------------- 心魔深渊副本-常量类-----------------------------------
    /** 心魔深渊副本冷却cd未开始 **/
    public static final int XM_SHENYUAN_STATUS_COOLING_NO = 0;
    /** 心魔深渊副本冷却cd进行中 **/
    public static final int XM_SHENYUAN_STATUS_COOLING_YES = 1;
    /** 定时器模块 **/
    public static final String COMPEONENT_XM_SHENYUAN_COOLING_PRODUCE = "compeonent_xm_shenyuan_cooling_produce";
    //------------------------灵火祝福常量类---------------------------------
    /** 灵火祝福最小孔位号 **/
    public static final int LINGHUO_BLESS_MIN_SLOT = 1;
    /** 灵火祝福最大孔位号 **/
    public static final int LINGHUO_BLESS_MAX_SLOT = 5;
    
    //------------------------跨服-云宫之巅常量类---------------------------------
    /** 活动准备阶段定时模块名称 **/
    public static final String KUAFU_YUNGONG_READY_PRODUCE = "kuafu_yungong_ready_produce";
    /** 活动开始阶段定时模块名称 **/
    public static final String KUAFU_YUNGONG_START_PRODUCE = "kuafu_yungong_start_produce";
    /** 活动结束阶段定时模块名称 **/
    public static final String KUAFU_YUNGONG_END_PRODUCE = "kuafu_yungong_end_produce";
    /** 活动开始标志 **/
    public static final String KUAFU_YUNGONG_ACTIVITY = "kuafu_yungong_activity";
    /** 活动场景过期定时模块名称 **/
    public static final String KUAFU_YUNGONG_STAGE_EXPIRE_PRODUCE = "kuafu_yungong_stage_expire_produce";
    /** 活动场景旗子守住产生占领公会定时模块名称 **/
    public static final String KUAFU_YUNGONG_HAS_WINNER_PRODUCE = "kuafu_yungong_has_winner_produce";
    /** 活动场景增加经验和真气定时模块名称 **/
    public static final String KUAFU_YUNGONG_ADD_EXP_ZQ_PRODUCE = "kuafu_yungong_add_exp_zq_produce";
    /** 活动场景旗子坐标同步定时模块名称 **/
    public static final String KUAFU_YUNGONG_SYN_QIZI_PRODUCE = "kuafu_yungong_syn_qizi_produce";
    /** 活动场景提前结束定时模块名称 **/
    public static final String KUAFU_YUNGONG_EARLY_END_PRODUCE = "kuafu_yungong_early_end_produce";
    /** 活动场景角色死亡强制自动复活 **/
    public static final String KUAFU_YUNGONG_BACK_FUHUO_PRODUCE = "kuafu_yungong_back_fuhuo_produce";
    
    //------------------------魔宫烈焰----------------------------------------------
    /** 怪物类型 */
    public static final int MGLY_MONSTER_TYPE = 16;
    /** 定时加经验真气 **/
    public static final String COMPONENT_MGLY_ADD_EXP_ZHENQI = "component_mgly_add_exp_zhenqi";
    /** 定时减少御魔值 **/
    public static final String COMPONENT_MGLY_CUT_YUMOVAL = "component_mgly_cut_yumoval";
    /** 御魔值值变化的锁 **/
    public static final String COMPONENT_MGLY_YUMO_SHARE = "component_mgly_yumo_share";
    /** 场景延迟踢人定时器 **/
    public static final String COMPONENT_MGLY_DELAY_KICK_OUT = "component_mgly_delay_kick_out";
    
   //------------------------五行----------------------------------------------
    /** 金类型 */
    public static final int WUXING_JIN_TYPE = 1;
    /** 木类型 */
    public static final int WUXING_MU_TYPE = 2;
    /** 土类型 */
    public static final int WUXING_TU_TYPE = 3;
    /** 水类型 */
    public static final int WUXING_SHUI_TYPE = 4;
    /** 火类型 */
    public static final int WUXING_HUO_TYPE = 5;
    
    /** 金类型 */
    public static final int WUXING_JIN_SJTYPE = 180;
    /** 木类型 */
    public static final int WUXING_MU_SJTYPE = 181;
    /** 土类型 */
    public static final int WUXING_TU_SJTYPE = 182;
    /** 水类型 */
    public static final int WUXING_SHUI_SJTYPE = 183;
    /** 火类型 */
    public static final int WUXING_HUO_SJTYPE = 184;
    
    
    //--------------------------云瑶晶脉-------------------------------------------
    /** 云瑶晶脉副本场景结束定时器常量 */
    public static final String COMPONENT_YUNYAOJINGMAI_END_PRODUCE = "component_yunyaojingmai_end_produce";
    
    
	/**  礼包码前缀  */
	public static final String GIFT_START_STR ="nzh";
	
	//--------------------投资------------------
	/**投资1*/
	public static final int TOUZHI_TYPE = 1;
	/**基金2类型*/
	public static final int JIJING_TYPE = 2;
	/**天数基金*/
	public static final int DAY_JIJIN = 1;
	/**等级基金*/
	public static final int LEVEL_JIJIN = 2;
	/** 投资邮件内容 */
	public static final String TOUZI_MAIL_MSG_CODE = "20600";
	/** 投资邮件标题 */
	public static final String TOUZI_MAIL_MSG_CODE_TITLE = "20602";
	/** 基金邮件内容 */
	public static final String JIJIN_MAIL_MSG_CODE = "20601";
	/** 基金邮件标题 */
	public static final String JIJIN_MAIL_MSG_CODE_TITLE = "20603";
	
	//--------------------限时礼包------------------
	public static final String XIANSHILIBAO_TITLE = "20690";
	public static final String XIANSHILIBAO_CONTET = "20691";
	
	/** 战斗模式**/
	public static final String BATTLE_MODE = "battle_mode";
	
	/**野外BOSS活动，对应定时刷怪表的type字段*/
	public static final int HUODONG_YEWAI_TYPE = 4;

	//泡澡排行更新记录
	public static final String COMPONENET_PAOZAO_RANK = "paozao_rank";


	/** 超值进阶返利*/
	public static final String JINJIE_FIANLI_EMAIL_TITLE = "20320";
	public static final String JINJIE_FIANLI_EMAIL_CONTENT = "20321";
	/**
	 * 重生守护buff
	 */
	public static final String SF_BUFF = "emzc";
	
	
	/**
	 * 万能进阶丹大类Id
	 */
	public static final String WND_UPJIE = "wnd01";
	
	/**
	 * 小暴击
	 */
	public static final int RATIO_1 = 1; 
	/**
	 * 大暴击
	 */
	public static final int RATIO_2 = 2; 
	public static final int RATIO_3 = 3; 
}