package com.junyou.stage.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafu_yungong.constants.KuafuYunGongConstants;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.RoleState;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuYunGongPublicConfig;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.module.GameModType;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.configure.export.impl.ZiYuanConfig;
import com.junyou.stage.configure.export.impl.ZiYuanConfigExportService;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.goods.Collect;
import com.junyou.stage.model.element.goods.CollectFacory;
import com.junyou.stage.model.element.goods.KuafuYunGongQizi;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.skill.buff.BuffFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.model.stage.kuafuyungong.KuafuYunGongStage;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.RandomUtil;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;
import com.kernel.gen.id.IdFactory;
import com.kernel.spring.container.DataContainer;

/**
 * 
 * @Description 跨服云宫之巅场景业务处理
 * @Author Yang Gao
 * @Since 2016-10-8
 * @Version 1.1.0
 */
@Service
public class KuafuYunGongStageService {

    @Autowired
    private PublicFubenStageFactory publicFubenStageFactory;
    @Autowired
    private MapConfigExportService mapConfigExportService;
    @Autowired
    private DataContainer dataContainer;
    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
    @Autowired
    private DiTuConfigExportService diTuConfigExportService;
    @Autowired
    private MonsterExportService monsterExportService;

    @Autowired
    private ZiYuanConfigExportService ziYuanConfigExportService;
    @Autowired
    private PublicRoleStateExportService publicRoleStateExportService;

    /**
     * 获取本服server编号
     * 
     * @return
     */
    private String getLocalServerId() {
        return ChuanQiConfigUtil.getServerId();
    }

