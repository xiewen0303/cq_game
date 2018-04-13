/**
 * 
 */
package com.junyou.bus.lunpan.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.lunpan.configure.export.LunPanConfig;
import com.junyou.bus.lunpan.configure.export.LunPanConfigExportService;
import com.junyou.bus.lunpan.configure.export.LunPanConfigGroup;
import com.junyou.bus.lunpan.dao.LunPanLogDao;
import com.junyou.bus.lunpan.dao.RefabuLunpanDao;
import com.junyou.bus.lunpan.entity.LunPanLog;
import com.junyou.bus.lunpan.entity.RefabuLunpan;
import com.junyou.bus.lunpan.filter.LunPanFilter;
import com.junyou.bus.lunpan.utils.LunPanUtils;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.entity.RoleYuanbaoRecord;
import com.junyou.bus.rfbactivity.service.RoleYuanbaoRecordService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tongyong.dao.ActityCountLogDao;
import com.junyou.bus.tongyong.entity.ActityCountLog;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * @Description 充值轮盘
 * @Author Yang Gao
 * @Since 2016-6-1
 * @Version 1.1.0
 */
@Service
public class LunPanService {

    @Autowired
    private RefabuLunpanDao lunpanDao;
    @Autowired
    private LunPanLogDao lunPanLogDao;
    @Autowired
    private ActityCountLogDao actityCountLogDao;
    @Autowired
    private RoleYuanbaoRecordService roleYuanbaoRecordService;

    @Autowired
    private RoleBagExportService roleBagExportService;
    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private GoodsConfigExportService goodsConfigExportService;
    @Autowired
    private EmailExportService emailExportService;

    /**
     * 初始化轮盘数据到内存
     * 
     * @param userRoleId
     * @return
     */
    public List<RefabuLunpan> initRefabuLunpan(Long userRoleId) {
        return lunpanDao.initRefabuLunpan(userRoleId);
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
        LunPanConfigGroup config = LunPanConfigExportService.getInstance().loadByMap(subId);
        if (config == null) {
            return null;
        }
        RefabuLunpan lunpan = getRefabuLunpan(userRoleId, subId);
        todayRechargeYb(lunpan);
        ActityCountLog log = actityCountLogDao.getActivityCountBySubIdAndUser(subId, userRoleId);
        if (log != null) {
            long startTime = configSong.getStartTimeByMillSecond();// 活动开始时间
            long upTime = log.getUpdateTime();
            long dTime = GameSystemTime.getSystemMillTime();
            if (startTime > upTime && startTime < dTime) {// 如果活动开始时间大于业务上次更新时间，清理业务数据
                actityCountLogDao.cleanActivityCount(subId, userRoleId);
            } 
        }
        return new Object[] { 
                config.getPic(), 
                getUtilGood(userRoleId, subId), 
                lunpan.getJifen(), 
                config.getDuiHuanData().toArray(), 
                getLog(userRoleId), 
                config.getGold(), 
                config.getCount(), 
                new Object[] { lunpan.getCount(), config.getMaxCount() }, 
                new Object[] { lunpan.getGold(), getRemainCount(lunpan) } 
                };
    }
    
    /**
     * 活动充值处理
     * 
     * @param userRoleId
     * @param addVal
     */
    public void rechargeYb(Long userRoleId, Long addVal) {
        if (addVal <= 0) {
            return;
        }

        Map<Integer, LunPanConfigGroup> groups = LunPanConfigExportService.getInstance().getAllConfig();
        if (null == groups || groups.size() == 0) {
            return;
        }
        // 循环充值礼包配置数据
        for (int subId : groups.keySet()) {
            // 是否在有这个活动或者是否在时间内
            ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
            if (configSong == null || !configSong.isRunActivity()) {
                continue;
            }
            RefabuLunpan lunpan = getRefabuLunpan(userRoleId, subId);
            lunpan.setGold((int) (lunpan.getGold() + addVal));
            lunpan.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
            lunpanDao.cacheUpdate(lunpan, userRoleId);
        }
    }

