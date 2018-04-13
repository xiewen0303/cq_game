package com.junyou.stage.model.stage.kuafuyungong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuYunGongPublicConfig;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 
 * @Description 跨服云宫之巅场景
 * @Author Yang Gao
 * @Since 2016-10-8
 * @Version 1.1.0
 */
public class KuafuYunGongStage extends PublicFubenStage {

    /** 场景定时器 */
    private StageScheduleExecutor scheduleExecutor;
    /** 锁对象 **/
    private ReentrantLock lock = new ReentrantLock();
    /** 活动配置 **/
    private KuafuYunGongPublicConfig config;
    /** 
     * 活动参赛玩家信息列表
     * Map={key:userRoleId,value=sourceServerId}
     */
    private Map<Long,String> roleServerMap;
    /** 活动场景创建时间 **/
    private long stageCreateTime;
    /** 活动场景是否存在旗子 **/
    private boolean qiziFlag;
    /** 活动场景旗子夺取公会id(图腾守护公会id) **/
    private Long ownerGuildId;
    /** 活动场景旗子夺取玩家 **/
    private IRole ownerRole;
    /** 活动场景旗子夺取玩家获得buff对象 **/
    private IBuff ownerRoleBuff;
    /** 活动场景旗子夺取时间戳 **/
    private Long ownerStartTime;
    /** 活动场景玩家采集旗子记录 **/
    private Map<Long, Long> roleCollectLog;
    /** 活动场景龙腾(怪物) **/
    private IStageElement longt;
    /** 活动场景参赛玩家集合 **/
    private Map<Long,IRole> roleMaps;
    /**
     * 活动角色每次进出场景,停留时长记录数据 </br> key:(Long)角色ID</br> value:(ArrayList)</br>
     * [{</br> 0:进入场景时间戳(Long)</br> 1.离开场景时间戳(Long)</br> }]</br>
     */
    private Map<Long, List<Object[]>> roleOnlineLogMap;

    public KuafuYunGongStage(String id, Integer mapId, Integer lineNo, AOIManager aoiManager, PathInfoConfig pathInfoConfig, KuafuYunGongPublicConfig config) {
        super(id, mapId, lineNo, aoiManager, pathInfoConfig, StageType.KUAFU_YUNGONG);
        this.config = config;
        this.roleServerMap = new HashMap<Long, String>();
        this.stageCreateTime = GameSystemTime.getSystemMillTime();
        this.scheduleExecutor = new StageScheduleExecutor(getId());
        this.roleCollectLog = new HashMap<Long, Long>();
        this.roleMaps = new HashMap<Long,IRole>();
        this.roleOnlineLogMap = new HashMap<Long, List<Object[]>>();
        this.startScheduleExpire();
    }

    /**
     * 场景配置数据
     * 
     * @return
     */
    public KuafuYunGongPublicConfig getConfig() {
        return config;
    }
    
    /**
     * 获取玩家所在服务器编号 
     * 
     * @param userRoleId
     * @return
     */
    public String getRoleServerId(Long userRoleId){
        return roleServerMap.get(userRoleId);
    }

    /**
     * 添加参赛玩家信息
     */
    public void addRoleServerInfo(Long userRoleId, String sourceServerId){
        this.roleServerMap.put(userRoleId, sourceServerId);
    }

    /**
     * 获取所有参赛玩家集合
     * 
     * @return
     */

    public List<IRole> getIRoleList() {
        return new ArrayList<>(roleMaps.values());
    }

