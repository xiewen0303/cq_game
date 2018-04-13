package com.junyou.cmd;

/**
 * 和客户端通信的游戏指令
 */
public class ClientCmdType {

    /**
     * 前后端通信的code必须在1~32767之间[负数指令预留给后端内部通信] 
     * 
     * 系统相关 0 ~ 99
     * 
     * 地图相关 100 ~ 199
     * 
     * GM功能 200 ~ 299
     * 
     * 系统邮件相关 300 ~ 399
     * 
     * 消息相关 400 ~ 499
     * 
     * 临时打开/关闭功能 500~509 
     * 
     * 技能Buff陷阱600~699
     * 
     * 场景同步基本动作700~749 
     * 
     * 死亡/复活750-799 
     * 
     * 主角属性变化800~899 
     * 
     * 背包900~950 
     */
    //-------------------系统相关 0 ~ 99--------------------------
    /**客户端接入游戏第一个指令**/
    public final static short IN = 0;
    /**创建角色**/
    public final static short CREATE_ROLE = 1;
    /**重新链接**/
//  public final static short CHONG_CONNECT = 2;
    /**服务端主动断开(需要状态)**/
    public final static short KICK_ROLE = 3;
    /**服务器端通知指定时间开始停机**/
    public final static short STOP_SERVER = 4;
    /**压缩的打包数据(建议150bytes以上数据使用)**/
    public final static short PACK_DATA = 5;
    /**不压缩的打包数据(建议150bytes以下数据使用)**/
    public final static short NO_PACK_DATA = 6;
    /**客户端通知进入游戏**/
    public final static short CLIENT_APPLY_ENTER_GAME = 7;
    /**通知客户端服务器数据初始完成**/
    public final static short SERVER_INIT_OK = 10;
    /**客服端通知可以发送100**/
    public final static short ENTER_STAGE_OK = 11;
    /**指定角色为线人**/
    public final static short SET_XIANREN = 20;
    
    /**30 推送主角快捷栏设置(如果有快捷栏设置)**/
    public final static short GET_SETTINGINFO = 30;
    /**31 客户端修改特定快捷栏**/
    public final static short CHANGE_QUICK_BAR = 31;
    /**40 反外挂处理**/
    public final static short PING_CMD = 40;
    /**50 客户端对时**/
    public final static short CLIENT_SYSTEM_TIME = 50;
    /**60 打印平台日志 (平台的链接按钮统计)*/
    public static final short PLATFORM_LOG_PRINT = 60;
    /**70 版署防沉迷显示取消*/
    public static final short BANSHU_FANGCHENMI_NOt_SHOW = 70;
    
    //-------------------地图相关 100 ~ 199--------------------------
    /**100 服务器通知客户端切换地图**/
    public final static short CHANGE_STAGE = 100;
    /**101 客户端请求过传送点**/
    public final static short APPLY_TELEPORT_POINT = 101;
    /**102 客户端请求线路信息**/
    public final static short APPLY_LINE_INFO = 102;
    /**103 客户端请求换线**/
    public final static short APPLY_CHANGE_LINE = 103;
    /**104 客户端请求飞鞋传送**/
    public final static short APPLY_FLY_SHOES = 104;
    /**106 回城**/
    public final static short APPLY_TOWN_PORTAL = 106;
    /**107 客户端请求剧情传送点**/
    public final static short APPLY_TELEPORT_PLOT_STAGE = 107;
    
    /**150 创建角色**/
    public static final short AOI_ROLES_ENTER = 150;
    /**151 创建怪物(只向客户端推送活着的怪物)**/
    public static final short AOI_MONSTERS_ENTER = 151;
    /**152 创建镖车**/
    public static final short AOI_BIAO_CHE = 152; 
    /**153 创建采集物**/
    public static final short AOI_COLLECT = 153; 
    /**154 创建糖宝**/
    public static final short AOI_PET = 154; 
    /**创建仙宫寻宝同模*/
    public static final short AOI_XIANGONG_SAME_CMD = 155;
    /**157 创建门派旗帜等*/
    public static final short AOI_TERRITORY_FLAG = 157;
    /**创建温泉同模*/
    public static final short AOI_WENQUAN_SAME_CMD = 158;
    /**创建宠物模型*/
    public static final short AOI_CHONGWU = 159;
    /**创建混沌同模*/
    public static final short AOI_HUNDUN_SAME_CMD = 160;
    /**170 移除单位（角色，怪物等等）**/
    public static final short AOI_ELEMENT_LEAVE = 170;
    /**171 移除主角以外所有单位**/
    public static final short AOI_ELEMENT_CLEAR = 171;
    /**180 创建物品**/
    public static final short AOI_GOODS_ENTER = 180;
    /**181 移除掉落物品**/
    public static final short AOI_GOODS_LEAVE = 181; 
    /**申请每日活动地图传送(传送过去参加每日活动)*/
    public static final short APPLY_GOTO_ACTIVE_MAP = 182;
    /**190 创建塔防NPC**/
    public static final short AOI_TAFANG_NPC_ENTER = 190;
    
    
    //------------------系统邮件相关 300 ~ 399--------------------------
    /**获取所有邮件*/
    public static final short GET_ALL_EMAIL = 300;
    /**有新邮件*/
    public static final short NEW_MESSAGE = 301;
    /**领取邮件附件*/
    public static final short RECIVE_EMAIL = 302;
    /**删除邮件*/
    public static final short DELETE_EMAIL = 303;
    /**读取邮件*/
    public static final short READ_EMAIL = 304;
    /**一键领取附件*/
    public static final short EMAIL_ONCE_RECIVE = 305;
    
    //-------------------消息相关 400 ~ 499--------------------------
    /**服务器通知客户端提示-在屏幕正中间提示(401)**/
    public final static short NOTIFY_CLIENT_ALERT = 401;
    /**服务器通知客户端提示-在顶部提示(402)**/
    public final static short NOTIFY_CLIENT_ALERT2 = 402;
    /**服务器通知客户端提示-在屏幕中间和顶部提示(403)**/
    public final static short NOTIFY_CLIENT_ALERT3 = 403;
    /**服务器通知客户端提示-在聊天框提示(404)**/
    public final static short NOTIFY_CLIENT_ALERT4 = 404;
    /**服务器通知客户端提示-在聊天框和屏幕正中间提示(405)**/
    public final static short NOTIFY_CLIENT_ALERT5 = 405;
    /**服务器通知客户端提示-在聊天框和顶部提示(406)**/
    public final static short NOTIFY_CLIENT_ALERT6 = 406;
    /**服务器通知客户端提示-在屏幕中间，屏幕顶部，聊天区域显示(407)**/
    public final static short NOTIFY_CLIENT_ALERT7 = 407;
    
    //-------------------------系统消息 ------------------------
    /**  系统公告  */
    public static final short GET_NOTICE = 440;
    /** 后端推送公告信息 */
    public static final short TUISONG_NOTICE = 441;
    /** 静默禁言 */
    public static final short JING_YAN_JM = 450;
    
    //----------------临时打开/关闭功能 500~509---------------------- 
    /**模块被动态关闭提示  */
    public final static  short MODEL_CLOSE = 500;
    /**开启被动态关闭的模块提示*/
    public final static  short MODEL_OPEN = 501;
    
    
    //-------------------GM调试指令 510~599----------------------
    /**设置金币*/
    public static final short GM_SET_GOLD = 510; 
    /**设置元宝*/
    public static final short GM_SET_YUANBAO = 511; 
    /**设置绑定元宝*/
    public static final short GM_SET_BANG_YUANBAO = 512; 
    /**增加经验*/
    public static final short GM_ADD_EXP = 513; 
    /**创建物品*/
    public static final short GM_CREATE_ITEM = 514; 
//  /**设置等级**/
//  public static final short GM_SET_LEVEL = 515;
    /**传送到指定地图**/
    public static final short GM_GOTO = 516;
    /**设置VIP等级**/
    public static final short GM_SET_VIP_LEVEL = 517;
    /**设置坐骑等级**/
    public static final short GM_SET_ZUOQI = 518;
    /**增加真气**/
    public static final short GM_ADD_ZHENQI = 519;
    /**修改任务**/
    public static final short GM_CHANGE_TASK = 520;
    /**修改等级**/
    public static final short GM_CHANGE_LEVEL = 521;
    /**设置怒气**/
    public static final short GM_SET_NUQI = 522;
    /**设置精华**/
    public static final short GM_SET_JINGHUA = 523;
    /**添加充值**/
    public static final short GM_ADD_CHARGE = 524;
    //TODO 需更换指令
    //-------------------技能Buff陷阱600~699--------------------------
    /**600客户端施放技能**/
    public static final short SKILL_FIRE = 600;
    /**601 基础验证未通过**/
    public static final short SKILL_FIRE_ERROR = 601;
    /**给某个单位增加一个BUFF*/
    public static final short ADD_BUFF = 630;
    /**消除BUFF*/
    public static final short XIAOCHU_BUFF = 631;
    /**触发BUFF*/
    public static final short CHUFA_BUFF = 632;
    
    
    //-----------------场景同步基本动作700~749--------------------------
    /**700 单位移动*/
    public static final short BEHAVIOR_MOVE = 700;
    /**701 角色移动失败*/
    public static final short BEHAVIOR_MOVE_ERROR = 701;
    /**702 单位瞬间移动*/
    public static final short BEHAVIOR_TELEPORT = 702;
    /**703 单位改变朝向*/
    public static final short BEHAVIOR_FACE_TO = 703;
    /**730 单位跳跃*/
    public static final short BEHAVIOR_JUMP_TO = 730;
    /**745 角色选中目标*/
    public static final short BEHAVIOR_CHOOSE_TARGET = 745;
    /**仙剑外显切换 */
    public static final short XIANJIAN_SHOW_REFRESH = 746;
    /**仙剑外显切换 */
    public static final short ZHANJIA_SHOW_REFRESH = 747;
    /**748 宠物移动同步换 */
    public static final short CHONGWU_MOVE_SYN = 748;
    /**749主角自己宠物过地图的时候，同步坐标 */
    public static final short CHONGWU_MOVE_SYN_4_ROLE = 749;
    //-------------------------采箱-----------------------------
    
    /**请求开始采箱*/
    public static final short  START_COLLECT_BOX = 704;
    /**请求采箱完成*/
    public static final short  END_COLLECT_BOX = 705;
    
    //-------------------死亡/复活750-799--------------------------
    /**750 主角死亡(单推送给主角)*/
    public static final short ROLE_DEAD = 750;
    /**751 普通复活*/
    public static final short TOWN_REVIVE = 751;
    /**752 复活药复活*/
    public static final short PROP_REVIVE = 752;
    /**753 爆出装备*/
    public static final short PK_DIAOLUO_GOODS = 753;
    /**754 推送装备触发复活*/
    public static final short ZHUANBEI_FUHOU = 754;
    /**760 战斗单位死亡*/
    public static final short FIGHTER_DEAD = 760;
    /**792 修改PK模式*/
    public static final short BATTLE_MODE = 792;
    
    //-----------------主角属性变化800~899--------------------------
    /**800 主角面板信息（有变化时推送主角）*/
    public static final short SELF_ATTRIBUTE = 800;
    /**801 角色hp变化(区域广播)*/
    public static final short HP_CHANGE = 801;
    /**802 角色真气变化(推送主角)*/
    public static final short ZQ_CHANGE = 802;
    /**803 角色移动速度发生变化（区域广播）*/
    public static final short SPEED_CHANGE = 803;
    /**802 角色跳闪值变化(推送主角)*/
    public static final short TIAO_SHAN = 804;
    
    
    /**820 角色经验变化(推送主角)*/
    public static final short EXP_CHANGE = 820;
    /**821 角色等级变化（区域广播）*/
    public static final short LEVEL_UPGRADE = 821;
    /**822 角色VIP经验变化*/
    public static final short VIP_EXP_CHANGE = 822;
    /**823 角色VIP等级变化*/
    public static final short VIP_LEVEL_CHANGE = 823;
    /**840 PK值变化*/
    public static final short PK_CHANGE = 840;
    /**841 灰名变化*/
    public static final short HUIMING_CHANGE = 841;
    
    /**850游戏币变化*/
    public static final short MONEY_CHANGE = 850;
    /**851元宝变化*/
    public static final short YUANBAO_CHANGE = 851;
    /**852绑定元宝变化*/
    public static final short BANG_YB_CHANGE = 852;
    /**853魂石变化*/
    public static final short HUNSHI_CHANGE = 853;
    /**854神器积分变化*/
    public static final short SHENQI_SCORE_CHANGE = 854;
    /**855荣誉值变化*/
    public static final short RONGYU_CHANGE = 855;
    /**856元宝充值到账*/
    public static final short YUANBAO_CHARGE = 856;
    /**858 角色修为值*/
    public static final short XIUWEI_CHARGE = 858;
    /**859 转生等级*/
    public static final short ZHUANSHENG_LEVEL = 859;
    /**870 拉取其他角色信息*/
    public static final short GET_OTHER_ATTRIBUTE_INFO = 870;
    /**871 请求多倍经验信息*/
    public static final short GET_ITEM_DUOBEI_INFO = 871;
    /**872 请求血包信息*/
    public static final short GET_ITEM_XUEBAO_INFO = 872;
    /**873 怒气变化*/
    public static final short NUQI_CHANGE = 873;
    /**874 门派掌门领地战勋章buff*/
    public static final short GUILD_TERRITORY_XUNZHANG = 874;
    /**875门派掌门皇城争霸赛勋章buff*/
    public static final short GUILD_HCZBS_XUNZHANG = 875;
    /**876创号时间*/
    public static final short CREATE_TIME = 876;
    //-------------------背包900~950----------------------
    /**打开背包**/
    public final static short GET_BAG_ITEMS =900;
    /**打开仓库**/
    public final static short GET_STORAGE_ITEMS=902;
    /**获取身上的物品信息 */
//  public final static short GET_BODY_ITEMS=7003;
    /**整理背包**/
    public final static short BAG_ZL=901;   
    /**整理仓库**/
    public final static short STORAGE_ZL=903;
    /**  移动物品 相同的物品Id*/
    public static final short MOVE_SLOT_ID = 904; 
    /**  移动物品 ID1相同,如两个不同的物品Id  但是一个是绑定  一个非绑定  */
    public static final short MOVE_SLOT_ID1=905;
    /** 拆分物品   */
    public static final short CHAIFEN_ITEM=906;
    /**  丢弃背包物品  */
    public static final short BAG_GIVE_UP = 907;
    /**  销毁(删除)背包物品  */
    public static final short BAG_DESTROY_ITEM = 909;
    /** 更新物品 */
    public static final short ITEM_COUNT_MODIFY = 910; 
    /**  背包新增物品 */
    public static final short PUT_IN_BAG = 911; 
    /**  使用物品 */ 
    public static final short USE_BAG_ITEM = 912;
    /**  使用单个物品 */
    public static final short USE_BAG_ONE_ITEM = 913;
    /**  使用物品失败结果返回 */
    public static final short USE_ITEM_FAIL = 914;
    /**915通知次数限制*/
    public static final short NOTITY_CSXZ = 915;
    /** 使用元宝激活背包格位 */
    public static final short BAG_SLOT_ACTIVATION = 916;
    /**使用元宝激活仓库格位*/
    public static final short STORAGE_SLOT_ACTIVATION = 917;
    /**出售物品*/
    public static final short SELL_ITEM = 918;
    /**物品进入冷却*/
    public static final short ITEM_ENTER_CD = 919;
    /** 客户端请求物品展示*/
    public static final short GOODS_ZHANSHI = 960;
    /** 领取/使用 需要展示物品的礼包返回 */
    public static final short GOODS_SHOW_ZHANSHI = 961;
    
    /**970 拾取一个物品**/
    public static final short GOODS_PICKUP = 970;
    /**971 拾取的物品不存在**/
    public static final short PICKUP_GOODS_IS_NOT_EXIST = 971; 
    
    /**990领取多选礼箱奖励**/
    public static final short LINGQU_LIXIANG = 990; 
    
    //------------------好友 ----------------------
    /**请求列表*/
    public static final short GET_MEMBERS = 1000;
    /**删除好友||仇人||黑名单*/
    public static final short DELETE_MEMBER = 1001;  
    /**查询玩家信息*/
    public static final short F_SELECT_ROLE = 1002;
    /**添加好友||黑名单*/
    public static final short ADD_MEMBER = 1003;
    /**添加仇人通知*/
    public static final Short ADD_ENEMY = 1005; 
    /**好友设置*/
    public static final short FRIEND_SET = 1006 ;
    /**批量删除好友*/
    public static final short DEL_FRIENDS = 1007 ;
    
    public static final short GET_FRIEND_SET = 1009;
    
    //---------------------------聊天------------------------
    
    /**世界频道*/
    public static final short CHAT_WORLD_MSG = 420;
    /**公会频道*/
    public static final short CHAT_GUILD_MSG = 421;
    /**队伍频道*/
    public static final short CHAT_TEAM_MSG = 422;
    
    /**玩家禁言状态*/
    public static final short JINYAN_STATE = 450;
    
    /**  私聊  */
    public static final short CHAT_PRIVATE = 1004; 
    /** 主动推给某人的私聊  */
    public static final short CHAT_PRIVATE_REPLY = 1008; 
    /** 私聊自动回复  */
    public static final short CHAT_PRIVATE_AUTO = 1010;
    
    
    // TODO（待修改具体数值）
    /**  系统信息  */
    public static final short SYSTEM_MSG = 6001;
    /**  喇叭信息  */
    public static final short CHAT_HORN_MSG = 6002;
    /**场景附近聊天*/
    public static final short CHAT_NEARBY = 6006;
    
    
    //------------------交易 ----------------------
    public static final short TRADE_LAUNCH = 1101;//申请交易
    public static final short TRADE_AGREE = 1102;//同意交易
    public static final short TRADE_REJECT = 1103; //拒绝交易
    public static final short TRADE_MODIFY_GOODS = 1104; //修改物品
    public static final short TRADE_MODIFY_MONEY = 1105;//修改金钱
    public static final short TRADE_CONFIRM1 = 1106;//锁定交易
    
    public static final short TRADE_CANCEL = 1107;//取消交易
    
    public static final short TRADE_CONFIRM2 = 1108 ;//两者都确定时 
    public static final short TRADE_CONFIRM3 = 1109  ;// 最终确认回馈
    
     
    
