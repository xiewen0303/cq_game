package com.junyou.bus.rfbactivity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.caidan.service.RoleCaidanExportService;
import com.junyou.bus.danfuchargerank.service.export.DanfuChargeRankExportService;
import com.junyou.bus.daomoshouzha.export.DaoMoShouZhaExportService;
import com.junyou.bus.extremeRecharge.export.RfbExtremeRechargeExportService;
import com.junyou.bus.firstChargeRebate.export.RefabuFirstChargeRebateExportService;
import com.junyou.bus.happycard.service.export.HappyCardExportService;
import com.junyou.bus.huiyanshijin.export.HuiYanShiJinExportService;
import com.junyou.bus.jueban.export.RefabuJuebanExportService;
import com.junyou.bus.kaifuactivity.export.KaiFuHuoDongExportService;
import com.junyou.bus.kuafuchargerank.service.export.KuafuChargeRankExportService;
import com.junyou.bus.kuafuxiaofei.service.export.KuafuXiaoFeiExportService;
import com.junyou.bus.laba.export.LaBaExportService;
import com.junyou.bus.laowanjia.export.RefbLaowanjiaExportService;
import com.junyou.bus.leichong.export.LeiChongExportService;
import com.junyou.bus.leihao.service.export.LeiHaoExportService;
import com.junyou.bus.lianchong.export.LianChongExportService;
import com.junyou.bus.login.export.RefabuSevenLoginExportService;
import com.junyou.bus.lunpan.export.LunPanExportService;
import com.junyou.bus.map.export.ActiveMapExportService;
import com.junyou.bus.miaosha.export.RefbMiaoshaExportService;
import com.junyou.bus.oncechong.export.OnceChongExportService;
import com.junyou.bus.onlinerewards.export.OnlineRewardsExportService;
import com.junyou.bus.pic.configure.export.PicNoticeConfigExportService;
import com.junyou.bus.qipan.export.QiPanExportService;
import com.junyou.bus.rechargefanli.export.RechargeFanliExportService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfig;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.configure.export.ReFaBuGxConfig;
import com.junyou.bus.rfbactivity.configure.export.ReFaBuGxConfigExportService;
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
import com.junyou.bus.rfbflower.export.FlowerCharmRankExportService;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.bus.shoplimit.export.ShopLimitExportService;
import com.junyou.bus.shouchong.export.RefbRoleShouchongExportService;
import com.junyou.bus.smsd.export.ShenMiShangDianExportService;
import com.junyou.bus.suoyaota.export.SuoYaoTaExportService;
import com.junyou.bus.superduihuan.service.export.RfbSuperDuihuanExportService;
import com.junyou.bus.tanbao.export.TanSuoBaoZangExportService;
import com.junyou.bus.tuangou.export.TuanGouExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiaofei.export.RefabuXiaoFeiExportService;
import com.junyou.bus.xingkongbaozang.export.XkbzExportService;
import com.junyou.bus.xiuxian.export.RfbXiuXianExportService;
import com.junyou.bus.xunbao.export.RefbXunBaoExportService;
import com.junyou.bus.zhuanpan.export.ZhuanPanExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.spring.SpringApplicationContext;

@Service
public class RefabuActivityService {

