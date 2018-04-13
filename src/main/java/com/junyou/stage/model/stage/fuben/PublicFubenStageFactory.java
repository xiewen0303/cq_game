package com.junyou.stage.model.stage.fuben;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.analysis.ServerInfoConfigManager;
import com.junyou.analysis.StageInfoConfig;
import com.junyou.bus.kuafu_boss.configure.KuafuBossChengZhangConfigExportService;
import com.junyou.bus.kuafu_dianfeng.configure.DianFengZhiZhanConfig;
import com.junyou.bus.shenmo.configure.export.ShenMoCampConfigExportService;
import com.junyou.bus.shenmo.configure.export.ShenMoScoreConfigExportService;
import com.junyou.bus.shenmo.util.KuafuMatch4v4Manager;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xianqi.configure.YunYaoJingMaiConfig;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.export.ResourceRefreshConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.util.DiTuConfigUtil;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.gameconfig.publicconfig.configure.export.CampPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.HundunPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuBossPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuLuanDouPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuQunXianYanPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuYunGongPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.MoGongLieYanPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.ShenMoPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.YunYaoJingMaiPublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.configure.export.impl.ZiYuanConfig;
import com.junyou.stage.configure.export.impl.ZiYuanConfigExportService;
import com.junyou.stage.hundun.configure.export.HunDunWarGuiZeBiaoConfig;
import com.junyou.stage.hundun.entity.HundunStage;
import com.junyou.stage.model.core.help.GongGongServiceHelper;
import com.junyou.stage.model.core.stage.DefaultStageProduceManager;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IElementProduceTeam;
import com.junyou.stage.model.core.stage.PointType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.core.stage.aoi.AoiFactory;
import com.junyou.stage.model.element.goods.Collect;
import com.junyou.stage.model.element.goods.CollectFacory;
import com.junyou.stage.model.element.monster.KuafuBossMonster;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.model.stage.CollectProduceTeam;
import com.junyou.stage.model.stage.MonsterProduceTeam;
import com.junyou.stage.model.stage.active.RefbActiveFubenStage;
import com.junyou.stage.model.stage.aoi.AoiMsgListenerFactory;
import com.junyou.stage.model.stage.kuafuboss.KuafuBossStage;
import com.junyou.stage.model.stage.kuafudianfeng.KuafuDianFengStage;
import com.junyou.stage.model.stage.kuafuluandou.KuafuLuanDouStage;
import com.junyou.stage.model.stage.kuafuquanxianyan.KuafuQunXianYanStage;
import com.junyou.stage.model.stage.kuafuyungong.KuafuYunGongStage;
import com.junyou.stage.model.stage.mogonglieyan.MoGongLieYanStage;
import com.junyou.stage.model.stage.tanbao.TanBaoStage;
import com.junyou.stage.model.stage.wenquan.WenquanStage;
import com.junyou.stage.model.stage.yunyaojingmai.YunYaoJingMaiStage;
import com.junyou.stage.model.stage.zhengbasai.HcZhengBaSaiStage;
import com.junyou.stage.qisha.entity.QiShaStage;
import com.junyou.stage.shenmo.entity.ShenMoStage;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;
import com.kernel.gen.id.IdFactory;

/**
 * 公共副本创建工厂
 * @author LiuYu
 * @date 2015-6-17 下午4:05:11
 */
@Component
public class PublicFubenStageFactory {

	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private ShenMoScoreConfigExportService shenMoScoreConfigExportService;
	@Autowired
	private ShenMoCampConfigExportService shenMoCampConfigExportService;
	@Autowired
	private KuafuMatch4v4Manager kuafuMatch4v4Manager;
	@Autowired
	private MonsterExportService monsterExportService;
	@Autowired
	private KuafuBossChengZhangConfigExportService kuafuBossChengZhangConfigExportService;
	@Autowired
	private ZiYuanConfigExportService ziYuanConfigExportService;
	