    public static final short TRADE_FINISH = 1110;//完成交易  
//  public static final short TRADE_SELF_AGREE = 1510;//用户同意交易反馈信息(仅发给交易点击同意按钮者)
//  public static final short TRADE_LAUNCH_SUCCESS = 1511;//交易申请成功
 
    
    //-----------------跨服战---------------------
    /**标记为跨服状态*/
    public final static short BIAOJI_KUAFU_STATE = 670;
    /**客户端申请进入跨服战**/
    public final static short CLIENT_KF_CMD = 30000;
    
    //-----------------装备强化---------------------
    /** 换装 */
    public static final short CHANGE_EQUIP = 950;
    /**物品发生变化(特指非绑定变绑定)*/
    public static final short EQUIP_GOODS_ID_CHANGE = 951;
    /**装备强化**/
    public static final short EQUIP_QH = 1201;
    /**装备一键强化**/
    public static final short EQUIP_AUTO_QH =1202; 
//  /**获取装备强化祝福**/
//  public static final short EQUIP_ZFZ =1001;
    /**1209打开界面请求熔炼值*/
    public static final short GET_RONGLIAN_VAL = 1209;
    /**熔炼装备**/
    public static final short EQUIP_RECYCLE = 1210;
    /**打开玄铁商店*/
    public static final short XUANTIE_STORE = 1211;
    /**玄铁兑换*/
    public static final short XUANTIE_DUIHUAN = 1212;
    /**装备升阶*/
    public static final short ZHUANGBEI_SHENGJI = 1213;
    /**装备提品*/
    public static final short ZHUANGBEI_TIPIN = 1214;
    /**附属装备提品*/
    public static final short FUSHU_ZHUANGBEI_TIPIN = 1222;
    /** 强化锤强化*/
    public static final short ZHUANGBEI_QIANGHUACHUI = 1215;
    /** 附属装备升阶*/
    public static final short FUSHU_EQUIP_SHENGJIE = 1221;
    /** 套装铸神*/
    public static final short TAO_ZHUANG_ZHU_SHEN = 3780;
    /** 套装象位信息*/
    public static final short TAO_ZHUANG_XIANG_WEI_INFO = 3781;
    /** 套装象位升阶*/
    public static final short TAO_ZHUANG_XIANG_WEI_UP = 3782;
    /**神武装备升阶*/
    public static final short SW_ZHUANBEI_SJ = 4015;
    /**神武装备升星*/
    public static final short SW_ZHUANBEI_SX = 4016;
    //-----------------------宠物附属装备-------------------//
    /** 991 宠物换装 */
    public static final short CHONGWU_EQUIP_CHANGE = 991;
    /** 1223 宠物附属装备强化 */
    public static final short CHONGWU_EQUIP_STRONG = 1223;
    /** 1224 宠物附属装备升阶 */
    public static final short CHONGWU_EQUIP_UPLEVEL = 1224;
    /** 1224 宠物附属装备提品 */
    public static final short CHONGWU_EQUIP_TIPIN = 1225;
    
    
    //---------------------新物品合成-------------------------
    /** 1226 请求特殊合成 */
    public static final short GOODS_HECHENG = 1226;
    
    //--------------------------------宝石----------------------
    /**宝石初始化面板*/
    public static final short JEWEL_INIT = 1216;
    /**宝石装上*/
    public static final short JEWEL_UP = 1217;
    /**宝石卸下*/
    public static final short JEWEL_DOWN = 1218;
    /**宝石打孔*/
    public static final short JEWEL_BURROW = 1219;
    /**宝石合成*/
    public static final short JEWEL_COMPOUND = 1220;
    
    
    
    //-------------------组队-----------------------
    /**创建队伍*/
    public static final short CREATE_TEAM = 1150;
    /**附近玩家*/
    public static final short TEAM_ARROUND_PLAYER = 1151;
    /**邀请玩家*/
    public static final short TEAM_YAOQING_PLAYER = 1152;
    /**离开队伍*/
    public static final short TEAM_LEAVE_TEAM = 1153;
    /**自动接收组队邀请*/
    public static final short TEAM_AUTO_AGREE_YAOQING = 1154;
    /**自动接收申请*/
    public static final short TEAM_AUTO_AGREE_APPLY = 1155;
    /**收到邀请*/
    public static final short TEAM_RECIVE_YAOQING = 1156;
    /**同意某人邀请*/
    public static final short TEAM_AGREE_YAPQING = 1157;
    /**我的队伍信息*/
    public static final short TEAM_MY_TEAM_INFO = 1158;
    /**附近队伍*/
    public static final short TEAM_ARROUND_TEAM = 1160;
    /**申请入队*/
    public static final short TEAM_APPLY_TEAM = 1161;
    /**任命队长*/
    public static final short TEAM_CHANGE_LEADER = 1162;
    /**同意组队申请*/
    public static final short TEAM_LEADER_AGREE = 1163;
    /**队长收到申请*/
    public static final short TEAM_LEADER_RECIVE = 1164;
    
    //-------------------------市场  ------------------------
    /** 市场物品 按类型分页查询 */ 
    public static final short PM_GOODS_LIST =1301;
    /** 购买 */
    public static final short PM_GOODS_GM = 1302;
    /** 自己拍卖的物品*/ 
    public static final short PM_MY_GOODS_LIST = 1303; 
    /** 吆喝*/
    public static final short PM_GOODS_YH = 1304;
    /**拍卖物品**/
    public static final short PM_GOODS_UP = 1305; 
    /**下架物品**/
    public static final short PM_GOODS_DOWN = 1306;
    /**一键下架*/
    public static final short PM_GOODS_DOWN_ALL = 1307;
    /**修改市场价格*/
    public static final short PM_GOODS_MODIFY = 1308;
    
    //------------------------竞技------------------------------------
    /**个人竞技信息*/
    public static final short GET_JINGJI_INFO = 1350;
    /**三仙信息*/
    public static final short GET_TOP_THREE_INFO = 1351;
    /**购买挑战次数*/
    public static final short BUY_TIAOZHAN_COUNT = 1352;
    /**秒挑战CD*/
    public static final short MIAO_TIAOZHAN_CD = 1353;
    /**鼓舞*/
    public static final short JINGJI_GUWU = 1354;
    /**挑战*/
    public static final short JINGJI_TIAOZHAN = 1355;
    /**领取奖励*/
    public static final short JINGJI_RECIVE_GIFT = 1356;
    /**兑换道具*/
    public static final short JINGJI_DUIHUAN_ITEM = 1357;
    /**战斗结果*/
    public static final short JINGJI_FIGHT_ANSWER = 1358;
    /**退出观看*/
    public static final short EXIT_JINGJI_WATCH = 1359;
    /**获取兑换信息*/
    public static final short GET_JINGJI_DUIHUAN_INFO = 1360;
    /**获取排名信息*/
    public static final short GET_JINGJI_PAIMING_INFO = 1361;
    /**获取挑战记录*/
    public static final short GET_JINGJI_TIAOZHAN_REPORT = 1362;
    /**是否领奖信息*/
    public static final short JINGJI_IS_RECIVE_GIFT = 1363;
    /**活动大厅有无可领奖励*/
    public static final short JINGJI_STATE_VALUE = 1364;
    /**竞技挑战异常*/
    public static final short JINGJI_FIGHT_ERROR = 1365;
    /**我要变强*/
    public static final short RANK_SOFT = 1366;
    /**刷新挑战成员*/
    public static final short REFRESH_TIAOZHAN = 1367;

    /**
     * 战力对比
     */
    public static final short FIGHT_VAL_COMPARE1 = 5460;
    public static final short FIGHT_VAL_COMPARE2 = 5461;
    
    public static final short FIGHT_VAL_COMPARE3 = 5462;
    public static final short FIGHT_VAL_COMPARE4 = 5463;

    //-------------------------打坐------------------------
    /**开始打坐*/
    public static final short DAZUO = 1370; 
    /**双休打坐*/
    public static final short DAZUO_SX = 1371;
    /**取消打坐*/
    public static final short DAZUO_CANCEL = 1372; 
    /**通知场景玩家打坐的变化信息*/
    public static final short NOTITY_DAZUO_STAGE = 1373; 
    /**自动双休*/
    public static final short DAZUO_AUTO_SX = 1374;
    /**打坐经验变化*/
    public static final short DAZUO_EXP_ADD = 1375;
    
    //-------------------门派1400~1499-----------------------
    /**拉取门派信息*/
    public static final short GET_MY_GUILD = 1400;
    /**创建门派*/
    public static final short CREATE_GUILD = 1401;
    /**申请成员列表*/
    public static final short GUILD_APPLY_LIST = 1402;
    /**拉取门派成员列表*/
    public static final short GUILD_MEMBER_LIST = 1403;
    /**拉取门派列表*/
    public static final short GUILD_LIST = 1404;
//  /**拉取跨服门派列表*/
//  public static final short GUILD_KUAFU_LIST = 1405;
    /**修改门派公告*/
    public static final short GUILD_CHANGE_NOTICE = 1406;
    /**申请加入某个门派*/
    public static final short GUILD_APPLY = 1407;
    /**审批进入申请*/
    public static final short GUILD_APPLY_SHENPI = 1408;
    /**把某些人踢出门派*/
    public static final short GUILD_KICK_MEMBER = 1409;
    /**退出门派*/
    public static final short GUILD_EXIT = 1410;
    /**任命某人职位*/
    public static final short GUILD_CHANGE_POSTION = 1411;
    /**门派新增成员*/
    public static final short GUILD_ADD_NEW_MEMBER = 1412;
    /**修改公会审批条件*/
    public static final short GUILD_CHANGE_ZHAOSHOU_TYPE = 1413;
    /**拉取门派日志*/
    public static final short GUILD_GET_LOGS = 1414;
    /**同步门派名称guid*/
    public static final short GUILD_SYN_GUILD_NAME = 1415;
    /**门派创建公告*/
    public static final short GUILD_CREATE_NOTICE = 1416;
    /**门派捐献信息*/
    public static final short GUILD_JUANXIAN_INFO = 1417;
    /**门派捐献*/
    public static final short GUILD_JUANXIAN = 1418;
    /**门派升级*/
    public static final short GUILD_LEVEL_UP = 1420;
    /**获取阁楼等级*/
    public static final short GET_GUILD_GELOU_LEVEL = 1421;
    /**升级门派阁楼*/
    public static final short GUILD_GELOU_UP = 1422;
    /**获取门派兑换信息*/
    public static final short GET_GUILD_DUIHUAN_INFO = 1423;
    /**门派兑换*/
    public static final short GUILD_DUIHUAN = 1424;
    /**门派有新申请*/
    public static final short GUILD_NEW_APPLY = 1425;
    /**弹劾掌门*/
    public static final short GUILD_IMPEACH_LEADER = 1426;
    
     
    
    
    //---------------------技能学习1500~1519---------------------
    /**角色技能信息 */
    public static final short SKILL_TAB_INFO = 1500;
    /**学习技能 */
    public static final short SKILL_LEARN = 1501;
    /**升级技能 */
    public static final short SKILL_LEVEL_UP = 1502;
    /**学习被动技能 */
    public static final short SKILL_BEIDONG_LEARN = 1503;
    /**升级被动技能 */
    public static final short SKILL_BEIDONG_LEVEL_UP = 1504;
    /**技能熟练度已满 */
    public static final short SKILL_SHULIAN_MAX = 1505;
    /**升级门派技能 */
    public static final short GUILD_SKILL_LEVEL_UP = 1506;
    /**升级妖神技能 */
    public static final short YAOSHEN_SKILL_LEVEL_UP = 1507;
    
    //-----------------------任务-------------------------
    /**推送主线完成任务情况*/
    public final static short SEND_TASK_STATE = 1550;
    /**完成并领取下一个任务*/
    public final static short FINISH_AND_RECIVE_TASK = 1551;
    /**更新任务状态*/
    public final static short REFRESH_TASK_STATE = 1552;
    /**更新任务进度*/
    public final static short REFRESH_TASK_PROGRESS = 1553;

    //-----------------------支线任务-------------------------
    /**推送主线完成任务情况*/
    public final static short SEND_BRANCHTASK_STATE = 1560;
    /**领取*/
    public final static short RECIVE_TASK = 1561;
    
    //-------------------------日常任务------------------------
    /**请求日常杀怪任务任务状态信息*/
    public static final short TASK_DAY_INFO = 1570;
    /**更新日常杀怪怪物击杀数量*/
    public static final short TASK_DAY_KILL = 1571;
    /**请求完成任务并接受下一个任务(客服端主动请求表示花费元宝直接完成)*/
    public static final short TASK_DAY_FINISH = 1572; 
    /**请求完成剩余的所有任务*/
    public static final short TASK_DAY_FINISH_ALL = 1573;
    
    //---------------------------押镖----------------------------
    /**1310 请求今日押镖任务信息**/
    public static final short GET_YABIAO_INFO = 1580;
    /**1311 请求接受押镖任务**/
    public static final short RECEIVE_YABIAO = 1581;
    /**1312 请求完成押镖任务**/
    public static final short FINISH_YABIAO = 1582;
    /**1313 请求角色飞至镖车处**/
    public static final short FLY_BIAOCHE = 1583;
    /**1314 通知劫镖者劫镖成功**/
    public static final short JIE_BIAOCHE = 1584;
    /**1315 通知押镖者镖车被劫**/
    public static final short YA_BIAOCHE = 1585;
    /**1315 刷新镖车**/
    public static final short REFRESH_BIAOCHE = 1586;
    /**1587 橙色镖车通告**/
    public static final short BIAOC_ORANGE_NOTICE = 1587;
    /**1588 押镖状态**/
    public static final short BIAOC_STATU = 1588;
    
    
    //-------------------------公会日常任务------------------------
    /**请求日常杀怪任务任务状态信息*/
    public static final short TASK_GUILD_INFO = 1690;
    /**更新日常杀怪怪物击杀数量*/
    public static final short TASK_GUILD_KILL = 1691;
    /**请求完成任务并接受下一个任务(客服端主动请求表示花费元宝直接完成)*/
    public static final short TASK_GUILD_FINISH = 1692; 
    /**请求完成剩余的所有任务*/
    public static final short TASK_GUILD_FINISH_ALL = 1693;
    
    //-------------------------BOSS日常任务------------------------
    /**请求日常杀怪任务任务状态信息*/
    public static final short TASK_BOSS_INFO = 1590;
    /**更新日常杀怪怪物击杀数量*/
    public static final short TASK_BOSS_KILL = 1591;
    /**请求完成任务并接受下一个任务(客服端主动请求表示花费元宝直接完成)*/
    public static final short TASK_BOSS_FINISH = 1592; 
    /**请求完成剩余的所有任务*/
    public static final short TASK_BOSS_FINISH_ALL = 1593;
    
    
    
    
    
    
    //-------------------------副本------------------------
    /** 请求日常副本任务挑战次数**/
    public final static short GET_FUBEN_COUNT = 1600;
    /** 请求进入日常副本任务**/
    public final static short ENTER_FUBEN = 1601;
    /** 通知客户端所需击杀怪物总数量**/
    public final static short KILL_MONSTER_COUNT = 1602;
    /** 请求完成日常副本任务(领取副本奖励)**/
    public final static short RECEIVE_FUBEN_REWARD = 1603;
    /** 请求离开日常副本任务**/
    public final static short LEAVE_FUBEN = 1604;
    /** 扫荡单次副本**/
    public final static short FUBEN_SAODANG_ONCE = 1605;
    /** 扫荡全部副本**/
    public final static short FUBEN_SAODANG_ALL = 1606;
    /** 扫荡完毕**/
    public final static short FUBEN_SAODANG_FINISH = 1607;
    /**获取守护副本信息*/
    public final static short FUBEN_SHOUHU_INFO = 1610;
    /**领取首次通关奖励*/
    public final static short FUBEN_SHOUHU_GIFT = 1611;
    /**重置守护副本*/
    public final static short FUBEN_SHOUHU_RESET = 1612;
    /**进入副本*/
    public final static short FUBEN_SHOUHU_ENTER = 1613;
    /**系统推送消息*/
    public final static short FUBEN_SHOUHU_SYSTEM_SEND = 1614;
    /**扫荡守护副本*/
    public final static short FUBEN_SHOUHU_SAODANG = 1615;
    /**退出副本*/
    public final static short FUBEN_SHOUHU_EXIT = 1616;
    /**单人副本有无可领奖励*/
    public final static short SHOUHU_STATE_VALUE = 1617;
    /**服务端推送南无月状态*/
    public final static short FUWU_TUISONG_NANWUYUE = 1618;
    /**服务端推送强制退出副本时间*/
    public final static short EXIT_FUBEN_TIME = 1619;
    /** 请求VIP副本任务挑战次数**/
    public final static short GET_VIP_FUBEN_COUNT = 1630;
    /** 进入VIP副本**/
    public final static short ENTER_VIP_FUBEN = 1631;
    /** 请求离开三生洗礼副本挑战**/
    public final static short LEAVE_VIP_FUBEN = 1632;
    /** 今日爬塔次数**/
    public final static short GET_PATA_INFO = 1640;
    /** 某层信息**/
    public final static short GET_PATA_CENG_INFO = 1641;
    /** 进入爬塔**/
    public final static short ENTER_PATA = 1642;
    /** 购买爬塔次数**/
    public final static short BUY_PATA_COUNT = 1643;
    /** 退出爬塔**/
    public final static short EXIT_PATA = 1644;
    /** 爬塔结果**/
    public final static short PATA_ANSWER = 1645;
    /** 蛮荒历练/云浮剑冢 副本信息**/
    public final static short JIANZHONG_FUBEN_INFO =  1650;
    /** 蛮荒历练/云浮剑冢   进入副本**/
    public final static short JIANZHONG_FUBEN_ENTER =  1651;
    /** 蛮荒历练/云浮剑冢   退出副本**/
    public final static short JIANZHONG_FUBEN_EXIT =  1652;
    /** 蛮荒历练/云浮剑冢  结果**/
    public final static short JIANZHONG_FUBEN_RESULT =  1653;
    
    /** 五行副本信息 **/
    public final static short GET_WUXING_FUBEN_INFO = 1654;
    /** 进入五行副本 **/
    public final static short ENTER_WUXING_FUBEN = 1655;
    /** 退出五行副本 **/
    public final static short EXIT_WUXING_FUBEN = 1656;
    /** 购买五行副本挑战次数 **/
    public final static short BUY_WUXING_FUBEN_COUNT = 1657;
    /** 领取五行副本奖励 **/
    public final static short RECEIVE_WUXING_FUBEN = 1658;
    /** 推送五行副本通关成功 **/
    public final static short FINISH_WUXING_FUBEN = 1659;

