package com.junyou.stage.model.stage.fuben;

import java.util.ArrayList;
import java.util.List;

import com.junyou.bus.fuben.entity.AbsFubenConfig;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.common.ObjectUtil;

/**
 * 
 *@Description 心魔斗场副本场景(单人副本)
 *@Author Yang Gao
 *@Since 2016-8-23
 *@Version 1.1.0
 */
public class XinmoDouchangFubenStage extends SingleFbStage {

    /** 玩家在场景中击杀怪物的数量 **/
    private int killNum;
    /** 玩家在场景中获取的buff列表 **/
    private List<IBuff> buffList;
    /** 副本定时器时间 **/
    private int expireDelay;
    /** 退出命令 **/
    private short exitCmd;

    public XinmoDouchangFubenStage(String id, Integer mapId, AOIManager aoiManager, PathInfoConfig pathInfoConfig, StageType stageType, AbsFubenConfig fubenConfig, int expireDelay) {
        super(id, mapId, aoiManager, pathInfoConfig, stageType, fubenConfig.getWantedMap());
        this.expireDelay = expireDelay * 1000;
        this.exitCmd = fubenConfig.getExitCmd();
    }

    /**
     * 获取所有buff列表 
     * @return
     */
    public List<IBuff> getAllBuffs() {
        return buffList;
    }

    /**
     * 添加获取的buff 
     * @param buff
     */
    public void addKillBuff(IBuff buff) {
        if (null == buffList) {
            buffList = new ArrayList<IBuff>();
        }
        buffList.add(buff);
    }

    /**
     * 获取击杀怪物数量
     * @return
     */
    public int getKillNum() {
        return this.killNum;
    }

    /**
     * 添加怪物数量
     */
    public void addKillNum() {
        this.killNum++;
    }
    
    
    @Override
    public void enter(IStageElement element, int x, int y) {
        super.enter(element, x, y);
    }

    @Override
    public void leave(IStageElement element) {
        if(null != element && ElementType.ROLE.equals(element.getElementType())){
            IRole fighter = (IRole)element;
            // 取消挑战者所有buff效果
            if(!ObjectUtil.isEmpty(buffList)){
                for(IBuff buff : buffList){
                    fighter.getBuffManager().removeBuff(buff.getId(),buff.getBuffCategory());
                }
                fighter.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
            }
        }
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
        BusMsgSender.send2One(roleId, ClientCmdType.XM_DOUCHANG_ENTER, new Object[] { AppErrorCode.SUCCESS, getEndTimeStamp() });
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
        return InnerCmdType.S_XM_DOUCHANG_FUBEN_FINISH;
    }

    /**
     * 挑战者离开副本，服务器主动通知客户端退出命令
     */
    @Override
    public void noticeClientExit(Long userRoleId) {
        StageMsgSender.send2Bus(userRoleId, InnerCmdType.B_XM_DOUCHANG_FUBEN_EXIT, this.getKillNum());
    }

}
