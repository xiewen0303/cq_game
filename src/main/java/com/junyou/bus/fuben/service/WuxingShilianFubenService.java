package com.junyou.bus.fuben.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.fuben.dao.WuxingShilianFubenDao;
import com.junyou.bus.fuben.entity.WuxingShilianFuben;
import com.junyou.bus.fuben.entity.WuxingShilianFubenConfig;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.WuxingShilianFubenLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.WuXingShilianFubenPublicConfig;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IElementProduceTeam;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.WuxingFubenMonsterProduceTeam;
import com.junyou.stage.model.stage.fuben.WuxingShilianFubenStage;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 *@Description 五行试炼副本业务
 *@Author Yang Gao
 *@Since 2016-6-21
 *@Version 1.1.0
 */
@Service
public class WuxingShilianFubenService {
    @Autowired
    private WuxingShilianFubenDao wuxingShilianFubenDao;
    @Autowired
    private WuxingShilianFubenConfigService wuxingShilianFubenConfigService;
    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
    @Autowired
    private StageControllExportService stageControllExportService;
    @Autowired
    private DiTuConfigExportService diTuConfigExportService;
    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private RoleBagExportService roleBagExportService;

    /**
     * @Description 获取不为空的玩家五行试炼副本记录
     * @param userRoleId
     * @return
     */
    public WuxingShilianFuben getWxShilianFuben(Long userRoleId) {
        WuxingShilianFuben wuShilianFuben = wuxingShilianFubenDao.cacheLoad(userRoleId, userRoleId);
        if (null == wuShilianFuben) {
            wuShilianFuben = createWxShilianFuben(userRoleId);
        }else if (!DatetimeUtil.dayIsToday(wuShilianFuben.getUpdateTime())) {
            /* 此处可以处理跨天业务 */
            resertWxShilianFuben(wuShilianFuben);
        }
        return wuShilianFuben;
    }

    /**
     * @Description 创建五行试炼副本数据
     * @param userRoleId
     * @return
     */
    private WuxingShilianFuben createWxShilianFuben(Long userRoleId) {
        WuxingShilianFuben wuShilianFuben = new WuxingShilianFuben();
        wuShilianFuben.setUserRoleId(userRoleId);
        wuShilianFuben.setTodayFightNum(0);
        long nowTime = GameSystemTime.getSystemMillTime();
        wuShilianFuben.setCreateTime(nowTime);
        wuShilianFuben.setUpdateTime(nowTime);
        wuxingShilianFubenDao.cacheInsert(wuShilianFuben, userRoleId);
        return wuShilianFuben;
    }

    /**
     * @Description 隔天重置五行试炼副本数据
     * @param wxSkillFuben
     */
    private void resertWxShilianFuben(WuxingShilianFuben wuShilianFuben) {
        if (null == wuShilianFuben) {
            return;
        }
        wuShilianFuben.setTodayFightNum(0);
        wuShilianFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
        wuxingShilianFubenDao.cacheUpdate(wuShilianFuben, wuShilianFuben.getUserRoleId());
    }
    
    /**
     * 上线初始化数据到内存
     * @param userRoleId
     * @return
     */
    public List<WuxingShilianFuben> initWxShilianFubenData(Long userRoleId) {
        return wuxingShilianFubenDao.initWuxingShilianFuben(userRoleId);
    }
    