	@Autowired
	private RefbRoleShouchongExportService refbRoleShouchongExportService;
	@Autowired
	private KaiFuHuoDongExportService kaiFuHuoDongExportService;
	@Autowired
	private RfbXiuXianExportService rfbXiuXianExportService;
	@Autowired
	private QiPanExportService qiPanExportService;
	@Autowired
	private LeiChongExportService leiChongExportService;
	@Autowired
	private ShenMiShangDianExportService shenMiShangDianExportService;
	@Autowired
	private RefabuXiaoFeiExportService refabuXiaoFeiExportService;
	@Autowired
	private ZhuanPanExportService zhuanPanExportService;
	@Autowired 
	private LianChongExportService lianChongExportService;
	@Autowired 
	private OnlineRewardsExportService onlineRewardsExportService;
	@Autowired 
	private FlowerCharmRankExportService flowerCharmRankExportService;
	@Autowired 
	private LaBaExportService laBaExportService;
	@Autowired
	private DaoMoShouZhaExportService daoMoShouZhaExportService;
	@Autowired
	private RoleYuanbaoRecordService roleYuanbaoRecordService;
	@Autowired
	private RfbSuperDuihuanExportService superDuihuanExportService;
	@Autowired
	private KuafuChargeRankExportService kuafuChargeRankExportService;
	@Autowired
	private DanfuChargeRankExportService danfuChargeRankExportService;
	@Autowired
	private SuoYaoTaExportService suoYaoTaExportService;
	@Autowired
	private ActiveMapExportService activeMapExportService;
	@Autowired
	private HappyCardExportService happyCardExportService;
	@Autowired
	private RechargeFanliExportService rechargeFanliExportService;
	@Autowired
	private RoleCaidanExportService roleCaidanExportService;
	@Autowired
	private RefbXunBaoExportService refbXunBaoExportService;
	@Autowired
	private TanSuoBaoZangExportService tanSuoBaoZangExportService;
	@Autowired LeiHaoExportService leiHaoExportService;
	@Autowired
	private TuanGouExportService tuanGouExportService;
	@Autowired
	private RefabuSevenLoginExportService refabuSevenLoginExportService;
	@Autowired
	private RefbMiaoshaExportService refbMiaoshaExportService;
	@Autowired
	private RefbLaowanjiaExportService refbLaowanjiaExportService;
	@Autowired
	private HuiYanShiJinExportService huiYanShiJinExportService;
	@Autowired
	private LunPanExportService lunpanExportService;
	@Autowired
	private RefabuFirstChargeRebateExportService refabuFirstChargeRebateExportService;
	@Autowired
	private RefabuJuebanExportService refabuJuebanExportService;
	@Autowired
	private RfbExtremeRechargeExportService rfbExtremeRechargeExportService;
	@Autowired
	private KuafuXiaoFeiExportService kuafuXiaoFeiExportService;
	@Autowired
	private OnceChongExportService onceChongExportService;
	@Autowired
	private XkbzExportService xkbzExportService;
	@Autowired
	private ShopLimitExportService shopLimitExportService;
	
	/**
	 * 获取主活动列表数据
	 * @param userRoleId
	 * @return
	 */
	public Object[] getActivity(Long userRoleId) {
		Map<Integer, ActivityConfig> activityMap = ActivityAnalysisManager.getInstance().loadAll();
		//没有主活动热发布数据
		if(activityMap.size() == 0){
			return null;
		}

		List<Object[]> list = new ArrayList<Object[]>();
		for(Map.Entry<Integer, ActivityConfig> model : activityMap.entrySet()){
			ActivityConfig activity = model.getValue();
			// 最后结果处理
			if (isShowActivity(userRoleId, activity)) {
				list.add(activity.getMainHdData());
			}
		}

		if(list.size() == 0){
			return null;
		}else{
			return list.toArray();
		}
	}

	/**
	 * 活动是否需要展示给玩家
	 * @param userRoleId
	 * @param activity
	 * @return true=展示;false=不展示
	 */
	public boolean isShowActivity(Long userRoleId, ActivityConfig activity){
        if(null == activity || !activity.isRunActivity() || activity.isDel()){
            return false;
        }
        ReFaBuGxConfig gxConfug = ReFaBuGxConfigExportService.getInstance().getReFaBuGxConfig(activity.getId());
        //如果不在关系配置的开服多少天后开启
        int serverDay = ServerInfoServiceManager.getInstance().getKaifuDays();
        if(gxConfug != null){
            if(!gxConfug.isServerDays(serverDay)){
                return false;
            }
        }
        int subSize = activity.getZihuodongMap().size();
        //没有子活动的主活动，服务器器默认认为没有这个主活动
        if(subSize == 0 && (activity.getFolder() == null || activity.getFolder() == 0)){
            return false;
        }
        
        //如果只有一个子活动，并且这个子活动是首充的话。判断该玩家是否有可以进行的首充活动:TODO
        if(subSize == 1){
            for (ActivityConfigSon configSon : activity.getZihuodongMap().values()) {
                if(configSon.getSubActivityType().intValue() == ReFaBuUtil.SOUCHONG_TYPE && !refbRoleShouchongExportService.huoquShouchong(userRoleId,configSon.getId())){
                    return false;
                }
            }
        }
        //如果只有一个子活动，并且这个子活动老玩家回归
        if(subSize == 1){
            for (ActivityConfigSon configSon : activity.getZihuodongMap().values()) {
                if(configSon.getSubActivityType().intValue() == ReFaBuUtil.LAOWANJIA_TYPE && !refbLaowanjiaExportService.isXianShiActity(userRoleId,configSon.getId())){
                    return false;
                }
            }
        }
        // 如果只有一个子活动，并且是首冲返利活动
        if (subSize == 1) {
            for (ActivityConfigSon configSon : activity.getZihuodongMap().values()) {
                if (configSon.getSubActivityType().intValue() == ReFaBuUtil.FIRST_CHARGE_REBATE_TYPE && !refabuFirstChargeRebateExportService.isActivityValid(userRoleId, configSon.getId())) {
                    return false;
                }
            }
        }
        return true;
	}
	
