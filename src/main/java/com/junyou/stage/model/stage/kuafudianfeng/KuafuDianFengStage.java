package com.junyou.stage.model.stage.kuafudianfeng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.bus.kuafu_dianfeng.configure.DianFengZhiZhanConfig;
import com.junyou.bus.kuafu_dianfeng.constants.KuaFuDianFengConstants;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.utils.common.ObjectUtil;

public class KuafuDianFengStage extends PublicFubenStage {

    // 每场比赛场景中可容纳的最大玩家数
    private static final int MAX_ROLE_COUNT = 2;
    // 房间号
    private int room;
    // 场景配置信息
    private DianFengZhiZhanConfig dfzzConfig;
    // 场景定时器
    private StageScheduleExecutor scheduleExecutor;
    // 场景中的玩家集合
    private List<Long> roleIds;
    // 记录玩家出生坐标信息
    private Map<Long, Integer[]> roleBirthPoint;
    // 当前轮次比赛胜利记录key:玩家编号;value:胜利次数
    private Map<Long, Integer> winMap;
    // 当前轮次比赛房间最终胜利的玩家编号
    private Long finalWinRoleId;
    // 是否可以开始给攻击
    private boolean isCanAttack = true;
    // 是否结束战斗
    private boolean isOverFight = false;
    
    public KuafuDianFengStage(String id, Integer mapId, Integer lineNo, AOIManager aoiManager, PathInfoConfig pathInfoConfig, int room, DianFengZhiZhanConfig dfzzConfig) {
        super(id, mapId, lineNo, aoiManager, pathInfoConfig, StageType.KUAFUDIANFENG);

        this.roleIds = new ArrayList<>();
        this.winMap = new HashMap<>();
        this.roleBirthPoint = new HashMap<>();
        this.scheduleExecutor = new StageScheduleExecutor(getId());
        this.room = room;
        this.dfzzConfig = dfzzConfig;

        super.start();
    }

    public StageScheduleExecutor getStageScheduleExecutor() {
        return scheduleExecutor;
    }

    
    public boolean isCanAttack() {
        return isCanAttack;
    }

    // 获取玩家出生坐标
    public Integer[] getRoleBirthPoint(Long userRoleId) {
        return roleBirthPoint.get(userRoleId);
    }
    
    // 设置玩家出生坐标
    public void setRoleBirthPoint(Long userRoleId, Integer[] birthPoint) {
        roleBirthPoint.put(userRoleId, birthPoint);
    }

    // 判断出生坐标是否被使用过
    public boolean birthPointNotUse(){
        if(ObjectUtil.isEmpty(roleBirthPoint)){
            return false;
        }
        return true;
    }
    
    // 获取场景所有玩家
    public List<Long> getRoleIds() {
        return roleIds;
    }

    // 添加玩家胜利记录
    public void addRoleWinLog(Long userRoleId) {
        Integer winCnt = winMap.get(userRoleId);
        winCnt = null == winCnt ? 1 : ++winCnt;
        winMap.put(userRoleId, winCnt);
    }

    // 是否结束战斗
    public boolean isOverFight(){
        return this.isOverFight;
    }
    
    // 判断玩家是否获得比赛胜利 
    public boolean isRoleWin(Long userRoleId) {
        int winCount = getWinCount();
        if (winCount <= 0) {
            return false;
        }
        return winMap.get(userRoleId) >= winCount;
    }

    // 获取最大容量人数
    public int getMaxRoleCount() {
        return MAX_ROLE_COUNT;
    }

    // 获取场景人数
    public int getStageRoleCount() {
        return ObjectUtil.isEmpty(roleIds) ? 0 : (roleIds.size() > MAX_ROLE_COUNT ? MAX_ROLE_COUNT : roleIds.size());
    }

    // 获取比赛轮次
    public int getLoop() {
        if (null == dfzzConfig) {
            return 0;
        }
        return dfzzConfig.getLoop();
    }

    // 获取比赛系统分配房间
    public int getRoom() {
        return this.room;
    }

    // 获取次轮比赛房间最终胜利玩家编号
    public Long getFinalWinRoleId() {
        return this.finalWinRoleId;
    }

    // 获取pk前的准备时间
    public int getReadyTimePkBefore() {
        if (null == dfzzConfig) {
            return 0;
        }
        return dfzzConfig.getFightBeforeTime();
    }

