package com.junyou.stage.model.stage.fuben;

import java.util.concurrent.TimeUnit;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * @Description 五行试炼副本场景
 * @Author Yang Gao
 * @Since 2016-6-21
 * @Version 1.1.0
 */
public class WuxingShilianFubenStage extends SingleFbStage {
    /* 比赛退出指令 */
    private short exitCmd;
    /* 比赛结束时间 */
    private int expireDelay;
    /* 强制副本清场踢人 定时器 */
    private StageScheduleExecutor scheduleExecutor;
    /* 击杀怪物数 */
    private int killMonsterNum;

    public WuxingShilianFubenStage(String id, Integer mapId, AOIManager aoiManager, PathInfoConfig pathInfoConfig, StageType stageType, int expireDelay, short exitCmd) {
        super(id, mapId, aoiManager, pathInfoConfig, stageType, null);
        this.expireDelay = expireDelay * 1000;
        this.exitCmd = exitCmd;
        this.scheduleExecutor = new StageScheduleExecutor(getId());
    }

    @Override
    public void leave(IStageElement element) {
        super.leave(element);
    }

    @Override
    public void enter(IStageElement element, int x, int y) {
        if (null != element && ElementType.isRole(element.getElementType())) {
            IRole role = (IRole) element;
            role.getFightAttribute().resetHp(); // 进副本满血
        }
        super.enter(element, x, y);
    }

    // 开启最终时间到，强制回收定时器
    public void startForceClearSchedule() {
        clearForceClearSchedule();
        StageTokenRunable runable = new StageTokenRunable(null, getId(), InnerCmdType.S_EXIT_FUBEN, getId());
        scheduleExecutor.schedule(getId(), GameConstants.COMPONENT_FUBEN_FORCED_LEAVE, runable, getExpireCheckInterval(), TimeUnit.MILLISECONDS);
    }

    public void clearForceClearSchedule() {
        scheduleExecutor.cancelSchedule(getId(), GameConstants.COMPONENT_FUBEN_FORCED_LEAVE);
    }

    public int getKillMonsterNum() {
        return killMonsterNum;
    }

    public void addKillMonsterNum(int killMonsterNum) {
        this.killMonsterNum += killMonsterNum;
    }

    //副本的结束时间戳
    public long getEndTimeStamp() {
        return getStartTime() + getExpireDelay();
    }

    @Override
    public int getExpireDelay() {
        return expireDelay;
    }

    @Override
    public int getExpireCheckInterval() {
        return GameConstants.EXPIRE_CHECK_INTERVAL;
    }

    @Override
    public void noticeClientKillInfo(Long roleId) {
        BusMsgSender.send2One(roleId, ClientCmdType.WUXING_SHILIAN_FUBEN_ENTER, new Object[] { AppErrorCode.SUCCESS, getEndTimeStamp() });
    }

    // 通知客户端玩家成功离开副本场景指令
    @Override
    public void noticeClientExit(Long userRoleId) {
        clearForceClearSchedule();
        StageMsgSender.send2Bus(userRoleId, InnerCmdType.B_EXIT_WUXING_SHILIAN_FUBEN, null);
    }

    @Override
    public short getExitCmd() {
        return exitCmd;
    }

    @Override
    public short getFinishCmd() {
        return 0;
    }

    @Override
    public short getFinishNoticeBusCmd() {
        return 0;
    }

}
