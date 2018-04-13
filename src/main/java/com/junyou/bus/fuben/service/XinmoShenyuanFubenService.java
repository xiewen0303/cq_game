package com.junyou.bus.fuben.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.fuben.dao.XinmoShenyuanFubenDao;
import com.junyou.bus.fuben.entity.XinmoShenyuanFuben;
import com.junyou.bus.fuben.entity.XinmoShenyuanFubenConfig;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.XinmoShenyuanLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.XinmoShenyuanFubenPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.XinmoShenyuanFubenStage;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.RandomUtil;

/**
 * @Description 心魔深渊副本业务
 * @Author Yang Gao
 * @Since 2016-8-9
 * @Version 1.1.0
 */
@Service
public class XinmoShenyuanFubenService {

    @Autowired
    private XinmoShenyuanFubenDao xmShenyuanFubenDao;

    @Autowired
    private XinmoShenyuanFubenConfigService xinmoShenyuanFubenConfigService;

    @Autowired
    private RoleExportService roleExportService;
    
    @Autowired
    private BusScheduleExportService scheduleExportService;

    @Autowired
    private StageControllExportService stageControllExportService;

    @Autowired
    private DiTuConfigExportService diTuConfigExportService;

    @Autowired
    private RoleBagExportService roleBagExportService;

    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;

    /* 获取心魔深渊副本对象数据 */
    private XinmoShenyuanFuben getXmShenyuanFuben(Long userRoleId) {
        XinmoShenyuanFuben xmShenyuanFuben = xmShenyuanFubenDao.cacheLoad(userRoleId, userRoleId);
        if (null != xmShenyuanFuben && !DatetimeUtil.dayIsToday(xmShenyuanFuben.getUpdateTime())) {
            resetXmShenyuanFuben(xmShenyuanFuben);
        }
        return xmShenyuanFuben;
    }

    /*重置心魔深渊跨天数据*/
    private void resetXmShenyuanFuben(XinmoShenyuanFuben xmShenyuanFuben) {
        if(null == xmShenyuanFuben){
            return;
        }
        xmShenyuanFuben.setPassFubenId(0);
        xmShenyuanFuben.setFailBossType(0);
        xmShenyuanFuben.setCoolingTime(0L);
        xmShenyuanFuben.setCoolingStatus(GameConstants.XM_SHENYUAN_STATUS_COOLING_NO);
        xmShenyuanFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
        xmShenyuanFubenDao.cacheUpdate(xmShenyuanFuben, xmShenyuanFuben.getUserRoleId());
        cancelCutXmShenyuanFubenColingSchedule(xmShenyuanFuben.getUserRoleId());
    }
    
    /* 创建新的心魔深渊副本对象数据 */
    private XinmoShenyuanFuben createXmShenyuanFuben(Long userRoleId) {
        XinmoShenyuanFuben xmShenyuanFuben = new XinmoShenyuanFuben();
        xmShenyuanFuben.setUserRoleId(userRoleId);
        xmShenyuanFuben.setPassFubenId(0);
        xmShenyuanFuben.setFailBossType(0);
        xmShenyuanFuben.setCoolingTime(0L);
        long now_time = GameSystemTime.getSystemMillTime();
        xmShenyuanFuben.setCreateTime(now_time);
        xmShenyuanFuben.setUpdateTime(now_time);
        return (XinmoShenyuanFuben) xmShenyuanFubenDao.cacheInsert(xmShenyuanFuben, userRoleId);
    }

    /* 根据编号获取副本配置 */
    private XinmoShenyuanFubenConfig loadConfigById(Integer id) {
        return xinmoShenyuanFubenConfigService.loadById(id);
    }

    /* 获取副本最小编号 */
    private int getMaxFubenId() {
        return xinmoShenyuanFubenConfigService.getMaxFubenId();
    }

