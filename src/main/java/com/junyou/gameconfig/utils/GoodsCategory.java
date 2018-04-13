package com.junyou.gameconfig.utils;

import java.util.HashMap;
import java.util.Map;


/**
 * 物品类型
 *@author  DaoZheng Yuan
 *@created 2012-3-23下午1:57:21
 */
public class GoodsCategory {

	/**
	 * 物品排序类型个数
	 */
	public static int GOODS_SORT_TYPE_SIZE = 10; 
	
	/**
		
		不进背包的数值类型：
		-1：经验
		-2：银两
		-3：元宝
		-4：绑定元宝
		-5：真气
		道具类型：
		1.装备
		2.固定回血道具      
		3.百分比回血道具   
		4.银两                
		5.元宝               
		6.绑定元宝
		7.固定经验值
		8.百分比经验值
		9.真气
		10.buff类道具
		11.飞鞋传送
		12.强化石
	 */
	//------------------------------------ 以下类型是不进背包的数值类型------------------------------------------------------
	/**经验*/	
	public static final int EXP = -1;
	/**银两*/	
	public static final int MONEY = -2;
	/**元宝*/	
	public static final int GOLD = -3;
	/**绑定元宝*/	
	public static final int BGOLD = -4;
	/**真气*/	
	public static final int ZHENQI = -5;
	/**荣誉*/	
	public static final int RONGYU = -6;
	/**贡献*/	
	public static final int GONGXIAN = -7;
	/**熔炼值*/	
	public static final int RONGLIAN = -8;
	/**玄铁*/	
	public static final int XUANTIE = -9;
	/**探宝积分*/	
	public static final int TANBAO = -10;
	/**跨服竞技场功勋*/	
	public static final int GONGXUN = -11;

 
	//------------------------------------  以下类型是道具类型------------------------------------------------------
	
	/**
	 * 1：装备类型
	 */
	public static final int EQUIP_TYPE = 1;
	
	/**
	 * 2: 固定回血道具
	 */
	public static final int GD_RECOVER_BLOOD = 2;
	/**
	 * 百分比回血道具
	 */
	public static final int BFB_RECOVER_BLOOD = 3;
	/**
	 * 4 金钱（使用对应类型的物品后可以获得一定的金钱值）
	 */
	public static final int MONEY_TYPE = 4;
	
	/**
	 * 5：元宝 （使用对应类型的物品后可以获得一定的元宝值）
	 */
	public static final int GOLD_TYPE = 5;
	
	/**
	 * 6：礼券（使用对应类型的物品后可以获得一定的礼券值）
	 */
	public static final int BIND_GOLD_TYPE = 6;
	
	/**
	 * 7：固定经验
	 */
	public static final int GD_ADD_EXP = 7;
	
	/**
	 * 8：百分比经验 
	 */
	public static final int BFB_ADD_EXP = 8;
	/**
	 * 9：真气（使用对应类型的物品后可以获得一定的真气值）
	 */
	public static final int ZHEN_QI_TYPE = 9;
	
	/**
	 * 10：buff道具
	 */
	public static final int BUFF = 10;
	
	/**
	 * 11：飞鞋
	 */
	public static final int FEI_XIE = 11;
	
	/**
	 * 12：强化石
	 */
	public static final int QIANG_HUA_SHI = 12;
	
	/**
	 * 13.御剑升阶材料
	 */
	public static final int ZQ_SJ_RESOURCE = 13;
	/**
	 * 14.潜能丹（固定属性）
	 */
	public static final int ZQ_QND= 14;
	/**
	 * 15.成长丹（百分比属性
	 */
	public static final int ZQ_CZD = 15;
	
	/**
	 * 16.指定地图回城卷轴
	 */
	public static final int FIXED_CITY_BACK = 16;
	
	/**
	 * 17：打怪多倍经验     buff持续时长（分）         使用后打怪n倍经验
	 */
	public static final int MANY_BEI_EXP = 17;
	
	/**
		18：血包              生命自动恢复
	 */
	public static final int HP_POOL_TYPE = 18;
	
	/**
		19：复活石
	 */
	public static final int REVIVE_PROP = 19;
	
	/**
		20：门派令牌
	 */
	public static final int GUILD_ITEM = 20;
	
