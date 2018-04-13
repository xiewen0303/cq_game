package com.junyou.stage.model.stage.fuben;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.bagua.configure.export.DuoRenTongYongBiaoConfig;
import com.junyou.bus.bagua.configure.export.DuoRenTongYongBiaoExportService;
import com.junyou.bus.bagua.constants.BaguaConstant;
import com.junyou.bus.fuben.entity.AbsFubenConfig;
import com.junyou.bus.fuben.entity.PataConfig;
import com.junyou.bus.fuben.entity.ShouhuFubenConfig;
import com.junyou.bus.fuben.entity.WuxingFubenConfig;
import com.junyou.bus.fuben.entity.WuxingShilianFubenConfig;
import com.junyou.bus.fuben.entity.WuxingSkillFubenConfig;
import com.junyou.bus.fuben.entity.XinmoDouchangFubenConfig;
import com.junyou.bus.fuben.entity.XinmoFubenConfig;
import com.junyou.bus.fuben.entity.XinmoShenyuanFubenConfig;
import com.junyou.bus.lianyuboss.configure.LianyuBossConfig;
import com.junyou.bus.lianyuboss.stage.LianyuBossStage;
import com.junyou.bus.maigu.configure.constants.MaiguConstant;
import com.junyou.bus.maigu.configure.export.ZuDuiShouHhuGuaiWuConfigExportService;
import com.junyou.bus.personal_boss.configure.RolePersonalBossConfig;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.ResourceRefreshConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.gameconfig.publicconfig.configure.export.BaguaPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GuildPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.MaiguPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.PataPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.ShouhuPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.WuXingShilianFubenPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.WuXingSkillFubenPublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.help.GongGongServiceHelper;
import com.junyou.stage.model.core.stage.DeadDisplay;
import com.junyou.stage.model.core.stage.DefaultStageProduceManager;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IElementProduceTeam;
import com.junyou.stage.model.core.stage.PointType;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.core.stage.aoi.AoiFactory;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.model.stage.aoi.AoiMsgListenerFactory;
import com.junyou.stage.model.stage.aoi.AoiStage;
import com.junyou.stage.tafang.stage.TaFangStage;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.parameter.RequestParameterUtil;
import com.kernel.gen.id.IdFactory;

/**
 * 副本
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-26 上午10:46:19
 */
@Component
public class FubenStageFactory {

	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private MonsterExportService monsterExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;

	@Autowired
	private DuoRenTongYongBiaoExportService duoRenTongYongBiaoExportService;
	@Autowired
	private ZuDuiShouHhuGuaiWuConfigExportService zuDuiShouHhuGuaiWuConfigExportService;
	
	public AoiStage create(Long roleId, String stageId, AbsFubenConfig fubenConfig, MapConfig mapConfig, Object data,DeadDisplay deadDisplay) {
		AoiStage aoiStage = create(roleId, stageId, fubenConfig, mapConfig, data);
		if (MapType.FUBEN_MAP.equals(fubenConfig.getMapType())) {
			DayFubenStage d = (DayFubenStage)aoiStage;
			d.setDeadHiddenState(deadDisplay);
		}
		return aoiStage;
	}
	
