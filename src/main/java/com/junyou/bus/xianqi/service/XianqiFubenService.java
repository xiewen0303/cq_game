/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xianqi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.xianqi.dao.XianqiFubenDao;
import com.junyou.bus.xianqi.entity.XianqiFuben;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.YunYaoJingMaiPublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @Description 仙器副本(云瑶晶脉公共副本场景)业务处理类
 * @Author Yang Gao
 * @Since 2016-10-30
 * @Version 1.1.0
 */
@Service
public class XianqiFubenService {

    @Autowired
    private XianqiFubenDao xianqiFubenDao;
    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private DiTuConfigExportService diTuConfigExportService;
    @Autowired
    private StageControllExportService stageControllExportService;
    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;

    /**
     * 获取缓存中的玩家副本数据
     * 
     * @param userRoleId
     * @return
     */
    private XianqiFuben getCacheXianqiFuben(Long userRoleId) {
        XianqiFuben xianqiFuben = xianqiFubenDao.cacheLoad(userRoleId, userRoleId);
        if(null != xianqiFuben && !DatetimeUtil.dayIsToday(xianqiFuben.getUpdateTime())){
            xianqiFuben.setUseCount(0);
            xianqiFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
            xianqiFubenDao.cacheUpdate(xianqiFuben, userRoleId);
        }
        return xianqiFuben;
    }

    /**
     * 创建玩家副本数据
     * 
     * @param userRoleId
     * @return
     */
    private XianqiFuben createXianqiFuben(Long userRoleId) {
        XianqiFuben xianqiFuben = new XianqiFuben();
        xianqiFuben.setUserRoleId(userRoleId);
        xianqiFuben.setUseCount(0);
        xianqiFuben.setCreateTime(GameSystemTime.getSystemMillTime());
        xianqiFuben.setUpdateTime(xianqiFuben.getCreateTime());
        xianqiFubenDao.cacheInsert(xianqiFuben, userRoleId);
        return xianqiFuben;
    }

    /**
     * 获取仙器副本公共数据配置
     * 
     * @return
     */
    private YunYaoJingMaiPublicConfig getFubenPublicConfig() {
        return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YUNYAOJINGMAI);
    }

    /**
     * 判断活动是否正在进行中
     * 
     * @return true=进行中;false=未开始或已结束
     */
    private boolean isOpen() {
        YunYaoJingMaiPublicConfig publicConfig = getFubenPublicConfig();
        if (null == publicConfig) {
            ChuanQiLog.error("xianqi fuben (yunyaojingmai),activity config not found!");
            return false;
        }
        int[] starttimeArr = publicConfig.getStarttime();
        int[] endtimeArr = publicConfig.getEndtime();
        if (starttimeArr == null || endtimeArr == null) {
            ChuanQiLog.error("xianqi fuben (yunyaojingmai),activity time not config!");
            return false;
        }
        long curtime = GameSystemTime.getSystemMillTime();
        long starttime = DatetimeUtil.getTheDayTheTime(starttimeArr[0], starttimeArr[1], curtime);
        long endtime = DatetimeUtil.getTheDayTheTime(endtimeArr[0], endtimeArr[1], curtime);
        if (starttime <= 0 || endtime <= 0) {
            ChuanQiLog.error("xianqi fuben (yunyaojingmai),activity time config error!,starttime={},endtime={}", starttimeArr, endtimeArr);
            return false;
        }
        return starttime <= curtime && curtime < endtime;
    }

    /**
     * 获取在线玩家数据
     * 
     * @param userRoleId
     * @return
     */
    private RoleWrapper getRoleWrapper(Long userRoleId) {
        return roleExportService.getLoginRole(userRoleId);
    }

    /**
     * 初始化玩家仙器副本个人数据到缓存
     * 
     * @param userRoleId
     * @return
     */
    public List<XianqiFuben> initXianqiFubenData(Long userRoleId) {
        return xianqiFubenDao.initXianqiFuben(userRoleId);
    }

    /**
     * 请求进入副本
     * 
     * @param userRoleId
     * @param busMsgQueue
     * @return
     */
    public void enterFubenStage(Long userRoleId, BusMsgQueue busMsgQueue) {
        // 校验功能是否开启
        YunYaoJingMaiPublicConfig config = getFubenPublicConfig();
        if (null == config) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.XQFUBEN_ENTER, AppErrorCode.FUNCTION_NOT_OPEN);
            return;
        }
        // 校验副本是否开启
        if (!isOpen()) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.XQFUBEN_ENTER, AppErrorCode.XQFUBEN_NOT_OPEN);
            return;
        }
        // 校验副本等级限制
        Integer roleLevel = null;
        RoleWrapper role = getRoleWrapper(userRoleId);
        if (null != role) {
            roleLevel = role.getLevel();
        }
        if (null == roleLevel || roleLevel.intValue() < config.getNeedLevel()) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.XQFUBEN_ENTER, AppErrorCode.XQFUBEN_LEVEL_NOT_ENOUGH);
            return;
        }
        // 校验副本状态
        if (stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.XQFUBEN_ENTER, AppErrorCode.FUBEN_IS_IN_FUBEN);
            return;
        }
        XianqiFuben xianqiFuben = getCacheXianqiFuben(userRoleId);
        if (null == xianqiFuben) {
            xianqiFuben = createXianqiFuben(userRoleId);
        }
        DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config.getMap());
        int[] birthXy = dituCoinfig.getRandomBirth();
        Object[] applyEnterData = new Object[] { dituCoinfig.getId(), birthXy[0], birthXy[1], MapType.YUNYAOJINGMAI_MAP };
        busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
    }

    /**
     * 请求退出副本
     * 
     * @param userRoleId
     * @param busMsgQueue
     */
    public void exitFubenStage(Long userRoleId, BusMsgQueue busMsgQueue) {
        // 校验副本状态
        if (!stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.XQFUBEN_EXIT, AppErrorCode.FUBEN_NOT_IN_FUBEN);
            return;
        }
        busMsgQueue.addStageMsg(userRoleId, InnerCmdType.INNER_YYJM_EXIT_STAGE, null);
    }

    /**
     * 获取玩家剩余的开采矿石次数
     * 
     * @param userRoleId
     * @return
     */
    public int getRoleXianqiFubenRemainCount(Long userRoleId) {
        YunYaoJingMaiPublicConfig publicConfig = getFubenPublicConfig();
        if (null == publicConfig) {
            return 0;
        }
        XianqiFuben xianqiFuben = getCacheXianqiFuben(userRoleId);
        if (null == xianqiFuben) {
            return 0;
        }
        return publicConfig.getMaxtimes() - xianqiFuben.getUseCount();
    }

    /**
     * 消耗玩家仙器副本采矿次数
     * 
     * @param userRoleId
     */
    public void cutXianqiFubenCount(Long userRoleId) {
        XianqiFuben xianqiFuben = getCacheXianqiFuben(userRoleId);
        if (null == xianqiFuben) {
            return;
        }
        xianqiFuben.setUseCount(xianqiFuben.getUseCount() + 1);
        xianqiFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
        xianqiFubenDao.cacheUpdate(xianqiFuben, userRoleId);
    }

}