	/**
		21：随机礼包
	 */
	public static final int RANDOM_GIFT_CARD = 21;
	
	/**
		22：固定礼包
	 */
	public static final int FIXED_GIFT_CARD = 22;
	/**
		23：多选宝箱
	 */
	public static final int DUOXUAN_BOX = 23;
	
	/**24:镖令	**/
	public static final int DART_TO = 24;
	
	/**
		25:糖宝资质丹
	 */
	public static final int TANGBAO_ZIZHI = 25;
	
	/**
		26:糖宝眉心之血
	 */
	public static final int TANGBAO_MXZX = 26;
	
	/**
		27.糖宝增加属性
	 */
	public static final int TANGBAO_ADD_ATT = 27;
	/**御剑技能书*/
	public static final int YUJIAN_SKILL_BOOK = 29;
	/**糖宝技能书*/
	public static final int TANGBAO_SKILL_BOOK = 30;
	/**翅膀技能书*/
	public static final int CHIBANG_SKILL_BOOK = 31;
	/**天裳技能书*/
	public static final int TIANSHANG_SKILL_BOOK = 32;
	/**天工技能书*/
	public static final int TIANGONG_SKILL_BOOK = 33;
	/**器灵技能书*/
	public static final int QILING_SKILL_BOOK = 94;
	/**天羽技能书*/
	public static final int TIANYU_SKILL_BOOK = 123;
	/**武器技能书*/
	public static final int WUQI_SKILL_BOOK = 192;
	
	/**
		35.新手礼包
	 */
	public static final int NEW_PLAYER_GIFT = 35;
	
	/**
		38.飞升丹
	 */
	public static final int FEI_SHENG_DAN = 38;
	
	/**
		40.赎罪药水
	 */
	public static final int SHUIZUI_YAOSHUI = 40;
	
	/**
	41.改名卡
	 */
	public static final int MODIFY_NAME_CARD = 41;
	/**
		42：神器激活道具
	 */
	public static final int SHENQI_ITEM = 42;
	/**
	44：翅膀潜能丹（固定属性）
	 */
	public static final int CHIBANG_QND = 44;
	/**
	45.翅膀成长丹（百分比属性）
	 */
	public static final int CHIBANG_CZD = 45;

	/**
	    器灵潜能丹（固定属性）
	 */
	public static final int QILING_QND = 47;
	/**
	   器灵成长丹（百分比属性）
	 */
	public static final int QILING_CZD = 48;
	/**
	    天羽潜能丹（固定属性）
	 */
	public static final int TIANYU_QND = 119;
	/**
	   天羽成长丹（百分比属性）
	 */
	public static final int TIANYU_CZD = 120;
	/**
		53.仙剑潜能丹（固定属性）
	 */
	public static final int XIANJIAN_QND = 53;
	/**
		54.仙剑成长丹（百分比属性）
	 */
	public static final int XIANJIAN_CZD = 54;
	/**
		50.战甲潜能丹（固定属性）
	 */
	public static final int ZHANJIA_QND = 50;
	/**
		51.战甲成长丹（百分比属性）
	 */
	public static final int ZHANJIA_CZD = 51;
	
	/**
		56：元宝随机礼包
	 */
	public static final int YUANBAO_RANDOM_GIFT_CARD = 56;
	
	/**
		57：银两随机礼包
	 */
	public static final int YINLIANG_RANDOM_GIFT_CARD = 57;
	
	/**
		58：元宝固定礼包
	 */
	public static final int YUANBAO_FIXED_GIFT_CARD = 58;
	
	/**
		59：银两固定礼包
	 */
	public static final int YINLIANG_FIXED_GIFT_CARD = 59;
	/**
	 * 翅膀直接进阶道具
	 */
	public static final int CB_SJ_ITEM= 60;
	/**
	 * 糖宝武器|天工直接进阶道具
	 */
	public static final int TG_SJ_ITEM= 64;
	/**
	 * 坐骑直接进阶道具
	 */
	public static final int ZUOQI_SJ_ITEM= 63;
	/**
	 * 糖宝战甲|天裳直接进阶道具
	 */
	public static final int TS_SJ_ITEM= 65;
	/**
	 *器灵直接进阶道具
	 */
	public static final int QL_SJ_ITEM= 88;
	/**
	 *天羽直接进阶道具
	 */
	public static final int TY_SJ_ITEM= 125;
	
