/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.mogonglieyan.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.mogonglieyan.configure.ZhuFuShangDianBiaoConfig;
import com.junyou.bus.mogonglieyan.configure.ZhuFuShangDianBiaoConfigExportService;
import com.junyou.bus.mogonglieyan.dao.RoleMogonglieyanDao;
import com.junyou.bus.mogonglieyan.entity.RoleMogonglieyan;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.MoGongLieYanLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.MoGongLieYanPublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.sync.annotation.Sync;

/**
 * @Description 魔宫猎焰业务处理
 * @Author Yang Gao
 * @Since 2016-10-19
 * @Version 1.1.0
 */
@Service
public class RoleMoGongLieYanService {
    @Autowired
    private RoleMogonglieyanDao roleMogonglieyanDao;
    @Autowired
    private DiTuConfigExportService diTuConfigExportService;
    @Autowired
    private StageControllExportService stageControllExportService;
    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private RoleBagExportService roleBagExportService;
    @Autowired
    private ZhuFuShangDianBiaoConfigExportService zhuFuShangDianBiaoConfigExportService;
    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;

    /**
     * 获取魔宫烈焰公共数据配置
     * 
     * @return
     */
    private MoGongLieYanPublicConfig getPublicConfig() {
        MoGongLieYanPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MOGONGLIEYAN);
        if (null == config) {
            ChuanQiLog.error("mogonglieyan public config is null");
        }
        return config;
    }

    /**
     * 获取初始化御魔值
     * 
     * @return
     */
    private int getInitYumoVal() {
        int initYumoVal = 0;
        MoGongLieYanPublicConfig config = getPublicConfig();
        if (null != config) {
            initYumoVal = config.getYumo();
        }
        return initYumoVal;
    }

    /**
     * 获取缓存中的魔宫猎焰对象
     * 
     * @param userRoleId
     * @return
     */
    public RoleMogonglieyan getCacheRoleMogonglieyan(Long userRoleId) {
        RoleMogonglieyan roleMogonglieyan = roleMogonglieyanDao.cacheLoad(userRoleId, userRoleId);
        if (null != roleMogonglieyan && !DatetimeUtil.dayIsToday(roleMogonglieyan.getUpdateTime())) {
            roleMogonglieyan.setYumoVal(getInitYumoVal());
            roleMogonglieyan.setUpdateTime(GameSystemTime.getSystemMillTime());
            roleMogonglieyanDao.cacheUpdate(roleMogonglieyan, userRoleId);
        }
        return roleMogonglieyan;
    }

    /**
     * 创建魔宫猎焰业务数据对象
     * 
     * @param userRoleId
     * @return
     */
    private RoleMogonglieyan createRoleMogonglieyan(Long userRoleId) {
        long now_time = GameSystemTime.getSystemMillTime();
        RoleMogonglieyan roleMogonglieyan = new RoleMogonglieyan();
        roleMogonglieyan.setUserRoleId(userRoleId);
        roleMogonglieyan.setJinghuaVal(0L);
        roleMogonglieyan.setYumoVal(getInitYumoVal());
        roleMogonglieyan.setCreateTime(now_time);
        roleMogonglieyan.setUpdateTime(now_time);
        roleMogonglieyanDao.cacheInsert(roleMogonglieyan, userRoleId);
        return roleMogonglieyan;
    }

    /**
     * 初始化数据到角色缓存中
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleMogonglieyan> initData2Cache(Long userRoleId) {
        return roleMogonglieyanDao.initRoleMogonglieyan(userRoleId);
    }

    /**
     * 更新御魔值
     * 
     * @param userRoleId
     * @param cutYumoVal
     */
    @Sync(component = GameConstants.COMPONENT_MGLY_YUMO_SHARE, indexes = { 0 })
    public void updateYumoVal(Long userRoleId, Integer cutYumoVal) {
        if (null == cutYumoVal || cutYumoVal.intValue() <= 0) {
            return;
        }
        // 玩家业务数据不存在
        RoleMogonglieyan roleMogonglieyan = getCacheRoleMogonglieyan(userRoleId);
        if (null == roleMogonglieyan) {
            return;
        }
        // 御魔值已变成0不可再减少
        if (roleMogonglieyan.getYumoVal() <= 0) {
            return;
        }
        roleMogonglieyan.setYumoVal((roleMogonglieyan.getYumoVal() - cutYumoVal) <= 0 ? 0 : (roleMogonglieyan.getYumoVal() - cutYumoVal));
        roleMogonglieyan.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleMogonglieyanDao.cacheUpdate(roleMogonglieyan, userRoleId);
        // 通知前端刷新御魔值变化
        BusMsgSender.send2One(userRoleId, ClientCmdType.MGLY_INIT_INFO, new Object[] { roleMogonglieyan.getYumoVal(), roleMogonglieyan.getJinghuaVal() });
        // 御魔值已变成0,通知stageServer启动延迟踢出场景定时器
        if (roleMogonglieyan.getYumoVal() <= 0) {
            BusMsgSender.send2Stage(userRoleId, InnerCmdType.I_MGLY_DELAY_EXIT, userRoleId);
        }
    }

    /**
     * 初始化魔宫猎焰面板信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] initInfo(Long userRoleId) {
        RoleMogonglieyan roleMogonglieyan = getCacheRoleMogonglieyan(userRoleId);
        if (null == roleMogonglieyan) {
            roleMogonglieyan = createRoleMogonglieyan(userRoleId);
        }
        return new Object[] { roleMogonglieyan.getYumoVal(), roleMogonglieyan.getJinghuaVal() };
    }

    /**
     * 请求进入场景
     * 
     * @param userRoleId
     * @param busMsgQueue
     */
    public void enterStage(Long userRoleId, BusMsgQueue busMsgQueue) {
        // 配置不存在
        MoGongLieYanPublicConfig config = getPublicConfig();
        if (null == config) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.MGLY_ENTER_STAGE, AppErrorCode.CONFIG_ERROR);
            return;
        }
        // 玩家业务数据不存在
        RoleMogonglieyan roleMogonglieyan = getCacheRoleMogonglieyan(userRoleId);
        if (null == roleMogonglieyan) {
            return;
        }
        // 玩家御魔值不够
        if (roleMogonglieyan.getYumoVal() <= 0) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.MGLY_ENTER_STAGE, AppErrorCode.MGLY_YUMO_NOT_ENOUGH);
            return;
        }
        // 玩家不可以从副本场景状态进入场景
        if (stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.MGLY_ENTER_STAGE, AppErrorCode.FUBEN_IS_IN_FUBEN);
            return;
        }
        // 修改状态为副本状态:控制进入地图后不可以进入其他副本场景,也不可以在地图中重复进入
        stageControllExportService.changeFuben(userRoleId, true);
        DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config.getMap());
        int[] birthXy = dituCoinfig.getRandomBirth();
        Object[] applyEnterData = new Object[] { dituCoinfig.getId(), birthXy[0], birthXy[1], MapType.MOGONGLIEYAN_MAP};
        busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
        //修炼任务
        BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.HUOZHONGMIGONG, null});
    }

    /**
     * 请求退出场景
     * 
     * @param userRoleId
     * @param busMsgQueue
     */
    public Object[] exitStage(Long userRoleId) {
        if (!stageControllExportService.inFuben(userRoleId)) {// 不再副本中
            return AppErrorCode.FUBEN_NOT_IN_FUBEN;
        }
//      stageControllExportService.changeFuben(userRoleId, false);
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.I_MGLY_EXIT_STAGE, userRoleId);
        return null;
    }

    /**
     * 消耗魔焰精华数值,购买(兑换)灵火之芯道具
     * 
     * @param userRoleId
     * @param order
     * @return
     */
    public void buyLhzx(Long userRoleId, Integer order, BusMsgQueue busMsgQueue) {
//        RoleMogonglieyan roleMogonglieyan = getCacheRoleMogonglieyan(userRoleId);
//        if (null == roleMogonglieyan) {
//            return;
//        }
//        long beforeJinghua = roleMogonglieyan.getJinghuaVal();
//        ZhuFuShangDianBiaoConfig config = zhuFuShangDianBiaoConfigExportService.loadById(order);
//        if (null == config) {
//            busMsgQueue.addMsg(userRoleId, ClientCmdType.MGLY_BUY_LHZX, AppErrorCode.CONFIG_ERROR);
//            return;
//        }
//        long needJinghua = config.getNeedJinghua();
//        if (beforeJinghua < needJinghua) {
//            busMsgQueue.addMsg(userRoleId, ClientCmdType.MGLY_BUY_LHZX, AppErrorCode.MGLY_JINGHUA_NOT_ENOUGH);
//            return;
//        }
//        Map<String, Integer> itemMap = config.getItemMap();
//        if (ObjectUtil.isEmpty(itemMap)) {
//            busMsgQueue.addMsg(userRoleId, ClientCmdType.MGLY_BUY_LHZX, AppErrorCode.CONFIG_ERROR);
//            return;
//        }
//        // 背包空间不足
//        Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(itemMap, userRoleId);
//        if (code != null) {
//            busMsgQueue.addMsg(userRoleId, ClientCmdType.MGLY_BUY_LHZX, code);
//            return;
//        }
//        // 更新数据
//        roleMogonglieyan.setJinghuaVal(beforeJinghua - needJinghua);
//        roleMogonglieyan.setUpdateTime(GameSystemTime.getSystemMillTime());
//        roleMogonglieyanDao.cacheUpdate(roleMogonglieyan, userRoleId);
//        // 发奖励
//        roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.MOGONGLIEYAN_EXCHANGE, LogPrintHandle.GET_MGLY_EXCHANGE_ITEM, LogPrintHandle.GBZ_MGLY_EXCHANGE_ITEM, true);
//        // 记录兑换日志
//        JSONArray goods = LogPrintHandle.getLogGoodsParam(itemMap, null);
//        RoleWrapper role = roleExportService.getLoginRole(userRoleId);
//        GamePublishEvent.publishEvent(new MoGongLieYanLogEvent(userRoleId, null == role ? "" : role.getName(), beforeJinghua, roleMogonglieyan.getJinghuaVal(), goods));
//        busMsgQueue.addMsg(userRoleId, ClientCmdType.MGLY_BUY_LHZX, AppErrorCode.OK);
//        // 刷新精华值
//        busMsgQueue.addMsg(userRoleId, ClientCmdType.MGLY_INIT_INFO, new Object[] { roleMogonglieyan.getYumoVal(), roleMogonglieyan.getJinghuaVal() });
    }

    /**
     * 使用道具获得魔焰精华数值
     * 
     * @param addJinghua
     * @return
     */
    public Object[] addJinghua(Long userRoleId, long addJinghua) {
        if (addJinghua < 0) {
            return AppErrorCode.CONFIG_ERROR;
        }
        RoleMogonglieyan roleMogonglieyan = getCacheRoleMogonglieyan(userRoleId);
        if (null != roleMogonglieyan) {
            roleMogonglieyan.setJinghuaVal(roleMogonglieyan.getJinghuaVal() + addJinghua);
            roleMogonglieyan.setUpdateTime(GameSystemTime.getSystemMillTime());
            roleMogonglieyanDao.cacheUpdate(roleMogonglieyan, userRoleId);
            BusMsgSender.send2One(userRoleId, ClientCmdType.MGLY_INIT_INFO, new Object[] { roleMogonglieyan.getYumoVal(), roleMogonglieyan.getJinghuaVal() });
        }
        return null;
    }

}
