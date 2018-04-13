package com.junyou.log;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;

/**
 * 日志业务打印类
 */
public class LogPrintHandle {

	/**分隔字符**/
	public static final String JOIN_CHAR = "\t";
	/**数据null字符**/
	public static final String LINE_CHAR = "-";
	
	//------------------角色上下线-------------------------
	public static final int ROLE_IN_OR_OUT = 1;
	
	public static final int ROLE_IO_ACCOUNT = 1;//账号登陆
	public static final int ROLE_IO_ACCOUNT_DLQ = 2;//账号微端登陆
	public static final int ROLE_IO_IN = 3;//角色登陆
	public static final int ROLE_IO_OUT = 4;//角色登出	
	
	//------------------创建角色-------------------------
	public static final int CREATE_ROLE = 2;
	
	public static final int CREATE_ACCOUNT = 1;//账号创建
	public static final int CREATE_USER = 2;//角色创建
	
	//------------------场景内怪物掉落-------------------------
	public static final int STAGE_MONSTER_DROP = 3;
	
	//------------------放入背包物品记录-------------------------
	public static final int CONTAINER_GET_GOODS= 4; 
	
	//------------------消耗背包物品记录---------------------
	public static final int CONTAINER_REMOVE_GOODS= 5;
	
	//-----------------主线任务日志----------------------------
	public static final int ZHUXIAN_TASK = 6;
	
	//-----------------聊天-----------------------------------------------------------
	public static final int CHAT_LOG = 7;
	
	public static final int CHAT_PRIVATE = 1;//私聊
	public static final int CHAT_WORLD = 2;//世界
	public static final int CHAT_GUILD = 3;//公会
	public static final int CHAT_LABA = 4;//喇叭
	public static final int CHAT_TEAM = 5;//队伍
	
	//-----------------消耗数值（元宝，金币，礼券）日志----------------------------
	public static final int CONSUME_NUM_LOG = 8; 
	
	public static final int CONSUME_BAG_OPEN_SLOT = 1; 
	public static final int CONSUME_STORAGE_OPEN_SLOT = 2;
	public static final int CONSUME_TRADE = 3;//交易
	public static final int CONSUME_EQUIP_QH = 4;
	public static final int CONSUME_LEARN_SKILL = 5;//学习技能
	public static final int CONSUME_TASKDAY_FINISH = 6;//元宝完成日常任务
	public static final int CONSUME_JINGJI = 7;//竞技场
	public static final int CONSUME_ZUOQI_SJ = 8;//坐骑升阶
	public static final int CONSUME_PAIMAI_GM = 9;//拍卖购买
	public static final int CONSUME_GUILD = 10;//门派
	public static final int CONSUME_OFFLINE_EXP = 11;//离线经验
	public static final int CONSUME_RESOURCE_BACK = 12;//资源找回
	public static final int CONSUME_OPEN_GIFT = 13;//礼包使用
	public static final int CONSUME_MALL = 14;//商城消耗
	public static final int CONSUME_LQDXBX = 15;//领取多选礼箱奖励 
	public static final int CONSUME_FUBEN = 16;//副本
	public static final int CONSUME_YABIAO = 17;//押镖
	public static final int CONSUME_CHIBANG_SJ = 18;//翅膀升阶
	public static final int CONSUME_XUN_BAO = 19;//寻宝消耗
	public static final int CONSUME_ZHUANGBEI_SJ = 20;//装备升阶
	public static final int CONSUME_RFB_XIUXIAN = 21;//热发布修仙
	public static final int CONSUME_MAXLEVEL_BIAOCHE = 22;//刷新镖车到最高级别
	public static final int CONSUME_REFRESH_BIAOCHE = 23;//刷新镖车
	public static final int CONSUME_PATA = 24;//爬塔
	public static final int CONSUME_SMSD_SX= 25;//神秘商店刷新
	public static final int CONSUME_SMSD_GM= 26;//神秘商店购买
	public static final int CONSUME_XIANJIAN_SJ = 27;//仙剑升阶
	public static final int CONSUME_TOUZI = 28;//投资计划
	public static final int CONSUME_ZHUANPAN = 29;//命运转盘
	public static final int CONSUME_ZHANJIA_SJ = 30;//战甲升阶
	public static final int CONSUME_BUY_SHENQI = 31;//开服购买神器
	public static final int CONSUME_GOLD_JU_LIN = 32;//元宝聚灵
	public static final int CONSUME_MONEY_JU_LIN = 33;//银两聚灵
	public static final int CONSUME_DAOMO = 34;//盗墓手札
	public static final int CONSUME_TIPIN = 35;//装备提品
	public static final int CONSUME_YAOSHEN_SJ = 36;//妖神升阶
	public static final int CONSUME_YAOSHEN_ACTIVATE = 37;//妖神激活
	public static final int CONSUME_JEWEL = 38;//宝石
	public static final int CONSUME_SUOYAOTA = 39;//锁妖塔消耗
	public static final int CONSUME_SHIZHUANG = 40;//时装消耗
	public static final int CONSUME_CHONGWU_ACTIVATE = 41;//宠物激活
	public static final int CONSUME_CHONGWU_UPGRADE = 42;//宠物进阶
	public static final int CONSUME_MARRY = 43;//结婚系统
	public static final int CONSUME_CUILIAN = 44;//淬炼
	public static final int CONSUME_SUPER_DUIHUAN = 45;//超值兑换
	public static final int CONSUME_FUSHU_SKILL = 46;//附属技能
	public static final int CONSUME_XILIAN = 47;//神器洗练
	public static final int CONSUME_HONGBAO = 48;//红包
	public static final int CONSUME_LABA = 49;//幸运拉霸
	public static final int CONSUME_CAIDAN = 50;//彩蛋
	public static final int CONSUME_YAOSHEN_MOWEN_SJ = 51;//妖神魔纹升级
	public static final int CONSUME_QILING_SJ = 52;//器灵升阶
	public static final int CONSUME_YAOSHEN_HUNPO_SJ = 53;//妖神魂魄升级
	public static final int CONSUME_YAOSHEN_MOYIN_SJ = 54;//妖神魔印升级
	public static final int CONSUME_TAFANG = 55;//塔防
	public static final int CONSUME_KUAFU_JINGJI = 56;//封神之战
	public static final int CONSUME_REFABU_XUNBAO = 57;//热发布寻宝
	public static final int CONSUME_REFABU_TANBAO = 58;//探索宝藏
	public static final int CONSUME_TANGBAO_XINWEN_SJ = 59;//糖宝心纹
	public static final int CONSUME_CUXIAO_GET_REWARD = 60;//领取促销奖励
	public static final int CONSUME_EQUIP_ZHUSHEN = 61;
	public static final int CONSUME_FLOWER_RANK_BUG = 62;//鲜花榜购买
	public static final int CONSUME_TENCENT_SHANGDIAN = 63;//腾讯折扣商店
	public static final int CONSUME_FUMO_SJ = 64;//妖神附魔升级
	public static final int CONSUME_RFBTG = 65;//热发布团购
	public static final int CONSUME_HUAJUAN_HECHENG = 66;//热发布团购
	public static final int CONSUME_TIANYU_SJ = 67;//天羽升阶
	public static final int CONSUME_MIAOSHA = 68;//秒杀活动
	public static final int CONSUME_ZHUANGBEI_SJ_SW = 69;//神武装备升阶
	public static final int CONSUME_HUIYANSHIJIN = 70;//慧眼识金消耗
	public static final int CONSUME_ZHUANGBEI_XZ_SW = 71;//神武装备星铸
	public static final int CONSUME_WUXING_JIHUO = 72;//五行激活
	public static final int CONSUME_WUXING_SHENGJI = 73;//五行升级
    public static final int CONSUME_WUXING_FUBEN = 74;// 五行副本
    public static final int CONSUME_WUXING_SKILL_UPLEVEL = 75;// 五行技能升级
    public static final int CONSUME_WUXING_OPEN_SLOT_BAG = 76;// 五行魔神背包开孔
    public static final int CONSUME_WUXING_OPEN_SLOT_BODY = 77;// 五行魔神身上开孔
    public static final int CONSUME_WUXING_GET_JP = 78;// 五行魔神猎命获取精魄
    public static final int CONSUME_WUXING_KTTJ = 79;// 五行魔神窥探天机
    public static final int CONSUME_TB_WUXING_JIHUO = 80;//糖宝五行激活
    public static final int CONSUME_TB_WUXING_SHENGJI = 81;//糖宝五行升级
    public static final int CONSUME_TB_WUXING_SKILL_UPLEVEL = 82;// 糖宝五行技能升级
    public static final int CONSUME_XINMO_SHENGJI = 83;// 心魔升级
    public static final int CONSUME_XINMO_TUPO = 84;// 心魔突破
    public static final int CONSUME_XINMO_NINGSHEN = 85;// 心魔凝神
    public static final int CONSUME_XM_LIANDAN_SHENGJI = 86;// 心魔天炉炼丹升级
    public static final int CONSUME_XM_MOSHEN_JIHUO = 87;// 心魔魔神激活
    public static final int CONSUME_XM_MOSHEN_SHENGJI= 88;// 心魔魔神升级
    public static final int CONSUME_REFABU_JUEBAN= 89;// 购买热发布活动(绝版礼包)道具
    public static final int CONSUME_XINMO_SKILL_UPLEVEL = 90;// 心魔魔神技能升级
    public static final int CONSUME_XM_SHENYUAN_CLEAR_CD = 91;// 清除心魔深渊副本冷却cd
    public static final int CONSUME_XINMO_XILIAN = 92;// 心魔洗练
    public static final int CONSUME_XINMO_DOUCHANG_FUBEN = 93;// 购买心魔斗场副本挑战次数
    public static final int CONSUME_CHONGWU_SKILL_SHENGJI= 94;// 宠物技能升级
    public static final int CONSUME_CHONGWU_EQUIP_QIANGHUA= 95;// 宠物附属装备强化
    public static final int CONSUME_CHONGWU_EQUIP_SHENGJIE= 96;// 宠物附属装备升阶
    public static final int CONSUME_CHONGWU_EQUIP_TIPIN= 97;// 宠物附属装备提品
    public static final int CONSUME_XIANYUAN_FEIHUA_SJ= 98;// 仙缘飞化升阶
    public static final int CONSUME_SHIZHUANG_JINJIE = 99;//时装进阶消耗
    public static final int CONSUME_SHIZHUANG_SJ = 100;//时装升阶
    public static final int CONSUME_SHENQI_SJ = 101;//神器升阶
    public static final int CONSUME_XIANGWEI_SJ = 102;//套装象位升星
    public static final int CONSUME_WUQI_SJ = 103;//新圣剑升阶
    public static final int CONSUME_LIMITLB = 104;//限时礼包
    
