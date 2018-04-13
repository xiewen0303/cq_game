package com.junyou.bus.activityboss.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.activityboss.dao.ActivityBossDao;
import com.junyou.bus.activityboss.manage.ActivityBoss;
import com.junyou.bus.activityboss.manage.ActivityBossInfo;
import com.junyou.bus.activityboss.manage.ActivityBossManage;
import com.junyou.bus.activityboss.manage.BossHurtRank;
import com.junyou.bus.activityboss.manage.BossKillInfo;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.yijiboss.configure.export.GuShenYiJiPaiMingConfigExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.YWBossPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.YijibossPublicConfig;
import com.junyou.gameconfig.shuaguai.configure.export.DingShiShuaGuaiConfig;
import com.junyou.gameconfig.shuaguai.configure.export.DingShiShuaGuaiConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 野外活动boss
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-25 下午3:24:43
 */
@Service
public class ActivityBossService {
	
	@Autowired
	private GongGongShuJuBiaoConfigExportService publicConfigExportServcie;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private ActivityBossDao activityBossDao;
	@Autowired
	private RoleExportService roleExportService;
//	@Autowired
//	private DingShiShuaGuaiConfigExportService refreshMonsterConfigExportService;
	
	@Autowired
	private GuShenYiJiPaiMingConfigExportService guShenYiJiPaiMingConfigExportService;
	
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;

	
	public String getRoleName(long userRoleId){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper!=null){
			return  roleWrapper.getName();
		}
		return null;
	}
	 
	
	/**
	 * 启动时初始化
	 */
	public void initBossState() {
		Map<String,ActivityBossInfo>  activityInfos = activityBossDao.getAllBossInfoDb();
		if(activityInfos != null){
			ActivityBossManage.initABInfos(activityInfos);
		}
	}
	
