/**
 * 
 */
package com.junyou.bus.firstChargeRebate.server;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.firstChargeRebate.configure.export.RefabuFirstChargeRebateConfig;
import com.junyou.bus.firstChargeRebate.configure.export.RefabuFirstChargeRebateConfigExportService;
import com.junyou.bus.firstChargeRebate.dao.RefabuFirstChargeRebateDao;
import com.junyou.bus.firstChargeRebate.entity.RefabuFirstChargeRebate;
import com.junyou.bus.firstChargeRebate.filter.RefabuFirstChargeRebateFilter;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * @description
 * @author ZHONGDIAN
 * @created 2011-11-16上午10:29:07
 */
@Service
public class RefabuFirstChargeRebateService {

    @Autowired
    private RefabuFirstChargeRebateDao refabuFirstChargeRebateDao;
    @Autowired
    private RoleVipInfoExportService roleVipInfoExportService;
    @Autowired
    private AccountExportService accountExportService;

    /**
     * 获取玩家活动数据
     * 
     * @param userRoleId
     * @param subId
     * @return
     */
    private RefabuFirstChargeRebate getRefabuFirstChargeRebate(Long userRoleId, int subId) {
        RefabuFirstChargeRebate firstChargeRebate = null;
        List<RefabuFirstChargeRebate> list = refabuFirstChargeRebateDao.cacheLoadAll(userRoleId, new RefabuFirstChargeRebateFilter(subId));
        long nowTime = GameSystemTime.getSystemMillTime();
        if (list == null || list.size() <= 0) {
            firstChargeRebate = new RefabuFirstChargeRebate();
            firstChargeRebate.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
            firstChargeRebate.setSubId(subId);
            firstChargeRebate.setUserRoleId(userRoleId);
            firstChargeRebate.setActivityStatus(GameConstants.ACTIVITY_STATUS_NO_FINISH);
            firstChargeRebate.setFirstGold(0);
            firstChargeRebate.setRebateRatio(0F);
            firstChargeRebate.setRebateGold(0);
            firstChargeRebate.setCreateTime(nowTime);
            firstChargeRebate.setUpdateTime(nowTime);
            refabuFirstChargeRebateDao.cacheInsert(firstChargeRebate, userRoleId);
        } else {
            firstChargeRebate = list.get(0);
            ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
            if (configSong != null) {
                long oldActUpdateTime = firstChargeRebate.getUpdateTime();
                long actStartTime = configSong.getStartTimeByMillSecond();
                if (oldActUpdateTime < actStartTime && actStartTime < nowTime) {// 子活动id相同,重新开始一个新的已存在的子活动,重置玩家活动数据
                    firstChargeRebate.setActivityStatus(GameConstants.ACTIVITY_STATUS_NO_FINISH);
                    firstChargeRebate.setFirstGold(0);
                    firstChargeRebate.setRebateRatio(0F);
                    firstChargeRebate.setRebateGold(0);
                    firstChargeRebate.setUpdateTime(nowTime);
                    refabuFirstChargeRebateDao.cacheUpdate(firstChargeRebate, userRoleId);
                }
            }
        }
        return firstChargeRebate;
    }

    // ------------------------------------------------------------------------------------//

    // 初始化数据
    public List<RefabuFirstChargeRebate> initRefabuFirstChargeRebate(Long userRoleId) {
        return refabuFirstChargeRebateDao.initRefabuFirstChargeRebate(userRoleId);
    }