	/**备注**/
	public static final int CBZ_TRADE = 1;//交易
	public static final int CBZ_EQUIP_QH = 2;
	public static final int CBZ_BAG_OPEN_SLOT = 3; 
	public static final int CBZ_STORAGE_OPEN_SLOT = 4; 
	public static final int CBZ_LEARN_ZD_SKILL = 5;//学习主动技能
	public static final int CBZ_TASKDAY_FINISH = 6;//元宝完成日常任务
	public static final int CBZ_JINGJI_BUY_COUNT = 7;//竞技场买次数
	public static final int CBZ_JINGJI_MIAO_CD = 8;//竞技场秒CD
	public static final int CBZ_JINGJI_GUWU = 9;//竞技场鼓舞
	public static final int CBZ_LEVEL_UP_ZD_SKILL = 10;//升级主动技能
	public static final int CBZ_LEARN_BD_SKILL = 11;//学习被动技能
	public static final int CBZ_LEVEL_UP_BD_SKILL = 12;//升级被动技能 
	public static final int CBZ_ZUOQI_SJ = 13;//坐骑升阶
	public static final int CBZ_PAIMAI_GM = 14;//拍卖购买
	public static final int CBZ_GUILD_JUANXIAN = 15;//门派捐献
	public static final int CBZ_OFFLINE_EXP = 16;//离线经验
	public static final int CBZ_RESOURCE_BACK = 17;//资源找回
	public static final int CBZ_OPEN_GIFT = 18;//礼包使用
	public static final int CBZ_MALL = 19;//商城消耗
	public static final int CBZ_LQDXBX = 20;//领取多选礼箱奖励
	public static final int CBZ_FUBEN_SHOUHU = 21;//守护副本重置
	public static final int CBZ_FUBEN_SHOUHU_SD = 22;//守护副本扫荡
	public static final int CBZ_CHIBANG_SJ = 23;//翅膀升阶
	public static final int CBZ_XUN_BAO = 24;//寻宝消耗
	public static final int CBZ_ZHUANGBEI_SJ = 25;//装备升阶
	public static final int CBZ_RFB_XIUXIAN  = 26;//热发布修仙
	public static final int CBZ_MAXLEVEL_BIAOCHE = 27;//刷新镖车到最高级别
	public static final int CBZ_REFRESH_BIAOCHE = 28;//刷新镖车
	public static final int CBZ_GUILDTASKDAY_FINISH = 29;//元宝完成门派日常任务
	public static final int CBZ_BUY_PATA_COUNT = 30;//购买爬塔次数
	public static final int CBZ_YABIAO = 31;//押镖
	public static final int CBZ_SMSD_SX = 32;//神秘商店刷新
	public static final int CBZ_SMSD_GM = 33;//神秘商店购买
	public static final int CBZ_XIANJIAN_SJ = 34;//仙剑升阶
	public static final int CBZ_TOUZI = 35;//投资计划
	public static final int CBZ_ZHUANPAN = 36;//命运转盘
	public static final int CBZ_ZHANJIA_SJ = 37;//战甲升阶
	public static final int CBZ_BUY_SHENQI = 38;//开服购买神器
	public static final int CBZ_GOLD_JU_LIN = 39;//元宝聚灵 
	public static final int CBZ_MONEY_JU_LIN = 40;//银两聚灵 
	public static final int CBZ_DAOMO = 41;//盗墓手札
	public static final int CBZ_TIPIN = 42;//装备提品
	public static final int CBZ_YAOSHEN_SJ = 43;//妖神升阶
	public static final int CBZ_YAOSHEN_ACTIVATE = 44;//妖神激活
	public static final int CBZ_JEWEL_UP = 45;//宝石打孔消耗
	public static final int CBZ_JEWEL_HECHENG = 46;//宝石合成
	public static final int CBZ_SUOYAOTA = 47;//锁妖塔抽奖
	public static final int CBZ_ACTIVE_SHIZHUANG = 48;//激活时装
	public static final int CBZ_SHENGJI_SHIZHUANG = 49;//升级时装
	public static final int CBZ_CHONGWU_ACTIVATE = 50;//宠物激活	
	public static final int CBZ_CHONGWU_UPGRADE = 51;//宠物进阶
	public static final int CBZ_DINGHUN = 52;//订婚
	public static final int CBZ_CANCEL_DINGHUN = 53;//取消订婚
	public static final int CBZ_MARRY = 54;//结婚
	public static final int CBZ_DIVOICE = 55;//离婚
	public static final int CBZ_YUANFEN = 56;//增加缘分
	public static final int CBZ_CUILIAN = 57;//淬炼
	public static final int CBZ_SUPER_DUIHUAN = 58;//超值兑换
	public static final int CBZ_XUFEI_SHIZHUANG = 59;//续费时装
	public static final int CBZ_FUSHU_SKILL_SUODING = 60;//附属技能锁定
	public static final int CBZ_CONSUME_XILIAN = 61;//神器洗练
	public static final int CBZ_CONSUME_HONGBAO = 62;//红包
	public static final int CBZ_CONSUME_LABA = 63;//幸运拉霸
	public static final int CBZ_RESET_CAIDAN = 64;//重置彩蛋
	public static final int CBZ_CONSUME_ZADAN = 65;//砸蛋
	public static final int CBZ_YAOSHEN_MOWEN_SJ = 66;//妖神魔纹升阶
	public static final int CBZ_QILING_SJ = 67;//器灵升阶
	public static final int CBZ_YAOSHEN_HUNPO_SJ = 68;//妖神魂魄升阶
	public static final int CBZ_YAOSHEN_MOYIN_SJ = 69;//妖神魔印升阶
	public static final int CBZ_TAFANG_SJ = 70;//塔防升级
	public static final int CBZ_TAFANG_REVICE_EXP = 71;//塔防领取经验
	public static final int CBZ_KUAFU_JINGJI_CHANGE_TARGETS = 72;//封神之战-换对手
	public static final int CBZ_KUAFU_JINGJI_BUY_COUNT = 73;//封神之战-购买次数
	public static final int CBZ_REFABU_XUNBAO = 74;//热发布寻宝
	public static final int CBZ_KUAFU_JINGJI_MIAO_CD = 75;//封神之战-秒cd
	public static final int CBZ_REFABU_TANBAO = 76;//热发布探索宝藏
	public static final int CBZ_TANGBAO_XINWEN_SJ = 77;//糖宝心纹升阶
	public static final int CBZ_CUXIAO_GET_REWARD = 78;//促销奖励
	public static final int CBZ_EQUIP_ZHUSHEN = 79;
	public static final int CBZ_FLOWER_RANK_BUG = 80;//鲜花榜购买
	public static final int CBZ_TENCENT_SHANGDIAN = 81;//腾讯折扣商店
	public static final int CBZ_FUMO_SJ = 82;//妖神附魔升级
	public static final int CBZ_RFBTG = 83;//热发布团购
	public static final int CBZ_HUAJUAN_HECHENG = 84;//画卷合成
	public static final int CBZ_TIANYU_SJ = 85;//天羽升阶
	public static final int CBZ_CONSUME_MIAOSHA = 86;//秒杀活动
	public static final int CBZ_ZHUANGBEI_SJ_SW = 87;//神武装备升阶
	public static final int CBZ_HUIYANSHIJIN = 88;//慧眼识金消耗
	public static final int CBZ_ZHUANGBEI_XZ_SW = 89;//神武装备星铸
	public static final int CBZ_WUXING_JIHUO = 90;//五行激活
	public static final int CBZ_WUXING_SHENGJI = 91;//五行升级
	public static final int CBZ_BUY_WUXING_COUNT = 92;//购买五行副本次数
	public static final int CBZ_WUXING_SKILL_UPLEVEL = 93;//五行技能升级
	public static final int CBZ_WUXING_OPEN_SLOT_BAG = 94;//五行魔神背包开孔
	public static final int CBZ_WUXING_OPEN_SLOT_BODY = 95;//五行魔神身上开孔
	public static final int CBZ_WUXING_GET_JP = 96;//五行魔神获取精魄
	public static final int CBZ_WUXING_KTTJ = 97;//五行魔神窥探天机
	public static final int CBZ_TB_WUXING_JIHUO = 98;//糖宝五行魔神激活
	public static final int CBZ_TB_WUXING_SHENGJI = 99;//糖宝五行魔神升级
	public static final int CBZ_TB_WUXING_SKILL_UPLEVEL = 100;//糖宝五行技能升级
	public static final int CBZ_LEARN_TB_SKILL = 101;//学习糖宝技能
	public static final int CBZ_LEVEL_UP_TB_SKILL = 102;//升级糖宝被动技能
	public static final int CBZ_XINMO_SHENGJI = 103;//心魔升级
	public static final int CBZ_XINMO_TUPO = 104;//心魔突破
	public static final int CBZ_XINMO_NINGSHEN = 105;//心魔凝神
    public static final int CBZ_XM_LIANDAN_SHENGJI = 106;// 心魔天炉炼丹升级
    public static final int CBZ_XM_MOSHEN_JIHUO = 107;// 心魔魔神激活
    public static final int CBZ_XM_MOSHEN_SHENGJI  = 108;// 心魔魔神升级
    public static final int CBZ_REFABU_JUEBAN  = 109;//  购买热发布活动(绝版礼包)道具
    public static final int CBZ_XINMO_SKILL_UPLEVEL = 110;//心魔魔神技能升级
    public static final int CBZ_XM_SHENYUAN_CLEAR_CD = 111;//清除心魔深渊副本冷却cd
    public static final int CBZ_XINMO_XILIAN = 112;//心魔洗练
    public static final int CBZ_BUY_XINMO_DOUCHANG_COUNT = 113;//购买心魔斗场副本挑战次数
    public static final int CBZ_CHONGWU_SKILL_SHENGJI  = 114;// 宠物技能升级
    public static final int CBZ_CHONGWU_EQUIP_QIANGHUA  = 115;// 宠物附属装备强化
    public static final int CBZ_CHONGWU_EQUIP_SHENGJIE  = 116;// 宠物附属装备升阶
    public static final int CBZ_CHONGWU_EQUIP_TIPIN = 117;// 宠物附属装备提品
    public static final int CBZ_XIANYUAN_FEIHUA_SJ = 118;// 仙缘飞化升阶
    public static final int CBZ_JINJIE_SHIZHUANG = 119;//升级时装
    public static final int CBZ_SHIZHUANG_SJ = 120;//时装升阶
    public static final int CBZ_SHENQI_SJ = 121;//神器升阶
    public static final int CBZ_XIANGWEI_SJ = 122;//套装象位升星
    public static final int CBZ_WUQI_SJ = 123;//新圣剑升阶
    public static final int CBZ_XIANSHILIBAO = 124;//限时礼包
	
