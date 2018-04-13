package com.junyou.bus.hundun.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.ChaosLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.dazuo.configure.export.DaZuoConfig;
import com.junyou.gameconfig.dazuo.configure.export.DaZuoConfigExportService;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfigExportService;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.stage.hundun.configure.export.HunDunWarGuiZeBiaoConfig;
import com.junyou.stage.hundun.configure.export.HunDunWarGuiZeBiaoConfigExportService;
import com.junyou.stage.hundun.configure.export.HundunWarGiftConfig;
import com.junyou.stage.hundun.configure.export.HundunWarGiftConfigExportService;
import com.junyou.stage.hundun.entity.HundunRank;
import com.junyou.stage.hundun.entity.HundunStage;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author LiuYu
 * 2015-9-6 下午2:34:28
 */
@Service
public class HundunService {
	@Autowired
	private BusScheduleExportService busScheduleExportService;
	@Autowired
	private DaZuoConfigExportService daZuoConfigExportService; 
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private DingShiActiveConfigExportService dingShiActiveConfigExportService;
	@Autowired
	private MapConfigExportService mapConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private PublicFubenStageFactory publicFubenStageFactory;
	@Autowired
	private HunDunWarGuiZeBiaoConfigExportService hunDunWarGuiZeBiaoConfigExportService;
	@Autowired
	private HundunWarGiftConfigExportService hundunWarGiftConfigExportService;
	@Autowired
	private EmailExportService emailExportService;
	
