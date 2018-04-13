package com.junyou.bus.rfbactivity.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 热发布子活动类型
 * @author DaoZheng Yuan
 * 2015年5月19日 下午4:33:50
 */
public class ReFaBuUtil {
	
	/**
	 * 1:首充常量
	 */
	public static final int SOUCHONG_TYPE = 1;
	public static final String SOUCHONG_NAME = "ShouChong";
	
	/**
	 * 2:开服等级类型常量
	 */
	public static final int RFB_LEVEL_TYPE = 2;
	/**
	 * 等级文 件名常量
	 */
	public static final String RFB_LEVEL_NAME = "ChongLevel";
	
	/**
	 * 3:修仙礼包类型常量
	 */
	public static final int XIUXIAN_LB_TYPE = 3;
	/**
	 * 修仙礼包文件名常量
	 */
	public static final String XIUXIAN_LB_NAME = "XiuXianLiBao";
	
	/**御剑飞行*/
	public static final int YUJIAN_FEIXING_TYPE = 4;
	public static final String YUJIAN_FEIXING_NAME = "YuJianFeiXian";
	/**战力比拼*/
	public static final int ZHANLI_BIPIN_TYPE = 5;
	public static final String ZHANLI_BIPIN_NAME = "ZhanLiBiPin";
	/**翅膀排名*/
	public static final int XIANJIE_YUYI_TYPE = 6;
	public static final String XIANJIE_YUYI_NAME = "XianJieYuYi";
	/**仙界竞技*/
	public static final int XIANJIE_JINGJI_TYPE = 7;
	public static final String XIANJIE_JINGJI_NAME = "XianJieJingJi";
	/**仙装强化*/
	public static final int XIANZHUANG_QH_TYPE = 8;
	public static final String XIANZHUANG_QH_NAME = "XianZhuangQiangHua";
	/**棋盘*/
	public static final int QI_PAN_TYPE = 9;
	public static final String QI_PAN_NAME = "QiPan";
	/**累充*/
	public static final int LEI_CHONG_TYPE = 10;
	public static final String LEI_CHONG_NAME = "LeiChong";
	/**糖宝仙剑*/
	public static final int TANG_BAO_TYPE = 11;
	public static final String TANG_BAO_NAME = "TangBaoXianJian";
	/**神秘商店*/
	public static final int SHENMI_SHANGDIAN_TYPE = 12;
	public static final String SHENMI_SHANGDIAN_NAME = "ShenMiShangDian";
	
	/**消费排名*/
	public static final int XIAOFEI_PAIMING_TYPE = 13;
	public static final String XIAOFEI_PAIMING_NAME = "XiaoFeiPaiMing";
	
	/**命运轮盘*/
	public static final int ZHUAN_PAN_TYPE = 14;
	public static final String ZHUAN_PAN_NAME = "MingYunLunPan";
	/**糖宝战甲*/
	public static final int ZHAN_JIA_TYPE = 15;
	public static final String ZHAN_JIA_NAME = "TangBaoZhanJia";

	/**盗墓手札*/
	public static final int DAOMO_SHOUZHA_TYPE = 16;
	public static final String DAOMO_SHOUZHA_NAME = "DaoMuShouZha";
	

	/**连充活动*/
	public static final int LIAN_CHONG_TYPE = 17;
	public static final String LIAN_CHONG_NAME = "LianChong";
	
	/**锁妖塔活动*/
	public static final int SUOYAOTA_TYPE = 18;
	public static final String SUOYAOYA_NAME = "CangBaoGe";
	
	/**超值兑换*/
	public static final int SUPER_DUIHUAN_TYPE = 19;
	public static final String SUPER_DUIHUAN_NAME = "SuperDuihuan";
	
	/**活动副本*/
	public static final int ACTIVE_MAP_TYPE = 20;
	public static final String ACTIVE_MAP_NAME = "HuoDongMap";
	
	/**热发布图片公告*/
	public static final int PIC_NOTICE_TYPE = 21;
	public static final String PIC_NOTICE_NAME = "HuoDongYuGao";
	
