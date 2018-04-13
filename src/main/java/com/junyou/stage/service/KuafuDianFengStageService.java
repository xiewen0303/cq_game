package com.junyou.stage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafu_dianfeng.configure.DianFengZhiZhanConfig;
import com.junyou.bus.kuafu_dianfeng.configure.DianFengZhiZhanConfigExportService;
import com.junyou.bus.kuafu_dianfeng.constants.KuaFuDianFengConstants;
import com.junyou.bus.kuafu_dianfeng.export.KuafuDianFengExportService;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.RoleState;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.module.GameModType;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.StageOutputWrapper;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.model.stage.kuafudianfeng.KuafuDianFengStage;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.BufferedMsgWriter;
import com.junyou.stage.tunnel.IMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.lottery.Lottery;
import com.kernel.cache.redis.Redis;
import com.kernel.spring.container.DataContainer;

@Service
public class KuafuDianFengStageService {

    @Autowired
    private DataContainer dataContainer;
    @Autowired
    private KuafuDianFengExportService kuafuDianFengExportService;
    @Autowired
    private DianFengZhiZhanConfigExportService dianFengZhiZhanConfigExportService;
    @Autowired
    private PublicFubenStageFactory publicFubenStageFactory;
    @Autowired
    private MapConfigExportService mapConfigExportService;
    @Autowired
    private PublicRoleStateExportService publicRoleStateExportService;

    /**
     * 发送参与巅峰之战的玩家信息
     * 
     * @param loop 轮次
     * @param room 房间
     * @param userRoleId 玩家编号
     * @param roleData
     */
    public void kuafuDianFengSendRoleData(Integer loop, Integer room, Long userRoleId, Object roleData) {
        Redis redis = GameServerContext.getRedis();
        if (redis == null) {
            ChuanQiLog.error("no redis config");
            return;
        }
        DianFengZhiZhanConfig dfzzConfig = dianFengZhiZhanConfigExportService.loadByLoop(loop);
        if (dfzzConfig == null) {
            ChuanQiLog.error("not found dianFengZhiZhanConfig config, id=" + loop);
            return;
        }

        int mapId = dfzzConfig.getMapId();
        String stageId = StageUtil.getDianFengStageId(mapId, loop, room);
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            synchronized (this) {
                stage = StageManager.getStage(stageId);
                if (stage == null) {
                    stage = createKuafuDianFengStage(stageId, mapId, loop, room);
                }
            }
        }

        if (stage == null) {
            ChuanQiLog.error("can not create or get stage in kuafu dianfeng");
            return;
        }
        dataContainer.putData(GameConstants.COMPONENET_KUAFU_DATA, userRoleId.toString(), roleData);

        IRole role = null;
        try {
            role = RoleFactory.createKuaFu(userRoleId, null, dataContainer.getData(GameConstants.COMPONENET_KUAFU_DATA, userRoleId.toString()));
        } catch (Exception ex) {
            enterKuafuDianFengFail(userRoleId);
            return;
        }
        KuafuDianFengStage dfStage = (KuafuDianFengStage) stage;
        int stageRoleCount = dfStage.getStageRoleCount();
        if (stageRoleCount >= dfStage.getMaxRoleCount()) {
            enterKuafuDianFengFail(userRoleId);
            return;
        }

        Integer[] birthPoint = null;
        if (dfStage.birthPointNotUse()) {
            birthPoint = dfzzConfig.getZuobiao1();
        } else {
            birthPoint = dfzzConfig.getZuobiao2();
        }
        dfStage.setRoleBirthPoint(userRoleId, birthPoint);