	/**
	 *妖神霸体直接升星道具
	 */
	public static final int YAOBA_SJ_ITEM= 126;
	/**
	 *妖神魔纹直接升星道具
	 */
	public static final int YAOMO_SJ_ITEM= 127;
	/**
	 *妖神魂魄直接升星道具
	 */
	public static final int YAOHUN_SJ_ITEM= 128;
	/**
	 *妖神魔印直接升星道具
	 */
	public static final int YAOYIN_SJ_ITEM= 129;
	/**
	 *糖宝心纹直接升星道具
	 */
	public static final int XINWEN_SJ_ITEM= 130;
	
	
	/**
	 * 妖神霸体基本道具
	 */
	public static final int YAOSHEN_SJ_ITEM= 67;
	
	/**
	 * 妖神霸体潜能丹（固定属性）
	 */
	public static final int  YAOSHEN_QND= 68;
	/**
	 * 妖神霸体成长丹（百分比属性）
	 */
	public static final int YAOSHEN_CZD= 69;
	/**
	 * 妖神 魔纹丹
	 */
	public static final int YAOSHEN_MOWEN_ITEM= 81;
	/**
	 *糖宝心纹丹
	 */
	public static final int XINWEN_ITEM= 105;
	/**
	 *糖宝心纹潜能丹
	 */
	public static final int XINWEN_QND= 106;
	/**
	 *糖宝心纹成长丹
	 */
	public static final int XINWEN_CZD= 107;
	

	/**
	 * 妖神魔纹潜能丹（固定属性）
	 */
	public static final int  YAOSHEN_MOWEN_QND= 82;
	/**
	 * 妖神魔纹成长丹（百分比属性）
	 */
	public static final int YAOSHEN_MOWEN_CZD= 83;
	/**
	 * 妖神精气
	 */
	public static final int YAOSHEN_HUNPO_ITEM= 84;
	/**
	 * 魄神道具类型
	 */
	public static final int YAOSHEN_HUNPO_POSHEN_ITEM= 87;
	/**
	 * 妖神魂魄潜能丹（固定属性）
	 */
	public static final int  YAOSHEN_HUNPO_QND= 85;
	/**
	 * 妖神魂魄成长丹（百分比属性）
	 */
	public static final int YAOSHEN_HUNPO_CZD= 86;

	/**
	 * 妖神魔印丹
	 */
	public static final int  YAOSHEN_MOYIN_DAN= 90;
	/**
	 * 妖神魔印潜能丹（固定属性）
	 */
	public static final int  YAOSHEN_MOYIN_QND= 91;
	/**
	 * 妖神魔印成长丹（百分比属性）
	 */
	public static final int YAOSHEN_MOYIN_CZD= 92;
	/**
	 * 成神--神魂丹
	 */
	public static final int CHENG_SHEN_SHD= 93;
	/**
	 * 限时时装
	 */
	public static final int XIANSHI_SHIZHUANG = 78;
	/**
	 * 修为道具
	 */
	public static final int ADD_XIUWEI = 95;
	/**
	 * 妖神万能进阶基本道具
	 */
	public static final int YAOSHEN_WANNENG_SJ_ITEM= 97;

	
	/**
		98.激活糖宝
	 */
	public static final int TANGBAO_ACTIVE = 98;
	
	/**
	万能潜能丹
	 */
	public static final int WANNENG_QND = 101;
	/**
	万能成长丹
	 */
	public static final int WANNENG_CZD = 102;
	/**
	妖神万能潜能丹
	 */
	public static final int YS_WANNENG_QND = 103;
	/**
	妖神万能成长丹
	 */
	public static final int YS_WANNENG_CZD = 104;
	/**
	 * 110	寻宝折扣卷
	 */
	public static final int XUNBAO_ZHEKOU = 110;
	/**
	妖神附魔石
	 */
	public static final int YS_FUMO_STONE = 115;
	
	/**
	画卷
	 */
	public static final int HUAJUAN = 116;
	/**
	画卷碎片
	 */
	public static final int HUAJUAN_SUIPIAN = 117;
	/**
	宠物经验丹
	 */
	public static final int CHONGWU_EXP_DAN = 121;

	 /**
     * 心魔升级和突破道具
     */
    public static final int XINMO_SJ_OR_TUPO_ITEM= 142;

