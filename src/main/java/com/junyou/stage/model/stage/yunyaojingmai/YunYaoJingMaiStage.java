package com.junyou.stage.model.stage.yunyaojingmai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.junyou.bus.xianqi.export.XianqiFubenServiceExport;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.publicconfig.configure.export.YunYaoJingMaiPublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.stage.utils.StageHelper;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 
 * @Description 云瑶晶脉场景
 * @Author Yang Gao
 * @Since 2016-11-2
 * @Version 1.1.0
 */
public class YunYaoJingMaiStage extends PublicFubenStage {

    /**
     * 矿石采集记录<br/>
     * key:userRoleId采集玩家ID<br/>
     * value:collectId采集物ID<br/>
     */
    private Map<Long, Long> roleCollectLog;

    /**
     * 采集奖励记录<br/>
     * key:userRoleId采集玩家ID<br/>
     * value:<br/>
     * {<br/>
     * key:奖励道具ID<br/>
     * value:奖励道具数量<br/>
     * }<br/>
     */
    private Map<Long, Map<String, Integer>> collectRewardLog;
    /** 锁对象 **/
    private ReentrantLock lock;
    // 场景定时器
    private StageScheduleExecutor scheduleExecutor;
    /**
     * 场景配置数据
     */
    private YunYaoJingMaiPublicConfig publicConfig;
    /**
     * 仙器副本-云瑶晶脉业务对外访问类
     */
    private XianqiFubenServiceExport xianqiFubenServerExport;

    public YunYaoJingMaiStage(String id, Integer mapId, Integer lineNo, AOIManager aoiManager, PathInfoConfig pathInfoConfig, YunYaoJingMaiPublicConfig publicConfig) {
        super(id, mapId, lineNo, aoiManager, pathInfoConfig, StageType.YYJM_STAGE);
        this.publicConfig = publicConfig;
        this.initHandler();
        this.startForceKickSchedule();
    }

    private void initHandler() {
        this.lock = new ReentrantLock();
        this.roleCollectLog = new ConcurrentHashMap<Long, Long>();
        this.collectRewardLog = new ConcurrentHashMap<Long, Map<String, Integer>>();
        this.scheduleExecutor = new StageScheduleExecutor(getId());
        this.xianqiFubenServerExport = StageHelper.getXianqiFubenServiceExport();
    }

    private void startForceKickSchedule() {
        Long delay = getEndTimeStamp();
        if (null == delay || delay.longValue() <= 0) {
            delay = 0L;
            ChuanQiLog.error("yunyaojingmai stage get end timestamp error, stage over!!!");
        }
        scheduleExecutor.cancelSchedule(getId(), GameConstants.COMPONENT_YUNYAOJINGMAI_END_PRODUCE);
        StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.INNER_YYJM_FORCE_KICK, null);
        scheduleExecutor.schedule(getId(), GameConstants.COMPONENT_YUNYAOJINGMAI_END_PRODUCE, runable, delay, TimeUnit.MILLISECONDS);
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
     * 获取离副本结束的时间戳
     * 
     * @return
     */
    private Long getEndTimeStamp() {
        if (null == this.publicConfig) {
            return null;
        }
        int[] endtimeArr = this.publicConfig.getEndtime();
        if (null == endtimeArr) {
            ChuanQiLog.error("yunyaojingmai public config end time not found!");
            return null;
        }
        long curtime = GameSystemTime.getSystemMillTime();
        long endtime = DatetimeUtil.getTheDayTheTime(endtimeArr[0], endtimeArr[1], curtime);
        return (curtime < endtime) ? endtime - curtime : 0;
    }

    /**
     * 获取公共场景配置数据
     * 
     * @return
     */
    public YunYaoJingMaiPublicConfig getPublicConfig() {
        return publicConfig;
    }

    /**
     * 获取玩家采矿记录
     * 
     * @param userRoleId
     * @return
     */
    public Long getRoleCollectLog(Long userRoleId) {
        return this.roleCollectLog.get(userRoleId);
    }

    /**
     * 记录每个采矿玩家信息
     * 
     * @param userRoleId
     * @param collectId
     */
    public void saveRoleCollectLog(Long userRoleId, Long collectId) {
        this.roleCollectLog.put(userRoleId, collectId);
    }

    /**
     * 清除所有采矿记录
     */
    public void clearRoleCollectLog(Long collectId) {
        for(Map.Entry<Long, Long> entry : roleCollectLog.entrySet()){
            if(entry.getValue().equals(collectId)){
                roleCollectLog.remove(entry.getKey());
            }
        }
    }

    /**
     * 获取玩家采矿的奖励记录
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getRoleCollectMapLog(Long userRoleId) {
        Map<String, Integer> rewardMap = this.collectRewardLog.get(userRoleId);
        if (ObjectUtil.isEmpty(rewardMap)) {
            return null;
        }
        List<Object[]> list = new ArrayList<Object[]>();
        for (Map.Entry<String, Integer> entry : rewardMap.entrySet()) {
            list.add(new Object[] { entry.getKey(), entry.getValue() });
        }
        return list.toArray();
    }

    /**
     * 保存玩家采矿的奖励记录
     * 
     * @param userRoleId
     * @param reward
     */
    public void saveCollectRewardLog(Long userRoleId, Map<String, Integer> reward) {
        if (ObjectUtil.isEmpty(reward)) {
            return;
        }
        Map<String, Integer> rewardMap = this.collectRewardLog.get(userRoleId);
        if (null == rewardMap) {
            rewardMap = new HashMap<String, Integer>();
        }
        ObjectUtil.mapAdd(rewardMap, reward);
        this.collectRewardLog.put(userRoleId, rewardMap);
    }

    /**
     * 获取客户端展示数据
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getClientShowData(Long userRoleId) {
        return new Object[] { AppErrorCode.SUCCESS, xianqiFubenServerExport.getRoleXianqiFubenRemainCount(userRoleId), getRoleCollectMapLog(userRoleId) };
    }

    @Override
    public void enterNotice(Long userRoleId) {
        StageMsgSender.send2One(userRoleId, ClientCmdType.XQFUBEN_ENTER, getClientShowData(userRoleId));
    }

    @Override
    public void exitNotice(Long userRoleId) {
        StageMsgSender.send2One(userRoleId, ClientCmdType.XQFUBEN_EXIT, AppErrorCode.OK);
    }

    @Override
    public boolean isAddPk() {
        return true;
    }

    @Override
    public boolean isCanPk() {
        return true;
    }

    @Override
    public boolean isFubenMonster() {
        return false;
    }

    @Override
    public boolean isCanRemove() {
        return !isOpen() && (getAllRoleIds() == null || getAllRoleIds().length == 0);
    }
}