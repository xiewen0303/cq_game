package com.junyou.bus.fuben.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.dao.XinmoFubenDao;
import com.junyou.bus.fuben.entity.XinmoFuben;
import com.junyou.bus.fuben.entity.XinmoFubenConfig;
import com.junyou.bus.fuben.entity.XinmoFubenFuHuaConfig;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xinmo.export.XinmoExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.XinmoFubenPublicConfig;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.XinmoFubenStage;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.sync.annotation.Sync;

/**
 * @Description 心魔副本业务
 * @Author Yang Gao
 * @Since 2016-7-26
 * @Version 1.1.0
 */
@Service
public class XinmoFubenService {

    @Autowired
    private XinmoFubenDao xinmoFubenDao;

    @Autowired
    private XinmoFubenConfigService xinmoFubenConfigService;

    @Autowired
    private XinmoExportService xinmoExportService;

    @Autowired
    private BusScheduleExportService scheduleExportService;

    @Autowired
    private StageControllExportService stageControllExportService;

    @Autowired
    private DiTuConfigExportService diTuConfigExportService;

    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;

    /**
     * 获取心魔副本数据对象
     * 
     * @param userRoleId
     * @param isNew 是否构建一个新的对象
     * @return
     */
    private XinmoFuben getXinmoFuben(Long userRoleId) {
        return xinmoFubenDao.cacheLoad(userRoleId, userRoleId);
    }

    /**
     * 创建新的心魔副本数据对象
     * 
     * @param userRoleId
     * @return
     */
    public XinmoFuben createXinmoFuben(Long userRoleId) {
        XinmoFuben xmFuben = new XinmoFuben();
        long now_time = GameSystemTime.getSystemMillTime();
        xmFuben.setUserRoleId(userRoleId);
        xmFuben.setFuhuaVal(0);
        xmFuben.setRevertTime(0L);
        xmFuben.setCreateTime(now_time);
        xmFuben.setUpdateTime(now_time);
        xinmoFubenDao.cacheInsert(xmFuben, userRoleId);
        return xmFuben;
    }

    /**
     * 获取心魔腐化配置数据
     * 
     * @param userRoleId
     * @return
     */
    private XinmoFubenFuHuaConfig getXinmoFuHuaConfig(Long userRoleId) {
        int roleXinmoRank = xinmoExportService.getRoleXinmoRank(userRoleId);
        return xinmoFubenConfigService.loadFuHuaConfigById(roleXinmoRank);
    }

    /**
     * 获取心魔副本配置数据
     * 
     * @param userRoleId
     * @return
     */
    private XinmoFubenConfig getXinmoFubenConfig(Integer xinmoId) {
        return xinmoFubenConfigService.loadFubenConfigById(xinmoId);
    }

    /**
     * 同步操作,减少心魔副本 腐化度
     * 
     * @param userRoleId
     * @param cutVal
     * @return
     */
    @Sync(component = GameConstants.COMPONENT_XM_FUBEN_SHARE, indexes = { 0 })
    private Object[] cutXmFubenFuHua(Long userRoleId, int cutVal) {
        if (cutVal <= 0) {
            return new Object[] { AppErrorCode.FAIL };
        }
        XinmoFuben xmFuben = getXinmoFuben(userRoleId);
        if (null == xmFuben) {
            return new Object[] { AppErrorCode.FAIL };
        }
        long now_time = GameSystemTime.getSystemMillTime();
        xmFuben.setFuhuaVal(xmFuben.getFuhuaVal() < cutVal ? 0 : (xmFuben.getFuhuaVal() - cutVal));
        xmFuben.setRevertTime(now_time);
        xmFuben.setUpdateTime(now_time);
        xinmoFubenDao.cacheUpdate(xmFuben, userRoleId);
        return new Object[] { AppErrorCode.SUCCESS, xmFuben.getFuhuaVal() };
    }

    /**
     * 同步操作,增加心魔副本腐化度
     * 
     * @param userRoleId
     * @param addVal
     * @return
     */
    @Sync(component = GameConstants.COMPONENT_XM_FUBEN_SHARE, indexes = { 0 })
    private Object[] addXmFubenFuHua(Long userRoleId, int addVal) {
        if (addVal <= 0) {
            return new Object[] { AppErrorCode.FAIL };
        }
        XinmoFuben xmFuben = getXinmoFuben(userRoleId);
        if (null == xmFuben) {
            return new Object[] { AppErrorCode.FAIL };
        }
        long now_time = GameSystemTime.getSystemMillTime();
        xmFuben.setFuhuaVal(xmFuben.getFuhuaVal() + addVal);
        xmFuben.setRevertTime(now_time);
        xmFuben.setUpdateTime(now_time);
        xinmoFubenDao.cacheUpdate(xmFuben, userRoleId);
        return new Object[] { AppErrorCode.SUCCESS, xmFuben.getFuhuaVal() };
    }