	/**跨服充值排名活动*/
	public static final int KUAFU_CHARGE_RANK_TYPE = 23;
	public static final String KUAFU_CHARGE_RANK_NAME = "KuafuChargeRank";
	
	/**欢乐卡牌活动*/
	public static final int HAPPY_CARD_TYPE = 25;
	public static final String HAPPY_CARD_NAME = "HappyCard";
	
	/**充值返利*/
	public static final int CHONGZHI_FANLI_TYPE = 24;
	public static final String CHONGZHI_FANLI_NAME = "ChongZhiFanLi";

	/**妖神排名*/
	public static final int YAOSHEN_JINJIE_TYPE = 26;
	public static final String YAOSHEN_JINJIE_NAME = "YaoShenJinJie";
	
	/**累计消耗*/
	public static final int LEIHAO_TYPE = 27;
	public static final String LEIHAO_NAME = "LeiJiXiaoHao";
	/**拉霸活动*/
	public static final int LABA_TYPE = 28;
	public static final String LABA_NAME = "LaBa";
	/**幸运彩蛋*/
	public static final int CAIDAN_TYPE = 29;
	public static final String CAIDAN_NAME = "CaiDan";
	/**妖神魔纹*/
	public static final int YAOSHENMOWEN_TYPE = 30;
	public static final String YAIOSHEN_MOWEN = "YaoShenMoWen";
	/**器灵*/
	public static final int QILING_TYPE = 31;
	public static final String QILING_NAME = "QiLingPaiHang";
	/**妖神魂魄*/
	public static final int YAOSHENHUNPO_TYPE = 32;
	public static final String YAOSHENHUNPO_NAME = "YaoShenHunPo";
	/**热发布寻宝 */
	public static final int REFABUXUNBAO_TYPE = 33;
	/**热发布寻宝文件名常量*/
	public static final String REFABUXUNBAO_NAME = "RefabuXunBao";
	/**热发布探宝 */
	public static final int REFABUTANBAO_TYPE = 34;
	public static final String REFABUTANBAO_NAME = "TanSuoBaoZang";
	/**妖神模印 */
	public static final int YAOSHENMOYIN_TYPE = 22;
	public static final String YAOSHENMOYIN_NAME = "YaoShenMoYin";
	/**糖宝心纹 */
	public static final int TANGBAOXINWEN_TYPE = 36;
	public static final String TANGBAOXINWEN_NAME = "TangBaoXinWen";
	
	/**单服充值排名活动*/
	public static final int DANFU_CHARGE_RANK_TYPE = 35;
	public static final String DANFU_CHARGE_RANK_NAME = "DanfuChargeRank";
	/**在线奖励*/
	public static final int ONLINE_REWARDS_TYPE = 37;
	public static final String ONLINE_REWARDS_NAME = "OnlineRewards";
	/**热发布鲜花排行榜*/
	public static final int FLOWER_CHARM_RANK_TYPE = 38;
	public static final String FLOWER_CHARM_RANK_NAME = "FlowerCharmRank";
	/**:团购*/
	public static final int TUANGOU_TYPE = 39;
	public static final String TUANGOU_NAME = "TuanGou";
	/**:登录*/
	public static final int RFBLOGIN_TYPE = 40;
	public static final String RFBLOGIN_NAME = "QiDeng";
	/** 团购秒杀 */
	public static final int MIAOSHA_TYPE = 41;
	public static final String MIAOSHA_NAME = "TuanGouMiaoSha";
	/** 团购秒杀 */
	public static final int LAOWANJIA_TYPE = 42;
	public static final String LAOWANJIA_NAME = "LaoWanJiaHuiGui";
	/**慧眼识金*/
	public static final int HUIYANSHIJING_TYPE = 43;
	public static final String HUIYANSHIJING_NAME = "HuiYanShiJin";
    /**充值轮盘*/
    public static final int LUNPAN_TYPE = 44;
    public static final String LUNPAN_NAME = "ChongZhiLunPan";
    /**首冲返利*/
    public static final int FIRST_CHARGE_REBATE_TYPE = 45;
    public static final String FIRST_CHARGE_REBATE_NAME = "ShouChongFanLi";
    /**绝版礼包*/
    public static final int JUEBAN_REBATE_TYPE = 46;
    public static final String JUEBAN_REBATE_NAME = "JueBanLiBao";
    /**至尊充值*/
    public static final int EXTREME_RECHARGE_TYPE = 47;
    public static final String EXTREME_RECHARGE_NAME = "ZhiZunChongZhi";
    /**至尊充值*/
    public static final int EXTREME_XIAOFEI_TYPE = 48;
    public static final String EXTREME_XIAOFEI_NAME = "KuaFuXiaoFeiPaiMing";
    