    public AoiStage create(Long roleId, String stageId, AbsFubenConfig fubenConfig, MapConfig mapConfig, Object data) {
        // 整个副本只有一个AOI
        AOIManager aoiManager = new AOIManager(mapConfig.getPathInfoConfig().getXsWidth(), mapConfig.getPathInfoConfig().getXsHeight(), mapConfig.getPathInfoConfig().getXsWidth(), mapConfig.getPathInfoConfig().getXsHeight(), PointType.PIXEL, new AoiFactory(new AoiMsgListenerFactory()));
        
        AoiStage aoiStage = null;
        if (MapType.FUBEN_MAP.equals(fubenConfig.getMapType())) {
        	aoiStage = new DayFubenStage(stageId, mapConfig.getMapId(), aoiManager, fubenConfig, mapConfig.getPathInfoConfig(), StageType.FUBEN_RC);
        } else if (MapType.MORE_FUBEN_MAP.equals(fubenConfig.getMapType())) {
            aoiStage = new MoreFbStage(stageId, mapConfig.getMapId(), aoiManager, mapConfig.getPathInfoConfig(), StageType.MORE_FUBEN, fubenConfig.getWantedMap(), GameConstants.MORE_FUBEN_DJS, fubenConfig.getExitCmd(), fubenConfig.getId());
        } else if (MapType.BAGUA_FUBEN_MAP.equals(fubenConfig.getMapType())) {
            Integer ceng = RequestParameterUtil.object2Integer(data);
            aoiStage = new BaguaFbStage(stageId, mapConfig.getMapId(), aoiManager, mapConfig.getPathInfoConfig(), StageType.BAGUA_FUBEN, fubenConfig.getWantedMap(), GameConstants.BAGUA_FUBEN_DJS, fubenConfig.getExitCmd(), fubenConfig.getId(), ceng == null ? 0 : ceng);
        } else if (MapType.MAIGU_FUBEN_MAP.equals(fubenConfig.getMapType())) {
            aoiStage = new MaiguFbStage(stageId, mapConfig.getMapId(), aoiManager, mapConfig.getPathInfoConfig(), StageType.MAIGU_FUBEN, GameConstants.MAIGU_FUBEN_DJS, fubenConfig.getExitCmd(), fubenConfig.getId(), gongGongShuJuBiaoConfigExportService, zuDuiShouHhuGuaiWuConfigExportService, monsterExportService);
        } else if (MapType.SAVE_FUBEN_MAP.equals(fubenConfig.getMapType())) {
            aoiStage = new ShouhuFubenStage(stageId, mapConfig.getMapId(), aoiManager, fubenConfig, mapConfig.getPathInfoConfig(), StageType.SAVE_FB);
        } else if (MapType.PATA_FUBEN_MAP.equals(fubenConfig.getMapType())) {
            aoiStage = new DayFubenStage(stageId, mapConfig.getMapId(), aoiManager, fubenConfig, mapConfig.getPathInfoConfig(), StageType.FUBEN_PATA);
        } else if (MapType.TAFANG_FUBEN_MAP.equals(fubenConfig.getMapType())) {
            aoiStage = new TaFangStage(stageId, mapConfig.getMapId(), aoiManager, mapConfig.getPathInfoConfig());
        } else if (MapType.JIANZHONG_FUBEN_MAP.equals(fubenConfig.getMapType())) {
            aoiStage = new JianzhongFubenStage(stageId, mapConfig.getMapId(), aoiManager, fubenConfig, mapConfig.getPathInfoConfig(), StageType.FUBEN_JIANZHONG);
        } else if (MapType.GUILD_LIANYU_BOSS.equals(fubenConfig.getMapType())) {
            aoiStage = new LianyuBossStage(stageId, mapConfig.getMapId(), aoiManager, fubenConfig, mapConfig.getPathInfoConfig(), StageType.GUILD_LIANYU_BOSS);
        } else if (MapType.WUXING_FUBEN_MAP.equals(fubenConfig.getMapType())) {
            aoiStage = new WuxingFubenStage(stageId, mapConfig.getMapId(), aoiManager, fubenConfig, mapConfig.getPathInfoConfig(), StageType.FUBEN_WUXING);
        } else if (MapType.WUXING_SKILL_FUBEN_MAP.equals(fubenConfig.getMapType())) {
            WuXingSkillFubenPublicConfig wxSkillFubenPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WUXING_SKILL_FUBEN);
            aoiStage = new WuxingSkillFubenStage(stageId, mapConfig.getMapId(), aoiManager, mapConfig.getPathInfoConfig(), StageType.FUBEN_WUXING_SKILL, wxSkillFubenPublicConfig.getTime1(), fubenConfig.getExitCmd(), fubenConfig.getId());
        } else if (MapType.WUXING_SHILIAN_FUBEN_MAP.equals(fubenConfig.getMapType())) {
            WuXingShilianFubenPublicConfig wxShilianFubenPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WUXING_SHILIAN);
            aoiStage = new WuxingShilianFubenStage(stageId, mapConfig.getMapId(), aoiManager, mapConfig.getPathInfoConfig(), StageType.FUBEN_WUXING_SHILIAN, wxShilianFubenPublicConfig.getTimeSeconds(), fubenConfig.getExitCmd());
        } else if (MapType.XINMO_FUBEN_MAP.equals(fubenConfig.getMapType())) {
            aoiStage = new XinmoFubenStage(stageId, mapConfig.getMapId(), aoiManager, mapConfig.getPathInfoConfig(), StageType.FUBEN_XINMO, fubenConfig);
        } else if (MapType.XINMO_SHENYUAN_FUBEN_MAP.equals(fubenConfig.getMapType())) {
            aoiStage = new XinmoShenyuanFubenStage(stageId, mapConfig.getMapId(), aoiManager, mapConfig.getPathInfoConfig(), StageType.FUBEN_XINMO_SHENYUAN, fubenConfig, RequestParameterUtil.object2Integer(data));
        } else if (MapType.XINMO_DOUCHANG_FUBEN_MAP.equals(fubenConfig.getMapType())) {
            aoiStage = new XinmoDouchangFubenStage(stageId, mapConfig.getMapId(), aoiManager, mapConfig.getPathInfoConfig(), StageType.FUBEN_XINMO_DOUCHANG, fubenConfig, RequestParameterUtil.object2Integer(data));
	    } else if (MapType.PERSONAL_BOSS.equals(fubenConfig.getMapType())) {
	    	aoiStage = new PersonalBossStage(stageId, mapConfig.getMapId(), aoiManager, mapConfig.getPathInfoConfig(), StageType.PERSONAL_BOSS_FUBEN, fubenConfig.getTime(), fubenConfig.getExitCmd(), fubenConfig.getId());
	    }
        if (aoiStage == null) {
            ChuanQiLog.error("fuben type is error. fubenId is " + fubenConfig.getId() + ", type is " + fubenConfig.getMapType());
            return null;
        }
        DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(mapConfig.getMapId());
        aoiStage.setCanPk(!diTuConfig.isSafeMap());
        aoiStage.setAddPk(diTuConfig.isAddPk());
        // 设置场景中是否可以使用道具复活
        if (aoiStage instanceof SingleFbStage) {
            SingleFbStage fubenStage = (SingleFbStage) aoiStage;
            fubenStage.setFuhuo(diTuConfig.isCanPropFuhuo());
        } 
        else if(aoiStage instanceof TaFangStage){
            TaFangStage tafangStage = (TaFangStage) aoiStage;
            tafangStage.setFuhuo(diTuConfig.isCanPropFuhuo());
        }
        aoiStage.setStageProduceManager(new DefaultStageProduceManager(aoiStage));
        if (fubenConfig.isAutoProduct()) {
            List<ResourceRefreshConfig> refreshConfigs = mapConfig.getResourceRefreshConfigs();
            if (null != refreshConfigs) {
                for (ResourceRefreshConfig config : refreshConfigs) {
                    FubenMonsterProduceTeam team = new FubenMonsterProduceTeam(config.getTeamId(), ElementType.MONSTER, config.getMaxCount(), config.getResourceId(), config.getPoint(), 0);
                    aoiStage.getStageProduceManager().addElementProduceTeam(team);
                }
            }
        } else {
            if (aoiStage instanceof ShouhuFubenStage) {
                ShouhuFubenConfig config = (ShouhuFubenConfig) fubenConfig;
                ShouhuPublicConfig publicConfig = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_SHOUHU);
                // 刷图腾
                List<Integer[]> list = new ArrayList<>();
                list.add(publicConfig.getShouhuPoint());
                TutengMonsterProduceTeam team = new TutengMonsterProduceTeam(IdFactory.getInstance().generateNonPersistentId() + "", ElementType.TUTENG, 1, publicConfig.getShouhuId(), list, 0, 0, (short) 0);
                aoiStage.getStageProduceManager().addElementProduceTeam(team);

                // 刷小怪
                int x = publicConfig.getMonsterPoint()[0];
                int y = publicConfig.getMonsterPoint()[1];
                List<Integer[]> list2 = new ArrayList<>();
                for (int i = 0; i < config.getMonsterCount(); i++) {
                    list2.add(new Integer[] { x - 2 + Lottery.roll(5), y - 2 + Lottery.roll(5) });
                }
                FubenMonsterProduceTeam team2 = new FubenMonsterProduceTeam(IdFactory.getInstance().generateNonPersistentId() + "", ElementType.MONSTER, config.getMonsterCount(), config.getMonsterId(), list2, 5000);
                aoiStage.getStageProduceManager().addElementProduceTeam(team2);
            } else if (MapType.PATA_FUBEN_MAP.equals(fubenConfig.getMapType())) {
                PataConfig config = (PataConfig) fubenConfig;
                PataPublicConfig publicConfig = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_PATA);
                for (String monsterId : config.getWantedMap().keySet()) {
                    FubenMonsterProduceTeam team = new FubenMonsterProduceTeam(IdFactory.getInstance().generateNonPersistentId() + "", ElementType.MONSTER, 1, monsterId, publicConfig.getPosition(), 0);
                    aoiStage.getStageProduceManager().addElementProduceTeam(team);
                }
            } else if (MapType.GUILD_LIANYU_BOSS.equals(fubenConfig.getMapType())) {
                LianyuBossConfig config = (LianyuBossConfig) fubenConfig;
                GuildPublicConfig publicConfig = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_GUILD);
                for (String monsterId : config.getWantedMap().keySet()) {
                    FubenMonsterProduceTeam team = new FubenMonsterProduceTeam(IdFactory.getInstance().generateNonPersistentId() + "", ElementType.MONSTER, 1, monsterId, publicConfig.getLianyuBossBirth(), 0);
                    aoiStage.getStageProduceManager().addElementProduceTeam(team);
                }
            } else if (MapType.JIANZHONG_FUBEN_MAP.equals(fubenConfig.getMapType())) {
                /******* 怪物资源 *********/
                List<ResourceRefreshConfig> refreshConfigs = mapConfig.getResourceRefreshConfigs();
                if (refreshConfigs != null) {
                    for (ResourceRefreshConfig refreshConfig : refreshConfigs) {
                        IElementProduceTeam team = new JianzhongMonsterProduceTeam(refreshConfig.getTeamId(), ElementType.MONSTER, refreshConfig.getMaxCount(), refreshConfig.getResourceId(), refreshConfig.getPoint(), refreshConfig.getRefreshInterval(), false);
                        aoiStage.getStageProduceManager().addElementProduceTeam(team);
                    }
                }
            } else if (MapType.WUXING_FUBEN_MAP.equals(fubenConfig.getMapType())) {
                WuxingFubenConfig wxconfig = (WuxingFubenConfig) fubenConfig;
                ReadOnlyMap<String, List<Integer[]>> monsterMap = wxconfig.getMonsterMap();
                if (!ObjectUtil.isEmpty(monsterMap)) {
                    for (String monsterId : monsterMap.keySet()) {
                        List<Integer[]> monsterList = monsterMap.get(monsterId);
                        IElementProduceTeam team = new WuxingFubenMonsterProduceTeam(String.valueOf(IdFactory.getInstance().generateNonPersistentId()), ElementType.MONSTER, monsterList.size(), monsterId, monsterList, wxconfig.getWxAttrsMap(), 0, 0);
                        aoiStage.getStageProduceManager().addElementProduceTeam(team);
                    }
                }
            } else if (MapType.WUXING_SKILL_FUBEN_MAP.equals(fubenConfig.getMapType())) {
                WuxingSkillFubenConfig wxSkillConfig = (WuxingSkillFubenConfig) fubenConfig;
                WuXingSkillFubenPublicConfig wxSkillFubenPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WUXING_SKILL_FUBEN);
                List<Integer[]> monsterList = new ArrayList<>();
                monsterList.add(wxSkillFubenPublicConfig.getBossXyPoint());
                IElementProduceTeam team = new WuxingFubenMonsterProduceTeam(String.valueOf(IdFactory.getInstance().generateNonPersistentId()), ElementType.MONSTER, monsterList.size(), wxSkillConfig.getMonsterId(), monsterList, wxSkillConfig.getWxAttrsMap(), 0, 0);
                aoiStage.getStageProduceManager().addElementProduceTeam(team);
            } else if (MapType.WUXING_SHILIAN_FUBEN_MAP.equals(fubenConfig.getMapType())) {
                WuxingShilianFubenConfig wxShilianConfig = (WuxingShilianFubenConfig) fubenConfig;
                if (!ObjectUtil.isEmpty(wxShilianConfig.getMonsterList())) {
                    for (WuxingShilianFubenConfig.ShilianMonster monster : wxShilianConfig.getMonsterList()) {
                        List<Integer[]> monsterList = new ArrayList<>();
                        Integer[] xy = monster.getXyPoint();
                        for (int i = 0; i < monster.getMonsterNum(); i++) {
                            monsterList.add(new Integer[] { xy[0] - 2 + Lottery.roll(5), xy[1] - 2 + Lottery.roll(5) });
                        }
                        IElementProduceTeam team = new WuxingFubenMonsterProduceTeam(String.valueOf(IdFactory.getInstance().generateNonPersistentId()), ElementType.MONSTER, monsterList.size(), monster.getMonsterId(), monsterList, monster.getWxAttrsMap(), 0, monster.getRefreTime() * 1000);
                        aoiStage.getStageProduceManager().addElementProduceTeam(team);
                    }
                }
            } else if (MapType.XINMO_FUBEN_MAP.equals(fubenConfig.getMapType())) {
                XinmoFubenConfig xmFubenConfig = (XinmoFubenConfig) fubenConfig;
                Map<String, Integer[]>  monsterMap = xmFubenConfig.getMonsterMap();
                for(Entry<String, Integer[]> monster : monsterMap.entrySet()){
                    List<Integer[]> monsterList = new ArrayList<>();
                    monsterList.add(monster.getValue());
                    FubenMonsterProduceTeam team = new FubenMonsterProduceTeam(String.valueOf(IdFactory.getInstance().generateNonPersistentId()), ElementType.MONSTER, monsterList.size(), monster.getKey(), monsterList, 0);
                    aoiStage.getStageProduceManager().addElementProduceTeam(team);
                }
            } else if (MapType.XINMO_SHENYUAN_FUBEN_MAP.equals(fubenConfig.getMapType())) {
                XinmoShenyuanFubenConfig xmShenyuanFubenConfig = (XinmoShenyuanFubenConfig) fubenConfig;
                List<Integer[]> xyPointList = new ArrayList<>();
                xyPointList.add(xmShenyuanFubenConfig.getXyPoint());
                MultiBaseAttributeMonsterProduceTeam team = new MultiBaseAttributeMonsterProduceTeam(String.valueOf(IdFactory.getInstance().generateNonPersistentId()), ElementType.MONSTER, xyPointList.size(), xmShenyuanFubenConfig.getMonsterId(RequestParameterUtil.object2Integer(data)), xyPointList, 0, EffectType.getBaseAttrTypes(), xmShenyuanFubenConfig.getMultiplier());
                aoiStage.getStageProduceManager().addElementProduceTeam(team);
            } else if (MapType.XINMO_DOUCHANG_FUBEN_MAP.equals(fubenConfig.getMapType())) {
                XinmoDouchangFubenConfig xmDouchangFubenConfig = (XinmoDouchangFubenConfig) fubenConfig;
                List<Object[]> monsterList = xmDouchangFubenConfig.getMonsterList();
                for(Object[] monsterData : monsterList){
                    if(monsterData != null && monsterData.length > 2){
                        String monsterId = RequestParameterUtil.object2String(monsterData[0]);
                        List<Integer[]> xyPointList = new ArrayList<>();
                        xyPointList.add((Integer[])monsterData[1]);
                        FubenMonsterProduceTeam team = new FubenMonsterProduceTeam(String.valueOf(IdFactory.getInstance().generateNonPersistentId()), ElementType.MONSTER, xyPointList.size(), monsterId, xyPointList, 0);
                        aoiStage.getStageProduceManager().addElementProduceTeam(team);
                    }
                }
            }else if (MapType.PERSONAL_BOSS.equals(fubenConfig.getMapType())) {
            	RolePersonalBossConfig config = (RolePersonalBossConfig) fubenConfig;
            	int x = config.getX();
            	int y = config.getY();
            	 List<Integer[]> list = new ArrayList<>();
            	 list.add(new Integer[]{x,y});
            	 FubenMonsterProduceTeam team = new FubenMonsterProduceTeam(IdFactory.getInstance().generateNonPersistentId() + "", ElementType.MONSTER, 1, config.getMonsterid(), list, 0);
            	 aoiStage.getStageProduceManager().addElementProduceTeam(team);
            }  

        }
        aoiStage.getStageProduceManager().produceAll();

        /**
         * 初始化场景需要击杀名单
         */
        if (aoiStage instanceof SingleFbStage) {
            SingleFbStage fubenStage = (SingleFbStage) aoiStage;
            // 初始化击杀名单
            fubenStage.initWantedMap();
        }
        if (fubenConfig.getMapType() == MapType.MORE_FUBEN_MAP) {
            MoreFbStage fubenStage = (MoreFbStage) aoiStage;
            // 初始化击杀名单
            fubenStage.initWantedMap();
        }
        if (fubenConfig.getMapType() == MapType.BAGUA_FUBEN_MAP && data != null && data instanceof Integer && ((Integer)data).intValue() == 8) {
            BaguaFbStage fubenStage = (BaguaFbStage) aoiStage;
            DuoRenTongYongBiaoConfig duoRenTongYongBiaoConfig = duoRenTongYongBiaoExportService.loadByOrder(BaguaConstant.BUAGUA_FUBEN, fubenConfig.getId());
            MonsterConfig monsterConfig = monsterExportService.load(duoRenTongYongBiaoConfig.getMonster());
            long id = IdFactory.getInstance().generateNonPersistentId();
            IMonster monster = MonsterFactory.createFubenMonster(id, monsterConfig);
            BaguaPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_BAGUA);
            int[] zuobiao = publicConfig.getBosszb();
            fubenStage.enter(monster, zuobiao[0], zuobiao[1]);
            // 初始化击杀名单
            fubenStage.initWantedMap();
        }
        if (fubenConfig.getMapType() == MapType.MAIGU_FUBEN_MAP) {
            MaiguFbStage fubenStage = (MaiguFbStage) aoiStage;
            MaiguPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MAIGU);
            // 创建npc
            MonsterConfig npcMonsterConfig = monsterExportService.load(publicConfig.getMgnpc());
            long npcId = IdFactory.getInstance().generateNonPersistentId();
            IMonster npc = MonsterFactory.createFubenMonster(npcId, npcMonsterConfig);
            int[] npcZuobiao = publicConfig.getShzuobiao();
            fubenStage.enter(npc, npcZuobiao[0], npcZuobiao[1]);
            fubenStage.setNpc(npc);

            int boshu = 1;
            fubenStage.setBoshu(boshu);
            List<Map<String, Integer>> monsters = zuDuiShouHhuGuaiWuConfigExportService.getConfig(boshu).getMonsters();
            int[][] guiwuZuobiao = publicConfig.getGwzuobiao();
            for (int i = 0; i < MaiguConstant.POINT_NUM; i++) {
                int[] zuobiao = guiwuZuobiao[i];
                for (Map<String, Integer> e : monsters) {
                    for (String monsterId : e.keySet()) {
                        MonsterConfig monsterConfig = monsterExportService.load(monsterId);
                        for (int m = 0; m < e.get(monsterId); m++) {
                            long id = IdFactory.getInstance().generateNonPersistentId();
                            IMonster monster = MonsterFactory.createFubenMonster(id, monsterConfig);
                            fubenStage.enter(monster, zuobiao[0], zuobiao[1]);
                            monster.getHatredManager().addActiveHatred(npc, 1);
                            fubenStage.addMonster(monster);
                        }
                    }
                }
            }
        }
        return aoiStage;
    }
}