    /**
     * 请求转轮盘操作
     * 
     * @param userRoleId
     * @param version
     * @param subId
     * @param busMsgQueue
     * @param many
     * @return
     */
    public Object[] zhuan(Long userRoleId, Integer version, int subId, BusMsgQueue busMsgQueue, boolean many) {
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
        
        LunPanConfigGroup config = LunPanConfigExportService.getInstance().loadByMap(subId);
        if (config == null) {
            return AppErrorCode.CONFIG_ERROR;
        }
        
        Map<Integer, Object[]> nMap = LunPanUtils.getXYZPMap(userRoleId, subId);
        if (nMap == null) {
            return AppErrorCode.ZP_DATA_ERROR;
        }

        RefabuLunpan lunpan = getRefabuLunpan(userRoleId, subId);
        int remainCountCount = getRemainCount(lunpan);
        if (remainCountCount <= 0) {
            return AppErrorCode.RFB_LUNPAN_NOT_COUNT;
        }
        
        int count = many ? 10 : 1;
        remainCountCount -= (remainCountCount < count) ? remainCountCount : count;

        boolean insert = true;
        // 如果配置了次数
        if (config.getMaxCount() > 0) {
            // 判断玩家次数
            ActityCountLog log = actityCountLogDao.getActivityCountBySubIdAndUser(subId, userRoleId);
            if (log != null) {
                insert = false;
                if (log.getCount() != null && log.getCount() + count > config.getMaxCount()) {
                    return AppErrorCode.ACTITY_MAX_COUNT;
                }
            }
        }

        Map<Integer, Object[]> nnGoodsMap = new HashMap<>();
        List<Object[]> zhuanList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // 进行抽奖
            Integer jiang = Lottery.getRandomKeyByInteger(config.getZpMap());
            
            Map<Integer, LunPanConfig> configMap = config.getConfigMap();
            LunPanConfig lunpanConfig = configMap.get(jiang);
            
            //转盘物品
            Map<Integer, Object[]> map = LunPanUtils.getXYZPMap(userRoleId,subId);
            if(map == null || map.get(jiang) == null){
                return AppErrorCode.ZP_NOT_GOODS; 
            }
            
            Map<String, Integer> goodMap = new HashMap<String, Integer>();
            Object[] obj = map.get(jiang);
            goodMap.put(obj[0].toString(), Integer.parseInt(obj[1].toString()));
        
            // 检查物品是否可以进背包
            Object[] bagCheck = roleBagExportService.checkPutInBag(goodMap, userRoleId);
            if (bagCheck != null) {
            	String title = EmailUtil.getCodeEmail(AppErrorCode.ZHUANPAN_EAMIL_TITLE);
                String content = EmailUtil.getCodeEmail(AppErrorCode.ZHUANPAN_EAMIL);
                emailExportService.sendEmailToOne(userRoleId, title,content, GameConstants.EMAIL_TYPE_SINGLE, obj[0] + GameConstants.CONFIG_SUB_SPLIT_CHAR + obj[1]);
            } else {
                zhuanList.add(obj);
                // 物品进背包
                roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.GOODS_GET_LUNPAN_GET, LogPrintHandle.GET_RFB_LUNPAN, LogPrintHandle.GBZ_RFB_LUNPAN, true);
            }

            // 新roll一个物品
            Object[] newGoods = getNewGood(lunpanConfig.getItemMap());
            if (newGoods == null) {
                newGoods = obj;// 没有找到新的物品，重新赋值老物品
            }

            // 设置新物品
            map.put(jiang, newGoods);
            LunPanUtils.setXYZPMap(userRoleId, map, subId);


            // 增加积分
            lunpan.setJifen(lunpan.getJifen() + 1);
            lunpan.setCount(lunpan.getCount() + 1);
            // 面板上变化的新物品
            nnGoodsMap.put(jiang, newGoods);