    /**
     * 进入跨服云宫之巅场景失败
     * 
     * @param userRoleId
     * @param errorMsg
     */
    private void enterKuafuYunGongFail(Long userRoleId, Object errorMsg) {
        KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.KUAFU_YUNGONG_ENTER_FAIL, errorMsg);
        BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_KUAFU_LEAVE, null);
        ChuanQiLog.error("********[KuafuYunGongZhiDian]***********kuafu serverId={},userRoleId=[},enter stage fail ,errorMsg={}********[KuafuYunGongZhiDian]*********", getLocalServerId(), userRoleId, errorMsg);
    }

    /**
     * 获取跨服云宫之巅公共配置数据
     * 
     * @return
     */
    private KuafuYunGongPublicConfig getKfYunGongPublicConfig() {
        return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_KUAFU_YUNGONG);
    }

    /**
     * 全地图通告
     * 
     * @param stage
     * @param code
     * @param parameter
     */
    private void sendStageNotice(IStage stage, int code, Object parameter) {
        if (null == stage)
            return;
        BusMsgSender.send2Many(stage.getAllRoleIds(), ClientCmdType.NOTIFY_CLIENT_ALERT6, new Object[] { code, parameter });
    }

    /**
     * 活动场景创建旗子
     * 
     * @param stage 场景
     */
    private void createQizi(KuafuYunGongStage stage) {
        if (null == stage || stage.hasFlag())
            return;
        KuafuYunGongPublicConfig config = stage.getConfig();
        ZiYuanConfig ziYuanConfig = ziYuanConfigExportService.loadById(config.getZiyuanid());
        KuafuYunGongQizi qizi = CollectFacory.createKuafuYunGongQizi(IdFactory.getInstance().generateNonPersistentId(), ziYuanConfig);
        int[] zuobiao = config.getZuobiao();
        stage.enter(qizi, zuobiao[0], zuobiao[1]);
        stage.setFlag(true);
        stage.setOwnerGuildId(null);
        stage.setOwnerRole(null);
        stage.setOwnerRoleBuff(null);
        stage.setOwnerStartTime(null);
        stage.cancelTobeWinnerSchedule();
        ChuanQiLog.info("********[KuafuYunGongZhiDian]********* kuafu serverId={}, stage show qizi,  stageId={}, qiziGuiId={}********[KuafuYunGongZhiDian]*********", getLocalServerId(), stage.getId(), qizi.getId());
    }

    /**
     * 活动场景创建图腾怪物
     * 
     * @param stage
     */
    private void createLongt(KuafuYunGongStage stage) {
        if (null == stage || stage.getLongt() != null)
            return;
        KuafuYunGongPublicConfig config = stage.getConfig();
        MonsterConfig monsterConfig = monsterExportService.load(config.getLongt());
        long id = IdFactory.getInstance().generateNonPersistentId();
        IMonster monster = MonsterFactory.create(String.valueOf(id), id, monsterConfig);
        stage.setLongt(monster);
        int[] zuobiao = config.getZuobiao();
        stage.enter(monster, zuobiao[0], zuobiao[1]);
        ChuanQiLog.info("********[KuafuYunGongZhiDian]********* kuafu serverId={},stage show tuteng monster, stageId={}, monsterId={}********[KuafuYunGongZhiDian]*********", getLocalServerId(), stage.getId(), id);
    }

    /**
     * 
     * 活动场景移除龙腾(场景中当作怪物元素处理)
     * 
     * @param stage 场景
     */
    private void removeLongt(KuafuYunGongStage stage) {
        if (null == stage || stage.getLongt() == null)
            return;
        stage.leave(stage.getLongt());
        stage.setLongt(null);
        ChuanQiLog.info("********[KuafuYunGongZhiDian]********* kuafu serverId={}, stage remove tuteng monster, stageId={}********[KuafuYunGongZhiDian]*********", getLocalServerId(), stage.getId());
    }

    /**
     * 活动场景产生胜利公会处理
     */
    private void stageHasWinnerHandler(KuafuYunGongStage stage) {
        if (null == stage) {
            return;
        }
        // 扛旗者
        IRole role = stage.getOwnerRole();
        if (null == role) {
            return;
        }
        stage.setOwnerRole(null);
        // 移除扛旗者buff
        IBuff buff = stage.getOwnerRoleBuff();
        if (null != buff) {
            role.getBuffManager().removeBuff(buff.getId(), buff.getBuffCategory());
            role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
            stage.setOwnerRoleBuff(null);
        }
        // 移除图腾
        removeLongt(stage);
        // 全地图通知获胜公会信息
        DiTuConfig dituConfig = diTuConfigExportService.loadDiTu(stage.getMapId());
        KuafuYunGongPublicConfig config = stage.getConfig();
        if (dituConfig != null) {
            sendStageNotice(stage, config.getGonggao3(), new Object[] { role.getBusinessData().getGuildName(), dituConfig.getName() });
        }
        // 更新结果数据
        KuafuMsgSender.send2KuafuSource(role.getId(), InnerCmdType.KUAFU_YUNGONG_UPDATE_RESULT, new Object[] { getLocalServerId(), stage.getOwnerGuildId() });
        // 通知跨服场景中活动结束
        BusMsgSender.send2Many(stage.getAllRoleIds(), ClientCmdType.KF_YUNGONG_END, null);        
        // 开启倒计时强制踢人定时器
        stage.startClearSchedule();

    }

    /**
     * 场景踢人
     */
    private void stageClearAllRole(KuafuYunGongStage stage) {
        if (null == stage)
            return;
        // 踢人
        Object[] roleIds = stage.getAllRoleIds();
        if (roleIds != null) {
            for (Object roleId : roleIds) {
                kuafuYunGongExit(stage.getId(), (Long) roleId);
            }
            ChuanQiLog.info("********[KuafuYunGongZhiDian]*********kuafu serverId={}, stage kick out all roles********[KuafuYunGongZhiDian]********", getLocalServerId());
        }
        // 发放邮件奖励
        List<IRole> roleList = stage.getIRoleList();
        if (!ObjectUtil.isEmpty(roleList)) {
            Long winnerGuildId = stage.getOwnerGuildId();
            KuafuYunGongPublicConfig config = stage.getConfig();
            for (IRole role : roleList) {
                if (stage.getRoleTotalOnlineTime(role.getId()) < config.getTime2()) {
                    continue;
                }
                String sourceServerId = stage.getRoleServerId(role.getId());
                // 停留一定时长奖励
                KuafuMsgSender.send2KuafuSource(sourceServerId, InnerCmdType.KUAFU_YUNGONG_SEND_EMAIL_REWARD, new Object[] { role.getId(), KuafuYunGongConstants.REWARD_TYPE_TIME });
                // 胜利方奖励
                if (role.getBusinessData().getGuildId().equals(winnerGuildId)) {
                    KuafuMsgSender.send2KuafuSource(sourceServerId, InnerCmdType.KUAFU_YUNGONG_SEND_EMAIL_REWARD, new Object[] { role.getId(), KuafuYunGongConstants.REWARD_TYPE_WIN });
                }
                // 失败方奖励
                else {
                    KuafuMsgSender.send2KuafuSource(sourceServerId, InnerCmdType.KUAFU_YUNGONG_SEND_EMAIL_REWARD, new Object[] { role.getId(), KuafuYunGongConstants.REWARD_TYPE_LOSE });
                }
            }
            ChuanQiLog.info("********[KuafuYunGongZhiDian]********* kuafu serverId={}, stage send eamil reward********[KuafuYunGongZhiDian]********", getLocalServerId());
        }
        // 回收场景
        if (stage.isCanRemove()) {
            StageManager.removeCopy(stage);
        }
    }
    
    /**
     * 同步源服活动结束状态,防止由于源服时间不同步,怎样所有源服就不会再有人进来跨服了
     */
    private void synchronizedActivityOver(){
        Redis redis = GameServerContext.getRedis();
        if (null == redis) {
            ChuanQiLog.error("redis is not open!");
            return ;
        }
        Map<String, String> matchMap = redis.hgetAll(RedisKey.KUAFU_YUNGONG_SERVER_MATCH_KEY);
        if(!ObjectUtil.isEmpty(matchMap)){
            for(Map.Entry<String, String> entry : matchMap.entrySet()){
                if(entry.getValue().equals(getLocalServerId())){
                    ChuanQiLog.info("********[KuafuYunGongZhiDian]*******kuafu serverId={}, synchronized source serverId={} activity over state!!!**********[KuafuYunGongZhiDian]*********", getLocalServerId(), entry.getKey());
                    KuafuMsgSender.send2KuafuSource(entry.getKey(), InnerCmdType.KUAFU_YUNGONG_ACTIVITY_END, null);
                }
            }
        }
    }
    
    /**
     * 跨服云宫之巅活动已结束
     * 
     * @return  true=结束;false=未结束
     */
    private boolean isStageOver(){
        Redis redis = GameServerContext.getRedis();
        if (null == redis) {
            ChuanQiLog.error("redis is not open!");
            return false;
        }
        return redis.exists(RedisKey.getKfYunGongResultKey(getLocalServerId()));
    }
    
    // ---------------------------------------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------------------------------------//

    /**
     * 源服玩家进入跨服场景
     * 
     * @param userRoleId
     * @param roleData
     */
    public void kuafuYunGongSendRoleData(Long userRoleId, String sourceServerId, Object roleData) {
        if (isStageOver()) {
            ChuanQiLog.error("********[KuafuYunGongZhiDian]*********kuafu serverId={}, enter stage fail, stage has winner guild or activity is over!!!********[KuafuYunGongZhiDian]*********", getLocalServerId());
            enterKuafuYunGongFail(userRoleId, AppErrorCode.KF_YUNGONG_NOT_START_OR_OVER);
            return;
        }
        KuafuYunGongPublicConfig config = getKfYunGongPublicConfig();
        if (config == null) {
            ChuanQiLog.error("********[KuafuYunGongZhiDian]*********kuafu serverId={}, enter stage KuafuYunGongPublicConfig is null!!!********[KuafuYunGongZhiDian]*********", getLocalServerId());
            return;
        }
        int mapId = config.getMap();
        String stageId = StageUtil.getStageId(mapId, 1);
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            synchronized (this) {
                stage = StageManager.getStage(stageId);
                if (stage == null) {
                    stage = createKuafuYunGongStage(stageId, mapId);
                }
            }
        }
        if (stage == null) {
            ChuanQiLog.error("********[KuafuYunGongZhiDian]********* kuafu serverId={}, enter stage create fail********[KuafuYunGongZhiDian]*********", getLocalServerId());
            return;
        }
        KuafuYunGongStage ygStage = (KuafuYunGongStage) stage;
        if (!ygStage.isOpen()) {
            ChuanQiLog.error("********[KuafuYunGongZhiDian]*********kuafu serverId={}, enter stage fail, stage open = {}********[KuafuYunGongZhiDian]*********", getLocalServerId(), ygStage.isOpen());
            enterKuafuYunGongFail(userRoleId, AppErrorCode.KF_YUNGONG_NOT_START_OR_OVER);
            return;
        }
        dataContainer.putData(GameConstants.COMPONENET_KUAFU_DATA, userRoleId.toString(), roleData);
        IRole role = null;
        try {
            role = RoleFactory.createKuaFu(userRoleId, null, roleData);
        } catch (Exception ex) {
            enterKuafuYunGongFail(userRoleId, AppErrorCode.KUAFU_ENTER_FAIL);
            return;
        }
        // 改变跨服状态
        publicRoleStateExportService.change2PublicOnline(userRoleId);
        RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, userRoleId.toString());
        if (null == roleState) {
            roleState = new RoleState(userRoleId);
            dataContainer.putData(GameModType.STAGE_CONTRAL, userRoleId.toString(), roleState);
        }
        // 添加玩家信息
        ygStage.addRoleServerInfo(userRoleId, sourceServerId);
        // 传送到复活坐标
        List<Integer[]> xyPointList = new ArrayList<Integer[]>();
        Long ownerGuildId = ygStage.getOwnerGuildId();
        if (null == ownerGuildId) {
            xyPointList = config.getAllFuhuoPointList();
        } else if (ownerGuildId.equals(role.getBusinessData().getGuildId())) {
            xyPointList = config.getGongfuhuo();
        } else {
            xyPointList = config.getShoufuhuo();
        }
        // 地图场景切换
        Integer[] birthPoint = xyPointList.get(RandomUtil.getIntRandomValue(xyPointList.size()));
        Object[] applyEnterData = new Object[] { config.getMap(), birthPoint[0], birthPoint[1], MapType.KUAFU_YUNGONG_MAP };
        // 传送前加一个无敌状态
        role.getStateManager().add(new NoBeiAttack());
        role.setChanging(true);
        StageMsgSender.send2StageControl(role.getId(), InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
        KuafuMsgSender.send2KuafuSource(role.getId(), InnerCmdType.KUAFU_YUNGONG_ENTER_XIAOHEIWU, null);
        ChuanQiLog.info("********[KuafuYunGongZhiDian]********* kuafu serverId={}, source[serverId={}] user roleId={} enter kuafu stage success!!!********[KuafuYunGongZhiDian]********", getLocalServerId(), sourceServerId, userRoleId);
    }

    /**
     * 创建云宫之巅场景
     * 
     * @param stageId
     * @param mapId
     * @param loop
     * @param room
     * @return
     */
    public IStage createKuafuYunGongStage(String stageId, Integer mapId) {
        MapConfig mapConfig = mapConfigExportService.load(mapId);
        if (mapConfig == null) {
            ChuanQiLog.error("no map config mapid={}", mapId);
            return null;
        }
        KuafuYunGongPublicConfig config = getKfYunGongPublicConfig();
        if (config == null) {
            ChuanQiLog.error("********[KuafuYunGongZhiDian]*********kuafu serverId={}, enter stage fail, activity config is null!!!********[KuafuYunGongZhiDian]*********", getLocalServerId());
            return null;
        }
        KuafuYunGongStage stage = publicFubenStageFactory.createKuafuYunGongStage(stageId, mapConfig, config);
        if (null != stage) {
            StageManager.addStageCopy(stage);
            stage.start();
            createQizi(stage);
        }
        return stage;
    }

    /**
     * 玩家开始采集旗子
     * 
     * @param stageId
     * @param userRoleId
     * @param qiziGuid 旗子guid
     */
    public void kuafuYunGongCollect(String stageId, Long userRoleId, Long qiziGuid) {
        if (isStageOver()) {
            return;
        }
        IStage istage = StageManager.getStage(stageId);
        if (istage == null || !StageType.KUAFU_YUNGONG.equals(istage.getStageType())) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_COLLECT, AppErrorCode.KF_YUNGONG_COLLECT_ERROR);
            return;
        }
        KuafuYunGongStage stage = (KuafuYunGongStage) istage;
        IStageElement element = stage.getElement(qiziGuid, ElementType.COLLECT);
        if (element == null) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_COLLECT, AppErrorCode.KF_YUNGONG_NOT_QIZI);
            return;
        }
        Collect collect = (Collect) element;
        if (collect.getCollertType() != KuafuYunGongConstants.ZIYUAN_TYPE) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_COLLECT, AppErrorCode.KF_YUNGONG_QIZI_ERROR);
            return;
        }
        stage.setCollectLog(userRoleId, qiziGuid);
        ChuanQiLog.info("********[KuafuYunGongZhiDian]********* kuafu serverId={}, userid={} begin collect qizi,[id={}, time={}]", getLocalServerId(), userRoleId, qiziGuid, collect.getCollectTime());
        BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_COLLECT, new Object[] { AppErrorCode.SUCCESS, collect.getCollectTime() });
    }

    /**
     * 玩家开始拔起旗子
     * 
     * @param stageId
     * @param userRoleId
     */
    public void kuafuYunGongPull(String stageId, Long userRoleId) {
        if (isStageOver()) {
            return;
        }
        IStage istage = StageManager.getStage(stageId);
        if (istage == null || !StageType.KUAFU_YUNGONG.equals(istage.getStageType())) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_PULL, AppErrorCode.KF_YUNGONG_COLLECT_ERROR);
            return;
        }
        KuafuYunGongStage stage = (KuafuYunGongStage) istage;
        stage.lock();
        try {
            Long qiziGuid = stage.getCollectLog(userRoleId);
            if (null == qiziGuid) {
                BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_PULL, AppErrorCode.KF_YUNGONG_QIZI_CHANGE);
                return;
            }
            IStageElement qizi = stage.getElement(qiziGuid, ElementType.COLLECT);
            if (null == qizi) {
                BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_PULL, AppErrorCode.KF_YUNGONG_NOT_QIZI);
                return;
            }
            DiTuConfig dituConfig = diTuConfigExportService.loadDiTu(stage.getMapId());
            if (dituConfig == null) {
                BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_PULL, AppErrorCode.CONFIG_ERROR);
                return;
            }
            KuafuYunGongPublicConfig config = stage.getConfig();
            // 清空所有旗子采集记录
            stage.clearCollectLog();
            // 开启占领胜利倒计时
            stage.startTobeWinnerSchedule(config.getNeedTime());
            // 开启旗子坐标同步定时器
            stage.startSynQiziPositionSchedule();
            // 移除旗帜
            stage.leave(qizi);
            stage.setFlag(false);
            stage.setOwnerStartTime(GameSystemTime.getSystemMillTime());
            // 创建图腾
            createLongt(stage);
            // 记录占领旗子玩家及公会信息
            IRole role = stage.getElement(userRoleId, ElementType.ROLE);
            stage.setOwnerRole(role);
            stage.setOwnerGuildId(role.getBusinessData().getGuildId());
            // 占领旗子获得buff
            IBuff buff = BuffFactory.create(role, role, config.getKqbuff());
            role.getBuffManager().addBuff(buff);
            role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
            stage.setOwnerRoleBuff(buff);
            // 全地图广播
            sendStageNotice(stage, config.getGonggao5(), new Object[] { role.getBusinessData().getGuildName(), dituConfig.getName() });
            // 全地图推送旗子被占领
            BusMsgSender.send2Many(stage.getAllRoleIds(), ClientCmdType.KF_YUNGONG_QIZI_CHANGE, new Object[] { role.getBusinessData().getGuildId(), role.getBusinessData().getName() });
            // 通知源服扣除拔旗消耗
            KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.KUAFU_YUNGONG_QIZI_CONSUME, null);
            ChuanQiLog.info("********[KuafuYunGongZhiDian]********* kuafu serverId={}, stage pull qizi success, roleName={}********[KuafuYunGongZhiDian]*********", getLocalServerId(), role.getBusinessData().getName());
        } catch (Exception e) {
            ChuanQiLog.error("********[KuafuYunGongZhiDian]********* kuafu serverId={}, stage pull qizi fail!!! error info={}********[KuafuYunGongZhiDian]*********", getLocalServerId(), e.getMessage());
            BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_PULL, AppErrorCode.KF_YUNGONG_COLLECT_ERROR);
        } finally {
            stage.unlock();
        }
    }

    /**
     * 玩家退出场景
     */
    public void kuafuYunGongExit(String stageId, Long userRoleId) {
        IStage istage = StageManager.getStage(stageId);
        if (istage == null || !StageType.KUAFU_YUNGONG.equals(istage.getStageType())) {
            return;
        }
        KuafuYunGongStage stage = (KuafuYunGongStage) istage;
        IRole role = stage.getElement(userRoleId, ElementType.ROLE);
        if (null == role) {
            return;
        }
        stage.leave(role);
        BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_KUAFU_LEAVE, null);
        KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.KUAFU_YUNGONG_EXIT_XIAOHEIWU, null);
        ChuanQiLog.info("********[KuafuYunGongZhiDian]********* kuafu serverId={}, userRoleId={} exit stage success!!!********[KuafuYunGongZhiDian]******** ", getLocalServerId(), userRoleId);
    }

    /**
     * 活动产生守住旗子获得胜利的公会
     * 
     * @param stageId
     */
    public void kuafuYunGongHasWinner(String stageId) {
        IStage istage = StageManager.getStage(stageId);
        if (istage == null || !StageType.KUAFU_YUNGONG.equals(istage.getStageType())) {
            return;
        }
        KuafuYunGongStage stage = (KuafuYunGongStage) istage;
        // 关闭场景过期定时器
        stage.cancelScheduleExpire();
        // 更新活动结束状态
        stage.stop();
        // 同步源服活动进度结束
        synchronizedActivityOver();
        // 关闭场景旗子坐标同步定时器
        stage.cancelSynQiziPositionSchedule();
        ChuanQiLog.info("********[KuafuYunGongZhiDian]********* kuafu serverId={}, stage generate winner guild, into exit stage step!!!********[KuafuYunGongZhiDian]******** ", getLocalServerId());
        stageHasWinnerHandler(stage);
    }

    /**
     * 活动场景正常结束(场景过期时间到)
     * 
     * @param stageId
     */
    public void kuafuYunGongOver(String stageId) {
        IStage istage = StageManager.getStage(stageId);
        if (istage == null || !StageType.KUAFU_YUNGONG.equals(istage.getStageType())) {
            return;
        }
        KuafuYunGongStage stage = (KuafuYunGongStage) istage;
        // 更新活动结束状态
        stage.stop();
        // 同步源服活动进度结束
        synchronizedActivityOver();
        // 关闭场景旗子坐标同步定时器
        stage.cancelSynQiziPositionSchedule();
        Long winnerGuildId = stage.getOwnerGuildId();
        if (winnerGuildId == null) {// 直到活动结束都没有人占领过旗子
            stageClearAllRole(stage);
        } else {
            stageHasWinnerHandler(stage);
        }

    }

    /**
     * 活动场景倒计时清人处理
     */
    public void kuafuYunGongClearRole(String stageId) {
        IStage istage = StageManager.getStage(stageId);
        if (istage == null || !StageType.KUAFU_YUNGONG.equals(istage.getStageType())) {
            return;
        }
        KuafuYunGongStage stage = (KuafuYunGongStage) istage;
        stageClearAllRole(stage);
    }

    /**
     * 活动场景刷新门主及其仙侣外显
     * 
     * @param roleId
     * @param stageId
     */
    public void kuafuYunGongChangeClothes(Long userRoleId, String stageId) {
        IStage istage = StageManager.getStage(stageId);
        if (istage == null || !StageType.KUAFU_YUNGONG.equals(istage.getStageType())) {
            return;
        }
        KuafuYunGongStage stage = (KuafuYunGongStage) istage;
        IStageElement roleElement = stage.getElement(userRoleId, ElementType.ROLE);
        IRole role = (IRole) roleElement;
        role.getBusinessData().setKfYunGongWinnerGuildLeader(true);
        BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_CLOTHES_SHOW, true);
        ChuanQiLog.info("********[KuafuYunGongZhiDian]********* kuafu serverId={}, stage refresh clothes show, userRoleId={}", getLocalServerId(), userRoleId);
    }

    /**
     * 活动场景同步旗子坐标
     * 
     * @param stageId
     */
    public void kuafuYunGongSynPosition(String stageId) {
        IStage istage = StageManager.getStage(stageId);
        if (istage == null || !StageType.KUAFU_YUNGONG.equals(istage.getStageType())) {
            return;
        }
        KuafuYunGongStage stage = (KuafuYunGongStage) istage;
        if (!stage.isOpen()) {
            return;
        }
        BusMsgSender.send2Many(stage.getAllRoleIds(), ClientCmdType.KF_YUNGONG_QIZI_POSITION, stage.getQiziPosition());
    }

    /**
     * 活动场景增加经验和真气
     * 
     * @param stageId
     * @param userRoleId
     */
    public void kuafuYunGongAddExpZq(String stageId, Long userRoleId) {
        if (isStageOver()) {
            return;
        }
        IStage istage = StageManager.getStage(stageId);
        if (istage == null || !StageType.KUAFU_YUNGONG.equals(istage.getStageType())) {
            return;
        }
        KuafuYunGongStage stage = (KuafuYunGongStage) istage;
        if (!stage.isOpen()) {
            return;
        }
        IStageElement element = stage.getElement(userRoleId, ElementType.ROLE);
        if (null == element) {
            return;
        }
        IRole role = (IRole) element;
        KuafuYunGongPublicConfig config = stage.getConfig();
        long exp = config.getExp() * role.getLevel() * role.getLevel();
        long zhenqi = config.getZhenqi() * role.getLevel();
        KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.KUAFU_YUNGONG_ADD_EXP_ZQ, new Object[] { exp, zhenqi });
        role.startKfYunGongAddExpZqSchedule(config.getDelay());
    }

    /**
     * 活动场景更新旗子占领状态
     * 
     * @param stageId
     * @param userRoleId
     */
    public void kuafuYunGongOwnerUpdate(String stageId, Long userRoleId) {
        if (isStageOver()) {
            return;
        }
        IStage istage = StageManager.getStage(stageId);
        if (istage == null || !StageType.KUAFU_YUNGONG.equals(istage.getStageType())) {
            return;
        }
        KuafuYunGongStage stage = (KuafuYunGongStage) istage;
        if (!stage.isOpen()) {
            return;
        }
        // 扛旗者
        IRole role = stage.getOwnerRole();
        if (null == role) {
            return;
        }
        // 移除扛旗者buff
        IBuff buff = stage.getOwnerRoleBuff();
        if (null != buff) {
            role.getBuffManager().removeBuff(buff.getId(), buff.getBuffCategory());
            role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
        }
        // 移除图腾
        removeLongt(stage);
        // 创建旗子
        createQizi(stage);
        // 同步旗子坐标
        BusMsgSender.send2Many(stage.getAllRoleIds(), ClientCmdType.KF_YUNGONG_QIZI_POSITION, stage.getQiziPosition());
        // 更新旗子占领门派信息
        BusMsgSender.send2Many(stage.getAllRoleIds(), ClientCmdType.KF_YUNGONG_QIZI_CHANGE, new Object[] { -1, null });
        ChuanQiLog.info("********[KuafuYunGongZhiDian]*********kuafu serverId={}, stage update qizi state!!!********[KuafuYunGongZhiDian]******** ", getLocalServerId());
    }

}