	/**
	 * 获取子活动列表根据主活动ID
	 * @param activityId 主活动id
	 * @param version
	 * @return
	 */
	public Object[] getZiActivity(Integer activityId, int version) {
		ActivityConfig activity = ActivityAnalysisManager.getInstance().loadById(activityId);
		if(activity == null || !activity.isRunActivity()){
			//服务器没有这个主活动
			return null;
		}else if(activity.getClientVersion() == version){
			//主活动版本没有变化
			return null;
		}
		
		List<Object[]> list = new ArrayList<Object[]>();
		Map<Integer, ActivityConfigSon> activityMap = activity.getZihuodongMap();
		if(activityMap != null && activityMap.size() > 0){
			
			for(Map.Entry<Integer, ActivityConfigSon> model : activityMap.entrySet()){
				
				ActivityConfigSon activityConfigSon = model.getValue();
				
				if(activityConfigSon.isDel()){
					//子活动删除
					continue;
				}
				
				//只要主活动在活动期间之内则子活动都推送
				list.add(activityConfigSon.getSubMsg());
			}
		}
		
		return new Object[]{activityId, activity.getClientVersion(), list.toArray()};
	}

	/**
	 * 10021 请求拉取指定子活动的数据
	 * @param subId 子活动id
	 * @param version 子活动版本号
	 * @param userRoleId
	 * @return
	 */
	public Object[] getZhiDingZiActivity(Long userRoleId,Integer subId, Integer version) {
		ActivityConfigSon activityConfigSon = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(activityConfigSon == null){
			return new Object[]{subId , AppErrorCode.ACTIVITY_OUT};
		}
		
		//判断主活动是否在活动期间内
		ActivityConfig activity = ActivityAnalysisManager.getInstance().loadById(activityConfigSon.getActivityId());
		if(activity == null || !activity.isRunActivity()){
			return new Object[]{subId , AppErrorCode.ACTIVITY_OUT};
		}
		
		//版本没有变化处理
		if(activityConfigSon.getClientVersion() == version){
			//服务器处理子活动有状态数据主动推送
			serverSubHandleChangeDataBuySubId(userRoleId, subId, activityConfigSon.getSubActivityType());
			return null;
		}
		
		//获取子活动实例业务数据根据子活动类型
		Object subTypeData = getSubHandleDataBySubType(userRoleId,subId,activityConfigSon.getSubActivityType());
		if(subTypeData == null){
			return null;
		}
		return new Object[]{subId,activityConfigSon.getClientVersion(),subTypeData};
	}
	
