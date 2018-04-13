package com.junyou.err;

/**
 * 游戏错误码-全局管理减少类的创建
 * 
 * @author DaoZheng Yuan 2014年11月24日 下午5:46:02
 */
public class AppErrorCode {

	/** 成功 */
	public static final int SUCCESS = 1;
	public static final Object[] OK = new Object[] { 1 };
	/** 失败标识 */
	public static final int FAIL = 0;
	public static final Object[] ERR = new Object[] { FAIL };

	public static final Object[] EMPTY_OBJECTS = new Object[] {};

	/** 生成动态不带状态的error code */
	public static Object createError(Object code, Object... args) {
		int length = args.length;
		if (length == 0) {
			return code;
		} else {
			Object[] ret = new Object[2];
			ret[0] = code;

			Object[] argArr = new Object[args.length];
			ret[1] = argArr;

			for (int i = 0; i < length; i++) {
				argArr[i] = args[i];
			}

			return ret;
		}
	}

	/** 生成动态带状态的error code */
	public static Object[] create(int code, Object... args) {
		int length = args.length;
		Object[] ret = new Object[2 + length];
		ret[0] = 0;
		ret[1] = code;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				ret[2 + i] = args[i];
			}
		}
		return ret;
	}

	// -------------------------登陆-------------------------
	/** 被挤出游戏 */
	public static final Object[] JICHU_GAME_CODE = new Object[] { 0, 700 };
	/** 进入游戏校验失败 **/
	public static final Object[] CHECK_FAILED = new Object[] { 0, 701 };
	/** 进入游戏失败,已被封号 **/
	public static final Object[] IS_FENG = new Object[] { 0, 702 };
	/** 进入游戏失败,已被封IP号 **/
	public static final Integer IS_FENG_IP = 703;
	/** 客户端连接不合法,服务器拒绝连接 **/
	public static final Integer CLENT_ERROR = 704;
	/** 服务器爆满 **/
	public static final Object[] SERVER_IS_FULL = new Object[] { 0, "baoman" };
	/** 进入游戏校验错误 **/
	public static final Object[] TIME_CHECK_FAILED = new Object[] { 0, 707 };
	// ---------------------玩家Role code
	// start-------------------------------------
	/** 角色名称不合法 **/
	public static final Object[] ROLE_NAME_NO_ALLOW = new Object[] { 0, 750 };
	/** 角色已创建 **/
	public static final Object[] CANNOT_CREATE_ROLE = new Object[] { 0, 751 };
	/** 角色名称长度不符合，过长或过短 **/
	public static final Object[] NAME_LENGTH_ERROR = new Object[] { 0, 752 };
	/** 配置错误 **/
	public static final Object[] JOB_ERROR = new Object[] { 0, 753 };
	/** 角色不存在 **/
	public static final Object[] ROLE_IS_NULL = new Object[] { 0, 754 };
	/** 角色已删除 **/
	public static final Object[] ROLE_IS_DELETE = new Object[] { 0, 755 };
	/** 创角服务器未知异常 **/
	public static final Object[] CREATE_EXCEPTION = new Object[] { 0, 756 };
	/** 角色名已被使用 **/
	public static final Object[] ROLE_IS_EXIST = new Object[] { 0, 764 };
	/** 角色不在线*/
	public static final Object[] ROLE_IS_NOT_ONLINE = new Object[] { 0, 765 };
	/** 名称有刷YY的嫌疑，请您再换个名字*/
	public static final Object[] NAME_S_YY = new Object[] { 0, 766 };
	/** 角色信息未找到 **/
	public static final Object[] ROLE_INFO_NOT_FOUND = new Object[] { 0, 767 };

	// ---------------------玩家Recharge code
	// start-----------------------------------
	/** 领取成功 */
	public static final int RECIVE_SUCCESS = 800;
	/** 充值成功 **/
	public static final int RECHARGE_SUCCESS = 801;
	/** 账号不存在 **/
	public static final int ACCOUNT_NOT_EXSIT = 802;
	/** 充值小于零 **/
	public static final int LESS_THAN_ZERO = 803;
	/** 订单号重复 **/
	public static final int ORDER_REPEAT = 804;
	/** 充值业务异常 **/
	public static final int BUS_EXCEPTION = 805;
	/** 异常订单 **/
	public static final int ERROR_ORDER = 806;
	/** 元宝超过上限 **/
	public static final int ERROR_YB_OVER_MAX = 807;
	/** 账号不匹配 **/
	public static final int ACCOUNT_NOT_THIS_ROLE = 808;
	// --------------------腾讯的充值 ---------------------

	public static final int TENGXUN_SUCCESS = 0;
	/** 您购买的道具给您邮寄过来了! */
	public static final String QDIAN_ZHIGOU = "15627";
	/** Q点直购 */
	public static final String QDIAN_ZHIGOU_EMAIL_TITLE = "15658";
	/** 今日已经没有购买次数了! */
	public static final Object[] NOT_BUY_COUNT = new Object[] { 0, 15743 };

	// ---------------------通用------------------------------------
	/** 玩家角色不存在 **/
	public static final Object[] ROLE_IS_NOT_EXIST = new Object[] { 0, 901 };
	/** 玩家职业不符合 **/
	public static final Object[] ROLE_JOB_NOT_NEED = new Object[] { 0, 902 };
	/** 所需物品不存在 **/
	public static final Object[] GOODS_NOT_ENOUGH = new Object[] { 0, 904 };
	/** 玩家等级不足 **/
	public static final Object[] ROLE_LEVEL_NOT_ENOUGH = new Object[] { 0, "nolevel" };
	/** 金币不足 **/
	public static final Object[] GOLD_NOT_ENOUGH = new Object[] { 0, "nomoney" };
	/** 元宝不足 **/
	public static final Object[] YUANBAO_NOT_ENOUGH = new Object[] { 0, "nogold" };
	/** 绑定元宝不足 **/
	public static final Object[] BANG_YUANBAO_NOT_ENOUGH = new Object[] { 0, "nobgold" };
	/** 配置异常 **/
	public static final Object[] CONFIG_ERROR = new Object[] { 0, AppErrorCode.CONFIG_ERROR_NUMBER };
	public static final int CONFIG_ERROR_NUMBER = 908;
	/** 已打到满阶 **/
	public static final Object[] IS_MAX_LEVEL = new Object[] { 0, 911 };
	/**等级不足**/
	public static final Object[] LEVEL_NOT_ENOUGH = new Object[] { 0, 914 };
	/**道具不足**/
	public static final Object[] ITEM_NOT_ENOUGH = new Object[] { 0, 915 };
	/**道具错误**/
	public static final Object[] ITEM_NOT_ERROR = new Object[] { 0, 916 };
	/**功能未开启**/
	public static final Object[] FUNCTION_NOT_OPEN = new Object[] { 0, 917 };
	/**功能未激活**/
	public static final Object[] FUNCTION_NOT_ACTIVE = new Object[] { 0, 918 };
	/**潜能丹使用达到上限**/
	public static final Object[] IS_MAX_QND = new Object[] { 0, 919 };
	/**成长丹使用达到上限**/
	public static final Object[] IS_MAX_CZD = new Object[] { 0, 920 };
	/**已领取**/
	public static final Object[] GET_ALREADY = new Object[] { 0, 921 };
	/**已达到活动参数最大次数*/
	public static final Object[] ACTITY_MAX_COUNT = new Object[] { 0, 922 };
	/**数据错误*/
	public static final Object[] DATA_ERROR = new Object[] { 0, 924 };

	// ---------------------场景code start----------------------------------------
	/** 地图找不到了,不能传送 */
	public static final Object[] TELEPORT_ERROR = new Object[] { 0, 2100 };
	/** 进入地图需要需要消耗的游戏币不足够,不能传送 **/
	public static final Object[] TELEPORT_MONEY_ERROR = new Object[] { 0, 2101 };
	/** 进入地图需要需要消耗的礼券不足够,不能传送 **/
	public static final Object[] TELEPORT_LIQUAN_ERROR = new Object[] { 0, 2102 };
	/** 你的等级大于进入地图最大等级,不能传送 **/
	public static final Object[] TELEPORT_MAX_LEVEL_ERROR = new Object[] { 0, 2103 };
	/** 进入地图需要官职太低,不能传送 **/
	public static final Object[] TELEPORT_GZ_ERROR = new Object[] { 0, 2104 };
	/** 进入地图的时间还没到,不能进入 **/
	public static final Object[] TELEPORT_START_TIME_ERROR = new Object[] { 0, 2105 };
	/** 进入地图的时间已过,不能进入 **/
	public static final Object[] TELEPORT_END_TIME_ERROR = new Object[] { 0, 2106 };
	/** 进入地图需要的VIP等级高于你的等级,不能进入 **/
	public static final Object[] TELEPORT_VIP_ERROR = new Object[] { 0, 2107 };
	/** 进入地图需要需要的等级不足够,不能传送 **/
	public static final Object[] TELEPORT_MIN_LEVEL_ERROR = new Object[] { 0, 2108 };
	/** 进入地图需要在公会内 **/
	public static final Object[] TELEPORT_GUILD_ERROR = new Object[] { 0, 2109 };
	/** 进入地图需要拥有物品不足,不能传送 **/
	public static final Object[] TELEPORT_MUST_ITEM_ERROR = new Object[] { 0, 2110 };
	/** 进入地图需要消耗的物品不足,不能传送 **/
	public static final Object[] TELEPORT_NEED_ITEM_ERROR = new Object[] { 0, 2111 };
	/** 传送时间错误 */
	public static final Object[] TELEPORT_TIME_ERROR = new Object[] { 0, 2120 };
	/** 场景不存在 */
	public static final Object[] STAGE_IS_NOT_EXIST = new Object[] { 0, 2114 };
	/** 副本内无法换线 */
	public static final Object[] COPY_CANNOT_CHANGE_LINE = new Object[] { 0, 2115 };
	/** 分线不存在 */
	public static final Object[] LINE_IS_NULL = new Object[] { 0, 2116 };
	/** 已在此线 */
	public static final Object[] IS_SAME_LINE = new Object[] { 0, 2117 };
	/** 分线已满 */
	public static final Object[] LINE_IS_FULL = new Object[] { 0, 2118 };
	// /**角色不存在*/
	// public static final Object[] ROLE_IS_NOT_EXIST = new Object[]{0, 2119};
	/** 飞鞋的目标不在地球上，不能进入 **/
	public static final int TELEPORT_FLY_CONFG = 2121;
	/** 飞鞋的目标点没有可以立身的坐标，不能进入 **/
	public static final int TELEPORT_FLY_ZB_CONFG = 2122;
	/** 您今日的飞鞋次数已经用完了，不能进入 **/
	public static final int TELEPORT_FLY_TIMES = 2123;
	/** 副本内无法切换地图 */
	public static final Object[] COPY_CANNOT_CHANGE_MAP = new Object[] { 0, 2133 };
	/** 不在活动期间 */
	public static final Object[] NOT_IN_TIME_CANNOT_CHANGE_MAP = new Object[] { 0, 2134 };
	/** 活动不支持切换地图 */
	public static final Object[] ACTIVE_CANNOT_CHANGE_MAP = new Object[] { 0, 2135 };
	/** 飞鞋次数不足 **/
	public static final int TELEPORT_FLY_NO_ITEMS = 2137;
	/** 传送点不存在，无法传送 **/
	public static final int TELEPORT_ERROR_NO_TELEPORT = 2139;
	/** 角色不存在，无法传送 **/
	public static final int TELEPORT_ERROR_NO_ROLE = 2140;
	/** 死亡状态无法传送 **/
	public static final int TELEPORT_ERROR_ROLE_DEAD = 2141;
	/** 传送点不在当前地图，无法传送 **/
	public static final int TELEPORT_ERROR_CUR_MAP_NO_TELEPORT = 2142;
	/** 死亡状态无法换线 **/
	public static final Object[] TELEPORT_CHANGE_LINE_ERROR_ROLE_DEAD = new Object[] { 0, 2143 };

	// ---------------------背包 code
	// start------------------------------------------
	/** 物品数量为空 **/
	public static final Object[] GOODSID_COUNT_ISNULL = new Object[] { 0, 1007 };
	/** 背包空间不足 **/
	public static final Object[] BAG_NOEMPTY = new Object[] { 0, 1008 };
	/** 该容器暂时没有开发背包整理功能 */
	public static final Object[] NO_RESET_EMPTY = new Object[] { 0, 1009 };
	/** 使用物品数量不对 **/
	public static final Object[] ITEM_COUNT_ENOUGH = new Object[] { 0, 1010 };
	/** 目标格位不存在 **/
	public static final Object[] NO_TARGET_SLOT = new Object[] { 0, 1011 };
	/** 仓库空间不够 **/
	public static final Object[] STORAGE_NOEMPTY = new Object[] { 0, 1012 };
	/** 装备没有可装备的位置 **/
	public static final Object[] NO_POSITION_EQUIPMENT = new Object[] { 0, 1013 };
	/** 配置不存在 **/
	public static final Object[] NO_FIND_CONFIG = new Object[] { 0, 1014 };
	/** 每日限制使用次数以用完 **/
	public static final Object[] NO_USE_COUNT = new Object[] { 0, 1015 };
	/** 格位不存在 **/
	public static final Object[] NO_FIND_SLOT = new Object[] { 0, 1016 };
	/** 该移动类型不存在 */
	public static final Object[] NO_OPERATION_MOVE = new Object[] { 0, 1017 };
	/** 原物品或指定格位没有物品 */
	public static final Object[] NO_SOURCE_TARGET = new Object[] { 0, 1018 };
	/** 有效时间不同，不能合并 */
	public static final Object[] NOT_SAME_EXPIRETIME = new Object[] { 0, 1019 };
	/** 不是同一类物品的绑定非绑定合并 */
	public static final Object[] NOT_SAME_ID1 = new Object[] { 0, 1020 };
	/** 拆分数量错误 */
	public static final Object[] CF_COUNT_ERROR = new Object[] { 0, 1021 };
	/** 没有空格位 */
	public static final Object[] NO_EMPTY_SLOT = new Object[] { 0, 1022 };
	/** 物品不能这样移动 **/
	public static final Object[] NOT_MOVE = new Object[] { 0, 1023 };
	/** 添加了空物品 **/
	public static final Object[] ADD_EMPTY_ITEM = new Object[] { 0, 1024 };
	/** 装备没有可装备的位置 **/
	public static final Object[] POSITION_EQUIP_ERR = new Object[] { 0, 1025 };
	/** 该格位没有物品 **/
	public static final Object[] POSITION_NO_EQUIP = new Object[] { 0, 1026 };
	/** 背包所有格位都已开启 */
	public static final Object[] BAG_ALL_SLOT_OPEN = new Object[] { 0, 1027 };
	/** 开启的格位号有误 */
	public static final Object[] BAG_OPEN_SLOT_ERROR = new Object[] { 0, 1028 };
	/** 开启格位元宝不足 */
	public static final Object[] YB_NOT_ENOUGHT = new Object[] { 0, 1029 };
	/** 仓库所有格位都已开启 */
	public static final Object[] STORAGE_ALL_SLOT_OPEN = new Object[] { 0, 1030 };

	/** 物品不存在 **/
	public static final Object[] NOT_FOUND_GOOODS = new Object[] { 0, 1057 };
	/** 格位配置表异常 */
	public static final Object[] BAG_SLOT_CONFIG_ERROR = new Object[] { 0, 1058 };
	/** 物品已过期,不能再使用了 **/
	public static final Object[] IS_EXPIRE_GOODS = new Object[] { 0, 1059 };
	/** 死亡状态不能使用道具 **/
	public static final Object[] DEAD_NO_USE_PROP = new Object[] { 0, 1060 };
	/** 已经是最大属性了,不可使用 **/
	public static final Object[] NUMBER_MAX = new Object[] { 0, 1061 };
	/** 数值错误 **/
	public static final Object[] NUMBER_ERROR = new Object[] { 0, 1062 };
	/*** 绑定物品不可丢弃 */
	public static final Object[] NO_GIVE_BIND = new Object[] { 0, 1063 };
	/** 丢弃物品失败 */
	public static final Object[] BAG_GVIEUP_FAIL = new Object[] { 0, 1064 };
	/** 拾取物品失败 */
	public static final Object[] NO_PICK_UP = new Object[] { 0, 1065 };
	/** 入背包物品类型错误 */
	public static final Object[] GOODS_TYPE_ERROR = new Object[] { 0, 1066 };
	/** 没有当前的物品,整理失败 */
	public static final Object[] RESET_ERROR = new Object[] { 0, 1067 };
	/** 需要消耗的物品对象为null */
	public static final Object[] REMOVE_TARGET_NULL = new Object[] { 0, 1068 };
	/** 物品不可出售 */
	public static final Object[] GOODS_NO_SELL = new Object[] { 0, 1069 };
	/** 物品正在冷却中 */
	public static final Object[] GOODS_IS_IN_CD = new Object[] { 0, 1080 };
	/** 链接已失效 */
	public static final Object[] LINK_IS_OUTTIME = new Object[] { 0, 999 };

	// ---------------------背包 code
	// end------------------------------------------

	/** 访问该模块检查是否已经关闭提示 */
	public static final Integer MODEL_CLOSE_CHECK = 912;
	// ---------------------------邮件code--------------------------------------
	/** 没有邮件 */
	public static final Object[] EMAIL_NO = new Object[] { 0, 2505 };
	/** 没有可领取附件 */
	public static final Object[] EMAIL_NO_ATT = new Object[] { 0, 2506 };
	/** 没有选择删除邮件 */
	public static final Object[] EMAIL_NO_SELECT_DEL = new Object[] { 0, 2507 };
	/** 邮件已过期 */
	public static final Object[] EMAIL_IS_EXPIRE = new Object[] { 0, 2508 };
	/** 邮件已删除 */
	public static final Object[] EMAIL_IS_DELETE = new Object[] { 0, 2509 };
	/** 系统补发给您发的邮件，我们需要暂时锁死从发邮时间算24小时后才可以领取 */
	public static final Object[] EMAIL_LOCK_24 = new Object[] { 0, 2515 };
	/** 系统补发给您发的邮件，我们需要暂时锁死从发邮时间算1小时后才可以领取 */
	public static final Object[] EMAIL_LOCK_1 = new Object[] { 0, 2516 };
	/** 邮件不存在 */
	public static final Object[] EMAIL_NO_DELETE = new Object[] { 0, 2521 };
	/** 选择的邮件中有附件未领取，不可删除*/
	public static final Object[] EMAIL_FUJIAN_NO_DELETE = new Object[] { 0, 2522 };
	/** 系统邮件*/
	public static final String EMAIL_SERVER_TITLE = "2520";
	/** 附件已领取过*/
	public static final Object[] EMAIL_FUJIAN_YI_LINGQU = new Object[]{0,2528};

	// ---------------------------公会code-----------------------------------------
	/** 成功退出公会 */
	public static final Object[] GUILD_EXIT_SUCCESS = new Object[] { 1, 0, null };
	/** 角色不存在 */
	public static final Object[] ROLE_NOT_EXIST = new Object[] { 0, 1347 };
	/** 角色已有公会 */
	public static final Object[] ROLE_HAS_GUILD = new Object[] { 0, 1337 };
	/** 角色等级不足 */
	public static final Object[] ROLE_LEVEL_ERROR = new Object[] { 0, 1348 };
	/** 角色没有公会 */
	public static final Object[] ROLE_NO_GUILD = new Object[] { 0, 1349 };
	/** 没有选择审批目标 */
	public static final Object[] GUILD_NO_SHENHE_TARGET = new Object[] { 0, 1350 };
	/** 公会名长度错误 */
	public static final Object[] GUILD_NAME_LENGTH_ERROR = new Object[] { 0, 1351 };
	/** 公会名不合法 */
	public static final Object[] GUILD_NAME_ERROR = new Object[] { 0, 1352 };
	/** 公告不合法 */
	public static final Object[] GUILD_GONGGAO_ERROR = new Object[] { 0, 1353 };
	/** 公会名重复 */
	public static final Object[] GUILD_NAME_REPEAT = new Object[] { 0, 1354 };
	/** 公会不存在 */
	public static final Object[] GUILD_IS_NOT_EXIST = new Object[] { 0, 1355 };
	/** 公会已满 */
	public static final Object[] GUILD_IS_FULL = new Object[] { 0, 1356 };
	/** 不满足入会条件 */
	public static final Object[] GUILD_ZHAOSHOU_NOT = new Object[] { 0, 1357 };
	/** 权限不足 */
	public static final Object[] GUILD_NO_QUANXIAN = new Object[] { 0, 1358 };
	/** 不在同一个公会 */
	public static final Object[] GUILD_NOT_SAME_GUILD = new Object[] { 0, 1359 };
	/** 职位已达到最大人数 */
	public static final Object[] GUILD_MAX_COUNT = new Object[] { 0, 1360 };
	/** 招收条件不存在 */
	public static final Object[] GUILD_TYPE_NO_EXIT = new Object[] { 0, 1361 };
	/** 不是只有会长不能解散公会 */
	public static final Object[] GUILD_NOT_ONLY_LEADER = new Object[] { 0, 1346 };
	/** 操作失败 */
	public static final Object[] GUILD_OPERATE_FAIL = new Object[] { 0, 1378 };
	/** 开服N天后才可以修改开服公告 */
	public static final int GUILD_KAIFU_DAYS_CHANGE_GONGGAO = 1379;
	/** 已达到满级 */
	public static final Object[] GUILD_IS_MAX_LEVEL = new Object[] { 0, 1421 };
	/** 升级所需门派资金不足 */
	public static final Object[] GUILD_NO_ENOUGH_MONEY = new Object[] { 0, 1422 };
	/** 升级所需门派令牌1不足 */
	public static final Object[] GUILD_NO_ENOUGH_ITEM1 = new Object[] { 0, 1423 };
	/** 升级所需门派令牌2不足 */
	public static final Object[] GUILD_NO_ENOUGH_ITEM2 = new Object[] { 0, 1424 };
	/** 升级所需门派令牌3不足 */
	public static final Object[] GUILD_NO_ENOUGH_ITEM3 = new Object[] { 0, 1425 };
	/** 升级所需门派令牌4不足 */
	public static final Object[] GUILD_NO_ENOUGH_ITEM4 = new Object[] { 0, 1426 };
	/** 升级所需门派旗帜等级不足 */
	public static final Object[] GUILD_NO_ENOUGH_LEVEL = new Object[] { 0, 1427 };
	/** 门派兑换次数不足 */
	public static final Object[] GUILD_NO_DUIHUAN_COUNT = new Object[] { 0, 1434 };
	/** 门派兑换贡献不足 */
	public static final Object[] GUILD_NO_ENOUGH_GONG = new Object[] { 0, 1435 };
	/** 门派阁楼等级不足 */
	public static final Object[] GUILD_NO_ENOUGH_GELOU = new Object[] { 0, 1436 };
	/** 门主在线不能弹劾 */
	public static final Object[] GUILD_NO_IMPEACH_1 = new Object[] { 0, 1471 };
	/**弹劾期内门主有登陆，不能弹劾 */
	public static final Object[] GUILD_NO_IMPEACH_2 = new Object[] { 0, 1455 };
	/**皇城战期间，不能弹劾 */
	public static final Object[] GUILD_NO_IMPEACH_3 = new Object[] { 0, 1472 };
	/**领地战期间，不能弹劾 */
	public static final Object[] GUILD_NO_IMPEACH_4 = new Object[] { 0, 1473 };
	/**跨服云宫之巅期间，不能弹劾 */
	public static final Object[] GUILD_NO_IMPEACH_5 = new Object[] { 0, 1477 };
	/**没有弹劾道具 */
	public static final Object[] GUILD_NO_IMPEACH_ITEM = new Object[] { 0, 1456 };
	/**服务器,程序异常，请稍后再试*/
	public static final Object[] GUILD_IMPEACH_ERROR = new Object[] { 0, 1474 };
	/**弹劾成功邮件 **/
	public static final String GUILD_IMPEACH_SUCCESS_EMAIL = "1475";
	/**弹劾成功邮件标题 **/
	public static final String GUILD_IMPEACH_SUCCESS_EMAIL_TITLE = "1485";

	// -------------------------队伍code------------------------------------
	/** 场景不存在 */
	public static final Object[] TEAM_STAGE_IS_NULL = new Object[] { 0, 1146 };
	/** 角色不存在 */
	public static final Object[] TEAM_ROLE_IS_NULL = new Object[] { 0, 1147 };
	/** 已有队伍 */
	public static final Object[] TEAM_HAS_TEAM = new Object[] { 0, 1148 };
	/** 不是队长 */
	public static final Object[] TEAM_IS_NOT_LEADER = new Object[] { 0, 1149 };
	/** 队伍已满 */
	public static final Object[] TEAM_IS_FULL = new Object[] { 0, 1150 };
	/** 对方不在线 */
	public static final Object[] TEAM_ROLE_IS_OFFLINE = new Object[] { 0, 1151 };
	/** 对方已有队伍 */
	public static final Object[] TEAM_ROLE_HAS_TEAM = new Object[] { 0, 1152 };
	/** 队伍不存在 */
	public static final Object[] TEAM_IS_NOT_EXIST = new Object[] { 0, 1153 };
	/** 不在同一队伍 */
	public static final Object[] TEAM_NOT_SAME_TEAM = new Object[] { 0, 1154 };
	/** 对方不是队长 */
	public static final Object[] TEAM_ROLE_IS_NOT_LEADER = new Object[] { 0, 1155 };
	/** 主动离开队伍 */
	public static final Object[] TEAM_LEAVE_TEAM = new Object[] { 1, true };
	/** 被T出队伍 */
	public static final Object[] TEAM_BE_KICK_TEAM = new Object[] { 1, false };
	/** 不能任命自己 */
	public static final Object[] TEAM_CHANGE_LEADER_SELF = new Object[] { 0, 1158 };
	/** 副本中无法组队 */
	public static final Object[] TEAM_ERROR_IN_FUBEN = new Object[] { 0, 2786 };

	// -----------------------------聊天--------------------------------------------
	/** 请输入内容 */
	public static final Object[] MSG_NULL = new Object[] { 0, 2003 };
	/** 内容太长 */
	public static final Object[] CHAT_TOO_LEN = new Object[] { 0, 2004 };
	/** 对方不在线 */
	public static final Object[] ROLE_NOT_LOGIN = new Object[] { 0, 2005 };
	/** 不能和自己聊天 */
	public static final Object[] NO_CHAT_SELF = new Object[] { 0, 2006 };
	/** 您黑名单中已有该玩家，不能私聊 */
	public static final Object[] BLACKIDS_CONTAIN = new Object[] { 0, 2008 };

	// -----------------------------好友-------------------------------------------
	/** 列表类型有误，不支持 */
	public static final Object[] FRIEND_TYPE_ERROR = new Object[] { 0, 1939 };
	/** 对方不存在 */
	public static final Object[] F_DATA_ERROR = new Object[] { 0, 1940 };
	/** 对方不在线 */
	public static final Object[] F_NO_ONLINE = new Object[] { 0, 1941 };
	/** 不能添加自己 */
	public static final Object[] FRIEND_NOT_ADD = new Object[] { 0, 1942 };
	/** 已是好友 */
	public static final Object[] ROLE_EXISTS_FRIENDS = new Object[] { 0, 1943 };
	/** 列表人数已满 */
	public static final Object[] FRIEND_MAX_COUNT = new Object[] { 0, 1944 };
	/** 不支持该操作,添加列表类型错误 */
	public static final Object[] F_NO_SUPPORT = new Object[] { 0, 1945 };
	/** 好友中没有该玩家 */
	public static final Object[] ROLE_NO_EXISTS_FRIENDS = new Object[] { 0, 1946 };
	/** 黑名单中没有该玩家 */
	public static final Object[] ROLE_NO_EXISTS_BLACKS = new Object[] { 0, 1947 };
	/** 仇人中没有该玩家 */
	public static final Object[] ROLE_NO_EXISTS_ENEMYS = new Object[] { 0, 1948 };
	/** 该玩家不存在 */
	public static final Object[] F_NO_FIND = new Object[] { 0, 1949 };
	/** 自己不能添加自己为仇人 */
	public static final Object[] ENEMYLIST_NOT_ADD = new Object[] { 0, 1950 };
	/** 仇人信息错误 */
	public static final Object[] ROLE_NOTEXISTS = new Object[] { 0, 1951 };
	/** 黑名单已经存在该玩家 */
	public static final Object[] ROLE_EXISTS_HEIS = new Object[] { 0, 1952 };

	// -----------------------------技能--------------------------------
	/** 技能配置异常 */
	public static final Object[] SKILL_CONFIG_ERROR = new Object[] { 0, 1194 };
	/** 技能尚未学习 */
	public static final Object[] SKILL_NOT_LEARN = new Object[] { 0, 1195 };
	/** 技能已满级 */
	public static final Object[] SKILL_IS_MAX_LEVEL = new Object[] { 0, 1196 };
	/** 真气不足 */
	public static final Object[] SKILL_NOT_ENOUGH_ZHENQI = new Object[] { 0, 1197 };
	/** 技能已经学习 */
	public static final Object[] SKILL_HAS_LEARN = new Object[] { 0, 1199 };
	/**格位已激活*/
	public static final Object[] SKILL_GEWEI_YI_JIHUO = new Object[] { 0, 18902 };
	/**请先激活前一个格位*/
	public static final Object[] SKILL_GEWEI_YI_JIHUO_QIAN = new Object[] { 0, 18903 };
	/**格位未激活*/
	public static final Object[] SKILL_GEWEI_WEI_JIHUO = new Object[] { 0, 18904 };
	/**技能已装备在格位上 */
	public static final Object[] SKILL_IS_ZHUANGBEI = new Object[] { 0, 18921 };
	// ---------------------交易----------------------
	/** 自己已在交易中 */
	public static final int ROLE_SELF_TRADEING = 1081;
	/** 对方在交易中 */
	public static final Integer ROLE_TRADEING = 1082;
	/** 【{0}】的交易状态异常，交易中断。 */
	public static final int ROLE_STATE_TRADE_ERROR = 1083;
	/** 交易物品不存在 */
	public static final int TRADE_GOODS_NO_EXIST = 1084;
	/** 交易的物品是绑定物品 */
	public static final int TRADE_GOODS_BIND = 1085;
	/** 交易格索引号错误 */
	public static final int TRADE_INDEX_ERROR = 1086;
	/** 交易被拒绝带名字 */
	public static final int TRADE_NAME_REJECT = 1087;
	/** 交易数据背包放不下 */
	public static final int TRADE_BAG_NO_SLOT = 1088;
	/** 交易异常，服务器中断交易 */
	public static final int TRADE_EXCEPTION = 1089;
	/** 交易角色不在线 */
	public static final int TRADE_ROLE_NO_ONLINE = 1090;
	/** 物品已过期,不能交易 */
	public static final int EXPIRE_ERROR = 1091;
	/** 很抱歉，您交易的物品数量不确认 */
	public static final int TRADE_GOODS_COUNT_ERROR = 1092;
	/** 交易物品重复,不能交易 */
	public static final int CONTAINS_ERROR = 1093;
	/** 背包物品交换失败 */
	public static final int GOODS_TRADE_ERROR = 1094;
	/** 玩家【{0}】下线，交易取消 **/
	public static final int ROLE_OFF_LINE = 1095;
	/** 对方玩家不在附近，不可交易 */
	public static final int TRADE_ROLE_NO_NEARBY = 1096;
	/** 对方已经被别人邀请,邀请失败 */
	public static final int TRADE_TARGET_NO_MUCH = 1097;
	/** 玩家【{0}】元宝交易后超过上限,交易取消 */
	public static final int TRADE_GOLD_MAX = 1098;
	/** 您已邀请，请耐心等待。 */
	public static final int TRADE_TARGET_LUNCHED = 1099;
	/** 玩家【{0}】取消交易 **/
	public static final int TRADE_CANCEL_CODE = 1110;

	// ---------------------玩家Account code-------------------------------------
	/** 货币类型错误 **/
	public static final Object[] ENOUGHT_ERROR = new Object[] { 0, 758 };
	/** 元宝不足 **/
	public static final Object[] YB_ERROR = new Object[] { 0, "nogold" };
	/** 金币不足 **/
	public static final Object[] JB_ERROR = new Object[] { 0, "nomoney" };
	/** 礼券不足 **/
	public static final Object[] LQ_ERROR = new Object[] { 0, "nobgold" };
	/** 数据错误,消耗或增加数据为负值 **/
	public static final Object[] MONEY_DATA_ERROR = new Object[] { 0, 759 };
	/** 真气不足 **/
	public static final Object[] ZHEN_ERROR = new Object[] { 0, 760 };
	/** 跳闪值不足 **/
	public static final Object[] TIAOSHAN_ERROR = new Object[] { 0, 761 };
	/** 元宝被交易系统暂时锁定,请不要在交易状态时使用元宝 **/
	public static final Object[] TRADE_YB_ERROR = new Object[] { 0, 762 };
	/** 亲爱的“花千骨”玩家，您充值的{0}元宝已经安全进入到您的帐号中，希望您游戏愉快！ **/
	public static final int YB_IN_ACCOUNT = 763;

	// ---------------------------装备code--------------------------------------
	/** 装备强化等级已满 **/
	public static final Object[] EQUIP_MAX_LEVEL = new Object[] { 0, 2601 };
	/** 强化所需金币不足 **/
	public static final Object[] QH_JB_BZ = new Object[] { 0, 2602 };
	/** 强化的装备不存在 **/
	public static final Object[] NO_QH_EQUIP = new Object[] { 0, 2603 };
	/** 开启格子时间没到 **/
	public static final Object[] TIME_NO_ENOUGH = new Object[] { 0, 2604 };
	/** 身上没有指定对应的装备 */
	public static final Object[] BODY_NO_ITEM = new Object[] { 0, 2605 };
	/** 没有可回收的装备 */
	public static final Object[] NO_HC_GOODS = new Object[] { 0, 2606 };
	/** 装备强化等级错误 */
	public static final Object[] EQUIP_LEVEL_ERROR = new Object[] { 0, 2607 };
	/** 装备需求等级不足 */
	public static final Object[] REQ_LEVEL_BZ = new Object[] { 0, 2608 };
	/** 装备需求职业错误 */
	public static final Object[] JOB_EQUIP_ERR = new Object[] { 0, 2609 };
	/** 装备目标强化等级错误 */
	public static final Object[] TARGET_LEVEL_ERROR = new Object[] { 0, 2610 };
	/** 该装备不可强化 */
	public static final Object[] NO_QH = new Object[] { 0, 2611 };
	/** 强化石不足 */
	public static final Object[] QIANGHUA_GOODS_ERROR = new Object[] { 0, 2635 };
	/** 玄铁兑换配置异常 */
	public static final Object[] XUANTIE_CONFIG_ERROR = new Object[] { 0, 2661 };
	/** 今日可兑换次数不足 */
	public static final Object[] XUANTIE_NOT_COUNT = new Object[] { 0, 2662 };
	/** 您的玄铁值不足 */
	public static final Object[] XUANTIE_NOT_VALUE = new Object[] { 0, 2663 };
	/** 您的等级不够，不能兑换 */
	public static final Object[] XUANTIE_NOT_LEVEL = new Object[] { 0, 2664 };
	/** 升级的装备不存在 */
	public static final Object[] SJ_ZHUANBEI_NOT = new Object[] { 0, 2666 };
	/** 没有找到配置 */
	public static final Object[] SJ_ZHUANBEI_NOT_CONFIG = new Object[] { 0, 2667 };
	/** 装备不能升阶 */
	public static final Object[] ZHUANGBEI_NOT_SJ = new Object[] { 0, 2668 };
	/** 升级后的装备大于人物等级，不可升级 */
	public static final Object[] ZHUANGBEI_NOT_LEVEL = new Object[] { 0, 2669 };
	/** 装备升级配置错误 */
	public static final Object[] SHENGJI_CONFIG_ERROR = new Object[] { 0, 2670 };
	/** 升级石不足 */
	public static final Object[] SHENGJI_GOODS_ERROR = new Object[] { 0, 2679 };
	/** 升级失败 */
	public static final Integer SHENGJI_SHIBEI = 2671;
	/** 装备不可提品 */
	public static final Object[] EQUIP_CANNOT_TP = new Object[] { 0, 2683 };
	/** 提品的装备不存在 */
	public static final Object[] TP_ZHUANBEI_NOT = new Object[] { 0, 2684 };
	/** 提品道具不足 */
	public static final Object[] TP_ITEM_NOT_ENOUGH = new Object[] { 0, 2685 };
	/** 等阶不足，无法装备 */
	public static final Object[] EQUIP_NO_ENOUGH_LEVEL = new Object[] { 0, 2697 };
	/** 升级后的装备所需等阶大于当前等阶，不可升级 */
	public static final Object[] ZHUANGBEI_NOT_ENOUGH_LEVEL = new Object[] { 0, 2600 };
	/** 附属装备升级失败 */
	public static final Object[] FUSHU_SHENGJI_SHIBEI = new Object[] { 2, 2671 };
	/** 装备需求转生等级不足 */
	public static final Object[] REQ_ZS_LEVEL_BZ = new Object[] { 0, 2599 };
	/** 铸神的装备不存在 **/
	public static final Object[] NO_ZS_EQUIP = new Object[] { 0, 17902 };
	/** 铸神等级已满 **/
	public static final Object[] ZHUSHEN_MAX_LEVEL = new Object[] { 0, 17903 };
	/** 只有套装可以铸神 */
	public static final Object[] NO_ZHUSHEN = new Object[] { 0, 17904 };
	/** 套装铸神等级错误 */
	public static final Object[] EQUIP_ZHUSHEN_LEVEL_ERROR = new Object[] { 0, 17905 };
	/** 目标装备不是套装 */
	public static final Object[] EQUIP_TYPE_ERROR = new Object[] { 0, 17930 };
	/**装备不存在 */
	public static final Object[] EQUIP_NOT = new Object[] { 0, 17931 };
	/**原始装备强化等级小于待传承装备强化等级，不可传承 */
	public static final Object[] EQUIP_NOT_QIANGHUA_LEVEL = new Object[] { 0, 17932 };
	/**待传承的装备不可强化 */
	public static final Object[] EQUIP_NOT_QIANGHUA = new Object[] { 0, 17933 };

	// ----------------------------副本-------------------------------------
	/** 当前状态不可挑战副本 */
	public static final Object[] FUBEN_STATE_ERROR_TZ = new Object[] { 0, 2709 };
	/** 已在副本中 */
	public static final Object[] FUBEN_IS_IN_FUBEN = new Object[] { 0, 2710 };
	/** 副本挑战次数不足 */
	public static final Object[] FUBEN_NO_COUNT = new Object[] { 0, 2711 };
	/** 副本尚未完成 */
	public static final Object[] FUBEN_NOT_FINISH = new Object[] { 0, 2712 };
	/** 不在副本中 */
	public static final Object[] FUBEN_NOT_IN_FUBEN = new Object[] { 0, 2713 };
	/** 当前状态不可扫荡副本 */
	public static final Object[] FUBEN_STATE_ERROR_SD = new Object[] { 0, 2714 };
	/** 尚未通关过的副本不可扫荡 */
	public static final Object[] FUBEN_NOT_TONGGUAN = new Object[] { 0, 2715 };
	/** 当前没有可扫荡的副本 */
	public static final Object[] FUBEN_NO_CAN_SD = new Object[] { 0, 2716 };
	/** 副本重置次数不足 */
	public static final Object[] FUBEN_NO_RESET_COUNT = new Object[] { 0, 2762 };
	/** 副本尚未通关 */
	public static final Object[] FUBEN_HAS_NO_TONGGUAN = new Object[] { 0, 2763 };
	/** 当前关卡无首次通关奖励 */
	public static final Object[] FUBEN_CUR_NO_TONGGUAN_GIFT = new Object[] { 0, 2764 };
	/** 当前关卡首次通关奖励已领取 */
	public static final Object[] FUBEN_TONGGUAN_GIFT_IS_RECIVED = new Object[] { 0, 2765 };
	/** 当前层不可挑战 */
	public static final Object[] FUBEN_NOW_CENG_CANNOT_TIAOZHAN = new Object[] { 0, 2784 };
	/** 今日已无可购买次数 */
	public static final Object[] FUBEN_TODAY_NO_BUY_COUNT = new Object[] { 0, 2785 };
	/**本关已通过*/
	public static final Object[] FUBEN_FINISHED_ALREADY = new Object[] { 0, 2792 };

	// -----------------------------任务-------------------------------------
	/** 任务已不存在 */
	public static final Object[] TASK_NOT_EXIST = new Object[] { 0, 3003 };
	/** 任务已领取 */
	public static final Object[] TASK_IS_RECIVE = new Object[] { 0, 3004 };
	/** 任务尚未完成 */
	public static final Object[] TASK_NOT_FINISH = new Object[] { 0, 3005 };

	// ----------------------------日常任务-------------------------------------
	/** 当前没有日常任务 */
	public static final int TASK_DAY_NO = 3026;
	/** 获取日常信息异常 */
	public static final Object[] TASK_DAY_ERROR = new Object[] { 0, 3027 };
	/** 日常信息配置错误 */
	public static final Object[] TD_CONFIG_ERROR = new Object[] { 0, 3028 };
	/** 日常任务已满 */
	public static final Object[] TASK_DAY_FULL = new Object[] { 0, 3029 };

	// ----------------------------打坐-------------------------------------
	/** 打坐错误 */
	public static final Object[] DAZUO_ERROR = new Object[] { 0, 3201 };
	/** 已经在打坐中 */
	public static final Object[] DAZUO_DAZUOING = new Object[] { 0, 3202 };
	/** 已经不在单休状态了 */
	public static final Object[] DAZUO_SX_DAZUOING = new Object[] { 0, 3203 };
	/** 该玩家已经是双休打坐了,不需要再与别人打坐或自己打坐 */
	public static final Object[] DAZUO_STATE_ERROR = new Object[] { 0, 3204 };

	// --------------------------竞技---------------------------------------
	/** 挑战次数已满 */
	public static final Object[] TIAOZHAN_COUNT_IS_MAX = new Object[] { 0, 3231 };
	/** 当前没有冷却 */
	public static final Object[] JINGJI_NOT_IN_CD = new Object[] { 0, 3232 };
	/** 鼓舞次数已满 */
	public static final Object[] JINGJI_GUWU_IS_MAX = new Object[] { 0, 3233 };
	/** 目标不可挑战 */
	public static final Object[] JINGJI_TARGET_CANNOT_TIAOZHAN = new Object[] { 0, 3234 };
	/** 当前不可进行挑战 */
	public static final Object[] JINGJI_NOW_CANNOT_FIGHT = new Object[] { 0, 3235 };
	/** 今日已没有可挑战次数 */
	public static final Object[] JINGJI_TODAY_IS_NO_TZ_COUNT = new Object[] { 0, 3236 };
	/** 目标正在战斗中 */
	public static final Object[] JINGJI_TARGET_IS_FIGHTING = new Object[] { 0, 3237 };
	/** 副本中不可发起挑战 */
	public static final Object[] JINGJI_FUBEN_CANNOT_TIAOZHAN = new Object[] { 0, 3238 };
	/** 排名奖励已领取过 */
	public static final Object[] JINGJI_GIFT_IS_RECIVED = new Object[] { 0, 3239 };
	/** 该物品今日兑换已满 */
	public static final Object[] JINGJI_DUIHUAN_NO_COUNT = new Object[] { 0, 3240 };
	/** 荣誉值不足 */
	public static final Object[] JINGJI_NO_ENOUGH_RONGYU = new Object[] { 0, 3241 };
	/** 当前正在冷却 */
	public static final Object[] JINGJI_NOW_IS_IN_CD = new Object[] { 0, 3269 };
	/** 竞技正在结算中，不可挑战 */
	public static final Object[] JINGJI_JIESUAN_ZHONG = new Object[] { 0, 3277 };
	/** 鼓舞操作成功，单业务失败 */
	public static final Object[] JINGJI_GUWU_FIAL = new Object[] { 2 };
	/** 没有可领取的奖励 */
	public static final Object[] JINGJI_NO_GIFT = new Object[] { 0, 3287 };
	/** 您正在战斗中 */
	public static final Object[] JINGJI_YOU_IS_FIGHTING = new Object[] { 0, 3288 };

	// ---------------------------------VIP-------------------------------------------
	/** VIP等级不足 */
	public static final Object[] VIP_NOT_ENOUGH_LEVEL = new Object[] { 0, 3351 };
	/** 目标玩家VIP等级不足 */
	public static final Object[] VIP_TARGET_NOT_ENOUGH_LEVEL = new Object[] { 0, 3352 };
	/** 请求数据异常 */
	public static final Object[] VIP_ASK_DATA_ERROR = new Object[] { 0, 3363 };
	/** 奖励已领取过 */
	public static final Object[] VIP_GIFT_HAS_REVICED = new Object[] { 0, 3364 };

	// ---------------------------------坐骑-------------------------------------------
	/** 坐骑未开启 */
	public static final Object[] ZQ_NO_OPEN = new Object[] { 0, 3421 };
	/** 坐骑等级已经最高了 */
	public static final Object[] ZQ_LEVEL_MAX = new Object[] { 0, 3422 };
	/** 坐骑目标升级等级错误 */
	public static final Object[] ZQ_TARGET_LEVEL_ERROR = new Object[] { 0, 3423 };
	/** 坐骑配置异常 */
	public static final Object[] ZQ_CONFIG_ERROR = new Object[] { 0, 3424 };
	/** 坐骑升阶所需物品不足 */
	public static final Object[] ZQ_GOODS_BZ = new Object[] { 0, 3437 };
	/** 坐骑外显没有获得 */
	public static final Object[] ZQ_NO_SHOW = new Object[] { 0, 3438 };
	/** 坐骑开启等级不足 */
	public static final Object[] ZUOQI_LEVEL_NO = new Object[] { 0, 3439 };
	/** 御剑等级不足,不能使用 */
	public static final Object[] ZUOQI_LEVEL_LIMIT_CAN_NOT_USE = new Object[] { 0, 3456 };
	/** 潜能丹使用数量达上限 */
	public static final Object[] ZUOQI_QND_MAX_NUM = new Object[] { 0, 3457 };
	/** 成长丹使用数量达上限 */
	public static final Object[] ZUOQI_CZD_MAX_NUM = new Object[] { 0, 3458 };
	/** 坐骑进阶全服公告 */
	public static final int ZUOQI_SJ_NOTICE = 3466;
	// ---------------------------------拍卖-------------------------------------------
	/** 角色不在线 */
	public static final Object[] ROLE_OFF = new Object[] { 0, 3500 };
	/** 市场中指定物品不存在 */
	public static final Object[] PAIMAI_NO_EXIST = new Object[] { 0, 3501 };
	/** 市场配置错误 */
	public static final Object[] PAIMAI_ERROR = new Object[] { 0, 3502 };
	/** 不可购买自己拍卖的物品 */
	public static final Object[] PAIMAI_SELF_GOODS = new Object[] { 0, 3503 };
	/** 吆喝时间冷却中 */
	public static final Object[] PAIMAI_YH_CD = new Object[] { 0, 3504 };
	/** 客服端结束索引比启始索引大 */
	public static final Object[] PAIMAI_INDEX_ERROR = new Object[] { 0, 3505 };
	/** 该物品不可拍卖 */
	public static final Object[] PM_NO_UP = new Object[] { 0, 3506 };
	/** 拍卖价格错误 */
	public static final Object[] PM_PRICE_ERROR = new Object[] { 0, 3507 };

	// ---------------------------------福利大厅3700-3799-------------------------------------------
	/** 今日已经签过到 */
	public static final Object[] ASSIGN_EXISTS = new Object[] { 0, 3708 };
	/** 礼包不存在 */
	public static final int GIFT_CARD_IS_NULL = 3709;
	/** 当前礼包卡已经领取 */
	public static final int GIFT_CARD_REWARDED = 3710;
	/** 补签次数不足 */
	public static final Object[] ASSIGN_RETROACTIVE_LESS = new Object[] { 0, 3714 };
	/** 您当前无漏签，无需补签！ */
	public static final Object[] ASSIGN_RETROACTIVE_REFUSED = new Object[] { 0, 3723 };
	/** 离线时间不足10分钟 */
	public static final Object[] OFFLINE_TIME_LESS = new Object[] { 0, 3735 };
	/** 当前类型奖励已经领取 */
	public static final Object[] ASSIGN_TYPE_REWARDED = new Object[] { 0, 3746 };
	/** 领取条件不满足 */
	public static final Object[] ASSIGN_CONDITION_LESS = new Object[] { 0, 3747 };
	/** 不能领取在线奖励 */
	public static final Object[] ONLINE_REWARD_ERROR = new Object[] { 0, 3748 };
	/** 该类型礼包卡不可重复领奖 */
	public static final int GIFT_CARD_REPEAT = 3749;
	/** 礼包类型异常 */
	public static final int GIFT_CARD_TYPE_ERROR = 3750;
	/** 礼包卡加密验证不通过 */
	public static final int GIFT_CARD_NO_PASS = 3751;
	/** 领取礼包卡其他未知错误 */
	public static final int GIFT_CARD_OTHER_ERROR = 3752;
	/** 领取礼包卡成功 */
	public static final int GIFT_CARD_SUCCESS = 3753;
	/** 已经领取过离线奖励，不能重复领取 */
	public static final Object[] OFFLINE_REWARDED = new Object[] { 0, 3754 };
	/** 已经领取过离线奖励，不能重复领取 */
	public static final int ONLINE_EMAIL_CONTENT = 3757;
	/** 没有可找回的资源 */
	public static final Object[] NO_CAN_FIND_RESOURCE = new Object[] { 0, 3766 };

	// ---------------------------------七日登陆-------------------------------------------
	/** 登陆天数不足 */
	public static final Object[] LOGIN_DAYS_LESS = new Object[] { 0, 3621 };
	/** 传递参数异常 */
	public static final Object[] PARAMETER_ERROR = new Object[] { 0, 3647 };
	/** 当前天数已领取过奖励 */
	public static final Object[] LOGIN_DAY_REWARDED = new Object[] { 0, 3623 };

	// ----------------------------------资源找回------------------------------------------
	/** 该资源找回类型的奖励已经领取 */
	public static final Object[] RESOURCE_BACK_REWARDED = new Object[] { 0, 3626 };
	/** 所有类型的资源奖励已经领取，不能重复领取 */
	public static final Object[] RESOURCE_BACK_OVER = new Object[] { 0, 3627 };

	// ------------------------------寻宝--------------------------------------------
	/** 寻宝公共数据表异常 */
	public static final Object[] XB_NO_PUBLIC_CONFIG = new Object[] { 0, 3821 };
	/** 寻宝类型不存在 */
	public static final Object[] XB_TYPE_ERROR = new Object[] { 0, 3822 };
	/** 寻宝背包空间不足 */
	public static final Object[] XB_BAG_FULL = new Object[] { 0, 3823 };
	/** 元宝不足 */
	public static final Object[] XB_NO_GOLD = new Object[] { 0, 3824 };
	/** 取出的物品不存在 */
	public static final Object[] XB_ITEM_NO_EXIST = new Object[] { 0, 3825 };
	/** 寻宝背包没有物品 */
	public static final Object[] XB_BAG_NO_GOODS = new Object[] { 0, 3826 };
	/** 寻宝积分配置表错误 */
	public static final Object[] XB_CONFIG_ERROR = new Object[] { 0, 3827 };
	/** 寻宝积分不足 */
	public static final Object[] XB_JIFEN_ERROR = new Object[] { 0, 3828 };
	/** 寻宝取出仓库物品有误 */
	public static final Object[] XB_QCWP_ERROR = new Object[] { 0, 3829 };
	/** 兑换的物品与自己职业不符合 */
	public static final Object[] XB_DUIHUAN_ERROR = new Object[] { 0, 3830 };

	// ---------------------------------活动-------------------------------------------
	/** 活动期间无法使用飞鞋 */
	public static final Object[] ATCIVE_CANNOT_FLY = new Object[] { 0, 10000 };
	/** 活动期间无法变更公会 */
	public static final Object[] ATCIVE_CANNOT_CHANGE_GUILD = new Object[] { 0, 10001 };

	// ----------------------------商城-------------------------------------------------
	/** 商城配置错误 */
	public static final Object[] MALLPZ_ERROR = new Object[] { 0, 4050 };
	/** 商城中没有您购买的商品 */
	public static final Object[] NO_GOODS = new Object[] { 0, 4051 };
	/** 消耗货币类型错误 */
	public static final Object[] ACCOUNT_TYPE_ERROR = new Object[] { 0, 4052 };
	/** 商城购买物品数量上限错误(对不起，不能购买这么的物品！) */
	public static final Object[] MALL_BUY_COUNT_ERROR = new Object[] { 0, 4060 };

	// ----------------------------多人副本--------------------------------------------
	/** 已有队伍 */
	public static final Object[] MORE_TEAM_EXISTS = new Object[] { 0, 5528 };
	/** 队伍不存在 */
	public static final Object[] MORE_TEAM_NOT_EXISTS = new Object[] { 0, 5529 };
	/** 已经在队伍中 */
	public static final Object[] MORE_TEAM_IN = new Object[] { 0, 5530 };
	/** 已经加入其它副本 */
	public static final Object[] MORE_TEAM_OTHER_IN = new Object[] { 0, 5531 };
	/** 人数已达上限 */
	public static final Object[] MORE_TEAM_PEOPLE_COUNT = new Object[] { 0, 5532 };
	/** 密码不正确 */
	public static final Object[] MORE_TEAM_PWD_ERROR = new Object[] { 0, 5533 };
	/** 战力不足 */
	public static final Object[] MORE_TEAM_STRENGTH_LESS = new Object[] { 0, 5534 };
	/** 当前有玩家没准备好，无法开始挑战 */
	public static final Object[] MORE_TEAM_NOT_PREPARED = new Object[] { 0, 5535 };
	/** 队伍中没有该玩家 */
	public static final Object[] MORE_TEAM_NOT_IN = new Object[] { 0, 5536 };
	/** 当前没有队伍可以加入 */
	public static final Object[] MORE_TEAM_JOIN_ERROR = new Object[] { 0, 5537 };
	/** 队长无需准备 */
	public static final Object[] MORE_TEAM_NO_NEED = new Object[] { 0, 5538 };
	/** 每天只能第一次领取哦 */
	public static final Object[] MORE_TEAM_REWARDED_ONCE = new Object[] { 0, 5539 };
	/** 当前副本队伍已达上限 */
	public static final Object[] MORE_FUBEN_TEAM_LIMIT_ERROR = new Object[] { 0, 5540 };
	/** 多人副本进入倒计时，不能加入 */
	public static final Object[] MORE_FUBEN_DJS = new Object[] { 0, 5545 };
	/** 未找到其他队伍 */
	public static final Object[] MORE_FUBEN_NO_AVAILABLE_TEAM = new Object[] { 0, 5547 };

	// ------------------------------押镖--------------------------------------------
	/** 镖车距离交镖NPC太远，不能交付护送任务 **/
	public static final Object[] BIAO_CHE_TOO_FAR = new Object[] { 0, 5609 };
	/** 今日已没有可押镖次数 **/
	public static final Object[] YB_MAX_TIMES = new Object[] { 0, 5606 };
	/** 您当前还在押镖中 **/
	public static final Object[] YABIAO_ING = new Object[] { 0, 5604 };
	/** 您还未接镖车任务 **/
	public static final Object[] NOT_YABIAO_ING = new Object[] { 0, 5619 };
	/** 【XXX】打劫了您的美女，美女已经消失，获得当前美女【XXX】的经验 **/
	public static final int DJ_BIAOCHE = 5620;
	/** 【XXX】洗劫了【XXX】的XX镖车 **/
	public static final int XJ_BIAOCHE = 5621;
	/** 您成功截获了【XXX】的XX镖车，劫得经验XXXX **/
	public static final int JB_BIAOCHE_EXP = 5624;
	/** 您成功截获了【XXX】的XX镖车，今日劫镖次数已经用完，未获得任何奖励 **/
	public static final int NO_JB_BIAOCHE_EXP = 5625;
	/** 道具不足 **/
	public static final Object[] NO_YB_GOODS = new Object[] { 0, 5627 };
	/** 任务已交付 */
	public static final Object[] YB_TASK_PLAY = new Object[] { 0, 5628 };
	/** 当前是最高等级的镖车，无法刷新 **/
	public static final Object[] MAX_LEVEL_CAN_NOT_REFRESH = new Object[] { 0, 5632 };
	/** 野外boss奖励发邮件 */
	public static final String DSBOSS_EMAIL_CONTENT_CODE = "5716";
	/** 野外boss奖励发邮件标题 */
	public static final String DSBOSS_EMAIL_CONTENT_CODE_TITLE = "5718";
	/** 镖车已远离 **/
	public static final int  BIAOCE_OUT_DISTANCE= 5650;
	// --------------------------答题--------------------------------------------
	/**已参加过活动不能直接领奖*/
	public static final Object[] DATI_CANNOT_RECEIVE = new Object[] { 0, 5100 };
	/** 已领取过不能再领*/
	public static final Object[] DATI_RECEIVED = new Object[] { 0, 5101 };
	/**等级不足*/
	public static final Object[] DATI_LEVEL_NOT_ENOUGH = new Object[] { 0, 5102 };

	// ---------------------------时装------------------------------------------
	/**时装尚未激活*/
	public static final Object[] SHIZHUANG_NOT_ACTIVE = new Object[] { 0, 6124 };
	/**时装已经激活*/
	public static final Object[] SHIZHUANG_IS_ACTIVED = new Object[] { 0, 6125 };
	/**时装性别不符*/
	public static final Object[] SHIZHUANG_SEX_ERROR = new Object[] { 0, 6127 };
	/**转职的时装id不存在*/
	public static final Object[] SHIZHUANG_CONFIG_ERROR = new Object[] { 0, 6132 };
	/**升阶等级不符*/
	public static final Object[] SHIZHUANG_JJLEVEL_ERROR = new Object[] { 0, 6133 };
	/**最高等阶*/
	public static final Object[] SHIZHUANG_MAX_LEVEL = new Object[] { 0, 6134 };

	// --------------------------翅膀--------------------------------------------
	/**翅膀开启等级不足*/
	public static final Object[] CHIBANG_LEVEL_NO = new Object[] { 0, 3444 };
	/**翅膀未开启*/
	public static final Object[] CB_NO_OPEN = new Object[] { 0, 3445 };
	/**翅膀等级已经最高了*/
	public static final Object[] CB_LEVEL_MAX = new Object[] { 0, 3446 };
	/**翅膀目标升级等级错误*/
	public static final Object[] CB_TARGET_LEVEL_ERROR = new Object[] { 0, 3447 };
	/**翅膀配置异常*/
	public static final Object[] CB_CONFIG_ERROR = new Object[] { 0, 3448 };
	/**翅膀升阶所需物品不足*/
	public static final Object[] CB_GOODS_BZ = new Object[] { 0, 3449 };
	/**翅膀外显没有获得*/
	public static final Object[] CB_NO_SHOW = new Object[] { 0, 3450 };
	/**翅膀等级不足,不能使用*/
	public static final Object[] CB_LEVEL_LIMIT_CAN_NOT_USE = new Object[] { 0, 3461 };
	/**潜能丹使用数量达上限*/
	public static final Object[] CB_QND_MAX_NUM = new Object[] { 0, 3462 };
	/**成长丹使用数量达上限*/
	public static final Object[] CB_CZD_MAX_NUM = new Object[] { 0, 3463 };
	/**翅膀进阶全服公告*/
	public static final int CB_SJ_NOTICE = 10451;
	/**击杀野外boss全服公告*/
	public static final int KILL_YWBOSS_NOTICE = 20223;
	
	
	// --------------------------器灵--------------------------------------------
	/**器灵开启等级不足*/
	public static final Object[] QL_LEVEL_NO = new Object[] { 0, 16641 };
	/**器灵未开启*/
	public static final Object[] QL_NO_OPEN = new Object[] { 0, 16642 };
	/**器灵等级已经最高了*/
	public static final Object[] QL_LEVEL_MAX = new Object[] { 0, 16643 };
	/**器灵目标升级等级错误*/
	public static final Object[] QL_TARGET_LEVEL_ERROR = new Object[] { 0, 16644 };
	/**器灵配置异常*/
	public static final Object[] QL_CONFIG_ERROR = new Object[] { 0, 16645 };
	/**器灵升阶所需物品不足*/
	public static final Object[] QL_GOODS_BZ = new Object[] { 0, 16646 };
	/**器灵外显没有获得*/
	public static final Object[] QL_NO_SHOW = new Object[] { 0, 16647 };
	/**器灵等级不足,不能使用*/
	public static final Object[] QL_LEVEL_LIMIT_CAN_NOT_USE = new Object[] { 0, 16648 };
	/**潜能丹使用数量达上限*/
	public static final Object[] QL_QND_MAX_NUM = new Object[] { 0, 16649 };
	/**成长丹使用数量达上限*/
	public static final Object[] QL_CZD_MAX_NUM = new Object[] { 0, 16650 };
	/**器灵进阶全服公告*/
	public static final int QL_SJ_NOTICE = 16660;

	// ---------------------------活跃度---------------------------------------------
	/** 领取奖励数据异常 */
	public static final Object[] HUOYUEDU_DATA_ERROR = new Object[] { 0, 15099 };
	/** 已领取 */
	public static final Object[] HUOYUEDU_GET_AWARD_ERROR = new Object[] { 0, 15100 };
	/**积分不足 */
	public static final Object[] HUOYUEDU_JIFEN_NOT_ENOUGHT = new Object[] { 0, 15101 };

	// ---------------------------糖宝---------------------------------------------
	/** 没有糖宝 */
	public static final Object[] TB_NO_TANGBAO = new Object[] { 0, 10340 };
	/** 已达到上限 */
	public static final Object[] TB_IS_MAX_COUNT = new Object[] { 0, 10341 };
	/** 等级不足 */
	public static final Object[] TB_NO_ENOUGH_LEVEL = new Object[] { 0, 10342 };
	/** 数量异常 */
	public static final Object[] TB_COUNT_IS_ERROR = new Object[] { 0, 10343 };

	// -------------------------------宝箱采集---------------------------
	/** 活动还没开始 */
	public static final Object[] ACTIVITY_END = new Object[] { 0, 5110 };
	/** 采集失败 */
	public static final Object[] COLLECT_FAIL = new Object[] { 0, 5111 };
	/** 宝箱已被采完 */
	public static final Object[] BOX_NULL = new Object[] { 0, 5112 };
	/** 等级不足，不能采集 */
	public static final Object[] LEVEL_LACK = new Object[] { 0, 5113 };
	/** 采集时间没到 */
	public static final Object[] TIME_LACK = new Object[] { 0, 5114 };
	
	/**击杀野外boss全服公告*/
	public static final Object ACTIVTE_NOTICE = 20225;
	// --------------------------------平台礼包---------------------------
	/**平台错误，数据异常*/
	public static final Object[] PLATFORM_DATA_ERROR = new Object[] { 0, 13413 };
	/**未达到充值条件不能领取礼包*/
	public static final Object[] PLATFORM_RECHARGE_NOT_ENOUGH = new Object[] { 0, 15600 };
	/**不是平台大厅登陆不能领取礼包*/
	public static final Object[] PLATFORM_NOT_DATING = new Object[] { 0, 15601 };
	/**VIP等级不到不能领取礼包*/
	public static final Object[] PLATFORM_VIP_NOT_ENOUGH = new Object[] { 0, 15602 };
	/**请求参数异常*/
	public static final Object[] PLATFORM_REQUEST_ARGS_ERROR = new Object[] { 0, 15603 };
	/**获取平台vip等级错误*/
	public static final Object[] PLATFORM_GET_VIP_LEVEL_ERROR = new Object[] { 0, 15618 };

	// --------------------------------360特权---------------------------
	/**当前用户尚未登陆安全卫士，未领取本游戏的特权*/
	public static final Object[] PLATFORM_360_ERROR_1 = new Object[] { 0, 15613 };
	/**特权活动未上线*/
	public static final Object[] PLATFORM_360_ERROR_2 = new Object[] { 0, 15614 };
	/**特权活动已经结束*/
	public static final Object[] PLATFORM_360_ERROR_3 = new Object[] { 0, 15615 };
	/**系统繁忙,领取失败*/
	public static final Object[] PLATFORM_360_ERROR_4 = new Object[] { 0, 15616 };
	/**不是手机令牌APP用户，请去扫码*/
	public static final Object[] PLATFORM_360_ERROR_5 = new Object[] { 0, 15617 };
	/**请先登录安全卫士，领取对应特权，分享特权活动到指定社交平台获得分享礼包*/
	public static final Object[] PLATFORM_360_ERROR_7 = new Object[] { 0, 15619 };
	/**礼包审核中，我们将于您分享特权活动48小时之后给您发送礼包，请耐心等待*/
	public static final Object[] PLATFORM_360_ERROR_8 = new Object[] { 0, 15620 };
	/**未获取到平台信息，请稍后再试*/
	public static final Object[] GET_PLATFORM_INFO_ERROR = new Object[] { 0, 15621 };
	/**不是V计划会员*/
	public static final Object[] NO_360_VPLAN_USER = new Object[] { 0, 15622 };
	/**V计划会员等级不足*/
	public static final Object[] _360_VPLAN_LEVEL_NOT_ENOUGH = new Object[] { 0, 15623 };
	/**V计划会员过期*/
	public static final Object[] NO_360_VPLAN_EXPIRE = new Object[] { 0, 15624 };
	/**V计划没有这个等级的礼包*/
	public static final Object[] NO_360_VPLAN_LEVEL_LB = new Object[] { 0, 15625 };
	/**V计划未达到消费额度*/
	public static final Object[] _360_VPLAN_LEVEL_GOLD_NOT_ENOUGH = new Object[] { 0, 15626 };

	// --------------------------------天天探宝---------------------------
	/** 活动尚未开启 */
	public static final Object[] ACTIVE_IS_NOT_START = new Object[] { 0, 5122 };

	// -----------------------------开服活动----------------------------
	/** 不在活动时间内 */
	public static final Object[] KAIFU_TIME_ERROR = new Object[] { 0, 13253 };
	/** 没有达到领取条件 */
	public static final Object[] KAIFU_TIAOJIAN_ERROR = new Object[] { 0, 13254 };
	/** 已领取过奖励 */
	public static final Object[] KAIFU_YI_LINGQU = new Object[] { 0, 13255 };
	/** 活动异常 */
	public static final Object[] KAIFU_HUODONG_ERROR = new Object[] { 0, 13256 };
	/** 抱歉，礼包已被领完 */
	public static final Object[] KAIFU_NUMBER_ERROR = new Object[] { 0, 13257 };
	// --------------------------------首充奖励---------------------------
	// --------------------------------跨服------------------------------------
	/** 跨服尚未连接 */
	public static final Object[] KUAFU_NO_CONNECTION = new Object[] { 0, 5800 };
	/** 跨服期间禁止此操作 */
	public static final Object[] KUAFUING_CANNOT_DO_THIS = new Object[] { 0, 5801 };
	/** 进入跨服失败 */
	public static final Object[] KUAFU_ENTER_FAIL = new Object[] { 0, 5802 };

	// --------------------------------神器------------------------------------
	/** 神器不能激活 */
	public static final Object[] SHENQI_CAN_NOT_ACTIVATE = new Object[] { 0, 5900 };
	/** 神器不存在 */
	public static final Object[] SHENQI_NOT_EXISTS = new Object[] { 0, 5901 };
	/** 神器已经激活 */
	public static final Object[] SHENQI_IS_ACTIVATED = new Object[] { 0, 5902 };
	/** 神器未激活 */
	public static final Object[] SHENQI_IS_NOT_ACTIVATED = new Object[] { 0, 5903 };
	/** VIP等级不足不能进行锁定洗练 */
	public static final Object[] SHENQI_XILIAN_LOCK_ERROR_VIP_1 = new Object[] { 0, 5908 };
	/**锁定的属性大于VIP等级拥有的数量 */
	public static final Object[] SHENQI_XILIAN_LOCK_ERROR_VIP_2 = new Object[] { 0, 5909 };
	// --------------------------------37手机礼包------------------------------------
	/** 37手机礼包：不能领取 */
	public static final Object[] _37_PHONE_REWARD_CAN_NOT_PICK = new Object[] { 0, 13300 };
	/** 37手机礼包：已领取 */
	public static final Object[] _37_PHONE_REWARD_PICKED = new Object[] { 0, 13301 };
	/** 平台错误，不能领取 */
	public static final Object[] _37_PLATFORM_ERROR = new Object[] { 0, 13412 };
	// --------------------------------领取微端奖励---------------------------
	/** 不是使用微端登录 */
	public static final Object[] NOT_WEIDUAN = new Object[] { 0, 13302 };
	/** 您已领取过该奖励 */
	public static final Object[] YI_LQWEIDUAN = new Object[] { 0, 13303 };
	/** 已领取 */
	public static final Object[] _37_VPLAN_GIFT_GET_ALREADY = new Object[] { 0, 13406 };
	/** 37平台vip等级礼包已经领取 */
	public static final Object[] _37_PLATFORM_GIFT_GET_ALREADY = new Object[] { 0, 13409 };
	/** 平台vip等级礼包不能领取 领取等级不匹配 */
	public static final Object[] _37_PLATFORM_GIFT_GET_NOT = new Object[] { 0, 13410 };
	/** 押镖橙色品质美女通知全服在线玩家 */
	public static final Object[] YABIAO_ORANGE_NOTICE_ALL = new Object[] { 0, 13411 };
	/** 37V计划会员等级不足 */
	public static final Object[] _37_VPLAN_VIP_LEVEL_NOT_ENOUGH = new Object[] { 0, 13414 };
	/**V计划没有这个礼包*/
	public static final Object[] _37_VPLAN_LB_NO = new Object[] { 0, 13415 };
	/**V计划未达到消费额度*/
	public static final Object[] _37_VPLAN_LEVEL_GOLD_NOT_ENOUGH = new Object[] { 0, 13416 };

	// --------------------------------热发布（10700-10899）---------------------------
	// 首充
	/** 此活动已经下架 */
	public static final int ACTIVITY_OUT = -1;
	/** 累积的元宝不够，请再充一丢丢补齐吧 */
	public static final Object[] SC_YB_NO_ENOU = new Object[] { 0, 10700 };
	/** 你还没有充值,不能领取礼包 */
	public static final Object[] NO_RECHARGE_ERROR = new Object[] { 0, 10701 };
	/** 没有可领取的礼包 */
	public static final Object[] NO_LQ_LIBAO = new Object[] { 0, 10702 };
	/** 没有正在进行的活动 */
	public static final Object[] NO_SUB_ACTIVITY = new Object[] { 0, 10703 };
	// 修仙礼包
	/** 礼包已不在出售列表中，请再试一下其它礼包 */
	public static final Object[] NO_XX_LIBAO = new Object[] { 0, 13206 };
	/** 您的领取完了此礼包个数，请再试一下其它礼包 */
	public static final Object[] XX_LIBAO_ROLE_COUNT = new Object[] { 0, 13207 };
	/** 本服此礼包已领取完了，请再试一下其它礼包 */
	public static final Object[] XX_LIBAO_SERVER_COUNT = new Object[] { 0, 13208 };
	/** 礼包奖励物品已不存在 */
	public static final Object[] NO_LIBAO_GOODS = new Object[] { 0, 13209 };

	// 棋盘
	/** 没有转盘次数 **/
	public static final Object[] NO_QIPAN_CPUNT = new Object[] { 0, 14105 };

	// 累充
	/** 没有达到领取条件 */
	public static final Object[] NO_LEICHONG_TIAOJIAN = new Object[] { 0, 14304 };
	/** 没有领取次数 */
	public static final Object[] NO_LEICHONG_COUNT = new Object[] { 0, 14305 };

	// 神秘商店
	/** 购买数据错误 */
	public static final Object[] SMSD_BUY_ERROR = new Object[] { 0, 14211 };
	/** 你已经购买过这件物品 */
	public static final Object[] SMSD_BUY_CHONGFU = new Object[] { 0, 14212 };
	// 命运转盘
	/** 转盘数据错误 */
	public static final Object[] ZP_DATA_ERROR = new Object[] { 0, 13108 };
	/** 没有找到物品 */
	public static final Object[] ZP_NOT_GOODS = new Object[] { 0, 13109 };
	/** 兑换所需积分不足 */
	public static final Object[] ZP_DUIHUAN_JIFEN = new Object[] { 0, 13110 };
	/** 由于你的背包已满，你的奖励物品给你寄过来啦 */
	public static final String ZHUANPAN_EAMIL = "13113";
	/** 轮盘奖励*/
	public static final String ZHUANPAN_EAMIL_TITLE = "13123";
	// 消费排名
	/** 客户端数据错误 */
	public static final Object[] CLIENT_DATA_ERROR = new Object[] { 0, 14501 };
	public static final String XIAOFEI_PAIMING_EMAIL = "14500";
	public static final String XIAOFEI_PAIMING_EMAIL_TITLE = "14506";

	// 盗墓手札
	/** 由于你的背包已满，你盗墓手札的物品给你寄过来啦 */
	public static final String DAOMO_EMAIL = "15651";
	public static final String DAOMO_EMAIL_TITLE = "15659";

	// 探索宝藏
	/**当前宝藏还未开启*/
	public static final Object[] BAOZANG_NOT_OPEN = new Object[] { 0, 17011 };
	/**探索王城次数不足，不能领取*/
	public static final Object[] WC_NOT_COUNT = new Object[] { 0, 17012 };
	/**已经领取过该奖励*/
	public static final Object[] WC_YI_LINGQU = new Object[] { 0, 17013 };

	// -------------------------------团购秒杀----------------------------------
	/**当前没有进行的团购秒杀活动*/
	public static final Object[] MIAOSHA_NO_ACTIVE = new Object[] { 0, 18207 };
	/**当前团购秒杀活动尚未开启*/
	public static final Object[] MIAOSHA_ACTIVE_NOT_START = new Object[] { 0, 18208 };
	/**您已参加过本次团购秒杀活动*/
	public static final Object[] MIAOSHA_IS_JOIN = new Object[] { 0, 18209 };

	// ---------------------开服活动-----------------
	// 邮件内容
	public static final String KAIFU_ZUOQI_PAIMING = "13249";
	public static final String KAIFU_CHIBANG_PAIMING = "13250";
	public static final String KAIFU_ZHANLI_PAIMING = "13248";
	public static final String KAIFU_JINGJI_PAIMING = "13252";
	public static final String KAIFU_ZHANJIA_PAIMING = "13258";
	public static final String KAIFU_YAOSHEN_PAIMING = "13259";
	public static final String KAIFU_YAOSHEN_MOWEN_PAIMING = "13260";
	public static final String KAIFU_QILING_PAIMING = "13261";
	public static final String KAIFU_TIANGONG_PAIMING = "13262";
	public static final String KAIFU_YAOSHEN_HUNPO_PAIMING = "13263";
	public static final String KAIFU_YAOSHEN_MOYIN_PAIMING = "13264";
	public static final String KAIFU_TANGBAO_XINWEN_PAIMING = "13265";
	public static final String KAIFU_PAIMING_EMAIL_TITLE = "13266";
	public static final String KAIFU_WUQI_PAIMING = "13267";
	public static final String KAIFU_JIEDUAN_TITIE	 = "13268";
	public static final String KAIFU_WUQI_JIEDUAN = "13269";
	public static final String KAIFU_ZUOQI_JIEDUAN = "13270";
	public static final String KAIFU_CHIBANG_JIEDUAN = "13271";
	public static final String KAIFU_XIANJIAN_JIEDUAN = "13272";
	public static final String KAIFU_ZHANLI_JIEDUAN = "13273";
	public static final String KAIFU_ZHANJIA_JIEDUAN = "13274";
	// --------------------------------阵营战报--------------------------------------
	/** 活动未开始 **/
	public static final Object[] CAMP_WAR_NO_START = new Object[] { 0, 13321 };
	// 阵营战
	/** 奖励邮件内容：恭喜您所在的阵营获得了胜利，您为本阵营的第{0}名，附件为奖励！ **/
	public static final String CAMP_EAMIL_CONTENT = "13334";
	/** 古神战场奖励 **/
	public static final String CAMP_EAMIL_CONTENT_TITLE = "13351";
	/** 邮件内容：恭喜您所在的阵营获得了胜利，附件为奖励！ **/
	public static final String CAMP_EAMIL_CONTENT_PJ_WIN = "13335";
	/** 邮件内容：恭喜您所在的阵营失败，附件为奖励！ **/
	public static final String CAMP_EAMIL_CONTENT_PJ_LOSE = "13347";
	// --------------------------仙剑--------------------------------------------
	/** 仙剑开启等级不足 */
	public static final Object[] XIANJIAN_LEVEL_NO = new Object[] { 0, 14561 };
	/** 仙剑未开启 */
	public static final Object[] XJ_NO_OPEN = new Object[] { 0, 14562 };
	/** 仙剑等级已经最高了 */
	public static final Object[] XJ_LEVEL_MAX = new Object[] { 0, 14563 };
	/** 仙剑目标升级等级错误 */
	public static final Object[] XJ_TARGET_LEVEL_ERROR = new Object[] { 0, 14564 };
	/** 仙剑配置异常 */
	public static final Object[] XJ_CONFIG_ERROR = new Object[] { 0, 14565 };
	/** 仙剑升阶所需物品不足 */
	public static final Object[] XJ_GOODS_BZ = new Object[] { 0, 14566 };
	/** 仙剑外显没有获得 */
	public static final Object[] XJ_NO_SHOW = new Object[] { 0, 14567 };
	/** 仙剑等级不足,不能使用 */
	public static final Object[] XJ_LEVEL_LIMIT_CAN_NOT_USE = new Object[] { 0, 14568 };
	/** 潜能丹使用数量达上限 */
	public static final Object[] XJ_QND_MAX_NUM = new Object[] { 0, 14569 };
	/** 成长丹使用数量达上限 */
	public static final Object[] XJ_CZD_MAX_NUM = new Object[] { 0, 14570 };
	/** 仙剑进阶全服公告 */
	public static final int XJ_SJ_NOTICE = 14580;
	// --------------------------战甲--------------------------------------------
	/** 仙剑开启等级不足 */
	public static final Object[] ZHANJIA_LEVEL_NO = new Object[] { 0, 14803 };
	/** 仙剑未开启 */
	public static final Object[] ZJ_NO_OPEN = new Object[] { 0, 14804 };
	/** 仙剑等级已经最高了 */
	public static final Object[] ZJ_LEVEL_MAX = new Object[] { 0, 14805 };
	/** 仙剑目标升级等级错误 */
	public static final Object[] ZJ_TARGET_LEVEL_ERROR = new Object[] { 0, 14806 };
	/** 仙剑配置异常 */
	public static final Object[] ZJ_CONFIG_ERROR = new Object[] { 0, 14807 };
	/** 仙剑升阶所需物品不足 */
	public static final Object[] ZJ_GOODS_BZ = new Object[] { 0, 14808 };
	/** 仙剑外显没有获得 */
	public static final Object[] ZJ_NO_SHOW = new Object[] { 0, 14809 };
	/** 仙剑等级不足,不能使用 */
	public static final Object[] ZJ_LEVEL_LIMIT_CAN_NOT_USE = new Object[] { 0, 14810 };
	/** 潜能丹使用数量达上限 */
	public static final Object[] ZJ_QND_MAX_NUM = new Object[] { 0, 14811 };
	/** 成长丹使用数量达上限 */
	public static final Object[] ZJ_CZD_MAX_NUM = new Object[] { 0, 14812 };
	/** 战甲进阶全服公告 */
	public static final int ZJ_SJ_NOTICE = 14816;

	// --------------------------------投资计划---------------------------
	/** 当前没有投资计划 **/
	public static final Object[] NO_TOUZI_PLAN = new Object[] { 0, 14412 };
	/** {0}购买了{1}投资，将获得{2}奖励 **/
	public static final int NOTIFY_SEND_TZ = 14422;
	/** 当前没有可领取的奖励 **/
	public static final Object[] NO_RECEIVE_ERROR = new Object[] { 0, 14429 };

	// ----------------------------------灵火----------------------------
	/** 不能跨级激活 */
	public static final Object[] LH_NO_KUAJI = new Object[] { 0, 14900 };
	/** 没有找到要激活的灵火 */
	public static final Object[] LH_NO_LINGHUO = new Object[] { 0, 14901 };
	/** 已激活过该灵火 */
	public static final Object[] LH_YI_JIHUO = new Object[] { 0, 14902 };
	/** 没有达到激活条件 */
	public static final Object[] LH_NO_TIAOJIAN = new Object[] { 0, 14903 };

	// ------------------------------灵火祝福-----------------------------
	/** 14917:灵火孔位号错误 **/
	public static final Object[] LH_BLESS_SLOT_ERROR = new Object[] { 0, 14917 };
	/** 14918:灵火尚未激活 **/
	public static final Object[] LH_BLESS_NOT_ACTIVATE = new Object[] { 0, 14918 };
	/** 14919:请求镶嵌的灵火不存在 **/
	public static final Object[] LH_BLESS_NOT_EXIST = new Object[] { 0, 14919 };
	/** 14920:灵火孔位上已镶嵌 **/
	public static final Object[] LH_BLESS_ALREADY_EXIST = new Object[] { 0, 14920 };
	/** 14921:镶嵌道具等级错误 **/
	public static final Object[] LH_BLESS_ITEM_LEVEL = new Object[] { 0, 14921 };

	// --------------------------------领地战---------------------------
	/** 您还未加入门派，请加入门派 **/
	public static final Object[] TERRITORY_NO_GUILD = new Object[] { 0, 14717 };
	/** 您没有权利拔起旗帜，门派掌门或副掌门才可拔起 **/
	public static final Object[] TERRITORY_POSITION_LIMIT = new Object[] { 0, 14718 };
	/** 门派银两不足 **/
	public static final Object[] TERRITORY_NO_GOLD = new Object[] { 0, 14719 };
	/** 活动未开始 **/
	public static final Object[] TERRITORY_NO_START = new Object[] { 0, 14720 };
	/** 等级不足 **/
	public static final Object[] TERRITORY_LEVEL_NO = new Object[] { 0, 14721 };
	/** 不能领取 **/
	public static final Object[] TERRITORY_CAN_NOT_REWARD = new Object[] { 0, 14723 };
	/** 活动期间不能领取 **/
	public static final Object[] TERRITORY_IN = new Object[] { 0, 14724 };
	/** 今日已领取领取 **/
	public static final Object[] TERRITORY_ALREADY_REWARD = new Object[] { 0, 14725 };
	/** 领地战活动奖励 **/
	public static final String TERRITORY_WIN_EAMIL_CONTENT_TITLE = "14742";
	/** 胜方奖励 **/
	public static final String TERRITORY_WIN_EAMIL_CONTENT = "14726";
	/** 败方奖励 **/
	public static final String TERRITORY_LOSE_EAMIL_CONTENT = "14727";
	/** 15分钟普奖 **/
	public static final String TERRITORY_15_MINUTE_REWARD = "14728";
	/** 旗帜已被抢 **/
	public static final Object[] TERRITORY_FLAG_NOT_EXIST = new Object[] { 0, 14729 };
	/** XXX帮帮主勇猛无敌，拔起XXX的旗帜 **/
	public static final int TERRITORY_FETCH_FLAG = 14730;
	/** XXXXX门派成功占领XXXX **/
	public static final int TERRITORY_HAVE_MAP = 14731;
	/** 活动已结束 **/
	public static final Object[] TERRITORY_ACTIVE_END = new Object[] { 0, 14735 };
	/** 请明日领取奖励 **/
	public static final Object[] TERRITORY_CAN_NOT_REWARD_FIRST_DAY = new Object[] { 0, 14738 };

	// ----------------------------争霸赛------------------------------------
	/**争霸赛未开始 */
	public static final Object[] HCZBS_NOT_START = new Object[] { 0, 16100 };
	/** 奖励邮件标题 **/
	public static final String HCZBS_EAMIL_TITLE = "16113";
	/** 胜方奖励 **/
	public static final String HCZBS_WIN_EAMIL_CONTENT = "16102";
	/** 败方奖励 **/
	public static final String HCZBS_LOSE_EAMIL_CONTENT = "16103";
	/** 15分钟普奖 **/
	public static final String HCZBS_15_MINUTE_REWARD = "16104";
	/** 旗帜已被抢 **/
	public static final Object[] HCZBS_FLAG_NOT_EXIST = new Object[] { 0, 16105 };
	/** 活动已结束 **/
	public static final Object[] HCZBS_ACTIVE_END = new Object[] { 0, 16106 };
	/** 您还未加入门派，请加入门派 **/
	public static final Object[] HCZBS_NO_GUILD = new Object[] { 0, 16107 };
	/** 您没有权利拔起旗帜，门派掌门或副掌门才可拔起 **/
	public static final Object[] HCZBS_POSITION_LIMIT = new Object[] { 0, 16108 };
	/** 活动期间不能领取 **/
	public static final Object[] HCZBS_IN = new Object[] { 0, 16109 };
	/**旗帜被永久占领 **/
	public static final Object[] HCZBS_FLAG_HOLD_END = new Object[] { 0, 16110 };
	/** 活动胜利增加帮贡邮件通知 **/
	public static final String ACTIVE_BANGGONG_WIN_EMAIL = "16111";
	/** 活动失败帮贡邮件通知 **/
	public static final String ACTIVE_BANGGONG_FAIL_EMAIL = "16112";

	// ----------------------------灵境----------------------------------
	/** 没有达到突破条件 */
	public static final Object[] LINGJING_NO_TUPO_TIAOJIAN = new Object[] { 0, 15006 };
	/** 已达到最大层数 */
	public static final Object[] LINGJING_IS_MAX_CENG = new Object[] { 0, 15008 };
	/** 该类型已激活 */
	public static final Object[] LINGJING_TYPE_IS_ACTIVED = new Object[] { 0, 15009 };
	/** 没有达到激活条件 */
	public static final Object[] LINGJING_NO_ACTIVE_TIAOJIAN = new Object[] { 0, 15010 };
	// ----------------------------温泉----------------------------------
	/** 今日丢肥皂搓背次数已用完 */
	public static final Object[] PLAY_TIMES_LIMIT_1 = new Object[] { 0, 15201 };
	/** 今日搓背次数已用完 */
	public static final Object[] PLAY_TIMES_LIMIT_2 = new Object[] { 0, 15202 };
	/** 今日银两聚灵次数已用完 */
	public static final Object[] WENQUAN_MONEY_TIMES_LIMIT = new Object[] { 0, 15203 };
	/** 不能给自己搓背 */
	public static final Object[] WENQUAN_CAN_NOT_PLAY_MYSELF_1 = new Object[] { 0, 15220 };
	/** 不能给自己丢肥皂 */
	public static final Object[] WENQUAN_CAN_NOT_PLAY_MYSELF_2 = new Object[] { 0, 15221 };
	/** 温泉活动未开始 */
	public static final Object[] WENQUAN_ACTIVE_NOT_START = new Object[] { 0, 15225 };
	// ----------------------------连充----------------------------------
	/** 已经领取 */
	public static final Object[] GET_ALREADY_ERROR = new Object[] { 0, 15604 };

	/** 温泉结束奖励邮件 **/
	public static final String WENQUAN_REWARD_EMAIL = "15226";
	/** 圣光洗礼奖励**/
	public static final String WENQUAN_REWARD_EMAIL_TITLE = "15237";
	// ---------------------------成就-----------------------------------
	/** 没有收到请求的成就类型 */
	public static final Object[] NOT_REQUEST_CHENGJIU_TYPES = new Object[] { 0, 3309 };
	/** 没有在配置中到请求的类型 */
	public static final Object[] NOT_CHENGJIU_TYPES = new Object[] { 0, 3310 };

	// ----------------------------称号----------------------------------
	/** 称号已激活 */
	public static final Object[] CHENHGAO_IS_ACTIVATED = new Object[] { 0, 15514 };
	/** 角色等级不足 */
	public static final Object[] CHENGHAO_ROLE_LEVEL_ERROR = new Object[] { 0, 15515 };
	/** 称号不存在 */
	public static final Object[] CHENHGAO_NOT_EXIST = new Object[] { 0, 15516 };
	/** 称号已过期 */
	public static final Object[] CHENHGAO_IS_EXPIRE = new Object[] { 0, 15517 };
	/** 称号已穿在身上 */
	public static final Object[] CHENHGAO_IS_WEARING = new Object[] { 0, 15518 };
	/** 称号未穿在身上 */
	public static final Object[] CHENHGAO_IS_NOT_WEARING = new Object[] { 0, 15519 };
	/** 称号穿戴数量已达上限 */
	public static final Object[] CHENHGAO_WEARING_NUM_LIMIT = new Object[] { 0, 15520 };
	/** 道具不足 */
	public static final Object[] CHENHGAO_ITEM_LIMIT = new Object[] { 0, 15521 };
	/** 成就称号不可手动激活 */
	public static final Object[] CHENGJIU_CHENGHAO_CANNOT_ACTIVE = new Object[] { 0, 15529 };
	// --------------------QQ充值=------------------------
	/** 服务器繁忙 */
	public static final Object[] FUWU_QI_FANMANG = new Object[] { 0, 15742 };

	// ----------------------------qq平台----------------------------------
	/** 不是黄钻不能领取 */
	public static final Object[] QQ_NOT_HUANGZUAN = new Object[] { 0, 15605 };
	/** 新手礼包已领取 */
	public static final Object[] QQ_XINSHOU_GIFT_REWARED = new Object[] { 0, 15606 };
	/** 每日礼包已领取 */
	public static final Object[] QQ_MEIRI_GIFT_REWARED = new Object[] { 0, 15607 };
	/** 该等级的成长礼包已领取 */
	public static final Object[] QQ_CHENGZHANG_GIFT_REWARED = new Object[] { 0, 15608 };
	/** 不存在该等级的成长 */
	public static final Object[] QQ_CHENGZHANG_GIFT_NOT_EXSITS = new Object[] { 0, 15609 };
	/** 等级不足，无法领取 */
	public static final Object[] QQ_CHENGZHANG_GIFT_LEVEL_LIMIT = new Object[] { 0, 15610 };
	/** 平台错误 */
	public static final Object[] QQ_PLATFORM_ERROR = new Object[] { 0, 15611 };
	/** 不是蓝钻不能领取 */
	public static final Object[] QQ_NOT_LANZUAN = new Object[] { 0, 15626 };
	/** 你赠送的角色不存在或不在线 */
	public static final Object[] QQ_TGP_NO_ZAIXIAN = new Object[] { 0, 15628 };
	/** 正能量值不够 */
	public static final Object[] QQ_TGP_NO_NLVALUE = new Object[] { 0, 15629 };
	/** 对方今日已获得3次赠送，不能再赠送给他了 */
	public static final Object[] QQ_TGP_NO_ZSONG = new Object[] { 0, 15630 };
	/** 不满足领取条件 */
	public static final Object[] QQ_NO_LANZUAN_XUFEI = new Object[] { 0, 15631 };
	/** 不满足购买条件 */
	public static final Object[] QQ_NO_LANZUAN_BUY = new Object[] { 0, 15632 };
	/** 已领取过该奖励 */
	public static final Object[] QQ_YAOQING_YI_LINGQU = new Object[] { 0, 15640 };

	// -------------------------任务集市邮件-------------------
	public static final String RWJS_YOUJIAN = "15741";
	public static final String RWJS_YOUJIAN_EMAIL_TITLE = "15744";
	/** 参数错误 */
	public static final Object[] QQ_ARGS_ERROR = new Object[] { 0, 15612 };
	// -------------------------妖神（1500-1899）-------------------
	/** 功能未开启 */
	public static final Object[] YAOSHEN_NOT_OPEN = new Object[] { 0, 1512 };
	/** 已是最高级 */
	public static final Object[] YAOSHEN_TOP_LEVEL = new Object[] { 0, 1513 };
	/** 道具不足 */
	public static final Object[] YAOSHEN_ITEM_NOT_ENOUGH = new Object[] { 0, 1514 };
	/** 道具错误 */
	public static final Object[] YAOSHEN_ITEM_ERROR = new Object[] { 0, 1515 };
	/** 妖神等级不足,不能使用 */
	public static final Object[] YAOSHEN_LEVEL_LIMIT_CAN_NOT_USE = new Object[] { 0, 1516 };
	/** 潜能丹使用数量达上限 */
	public static final Object[] YAOSHEN_QND_MAX_NUM = new Object[] { 0, 1517 };
	/** 成长丹使用数量达上限 */
	public static final Object[] YAOSHEN_CZD_MAX_NUM = new Object[] { 0, 1518 };
	/** 妖神已激活 */
	public static final Object[] YAOSHEN_IS_ACTIVATED = new Object[] { 0, 1519 };
	/** 在cd中 */
	public static final Object[] YAOSHEN_SHOW_CHANGE_CD = new Object[] { 0, 1520 };
	/** 所需妖神层数不足 */
	public static final Object[] NO_ENOUGH_YAOSHEN_CENG = new Object[] { 0, 1524 };
	/** 活动期间不能切换妖神 */
	public static final Object[] WENQUAN_NOT_CHANGE = new Object[] { 0, 1527 };
	/**妖神魔纹未开启*/
	public static final Object[] YAOSHEN_MOWEN_NOT_OPEN = new Object[] { 0, 1528 };
	/**妖神魔纹等级不足,不能使用*/
	public static final Object[] YAOSHEN_MOWEN_LEVEL_LIMIT_CAN_NOT_USE = new Object[] { 0, 1529 };
	/**妖神魔纹潜能丹使用数量达上限*/
	public static final Object[] YAOSHEN_MOWEN_QND_MAX_NUM = new Object[] { 0, 1530 };
	/**妖神魔纹成长丹使用数量达上限*/
	public static final Object[] YAOSHEN_MOWEN_CZD_MAX_NUM = new Object[] { 0, 1531 };
	public static final Object[] YAOSHEN_SHENGXING_MAX_CHAOCHU = new Object[] { 0, 16809 };
	/**聊天框提示*/
	public static final int YAOSHEN_MOWEN_UP_NOTICE1 = 1541;
	/**游戏中央提示*/
	public static final int YAOSHEN_MOWEN_UP_NOTICE2 = 1542;
	/**妖神霸体聊天框提示*/
	public static final int YAOSHEN_BATI_UP_NOTICE1 = 1532;
	/**妖神霸体游戏中央提示*/
	public static final int YAOSHEN_BATI_UP_NOTICE2 = 1533;
	/**恶魔未激活*/
	public static final Object[] YAOSHEN_NOT_ACTIVATE = new Object[] { 0, 1578 };
	// ==============妖神魂魄===============
	/**妖神魂魄未激活*/
	public static final Object[] YAOSHEN_HUNPO_NOT_OPEN = new Object[] { 0, 1543 };
	/**聊天框提示*/
	public static final int YAOSHEN_HUNPO_UP_NOTICE1 = 1544;
	/**游戏中央提示*/
	public static final int YAOSHEN_HUNPO_UP_NOTICE2 = 1545;
	/**妖神魂魄等级不足,不能使用*/
	public static final Object[] YAOSHEN_HUNPO_LEVEL_LIMIT_CAN_NOT_USE = new Object[] { 0, 1546 };
	/**妖神魔纹潜能丹使用数量达上限*/
	public static final Object[] YAOSHEN_HUNPO_QND_MAX_NUM = new Object[] { 0, 1547 };
	/**妖神魔纹成长丹使用数量达上限*/
	public static final Object[] YAOSHEN_HUNPO_CZD_MAX_NUM = new Object[] { 0, 1548 };
	/**此魂魄未激活*/
	public static final Object[] YAOSHEN_HUNPO_NOT_ACTIVE = new Object[] { 0, 1549 };
	/**魂魄镶嵌是同一个魄神*/
	public static final Object[] YAOSHEN_HUNPO_ON_SAME = new Object[] { 0, 1550 };
	/**这个格位没有镶嵌魄神*/
	public static final Object[] YAOSHEN_HUNPO_POSHEN_POSITION_NO = new Object[] { 0, 1551 };
	/**格位有误*/
	public static final Object[] YAOSHEN_HUNPO_POSHEN_ON_INDEX_ERROR = new Object[] { 0, 1552 };

	// ----------------------------幸运拉霸----------------------------------
	public static final int LABA_REWARD_NOTICE = 16473;

	// ----------------------------合服七登----------------------------------
	/** 合服七登已过期 */
	public static final Object[] HEFU_SEVEN_LOGIN_EXPIRE = new Object[] { 0, 15730 };
	// ----------------------------宝石----------------------------------

	/**道具不能合成 */
	public static final Object[] JEWEL_TYPE_ERROR = new Object[] { 0, 15900 };
	/** 不能镶嵌在这个孔位上 */
	public static final Object[] JEWEL_UP_POSITION_ERROR = new Object[] { 0, 15901 };
	/** 孔还未开启 */
	public static final Object[] JEWEL_KONG_NOT_OPEN = new Object[] { 0, 15902 };
	/** 到顶级了不能再合成 */
	public static final Object[] JEWEL_LEVEL_MAX = new Object[] { 0, 15903 };
	/** 不能重复开孔 */
	public static final Object[] JEWEL_DAKONG_CHONGFU = new Object[] { 0, 15904 };
	/** 不能越级开孔 */
	public static final Object[] JEWEL_DAKONG_YUEJI = new Object[] { 0, 15905 };
	/** 同一颗宝石已经镶嵌了，不要重复镶嵌 */
	public static final Object[] JEWEL_UP_ALREADY = new Object[] { 0, 15906 };
	/** 此位置上没有镶嵌宝石不能卸载 */
	public static final Object[] JEWEL_DOWN_ERROR = new Object[] { 0, 15907 };

	// ----------------------------系统设置------------------------------------
	/** 很抱歉，对方拒绝添加好友 */
	public static final Object[] OPERATE_ERROR_TYPE_FIREND = new Object[] { 0, 15759 };
	/** 很抱歉，对方拒绝进行交易 */
	public static final Object[] OPERATE_ERROR_TYPE_TRADE = new Object[] { false, 15760 };
	/** 很抱歉，对方拒绝私聊 */
	public static final Object[] OPERATE_ERROR_TYPE_CHAT = new Object[] { 0, 15761 };
	/** 很抱歉，对方拒绝加入门派 */
	public static final Object[] OPERATE_ERROR_TYPE_GUILD = new Object[] { 0, 15762 };
	/** 很抱歉，对方拒绝加入队伍 */
	public static final Object[] OPERATE_ERROR_TYPE_TEAM = new Object[] { 0, 15763 };
	// ----------------------------超值兑换------------------------------------
	/** 兑换次数已达上限 */
	public static final Object[] SUPER_DUIHUAN_COUNT_TIMES_LIMIT = new Object[] { 0, 15950 };
	/** 道具不足 */
	public static final Object[] SUPER_DUIHUAN_ITEM_NOT_ENOUGH = new Object[] { 0, 15951 };
	/** 道具错误 */
	public static final Object[] SUPER_DUIHUAN_ITEM_ERROR = new Object[] { 0, 15952 };

	// ----------------------------宠物------------------------------------
	/** 该宠物已激活 */
	public static final Object[] CHONGWU_ACTIVATED = new Object[] { 0, 16000 };
	/** 道具不足 */
	public static final Object[] CHONGWU_ITEM_NOT_ENOUGH = new Object[] { 0, 16001 };
	/** 道具错误 */
	public static final Object[] CHONGWU_ITEM_ERROR = new Object[] { 0, 16002 };
	/** 该宠物未激活 */
	public static final Object[] CHONGWU_NOT_ACTIVATED = new Object[] { 0, 16003 };
	/** 已是最高级 */
	public static final Object[] CHONGWU_TOP_LEVEL = new Object[] { 0, 16004 };

	/**功能未开启*/
	public static final Object[] CHONGWU_NOT_OPEN = new Object[] { 0, 16005 };
	/**银两不足*/
	public static final Object[] CHONGWU_MONEY_NOT_ENOUGH = new Object[] { 0, 16006 };
	/**没有出战宠物*/
	public static final Object[] CHONGWU_NO_FIGHT = new Object[] { 0, 16008 };
	/** 宠物技能错误 **/
	public static final Object[] CHONGWU_SKILL_ID_ERROR = new Object[] { 0, 19452 };
	/** 宠物技能已经达到最高等级 */
	public static final Object[] CHONGWU_SKILL_MAX_LEVEL = new Object[] { 0, 19453 };
	// -------------------------------结婚--------------------------------------
	/**当前不是单身*/
	public static final Object[] JIEHUN_NOT_NO_MARRY = new Object[] { 0, 16130 };
	/**当前在离婚CD中*/
	public static final Object[] JIEHUN_IN_DIVORCE_CD = new Object[] { 0, 16131 };
	/**当前已有订婚申请*/
	public static final Object[] JIEHUN_HAS_DING_APPLY = new Object[] { 0, 16132 };
	/**订婚双方必须在队伍中*/
	public static final Object[] JIEHUN_BOTH_IN_TEAM = new Object[] { 0, 16133 };
	/**目标不是单身*/
	public static final Object[] JIEHUN_TARGET_NOT_NO_MARRY = new Object[] { 0, 16134 };
	/**当前没有订婚申请*/
	public static final Object[] JIEHUN_NO_DING_APPLY = new Object[] { 0, 16135 };
	/**对方没有足够银两*/
	public static final Object[] JIEHUN_TARGET_NO_ENOUGH_MONEY = new Object[] { 0, 16138 };
	/**没有订婚*/
	public static final Object[] JIEHUN_NOT_DING = new Object[] { 0, 16139 };
	/**没有足够缘分值*/
	public static final Object[] JIEHUN_NO_ENOUGH_YUANFEN = new Object[] { 0, 16140 };
	/**对方没有足够道具*/
	public static final Object[] JIEHUN_TARGET_NO_ENOUGH_ITEM = new Object[] { 0, 16141 };
	/**对方没有足够元宝*/
	public static final Object[] JIEHUN_TARGET_NO_ENOUGH_GOLD = new Object[] { 0, 16142 };
	/**当前未结婚*/
	public static final Object[] JIEHUN_NO_IN_MARRY = new Object[] { 0, 16143 };
	/**已经发送离婚申请*/
	public static final Object[] JIEHUN_HAS_SEND_DIVORCE_APPLY = new Object[] { 0, 16144 };
	/**对方没有发送离婚申请*/
	public static final Object[] JIEHUN_TARGET_NOT_SEND_DIVORCE = new Object[] { 0, 16145 };
	/**当前不在离婚状态*/
	public static final Object[] JIEHUN_NOT_IN_DIVORCE = new Object[] { 0, 16147 };
	/**已经离婚了*/
	public static final Object[] JIEHUN_HAS_DIVORCE = new Object[] { 0, 16148 };
	/**已达到最大等级*/
	public static final Object[] JIEHUN_IS_MAX_LEVEL = new Object[] { 0, 16149 };
	/**只能换更高级的信物*/
	public static final Object[] JIEHUN_ONLY_CAN_CHANGE_BETTER = new Object[] { 0, 16150 };
	/**对方没有发送求婚申请*/
	public static final Object[] JIEHUN_NO_MARRY_APPLY = new Object[] { 0, 16160 };
	/**已达到最大缘分值*/
	public static final Object[] JIEHUN_IS_MAX_YUANFEN = new Object[] { 0, 16161 };
	/**对方正在打开订婚面板*/
	public static final Object[] JIEHUN_TARGET_IS_OPEN_DINGHUN = new Object[] { 0, 16162 };
	/**对方正在打开信物面板*/
	public static final Object[] JIEHUN_TARGET_IS_OPEN_CHANGE = new Object[] { 0, 16163 };
	/**心情字数超过上限*/
	public static final Object[] JIEHUN_XINQING_TOO_LENGTH = new Object[] { 0, 16164 };
	/**拒绝离婚回应*/
	public static final Object[] JIEHUN_REFUSE_DIVORCE = new Object[] { 1, null };
	// ----------------------------跨服充值排名活动------------------------------------
	/**奖励邮件内容：您在本地跨服充值排名活动中获得第{0}名，附件为奖励！**/
	public static final String KUAFU_CHARGE_RANK_REWARD = "16200";
	/**您在本次跨服充值排名活动中获得第一名且达到特殊奖励发放要求，附件为特殊奖励！**/
	public static final String KUAFU_CHARGE_SPECIAL_REWARD = "16201";
	/**充值排名奖励**/
	public static final String KUAFU_CHARGE_SPECIAL_REWARD_TITLE = "16202";
	// ----------------------------跨服消费排名活动------------------------------------
	/**奖励邮件内容：您在本地跨服消费排名活动中获得第{0}名，附件为奖励！**/
	public static final String KUAFU_XIAOFEI_RANK_REWARD = "16206";
	/**您在本次跨服消费排名活动中获得第一名且达到特殊奖励发放要求，附件为特殊奖励！**/
	public static final String KUAFU_XIAOFEI_SPECIAL_REWARD = "16207";
	/**消费排名奖励**/
	public static final String KUAFU_XIAOFEI_SPECIAL_REWARD_TITLE = "16208";
	// --------------------------累计充值--------------------------------------
	/**充值未达到领取最低条件*/
	public static final Object[] RECHARGE_FANLI_MINGOLD = new Object[] { 0, 16213 };
	/**没有可领取的元宝*/
	public static final Object[] RECHARGE_FANLI_NOGOLD = new Object[] { 0, 16214 };
	/**未充值，不可领取*/
	public static final Object[] RECHARGE_NORE_NOLINGQU = new Object[] { 0, 16218 };
	/**无需补充*/
	public static final Object[] RECHARGE_NO_BUQIAN = new Object[] { 0, 16219 };
	/**补充次数不足，请先充值*/
	public static final Object[] RECHARGE_NO_BUQIAN_RE = new Object[] { 0, 16220 };
	/**累充7天大奖邮件标题*/
	public static final String LEICHONG_EMAIL_TITLE = "16221";
	/**累充7天大奖邮件内容*/
	public static final String LEICHONG_EMAIL_CONTENT = "16222";
	
	// ----------------------------欢乐卡牌活动------------------------------------
	/**翻牌次数已达上限*/
	public static final Object[] CARD_FAN_TIMES_LIMIT = new Object[] { 0, 16230 };
	/**格位错误*/
	public static final Object[] CARD_INEDX_ERROR = new Object[] { 0, 16231 };
	/**重置次数已达上限*/
	public static final Object[] CARD_RESET_TIMES_LIMIT = new Object[] { 0, 16232 };

	// ----------------------------淬炼------------------------------------
	/**淬炼类型错误*/
	public static final Object[] CUI_LIAN_TYPE_ERROR = new Object[] { 0, 16310 };
	/**淬炼次数已达上限*/
	public static final Object[] CUI_LIAN_TIMES_LIMIT = new Object[] { 0, 16311 };

	// ----------------------------改名------------------------------------
	/**道具不足*/
	public static final Object[] MODIFY_NAME_ITEM_ERROR= new Object[]{0,22512};
	/**改名卡cd中，请稍后再试*/
	public static final Object[] MODIFY_NAME_IN_CD= new Object[]{0,22505};
	// -------------------------附属技能-------------------------------------
	/**没有可以学习技能的格位*/
	public static final Object[] FUSHU_SKILL_NO_GEZI = new Object[] { 0, 16411 };
	/**锁定数量超过上限*/
	public static final Object[] FUSHU_SKILL_SUOXING_MAX = new Object[] { 0, 16412 };
	// -------------------------混沌战场-------------------------------------
	/**当前不可进入下一层*/
	public static final Object[] HUNDUN_NOT_IN_ENTER_NEXT_TIME = new Object[] { 0, 6440 };
	/**您不在可进入下层名单中*/
	public static final Object[] HUNDUN_CANNOT_ENTER_NEXT = new Object[] { 0, 6441 };
	// -------------------------红包-------------------------------------
	/**红包已领取*/
	public static final Object[] HONGBAO_GET_ALREADY = new Object[] { 0, 16462 };
	/**红包不存在或者已过期*/
	public static final int HONGBAO_NO_EXIST = 16463;
	/**红包已过期*/
	public static final int HONGBAO_EXPIRE = 16464;
	/**玩家登陆前的红包不可领取，此红包不可领取*/
	public static final Object[] HONGBAO_NOT_GET = new Object[] { 0, 16465 };
	/**没中*/
	public static final Object[] HONGBAO_MISS = new Object[] { 0, 16466 };

	// ----------------------------个人跨服竞技------------------------------------
	/**取消失败*/
	public static final Object[] KUAFU_ARENA_1V1_CANCEL_MATCH_FAIL = new Object[] { 0, 2858 };
	/**等级不足*/
	public static final Object[] KUAFU_ARENA_1V1_LEVEL_LIMIT = new Object[] { 0, 2859 };
	/**不在开放时间内*/
	public static final Object[] KUAFU_ARENA_1V1_NOT_OPEN_TIME = new Object[] { 0, 2860 };
	/**进入副本失败*/
	public static final Object[] KUAFU_ARENA_1V1_ENTER_FAIL = new Object[] { 0, 2861 };
	/**正在匹配中*/
	public static final Object[] KUAFU_ARENA_1V1_MATCH_ING = new Object[] { 0, 2862 };
	/**正在准备中*/
	public static final Object[] KUAFU_ARENA_1V1_PREPARE_TING = new Object[] { 0, 2863 };
	/**正在pk中，不能再次匹配*/
	public static final Object[] KUAFU_ARENA_1V1_PK_ING = new Object[] { 0, 2864 };
	/**准备状态下，不能退出*/
	public static final Object[] KUAFU_ARENA_1V1_CAN_NOT_EXIT = new Object[] { 0, 2865 };
	/**请退出当前副本*/
	public static final Object[] KUAFU_ARENA_EXIT_CURRENT = new Object[] { 0, 2866 };
	/**不能领取*/
	public static final Object[] KUAFU_ARENA_CAN_NOT_PICK = new Object[] { 0, 2867 };
	/**今日奖励已领取*/
	public static final Object[] KUAFU_ARENA_CAN_PICKED = new Object[] { 0, 2868 };
	/**段位不足，不能兑换*/
	public static final Object[] KUAFU_ARENA_DUAN_LIMIT = new Object[] { 0, 2869 };
	/**功勋不足*/
	public static final Object[] KUAFU_ARENA_GONGXUN_LIMIT = new Object[] { 0, 2870 };
	/**今日兑换数量已达上限*/
	public static final Object[] KUAFU_ARENA_DAY_NUM_LIMIT = new Object[] { 0, 2871 };
	/**奖励邮件内容*/
	public static final String KUAFU_ARENA_REWARD_MAIL_CODE = "2873";
	/**奖励邮件标题*/
	public static final String KUAFU_ARENA_REWARD_MAIL_CODE_TITLE = "2886";
	/** 匹配服尚未连接 */
	public static final Object[] MATCH_SERVER_NOT_CONNECT = new Object[] { 0, 2880 };

	// ----------------------------累计消耗------------------------------------
	public static final Object[] LEIHAO_CAN_NOT_PICK = new Object[] { 0, 16431 };

	// ----------------------------八卦副本--------------------------------------------
	/** 已有队伍 */
	public static final Object[] BAGUA_TEAM_EXISTS = new Object[] { 0, 16603 };
	/** 队伍不存在 */
	public static final Object[] BAGUA_TEAM_NOT_EXISTS = new Object[] { 0, 16604 };
	/** 已经在队伍中 */
	public static final Object[] BAGUA_TEAM_IN = new Object[] { 0, 16605 };
	/** 已经加入其它副本 */
	public static final Object[] BAGUA_TEAM_OTHER_IN = new Object[] { 0, 16606 };
	/** 人数已达上限 */
	public static final Object[] BAGUA_TEAM_PEOPLE_COUNT = new Object[] { 0, 16607 };
	/** 密码不正确 */
	public static final Object[] BAGUA_TEAM_PWD_ERROR = new Object[] { 0, 16608 };
	/** 战力不足 */
	public static final Object[] BAGUA_TEAM_STRENGTH_LESS = new Object[] { 0, 16609 };
	/** 当前有玩家没准备好，无法开始挑战 */
	public static final Object[] BAGUA_TEAM_NOT_PREPARED = new Object[] { 0, 16610 };
	/** 队伍中没有该玩家 */
	public static final Object[] BAGUA_TEAM_NOT_IN = new Object[] { 0, 16611 };
	/** 当前没有队伍可以加入 */
	public static final Object[] BAGUA_TEAM_JOIN_ERROR = new Object[] { 0, 16612 };
	/** 队长无需准备 */
	public static final Object[] BAGUA_TEAM_NO_NEED = new Object[] { 0, 16613 };
	/** 每天只能第一次领取哦 */
	public static final Object[] BAGUA_TEAM_REWARDED_ONCE = new Object[] { 0, 16614 };
	/** 当前副本队伍已达上限 */
	public static final Object[] BAGUA_FUBEN_TEAM_LIMIT_ERROR = new Object[] { 0, 16615 };
	/** 多人副本进入倒计时，不能加入 */
	public static final Object[] BAGUA_FUBEN_DJS = new Object[] { 0, 16616 };
	/** 未找到其他队伍 */
	public static final Object[] BAGUA_FUBEN_NO_AVAILABLE_TEAM = new Object[] { 0, 5547 };
	// -----------------------------彩蛋------------------------------------
	/**该蛋已被砸过*/
	public static Object[] THIS_EGG_IS_OPEN = new Object[] { 0, 16501 };
	/**选择的蛋异常*/
	public static Object[] THIS_EGG_ERROR = new Object[] { 0, 16502 };
	/**不再活动期间内*/
	public static Object[] NOT_IN_TIME = new Object[] { 0, 16503 };
	/**所有的蛋已被砸过*/
	public static Object[] ALL_EGGS_IS_OPEN = new Object[] { 0, 16504 };
	/**没有足够的积分*/
	public static Object[] NO_ENOUGH_SCORE = new Object[] { 0, 16505 };

	// ----------------------------塔防------------------------------------
	/**当前没有可领取经验值*/
	public static Object[] TAFANG_NO_EXP = new Object[] { 0, 16701 };
	/**进入次数已满*/
	public static Object[] TAFANG_NO_COUNT = new Object[] { 0, 16702 };
	/**上次经验尚未领取*/
	public static Object[] TAFANG_NO_RECIVE_LAST_EXP = new Object[] { 0, 16703 };
	/**当前位置没有NPC*/
	public static Object[] TAFANG_NO_NPC = new Object[] { 0, 16704 };
	/**NPC已到最高等级*/
	public static Object[] TAFANG_NPC_IS_MAX_LEVEL = new Object[] { 0, 16705 };
	/**当前位置已有NPC*/
	public static Object[] TAFANG_HAS_NPC = new Object[] { 0, 16706 };

	// ----------------------------神魔------------------------------------
	/**取消失败*/
	public static final Object[] KUAFU_ARENA_4V4_CANCEL_MATCH_FAIL = new Object[] { 0, 11214 };
	/**取消失败*/
	public static final Object[] KUAFU_ARENA_4V4_CANCEL_MATCH_FAIL_1 = new Object[] { 2, 11214 };
	/**等级不足*/
	public static final Object[] KUAFU_ARENA_4V4_LEVEL_LIMIT = new Object[] { 0, 11215 };
	/**进入副本失败*/
	public static final Object[] KUAFU_ARENA_4V4_ENTER_FAIL = new Object[] { 0, 11216 };
	/**正在匹配中*/
	public static final Object[] KUAFU_ARENA_4V4_MATCH_ING = new Object[] { 0, 11217 };
	/**正在准备中*/
	public static final Object[] KUAFU_ARENA_4V4_PREPARE_TING = new Object[] { 0, 11218 };
	/**正在pk中，不能再次匹配*/
	public static final Object[] KUAFU_ARENA_4V4_PK_ING = new Object[] { 0, 11219 };
	/**准备状态下，不能退出*/
	public static final Object[] KUAFU_ARENA_4V4_CAN_NOT_EXIT = new Object[] { 0, 11220 };
	/**请退出当前副本*/
	public static final Object[] KUAFU_ARENA_4V4_EXIT_CURRENT = new Object[] { 0, 11221 };
	/**不能领取*/
	public static final Object[] KUAFU_ARENA_4V4_CAN_NOT_PICK = new Object[] { 0, 11222 };
	/**今日奖励已领取*/
	public static final Object[] KUAFU_ARENA_4V4_PICKED = new Object[] { 0, 11223 };
	/**奖励邮件内容*/
	public static final String KUAFU_ARENA_4V4_REWARD_MAIL_CODE = "11224";
	/**奖励邮件标题*/
	public static final String KUAFU_ARENA_4V4_REWARD_MAIL_CODE_TITLE = "11237";
	/**队伍已解散*/
	public static final Object[] KUAFU_ARENA_4V4_TEAM_DISPOSE = new Object[] { 0, 11225 };
	/**由于今天强退次数达到上限，请明天参加活动*/
	public static final Object[] KUAFU_ARENA_4V4_ESCAPE_TIMES_LIMIT = new Object[] { 0, 11227 };
	/**当前没有任何队伍*/
	public static final Object[] KUAFU_ARENA_4V4_NO_TEAM = new Object[] { 0, 11228 };
	/**只有队长才能进行此操作*/
	public static final Object[] KUAFU_ARENA_4V4_NOT_CAPTAIN = new Object[] { 0, 11229 };
	/**队伍中有成员正在副本中，无法进行匹配*/
	public static final Object[] KUAFU_ARENA_4V4_MEMBER_IN_FUBEN = new Object[] { 0, 11230 };
	/**不在开放时间内*/
	public static final Object[] KUAFU_ARENA_4V4_NOT_OPEN_TIME = new Object[] { 0, 11232 };
	/**队伍中有成员等级不足，无法进行匹配*/
	public static final Object[] KUAFU_ARENA_4V4_MEMBER_LEVEL_LIMIT = new Object[] { 0, 11233 };
	/**队伍中有成员今日逃跑次数达到上限，无法进行匹配*/
	public static final Object[] KUAFU_ARENA_4V4_MEMBER_ESCAPE_LIMIT = new Object[] { 0, 11234 };
	/**队伍中有成员无法参加，无法进行匹配*/
	public static final Object[] KUAFU_ARENA_4V4_MEMBER_CAN_NOT_COMPETITION = new Object[] { 0, 11235 };

	// ----------------------------埋骨之地--------------------------------------------
	/** 已有队伍 */
	public static final Object[] MAIGU_TEAM_EXISTS = new Object[] { 0, 16901 };
	/** 队伍不存在 */
	public static final Object[] MAIGU_TEAM_NOT_EXISTS = new Object[] { 0, 16902 };
	/** 已经在队伍中 */
	public static final Object[] MAIGU_TEAM_IN = new Object[] { 0, 16903 };
	/** 已经加入其它副本 */
	public static final Object[] MAIGU_TEAM_OTHER_IN = new Object[] { 0, 16904 };
	/** 人数已达上限 */
	public static final Object[] MAIGU_TEAM_PEOPLE_COUNT = new Object[] { 0, 16905 };
	/** 密码不正确 */
	public static final Object[] MAIGU_TEAM_PWD_ERROR = new Object[] { 0, 16906 };
	/** 战力不足 */
	public static final Object[] MAIGU_TEAM_STRENGTH_LESS = new Object[] { 0, 16907 };
	/** 当前有玩家没准备好，无法开始挑战 */
	public static final Object[] MAIGU_TEAM_NOT_PREPARED = new Object[] { 0, 16908 };
	/** 队伍中没有该玩家 */
	public static final Object[] MAIGU_TEAM_NOT_IN = new Object[] { 0, 16909 };
	/** 当前没有队伍可以加入 */
	public static final Object[] MAIGU_TEAM_JOIN_ERROR = new Object[] { 0, 16910 };
	/** 队长无需准备 */
	public static final Object[] MAIGU_TEAM_NO_NEED = new Object[] { 0, 16911 };
	/** 每天只能第一次领取哦 */
	public static final Object[] MAIGU_TEAM_REWARDED_ONCE = new Object[] { 0, 16912 };
	/** 当前副本队伍已达上限 */
	public static final Object[] MAIGU_FUBEN_TEAM_LIMIT_ERROR = new Object[] { 0, 16913 };
	/** 多人副本进入倒计时，不能加入 */
	public static final Object[] MAIGU_FUBEN_DJS = new Object[] { 0, 16914 };
	/** 未找到其他队伍 */
	public static final Object[] MAIGU_FUBEN_NO_AVAILABLE_TEAM = new Object[] { 0, 5547 };

	// ==============妖神魔印===============
	/**妖神魔印未激活*/
	public static final Object[] YAOSHEN_MOYIN_NOT_OPEN = new Object[] { 0, 16800 };
	/**妖神魔印潜能丹使用数量达上限*/
	public static final Object[] YAOSHEN_MOYIN_QND_MAX_NUM = new Object[] { 0, 16801 };
	/**妖神魔印成长丹使用数量达上限*/
	public static final Object[] YAOSHEN_MOYIN_CZD_MAX_NUM = new Object[] { 0, 16802 };
	/**妖神魔印等级不足,不能使用*/
	public static final Object[] YAOSHEN_MOYIN_LEVEL_LIMIT_CAN_NOT_USE = new Object[] { 0, 16803 };
	/**妖神魔印等级不足,不能激活*/
	public static final Object[] YAOSHEN_MOYIN_LEVEL_LIMIT_CAN_NOT_ACTIVE = new Object[] { 0, 16804 };
	/**妖神魔印已激活*/
	public static final Object[] YAOSHEN_MOYIN_ACTIVE_ALREADY = new Object[] { 0, 16805 };
	/** 道具不足 */
	public static final Object[] YAOSHEN_MOYIN_ITEM_NOT_ENOUGH = new Object[] { 0, 16806 };
	/**聊天框提示*/
	public static final int YAOSHEN_MOYIN_UP_NOTICE1 = 16807;
	/**游戏中央提示*/
	public static final int YAOSHEN_MOYIN_UP_NOTICE2 = 16808;

	// ==============成神===============
	/**已经是满级了*/
	public static final Object[] CHENG_SHEN_MAX_LIMIT_ERROR = new Object[] { 0, 6508 };
	/**神魂值不足*/
	public static final Object[] CHENG_SHEN_VALUE_NOT_ENOUGH = new Object[] { 0, 6509 };
	/**此等级已激活*/
	public static final Object[] CHENG_SHEN_LEVEL_SJ_ALREADY = new Object[] { 0, 6510 };
	/**激活等级错误*/
	public static final Object[] CHENG_SHEN_LEVEL_SJ_ERROR = new Object[] { 0, 6511 };
	// ==============糖宝心纹===============
	/**糖宝心纹未开启*/
	public static final Object[] XINWEN_NOT_OPEN = new Object[] { 0, 6610 };
	/**已是最高级 */
	public static final Object[] XINWEN_TOP_LEVEL = new Object[] { 0, 6611 };
	/** 道具不足 */
	public static final Object[] XINWEN_ITEM_NOT_ENOUGH = new Object[] { 0, 6612 };
	/** 道具错误 */
	public static final Object[] XINWEN_ITEM_ERROR = new Object[] { 0, 6613 };
	/**潜能丹使用数量达上限*/
	public static final Object[] XINWEN_QND_MAX_NUM = new Object[] { 0, 6614 };
	/**成长丹使用数量达上限*/
	public static final Object[] XINWEN_CZD_MAX_NUM = new Object[] { 0, 6615 };
	/**等级不足,不能使用*/
	public static final Object[] XINWEN_LEVEL_LIMIT_CAN_NOT_USE = new Object[] { 0, 6616 };
	/**聊天框提示*/
	public static final int TANGBAO_XINWEN_NOTICE_1 = 6617;
	/**游戏中央提示*/
	public static final int TANGBAO_XINWEN_NOTICE_2 = 6618;
	// ==============通天之路===============
	/**道具不能转换*/
	public static final Object[] TONGTIAN_ITEM_NO_CUILIAN = new Object[] { 0, 17022 };
	/**属性全部加满了*/
	public static final Object[] TONGTIAN_ITEM_ATTR_MAX = new Object[] { 0, 17031 };
	/**灵朽值不足不能转换*/
	public static final Object[] TONGTIAN_LINGXIUZHI_NOT_ENOUGH = new Object[] { 0, 17032 };

	// ---------------------------------------转生----------------------------------
	/**经验不足*/
	public static final Object[] NO_ENOUGH_EXP = new Object[] { 0, 16960 };
	/**低于最低兑换要求数量*/
	public static final Object[] ZHUANSHENG_VALUE_LOW_MIN = new Object[] { 0, 16961 };
	/**所需修为不足*/
	public static final Object[] ZHUANSHENG_NO_ENOUGH_XIUWEI = new Object[] { 0, 16962 };

	// --------------------------------------越南邀请好友------------------------
	/**领取次数不足*/
	public static final Object[] YUENAN_YAOQING_LINGQU_NOT_COUNT = new Object[] { 0, 17506 };
	// ----------------------------单服充值排名活动------------------------------------
	/**恭喜你在【开服充值排行】活动中获得了【XX】名，同时您的累充额度达到了【数量】元宝，获得了【道具名】*1，以及常规排行奖励*/
	public static final String DANFU_CHARGE_RANK_EWAI = "17018";
	/**喜你在【开服充值排行】活动中获得了【XX】名，但是您的累充额度未达到【数量】元宝，没有获得额外奖励，只有常规排行奖励*/
	public static final String DANFU_CHARGE_RANK_NO_EWAI = "17019";
	/**恭喜你在【{0}】活动中获得了【{1}】名，获得了以下奖励(0是活动名字)**/
	public static final String DANFU_CHARGE_RANK_REWARD = "17021";
	/**充值排名奖励**/
	public static final String DANFU_CHARGE_RANK_REWARD_TITLE = "17017";
	// ----------------------------遗迹boss
	// 奖励邮件------------------------------------
	/** 遗迹boss击杀奖励发邮件 */
	public static final String YIJIBOSS_KILL_EMAIL_CONTENT_CODE = "17052";
	/** 遗迹boss排名奖励发邮件 */
	public static final String YIJIBOSS_RANK_EMAIL_CONTENT_CODE = "17053";
	/** 遗迹boss参与奖励发邮件 */
	public static final String YIJIBOSS_ATTACK_EMAIL_CONTENT_CODE = "17054";
	/** 遗迹boss奖励邮件标题 */
	public static final String YIJIBOSS_ATTACK_EMAIL_CONTENT_TITLE = "17055";
	/** 遗迹boss击杀公告*/
	public static final String YIJIBOSS_KILL_NOTICE = "17051";

	/**挑战无间炼狱成功，这是给你的奖励**/
	public static final String LIANYU_BOSS_REWARD_EMAIL = "17119";
	/**邮件标题**/
	public static final String LIANYU_BOSS_REWARD_EMAIL_TITLE = "17123";
	public static final Object[] LIANYU_BOSS_DAY_ENTTER = new Object[] { 0, 17120 };
	// ----------------------------热发布在线奖励------------------------------------
	/**时间未到不能领取**/
	public static final Object[] RFB_ONLINE_REWARDS_TIME_NOT_ENOUGH = new Object[] { 0, 17803 };

	// ----------------------------送花功能------------------------------------
	/**不能跨服送*/
	public static final Object[] FLOWER_SEND_NO_KUAFU = new Object[] { 0, 17851 };
	/**不能送给自己*/
	public static final Object[] FLOWER_SEND_NO_MYSELF = new Object[] { 0, 17852 };
	/**聊天框提示*/
	public static final int FLOWER_SEND_NOTICE_ALL = 17853;
	/**恭喜您获得鲜花榜第{0}名，大量珍稀道具属于您！**/
	public static final String KUAFU_FLOWER_RANK_REWARD = "17859";
	/**鲜花榜奖励**/
	public static final String KUAFU_FLOWER_RANK_REWARD_TITLE = "17863";

	// ----------------------------腾讯管家------------------------------------
	/**请求管家登录没有返回数据*/
	public static final Object[] TENCENT_GUANJIA_NO_DATA = new Object[] { 0, 18000 };
	/**请求管家登录查询验证openid失败*/
	public static final Object[] TENCENT_GUANJIA_OPENID_ERROR = new Object[] { 0, 18001 };
	/**请求管家登录信息失败，检查参数*/
	public static final Object[] TENCENT_GUANJIA_DATA_RETURN_ERROR = new Object[] { 0, 18002 };
	/**管家登录不在线*/
	public static final Object[] TENCENT_GUANJIA_OFFLINE = new Object[] { 0, 18003 };

	// ----------------------------妖神附魔------------------------------------
	/**不能超过最大等级*/
	public static final Object[] YAOSHEN_FUMO_MAX_LEVEL = new Object[] { 0, 18010 };
	/**当前槽位升级不满足霸体最低激活等级*/
	public static final Object[] YAOSHEN_FUMO_CAOWEI_SJ_ERROR_1 = new Object[] { 0, 18011 };
	/**当前槽位未激活*/
	public static final Object[] YAOSHEN_FUMO_CAOWEI_SJ_NOT_ACTIVE = new Object[] { 0, 18012 };
	/**当前槽位与霸体激活等级不匹配*/
	public static final Object[] YAOSHEN_FUMO_CAOWEI_SJ_ERROR_2 = new Object[] { 0, 18013 };

	// ----------------------------跨服世界boss------------------------------------
	/**活动已经结束*/
	public static final Object[] KUAFU_BOSS_ACTIVE_END = new Object[] { 0, 18045 };
	/**当前服务器繁忙*/
	public static final Object[] KUAFU_BOSS_ACTIVE_FULL = new Object[] { 0, 18046 };
	/**奖励邮件内容：您在本次跨服世界boss活动中获得第{0}名，附件为奖励！**/
	public static final String KUAFUBOSS_RANK_REWARD = "18041";
	/**奖励邮件内容：本次跨服世界boss活动参与奖，附件为奖励！**/
	public static final String KUAFUBOSS_JOIN_REWARD = "18042";
	/**奖励邮件内容：本次跨服世界boss活动最后一击，附件为奖励！**/
	public static final String KUAFUBOSS_KILL_REWARD = "18043";
	/**跨服BOSS邮件标题**/
	public static final String KUAFUBOSS_REWARD_EMAIL_TITLE = "18051";

	// ----------------------------画卷-----------------------------------
	/**该画卷已收集，不能重复收集*/
	public static final Object[] HUAJUAN_ALREADY_HAS = new Object[] { 0, 6254 };
	/**该画卷不存在*/
	public static final Object[] HUAJUAN_NOT_EXIT = new Object[] { 0, 6255 };
	/**该画卷装备数量达到上限*/
	public static final Object[] HUAJUAN_EQUIP_NUM_LIMIT = new Object[] { 0, 6256 };
	/** 道具不足 */
	public static final Object[] HUAJUAN_ITEM_NOT_ENOUGH = new Object[] { 0, 6257 };
	/** 画卷等级已达最高等级 */
	public static final Object[] HUAJUAN_LEVEL_TOP = new Object[] { 0, 6258 };
	/** 不能批量使用 */
	public static final Object[] HUAJUAN_CAN_NOT_BATCH = new Object[] { 0, 6261 };
	/**该画卷未收集，不能升级*/
	public static final Object[] HUAJUAN_WEIJIHUO_NO_SHENGJI = new Object[] { 0, 6273 };
	/**道具不能用于画卷升级*/
	public static final Object[] HUAJUAN_ITEM_NO_SHENGJI = new Object[] { 0, 6274 };
	/**该画卷已经升到顶级了*/
	public static final Object[] HUAJUAN_SHENGJI_DAODING = new Object[] { 0, 6275 };
	// ------------------------------团购--------------------------
	/** 不在购买时间买，不能购买*/
	public static final Object[] TUANGOU_TIME_ERROR = new Object[] { 0, 18109 };
	/** 你已经买得够多了，不能再购买*/
	public static final Object[] TUANGOU_COUNT_ERROR = new Object[] { 0, 18110 };
	/**团购邮件*/
	public static final String TUANGOU_EMAIL_CONTENT = "18112";
	/**团购邮件标题*/
	public static final String TUANGOU_EMAIL_CONTENT_TITLE = "18113";
	// ----------------------------幻化-----------------------------------
	/**不能重复激活*/
	public static final Object[] HUANHUA_ACTIVATED = new Object[] { 0, 18169 };
	/**未激活*/
	public static final Object[] HUANHUA_NOT_ACTIVATED = new Object[] { 0, 18170 };

	// --------------------------天羽--------------------------------------------
	/**天羽开启等级不足*/
	public static final Object[] TY_LEVEL_NO = new Object[] { 0, 6651 };
	/**天羽未开启*/
	public static final Object[] TY_NO_OPEN = new Object[] { 0, 6652 };
	/**天羽等级已经最高了*/
	public static final Object[] TY_LEVEL_MAX = new Object[] { 0, 6653 };
	/**天羽目标升级等级错误*/
	public static final Object[] TY_TARGET_LEVEL_ERROR = new Object[] { 0, 6654 };
	/**天羽配置异常*/
	public static final Object[] TY_CONFIG_ERROR = new Object[] { 0, 6655 };
	/**天羽升阶所需物品不足*/
	public static final Object[] TY_GOODS_BZ = new Object[] { 0, 6656 };
	/**天羽外显没有获得*/
	public static final Object[] TY_NO_SHOW = new Object[] { 0, 6657 };
	/**天羽等级不足,不能使用*/
	public static final Object[] TY_LEVEL_LIMIT_CAN_NOT_USE = new Object[] { 0, 6658 };
	/**潜能丹使用数量达上限*/
	public static final Object[] TY_QND_MAX_NUM = new Object[] { 0, 6659 };
	/**成长丹使用数量达上限*/
	public static final Object[] TY_CZD_MAX_NUM = new Object[] { 0, 6660 };
	/**天羽进阶全服公告*/
	public static final int TY_SJ_NOTICE = 6661;

	// -------------------------------跨服大乱斗--------------------------------------
	/**活动不在报名时间内*/
	public static final Object[] LD_HD_TIME_BAOMING_TIME_NO = new Object[] { 0, 6725 };
	/**抱歉，您还没有达到报名要求，请继续努力*/
	public static final Object[] LD_HD_BAOMING_TIAOJIAN_NO = new Object[] { 0, 6726 };
	/**您已经报过名了*/
	public static final Object[] LD_HD_BAOMING_YI_BAO = new Object[] { 0, 6727 };
	/**跨服大乱斗海选通知**/
	public static final String KUAFU_LUANDOU_HAIXUAN_SHENGLI_TITLE = "6742";
	/**恭喜您通过了跨服大乱斗海选赛，这是给您的奖励！**/
	public static final String KUAFU_LUANDOU_HAIXUAN_SHENGLI = "6731";
	/**很遗憾，您没有通过跨服大乱斗海选赛！**/
	public static final String KUAFU_LUANDOU_HAIXUAN_SHIBAI = "6732";
	/**恭喜你在跨服大乱斗小组赛中获得了第 {0} 名，获得了以下奖励**/
	public static final String KUAFU_LUANDOU_JIESUAN_EMAIL = "6733";
	/**跨服大乱斗小组赛奖励**/
	public static final String KUAFU_LUANDOU_JIESUAN_EMAIL_TITLE = "6743";
	/**现在不是乱斗时间*/
	public static final Object[] LD_HD_DALUANDOU_TIME_NO = new Object[] { 0, 6728 };
	/**您没有通过海选赛，不能进入跨服大乱斗*/
	public static final Object[] LD_HD_DALUANDOU_FANGJIAN_NO = new Object[] { 0, 6729 };
	/**当前服务器繁忙*/
	public static final Object[] KUAFU_DALUANDOU_ACTIVE_FULL = new Object[] { 0, 6730 };

	// -----------------------------转职------------------------------------------
	/**不能同职业转职*/
	public static final Object[] ZHUANZHI_IS_TONG_JOB = new Object[] { 0, 18255 };
	// ----------------------------老玩家回归------------------------------
	/**充值金额未达成*/
	public static final Object[] LAOWANJIA_RE_NO = new Object[] { 0, 18275 };
	/**你不是老玩家回归用户*/
	public static final Object[] LAOWANJIA_IS_NO = new Object[] { 0, 18276 };
	// ---------------------------- 神武装备星铸
	// ------------------------------------------
	/** 神武装备星铸超过最大等级 **/
	public static final Object[] ZHUANGBEI_XZ_SW_MAX_LEVEL = new Object[] { 0, 18306 };

	/**跨服群仙宴*/
	public static final Object[] KUAFU_QUNXIANYAN_CAIJI_BEI_CAIWAN = new Object[] { 0, 6868 };
	/**活动还未结束，不可领取奖励*/
	public static final Object[] KUAFU_QUNXIANYAN_WEI_JIESHU = new Object[] { 0, 6869 };
	/**角色排名不存在*/
	public static final Object[] KUAFU_QUNXIANYAN_PAIMING_NO = new Object[] { 0, 6870 };
	/**已领取过奖励*/
	public static final Object[] KUAFU_QUNXIANYAN_LINGQU = new Object[] { 0, 6871 };
	/**邮件奖励*/
	public static final String KUAFU_EMAIL_JIANGLI = "6872";
	/**邮件标题*/
	public static final String KUAFU_EMAIL_JIANGLI_TITLE = "6873";

	// --------------------------五行魔神--------------------------------------
	/**该五行已被激活过*/
	public static final Object[] WUXING_YI_JIHUO = new Object[] { 0, 6910 };
	/**该五行还未被激活，不能进行升阶*/
	public static final Object[] WUXING_WEI_JIHUO = new Object[] { 0, 6911 };
	/**该五行还未被激活，不能进行附体*/
	public static final Object[] WUXING_WEI_JIHUO_FUTI = new Object[] { 0, 6912 };
	/**道具不足*/
	public static final Object[] WUXING_NO_ITEM = new Object[] { 0, 6913 };
	/**五行已达到最大等级*/
	public static final Object[] WUXING_MAX_LEVEL = new Object[] { 0, 6914 };
	/** 五行进阶全服公告 */
	public static final int WUXING_SJ_NOTICE_1 = 6915;
	public static final int WUXING_SJ_NOTICE_2 = 6916;
	public static final int WUXING_SJ_NOTICE_3 = 6917;
	public static final int WUXING_SJ_NOTICE_4 = 6918;
	public static final int WUXING_SJ_NOTICE_5 = 6919;

	// -------------------------五行副本----------------------------------------
	/** 五行副本难度错误 **/
	public static final Object[] WUXING_FUBEN_LEVEL = new Object[] { 0, 18371 };
	/** 五行副本尚未完成 */
	public static final Object[] WUXING_FUBEN_NOT_FINISH = new Object[] { 0, 18372 };
	/** 五行副本挑战副本数据错误 */
	public static final Object[] WUXING_FUBEN_ID_ERROR = new Object[] { 0, 18373 };
	/** 五行副本今日已无可购买次数 */
	public static final Object[] WUXING_FUBEN_TODAY_NO_BUY_COUNT = new Object[] { 0, 18374 };

	// --------------------------五行魔神技能--------------------------------------
	/** 技能不能学习 **/
	public static final Object[] WX_SKILL_NO_LEARN = new Object[] { 0, 7012 };
	/** 技能学习等级已满 **/
	public static final Object[] WX_SKILL_MAX_LEVEL = new Object[] { 0, 7013 };
	/** 技能学习魔神阶级不够 **/
	public static final Object[] WX_SKILL_LIMIT_LEVEL = new Object[] { 0, 7014 };
	/** 技能学习道具不够 **/
	public static final Object[] WX_SKILL_NOT_ENOUGH = new Object[] { 0, 7015 };

	// -------------------------五行技能副本----------------------------------------

	/** 五行技能副本层次错误 **/
	public static final Object[] WX_FUBEN_SKILL_LAYER = new Object[] { 0, 18683 };
	/** 五行技能副本难度错误 **/
	public static final Object[] WX_FUBEN_SKILL_LEVEL = new Object[] { 0, 18684 };
	/** 五行技能副本奖励类型错误 **/
	public static final Object[] WX_FUBEN_SKILL_REWARD_TYPE_ERROR = new Object[] { 0, 18685 };
	/** 五行技能副本奖励领取错误 **/
	public static final Object[] WX_FUBEN_SKILL_RECEIVE_ERROR = new Object[] { 0, 18686 };
	/** 五行技能副本副本扫荡错误 **/
	public static final Object[] WX_FUBEN_SKILL_CLEAR_FAIL = new Object[] { 0, 18687 };

	// -------------------------五行魔神精魄----------------------------------------
	/** 魂器开启新格位错误 **/
	public static final Object[] WX_JP_OPEN_SLOT_ERROR = new Object[] { 0, 7040 };
	/** 精魄不存在 **/
	public static final Object[] WX_JP_IS_NULL = new Object[] { 0, 7041 };
	/** 精魄吞噬参数错误 **/
	public static final Object[] WX_JP_MERGE_GUID_ERROR = new Object[] { 0, 7042 };
	/** 精魄等级已满 **/
	public static final Object[] WX_JP_MERGE_LEVEL_MAX = new Object[] { 0, 7043 };
	/** 精魄升级经验溢出 **/
	public static final Object[] WX_JP_MERGE_EXP_OVER = new Object[] { 0, 7044 };
	/** 背包格位不足 **/
	public static final Object[] WX_JP_BAG_SLOT_OVER = new Object[] { 0, 7039 };
	/** 精魄镶嵌位置错误 **/
	public static final Object[] WX_JP_POSITION_ERROR = new Object[] { 0, 7045 };
	/** 经验类型精魄 **/
	public static final Object[] WX_JP_EXP_NOT_PUT = new Object[] { 0, 7057 };
	/** 魔神已镶嵌此类型精魄 **/
	public static final Object[] WX_JP_HAS_ATTYTYPE = new Object[] { 0, 7056 };
	/** 孔位未开启 **/
	public static final Object[] WX_JP_SLOT_NOT_OPEN = new Object[] { 0, 7046 };
	/** 孔位上已镶嵌精魄 **/
	public static final Object[] WX_JP_HAS_PUT_ON = new Object[] { 0, 7047 };
	/** 背包精魄不能卸下 **/
	public static final Object[] WX_JP_BAG_NO_TAKE_OFF = new Object[] { 0, 7048 };
	/** 孔位已开启 **/
	public static final Object[] WX_JP_SLOT_HAS_OPEN = new Object[] { 0, 7049 };
	/** 存入背包失败 **/
	public static final Object[] WX_JP_INSERT_BAG_ERROR = new Object[] { 0, 7050 };
	/** 兑换精华不足 **/
	public static final Object[] WX_JP_JING_HUA_NOT_ENOUGH = new Object[] { 0, 7025 };

	// -------------------------------跨服巅峰之战--------------------------------------
	/** 恭喜您获得大乱斗比赛小组第{0}名，并且获得巅峰之战的比赛资格，请关注巅峰之战比赛时间，准时上线参赛赢得丰厚奖励 **/
	public static final String KUAFU_DIANFENG_NOTICE_EMAIL = "18717";
	/** 巅峰之战**/
	public static final String KUAFU_DIANFENG_NOTICE_EMAIL_TITLE = "18727";
	/** 您在巅峰之战中的表现非常亮眼，根据您的晋级程度，获得以下奖励 **/
	public static final String KUAFU_DIANFENG_REWARD_EMAIL = "18718";
	/** 巅峰之战奖励**/
	public static final String KUAFU_DIANFENG_REWARD_EMAIL_TITLE = "18728";
	/** 巅峰之战已经结束,冠军通告 **/
	public static final String KUAFU_DIANFENG_OVER_NOTICE = "5150";

	/** 未到活动时间 **/
	public static final Object[] KF_DIANFENG_NOT_ENTER = new Object[] { 0, 18719 };
	/** 已经在房间副本 **/
	public static final Object[] KF_DIANFENG_HAS_ENTER = new Object[] { 0, 18720 };
	/** 房间不存在 **/
	public static final Object[] KF_DIANFENG_NOT_ROOM = new Object[] { 0, 18721 };
	/** 进入小组房间失败 **/
	public static final Object[] KF_DIANFENG_ENTER_ROOM_FAIL = new Object[] { 0, 18722 };

	// ----------------------------热发布活动:充值轮盘----------------------------------
	/** 次数不够  **/
	public static final Object[] RFB_LUNPAN_NOT_COUNT = new Object[] { 0, 14102 };
	// ----------------------------热发布活动:首冲返利----------------------------------
	/** 不能领取首冲返利奖励 **/
	public static final Object[] RFB_FIRST_CHARGE_REBATE_NOT_REWARD = new Object[] { 0, 16214 };

	// --------------------------糖宝五行魔神--------------------------------------
	/**该糖宝五行已被激活过*/
	public static final Object[] TB_WUXING_YI_JIHUO = new Object[] { 0, 6910 };
	/**该糖宝五行还未被激活，不能进行升阶*/
	public static final Object[] TB_WUXING_WEI_JIHUO = new Object[] { 0, 6911 };
	/**道具不足*/
	public static final Object[] TB_WUXING_NO_ITEM = new Object[] { 0, 6913 };
	/**糖宝五行已达到最大等级*/
	public static final Object[] TB_WUXING_MAX_LEVEL = new Object[] { 0, 6914 };
	/** 糖宝五行进阶全服公告 */
	public static final int TB_WUXING_SJ_NOTICE_1 = 7065;
	public static final int TB_WUXING_SJ_NOTICE_2 = 7066;
	public static final int TB_WUXING_SJ_NOTICE_3 = 7067;
	public static final int TB_WUXING_SJ_NOTICE_4 = 7068;
	public static final int TB_WUXING_SJ_NOTICE_5 = 7069;

	// -----------------------------心魔ErrorCode----------------------------------------
	/** 心魔升级失败,不能升级 **/
	public static final Object[] XINMO_CANNOT_SHENGJI = new Object[] { 0, 18970 };
	/** 心魔突破失败,不能突破 **/
	public static final Object[] XINMO_CANNOT_TUPO = new Object[] { 0, 18971 };
	/** 心魔凝神失败,不能凝神 **/
	public static final Object[] XINMO_CANNOT_NINGSHEN = new Object[] { 0, 18972 };
	/** 心魔升级失败,等级已满 **/
	public static final Object[] XINMO_HAS_MAX_LEVEL = new Object[] { 0, 18973 };
	/** 心魔突破失败,等级未满 **/
	public static final Object[] XINMO_LEVEL_ENOUGH = new Object[] { 0, 18974 };
	/** 心魔凝神失败,已达最高等级 **/
	public static final Object[] XINMO_MAX_NINGSHEN = new Object[] { 0, 18975 };
	/** 心魔操作失败,道具不足 **/
	public static final Object[] XINMO_ITEM_ENOUGH = new Object[] { 0, 1514 };
	/** 心魔操作失败,道具错误 */
	public static final Object[] XINMO_ITEM_ERROR = new Object[] { 0, 1515 };

	// -----------------------------心魔天炉炼丹ErrorCode----------------------------------------
	/** 心魔天炉炼丹升级失败,等级已满 **/
	public static final Object[] XM_LIANDAN_LEVEL_MAX = new Object[] { 0, 19011 };
	/** 心魔天炉炼丹提取丹药:丹药不存在 **/
	public static final Object[] XM_LIANDAN_NOT_DANYAO = new Object[] { 0, 19016 };
	/** 心魔天炉炼丹提取丹药:没有丹药可提取 **/
	public static final Object[] XM_LIANDAN_NULL_DANYAO = new Object[] { 0, 19007 };
	/** 心魔天炉炼丹开启丹药,道具错误 */
	public static final Object[] XM_LIANDAN_ITEM_ERROR = new Object[] { 0, 1515 };

	// -----------------------------心魔魔神ErrorCode----------------------------------------
	/** 心魔已经激活 **/
	public static final Object[] XM_MOSHEN_HAS_ACTITY = new Object[] { 0, 19061 };
	/** 道具不足 **/
	public static final Object[] XM_MOSHEN_NO_ITEM = new Object[] { 0, 6913 };
	/** 心魔尚未激活 **/
	public static final Object[] XM_MOSHEN_NOT_ACTITY = new Object[] { 0, 19062 };
	/** 心魔已达顶阶 **/
	public static final Object[] XM_MOSHEN_MAX_RANK = new Object[] { 0, 19063 };
	/** 元神境界不够,无法升级心魔 **/
	public static final Object[] XM_MOSHEN_NO_NISHEN_RANK = new Object[] { 0, 19064 };
	/** 心魔噬体正在CD时间 **/
	public static final Object[] XM_MOSHEN_SHITI_CD = new Object[] { 0, 19065 };

	// ------------------------------心魔-挑战魔神副本----------------------------------------
	/** 腐化度已满 **/
	public static final Object[] XM_FUBEN_MAX_FUHUA = new Object[] { 0, 19159 };

	// --------------------------心魔-魔神技能--------------------------------------
	/** 心魔技能不能学习 **/
	public static final Object[] XM_SKILL_NO_LEARN = new Object[] { 0, 19172 };
	/** 心魔技能学习等级已满 **/
	public static final Object[] XM_SKILL_MAX_LEVEL = new Object[] { 0, 19173 };
	/** 心魔技能学习心魔阶级不够 **/
	public static final Object[] XM_SKILL_LIMIT_LEVEL = new Object[] { 0, 19174 };
	/** 心魔技能学习道具不够 **/
	public static final Object[] XM_SKILL_NOT_ENOUGH = new Object[] { 0, 915 };

	// ------------------------------心魔-深渊副本----------------------------------------
	/** 副本难度已满 **/
	public static final Object[] XM_SHENYUAN_FUBEN_FIGHT_OVER = new Object[] { 0, 19209 };
	/** 副本难度错误 **/
	public static final Object[] XM_SHENYUAN_LEVEL_ERROR = new Object[] { 0, 18684 };
	/** 副本正在冷却中**/
	public static final Object[] XM_SHENYUAN_FUBEN_COLING_RUN = new Object[] { 0, 19210 };
	/** 副本不在冷却中**/
	public static final Object[] XM_SHENYUAN_FUBEN_COLING_OVER = new Object[] { 0, 19211 };

	// ------------------------------心魔-洗练----------------------------------------
	/** 洗练保留的属性尚未拥有**/
	public static final Object[] XM_XILIAN_BASE_ATTR_NOT = new Object[] { 0, 19313 };
	/** 洗练保留的属性个数超过上限**/
	public static final Object[] XM_XILIAN_BASE_ATTR_UPPER_LIMIT = new Object[] { 0, 19314 };
	/** 道具不足 **/
	public static final Object[] XM_XILIAN_NO_ITEM = new Object[] { 0, 6913 };
	/** 没有备份属性可以替换 **/
	public static final Object[] XM_XILIAN_NOT_BACK_ATTR = new Object[] { 0, 19315 };

	// ------------------------------画卷2----------------------------------------------
	/** 画卷已装备身上 **/
	public static final Object[] HUAJUAN2_UP_READY = new Object[] { 0, 6298 };
	/** 画卷已不在身上 **/
	public static final Object[] HUAJUAN2_DOWN_READY = new Object[] { 0, 6299 };
	/** 画卷已不能升星 **/
	public static final Object[] HUAJUAN2_NOT_UPGRADE = new Object[] { 0, 6300 };

	// -----------------------------跨服云宫之巅----------------------------------------
	/** 活动尚未开始或已结束 **/
	public static final Object[] KF_YUNGONG_NOT_START_OR_OVER = new Object[] { 0, 19614 };
	/** 活动期间不能领取奖励 **/
	public static final Object[] KF_YUNGONG_RUNNING = new Object[] { 0, 19615 };
	/** 没有加入门派 **/
	public static final Object[] KF_YUNGONG_NOT_GUILD = new Object[] { 0, 19616 };
	/** 不是占领云宫门派 **/
	public static final Object[] KF_YUNGONG_NOT_OWN = new Object[] { 0, 19617 };
	/** 参赛等级不够 **/
	public static final Object[] KF_YUNGONG_NOT_LEVEL = new Object[] { 0, 19618 };
	/** 参赛战力不够 **/
	public static final Object[] KF_YUNGONG_NOT_FIGHT = new Object[] { 0, 19619 };
	/** 没有可领取奖励 **/
	public static final Object[] KF_YUNGONG_NOT_REWARD = new Object[] { 0, 19620 };
	/** 奖励已领取 **/
	public static final Object[] KF_YUNGONG_HAS_REWARD = new Object[] { 0, 19621 };
	/** 采集旗子错误 **/
	public static final Object[] KF_YUNGONG_COLLECT_ERROR = new Object[] { 0, 19622 };
	/** 战旗不存在 **/
	public static final Object[] KF_YUNGONG_NOT_QIZI = new Object[] { 0, 19623 };
	/** 战旗资源错误 **/
	public static final Object[] KF_YUNGONG_QIZI_ERROR = new Object[] { 0, 19624 };
	/** 战旗已被抢走 **/
	public static final Object[] KF_YUNGONG_QIZI_CHANGE = new Object[] { 0, 19625 };

	// --------------------------------魔宫猎焰---------------------------------------------------
	/** 御魔值不足**/
	public static final Object[] MGLY_YUMO_NOT_ENOUGH = new Object[] { 0, 19717 };
	/** 魔焰精华不足**/
	public static final Object[] MGLY_JINGHUA_NOT_ENOUGH = new Object[] { 0, 19718 };

	// -------------------------------仙器觉醒------------------------------------------
	/** 仙器觉醒类型错误 **/
	public static final Object[] XQJX_TYPE_ERROR = new Object[] { 0, 19815 };
	/** 仙洞等级已达上限 **/
	public static final Object[] XQJX_UPLVL_LEVEL_LIMIT = new Object[] { 0, 19816 };
	/** 仙器觉醒等级已达上限 **/
	public static final Object[] XQJX_JUEXING_LEVEL_LIMIT = new Object[] { 0, 19817 };
	/** 仙器觉醒仙洞等级不够 **/
	public static final Object[] XQJX_JUEXING_LEVEL_NOT_ENOUGN = new Object[] { 0, 19818 };

	// -------------------------------仙器副本(云瑶晶脉)------------------------------------------
	/** 云瑶晶脉副本尚未开启 **/
	public static final Object[] XQFUBEN_NOT_OPEN = new Object[] { 0, 19830 };
	/** 云瑶晶脉副本次数已用完 **/
	public static final Object[] XQFUBEN_COUNT_LIMIT = new Object[] { 0, 19831 };
	/** 云瑶晶脉副本玩家等级不够 **/
	public static final Object[] XQFUBEN_LEVEL_NOT_ENOUGH = new Object[] { 0, 19832 };

	// --------------------------------------仙缘飞化------------------------------------------------
	/**仙缘飞化未开启 */
	public static final Object[] XYFH_NOT_OPEN = new Object[] { 0, 19845 };
	/**仙缘飞化等级已达上限*/
	public static final Object[] XYFH_LEVEL_MAX = new Object[] { 0, 19846 };
	/**仙缘飞化目标升级等级错误*/
	public static final Object[] XYFH_TARGET_LEVEL_ERROR = new Object[] { 0, 19847 };
	/**仙缘飞化进阶全服公告*/
	public static final int XYFH_SJ_NOTICE = 19848;
	/**您有尚未达成的飞化条件，无法飞化*/
	public static final Object[] XYFH_CANNOT_SJ = new Object[] { 0, 19849 };

	// --------------------------------------至尊充值------------------------------------------------
	/**领取公告*/
	public static final int EXTREME_RECHARGE_REVEIVE = 19905;
	/**充值额度达到领取至尊时装 由于背包已满 奖励通过邮件发放*/
	public static final String EXTREME_RECHARGE_EMAIL_REVEIVE = "19906";
	/**绝版至尊活动奖励*/
	public static final String EXTREME_RECHARGE_EMAIL_REVEIVE_TITLE = "19907";
	
	//------------------------------个人boss-----------------------------
	public static final Object[] PERSONAL_BOSS_MAP_NOT_EXIST = new Object[] {0,19950};
	public static final Object[] PERSONAL_BOSS_IN_JUQING_MAP = new Object[] {0,19951};
	public static final Object[] PERSONAL_BOSS_IN_FUBEN = new Object[] {0,19952};
	public static final Object[] PERSONAL_BOSS_LEVEL_NO_MATCH = new Object[] {0,19953};
	public static final Object[] PERSONAL_BOSS_COUNT_OVER_LIMIT = new Object[] {0,19954};
	public static final String PERSONAL_BOSS_MAIL = "19956";
	/**boss积分不足*/
	public static final Object[] BOSS_JIFEN_NOT_ENOUGH = new Object[] {0,19958};
	/**该星位已激活*/
	public static final Object[] BOSS_JIFEN_ALREADY_ACTIVATED = new Object[] {0,19959};
	//---------------------------------魔宫寻宝--------------
	/**邮件标题*/
	public static final String MOGONG_XUNBAO_EMAIL_TITLE = "13520";
	// ---------------------------------新圣剑-------------------------------------------
	/** 圣剑未开启 */
	public static final Object[] WQ_NO_OPEN = new Object[] { 0, 20062 };
	/** 圣剑等级已经最高了 */
	public static final Object[] WQ_LEVEL_MAX = new Object[] { 0, 20063 };
	/** 圣剑目标升级等级错误 */
	public static final Object[] WQ_TARGET_LEVEL_ERROR = new Object[] { 0, 20064 };
	/** 圣剑配置异常 */
	public static final Object[] WQ_CONFIG_ERROR = new Object[] { 0, 20065 };
	/** 圣剑升阶所需物品不足 */
	public static final Object[] WQ_GOODS_BZ = new Object[] { 0, 20066 };
	/** 圣剑外显没有获得 */
	public static final Object[] WQ_NO_SHOW = new Object[] { 0, 20067 };
	/** 圣剑开启等级不足 */
	public static final Object[] WQ_LEVEL_NO = new Object[] { 0, 20061 };
	/** 圣剑等级不足,不能使用 */
	public static final Object[] WQ_LEVEL_LIMIT_CAN_NOT_USE = new Object[] { 0, 20068 };
	/** 潜能丹使用数量达上限 */
	public static final Object[] WQ_QND_MAX_NUM = new Object[] { 0, 20069 };
	/** 成长丹使用数量达上限 */
	public static final Object[] WQ_CZD_MAX_NUM = new Object[] { 0, 20070 };
	/** 圣剑进阶全服公告 */   
	public static final int WQ_SJ_NOTICE = 20080;
	
	
	//---------------------------------修炼之路------------------------------------
	/**不在活动时间内 */
	public static final Object[] XIULIAN_NOT_TIME = new Object[]{0,20300};
	/**当前奖池等级不匹配*/
	public static final Object[] XIULIAN_JC_LEVEL_NO = new Object[]{0,20301};
	/**已兑换过此奖励*/
	public static final Object[] XIULIAN_YI_DUIHUAN = new Object[]{0,20302};
	/**请先兑换完积分道具*/
	public static final Object[] XIULIAN_NOT_JIFEN_DUIHUAN = new Object[]{0,20303};
	/**积分不够，不可兑换*/
	public static final Object[] XIULIAN_NOT_JIFEN = new Object[]{0,20304};
	/**已领取过今日任务奖励*/
	public static final Object[] XIULIAN_YI_LINGQU_DAY = new Object[]{0,20305};
	/**请先完成今日任务*/
	public static final Object[] XIULIAN_XIAN_TASK = new Object[]{0,20306};
	/**你已兑换过该奖励*/
	public static final Object[] XIULIAN_YI_CHENGHAO= new Object[]{0,20307};
	/**时间未到，不可兑换*/
	public static final Object[] XIULIAN_NO_DUIHUAN_TIME= new Object[]{0,20308};
	/**该奖励已领取过*/
	public static final Object[] XIULIAN_YI_LINGQU_JIFEN= new Object[]{0,20309};
	/**未达到领取条件*/
	public static final Object[] XIULIAN_WEI_TIAOJIAN= new Object[]{0,20310};
	
	// ---------------------------------限时礼包---------------------------------
	/** 已购买 */
	public static final Object[] LIMIT_BUY_YGM = new Object[] { 0, 20694 };
	/** 改礼包已过期 */
	public static final Object[] LIMIT_BUY_GQ = new Object[] { 0, 20695 };
	/** 充值金额不足 */
	public static final Object[] LIMIT_BUY_CZBZ = new Object[] { 0, 20696 };
	/** 类型不存在 */
	public static final Object[] LIMIT_BUY_TYPE_ERROR =  new Object[] { 0, 20697 };
	
	/** 邮件满，收到新有件时提醒**/
	public static final int EMAIL_FULL = 2517;
	//-------------------------------------星空宝藏------------------------
	public static final Object[] XKBZ_NO_YAOQIU = new Object[]{0,20956};
	public static final Object[] XKBZ_YI_LINGQU = new Object[]{0,20957};
	// ---------------------------------炼金---------------------------------
	/** 次数不足 */
	public static final Object[] COUNT_FINISH = new Object[] { 0, 20415 };

}