	//-----------------获得数值（元宝，金币，礼券）日志----------------------------
	public static final int GET_NUM_LOG = 9;
	
	public static final int GET_NORMAL=0;//不会被用到，但是调方法需要传值
	public static final int GET_TRADE = 1;//交易获得
	public static final int GET_USE_PROP = 2; 
	public static final int GET_GOODS_PICKUP = 3;
	public static final int GET_FUBEN = 4; 
	public static final int GET_EMAIL = 5; //邮件获得
	public static final int GET_TASKDAY_FINISH = 6;//每日任务完成获得的奖励
	public static final int GET_DAZUO = 7;//打坐获得
	public static final int GET_TASK = 8; //主线任务
	public static final int GET_JINGJI = 9; //竞技场
	public static final int GET_SELL_GOODS = 10; //物品出售
	public static final int GET_GIFT_CARD = 11;//礼包卡 
	public static final int GET_OFFLINE_EXP = 12;//离线经验
	public static final int GET_RESOURCE_BACK = 13;//资源找回
	public static final int GET_OPEN_GIFT = 14;//打开礼包卡
	public static final int GET_SEVEN_LOGIN_GIFT = 15;//领取七日登陆奖励
	public static final int GET_DATI_AWARD=17;//直接领取答题奖励
	public static final int GET_MORE_FUBEN = 18;//领取多人副本奖励
	public static final int GET_RFB_SHOUCHONG = 19;//热发布首充奖励
	public static final int GET_RFB_ZHANLIBIPIN = 20;//热发布战力比拼奖励
	public static final int GET_RECHARGE = 21;//充值获得
	public static final int GET_RFB_YUJIANFEIXING = 22;//热发布御剑飞行奖励
	public static final int GET_RFB_CHIBANGPAIMING = 23;//热发布翅膀排名奖励
	public static final int GET_YABIAO_MONEY = 24;//押镖money奖励
	public static final int GET_WEIDUAN_GET = 25;//微端获取奖励
	public static final int GET_PHONE_REWARD = 26;//手机礼包奖励
	public static final int GET_PATA_GIFT = 27;//爬塔副本奖励
	public static final int GET_PT_VIP_REWARD = 28;// 平台vip礼包奖励
	public static final int GET_RFB_QIANGHUA = 29;//热发布强化等级
	public static final int GET_RFB_QIPAN = 30;//热发布棋盘
	public static final int GET_RFB_LEICHONG = 31;//热发布累充
	public static final int GET_RFB_SMSD = 32;//神秘商店
	public static final int GET_TOUZI = 33;//投资计划
	public static final int GET_RFB_ZHUANPAN = 34;//命运转盘
	public static final int GET_RFB_TBXJ = 35;//热发布糖宝仙剑
	public static final int GET_RFB_TBZJ = 36;//热发布糖宝战甲
	public static final int GET_CB_CJ_BGOLD = 37;//翅膀吃道具直接升阶转换绑元
	public static final int GET_RFB_DMSZ = 38;//热发布盗墓手札
	public static final int GET_HUOYUEDU_LB = 39;//活跃度礼包
	public static final int GET_RFB_LIANCHONG = 40;//热发布连充
	public static final int GET_QQ_HZ_XINSHOW = 41;//qq黄钻新手礼包
	public static final int GET_QQ_HZ_DENGJI = 42;//qq黄钻等级礼包
	public static final int GET_QQ_HZ_NIANFEI = 43;//qq黄钻年费礼包
	public static final int GET_QQ_HZ_CHENGZHANG = 44;//qq黄钻成长礼包
	public static final int GET_HEFU_SEVEN_LOGIN_GIFT = 45;//合服七天奖励
	public static final int GET_PLATFORM_GIFT = 46; //平台对接礼包领取
	public static final int GET_QQ_WEIDUAN_GIFT = 47; //腾讯微端礼包领取
	public static final int GET_QQ_TGP_GIFT = 48; //腾讯TGP礼包领取
	public static final int GET_RFB_CZ_FANLI = 49; //热发布充值返利领取
	public static final int GET_QQ_TGP_LOGIN_GIFT = 50; //腾讯TGP登陆礼包领取
	public static final int GET_QQ_TGP_LEVEL_GIFT = 51; //腾讯TGP等级礼包领取
	public static final int GET_CUILIAN = 52;//淬炼
	public static final int GET_SUPER_DUIHUAN = 53;//超值兑换
	public static final int GET_QQ_LZ_XINSHOW = 54;//qq蓝钻新手礼包
	public static final int GET_QQ_LZ_DENGJI = 55;//qq蓝钻等级礼包
	public static final int GET_QQ_LZ_NIANFEI = 56;//qq蓝钻年费礼包
	public static final int GET_QQ_LZ_CHENGZHANG = 57;//qq蓝钻成长礼包
	public static final int GET_QQ_3366_BAOZI = 58;//qq 3366包子每日礼包
	public static final int GET_LEI_HAO = 59;//累计消耗
	public static final int GET_HONGBAO = 60;//红包
	public static final int GET_CHAOS = 61;//混沌战场
	public static final int GET_QQ_LZ_HAOHUA = 62;//qq豪华蓝钻礼包
	public static final int GET_LABA = 63;//幸运拉霸
	public static final int GET_TGP_ZNL_ZHUANPAN = 64; //TGP正能量转盘
	public static final int GET_TGP_ZNL_DUIHUAN = 65; //TGP正能量兑换
	public static final int GET_CAIDAN = 66;//幸运彩蛋
	public static final int GET_BAGUA_FUBEN = 67;//领取八卦副本奖励
	public static final int GET_RFB_YAOSHENMOWEN = 68;//热发布妖神魔纹排名奖励
	public static final int GET_RFB_LANZUANXUFEI = 69;//蓝钻续费
	public static final int GET_RFB_QILINGPAIMING = 70;//热发布翅膀排名奖励
	public static final int GET_RFB_YAOSHENPAIMING = 71;//热发布翅膀排名奖励
	public static final int GET_RFB_YAOSHENHUNPOPAIMING = 72;//热发布妖神魂魄排名奖励
	public static final int GET_MAIGU_FUBEN = 73;//领取埋骨之地副本奖励
	public static final int GET_KUAFU_JINGJI = 74;//封神之战
	public static final int GET_REFABU_XUNBAO = 75;//热发布寻宝
	public static final int GET_REFABU_TANGBAO = 76;//热发布探宝
	public static final int GET_REFABU_TANGBAO_WANG = 77;//热发布探宝王城领取
	public static final int GET_REFABU_YAOSHENMOYIN = 78;//热发布妖神魔印
	public static final int GET_REFABU_TANGBAOXINWEN = 79;//热发布糖宝心纹
	public static final int GET_CUXIAO_REWARD = 80;//领取促销礼包
	public static final int GET_YN_YQ_HY = 81;//越南版邀请好友
	public static final int GET_YN_YU_RECHARGE = 82;//越南版预充值
	public static final int GET_TENCENT_SHANGDIAN = 83;//腾讯折扣商店
	public static final int GET_TENCENT_GUANJIA_FIRST = 84;//腾讯管家新手
	public static final int GET_TENCENT_GUANJIA_DAY = 85;//腾讯管家每日
	public static final int GET_TENCENT_YAOQING = 86;//腾讯邀请好友
	public static final int GET_RFB_SEVEN_LOGIN_GIFT = 87;//热发布登陆奖励
	public static final int GET_RFB_LAOWANJIA_HUIGUI = 88;//热发布老玩家回归奖励
	public static final int GET_RFB_HUIYANSHIJIN = 89;//慧眼识金
	public static final int GET_QUNXIANYAN = 90;//群仙宴排名
	public static final int GET_QUNXIANYAN_CJ = 91;//群仙宴采集
    public static final int GET_WUXING_FUBEN_GIFT = 92;// 五行副本奖励
    public static final int GET_WUXING_SKILL_FUBEN_GIFT = 93;//五行技能副本奖励
    public static final int GET_RFB_LUNPAN = 94;//热发布充值轮盘领取
    public static final int GET_RFB_FIRST_REBATE = 95; //热发布首充返利领取
    public static final int GET_WUXING_SHILIAN_FUBEN_GIFT = 96;//五行试炼副本奖励
    public static final int GET_QQGAME_XINSHOU = 97;//QQ游戏大厅新手礼包奖励
    public static final int GET_QQGAME_LEVEL = 98;//QQ游戏大厅等级礼包奖励
    public static final int GET_XM_LIANDAN_TOOK_OUT = 99;//心魔炼丹取出丹药
    public static final int GET_QQGAME_EVERY = 100;//QQ游戏大厅每日礼包奖励
    public static final int GET_QqQZone_XINSHOU = 101;//QQ空间新手礼包奖励
    public static final int GET_QqQZone_LEVEL = 102;//QQ空间等级礼包奖励
    public static final int GET_QqQZone_EVERY = 103;//QQ空间每日礼包奖励
    public static final int GET_RFB_JUEBAN = 104;//热发布-绝版礼包奖励
    public static final int GET_XINMO_SHENYUAN_FUBEN_GIFT = 105;//心魔深渊副本奖励
    public static final int GET_MGLY_EXCHANGE_ITEM = 106;//魔宫猎焰兑换道具
    public static final int GET_EXTREME_RECHARGE_ITEM = 107;//热发布至尊充值奖励
    public static final int GET_PERNONAL_BOSS = 108;//个人BOSS获得
    public static final int GET_WUXING_GIFT = 109; //五行直升丹使用
    public static final int GET_CJ_GIFT = 110;//成就领奖
    public static final int GET_ZHIXIAN_REWARD = 111;//领取支线奖励
    public static final int GET_FUBENDAY_SHAODANG = 112;//每日副本扫荡
    public static final int GET_REFABU_XIAOHAO = 113;//热发布累计消耗
    public static final int GET_XSLB = 114;//限时礼包
    public static final int GET_XIULIAN_DUIHUAN = 115;//修炼兑换
    public static final int GET_XIULIAN_DAY = 116;//修炼任务每日奖励
    public static final int GET_LJ = 117;//炼金
    public static final int GET_XKBZ = 112;//星空宝藏
    