    /** 五行技能副本信息 **/
    public final static short WUXING_SKILL_FUBEN_INFO = 1660;
    /** 进入五行技能副本 **/
    public final static short WUXING_SKILL_FUBEN_ENTER = 1661;
    /** 退出五行技能副本 **/
    public final static short WUXING_SKILL_FUBEN_EXIT = 1662;
    /** 领取五行技能副本奖励 **/
    public final static short WUXING_SKILL_FUBEN_RECEIVE = 1663;
    /** 扫荡五行技能副本 **/
    public final static short WUXING_SKILL_FUBEN_CLEAR = 1664;
    /** 进入下层五行技能副本 **/
    public final static short WUXING_SKILL_FUBEN_NEXT = 1665;
    /** 五行技能副本通关完成 **/
    public final static short WUXING_SKILL_FUBEN_FINISH = 1666;
    
    /** 进入五行试炼副本 **/
    public final static short WUXING_SHILIAN_FUBEN_ENTER = 1670;
    /** 退出五行试炼副本 **/
    public final static short WUXING_SHILIAN_FUBEN_EXIT = 1671;
    /** 推送五行试炼副本奖励信息 **/
    public final static short WUXING_SHILIAN_FUBEN_RECEIVE = 1672;
    /** 五行试炼副本信息 **/
    public final static short WUXING_SHILIAN_FUBEN_INFO = 1673;
	
	
	//-------------------------多人副本2300------------------------
	/** 请求幻境历练副本信息*/
	public final static short MORE_FUBEN_INFO = 2300;
	/** 请求幻境历练副本队伍信息*/
	public final static short MORE_FUBEN_INIT_TEAM = 2301;
	/** 请求创建幻境历练副本队伍**/
	public final static short MORE_FUBEN_TEAM_CREATE = 2302;
	/** 请求加入幻境历练副本队伍**/
	public final static short MORE_FUBEN_TEAM_JOIN = 2303;
	/** 幻境历练副本队伍队长变化**/
	public final static short MORE_FUBEN_TEAM_CHANGE = 2304;
	/** 幻境历练副本队伍准备状态变化*/
	public final static short MORE_FUBEN_PREPARE = 2305;
	/** 谁谁加入幻境历练副本队伍(其他成员)*/
	public final static short MORE_FUBEN_TEAM_ENTER = 2306;
	/** 谁谁离开幻境历练副本队伍*/
	public final static short MORE_FUBEN_LEAVE_SENDER = 2307;
	/**  队长把谁踢出幻境历练副本队伍*/
	public final static short MORE_FUBEN_TEAM_REMOVE = 2308;
	/**  队长更改幻境历练副本队伍进入需求战力*/
	public final static short MORE_FUBEN_CHANGE_STRENGTH = 2309;
	/** 队长更改幻境历练副本队伍是否满员自动挑战*/
	public final static short MORE_FUBEN_SET_AUTO = 2310;
	/** 退出幻境历练副本队伍**/
	public final static short MORE_FUBEN_TEAM_LEAVE = 2311;
	/**请求进入幻境历练副本*/
	public final static short ENTER_MORE_FUBEN = 2312;
	/** （副本预进入请求）通知队伍里所有的人在指定时间戳后进入副本*/
	public final static short MORE_FUBEN_PRE_START = 2313;
	/** 当前进入的幻境历练副本需要击杀怪物数量*/
	//public final static short MORE_FUBEN_MONSTER_COUNT = 2313;
	/** 多人副本领取奖励*/
	public final static short MORE_FUBEN_FIRST_REWARD = 2314;
	/** 请求退出幻境历练副本*/
	public final static short MORE_FUBEN_EXIT = 2315;
	/** 邀请别人加入队伍 */
	public final static short MORE_FUBEN_YAOQING_OTHER = 2316;
	/** 多人副本伤害输出(客户端实现)**/
	//public final static short MORE_FUBEN_SHANGHAI_CONSOLE = 2317;
	/** 快速加入队伍 */
	public final static short MORE_FUBEN_QUICK_JOIN = 2317;
	//-------------------------坐骑------------------------
	/** 坐骑阶级信息*/
	public static final short ZUOQI_JJ_LEVE = 1701;
	/** 角色坐骑面板信息*/
	public static final short ZUOQI_SHOW = 1702;
	/** 角色坐骑正常进阶*/
	public static final short ZUOQI_JJ_UP = 1703;
	/** 角色坐骑自动进阶*/
	public static final short ZUOQI_AUTO_UP = 1704;
	/**上坐骑  **/
	public static final short ZUOQI_UP = 720;
	/**下坐骑  **/
	public static final short ZUOQI_DOWN = 721;
	/** 角色坐骑形象切换 */
	public static final short ZUOQI_UPDATE_SHOW = 1705;
	/** 已经使用了御剑潜能丹数量 */
	public static final short ZUOQI_QND_NUM = 1707;
	/** 已经使用了御剑成长丹数量 */
	public static final short ZUOQI_CZD_NUM = 1708;
	/** 推送御剑属性变化 */
	public static final short ZUOQI_ATTR_CHANGE = 1709;
	/** 1710 御剑，翅膀，仙剑，战甲成长丹等通用指令 */
	public static final short WANNEG_CZD = 1710;
	/** 1711 御剑，翅膀，仙剑，战甲潜能丹等通用指令 */
	public static final short WANNEG_QND = 1711;
	//----------------------挂机设置--------------------------
	/**获取挂机设置*/
	public static final short GET_GUAJI_SET = 2000;
	/**保存挂机设置*/
	public static final short SAVE_GUAJI_SET = 2001;
	
	//----------------------领取微端登录奖励---------------------------
	/**请求领取微端奖励*/
	public static final short LQ_WEIDUAN = 2210;
	/**微端奖励是否已领取推送*/
	public static final short ISLQ_WEIDUAN = 2211;


    /**请求领取微端奖励*/
    public static final short LQ_WEIDUAN2 = 5452;
    /**微端奖励是否已领取推送*/
    public static final short ISLQ_WEIDUAN2 = 5451;
	
	
	//---------------------福利大厅  --------------------
	//签到1950-1954
	/** 签到初始化 */ 
	public static final short ASSIGN_INIT =1950;
	/** 签到*/
	public static final short ASSIGN = 1951;
	/** 一键补签 */ 
	public static final short ASSIGN_RETROACTIVE = 1952; 
	/** 领取累计签到奖励*/
	public static final short ASSIGN_TOTAL = 1953;
	//在线奖励1955-1956
	/** 在线奖励初始化 */ 
	public static final short ONLINE_REWARD_INIT =1955;
	/** 在线领奖*/
	public static final short ONLINE_REWARD = 1956;
	//礼包卡兑换1959
	/** 礼包卡兑换 */ 
	public static final short GIFT_CARD = 1959;
	//福利大厅有无可领奖励
	/** 1966 福利大厅有无可领奖励*/ 
	public static final short FLDT_HAVE_JL = 1966;
	
	//七日奖励1970-1971
	/** 七日奖励初始化 */ 
	public static final short SEVEN_REWARD_INIT = 1970;
	/** 七日领奖*/ 
	public static final short SEVEN_REWARD = 1971;
	/** 合服七日奖励初始化 */ 
	public static final short HEFU_SEVEN_REWARD_INIT = 2911;
	/** 合服七日领奖*/ 
	public static final short HEFU_SEVEN_REWARD = 2912;
	
	//---------------------离线经验1957-1958  --------------------
	/** 离线经验初始化 */ 
	public static final short OFFLINE_EXP_INIT =1957;
	/** 离线经验领奖*/ 
	public static final short OFFLINE_EXP_REWARD =1958;
	
	//---------------------资源找回1970-1979  --------------------
	/** 资源找回初始化 */ 
	public static final short RESOURCE_BACK_INIT =1975;
	/** 具体资源找回类型奖励 */ 
	public static final short RESOURCE_BACK_TYPE_REWARD =1976;
	/** 一键至尊奖励 */ 
	public static final short RESOURCE_BACK_KEY_EXTREME =1977;
	
	//-------------------------商城---------------------------------
	/**拉取热发布商城信息（2050）*/
	public static final short GET_MALL_DATA = 2050;
	/**商城购买（2051）*/
	public static final short MALL_BUY = 2051; 
	
	//---------------------------寻宝 ----------------------------
	/**打开寻宝界面*/
	public static final short GET_XB_INFO = 1900;
	/**寻宝*/
	public static final short XUBAO = 1901;
	/**领取单个物品*/
	public static final short TAKE_OUT_ONE = 1902;
	/**一键领取*/
	public static final short TAKE_OUT_MANY = 1903;
	/**寻宝背包界面*/
	public static final short GET_XB_DATA = 1904;
	/**请求寻宝积分商城页签*/
	public static final short GET_XB_JIFEN_FZ_INFO = 1905;
	/**获取寻宝积分信息*/ 
	public static final short GET_XB_JIFEN_INFO = 1906;
	/**寻宝积分推送 */ 
	public static final short XB_JIFEN = 1907;
	/**寻宝兑换(消耗积分,及除装备外的道具) */ 
	public static final short XB_CONVERT = 1908;
    /**寻宝系统广播*/ 
    public static final short XUNBAO_SYSTEM_NOTIFY = 1909;
    
    //------------------------定时活动------------------------------
    /**请求题目信息*/
    public static final short GET_TIMU=2070;
    /**提交答案选项*/
    public static final short SUBMIT_OPT=2071;
    /**积分排名*/
    public static final short SHOW_RANK=2072;
    /**使用排除符*/
    public static final short USE_EXCLUDE_CARD=2073;
    /**直接领答题奖*/
    public static final short PROMPTLY_AWARD=2074;
    /**玩家当前活动数据*/
    public static final short CURRENT_ITEM=2075; 
    
    //---------------------------阵营战------------------------
    /**2080请求进入阵营战地图**/
    public final static short ENTER_CAMP_WAR = 2080;
    /**2081请求退出阵营战地图**/
    public final static short LEVEL_CAMP_WAR = 2081;
    /**2082获得经验推送**/
    public final static short NOTICE_EXP = 2082;
    /**2083所属阵营推送**/
    public final static short NOTICE_CAMP = 2083;
    /**2084请求排名信息**/
    public final static short GET_RANK = 2084;
    /**2085活动结束排名推送**/
    public final static short GET_CAMP_RANK = 2085;
    /**2086阵营战随机复活**/
    public final static short CAMP_FUHUO = 2086;
    /**2087阵营战雕像摧毁推送**/
    public final static short CMAP_MONSTER_DEAD = 2087;
    /**2088阵营战雕像刷新时间**/
    public final static short CMAP_MONSTER_REFRESH = 2088;
    //-------------------------探宝-------------------------------
    /**进入探宝*/
    public final static short ENTER_TANBAO = 2090;
    /**退出探宝*/
    public final static short EXIT_TANBAO = 2091;
    /**获得探宝金币*/
    public final static short GET_TANBAO_GOLD = 2092;
    /**获得探宝经验*/
    public final static short GET_TANBAO_EXP = 2093;
    /**发送前五*/
    public final static short SEND_TANBAO_TOP_FIVE = 2094;
    /**自己探宝信息*/
    public final static short SELF_TANBAO_INFO = 2095;
    /**请求排行信息*/
    public final static short GET_RANK_INFO = 2096;
    /**活动结束推送奖励*/
    public final static short TANBAO_STOP_GIFT = 2097;
    /**复活点复活*/
    public final static short FUHUO_POINT_FUHUO = 2098;
    /**探宝采集开始*/
    public final static short TANBAO_COLLECT_START = 2099;
    /**探宝采集停止*/
    public final static short TANBAO_COLLECT_STOP = 2100;
    
    //--------------------------领地战--------------------------------
    /**请求领地战信息(2105)**/
    public final static short TERRITORY_GET_INFO = 2105;
    /**请求公会会长所在地图(2106)**/
    public final static short TERRITORY_GET_GUILD_LEADER_MAP_ID = 2106;
    /**请求领取占领地图奖励(2107)**/
    public final static short TERRITORY_GET_REWARD = 2107;
    /**2108领地战夺旗帜开始**/
    public final static short TERRITORY_FETCH_FLAG_BEGIN = 2108;
    /**2109领地战夺旗帜结束**/
    public final static short TERRITORY_FETCH_FLAG_END = 2109;
    /**复活**/
    public final static short TERRITORY_FUHUO = 2110;
    /**获得经验,真气推送**/
    public final static short TERRITORY_NOTICE_EXP_ZHENQI = 2111;
    /**2112 领地战期间推送给地图上的玩家旗子的实时位置**/
    public final static short TERRITORY_SYN_FLAG = 2112;
    /**2113 领城战某地图旗帜被某门派占领**/
    public final static short TERRITORY_FLAG_CHANGE = 2113;
    /**2114 某个地图领地战结束**/
    public final static short TERRITORY_END = 2114;
     
    //--------------------温泉----------------------------
    /**进入温泉*/
    public final static short ENTER_WENQUAN = 2116;
    /**退出温泉*/
    public final static short EXIT_WENQUAN = 2121;
    /**温泉经验，真气变化*/
    public final static short WENQUAN_EXP_ZHENQI = 2122;
    /**请求温泉信息*/
    public static final short GET_WENQUAN_INFO = 2117;
    /**进入高倍区*/
    public static final short GOTO_HIGH_AREA = 2124;
    /**请求排名*/
    public static final short GET_WENQUAN_RANK = 2118;
    /**聚灵*/
    public static final short WENQUAN_JU_LIN = 2123;
    /**2119 请求丢肥皂*/
    public static final short WENQUAN_PLAY_1 = 2119;
    /**2120 请求搓背*/
    public static final short WENQUAN_PLAY_2 = 2120;
    /**2125 请求面板排行信息*/
    public static final short WENQUAN_RANK_SYN = 2125;
    /**2126*/
    public static final short WENQUAN_PLAY_NOTICE = 2126;
    
    //-------------------------勇闯七杀----------------------------
    /**请求进入勇闯七杀*/
    public static final short ENTER_QISHA = 2146;
    /**请求退出勇闯七杀*/
    public static final short EXIT_QISHA = 2147;
    /**服务器推送伤害榜*/
    public static final short QISHA_BOSS_DAMAGE_RANK = 2148;
    /**地图内获得经验真气*/
    public static final short QISHA_GET_EXP_ZHENQI = 2149;
    /**地图内BOSS状态变化*/
    public static final short QISHA_BOSS_STATE_CHANGE = 2150;
    /**关闭伤害榜*/
    public static final short QISHA_BOSS_RANK_CLOSE = 2151;
    
    
    //--------------------------VIP面板--------------------------------
    /**请求VIP奖励状态*/
    public static final short ASK_VIP_GIFT_STATE = 2200;
    /**请求领取VIP周奖励*/
    public static final short RECIVE_VIP_WEEK_GIFT = 2201;
    /**请求领取VIP等级奖励*/
    public static final short RECIVE_VIP_LEVEL_GIFT = 2202;
    
    //-------------------------野外boss---------------------------------
    /**请求野外boss活动信息*/
    public static final short YW_BOSS_SHOW = 2400;
    /**请求野外boss活动排行榜*/
    public static final short YW_BOSS_RANK = 2401;
    /**通知前端关闭BOSS伤害排行*/
    public static final short YW_BOSS_CLOSE_HURT_RANK = 2402;
 
    //-------------------------翅膀 ---------------------------------
    /** 翅膀阶级信息(登陆打包发送)*/
    public static final short CHIBANG_JJ_LEVE = 2500;
    /**翅膀面板信息*/
    public static final short CHIBANG_SHOW = 2501;
    /**升阶*/
    public static final short CHIBANG_JJ_UP = 2502;
    /**自动升阶*/
    public static final short CHIBANG_AUTO_UP = 2503;
    
//  /**下翅膀*/
//  public static final short CHIBANG_DOWN = 9993;
//  /**上翅膀*/
//  public static final short CHIBANG_UP = 9994;
    
    /**幻化翅膀 TODO*/
    public static final short CHIBANG_UPDATE_SHOW = 2504;
    /** 已经使用了翅膀潜能丹数量 */
    public static final short CHIBANG_QND_NUM = 2505;
    /** 已经使用了翅膀成长丹数量 */
    public static final short CHIBANG_CZD_NUM = 2506;
    /** 推送翅膀属性变化 */
    public static final short CHIBANG_ATTR_CHANGE = 2507;
    
    //-------------------------器灵 ---------------------------------
    /** 器灵阶级信息(登陆打包发送)*/
    public static final short QILING_JJ_LEVE = 3400;
    /**器灵面板信息*/
    public static final short QILING_SHOW = 3401;
    /**升阶*/
    public static final short QILING_JJ_UP = 3402; 
    /**自动升阶*/
    public static final short QILING_JJ_UP_AUTO = 3403; 

    /** 推送器灵属性变化 */
    public static final short QILING_ATTR_CHANGE = 3404 ;
    /** 已经使用了器灵潜能丹数量 */
    public static final short QILING_QND_NUM = 3405;
    /** 已经使用了器灵成长丹数量 */
    public static final short QILING_CZD_NUM = 3406;
    /**3407 器灵形象切换*/
    public static final short QILING_UPDATE_SHOW = 3407;
    /**3408 同步场景中角色的器灵外显*/
    public static final short QILING_SHOW_REFRESH = 3408;
    
    //-------------------------天羽 ---------------------------------
    /** 器灵阶级信息(登陆打包发送)*/
    public static final short TIANYU_JJ_LEVE = 3430;
    /**器灵面板信息*/
    public static final short TIANYU_SHOW = 3431;
    /**升阶*/
    public static final short TIANYU_JJ_UP = 3432; 
    /**自动升阶*/
    public static final short TIANYU_JJ_UP_AUTO = 3433; 
    
    /** 推送器灵属性变化 */
    public static final short TIANYU_ATTR_CHANGE = 3434 ;
    /** 已经使用了器灵潜能丹数量 */
    public static final short TIANYU_QND_NUM = 3435;
    /** 已经使用了器灵成长丹数量 */
    public static final short TIANYU_CZD_NUM = 3436;
    /**3407 器灵形象切换*/
    public static final short TIANYU_UPDATE_SHOW = 3437;
    /**3408 同步场景中角色的器灵外显*/
    public static final short TIANYU_SHOW_REFRESH = 3438;
    
    
    