    /**
     * 初始化业务数据到缓存
     * 
     * @return
     */
    public List<XinmoFuben> initXinmoFubenData(Long userRoleId) {
        return xinmoFubenDao.initXinmoFuben(userRoleId);
    }

    /**
     * 上线初始化处理腐化度减少定时器
     * 
     * @param userRoleId
     */
    public void onlineHandle(Long userRoleId) {
        XinmoFuben xmFuben = getXinmoFuben(userRoleId);
        if (null == xmFuben) {
            return;
        }
        if(xmFuben.getFuhuaVal() <= 0){
            return;
        }
        XinmoFubenFuHuaConfig xmFuhuaConfig = getXinmoFuHuaConfig(userRoleId);
        if (null == xmFuhuaConfig) {
            return;
        }
        long refresh_time = xmFuhuaConfig.getTiming() * DatetimeUtil.SECOND_MILLISECOND;
        long delay = refresh_time;
        if (refresh_time > 0 && xmFuben.getRevertTime() > 0) {
            long diff_time = GameSystemTime.getSystemMillTime() - xmFuben.getRevertTime();
            int cut_num = (int) (diff_time / refresh_time);
            cutXmFubenFuHua(userRoleId, cut_num * xmFuhuaConfig.getFuhuaVal());
            delay -= (diff_time % refresh_time < 0 ? 0 : diff_time % refresh_time);
        }
        xmFuben.setFuhuaRunnableState(GameConstants.XM_FUHUA_RUNNABLE_STATE_RUN);
        xinmoFubenDao.cacheUpdate(xmFuben, userRoleId);
        BusTokenRunable runable = new BusTokenRunable(userRoleId, InnerCmdType.XM_FUBEN_CUT_FUHUA, null);
        scheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPEONENT_XM_FUHUA_PRODUCE, runable, (int) delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 下线取消心魔副本腐化度减少的定时器
     * 
     * @param userRoleId
     * @param delay
     */
    public void cancelCutXmFuHuaSchedule(Long userRoleId) {
        scheduleExportService.cancelSchedule(userRoleId.toString(), GameConstants.COMPEONENT_XM_FUHUA_PRODUCE);
    }

    /**
     * 预开启心魔副本腐化度减少的定时器
     * 
     * @param userRoleId
     */
    public void startCutxmFubenFuHuaSchedule(Long userRoleId) {
        XinmoFuben xmFuben = getXinmoFuben(userRoleId);
        if (null == xmFuben) {
            return;
        }
        if(xmFuben.getFuhuaRunnableState() == GameConstants.XM_FUHUA_RUNNABLE_STATE_RUN){
            return;
        }
        if(xmFuben.getFuhuaVal() <= 0){            
            return;
        }
        XinmoFubenFuHuaConfig xmFuhuaConfig = getXinmoFuHuaConfig(userRoleId);
        if (null == xmFuhuaConfig) {
            return;
        }
        xmFuben.setFuhuaRunnableState(GameConstants.XM_FUHUA_RUNNABLE_STATE_RUN);
        xinmoFubenDao.cacheUpdate(xmFuben, userRoleId);
        BusTokenRunable runable = new BusTokenRunable(userRoleId, InnerCmdType.XM_FUBEN_CUT_FUHUA, null);
        scheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPEONENT_XM_FUHUA_PRODUCE, runable, (int) (xmFuhuaConfig.getTiming() * DatetimeUtil.SECOND_MILLISECOND), TimeUnit.MILLISECONDS);
    }