    /**
     * @Description 获取五行试炼副本信息
     * @param userRoleId
     * @return
     */
    public void getRoleWxShilianFubenInfo(Long userRoleId, BusMsgQueue busMsgQueue) {
        WuXingShilianFubenPublicConfig wxShilianFubenPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WUXING_SHILIAN);
        if (null == wxShilianFubenPublicConfig) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SHILIAN_FUBEN_ENTER, AppErrorCode.CONFIG_ERROR);
            return ;
        }
        WuxingShilianFuben wuShilianFuben = getWxShilianFuben(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.WUXING_SHILIAN_FUBEN_INFO, new Object[]{wxShilianFubenPublicConfig.getMaxFightNum() - wuShilianFuben.getTodayFightNum()});
    }

    /**
     * @Description 玩家进入五行试炼副本
     * @param userRoleId 玩家编号
     * @param layer 副本层次
     * @param busMsgQueue 业务消息分发载体
     */
    public void enterWxShilianFuben(Long userRoleId, BusMsgQueue busMsgQueue) {
        WuxingShilianFuben wuShilianFuben = getWxShilianFuben(userRoleId);
        /* 当前状态不可挑战副本 */
        if (GameConstants.FUBEN_STATE_READY != wuShilianFuben.getState()) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SHILIAN_FUBEN_ENTER, AppErrorCode.FUBEN_STATE_ERROR_TZ);
            return;
        }
        /* 在副本中 */
        if (stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SHILIAN_FUBEN_ENTER, AppErrorCode.FUBEN_IS_IN_FUBEN);
            return;
        }
        WuXingShilianFubenPublicConfig wxShilianFubenPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WUXING_SHILIAN);
        if (null == wxShilianFubenPublicConfig) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SHILIAN_FUBEN_ENTER, AppErrorCode.CONFIG_ERROR);
            return;
        }
        // 今日挑战次数不足
        int todayFightNum = wuShilianFuben.getTodayFightNum();
        if(todayFightNum >= wxShilianFubenPublicConfig.getMaxFightNum()){
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SHILIAN_FUBEN_ENTER, AppErrorCode.FUBEN_NO_COUNT);
            return;
        }
        
        /* 更新数据 */
        wuShilianFuben.setTodayFightNum(++todayFightNum);
        wuShilianFuben.setState(GameConstants.FUBEN_STATE_FIGHT);
        wuShilianFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
        wuxingShilianFubenDao.cacheUpdate(wuShilianFuben, userRoleId);

        /* 发送到场景进入地图 */
        DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(wxShilianFubenPublicConfig.getMapId());
        int[] birthXy = dituCoinfig.getRandomBirth();
        Object[] applyEnterData = new Object[] { dituCoinfig.getId(), birthXy[0], birthXy[1], MapType.WUXING_SHILIAN_FUBEN_MAP, GameConstants.HUNPO_FUBEN_ID};
        busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
    }

    /**
     * @Description 玩家请求离开五行试炼副本
     * @param userRoleId
     * @param busMsgQueue
     */
    public void exitWxShilianFuben(Long userRoleId, BusMsgQueue busMsgQueue) {
        if (!stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SHILIAN_FUBEN_EXIT, AppErrorCode.FUBEN_NOT_IN_FUBEN);
            return;
        }
        /* 发送到场景内部处理:退出副本命令 */
        busMsgQueue.addStageMsg(userRoleId, InnerCmdType.S_EXIT_FUBEN, null);
    }
    
    /**
     * @Description 内部处理客户端成功离开五行试炼副本场景后操作
     * @param userRoleId
     */
    public void innerExitWxShilianFuben(Long userRoleId) {
        WuxingShilianFuben wxShilianFuben = getWxShilianFuben(userRoleId);
        wxShilianFuben.setState(GameConstants.FUBEN_STATE_READY);
        wuxingShilianFubenDao.cacheUpdate(wxShilianFuben, userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.WUXING_SHILIAN_FUBEN_EXIT, AppErrorCode.OK);
    }
    
    /**
     * @Description 内部处理五行试炼副本通关完成指令
     * @param userRoleId
     */
    public void finishWxShilianFuben(Long userRoleId) {
        String stageId = stageControllExportService.getCurStageId(userRoleId);
        IStage iStage = StageManager.getStage(stageId);
        if (!WuxingShilianFubenStage.class.isInstance(iStage)) {
            return;
        }

        WuxingShilianFubenStage stage = (WuxingShilianFubenStage) iStage;
        if(null == stage){
            return;
        }
        /* 清除所有怪物 */
        stage.clearFubenMonster();
        /* 停止刷怪定时器 */
        Collection<IElementProduceTeam> produceTeams = stage.getStageProduceManager().getElementProduceTeams(ElementType.MONSTER);
        if (produceTeams != null) {
            for (IElementProduceTeam elementProduceTeam : produceTeams) {
                if (elementProduceTeam instanceof WuxingFubenMonsterProduceTeam) {
                    WuxingFubenMonsterProduceTeam produceTeam = (WuxingFubenMonsterProduceTeam) elementProduceTeam;
                    produceTeam.clearSchedule();
                }
            }
        }
        /* 开启强踢定时器 */
        stage.startForceClearSchedule();
        /* 怪物击杀数量 */
        int killMonsterNum = stage.getKillMonsterNum();
        /* 奖励描述 */
        String rewardStr = null;
        WuxingShilianFubenConfig config = wuxingShilianFubenConfigService.loadByConfig();
        /* key:怪物击杀数量;value:对应的击杀奖励 */
        Map<Integer, Map<String, Integer>> rewardMaps = config.getRewardMap();
        if (!ObjectUtil.isEmpty(rewardMaps)) {
            Map<Integer, Map<String, Integer>> treeMap = new TreeMap<Integer, Map<String, Integer>>(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o2.compareTo(o1);// 降序排列
                }
            });
            treeMap.putAll(rewardMaps);
            /* 奖励对应的怪物击杀数量 */
            Integer rewardKey = null;
            for (Integer k : treeMap.keySet()) {
                if (null != k && killMonsterNum >= k.intValue()) {
                    rewardKey = k;
                    break;
                }
            }
            /* 奖励集合 */
            Map<String, Integer> rewardMap = rewardKey == null ? null : treeMap.get(rewardKey);
            if (null != rewardMap) {
                /* 发送奖励 */
                sendReward(userRoleId, killMonsterNum, rewardMap);
                rewardStr = ObjectUtil.map2String(rewardMap);
            }
        }
        BusMsgSender.send2One(userRoleId, ClientCmdType.WUXING_SHILIAN_FUBEN_RECEIVE, new Object[] { rewardStr, killMonsterNum });
    }

    /**
     * 发送奖励和日志记录
     * @param userRoleId
     * @param rewardMap
     */
    private void sendReward(long userRoleId, int killNum, Map<String, Integer> rewardMap) {
        /* 发送奖励 */
        roleBagExportService.putInBagOrEmailWithNumber(rewardMap, userRoleId, GoodsSource.WUXING_SHILIAN_FUBEN, LogPrintHandle.GET_WUXING_SHILIAN_FUBEN_GIFT, LogPrintHandle.GBZ_WUXING_SHILIAN_FUBEN_GIFT, true, EmailUtil.getCodeEmail(GameConstants.FUBEN_EMAIL_CONTENT_CODE));
        /* 日志记录 */
        RoleWrapper role = roleExportService.getLoginRole(userRoleId);
        GamePublishEvent.publishEvent(new WuxingShilianFubenLogEvent(userRoleId, role.getName(), killNum, LogPrintHandle.getLogGoodsParam(rewardMap, null)));
    }


}
