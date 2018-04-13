package com.junyou.stage.model.stage.fuben;

import com.junyou.bus.fuben.entity.AbsFubenConfig;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * @Description 心魔深渊副本场景(单人副本)
 * @Author Yang Gao
 * @Since 2016-8-10
 * @Version 1.1.0
 */
public class XinmoShenyuanFubenStage extends SingleFbStage {

    /** 副本编号:挑战心魔编号 **/
    private int fubenId;
    /** 挑战心魔boss类型 **/
    private Integer bossType;
    /** 副本定时器时间 **/
    private int expireDelay;
    /** 退出命令 **/
    private short exitCmd;

    public XinmoShenyuanFubenStage(String id, Integer mapId, AOIManager aoiManager, PathInfoConfig pathInfoConfig, StageType stageType, AbsFubenConfig fubenConfig, Integer bossType) {
        super(id, mapId, aoiManager, pathInfoConfig, stageType, fubenConfig.getWantedMap());
        this.fubenId = fubenConfig.getId();
        this.bossType = bossType;
        this.expireDelay = fubenConfig.getTime() * 1000;
        this.exitCmd = fubenConfig.getExitCmd();
    }

    public int getFubenId() {
        return fubenId;
    }

    public Integer getBossType() {
        return bossType;
    }

    @Override
    public void enter(IStageElement element, int x, int y) {
        super.enter(element, x, y);
    }

    @Override
    public void leave(IStageElement element) {
        super.leave(element);
    }

    /**
     * @Description 副本结束时间戳
     */
    public long getEndTimeStamp() {
        return getStartTime() + getExpireDelay();
    }

    @Override
    public int getExpireDelay() {
        return expireDelay;
    }

    @Override
    public short getExitCmd() {
        return exitCmd;
    }

    @Override
    public int getExpireCheckInterval() {
        return GameConstants.EXPIRE_CHECK_INTERVAL;
    }

    /**
     * 通知客户端信息
     */
    @Override
    public void noticeClientKillInfo(Long roleId) {
        BusMsgSender.send2One(roleId, ClientCmdType.XM_SHENYUAN_ENTER, new Object[] { AppErrorCode.SUCCESS, getBossType(), getFubenId(), getEndTimeStamp() });
    }

    /**
     * 玩家正常通关(在定时器规定时间内,玩家未被强制踢出副本场景之前,玩家完成击杀怪物数量), 调用内部指令执行清除场景数据等操作
     */
    @Override
    public short getFinishCmd() {
        return InnerCmdType.S_FUBEN_OVER;
    }

    /**
     * 玩家通关后业务处理命令
     * 
     */
    public short getFinishNoticeBusCmd() {
        return InnerCmdType.S_XM_SHENYUAN_FUBEN_FINISH;
    }

    /**
     * 成功通关后，服务器主动通知客户端退出命令
     */
    @Override
    public void noticeClientExit(Long userRoleId) {
        StageMsgSender.send2Bus(userRoleId, InnerCmdType.B_XM_SHENYUAN_FUBEN_EXIT, null);
    }

}
