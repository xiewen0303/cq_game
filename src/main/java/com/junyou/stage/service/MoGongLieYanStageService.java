package com.junyou.stage.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.MoGongLieYanPublicConfig;
import com.junyou.gameconfig.shuaguai.configure.export.DingShiShuaGuaiConfig;
import com.junyou.gameconfig.shuaguai.configure.export.DingShiShuaGuaiConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.model.stage.mogonglieyan.MoGongLieYanStage;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class MoGongLieYanStageService {
    @Autowired
    private PublicFubenStageFactory publicFubenStageFactory;
    @Autowired
    private MapConfigExportService mapConfigExportService;
    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private DingShiShuaGuaiConfigExportService refreshMonsterConfigExportService;
    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
    @Autowired
    private StageControllExportService stageControllExportService;
    
    
    /**
     * 获取魔宫烈焰公共数据配置
     * 
     * @return
     */
    private MoGongLieYanPublicConfig getPublicConfig() {
        MoGongLieYanPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MOGONGLIEYAN);
        if (null == publicConfig) {
            ChuanQiLog.error("mogonglieyan public config is null");
        }
        return publicConfig;
    }

    /**
     * 更新御魔值
     * 
     * @param userRoleId
     * @param result
     */
    private void updateYuMoVal(Long userRoleId, Object result) {
        BusMsgSender.send2BusInner(userRoleId, InnerCmdType.I_MGLY_UPDATE_YUMO_VAL, result);
    }

    /**
     * 场景初始化时根据当前服务器时间检测是否需要即时刷新boss怪物
     * 
     * @param stage
     */
    private void initStageBoss(MoGongLieYanStage stage) {
        Collection<DingShiShuaGuaiConfig> configs = refreshMonsterConfigExportService.getAllDingShiShuaGuaiConfigs();
        if (configs == null) {
            ChuanQiLog.error("mogonglieyan initStageBoss, boss monstere config is null");
            return;
        }
        for (DingShiShuaGuaiConfig config : configs) {
            if (config.getType1() != 3)
                continue;
            int[] time1 = config.getTime1();
            int[] time2 = config.getTime2();
            if (time1 == null || time2 == null) {
                continue;
            }
            long startTime = DatetimeUtil.getTheTime(config.getTime1()[0], config.getTime1()[1]);
            long endTime = DatetimeUtil.getTheTime(config.getTime2()[0], config.getTime2()[1]);
            long nowTime = GameSystemTime.getSystemMillTime();
            if (startTime <= nowTime && nowTime < endTime) {
                StageMsgSender.send2StageInner(GameConstants.DEFAULT_ROLE_ID, stage.getId(), InnerCmdType.I_MGLY_DS_REFRESH_BOSS, new Object[] { config.getId(), stage.getLineNo(), (int) (endTime - nowTime), stage.getMapId() });
            }
        }
    }

    /**
     * 创建魔宫猎焰场景
     * 
     * @param stageId 场景id
     * @param mapId 地图id
     * @param lineNo 线路
     * @return
     */
    public MoGongLieYanStage createMoGongLieYanStage(String stageId, Integer mapId, Integer lineNo) {
        MapConfig mapConfig = mapConfigExportService.load(mapId);
        if (null == mapConfig) {
            ChuanQiLog.error("mogonglieyan stage init , mapId={}, config is null", mapId);
            return null;
        }
        MoGongLieYanPublicConfig publicConfig = getPublicConfig();
        if (null == publicConfig) {
            return null;
        }
        MoGongLieYanStage stage = publicFubenStageFactory.createMoGongLieYanStage(stageId, lineNo, mapConfig, publicConfig);
        if (null != stage) {
            StageManager.addStageCopy(stage);
            initStageBoss(stage);
            stage.start();
            ChuanQiLog.info("mogonglieyan stage init create success,stageId={}", stageId);
        }
        return stage;
    }

    /**
     * 定时增加经验和真气
     * 
     * @param userRoleId
     * @param stageId
     */
    public void innerAddExpZhenqi(Long userRoleId, String stageId) {
        IStage istage = StageManager.getStage(stageId);
        if (null == istage || istage.getStageType() != StageType.MGLY_STAGE) {
            return;
        }
        MoGongLieYanStage stage = (MoGongLieYanStage) istage;
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        MoGongLieYanPublicConfig config = stage.getPublicConfig();
        long exp = config.getExp() * role.getLevel() * role.getLevel();
        long zhenqi = config.getZhenqi() * role.getLevel();
        if (exp > 0) {
            roleExportService.incrExp(userRoleId, exp);
        }
        if (zhenqi > 0) {
            roleExportService.addZhenqi(userRoleId, zhenqi);
        }
        role.startMglyAddExpZhenqiSchedule(config.getTime());
    }

    /**
     * 定时扣除御魔值
     * 
     * @param userRoleId
     * @param stageId
     */
    public void innerCutYumoVal(Long userRoleId, String stageId) {
        IStage istage = StageManager.getStage(stageId);
        if (null == istage || istage.getStageType() != StageType.MGLY_STAGE) {
            return;
        }
        MoGongLieYanStage stage = (MoGongLieYanStage) istage;
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        MoGongLieYanPublicConfig config = stage.getPublicConfig();
        updateYuMoVal(userRoleId, config.getJianyumo4());
        role.startMglyCutYumoValSchedule(config.getTime());
    }

    /**
     * 玩家死亡处理:扣除御魔值
     * 
     * @param userRoleId
     * @param stageId
     */
    public void innerRoleDeadHandler(Long userRoleId, String stageId) {
        IStage istage = StageManager.getStage(stageId);
        if (null == istage || istage.getStageType() != StageType.MGLY_STAGE) {
            return;
        }
        MoGongLieYanStage stage = (MoGongLieYanStage) istage;
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        MoGongLieYanPublicConfig config = stage.getPublicConfig();
        updateYuMoVal(userRoleId, config.getJianyumo3());
    }

    /**
     * 怪物死亡处理:扣除御魔值
     * 
     * @param userRoleId
     * @param stageId
     * @param monsterId 怪物id
     * @param monsterRank 0=普通怪物,2=boss怪物
     */
    public void innerMonsterDeadHandler(Long userRoleId, String stageId, String monsterId, Integer monsterRank) {
        if (null == monsterId || null == monsterRank) {
            return;
        }
        IStage istage = StageManager.getStage(stageId);
        if (null == istage || istage.getStageType() != StageType.MGLY_STAGE) {
            return;
        }
        MoGongLieYanStage stage = (MoGongLieYanStage) istage;
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        MoGongLieYanPublicConfig config = stage.getPublicConfig();
        int cutYuMoVal = 0;
        if (monsterRank.equals(2)) {// boss怪物
            cutYuMoVal = config.getJianyumo2();
            ChuanQiLog.info("mogonglieyan stageId={},roleId={}, {} monster dead cut yumo value={}", stageId, role.getId(), "boss", cutYuMoVal);
        } else if (monsterRank.equals(0)) {// 普通怪物
            cutYuMoVal = config.getJianyumo1();
            ChuanQiLog.info("mogonglieyan stageId={},roleId={}, {} monster dead cut yumo value={}", stageId, role.getId(), "normal", cutYuMoVal);
        }
        updateYuMoVal(userRoleId, cutYuMoVal);
    }

    
    /**
     * 玩家御魔值为0,延迟踢出场景处理
     * @param userRoleId
     * @param stageId
     */
    public void innerDelayExitHandler(Long userRoleId, String stageId) {
        IStage istage = StageManager.getStage(stageId);
        if (null == istage || istage.getStageType() != StageType.MGLY_STAGE) {
            return;
        }
        MoGongLieYanStage stage = (MoGongLieYanStage) istage;
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        MoGongLieYanPublicConfig config = stage.getPublicConfig();
        role.cancelMglyAddExpZhenqiSchedule();
        role.cancelMglyCutYumoValSchedule();
        role.startMglyDelayExitStageSchedule(config.getDelaytime());
    }

    /**
     * 场景处理玩家退出
     * @param userRoleId
     * @param stageId
     */
    public void innerExitHandler(Long userRoleId, String stageId) {
        IStage istage = StageManager.getStage(stageId);
        if (null == istage || istage.getStageType() != StageType.MGLY_STAGE) {
            return;
        }
        MoGongLieYanStage stage = (MoGongLieYanStage) istage;
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        stageControllExportService.changeFuben(userRoleId, false);
        StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_LEAVE_STAGE, null);
    }

}
