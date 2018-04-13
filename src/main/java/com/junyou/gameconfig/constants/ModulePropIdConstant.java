package com.junyou.gameconfig.constants;

import java.util.ArrayList;
import java.util.List;

 

/**
 * 功能模块服务器写死的道具ID
 *@author  DaoZheng Yuan
 *@created 2012-12-20下午3:54:54
 */
public class ModulePropIdConstant {

	/**
	 * 喇叭ID(模块:bag)
	 */
	public static String HORN_ITEM_ID = "laba"; 
	
	/**
	 * 游戏币(模块:stage 物品掉落)
	 */
	public static String MONEY_GOODS_ID = "money";
	/**
	 * 真气(模块:stage 物品掉落)
	 */
	public static String MONEY_ZHENQI_ID = "zhenqi";
	
	/**
	 * 跨服战积分
	 */
	public static String KF_JF_GOODS_ID = "kfjifen";
	
	/**
	 * 元宝
	 */
	public static String GOLD_GOODS_ID = "gold";
	/**
	 * 礼券
	 */
	public static String BIND_GOLD_GOODS_ID = "bgold";
	/**
	 * 经验
	 */
	public static String EXP_GOODS_ID = "exp";
	/**
	 * 魂石
	 */
	public static String HUN_GOODS_ID = "hun";
	/**
	 * 荣誉
	 */
	public static String RONGYU_GOODS_ID = "rongyu";
	/**
	 * 贡献
	 */
	public static String GONGXIAN_GOODS_ID = "gongxian";
	/**
	 * 夺宝积分
	 */
	public static String TANBAO_GOODS_ID = "xbjf";
	/**
	 * 功勋
	 */
	public static String GONGXUN_GOODS_ID = "gongxun";
	
	/**
	 * 复活石大类id
	 */
	public static String SPOT_REVIVE_ITEM_ID = null; 
	
	/**
	 * 门派令牌1id
	 */
	public static String GUILD_ITEM1_ID = null; 
	/**
	 * 门派令牌2id
	 */
	public static String GUILD_ITEM2_ID = null; 
	/**
	 * 门派令牌3id
	 */
	public static String GUILD_ITEM3_ID = null; 
	/**
	 * 门派令牌4id
	 */
	public static String GUILD_ITEM4_ID = null; 
	
	/**
	 * 改名卡
	 */
	public static String CHANGE_NAME_ITEM_ID = "gaimingka"; 
	
	/**
	 * 飞鞋
	 */
	public static String FEI_XIE_ITEM_ID = null; 
	
	/**
	 * 菩提id集合
	 */
	private static List<String> PUTI_IDS = new ArrayList<>();

	public static List<String> getPutiIds() {
		return PUTI_IDS;
	}

	public static void addPutiId(String putiId) {
		ModulePropIdConstant.PUTI_IDS.add(putiId);
	}
	
	
	
//	/**
//	 * 是否是组包数字类型
//	 * @param goodsId
//	 * @return true:是
//	 */
//	public static final boolean isNumberId(String goodsId){
//		return MONEY_GOODS_ID.equals(goodsId) || GOLD_GOODS_ID.equals(goodsId) || RONGYU_GOODS_ID.equals(goodsId) || EXP_GOODS_ID.equals(goodsId) || BIND_GOLD_GOODS_ID.equals(goodsId) || MONEY_ZHENQI_ID.equals(goodsId);
//	}
//	
//	/**
//	 * 是否是组包数字类型
//	 * @param goodsId
//	 * @return true:是
//	 */
//	public static final Integer getNumberId(String goodsId){
//		
//		if(MONEY_GOODS_ID.equals(goodsId)){
//			return GoodsCategory.MONEY;
//		}
//		
//		if(GOLD_GOODS_ID.equals(goodsId)){
//			return GoodsCategory.GOLD;
//		}
//		
//		if(EXP_GOODS_ID.equals(goodsId)){
//			return GoodsCategory.EXP;
//		} 
//		
//		if(BIND_GOLD_GOODS_ID.equals(goodsId)){
//			return GoodsCategory.BGOLD;
//		} 
//		
//		if(MONEY_ZHENQI_ID.equals(goodsId)){
//			return GoodsCategory.ZHENQI;
//		} 
//		return null;
//	}
	
}