    /**
	 * 每日充值常量
	 */
	public static final int LOOP_DAY_CHONGZHI_TYPE = 49;
	public static final String LOOP_DAY_CHONGZHI_NAME = "LoopDayChongZhi";
	
	/**
	 * 圣剑排行
	 */
	public static final int SHENG_JIAN_RANK = 50;
	public static final String SHENG_JIAN_RANK_NAME = "ShengJianRank";
	
	/**单笔充值*/
	public static final int ONCE_CHONG_TYPE = 51;
	public static final String ONCE_CHONG_NAME = "OnceChong";
	
	/**星空宝藏*/
	public static final int XINGKONG_TYPE = 52;
	public static final String XINGKONG_NAME = "ZhaoHuanShenLong";
	
	/**充值返钻*/
	public static final int CZFZ_TYPE = 53;
	public static final String CZFZ_NAME = "XinChongzhiFanzuan";
	
	/**新拉霸*/
	public static final int LB_TYPE = 54;
	public static final String LB_NAME = "ZhanBuQiu";
	
	
	public static Map<Integer,String> RFB_MAP = new HashMap<Integer, String>();
	static{
		//首充
		RFB_MAP.put(SOUCHONG_TYPE, SOUCHONG_NAME);
		RFB_MAP.put(RFB_LEVEL_TYPE, RFB_LEVEL_NAME);//2.开服等级
		RFB_MAP.put(XIUXIAN_LB_TYPE, XIUXIAN_LB_NAME);//3.修仙礼包
		RFB_MAP.put(ZHANLI_BIPIN_TYPE, ZHANLI_BIPIN_NAME);//战力比拼
		RFB_MAP.put(YUJIAN_FEIXING_TYPE, YUJIAN_FEIXING_NAME);//
		RFB_MAP.put(XIANJIE_YUYI_TYPE, XIANJIE_YUYI_NAME);//
		RFB_MAP.put(XIANJIE_JINGJI_TYPE, XIANJIE_JINGJI_NAME);//
		RFB_MAP.put(XIANZHUANG_QH_TYPE, XIANZHUANG_QH_NAME);//
		RFB_MAP.put(QI_PAN_TYPE, QI_PAN_NAME);//
		RFB_MAP.put(LEI_CHONG_TYPE, LEI_CHONG_NAME);//
		RFB_MAP.put(TANG_BAO_TYPE, TANG_BAO_NAME);//
		RFB_MAP.put(SHENMI_SHANGDIAN_TYPE, SHENMI_SHANGDIAN_NAME);//
		RFB_MAP.put(XIAOFEI_PAIMING_TYPE, XIAOFEI_PAIMING_NAME);//
		RFB_MAP.put(ZHUAN_PAN_TYPE, ZHUAN_PAN_NAME);//
		RFB_MAP.put(ZHAN_JIA_TYPE, ZHAN_JIA_NAME);//
		RFB_MAP.put(LIAN_CHONG_TYPE, LIAN_CHONG_NAME);//连充奖励
		RFB_MAP.put(LABA_TYPE, LABA_NAME);//拉霸活动
		RFB_MAP.put(DAOMO_SHOUZHA_TYPE, DAOMO_SHOUZHA_NAME);//
		RFB_MAP.put(SUOYAOTA_TYPE, SUOYAOYA_NAME);//
		RFB_MAP.put(SUPER_DUIHUAN_TYPE, SUPER_DUIHUAN_NAME);//
		RFB_MAP.put(ACTIVE_MAP_TYPE, ACTIVE_MAP_NAME);//
		RFB_MAP.put(PIC_NOTICE_TYPE, PIC_NOTICE_NAME);//
		RFB_MAP.put(KUAFU_CHARGE_RANK_TYPE, KUAFU_CHARGE_RANK_NAME);//
		RFB_MAP.put(HAPPY_CARD_TYPE, HAPPY_CARD_NAME);//
		RFB_MAP.put(YAOSHEN_JINJIE_TYPE, YAOSHEN_JINJIE_NAME);//
		RFB_MAP.put(CHONGZHI_FANLI_TYPE, CHONGZHI_FANLI_NAME);//
		RFB_MAP.put(LEIHAO_TYPE, LEIHAO_NAME);//
		RFB_MAP.put(CAIDAN_TYPE, CAIDAN_NAME);//
		RFB_MAP.put(YAOSHENMOWEN_TYPE, YAIOSHEN_MOWEN);//
		RFB_MAP.put(QILING_TYPE, QILING_NAME);//
		RFB_MAP.put(YAOSHENHUNPO_TYPE, YAOSHENHUNPO_NAME);//
		RFB_MAP.put(REFABUXUNBAO_TYPE, REFABUXUNBAO_NAME);//
		RFB_MAP.put(REFABUTANBAO_TYPE, REFABUTANBAO_NAME);//
		RFB_MAP.put(YAOSHENMOYIN_TYPE, YAOSHENMOYIN_NAME);//
		RFB_MAP.put(TANGBAOXINWEN_TYPE, TANGBAOXINWEN_NAME);//
		RFB_MAP.put(DANFU_CHARGE_RANK_TYPE, DANFU_CHARGE_RANK_NAME);//
		RFB_MAP.put(ONLINE_REWARDS_TYPE, ONLINE_REWARDS_NAME);//在线奖励
		RFB_MAP.put(FLOWER_CHARM_RANK_TYPE, FLOWER_CHARM_RANK_NAME);//热发布鲜花排行榜
		RFB_MAP.put(TUANGOU_TYPE, TUANGOU_NAME);
		RFB_MAP.put(RFBLOGIN_TYPE, RFBLOGIN_NAME);
		RFB_MAP.put(MIAOSHA_TYPE, MIAOSHA_NAME);
		RFB_MAP.put(LAOWANJIA_TYPE, LAOWANJIA_NAME);
		RFB_MAP.put(HUIYANSHIJING_TYPE, HUIYANSHIJING_NAME);
		RFB_MAP.put(LUNPAN_TYPE, LUNPAN_NAME);
		RFB_MAP.put(FIRST_CHARGE_REBATE_TYPE, FIRST_CHARGE_REBATE_NAME);
		RFB_MAP.put(JUEBAN_REBATE_TYPE, JUEBAN_REBATE_NAME);
		RFB_MAP.put(EXTREME_RECHARGE_TYPE, EXTREME_RECHARGE_NAME);
		RFB_MAP.put(EXTREME_XIAOFEI_TYPE, EXTREME_XIAOFEI_NAME);
		RFB_MAP.put(LOOP_DAY_CHONGZHI_TYPE, LOOP_DAY_CHONGZHI_NAME);
		RFB_MAP.put(SHENG_JIAN_RANK, SHENG_JIAN_RANK_NAME);
		RFB_MAP.put(ONCE_CHONG_TYPE, ONCE_CHONG_NAME);
		RFB_MAP.put(XINGKONG_TYPE, XINGKONG_NAME);
		RFB_MAP.put(CZFZ_TYPE, CZFZ_NAME);
		RFB_MAP.put(LB_TYPE, LB_NAME);
		
	}
	
	
	/**
	 * 根据子活动类型获取子活动文件名前缀
	 * @param type
	 * @return 
	 */
	public static String getRfbSubPrefixName(int type){
		return RFB_MAP.get(type);
	}
	
	public static boolean isRfbType(int type){
		String name = RFB_MAP.get(type);
		if(name != null){
			return true;
		}
		
		return false;
	}
}