            // 公告
            GoodsConfig goodsConfig = goodsConfigExportService.loadById(obj[0].toString());
            saveLogAndNotify(subId, userRoleId, goodsConfig, Integer.parseInt(obj[1].toString()), configSong.getSkey(), busMsgQueue);
        }
        
        lunpan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        lunpanDao.cacheUpdate(lunpan, userRoleId);
        
        List<Object[]> list = new ArrayList<>();
        for (Integer key : nnGoodsMap.keySet()) {
            list.add(new Object[] { key, nnGoodsMap.get(key) });
        }
        
        if (config.getMaxCount() > 0) {
            if (insert) {
                ActityCountLog log = new ActityCountLog();
                log.setUserRoleId(userRoleId);
                log.setCount(count);
                log.setUpdateTime(GameSystemTime.getSystemMillTime());
                actityCountLogDao.insertDb(log, subId);
            } else {
                actityCountLogDao.addActivityCount(subId, userRoleId, count);
            }
        }
        return new Object[] { 
                AppErrorCode.SUCCESS, 
                subId, 
                list.toArray(), 
                zhuanList.toArray(), 
                lunpan.getJifen(), 
                count 
                };
    }

    /**
     * 请求积分兑换道具
     * 
     * @param userRoleId
     * @param version
     * @param subId
     * @param configId
     * @return
     */
    public Object[] duihuan(Long userRoleId, Integer version, int subId, int configId) {
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
        LunPanConfigGroup config = LunPanConfigExportService.getInstance().loadByMap(subId);

        if (config == null) {
            return AppErrorCode.CONFIG_ERROR;
        }

        Map<Integer, LunPanConfig> configMap = config.getConfigMap();
        LunPanConfig zpConfig = configMap.get(configId);
        if (zpConfig == null) {
            return AppErrorCode.CONFIG_ERROR;
        }
        // 兑换所需积分
        int jifen = zpConfig.getJifen();
        RefabuLunpan lunpan = getRefabuLunpan(userRoleId, subId);
        if (lunpan.getJifen() < jifen) {
            return AppErrorCode.ZP_DUIHUAN_JIFEN;
        }

        // 检查物品是否可以进背包
        Object[] bagCheck = roleBagExportService.checkPutInBag(zpConfig.getDuiHuanMap(), userRoleId);
        if (bagCheck != null) {
            return bagCheck;
        }

        // 消耗积分
        lunpan.setJifen(lunpan.getJifen() - jifen);
        lunpan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        lunpanDao.cacheUpdate(lunpan, userRoleId);

        // 物品进背包
        roleBagExportService.putGoodsAndNumberAttr(zpConfig.getDuiHuanMap(), userRoleId, GoodsSource.GOODS_GET_LUNPAN_GET, LogPrintHandle.GET_RFB_ZHUANPAN, LogPrintHandle.GBZ_RFB_LUNPAN, true);
        return new Object[] { 1, subId, configId, lunpan.getJifen() };
    }
    
    /**
     * 获取轮盘抽奖信息
     * @param userRoleId
     * @return
     */
    public Object[] getLunpanInfo(Long userRoleId,Integer subId) {
        ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
        if (configSong == null || !configSong.isRunActivity()) {
            return null;
        }
        RefabuLunpan lunpan = getRefabuLunpan(userRoleId, subId);
        return new Object[] { lunpan.getGold(), getRemainCount(lunpan), subId };
    }
    
    // 获取一个不为空的玩家的轮盘数据对象
    private RefabuLunpan getRefabuLunpan(Long userRoleId, int subId) {
        RefabuLunpan lunpan = null;
        List<RefabuLunpan> list = lunpanDao.cacheLoadAll(userRoleId, new LunPanFilter(subId));
        if (list == null || list.size() <= 0) {
            lunpan = new RefabuLunpan();
            lunpan.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
            lunpan.setUserRoleId(userRoleId);
            lunpan.setSubId(subId);
            lunpan.setJifen(0);
            lunpan.setCount(0);
            lunpan.setGold(0);
            lunpan.setCreateTime(new Timestamp(System.currentTimeMillis()));
            lunpan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            lunpanDao.cacheInsert(lunpan, userRoleId);

        } else {
            lunpan = list.get(0);
            ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
            if (configSong != null) {
                long oldActUpdateTime = lunpan.getUpdateTime().getTime();
                long actStartTime = configSong.getStartTimeByMillSecond();
                long nowTime = GameSystemTime.getSystemMillTime();
                if (oldActUpdateTime < actStartTime && actStartTime < nowTime) {
                    lunpan.setJifen(0);
                    lunpan.setCount(0);
                    lunpan.setGold(0);
                    lunpan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    lunpanDao.cacheUpdate(lunpan, userRoleId);
                }
            }
        }
        return lunpan;
    }

    /**
     * 保存广播日志并全服广播
     * 
     * @param userRoleId
     * @param goodsConfig
     * @param count
     * @param busMsgQueue
     */
    private void saveLogAndNotify(int subId, long userRoleId, GoodsConfig goodsConfig, int count, String key, BusMsgQueue busMsgQueue) {
        RoleWrapper role = roleExportService.getLoginRole(userRoleId);
        String roleName = role.getName();
        // 全局通知
        if (goodsConfig.isNotify()) {

            if (role.isGm()) {
                return;// GM不广播
            }

            busMsgQueue.addBroadcastMsg(ClientCmdType.LUNPAN_SYSTEM_NOTIFY, new Object[] { subId, new Object[] { goodsConfig.getId(), count, roleName }, key });
        }
        // 记录通知
        LunPanLog log = new LunPanLog();

        log.setRoleName(roleName);
        log.setGoodsId(goodsConfig.getId());
        log.setGoodsCount(count);
        log.setCreateTime(GameSystemTime.getSystemMillTime());
        log.setUserRoleId(userRoleId);
        try {
            lunPanLogDao.insertDb(log);
        } catch (Exception e) {
            ChuanQiLog.error("", e);
        }
    }

    /**
     * 获取转盘物品
     * 
     * @param userRoleId
     * @param subId
     * @return
     */
    private Object[] getUtilGood(Long userRoleId, Integer subId) {
        LunPanConfigGroup config = LunPanConfigExportService.getInstance().loadByMap(subId);
        Map<Integer, LunPanConfig> configMap = config.getConfigMap();
        Map<Integer, Object[]> map = LunPanUtils.getXYZPMap(userRoleId, subId);

        Object[] returnObj = new Object[config.getMaxGe()];
        if (map != null && map.size() > 0) {
            for (int i = 0; i < config.getMaxGe(); i++) {
                returnObj[i] = map.get(i + 1);
            }
            return returnObj;
        }
        map = new HashMap<Integer, Object[]>();
        for (int i = 0; i < returnObj.length; i++) {
            LunPanConfig zpConfig = configMap.get(i + 1);
            if (zpConfig == null) {
                return null;
            }
            Object[] newGoods = getNewGood(zpConfig.getItemMap());
            returnObj[i] = newGoods;
            map.put(i + 1, newGoods);
        }
        LunPanUtils.setXYZPMap(userRoleId, map, subId);
        return returnObj;

    }

    /**
     * 获取新物品
     * 
     * @param item
     * @return
     */
    private Object[] getNewGood(Map<String, Integer> item) {
        if (item == null || item.size() <= 0) {
            return null;
        }
        String good = Lottery.getRandomKeyByInteger(item);
        if (good == null) {
            return null;
        }
        String[] g = good.split(":");
        if (g.length <= 1) {
            return new Object[] { g[0], 1 };
        }
        return new Object[] { g[0], g[1] };
    }

    // 处理活动延迟上线之前玩家充值的元宝,延迟时间在一天之内有效恢复,过期不处理
    private void todayRechargeYb(RefabuLunpan lunpan) {
        RoleYuanbaoRecord record = roleYuanbaoRecordService.getRoleYuanBaoRecord(lunpan.getUserRoleId());
        if (record.getCzValue() > lunpan.getGold()) {
            lunpan.setGold(record.getCzValue());
            lunpan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            lunpanDao.cacheUpdate(lunpan, lunpan.getUserRoleId());
        }
    }

    //获取剩余抽奖次数
    private int getRemainCount(RefabuLunpan lunpan) {
        LunPanConfigGroup config = LunPanConfigExportService.getInstance().loadByMap(lunpan.getSubId());
        int needGold = config.getGold();
        int roleGold = lunpan.getGold();
        int maxCount = config.getCount() * (roleGold / needGold);
        int remainCount = maxCount - lunpan.getCount();
        return remainCount <= 0 ? 0 : remainCount;
    }

    /**
     * 获取个人购买日志
     * 
     * @return
     */
    private Object[] getLog(Long userRoleId) {
        List<LunPanLog> lunpanLogs = null;
        try {
            lunpanLogs = lunPanLogDao.getLunpanLogByIdDb(userRoleId);
        } catch (Exception e) {
            ChuanQiLog.error("", e);
        }
        if (lunpanLogs == null || lunpanLogs.size() <= 0) {
            return null;
        }
        List<Object[]> list = new ArrayList<>();
        for (int i = 0; i < lunpanLogs.size(); i++) {
            LunPanLog log = lunpanLogs.get(i);
            list.add(new Object[] { log.getGoodsId(), log.getGoodsCount(), log.getRoleName() });
        }
        return list.toArray();

    }

    
}