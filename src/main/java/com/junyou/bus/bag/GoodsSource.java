package com.junyou.bus.bag;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 物品来源(消耗/获得) 请注明获得或消耗
 * @author LiuYu
 * @date 2014-12-29 下午5:04:42
 */
public class GoodsSource {
	
	/**无（用于无物品获得时使用）*/
	public static final int NONE = -1;
	
	/**主线任务(获得)*/
	public static final int ZHUXIAN_TASK = 1;
	
	/**邮件附件(获得)*/
	public static final int EMAIL_FUJIAN = 2; 
	
	/**激活神器(消耗)*/
	public static final int ACTIVATE_SHENQI = 3;
	
	/**GM指令添加(获得)*/
	public static final int GM_FOR_DEBUG = 4;
	
	/**装备强化(消耗)*/
	public static final int EQUIP_QH = 5;
	
	/**随身商店购买(获得)*/
	public static final int BUY_GOODS = 6;
	
	/**PK掉落(消耗)*/
	public static final int PK_DROP = 7;
	
	/**装备回收(消耗/获得)*/
	public static final int EQUIP_RECYCLE = 8;
	
	/**物品合成(消耗/获得)*/
	public static final int GOODS_HC = 9;
	
	/**特权奖励(获得)*/
	public static final int TEQUAN_GIFT = 10;
	
	/**翅膀进阶(消耗)*/
	public static final int WING_JINJIE = 11;

	/**交易(消耗/获得)*/
	public static final int TRADE = 12;

	/**刷新镖车(消耗)*/
	public static final int RERESH_BIAO_CHE = 13;
	
	/**场景拾取物品(获取)*/
	public static final int STAGE_TAKE_GOODS = 14; 
	
	/**神器进阶(消耗)*/
	public static final int SQSJ = 15;
	
	/**神器兑换(获得)*/
	public static final int SQDH = 16;
	
	/**领地战每日奖励*/	
	public static final int TERRITORY_DAY_REWARD = 17;
 
	/**  物品拆分（消耗/获得）*/
	public static final int SPLIT_GOODS = 18;
	
	/** 丢弃物品 （消耗）*/
	public static final int GIVE_UP = 19;
	
	/** 销毁 （消耗）*/
	public static final int DESTROY_ITEM = 20;
	