	/**
	 * 获取子活动实例业务数据根据子活动类型
	 * @return
	 */
	public Object getSubHandleDataBySubType(Long userRoleId,Integer subId,int type){
		switch (type) {
		
		case ReFaBuUtil.SOUCHONG_TYPE:
			
			return refbRoleShouchongExportService.getShouChongRechargeInfo(userRoleId, subId);
		case ReFaBuUtil.RFB_LEVEL_TYPE:
			
			return kaiFuHuoDongExportService.getRefbLevelInfo(userRoleId, subId);

		case ReFaBuUtil.XIUXIAN_LB_TYPE:	
			return rfbXiuXianExportService.getXiuXianHandleDataBySubType(userRoleId, subId);
		
		case ReFaBuUtil.ZHANLI_BIPIN_TYPE:	
		
			return kaiFuHuoDongExportService.getRefbZhanliInfo(userRoleId, subId);
		case ReFaBuUtil.YUJIAN_FEIXING_TYPE:	
			
			return kaiFuHuoDongExportService.getRefbYuJianFeiXingInfo(userRoleId, subId);
		case ReFaBuUtil.XIANJIE_YUYI_TYPE:	
			
			return kaiFuHuoDongExportService.getRefbChiBangInfo(userRoleId, subId);
		case ReFaBuUtil.XIANJIE_JINGJI_TYPE:	
			
			return kaiFuHuoDongExportService.getRefbJingJiInfo(userRoleId, subId);
		case ReFaBuUtil.XIANZHUANG_QH_TYPE:	
			
			return kaiFuHuoDongExportService.getRefbQiangHuaInfo(userRoleId, subId);
		case ReFaBuUtil.QI_PAN_TYPE:	
			
			return qiPanExportService.getRefbQiPanInfo(userRoleId, subId);
		case ReFaBuUtil.LEI_CHONG_TYPE:	
			
			return leiChongExportService.getRefbQiPanInfo(userRoleId, subId);
		case ReFaBuUtil.TANG_BAO_TYPE:	
			
			return kaiFuHuoDongExportService.getRefbTangBaoInfo(userRoleId, subId);
		case ReFaBuUtil.SHENMI_SHANGDIAN_TYPE:	
			
			return shenMiShangDianExportService.getRefbSMSDInfo(userRoleId, subId);
		case ReFaBuUtil.XIAOFEI_PAIMING_TYPE:	
			
			return refabuXiaoFeiExportService.getRefbXiaoFeiInfo(userRoleId, subId);
		case ReFaBuUtil.ZHUAN_PAN_TYPE:	
			
			return zhuanPanExportService.getRefbZPInfo(userRoleId, subId);
		case ReFaBuUtil.LUNPAN_TYPE:	
		    
		    return lunpanExportService.getRefbLunpanInfo(userRoleId, subId);
		case ReFaBuUtil.ZHAN_JIA_TYPE:	
			
			return kaiFuHuoDongExportService.getRefbZhanJiaInfo(userRoleId, subId);
		case ReFaBuUtil.LIAN_CHONG_TYPE:	
			
			return lianChongExportService.getRefbInfo(userRoleId, subId);
			
		case ReFaBuUtil.ONLINE_REWARDS_TYPE:	
			return onlineRewardsExportService.getRefbInfo(userRoleId, subId);
			 
		case ReFaBuUtil.FLOWER_CHARM_RANK_TYPE:	
		
			return flowerCharmRankExportService.getRefbInfo(userRoleId, subId);
		case  ReFaBuUtil.LABA_TYPE:
			
			return  laBaExportService.getRefbInfo(userRoleId, subId);
		case ReFaBuUtil.DAOMO_SHOUZHA_TYPE:	
			
			return daoMoShouZhaExportService.getRefbDaoMoInfo(userRoleId, subId);
		case ReFaBuUtil.SUPER_DUIHUAN_TYPE:	
			
			return superDuihuanExportService.getRfbInfo(userRoleId, subId);
		case ReFaBuUtil.SUOYAOTA_TYPE:	
			
			return suoYaoTaExportService.getRefbInfo(userRoleId, subId);
		case ReFaBuUtil.ACTIVE_MAP_TYPE:	
			
			return activeMapExportService.getRefbInfo(userRoleId, subId);
		case ReFaBuUtil.PIC_NOTICE_TYPE:
			
			return PicNoticeConfigExportService.getInstance().loadByMap(subId);
		case ReFaBuUtil.KUAFU_CHARGE_RANK_TYPE:	
			
			return kuafuChargeRankExportService.getRfbInfo(userRoleId, subId);
		case ReFaBuUtil.HAPPY_CARD_TYPE:	
			
			return happyCardExportService.getInfo(userRoleId, subId);
		case ReFaBuUtil.YAOSHEN_JINJIE_TYPE:
			
			return kaiFuHuoDongExportService.getRefbYaoShenInfo(userRoleId, subId);
		case ReFaBuUtil.YAOSHENMOWEN_TYPE:
			
			return kaiFuHuoDongExportService.getRefbYaoMoInfo(userRoleId, subId);
		case ReFaBuUtil.CHONGZHI_FANLI_TYPE:	
			
			return rechargeFanliExportService.getRefbInfo(userRoleId, subId);
		case ReFaBuUtil.LEIHAO_TYPE:	
			
			return leiHaoExportService.getInfo(userRoleId, subId);
		case ReFaBuUtil.CAIDAN_TYPE:	
			
			return roleCaidanExportService.getInfo(userRoleId, subId);
		case ReFaBuUtil.QILING_TYPE:
			
			return kaiFuHuoDongExportService.getRefbQiLingInfo(userRoleId, subId);
		case ReFaBuUtil.YAOSHENHUNPO_TYPE:
			
			return kaiFuHuoDongExportService.getRefbYaoShenHunPoInfo(userRoleId, subId);
		case ReFaBuUtil.REFABUXUNBAO_TYPE:
			
			return refbXunBaoExportService.getRefbXunBaoInfo(userRoleId, subId);
		case ReFaBuUtil.REFABUTANBAO_TYPE:
			
			return tanSuoBaoZangExportService.getRefbTanBaoInfo(userRoleId, subId);
		case ReFaBuUtil.YAOSHENMOYIN_TYPE:
			
			return kaiFuHuoDongExportService.getRefbYaoShenMoYinInfo(userRoleId, subId);
		case ReFaBuUtil.TANGBAOXINWEN_TYPE:
			
			return kaiFuHuoDongExportService.getRefbTangBaoXinWenInfo(userRoleId, subId);
		case ReFaBuUtil.DANFU_CHARGE_RANK_TYPE:	
			
			return danfuChargeRankExportService.getRfbInfo(userRoleId, subId);
		case ReFaBuUtil.TUANGOU_TYPE:	

			return tuanGouExportService.getRefbInfo(userRoleId, subId);
		case ReFaBuUtil.RFBLOGIN_TYPE:	
			
			return refabuSevenLoginExportService.getRefbInfo(userRoleId, subId);
		case ReFaBuUtil.MIAOSHA_TYPE:
			
			return refbMiaoshaExportService.getRefbInfo(userRoleId, subId);
		case ReFaBuUtil.LAOWANJIA_TYPE:	
			
			return refbLaowanjiaExportService.getRefbInfo(userRoleId, subId);
		case ReFaBuUtil.HUIYANSHIJING_TYPE:	
			
			return huiYanShiJinExportService.getRefbInfo(userRoleId, subId);
		case ReFaBuUtil.FIRST_CHARGE_REBATE_TYPE:	
		    
		    return refabuFirstChargeRebateExportService.getRefbFirstChargeRebateInfo(userRoleId, subId);
		case ReFaBuUtil.JUEBAN_REBATE_TYPE:	
		    
		    return refabuJuebanExportService.getRefbInfo(userRoleId, subId);
		case ReFaBuUtil.EXTREME_RECHARGE_TYPE:	
		    
		    return rfbExtremeRechargeExportService.getRefbInfo(userRoleId, subId);
		case ReFaBuUtil.EXTREME_XIAOFEI_TYPE:	
		    
		    return kuafuXiaoFeiExportService.getRfbInfo(userRoleId, subId);
		    
		case ReFaBuUtil.LOOP_DAY_CHONGZHI_TYPE:
			return refbRoleShouchongExportService.getLoopDayChongZhiRechargeInfo(userRoleId, subId);
		case ReFaBuUtil.SHENG_JIAN_RANK:
			return kaiFuHuoDongExportService.getRefbWuQiInfo(userRoleId, subId);
		case ReFaBuUtil.ONCE_CHONG_TYPE:
			return onceChongExportService.getRefbInfo(userRoleId, subId);
		case ReFaBuUtil.XINGKONG_TYPE:
			return xkbzExportService.getRefbXkbzInfo(userRoleId, subId);
		case ReFaBuUtil.CZFZ_TYPE:
			return leiChongExportService.getFanLi53Info(userRoleId, subId);
		case ReFaBuUtil.LB_TYPE:
			return  laBaExportService.getRefbInfo(userRoleId, subId);
		default:
			return null;
		}
		
	}
	
