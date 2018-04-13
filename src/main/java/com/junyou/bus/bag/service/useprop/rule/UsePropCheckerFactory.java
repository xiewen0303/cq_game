package com.junyou.bus.bag.service.useprop.rule;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.utils.GoodsCategory;

/**
 * 道具规则
 */
public class UsePropCheckerFactory {
	private static Map<Integer,IUsePropCheckRule> propCheckRole = new HashMap<Integer,IUsePropCheckRule>();
	static{
		propCheckRole.put(GoodsCategory.MONEY_TYPE, new CurrencyCheckRule(GoodsCategory.MONEY));
		propCheckRole.put(GoodsCategory.GOLD_TYPE, new CurrencyCheckRule(GoodsCategory.GOLD));
		propCheckRole.put(GoodsCategory.BIND_GOLD_TYPE, new CurrencyCheckRule(GoodsCategory.BGOLD));
		
//		propCheckRole.put(GoodsCategory.SHENQIJIFEN_TYPE, new ShengQiValueCheckRule());
		propCheckRole.put(GoodsCategory.GD_RECOVER_BLOOD, new AddHpCheckRule());
		propCheckRole.put(GoodsCategory.BFB_RECOVER_BLOOD, new AddHpBaiFenBiCheckRule());
//		propCheckRole.put(GoodsCategory.ADD_ROLE_EXP, new AddRoleExpCheckRule());
		
		propCheckRole.put(GoodsCategory.ZQ_CZD, new AddZuoQiCZDCheckRule());
		propCheckRole.put(GoodsCategory.ZQ_QND, new AddZuoQiQNDCheckRule());
		
		propCheckRole.put(GoodsCategory.WQ_CZD, new AddWuQiCZDCheckRule());
		propCheckRole.put(GoodsCategory.WQ_QND, new AddWuQiQNDCheckRule());
		
		propCheckRole.put(GoodsCategory.FIXED_CITY_BACK, new TownPortalCheckRule());
		
		
		IUsePropCheckRule checkRule = new TSEffectPropCheckRule();
		propCheckRole.put(GoodsCategory.MANY_BEI_EXP, checkRule);
		propCheckRole.put(GoodsCategory.HP_POOL_TYPE, checkRule);
		
		propCheckRole.put(GoodsCategory.FIXED_GIFT_CARD, new OpenGiftCardCheckRule(GoodsCategory.FIXED_GIFT_CARD));
		propCheckRole.put(GoodsCategory.RANDOM_GIFT_CARD, new OpenGiftCardCheckRule(GoodsCategory.RANDOM_GIFT_CARD));
		propCheckRole.put(GoodsCategory.NEW_PLAYER_GIFT, new OpenGiftCardCheckRule(GoodsCategory.FIXED_GIFT_CARD));//新手礼包等同固定礼包
		propCheckRole.put(GoodsCategory.YUANBAO_RANDOM_GIFT_CARD, new OpenGiftCardCheckRule(GoodsCategory.RANDOM_GIFT_CARD));
		propCheckRole.put(GoodsCategory.YINLIANG_RANDOM_GIFT_CARD, new OpenGiftCardCheckRule(GoodsCategory.RANDOM_GIFT_CARD));
		propCheckRole.put(GoodsCategory.YUANBAO_FIXED_GIFT_CARD, new OpenGiftCardCheckRule(GoodsCategory.FIXED_GIFT_CARD));
		propCheckRole.put(GoodsCategory.YINLIANG_FIXED_GIFT_CARD, new OpenGiftCardCheckRule(GoodsCategory.FIXED_GIFT_CARD));

		propCheckRole.put(GoodsCategory.GD_ADD_EXP, new AddExpCheckRule(GoodsCategory.GD_ADD_EXP));
		propCheckRole.put(GoodsCategory.BFB_ADD_EXP, new AddExpCheckRule(GoodsCategory.BFB_ADD_EXP));
		
		propCheckRole.put(GoodsCategory.ZHEN_QI_TYPE, new AddZhenQiCheckRule());
		
		propCheckRole.put(GoodsCategory.TANGBAO_ACTIVE, new ActiveTangbaoCheckRule());
		propCheckRole.put(GoodsCategory.TANGBAO_MXZX, new MXZXCheckRule());
		propCheckRole.put(GoodsCategory.TANGBAO_ZIZHI, new ZZDCheckRule());
		propCheckRole.put(GoodsCategory.TANGBAO_ADD_ATT, new PuTiCheckRule());
		
		propCheckRole.put(GoodsCategory.FEI_SHENG_DAN, new ShengXianDanCheckRule());
		propCheckRole.put(GoodsCategory.SHUIZUI_YAOSHUI, new ShuZuiYaoShuiCheckRule());
		propCheckRole.put(GoodsCategory.SHENQI_ITEM, new ActiveShenqiCheckRule());
		propCheckRole.put(GoodsCategory.CHIBANG_CZD, new AddChiBangCZDCheckRule());
		propCheckRole.put(GoodsCategory.CHIBANG_QND, new AddChiBangQNDCheckRule());
		
		propCheckRole.put(GoodsCategory.QILING_CZD, new AddQiLingCZDCheckRule());
		propCheckRole.put(GoodsCategory.QILING_QND, new AddQiLingQNDCheckRule());
		propCheckRole.put(GoodsCategory.TIANYU_CZD, new AddTianYuCZDCheckRule());
		propCheckRole.put(GoodsCategory.TIANYU_QND, new AddTianYuQNDCheckRule());
		
		propCheckRole.put(GoodsCategory.XIANJIAN_CZD, new AddXianJianCZDCheckRule());
		propCheckRole.put(GoodsCategory.XIANJIAN_QND, new AddXianJianQNDCheckRule());
		
		propCheckRole.put(GoodsCategory.ZHANJIA_CZD, new AddZhanJiaCZDCheckRule());
		propCheckRole.put(GoodsCategory.ZHANJIA_QND, new AddZhanJiaQNDCheckRule());
		
		propCheckRole.put(GoodsCategory.CB_SJ_ITEM, new ChibangSjCheckRule());
		
		propCheckRole.put(GoodsCategory.YAOSHEN_CZD, new AddYaoshenCZDCheckRule());
		propCheckRole.put(GoodsCategory.YAOSHEN_QND, new AddYaoshenQNDCheckRule());
		
		propCheckRole.put(GoodsCategory.YAOSHEN_MOWEN_CZD, new AddYaoshenMowenCZDCheckRule());
		propCheckRole.put(GoodsCategory.YAOSHEN_MOWEN_QND, new AddYaoshenMowenQNDCheckRule());
		
		propCheckRole.put(GoodsCategory.YAOSHEN_HUNPO_CZD, new AddYaoshenHunpoCZDCheckRule());
		propCheckRole.put(GoodsCategory.YAOSHEN_HUNPO_QND, new AddYaoshenHunpoQNDCheckRule());
		
		propCheckRole.put(GoodsCategory.YAOSHEN_MOYIN_CZD, new AddYaoshenMoYinCZDCheckRule());
		propCheckRole.put(GoodsCategory.YAOSHEN_MOYIN_QND, new AddYaoshenMoYinQNDCheckRule());
		 
		
		
		propCheckRole.put(GoodsCategory.CHENG_SHEN_SHD, new AddChengShenSHDCheckRule());
		
		
		
		propCheckRole.put(GoodsCategory.TS_SJ_ITEM, new ZhanjiaSjCheckRule());
		
		propCheckRole.put(GoodsCategory.TG_SJ_ITEM, new XianjianSjCheckRule());
		
		propCheckRole.put(GoodsCategory.ZUOQI_SJ_ITEM, new ZuoQiSjCheckRule());
		
		propCheckRole.put(GoodsCategory.QL_SJ_ITEM, new QiLingChibangSjCheckRule());
		propCheckRole.put(GoodsCategory.TY_SJ_ITEM, new TianYuSjCheckRule());
		propCheckRole.put(GoodsCategory.YAOBA_SJ_ITEM, new YaoShenBaTiSjCheckRule());
		propCheckRole.put(GoodsCategory.YAOHUN_SJ_ITEM, new YaoShenHunPoSjCheckRule());
		propCheckRole.put(GoodsCategory.YAOMO_SJ_ITEM, new YaoShenMoWenSjCheckRule());
		propCheckRole.put(GoodsCategory.YAOYIN_SJ_ITEM, new YaoShenMoYinSjCheckRule());
		propCheckRole.put(GoodsCategory.XINWEN_SJ_ITEM, new TangBaoXinWenSjCheckRule());
		
		propCheckRole.put(GoodsCategory.XIANSHI_SHIZHUANG, new XianshiShizhuangCheckRule());
		
		propCheckRole.put(GoodsCategory.ADD_XIUWEI, new AddXiuweiCheckRule());
		
		propCheckRole.put(GoodsCategory.HUAJUAN, new HuajuanActivateCheckRule());
		
		propCheckRole.put(GoodsCategory.CHONGWU_EXP_DAN, new AddChongwuExpCheckRule());
		
		propCheckRole.put(GoodsCategory.ADD_VIP_EXP_ITEM, new AddVipExpCheckRule());
		
		propCheckRole.put(GoodsCategory.MOYANJINGHUA_ITEM, new AddMglyJinghuaCheckRule());
		propCheckRole.put(GoodsCategory.WUXIANG_JIN_SJ_ITEM, new WuXingSjCheckRule());
		propCheckRole.put(GoodsCategory.WUXIANG_MU_SJ_ITEM, new WuXingSjCheckRule());
		propCheckRole.put(GoodsCategory.WUXIANG_TU_SJ_ITEM, new WuXingSjCheckRule());
		propCheckRole.put(GoodsCategory.WUXIANG_SHUI_SJ_ITEM, new WuXingSjCheckRule());
		propCheckRole.put(GoodsCategory.WUXIANG_HUO_SJ_ITEM, new WuXingSjCheckRule());
		propCheckRole.put(GoodsCategory.BOSS_JIFEN, new BossJifenCheckRule());
		propCheckRole.put(GoodsCategory.WUQI_SJ_ITEM, new WuQiSjCheckRule());
	}
	
	public static IUsePropCheckRule getUsePropRoleByType(int goodType){
		return propCheckRole.get(goodType);
	}
}