	public PublicFubenStage create(String stageId,Integer lineNo,MapConfig mapConfig){
		//场景创建基本配置
		StageInfoConfig stageInfoConfig = ServerInfoConfigManager.getInstance().getStageInfoConfig();
		//正常AOI
		AOIManager aoiManager = new AOIManager(stageInfoConfig.getAoiWidth(), stageInfoConfig.getAoiHeight(),
												mapConfig.getPathInfoConfig().getXsWidth(), 
												mapConfig.getPathInfoConfig().getXsHeight(), 
												PointType.PIXEL,new AoiFactory(new AoiMsgListenerFactory()));
		
		DiTuConfig config = diTuConfigExportService.loadDiTu(mapConfig.getMapId());
		PublicFubenStage stage = null;
		if(config.getType() == DiTuConfigUtil.XIANGONG_MAP_TYPE){
			stage = new TanBaoStage(stageId, mapConfig.getMapId(), lineNo, aoiManager, mapConfig.getPathInfoConfig());
		}else if(config.getType() == DiTuConfigUtil.CAMP_MAP_TYPE){
			CampPublicConfig campConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_CAMP);
			stage = new CampStage(stageId, mapConfig.getMapId(), aoiManager, mapConfig.getPathInfoConfig(), campConfig);
		}else if(config.getType() == DiTuConfigUtil.WENQUAN_MAP_TYPE){
			stage = new WenquanStage(stageId, mapConfig.getMapId(), lineNo, aoiManager, mapConfig.getPathInfoConfig());
		}else if(config.getType() == DiTuConfigUtil.HC_ZBS_MAP_TYPE){
			stage = new HcZhengBaSaiStage(stageId, mapConfig.getMapId(), lineNo, aoiManager, mapConfig.getPathInfoConfig());
		}else if(config.getType() == DiTuConfigUtil.REFB_ACTIVE_MAP_TYPE){
			stage = new RefbActiveFubenStage(stageId, mapConfig.getMapId(), lineNo, aoiManager, mapConfig.getPathInfoConfig());
		}else{
			return null;
		}
		stage.setCanPk(!config.isSafeMap());
		stage.setAddPk(config.isAddPk());
		
		stage.setStageProduceManager(new DefaultStageProduceManager(stage));
		
		/*******怪物资源*********/
		List<ResourceRefreshConfig> refreshConfigs = mapConfig.getResourceRefreshConfigs();
		if( refreshConfigs != null ){
			for( ResourceRefreshConfig refreshConfig : refreshConfigs ){
				IElementProduceTeam team = null;
				if(stage.isFubenMonster()){
					team = new FubenMonsterProduceTeam(refreshConfig.getTeamId(), ElementType.MONSTER, refreshConfig.getMaxCount(), refreshConfig.getResourceId(), refreshConfig.getPoint(), refreshConfig.getRefreshInterval());
				}else{
					team = new MonsterProduceTeam(refreshConfig.getTeamId(), ElementType.MONSTER, refreshConfig.getMaxCount(), refreshConfig.getResourceId(), refreshConfig.getPoint(), refreshConfig.getRefreshInterval(),false);
				}
				stage.getStageProduceManager().addElementProduceTeam(team);
			}
		}
		stage.getStageProduceManager().produceAll();
		return stage;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T createWithActiveConfig(String stageId,Integer lineNo,MapConfig mapConfig,DingShiActiveConfig activeConfig,Object... otherConfig){
		//场景创建基本配置
		StageInfoConfig stageInfoConfig = ServerInfoConfigManager.getInstance().getStageInfoConfig();
		//正常AOI
		AOIManager aoiManager = new AOIManager(stageInfoConfig.getAoiWidth(), stageInfoConfig.getAoiHeight(),
				mapConfig.getPathInfoConfig().getXsWidth(), 
				mapConfig.getPathInfoConfig().getXsHeight(), 
				PointType.PIXEL,new AoiFactory(new AoiMsgListenerFactory()));
		
		DiTuConfig config = diTuConfigExportService.loadDiTu(mapConfig.getMapId());
		PublicFubenStage stage = null;
		if(config.getType() == DiTuConfigUtil.CHAOS_MAP_TYPE){
			HundunPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.HUNDUN_WAR);
			HunDunWarGuiZeBiaoConfig hdConfig = (HunDunWarGuiZeBiaoConfig)otherConfig[0];
			stage = new HundunStage(stageId, mapConfig.getMapId(), lineNo, aoiManager, mapConfig.getPathInfoConfig(), hdConfig, activeConfig, publicConfig);
		}else{
			return null;
		}
		stage.setCanPk(!config.isSafeMap());
		stage.setAddPk(config.isAddPk());
		
		stage.setStageProduceManager(new DefaultStageProduceManager(stage));
		