	/**备注**/
	public static final int GBZ_NORMAL = 0;//不会被用到，但是调方法需要传值
	public static final int GBZ_TRADE = 1;//交易获得
	public static final int GBZ_USE_PROP = 2; 
	public static final int GBZ_GOODS_PICKUP = 3;
	public static final int GBZ_FUBEN = 4; 
	public static final int GBZ_EMAIL = 5; //邮件获得
	public static final int GBZ_TASKDAY_FINISH = 6;//每日任务完成获得的奖励
	public static final int GBZ_DAZUO = 7;//打坐获得
	public static final int GBZ_TASK = 8; //主线任务
	public static final int GBZ_JINGJI_RANK = 9; //竞技场排名奖励
	public static final int GBZ_SELL_GOODS = 10; //物品出售
	public static final int GBZ_GIFT_CARD = 11;//礼包卡
	public static final int GBZ_OFFLINE_EXP = 12;//离线经验
	public static final int GBZ_RESOURCE_BACK = 13;//资源找回
	public static final int GBZ_OPEN_GIFT = 14;//打开礼包卡
	public static final int GBZ_SEVEN_LOGIN_GIFT = 15;//领取七日登陆奖励
	public static final int GBZ_SHOUHU_FUBEN = 16;//守护副本奖励
	public static final int GBZ_DATI_AWARD=17;//直接领取答题奖励
	public static final int GBZ_MORE_FUBEN = 18;//领取多人副本奖励
	public static final int GBZ_RFB_SHOUCHONG1 = 19;//首充奖励第一档
	public static final int GBZ_RFB_SHOUCHONG2 = 20;//首充奖励第二档
	public static final int GBZ_RFB_SHOUCHONG3 = 21;//首充奖励第三档
	public static final int GBZ_RFB_SHOUCHONG4 = 22;//首充奖励第四档
	public static final int GBZ_RFB_ZHANLIBIPIN = 23;//战力比拼
	public static final int GBZ_RECHARGE = 24;//充值获得
	public static final int GBZ_RFB_YUJIANFEIXING= 25;//御剑飞行
	public static final int GBZ_RFB_CHIBANGPAIMING= 26;//翅膀排名
	public static final int GBZ_YABIAO_MONEY = 27;//押镖奖励
	public static final int GBZ_WEIDUAN_GET = 28;//微端获取奖励
	public static final int GBZ_PHONE_REWARD = 29;//手机礼包奖励
	public static final int GBZ_PATA_GIFT = 30;//爬塔副本奖励
	public static final int GBZ_PT_VIP_REWARD = 31;//37wan VIP等级礼包
	public static final int GBZ_RFB_QIANGHUA = 32;//强化等级领取
	public static final int GBZ_RFB_QIPAN = 33;//棋盘
	public static final int GBZ_RFB_LEICHONG = 34;//累充
	public static final int GBZ_RFB_SMSD = 35;//神秘商店
	public static final int GBZ_TOUZI = 36;//投资计划
	public static final int GBZ_RFB_ZHUANPAN = 37;//命运转盘
	public static final int GBZ_RFB_TBXJ= 38;//糖宝仙剑	
	public static final int GBZ_RFB_TBZJ= 39;//糖宝战甲
	public static final int GBZ_CB_CJ_BGOLD = 40;//翅膀吃道具直接升阶转换绑元
	public static final int GBZ_RFB_DMSZ= 41;//盗墓手札
	public static final int GBZ_RFB_LIANCHONG= 42;//连充
	public static final int GBZ_QQ_HZ_XINSHOW= 43;//qq黄钻新手
	public static final int GBZ_QQ_HZ_DENGJI = 44;//qq黄钻等级礼包
	public static final int GBZ_QQ_HZ_NIANFEI = 45;//qq黄钻年费礼包
	public static final int GBZ_QQ_HZ_CHENGZHANG = 46;//qq黄钻成长礼包
	public static final int GBZ_HEFU_SEVEN_LOGIN_GIFT = 47;//领取合服七日登陆奖励
	public static final int GBZ_QQ_WENDUAN_GIFT = 48;//腾讯微端礼包领取
	public static final int GBZ_QQ_TGP_GIFT = 49;//腾讯TGP礼包领取
	public static final int GBZ_RFB_CZ_FANLI = 50;//热发布充值返利领取
	public static final int GBZ_QQ_TGP_LOGIN_GIFT = 51;//腾讯TGP登陆礼包领取
	public static final int GBZ_QQ_TGP_LEVEL_GIFT = 52;//腾讯TGP等级礼包领取
	public static final int GBZ_CUILIAN = 53;//淬炼
	public static final int GBZ_SUPER_DUIHUAN = 54;//超值兑换
	public static final int GBZ_QQ_LZ_XINSHOW= 55;//qq黄钻新手
	public static final int GBZ_QQ_LZ_DENGJI = 56;//qq黄钻等级礼包
	public static final int GBZ_QQ_LZ_NIANFEI = 57;//qq黄钻年费礼包
	public static final int GBZ_QQ_LZ_CHENGZHANG = 58;//qq黄钻成长礼包
	public static final int GBZ_QQ_3366_BAOZI = 59;//qq3366包子每日礼包
	public static final int GBZ_LEIHAO = 60;//累计消耗
	public static final int GBZ_GET_HONGBAO = 61;//红包
	public static final int GBZ_CHAOS_PUJIANG = 62;//混沌战场全民奖励
	public static final int GBZ_QQ_LZ_HAOHUA = 63;//qq豪华黄钻礼包
	public static final int GBZ_LABA = 64;//幸运拉霸
	public static final int GBZ_TGP_ZNL_ZHUANPAN = 65;//TGP正能量转盘
	public static final int GBZ_TGP_ZNL_DUIHUAN = 66;//TGP正能量兑换
	public static final int GBZ_ZADAN = 67;//砸蛋
	public static final int GBZ_BAGUA_FUBEN = 68;//领取八卦副本奖励
	public static final int GBZ_RFB_YAOSHENMOWEN= 69;//妖神魔纹
	public static final int GBZ_RFB_LANZUANXUFEI= 70;//蓝钻续费
	public static final int GBZ_RFB_QILING= 71;//翅膀排名
	public static final int GBZ_RFB_YAOSHEN= 72;//翅膀排名
	public static final int GBZ_RFB_YAOSHENHUNPO= 73;//妖神魂魄排名
	public static final int GBZ_MAIGU_FUBEN = 74;//领取埋骨之地副本奖励
	public static final int GBZ_KUAFU_JINGJI_DAY_GIFT = 75;//封神之战 每日奖励
	public static final int GBZ_REFABU_TANBAO = 76;//热发布探宝
	public static final int GBZ_REFABU_TANBAO_WANG = 77;//热发布探宝王城领取
	public static final int GBZ_REFABU_YAOSHENMOYIN = 78;//热发布妖神魔印排名领取
	public static final int GBZ_REFABU_TANGBAOXINWEN = 79;//热发布糖宝心纹排名领取
	public static final int GBZ_HUOYUEDU_LB = 80;//活跃度礼包
	public static final int GBZ_CUXIAO_REWARD_LB = 81;//促销礼包
	public static final int GBZ_YN_YQ_HY = 82;//越南版邀请好友
	public static final int GBZ_YN_YU_RECHARGE = 83;//越南版预充值
	public static final int GBZ_TENCENT_SHANGDIAN = 84;//腾讯折扣商店
	public static final int GBZ_TENCENT_GUANJIA_FIRST = 85;//腾讯管家新手领取
	public static final int GBZ_TENCENT_GUANJIA_DAY = 86;//腾讯管家每日
	public static final int GBZ_TENCENT_YAOQING = 87;//腾讯邀请好友
	public static final int GBZ_RFB_SEVEN_LOGIN_GIFT = 88;//热发布登陆奖励
	public static final int GBZ_RFB_LAOWANJIA_HUIGUI = 89;//热发布老玩家回归奖励
	public static final int GBZ_RFB_HUIYANSHIJIN= 90;//慧眼识金
	public static final int GBZ_QUNXIANYAN= 91;//群仙宴排名
	public static final int GBZ_QUNXIANYAN_CJ= 92;//群仙宴采集
    public static final int GBZ_WUXING_FUBEN_GIFT = 93;// 五行副本奖励
    public static final int GBZ_WUXING_SKILL_FUBEN_GIFT = 94;//五行技能副本奖励
    public static final int GBZ_RFB_LUNPAN = 95;//热发布轮盘奖励
    public static final int GBZ_RFB_FIRST_REBATE = 96;//热发布首充返利领取
    public static final int GBZ_WUXING_SHILIAN_FUBEN_GIFT = 97;//五行试炼副本奖励
    public static final int GBZ_QQGAME_XINSHOU = 98;//QQ游戏大厅新手礼包领取
    public static final int GBZ_QQGAME_LEVEL = 99;//QQ游戏大厅等级礼包领取
    public static final int GBZ_XM_LIANDAN_TOOK_OUT = 100;//心魔炼丹取出丹药
    public static final int GBZ_QQGAME_EVERY = 101;//QQ游戏大厅每日礼包领取
    public static final int GBZ_QqQZone_XINSHOU = 102;//QQ空间新手礼包领取
    public static final int GBZ_QqQZone_LEVEL = 103;//QQ空间等级礼包领取
    public static final int GBZ_QqQZone_EVERY = 104;//QQ空间每日礼包领取
    public static final int GBZ_RFB_JUEBAN = 105;//热发布-绝版礼包
    public static final int GBZ_XINMO_SHENYUAN_FUBEN_GIFT = 106;//心魔深渊副本奖励
    public static final int GBZ_MGLY_EXCHANGE_ITEM = 107;//魔宫猎焰兑换道具
    public static final int GBZ_EXTREME_RECHARGE_ITEM = 108;//热发布至尊充值奖励
    public static final int GBZ_PERNONAL_BOSS = 109;//个人BOSS
    public static final int GBZ_RFB_ONCECHONG = 110;//单笔充值
    public static final int GBZ_CJ_GIFT = 111;//成就领奖
    public static final int GBZ_ZHIXIAN_REWARD_LB = 112;//促销礼包
    public static final int GBZ_FUBENDAY_SAODANG = 113;//每日副本扫荡
    public static final int GBZ_REFABU_XIAOHAO = 114;//热发布累计消耗
    public static final int GBZ_LIMIT_SHOP = 115;//限时礼包
    public static final int GBZ_XIULIAN_DUIHUAN = 116;//修炼兑换
    public static final int GBZ_XIULIAN_DAY = 118;//修炼兑换
    public static final int GBZ_LJ = 117;//炼金
    public static final int GBZ_XKBZ = 119;//星空宝藏
	//----------------技能--------------------------------------------------------
	public static final int SKILL = 10;
	