    /**
     * 心魔副本腐化度减少业务处理
     * 
     * @param userRoleId
     */
    public void xmFubenFuHuaCutHandle(Long userRoleId) {
        XinmoFuben xmFuben = getXinmoFuben(userRoleId);
        if(null == xmFuben){
            return;
        }
        xmFuben.setFuhuaRunnableState(GameConstants.XM_FUHUA_RUNNABLE_STATE_OVER);
        xinmoFubenDao.cacheUpdate(xmFuben, userRoleId);
        XinmoFubenFuHuaConfig xmFuhuaConfig = getXinmoFuHuaConfig(userRoleId);
        if (null == xmFuhuaConfig) {
            return;
        }
        Object[] rsObj = cutXmFubenFuHua(userRoleId, xmFuhuaConfig.getFuhuaVal());
        if (AppErrorCode.SUCCESS == (int) rsObj[0]) {
            startCutxmFubenFuHuaSchedule(userRoleId);
            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_FUHUA_REFRESH, rsObj[1]);
        }
    }

    /**
     * 获取心魔副本信息
     * 
     * @param userRoleId
     * @return
     */
    public Object getXmFubenInfo(Long userRoleId) {
        XinmoFuben xmFuben = getXinmoFuben(userRoleId);
        if (null == xmFuben) {
            xmFuben = createXinmoFuben(userRoleId);
        }
        return xmFuben.getFuhuaVal();
    }

    /**
     * 请求进入心魔副本
     * 
     * @param userRoleId
     * @param xinmoId
     * @param busMsgQueue
     */
    public void enterXmFuben(Long userRoleId, Integer xinmoId, BusMsgQueue busMsgQueue) {
        short requestCmd = ClientCmdType.XM_FUBEN_ENTER;
        /* 请求参数错误 */
        if (null == xinmoId) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.PARAMETER_ERROR);
            return;
        }
        /* 副本配置不存在 */
        XinmoFubenConfig xinmoFubenConfig = getXinmoFubenConfig(xinmoId);
        if (null == xinmoFubenConfig) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
            return;
        }
        XinmoFuben xmFuben = getXinmoFuben(userRoleId);
        /* 副本挑战系统尚未开启 */
        if (null == xmFuben) {
            return;
        }
        /* 在副本中 */
        if (stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.FUBEN_IS_IN_FUBEN);
            return;
        }
        /* 当前状态不可挑战副本 */
        if (GameConstants.FUBEN_STATE_READY != xmFuben.getState()) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.FUBEN_STATE_ERROR_TZ);
            return;
        }
        /* 检验副本腐化度是否已满 */
        XinmoFubenPublicConfig xmFubenPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_XINMO_FUBEN);
        if (null == xmFubenPublicConfig) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
            return;
        }
        if (xmFuben.getFuhuaVal() >= xmFubenPublicConfig.getMaxFuhuaVal()) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.XM_FUBEN_MAX_FUHUA);
            return;
        }
        /* 更新副本数据 */
        xmFuben.setState(GameConstants.FUBEN_STATE_FIGHT);
        xinmoFubenDao.cacheUpdate(xmFuben, userRoleId);
        /* 发送到场景进入地图 */
        DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(xinmoFubenConfig.getMapId());
        int[] birthXy = dituCoinfig.getRandomBirth();
        Object[] applyEnterData = new Object[] { dituCoinfig.getId(), birthXy[0], birthXy[1], MapType.XINMO_FUBEN_MAP, xinmoFubenConfig.getId() };
        busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
    }

    public static void main(String[] args) {
		System.out.println(DatetimeUtil.formatTime(1478692585770L, DatetimeUtil.FORMART3));
		System.out.println(DatetimeUtil.formatTime(1479004440228L, DatetimeUtil.FORMART3));
	}
    
    /**
     * 心魔副本通关
     * 
     * @param userRoleId
     */
    public void innerXmFubenFinish(Long userRoleId) {
        String stageId = stageControllExportService.getCurStageId(userRoleId);
        IStage stage = StageManager.getStage(stageId);
        if (null == stage || !XinmoFubenStage.class.isInstance(stage)) {
            return;
        }
        // 更新副本状态
        XinmoFuben xmFuben = getXinmoFuben(userRoleId);
        if (null == xmFuben) {
            return;
        }
        xmFuben.setState(GameConstants.FUBEN_STATE_FINISH);
        xinmoFubenDao.cacheUpdate(xmFuben, userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_FUBEN_FINISH, AppErrorCode.OK);
        // 更新腐化度
        XinmoFubenStage xmStage = (XinmoFubenStage) stage;
        XinmoFubenConfig fubenConfig = getXinmoFubenConfig(xmStage.getFubenId());
        if (null == fubenConfig) {
            return;
        }
        Object[] rsObj = addXmFubenFuHua(userRoleId, fubenConfig.getFuhuaVal());
        if (AppErrorCode.SUCCESS == (int) rsObj[0]) {
            startCutxmFubenFuHuaSchedule(userRoleId);
            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_FUHUA_REFRESH, rsObj[1]);
        }
    }

    /**
     * 请求退出心魔副本
     * 
     * @param userRoleId
     * @param busMsgQueue
     */
    public void exitXmFuben(Long userRoleId, BusMsgQueue busMsgQueue) {
        if (!stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.XM_FUBEN_EXIT, AppErrorCode.FUBEN_NOT_IN_FUBEN);
            return;
        }
        /* 发送到场景内部处理:退出副本命令 */
        busMsgQueue.addStageMsg(userRoleId, InnerCmdType.S_EXIT_FUBEN, null);
    }
    
    /**
     * 成功退出心魔副本
     * @param userRoleId
     */
    public void innerXmFubenExit(Long userRoleId) {
        XinmoFuben xmFuben = getXinmoFuben(userRoleId);
        if (null == xmFuben) {
            return;
        }
        // 更新副本状态
        xmFuben.setState(GameConstants.FUBEN_STATE_READY);
        xinmoFubenDao.cacheUpdate(xmFuben, userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_FUBEN_EXIT, AppErrorCode.OK);
    }

 

}
