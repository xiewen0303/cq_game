package com.junyou.stage.model.stage.fuben;

import java.util.HashMap;
import java.util.Map;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.stage.utils.StageHelper;
import com.junyou.utils.datetime.GameSystemTime;

/**
 *@Description 五行技能副本场景(单人副本)
 *@Author Yang Gao
 *@Since 2016-5-3
 *@Version 1.1.0
 */
public class WuxingSkillFubenStage extends SingleFbStage {
    /** 副本编号:层次 **/
    private int fubenId;
    /**副本定时器时间**/
    private int expireDelay;
    /**退出命令**/
    private short exitCmd;
    /** 创建时间 : 单位毫秒 **/
    private long startTime;
    /** 副本完成时间 : 单位毫秒 **/
    private long finishTime;    
  
    public WuxingSkillFubenStage(String id, Integer mapId, AOIManager aoiManager, PathInfoConfig pathInfoConfig, StageType stageType,int expireDelay, short exitCmd, int fubenId) {
        super(id, mapId, aoiManager, pathInfoConfig, stageType, null);
        this.expireDelay = expireDelay * 1000;
        this.exitCmd = exitCmd;
        this.fubenId = fubenId;
        setStatTime();
    }
    
    private int getFubenId() {
        return this.fubenId;
    }

    @Override
    public void enter(IStageElement element, int x, int y) {
        super.enter(element, x, y);
        addWxSkillFubenBuff(element);
    }

    @Override
    public void leave(IStageElement element) {
        super.leave(element);
        clearWxSkillFubenBuff();
    }
    
    /**
     * @Description 添加五行技能副本buff
     * @param element
     */
    public void addWxSkillFubenBuff(IStageElement element){
        if(null != element && ElementType.isRole(element.getElementType())){
            IRole role = (IRole)element;
            int[] buffs = StageHelper.getFubenExportService().getRoleWxSkillFubenBuff(role.getId());
            if(buffs == null) return;
            Map<String, Long> valMap = new HashMap<>();
            valMap.put(EffectType.x26.name(), (long)buffs[0]);
            valMap.put(EffectType.x27.name(), (long)buffs[1]);
            role.getFightAttribute().setBaseAttribute(BaseAttributeType.WUXING_SKILL_FUBEN, valMap);
            role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
        }
    }
    
    /**
     * @Description 清除五行技能副本buff
     * @param element
     */
    public void clearWxSkillFubenBuff(){
        IRole role = super.getChallenger();
        if(null != role){
            role.getFightAttribute().clearBaseAttribute(BaseAttributeType.WUXING_SKILL_FUBEN, true);
            role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
        }
    }

    /**
     * @Description 副本结束时间戳
     */
    public long getEndTimeStamp(){
        return getStartTime() + getExpireDelay();
    }
    
    /**
     * @Description 副本完成时间
     */
    public long getFinishTime(){
        return this.finishTime;
    }
      
    public void setStatTime(){
        this.startTime = GameSystemTime.getSystemMillTime();
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
        BusMsgSender.send2One(roleId, ClientCmdType.WUXING_SKILL_FUBEN_ENTER, new Object[] { AppErrorCode.SUCCESS, getFubenId(), getEndTimeStamp() });
    }

    /**
     * 玩家正常通关(在定时器规定时间内,玩家未被强制踢出副本场景之前,玩家完成击杀怪物数量),
     * 调用内部指令执行清除场景数据等操作
     */
    @Override
    public short getFinishCmd() {
        this.finishTime = GameSystemTime.getSystemMillTime() - this.startTime;
        return InnerCmdType.S_FUBEN_OVER;
    }
    
    /**
     * 玩家通关后业务处理命令
     *
     */
    public short getFinishNoticeBusCmd() {
        return InnerCmdType.S_WUXING_SKILL_FUBEN_FINISH;
    }

    /**
     *  成功通关后，服务器主动通知客户端退出命令 
     */
    @Override
    public void noticeClientExit(Long userRoleId) {
        StageMsgSender.send2Bus(userRoleId, InnerCmdType.B_EXIT_WUXING_SKILL_FUBEN, null);
    }

}