	public static final int SKILL_TYPE_ZD_LEARN = 1;//主动学习
	public static final int SKILL_TYPE_ZD_LEVEL_UP = 2;//主动升级
	public static final int SKILL_TYPE_BD_LEARN = 3;//被动学习
	public static final int SKILL_TYPE_BD_LEVEL_UP = 4;//被动升级
	public static final int SKILL_TYPE_GUILD_LEVEL_UP = 5;//门派技能升级
	public static final int SKILL_TYPE_YS_LEVEL_UP = 6;//妖神技能升级
	public static final int SKILL_TYPE_TB_LEARN = 7;//糖宝技能学习
	public static final int SKILL_TYPE_TB_LEVEL_UP = 8;//糖宝技能升级
	
	//----------------竞技--------------------------------------------------------
	public static final int JINGJI_GIFT = 11;//竞技排名奖励
	public static final int JINGJI_DUIHUAN = 12;//竞技物品兑换
	
	//----------------交易--------------------------------------------------------
	public static final int TRADE = 13;
	
	//----------------装备回收--------------------------------------------------------
	public static final int EQUIP_RECYCLE = 14;
	
	//----------------装备强化--------------------------------------------------------
	public static final int EQUIP_QH = 15;
	


	
	//----------------副本--------------------------------------------------------
	public static final int FUBEN = 16;
	
