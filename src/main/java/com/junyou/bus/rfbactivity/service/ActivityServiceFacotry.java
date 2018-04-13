package com.junyou.bus.rfbactivity.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.junyou.bus.kaifuactivity.service.RfbChiBangPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbLevelHuoDongService;
import com.junyou.bus.kaifuactivity.service.RfbTangBaoService;
import com.junyou.bus.kaifuactivity.service.RfbWuQiPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbXianZhuangQiangHuaService;
import com.junyou.bus.kaifuactivity.service.RfbYuJianFeiXingService;
import com.junyou.bus.kaifuactivity.service.RfbZhanJiaPaiMingService;
import com.junyou.bus.laba.service.LaBaService;
import com.junyou.bus.leichong.server.LeiChongService;
import com.junyou.bus.leihao.service.LeiHaoService;
import com.junyou.bus.qipan.server.QiPanService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
import com.junyou.bus.shouchong.service.RefbRoleShouchongService;
import com.junyou.bus.xingkongbaozang.server.XkbzService;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.spring.SpringApplicationContext;

/**
 * 热发布配置解析模板工厂
 */
public class ActivityServiceFacotry {
	
	private static  Map<Integer,Class<? extends IActivityService>> TEMPLATES = new HashMap<>();
	
	static{
		TEMPLATES.put(ReFaBuUtil.SOUCHONG_TYPE, RefbRoleShouchongService.class);
		TEMPLATES.put(ReFaBuUtil.RFB_LEVEL_TYPE, RfbLevelHuoDongService.class);
//		TEMPLATES.put(ReFaBuUtil.XIUXIAN_LB_TYPE, RfbXiuXianConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.ZHANLI_BIPIN_TYPE, KaiFuZhanLiBiPinConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.YUJIAN_FEIXING_TYPE, RfbYuJianFeiXingService.class);
		TEMPLATES.put(ReFaBuUtil.XIANJIE_YUYI_TYPE, RfbChiBangPaiMingService.class);
//		TEMPLATES.put(ReFaBuUtil.XIANJIE_JINGJI_TYPE, KaiFuXianJieJingJiConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.XIANZHUANG_QH_TYPE, RfbXianZhuangQiangHuaService.class);
		TEMPLATES.put(ReFaBuUtil.QI_PAN_TYPE, QiPanService.class);
		TEMPLATES.put(ReFaBuUtil.LEI_CHONG_TYPE, LeiChongService.class);
		TEMPLATES.put(ReFaBuUtil.TANG_BAO_TYPE, RfbTangBaoService.class);
//		TEMPLATES.put(ReFaBuUtil.SHENMI_SHANGDIAN_TYPE, ShenMiShangDianConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.XIAOFEI_PAIMING_TYPE, RefabuXiaoFeiService.class);
//		TEMPLATES.put(ReFaBuUtil.ZHUAN_PAN_TYPE, ZhuanPanConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.LUNPAN_TYPE, LunPanConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.ZHAN_JIA_TYPE, RfbZhanJiaPaiMingService.class);
//		//*************************连充奖励*************************
//		TEMPLATES.put(ReFaBuUtil.LIAN_CHONG_TYPE, LianChongConfigExportService.getInstance());
//		//*************************在线奖励*************************
//		TEMPLATES.put(ReFaBuUtil.ONLINE_REWARDS_TYPE, OnlineRewardsConfigExportService.getInstance());
//		//*************************鲜花魅力榜*************************
//		TEMPLATES.put(ReFaBuUtil.FLOWER_CHARM_RANK_TYPE, FlowerCharmRankConfigExportService.getInstance());
//		//*************************拉霸活动*************************
//		TEMPLATES.put(ReFaBuUtil.LABA_TYPE, LaBaConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.DAOMO_SHOUZHA_TYPE, DaoMoShouZhaConfigExportService.getInstance());
//		//超值兑换
//		TEMPLATES.put(ReFaBuUtil.SUPER_DUIHUAN_TYPE, SuperDuihuanConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.SUOYAOTA_TYPE, SuoYaoTaConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.ACTIVE_MAP_TYPE, ActiveMapConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.PIC_NOTICE_TYPE, PicNoticeConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.KUAFU_CHARGE_RANK_TYPE, KuaFuChongZhiPaiMingConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.HAPPY_CARD_TYPE, HappyCardConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.YAOSHEN_JINJIE_TYPE, KaiFuYaoShenConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.CHONGZHI_FANLI_TYPE, RechargeFanLiConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.LEIHAO_TYPE, LeiHaoService.class);
//		//**************************幸运彩蛋**************************
//		TEMPLATES.put(ReFaBuUtil.CAIDAN_TYPE, RoleCaidanConfigService.getInstance());
//		//**************************妖神魔纹**************************
//		TEMPLATES.put(ReFaBuUtil.YAOSHENMOWEN_TYPE, KaiFuYaoMoConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.YAOSHENMOYIN_TYPE, KaiFuYaoShenMoYinConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.QILING_TYPE, KaiFuQiLingConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.YAOSHENHUNPO_TYPE, KaiFuYaoShenHunpoConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.REFABUXUNBAO_TYPE, RfbXunBaoConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.REFABUTANBAO_TYPE, TanSuoBaoZangConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.TANGBAOXINWEN_TYPE, KaiFuTangBaoXinWenConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.DANFU_CHARGE_RANK_TYPE, DanFuChongZhiPaiMingConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.TUANGOU_TYPE, RfbTuangouConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.RFBLOGIN_TYPE, RefabuLoginConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.MIAOSHA_TYPE, MiaoShaConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.LAOWANJIA_TYPE, LaoWanJiaConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.HUIYANSHIJING_TYPE, HuiYanShiJingConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.FIRST_CHARGE_REBATE_TYPE, RefabuFirstChargeRebateConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.JUEBAN_REBATE_TYPE, RefabuJuebanConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.EXTREME_RECHARGE_TYPE, RfbExtremeRechargeConfigExportService.getInstance());
//		TEMPLATES.put(ReFaBuUtil.EXTREME_XIAOFEI_TYPE, KuaFuXiaoFeiPaiMingConfigExportService.getInstance());
		TEMPLATES.put(ReFaBuUtil.LOOP_DAY_CHONGZHI_TYPE, RefbRoleShouchongService.class);
		TEMPLATES.put(ReFaBuUtil.SHENG_JIAN_RANK, RfbWuQiPaiMingService.class);
		TEMPLATES.put(ReFaBuUtil.CZFZ_TYPE, LeiChongService.class);
		TEMPLATES.put(ReFaBuUtil.LB_TYPE, LaBaService.class);
		
		TEMPLATES.put(ReFaBuUtil.XINGKONG_TYPE, XkbzService.class);
		
	}
	
	
	/**
	 * 获取热发布配置解析模板
	 * @param type  {@link ReFaBuUtil}
	 * @return
	 */
	public static Class<? extends IActivityService> getActivityService(int type){
		return TEMPLATES.get(type);
	}