    //------------------------糖宝-----------------------------------
    /**糖宝激活进度*/
    public static final short TANGBAO_ACTIVE_PROGRESS = 2540;
    /**请求糖宝属性*/
    public static final short TANGBAO_ATTRIBUTE = 2541;
    /**一键使用血菩提*/
    public static final short TANGBAO_ONE_KEY_EAT = 2542;
    /**糖宝死亡*/
    public static final short TANGBAO_DEAD = 2543;
    /**糖宝血量变化*/
    public static final short TANGBAO_MAX_HP = 2544;
    /**资质丹已使用数量*/
    public static final short TANGBAO_ZZD_COUNT = 2545;
    /**眉心血已使用数量*/
    public static final short TANGBAO_MXZX_COUNT = 2546;
    /**糖宝主人属性变化*/
    public static final short TANGBAO_OWNER_ATTRIBUTE_CHANGE = 2547;
    /**糖宝属性变化*/
    public static final short TANGBAO_ATTRIBUTE_CHANGE = 2548;
    /**请求糖宝技能格位信息*/
    public static final short GET_TANGBAO_SKILL_GEWEI_INFO = 2549;
    /**请求糖宝技能格位开启*/
    public static final short GET_TANGBAO_SKILL_GEWEI_KAIKONG = 2550;
    /**学习糖宝被动技能 */
    public static final short SKILL_TB_BEIDONG_LEARN = 2551;
    /**升级糖宝被动技能 */
    public static final short SKILL_TB_BEIDONG_LEVEL_UP = 2552;
    /**请求装备糖宝技能 */
    public static final short SKILL_TB_BEIDONG_ZHUANGBEI = 2553;
    
    //--------------------------------排行榜----------------------
    
    /**请求魁首榜数据信息*/
    public static final short KUISHOU_INFO = 2600;
    /**请求榜的时间戳和主角自己在这个榜的排名*/
    public static final short RANK_INFO = 2601;
    /**请求等级榜*/
    public static final short LEVEL_RANK = 2602;
    /**请求战力榜*/
    public static final short FIGHTING_RANK = 2603;
    /**请求坐骑榜*/
    public static final short ZUOQI_RANK = 2604;
    /**请求翅膀榜*/
    public static final short CHIBANG_RANK = 2605;
    /**请求仙剑榜*/
    public static final short XIANJIAN_RANK = 2606;
    /**请求战甲榜*/
    public static final short ZHANJIA_RANK = 2607;
    /**请求妖神榜*/
    public static final short YAOSHEN_RANK = 2608;
    /**魔纹排行榜*/
    public static final short YAOSHEN_MOWEN_RANK = 2609;
    /**器灵排行榜*/
    public static final short QILING_RANK = 2610;
    /**魂魄排行榜*/
    public static final short YAOSHEN_HUNPO_RANK = 2611;
    /**魔印排行榜*/
    public static final short YAOSHEN_MOYIN_RANK = 2612;
    /**糖宝心纹榜*/
    public static final short TANGBAO_XINWEN_RANK = 2613;
    /**圣剑榜*/
    public static final short SHENGJIAN_RANK = 2614;
     
    //-------------------------战甲 ---------------------------------
    /**糖宝正在使用的战甲id*/
    public static final short ZHANJIA_SHOW_ID= 2660;
    /**仙剑面板信息*/
    public static final short ZHAN_JIA_SHOW = 2661;
    /**仙剑正常升阶*/
    public static final short ZHANJIA_JJ_UP_COMMON = 2662;
    /**仙剑自动升阶*/
    public static final short ZHANJIA_JJ_UP_AUTO = 2663;
    /** 推送仙剑属性变化 */
    public static final short ZHANJIA_ATTR_CHANGE = 2664;
    /** 已经使用了仙剑潜能丹数量 */
    public static final short ZHANJIA_QND_NUM = 2665;
    /** 已经使用了仙剑成长丹数量 */
    public static final short ZHANJIA_CZD_NUM = 2666;
    /**幻化仙剑 */
    public static final short ZHANJIA_UPDATE_SHOW = 2667;
    //-------------------------仙剑 ---------------------------------
    /**糖宝正在使用的武器id*/
    public static final short XIANJIAN_SHOW_ID= 2701;
    /**仙剑面板信息*/
    public static final short XIANJIAN_SHOW = 2702;
    /**仙剑正常升阶*/
    public static final short XIANJIAN_JJ_UP_COMMON = 2703;
    /**仙剑自动升阶*/
    public static final short XIANJIAN_JJ_UP_AUTO = 2704;
    /** 推送仙剑属性变化 */
    public static final short XIANJIAN_ATTR_CHANGE = 2705;
    /** 已经使用了仙剑潜能丹数量 */
    public static final short XIANJIAN_QND_NUM = 2706;
    /** 已经使用了仙剑成长丹数量 */
    public static final short XIANJIAN_CZD_NUM = 2707;
    /**幻化仙剑 */
    public static final short XIANJIAN_UPDATE_SHOW = 2708;
    /**2709 请求领取糖宝武器返利**/
    public static final short XIANJIAN_FANLI = 2709;
    //--------------------------------神器----------------------
    /**请求神器信息*/
    public static final short GET_SHENQI_INFO = 2751;
    /**激活神器*/
    public static final short ACTIVATE_SHENQI = 2752;
    /**佩戴神器*/
    public static final short WEAR_SHENQI = 2753;
    /**通知其他人神器刷新*/
    public static final short REFRESH_SHENQI = 2754;
    /**获取累计签到天数*/
    public static final short GET_TOTAL_ASSIGN_DAYS = 2755; 
    /**通知前端某玩家的神器进行了攻击*/
    public static final short NOTICE_SHENQI_ATTACK = 2756;  
    /**获取累计在线*/
    public static final short GET_TOTAL_ONLINE_TIME = 2757; 
    /**是否已激活可购买神器*/
    public static final short IS_ACITVE_BUY_SHENQI = 2770;  
    /**购买开服神器*/
    public static final short BUY_KAIFU_SHENQI = 2771;
    /**洗练神器*/
    public static final short SHENQI_XILIAN = 2772;
    /**保存属性*/
    public static final short SHENQI_XILIAN_SAVE = 2773;
    /**放弃保存*/
    public static final short SHENQI_XILIAN_CANCEL = 2774;  
    /**神器进阶信息*/
    public static final short GET_SHENQI_JINJIE_INFO = 2775;  
    /**神器正常进阶*/
    public static final short SHENQI_UPGRADE = 2776; 
    /** 推送神器格位信息*/
    public static final short SHENQI_EQUIP_GEWEI_CHANGE = 2778; 
    /**神器穿/脱/改变装备*/
    public static final short SHENQI_UP_EQUIP = 2779; 
     
    
    //------------------------投资计划----------------------
    /**2801 拉取投资计划数据**/
    public static final short GET_TOUZI_DATA = 2801;
    /**2802 投资某计划**/
    public static final short TOUZI_PLAN = 2802;
    /**2803 领取某计划奖励**/
    public static final short RECEVIE_TOUZI = 2803;
    
    //---------------------灵火-------------------------
    /**获取已激活的灵火ID*/
    public static final short GET_LINGHUO_INFO = 2850;
    /**激活某个灵火*/
    public static final short JIHUO_LINGHUO = 2851;
    
    //--------------------周天灵境----------------------------
    /**请求周天灵境重数和当前激活状态*/
    public static final short GET_LINGJING_INFO = 2860;
    /**请求激活指定周天灵境的类型*/
    public static final short ACTIVE_LINGJING = 2861;
    /**请求突破周天灵境重数*/
    public static final short TUPO_LINGJING = 2862;
    
    //--------------------------------活跃度----------------------
    /**请求活跃度信息*/
    public static final short GET_HUOYUEDU_INFO = 2870;
    /**领取奖励*/
    public static final short GET_HUOYUEDU_AWARD = 2871;
    /**测试完成任务指令*/
    public static final short HUOYUEDU_TEST_ACTIVITY = 2872;
    
    //--------------------称号----------------------------
    /**获得称号信息*/
    public final static short GET_CHENGHAO_INFO = 2880;
    /**激活称号*/
    public final static short ACTIVATE_CHENGHAO = 2881;
    /**请求启用(顶在头上)头衔称号(2882)*/
    public final static short WEAR_CHENGHAO = 2882;
    
    /**请求取消启用（不再顶头上）头衔称号(2883)*/
    public final static short UNWEAR_CHENGHAO = 2883;
    /**请求全服已有高级定制头衔列表(做展示宣传)(2884)*/
    public final static short GET_ALL_DINGZHI_CHENGHAO = 2884;
    
    /**称号激活*/
    public static final short REFRESH_CHENGHAO_ACTIVATE = 2885;
    /**称号过期删除推送**/
    public static final short REFRESH_CHENGHAO_DELETE= 2889;
    /**通知其他人称号刷新*/
    public static final short REFRESH_CHENGHAO = 2890;
    
    //*******************成就**************************
    /**2901 成就总信息*/
    public static final short CHENGJIU_INFO = 2901;
    /**2902某成就类型信息*/
    public static final short CHENGJIU_TYPE_INFO = 2902;
    /**2903 某成就达成*/
    public static final short CHENGJIU_TUISONG = 2903;
    /**2904 领奖成就*/
    public static final short LINGJIANG = 2904;
    
    //--------------------------------妖神系统---------------------
    /**2925 获取妖神信息*/
    public static final short GET_YAOSHEN_INFO = 2925;
    /**2926 激活妖神*/
    public static final short ACTIVATE_YAOSHEN = 2926;
    /**2927 妖神升级*/
    public static final short YAOSHEN_UPGRADE = 2927;
    /**2928  妖神切换*/
    public static final short YAOSHEN_CHANGE_SHOW = 2928;
    
    /**2929 推送妖神霸体属性变化*/
    public static final short YAOSHEN_ATTR_CHANGE = 2929;
    /** 已经使用了 妖神潜能丹数量 */
    public static final short YAOSHEN_QND_NUM = 2930;
    /** 已经使用了 妖神成长丹数量 */
    public static final short YAOSHEN_CZD_NUM = 2931;
    /** 妖神形态切换通知 */
    public static final short YAOSHEN_SHOW_UPDATE = 2932;
    /** 2933 角色当前妖神配置id */
    public static final short YAOSHEN_CONFIG_ID = 2933;

    /**2940 妖神魔纹信息 */
    public static final short YAOSHEN_MOWEN_INFO = 2940;
    /**妖神魔纹属性变化 */
    public static final short YAOSHEN_MOWEN_ATTR_CHANGE = 2941;
    /**2942 已经使用了魔纹潜能丹数量 */
    public static final short YAOSHEN_MOWEN_QND_NUM = 2942;
    /**2943 已经使用了魔纹成长丹数量*/
    public static final short YAOSHEN_MOWEN_CZD_NUM = 2943;
    /** 请求升级妖神魔纹 */
    public static final short YAOSHEN_MOWEN_UP = 2944;
    /** 登陆成功后通知客户端*/
    public static final short YAOSHEN_MOWEN_JIE_NOTICE_CLIENT = 2945;
    
    //-------------------妖神魂魄-----------------
    /**妖神魂魄信息 */
    public static final short YAOSHEN_HUNPO_INFO = 2950;
    /**妖神魂魄属性变化 */
    public static final short YAOSHEN_HUNPO_ATTR_CHANGE = 2951;
    /**已经使用了魂魄潜能丹数量 */
    public static final short YAOSHEN_HUNPO_QND_NUM = 2952;
    /**已经使用了魂魄成长丹数量 */
    public static final short YAOSHEN_HUNPO_CZD_NUM = 2953;
    /**请求升级妖神魂魄 */
    public static final short YAOSHEN_HUNPO_SJ = 2954;
    /**某魂魄信息*/
    public static final short YAOSHEN_HUNPO_INFO_ONE = 2955;
    /**请求镶嵌魄神*/
    public static final short YAOSHEN_HUNPO_TAIGUANG_ON = 2956;
    /**请求卸载魄神*/
    public static final short YAOSHEN_HUNPO_TAIGUANG_OFF = 2957;
    /**某魂魄属性变化*/
    public static final short YAOSHEN_HUNPO_TAIGUANG_ATTR_CHANGE = 2958;
    /**魄神兑换*/
    public static final short YAOSHEN_HUNPO_POSHEN_DUIHUAN = 2959;
    
    //-------------------妖神魔印-----------------
    /**请求妖神魔印信息 */
    public static final short YAOSHEN_MOYIN_INFO = 2961;
    /**请求升级妖神魔印 */
    public static final short YAOSHEN_MOYIN_SJ = 2962;
    /**妖神魔印属性变化 */
    public static final short YAOSHEN_MOYIN_ATTR_CHANGE = 2963;
    /**已经使用了潜能丹数量 */
    public static final short YAOSHEN_MOYIN_QND_NUM = 2964;
    /**已经使用了成长丹数量 */
    public static final short YAOSHEN_MOYIN_CZD_NUM = 2965;
    /**上线后推送给客户端阶数 */
    public static final short YAOSHEN_MOYIN_JIE_TO_CLIENT = 2966;
    /** 妖神霸体使用成长丹 */
    public static final short YAOSHEN_BATI_CZ = 2967;
    /** 妖神霸体使用潜能丹*/
    public static final short YAOSHEN_BATI_QN = 2968;
    /** 妖神魔纹使用成长丹 */
    public static final short YAOSHEN_MOWEN_CZ = 2969;
    /** 妖神魔纹使用潜能丹*/
    public static final short YAOSHEN_MOWEN_QN = 2970;
    /** 妖神魂魄使用成长丹 */
    public static final short YAOSHEN_HUNPO_CZ = 2971;
    /** 妖神魂魄使用潜能丹*/
    public static final short YAOSHEN_HUNPO_QN = 2972;
    /** 妖神魂魄使用成长丹 */
    public static final short YAOSHEN_MOYING_CZ = 2973;
    /** 妖神魂魄使用潜能丹*/
    public static final short YAOSHEN_MOYING_QN = 2974;
    
    //-----------------------时装-------------------------
    /**已激活时装信息*/
    public static final short GET_SHIZHUANG_INFO = 3011;
    /**激活时装*/
    public static final short ACTIVE_SHIZHUANG = 3012; 
    /**穿脱时装*/
    public static final short CHANGE_SHIZHUANG = 3013;
    /**玩家时装变化*/
    public static final short ROLE_SHOW_CHANGE = 3014;
    /**时装升级*/
    public static final short SHIZHUANG_LEVEL_UP = 3015;
    /**限时时装激活*/
    public static final short XIANSHI_SHIZHUANG_ACTIVE = 3016;
    /**限时时装续费*/
    public static final short XIANSHI_SHIZHUANG_XUFEI = 3017;
    /**时装升阶信息*/
    public static final short SHIZHUANG_JINJIE_INFO = 3018;
    /**请求时装升阶*/
    public static final short SHIZHUANG_JINJIE_UP = 3019;
    
    //-----------------------附属技能----------------------
    /**3150 请求御剑技能信息*/
    public static final short GET_YUJIAN_SKILL_INFO = 3150;
    /**3151 请求学习御剑技能*/
    public static final short LEARN_YUJIAN_SKILL = 3151;
    /**3153 请求翅膀技能信息*/
    public static final short GET_CHIBANG_SKILL_INFO = 3153;
    /**3154 请求学习翅膀技能*/
    public static final short LEARN_CHIBANG_SKILL = 3154;
    /**3155 请求战甲技能信息*/
    public static final short GET_TIANSHANG_SKILL_INFO = 3155;
    /**3156 请求学习战甲技能*/
    public static final short LEARN_TIANSHANG_SKILL = 3156;
    /**3157 请求仙剑技能信息*/
    public static final short GET_TIANGONG_SKILL_INFO = 3157;
    /**3158 请求学习仙剑技能*/
    public static final short LEARN_TIANGONG_SKILL = 3158;
    /**3159 请求器灵技能信息*/
    public static final short GET_QILING_SKILL_INFO = 3159;
    /**3160 请求学习器灵技能*/
    public static final short LEARN_QILING_SKILL = 3160;
    /**3159 请求天羽技能信息*/
    public static final short GET_TIANYU_SKILL_INFO = 3161;
    /**3160 请求学习天羽技能*/
    public static final short LEARN_TIANYU_SKILL = 3162;
    /**3159 请求圣剑技能信息*/
    public static final short GET_WUQI_SKILL_INFO = 3163;
    /**3160 请求学习圣剑技能*/
    public static final short LEARN_WUQI_SKILL = 3164;
    
    //------------------------热发布运营活动----------------------
    /**获取主活动数据(服务端只推送进行中或者请求当天开始的活动)*/
    public static final short RFB_GET_ACTIVITY = 10000;
    /**主活动时间发生改变*/
    public static final short RFB_ACTIVITY_TIME_UPDATE = 10001;
    /**主活动图标发生改变*/
    public static final short RFB_ACTIVITY_ICON_UPDATE = 10002;
    /**主活动名称发生改变*/
    public static final short RFB_ACTIVITY_NAME_UPDATE = 10003;
    /**主活动被下架(服务端主动推送)*/
    public static final short RFB_ACTIVITY_DELETE = 10004;
    /**新增主活动*/
    public static final short RFB_ACTIVITY_NAME_ADD = 10005;
    /**请求拉取子活动列表*/
    public static final short GET_ZIACTIVITY = 10020;
    /**请求拉取指定子活动的数据*/
    public static final short GET_ZHIDINGZIACTIVITY = 10021;
    /**子活动脚标状态*/
    public static final short CHILD_ICON_FLAG = 10022;
    