	public static final int FUBEN_TYPE_TIAOZHAN = 1;//挑战
	public static final int FUBEN_TYPE_SAODANG = 2;//扫荡
	
	//----------------坐骑升阶--------------------------------------------------------
	public static final int ZUOQI_SJ = 17;
	
	//----------------签到--------------------------------------------------------
	public static final int ASSIGN = 18;
	
	public static final int ASSIGN_RETROACTIVE = 1;//一键补签
	public static final int ASSIGN_IN = 2;//签到
	
	//----------------签到领奖--------------------------------------------------------
	public static final int ASSIGN_REWARD = 19;

	//----------------在线奖励--------------------------------------------------------
	public static final int ONLINE_REWARD = 20;
	
	//----------------礼包卡兑换--------------------------------------------------------
	public static final int GIFT_CARD = 21;	
	
	//----------------七日奖励--------------------------------------------------------
	public static final int SEVEN_DAY_REWARD = 22;	
	
	//----------------离线经验--------------------------------------------------------
	public static final int OFFLINE_EXP = 23;	
	
	//----------------资源找回--------------------------------------------------------
	public static final int RESOURCE_BACK = 24;
	
	//----------------用户登陆gate信息---------------------------
	public static final int LOGIN_GATE_INFO = 25;
	
	//----------------首次充值日志---------------------------
	public static final int PLAYER_FIRST_RECHARE = 26;

	
	//----------------翅膀升阶--------------------------------------------------------
	public static final int CHIBANG_SJ = 27;
	
	//-----------------在线人数---------------------------------
	public static final int ONLINE_COUNT = 28;
	
	//-----------------在线时长---------------------------------
	public static final int ONLINE_TIME = 29;


	//-----------------首充类活动----------
	public static final int GET_FIRST_RECHARGE = 30;
	
	
	//---------------------------寻宝--------------------------------------
	public static final int XUNBAO = 31;
	
	public static final int XUNBAO_TYPE_PT = 1;//普通寻宝
	public static final int XUNBAO_TYPE_CBZ = 2;//热发布藏宝阁
	
	
	//---------------------------寻宝积分兑换--------------------------------------
	public static final int XUNBAO_JFDH = 32;
	
	//---------------------------寻宝背包取出物品--------------------------------------
	public static final int XUNBAO_BAG_QC = 33; 
	//----------------装备升级--------------------------------------------------------
	public static final int EQUIP_SJ = 34;
	//----------------玄铁值兑换--------------------------------------------------------
	public static final int XUANTIE_DUIHUAN = 35;
	//----------------开服排名活动--------------------------------------------------------
	public static final int KAIFU_ACTITY = 36;
	public static final int KAIFU_ACTITY_ZHANLI = 1;
	public static final int KAIFU_ACTITY_ZUOQI = 2;
	public static final int KAIFU_ACTITY_CHIBANG = 3;
	public static final int KAIFU_ACTITY_JINGJI = 4;
	public static final int XIAOFEI_PAIMING = 5;
	public static final int ZHANJIA_PAIMING = 6;
	public static final int YAOSHEN_PAIMING = 7;
	public static final int YAOSHEN_MOWEN = 8;
	public static final int QILING_PAIMING = 9;
	public static final int TIANGONG_PAIMING = 10;
	public static final int YAOSHEN_HUNPO = 11;
	public static final int YAOSHEN_MOYIN = 12;
	public static final int TANGBAO_XINWEN = 13;
	public static final int KAIFU_ACTITY_WUQI = 14;
	//---------------------------神器激活--------------------------------------
	public static final int SHENQI_ACTIVATE = 37; 	
	//---------------------------37领取手机礼包--------------------------------------
	public static final int _37_PHONE_REWARD = 38; 	
	