        publicRoleStateExportService.change2PublicOnline(userRoleId);
        RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, userRoleId.toString());
        if (null == roleState) {
            roleState = new RoleState(userRoleId);
            dataContainer.putData(GameModType.STAGE_CONTRAL, userRoleId.toString(), roleState);
        }
        Object[] applyEnterData = new Object[] { dfzzConfig.getMapId(), birthPoint[0], birthPoint[1], MapType.KUAFU_DIANFENG_MAP, loop, room };
        // 传送前加一个无敌状态
        role.getStateManager().add(new NoBeiAttack());
        role.setChanging(true);

        StageMsgSender.send2StageControl(role.getId(), InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
        KuafuMsgSender.send2KuafuSource(role.getId(), InnerCmdType.KUAFU_DIANFENG_ENTER_XIAOHEIWU, null);
    }

    /**
     * 创建巅峰之战场景
     * 
     * @param stageId
     * @param mapId
     * @param loop
     * @param room
     * @return
     */
    public IStage createKuafuDianFengStage(String stageId, Integer mapId, Integer loop, Integer room) {
        MapConfig mapConfig = mapConfigExportService.load(mapId);
        if (mapConfig == null) {
            ChuanQiLog.error("no map config mapid={}", mapId);
            return null;
        }
        DianFengZhiZhanConfig dfzzConfig = dianFengZhiZhanConfigExportService.loadByLoop(loop);
        if (dfzzConfig == null) {
            ChuanQiLog.error("not found DianFengZhiZhanConfig config, id=" + loop);
            return null;
        }
        KuafuDianFengStage stage = publicFubenStageFactory.createKuafuDianFengStage(stageId, loop, room, mapConfig, dfzzConfig);
        if(null != stage){
            StageManager.addStageCopy(stage);
        }
        return stage;
    }

    /**
     * 巅峰之战小场战斗开始:准备时间结束之后
     * 
     * @param userRoleId
     */
    public void kuafuDianFengStartPk(String stageId) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null || stage.getStageType() != StageType.KUAFUDIANFENG) {
            return;
        }
        KuafuDianFengStage dfStage = (KuafuDianFengStage) stage;
        List<Long> roleIds = dfStage.getRoleIds();
        int roleCount = ObjectUtil.isEmpty(roleIds) ? 0 : roleIds.size();
        if (roleCount == 2) {
            Long redRoleId = roleIds.get(0);
            IRole redRole = dfStage.getElement(redRoleId, ElementType.ROLE);
            
            Long blueRoleId = roleIds.get(1);
            IRole blueRole = dfStage.getElement(blueRoleId, ElementType.ROLE);
            KuafuMsgSender.send2KuafuSource(redRoleId, ClientCmdType.KUAFU_DIANFENG_ONCE_NOTICE, new Object[] { blueRoleId, blueRole.getName() });
            KuafuMsgSender.send2KuafuSource(blueRoleId, ClientCmdType.KUAFU_DIANFENG_ONCE_NOTICE, new Object[]{ redRoleId, redRole.getName()});
            
            dfStage.startPKBeginSchedule();
        } else if (roleCount == 1) {
            dfStage.startShowResultSchedule(roleIds.get(0), null, true);
        }
    }

    /**
     * 巅峰之战结果展示结束后处理
     * @param stageId
     */
    public void kuafuDianFengShowResult(String stageId) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null || stage.getStageType() != StageType.KUAFUDIANFENG) {
            return;
        }
        KuafuDianFengStage dfStage = (KuafuDianFengStage) stage;
        if(dfStage.isOverFight()){
            // 结束强踢        
            dfStage.startForceKickSchedule();
        }else{
            // 重置场景数据
            resetStageData(dfStage);
            // 再开一场
            dfStage.beginPKReady();
        }
    }
    
    /**
     * 巅峰之战小场战斗结束处理:时间到了
     * 
     * @param userRoleId
     */
    public void kuafuDianFengEndPk(String stageId) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null || stage.getStageType() != StageType.KUAFUDIANFENG) {
            return;
        }
        KuafuDianFengStage dfStage = (KuafuDianFengStage) stage;
        List<Long> roleIds = dfStage.getRoleIds();
        if (roleIds.size() != dfStage.getMaxRoleCount()) {
            return;
        }
        Long roleId0 = roleIds.get(0);
        Long roleId1 = roleIds.get(1);
        IRole redRole = dfStage.getElement(roleId0, ElementType.ROLE);
        IRole blueRole = dfStage.getElement(roleId1, ElementType.ROLE);
        if (null == redRole || null == blueRole) {
            return;
        }
        Long winRoleId = null;
        Long loseRoleId = null;
        long redCurHp = redRole.getFightAttribute().getCurHp();
        long blueCurHp = blueRole.getFightAttribute().getCurHp();
        // 剩余血量比拼
        if (redCurHp > blueCurHp) {
            winRoleId = roleId0;
            loseRoleId = roleId1;
        } else if (redCurHp < blueCurHp) {
            winRoleId = roleId1;
            loseRoleId = roleId0;
        } else {
            long redZhiLi = redRole.getFightAttribute().getZhanLi();
            long blueZhiLi = blueRole.getFightAttribute().getZhanLi();
            // 战斗力比拼
            if (redZhiLi > blueZhiLi) {
                winRoleId = roleId0;
                loseRoleId = roleId1;
            } else if (redZhiLi < blueZhiLi) {
                winRoleId = roleId1;
                loseRoleId = roleId0;
            } else {
                // 剩余血量和战斗力都相同,系统随机获取一方胜利(双方胜利概率各站50%)
                if (Lottery.roll(KuaFuDianFengConstants.ZHANLI_RATE, Lottery.HUNDRED)) {
                    winRoleId = roleId0;
                    loseRoleId = roleId1;
                } else {
                    winRoleId = roleId1;
                    loseRoleId = roleId0;
                }
            }
        }
        dfStage.startShowResultSchedule(winRoleId, loseRoleId, false);
    }

    /**
     * 巅峰之战小场战斗玩家pk死亡
     * 
     * @param stageId
     * @param loseRoleId
     * @param winRoleId
     */
    public void kuafuDianFengDeathPk(String stageId, Long winRoleId, Long loseRoleId) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null || stage.getStageType() != StageType.KUAFUDIANFENG) {
            return;
        }
        KuafuDianFengStage dfStage = (KuafuDianFengStage) stage;
        // 关闭pk倒计时的定时器
        dfStage.cancelSchedule(GameConstants.KUAFU_DIANFENG_END_PK);
        dfStage.startShowResultSchedule(winRoleId, loseRoleId, false);
    }

    /**
     * 活动结束，强制踢人
     * 
     * @param stageId
     */
    public void kuafuDianFengForceKick(String stageId) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        if (stage.getStageType() != StageType.KUAFUDIANFENG) {
            return;
        }
        KuafuDianFengStage dfStage = (KuafuDianFengStage) stage;
        // 更新redis数据
        kuafuDianFengExportService.updateDianFengRedisData(dfStage.getLoop(), dfStage.getRoom(), dfStage.getFinalWinRoleId());
        List<Long> roleIds = dfStage.getRoleIds();
        //ChuanQiLog.info("跨服巅峰之战第{}轮房间{}的场景{},强制踢人,更新redis数据", dfStage.getLoop(), dfStage.getRoom(), dfStage.getId());
        for (Long roleId : roleIds) {
            IRole role = dfStage.getElement(roleId, ElementType.ROLE);
            if(null != role){
                dfStage.leave(role);
                KuafuMsgSender.send2KuafuSource(roleId, InnerCmdType.KUAFU_DIANFENG_EXIT_STAGE, null);
                BusMsgSender.send2BusInner(roleId, InnerCmdType.INNER_KUAFU_LEAVE, null);
            }
        }
        dfStage.stop();
        if (dfStage.isCanRemove()) {
            StageManager.removeCopy(dfStage);
        }
    }

    /**
     * 跨服巅峰玩家在小场比赛中下线处理
     * 
     * @param userRoleId
     */
    public void kuafuDianFengOffineHandle(Long userRoleId) {
        //ChuanQiLog.info("跨服巅峰玩家{}在比赛中途下线处理", userRoleId);
        String stageId = kuafuDianFengExportService.getDianFengStageIdByRoleId(userRoleId);
        if (ObjectUtil.strIsEmpty(stageId)) {
            return;
        }
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        if (stage.getStageType() != StageType.KUAFUDIANFENG) {
            return;
        }
        KuafuDianFengStage dfStage = (KuafuDianFengStage) stage;
        List<Long> roleIds = dfStage.getRoleIds();
        Long winRoleId = null;
        for (Long roleId : roleIds) {
            if (!roleId.equals(userRoleId)) {
                winRoleId = roleId;
                break;
            }
        }
        if(null != winRoleId){
            dfStage.startShowResultSchedule(winRoleId, userRoleId, true);
        }
    }

    // 进入巅峰之战失败
    private void enterKuafuDianFengFail(Long userRoleId) {
        KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.KUAFU_DIANFENG_ENTER_FAIL, null);
        BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_KUAFU_LEAVE, null);
        //ChuanQiLog.error("玩家{},进入巅峰之战房间场景失败", userRoleId);
    }

    // 重置场景数据
    private void resetStageData(KuafuDianFengStage dfStage) {
        //ChuanQiLog.info("重置场景数据");
        if (null == dfStage) {
            return;
        }
        List<Long> roleIds = dfStage.getRoleIds();
        if (dfStage.getStageRoleCount() <= 1) {
            return;
        }
        for (Long roleId : roleIds) {
            IRole role = dfStage.getElement(roleId, ElementType.ROLE);
            if (null != role) {
                // 重置玩家血量
                role.getFightAttribute().resetHp();
                // 清理玩家所有buff
                role.getBuffManager().clearAll();
                if (role.getStateManager().contains(StateType.DEAD)) {
                    role.getStateManager().remove(StateType.DEAD);
                    IMsgWriter writer = BufferedMsgWriter.getInstance();
                    role.getFightStatistic().flushChanges(writer);
                    writer.flush();
                    StageMsgSender.send2Many(dfStage.getSurroundRoleIds(role.getPosition()), ClientCmdType.TOWN_REVIVE, roleId);
                    // 重置玩家坐标
                    Integer[] birthPoint = dfStage.getRoleBirthPoint(roleId);
                    birthPoint = null == birthPoint ? new Integer[] { 50, 50 } : birthPoint;
                    dfStage.teleportTo(role, birthPoint[0], birthPoint[1]);
                    StageMsgSender.send2Many(dfStage.getSurroundRoleIds(role.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, StageOutputWrapper.teleport(role));
                } else {
                    IMsgWriter writer = BufferedMsgWriter.getInstance();
                    role.getFightStatistic().flushChanges(writer);
                    writer.flush();
                    // 重置玩家坐标
                    Integer[] birthPoint = dfStage.getRoleBirthPoint(roleId);
                    birthPoint = null == birthPoint ? new Integer[] { 50, 50 } : birthPoint;
                    dfStage.teleportTo(role, birthPoint[0], birthPoint[1]);
                    StageMsgSender.send2Many(dfStage.getSurroundRoleIds(role.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, StageOutputWrapper.teleport(role));
                    //重置糖宝坐标
                    Pet pet = role.getPet();
                    if(null != pet && dfStage.isCanHasTangbao()){
                        pet.setPosition(birthPoint[0], birthPoint[1]);
                    	StageMsgSender.send2Many(dfStage.getSurroundRoleIds(pet.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, StageOutputWrapper.teleport(pet));
                    }
                }
            }
        }
    }

    

}