    /* 获取公共副本配置数据 */
    private XinmoShenyuanFubenPublicConfig loadPublicConfig() {
        return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_XINMO_SHENYUAN_FUBEN);
    }

    /**
     * 初始化心魔深渊副本数据库到缓存数据
     * 
     * @param userRoleId
     * @return
     */
    public List<XinmoShenyuanFuben> initXmShenyuanFubenCacheData(Long userRoleId) {
        return xmShenyuanFubenDao.initXinmoShenyuanFuben(userRoleId);
    }

    /**
     * 上线初始化处理心魔深渊副本冷却cd定时器
     * 
     * @param userRoleId
     */
    public void onlineHandle(Long userRoleId) {
        XinmoShenyuanFuben xinmoShenyuanFuben = getXmShenyuanFuben(userRoleId);
        if (null == xinmoShenyuanFuben) {
            return;
        }
        Long colingTime = xinmoShenyuanFuben.getCoolingTime();
        if (null == colingTime || colingTime.longValue() <= 0) {
            return;
        }
        long coling_time = colingTime.longValue();
        long now_time = GameSystemTime.getSystemMillTime();
        long delay = coling_time - now_time;
        if (delay > 0) {// 冷却时间未结束
            // 设置冷却状态
            xinmoShenyuanFuben.setCoolingStatus(GameConstants.XM_SHENYUAN_STATUS_COOLING_YES);
            xinmoShenyuanFuben.setUpdateTime(now_time);
            xmShenyuanFubenDao.cacheUpdate(xinmoShenyuanFuben, userRoleId);
            // 开启冷却定时器
            BusTokenRunable runable = new BusTokenRunable(userRoleId, InnerCmdType.XM_SHENYUAN_FUBEN_COLING, null);
            scheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPEONENT_XM_SHENYUAN_COOLING_PRODUCE, runable, (int) delay, TimeUnit.MILLISECONDS);
        } else {
            xinmoShenyuanFuben.setCoolingTime(0L);
            xinmoShenyuanFuben.setCoolingStatus(GameConstants.XM_SHENYUAN_STATUS_COOLING_NO);
            xinmoShenyuanFuben.setUpdateTime(now_time);
            xmShenyuanFubenDao.cacheUpdate(xinmoShenyuanFuben, userRoleId);
        }
    }

    /**
     * 下线取消心魔深渊副本冷却cd定时器
     * 
     * @param userRoleId
     * @param delay
     */
    public void cancelCutXmShenyuanFubenColingSchedule(Long userRoleId) {
        scheduleExportService.cancelSchedule(userRoleId.toString(), GameConstants.COMPEONENT_XM_SHENYUAN_COOLING_PRODUCE);
    }

    /**
     * 心魔深渊副本冷却cd结束处理
     * 
     * @param userRoleId
     */
    public void xmShenyuanFubenColingHandle(Long userRoleId) {
        XinmoShenyuanFuben xinmoShenyuanFuben = getXmShenyuanFuben(userRoleId);
        if (null == xinmoShenyuanFuben) {
            return;
        }
        xinmoShenyuanFuben.setCoolingTime(0L);
        xinmoShenyuanFuben.setCoolingStatus(GameConstants.XM_SHENYUAN_STATUS_COOLING_NO);
        xinmoShenyuanFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
        xmShenyuanFubenDao.cacheUpdate(xinmoShenyuanFuben, userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_SHENYUAN_SEND_CD_OVER, AppErrorCode.OK);
    }

    /**
     * 请求获取心魔深渊副本面板信息
     * 
     * @param userRoleId
     * @return
     */
    public Object getXmShenyuanFubenInfo(Long userRoleId) {
        XinmoShenyuanFuben xmShenyuanFuben = xmShenyuanFubenDao.cacheLoad(userRoleId, userRoleId);
        if (null == xmShenyuanFuben) {
            xmShenyuanFuben = createXmShenyuanFuben(userRoleId);
        } else if (!DatetimeUtil.dayIsToday(xmShenyuanFuben.getUpdateTime())) {
            /* 在当前副本中战斗,请求不响应.(防止跨天数据混乱) */
            String stageId = stageControllExportService.getCurStageId(userRoleId);
            IStage stage = StageManager.getStage(stageId);
            if (null != stage && XinmoShenyuanFubenStage.class.isInstance(stage)) {
                return null;
            } else {
                resetXmShenyuanFuben(xmShenyuanFuben);
            }
        }
        return new Object[] { xmShenyuanFuben.getPassFubenId(), xmShenyuanFuben.getCoolingTime() };
    }

    /**
     * 请求进入心魔深渊副本
     * 
     * @param userRoleId
     * @param fubenId 副本层次编号
     * @param busMsgQueue
     * @return
     */
    public void xmShenyuanFubenEnter(Long userRoleId, Integer fubenId, BusMsgQueue busMsgQueue) {
        short requestCmd = ClientCmdType.XM_SHENYUAN_ENTER;
        /* 请求参数错误 */
        if (null == fubenId) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.PARAMETER_ERROR);
            return;
        }
        /* 副本挑战系统尚未开启 */
        XinmoShenyuanFuben xinmoShenyuanFuben = getXmShenyuanFuben(userRoleId);
        if (null == xinmoShenyuanFuben) {
            return;
        }
        
        int fubenIdInt = fubenId.intValue();
        /* 副本最大难度 */
        int maxFubenId = getMaxFubenId();
        if (maxFubenId <= 0) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
            return;
        }
        if (fubenIdInt > maxFubenId) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.XM_SHENYUAN_FUBEN_FIGHT_OVER);
            return;
        }
        /* 副本挑战难度 */
        if (fubenIdInt != (xinmoShenyuanFuben.getPassFubenId() + 1)) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.XM_SHENYUAN_LEVEL_ERROR);
            return;
        }
        /* 在副本中 */
        if (stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.FUBEN_IS_IN_FUBEN);
            return;
        }
        /* 当前状态不可挑战副本 */
        if (GameConstants.FUBEN_STATE_READY != xinmoShenyuanFuben.getFubenStatus()) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.FUBEN_STATE_ERROR_TZ);
            return;
        }
        /* 副本冷却cd */
        if (GameConstants.XM_SHENYUAN_STATUS_COOLING_YES == xinmoShenyuanFuben.getCoolingStatus()) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.XM_SHENYUAN_FUBEN_COLING_RUN);
            return;
        }
        /* 副本配置不存在 */
        XinmoShenyuanFubenConfig xinmoShenyuanFubenConfig = loadConfigById(fubenIdInt);
        if (null == xinmoShenyuanFubenConfig) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
            return;
        }
        /* 更新副本数据 */
        Integer boss_type = xinmoShenyuanFuben.getFailBossType();
        Map<Integer, String> monsterMap = xinmoShenyuanFubenConfig.getMonsterMap();
        if (null == boss_type || !monsterMap.containsKey(boss_type.intValue())) {
            Set<Integer> keySet = monsterMap.keySet();
            if (null != keySet) {
                int len = keySet.size();
                boss_type = keySet.toArray(new Integer[len])[RandomUtil.getIntRandomValue(len)];
                xinmoShenyuanFuben.setFailBossType(boss_type);
            }
        }
        xinmoShenyuanFuben.setFubenStatus(GameConstants.FUBEN_STATE_FIGHT);
        xmShenyuanFubenDao.cacheUpdate(xinmoShenyuanFuben, userRoleId);
        /* 发送到场景进入地图 */
        DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(xinmoShenyuanFubenConfig.getMapId());
        int[] birthXy = dituCoinfig.getRandomBirth();
        Object[] applyEnterData = new Object[] { dituCoinfig.getId(), birthXy[0], birthXy[1], MapType.XINMO_SHENYUAN_FUBEN_MAP, fubenIdInt, boss_type };
        busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);

    }

    /**
     * 请求退出心魔深渊副本
     * 
     * @param userRoleId
     * @param busMsgQueue
     */
    public void exitXmShenyuanFuben(Long userRoleId, BusMsgQueue busMsgQueue) {
        if (!stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.XM_SHENYUAN_EXIT, AppErrorCode.FUBEN_NOT_IN_FUBEN);
            return;
        }
        /* 发送到场景内部处理:退出副本命令 */
        busMsgQueue.addStageMsg(userRoleId, InnerCmdType.S_EXIT_FUBEN, null);
    }

    /**
     * 心魔深渊副本挑战成功业务处理
     * 
     * @param userRoleId
     */
    public void innerXmShenyuanFubenFinish(Long userRoleId) {
        String stageId = stageControllExportService.getCurStageId(userRoleId);
        IStage stage = StageManager.getStage(stageId);
        if (null == stage || !XinmoShenyuanFubenStage.class.isInstance(stage)) {
            return;
        }
        // 更新数据
        XinmoShenyuanFuben xinmoShenyuanFuben = xmShenyuanFubenDao.cacheLoad(userRoleId, userRoleId);
        if (null == xinmoShenyuanFuben) {
            return;
        }
        xinmoShenyuanFuben.setFubenStatus(GameConstants.FUBEN_STATE_FINISH);
        // 副本场景
        XinmoShenyuanFubenStage xmShenyuanStage = (XinmoShenyuanFubenStage) stage;

        int passFubenId = xmShenyuanStage.getFubenId();
        xinmoShenyuanFuben.setPassFubenId(passFubenId);

        Integer boosType = xmShenyuanStage.getBossType();
        if (null != boosType && boosType.equals(xinmoShenyuanFuben.getFailBossType())) {
            xinmoShenyuanFuben.setFailBossType(0);
        }
        // 副本冷却数据
        long cooling_time = 0;
        long now_time = GameSystemTime.getSystemMillTime();
        if(passFubenId < getMaxFubenId()){// 今日所有挑战尚未完毕
            // 开启副本冷却cd定时器
            XinmoShenyuanFubenPublicConfig publicConfig = loadPublicConfig();
            if(null != publicConfig){
                long delay = publicConfig.getCd() * DatetimeUtil.SECOND_MILLISECOND;
                cooling_time = now_time + delay;
                xinmoShenyuanFuben.setCoolingStatus(GameConstants.XM_SHENYUAN_STATUS_COOLING_YES);
                xinmoShenyuanFuben.setCoolingTime(cooling_time);
                BusTokenRunable runable = new BusTokenRunable(userRoleId, InnerCmdType.XM_SHENYUAN_FUBEN_COLING, null);
                scheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPEONENT_XM_SHENYUAN_COOLING_PRODUCE, runable, (int) delay, TimeUnit.MILLISECONDS);
            }
        }
        // 更新数据
        if(!DatetimeUtil.dayIsToday(xinmoShenyuanFuben.getUpdateTime())) {
            resetXmShenyuanFuben(xinmoShenyuanFuben);//单独处理跨天业务
        }else{
            xinmoShenyuanFuben.setUpdateTime(now_time);
            xmShenyuanFubenDao.cacheUpdate(xinmoShenyuanFuben, userRoleId);
        }
        // 推送更新通关成功
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_SHENYUAN_FINISH, new Object[] { AppErrorCode.SUCCESS, passFubenId, cooling_time });
        // 发送通关奖励
        XinmoShenyuanFubenConfig config = loadConfigById(passFubenId);
        if (null == config) {
            return;
        }
        long zq = config.getZq();// 奖励真气
        long exp = config.getExp();// 奖励经验
        long money = config.getMoney();// 奖励银两
        Map<String, Integer> rewardMap = config.getItemMap();
        if (zq > 0) {
            roleBagExportService.incrNumberWithNotify(GoodsCategory.ZHENQI, zq, userRoleId, LogPrintHandle.GET_XINMO_SHENYUAN_FUBEN_GIFT, LogPrintHandle.GBZ_XINMO_SHENYUAN_FUBEN_GIFT);
        }
        if (money > 0) {
            roleBagExportService.incrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.GET_XINMO_SHENYUAN_FUBEN_GIFT, LogPrintHandle.GBZ_XINMO_SHENYUAN_FUBEN_GIFT);
        }
        if (exp > 0) {
            roleBagExportService.incrNumberWithNotify(GoodsCategory.EXP, exp, userRoleId, 0, 0);
        }
        if (!ObjectUtil.isEmpty(rewardMap)) {
            roleBagExportService.putInBagOrEmailWithNumber(rewardMap, userRoleId, GoodsSource.XINMO_SHENYUAN_FUBEN, LogPrintHandle.GET_XINMO_SHENYUAN_FUBEN_GIFT, LogPrintHandle.GBZ_XINMO_SHENYUAN_FUBEN_GIFT, true, EmailUtil.getCodeEmail(GameConstants.FUBEN_EMAIL_CONTENT_CODE));
        }
        // 记录副本日志
        RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
        GamePublishEvent.publishEvent(new XinmoShenyuanLogEvent(userRoleId, roleWrapper.getName(), passFubenId, LogPrintHandle.getLogGoodsParam(rewardMap, null), exp, money, zq));
        
    }

    /**
     * 心魔深渊副本成功退出离开处理
     * 
     * @param userRoleId
     */
    public void innerXmShenyuanFubenExit(Long userRoleId) {
        XinmoShenyuanFuben xinmoShenyuanFuben = getXmShenyuanFuben(userRoleId);
        if (null == xinmoShenyuanFuben) {
            return;
        }
        // 更新数据
        xinmoShenyuanFuben.setFubenStatus(GameConstants.FUBEN_STATE_READY);
        xmShenyuanFubenDao.cacheUpdate(xinmoShenyuanFuben, userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_SHENYUAN_EXIT, AppErrorCode.OK);
    }

    /**
     * 请求使用元宝清除副本冷却cd时间
     * 
     * @param userRoleId
     * @return
     */
    public Object[] clearXmShenyuanFubenCd(Long userRoleId) {
        XinmoShenyuanFuben xinmoShenyuanFuben = getXmShenyuanFuben(userRoleId);
        if (null == xinmoShenyuanFuben) {
            return null;
        }
        /* 副本冷却状态 */
        long now_time = GameSystemTime.getSystemMillTime();
        long cooling_time = xinmoShenyuanFuben.getCoolingTime() - now_time;
        if (GameConstants.XM_SHENYUAN_STATUS_COOLING_YES != xinmoShenyuanFuben.getCoolingStatus() || cooling_time <= 0) {
            return AppErrorCode.XM_SHENYUAN_FUBEN_COLING_OVER;
        }
        XinmoShenyuanFubenPublicConfig publicConfig = loadPublicConfig();
        if (null == publicConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        /* 扣除元宝 */
        long cd_time = publicConfig.getCdTime() * DatetimeUtil.SECOND_MILLISECOND;
        int needGold = publicConfig.getCdGold() * ((int) (cooling_time / cd_time) + (cooling_time % cd_time > 0 ? 1 : 0));
        if (needGold > 0) {
            Object[] result = roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, needGold, userRoleId, LogPrintHandle.CONSUME_XM_SHENYUAN_CLEAR_CD, true, LogPrintHandle.CBZ_XM_SHENYUAN_CLEAR_CD);
            if (result != null) {
                return result;
            } else {
                if (PlatformConstants.isQQ()) {
                    BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, needGold, LogPrintHandle.CBZ_XM_SHENYUAN_CLEAR_CD, QQXiaoFeiType.CONSUME_XM_SHENYUAN_CLEAR_CD, 1 });
                }
            }
        }
        // 取消冷却定时器
        cancelCutXmShenyuanFubenColingSchedule(userRoleId);
        /* 更新数据 */
        xinmoShenyuanFuben.setCoolingTime(0L);
        xinmoShenyuanFuben.setCoolingStatus(GameConstants.XM_SHENYUAN_STATUS_COOLING_NO);
        xinmoShenyuanFuben.setUpdateTime(now_time);
        xmShenyuanFubenDao.cacheUpdate(xinmoShenyuanFuben, userRoleId);
        return new Object[]{AppErrorCode.SUCCESS, 0};
    }

}
