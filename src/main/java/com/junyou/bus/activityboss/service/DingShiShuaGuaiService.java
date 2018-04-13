package com.junyou.bus.activityboss.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.activityboss.dao.ActivityBossDao;
import com.junyou.bus.activityboss.manage.ActivityBoss;
import com.junyou.bus.activityboss.manage.ActivityBossInfo;
import com.junyou.bus.activityboss.manage.ActivityBossManage;
import com.junyou.bus.activityboss.manage.BossKillInfo;
import com.junyou.bus.activityboss.manage.DingShiMonsterFactory;
import com.junyou.bus.activityboss.util.ActivityBossUtil;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.StageStatistic;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.shuaguai.configure.export.DingShiShuaGuaiConfig;
import com.junyou.gameconfig.shuaguai.configure.export.DingShiShuaGuaiConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.gen.id.IdFactory;
import com.kernel.spring.container.DataContainer;

/**
 * 定时刷怪表
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-25 下午2:35:22
 */
@Service
public class DingShiShuaGuaiService {
	
	@Autowired
	private BusScheduleExportService busScheduleExportService;
	@Autowired
	private DingShiShuaGuaiConfigExportService refreshMonsterConfigExportService;
	@Autowired
	private DataContainer dataContainer; 
	@Autowired
	private ActivityBossDao activityBossDao;
	
