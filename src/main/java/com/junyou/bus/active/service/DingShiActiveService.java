package com.junyou.bus.active.service;

import com.junyou.bus.activityboss.service.DingShiShuaGuaiService;
import com.junyou.bus.dati.service.DaTiService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.tanbao.export.TanBaoPaiMingJiangLiConfigExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.TanBaoPublicConfig;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.stage.model.stage.tanbao.TanBaoManager;
import com.junyou.stage.model.stage.tanbao.TanBaoRoleVo;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class DingShiActiveService {

	@Autowired
	private DingShiActiveConfigExportService dingShiActiveConfigExportService;
	@Autowired
	private BusScheduleExportService scheduleExportService;
	@Autowired
	private DingShiShuaGuaiService  dingShiShuaGuaiService;
	@Autowired
	private DaTiService daTiService;
	@Autowired 
	private TanBaoPaiMingJiangLiConfigExportService tanBaoPaiMingJiangLiConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private EmailExportService emailExportService;

	
	private byte weekType = 0;
	
	/**
	 * 初始化今日活动
	 */
	public void initDayActive(){
		if(KuafuConfigPropUtil.isKuafuServer()){
			weekType = GameConstants.DINGSHI_WEEK_TYPE_NOMAL;
			return;//跨服仅启动跨服活动  暂无
		}
		if(weekType != GameConstants.DINGSHI_WEEK_TYPE_NOMAL){
			if(ServerInfoServiceManager.getInstance().getServerHefuTimes() > 0){
				if(ServerInfoServiceManager.getInstance().getHefuDays() < 8){
					weekType = GameConstants.DINGSHI_WEEK_TYPE_HF;
				}
			}else if(ServerInfoServiceManager.getInstance().getKaifuDays() < 8){
				weekType = GameConstants.DINGSHI_WEEK_TYPE_KF;
			}else{
				weekType = GameConstants.DINGSHI_WEEK_TYPE_NOMAL;
			}
		}
		
		long cur = GameSystemTime.getSystemMillTime();
		int week = DatetimeUtil.getCurrentWeek();
		for (DingShiActiveConfig config : dingShiActiveConfigExportService.getAllConfigs()) {
			if(config.getOpenDay() > ServerInfoServiceManager.getInstance().getKaifuDays()){
				continue;//未到活动开启天数
			}
			if(config.getWeeksByType(weekType) == null || !config.getWeeksByType(weekType).contains(week)){
				continue;//今天不开
			}
			long endTime = DatetimeUtil.getTheDayTheTime(config.getEndTime1()[0], config.getEndTime1()[1],cur);
			if(endTime <= cur){
				continue;
			}
			long startTime = DatetimeUtil.getTheDayTheTime(config.getStartTime1()[0], config.getStartTime1()[1],cur);
			int id = config.getId();
			BusTokenRunable runable = null;
			Long delay = startTime - cur;
			if(config.getType() == GameConstants.DINGSHI_DAZUO_MANY_BEI){//多倍打坐活动
				if(delay > 0){
					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_DAZUO_START,id);
				}else{
					dazuoActiveStart(id);
				}
			}else if(config.getType() == GameConstants.DINGSHI_KILL_MANY_BEI){//多倍打怪活动
				if(delay > 0){
					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_KILL_MONSTER_START,id);
				}else{
					killMonsterActiveStart(id);
				}
			}else if(config.getType() == GameConstants.DINGSHI_YABIAO_MANY_BEI){//多倍打怪活动
				if(delay > 0){
					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_YABIAO_MONSTER_START,id);
				}else{
					yaBiaoActiveStart(id);
				}
			}else if(config.getType() == GameConstants.DINGSHI_DATI){
				if(delay>0){
					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_DATI_STATRT,id);			
				}else{
					daTiService.datiActivityStart(id);
				}
			}else if(config.getType() == GameConstants.DINGSHI_COLLECT_BOX){
				if(delay>0){
					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_COLLECT_BOX_START,id);
				}else{
				    collectBoxActivityStart(id);
				}
			}else if(config.getType() == GameConstants.DINGSHI_CAMP_WAR){
				if(delay>0){
					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_CAMP_WAR_START,new Object[]{id,config.getMapId() + "_1"});
				}else{
					campWarActivityStart(config.getMapId() + "_1",id);
				}
			}else if(config.getType() == GameConstants.DINGSHI_XIANGONG_TANBAO){
				if(delay>0){
					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_XIANGONG_START,id);
				}else{
					xianGongActiveStart(id);
				}
			}
			else if(config.getType() == GameConstants.DINGSHI_TERRITORY){
				if(delay>0){
					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_TERRITORY_START,id);
				}else{
					territoryActivityStart(id);
				}
			}
			else if(config.getType() == GameConstants.DINGSHI_WENQUAN){
				if(delay>0){
					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_WENQUAN_START,id);
				}else{
					wenquanActivityStart(id);
				}
			}else if(config.getType() == GameConstants.DINGSHI_ZHENGBASAI){
				if(delay>0){
					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_ZHENGBASAI_START,id);
				}else{
					zhengbasaiActivityStart(id);
				}
			}else if(config.getType() == GameConstants.DINGSHI_QISHA){
				if(delay>0){
					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_QISHA_START,id);
				}else{
					BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_QISHA_START,id);
				}
			}else if(config.getType() == GameConstants.DINGSHI_HUNDUN){
				if(delay>0){
					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_HUNDUN_START,id);
				}else{
					BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_HUNDUN_START,id);
				}
			}else if(config.getType() == GameConstants.DINGSHI_KUAFUBOSS){
				if(delay>0){
					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_KUAFUBOSS_ACTIVE_START,id);
				}else{
					BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_KUAFUBOSS_ACTIVE_START,id);
				}
			}
			