	public void addExp(String stageId){
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null){
			return;
		}
		if(!StageType.CHAOS.equals(iStage.getStageType())){
			return;
		}
		HundunStage stage = (HundunStage)iStage;
		if(stage.isOpen()){
			try{
				List<IStageElement> roles = stage.getAllElements(ElementType.ROLE);
				for (IStageElement iStageElement : roles) {
					IRole role = (IRole)iStageElement;
					DaZuoConfig daZuoConfig = daZuoConfigExportService.loadById(role.getLevel());
					if(daZuoConfig != null){
						int addExp = daZuoConfig.getSkill1exp();
						int addzhenqi = daZuoConfig.getZhenqi();
						
						//VIP特权经验加成
						int dazuo = role.getBusinessData().getDazuoExp();
						if(dazuo == 0){//未知加成
							dazuo = roleVipInfoExportService.getVipTequan(role.getId(), GameConstants.VIP_DAZUO_EXP);
							role.getBusinessData().setDazuoExp(dazuo);
							dazuo += 100;
							addExp = addExp * dazuo / 100;
							addzhenqi = addzhenqi * dazuo / 100;
						}else if(dazuo > 100){
							addExp = addExp * dazuo / 100;
							addzhenqi = addzhenqi * dazuo / 100;
						}
						addExp = addExp * stage.getExpRate();
						addzhenqi = addzhenqi * stage.getExpRate();
						StageMsgSender.send2Bus(role.getId(), InnerCmdType.INNER_DAZUO_AWARD, new Object[]{addExp,addzhenqi,ClientCmdType.CHAOS_GET_EXP_ZHENQI});// 换成混沌奖励指令 
					}
				}
			}catch (Exception e) {
				ChuanQiLog.error("",e);
			}
			//开启下次定时
			BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_HUNDUN_ADD_EXP, stageId);
			busScheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.COMPONENT_CHAOS_ADD_EXP, runable, GameConstants.DAZUO_AWARD_CD, TimeUnit.SECONDS);
		}
	}
	
	public Object[] enterMap(Long userRoleId,Integer activeId){
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(activeId);
		if(config == null || config.getType() != GameConstants.DINGSHI_HUNDUN){
			return AppErrorCode.CONFIG_ERROR;
		}
		long startTime = DatetimeUtil.getTheTime(config.getStartTime1()[0], config.getStartTime1()[1]);
		long endTime = DatetimeUtil.getTheTime(config.getEndTime1()[0], config.getEndTime1()[1]);
		long cur = GameSystemTime.getSystemMillTime();
		if(cur < startTime || endTime < cur){
			return AppErrorCode.NOT_IN_TIME_CANNOT_CHANGE_MAP;//不在活动期间内 
		}
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			return AppErrorCode.ROLE_IS_NOT_EXIST;//角色不存在 
		}
		if(role.getLevel() < config.getMinLevel()){
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		// 判断是否在副本中
		if (stageControllExportService.inFuben(userRoleId)) {
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
		}
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config.getMapId());
		if (dituCoinfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		// 发送到场景进入地图
		int birthPoint[] = dituCoinfig.getRandomBirth();
		int x = birthPoint[0];
		int y = birthPoint[1];
		BusMsgSender.send2BusInner(userRoleId,InnerCmdType.S_APPLY_CHANGE_STAGE,new Object[] { dituCoinfig.getId(), x, y,MapType.HUNDUN_MAP_TYPE });
		return null;
	}
	
	public Object[] leaveMap(Long userRoleId){
		if (!stageControllExportService.inFuben(userRoleId)) {
			return AppErrorCode.FUBEN_NOT_IN_FUBEN;
		}
		// 通知场景离开战场
		StageMsgSender.send2StageControl(userRoleId,InnerCmdType.S_APPLY_LEAVE_STAGE, userRoleId);
		return null;
	}
	
	public void ActiveStart(Integer id){
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(id);
		if(config == null){
			return;
		}
		long endTime = DatetimeUtil.getTheTime(config.getEndTime1()[0], config.getEndTime1()[1]);
		long cur = GameSystemTime.getSystemMillTime();
		if(endTime < cur){
			return;
		}
		HunDunWarGuiZeBiaoConfig hdConfig = hunDunWarGuiZeBiaoConfigExportService.getHdConfigByMinId();
		if(hdConfig == null){
			ChuanQiLog.error("混沌战场配置未解析到.");
			return;
		}
		Long scheduleTime = endTime - cur + GameConstants.SPRING_DINGSHI_ERRER_TIME;
		for (int i = 0; i <= GameConstants.CHAOS_MAX_CENG; i++) {
			Integer mapId = hdConfig.getMapId();
			String stageId = StageUtil.getStageId(mapId, 1);
			MapConfig mapConfig = mapConfigExportService.load(mapId);
			HundunStage stage = publicFubenStageFactory.createWithActiveConfig(stageId, 1, mapConfig, config, hdConfig);
			hdConfig = hunDunWarGuiZeBiaoConfigExportService.getHdConfigByNextId(hdConfig.getId());
			if(stage == null){
				continue;
			}
			stage.start();
			StageManager.addStageCopy(stage);
			BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_HUNDUN_END,stageId);
			busScheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.COMPONENT_CHAOS_RANK + i, runable, scheduleTime.intValue(), TimeUnit.MILLISECONDS);
			
			if(hdConfig == null){
				break;
			}
		}
		
	}
	
	
	public void ActiveEnd(String stageId){
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null){
			return;
		}
		if(!StageType.CHAOS.equals(iStage.getStageType())){
			ChuanQiLog.error("活动结束时，发现活动场景不是混沌战场活动地图，当前活动地图类型：{}",iStage.getStageType());
			return;
		}
		HundunStage stage = (HundunStage)iStage;
		stage.stop();
		Map<Integer, HundunWarGiftConfig> map = hundunWarGiftConfigExportService.getCengConfigs(stage.getCengId());
		if(map == null || map.size() < 1){
			ChuanQiLog.error("混沌战场结束时，发现没有解析到活动排名奖励。");
			return;
		}
		List<Object[]> top5 = null;
		if(stage.getCengId() == GameConstants.CHAOS_MAX_CENG){
			top5 = new ArrayList<>();
		}
		List<HundunRank> list = stage.reRankIngChaos(true);
		List<Long> roleIds = null;
		if(list != null && list.size() > 0){
			roleIds = new ArrayList<>();
			for (HundunRank hundunRank : new ArrayList<>(list)) {
				roleIds.add(hundunRank.getUserRoleId());
				HundunWarGiftConfig giftConfig = map.get(hundunRank.getRank());
				if(giftConfig == null){
					continue;
				}
				if(stage.getCengId() == GameConstants.CHAOS_MAX_CENG){
					top5.add(new Object[]{hundunRank.getRank(),hundunRank.getUserRoleId(),hundunRank.getUserName()});
				}
				
				String title = EmailUtil.getCodeEmail(GameConstants.CHAOS_PAIMING_GIFT_EMAIL_TITLE);
				String content = EmailUtil.getCodeEmail(GameConstants.CHAOS_PAIMING_GIFT_EMAIL, stage.getCengId().toString(),hundunRank.getRank()+"");
				emailExportService.sendEmailToOne(hundunRank.getUserRoleId(),title,content, GameConstants.EMAIL_TYPE_SINGLE, giftConfig.getGift());
			}
		}
		if(top5 != null && top5.size() > 0){
			StageMsgSender.send2All(ClientCmdType.CHAOS_NOTICE_TOP_5_NAMES, top5.toArray());
		}
		
		if(roleIds != null){
			String title = EmailUtil.getCodeEmail(GameConstants.CHAOS_PUJIANG_GIFT_EMAIL_TITLE);
			String content = EmailUtil.getCodeEmail(GameConstants.CHAOS_PUJIANG_GIFT_EMAIL, stage.getCengId().toString());
			emailExportService.sendEmailToMany(roleIds,title, content, GameConstants.EMAIL_TYPE_SINGLE, stage.getHdConfig().getPujiang());
		}
		
		pringLog(new ArrayList<>(stage.getRoleJfs().values()), stage.getCengId());
		
		if(stage.isCanRemove()){
			StageManager.removeCopy(stage);
		}else{
			stage.kickAll();
		}
	}
	
	private void pringLog(List<HundunRank> list,int cengId){
		for (HundunRank hundunRank : list) {
			try {
				GamePublishEvent.publishEvent(new ChaosLogEvent(hundunRank.getUserRoleId(), hundunRank.getUserName(), cengId, hundunRank.getRank(), hundunRank.getJfVal()));
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}
	}
	
}