		/*******怪物资源*********/
		List<ResourceRefreshConfig> refreshConfigs = mapConfig.getResourceRefreshConfigs();
		if( refreshConfigs != null ){
			for( ResourceRefreshConfig refreshConfig : refreshConfigs ){
				IElementProduceTeam team = null;
				if(stage.isFubenMonster()){
					team = new FubenMonsterProduceTeam(refreshConfig.getTeamId(), ElementType.MONSTER, refreshConfig.getMaxCount(), refreshConfig.getResourceId(), refreshConfig.getPoint(), refreshConfig.getRefreshInterval());
				}else{
					team = new MonsterProduceTeam(refreshConfig.getTeamId(), ElementType.MONSTER, refreshConfig.getMaxCount(), refreshConfig.getResourceId(), refreshConfig.getPoint(), refreshConfig.getRefreshInterval(),false);
				}
				stage.getStageProduceManager().addElementProduceTeam(team);
			}
		}
		stage.getStageProduceManager().produceAll();
		return (T)stage;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T  createNoMonsters(String stageId,Integer lineNo,MapConfig mapConfig){
		//场景创建基本配置
		StageInfoConfig stageInfoConfig = ServerInfoConfigManager.getInstance().getStageInfoConfig();
		//正常AOI
		AOIManager aoiManager = new AOIManager(stageInfoConfig.getAoiWidth(), stageInfoConfig.getAoiHeight(),
												mapConfig.getPathInfoConfig().getXsWidth(), 
												mapConfig.getPathInfoConfig().getXsHeight(), 
												PointType.PIXEL,new AoiFactory(new AoiMsgListenerFactory()));
		
		DiTuConfig config = diTuConfigExportService.loadDiTu(mapConfig.getMapId());
		PublicFubenStage stage = null;
		if(config.getType() == DiTuConfigUtil.QISHA_MAP_TYPE){
			stage = new QiShaStage(stageId, mapConfig.getMapId(), lineNo, aoiManager, mapConfig.getPathInfoConfig());
		}else{
			return null;
		}
		stage.setCanPk(!config.isSafeMap());
		stage.setAddPk(config.isAddPk());
		return (T)stage;
	}
	
	public ShenMoStage createShenMoStage(String stageId,MapConfig mapConfig){
		StageInfoConfig stageInfoConfig = ServerInfoConfigManager.getInstance().getStageInfoConfig();
		//正常AOI
		AOIManager aoiManager = new AOIManager(stageInfoConfig.getAoiWidth(), stageInfoConfig.getAoiHeight(),
														mapConfig.getPathInfoConfig().getXsWidth(), 
														mapConfig.getPathInfoConfig().getXsHeight(), 
														PointType.PIXEL,new AoiFactory(new AoiMsgListenerFactory()));
		ShenMoPublicConfig publicConfig = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_SHENMO);
		ShenMoStage stage = new ShenMoStage(stageId, mapConfig.getMapId(), 1, aoiManager, mapConfig.getPathInfoConfig(), publicConfig,shenMoScoreConfigExportService,shenMoCampConfigExportService,kuafuMatch4v4Manager);
		List<ResourceRefreshConfig> refreshConfigs = mapConfig.getResourceRefreshConfigs();
		stage.setStageProduceManager(new DefaultStageProduceManager(stage));
		if( refreshConfigs != null ){
			for( ResourceRefreshConfig refreshConfig : refreshConfigs ){
				IElementProduceTeam team = new MonsterProduceTeam(refreshConfig.getTeamId(), ElementType.MONSTER, refreshConfig.getMaxCount(), refreshConfig.getResourceId(), refreshConfig.getPoint(), refreshConfig.getRefreshInterval(),false);
				stage.getStageProduceManager().addElementProduceTeam(team);
			}
		}
		stage.getStageProduceManager().produceAll();
		return stage;
	}
	public KuafuBossStage createKuafuBossStage(String stageId,MapConfig mapConfig){
		StageInfoConfig stageInfoConfig = ServerInfoConfigManager.getInstance().getStageInfoConfig();
		//正常AOI
		AOIManager aoiManager = new AOIManager(stageInfoConfig.getAoiWidth(), stageInfoConfig.getAoiHeight(),
														mapConfig.getPathInfoConfig().getXsWidth(), 
														mapConfig.getPathInfoConfig().getXsHeight(), 
														PointType.PIXEL,new AoiFactory(new AoiMsgListenerFactory()));
		
		KuafuBossPublicConfig publicConfig = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.KUAFU_BOSS);
		KuafuBossStage stage = new KuafuBossStage(stageId, mapConfig.getMapId(), 1, aoiManager, mapConfig.getPathInfoConfig(), publicConfig);
		ChuanQiLog.info("create kuafuboss stage finish");