	//------------------------商城购买--------------------------------------
	public static final int MAIL_BUY = 39;
	
	//------------------------角色升级日志--------------------------------------
	public static final int ROLE_UP_LEVEL = 40;
	
	//------------------------平台打印统计日志--------------------------------------
	public static final int PLATFORM_LOG = 41;
	//----------------开服排名活动领取--------------------------------------------------------
	public static final int KAIFU_ACTITY_LQ = 42;
	public static final int KAIFU_ACTITY_LQ_ZHANLI = 1;//战力
	public static final int KAIFU_ACTITY_LQ_ZUOQI = 2;//坐骑
	public static final int KAIFU_ACTITY_LQ_CHIBANG = 3;//翅膀
	public static final int KAIFU_ACTITY_LQ_LEVEL = 4;//等级
	public static final int KAIFU_ACTITY_LQ_QIANGHUA = 5;//强化
	public static final int KAIFU_ACTITY_LQ_TBXJ = 6;//糖宝仙剑
	public static final int KAIFU_ACTITY_LQ_TBZJ = 7;//糖宝战甲
	public static final int KAIFU_ACTITY_LQ_YSMW = 8;//妖神魔纹
	public static final int KAIFU_ACTITY_LQ_YS = 9;//妖神魔纹
	public static final int KAIFU_ACTITY_LQ_YSHP = 10;//妖神魂魄
	public static final int KAIFU_ACTITY_LQ_QL = 11;//器灵
	public static final int KAIFU_ACTITY_LQ_YQMY = 12;//妖神魔印
	public static final int KAIFU_ACTITY_LQ_TBXW = 13;//糖宝心纹
	public static final int KAIFU_ACTITY_LQ_WUQI = 14;//武器
	
	//------------------------玩家手动删除带附件的邮件日志--------------------------------------
	public static final int EMAIL_DEL_LOG_BIG = 43;
	//----------------仙剑升阶--------------------------------------------------------
	public static final int XIANJIAN_SJ = 44;
	//---------------------------37领取手机礼包激活--------------------------------------
	public static final int _37_PHONE_REWARD_ACTICATE = 45; 	
	//----------------战甲升阶--------------------------------------------------------
	public static final int ZHANJIA_SJ = 46;
		
    //----------------排行日志--------------------------------------------------------
	public static final int CAMPWAR_STAGE = 47;
	
	//----------------运营活动结算排行日志--------------------------------------------------------
	public static final int YUNYIN_ACTITY_PAIMING = 48;
	public static final int PAIMING_ACTITY_ZHANLI = 1;
	public static final int PAIMING_ACTITY_ZUOQI = 2;
	public static final int PAIMING_ACTITY_CHIBANG = 3;
	public static final int PAIMING_ACTITY_JINGJI = 4;
	public static final int PAIMING_XIAOFEI_PAIMING = 5;
	public static final int PAIMING_ZHANJIA_PAIMING = 6;
	public static final int PAIMING_YAOSHEN_PAIMING = 7;
	public static final int PAIMING_YAOSHEN_MOWEN_PAIMING = 8;
	public static final int PAIMING_QILING_PAIMING = 9;
	public static final int PAIMING_TIANGONG_PAIMING = 10;
	public static final int PAIMING_YAOSHEN_HUNPO_PAIMING = 11;
	public static final int PAIMING_YAOSHEN_MOYIN_PAIMING = 12;
	public static final int PAIMING_TANG_BAO_XINWEN = 13;
	public static final int PAIMING_XIANHUA_BANG = 14;
	public static final int PAIMING_ACTITY_WUQI = 15;
	

	//----------------平台相关礼包领取--------------------------------------------------------
	public static final int PLATFORM_GIFT = 49;
	public static final int PLATFORM_GIFT_37WAN_VIP = 1; //37wanVip礼包
	public static final int PLATFORM_GIFT_360_JIASUQIU = 2;//360加速球
	public static final int PLATFORM_GIFT_360_DATING = 3; //360大厅
	public static final int PLATFORM_GIFT_XUNLEI_VIP = 4; //迅雷vip
	public static final int PLATFORM_GIFT_XUNLEI_DATING = 5; //迅雷大厅
	public static final int PLATFORM_GIFT_XUNLEI_TEQUAN = 6; // 迅雷特权
	public static final int PLATFORM_GIFT_SOGOU_DATING = 7; //搜狗大厅
	public static final int PLATFORM_GIFT_SOGOU_PIFU = 8; //搜狗皮肤
	public static final int PLATFORM_GIFT_SWJOY = 9; //顺网 
	public static final int PLATFORM_GIFT_37WAN_LINGPAI = 10; //37wan令牌礼包
	public static final int PLATFORM_GIFT_360_Tequan = 11; //360特权等级礼包
	public static final int PLATFORM_GIFT_SWJOY_VIP_LB = 12; //顺网VIP等级礼包
	public static final int PLATFORM_GIFT_360_Tequan_Share_LB = 13; //360特权分享礼包
	
	//----------------装备提品--------------------------------------------------------
	public static final int EQUIP_TIPIN = 50;
	

	//----------------合服七日奖励--------------------------------------------------------
	public static final int HEFU_SEVEN_DAY_REWARD = 51;	
	
	//-----------------腾讯用户来源日志
	public static final int TENCENT_APP_CUSTOM = 52;
	//-----------------腾讯用户联盟上报失败日志
	public static final int TENCENT_APP_SBSB = 53;
	//-----------------宝石系统日志
	public static final int JEWEL_LOG = 54;
	//---------------------------妖神--------------------------------------
	public static final int YAOSHNE_ACTIVATE = 55;
	public static final int YAOSHNE_UPGRADE = 56;
	//-----------------------神秘商店购买------------
	public static final int SMSD_BUY = 57;
	//---------------------幸运转盘
	public static final int ZHUANGPAN_LOG = 58;
	//---------------------------宠物--------------------------------------
	public static final int CHONGWU_ACTIVATE = 59;
	
	public static final int ZHUANGPAN_LOG_DUIHUAN = 60;
	//---------------------------帮派转让日志--------------------------------------
	public static final int GUILD_POSITION_CHANGE_LOG = 61;
	//---------------------------改名日志--------------------------------------
	public static final int  USER_MODIFY_NAME= 62; 	
	//---------------------------宠物升阶--------------------------------------
	public static final int  CHONGWU_UPDATE_JIE= 63; 	
	//----------------360V计划礼包--------------------------------------------------------
	public static final int  _360_VPLAN_GIFT= 64; 	
	//---------------------棋盘
	public static final int QINPAN_LOG = 65;
	//---------------------Q点直购
	public static final int QDIAN_ZHIGOU = 66;
	//----------------红包--------------------------------------------------------
	public static final int  HONGBAO_GIFT= 67; 	
	//---------------------混沌战场------------------------------------------------
	public static final int CHAOS_LOG = 68;
	
	//---------------------拉霸活动------------------------------------------------
	public static final int LABA_LOG = 69;

	//---------------------幸运彩蛋------------------------------------------------
	public static final int CAIDAN_LOG = 70;
	public static final int CAIDAN_DUIHUAN_LOG = 71;
	//---------------------妖神魔纹------------------------------------------------
	public static final int YAOSHEN_MOWEN_LOG = 72;
	//---------------------跨服竞技兑换日志------------------------------------------------
	public static final int KUAFU_JINGJI_DUIHUAN_LOG = 73;
	//---------------------门派兑换日志------------------------------------------------
	public static final int GUILD_DUIHUAN_LOG = 74;