    //首充类指令
    /**领取类首充活动的礼包*/
    public static final short RECEVIE_ACTIVITY_SC = 10100;
    /**更新首充数值*/
    public static final short UPDATE_SHOUCHONG = 10101;
    /**特殊错误推送，礼包已经全部领完*/
    public static final short TESHU_SHOUCHONG = 10102;
    /**推送充值成功信息*/
    public static final short GONGGAO_SHOUCHONG = 10103;
    //图片公告
    public static final short TUPIAN_GONGGAO_TUISONG = 10299;
    //全民修仙活动指令
    /**请求领取礼包*/
    public static final short RECEVIE_ACTIVITY_QMXX = 10110;
    /**礼包状态发生变化*/
    public static final short UPDATE_QMXX = 10111;
    //修仙礼包
    /**10180 请求买并领取礼包*/
    public static final short BUY_XIUXIAN_LIBAO = 10180;
    /**10181 后端推送购买数量变化*/
    public static final short XIUXIAN_LIBAO_CHANGE = 10181;
    // 热发布战力比拼
    /**10131   请求领取礼包*/
    public static final short RECEVIE_ACTIVITY_ZLBP = 10131;
    /**10130 子活动配置未变化时的推送*/
    public static final short ZHANLI_BIPIN_CHANGE = 10130;
    //御剑飞行
    /**10131   请求领取礼包*/
    public static final short RECEVIE_ACTIVITY_YJFX = 10121;
    /**10130 子活动配置未变化时的推送*/
    public static final short YUJIAN_FEIXING_CHANGE = 10120;
    //翅膀
    /**10131   请求领取礼包*/
    public static final short RECEVIE_ACTIVITY_XJYY = 10141;
    /**10130 子活动配置未变化时的推送*/
    public static final short XIANJIE_YUYI_CHANGE = 10140;
    //仙界竞技
    /**10130 子活动配置未变化时的推送*/
    public static final short XIANJIE_JINGJI_CHANGE = 10150;
    //仙装强化
    /**10161   请求领取礼包*/
    public static final short RECEVIE_ACTIVITY_XZQH = 10161;
    /**10160 子活动配置未变化时的推送*/
    public static final short XIANZHUANG_QIANGHUA_CHANGE = 10160;
    //棋盘
    /**10171   请求转*/
    public static final short RECEVIE_ACTIVITY_QIPAN = 10171;
    //连充
    /**10260请求领取连充奖励*/
    public static final short LIANCHONG_REWARD_GET = 10260;
    /**10261请求连充状态数据*/
    public static final short LIANCHONG_DATA = 10261;
    /**10262 充值元宝推送事件*/
    public static final short LIANCHONG_RECHARGE = 10262;
    /**模拟充值TODO*/
    public static final short LIANCHONG_TEST = 10263;
    /**请求进入地图*/
    public static final short ENTER_ACTIVE_GAME = 10290;
    //妖神
    /** 请求领取礼包*/
    public static final short RECEVIE_ACTIVITY_YAOSHEN = 10341;
    /** 子活动配置未变化时的推送*/
    public static final short YAOSHEN_JINJIE_CHANGE = 10340;
    //妖神魔纹
    /** 请求领取礼包*/
    public static final short RECEVIE_ACTIVITY_YAOMO= 10381;
    /** 子活动配置未变化时的推送*/
    public static final short YAOSHENMOWEN_JINJIE_CHANGE = 10380;
    //妖神魔印
    /** 请求领取礼包*/
    public static final short RECEVIE_ACTIVITY_MOYIN= 10301;
    /** 子活动配置未变化时的推送*/
    public static final short YAOSHENMOYIN_JINJIE_CHANGE = 10300;
    //妖神魂魄
    /** 请求领取礼包*/
    public static final short RECEVIE_ACTIVITY_YAOPO= 10401;
    /** 子活动配置未变化时的推送*/
    public static final short YAOSHENHUNPO_JINJIE_CHANGE = 10400;
    //器灵
    /** 请求领取礼包*/
    public static final short RECEVIE_ACTIVITY_QILING = 10391;
    /** 子活动配置未变化时的推送*/
    public static final short YAOSHEN_JINJIE_QILING = 10390;
    //糖宝心纹
    /** 请求领取礼包*/
    public static final short RECEVIE_ACTIVITY_TANGBAOXINWEN = 10481;
    /** 子活动配置未变化时的推送*/
    public static final short YAOSHEN_JINJIE_TANGBAOXINWEN = 10480;

    /**10170 子活动配置未变化时的推送*/
    public static final short QI_PAN_CHANGE = 10170;
    //累充
    /**10181   请求领取*/
    public static final short RECEVIE_ACTIVITY_LEICHONG = 10191;
    /**10180 子活动配置未变化时的推送*/
    public static final short LEI_CHONG_CHANGE = 10190;
    /**服务端推送充值元宝变化*/
    public static final short TUI_SONG_RECHARGE_YB = 10192;

    
    /**请求领取连续天数充值奖励*/
    public static final short RECEVIE_DAY_LEICHONG = 10193;
    
    
    /**请求补充值之前的天数*/
    public static final short RECEVIE_BUQIAN_DAY_LEICHONG = 10194;
    //糖宝
    /**10181   请求领取*/
    public static final short RECEVIE_ACTIVITY_TANGBAO = 10201;
    /**10180 子活动配置未变化时的推送*/
    public static final short TANG_BAO_CHANGE = 10200;
    //神秘商店
    /**请求购买*/
    public static final short RECEVIE_BUY_SMSD = 10212;
    /**请求刷新*/
    public static final short RECEVIE_SX_SMSD = 10211;
    /**检测刷新*/
    public static final short RECEVIE_JCSX_SMSD = 10210;
     /**神秘商店系统广播*/ 
    public static final short SMSD_SYSTEM_NOTIFY = 10213;
    //消费排名
    /**   请求单服消耗排行榜*/
    public static final short GET_XIAOFEI_INFO = 10221;
    /** 子活动配置未变化时的推送*/
    public static final short XIAOFEI_PAIMING_CHANGE = 10220;
    //战甲
    /**10131   请求领取礼包*/
    public static final short RECEVIE_ACTIVITY_TBZJ = 10241;
    /**10130 子活动配置未变化时的推送*/
    public static final short TANGBAO_ZHANJIA_CHANGE = 10240;
    //命运转盘
    /**转盘公告*/
    public static final short ZP_SYSTEM_NOTIFY = 10230;
    /**转盘个人数据*/
    public static final short ZP_ONE_SYSTEM_NOTIFY = 10231;
    /**请求转*/
    public static final short ZP_ZHUAN = 10232;
    /**请求兑换*/
    public static final short ZP_DUIHUAN = 10233;
    //老玩家回归
    /**10540 请求拉取累计登陆数据*/
    public static final short LAOWANJIA_INFO = 10540;
    /**10541 请求领取某个登陆奖励*/
    public static final short LAOWANJIA_LINGQU = 10541;
    /**10542 推送活动显示状态*/
    public static final short LAOWANJIA_TUISONG = 10542;
    //盗墓手札
    /**10250 请求追寻*/
    public static final short DAOMO_ZHUIXUN = 10250;
    /**10251 公告*/
    public static final short DAOMO_GONGGAO = 10251;
    
    //探宝
    /**探索宝藏*/
    public static final short TANBAO_TANBAO = 10460;
    /**请求领取王城奖励*/
    public static final short TANBAO_LINGQU_WANG = 10461;
    /**服务端推送有玩家通过探索宝藏活动获得高级物品*/
    public static final short TANBAO_TUISONG = 10462;
    
    //--------------------------------团购秒杀-------------------------
    /**10570 请求秒杀信息*/
    public static final short GET_MIAOSHA_INFO = 10530 ;
    /**10571 请求秒杀*/
    public static final short MIAOSHA_BUY = 10531 ;
    //超值兑换
    /**10280 请求拉取超值兑换数据*/
    public static final short GET_SUPER_DUIHUAN_USER_INFO = 10280;
    /**10281 请求领取某个超值兑换的奖励*/
    public static final short SUPER_DUIHUAN = 10281 ;
    //跨服充值排名
    /**10310 请求拉取跨服充值排名数据*/
    public static final short GET_KUAFU_CHARGE_RANK_INFO = 10310;
    /**10311 请求拉取跨服充值我的信息*/
    public static final short GET_KUAFU_CHARGE_MY_INFO = 10311;
    //充值返利
    /**请求领取*/
    public static final short RECEVIE_ACTIVITY_CHONGZHIFANLI = 10321;
    /**子活动配置未变化时的推送*/
    public static final short CHONGZHI_FANLI_CHANGE = 10320;
    //欢乐卡牌
    /**10330 请求翻牌*/
    public static final short FAN_CARD = 10330;
    /**10331 翻牌公告*/
    public static final short FAN_CARD_NOTICE = 10331;
    /**10332 重置*/
    public static final short RESET_FAN_CARD = 10332;
    /**10333 充值金额变化翻牌次数变化*/
    public static final short HAPPY_CARD_USER_INFO = 10333;
    //热发布寻宝
    /**10370 寻宝**/
    public static final short REFABU_XUNBAO = 10450;
    /**10371 广播全服寻宝信息 **/
    public static final short NOTICE_XUNBAO_INFO = 10451;
    /**10372 领取全服寻宝奖励  **/
    public static final short GET_XUNBAO_REWARDS = 10452;
    /**10373 全服寻宝次数变化  **/
    public static final short CHANGE_XUNBAO_COUNT = 10453;
    //--------------------------------排行榜----------------------
  //----------------------------------跨服消费排行------------------------------
    /**10590 子活动配置未变化时的推送**/
    public static final short GET_KUAFU_XIAOFEI_INFO = 10590;
    /**10591 请求单服消耗排行榜  **/
    public static final short GET_KUAFU_XIAOFEI_RANK = 10591;
    
    //--------------------------每日充值------------------------------
    /**10600 请求每日充值领奖  **/
    public static final short LOOP_DAY_CHONGZHI_LJ = 10600;
    /**更新首充数值*/
    public static final short UPDATE_LOOPDAYCHONG = 10601;
    /**更新首充数值*/
    public static final short UPDATE_LOOPDAYCHONG_BC = 10602;
    
  //--------------------------圣剑排行------------------------------
    /**子活动配置未变化时的推送  **/
    public static final short SHENG_JIAN_PAIMING = 10606;
    /**领奖*/
    public static final short SHENG_JIAN_LINGJIANG = 10607;
    
    //-----------------------------------藏宝阁------------------------------
    /**请求藏宝阁信息*/
    public static final short CANGBAOGE_INFO = 10270;
    /**请求开始藏宝阁抽奖*/
    public static final short CANGBAOGE_START_CHOU = 10271;
    
    //--------------------------------绑定手机奖励----------------------
    /**获得绑定手机奖励信息*/
    public static final short GET_PHONE_REWARD_INFO = 20002;
    /**获取手机奖励*/
    public static final short PICK_PHONE_REWARD = 20003;
    //--------------------------------37玩平台礼包----------------------
    /**获取37wan玩家礼包领取情况*/
    public static final short GET_PLATFORM_37WAN_GIFT_STATE = 20000;
    /**领取37wan玩家礼包*/
    public static final short GET_PLATFORM_37WAN_GIFT = 20001;
    /**37wan请求超级会员状态*/
    public static final short GET_PLATFORM_37WAN_SUPER_VIP_INFO = 20004;

    public static final short RECHARGE_TEST = 20007;
    /**37wan超级会员是否现实qq*/
    public static final short GET_PLATFORM_37WAN_SHOWQQ = 20005;
    /**37wan超级会员活动是否关闭*/
    public static final short GET_PLATFORM_37WAN_SUPER_VIP_CLOSED = 20006;
    /**37令牌奖励领取状态*/
    public static final short GET_PLATFORM_37WAN_LINGPAI_STATE = 20008;
    /**37令牌奖励领取*/
    public static final short GET_PLATFORM_37WAN_LINGPAI_GET = 20009;
    /**20010 V计划一键领取每日礼包*/
    public static final short GET_PLATFORM_37WAN_DAY_LB = 20010;
    /**20011 V计划升级礼包领取状态*/
    public static final short GET_PLATFORM_37WAN_UPGRADE_STATE = 20011;
    /**20012 V计划领取升级礼包*/
    public static final short GET_PLATFORM_37WAN_UPGRADE_LB = 20012;
    /**20013 请求V计划消费礼包的状态*/
    public static final short GET_PLATFORM_37WAN_CONSUME_STATE = 20013;
    /**20014 本轮以消费金额*/
    public static final short GET_PLATFORM_37WAN_CONSUM_VALUE = 20014;
    /**20015 请求领取消费礼包奖励*/
    public static final short GET_PLATFORM_37WAN_CONSUM_LB = 20015;
    
     
    
    //--------------------------------360平台礼包----------------------
     
    /**360礼包领取情况*/
    public static final short GET_PLATFORM_360_GIFT_STATE = 20050;
    /**领取360加速球礼品*/
    public static final short GET_PLATFORM_360_BALL_GIFT = 20051;
    /**领取360游戏大厅的奖品*/
    public static final short GET_PLATFORM_360_DATING_GIFT = 20052;
    /**领取360特权等级礼包*/  
    public static final short GET_PLATFORM_360_TEQUAN_GIFT = 20053;
    /**360 特权等级礼包状态 */  
    public static final short GET_PLATFORM_360_TEQUAN_STATES = 20054;
    /**领取360 特权分享礼包 */  
    public static final short GET_PLATFORM_360_TEQUAN_SHARE_LB = 20055;
    /**20056 V计划一键领取每日礼包 */  
    public static final short GET_PLATFORM_360_VPLAN_DAY_LB = 20056;
    /**V计划升级礼包领取状态*/  
    public static final short GET_PLATFORM_360_VPLAN_UP_STATE = 20057;
    /**V计划领取升级礼包 */  
    public static final short GET_PLATFORM_360_VPLAN_UP_LB = 20058;
    /**V计划开通礼包状态 */  
    public static final short GET_PLATFORM_360_VPLAN_KAITONG_STATE = 20059;
    /**V计划请求领取开通礼包*/  
    public static final short GET_PLATFORM_360_VPLAN_KAITONG = 20060;
    /**V计划消费礼包状态*/  
    public static final short GET_PLATFORM_360_XF_LB_STATE = 20061;
    /**V计划消费礼包消费金额*/  
    public static final short  GET_PLATFORM_360_XF_LB_GOLD = 20062;
    /**V计划消费礼包领取*/  
    public static final short  GET_PLATFORM_360_XF_LB = 20063;
    
     
    
     
    
    

    //--------------------------------迅雷平台礼包----------------------
    /**迅雷vip礼包领取情况*/
    public static final short GET_PLATFORM_XUNLEI_GIFT_STATE = 20100; 
    /**领取迅雷大厅礼包*/
    public static final short GET_PLATFORM_XUNLEI_DATING_GIFT = 20105; 
    /**领取迅雷特权礼包*/
    public static final short GET_PLATFORM_XUNLEI_TEQUAN_GIFT = 20106; 
    /**领取迅雷vip礼包*/
    public static final short GET_PLATFORM_XUNLEI_GIFT = 20101; 
    /**迅雷特权是否关闭*/
    public static final short GET_PLATFORM_XUNLEI_CLOSED = 20102; 
    /**迅雷特权是否显示qq*/
    public static final short GET_PLATFORM_XUNLEI_SHOWQQ = 20103; 
    /**迅雷特权面板基本信息*/
    public static final short GET_PLATFORM_XUNLEI_SUPER_VIP_INFO = 20104; 
    
    //--------------------------------sogou平台礼包----------------------
    /**搜狗礼包领取情况*/
    public static final short GET_PLATFORM_SOGOU_GIFT_STATE = 20120; 
    /**大厅礼包*/
    public static final short GET_PLATFORM_SOGOU_GIFT_DATING = 20121; 
    /**皮肤礼包*/
    public static final short GET_PLATFORM_SOGOU_GIFT_PIFU = 20122; 
    
    //--------------------------------超级会员公用指令----------------------
    /**超级会员状态*/
    public static final short GET_PLATFORM_SUPER_QQ_STATE = 20123; 
    /**20124 超级会员活动是否关闭*/
    public static final short GET_PLATFORM_SUPER_QQ_CLOSE = 20125; 
    /**20125 超值是否达到显示qq号*/
    public static final short GET_PLATFORM_SHOWQQ = 20124; 
    //--------------------------------顺网平台礼包----------------------
    /**顺网礼包领取情况*/
    public static final short GET_PLATFORM_SHUNWANG_GIFT_STATE = 20140; 
    /**显示qq号*/
    public static final short GET_PLATFORM_SHUNWANG_SHOWQQ = 20141; 
    /**20142 顺网超级会员按钮显示状态*/
    public static final short GET_PLATFORM_SHUNWANG_CLOSED = 20142; 
    /** 顺网领取礼包状态*/
    public static final short GET_PLATFORM_SHUNWANG_STATE = 20143; 
    /**领取顺网礼包*/
    public static final short GET_PLATFORM_SHUNWANG_LB = 20144; 
    
    
 
//  public static final short PLATFORM_PPS_GIFT = 2902;
    //--------------------------------qq平台2900-2950---------------------
    /**20160 请求1次性领取状态(黄钻新手礼包，黄钻角色等级礼包）*/
    public static final short GET_QQ_GIFT_STATUS = 20160;
    /**20161 请求领取黄砖新手礼包*/
    public static final short GET_QQ_XINSHOU_GIFT = 20161;
    /**20162 请求领取黄砖角色等级礼包*/
    public static final short GET_QQ_DENGJI_GIFT = 20162;
    /**20163 请求领取黄砖每日礼包的领取状态*/
    public static final short GET_QQ_MEIRI_GIFT_STATUS = 20163;
    /**20164 请求领取每日奖励(年费和黄钻级别一并领取)*/
    public static final short GET_QQ_MEIRI_GIFT = 20164;
    /**20165 请求角色自己的平台状态*/
    public static final short GET_QQ_PLATFORM_INFO= 20165;
    /**20166 请求购买*/
    public static final short QINGQIU_RECHARGE = 20166;
    /**Q点直购*/
    public static final short QIANDIAN_ZHIGOU = 20167;
    /**请求Q点直购次数*/
    public static final short QIANDIAN_ZHIGOU_COUNT = 20168;
    /**推送Q点直购*/
    public static final short TUISONG_QDIAN = 20169;
    /**请求今日微端礼包领取状态*/
    public static final short GET_WEIDUAN_INFO = 20170;
    /**请求领取微端礼包*/
    public static final short LINGQU_WEIDUAN = 20171;
    /**请求tgp礼包状态*/
    public static final short GET_TGP_INFO = 20180;
    /**请求领取tgp礼包*/
    public static final short LINGQU_TGP = 20181;
    /**请求领取tgp礼包*/
    public static final short LINGQU_TGP_LOGIN = 20183;
    /**请求领取tgp礼包*/
    public static final short LINGQU_TGP_LEVEL = 20182;
    public static final short GET_TGP_ZHUANOAN_INFO = 20184;
    public static final short GET_TGP_ZSONG_HAOYOU = 20185;
    public static final short GET_TGP_ZHUANA = 20186;
    public static final short GET_TGP_DUIHUAN = 20187;
    public static final short GET_TGP_NL_TUISONG = 20188;
    /**20190 请求1次性领取状态(蓝钻新手礼包，蓝钻角色等级礼包）*/
    public static final short GET_QQ_LANZUAN_GIFT_STATUS = 20190;
    /**20191 请求领取蓝砖新手礼包*/
    public static final short GET_QQ_LANZUAN_XINSHOU_GIFT = 20191;
    /**20192 请求领取蓝砖角色等级礼包*/
    public static final short GET_QQ_LANZUAN_DENGJI_GIFT = 20192;
    /**20193 请求领取蓝砖每日礼包的领取状态*/
    public static final short GET_QQ_LANZUAN_MEIRI_GIFT_STATUS = 20193;
    /**20194 请求领取每日奖励(年费和蓝钻级别一并领取)*/
    public static final short GET_QQ_LANZUAN_MEIRI_GIFT = 20194;
    /**20195 请求3366包子每日礼包状态*/
    public static final short GET_QQ_3366_BAOZI_MEIRI = 20195;
    /**20195 请求领取3366包子每日礼包*/
    public static final short GET_QQ_3366_BAOZI_MEIRI_LINGQU = 20196;
    /**推送蓝钻过期时间*/
    public static final short TUISONG_LANZUAN_GOUQI_SHIJIAN = 20197;
    /**请求领取续费礼包*/
    public static final short GET_LINGQU_XUFEI_LIBAO = 20198;
    /**请求蓝钻折扣商城基础信息*/
    public static final short GET_LANZUAN_ZHEKUO_SHANGDIAN = 20199;
    /**请求购买蓝钻折扣商城物品*/
    public static final short GET_LANZUAN_SHANGDIAN_BUY = 20200;
    /**请求开通蓝钻送好礼基本信息*/
    public static final short GET_LANZUAN_SONGLI_INFO = 20256;
    /**请求开通蓝钻好礼token号*/
    public static final short GET_LANZUAN_SONGLI_TOKEN = 20257;
    /**请求领取开通蓝钻好礼奖励*/
    public static final short GET_LANZUAN_SONGLI_LINGQU = 20258;
    /**请求已领取的邀请好友奖励信息*/
    public static final short GET_YAOQONG_LINGQU_INFO = 20260;
    /**请求已邀请成功且达到指定等级的好友数量*/
    public static final short GET_YAOQING_SECCESS_COUNT = 20261;
    /**请求领取邀请好友奖励*/
    public static final short LINGQU_YAOQING_JITEM = 20262;