    // 获取一场pk的时间
    public int getOnePKTime() {
        if (null == dfzzConfig) {
            return 0;
        }
        return dfzzConfig.getFighttime();
    }

    // 获取一场pk结果展示的时间
    public int getShowResultTime() {
        if (null == dfzzConfig) {
            return 0;
        }
        return dfzzConfig.getResultShowTime();
    }

    // 获取一轮比赛胜利的小场次数
    public int getWinCount() {
        if (null == dfzzConfig) {
            return 0;
        }
        return dfzzConfig.getWinfightcount();
    }

    
    @Override
    public void enter(IStageElement element, int x, int y) {
        super.enter(element, x, y);
        if (ElementType.isRole(element.getElementType())) {
            IRole role = (IRole) element;
            role.getFightAttribute().resetHp();
            if (!roleIds.contains(role.getId())) {
                roleIds.add(role.getId());
                //ChuanQiLog.info("玩家{}进入跨服巅峰之战第{}轮房间{}的场景{}", role.getName(), this.getLoop(), this.getRoom(), this.getId());
            }
        }
    }

    @Override
    public void enterNotice(Long userRoleId) {
        KuafuMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_DIANFENG, AppErrorCode.OK);
        // 场景中第一个人进入,启动战斗准备倒计时的定时器
        if (0 == getStageRoleCount())
            this.beginPKReady();
    }

    // 开始小场的战斗准备,启动预开始倒计时定时器
    public void beginPKReady() {
        isCanAttack = false;
        StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.KUAFU_DIANFENG_START_PK, null);
        scheduleExecutor.schedule(getId(), GameConstants.KUAFU_DIANFENG_START_PK, runable, getReadyTimePkBefore(), TimeUnit.SECONDS);
    }

    // PK正式开始,启动结束倒计时定时器
    public void startPKBeginSchedule() {
        isCanAttack = true;
        StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.KUAFU_DIANFENG_END_PK, null);
        scheduleExecutor.schedule(getId(), GameConstants.KUAFU_DIANFENG_END_PK, runable, getOnePKTime(), TimeUnit.SECONDS);
    }

    // 场景执行强踢操作
    public void startForceKickSchedule() {
        BusMsgSender.send2Stage(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.KUAFU_DIANFENG_FORCE_KICK, getId());
    }
    
    // 展示战斗结果定时器处理
    public void startShowResultSchedule(Long winRoleId, Long loseRoleId, boolean winFlag){
        this.addRoleWinLog(winRoleId);
        // 通知前端展示战斗结果
        boolean finalWinFlag = winFlag || isRoleWin(winRoleId);
        if(finalWinFlag){
        	this.finalWinRoleId = winRoleId;
        	this.isOverFight = true;
        }
        int winType = finalWinFlag ? KuaFuDianFengConstants.RESULT_TYPE_FINAL_WIN : KuaFuDianFengConstants.RESULT_TYPE_WIN;
        KuafuMsgSender.send2KuafuSource(winRoleId, ClientCmdType.KUAFU_DIANFENG_RESULT, winType);
        if (loseRoleId != null) {
            KuafuMsgSender.send2KuafuSource(loseRoleId, ClientCmdType.KUAFU_DIANFENG_RESULT, KuaFuDianFengConstants.RESULT_TYPE_FAIL);
        }
        //启动展示战斗结果定时器
        StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.KUAFU_DIANFENG_RESULT_SHOW, null);
        scheduleExecutor.schedule(getId(), GameConstants.KUAFU_DIANFENG_RESULT_SHOW, runable, getShowResultTime(), TimeUnit.SECONDS);
    }

    // 取消定时器
    public void cancelSchedule(String component) {
        scheduleExecutor.cancelSchedule(getId(), component);
    }

    @Override
    public void leave(IStageElement element) {
        super.leave(element);
    }

    @Override
    public void exitNotice(Long userRoleId) {
        KuafuMsgSender.send2One(userRoleId, ClientCmdType.EXIT_KUAFU_DIANFENG, AppErrorCode.OK);
    }

    @Override
    public boolean isAddPk() {
        return false;
    }

    @Override
    public boolean isCanPk() {
        return true;
    }

    @Override
    public boolean isCanRemove() {
        return !isOpen() && getAllRoleIds().length < 1;
    }

    @Override
    public boolean isFubenMonster() {
        return false;
    }

    @Override
    public boolean isCanDazuo() {
        return false;
    }

}