//		List<ResourceRefreshConfig> refreshConfigs = mapConfig.getResourceRefreshConfigs();
//		stage.setStageProduceManager(new DefaultStageProduceManager(stage));
//		if( refreshConfigs != null ){
//			for( ResourceRefreshConfig refreshConfig : refreshConfigs ){
//				IElementProduceTeam team = new MonsterProduceTeam(refreshConfig.getTeamId(), ElementType.MONSTER, refreshConfig.getMaxCount(), refreshConfig.getResourceId(), refreshConfig.getPoint(), refreshConfig.getRefreshInterval(),false);
//				stage.getStageProduceManager().addElementProduceTeam(team);
//			}
//		}
//		stage.getStageProduceManager().produceAll();
		
		Redis redis = GameServerContext.getRedis();
		if(redis!=null){
			String bossDeadFlagKey = RedisKey.getKuafuBossDeadFlag(ChuanQiConfigUtil.getServerId());
			if(redis.get(bossDeadFlagKey)==null){
				Long id = IdFactory.getInstance().generateNonPersistentId() * 1L;
				String bossChengZhangId= redis.get(RedisKey.KUAFU_BOSS_NEXT_ID);
				String bossId = null;
				if(bossChengZhangId==null){
					bossId = publicConfig.getBossid();
				}else{
					bossId = kuafuBossChengZhangConfigExportService.getBossIdById(Integer.parseInt(bossChengZhangId));
					if(bossId==null){
						bossId = publicConfig.getBossid();
					}else{
						stage.setBossId(Integer.parseInt(bossChengZhangId));
					}
				}
				KuafuBossMonster monster = MonsterFactory.createKuafuBoss(null, id, monsterExportService.load(bossId));
				int[] zuobiao = publicConfig.getZuobiao();
				stage.enter(monster, zuobiao[0], zuobiao[1]);
				//刷新怪物排行榜
				monster.schedulerRefreshRank();
				ChuanQiLog.info("create kuafuboss bossId={}",bossId);
			}else{
				stage.setBossAlive(false);
			}
		}
		return stage;
	}
	public KuafuQunXianYanStage createKuafuQunXianYanStage(String stageId,MapConfig mapConfig){
		StageInfoConfig stageInfoConfig = ServerInfoConfigManager.getInstance().getStageInfoConfig();
		//正常AOI
		AOIManager aoiManager = new AOIManager(stageInfoConfig.getAoiWidth(), stageInfoConfig.getAoiHeight(),
				mapConfig.getPathInfoConfig().getXsWidth(), 
				mapConfig.getPathInfoConfig().getXsHeight(), 
				PointType.PIXEL,new AoiFactory(new AoiMsgListenerFactory()));
		
		KuafuQunXianYanPublicConfig publicConfig = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.KUAFU_QUNXIANYAN);
		KuafuQunXianYanStage stage = new KuafuQunXianYanStage(stageId, mapConfig.getMapId(), 1, aoiManager, mapConfig.getPathInfoConfig(), publicConfig);
		ChuanQiLog.info("create kuafuqunxianyan stage finish");


		stage.setStageProduceManager(new DefaultStageProduceManager(stage));
		List<ResourceRefreshConfig> refreshConfigs = mapConfig.getResourceRefreshConfigs();
		if (null != refreshConfigs) {
			for (ResourceRefreshConfig refreshConfig : refreshConfigs) {
				IElementProduceTeam team = new MonsterProduceTeam(refreshConfig.getTeamId(), ElementType.MONSTER, refreshConfig.getMaxCount(), refreshConfig.getResourceId(), refreshConfig.getPoint(), refreshConfig.getRefreshInterval(),false);
				stage.getStageProduceManager().addElementProduceTeam(team);
			
			}
			stage.getStageProduceManager().produceAll();
		}
		List<ZiYuanConfig> zyList = ziYuanConfigExportService.getZiYuanConfigByType(GameConstants.QUNXIANYAN_ZIYAN_TYPE);
		for (int i = 0; i < zyList.size(); i++) {
			ZiYuanConfig ziYuanConfig = zyList.get(i);
			if(ziYuanConfig == null){
				break;
			}
			if(ziYuanConfig.getDelaNumber() == 1){
				List<Integer[]> zuobiaoList = ziYuanConfig.getZuobiao();
				Integer[] zuoBiao = zuobiaoList.get((int)(Math.random()*zuobiaoList.size()));
				Collect taozi = CollectFacory.create(IdFactory.getInstance().generateNonPersistentId(),ziYuanConfig.getId().toString(), ziYuanConfig);
				stage.enter(taozi, zuoBiao[0], zuoBiao[1]);
				//公告
				KuafuQunXianYanPublicConfig pConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.KUAFU_QUNXIANYAN);;
				KuafuQunXianYanStage kStage = (KuafuQunXianYanStage) stage;
				BusMsgSender.send2Many(kStage.getAllRoleIds(),ClientCmdType.NOTIFY_CLIENT_ALERT6, new Object[]{pConfig.getSxCode()});
			}else{
				for (Integer[] zuobiao : ziYuanConfig.getZuobiao()) {
					Collect taozi = CollectFacory.create(IdFactory.getInstance().generateNonPersistentId(),ziYuanConfig.getId().toString(), ziYuanConfig);
					stage.enter(taozi, zuobiao[0], zuobiao[1]);
				}
			}
		}
		
		
		return stage;
	}
	
	public KuafuLuanDouStage createKuafuLuanDouStage(String stageId,MapConfig mapConfig){
		StageInfoConfig stageInfoConfig = ServerInfoConfigManager.getInstance().getStageInfoConfig();
		//正常AOI
		AOIManager aoiManager = new AOIManager(stageInfoConfig.getAoiWidth(), stageInfoConfig.getAoiHeight(),
				mapConfig.getPathInfoConfig().getXsWidth(), 
				mapConfig.getPathInfoConfig().getXsHeight(), 
				PointType.PIXEL,new AoiFactory(new AoiMsgListenerFactory()));
		
		KuafuLuanDouPublicConfig publicConfig = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.KUAFU_DALUANDOU);
		KuafuLuanDouStage stage = new KuafuLuanDouStage(stageId, mapConfig.getMapId(), 1, aoiManager, mapConfig.getPathInfoConfig(), publicConfig);
		
		/*Redis redis = GameServerContext.getRedis();
		if(redis!=null){
			String bossDeadFlagKey = RedisKey.getKuafuBossDeadFlag(ChuanQiConfigUtil.getServerId());
			if(redis.get(bossDeadFlagKey)==null){
				Long id = IdFactory.getInstance().generateNonPersistentId() * 1L;
				String bossId= publicConfig.getBossid();
				KuafuBossMonster monster = MonsterFactory.createKuafuBoss(null, id, monsterExportService.load(bossId));
				int[] zuobiao = publicConfig.getZuobiao();
				stage.enter(monster, zuobiao[0], zuobiao[1]);
				//刷新怪物排行榜
				monster.schedulerRefreshRank();
			}else{
				stage.setBossAlive(false);
			}
		}*/
		return stage;
	}
	
	public KuafuDianFengStage createKuafuDianFengStage(String stageId,int loop, int room, MapConfig mapConfig, DianFengZhiZhanConfig dfzzConfig){
        PathInfoConfig pathInfoConfig = mapConfig.getPathInfoConfig();
        //正常AOI
        AOIManager aoiManager = new AOIManager(
                pathInfoConfig.getXsWidth(), 
                pathInfoConfig.getXsHeight(),
                pathInfoConfig.getXsWidth(), 
                pathInfoConfig.getXsHeight(),  
                PointType.PIXEL,new AoiFactory(new AoiMsgListenerFactory()));
        return new KuafuDianFengStage(stageId, mapConfig.getMapId(), 1, aoiManager, pathInfoConfig, room, dfzzConfig);
    }

    /**
     * 创建跨服云宫之巅场景
     * @param stageId
     * @param mapConfig
     * @param config
     * @return
     */
    public KuafuYunGongStage createKuafuYunGongStage(String stageId, MapConfig mapConfig, KuafuYunGongPublicConfig config) {
        StageInfoConfig stageInfoConfig = ServerInfoConfigManager.getInstance().getStageInfoConfig();
        //正常AOI
        AOIManager aoiManager = new AOIManager(stageInfoConfig.getAoiWidth(), stageInfoConfig.getAoiHeight(),
                mapConfig.getPathInfoConfig().getXsWidth(), 
                mapConfig.getPathInfoConfig().getXsHeight(), 
                PointType.PIXEL,new AoiFactory(new AoiMsgListenerFactory()));
        return new KuafuYunGongStage(stageId, mapConfig.getMapId(), 1, aoiManager, mapConfig.getPathInfoConfig(), config);
    }
    
    /**
     * 创建魔宫猎焰场景
     * 
     * @param stageId
     * @param lineNo
     * @param mapConfig
     * @param publicConfig
     * @return
     */
    public MoGongLieYanStage createMoGongLieYanStage(String stageId, Integer lineNo, MapConfig mapConfig, MoGongLieYanPublicConfig publicConfig) {
        // 场景创建基本配置
        StageInfoConfig stageInfoConfig = ServerInfoConfigManager.getInstance().getStageInfoConfig();
        // 正常AOI
        AOIManager aoiManager = new AOIManager(stageInfoConfig.getAoiWidth(), stageInfoConfig.getAoiHeight(), mapConfig.getPathInfoConfig().getXsWidth(), mapConfig.getPathInfoConfig().getXsHeight(), PointType.PIXEL, new AoiFactory(new AoiMsgListenerFactory()));
        MoGongLieYanStage stage = new MoGongLieYanStage(stageId, mapConfig.getMapId(), lineNo, aoiManager, mapConfig.getPathInfoConfig(), publicConfig);
        stage.setStageProduceManager(new DefaultStageProduceManager(stage));
        /******* 怪物资源 *********/
        List<ResourceRefreshConfig> refreshConfigs = mapConfig.getResourceRefreshConfigs();
        if (refreshConfigs != null) {
            for (ResourceRefreshConfig refreshConfig : refreshConfigs) {
                IElementProduceTeam team = new MonsterProduceTeam(refreshConfig.getTeamId(), ElementType.MONSTER, refreshConfig.getMaxCount(), refreshConfig.getResourceId(), refreshConfig.getPoint(), refreshConfig.getRefreshInterval(), false);
                stage.getStageProduceManager().addElementProduceTeam(team);
            }
        }
        stage.getStageProduceManager().produceAll();
        return stage;
    }

    /**
     * 创建云瑶晶脉场景
     * @param stageId
     * @param lineNo
     * @param mapConfig
     * @param publicConfig
     * @param yunYaoJingMaiConfigs 
     * @return
     */
    public YunYaoJingMaiStage createYunYaoJingMapStage(String stageId, int lineNo, MapConfig mapConfig, YunYaoJingMaiPublicConfig publicConfig, List<YunYaoJingMaiConfig> yunYaoJingMaiConfigs) {
        // 游戏场景基础配置
        StageInfoConfig stageInfoConfig = ServerInfoConfigManager.getInstance().getStageInfoConfig();
        // 正常AOI
        AOIManager aoiManager = new AOIManager(
                stageInfoConfig.getAoiWidth(), 
                stageInfoConfig.getAoiHeight(), 
                mapConfig.getPathInfoConfig().getXsWidth(), 
                mapConfig.getPathInfoConfig().getXsHeight(), 
                PointType.PIXEL, new AoiFactory(new AoiMsgListenerFactory()));
        YunYaoJingMaiStage stage = new YunYaoJingMaiStage(stageId, mapConfig.getMapId(), lineNo, aoiManager, mapConfig.getPathInfoConfig(), publicConfig);
        stage.setStageProduceManager(new DefaultStageProduceManager(stage));
        for(YunYaoJingMaiConfig config : yunYaoJingMaiConfigs){
            List<Integer[]> zuobiaoList = config.getZuobiao();
            if(ObjectUtil.isEmpty(zuobiaoList)){
                ChuanQiLog.error("create yunyaojingmai stage, config zuobiao is null, config id={}", config.getId());
                continue;
            }
            stage.getStageProduceManager().addElementProduceTeam(new CollectProduceTeam(String.valueOf(IdFactory.getInstance().generateNonPersistentId()), ElementType.COLLECT, zuobiaoList.size(), String.valueOf(config.getId()), zuobiaoList, config.getCd()));
        }
        stage.getStageProduceManager().produceAll();
        return stage;
    }

    
}