    //-------------------------qq游戏大厅特权礼包(20263-20268)--------------------------------
    /** 请求领取新手礼包状态 */
    public static final short QQGAME_XINSHOU_INFO = 20263;
    /** 请求领取新手礼包 */
    public static final short QQGAME_XINSHOU_GIFT = 20264;
    /** 请求领取等级礼包状态 */
    public static final short QQGAME_LEVEL_INFO = 20265;
    /** 请求领取等级礼包状态 */
    public static final short QQGAME_LEVEL_GIFT = 20266;
    /** 请求领取每日礼包状态 */
    public static final short QQGAME_EVERY_INFO = 20267;
    /** 请求领取每日礼包状态 */
    public static final short QQGAME_EVERY_GIFT = 20268;
    
    //-------------------------qq空间特权礼包(20269-20274)--------------------------------
    /** 请求领取新手礼包状态 */
    public static final short QqQzone_XINSHOU_INFO = 20269;
    /** 请求领取新手礼包 */
    public static final short QqQzone_XINSHOU_GIFT = 20270;
    /** 请求领取等级礼包状态 */
    public static final short QqQzone_LEVEL_INFO = 20271;
    /** 请求领取等级礼包状态 */
    public static final short QqQzone_LEVEL_GIFT = 20272;
    /** 请求领取每日礼包状态 */
    public static final short QqQzone_EVERY_INFO = 20273;
    /** 请求领取每日礼包状态 */
    public static final short QqQzone_EVERY_GIFT = 20274;
    
    //--------------------皇城争霸赛---------------------------

    /**2131请求皇城占领信息*/
    public static final short HC_ZBS_WIN_INFO = 2131;
    /**请求进入皇城争霸*/
    public static final short HC_ZBS_ENTER = 2132;
    /**2133请求领取皇城争霸奖励*/
    public static final short HC_ZBS_REWARD = 2133;
    /**2134皇城争霸开始夺旗帜*/
    public static final short HC_ZBS_FETCH_FLAG_BEGIN = 2134;
    /**2135 皇城争霸夺旗帜结束*/
    public final static short HC_ZBS_FETCH_FLAG_END = 2135;
    /**2136 皇城争霸复活*/
    public final static short HC_ZBS_FUHUO = 2136;
    /**2137 皇城争霸期间在该地图内获得经验真气*/
    public static final short HC_ZBS_ADD_ZHENQI = 2137;
    /**皇城争霸期间推送给地图上的玩家旗子的实时位置*/
    public static final short HC_ZBS_SYN_FLAG = 2138;
    /**皇城争霸地图旗帜被某门派占领*/
    public static final short HC_ZBS_FLAG_CHANGE = 2139;
    /**皇城争霸地图提前结束了*/
    public static final short HC_ZBS_END = 2140;
    /**退出皇城争霸地图*/
    public static final short HC_ZBS_EXIT = 2141;
    /**2142 角色是否拥有皇城套装*/
    public static final short HC_ZBS_CLOTHES_SHOW   = 2142;
    //-----------------------------------宠物------------------------------
    /**请求宠物左侧列表信息*/
    public static final short GET_CHONGWU_LIST = 3001;
    /**激活宠物*/
    public static final short ACTIVATE_CHONGWU = 3002;
    /**某宠物具体信息*/
    public static final short GET_CHONGWU_DETAIL = 3003;
    /**宠物升阶*/
    public static final short CHONGWU_UPGRADE_JIE = 3004;
    /**宠物出战*/
    public static final short CHONGWU_FIGHT = 3005;
    /**3306 宠物经验变化*/
    public static final short CHONGWU_EXP_CHANGE = 3006;
    /**推送宠物属性变化*/
    public static final short CHONGWU_ATTR_CHANGE = 3007;
    /** 宠物升级技能 */
    public static final short CHONGWU_SKILL_UPLEVEL = 3008;
    
    //------------------------------结婚-------------------------------------
    /**请求自身婚姻状况*/
    public static final short GET_SELF_MARRY_INFO = 3021;
    /**请求订婚*/
    public static final short APPLY_DINGHUN = 3022;
    /**求婚*/
    public static final short APPLY_MARRY = 3023;
    /**取消订婚*/
    public static final short CANCEL_DINGHUN = 3024;
    /**缘分培养*/
    public static final short ADD_YUANFEN = 3025;
    /**亲密度培养*/
    public static final short ADD_QINMIDU = 3026;
    /**请求龙凤呈祥信息*/
    public static final short GET_LONGFENG_INFO = 3027;
    /**推送缘分值*/
    public static final short NOTICE_YUANFEN = 3028;
    /**推送亲密度*/
    public static final short NOTICE_QINMIDU = 3029;
    /**同意订婚*/
    public static final short AGREE_DINGHUN = 3030;
    /**同意结婚*/
    public static final short AGREE_MARRY = 3031;
    /**通知双方订婚成功*/
    public static final short NOTICE_DING_SUCCESS = 3032;
    /**通知双方结婚成功*/
    public static final short NOTICE_MARRY_SUCCESS = 3033;
    /**更换信物*/
    public static final short CHANGE_MARRY_XINWU = 3034;
    /**请求订婚信息*/
    public static final short GET_DING_INFO = 3035;
    /**请求结婚信息*/
    public static final short GET_MARRY_INFO = 3036;
    /**保存今日心情*/
    public static final short SAVE_TODAY_XINQING = 3037;
    /**通知玩家收到订婚请求*/
    public static final short NOTICE_RECIVE_DING_APPLY = 3038;
    /**通知玩家收到求婚请求*/
    public static final short NOTICE_RECIVE_MARRY_APPLY = 3039;
    /**推送亲密度等级*/
    public static final short NOTICE_LOVE_LEVEL_CHANGE = 3040;
    /**拒绝订婚*/
    public static final short REFUSE_DINGHUN = 3041;
    /**拒绝结婚*/
    public static final short REFUSE_MARRY = 3042;
    /**通知玩家收到拒绝订婚消息*/
    public static final short NOTICE_TARGET_REFUSE_DINGHUN = 3043;
    /**通知玩家收到拒绝结婚消息*/
    public static final short NOTICE_TARGET_REFUSE_MARRY = 3044;
    /**请求打开、关闭求婚小面板*/
    public static final short OPEN_CLOSE_DINGHUN_PANEL = 3045;
    /**请求打开、关闭变更信息面板*/
    public static final short OPEN_CLOSE_CHANGE_XINWU_PANEL = 3046;
    /**通知双方信物发生改变*/
    public static final short NOTICE_XINWU_CHANGED = 3047;
    /**对方取消订婚*/
    public static final short TARGET_CANCEL_DING = 3048;
    /**请求离婚*/
    public static final short APPLY_DIVORCE = 3061;
    /**中断离婚*/
    public static final short CANCEL_DIVORCE = 3062;
    /**通知对方协议离婚*/
    public static final short NOTICE_TARGET_DIVORCE = 3063;
    /**通知对方离婚成功*/
    public static final short NOTICE_TARGET_DIVORCE_SUCCESS = 3064;
    /**同意离婚*/
    public static final short AGREE_DIVORCE = 3065;
    /**拒绝离婚*/
    public static final short REFUSE_DIVORCE = 3066;
    /**通知主动方对方离婚答复*/
    public static final short NOTICE_TARGET_ANSWER_DIVORCE = 3067;
    /**通知对方取消离婚*/
    public static final short NOTICE_TARGET_CANCEL_DIVORCE = 3068;
    
    //--------------------------------淬炼----------------------
    /**请求淬炼信息*/
    public static final short GET_CUILIAN_INFO = 3081;
    /**淬炼*/
    public static final short CUILIAN_GO = 3082;
    
    //--------------------------------改名----------------------
    /**改名*/
    public static final short USER_MODIFY_NAME = 3071;
    /**改名*/
    public static final short GET_MODIFY_NAME_CD = 3072;
    
    //--------------------------------红包----------------------
    /**3261玩家首冲推送新红包给所有在线玩家  后台主推*/
    public static final short HONGBAO_NEW = 3261;
    /**3262 请求领取红包*/
    public static final short HONGBAO_GET = 3262; 
    
    
    
    // -------------------------跨服竞技 1v1 ------------------------
    /** 请求个人跨服竞技信息 */
    public final static short KUAFU_ARENA_1V1_GET_MY_RANK = 3101;
    /** 请求某个类型的信息 */
    public final static short GET_KUAFU_ARENA_1V1_INFO = 3102;
    /** 请求匹配单人竞技 */
    public final static short KUAFU_ARENA_1V1_MATCH = 3103;
    /** 3104 请求段位榜 */
    public final static short GET_KUAFU_ARENA_1V1_RANK = 3104;
    /** 3105 1v1领取奖励*/
    public final static short GET_KUAFU_ARENA_1V1_PICK_REWARD = 3105;
    /** 3106 1v1请求今日已兑换信息*/
    public final static short GET_KUAFU_ARENA_1V1_DUIHUAN_INFO = 3106;
    /** 3107 1v1兑换道具*/
    public final static short KUAFU_ARENA_1V1_DUIHUAN = 3107;
    /** 取消请求匹配单人竞技 */
    public final static short KUAFU_ARENA_1V1_CANCEL_MATCH = 3108;
    /** 匹配单人竞技成功 */
    public final static short KUAFU_ARENA_1V1_MATCH_SUCCESS = 3109;
    /** 进入副本 */
    public final static short KUAFU_ARENA_1V1_ENTER= 3110 ;
    /** 退出副本 */
    public final static short KUAFU_ARENA_1V1_EXIT = 3111 ;
    /** 挑战结果 */
    public final static short KUAFU_ARENA_1V1_RESULT = 3112 ;
    /** 匹配失败 */
    public final static short KUAFU_ARENA_1V1_MATCH_FAIL = 3113 ;
    /** 1v1请求段位榜昨日信息 */
    public final static short KUAFU_ARENA_1V1_YESTERDAY_INFO = 3114 ;
    
    //---------------------------------混沌战场------------------------
    /**混沌战场下次结算时间*/
    public static final short CHAOS_END_TIME = 3272;
    /**混沌战场进入下一层*/
    public static final short CHAOS_ENTER_NEXT = 3275;
    /**进入混沌战场*/
    public static final short CHAOS_ENTER = 3271;
    /**退出混沌战场*/
    public static final short CHAOS_EXIT = 3273;
    /**混沌战场进入下层信息*/
    public static final short CHAOS_NOTICE_ENTER_NEXT = 3274;
    /**推送本次混沌战场1-5名玩家名字信息*/
    public static final short CHAOS_NOTICE_TOP_5_NAMES = 3277;
    /**客户端拉取当前层排行*/
    public static final short CHAOS_CUR_CENG_RANK_INFO = 3278;
    /**推送客户端自己积分变化*/
    public static final short CHAOS_NOTICE_SELF_SCORE_CHANGE = 3279;
    /**请求自己排名*/
    public static final short CHAOS_ASK_SELF_RANK = 3280;
    /**通知战场中获得经验真气*/
    public static final short CHAOS_GET_EXP_ZHENQI = 3281;
    /**混沌战场中普通复活*/
    public static final short CHAOS_PUTONG_FUHUO = 3284;
    
    // -------------------------累计消耗------------------------
//  /**10350 请求拉取累计充值数据*/
//  public static final short GET_LEIHAO_INFO = 10350;
    /**10350 请求领取奖励*/
    public static final short PICK_LEI_HAO_REWARD = 10350;
    /**10351 消耗元宝推送事件*/
    public static final short PUSH_LEI_HAO_YB = 10351;
    
    // -------------------------八卦阵------------------------
    /** 请求八卦副本信息*/
    public final static short BAGUA_GET_MY_INFO = 3300;
    /** 请求八卦副本队伍信息*/
    public final static short BAGUA_SELECT_TEAM = 3301;
    /** 请求创建八卦练副本队伍**/
    public final static short BAGUA_CREATE_TEAM = 3302;
    /** 请求加入八卦副本队伍**/
    public final static short BAGUA_JOIN_TEAM = 3303;
    /** 八卦副本队伍队长变化**/
    public final static short BAGUA_FUBEN_TEAM_CHANGE = 3304;
    /** 八卦副本队伍准备状态变化*/
    public final static short BAGUA_CHANGE_PREPARE_STATUS = 3305;
    /** 谁谁加入八卦副本队伍(其他成员)*/
    public final static short BAGUA_JOIN_TEAM_NOTICE = 3306;
    /** 谁谁离开八卦副本队伍*/
    public final static short BAGUA_FUBEN_LEAVE_SENDER = 3307;
    /**  队长把谁踢出八卦副本队伍*/
    public final static short BAGUA_KICK = 3308;
    /**  队长更改八卦副本队伍进入需求战力*/
    public final static short BAGUA_CHANGE_STRENGTH = 3309;
    /** 队长更改八卦副本队伍是否满员自动挑战*/
    public final static short BAGUA_CHANGE_TEAM_AUTO_START = 3310;
    /** 退出八卦副本队伍**/
    public final static short BAGUA_TEAM_LEAVE = 3311;
    /**请求进入八卦副本*/
    public final static short BAGUA_ENTER_FUBEN = 3312;
    /** （副本预进入请求）通知队伍里所有的人在指定时间戳后进入副本*/
    public final static short BAGUA_START_TEAM = 3313;
    /** 当前进入的幻境历练副本需要击杀怪物数量*/
    //public final static short MORE_FUBEN_MONSTER_COUNT = 2313;
    /** 多人八卦副本领取奖励*/
    public final static short BAGUA_PICK_REWARD = 3314;
    /** 请求退出八卦副本*/
    public final static short BAGUA_LEAVE_FUBEN = 3315;
    
    /**3316 请求进入八卦奇阵副本传送门*/
    public final static short BAGUA_FUBEN_ENTER_DOOR = 3316;
    /**3316 请求进入八卦奇阵副本下一层*/
    public final static short BAGUA_FUBEN_DOOR_NOTICE = 3317;
    
    /**3318 请求世界聊天频道里发一条广播 (邀请别人加入我的队伍)*/
    public final static short BAGUA_FUBEN_YAOQING_OTHER = 3318;
    
    /**3319 玩家进入生门后，在屏幕顶端出现提示信息：恭喜{0}找到第{1}层的生门！*/
    public final static short BAGUA_NOTICE_ENTER_SHENGMEN = 3319;   
    /**3320 推送给队伍内的玩家奖励*/
    public final static short BAGUA_RESULT_TIP= 3320;   
    /**3321 复活*/
    public final static short BAGUA_FUHUO= 3321;    
    public final static short BAGUA_FUBEN_QUICK_JOIN= 3322; 
    

	// -------------------------拉霸活动------------------------
	/**拉霸一次*/
	public static final short LABA_GO = 10360;
	/**10362 推送获奖公告**/
	public static final short LABA_REWARD_NOTICE = 10361;
	/**拉霸一次*/
	public static final short LABA_GO53 = 10636;
	public static final short LABA_REWARD_NOTICE53 = 10637;
	public static final short LABA_RECORD53 = 10638;
	
	
	
	 
	
	//---------------------------幸运彩蛋------------------------
	/**请求幸运彩蛋运行配置*/
	public static final short GET_CAIDAN_CONFIG_INFO = 10370;
	/**请求幸运彩蛋积分兑换配置*/
	public static final short GET_CAIDAN_DUIHUAN_CONFIG = 10371;
	/**请求全服砸蛋奖励日志*/
	public static final short GET_CAIDAN_ZADAN_LOGS = 10372;
	/**请求重置彩蛋*/
	public static final short CAIDAN_RESET = 10373;
	/**请求砸某一个蛋*/
	public static final short CAIDAN_ZADAN_ONE = 10374;
	/**请求砸掉剩余的蛋蛋*/
	public static final short CAIDAN_ZADAN_ALL = 10375;
	/**请求砸蛋积分兑换物品*/
	public static final short CAIDAN_DUIHUAN = 10376;
	
	// -------------------------跨服竞技 4v4------------------------
	/** 3201 4v4活动信息 */
	public final static short GET_KUAFU_ARENA_4V4_INFO = 3201;
	/** 3202 4v4开始自动匹配 */
	public final static short KUAFU_ARENA_4V4_MATCH = 3202;
	/** 3203 4v4请求段位榜昨日信息 */
	public final static short GET_KUAFU_ARENA_4V4_YESTERDAY_INFO = 3203;
	/** 3204 4v4请求段位榜 */
	public final static short GET_KUAFU_ARENA_4V4_RANK = 3204;
	/** 3205 4v4领取排行榜奖励*/
	public final static short GET_KUAFU_ARENA_4V4_PICK_REWARD = 3205;
	/**3206 4v4跨服竞技晶岩占领比分变化*/
	public final static short GET_KUAFU_ARENA_4V4_SCORE_CHANGE = 3206;
	/**3207 4v4跨服竞技BOSS最后一次击杀阵营*/
	public final static short GET_KUAFU_ARENA_4V4_BOSS_DEAD = 3207;
	/**4v4跨服竞技当前积分变化*/
	public static final short SHENMO_NOTICE_CUR_SCORE = 3208;
	/** 3209 4v4取消匹配 */
	public final static short KUAFU_ARENA_4V4_CANCEL_MATCH = 3209;
	/**3210 4v4匹配成功*/
	public final static short KUAFU_ARENA_4V4_MATCH_SUCCESS = 3210;
	/**4v4进入活动，通知结束时间*/
	public static final short SHENMO_OVER_TIME = 3211;
	/**3212 4v4退出活动*/
	public final static short KUAFU_ARENA_4V4_EXIT = 3212 ;
	/**3213 4v4挑战结果*/
	public final static short KUAFU_ARENA_4V4_RESULT = 3213 ;
	/**3214  匹配失败 */
	public final static short KUAFU_ARENA_4V4_MATCH_FAIL = 3214 ;
	/**3215 组队匹配 */
	public final static short KUAFU_ARENA_4V4_TEAM_MATCH = 3215 ;
	/**3216 确认是否当前队伍进入神魔战场 */
	public final static short KUAFU_ARENA_4V4_CONFIRM_TEAM_MATCH = 3216 ;
	
