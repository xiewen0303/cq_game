package com.junyou.stage.model.stage.fuben;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.bus.fuben.entity.AbsFubenConfig;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.common.ObjectUtil;

/**
 * 
 *@Description  五行副本场景(单人副本)
 *@Author Yang Gao
 *@Since 2016-4-21 下午1:56:30
 *@Version 1.1.0
 */
public class WuxingFubenStage extends SingleFbStage {
    /** 副本编号 **/
    private int fubenId;
    /**副本定时器时间**/
    private int expireDelay;
    /**退出命令**/
    private short exitCmd;

    public WuxingFubenStage(String id, Integer mapId, AOIManager aoiManager, AbsFubenConfig fubenConfig, PathInfoConfig pathInfoConfig, StageType stageType) {
        super(id, mapId, aoiManager, pathInfoConfig, stageType, fubenConfig.getWantedMap());
        this.expireDelay = fubenConfig.getTime() * 1000;
        this.exitCmd = fubenConfig.getExitCmd();
        this.fubenId = fubenConfig.getId();
    }

    
    public int getFubenId() {
        return fubenId;
    }
    
    /**
     * @Description 五行副本场景规定的杀怪结束时间戳
     */
    public long getEndTimeStamp(){
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
     * 通知客户端五行副本击杀怪物信息
     */
    @Override
    public void noticeClientKillInfo(Long roleId) {
        Map<String, Integer> wantedMap = super.getWantedMap();
        List<Object[]> array = new ArrayList<Object[]>();
        if (!ObjectUtil.isEmpty(wantedMap)) {
            for (String key : wantedMap.keySet()) {
                array.add(new Object[] { key, wantedMap.get(key) });
            }
        }
        BusMsgSender.send2One(roleId, ClientCmdType.ENTER_WUXING_FUBEN, new Object[] { AppErrorCode.SUCCESS, getFubenId(), getEndTimeStamp(), array.toArray() });
    }

    /**
     * 玩家正常通关(在定时器规定时间内,玩家未被强制踢出副本场景之前,玩家完成击杀怪物数量),
     * 调用内部指令执行通关父类中通关业务，清除场景数据
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
        return InnerCmdType.S_WUXING_FUBEN_FINISH;
    }

    /**
     *  离开五行副本场景后，服务器回复客户端的退出命令 
     */
    @Override
    public void noticeClientExit(Long userRoleId) {
        StageMsgSender.send2Bus(userRoleId, InnerCmdType.B_EXIT_WUXING_FUBEN, null);
    }

}
