package com.junyou.stage.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.ShouhuFubenConfig;
import com.junyou.bus.fuben.service.ShouhuFubenConfigService;
import com.junyou.bus.lianyuboss.stage.LianyuBossStage;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.ShouhuPublicConfig;
import com.junyou.stage.model.core.help.GongGongServiceHelper;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IElementProduceTeam;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.FubenMonsterProduceTeam;
import com.junyou.stage.model.stage.fuben.JianzhongFubenStage;
import com.junyou.stage.model.stage.fuben.JianzhongMonsterProduceTeam;
import com.junyou.stage.model.stage.fuben.ShouhuFubenStage;
import com.junyou.stage.model.stage.fuben.SingleFbStage;
import com.junyou.stage.tafang.stage.TaFangStage;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.gen.id.IdFactory;

/**
 * 副本场景Service
 * 
 * @author LiuYu
 * @date 2015-3-13 下午6:35:35
 */
@Service
public class FubenStageService {

    @Autowired
    private ShouhuFubenConfigService shouhuFubenConfigService;

    /**
     * 退出副本,副本检测
     * 
     * @param stageId
     */
    public void exitFuben(String stageId) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null || !StageType.isSingleFuben(stage.getStageType())) {
            return;
        }
        SingleFbStage fubenStage = (SingleFbStage) stage;

        IRole role = fubenStage.getChallenger();
        if (role == null) {
            // 场景无人，立马回收场景
            StageManager.removeCopy(fubenStage);
            return;
        }

        fubenStage.cancleScheduleExpire();
        fubenStage.clearFubenMonster();

        // 定时强制退出副本操作
        StageMsgSender.send2StageControl(role.getId(), InnerCmdType.S_APPLY_LEAVE_STAGE, role.getId());
    }

    /**
     * 完成副本清场
     * 
     * @param stageId
     * @param type
     */
    public void challengeOver(String stageId) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null || !StageType.isSingleFuben(stage.getStageType())) {
            return;
        }
        SingleFbStage fubenStage = (SingleFbStage) stage;

        IRole role = fubenStage.getChallenger();
        // 通关
        fubenStage.tongGuanHandle();

        short finishBusCmd = fubenStage.getFinishNoticeBusCmd();
        if (0 != finishBusCmd) {
            StageMsgSender.send2Bus(role.getId(), finishBusCmd, null);
        }
    }

    /**
     * 退出塔防副本
     * 
     * @param stageId
     */
    public void exitTaFangFuben(String stageId) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null || !StageType.TAFANG_FUBEN.equals(stage.getStageType())) {
            return;
        }
        TaFangStage taFangStage = (TaFangStage) stage;

        IRole role = taFangStage.getChallenger();
        if (role == null) {
            // 场景无人，立马回收场景
            StageManager.removeCopy(taFangStage);
            return;
        }
        taFangStage.cancelSchedule();
        taFangStage.clearFubenMonster();

        // 定时强制退出副本操作
        StageMsgSender.send2StageControl(role.getId(), InnerCmdType.S_APPLY_LEAVE_STAGE, role.getId());
    }

    /**
     * 剑冢副本时间到,副本检测
     * 
     * @param stageId
     */
    public void jianzhongFubenTimeOver(String stageId) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null || !StageType.isSingleFuben(stage.getStageType())) {
            return;
        }

        JianzhongFubenStage fubenStage = (JianzhongFubenStage) stage;
        IRole role = fubenStage.getChallenger();
        if (role == null) {
            // 场景无人，立马回收场景
            StageManager.removeCopy(fubenStage);
            return;
        }
        fubenStage.setGameOver(true);
        // 场景结束
        fubenStage.cancleScheduleExpire();// 停止之前的副本过期强踢定时
        fubenStage.clearFubenMonster();// 清怪
        // 停止刷怪定时器
        Collection<IElementProduceTeam> produceTeams = fubenStage.getStageProduceManager().getElementProduceTeams(ElementType.MONSTER);
        if (produceTeams != null) {
            for (IElementProduceTeam elementProduceTeam : produceTeams) {
                if (elementProduceTeam instanceof JianzhongMonsterProduceTeam) {
                    JianzhongMonsterProduceTeam produceTeam = (JianzhongMonsterProduceTeam) elementProduceTeam;
                    produceTeam.cancleSchedule();
                    break;
                }
            }
        }

        // 此次副本击杀数和道具获取数
        BusMsgSender.send2One(role.getId(), ClientCmdType.JIANZHONG_FUBEN_RESULT, new Object[] { fubenStage.getKillMonsterNum(), fubenStage.getJingqiItemNum() });

        // 定时强制退出副本操作
        fubenStage.startForceClearSchedule();
    }

    /**
     * 最终时间到强制退出剑冢副本,再次清理下场景 副本检测
     * 
     * @param stageId
     */
    public void forceExitJianzhongFuben(String stageId) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null || !StageType.isSingleFuben(stage.getStageType())) {
            return;
        }
        JianzhongFubenStage fubenStage = (JianzhongFubenStage) stage;

        IRole role = fubenStage.getChallenger();
        if (role == null) {
            // 场景无人，立马回收场景
            StageManager.removeCopy(fubenStage);
            return;
        }
        // 场景结束
        fubenStage.cancleScheduleExpire();// 停止之前的副本过期强踢定时
        fubenStage.clearFubenMonster();// 清怪
        fubenStage.clearForceClearSchedule();
        // 停止刷怪定时器
        Collection<IElementProduceTeam> produceTeams = fubenStage.getStageProduceManager().getElementProduceTeams(ElementType.MONSTER);
        if (produceTeams != null) {
            for (IElementProduceTeam elementProduceTeam : produceTeams) {
                if (elementProduceTeam instanceof JianzhongMonsterProduceTeam) {
                    JianzhongMonsterProduceTeam produceTeam = (JianzhongMonsterProduceTeam) elementProduceTeam;
                    produceTeam.cancleSchedule();
                    break;
                }
            }
        }
        // 强制切换场景
        StageMsgSender.send2StageControl(role.getId(), InnerCmdType.S_APPLY_LEAVE_STAGE, role.getId());
        // 推送给客户端退出副本成功
        BusMsgSender.send2One(role.getId(), ClientCmdType.JIANZHONG_FUBEN_EXIT, AppErrorCode.OK);
    }

    /**
     * 完成门派炼狱boss副本
     * 
     * @param stageId
     * @param type
     */
    public void lianyuBossFinish(String stageId) {
        LianyuBossStage fubenStage = (LianyuBossStage) StageManager.getStage(stageId);
        if (fubenStage == null) {
            return;
        }
        long completeTime = GameSystemTime.getSystemMillTime() - fubenStage.getStartTime();
        Object[] data = new Object[3];
        data[0] = fubenStage.getConfigId();
        data[1] = fubenStage.getStartTime();
        data[2] = completeTime;
        IRole role = fubenStage.getChallenger();
        // 通关
        fubenStage.tongGuanHandle();
        StageMsgSender.send2Bus(role.getId(), fubenStage.getFinishNoticeBusCmd(), data);
    }

    /**
     * 完成守护副本清场
     * 
     * @param stageId
     * @param type
     */
    public void shouhuFinish(String stageId) {
        ShouhuFubenStage fubenStage = (ShouhuFubenStage) StageManager.getStage(stageId);
        if (fubenStage == null) {
            return;
        }
        if (fubenStage.isFail()) {
            return;
        }

        IRole role = fubenStage.getChallenger();
        int nextId = fubenStage.getConfig().getId() + 1;
        // 同步已完成关卡数
        StageMsgSender.send2Bus(role.getId(), InnerCmdType.SYN_SHOUHU_FUBEN_GUAN, nextId);

        ShouhuFubenConfig config = shouhuFubenConfigService.loadById(nextId);
        if (config == null) {
            StageMsgSender.send2One(role.getId(), ClientCmdType.FUBEN_SHOUHU_SYSTEM_SEND, nextId);
            return;// 已通关
        }
        fubenStage.cancleScheduleExpire();
        fubenStage.setConfig(config);
        ShouhuPublicConfig publicConfig = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_SHOUHU);

        int x = publicConfig.getMonsterPoint()[0];
        int y = publicConfig.getMonsterPoint()[1];
        List<Integer[]> list2 = new ArrayList<>();
        for (int i = 0; i < config.getMonsterCount(); i++) {
            list2.add(new Integer[] { x - 2 + Lottery.roll(5), y - 2 + Lottery.roll(5) });
        }
        FubenMonsterProduceTeam team = new FubenMonsterProduceTeam(IdFactory.getInstance().generateNonPersistentId() + "", ElementType.MONSTER, config.getMonsterCount(), config.getMonsterId(), list2, 1000);
        fubenStage.getStageProduceManager().addElementProduceTeam(team);
        team.schedule();

        fubenStage.changeWantedMap(config.getWantedMap());
        fubenStage.startScheduleExpireCheck(role.getId());

        fubenStage.noticeClientKillInfo(role.getId());
    }
}