	//-------------------------塔防-------------------------------
	/**3450 请求塔防副本信息*/
	public static final short TAFANG_GET_INFO = 3450;
	/**3451 请求进入塔防副本*/
	public static final short TAFANG_ENTER = 3451;
	/**3452 通知客户端当前波数的怪物已刷完*/
	public static final short TAFANG_NOTICE_MONSTER_CREATE_OVER = 3452;
	/**3453 请求开始塔防副本*/
	public static final short TAFANG_START = 3453;
	/**3454 推送经验变化*/
	public static final short TAFANG_EXP_CHANGE = 3454;
	/**3455 请求建造塔防*/
	public static final short TAFANG_BUILD_TAFANG = 3455;
	/**3456 请求升级塔防*/
	public static final short TAFANG_TAFANG_LEVEL_UP = 3456;
	/**3457 请求领取奖励*/
	public static final short TAFANG_RECIVE_EXP = 3457;
	/**3458 请求退出塔防*/
	public static final short TAFANG_EXIT = 3458;
	
	
	
	
	// -------------------------埋骨之地------------------------
	/** 请求埋骨之地副本信息*/
	public final static short MAIGU_GET_MY_INFO = 3500;
	/** 请求埋骨之地副本队伍信息*/
	public final static short MAIGU_SELECT_TEAM = 3501;
	/** 请求创建埋骨之地练副本队伍**/
	public final static short MAIGU_CREATE_TEAM = 3502;
	/** 请求加入埋骨之地副本队伍**/
	public final static short MAIGU_JOIN_TEAM = 3503;
	/** 埋骨之地副本队伍队长变化**/
	public final static short MAIGU_FUBEN_TEAM_CHANGE = 3504;
	/** 埋骨之地副本队伍准备状态变化*/
	public final static short MAIGU_CHANGE_PREPARE_STATUS = 3505;
	/** 谁谁加入埋骨之地副本队伍(其他成员)*/
	public final static short MAIGU_JOIN_TEAM_NOTICE = 3506;
	/** 谁谁离开埋骨之地副本队伍*/
	public final static short MAIGU_FUBEN_LEAVE_SENDER = 3507;
	/**  队长把谁踢出埋骨之地副本队伍*/
	public final static short MAIGU_KICK = 3508;
	/**  队长更改埋骨之地副本队伍进入需求战力*/
	public final static short MAIGU_CHANGE_STRENGTH = 3509;
	/** 队长更改埋骨之地副本队伍是否满员自动挑战*/
	public final static short MAIGU_CHANGE_TEAM_AUTO_START = 3510;
	/** 退出埋骨之地副本队伍**/
	public final static short MAIGU_TEAM_LEAVE = 3511;
	/**请求进入埋骨之地副本*/
	public final static short MAIGU_ENTER_FUBEN = 3512;
	/** （副本预进入请求）通知队伍里所有的人在指定时间戳后进入副本*/
	public final static short MAIGU_START_TEAM = 3513;
	/** 当前进入的埋骨之地副本需要击杀怪物数量*/
	//public final static short MORE_FUBEN_MONSTER_COUNT = 2313;
	/** 多人埋骨之地副本领取奖励*/
	public final static short MAIGU_PICK_REWARD = 3514;
	/** 请求退出埋骨之地副本*/
	public final static short MAIGU_LEAVE_FUBEN = 3515;
	
	/**3318 请求世界聊天频道里发一条广播 (邀请别人加入我的队伍)*/
	public final static short MAIGU_FUBEN_YAOQING_OTHER = 3518;
	
	/**3320 推送给队伍内的玩家奖励*/
	public final static short MAIGU_RESULT_TIP= 3520;	
	/**3321 复活*/
	public final static short MAIGU_FUHUO= 3521;	
	/**3322 推送告诉玩家当前进行到第几波*/
	public final static short MAIGU_BO_SHU= 3522;
	public final static short MAIGU_QUICK_JOIN= 3523;	
	

	// -------------------------成神------------------------
	/**请求成神信息*/
	public final static short CHENG_SHEN_INFO= 3550;
	/**成神称号升级*/
	public final static short CHENG_SHEN_SJ= 3551;
	/**获得神魂值*/
	public final static short CHENG_SHEN_SHZ_GET= 857;
	

	// -------------------------糖宝心纹------------------------
	/**请求心纹信息*/
	public final static short XINWEN_INFO= 3600;
	/**3601 心纹属性变化*/
	public final static short XINWEN_ATTR_CHANGE= 3601;
	/**3602 已经使用了心纹潜能丹数量*/
	public final static short XINWEN_QND_USE_NUMBER= 3602;
	/**3603 已经使用了心纹成长丹数量*/
	public final static short XINWEN_CZD_USE_NUMBER= 3603;
	/**心纹升级*/
	public final static short XINWEN_SJ= 3604;
	/**上线后推送给客户端阶数 */
	public static final short XINWEN_JIE_TO_CLIENT = 3605;
	

	// -------------------------通天之路------------------------
	/** 通天之路面板信息*/
	public static final short TONGTIAN_ROAD_INFO = 3700;
	/** 请求淬炼 */
	public static final short TONGTIAN_ROAD_CUILIAN = 3701;
	
	
	//--------------------封神之战-------------------------------
	/**3570 跨服个人竞技信息*/
	public final static short KUAFU_JINGJI_GET_INFO = 3570;
	/**3571 购买挑战次数*/
	public final static short KUAFU_JINGJI_BUY_COUNT = 3571;
	/**3572 秒挑战CD*/
	public final static short KUAFU_JINGJI_MIAO_CD = 3572;
	/**3573 挑战*/
	public final static short KUAFU_JINGJI_TIAOZHAN = 3573;
	/**3574 领取奖励*/
	public final static short KUAFU_JINGJI_RECIVE = 3574;
	/**3575 战斗结果*/
	public final static short KUAFU_JINGJI_FIGHT_ANSWER = 3575;
	/**3576 退出*/
	public final static short KUAFU_JINGJI_EXIT = 3576;
	/**3577 某排名等级的玩家*/
	public final static short KUAFU_JINGJI_RANK_PLAYERS = 3577;
	/**3578 挑战记录*/
	public final static short KUAFU_JINGJI_FIGHT_LIST = 3578;
	/**3579 排名领奖信息*/
	public final static short KUAFU_JINGJI_RECIVE_INFO = 3579;
	/**3580 主界面图标有无可领奖励*/
	public final static short KUAFU_JINGJI_CAN_RECIVE = 3580;
	/**3581 换一批可挑战者*/
	public final static short KUAFU_JINGJI_CHANGE_TARGETS = 3581;
	/**竞技挑战异常*/
	public static final short KUAFU_JINGJI_FIGHT_ERROR = 3582;
	
	//-------------------------转生--------------------------------
	/**3962 请求兑换修为值*/
	public static final short ZHUANSHENG_DUIHUAN_XIUWEI = 3692;
	/**3693 请求升级转生*/
	public static final short ZHUANSHENG_LEVEL_UP = 3693;
	
	//单服充值排名
	/**3700 请求拉取跨服充值排名数据*/
	public static final short GET_DANFU_CHARGE_RANK_INFO = 10470;
	/**3701 请求拉取跨服充值我的信息*/
	public static final short GET_DANFU_CHARGE_MY_INFO = 10471;
	
	//_________________________台湾___________________________
	/**20300  主播发起消息*/
	public static final short TAIWAN_ZHUBO_XIAOXI = 20300;

	//-------------------------越南
	/**请求邀请好友基本信息*/
	public static final short YUENAN_GET_INFO = 20320;
	/**请求领取邀请好友奖励*/
	public static final short YUENNA_LINGQU_JITEM = 20321;
	/**拉取今日已邀请好友id列表*/
	public static final short LAQU_YAOQING_ID = 20322;
	/**存储今日已邀请好友id列表*/
	public static final short CUNCHU_YAOQING_ID = 20323;
	
	//-------------------------促销--------------------------------
	/**3750 推送已领的任务id*/
	public static final short CUXIAO_NOTICE_TO_CLIENT = 3750;
	/**3751 领取*/
	public static final short CUXIAO_GET_REWARD = 3751;
	//-------------------------门派炼狱boss--------------------------------
	/**1427 请求已通关关卡数*/
	public static final short LIANYU_BOSS_CURRENT_INFO = 1427;
	/**1428 请求关卡信息*/
	public static final short LIANYU_BOSS_TONGGUAN_INFO = 1428;
	/**1429 请求挑战*/
	public static final short LIANYU_BOSS_ENTER = 1429;
	/**1430 退出挑战*/
	public static final short LIANYU_BOSS_EXIT = 1430;
	/**1431 挑战结果*/
	public static final short LIANYU_BOSS_SUCCESS = 1431;

	//-------------------------鲜花--------------------------------
	/**送花*/
	public static final short FLOWER_SEND = 3760 ;
	/**收花*/
	public static final short FLOWER_RECEIVE = 3761 ;
	/**推送鲜花全服公告*/
	public static final short FLOWER_NOTICE_ALL = 3762;
	/**测试*/
	public static final short FLOWER_TEST = 3763;
	/**测试*/
	public static final short FLOWER_TEST_2 = 3764;
	
	/**10500 请求购买鲜花*/
	public static final short FLOWER_RFB_BUY = 10500;
	/**10501请求排行榜*/
	public static final short FLOWER_RFB_PANEL_INFO = 10501;
	//-------------------------热发布在线奖励--------------------------------
	/**10490 请求在线奖励面板数据*/
	public static final short RFB_ONLINE_REWARDS_INFO = 10490;
	/**10491 领取在线奖励*/
	public static final short RFB_ONLINE_REWARDS_GET = 10491;
	//-------------------------qq管家奖励--------------------------------
	/**请求电脑管家礼包领取状态*/
	public static final short TENCENT_GUANJIA_INFO = 20250;
	/**  请求领取新手礼包*/
	public static final short TENCENT_GUANJIA_FIRST = 20251;
	/**请求领取每日登录礼包*/
	public static final short RFB_ONLINE_REWARDS_DAY = 20252;
	
	//-------------------------跨服世界boss--------------------------------
	/**跨服世界boss进入*/
	public final static short ENTER_KUAFU_BOSS= 2156;
	/**跨服世界boss退出*/
	public final static short EXIT_KUAFU_BOSS = 2160;
	/**跨服世界boss排行榜*/
	public final static short KUAFU_BOSS_RANK = 2157;
	/**地图内获得经验*/
	public final static short KUAFU_BOSS_EXP_ADD = 2158;
	/**BOSS死亡*/
	public final static short KUAFU_BOSS_DEAD = 2159;
	/**复活*/
	public static final short KUAFU_BOSS_FUHUO = 2161;
	/**跨服boss显示loading*/
	public static final short KUAFU_BOSS_SHOW_LOADING = 2162;
	/**跨服boss取消loading*/
	public static final short KUAFU_BOSS_CANCEL_LOADING = 2163;
	/**关闭跨服世界boss伤害榜*/
	public static final short KUAFU_BOSS_RANK_CLOSE = 2164;
	//-------------------------跨服群仙宴-------------------------------
	/**跨服世界boss进入*/
	public final static short ENTER_KUAFU_QUNXIANYAN= 3123;
	/**跨服世界boss退出*/
	public final static short EXIT_KUAFU_QUNXIANYAN = 3124;
	/**地图内获得经验*/
	public final static short KUAFU_QUNXIANYAN_EXP_ADD = 3131;
	/**复活*/
	public static final short KUAFU_QUNXIANYAN_FUHUO = 3128;
	/**跨服boss显示loading*/
	public static final short KUAFU_QUNXIANYAN_SHOW_LOADING = 3121;
	/**跨服boss取消loading*/
	public static final short KUAFU_QUNXIANYAN_CANCEL_LOADING = 3122 ;
	/**推送积分榜变化(10秒一同步)*/
	public final static short KUAFU_QUNXIANYAN_PAIHENG = 3125;
	/**主角积分变化*/
	public final static short KUAFU_QUNXIANYAN_USER_INFO = 3126;
	/**跨服群仙宴排行榜 TODO*/
	public final static short KUAFU_QUNXIANYAN_RANK = 3127;
	/**群仙宴结算面板  TODO*/
	public final static short KUAFU_QUNXIANYAN_JIESUAN = 3129;
	/**领取奖励  TODO*/
	public final static short KUAFU_QUNXIANYAN_LINGQU = 3130;
	//------------------------跨服大乱斗--------------------------
	/**请求跨服大乱斗报名状态*/
	public final static short GET_KUAFU_LUANDOU_BAOMING_INFO = 3971;
	/**请求跨服大乱斗海选状态*/
	public final static short GET_KUAFU_LUANDOU_HAIXUAN_INFO = 3972;
	/**请求报名大乱斗*/
	public final static short GET_KUAFU_LUANDOU_BAOMING = 3974;
	/**跨服boss显示loading*/
	public static final short KUAFU_LUANDOU_SHOW_LOADING = 3975;
	/**跨服boss取消loading*/
	public static final short KUAFU_LUANDOU_CANCEL_LOADING = 3976;
	/**请求进入跨服大乱斗房间*/
	public final static short ENTER_KUAFU_LUANDOU = 3977;
	/**跨服大乱斗房间退出*/
	public final static short EXIT_KUAFU_LUANDOU = 3978;
	/**推送积分榜变化(10秒一同步)*/
	public final static short KUAFU_LUANDOU_PAIHENG = 3979;
	/**主角积分变化*/
	public final static short KUAFU_LUANDOU_USER_INFO = 3980;
	/**复活*/
	public static final short KUAFU_LUANDOU_FUHUO = 3981;
	/** 大乱斗结束后每小组排名 **/
    public static final short KUAFU_LUANDOU_GROUP_RANK = 3982;
    
    // -------------------------跨服巅峰之战--------------------------------
    /** 获取巅峰之战信息 **/
    public static final short KUAFU_DIANFENG_GET_INFO = 3983;
    /** 请求进入跨服巅峰之战房间 **/
    public final static short ENTER_KUAFU_DIANFENG = 3984;
    /** 退出跨服巅峰之战房间 **/
    public final static short EXIT_KUAFU_DIANFENG = 3985;
    /** 推送新一轮比赛开始通知  **/
    public final static short KUAFU_DIANFENG_LOOP_NOTICE = 3986;
    /** 推送小场比赛结果  **/
    public final static short KUAFU_DIANFENG_RESULT = 3987;
    /** 推送比赛直接胜利结果  **/
    public final static short KUAFU_DIANFENG_WINNER = 3988;
    /** 推送新一场比赛开始通知  **/
    public final static short KUAFU_DIANFENG_ONCE_NOTICE = 3989;
    
	//-------------------------妖神附魔--------------------------------
	/**妖神附魔信息*/
	public static final short YAOSHEN_FUMO_INFO = 3850;
	/**妖神附魔升级*/
	public static final short YAOSHEN_FUMO_SJ = 3851;
	/**妖神附魔属性变化*/
	public static final short YAOSHEN_FUMO_ATTR_CHANGE = 3852;
	/**客户端请求妖神附魔面板信息*/
	public static final short YAOSHEN_FUMO_PANEL_INFO = 3853;
	
	//-------------------------画卷--------------------------------
	/** 请求画卷合成*/
	public static final short HUAJUAN_HECHENG = 3880;
	/**3883 请求身上的画卷基本信息*/
	public static final short HUAJUAN_GET_INFO = 3883;
	/**3884 请求装备画卷到身上*/
	public static final short HUAJUAN_UP = 3884;
	/**3885 请求卸下画卷*/
	public static final short HUAJUAN_DOWN = 3885;
	/**3886 画卷面板总属性变化*/
	public static final short HUAJUAN_ATTR = 3886;
	/**3888 请求分解画卷*/
	public static final short HUAJUAN_FENJIE = 3888 ;
	/**3888 请求画卷升级*/
	public static final short HUAJUAN_SHENGJI = 3889;
	/**3888 推送画卷库存经验*/
	public static final short TUISONG_HUAJUAN_KUCUN_EXP = 3890;
	//---------------------团购活动------------------------------
	/**请求购买特惠物品 */
	public static final short TUANGOU_BUY = 10510;
	/**全服开奖*/
	public static final short TUANGOU_TUISONG_CILENT = 10511;
	/**请求全服购买次数*/
	public static final short TUANGOU_GQT_SCOUNT = 10512;
	
	//登陆活动
	/**请求领取奖励状态*/
	public static final short GET_REFABU_LOGIN_INFO = 10520;
	/**请求领取奖励*/
	public static final short LINGQU_REFABU_LONG  = 10521;
	
	
	//---------------------幻化------------------------------
	/**3951 请求幻化*/
	public static final short HUANHUA= 3951;
	/**3952 请求激活*/
	public static final short HUANHUA_ACTIVATE= 3952;
	/**3953 请求幻化信息*/
	public static final short HUANHUA_INFO= 3953;
	
	//----------------------转职-----------------------
	/***请求转职*/
	public static final short ZHUANZHI = 4000;
	//----------------------慧眼识金--------------------
	/**10550 请求挖矿*/
	public static final short HUIYAN_WA_KUANG = 10550;
	/**10551 推送公告*/
	public static final short HUIYAN_TUISONG = 10551;
	