	/** 使用 （消耗）*/
	public static final int USE_ITEM = 21;
	/**qq黄钻新手礼包(获得)*/	
	public static final int QQ_HZ_XINSHOU = 22;
	/**副本(获得)**/
	public static final int FUBEN = 23;
	/**喇叭(消耗)**/
	public static final int USE_LABA = 24;
	/**技能学习(消耗)**/
	public static final int SKILL_LEARN = 25; 
	/**日常任务(获得)**/
	public static final int TASK_DAY = 26;
	/**个人竞技(获得)**/
	public static final int ROLE_JINGJI = 27;
	/**坐骑升阶(消耗)*/
	public static final int ZQ_SJ = 28;
	/**市场上下架(消耗/获得)*/
	public static final int PAI_MAI = 29;
	/**npc出售(消耗)*/
	public static final int NPCSELL = 30;
	/**市场购买购买卖出(消耗/获得)*/
	public static final int PAI_MAI_GM = 31;
	/**复活消耗*/
	public static final int PROP_REVIVE = 32;
	/**门派捐献消耗*/
	public static final int GUILD_JUANXIAN = 33;
	/**签到领取奖励*/
	public static final int ASSIGN_TOTAL = 34;
	/**在线奖励领取*/
	public static final int ONLINE_REWARD = 35;
	/**礼包卡兑换*/
	public static final int GIFT_CARD_REWARD = 36;
	/**七日登陆奖励*/
	public static final int SEVEN_DAY_REWARD = 37;
	/**打开固定/随机礼包卡*/
	public static final int OPEN_GIFT_CARD = 38;
	/**商城（获得）**/
	public static final int MALL_BUY = 39;
	/**领取多选礼箱奖励（获得）**/
	public static final int DXLX_GET = 40;
	/**直接领取答题奖励*/
	public static final int DIRECT_GET_DATI_AWARD = 41;
	/**守护副本首次通关奖励**/
	public static final int SH_FB_FIRST_GIST = 42;
	/**后台GM删除物品**/
	public static final int GM_TOOLS_DELETE = 43;
	/**VIP等级礼包*/
	public static final int VIP_LEVEL_GIFT = 44;
	/**VIP周礼包*/
	public static final int VIP_WEEK_GIFT = 45;
	/**翅膀升阶(消耗)*/
	public static final int CB_SJ = 46;
	/**押镖（消耗）**/
	public static final int YABIAO = 47;
	/**多人副本奖励（获得）*/
	public static final int MORE_FUBEN_GET = 48;
	/**糖宝吃的*/
	public static final int TB_EAT = 49;
	/**采箱*/
	public static final int COLLECT_BOX = 50;
	/**飞鞋消耗*/
	public static final int FEIXIE = 51;
	/**领取首充礼包*/
	public static final int GOODS_GET_SC = 52;
	/**玄铁兑换(获得)**/
	public static final int XUANTIE_DUIHUAN = 53;
	/**装备熔炼(获得)**/
	public static final int ZB_RONGLIAN = 54;
	/**装备升级（消耗）**/
	public static final int ZB_SHENGJI = 55;
	/**寻宝(获得/消耗)*/
	public static final int XUNBAO = 56;
	/**热发布全民修仙(获得)*/
	public static final int RFB_QMXX = 57;
	/**热发布修仙礼包*/
	public static final int RFB_XIUXIAN_LB = 58;
	/**领取战力比拼礼包*/
	public static final int GOODS_GET_ZLBP = 59;
	/**领取御剑飞行礼包*/
	public static final int GOODS_GET_YJFX = 60;
	/**领取翅膀排名礼包*/
	public static final int GOODS_GET_CBPM = 61;
	/**37手机礼包*/
	public static final int _37_PHONE_REWARD = 62;
	/**微端领取奖励*/
	public static final int WEIDUAN_GET = 63;
	/**37vip等级礼包*/
	public static final int _37_VIP_REWARD = 64;
	/**领取强化等级礼包*/
	public static final int GOODS_GET_QHDJ = 65;
	/**棋盘*/
	public static final int GOODS_GET_QP = 66;
	/**累充*/
	public static final int GOODS_GET_LC = 67;
	/**神秘商店*/
	public static final int GOODS_GET_SMSD = 68;
	/**仙剑升阶(消耗)*/
	public static final int XJ_SJ = 69;
	/**投资计划(获得)*/
	public static final int GOODS_GET_TOUZI = 70;
	/**命运转盘(获得)*/
	public static final int GOODS_GET_ZHUANPAN = 71;
	/**命运转盘(消耗)*/
	public static final int GOODS_GET_ZHUANPAN_XH = 72;
	/**战甲升阶(消耗)*/
	public static final int ZJ_SJ = 73;
	/**领取糖宝仙剑礼包*/
	public static final int GOODS_GET_TBXJ = 74;
	/**领取糖宝战甲礼包*/
	public static final int GOODS_GET_TBZJ = 75;
	/**灵境突破(获得)*/
	public static final int LINGSHI_TUPO = 76;
	/**盗墓手札(获得)*/
	public static final int GOODS_DAOMO_SHOUZHA = 77;
	/**活跃度(获得)*/
	public static final int GOODS_HUOYUEDU = 78;
	/**平台对接礼包*/
	public static final int GOODS_PLATFORM_GIFT = 79;
	/**qq黄钻等级礼包(获得)*/	
	public static final int QQ_HZ_DENGJI = 80;
	/**qq黄钻年费礼包(获得)*/	
	public static final int QQ_HZ_NIANFEI = 81;
	/**qq黄钻成长礼包(获得)*/	
	public static final int QQ_HZ_CHENGZHANG = 82;
	/**装备提品(消耗)*/
	public static final int GOODS_EQUIP_TIPIN = 83;
	/**合服七日登陆奖励*/
	public static final int HEFU_SEVEN_DAY_REWARD = 84;
	/**激活称号*/
	public static final int ACTIVATE_CHENGHAO = 85;
	/**门贡兑换(获得)*/
	public static final int GOODS_GET_GUILD_DUIHUAN = 86;
	/**玩家手动删除(消耗)*/
	public static final int GOODS_DROP_ITEMS = 87;
	/**激活妖神*/
	public static final int ACTIVATE_YAOSHEN = 88;
	/**升级妖神*/
	public static final int UPDATE_YAOSHEN = 89;

