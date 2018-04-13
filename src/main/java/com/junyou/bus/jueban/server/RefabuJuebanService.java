/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.jueban.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.jueban.configure.RefabuJuebanConfig;
import com.junyou.bus.jueban.configure.RefabuJuebanConfigExportService;
import com.junyou.bus.jueban.configure.RefabuJuebanGroupConfig;
import com.junyou.bus.jueban.dao.RefabuJuebanDao;
import com.junyou.bus.jueban.entity.RefabuJueban;
import com.junyou.bus.jueban.filter.RefabuJuebanFilter;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * @Description 热发布-绝版礼包活动业务处理
 * @Author Yang Gao
 * @Since 2016-8-1
 * @Version 1.1.0
 */
@Service
public class RefabuJuebanService {

    @Autowired
    private RefabuJuebanDao refabuJuebanDao;
    @Autowired
    private RoleBagExportService roleBagExportService;

    /**
     * 根据子活动编号获取玩家活动数据(更新活动过期的数据)
     * 
     * @param userRoleId
     * @param subId
     * @return
     */
    private RefabuJueban getRefabuJueban(Long userRoleId, Integer subId) {
        RefabuJueban rfbJueban = null;
        List<RefabuJueban> list = refabuJuebanDao.cacheLoadAll(userRoleId, new RefabuJuebanFilter(subId));
        long nowTime = GameSystemTime.getSystemMillTime();
        if (list == null || list.size() <= 0) {
            rfbJueban = new RefabuJueban();
            rfbJueban.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
            rfbJueban.setSubId(subId);
            rfbJueban.setUserRoleId(userRoleId);
            rfbJueban.setActivityStatus(0);
            rfbJueban.setCreateTime(nowTime);
            rfbJueban.setUpdateTime(nowTime);
            refabuJuebanDao.cacheInsert(rfbJueban, userRoleId);
        } else {
            rfbJueban = list.get(0);
            ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
            if (configSong != null) {
                long oldActUpdateTime = rfbJueban.getUpdateTime();
                long actStartTime = configSong.getStartTimeByMillSecond();
                if (oldActUpdateTime < actStartTime && actStartTime < nowTime) {// 活动过期处理
                    rfbJueban.setActivityStatus(0);
                    rfbJueban.setUpdateTime(nowTime);
                    refabuJuebanDao.cacheUpdate(rfbJueban, userRoleId);
                }
            }
        }
        return rfbJueban;
    }

    /**
     * 初始化到内存数据
     * 
     * @param userRoleId
     * @return
     */
    public List<RefabuJueban> initRefabuJueban(Long userRoleId) {
        return refabuJuebanDao.initRefabuJueban(userRoleId);
    }

    /**
     * 10021请求获取子活动信息
     * 
     * @param userRoleId
     * @param subId
     * @return
     */
    public Object getRefabuInfo(Long userRoleId, Integer subId) {
        ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
        if (configSong == null || !configSong.isRunActivity()) {
            return null;
        }

        RefabuJuebanGroupConfig configGroup = RefabuJuebanConfigExportService.getInstance().loadBySubId(subId);
        if (null == configGroup) {
            return null;
        }
        List<Object> configList = new ArrayList<>();
        Map<Integer, RefabuJuebanConfig> configMap = configGroup.getConfigMap();
        if (!ObjectUtil.isEmpty(configMap)) {
            for (RefabuJuebanConfig config : configMap.values()) {
                configList.add(new Object[] { config.getId(), config.getRes(), config.getNeedGold(), config.getItemString() });
            }
        }
        return new Object[] { configList.toArray(), getRefabuJueban(userRoleId, subId).getActivityStatus() };
    }

    /**
     * 请求领取绝版礼包奖励
     * 
     * @param userRoleId
     * @param version 子活动版本号
     * @param subId 子活动编号
     * @param configId 礼包编号
     * @return
     */
    public Object[] receiveJueban(Long userRoleId, Integer version, Integer subId, Integer configId) {
        ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
        if (configSong == null) {
            return AppErrorCode.NO_SUB_ACTIVITY;
        }
        // 版本不一样
        if (configSong.getClientVersion() != version) {
            // 处理数据变化:
            Object newSubHandleData = getRefabuInfo(userRoleId, subId);
            Object[] data = new Object[] { subId, configSong.getClientVersion(), newSubHandleData };
            BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
            return null;
        }
        RefabuJuebanConfig config = RefabuJuebanConfigExportService.getInstance().loadBySubAndId(subId, configId);
        if (null == config) {
            return AppErrorCode.CONFIG_ERROR;
        }
        RefabuJueban rfbJueban = getRefabuJueban(userRoleId, subId);
        int status = rfbJueban.getActivityStatus();
        int index = configId - 1;
        // 领取状态校验
        if (!BitOperationUtil.calState(status, index)) {
            return AppErrorCode.GET_ALREADY;
        }
        // 元宝校验
        int needGold = config.getNeedGold();
        Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, needGold, userRoleId);
        if (null != goldError) {
            return AppErrorCode.YB_ERROR;
        }
        // 背包空间校验
        Map<String, Integer> itemMap = config.getItemMap();
        Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(itemMap, userRoleId);
        if (code != null) {
            return code;
        }
        // 扣除元宝
        if (needGold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, needGold, userRoleId, LogPrintHandle.CONSUME_REFABU_JUEBAN, true, LogPrintHandle.CBZ_REFABU_JUEBAN);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, needGold, LogPrintHandle.CONSUME_REFABU_JUEBAN, QQXiaoFeiType.CONSUME_REFABU_JUEBAN, 1 });
            }
        }
        // 更新玩家领取状态
        rfbJueban.setActivityStatus(BitOperationUtil.chanageState(status, index));
        rfbJueban.setUpdateTime(GameSystemTime.getSystemMillTime());
        refabuJuebanDao.cacheUpdate(rfbJueban, userRoleId);
        // 发送奖励
        roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.REFABU_JUEBAN, LogPrintHandle.GET_RFB_JUEBAN, LogPrintHandle.GBZ_RFB_JUEBAN, true);
        return new Object[] { AppErrorCode.SUCCESS, subId, configId };
    }

}