	//----------------多人副本领奖--------------------------------------------------------
	public static final int MORE_FUBEN_AWARD= 75;
	//----------------八卦副本领奖--------------------------------------------------------
	public static final int BAGUA_FUBEN_AWARD= 76;
	//----------------器灵升阶--------------------------------------------------------
	public static final int QILING_SJ = 77;
	//---------------------妖神魂魄升级------------------------------------------------
	public static final int YAOSHEN_HUNPO_LOG = 78;
	//-----------------妖神魂魄--镶嵌魄神------------------------------------------------
	public static final int YAOSHEN_HUNPO_POSHEN_LOG = 79;
	//-----------------妖神魔印------------------------------------------------
	public static final int YAOSHEN_HUNPO_MOYIN_LOG = 80;
	//----------------埋骨副本领奖--------------------------------------------------------
	public static final int MAIGU_FUBEN_AWARD= 81;
	//-----------------------热发布修仙购买------------
	public static final int RFB_XIUXIAN_BUY = 82;
	//----------------神魂值--------------------------------------------------------
	public static final int SHEN_HUN_VALUE_INFO_LOG= 83;
	//---------------------糖宝心纹------------------------------------------------
	public static final int TANGBAO_XINWEN_LOG = 84;
	//---------------------通天之路------------------------------------------------
	public static final int TONGTIAN_ROAD_LOG = 85;
	//----------------附属装备升级--------------------------------------------------------
	public static final int FUSHU_EQUIP_SJ = 86;
	//----------------活跃度礼包--------------------------------------------------------
	public static final int HUOYUEDU_LOG = 87;
	//----------------转生--------------------------------------------------------
	public static final int ZHUANSHENG = 88;
	//----------------转生经验兑换--------------------------------------------------------
	public static final int ZHUANSHENG_DUIHUAN = 89;
	//----------------促销奖励--------------------------------------------------------
	public static final int CUXIAO_REWARD = 90;
	//----------------门派boss炼狱--------------------------------------------------------
	public static final int LIANYU_BOSS = 91;
	//----------------热发布在线奖励--------------------------------------------------------
	public static final int RFB_ONLINE_REWARDS = 92;

	//----------------套装铸神--------------------------------------------------------
	public static final int EQUIP_TAO_ZHUANG_ZHUSHEN = 93;
	//----------------送花--------------------------------------------------------
	public static final int FLOWER_SEND = 94;
	//----------------鲜花榜购买鲜花--------------------------------------------------------
	public static final int FLOWER_RANK_BUY = 95;
	//----------------腾讯管家礼包--------------------------------------------------------
	public static final int TENCENT_GUANJIA_GIFT = 96;
	//----------------神器洗练--------------------------------------------------------
	public static final int SHENQI_XILIAN = 97;

	//----------------妖神附魔升级--------------------------------------------------------
	public static final int YAOSHEN_HUMO_SJ = 98;
	//-----------------称号--------------------------------------------------------
	public static final int CHENHGAO_ACTIVATE = 99;
	//-----------------画卷--------------------------------------------------------
	public static final int HUAJUAN_HECHENG = 100;
	public static final int HUAJUAN_FENJIE = 101;
	//-----------------1v1 4v4日志--------------------------------------------------------
	public static final int KUAFU_ARENA_1V1 = 102;
	public static final int KUAFU_ARENA_4V4 = 103;
	//----------------天羽升阶--------------------------------------------------------
	public static final int TIANYU_SJ = 104;
	//----------------------------热发布秒杀---------------------------------------
	public static final int REFABU_MIAOSHA = 105;
	//----------------------------慧眼识金---------------------------------------
	public static final int REFABU_HUIYANSHIJN = 106;
	//----------------------------五行副本--------------------------------------
	public static final int WUXING_FUBEN = 107;
	//----------------------------热发布活动参与--------------------------------
	public static final int REFABU_PART_IN = 108;		
	public static final int REFABU_LEICHONG = 1;//累计充值
	public static final int REFABU_XIAOFEI = 2;//累计消费
	public static final int REFABU_LIANCHONG = 3;//连充奖励
	public static final int REFABU_SUPERDUIHUAN = 4;//超值兑换
	public static final int REFABU_SHOUCHONG = 5;//线性充值奖励
	public static final int REFABU_LOOPDAYCHONGZHI = 6;//每日充值
	public static final int REFABU_ONCECHONGZHI = 7;//单笔充值
	// ----------------------------五行技能副本--------------------------------------
	public static final int WUXING_SKILL_FUBEN = 109;
	public static final int WX_SKILL_FUBEN_TIAOZHAN = 1;// 挑战
	public static final int WX_SKILL_FUBEN_SAODANG = 2;// 扫荡
    //超值兑换日志打印
    public static final int SUPERDUIHUAN_LOG = 110;
    //团购日志打印
    public static final int TUANGOU_LOG = 111;
    //-----------------------------五行试炼副本-------------------------------------
    public static final int WUXING_SHILIAN_FUBEN = 112;
    //-----------------------------心魔系统日志-------------------------------------
    public static final int XINMO_LOG = 113;
	//------------------------------统计暗金装备日志------------------------------------
    public static final int CALC_ANJIN_EQUIP_LOG = 114;
    //------------------------------统计神武装备日志------------------------------------
    public static final int CALC_SHENWU_EQUIP_LOG = 115;
    //-----------------------------心魔-魔神日志-------------------------------------
    public static final int XINMO_MOSHEN_LOG = 116;
    //-----------------------------心魔-深渊副本日志-------------------------------------
    public static final int XINMO_SHENYUAN_FUBEN_LOG = 117;
    //-----------------------------心魔-斗场副本日志-------------------------------------
    public static final int XINMO_DOUCHANG_FUBEN_LOG = 118;
    //-----------------------------宠物技能升级日志-------------------------------------
    public static final int CHONGWU_SKILL_UPLEVEL_LOG = 119;
    //-----------------------------画卷2-------------------------------------------
    /* 激活 */
    public static final int HUAJUAN2_ACTIVE = 120;
    /* 分解 */
    public static final int HUAJUAN2_FENJIE = 121;
    /* 升星 */
    public static final int HUAJUAN2_UPGREADE = 122;
    // -----------------------------五行魔神升阶日志-------------------------------------
    public static final int WUXING_MOSHEN_LOG = 123;
    public static final int WUXING_MOSHEN_TYPE_ROLE = 1;// 玩家五行魔神升阶日志
    public static final int WUXING_MOSHEN_TYPE_TANGBAO = 2;// 糖宝五行魔神升阶日志
    // -----------------------------------魔宫猎焰兑换日志------------------------------
    public static final int MOGONGLIEYAN_LOG = 124;
    // -----------------------------------仙洞升级日志------------------------------
    public static final int XIANDONG_LOG = 125;
    // -----------------------------------仙器觉醒升级日志------------------------------
    public static final int XIANDONG_JUEXING_LOG = 126;
    // -----------------------------------仙缘飞化升级日志---------------------------
    public static final int XIANYUAN_FEIHUA_LOG = 127;
    // -----------------------------------神器进阶日志---------------------------
    public static final int SHENQI_JINJIE_LOG = 128;
    // -----------------------------------套装象位升阶日志---------------------------
    public static final int TAOZHUANG_XIANGWEI_LOG = 129;
    
 // -----------------------------------新圣剑升阶日志---------------------------
    public static final int WUQI_SJ = 130;
	/**
	 * 打印日志
	 * @param bigType 日志大类类型
	 * @param message 主体数据
	 */
	public static void printLog(int bigType,String message){
		StringBuffer buff = new StringBuffer();
		buff.append(bigType).append(JOIN_CHAR).append(message);
		
		ChuanQiLog._1MinuteLog(buff.toString());
	}
	
	/**
	 * 把物品map转成所日志所需的json格式
	 * @param goods
	 * @param result	json结果对象，可以为null
	 * @return
	 */
	public static JSONArray getLogGoodsParam(Map<String,Integer> goods,JSONArray result){
		if(result == null){
			result = new JSONArray();
		}
		if(goods != null && goods.size() > 0){
			for (Entry<String,Integer> entry : goods.entrySet()) {
				Map<String,Object> goodsData = new HashMap<>();
				goodsData.put("goodsId", entry.getKey());
				goodsData.put("count", entry.getValue());
				result.add(goodsData);
			}
		}
		return result;
	}
	
}
