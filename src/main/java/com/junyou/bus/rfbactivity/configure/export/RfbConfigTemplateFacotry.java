package com.junyou.bus.rfbactivity.configure.export;

import java.util.HashMap;
import java.util.Map;

import com.junyou.bus.caidan.service.RoleCaidanConfigService;
import com.junyou.bus.danfuchargerank.configure.export.DanFuChongZhiPaiMingConfigExportService;
import com.junyou.bus.daomoshouzha.configure.export.DaoMoShouZhaConfigExportService;
import com.junyou.bus.extremeRecharge.configure.export.RfbExtremeRechargeConfigExportService;
import com.junyou.bus.firstChargeRebate.configure.export.RefabuFirstChargeRebateConfigExportService;
import com.junyou.bus.happycard.configure.export.HappyCardConfigExportService;
import com.junyou.bus.huiyanshijin.configue.HuiYanShiJingConfigExportService;
import com.junyou.bus.jueban.configure.RefabuJuebanConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuChiBangConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuQiLingConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuQiangHuaConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuQuanMingXiuXianConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuTangBaoConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuTangBaoXinWenConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuWuQiConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuXianJieJingJiConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuYaoMoConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuYaoShenConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuYaoShenHunpoConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuYaoShenMoYinConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuYuJianFeiXianConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuZhanJiaConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuZhanLiBiPinConfigExportService;
import com.junyou.bus.kuafuchargerank.configure.export.KuaFuChongZhiPaiMingConfigExportService;
import com.junyou.bus.kuafuxiaofei.configure.export.KuaFuXiaoFeiPaiMingConfigExportService;
import com.junyou.bus.laba.configure.export.LaBaConfigExportService;
import com.junyou.bus.laowanjia.configue.LaoWanJiaConfigExportService;
import com.junyou.bus.leichong.configue.export.LeiChong53ConfigExportService;
import com.junyou.bus.leichong.configue.export.LeiChongConfigExportService;
import com.junyou.bus.leihao.configure.export.LeiHaoConfigExportService;
import com.junyou.bus.lianchong.configure.export.LianChongConfigExportService;
import com.junyou.bus.login.configure.RefabuLoginConfigExportService;
import com.junyou.bus.lunpan.configure.export.LunPanConfigExportService;
import com.junyou.bus.map.configure.export.ActiveMapConfigExportService;
import com.junyou.bus.miaosha.configure.MiaoShaConfigExportService;
import com.junyou.bus.oncechong.configure.export.OnceChongConfigExportService;
import com.junyou.bus.onlinerewards.configue.export.OnlineRewardsConfigExportService;
import com.junyou.bus.pic.configure.export.PicNoticeConfigExportService;
import com.junyou.bus.qipan.configure.export.QiPanConfigExportService;
import com.junyou.bus.rechargefanli.configure.export.RechargeFanLiConfigExportService;
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
import com.junyou.bus.rfbflower.configue.export.FlowerCharmRankConfigExportService;
import com.junyou.bus.shouchong.configure.RfbShouChongConfigExportService;
import com.junyou.bus.smsd.configue.export.ShenMiShangDianConfigExportService;
import com.junyou.bus.suoyaota.configure.export.SuoYaoTaConfigExportService;
import com.junyou.bus.superduihuan.configure.export.SuperDuihuanConfigExportService;
import com.junyou.bus.tanbao.configure.TanSuoBaoZangConfigExportService;
import com.junyou.bus.tuangou.configure.RfbTuangouConfigExportService;
import com.junyou.bus.xiaofei.configure.export.XiaofeiConfigExportService;
import com.junyou.bus.xingkongbaozang.configure.export.XkbzConfigExportService;
import com.junyou.bus.xiuxian.configure.RfbXiuXianConfigExportService;
import com.junyou.bus.xunbao.configure.RfbXunBaoConfigExportService;
import com.junyou.bus.zhuanpan.configure.export.ZhuanPanConfigExportService;

/**
 * 热发布配置解析模板工厂
 * @author DaoZheng Yuan
 * 2015年5月21日 下午2:04:27
 */
public class RfbConfigTemplateFacotry {
	
