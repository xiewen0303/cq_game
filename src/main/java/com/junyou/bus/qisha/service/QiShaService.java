package com.junyou.bus.qisha.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.qisha.configure.QiShaBossConfig;
import com.junyou.bus.qisha.configure.export.QiShaBossConfigServiceExport;
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
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.qisha.entity.QiShaStage;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author LiuYu
 * 2015-8-20 下午2:11:41
 */
@Service
public class QiShaService {
	
	@Autowired
	private QiShaBossConfigServiceExport qiShaBossConfigServiceExport;
	@Autowired
	private DingShiActiveConfigExportService dingShiActiveConfigExportService;
	@Autowired
	private PublicFubenStageFactory publicFubenStageFactory;
	@Autowired
	private MapConfigExportService mapConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private BusScheduleExportService busScheduleExportService;
	@Autowired
	private DaZuoConfigExportService daZuoConfigExportService; 
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private EmailExportService emailExportService;
	
	
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
		Integer mapId = config.getMapId();
		String stageId = StageUtil.getStageId(mapId, 1);
		MapConfig mapConfig = mapConfigExportService.load(mapId);
		QiShaStage stage = publicFubenStageFactory.createNoMonsters(stageId, 1, mapConfig);
		if(stage == null){
			return;
		}
		stage.initBoss(qiShaBossConfigServiceExport.getBossIds());
		stage.start();
		StageManager.addStageCopy(stage);
		
		//活动结束
		Long scheduleTime = (endTime - cur)/1000 + 30;
		BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_QISHA_END,stageId);
		busScheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.COMPONENT_QISHA, runable, scheduleTime.intValue(), TimeUnit.SECONDS);
	}
	
	public void ActiveEnd(String stageId){
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null){
			return;
		}
		if(!StageType.QISHA.equals(iStage.getStageType())){
			ChuanQiLog.error("活动结束时，发现活动场景不是七杀活动地图，当前活动地图类型：{}",iStage.getStageType());
			return;
		}
		QiShaStage stage = (QiShaStage)iStage;
		stage.stop();
		if(stage.isCanRemove()){
			StageManager.removeCopy(stage);
		}else{
			stage.kickAll();
		}
	}
	
	public Object[] enterMap(Long userRoleId,Integer activeId){
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(activeId);
		if(config == null || config.getType() != GameConstants.DINGSHI_QISHA){
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
		BusMsgSender.send2BusInner(userRoleId,InnerCmdType.S_APPLY_CHANGE_STAGE,new Object[] { dituCoinfig.getId(), x, y,MapType.QISHA_MAP_TYPE });
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
	
	public void addExp(String stageId){
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null){
			return;
		}
		if(!StageType.QISHA.equals(iStage.getStageType())){
			return;
		}
		QiShaStage stage = (QiShaStage)iStage;
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
						addExp = addExp * 10;
						addzhenqi = addzhenqi * 10;
						StageMsgSender.send2Bus(role.getId(), InnerCmdType.INNER_DAZUO_AWARD, new Object[]{addExp,addzhenqi,ClientCmdType.QISHA_GET_EXP_ZHENQI}); 
					}
				}
			}catch (Exception e) {
				ChuanQiLog.error("",e);
			}
			//开启下次定时
			BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.QISHA_EXP_ADD, stageId);
			busScheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.COMPONENT_QISHA_EXP, runable, GameConstants.DAZUO_AWARD_CD, TimeUnit.SECONDS);
		}
	}
	
	public void sendBossDeadGift(String monsterId,Long userRoleId,String userRoleIds,String bossName){
		QiShaBossConfig config = qiShaBossConfigServiceExport.getConfigById(monsterId);
		if(config == null){
			ChuanQiLog.error("七杀BOSS死亡时，七杀BOSS击杀奖励配置不存在。BOSSID:{},userRoleId:{},userRoleIds:{}",monsterId,userRoleId,userRoleIds);
			return;
		}
		String title = EmailUtil.getCodeEmail(GameConstants.QISHA_KILL_CODE_TITLE);
		String killContent = EmailUtil.getCodeEmail(GameConstants.QISHA_KILL_CODE, bossName);
		emailExportService.sendEmailToOne(userRoleId, title,killContent, GameConstants.EMAIL_TYPE_SINGLE, config.getKillItemStr());
		String hurtContent = EmailUtil.getCodeEmail(GameConstants.QISHA_HURT_CODE, bossName);
		emailExportService.sendEmailToMany(userRoleIds,title, hurtContent, GameConstants.EMAIL_TYPE_SINGLE, config.getHurtItemStr());
	}
	
	
}