	/**宝石卸下(获得)*/
	public static final int GOODS_JEWEL_DOWN = 90;
	/**宝石镶嵌(消耗)*/
	public static final int GOODS_JEWEL_UP = 91;
	/**宝石合成(获得)*/
	public static final int GOODS_JEWEL_HECHENG_GET = 92;
	/**宝石合成(消耗)*/ 
	public static final int GOODS_JEWEL_DOWN_HECHENG_CONSUME = 93;
	/**宝石镶嵌替换所的(获得)*/ 
	public static final int GOODS_JEWEL_UP_REPLACE = 94;
	/**腾讯微端礼包领取*/ 
	public static final int GOODS_QQ_WEIDUAN_LINGQU = 95;
	/**腾讯TGP礼包领取*/ 
	public static final int GOODS_QQ_TGP_LINGQU = 96;
	/**热发布锁妖塔(获得)*/ 
	public static final int GOODS_REFB_SUOYAOTA = 97;
	
	/**时装激活(消耗)*/ 
	public static final int GOODS_SHIZHUANG_ACTIVE = 98;
	/**时装升级(消耗)*/ 
	public static final int GOODS_SHIZHUANG_SHENGJI = 99;
	/**超值兑换(获得)*/ 
	public static final int GOODS_SUPER_DUIHUAN_GET = 100;
	/**超值兑换(消耗)*/
	public static final int GOODS_SUPER_DUIHUAN_CONSUME = 101;
	/**宠物激活*/
	public static final int GOODS_CHONGWU_ACTIVATE_CONSUME = 102;
	/**宠物兑换*/
	public static final int GOODS_CHONGWU_UPGRADE_CONSUME = 103;
	/**皇城争霸赛每日奖励*/	
	public static final int HCZBS_DAY_REWARD = 104;
	/**连充礼包*/
	public static final int GOODS_LIANCHONG_GIFT = 105;
	/**欢乐卡牌*/ 
	public static final int GOODS_HAPPY_CARD = 106;
	/**结婚消耗*/
	public static final int GOODS_MARRY_CONSUME = 107;
	/**缘分消耗*/
	public static final int GOODS_YUANFEN_CONSUME = 108;
	/**腾讯TGP登陆礼包领取*/ 
	public static final int GOODS_QQ_TGP_LOGIN_LINGQU = 109;
	/**腾讯TGP等级礼包领取*/ 
	public static final int GOODS_QQ_TGP_LEVEL_LINGQU = 110;
	/**改名卡消耗*/
	public static final int GOODS_MODIFY_NAME = 111;
	
	/**qq蓝钻等级礼包(获得)*/	
	public static final int QQ_LZ_DENGJI = 112;
	/**qq蓝钻年费礼包(获得)*/	
	public static final int QQ_LZ_NIANFEI = 113;
	/**qq蓝钻成长礼包(获得)*/	
	public static final int QQ_LZ_CHENGZHANG = 114;
	/**qq蓝钻新手礼包(获得)*/	
	public static final int QQ_LZ_XINSHOU = 115;
	/**学习附属技能*/
	public static final int GOODS_LEARN_FUSHU_SKILL = 116;
	/**弹劾门主消耗道具*/
	public static final int GOODS_IMPEACH_ITEM = 117;
	