	/**
	 * 生产定时怪   (启动或每天凌晨刷新调用)
	 */
	public void productDingShiMonster(){
		Collection<DingShiShuaGuaiConfig>  configs = refreshMonsterConfigExportService.getAllDingShiShuaGuaiConfigs();
		if(configs == null) { 
			return ;
		}
		
		for (DingShiShuaGuaiConfig config : configs) {
			int[] time1 = config.getTime1();
			int[] time2 = config.getTime2();
			
			if(time1 == null || time2 == null){
				continue;
			}
			
			long startTime = DatetimeUtil.getTheTime(config.getTime1()[0], config.getTime1()[1]);
			long endTime = DatetimeUtil.getTheTime(config.getTime2()[0], config.getTime2()[1]);
			long nowTime = GameSystemTime.getSystemMillTime();
			
			
			int initialDelay = 0; //间隔多久启动定时器
			int disappearDuration = 0;//怪物回收时间
			if(startTime > nowTime) {
				
				initialDelay =  (int)(startTime - nowTime); 
				disappearDuration =  (int)(endTime -startTime);
				
			}else if(startTime <= nowTime && nowTime < endTime){
				
				initialDelay = 0; 
				disappearDuration = (int)(endTime - nowTime);
				
			}else{
				continue;
			}
			
			if(disappearDuration>0){
				BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_REFRESH_BOSS,new Object[]{config.getId(),disappearDuration});
				busScheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(),GameConstants.COMPONENET_DINGSHI_MONSTER + config.getId(), runable, initialDelay,TimeUnit.MILLISECONDS);
			}
		}
	}
	
	/**
	 * 生成对应的定时怪
	 * @param bossId
	 */
	public void produceMonster(int bossId,int disappearDuration) {
		DingShiShuaGuaiConfig config = refreshMonsterConfigExportService.load(bossId);
		if(config == null) {
			return ;
		}
		List<Integer> mapIds =	config.getMapId();
		if(mapIds == null) { return ;}
		
		for (Integer mapId : mapIds) {
			List<Integer> lines =  getLines(mapId, config.getLine());
			if(lines == null || lines.size() == 0){
				return;
			}
			
			//过滤掉已经被杀死的boss
			String key = ActivityBossUtil.getKey(mapId, config.getId());
			ActivityBossInfo  abInfos = ActivityBossManage.getActivityBossInfo(key);
			
			for (Integer line : lines) {
				for (int i=0; i < config.getCount(); i++) {
					String stageId = StageUtil.getStageId(mapId, line);
					Object[] result = new Object[]{bossId,line,disappearDuration,mapId};
					
					switch (config.getType1()) {
					case 1:
						
						//过滤掉已经被杀死的boss
						if(abInfos != null){
							BossKillInfo bkInfo = abInfos.getBossKillInfo(line);
							if(bkInfo != null && (bkInfo.getState() == GameConstants.BOSS_KILL && bkInfo.getEndTime() > GameSystemTime.getSystemMillTime())){
								continue;
							}
						}
						StageMsgSender.send2StageInner(GameConstants.DEFAULT_ROLE_ID, stageId, InnerCmdType.INNER_DS_BOSS_MONSTER, result);
						break;

					case 2:
						StageMsgSender.send2StageInner(GameConstants.DEFAULT_ROLE_ID, stageId, InnerCmdType.INNER_DS_GUILD_HURT_MONSTER, result);
						break;
					case 3:
					    StageMsgSender.send2StageInner(GameConstants.DEFAULT_ROLE_ID, stageId, InnerCmdType.I_MGLY_DS_REFRESH_BOSS, result);
					    break;
					default:

						StageMsgSender.send2StageInner(GameConstants.DEFAULT_ROLE_ID, stageId, InnerCmdType.INNER_DS_MONSTER, result);
						break;
					}  
				}
			}
		} 
		
	}
	
	/**
	 * 定时活动进入对应的地图
	 * @param monster
	 * @param config
	 * @param line
	 */
	private void bossMonsterEnterStage(Monster monster,DingShiShuaGuaiConfig config,IStage stage) {
		
		int[] xyPoint = rollBornPosition(config.getRandomCoord());
		monster.setFaceTo(0); 
		monster.setBornPosition(xyPoint[0], xyPoint[1]);
		monster.setActivityBoss(true);
		
		stage.enter(monster,monster.getBornPosition().getX(), monster.getBornPosition().getY());
	}
	
	
	/**
	 * 获得地图的线数
	 * @param mapId
	 * @param lineType
	 * @return
	 */
	private List<Integer> getLines(int mapId,int lineType){
		
		List<Integer> lines = new ArrayList<>();
		if(lineType == 1) {
			lines.add(1);
		}else{
			StageStatistic stageStatistic = dataContainer.getData(GameConstants.DATA_CONTAINER_STAGELINE, mapId+"");
			if(stageStatistic == null){
				return null;
			}
			lines.addAll(stageStatistic.getLines());
		}
		
		return lines;
	}
	
	/**随机刷怪坐标
	 * @param
	 */
	private int[] rollBornPosition(List<int[]> coords) {
		int rollVal = Lottery.roll(coords.size());
		return coords.remove(rollVal);
	}

	
	public void dsBossMonster(int configId,int mapId,int line,IStage stage,int disappearDuration) {
		DingShiShuaGuaiConfig config = refreshMonsterConfigExportService.load(configId);
		
		ActivityBoss monster = DingShiMonsterFactory.createActivityMonster(config,line,mapId);
		
		
		bossMonsterEnterStage(monster,config,stage);
		//刷新怪物排行榜
		monster.schedulerRefreshRank();
		
		//添加自动回收
		if(config.isQingguai()) {
			StageTokenRunable runable = new StageTokenRunable(monster.getId(), stage.getId(), InnerCmdType.RETRIEVE_BOSS, monster.getId());
			monster.getScheduler().schedule(monster.getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, disappearDuration, TimeUnit.MILLISECONDS);
		}
		
		long endTime = DatetimeUtil.getTheTime(config.getTime2()[0], config.getTime2()[1]);
		ActivityBossManage.addActivityBossInfo(configId, line, endTime,mapId);
		
		//加入manage管理
		ActivityBossManage.add(monster);
		activityBossDao.modifyDb(ActivityBossManage.getAbInfos());
	}
	
	public void dsMonster(int bossId, int line, IStage stage,int disappearDuration) {
		DingShiShuaGuaiConfig config = refreshMonsterConfigExportService.load(bossId);
		
		Long id = IdFactory.getInstance().generateNonPersistentId() * 1L;
		
		MonsterConfig monsterConfig = StageConfigureHelper.getMonsterExportService().load(config.getMonsterId1());
		Monster monster =  (Monster) MonsterFactory.createDingShiMonster(id, monsterConfig);
		bossMonsterEnterStage(monster, config,stage);
		
		//添加自动回收
		if(config.isQingguai()){ 
			//注册回收定时器
			StageTokenRunable runable = new StageTokenRunable(monster.getId(), stage.getId(), InnerCmdType.AI_DISAPPEAR, new Object[]{monster.getId(),monster.getElementType().getVal()});
			monster.getScheduler().schedule(monster.getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, disappearDuration, TimeUnit.MILLISECONDS);
		} 
	}
	
	public void dsGuildHurtMonster(int bossId, int line, IStage stage,int disappearDuration) {
		DingShiShuaGuaiConfig config = refreshMonsterConfigExportService.load(bossId);
		
		Long id = IdFactory.getInstance().generateNonPersistentId() * 1L;
		
		MonsterConfig monsterConfig = StageConfigureHelper.getMonsterExportService().load(config.getMonsterId1());
		Monster monster = null;
		if(monsterConfig.getType() == GameConstants.QISHA_MONSTER_TYPE){
			monster = MonsterFactory.createQiSha(null, id, monsterConfig);
		}else{
			return;
		}
		bossMonsterEnterStage(monster, config,stage);
		//添加自动回收
		if(config.isQingguai()){ 
			//注册回收定时器
			StageTokenRunable runable = new StageTokenRunable(monster.getId(), stage.getId(), InnerCmdType.AI_DISAPPEAR, new Object[]{monster.getId(),monster.getElementType().getVal()});
			monster.getScheduler().schedule(monster.getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, disappearDuration, TimeUnit.MILLISECONDS);
		} 
	}
	
	/**
	 * 定时刷新魔攻猎焰boss怪物
	 * 
	 * @param id   定时刷怪表id
	 * @param line     刷怪地图线路
	 * @param stage    刷怪地图场景
	 * @param disappearDuration 怪物回收时间:秒
	 */
	public void dsMglyBossMonster(int id, int line, IStage stage,int disappearDuration) {
        if (null == stage || stage.getStageType() != StageType.MGLY_STAGE) {
            ChuanQiLog.error("dingshi refresh mogonglieyan boss monster, stage={} is null or stage type error", stage);
            return;
        }
        DingShiShuaGuaiConfig config = refreshMonsterConfigExportService.load(id);
        if (null == config) {
            ChuanQiLog.error("dingshi refresh mogonglieyan boss monster, DingShiShuaGuaiBiao.xlsx,id={},config is not exists ", id);
            return;
        }
        MonsterConfig monsterConfig = StageConfigureHelper.getMonsterExportService().load(config.getMonsterId1());
        if (null == monsterConfig) {
            ChuanQiLog.error("dingshi refresh mogonglieyan boss monster, DingShiShuaGuaiBiao.xlsx,id={},boss monster id is null", id);
            return;
        }
        if (monsterConfig.getType() != GameConstants.MGLY_MONSTER_TYPE) {
            ChuanQiLog.error("dingshi refresh mogonglieyan boss monster, boss monster type error, bossId={}", config.getMonsterId1());
            return;
        }
        Monster monster = (Monster) MonsterFactory.create(null, IdFactory.getInstance().generateNonPersistentId() * 1L, monsterConfig);
        bossMonsterEnterStage(monster, config, stage);
        // 添加自动回收
        if (config.isQingguai()) {
            // 注册回收定时器
            StageTokenRunable runable = new StageTokenRunable(monster.getId(), stage.getId(), InnerCmdType.AI_DISAPPEAR, new Object[] { monster.getId(), monster.getElementType().getVal() });
            monster.getScheduler().schedule(monster.getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, disappearDuration, TimeUnit.MILLISECONDS);
        }
	}
	
}