    /**
     * 场景资源锁
     */
     public void lock() {
        try {
            lock.tryLock(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

        }
    }

    public void unlock() {
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * 开启场景过期定时器
     */
    private void startScheduleExpire() {
        this.cancelScheduleExpire();
        StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.I_KUAFU_YUNGONG_OVER, null);
        long dealy = DatetimeUtil.getTheDayTheTime(config.getEndtime()[0], config.getEndtime()[1], stageCreateTime) - GameSystemTime.getSystemMillTime();
        scheduleExecutor.schedule(getId(), GameConstants.KUAFU_YUNGONG_STAGE_EXPIRE_PRODUCE, runable, dealy, TimeUnit.MILLISECONDS);
    }

    /**
     * 取消场景过期定时器
     */
    public void cancelScheduleExpire() {
        scheduleExecutor.cancelSchedule(getId(), GameConstants.KUAFU_YUNGONG_STAGE_EXPIRE_PRODUCE);
    }

    public StageScheduleExecutor getScheduleExecutor() {
        return scheduleExecutor;
    }

    /**
     * 获取占领旗子公会id
     * 
     * @return
     */
    public Long getOwnerGuildId() {
        return ownerGuildId;
    }

    public void setOwnerGuildId(Long ownerGuildId) {
        this.ownerGuildId = ownerGuildId;
    }

    /**
     * 获取占领旗子玩家
     * 
     * @return
     */
    public IRole getOwnerRole() {
        return ownerRole;
    }

    public void setOwnerRole(IRole ownerRole) {
        this.ownerRole = ownerRole;
    }

    /**
     * 获取占领旗子玩家获得的buff
     * 
     * @return
     */
    public IBuff getOwnerRoleBuff() {
        return ownerRoleBuff;
    }

    public void setOwnerRoleBuff(IBuff ownerRoleBuff) {
        this.ownerRoleBuff = ownerRoleBuff;
    }

    /**
     * 获取占领旗子开始时间
     * 
     * @return
     */
    public Long getOwnerStartTime() {
        return ownerStartTime;
    }

    public void setOwnerStartTime(Long ownerStartTime) {
        this.ownerStartTime = ownerStartTime;
    }

    /**
     * 获取场景龙腾(怪物)
     * 
     * @return
     */
    public IStageElement getLongt() {
        return longt;
    }

    public void setLongt(IStageElement longt) {
        this.longt = longt;
    }

    /**
     * 记录每个采集旗子的玩家
     * 
     * @param userRoleId
     * @param qiziGuid
     */
    public void setCollectLog(Long userRoleId, Long qiziGuid) {
        this.roleCollectLog.put(userRoleId, qiziGuid);
    }

    /**
     * 清除所有旗子采集记录
     */
    public void clearCollectLog() {
        this.roleCollectLog.clear();
    }

    /**
     * 获取玩家采集的旗子记录
     * 
     * @param userRoleId
     * @return
     */
    public Long getCollectLog(Long userRoleId) {
        return this.roleCollectLog.get(userRoleId);
    }

    public void cancelTobeWinnerSchedule() {
        scheduleExecutor.cancelSchedule(getId(), GameConstants.KUAFU_YUNGONG_HAS_WINNER_PRODUCE);
    }

    /**
     * 开启守护旗子产生获胜公会定时器
     * 
     * @param delay
     */
    public void startTobeWinnerSchedule(int delay) {
        cancelTobeWinnerSchedule();
        StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.I_KUAFU_YUNGONG_HAS_WINNER, null);
        scheduleExecutor.schedule(getId(), GameConstants.KUAFU_YUNGONG_HAS_WINNER_PRODUCE, runable, delay, TimeUnit.SECONDS);
    }

    public void cancelSynQiziPositionSchedule() {
        scheduleExecutor.cancelSchedule(getId(), GameConstants.KUAFU_YUNGONG_SYN_QIZI_PRODUCE);
    }