	/**
	 * 服务器处理子活动有状态数据主动推送
	 * @param userRoleId
	 * @param subId
	 * @param subType  
	 */
	private void serverSubHandleChangeDataBuySubId(Long userRoleId, int subId,int subType){
		if(subType == ReFaBuUtil.SOUCHONG_TYPE){
			Object rechargeData = refbRoleShouchongExportService.getRechargeValueBySubId(userRoleId, subId);
			if(rechargeData != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.UPDATE_SHOUCHONG, rechargeData);
			}
		}else if(subType == ReFaBuUtil.LOOP_DAY_CHONGZHI_TYPE){
			Object rechargeData = refbRoleShouchongExportService.getRechargeValueBySubId(userRoleId, subId);
			if(rechargeData != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.UPDATE_LOOPDAYCHONG, rechargeData);
			}
		}else if(subType == ReFaBuUtil.RFB_LEVEL_TYPE){
			Object rechargeData = kaiFuHuoDongExportService.getRefbLevelLingQuStatus(subId);
			if(rechargeData != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.UPDATE_QMXX, rechargeData);
			}
		}else if(subType == ReFaBuUtil.XIUXIAN_LB_TYPE){
			Object result = rfbXiuXianExportService.getXiuXianStateDataBySubId(userRoleId, subId);
			 if(result != null){
				 BusMsgSender.send2One(userRoleId, ClientCmdType.XIUXIAN_LIBAO_CHANGE, result);
			 }
		}else if(subType == ReFaBuUtil.ZHANLI_BIPIN_TYPE){
			Object result = kaiFuHuoDongExportService.getRefbZhanLiLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.ZHANLI_BIPIN_CHANGE, result);
			}
		}else if(subType == ReFaBuUtil.YUJIAN_FEIXING_TYPE){
			Object result = kaiFuHuoDongExportService.getRefbYuJianLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.YUJIAN_FEIXING_CHANGE, result);
			}
		}else if(subType == ReFaBuUtil.XIANJIE_YUYI_TYPE){
			Object result = kaiFuHuoDongExportService.getRefbChiBangLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIE_YUYI_CHANGE, result);
			}
		}else if(subType == ReFaBuUtil.XIANJIE_JINGJI_TYPE){
			Object result = kaiFuHuoDongExportService.getRefbJingJiLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIE_JINGJI_CHANGE, result);
			}
		}else if(subType == ReFaBuUtil.XIANZHUANG_QH_TYPE){
			
			Object result = kaiFuHuoDongExportService.getRefbQiangHuaLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.XIANZHUANG_QIANGHUA_CHANGE, result);
			}
		}else if(subType == ReFaBuUtil.QI_PAN_TYPE){
			
			Object result = qiPanExportService.getRefbQiPanLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.QI_PAN_CHANGE, result);
			}
		}else if(subType == ReFaBuUtil.LEI_CHONG_TYPE){
			
			Object result = leiChongExportService.getRefbQiPanLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.LEI_CHONG_CHANGE, result);
			}
		}else if(subType == ReFaBuUtil.TANG_BAO_TYPE){
			
			Object result = kaiFuHuoDongExportService.getRefbTangBaoLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.TANG_BAO_CHANGE, result);
			}
		}else if(subType == ReFaBuUtil.XIAOFEI_PAIMING_TYPE){
			
			Object result = refabuXiaoFeiExportService.getRefbXiaoFeiLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.XIAOFEI_PAIMING_CHANGE, result);
			}
		}else if(subType == ReFaBuUtil.ZHAN_JIA_TYPE){
			
			Object result = kaiFuHuoDongExportService.getRefbZhanJiaLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_ZHANJIA_CHANGE, result);
			}
		}else if(subType == ReFaBuUtil.LIAN_CHONG_TYPE){
			Object result = lianChongExportService.getLianChongStates(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.LIANCHONG_DATA, result);
			}
		}else if(subType == ReFaBuUtil.ONLINE_REWARDS_TYPE){
			Object result = onlineRewardsExportService.getOnlineRewardsStates(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.RFB_ONLINE_REWARDS_INFO, result);
			}
		}else if(subType == ReFaBuUtil.SUOYAOTA_TYPE){
			Object result = suoYaoTaExportService.getRefbState(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.CANGBAOGE_INFO, result);
			}
		}
		else if(subType == ReFaBuUtil.SUPER_DUIHUAN_TYPE){
			Object result = superDuihuanExportService.getSuperDuihuanStates(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.GET_SUPER_DUIHUAN_USER_INFO, result);
			}
		}else if(subType == ReFaBuUtil.CHONGZHI_FANLI_TYPE){
			
			Object result = rechargeFanliExportService.getRefbLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.CHONGZHI_FANLI_CHANGE, result);
			}
		}
		else if(subType == ReFaBuUtil.KUAFU_CHARGE_RANK_TYPE){
			Object result = kuafuChargeRankExportService.getKuafuChargeRankStates(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.GET_KUAFU_CHARGE_RANK_INFO, result);
			}
		}
		else if(subType == ReFaBuUtil.HAPPY_CARD_TYPE){
			Object result = happyCardExportService.getInfo2(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.HAPPY_CARD_USER_INFO, result);
			}
		}
		else if(subType == ReFaBuUtil.YAOSHEN_JINJIE_TYPE){
			Object result = kaiFuHuoDongExportService.getRefbYaoShenLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_JINJIE_CHANGE, result);
			}
		}
		else if(subType == ReFaBuUtil.YAOSHENMOWEN_TYPE){
			Object result = kaiFuHuoDongExportService.getRefbYaoMoLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHENMOWEN_JINJIE_CHANGE, result);
			}
		}
		else if(subType == ReFaBuUtil.CAIDAN_TYPE){
			Object result = roleCaidanExportService.getRefbState(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.GET_CAIDAN_CONFIG_INFO, result);
			}
		}else if(subType == ReFaBuUtil.QILING_TYPE){
			Object result = kaiFuHuoDongExportService.getRefbQiLingLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_JINJIE_QILING, result);
			}
		}else if(subType == ReFaBuUtil.YAOSHENHUNPO_TYPE){
			Object result = kaiFuHuoDongExportService.getRefbYaoShenHunPoLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHENHUNPO_JINJIE_CHANGE, result);
			}
		}else if(subType == ReFaBuUtil.REFABUXUNBAO_TYPE){
			Object result  = refbXunBaoExportService.getAllRefbXunbaoCount(subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.CHANGE_XUNBAO_COUNT, result);
			}
		}else if(subType == ReFaBuUtil.YAOSHENMOYIN_TYPE){
			Object result = kaiFuHuoDongExportService.getRefbYaoShenMoYinLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHENMOYIN_JINJIE_CHANGE, result);
			}
		}else if(subType == ReFaBuUtil.TANGBAOXINWEN_TYPE){
			Object result = kaiFuHuoDongExportService.getRefbTangBaoXinWenLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_JINJIE_TANGBAOXINWEN, result);
			}
		}else if(subType == ReFaBuUtil.DANFU_CHARGE_RANK_TYPE){
			Object result = danfuChargeRankExportService.getDanfuChargeRankStates(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.GET_DANFU_CHARGE_RANK_INFO, result);
			}
		}else if(subType == ReFaBuUtil.MIAOSHA_TYPE){
			Object result = refbMiaoshaExportService.getRefbStates(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.GET_MIAOSHA_INFO, result);
			}
		}else if (subType == ReFaBuUtil.EXTREME_RECHARGE_TYPE){
			Object result = rfbExtremeRechargeExportService.getRFbExtremeRechargeStates(userRoleId,subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.EXTREME_RECHARGE_RECHARGE_CHANGE, result);
			}
		}else if(subType == ReFaBuUtil.EXTREME_XIAOFEI_TYPE){
			Object result = kuafuXiaoFeiExportService.getKuafuXiaoFeiRankStates(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.GET_KUAFU_XIAOFEI_INFO, result);
			}
		}else if(subType == ReFaBuUtil.SHENG_JIAN_RANK){
			Object result = kaiFuHuoDongExportService.getRefbWuQiStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.SHENG_JIAN_PAIMING, result);
			}
		}else if(subType == ReFaBuUtil.ONCE_CHONG_TYPE){
			Object result = onceChongExportService.getRefbStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.UPDATE_ONCECHONG, result);
			}
		}else if(subType == ReFaBuUtil.XINGKONG_TYPE){
			Object result = xkbzExportService.getRefbXkbzLingQuStatus(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.UPDATE_XINGKONGBAOZHAN, result);
			}
		}else if(subType == ReFaBuUtil.CZFZ_TYPE){
			Object result = leiChongExportService.getRefb53Status(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.TUI_SONG_RECHARGE53_YB, result);
			}
		}else if(subType == ReFaBuUtil.LB_TYPE){
			Object result = laBaExportService.getLaBaStates(userRoleId, subId);
			if(result != null){
				BusMsgSender.send2One(userRoleId, ClientCmdType.LABA_REWARD_NOTICE53, result);
			}
		}
	}

	
	/**
	 * 充值元宝热发布监听器业务
	 * @param userRoleId
	 * @param rechargeData 
	 * @param addVal
	 */
	public void chargeYbRefbMonitorHandle(Long userRoleId,Long addVal, Map<String, Long[]> rechargeData){
	    try {
            roleYuanbaoRecordService.rechargeYuanBao(userRoleId, addVal);
        } catch (Exception e) {
            ChuanQiLog.error("",e);
        }
		try {
			//首充类活动
			refbRoleShouchongExportService.changeRecharge(userRoleId, addVal);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		try {
			//棋盘
			qiPanExportService.rechargeYb(userRoleId, addVal);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		try {
			//累充
			leiChongExportService.rechargeYb(userRoleId, addVal);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		try {
			//连充奖励
			lianChongExportService.updateRecharge(userRoleId, addVal);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		try {
			//充值返利
			rechargeFanliExportService.rechargeYb(userRoleId, addVal);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		//老玩家回归
		try {
			refbLaowanjiaExportService.rechargeYb(userRoleId, addVal);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		try {
		    //充值轮盘
		    lunpanExportService.rechargeYb(userRoleId, addVal);
		} catch (Exception e) {
		    ChuanQiLog.error("",e);
		}
		
		try {
			//单笔充值
			onceChongExportService.rechargeYb(userRoleId, addVal);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		try {
			//累充53
			leiChongExportService.rechargeYb53(userRoleId, addVal);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		try {
			//累充53
			laBaExportService.rechargeYb(userRoleId, addVal);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		try {
			//限时礼包
			shopLimitExportService.rechargeYb(userRoleId, addVal,rechargeData);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		//其它充值业务写到下面try catch自己的业务.......................
		
	}
	
	
	/**
	 * 消费元宝热发布监听器业务
	 * @param userRoleId
	 * @param yb
	 */
	public void xfYbRefbMonitorHandle(Long userRoleId,Long yb){
		try {
			//累充
			refabuXiaoFeiExportService.xiaofeiYb(userRoleId, yb);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		try {
			//星空宝藏
			xkbzExportService.xiaofeiYb(userRoleId, yb);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		//记录每天消费的元宝
		try {
			roleYuanbaoRecordService.xiaofeiYuanBao(userRoleId, yb);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		//累计消耗
		try {
			leiHaoExportService.xiaofeiYb(userRoleId, yb);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		try {
			//消费元宝监听
			BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.KUAFU_XIAOFEI_RANK, new Object[]{GameSystemTime.getSystemMillTime(),yb,userRoleId});
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		//其它充值业务写到下面try catch自己的业务.......................
	}
	
	/**
	 * 获得子活动的脚标数据
	 * @param userRoleId
	 * @return
	 */
	public Object[] retIconFlag(long userRoleId){
		
		List<int[]> result = new ArrayList<>();
		
		Map<Integer, ActivityConfig> activityMap = ActivityAnalysisManager.getInstance().loadAll();
		
		//没有主活动热发布数据
		if(ObjectUtil.isEmpty(activityMap)){
			return null;
		}

		for(Map.Entry<Integer, ActivityConfig> model : activityMap.entrySet()){
			ActivityConfig activity = model.getValue();
			
			//主活动是否在
			if (!isShowActivity(userRoleId, activity)) {
				continue;
			}
			
			Map<Integer, ActivityConfigSon> subActivitys = activity.getZihuodongMap();
			if(ObjectUtil.isEmpty(subActivitys)){
				ChuanQiLog.debug("sub activity is empty,mainActId={},name={}",activity.getId(),activity.getName());
				continue;
			}
			for (ActivityConfigSon entry : activity.getZihuodongMap().values()) {
				//子活动删除
				if(entry.isDel()){
					ChuanQiLog.debug("sub activity is delete,subActId={},guid={}",entry.getSubName(),entry.getId());
	                continue;
	            }
				
	            //不在活动期间内的
	            if(!entry.isRunActivity()){
	            	continue;
	            }
	            
	            Class<? extends IActivityService> activityServiceClass = ActivityServiceFacotry.getActivityService(entry.getSubActivityType());
	            if(activityServiceClass == null){
	            	ChuanQiLog.error("subActivity is not regist,type={}",entry.getSubActivityType());
	            	continue;
	            }
	    		IActivityService service = SpringApplicationContext.getApplicationContext().getBean(activityServiceClass);
	    		boolean flag = service.getChildFlag(userRoleId, entry.getId());
	    		result.add(new int[]{entry.getId(),flag?1:0});
			}
		}
		
		return result.toArray();
	}
}