	private static  Map<Integer,IRfbConfigTemplateService> TEMPLATES = new HashMap<>();
	static{
		TEMPLATES.put(ReFaBuUtil.SOUCHONG_TYPE, RfbShouChongConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.RFB_LEVEL_TYPE, KaiFuQuanMingXiuXianConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.XIUXIAN_LB_TYPE, RfbXiuXianConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.ZHANLI_BIPIN_TYPE, KaiFuZhanLiBiPinConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.YUJIAN_FEIXING_TYPE, KaiFuYuJianFeiXianConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.XIANJIE_YUYI_TYPE, KaiFuChiBangConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.XIANJIE_JINGJI_TYPE, KaiFuXianJieJingJiConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.XIANZHUANG_QH_TYPE, KaiFuQiangHuaConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.QI_PAN_TYPE, QiPanConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.LEI_CHONG_TYPE, LeiChongConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.TANG_BAO_TYPE, KaiFuTangBaoConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.SHENMI_SHANGDIAN_TYPE, ShenMiShangDianConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.XIAOFEI_PAIMING_TYPE, XiaofeiConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.ZHUAN_PAN_TYPE, ZhuanPanConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.LUNPAN_TYPE, LunPanConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.ZHAN_JIA_TYPE, KaiFuZhanJiaConfigExportService.getInstance());
		//*************************连充奖励*************************
		TEMPLATES.put(ReFaBuUtil.LIAN_CHONG_TYPE, LianChongConfigExportService.getInstance());
		//*************************在线奖励*************************
		TEMPLATES.put(ReFaBuUtil.ONLINE_REWARDS_TYPE, OnlineRewardsConfigExportService.getInstance());
		//*************************鲜花魅力榜*************************
		TEMPLATES.put(ReFaBuUtil.FLOWER_CHARM_RANK_TYPE, FlowerCharmRankConfigExportService.getInstance());
		//*************************拉霸活动*************************
		TEMPLATES.put(ReFaBuUtil.LABA_TYPE, LaBaConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.DAOMO_SHOUZHA_TYPE, DaoMoShouZhaConfigExportService.getInstance());
		//超值兑换
		TEMPLATES.put(ReFaBuUtil.SUPER_DUIHUAN_TYPE, SuperDuihuanConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.SUOYAOTA_TYPE, SuoYaoTaConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.ACTIVE_MAP_TYPE, ActiveMapConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.PIC_NOTICE_TYPE, PicNoticeConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.KUAFU_CHARGE_RANK_TYPE, KuaFuChongZhiPaiMingConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.HAPPY_CARD_TYPE, HappyCardConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.YAOSHEN_JINJIE_TYPE, KaiFuYaoShenConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.CHONGZHI_FANLI_TYPE, RechargeFanLiConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.LEIHAO_TYPE, LeiHaoConfigExportService.getInstance());
		//**************************幸运彩蛋**************************
		TEMPLATES.put(ReFaBuUtil.CAIDAN_TYPE, RoleCaidanConfigService.getInstance());
		//**************************妖神魔纹**************************
		TEMPLATES.put(ReFaBuUtil.YAOSHENMOWEN_TYPE, KaiFuYaoMoConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.YAOSHENMOYIN_TYPE, KaiFuYaoShenMoYinConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.QILING_TYPE, KaiFuQiLingConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.YAOSHENHUNPO_TYPE, KaiFuYaoShenHunpoConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.REFABUXUNBAO_TYPE, RfbXunBaoConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.REFABUTANBAO_TYPE, TanSuoBaoZangConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.TANGBAOXINWEN_TYPE, KaiFuTangBaoXinWenConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.DANFU_CHARGE_RANK_TYPE, DanFuChongZhiPaiMingConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.TUANGOU_TYPE, RfbTuangouConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.RFBLOGIN_TYPE, RefabuLoginConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.MIAOSHA_TYPE, MiaoShaConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.LAOWANJIA_TYPE, LaoWanJiaConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.HUIYANSHIJING_TYPE, HuiYanShiJingConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.FIRST_CHARGE_REBATE_TYPE, RefabuFirstChargeRebateConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.JUEBAN_REBATE_TYPE, RefabuJuebanConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.EXTREME_RECHARGE_TYPE, RfbExtremeRechargeConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.EXTREME_XIAOFEI_TYPE, KuaFuXiaoFeiPaiMingConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.LOOP_DAY_CHONGZHI_TYPE, RfbShouChongConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.SHENG_JIAN_RANK, KaiFuWuQiConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.ONCE_CHONG_TYPE, OnceChongConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.XINGKONG_TYPE, XkbzConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.CZFZ_TYPE, LeiChong53ConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.LB_TYPE, LaBaConfigExportService.getInstance());
		
	}
	
	
	/**
	 * 获取热发布配置解析模板
	 * @param type  {@link ReFaBuUtil}
	 * @return
	 */
	public static IRfbConfigTemplateService getRfbConfigTemplateService(int type){
		return TEMPLATES.get(type);
	}
}