    /**
     * 判断玩家是否可以参与子活动
     * 
     * @param userRoleId 玩家编号
     * @param subId 子活动编号
     * @return true=可以参与;false=不能参与
     */
    public boolean isIntoActivity(Long userRoleId, Integer subId) {
        RoleVipWrapper vip = roleVipInfoExportService.getRoleVipInfo(userRoleId);
        if (vip == null || (vip.getVipLevel() == 0 && vip.getVipExp() == 0)) {// 没有充值过
            return true;
        }
        // 充值过
        List<RefabuFirstChargeRebate> list = refabuFirstChargeRebateDao.cacheLoadAll(userRoleId, new RefabuFirstChargeRebateFilter(subId));
        if(!ObjectUtil.isEmpty(list)){
            RefabuFirstChargeRebate firstChargeRebate = list.get(0);
            ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
            if (configSong != null && configSong.isRunActivity()) {
                long updateTime = firstChargeRebate.getUpdateTime();
                long actStartTime = configSong.getStartTimeByMillSecond();
                long actEndTime = configSong.getEndTimeByMillSecond();
                if (actStartTime < updateTime && updateTime < actEndTime && GameConstants.ACTIVITY_STATUS_RECEIVE != firstChargeRebate.getActivityStatus()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取某个子活动的热发布某个活动信息
     * 
     * @param userRoleId
     * @param subId
     * @return
     */
    public Object[] getRefbInfo(Long userRoleId, Integer subId) {
        ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
        if (configSong == null || !configSong.isRunActivity()) {
            return null;
        }

        RefabuFirstChargeRebateConfig config = RefabuFirstChargeRebateConfigExportService.getInstance().loadBySubId(subId);
        if (config == null) {
            return null;
        }

        int rebateGold = 0;
        float rebateRatio = config.getRebateRatio();
        RefabuFirstChargeRebate roleFirstCharge = getRefabuFirstChargeRebate(userRoleId, subId);
        if (GameConstants.ACTIVITY_STATUS_YES_FINISH == roleFirstCharge.getActivityStatus()) {
            rebateRatio = roleFirstCharge.getRebateRatio();
            rebateGold = roleFirstCharge.getRebateGold();
        }

        return new Object[] { config.getPic(), config.getDesc(), roleFirstCharge.getFirstGold(), rebateGold, Math.round(rebateRatio * Lottery.HUNDRED.getVal()) };
    }

    /**
     * 充值处理
     * 
     * @param userRoleId
     * @param addVal
     */
    public void firstChargeYb(Long userRoleId, Integer addVal) {
        if (addVal < 0) {
            return;
        }

        Map<Integer, RefabuFirstChargeRebateConfig> configMap = RefabuFirstChargeRebateConfigExportService.getInstance().getAllConfig();
        if (ObjectUtil.isEmpty(configMap)) {
            return;
        }

        for (Integer subId : configMap.keySet()) {
            // 是否在有这个活动或者是否在时间内
            ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
            if (configSong == null || !configSong.isRunActivity()) {
                continue;
            }
            if(!isIntoActivity(userRoleId, subId)){
                continue;
            }
            RefabuFirstChargeRebateConfig config = RefabuFirstChargeRebateConfigExportService.getInstance().loadBySubId(subId);
            float rebateRatio = config.getRebateRatio();
            int rebateGold = Math.round(addVal * rebateRatio);
            // 处理数据
            RefabuFirstChargeRebate roleFirstCharge = getRefabuFirstChargeRebate(userRoleId, subId);
            roleFirstCharge.setActivityStatus(GameConstants.ACTIVITY_STATUS_YES_FINISH);
            roleFirstCharge.setFirstGold(addVal);
            roleFirstCharge.setRebateRatio(rebateRatio);
            roleFirstCharge.setRebateGold(rebateGold);
            roleFirstCharge.setUpdateTime(GameSystemTime.getSystemMillTime());
            refabuFirstChargeRebateDao.cacheUpdate(roleFirstCharge, userRoleId);
            break;
        }
    }
    
    // ------------------------------------------------------------------------------------//

    /**
     * 获取首冲返利活动面板信息
     * 
     * @param userRoleId
     * @param version
     * @param subId
     * @return
     */
    public Object[] firstChargeGetInfo(Long userRoleId, Integer version, Integer subId) {
        ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
        if (configSong == null) {
            return AppErrorCode.NO_SUB_ACTIVITY;
        }

        // 版本不一样
        if (configSong.getClientVersion() != version) {
            // 处理数据变化:
            Object newSubHandleData = getRefbInfo(userRoleId, subId);
            Object[] data = new Object[] { subId, configSong.getClientVersion(), newSubHandleData };
            BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
            return null;
        }

        int rebateGold = 0;
        RefabuFirstChargeRebate roleFirstCharge = getRefabuFirstChargeRebate(userRoleId, subId);
        if (GameConstants.ACTIVITY_STATUS_YES_FINISH == roleFirstCharge.getActivityStatus()) {
            rebateGold = roleFirstCharge.getRebateGold();
        }
        return new Object[] { subId, roleFirstCharge.getFirstGold(), rebateGold };
    }

   

    /**
     * 领取首冲返利奖励
     * 
     * @param userRoleId
     * @param version
     * @param subId
     * @return
     */
    public Object[] firstChargeReceive(Long userRoleId, Integer version, Integer subId) {
        ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
        if (configSong == null) {
            return AppErrorCode.NO_SUB_ACTIVITY;
        }

        // 版本不一样
        if (configSong.getClientVersion() != version) {
            // 处理数据变化:
            Object newSubHandleData = getRefbInfo(userRoleId, subId);
            Object[] data = new Object[] { subId, configSong.getClientVersion(), newSubHandleData };
            BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
            return null;
        }

        RefabuFirstChargeRebateConfig config = RefabuFirstChargeRebateConfigExportService.getInstance().loadBySubId(subId);
        if (config == null) {
            return AppErrorCode.CONFIG_ERROR;
        }

        RefabuFirstChargeRebate roleFirstCharge = getRefabuFirstChargeRebate(userRoleId, subId);
        if (GameConstants.ACTIVITY_STATUS_YES_FINISH != roleFirstCharge.getActivityStatus()) {
            return AppErrorCode.RFB_FIRST_CHARGE_REBATE_NOT_REWARD;
        }

        // 更新玩家领取状态
        roleFirstCharge.setActivityStatus(GameConstants.ACTIVITY_STATUS_RECEIVE);
        roleFirstCharge.setUpdateTime(GameSystemTime.getSystemMillTime());
        refabuFirstChargeRebateDao.cacheUpdate(roleFirstCharge, userRoleId);

        // 增加元宝
        int rebateGold = roleFirstCharge.getRebateGold();
        if (rebateGold > 0) {
            accountExportService.incrCurrencyWithNotify(GoodsCategory.GOLD, rebateGold, userRoleId, LogPrintHandle.GET_RFB_FIRST_REBATE, LogPrintHandle.GBZ_RFB_FIRST_REBATE);
        }

        return new Object[] { AppErrorCode.SUCCESS, subId };
    }

}