//	//将没有杀死的  但是活动时间没有结束掉的  重新刷出来
//	public void startHandle(Map<String,ActivityBossInfo> abInfoTemps) {
//		for (Entry<String,ActivityBossInfo> entry : abInfoTemps.entrySet()) {
//			String key = entry.getKey();
//			int[] datas = ActivityBossUtil.getMapConfigId(key);
//			int mapId = datas[0];
//			int configId = datas[1];
//			
//			DingShiShuaGuaiConfig  config = refreshMonsterConfigExportService.load(configId);
//			if(config == null){
//				continue;
//			}
//			
//			ActivityBossInfo abInfo = entry.getValue();
//			
//			Map<Integer, BossKillInfo> bkInfos = abInfo.getBossKillInfos();
//			for (Entry<Integer,BossKillInfo> element : bkInfos.entrySet()) {
//				BossKillInfo bkInfo = element.getValue();
//				long nowTime = GameSystemTime.getSystemMillTime();
//				if(bkInfo.getState() == GameConstants.BOSS_NO_KILL && bkInfo.getEndTime() > nowTime){
//					
//					String  stageId = StageUtil.getStageId(mapId, bkInfo.getLine());
//					
//					long endTime = DatetimeUtil.getTheTime(config.getTime2()[0], config.getTime2()[1]);
//					
//					int disappearDuration =  (int)(endTime -nowTime);
//					 
//					Object[] result = new Object[]{configId,bkInfo.getLine(),disappearDuration,mapId};
//					
//					//创建新的boss出来
//					StageMsgSender.send2StageInner(GameConstants.DEFAULT_ROLE_ID, stageId, InnerCmdType.INNER_DS_BOSS_MONSTER, result);
//				}
//			}
//			
//		} 
//	}
	
	/**
	 * 发放奖励
	 */
	public void giveAwards(List<BossHurtRank> ranks) {
		YWBossPublicConfig config =	publicConfigExportServcie.loadPublicConfig(PublicConfigConstants.MOD_YW_BOSS);
		int size = ranks.size();
		if(ranks.size() > 10){
			size = 10;
		}
		for (int i = 0; i<size; i++) {
			BossHurtRank rankRole =	ranks.get(i);
			String itemId = config.getAwardsByRank(i);
			
			String title = EmailUtil.getCodeEmail(AppErrorCode.DSBOSS_EMAIL_CONTENT_CODE_TITLE);
			String content = EmailUtil.getCodeEmail(AppErrorCode.DSBOSS_EMAIL_CONTENT_CODE);
			Map<String, Integer> totalItems = new HashMap<>();
			totalItems.put(itemId, 1);
			String[] attachments = EmailUtil.getAttachments(totalItems);
			for (String attachment : attachments) {
				emailExportService.sendEmailToOne(rankRole.getUserRoleId(),title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
			}
		}
	}
	/**
	 * 发放奖励
	 */
	public void giveAwards4Yijiboss(YijibossPublicConfig yijibossPublicConfig,long killerId,List<BossHurtRank> ranks) {
		//击杀奖励
		ChuanQiLog.info("yijiboss killer={}", killerId);
		String goodsId = yijibossPublicConfig.getLastattack();
		String title = EmailUtil.getCodeEmail(AppErrorCode.YIJIBOSS_ATTACK_EMAIL_CONTENT_TITLE);
		String content = EmailUtil.getCodeEmail(AppErrorCode.YIJIBOSS_KILL_EMAIL_CONTENT_CODE);
		Map<String, Integer> totalItems = new HashMap<>();
		totalItems.put(goodsId, 1);
		String[] attachments = EmailUtil.getAttachments(totalItems);
		emailExportService.sendEmailToOne(killerId,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachments[0]);
		//排名奖励
		int rankSize = ranks.size();
		int maxRank = guShenYiJiPaiMingConfigExportService.getMaxRank();
		int jiangSize = rankSize > maxRank?maxRank:rankSize; 
		for (int i = 0; i<jiangSize; i++) {
			BossHurtRank rankRole =	ranks.get(i);
			ChuanQiLog.info("yijiboss rank={} userRoleId={}",i+1, rankRole.getUserRoleId());
			Map<String, Integer> jiangliMap = guShenYiJiPaiMingConfigExportService.loadByRank(i+1).getJiangitem();
			
			content = EmailUtil.getCodeEmail(AppErrorCode.YIJIBOSS_RANK_EMAIL_CONTENT_CODE,String.valueOf(i+1));
			attachments = EmailUtil.getAttachments(jiangliMap);
			for (String attachment : attachments) {
				emailExportService.sendEmailToOne(rankRole.getUserRoleId(),title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
			}
		}
		//参与奖励
		if(rankSize>maxRank){
			for (int i = maxRank; i<rankSize; i++) {
				BossHurtRank rankRole =	ranks.get(i);
				ChuanQiLog.info("yijiboss canyujiang userRoleId={}", rankRole.getUserRoleId());
				Map<String, Integer> jiangliMap = guShenYiJiPaiMingConfigExportService.loadByRank(GuShenYiJiPaiMingConfigExportService.anyRank).getJiangitem();
				
				content = EmailUtil.getCodeEmail(AppErrorCode.YIJIBOSS_ATTACK_EMAIL_CONTENT_CODE);
				attachments = EmailUtil.getAttachments(jiangliMap);
				for (String attachment : attachments) {
					emailExportService.sendEmailToOne(rankRole.getUserRoleId(),title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
				}
			}
		}
	}
	
	/**
	 * 修改伤害排行数据
	 * @param userRoleId
	 * @param monsterId
	 * @param harmVal
	 */
	public void modifyHurtValue(Long userRoleId, long monsterId, long harmVal,boolean isDead) {
		ActivityBoss activityBoss =	ActivityBossManage.getBoss(monsterId);
		if(activityBoss == null) {
			return;
		}
		
		activityBoss.addHurt(userRoleId, harmVal);
		//被杀怪发放奖励
		if(isDead) {
			List<BossHurtRank> ranks = activityBoss.getRanks();
			YijibossPublicConfig yijibossPublicConfig = publicConfigExportServcie.loadPublicConfig(PublicConfigConstants.MOD_YIJIBOSS);
			if(activityBoss.getMonsterId().equals(yijibossPublicConfig.getMonsterId())){
				String killerName = null;
				for(BossHurtRank e:ranks){
					if(e.getUserRoleId()==userRoleId.longValue()){
						killerName = e.getRoleName();
					}
				}
				BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT7, new Object[]{
						AppErrorCode.YIJIBOSS_KILL_NOTICE, 
						new Object[]{new Object[]{2, userRoleId, killerName}}
						});
				giveAwards4Yijiboss(yijibossPublicConfig,userRoleId, ranks);
			}else{
				
				giveAwards(ranks);
				//活跃度lxn
//				setHuoyuedu(ranks);
			}
			//移除ActivityBossManage中的boss引用
			ActivityBossManage.remove(monsterId);
			
			ActivityBossManage.modifyBossState(activityBoss.getDsMonsterConfigId(), activityBoss.getLine(), GameConstants.BOSS_KILL, getRoleName(userRoleId));
			
			//取消刷新排行榜定时器
			activityBoss.getScheduler().cancelSchedule(activityBoss.getId().toString(), GameConstants.DSBOSS_REFRESH_RANK);
			
			//怪物死了 关闭面板
			BusMsgSender.send2Stage(userRoleId,InnerCmdType.CLOSE_BOSS_HURT_RANK, monsterId);
			
			//持久化db
			activityBossDao.modifyDb(ActivityBossManage.getAbInfos());
			
			if(activityBoss.getHuodongType() == GameConstants.HUODONG_YEWAI_TYPE){
				MonsterConfig monsterConfig = monsterExportService.load(activityBoss.getMonsterId());
				DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(activityBoss.getMapId());
				
				String roleName = getRoleName(userRoleId);
				String diTuConfigName = diTuConfig == null? "":diTuConfig.getName();
				String monsterName = monsterConfig == null ? "" :monsterConfig.getName();
				
				BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
						AppErrorCode.KILL_YWBOSS_NOTICE, 
						new Object[]{roleName,activityBoss.getLine(),diTuConfigName,monsterName}
				});
			}
		}
	}
	
	@Autowired
	private MonsterExportService monsterExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	/**
	 * 活跃度lxn
	 */
	private void setHuoyuedu(List<BossHurtRank> ranks){
		if(ranks==null){return;}
		for (int i = 0; i <ranks.size(); i++) {
			BossHurtRank rank= ranks.get(i);
			huoYueDuExportService.completeActivity(rank.getUserRoleId(),ActivityEnum.A17);
		}
	}
	
	/**
	 * 刷新并且推送给客服端boss伤害排行榜数据
	 * @param monsterId
	 * @param stage
	 */
	public void innerDSBossRank(long monsterId,IStage stage) {
		ActivityBoss boss  = ActivityBossManage.getBoss(monsterId);
		if(boss != null){
			boss.noticeRankData();
			boss.schedulerRefreshRank();
		}
	}
	
	/**
	 * 通知客户端关闭面板
	 * @param monsterId
	 * @param stage
	 */
	public void closeBossRank(long monsterId,IStage stage) {
		IStageElement stageElement = stage.getElement(monsterId, ElementType.MONSTER);
		
		if(stageElement == null){
			return;
		}
		
		Long[] roleIds = stage.getSurroundRoleIds(stageElement.getPosition());
		
		if(roleIds != null && roleIds.length > 0){
			StageMsgSender.send2Many(roleIds, ClientCmdType.YW_BOSS_CLOSE_HURT_RANK, null);
		}
	}
	
	/**
	 * 获得排行数据给客服端展示
	 * @param bossHurtRanks
	 * @return
	 */
	public Object[] getRankDatas(List<BossHurtRank> bossHurtRanks){
		Object[] result = new Object[bossHurtRanks.size()];
		
		for (int i = 0;i<bossHurtRanks.size();i++) {
			BossHurtRank bossHurtRank = bossHurtRanks.get(i);
			result[i] = new Object[]{bossHurtRank.getRoleName(),bossHurtRank.getHurt()};
		}
		
		return result;
	}
	
	/**
	 * 开启野外boss排行榜数据
	 * @param userRoleId
	 * @param monsterId
	 */
	public void openBossRank(long userRoleId,long monsterId){
		ActivityBoss activityBoss = ActivityBossManage.getBoss(monsterId);
		List<BossHurtRank> ranks = activityBoss.getRanks();
		if(ranks.size() > 0){
			BusMsgSender.send2One(userRoleId, ClientCmdType.YW_BOSS_RANK, getRankDatas(ranks));
		}
	}
	
	/**
	 * 回收定时刷出的怪物
	 * @param monsterId
	 */
	public void retrieveBoss(long monsterId) {
		ActivityBoss  monster = ActivityBossManage.getBoss(monsterId);
		
		if(monster == null){
			return ;
		}
		
		IStage stage = monster.getStage(); 
		stage.leave(monster);
		
		//清除定时刷怪boss
		ActivityBossManage.remove(monsterId);
		activityBossDao.modifyDb(ActivityBossManage.getAbInfos());
		
		//取消排行榜刷新定时器
		monster.getScheduler().cancelSchedule(monster.getId().toString(), GameConstants.DSBOSS_REFRESH_RANK);
	}
	
	@Autowired
	private DingShiShuaGuaiConfigExportService dingShiShuaGuaiConfigExportService;
	
	/**
	 * 野外boss信息显示
	 */
	public Object[] ywBossShow() {
		List<Object> result = new ArrayList<>();
		
		Map<String,ActivityBossInfo> activityBossInfo = ActivityBossManage.getAbInfos();
		for (ActivityBossInfo abInfo : activityBossInfo.values()) {
			
			DingShiShuaGuaiConfig config = dingShiShuaGuaiConfigExportService.load(abInfo.getId());
			if(config == null){
				ChuanQiLog.error("DingShiShuaGuaiConfig is null,id:"+abInfo.getId());
				continue;
			}
			if(!config.inBossTime()){
				continue;
			}
			
			Map<Integer, BossKillInfo> bkinfos = abInfo.getBossKillInfos();
			List<Object> rs = new ArrayList<>();
			
			for (Entry<Integer,BossKillInfo> entry : bkinfos.entrySet()) {
				BossKillInfo  bossKillInfo = entry.getValue();
				
				//不在活动时间内了的boss不需要刷出来
				if(bossKillInfo.getEndTime() < GameSystemTime.getSystemMillTime()){
					continue;
				}
				
				//被击杀的怪需要做处理   判断是否已经过期
				if(bossKillInfo.getState() == GameConstants.BOSS_KILL){
					Object[] bossVo = new Object[]{entry.getKey(),bossKillInfo.getState(),bossKillInfo.getRoleName()};
					rs.add(bossVo);
				}else if(bossKillInfo.getState() == GameConstants.BOSS_NO_KILL){
					Object[] bossVo = new Object[]{entry.getKey(),bossKillInfo.getState()};
					rs.add(bossVo);
				}
			}
			
			if(rs.size()>0){
				result.add(new Object[]{abInfo.getId(),abInfo.getMapId(),rs.toArray()});
			}
		}
		
		for (DingShiShuaGuaiConfig config: dingShiShuaGuaiConfigExportService.loadAll()) {
			if(config.isNoBoss()){
				result.add(new Object[]{config.getId(),config.getMapId().get(0),null});
			}
		}
		
		return result.toArray();
	}
	
	/**
	 * 转换成对应的vo
	 * @param lines
	 * @return
	 */
	public Object[] coverToBossVo(List<ActivityBoss> lines) {
		Object[] result = new Object[lines.size()];
		for (int i = 0;i<lines.size();i++) {
			ActivityBoss  aBoss = lines.get(i);
			result[i] = new Object[]{aBoss.getLine(),GameConstants.BOSS_KILL};
		}
		return result;
	}
}