    /**
     * 开启实时同步旗子移动坐标定时器(每隔1秒同步1次)
     */
    public void startSynQiziPositionSchedule() {
        cancelSynQiziPositionSchedule();
        StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.I_KUAFU_YUNGONG_SYN_QIZI, getId());
        scheduleExecutor.schedule(getId(), GameConstants.KUAFU_YUNGONG_SYN_QIZI_PRODUCE, runable, 1, TimeUnit.SECONDS);
    }

    public void canceClearSchedule() {
        scheduleExecutor.cancelSchedule(getId(), GameConstants.KUAFU_YUNGONG_EARLY_END_PRODUCE);
    }

    /**
     * 开启活动结束系统默认清人定时器
     */
    public void startClearSchedule() {
        canceClearSchedule();
        StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.I_KUAFU_YUNGONG_CLEAR, null);
        scheduleExecutor.schedule(getId(), GameConstants.KUAFU_YUNGONG_EARLY_END_PRODUCE, runable, GameConstants.EXPIRE_CHECK_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取旗子实时的坐标
     */
    public int[] getQiziPosition() {
        int[] xyPosition = new int[2];
        IRole role = this.getOwnerRole();
        if (null == role) {
            xyPosition = config.getZuobiao();
        } else {
            xyPosition[0] = role.getPosition().getX();
            xyPosition[1] = role.getPosition().getY();
            this.startSynQiziPositionSchedule();
        }
        return xyPosition;
    }

    /**
     * 
     * 获取玩家在场景中累计停留时长:单位秒
     * @param userRoleId
     * @return
     */
    public long getRoleTotalOnlineTime(Long userRoleId) {
        long totalOnlineTime = 0L;
        List<Object[]> onlineLogList = roleOnlineLogMap.get(userRoleId);
        int logSize = null == onlineLogList ? 0 : onlineLogList.size();
        if (logSize > 0) {
            long nowTime = GameSystemTime.getSystemMillTime();
            for (int idx = 0; idx < logSize; idx++) {
                Object[] onlineLog = onlineLogList.get(idx);
                if (null == onlineLog[0])
                    continue;
                if (null == onlineLog[1] && idx != logSize - 1)
                    continue;
                totalOnlineTime += ((null == onlineLog[1]) ? nowTime : (long) onlineLog[1] - (long) onlineLog[0]);
            }
        }
        return totalOnlineTime / 1000L;
    }
    
    @Override
    public void enter(IStageElement element, int x, int y) {
        super.enter(element, x, y);
        if (null != element && ElementType.isRole(element.getElementType())) {
            IRole role = (IRole) element;
            roleMaps.put(role.getId(), role);
            // 记录进入时间,便于统计到离开场景的在线时长
            List<Object[]> onlineLog = roleOnlineLogMap.get(role.getId());
            if (null == onlineLog) {
                onlineLog = new ArrayList<Object[]>();
            }
            onlineLog.add(new Object[] { GameSystemTime.getSystemMillTime(), null });
            roleOnlineLogMap.put(role.getId(), onlineLog);
            // 开启增加经验和真气的定时器
            role.startKfYunGongAddExpZqSchedule(config.getDelay());
        }
    }

    @Override
    public void leave(IStageElement element) {
        super.leave(element);
        if (null != element && ElementType.isRole(element.getElementType())) {
            IRole role = (IRole) element;
            Long roleId = role.getId();
            if (super.isOpen() && this.getOwnerRole() != null && this.getOwnerRole().getId().equals(roleId)) {
                // 扛旗者离开场景更新旗子占领状态
                StageMsgSender.send2StageInner(roleId, getId(), InnerCmdType.I_KUAFU_YUNGONG_OWNER_UPDATE, null);
            }
            // 记录离开时间,便于统计到离开场景的在线时长
            List<Object[]> onlineLog = roleOnlineLogMap.get(role.getId());
            if (!ObjectUtil.isEmpty(onlineLog)) {
                Object[] log = onlineLog.get(onlineLog.size() - 1);
                log[log.length - 1] = GameSystemTime.getSystemMillTime();
            }
            // 取消增加经验和真气的定时器
            role.cancelKfYunGongAddExpZqSchedule();
        }
    }

    @Override
    public void enterNotice(Long userRoleId) {
        long ownerTime = 0L;
        Long ownerStartTime = this.getOwnerStartTime();
        if (null != ownerStartTime) {
            ownerTime = GameSystemTime.getSystemMillTime() - ownerStartTime.longValue();// 距离拔起旗子相差的时间戳
            long maxTime = config.getNeedTime() * DatetimeUtil.SECOND_MILLISECOND;// 拔起旗子需要守住的最大时间戳
            ownerTime = ownerTime >= maxTime ? maxTime : ownerTime;
        }
        String ownerName = null;
        IRole ownerRole = this.getOwnerRole();
        if (null != ownerRole) {
            ownerName = ownerRole.getName();
        }
        BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_ENTER, new Object[] { AppErrorCode.SUCCESS, ownerTime, this.getQiziPosition(), ownerName });
    }

    @Override
    public void exitNotice(Long userRoleId) {
        StageMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_EXIT, AppErrorCode.OK);
    }

    @Override
    public boolean isFubenMonster() {
        return false;
    }

    @Override
    public void setFlag(boolean flag) {
        this.qiziFlag = flag;
    }

    @Override
    public boolean hasFlag() {
        return this.qiziFlag;
    }

    @Override
    public boolean isCanRemove() {
        return !isOpen() && (getAllRoleIds() == null || getAllRoleIds().length == 0);
    }
    
    @Override
    public Short getBackFuHuoCmd() {
        return InnerCmdType.I_KUAFU_YUNGONG_AUTO_FUHUO;
    }

    @Override
    public String getBackFuhuoConstants() {
        return GameConstants.KUAFU_YUNGONG_BACK_FUHUO_PRODUCE;
    }

  
    @Override
    public boolean canFuhuo() {
        return false;
    }
    

    @Override
    public boolean isCanPk() {
        return true;
    }
    

    @Override
    public boolean isAddPk() {
        return false;
    }

    
}