//			else if(config.getType() == GameConstants.DINGSHI_ACTIVITY_BOSS){
//				if(delay > 0){
//					runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_BOSS_START,id);
//				}else{
//					bossActiveStart(id);
//				}
//			}
			
			
			if(runable != null){
				scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(),GameConstants.COMPONENET_DINGSHI_ACTIVE + id, runable, delay.intValue(),TimeUnit.MILLISECONDS);
			}
		}
		
		//刷新野外boss
		taskRefreshMonsterStart();
	}
	
//	/**
//	 * 野外活动boss
//	 * @param id
//	 */
//	public void bossActiveStart(int id) {
//		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(id);
//		
//		long endTime = DatetimeUtil.getTheTime(config.getEndTime1()[0], config.getEndTime1()[1]);
//		for (Integer mapId : config.getMaps()) {
//			 
//		}
//		
//		//开启结束定时
//		Long delay = endTime - GameSystemTime.getSystemMillTime();
//		if(delay > 0){
//			scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(),GameConstants.COMPONENET_DINGSHI_ACTIVE + id, new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_DAZUO_STOP,null), delay.intValue(),TimeUnit.MILLISECONDS);
//		}else{
//			dazuoActiveStop();
//		}
//	}

	/**
	 * 打坐活动开始
	 * @param id
	 */
	public void dazuoActiveStart(Integer id){
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(id);
		float bei = Float.parseFloat(config.getData1());
		long endTime = DatetimeUtil.getTheTime(config.getEndTime1()[0], config.getEndTime1()[1]);
		for (Integer mapId : config.getMaps()) {
			ActiveUtil.setDazuoMap(mapId, bei);
		}
		
		//开启结束定时
		Long delay = endTime - GameSystemTime.getSystemMillTime();
		if(delay > 0){
			scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(),GameConstants.COMPONENET_DINGSHI_ACTIVE + id, new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_DAZUO_STOP,null), delay.intValue(),TimeUnit.MILLISECONDS);
		}else{
			dazuoActiveStop();
		}
	}
	/**
	 * 打坐活动结束
	 */
	public void dazuoActiveStop(){
		ActiveUtil.clearDazuoMap();
	}
	
	/**
	 * 打怪活动开始
	 * @param id
	 */
	public void killMonsterActiveStart(Integer id){
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(id);
		int bei = Integer.parseInt(config.getData1());
		long endTime = DatetimeUtil.getTheTime(config.getEndTime1()[0], config.getEndTime1()[1]);
		ActiveUtil.setKillBei(bei);
		
		//开启结束定时
		Long delay = endTime - GameSystemTime.getSystemMillTime();
		if(delay > 0){
			scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(),GameConstants.COMPONENET_DINGSHI_ACTIVE + id, new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_KILL_MONSTER_STOP,null), delay.intValue(),TimeUnit.MILLISECONDS);
		}else{
			killMonsterActiveStop();
		}
	}
	/**
	 * 打怪活动结束
	 */
	public void killMonsterActiveStop(){
		ActiveUtil.setKillBei(0);
	}
	/**
	 * 押镖活动开始
	 * @param id
	 */
	public void yaBiaoActiveStart(Integer id){
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(id);
		int bei = Integer.parseInt(config.getData1());
		long endTime = DatetimeUtil.getTheTime(config.getEndTime1()[0], config.getEndTime1()[1]);
		ActiveUtil.setYabiaoBei(bei);
		
		//开启结束定时
		Long delay = endTime - GameSystemTime.getSystemMillTime();
		if(delay > 0){
			scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(),GameConstants.COMPONENET_DINGSHI_ACTIVE + id, new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_YABIAO_MONSTER_STOP,null), delay.intValue(),TimeUnit.MILLISECONDS);
		}else{
			yaBiaoActiveStop();
		}
	}
	/**
	 * 押镖活动结束
	 */
	public void yaBiaoActiveStop(){
		ActiveUtil.setYabiaoBei(1);
	}
	
	/**
	 * 获取活动（如果不在活动期间内，返回null）
	 * @param activeId
	 * @return
	 */
	public DingShiActiveConfig getActiveInTime(Integer activeId){
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(activeId);
		if(config ==null){
			return null;
		}
		long cur = GameSystemTime.getSystemMillTime();
		int week = DatetimeUtil.getCurrentWeek();
		if(config.getWeeksByType(weekType) == null || !config.getWeeksByType(weekType).contains(week)){
			return null;//今天不开
		}
		long endTime = DatetimeUtil.getTheDayTheTime(config.getEndTime1()[0], config.getEndTime1()[1],cur);
		if(endTime <= cur){
			return null;//已经结束
		}
		long startTime = DatetimeUtil.getTheDayTheTime(config.getStartTime1()[0], config.getStartTime1()[1],cur);
		if(startTime > cur){
			return null;//尚未开始
		}
		return config;
	}
	
	/**
	 * 通知场景阵营战活动开始
	 */
	public void campWarActivityStart(String stageId,int activityId){
		BusMsgSender.send2Stage(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.S_CAMP_WAR_START, new Object[]{activityId,stageId});
	}
	
	/**
	 * 领地战活动开始
	 */
	public void territoryActivityStart(int activityId){
		BusMsgSender.send2Stage(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.TERRITORY_START, new Object[]{activityId});
	}
	/**
	 * 温泉活动开始
	 */
	public void wenquanActivityStart(int activityId){
		BusMsgSender.send2Stage(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.WENQUAN_START, new Object[]{activityId});
	}
	

	/**
	 *争霸赛活动开始
	 */
	public void zhengbasaiActivityStart(int activityId){
		BusMsgSender.send2Stage(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.ZHENGBASAI_START, new Object[]{activityId});
	}
	
	
	/**
	 * 通知场景宝箱采集活动开始
	 */
	public void collectBoxActivityStart(int activityId){
		BusMsgSender.send2Stage(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.COLLECT_BOX_START, activityId);
		
		/**
		 * 发布公告
		 */
		BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_ACTIVE_NOTICE,activityId);
		scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(),GameConstants.COMPONENET_DINGSHI_ACTIVE_NOTICE + activityId, runable,15*1000,TimeUnit.MILLISECONDS);
	}
	/**
	 * 通知场景宝箱采集活动结束
	 */
	public void collectBoxActivityEnd(){
		BusMsgSender.send2Stage(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.COLLECT_BOX_STOP, null);
	}
	
	/**
	 * 仙宫探宝活动开始
	 * @param id
	 */
	public void xianGongActiveStart(Integer id){
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(id);
		long endTime = DatetimeUtil.getTheTime(config.getEndTime1()[0], config.getEndTime1()[1]);
		
		//开启结束定时
		Long delay = endTime - GameSystemTime.getSystemMillTime();
		if(delay > 0){
			BusMsgSender.send2Stage(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_XIANGONG_INIT, id);
			TanBaoManager.getManager().setStopTime(endTime);
			scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(),GameConstants.COMPONENET_DINGSHI_ACTIVE + id, new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_XIANGONG_STOP,null), delay.intValue(),TimeUnit.MILLISECONDS);
		}else{
			xianGongActiveStop();
		}
	}
	/**
	 * 仙宫探宝活动结束
	 */
	public void xianGongActiveStop(){
		TanBaoManager.getManager().activeStop();
		TanBaoPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TANBAO);
		if(config != null){
			//结算奖励
			for (TanBaoRoleVo roleVo : TanBaoManager.getManager().getRankList()) {
				String gift = tanBaoPaiMingJiangLiConfigExportService.getGift(roleVo.getRank());
				String title = EmailUtil.getCodeEmail(AppErrorCode.MOGONG_XUNBAO_EMAIL_TITLE);
				String email = EmailUtil.getCodeEmail(config.getEmailCode(), roleVo.getRank() + "");
				emailExportService.sendEmailToOne(roleVo.getUserRoleId(),title, email, GameConstants.EMAIL_TYPE_SINGLE, gift);

				//判定玩家是否在仙宫寻宝的场景中
				if(TanBaoManager.getManager().checkInStarge(roleVo.getUserRoleId())){
					BusMsgSender.send2BusInner(roleVo.getUserRoleId(), InnerCmdType.S_APPLY_LEAVE_STAGE, roleVo.getUserRoleId());
				}
			}
		}
		TanBaoManager.getManager().clear();
	}
	
	/**
	 * 每天凌晨刷新一下
	 */
	public void taskRefreshMonsterStart(){
		dingShiShuaGuaiService.productDingShiMonster();
	}
	
	/**
	 * 定时通知刷宝箱公告
	 */
	public void dingshiActiveNotice(int activityId) {
		if(getActiveInTime(activityId) != null){
			BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{AppErrorCode.ACTIVTE_NOTICE});
			
			/**
			 * 发布公告
			 */
			BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_ACTIVE_NOTICE,activityId);
			scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(),GameConstants.COMPONENET_DINGSHI_ACTIVE_NOTICE + activityId, runable,10*60*1000,TimeUnit.MILLISECONDS);
		}
	}
}