	/**腾讯3366包子每日礼包领取*/ 
	public static final int GOODS_QQ_3366_BAOZI_LINGQU = 118;

	/**洗练魔血(消耗)*/
	public static final int GOODS_XILIAN_MOXUE_ITEM = 119;
	/**洗练锁定符(消耗)*/
	public static final int GOODS_XILIAN_LOCK_ITEM = 120;

	
	/**功勋兑换(获得)*/ 
	public static final int GOODS_GET_GONGXUN_DUIHUAN = 121;
	/**累计消耗奖励*/ 
	public static final int GOODS_LEIHAO_GET = 122;
	/**红包获的*/ 
	public static final int GOODS_HONGBAO = 123;
	/**qq豪华蓝钻礼包(获得)*/	
	public static final int QQ_LZ_HAOHUA = 124;

	/**幸运拉霸获的*/ 
	public static final int GOODS_LABA = 125;
	/**TGP正能量转盘*/ 
	public static final int GOODS_TGP_ZHUANPAN = 126;
	/**TGP正能量兑换*/ 
	public static final int GOODS_TGP_DUIHUAN = 127;
	/**幸运彩蛋砸蛋(获得)*/	
	public static final int GOODS_CAIDAN = 128;
	/**幸运彩蛋兑换(获得)*/	
	public static final int GOODS_CAIDAN_DUIHUAN = 129;
	/**八卦副本奖励（获得）*/
	public static final int BAGUA_FUBEN_GET = 130;
	/**升级妖神魔纹*/
	public static final int UPDATE_YAOSHEN_MOWEN = 131;
	/**跨服1v1兑换道具(获的)*/
	public static final int KUAFU_ARENA_1V1_GONGXUN = 132;
	/**领取妖神魔纹排名礼包*/
	public static final int GOODS_GET_YSMW = 133;
	/**器灵升阶(消耗)*/
	public static final int QL_SJ = 134;
	/**腾讯蓝钻续费礼包*/
	public static final int GOODS_GET_LZXF = 135;
	/**升级妖神魂魄*/
	public static final int YAOSHEN_HUNPO_UPDATE = 136;
	/**镶嵌魄神(消耗)*/
	public static final int YAOSHEN_HUNPO_ON = 137;
	/**卸载魄神(获得)*/
	public static final int YAOSHEN_HUNPO_OFF = 138;
	/**魄神兑换(获得)*/
	public static final int GOODS_POSHEN_DUIHUAN = 139;
	/**镶嵌魄神旧的魄神被替换(获得)*/
	public static final int YAOSHEN_HUNPO_ON_OLD = 140;
	/**云浮剑冢副本(获得)*/
	public static final int JIANZHONG_FUBEN_GET = 141;
	/**升级妖神魔印(消耗)*/
	public static final int  YAOSHEN_MOYIN_SJ = 142;
	/**埋骨之地副本奖励（获得）*/
	public static final int MAIGU_FUBEN_GET = 143;
	/**塔防*/
	public static final int GOODS_TAFANG = 144;
	/**领取热发布寻宝全服奖励 (获得)**/
	public static final int QF_XUNBAO = 145;
	/** 坐骑成长消耗*/
	public static final int ZUOQI_CZ = 146;
	/** 坐骑潜能消耗*/
	public static final int ZUOQI_QN = 147;
	/** 翅膀成长消耗*/
	public static final int CHIBANG_CZ = 148;
	/** 翅膀潜能消耗*/
	public static final int CHIBANG_QN = 149;
	/** 仙剑成长消耗*/
	public static final int XIANJIAN_CZ = 150;
	/** 仙剑潜能消耗*/
	public static final int XIANJIAN_QN = 151;
	/** 战甲成长消耗*/
	public static final int ZHANJIA_CZ = 152;
	/** 战甲潜能消耗*/
	public static final int ZHANJIA_QN = 153;
	/** 器灵成长消耗*/
	public static final int QILING_CZ = 154;
	/** 器灵潜能消耗*/
	public static final int QILING_QN = 155;
	/** 糖宝成长消耗*/
	public static final int TANGBAO_CZ = 156;
	/** 糖宝潜能消耗*/
	public static final int TANGBAO_QN = 157;
	/** 妖神霸体成长消耗*/
	public static final int YAOSHEN_BATI_CZ = 158;
	/** 妖神霸体潜能消耗*/
	public static final int YAOSHEN_BATI_QN = 159;
	/** 妖神魔纹成长消耗*/
	public static final int YAOSHEN_MOWEN_CZ = 160;
	/** 妖神魔纹潜能消耗*/
	public static final int YAOSHEN_MOWEN_QN = 161;
	/** 妖神魂魄成长消耗*/
	public static final int YAOSHEN_HUNPO_CZ = 162;
	/** 妖神魂魄潜能消耗*/
	public static final int YAOSHEN_HUNPO_QN = 163;
	/** 妖神魔印成长消耗*/
	public static final int YAOSHEN_MOYING_CZ = 164;
	/** 妖神魔印潜能消耗*/
	public static final int YAOSHEN_MOYING_QN = 165;
	/**糖宝心纹升级（消耗）*/
	public static final int TANGBAO_XINWEN_SJ = 166;
	/**探索宝藏(获得)*/
	public static final int TAN_SUO_BAO_ZANG = 167;
	/**探索宝藏王城领取(获得)*/
	public static final int TAN_SUO_BAO_ZANG_WANG = 168;
	/**糖宝心纹潜能丹(消耗)*/
	public static final int TANGBAO_XINWEN_QND_USE = 170;
	/**糖宝心纹成长丹(消耗)*/
	public static final int TANGBAO_XINWEN_CZD_USE = 171;
	/**转生 (消耗)**/
	public static final int GOODS_ZHUANSHENG = 169;
	/**领取妖神魔印排名礼包*/
	public static final int GOODS_GET_YSMYPM = 172;
	/**领取糖宝心纹排名礼包*/
	public static final int GOODS_GET_TBXWPM = 173;
	/**通天之路道具淬炼（消耗）*/
	public static final int TONGTIAN_ROAD_CUILIAN = 174;
	/**领取促销奖励（获得）*/
	public static final int CUXIAO_GET = 175;
	/**门派炼狱boss（获得）*/
	public static final int GUILD_BOSS_LIANYU = 176;
	/**越南版邀请好友（获得）*/
	public static final int YUENAN_YAOQING_HAOYOU = 177;
	/**热发布在线奖励领取（获得）*/
	public static final int RFB_ONLINE_REWARDS = 178;
	/**铸神强化(消耗)*/
	public static final int EQUIP_ZHUSHEN = 179;
	/**送花（消耗）*/
	public static final int FLOWER_SEND = 180;
	/**鲜花榜购买（所得）*/
	public static final int FLOWER_RANK_BUY = 181;
	/**蓝钻折扣商店（获得）*/
	public static final int TENCENT_SHANGDIAN = 182;
	/**腾讯管家新手登录（获得）*/
	public static final int TENCENT_GUANJIA_FIRST = 183;
	/**腾讯管家每日（获得）*/
	public static final int TENCENT_GUANJIA_DAY = 184;
	/**腾讯邀请好友 */
	public static final int TENCENT_YAOQING_HAOYOU = 185; 
	/**升级妖神附魔(消耗)*/
	public static final int  YAOSHEN_FUMO_SJ = 186;
	/**热发布团购活动（获得）**/
	public static final int RFB_TG = 187;
	/**画卷分解(消耗)*/
	public static final int HUAJUAN_FENJIE = 188;
	/**画卷合成*/
	public static final int HECHENG_HUAJUAN = 189;
	/**画卷合成(获得)*/
	public static final int HUAJUAN_HECHENG_GET = 190;
	/**热发布登陆奖励*/
	public static final int RFB_SEVEN_DAY_REWARD = 191;
	/**幻化激活*/
	public static final int HUANHUA_ACTIVATED = 192;
	/** 天羽成长消耗*/
	public static final int TIANYU_CZ = 193;
	/** 天羽潜能消耗*/
	public static final int TIANYU_QN = 194;
	/**天羽升阶(消耗)*/
	public static final int TY_SJ = 195;
	/**转职（消耗）*/
	public static final int ZHUANG_ZHI = 196;
	/**老玩家回归（获取）*/
	public static final int LAOWANJIA_HUIGUI = 197;
	/**神武装备升级（消耗）**/
	public static final int SW_ZB_SHENGJI = 198;
	/**神武装备升星（消耗）**/
	public static final int SW_ZB_XINGZHU = 199;
	/**群仙宴排名奖励**/
	public static final int QUNXIANYAN_JT = 200;
	/**群仙宴采集获得*/
	public static final int QUNXIANYAN_CJ = 201;
	/**五行激活*/
	public static final int WUXING_JH = 202;
	/**五行升级*/
	public static final int WUXING_SJ = 203;
	/**五行副本*/
	public static final int WUXING_FUBEN = 204;
    /**五行技能升级(消耗)*/
    public static final int CONSUME_WX_SKILL_UPLEVEL = 205;
    /**五行技能副本*/
    public static final int WUXING_SKILL_FUBEN = 206;
    /**充值轮盘(获取)*/
    public static final int GOODS_GET_LUNPAN_GET = 207;
    /**糖宝五行激活*/
    public static final int TB_WUXING_JH = 208;
    /**糖宝五行升级*/
    public static final int TB_WUXING_SJ = 209;
    /**糖宝五行技能升级(消耗)*/
    public static final int CONSUME_TB_WX_SKILL_UPLEVEL = 210;
    /**五行试炼副本*/
    public static final int WUXING_SHILIAN_FUBEN = 211;
    /**糖宝技能格位激活 (消耗)**/
	public static final int TANGBAO_SKILL_JIHUO = 212;
	/**心魔升级（消耗）**/
    public static final int XINMO_SHENGJI = 213;
    /**心魔突破（消耗）**/
    public static final int XINMO_TUPO = 214;
    /**心魔凝神（消耗）**/
    public static final int XINMO_NINGSHEN = 215;
    /** QQ游戏大厅新手礼包奖励（获得） */
    public static final int QQGAME_XINSHOU = 216;
    /** QQ游戏大厅等级礼包奖励（获得） */
    public static final int QQGAME_LEVEL = 217;
    /** QQ游戏大厅每日礼包奖励（获得） */
    public static final int QQGAME_EVERY = 218;
    /**心魔炼丹取出丹药（获得）**/
    public static final int XM_LIANDAN_TOOK_OUT = 219;
    /** QQ空间新手礼包奖励（获得） */
    public static final int QqQzone_XINSHOU = 220;
    /** QQ空间等级礼包奖励（获得） */
    public static final int QqQzone_LEVEL = 221;
    /** QQ空间每日礼包奖励（获得） */
    public static final int QqQzone_EVERY = 222;
    /**心魔魔神激活*/
    public static final int XM_MOSHEN_JH = 223;
    /**心魔魔神升级*/
    public static final int XM_MOSHEN_SHENGJI = 224;
    /**热发布-绝版礼包*/
    public static final int REFABU_JUEBAN = 225;
    /**心魔魔神技能升级(消耗)*/
    public static final int CONSUME_XM_SKILL_UPLEVEL = 226;
    /** 心魔深渊副本 */
    public static final int XINMO_SHENYUAN_FUBEN = 227;
    /** 心魔洗练 */
    public static final int XINMO_XILIAN = 228;
    /** 宠物技能升级 */
    public static final int CHONGWU_SKILL_SHENGJI = 229;
    /** 宠物附属装备强化 */
    public static final int CHONGWU_EQUIP_QIANGHUA = 230;
    /** 宠物附属装备强化 */
    public static final int CHONGWU_EQUIP_SHENGJIE = 231;
    /** 宠物附属装备提品 */
    public static final int CHONGWU_EQUIP_TIPIN = 232;
    /** 画卷2激活 */
    public static final int HUAJUAN2_ACTIVE = 233;
    /** 画卷2分解 */
    public static final int HUAJUAN2_FENJIE = 234;
    /** 画卷2升星 */
    public static final int HUAJUAN2_UPGRADE = 235;
    /** 灵火祝福镶嵌 */
    public static final int LINGHUO_BLESS_PUT_ON = 236;
    /** 跨服云宫之巅每日奖励*/  
    public static final int KF_YUNGONG_DAY_REWARD = 237;
    /** 购买(兑换)灵火之芯道具*/  
    public static final int MOGONGLIEYAN_EXCHANGE = 238;
    /** 升级云遥仙洞等级 **/  
    public static final int XIANQI_XIANDONG_UPLVL = 239;
    /** 升级仙器觉醒等级 **/  
    public static final int XIANQI_JUEXING_UPLVL = 240;
    /**时装进阶(消耗)*/ 
	public static final int GOODS_SHIZHUANG_JINJIE = 241;
    /** 至尊充值奖励 **/  
    public static final int EXTREME_RECHARGE_RECEIVE = 242;
    /** 仙缘飞化消耗 **/  
    public static final int CONSUME_XIANYUAN_FEIHUA = 243;
    /** 神器进阶消耗 **/  
    public static final int CONSUME_SHENQI_SJ = 244;
    /** 个人Boss（消耗） **/  
    public static final int PERSONAL_BOSS = 245;
    /** 个人Boss（获得） **/  
    public static final int PERNONAL_BOSS_GET = 246;
    /** 套装象位升星 **/  
    public static final int CONSUME_SUIT_XIANGWEI_SJ = 247;
    /**五行进阶丹使用（获得）*/
	public static final int GOODS_WUXING_GIFT = 248;
	/** (新)物品合成消耗 **/  
	public static final int CONSUME_NEW_HECHENG = 249;
	/** (新)物品合成获得 **/  
	public static final int GET_NEW_HECHENG = 250;
	/**新圣剑升阶(消耗)*/
	public static final int WQ_SJ = 251;
	/** 新圣剑潜能消耗*/
	public static final int WUQI_QN = 252;
	/** 新圣剑成长消耗*/
	public static final int WUQI_CZ = 253;
	/**单笔充值*/
	public static final int GOODS_GET_ONCEC = 254;
	/**成就领奖*/
	public static final int CHENGJIU_AWARD = 255;
	/**领取支线奖励（获得）*/
	public static final int ZHIXIAN_GET = 256;
	/**每日副本扫荡（获得）*/
	public static final int DAY_FUBEN_GET = 257;
	 /**限时礼包*/
    public static final int LIMIT_LIBAO = 258;
    /**修炼任务兑换*/
    public static final int XIULIAN_DUIHUAN = 259;
    /**修炼任务每日奖励*/
    public static final int XIULIAN_DAY = 260;
    /**炼金*/
	public static final int LJ = 261;
	/**星空宝藏*/
	public static final int GOODS_GET_XKBZ = 262;
	
	static {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (Field f : GoodsSource.class.getFields()) {
			try {
				String fName = f.getName();
				int value = f.getInt(null);
				if (map.get(value) != null)
					throw new RuntimeException(
							MessageFormat.format(
									"duplicate goodssource error:[{0},{1}]", fName,
									map.get(value)));
				else
					map.put(value, fName);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