	/**
	 * 判定是否可以点亮排行榜的角标
	 * @param type
	 */
	public static void checkRankIconFlag(long userRoleId,int type){
		Class<? extends IActivityService> activityServiceClass = ActivityServiceFacotry.getActivityService(type);
		if(activityServiceClass == null){
			ChuanQiLog.error("subActivity is not regist,type={}",type);
			return ;
		}
		IActivityService service = SpringApplicationContext.getApplicationContext().getBean(activityServiceClass);

		Collection<ActivityConfigSon> subActivitys = ActivityAnalysisManager.getInstance().loadAllSubActivitys();

		if(ObjectUtil.isEmpty(subActivitys)){
			ChuanQiLog.debug("sub activitys is empty!");
			return;
		}
		for (ActivityConfigSon activityConfigSon : subActivitys) {
	//			//主活动是否在
	//			if (!isShowActivity(userRoleId, activity)) {
	//				continue;
	//			}
	//			//子活动删除
	//			if(entry.isDel()){
	//				ChuanQiLog.debug("sub activity is delete,subActId={},guid={}",entry.getSubName(),entry.getId());
	//				continue;
	//			}

				if(type != activityConfigSon.getSubActivityType()){
					continue;
				}

				//不在活动期间内的
				if(!activityConfigSon.isRunActivity()){
					continue;
				}

				if(activityServiceClass == null){
					ChuanQiLog.error("subActivity is not regist,type={}",activityConfigSon.getSubActivityType());
					continue;
				}
				service.checkIconFlag(userRoleId, activityConfigSon.getId());
		}
	}
}