	//---------------------五行------------------------
	/**4100 推送五行附体ID*/
	public static final short TUISONG_FUTI = 4100;
	/**4101 请求已获得的五行信息*/
	public static final short GET_WUXING_INFO = 4101;
	/**4102 请求激活五行*/
	public static final short QINGQIU_WUXING_JIHUO = 4102;
	/**4103 请求升级五行*/
	public static final short QINGQIU_WUXING_SHENGJI = 4103;
	/**4104 请求五行附体*/
	public static final short QINGQIU_WUXING_FUTI = 4104;
	/**请求指定五行祝福值和清零时间*/
	public static final short QINGQIU_WUXING_ZFZ = 4105;
	//---------------------五行技能------------------------
	/**请求获取玩家技能信息*/
	public static final short GET_WX_SKILL_INFO = 4107;
    /**请求升级魔神技能*/
    public static final short UPLEVEL_WX_SKILL = 4108;
	//---------------------五行精魄-----------------------
    /**获取五行精魄背包的精魄信息**/
    public static final short WX_JP_GET_BAG = 4110;
    /**获取五行精魄身上的精魄信息**/
    public static final short WX_JP_GET_BODY = 4111;
    /**开启五行魔神背包的格位**/
    public static final short WX_JP_OPEN_SLOT_BAG = 4112;
    /**合并五行精魄**/
    public static final short WX_JP_MERGE = 4113;
    /**镶嵌五行精魄**/
    public static final short WX_JP_PUT_ON = 4114;
    /**卸下五行精魄**/
    public static final short WX_JP_TAKE_OFF = 4115;
    /**普通获取五行精魄**/
    public static final short WX_JP_GET_NORMAL = 4116;
    /**窥探天机**/
    public static final short WX_JP_KTTJ = 4117;
    /**一键获取五行精魄**/
    public static final short WX_JP_GET_AUTO = 4118;
    /**五行精魄精华兑换五行精魄精魄**/
    public static final short WX_JP_EXCHANGE = 4119;
    /**开启魔神身上的格位**/
    public static final short WX_JP_OPEN_SLOT_BODY = 4120;
    //---------------------糖宝五行魔神------------------------
    /**4101 请求已获得的糖宝五行信息*/
    public static final short GET_TB_WUXING_INFO = 4121;
    /**4102 请求激活糖宝五行*/
    public static final short QINGQIU_TB_WUXING_JIHUO = 4122;
    /**4103 请求升级糖宝五行*/
    public static final short QINGQIU_TB_WUXING_SHENGJI = 4123;
    /**请求指定糖宝五行祝福值和清零时间*/
    public static final short QINGQIU_TB_WUXING_ZFZ = 4124;
    //---------------------糖宝五行魔神技能------------------------
    /**请求获取糖宝五行魔神技能信息*/
    public static final short GET_TB_WX_SKILL_INFO = 4125;
    /**请求升级糖宝五行魔神技能*/
    public static final short UPLEVEL_TB_WX_SKILL = 4126;
    
    // ---------------------------------心魔系统(4250-4254)--------------------------------
    /** 请求心魔系统信息 **/
    public static final short XINMO_GET_INFO = 4250;
    /** 请求升级心魔等级 **/
    public static final short XINMO_SHENGJI = 4251;
    /** 请求突破心魔时期 **/
    public static final short XINMO_TUPO = 4252;
    /** 请求凝神心魔境界 **/
    public static final short XINMO_NINGSHEN = 4253;
    /** 推送刷新心魔心神力变化 **/
    public static final short XINMO_REFRESH_EXP = 4254;
    
    // ---------------------------------心魔系统-天炉炼丹(4350-4356)--------------------------------
    /** 请求炼丹信息 **/
    public static final short XM_LIANDAN_GET_INFO = 4350;
    /** 请求升级炼丹等级 **/
    public static final short XM_LIANDAN_SHENGJI = 4351;
    /** 推送通知炼丹产出 **/
    public static final short XM_LIANDAN_PRODUCE = 4352;
    /** 请求开启炼丹 **/
    public static final short XM_LIANDAN_OPEN = 4353;
    /** 请求取出丹药 **/
    public static final short XM_LIANDAN_TOOK_OUT = 4354;
    /** 请求整理丹药仓库 **/
    public static final short XM_LIANDAN_SORT_DANLI = 4355;
    /**  推送通知剩余空的格位 **/
    public static final short XM_LIANDAN_NOTIFY_SOLT = 4356;
    
    // ---------------------------------心魔系统-祸世魔神(4357-4365)--------------------------------
    /** 4357 推送心魔-魔神噬体ID **/
    public static final short XM_MOSHEN_SNED_SHITI_ID = 4357;
    /** 4358 请求已获得的心魔-魔神信息 **/
    public static final short XM_MOSHEN_GET_INFO = 4358;
    /** 4359 请求激活心魔-魔神 **/
    public static final short XM_MOSHEN_ACTIVATE = 4359;
    /** 4360 请求升级心魔-魔神 **/
    public static final short XM_MOSHEN_UP_LEVEL = 4360;
    /** 4361 请求噬体心魔-魔神 **/
    public static final short XM_MOSHEN_SHITI = 4361;
    /** 4362 请求心魔-魔神祝福值信息 **/
    public static final short XM_MOSHEN_BLESS_INFO = 4362;
    /** 4363 推送心魔-魔神噬体解体ID **/
    public static final short XM_MOSHEN_SNED_JIETI_SHITI_ID = 4363;
    
    
    // ---------------------------------心魔系统-心魔副本(4364-4368)--------------------------------
    /** 4364 请求心魔副本信息**/
    public static final short XM_FUBEN_GET_INFO = 4364;
    /** 4365 请求进入心魔副本**/
    public static final short XM_FUBEN_ENTER = 4365;
    /** 4366 请求退出心魔副本**/
    public static final short XM_FUBEN_EXIT = 4366;
    /** 4367 推送心魔副本通关**/
    public static final short XM_FUBEN_FINISH = 4367;
    /** 4364 推送刷新心魔副本腐化度**/
    public static final short XM_FUHUA_REFRESH = 4368;
    
    //充值轮盘
    /**轮盘公告*/
    public static final short LUNPAN_SYSTEM_NOTIFY = 10560;
    /**请求转*/
    public static final short LUNPAN_ZHUAN = 10561;
    /**请求兑换*/
    public static final short LUNPAN_DUIHUAN = 10562;
    /**请求轮盘抽奖信息*/
    public static final short LUNPAN_GET_INFO = 10563;
    
    //首冲返利
    /**获取首充返利元宝面板信息 **/
    public static final short FIRST_CHARGE_REBATE_INFO = 10570;
    /**领取首充返利奖励**/
    public static final short FIRST_CHARGE_REBATE_RECEIVE = 10571;
    
    //绝版礼包
    /** 请求领取绝版礼包 **/
    public static final short JUEBAN_RECEIVE = 10575;
    
    // ---------------------------------心魔系统-魔神技能(4369-4370)--------------------------------
    /** 请求心魔已学技能 **/
    public static final short XM_SKILL_GET_INFO = 4369;
    /** 请求升级心魔技能 **/
    public static final short XM_SKILL_UP_LEVEL = 4370;
    
    //-----------------------------------心魔深渊副本(4371-4377)------------------------------------------
    /** 请求心魔深渊面板信息  **/
    public static final short XM_SHENYUAN_GET_INFO = 4371;
    /** 请求进入心魔深渊副本  **/
    public static final short XM_SHENYUAN_ENTER = 4372;
    /** 请求退出心魔深渊副本  **/
    public static final short XM_SHENYUAN_EXIT = 4373;
    /** 推送心魔深渊副本挑战成功  **/
    public static final short XM_SHENYUAN_FINISH = 4374;
//    /** 请求领取心魔深渊副本奖励  **/
//    public static final short XM_SHENYUAN_RECEIVE = 4375;
    /** 请求清除心魔深渊副本冷却cd **/
    public static final short XM_SHENYUAN_CLEAR_CD = 4376;
    /** 推送心魔深渊副本cd结束  **/
    public static final short XM_SHENYUAN_SEND_CD_OVER = 4377;
    
    // ---------------------------------心魔系统-洗练(4378-4380)--------------------------------
    /** 请求心魔洗练面板信息  **/
    public static final short XM_XILIAN_GET_INFO = 4378;
    /** 开始心魔洗练  **/
    public static final short XM_XILIAN_BEGIN = 4379;
    /** 心魔洗练替换  **/
    public static final short XM_XILIAN_REPLACE = 4380;
    
    //-----------------------------------心魔斗场副本(4381-4377)------------------------------------------
    /** 请求心魔斗场面板信息  **/
    public static final short XM_DOUCHANG_GET_INFO = 4381;
    /** 请求进入心魔斗场副本  **/
    public static final short XM_DOUCHANG_ENTER = 4382;
    /** 请求退出心魔斗场副本  **/
    public static final short XM_DOUCHANG_EXIT = 4383;
    /** 推送心魔斗场副本挑战击杀怪物信息  **/
    public static final short XM_DOUCHANG_FINISH = 4384;
    /** 请求购买魔斗场副本挑战次数 **/
    public static final short XM_DOUCHANG_BUY_COUNT = 4385;
    
    //------------------------画卷2-----------------------------
    /**4420 请求激活画卷2*/
    public static final short HUAJUAN2_ACTIVE = 4501;
    /**4421 请求身上的画卷2基本信息*/
    public static final short HUAJUAN2_INFO = 4502;
    /**4422 请求装备画卷2到身上*/
    public static final short HUAJUAN2_UP = 4503;
    /**4423 请求卸下画卷2*/
    public static final short HUAJUAN2_DOWN = 4504;
    /**4424 画卷2面板总属性变化*/
    public static final short HUAJUAN2_ATTR_CHANGE = 4505;
    /**4425 请求分解画卷2*/
    public static final short HUAJUAN2_FENJIE = 4506;
    /**4426 请求画卷2升星*/
    public static final short HUAJUAN2_UPGRADE = 4507;
 
    //------------------------灵火祝福---------------------------
    /** 2852 灵火祝福:请求基本信息 **/
    public static final short LINGHUO_BLESS_INFO = 2852;
    /** 2853 灵火祝福:请求镶嵌 **/
    public static final short LINGHUO_BLESS_PUT_ON = 2853;
    /** 2854 灵火祝福:推送加成属性 **/
    public static final short LINGHUO_BLESS_SEND_ATTRS = 2854;
    
    //-----------------------跨服-云宫之巅----------------------
    /**
     * 4531 请求活动结果数据
     */
    public static final short KF_YUNGONG_RESULT_INFO = 4531;
    /**
     * 4532 请求进入活动场景
     */
    public static final short KF_YUNGONG_ENTER = 4532;
    /**
     * 4533 请求领取活动奖励
     */
    public static final short KF_YUNGONG_RECEIVE = 4533;
    /**
     * 4534 请求采集旗子
     */
    public static final short KF_YUNGONG_COLLECT = 4534;
    /**
     * 4535 请求拔起旗子
     */
    public static final short KF_YUNGONG_PULL = 4535;
    /**
     * 4536 请求复活
     */
    public static final short KF_YUNGONG_FUOHUO = 4536;
    /**
     * 4537 活动期间获得经验真气
     */
    public static final short KF_YUNGONG_ADD_EXP_ZQ = 4537;
    /**
     * 4538 推送旗帜实时坐标
     */
    public static final short KF_YUNGONG_QIZI_POSITION = 4538;
    /**
     * 4539 旗子被某门派占领
     */
    public static final short KF_YUNGONG_QIZI_CHANGE = 4539;
    /**
     * 4540 活动结束通知
     */
    public static final short KF_YUNGONG_END = 4540;
    /**
     * 4541 请求退出活动场景
     */
    public static final short KF_YUNGONG_EXIT = 4541;
    /**
     * 4542 角色是否拥有皇城套装
     */
    public static final short KF_YUNGONG_CLOTHES_SHOW = 4542;
    /**
     * 4543推送可参赛公会活动开始
     */
    public static final short KF_YUNGONG_BEGIN_NOTICE = 4543;
    
    
    //------------------------------------魔宫猎焰(4551-4556)--------------------------------------
    
    /**
     * 护盾值初始化 4551
     */
    public static final short MGLY_INIT_INFO = 4551;
    /**
     * 购买灵火之芯 4552
     */
    public static final short MGLY_BUY_LHZX = 4552;
    /**
     * 请求进入魔宫猎焰 4553
     */
    public static final short MGLY_ENTER_STAGE = 4553;
    /**
     * 请求退出魔宫猎焰 4554
     */
    public static final short MGLY_EXIT_STAGE = 4554;
    /**
     * 推送刷新场景中存活的boss信息 4555
     */
    public static final short MGLY_REFRESH_MONSTER_INFO = 4555;
    
    //------------------------------------仙器觉醒(4561-4570)--------------------------------------
    /**
     * 请求初始化数据 4561
     */
    public static final short XQJX_INFO = 4561;
    /**
     * 请求升级仙洞 4562
     */
    public static final short XQJX_UPLEVEL = 4562;
    /**
     * 请求觉醒仙器 4563
     */
    public static final short XQJX_JUEXING = 4563;
    
    //------------------------------------仙器副本(云瑶晶脉)(4564-4570)--------------------------------------
    /**
     * 请求进入 4564
     */
    public static final short XQFUBEN_ENTER = 4564;
    /**
     * 请求退出 4565(到时间后端主动推送)
     */
    public static final short XQFUBEN_EXIT = 4565;
    /**
     * 采集开始 4566
     */
    public static final short XQFUBEN_COLLECT = 4566;
    /**
     * 采集结束 4567
     */
    public static final short XQFUBEN_PULL = 4567;
    
    
    //---------------------------------------------仙缘飞化--------------------------
    /**
     * 请求仙缘飞化信息
     */
    public static final short XYFH_INFO=4571;
    
    /**
     * 仙缘飞化正常进阶
     */
    public static final short XYFH_NORMAL_UPGRADE=4572;
    
    //----------------------------------------------至尊充值----------------------------
    /**
     * 请求领取奖励
     */
    public static final short EXTREME_RECHARGE_RECEIVE_REWARD = 10580;
    /**
     * 充值信息
     */
    public static final short EXTREME_RECHARGE_RECHARGE_CHANGE = 10581;
    
  //------------------------------个人boss
  	/**4590 请求个人BOSS信息*/
	public static final short PERSONAL_BOSS_INFO = 4590;
	/**4591  请求进入个人BOSS副本*/
	public static final short PERSONAL_BOSS_ENTER = 4591;
	/**4592  弹结算面板*/
	public static final short PERSONAL_BOSS_RESULT = 4592;
	/**4593  退出副本*/
	public static final short PERSONAL_BOSS_EXIT = 4593;
	/**4594  主動領獎*/
	public static final short PERSONAL_BOSS_REWARD = 4594;
	/** boss积分信息*/
	public static final short BOSS_JIFEN_INFO= 4595;
	/** boss积分请求升级*/
	public static final short BOSS_JIFEN_UPGRADE= 4596;
	/** boss积分变化推送*/
	public static final short BOSS_JIFEN_CHANGE = 4597;
	
	
  //-------------------------圣剑------------------------
	/** 圣剑阶级信息*/
	public static final short WUQI_JJ_LEVE = 5001;
	/** 角色圣剑面板信息*/
	public static final short WUQI_SHOW = 5002;
	/** 角色圣剑正常进阶*/
	public static final short WUQI_JJ_UP = 5003;
	/** 角色圣剑自动进阶*/
	public static final short WUQI_AUTO_UP = 5004;
	/** 角色圣剑形象切换 */
	public static final short WUQI_UPDATE_SHOW = 5005;
	/** 已经使用了圣剑潜能丹数量 */
	public static final short WUQI_QND_NUM = 5007;
	/** 已经使用了圣剑成长丹数量 */
	public static final short WUQI_CZD_NUM = 5008;
	/** 推送圣剑属性变化 */
	public static final short WUQI_ATTR_CHANGE = 5009;
	/** 推送圣剑外显示变化 */
	public static final Short WUQI_SHOWID = 5010;
	
  //------------------------基金购买----------------------
    /**2801 拉取基金数据**/
    public static final short GET_JIJIN_DATA = 5101;
    /**2802 基金购买**/
    public static final short JIJIN_PLAN = 5102;
    /**2803 领取基金奖励**/
    public static final short RECEVIE_JIJIN = 5103;
   //-------------------------集合ICON------------
    /**推线前端今天是第一次登录*/
    public static final short JIHE_ICON_TUISONG = 5401;
    
  //------------------------限时礼包（5300 - 5350）----------------------
    /**5300 获取限时礼包信息**/
    public static final short GET_LIMIT_LIBAO = 5300;
    /**5301 购买限时礼包**/
    public static final short BUY_SHOP_LIMIT = 5301;
    /**5302 推送激活的限时礼包信息(登陆上线时或开启时推送，没有则不推)**/
    public static final short TUISONG_INFO = 5302;
    /**5303 推送当前时间段累计充值元宝 */
    public static final short LEIJI_GOLD = 5303;
    
    
    //------------------------在线礼包（5351 - 5360）-----------------
	/** 获取限时在线信息*/
	public static final short AWARD_ONLINE_INFO = 5351;
	/** 请求领取某个奖励*/
	public static final short LINGQU_ONLINE = 5352;
	
  //------------------------单笔充值10611-10615----------------------
	public static final short RECEVIE_ACTIVITY_ONCECHONG = 10611;
	/**更新单笔充值数值*/
    public static final short UPDATE_ONCECHONG = 10612;
    
    //------------------------星空宝藏----------------------
    /**领取奖励*/
    public static final short XINGKONGBAOZANG_LINGQU = 10621 ;
    public static final short UPDATE_XINGKONGBAOZHAN = 10620 ;
    
    
    //-------------------------修炼之路----------------------
    /**5410请求修炼之路的数据 */
    public static final short XIULIAN_GET_INFO = 5410;
    /**5411修炼之路积分改变 */
    public static final short XIULIAN_JIFEN_CHARGE = 5411;
    /**5412积分兑换物品 */
    public static final short XIULIAN_JIFEN_DUIHUAN = 5412;
    /**5413 领取每日奖励 */
    public static final short XIULIAN_LINGQU_DAY = 5413;
    /**5414 积分兑换称号 */
    public static final short XIULIAN_LINGQU_CHENGHAO = 5414;
    /**5415 领取积分 */
    public static final short XIULIAN_LINGQU_JIFEN = 5415;
    
    //-------------------------炼金----------------------
    public static final short LJ_SEND_LINFO = 5470 ;
	public static final short LJ_OPT = 5471;
	public static final short LJ_GET_AWARD = 5472;
	
	//-------------------------53充值返利----------------------
	public static final short RECEVIE_DAY_LEICHONG53GOLD = 10630;
	public static final short RECEVIE_DAY_LEICHONG53ITEM = 10631;
    public static final short TUI_SONG_RECHARGE53_YB = 10632;
    
    
}