    /**
     * 心魔凝神道具
     */
    public static final int XINMO_NINGSHEN_ITEM= 143;
    /**
     * 心魔炼丹:未知丹药道具
     */
    public static final int XM_LIANDAN_ITEM= 144;
    
    /**
     * 增加vip经验道具
     */
    public static final int ADD_VIP_EXP_ITEM= 146;
    
    /**
     * 画卷2道具类型
     */
    public static final int HUAJUAN2 = 153;
    
    /**
     * 画卷2碎片道具类型(后端程序未使用)
     */
    public static final int HUAJUAN2_SUIPIAN = 154;

    /**
     * 灵火祝福道具类型155
     */
    public static final int LINGHUO_BLESS_ITEM = 155;
    
    /**
     * 魔焰精华道具类型156
     */
    public static final int MOYANJINGHUA_ITEM = 156;
    /**
     * 五行金升阶丹类型
     */
    public static final int WUXIANG_JIN_SJ_ITEM = 180;
    /**
     * 五行木升阶丹类型
     */
    public static final int WUXIANG_MU_SJ_ITEM = 181;
    /**
     * 五行土升阶丹类型
     */
    public static final int WUXIANG_TU_SJ_ITEM = 182;
    /**
     * 五行水升阶丹类型
     */
    public static final int WUXIANG_SHUI_SJ_ITEM = 183;
    /**
     * 五行火升阶丹类型
     */
    public static final int WUXIANG_HUO_SJ_ITEM = 184;
	/**
	 * boss积分
	 */
	public static final int BOSS_JIFEN= 185;
	/**
	 * 武器潜能丹
	 */
	public static final int WQ_QND= 188;
	/**
	 * 武器成长丹
	 */
	public static final int WQ_CZD= 189;
	
	/**
	 * 武器升阶丹
	 */
	public static final int WUQI_SJ_ITEM= 190;
    
	/**
	 * 数字 = goodsId
	 */
	public static final Map<String,Integer> numberTypeGoods = new HashMap<String,Integer>();
	
	
	/**
	 * 是否是可NPC购买并使用
	 * @param category
	 * @return true:可以
	 */
	public static boolean isNpcBuyAndUse(int category){
		return MONEY_TYPE == category || GOLD_TYPE == category || BIND_GOLD_TYPE == category;
	}
	
	
	/**
	 * 是否是改名卡
	 * @param category
	 * @return true:是
	 */
	public static boolean isChangeNameCard(String category){
		//TODO
//		if(category.equals(CHANGE_NAME_CARD)){
//			return true;
//		}else{
			return false;
//		}
	}
	 
	
	
	public static final void addNumberTypeGoods(int numberType,String goodsId){
		numberTypeGoods.put(goodsId, numberType);
	}
	
	/**
	 * 是否是数值类型
	 * @param goodsId
	 * @return
	 */
	public static final boolean isNumberType(String goodsId){
		return numberTypeGoods.containsKey(goodsId);
	}
	
	public static final Integer getNumberType(String goodsId){
		return numberTypeGoods.get(goodsId);
	}
	/**
	 * 是否是数字类型道具
	 * @param category
	 * @return
	 */
	public static final boolean isNumberType(int category){
		if(category < 0){
			return true;	
		}
		return false;
	}
	
	/**
	 * VIP测试道具
	 * @param category
	 * @return
	 */
	public static final boolean isVIP_TestProp(String category){
		//TODO
//		if(category.equals(VIP_TEST)){
//			return true;
//		}else{
			return false;
//		}
	}
	
	
	/**
	 * 是否是装备
	 * @param category
	 * @return
	 */
	public static final boolean isEquip(int category){
		if(category == EQUIP_TYPE){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 是否是糖宝菩提
	 * @param category
	 * @return
	 */
	public static final boolean isTangbaoPuti(int category){
		return category == TANGBAO_ADD_ATT;
	}
	
	/**
	 * 是否是飞鞋
	 * @param category
	 * @return
	 */
	public static final boolean isFeiXie(int category){
		return category == FEI_XIE;
	}
	
	/**
	 * 是否是回城卷轴
	 * @param category
	 * @return
	 */
	public static final boolean isTP(int category){
		return category == FIXED_CITY_BACK;
	}
	
}
