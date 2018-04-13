/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.equip.configure.export.ZhanLiXiShuConfigExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xinmo.configure.NingShenJiChuBiaoConfig;
import com.junyou.bus.xinmo.configure.NingShenJiChuBiaoConfigExport;
import com.junyou.bus.xinmo.configure.TianLuLianDanBiaoConfig;
import com.junyou.bus.xinmo.configure.TianLuLianDanBiaoConfigExport;
import com.junyou.bus.xinmo.configure.XinMoJiNengBiaoConfig;
import com.junyou.bus.xinmo.configure.XinMoJiNengBiaoConfigExport;
import com.junyou.bus.xinmo.configure.XinMoXiLianBiaoConfig;
import com.junyou.bus.xinmo.configure.XinMoXiLianBiaoConfigExport;
import com.junyou.bus.xinmo.configure.XinmoMoshenConfigExport;
import com.junyou.bus.xinmo.configure.XinmoMoshenJiChuBiaoConfig;
import com.junyou.bus.xinmo.configure.XinmoMoshenShitiBiaoConfig;
import com.junyou.bus.xinmo.constants.XinmoConstants;
import com.junyou.bus.xinmo.dao.RoleXinmoDao;
import com.junyou.bus.xinmo.dao.RoleXinmoLiandanDao;
import com.junyou.bus.xinmo.dao.RoleXinmoLiandanItemDao;
import com.junyou.bus.xinmo.dao.RoleXinmoMoshenDao;
import com.junyou.bus.xinmo.dao.RoleXinmoMoshenShitiDao;
import com.junyou.bus.xinmo.dao.RoleXinmoSkillDao;
import com.junyou.bus.xinmo.dao.RoleXinmoXilianDao;
import com.junyou.bus.xinmo.entity.RoleXinmo;
import com.junyou.bus.xinmo.entity.RoleXinmoLiandan;
import com.junyou.bus.xinmo.entity.RoleXinmoLiandanItem;
import com.junyou.bus.xinmo.entity.RoleXinmoMoshen;
import com.junyou.bus.xinmo.entity.RoleXinmoMoshenShiti;
import com.junyou.bus.xinmo.entity.RoleXinmoSkill;
import com.junyou.bus.xinmo.entity.RoleXinmoXilian;
import com.junyou.bus.xinmo.filter.XinmoMoshenFilter;
import com.junyou.bus.xinmo.filter.XinmoSkillFilter;
import com.junyou.bus.xinmo.utils.XinmoUtils;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.XinmoLogEvent;
import com.junyou.event.XinmoMoshenLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.XinmoLiandanPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.XinmoMoshenPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.XinmoXilianPublicConfig;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.XinmoDouchangFubenStage;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.lottery.RandomUtil;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 *@Description 心魔系统业务逻辑处理
 *@Author Yang Gao
 *@Since 2016-6-28
 *@Version 1.1.0
 */
@Service
public class XinmoService {

    @Autowired
    private RoleXinmoDao roleXinmoDao;
    @Autowired
    private RoleXinmoLiandanDao roleXinmoLiandanDao;
    @Autowired
    private RoleXinmoLiandanItemDao roleXinmoLiandanItemDao;
    @Autowired
    private RoleXinmoMoshenDao roleXinmoMoshenDao;
    @Autowired
    private RoleXinmoMoshenShitiDao roleXinmoMoshenShitiDao;
    @Autowired
    private RoleXinmoSkillDao roleXinmoSkillDao;
    @Autowired
    private RoleXinmoXilianDao roleXinmoXilianDao;
    
    @Autowired
    private NingShenJiChuBiaoConfigExport ningShenJiChuBiaoConfigExport;
    @Autowired
    private TianLuLianDanBiaoConfigExport tianLuLianDanBiaoConfigExport;
    @Autowired
    private XinmoMoshenConfigExport xinmoMoshenConfigExport;
    @Autowired
    private XinMoJiNengBiaoConfigExport xinMoJiNengBiaoConfigExport;
    @Autowired
    private XinMoXiLianBiaoConfigExport xinMoXiLianBiaoConfigExport;
    
    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private RoleBagExportService roleBagExportService;
    @Autowired
    private BusScheduleExportService scheduleExportService;
    @Autowired
    private GoodsConfigExportService goodsConfigExportService;
    @Autowired
    private StageControllExportService stageControllExportService;
    @Autowired
    private ZhanLiXiShuConfigExportService zhanLiXiShuConfigExportService;
    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
    
    // 记录玩家击杀心魔怪物日志
    private Map<Long, XinmoLogEvent> killMonsterXinmoLogMap = new HashMap<Long, XinmoLogEvent>();
    
    //**************************************心魔*********************************************//

    /**
     * 获取玩家心魔数据对象
     * @param userRoleId
     * @return
     */
    private RoleXinmo getRoleXinmo(Long userRoleId) {
        return roleXinmoDao.cacheLoad(userRoleId, userRoleId);
    }

    /**
     * 初始化玩家心魔数据对象
     * @param userRoleId
     * @return
     */
    private RoleXinmo createRoleXinmo(Long userRoleId) {
        long nowTime = GameSystemTime.getSystemMillTime();
        NingShenJiChuBiaoConfig config = ningShenJiChuBiaoConfigExport.getConfig(XinmoConstants.XINMO_INIT_CATEGORY, XinmoConstants.XINMO_INIT_TYPE, XinmoConstants.XINMO_INIT_LEVEL);
        
        RoleXinmo roleXinmo = new RoleXinmo();
        roleXinmo.setUserRoleId(userRoleId);
        roleXinmo.setXinmoId(config.getId());
        roleXinmo.setXinmoExp(0);
        roleXinmo.setCreateTime(nowTime);
        roleXinmo.setUpdateTime(nowTime);
        roleXinmoDao.cacheInsert(roleXinmo, userRoleId);
        return roleXinmo;
    }
    
    /**
     * 记录心魔操作日志 
     * @param userRoleId 玩家编号
     * @param type 操作类型1=心魔升级;2=心魔突破;3=心魔凝神
     * @param oldConfigId 操作前旧的配置编号
     * @param newConfigId 操作后新的配置编号
     * @param consumeItemArray 操作消耗的道具集合
     * @param consumeJBVal 消耗的金币
     * @param consumeYBVal 消耗的元宝
     * @param consumeBYBVal 消耗的绑定元宝
     */
    private void recordXinmoLog(Long userRoleId, Integer type, Integer oldConfigId, Integer newConfigId, JSONArray consumeItemArray, Integer consumeJBVal, Integer consumeYBVal, Integer consumeBYBVal, Integer oldXinmoExp, Integer newXinmoExp){
        RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
        GamePublishEvent.publishEvent(new XinmoLogEvent(userRoleId, roleWrapper.getName(), type, oldConfigId, newConfigId, consumeItemArray, consumeJBVal,consumeYBVal, consumeBYBVal, oldXinmoExp, newXinmoExp));
    }
    
    /**
     * 保存玩家击杀怪物的心魔日志到缓存 
     * @throws:
     * @Author: Yang Gao
     * @date: 2017-1-16 下午12:41:55 
     */
    private void saveRoleXinmoKillLog2Cache(Long userRoleId, Integer oldConfigId, Integer newConfigId, Integer oldXinmoExp, Integer newXinmoExp) {
        XinmoLogEvent xinmoLog = killMonsterXinmoLogMap.get(userRoleId);
        if (null == xinmoLog) {
            RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
            xinmoLog = new XinmoLogEvent(userRoleId, roleWrapper.getName(), XinmoConstants.ACTION_TYPE_KILL, oldConfigId, newConfigId, null, null, null, null, oldXinmoExp, newXinmoExp);
        } else {
            xinmoLog.setNewXinmoExp(newXinmoExp);
        }
        killMonsterXinmoLogMap.put(userRoleId, xinmoLog);
    }
    
    /**
     * 判断心魔突破是否成功 
     * @param rate
     * @return true=成功;false=失败
     */
    private boolean isTopoSuccess(int rate){ 
        return rate >= RandomUtil.getIntRandomValue(1, 10001);
    }
    
    /**
     * 检查心魔操作需要消耗的道具和银两
     * @param userRoleId
     * @param goodsIdList
     * @param isAuto
     * @return
     */
    private Object[] xinmoCheck(Long userRoleId, int actionType, List<Long> itemIds, Boolean isAuto) {
        if (null == isAuto) {
            return new Object[] { AppErrorCode.FAIL, AppErrorCode.PARAMETER_ERROR};
        }
        RoleXinmo roleXinmo = getRoleXinmo(userRoleId);
        if (null == roleXinmo) {
            return null;
        }
        /* 校验前端传递的是否为操作需要的道具 */
        List<String> goodsIdList = null;// 需要消耗的道具id集合
        if (!ObjectUtil.isEmpty(itemIds)) {
            goodsIdList = new ArrayList<>();
            Integer goodsCategory = XinmoUtils.getActionCheckGoodsCategory(actionType);
            for (Long guid : itemIds) {
                RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, guid);
                if (roleItemExport == null) {
                    return new Object[] { AppErrorCode.FAIL, AppErrorCode.XINMO_ITEM_ERROR };
                }
                String goodsId = roleItemExport.getGoodsId();
                GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
                if (null != goodsConfig && null != goodsCategory && goodsConfig.getCategory() != goodsCategory.intValue()) {
                    return new Object[] { AppErrorCode.FAIL, AppErrorCode.XINMO_ITEM_ERROR };
                }
                goodsIdList.add(goodsId);
            }
        }
        /* 是否可以操作校验 */
        int xinmoId = roleXinmo.getXinmoId();
        NingShenJiChuBiaoConfig config = ningShenJiChuBiaoConfigExport.loadById(xinmoId);
        Object[] canCheck = XinmoUtils.isCanCheck(actionType, config.getNeedType());
        if (null != canCheck) {
            return new Object[] { AppErrorCode.FAIL, canCheck };
        }

        /* 等级校验 */
        int category = config.getCategory();
        int type = config.getType();
        int level = config.getLevel();
        int maxLevel = ningShenJiChuBiaoConfigExport.getMaxLevelByCategoryAndType(category, type);
        Object[] levelCheck = XinmoUtils.levelChack(actionType, level, maxLevel);
        if (null != levelCheck) {
            return new Object[] { AppErrorCode.FAIL, levelCheck };
        }

        /* 银两消耗检验 */
        int money = config.getMoney();
        if (money > 0) {
            Object[] isOb = roleBagExportService.isEnought(GoodsCategory.MONEY, money, userRoleId);
            if (null != isOb) {
                return new Object[] { AppErrorCode.FAIL, AppErrorCode.JB_ERROR };
            }
        }
        
        /* 道具消耗校验 */
        int useGold = 0;// 消耗的元宝
        int useBgold = 0;// 消耗的绑定元宝
        Map<String, Integer> itemMap = new HashMap<>();// 消耗的道具
        int needCount = config.getCount();// 需要消耗的道具数量
        if (needCount > 0 && !ObjectUtil.isEmpty(goodsIdList)) {
            for (String goodsId : goodsIdList) {
                int itemCnt = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
                if (itemCnt >= needCount) {
                    itemMap.put(goodsId, needCount);
                    needCount = 0;
                    break;
                } else if (itemCnt > 0) {
                    itemMap.put(goodsId, itemCnt);
                    needCount -= itemCnt;
                }
            }
        }
        if (needCount > 0 && isAuto) {// 道具不够:系统自动购买消耗道具,优先消耗绑定元宝,次消耗元宝
            int propBgoldPrice = config.getBgold(); // 每个道具消耗的绑定元宝价格
            if (propBgoldPrice < 1) {
                return new Object[] { AppErrorCode.FAIL, AppErrorCode.CONFIG_ERROR };
            }
            /* 消耗绑定元宝 */
            int bgCount = 0;
            for (int count = 1; count <= needCount; count++) {
                Object[] bgoldError = roleBagExportService.isEnought(GoodsCategory.BGOLD, (count * propBgoldPrice), userRoleId);
                if (null != bgoldError) {
                    break;
                }
                ++bgCount;
            }
            if(bgCount > 0){
                needCount -= bgCount;
                useBgold = bgCount * propBgoldPrice;
            }
            /* 剩余道具消耗元宝 */
            if (needCount > 0) {
                int propGoldPrice = config.getGold(); // 每个道具消耗的元宝价格
                if (propGoldPrice < 1) {
                    return new Object[] { AppErrorCode.FAIL, AppErrorCode.CONFIG_ERROR };
                }
                useGold = needCount * propGoldPrice;// 需要消耗的元宝价格总和
                Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, useGold, userRoleId);
                if (null != goldError) {
                    return new Object[] { AppErrorCode.FAIL, AppErrorCode.YB_ERROR };
                }
                /* 记录消耗的元宝 */
                needCount = 0;
            }
        }

        if (needCount > 0) {
            return new Object[] { AppErrorCode.FAIL, AppErrorCode.XINMO_ITEM_ENOUGH };
        }

        int consumeLogPrintHandle = XinmoUtils.getActionConsumeLogPrintHandle(actionType);
        int consumeBeiZhu = XinmoUtils.getActionConsumeBeiZhu(actionType);
        String qqXiaoFei = XinmoUtils.getActionQQXiaoFei(actionType);
        // 扣除银两
        if (money > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId, consumeLogPrintHandle, true, consumeBeiZhu);
        }

        // 扣除绑定元宝
        if (useBgold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, useBgold, userRoleId, consumeLogPrintHandle, true, consumeBeiZhu);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_BYB, useBgold, consumeLogPrintHandle, qqXiaoFei, 1 });
            }
        }

        // 扣除元宝
        if (useGold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, useGold, userRoleId, consumeLogPrintHandle, true, consumeBeiZhu);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, useGold, consumeLogPrintHandle, qqXiaoFei, 1 });
            }
        }
        int goodsSource = XinmoUtils.getActionGoodsSource(actionType);
        // 扣除道具
        BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(itemMap, userRoleId, goodsSource, true, true);
        if (!bagSlots.isSuccee()) {
            return new Object[] { AppErrorCode.FAIL, bagSlots.getErrorCode() };
        }
        return new Object[] { AppErrorCode.SUCCESS, new Object[]{itemMap, money, useGold, useBgold}};
    }
    
    /**
     * 推送内部场景心魔属性变化
     * @param userRoleId
     */
    private void notifyStageXinmoAttrChange(Long userRoleId){
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_XINMO_CHANGE, getXinmoAttrs(userRoleId));
    }
    
    /**
     * 心魔全服通告 
     * @param code
     * @param parameter
     */
    private void xinmoNotice(int code, Object parameter) {
        BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { code, parameter });
    }
    
    /**
     * 初始化玩家心魔数据 
     * @param userRoleId
     * @return
     */
    public List<RoleXinmo> initRoleXinmo(Long userRoleId){
        return roleXinmoDao.initRoleXinmo(userRoleId);
    }
    
    /**
     * 获取玩家心魔属性集合 
     * @param userRoleId
     * @return
     */
    public Map<String, Long> getXinmoAttrs(Long userRoleId){
        RoleXinmo roleXinmo = getRoleXinmo(userRoleId);
        if(null == roleXinmo){
            return null;
        }
        NingShenJiChuBiaoConfig config = ningShenJiChuBiaoConfigExport.loadById(roleXinmo.getXinmoId());
        if(null == config){
            return null;
        }
        return config.getXmAttrsMap();
    }
    
    /**
     * 请求获取心魔信息
     * @param userRoleId
     * @return
     */
    public Object[] getXinmoInfo(Long userRoleId) {
        RoleXinmo roleXinmo = getRoleXinmo(userRoleId);
        if(null == roleXinmo){
            roleXinmo = createRoleXinmo(userRoleId);
            // 激活心魔刷新属性
            notifyStageXinmoAttrChange(userRoleId);
        }
        return new Object[] { roleXinmo.getXinmoId(), roleXinmo.getXinmoExp() };
    }

    /**
     * 请求升级心魔等级
     * @param userRoleId
     * @param itemIds 升级所需道具guid集合
     * @param isAuto 是否系统自动购买消耗道具升级
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object[] xinmoShengji(Long userRoleId, List<Long> itemIds ,Boolean isAuto) {
        RoleXinmo roleXinmo = getRoleXinmo(userRoleId);
        if(null == roleXinmo){
            return null;
        }
        Object[] checkResult = xinmoCheck(userRoleId, XinmoConstants.ACTION_TYPE_SHENGJI, itemIds, isAuto);
        if(null == checkResult){
            return null;
        }
        int resultCode = (Integer) checkResult[0];
        Object[] resultData = (Object[]) checkResult[1];
        if (resultCode == AppErrorCode.FAIL) {
            return resultData;
        }
        boolean notifyFlag = false;        
        int xinmoId = roleXinmo.getXinmoId();
        int oldXinmoExp = roleXinmo.getXinmoExp();
        NingShenJiChuBiaoConfig config = ningShenJiChuBiaoConfigExport.loadById(xinmoId);
        /*更新数据*/
        int newXinmoId = xinmoId;
        int newXinmoExp = oldXinmoExp + config.getAddVal();
        if (newXinmoExp >= config.getNeedVal()) {
            notifyFlag = true;
            newXinmoExp = 0;
            int level = config.getLevel() + 1;
            NingShenJiChuBiaoConfig nextConfig = ningShenJiChuBiaoConfigExport.getConfig(config.getCategory(), config.getType(), level);
            newXinmoId = nextConfig.getId();
            roleXinmo.setXinmoId(newXinmoId);
        }
        roleXinmo.setXinmoExp(newXinmoExp);
        roleXinmo.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleXinmoDao.cacheUpdate(roleXinmo, userRoleId);
        // 通知场景刷新属性
        if (notifyFlag)
            notifyStageXinmoAttrChange(userRoleId);
        // 记录升级操作日志
        Map<String, Integer> itemMap = (Map<String, Integer>) resultData[0];
        Integer money = (Integer) resultData[1];
        Integer useGold = (Integer) resultData[2];
        Integer useBgold = (Integer) resultData[3];
        recordXinmoLog(userRoleId, XinmoConstants.ACTION_TYPE_SHENGJI, xinmoId, newXinmoId, LogPrintHandle.getLogGoodsParam(itemMap, null), money, useGold, useBgold, oldXinmoExp, newXinmoExp);
        return new Object[] { AppErrorCode.SUCCESS, newXinmoId, newXinmoExp };
    }

    /**
     *  请求突破心魔时期
     * @param userRoleId
     * @param itemIds
     * @param isAuto
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object[] xinmoTupo(Long userRoleId, List<Long> itemIds, Boolean isAuto) {
        RoleXinmo roleXinmo = getRoleXinmo(userRoleId);
        if(null == roleXinmo){
            return null;
        }
        Object[] checkResult = xinmoCheck(userRoleId, XinmoConstants.ACTION_TYPE_TUPO, itemIds, isAuto);
        if(null == checkResult){
            return null;
        }
        int resultCode = (Integer) checkResult[0];
        Object[] resultData = (Object[]) checkResult[1];
        if (resultCode == AppErrorCode.FAIL) {
            return resultData;
        }
        
        boolean notifyFlag = false;
        int xinmoId = roleXinmo.getXinmoId();
        int oldXinmoExp = roleXinmo.getXinmoExp();
        NingShenJiChuBiaoConfig config = ningShenJiChuBiaoConfigExport.loadById(xinmoId);
        /*更新数据*/
        int newXinmoId = xinmoId;
        int rate = config.getNeedVal();
        if(isTopoSuccess(rate)){
            notifyFlag = true;
            int type = config.getType() + 1;
            NingShenJiChuBiaoConfig nextConfig = ningShenJiChuBiaoConfigExport.getConfig(config.getCategory(), type, XinmoConstants.XINMO_INIT_LEVEL);
            newXinmoId = nextConfig.getId();
            roleXinmo.setXinmoId(newXinmoId);
            roleXinmo.setXinmoExp(0);
            roleXinmo.setUpdateTime(GameSystemTime.getSystemMillTime());
            roleXinmoDao.cacheUpdate(roleXinmo, userRoleId);
        }
        // 通知场景刷新属性
        if (notifyFlag)
            notifyStageXinmoAttrChange(userRoleId);
        // 记录升级操作日志
        Map<String, Integer> itemMap = (Map<String, Integer>) resultData[0];
        Integer money = (Integer) resultData[1];
        Integer useGold = (Integer) resultData[2];
        Integer useBgold = (Integer) resultData[3];
        recordXinmoLog(userRoleId, XinmoConstants.ACTION_TYPE_TUPO, xinmoId, newXinmoId, LogPrintHandle.getLogGoodsParam(itemMap, null), money, useGold, useBgold, oldXinmoExp, null);
        return new Object[] { AppErrorCode.SUCCESS, newXinmoId, new Object[] { ObjectUtil.map2String(itemMap), money, useGold, useBgold } };
    }

    /**
     * 请求凝神心魔境界
     * @param userRoleId
     * @param itemIds
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object[] xinmoNingshen(Long userRoleId, List<Long> itemIds) {
        RoleXinmo roleXinmo = getRoleXinmo(userRoleId);
        if(null == roleXinmo){
            return null;
        }
        int xinmoId = roleXinmo.getXinmoId();
        if(ningShenJiChuBiaoConfigExport.isXinmoLast(xinmoId)){
            return AppErrorCode.XINMO_MAX_NINGSHEN;
        }
        Object[] checkResult = xinmoCheck(userRoleId, XinmoConstants.ACTION_TYPE_NINGSHEN, itemIds, false);
        if(null == checkResult){
            return null;
        }
        int resultCode = (Integer) checkResult[0];
        Object[] resultData = (Object[]) checkResult[1];
        if (resultCode == AppErrorCode.FAIL) {
            return resultData;
        }
        int newXinmoId = xinmoId;
        int oldXinmoExp = roleXinmo.getXinmoExp();
        /*更新数据*/
        NingShenJiChuBiaoConfig config = ningShenJiChuBiaoConfigExport.loadById(xinmoId);
        int newXinmoExp = oldXinmoExp + config.getAddVal();
        NingShenJiChuBiaoConfig nextConfig = null;
        if(newXinmoExp >= config.getNeedVal()){
            newXinmoExp = 0;
            int category = config.getCategory() + 1;
            nextConfig = ningShenJiChuBiaoConfigExport.getConfig(category, XinmoConstants.XINMO_INIT_TYPE, XinmoConstants.XINMO_INIT_LEVEL);
            newXinmoId = nextConfig.getId();
            roleXinmo.setXinmoId(newXinmoId);
        }
        roleXinmo.setXinmoExp(newXinmoExp);
        roleXinmo.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleXinmoDao.cacheUpdate(roleXinmo, userRoleId);
        // 境界进阶
        if (null != nextConfig) {
            // 通知场景刷新属性
            notifyStageXinmoAttrChange(userRoleId);
            // 全服通告
            if (config.getGgCode() > 0) {
                UserRole userRole = roleExportService.getUserRole(userRoleId);
                xinmoNotice(config.getGgCode(), new Object[] { userRole.getName(), nextConfig.getCategory() });
            }
        }
        // 记录升级操作日志
        Map<String, Integer> itemMap = (Map<String, Integer>) resultData[0];
        Integer money = (Integer) resultData[1];
        Integer useGold = (Integer) resultData[2];
        Integer useBgold = (Integer) resultData[3];
        recordXinmoLog(userRoleId, XinmoConstants.ACTION_TYPE_NINGSHEN, xinmoId, newXinmoId, LogPrintHandle.getLogGoodsParam(itemMap, null), money, useGold, useBgold, oldXinmoExp, newXinmoExp);
        return new Object[] { AppErrorCode.SUCCESS, newXinmoId, newXinmoExp };
    }
  
    /**
     * 击杀特殊怪物,提升凝神需要的心神力 
     * @param userRoleId
     */
    public void killMonsterAddXinmoExp(Long userRoleId){
        RoleXinmo roleXinmo = getRoleXinmo(userRoleId);
        if(null == roleXinmo){
            return ;
        }
        int xinmoId = roleXinmo.getXinmoId();
        /*最大限制校验*/
        if(ningShenJiChuBiaoConfigExport.isXinmoLast(xinmoId)){
            return ;
        }
        int actionType = XinmoConstants.ACTION_TYPE_NINGSHEN;
        /* 是否可以操作校验 */
        NingShenJiChuBiaoConfig config = ningShenJiChuBiaoConfigExport.loadById(xinmoId);
        Object[] canCheck = XinmoUtils.isCanCheck(actionType, config.getNeedType());
        if (null != canCheck) {
            return;
        }
        /* 等级校验 */
        int category = config.getCategory();
        int type = config.getType();
        int level = config.getLevel();
        int maxLevel = ningShenJiChuBiaoConfigExport.getMaxLevelByCategoryAndType(category, type);
        Object[] levelCheck = XinmoUtils.levelChack(actionType, level, maxLevel);
        if (null != levelCheck) {
            return ;
        }
        int oldXinmoExp = roleXinmo.getXinmoExp();
        /*更新数据*/
        int newXinmoId = xinmoId;
        int newXinmoExp = oldXinmoExp + XinmoConstants.MONSTER_ADD_EXP;;
        NingShenJiChuBiaoConfig nextConfig = null;
        if(newXinmoExp >= config.getNeedVal()){
            ++category;
            newXinmoExp = 0;
            nextConfig = ningShenJiChuBiaoConfigExport.getConfig(category, XinmoConstants.XINMO_INIT_TYPE, XinmoConstants.XINMO_INIT_LEVEL);
            newXinmoId = nextConfig.getId();
            roleXinmo.setXinmoId(newXinmoId);
        }
        roleXinmo.setXinmoExp(newXinmoExp);
        roleXinmo.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleXinmoDao.cacheUpdate(roleXinmo, userRoleId);
        // 境界进阶
        if (null != nextConfig) {
            // 通知场景刷新属性
            notifyStageXinmoAttrChange(userRoleId);
            // 全服通告
            if (config.getGgCode() > 0) {
                UserRole userRole = roleExportService.getUserRole(userRoleId);
                xinmoNotice(config.getGgCode(), new Object[] { userRole.getName(), nextConfig.getCategory() });
            }
            saveRoleXinmoKillLog(userRoleId);
        } else {
            saveRoleXinmoKillLog2Cache(userRoleId, xinmoId, newXinmoId, oldXinmoExp, newXinmoExp);
        }
        BusMsgSender.send2One(userRoleId, ClientCmdType.XINMO_REFRESH_EXP, new Object[] { newXinmoId, newXinmoExp });
    }
    
    /**
     *  获取玩家元神等阶
     * @param userRoleId
     * @return
     */
    public int getRoleXinmoRank(Long userRoleId){
        RoleXinmo roleXinmo = getRoleXinmo(userRoleId);
        if(null == roleXinmo){
            return 0;
        }
        NingShenJiChuBiaoConfig ningshenConfig = ningShenJiChuBiaoConfigExport.loadById(roleXinmo.getXinmoId());
        if(null == ningshenConfig){
            return 0;
        }
        return ningshenConfig.getCategory();
    }
    
    /**
     * 保存玩家心魔击杀怪物日志到文件 
     * @param userRoleId
     * @throws:
     * @Author: Yang Gao
     * @date: 2017-1-16 下午12:52:08 
     */
    public void saveRoleXinmoKillLog(Long userRoleId){
        XinmoLogEvent xinmoLog = killMonsterXinmoLogMap.get(userRoleId);
        if (null == xinmoLog) {
            return;
        }
        killMonsterXinmoLogMap.remove(userRoleId);
        GamePublishEvent.publishEvent(xinmoLog);
    }
    
    // *******************************************心魔:天炉炼丹*****************************************//
    /**
     * 根据等级判定是否开启天炉炼丹系统
     * 
     * @param userRoleId
     * @return
     */
    private boolean isOpenLianDan(Long userRoleId) {
        RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
        if (null == roleWrapper) {
            return false;
        }
        XinmoLiandanPublicConfig xinLiandanPublic = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_XINMO_LIANDAN);
        if (null == xinLiandanPublic) {
            return false;
        }
        return roleWrapper.getLevel() >= xinLiandanPublic.getOpenLevel();
    }

    /**
     * 获取缓存中的心魔炼丹数据对象
     * 
     * @param userRoleId
     * @return
     */
    private RoleXinmoLiandan getCacheRoleXinmoLiandan(Long userRoleId) {
        return roleXinmoLiandanDao.cacheLoad(userRoleId, userRoleId);
    }

    /**
     * 获取缓存中玩家丹药仓库中单个丹药数据
     * 
     * @param userRoleId
     * @param guid
     * @return
     */
    private RoleXinmoLiandanItem getCacheRoleXinmoLiandanItemByGuid(Long userRoleId, long guid) {
        return roleXinmoLiandanItemDao.cacheLoad(guid, userRoleId);
    }

    /**
     * 获取玩家所有丹药仓库数据集合
     * 
     * @param userRoleId
     * @return
     */
    private List<RoleXinmoLiandanItem> getAllRoleXinmoLiandanItem(Long userRoleId) {
        return roleXinmoLiandanItemDao.cacheLoadAll(userRoleId);
    }

    /**
     * 获取丹药在仓库的格位号
     * 
     * @param userRoleId
     * @param maxSolt
     * @return
     */
    private Integer getLiandanItemSolt(Long userRoleId, int maxSolt) {
        if (XinmoConstants.XM_LIANDAN_MIN_SOLT > maxSolt)
            return null;
        List<RoleXinmoLiandanItem> roleXinmoLiandanList = getAllRoleXinmoLiandanItem(userRoleId);
        int size = null == roleXinmoLiandanList ? 0 : roleXinmoLiandanList.size();
        if (0 >= size) {
            return XinmoConstants.XM_LIANDAN_MIN_SOLT;
        }
        if (size >= maxSolt) {
            return null;
        }
        Integer rsSolt = null;
        Set<Integer> soltSet = new HashSet<Integer>();
        for (RoleXinmoLiandanItem item : roleXinmoLiandanList) {
            soltSet.add(item.getSolt());
        }
        for (int solt = XinmoConstants.XM_LIANDAN_MIN_SOLT; solt <= maxSolt; solt++) {
            if (!soltSet.contains(solt)) {
                rsSolt = solt;
                break;
            }
        }
        return rsSolt;
    }

    /**
     * 创建仓库中新的丹药对象
     * 
     * @param guid
     * @param userRoleId
     * @param solt
     * @param goodsId
     * @param goodsNum
     * @return
     */
    private RoleXinmoLiandanItem createRoleXinmoLiandanItem(Long guid, Long userRoleId, Integer solt, String goodsId, Integer goodsNum) {
        long nowTimestamp = GameSystemTime.getSystemMillTime();
        RoleXinmoLiandanItem roleXinmoLiandanItem = new RoleXinmoLiandanItem();
        roleXinmoLiandanItem.setGuid(guid);
        roleXinmoLiandanItem.setUserRoleId(userRoleId);
        roleXinmoLiandanItem.setSolt(solt);
        roleXinmoLiandanItem.setGoodsId(goodsId);
        roleXinmoLiandanItem.setGoodsNum(goodsNum);
        roleXinmoLiandanItem.setCreateTime(nowTimestamp);
        roleXinmoLiandanItem.setUpdateTime(nowTimestamp);
        roleXinmoLiandanItemDao.cacheInsert(roleXinmoLiandanItem, userRoleId);
        return roleXinmoLiandanItem;
    }

    /**
     * 删除仓库中的丹药数据
     * 
     * @param removeObj
     * @return
     */
    private boolean removeRoleXinmoLiandanItem(RoleXinmoLiandanItem removeObj) {
        if (null == removeObj) {
            return false;
        }
        return roleXinmoLiandanItemDao.cacheDelete(removeObj.getGuid(), removeObj.getUserRoleId()) > 0;
    }

    /**
     * 创建新的心魔炼丹数据对象
     * 
     * @param userRoleId
     * @return
     */
    private RoleXinmoLiandan createRoleXinmoLiandan(Long userRoleId, int liandanId, int openSolt) {
        long nowTimestamp = GameSystemTime.getSystemMillTime();
        RoleXinmoLiandan roleXinmoLiandan = new RoleXinmoLiandan();
        roleXinmoLiandan.setUserRoleId(userRoleId);
        roleXinmoLiandan.setLiandanId(liandanId);
        roleXinmoLiandan.setOpenSolt(openSolt);
        roleXinmoLiandan.setCreateTime(nowTimestamp);
        roleXinmoLiandan.setUpdateTime(nowTimestamp);
        roleXinmoLiandanDao.cacheInsert(roleXinmoLiandan, userRoleId);
        return roleXinmoLiandan;
    }

    /**
     * 创建客户端vo对象
     * 
     * @param roleXinmoLiandanItem
     * @return
     */
    private Object[] createClientVoObject(RoleXinmoLiandanItem roleXinmoLiandanItem) {
        return roleXinmoLiandanItem == null ? null : new Object[] { roleXinmoLiandanItem.getGoodsId(), roleXinmoLiandanItem.getGuid(), roleXinmoLiandanItem.getSolt(), roleXinmoLiandanItem.getGoodsNum() };
    }

    /**
     * 初始化玩家心魔炼丹数据
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleXinmoLiandan> initRoleXinmoLiandan(Long userRoleId) {
        return roleXinmoLiandanDao.initRoleXinmoLiandan(userRoleId);
    }

    /**
     * 初始化玩家丹药仓库所有丹药数据
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleXinmoLiandanItem> initRoleXinmoLiandanItemList(Long userRoleId) {
        return roleXinmoLiandanItemDao.initRoleXinmoLiandanItem(userRoleId);
    }

    /**
     * 心魔炼丹定时器开始
     * 
     * @param userRoleId
     */
    public void xinmoLianDanScheduleBegin(Long userRoleId) {
        if (!isOpenLianDan(userRoleId)) {
            return;
        }
        RoleXinmoLiandan roleXinmoLiandan = getCacheRoleXinmoLiandan(userRoleId);
        if (null == roleXinmoLiandan) {
            TianLuLianDanBiaoConfig config = tianLuLianDanBiaoConfigExport.loadById(XinmoConstants.XM_LIANDAN_INIT_ID);
            if (null == config) {
                return;
            }
            roleXinmoLiandan = createRoleXinmoLiandan(userRoleId, config.getId(), config.getGiveSolt());
        }
        TianLuLianDanBiaoConfig config = tianLuLianDanBiaoConfigExport.loadById(roleXinmoLiandan.getLiandanId());
        if (null == config) {
            return;
        }
        if(roleXinmoLiandan.getLianDanRunnableState() == GameConstants.XM_LIANDAN__RUNNABLE_STATE_RUN){
            return;
        }
        Integer liandanItemSolt = getLiandanItemSolt(userRoleId, roleXinmoLiandan.getOpenSolt());
        if (null == liandanItemSolt) {
            return;
        }
        roleXinmoLiandan.setLianDanRunnableState(GameConstants.XM_LIANDAN__RUNNABLE_STATE_RUN);
        roleXinmoLiandanDao.cacheUpdate(roleXinmoLiandan, userRoleId);
        BusTokenRunable runable = new BusTokenRunable(userRoleId, InnerCmdType.XM_LIANDAN_DINGSHI, null);
        scheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPEONENT_DANYAN_DINGSHI_PRODUCE, runable, config.getTiming(), TimeUnit.SECONDS);
    }

    /**
     * 心魔炼丹定时器结束
     * 
     * @param userRoleId
     */
    public void xinmoLianDanScheduleEnd(Long userRoleId) {
        scheduleExportService.cancelSchedule(userRoleId.toString(), GameConstants.COMPEONENT_DANYAN_DINGSHI_PRODUCE);
    }

    /**
     * 开始生产炼丹
     * 
     * @param userRoleId
     */
    public void produceLianDan(Long userRoleId) {
        RoleXinmoLiandan roleXinmoLiandan = getCacheRoleXinmoLiandan(userRoleId);
        if (null == roleXinmoLiandan) {
            return;
        }
        /* 丹炉编号 */
        int liandanId = roleXinmoLiandan.getLiandanId();
        TianLuLianDanBiaoConfig liandanConfig = tianLuLianDanBiaoConfigExport.loadById(liandanId);
        if (null == liandanConfig) {
            return;
        }
        roleXinmoLiandan.setLianDanRunnableState(GameConstants.XM_LIANDAN_RUNNABLE_STATE_OVER);
        roleXinmoLiandanDao.cacheUpdate(roleXinmoLiandan, userRoleId);
        /* 丹药在仓库中的格位号 */
        Integer liandanItemSolt = getLiandanItemSolt(userRoleId, roleXinmoLiandan.getOpenSolt());
        if (null == liandanItemSolt) {
            return;
        }
        /* 丹炉产出物品集合 */
        Map<String, Integer> itemMap = liandanConfig.getItemConfig();
        if (ObjectUtil.isEmpty(itemMap)) {
            return;
        }
        /* 随机产出物品编号 */
        String goodsId = Lottery.getRandomKeyByInteger(itemMap);
        GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
        if (null == goodsConfig) {
            return;
        }
        long guid = IdFactory.getInstance().generateId(ServerIdType.COMMON);
        RoleXinmoLiandanItem roleXinmoLianItem = createRoleXinmoLiandanItem(guid, userRoleId, liandanItemSolt, goodsId, XinmoConstants.XM_LIANDAN_DANYAN_NUM);
        /* 通知客户端 */
        Object result = createClientVoObject(roleXinmoLianItem);
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_LIANDAN_PRODUCE, result);
        xinmoLianDanScheduleBegin(userRoleId);
    }

    /**
     * 获取心魔炼丹信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] xinmoLiandanGetInfo(Long userRoleId) {
        RoleXinmoLiandan roleXinmoLiandan = getCacheRoleXinmoLiandan(userRoleId);
        if (null == roleXinmoLiandan) {
            return null;
        }
        Object[] result = new Object[3];
        int idx = 0;
        List<Object[]> itemVoArray = null;
        List<RoleXinmoLiandanItem> xinmoLiandanList = getAllRoleXinmoLiandanItem(userRoleId);
        if (!ObjectUtil.isEmpty(xinmoLiandanList)) {
            itemVoArray = new ArrayList<>();
            for (int i = 0; i < xinmoLiandanList.size(); i++) {
                itemVoArray.add(createClientVoObject(xinmoLiandanList.get(i)));
            }
        }
        result[idx++] = null == itemVoArray ? null : itemVoArray.toArray();
        result[idx++] = roleXinmoLiandan.getOpenSolt();
        result[idx++] = roleXinmoLiandan.getLiandanId();
        return result;
    }

    /**
     * 升级心魔天炉炼丹等级
     * 
     * @param userRoleId
     * @return
     */
    public Object[] xinmoLiandanShengji(Long userRoleId) {
        RoleXinmoLiandan roleXinmoLiandan = getCacheRoleXinmoLiandan(userRoleId);
        if (null == roleXinmoLiandan) {
            return null;
        }
        int liandanId = roleXinmoLiandan.getLiandanId();
        /* 丹炉最大编号检验 */
        if (liandanId >= tianLuLianDanBiaoConfigExport.getMaxConfigId()) {
            return AppErrorCode.XM_LIANDAN_LEVEL_MAX;
        }
        TianLuLianDanBiaoConfig liandanConfig = tianLuLianDanBiaoConfigExport.loadById(liandanId);
        if (null == liandanConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        int needGold = liandanConfig.getNeedGold();
        // 元宝校验
        Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, needGold, userRoleId);
        if (null != goldError) {
            return new Object[] { AppErrorCode.FAIL, AppErrorCode.YB_ERROR };
        }
        // 扣除元宝
        if (needGold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, needGold, userRoleId, LogPrintHandle.CONSUME_XM_LIANDAN_SHENGJI, true, LogPrintHandle.CBZ_XM_LIANDAN_SHENGJI);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, needGold, LogPrintHandle.CONSUME_XM_LIANDAN_SHENGJI, QQXiaoFeiType.CONSUME_XM_LIANDAN_SHENGJI, 1 });
            }
        }
        // 更新数据
        TianLuLianDanBiaoConfig newLiandanConfig = tianLuLianDanBiaoConfigExport.loadById(++liandanId);
        roleXinmoLiandan.setLiandanId(newLiandanConfig.getId());
        roleXinmoLiandan.setOpenSolt(newLiandanConfig.getGiveSolt());
        roleXinmoLiandanDao.cacheUpdate(roleXinmoLiandan, userRoleId);
        
        return new Object[] { AppErrorCode.SUCCESS, newLiandanConfig.getId() };
    }

    /**
     * 心魔炼丹:开启丹药
     * 
     * @param userRoleId
     * @param guid
     * @return
     */
    public Object[] xinmoLiandanOpen(Long userRoleId, long guid) {
        if (0 >= guid) {
            return AppErrorCode.PARAMETER_ERROR;
        }
        RoleXinmoLiandanItem roleXinmoLiandanItem = getCacheRoleXinmoLiandanItemByGuid(userRoleId, guid);
        if (null == roleXinmoLiandanItem) {
            return AppErrorCode.XM_LIANDAN_NOT_DANYAO;
        }
        GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleXinmoLiandanItem.getGoodsId());
        if (null == goodsConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        /* 未知丹药类型校验 */
        if (GoodsCategory.XM_LIANDAN_ITEM != goodsConfig.getCategory()) {
            return AppErrorCode.XM_LIANDAN_ITEM_ERROR;
        }
        /* 随机丹药 */
        Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(goodsConfig.getData3());
        /* 丹药物品编号 */
        String randomGoodsId = Lottery.getRandomKeyByInteger(itemMap);
        GoodsConfig goodsConfig2 = goodsConfigExportService.loadById(randomGoodsId);
        if (null == goodsConfig2) {
            return AppErrorCode.CONFIG_ERROR;
        }
        /* 丹药物品数量 */
        String goodsNumStr = goodsConfig.getData4();
        if (ObjectUtil.strIsEmpty(goodsNumStr)) {
            return AppErrorCode.CONFIG_ERROR;
        }
        String[] goodsNumStrArray = goodsNumStr.split(GameConstants.CONFIG_SPLIT_CHAR);
        int len = null == goodsNumStrArray ? 0 : goodsNumStrArray.length;
        if (len != XinmoConstants.XM_LIANDAN_CONFIG_GOODS_NUM_LEN) {
            return AppErrorCode.CONFIG_ERROR;
        }
        Integer[] goodsNumArray = new Integer[len];
        for (int idx = 0; idx < len; idx++) {
            goodsNumArray[idx] = Integer.parseInt(goodsNumStrArray[idx]);
        }
        int randomGoodsNum = Lottery.roll(goodsNumArray[0], goodsNumArray[1] + 1);
        /* 更新数据 */
        int solt = roleXinmoLiandanItem.getSolt();
        if(removeRoleXinmoLiandanItem(roleXinmoLiandanItem)){
            roleXinmoLiandanItem = createRoleXinmoLiandanItem(IdFactory.getInstance().generateId(ServerIdType.COMMON), userRoleId, solt, randomGoodsId, randomGoodsNum);
        }
        
        return new Object[] { AppErrorCode.SUCCESS, createClientVoObject(roleXinmoLiandanItem) };
    }
    
    /**
     * 心魔炼丹:取出丹药
     * 
     * @param userRoleId
     * @param guid
     * @return
     */
    public Object[] xinmoLiandanTookOut(Long userRoleId, long guid) {
        if (0 >= guid) {
            return AppErrorCode.PARAMETER_ERROR;
        }
        RoleXinmoLiandanItem roleXinmoLiandanItem = getCacheRoleXinmoLiandanItemByGuid(userRoleId, guid);
        if (null == roleXinmoLiandanItem) {
            return AppErrorCode.XM_LIANDAN_NOT_DANYAO;
        }

        Map<String, Integer> goodsMap = new HashMap<>();
        goodsMap.put(roleXinmoLiandanItem.getGoodsId(), roleXinmoLiandanItem.getGoodsNum());
        Object[] errorCode = roleBagExportService.checkPutInBag(goodsMap, userRoleId);
        if (null != errorCode) {
            return errorCode;
        }
        // 更新数据
        if (removeRoleXinmoLiandanItem(roleXinmoLiandanItem)) {
            xinmoLianDanScheduleBegin(userRoleId);
            roleBagExportService.putGoodsAndNumberAttr(goodsMap, userRoleId, GoodsSource.XM_LIANDAN_TOOK_OUT, LogPrintHandle.GET_XM_LIANDAN_TOOK_OUT, LogPrintHandle.GBZ_XM_LIANDAN_TOOK_OUT, true);
        }
        return new Object[] { AppErrorCode.SUCCESS, guid };
    }
   
    /**
     *  心魔炼丹:整理丹药仓库
     * @param userRoleId
     * @return
     */
    public Object[] xinmoLiandanSort(Long userRoleId) {
        List<Object[]> itemVoArray = null;
        List<RoleXinmoLiandanItem> xinmoLiandanList = getAllRoleXinmoLiandanItem(userRoleId);
        if (!ObjectUtil.isEmpty(xinmoLiandanList)) {
            itemVoArray = new ArrayList<>();
            int beginSole = XinmoConstants.XM_LIANDAN_MIN_SOLT;
            for (int i = 0; i < xinmoLiandanList.size(); i++) {
                RoleXinmoLiandanItem item = xinmoLiandanList.get(i);
                item.setSolt(beginSole + i);
                roleXinmoLiandanItemDao.cacheUpdate(item, userRoleId);
                itemVoArray.add(createClientVoObject(item));
            }
        }
        return null == itemVoArray ? null : itemVoArray.toArray();
    }

    /**
     *  推送通知剩余格位
     * @param userRoleId
     */
    public void xinmoLianDanNotifySolt(Long userRoleId) {
        RoleXinmoLiandan roleXinmoLiandan = getCacheRoleXinmoLiandan(userRoleId);
        if(null == roleXinmoLiandan){
            return ;
        }
        List<RoleXinmoLiandanItem> xinmoLiandanList = getAllRoleXinmoLiandanItem(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_LIANDAN_NOTIFY_SOLT, roleXinmoLiandan.getOpenSolt() - (null == xinmoLiandanList ? 0 : xinmoLiandanList.size()));
    }

    // *******************************************心魔:魔神*****************************************//
    
    /**
     * 获取玩家所有心魔魔神数据列表
     * 
     * @param userRoleId
     * @return
     */
    private List<RoleXinmoMoshen> getRoleXinmoMoshenList(Long userRoleId) {
        return roleXinmoMoshenDao.cacheLoadAll(userRoleId);
    }

    /**
     * 根据魔神类型获取玩家所有心魔魔神数据对象
     * @param userRoleId
     * @param id
     * @return
     */
    private RoleXinmoMoshen getRoleXinmoMoshenByType(Long userRoleId, int type) {
        List<RoleXinmoMoshen> xinmoMoshenList = roleXinmoMoshenDao.cacheLoadAll(userRoleId, new XinmoMoshenFilter(type));
        if (ObjectUtil.isEmpty(xinmoMoshenList)) {
            return null;
        }
        return xinmoMoshenList.get(0);
    }

    /**
     * 创建新的心魔魔神数据对象
     * @param userRoleId
     * @param typeVal
     * @param rankVal
     * @return
     */
    private RoleXinmoMoshen createRoleXinmoMoshen(Long userRoleId, int typeVal, int rankVal, int resetHour) {
        long now_time = GameSystemTime.getSystemMillTime();
        RoleXinmoMoshen roleXinmoMoshen = new RoleXinmoMoshen();
        roleXinmoMoshen.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
        roleXinmoMoshen.setUserRoleId(userRoleId);
        roleXinmoMoshen.setXmMoshenType(typeVal);
        roleXinmoMoshen.setXmMoshenRank(rankVal);
        roleXinmoMoshen.setBlessValue(0);
        roleXinmoMoshen.setBlessClearTime(resetHour <= 0 ? 0 : DatetimeUtil.addHours(now_time, resetHour));
        roleXinmoMoshen.setCreateTime(now_time);
        roleXinmoMoshen.setUpdateTime(now_time);
        roleXinmoMoshenDao.cacheInsert(roleXinmoMoshen, userRoleId);
        return roleXinmoMoshen;
    }
    
    /**
     *  获取心魔-魔神祝福值 
     * @param roleXinmoMoshen
     * @param config
     * @return
     */
    private int getRoleXinmoMoshenBlessVal(RoleXinmoMoshen roleXinmoMoshen, XinmoMoshenJiChuBiaoConfig config) {
        if (null == roleXinmoMoshen) {
            return 0;
        }
        if (config != null && config.isReset()) {
            long now_time = GameSystemTime.getSystemMillTime();
            long clear_time = roleXinmoMoshen.getBlessClearTime();
            if (clear_time > 0 && now_time >= clear_time) {
                roleXinmoMoshen.setBlessValue(0);
                roleXinmoMoshen.setBlessClearTime(0L);
                roleXinmoMoshen.setUpdateTime(now_time);
                roleXinmoMoshenDao.cacheUpdate(roleXinmoMoshen, roleXinmoMoshen.getUserRoleId());
            }
        }
        return roleXinmoMoshen.getBlessValue();
    }
    
    /**
     * 推送内部场景心魔-魔神属性变化
     * @param userRoleId
     * @param busMsgQueue 
     */
    private void notifyStageXinmoMoshenAttrChange(Long userRoleId, BusMsgQueue busMsgQueue){
        busMsgQueue.addStageMsg(userRoleId, InnerCmdType.INNER_XM_MOSHEN_CHARGE, getXinmoMoshenAttrs(userRoleId));
    }
    
    /**
     * 推送内部场景心魔-魔神噬体属性变化
     * @param userRoleId
     */
    private void notifyStageXinmoMoshenShitiAttrChange(Long userRoleId, BusMsgQueue busMsgQueue) {
        Map<String, Long> xm_shiti_attrMap = getXinmoMoshenShitiAttrs(userRoleId);
        if (!ObjectUtil.isEmpty(xm_shiti_attrMap)) {
            ObjectUtil.longMapAdd(xm_shiti_attrMap, getXinmoShitiSkillAttrs(userRoleId));
            busMsgQueue.addStageMsg(userRoleId, InnerCmdType.INNER_XM_MOSHEN_SHITI_CHARGE, new Object[] { false, xm_shiti_attrMap });
        }
    }
    
    /**
     * 心魔-魔神全服公告 
     * @param busMsgQueue
     * @param code
     * @param parameter
     */
    private void xinmoMoshenNotice(BusMsgQueue busMsgQueue, int code, Object parameter) {
        busMsgQueue.addBroadcastMsg(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { code, parameter });
    }
    
    /**
     * 记录心魔-魔神升阶日志
     * @param userRoleId
     * @param oldConfigId
     * @param newConfigId
     * @param consumeItemArray
     * @param consumeJBVal
     * @param consumeYBVal
     * @param consumeBYBVal
     * @param newBlessVal
     */
    private void recordXinmoMoshenLog(Long userRoleId,Integer oldConfigId, Integer newConfigId, JSONArray consumeItemArray, Integer consumeJBVal, Integer consumeYBVal, Integer consumeBYBVal, Integer newBlessVal){
        RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
        GamePublishEvent.publishEvent(new XinmoMoshenLogEvent(userRoleId, roleWrapper.getName(), oldConfigId, newConfigId, consumeItemArray, consumeJBVal,consumeYBVal, consumeBYBVal, newBlessVal));
    }
    
    /**
     * 开启心魔-魔神噬体解体定时器 
     * @param userRoleId
     * @param delay
     */
    private void xinmoMoshenScheduleBegin(Long userRoleId, int delay) {
        if (delay <= 0) {
            return ;
        }
        // 开启心魔-魔神噬体解体定时器
        BusTokenRunable runable = new BusTokenRunable(userRoleId, InnerCmdType.XM_MOSHEN_SHITI_CD, null);
        scheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPEONENT_XM_SHITI_CD_PRODUCE, runable, delay, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 取消心魔-魔神噬体解体定时器 
     * @param userRoleId
     * @param delay
     */
    public void xinmoMoshenScheduleEnd(Long userRoleId) {
        scheduleExportService.cancelSchedule(userRoleId.toString(), GameConstants.COMPEONENT_XM_SHITI_CD_PRODUCE);
    }
    
    /**
     *  初始化玩家心魔数据到缓存
     * @param userRoleId 
     * @return
     */
    public List<RoleXinmoMoshen> initRoleXinmoMoshen(Long userRoleId) {
        return roleXinmoMoshenDao.initRoleXinmoMoshen(userRoleId);
    }

    /**
     *  初始化玩家心魔噬体数据到缓存
     * @param userRoleId
     * @return
     */
    public List<RoleXinmoMoshenShiti> initRoleXinmoMoshenShiti(Long userRoleId) {
        return roleXinmoMoshenShitiDao.initRoleXinmoMoshenShiti(userRoleId);
    }
    
    /**
     * 上线推送心魔-噬体魔神的id到客户端
     * @param userRoleId
     */
    public void onlineSendXinmoMoshenShitiId(Long userRoleId) {
        RoleXinmoMoshenShiti roleXinmoMoshenShiti = roleXinmoMoshenShitiDao.cacheLoad(userRoleId, userRoleId);
        if (null == roleXinmoMoshenShiti) {
            return;
        }
        XinmoMoshenPublicConfig xinmoMoshenConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_XINMO_MOSHEN);
        if (null == xinmoMoshenConfig) {
            return;
        }
        int moshenId = roleXinmoMoshenShiti.getXinmoMoshenId();
        if (0 >= moshenId) {
            return;
        }
        long now_time = GameSystemTime.getSystemMillTime();
        long pass_time = now_time - roleXinmoMoshenShiti.getShitiTime();
        long xinmoChiXuCd = xinmoMoshenConfig.getXinMoChiXuCd() * DatetimeUtil.SECOND_MILLISECOND;
        if (pass_time >= xinmoChiXuCd) {// 解体时间到
            roleXinmoMoshenShiti.setXinmoMoshenId(0);
            roleXinmoMoshenShiti.setUpdateTime(now_time);
            roleXinmoMoshenShitiDao.cacheUpdate(roleXinmoMoshenShiti, userRoleId);
        } else {// 在噬体持续cd时间内
            xinmoMoshenScheduleBegin(userRoleId, (int) (xinmoChiXuCd - pass_time));
            // 推送当前噬体魔神id到客户端
//            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_MOSHEN_SNED_SHITI_ID, moshenId);
        }
    }

    /**
     *  获取玩家心魔-魔神的加成属性集合
     * @param userRoleId
     * @return
     */
    public Map<String, Long> getXinmoMoshenAttrs(Long userRoleId) {
        List<RoleXinmoMoshen> xinmoMoshenList  = roleXinmoMoshenDao.cacheLoadAll(userRoleId);
        if(ObjectUtil.isEmpty(xinmoMoshenList)){
            return null;
        }
        Map<String, Long> attrMap = new HashMap<>();
        for(RoleXinmoMoshen xinmoMoshen : xinmoMoshenList){
            XinmoMoshenJiChuBiaoConfig config = xinmoMoshenConfigExport.loadJichuConfigByTypeAndRank(xinmoMoshen.getXmMoshenType(), xinmoMoshen.getXmMoshenRank());
            if(null == config){
                continue;
            }
            ObjectUtil.longMapAdd(attrMap, config.getAttrMap());
        }
        return attrMap;
    }

    /**
     *  获取玩家心魔-魔神噬体的加成属性集合
     * @param userRoleId
     * @return
     */
    public Map<String, Long> getXinmoMoshenShitiAttrs(Long userRoleId) {
        RoleXinmoMoshenShiti roleXinmoMoshenShiti = roleXinmoMoshenShitiDao.cacheLoad(userRoleId, userRoleId);
        if(null == roleXinmoMoshenShiti){
            return null;
        }
        Map<String, Long> xm_shiti_attrMap = new HashMap<>();
        XinmoMoshenShitiBiaoConfig config = xinmoMoshenConfigExport.loadShitiConfigById(roleXinmoMoshenShiti.getXinmoMoshenId());
        if(null != config){
            ObjectUtil.longMapAdd(xm_shiti_attrMap, config.getAttrMap());
        }
        return xm_shiti_attrMap;
    }

    /**
     * 获取玩家心魔-魔神的总战斗力 
     * @param userRoleId
     * @return
     * @throws:
     * @Author: Yang Gao
     * @date: 2017-1-16 下午12:47:39 
     */
    public Long getRoleXinmoMoshenTotalZplus(Long userRoleId){
        Map<String, Long> attr = getXinmoMoshenAttrs(userRoleId);
        if(attr == null || attr.size() ==0 ||! attr.containsKey(EffectType.zplus.name())){
            return 0l;
        }
        
        return attr.get(EffectType.zplus.name());
    }
    
    /**
     * 请求已激活的心魔魔神信息
     * @param userRoleId
     * @return
     */
    public Object[] getXinmoMoshenInfo(Long userRoleId) {
        List<RoleXinmoMoshen> xinmoMoshenList = getRoleXinmoMoshenList(userRoleId);
        if (ObjectUtil.isEmpty(xinmoMoshenList)) {
            return null;
        }
        List<Object[]> result = new ArrayList<>();
        for (RoleXinmoMoshen xinmoMoshen : xinmoMoshenList) {
            XinmoMoshenJiChuBiaoConfig config = xinmoMoshenConfigExport.loadJichuConfigByTypeAndRank(xinmoMoshen.getXmMoshenType(), xinmoMoshen.getXmMoshenRank());
            if (null == config) {
                continue;
            }
            result.add(new Object[] { config.getType(), config.getId(), getRoleXinmoMoshenBlessVal(xinmoMoshen, config), xinmoMoshen.getBlessClearTime() });
        }
        long shitiEnd = 0;
        long shitiChiXuEnd = 0;
        Integer xmShitiId = null;
        RoleXinmoMoshenShiti roleXinmoMoshenShiti = roleXinmoMoshenShitiDao.cacheLoad(userRoleId, userRoleId);
        if (null != roleXinmoMoshenShiti) {
            XinmoMoshenPublicConfig xinmoMoshenConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_XINMO_MOSHEN);
            if (null != xinmoMoshenConfig) {
                    long now_time = GameSystemTime.getSystemMillTime();
                    long shiti_time = roleXinmoMoshenShiti.getShitiTime();
                    // 计算噬体冷却结束时间戳
                    long xinmoCdEnd = shiti_time + xinmoMoshenConfig.getXinMoCd() * DatetimeUtil.SECOND_MILLISECOND;
                    if (xinmoCdEnd > now_time) {
                        shitiEnd = xinmoCdEnd;
                    }                    
                    if (roleXinmoMoshenShiti.getXinmoMoshenId()> 0) {
                        // 计算噬体解体结束时间戳
                        long xinmoChiXuEnd = shiti_time + xinmoMoshenConfig.getXinMoChiXuCd() * DatetimeUtil.SECOND_MILLISECOND;
                        if (xinmoChiXuEnd > now_time) {
                            xmShitiId = roleXinmoMoshenShiti.getXinmoMoshenId();
                            shitiChiXuEnd = xinmoChiXuEnd;
                        }
                    }
                }
        }
        return new Object[] { result.toArray(), shitiEnd, shitiChiXuEnd, xmShitiId};
    }

    /**
     * 心魔-魔神激活
     * 
     * @param userRoleId
     * @param type
     * @param isAutoGM
     * @param busMsgQueue 
     * @return
     */
    public void xinmoMoshenActivate(Long userRoleId, Integer type, Boolean isAutoGM, BusMsgQueue busMsgQueue) {
        short requestCmd = ClientCmdType.XM_MOSHEN_ACTIVATE;
        if (null == type || type.intValue() <= 0 || null == isAutoGM) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.PARAMETER_ERROR);
            return;
        }
        int typeVal = type.intValue();
        RoleXinmoMoshen oldXinmoMoshen = getRoleXinmoMoshenByType(userRoleId, typeVal);
        if (null != oldXinmoMoshen) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.XM_MOSHEN_HAS_ACTITY);
            return;
        }
        int rankVal = XinmoConstants.XM_MOSHEN_INIT_RANK;
        XinmoMoshenJiChuBiaoConfig config = xinmoMoshenConfigExport.loadJichuConfigByTypeAndRank(typeVal, rankVal);
        if (config == null) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
            return;
        }
        /* 银两消耗检验 */
        int money = config.getMoney();
        if (money > 0) {
            Object[] isOb = roleBagExportService.isEnought(GoodsCategory.MONEY, money, userRoleId);
            if (null != isOb) {
                busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.JB_ERROR);
                return;
            }
        }
        // 激活需要道具
        Map<String, Integer> tempResources = new HashMap<>();
        List<String> needGoodsIds = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(config.getItemId());
        int needCount = config.getItemCount();
        Object[] goldObj = new Object[2];
        for (String goodsId : needGoodsIds) {
            int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
            if (owerCount >= needCount) {
                tempResources.put(goodsId, needCount);
                needCount = 0;
                break;
            }else if(owerCount > 0){
                needCount = needCount - owerCount;
                tempResources.put(goodsId, owerCount);
            }
        }
        int newNeedGold = 0;// 消耗的元宝
        int newNeedBgold = 0;// 消耗的绑定元宝
        if (needCount > 0) {
            if (!isAutoGM) {
                busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.XM_MOSHEN_NO_ITEM);
                return;
            }
            int bPrice = config.getBgold();// 绑定元宝的价格
            if (bPrice < 1) {
                busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
                return;
            }
            int bCount = 0;
            int nowNeedBgold = 0;
            for (int i = 0; i < needCount; i++) {
                nowNeedBgold = (bCount + 1) * bPrice;
                Object[] bgoldError = roleBagExportService.isEnought(GoodsCategory.BGOLD, nowNeedBgold, userRoleId);
                if (null != bgoldError) {
                    break;
                }
                bCount++;
            }
            nowNeedBgold = bCount * bPrice;
            goldObj[0] = nowNeedBgold;
            newNeedBgold = nowNeedBgold;
            needCount = needCount - bCount;

            int price = config.getGold();// 元宝的价格
            if (price < 1) {
                busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
                return;
            }
            int nowNeedGold = needCount * price;
            Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, nowNeedGold, userRoleId);
            if (null != goldError) {
                busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.YB_ERROR);
                return;
            }
            goldObj[1] = nowNeedGold;
            newNeedGold = nowNeedGold;
            needCount = 0;
        }
        // 扣除银两
        if (money > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.CONSUME_XM_MOSHEN_JIHUO, true, LogPrintHandle.CBZ_XM_MOSHEN_JIHUO);
        }
        // 扣除元宝
        if (newNeedGold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId, LogPrintHandle.CONSUME_XM_MOSHEN_JIHUO, true, LogPrintHandle.CBZ_XM_MOSHEN_JIHUO);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                busMsgQueue.addBusMsg(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, newNeedGold, String.valueOf(LogPrintHandle.CONSUME_XM_MOSHEN_JIHUO), QQXiaoFeiType.CONSUME_XM_MOSHEN_JIHUO, 1 });
            }
        }
        // 扣除绑定元宝
        if (newNeedBgold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId, LogPrintHandle.CONSUME_XM_MOSHEN_JIHUO, true, LogPrintHandle.CBZ_XM_MOSHEN_JIHUO);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                busMsgQueue.addBusMsg(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_BYB, newNeedBgold, String.valueOf(LogPrintHandle.CONSUME_XM_MOSHEN_JIHUO), QQXiaoFeiType.CONSUME_XM_MOSHEN_JIHUO, 1 });
            }
        }
        // 扣除道具
        BagSlots errorCode = roleBagExportService.removeBagItemByGoods(tempResources, userRoleId, GoodsSource.XM_MOSHEN_JH, true, true);
        if (!errorCode.isSuccee()) {
            busMsgQueue.addMsg(userRoleId, requestCmd, errorCode.getErrorCode());
            return;
        }
        // 更新数据
        ++rankVal;
        XinmoMoshenJiChuBiaoConfig nextConfig = xinmoMoshenConfigExport.loadJichuConfigByTypeAndRank(typeVal, rankVal);
        if (null == nextConfig) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
            return;
        }
        createRoleXinmoMoshen(userRoleId, nextConfig.getType(), nextConfig.getRank(), nextConfig.getResetHour());
        // 全服通告
        if(nextConfig.getNoticeCode() > 0){
            UserRole userRole = roleExportService.getUserRole(userRoleId);
            xinmoMoshenNotice(busMsgQueue, nextConfig.getNoticeCode(), new Object[]{userRole.getName(), nextConfig.getRank()});
        }
        // 通知属性变化
        notifyStageXinmoMoshenAttrChange(userRoleId, busMsgQueue);
        busMsgQueue.addMsg(userRoleId, requestCmd, new Object[] { AppErrorCode.SUCCESS, type, nextConfig.getId(), goldObj });
    }

    /**
     *  心魔-魔神升阶
     * @param userRoleId
     * @param type
     * @param isAutoGM
     * @param busMsgQueue
     */
    public void xinmoMoshenUpLvl(Long userRoleId, Integer type, Boolean isAutoGM, BusMsgQueue busMsgQueue) {
        short requestCmd = ClientCmdType.XM_MOSHEN_UP_LEVEL;
        if (null == type || type.intValue() <= 0 || null == isAutoGM) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.PARAMETER_ERROR);
            return;
        }
        int typeVal = type.intValue();
        RoleXinmoMoshen roleXinmoMoshen = getRoleXinmoMoshenByType(userRoleId, typeVal);
        if (null == roleXinmoMoshen) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.XM_MOSHEN_NOT_ACTITY);
            return;
        }
        int rankVal = roleXinmoMoshen.getXmMoshenRank();
        int maxRank = xinmoMoshenConfigExport.getXinmoMoshenMaxRankByType(typeVal);
        // 最大阶级校验
        if(rankVal >= maxRank){
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.XM_MOSHEN_MAX_RANK);
            return;
        }
        XinmoMoshenJiChuBiaoConfig config = xinmoMoshenConfigExport.loadJichuConfigByTypeAndRank(typeVal, rankVal);
        if (config == null) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
            return;
        }
        // 凝神元神境界阶级校验
        int needNingshenRank = config.getNingshenRank();
        if(needNingshenRank > 0){
            int ningshenRank = getRoleXinmoRank(userRoleId);
            if (ningshenRank < needNingshenRank) {
                busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.XM_MOSHEN_NO_NISHEN_RANK);
                return;
            }
        }
        /* 银两消耗检验 */
        int money = config.getMoney();
        if (money > 0) {
            Object[] isOb = roleBagExportService.isEnought(GoodsCategory.MONEY, money, userRoleId);
            if (null != isOb) {
                busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.JB_ERROR);
                return;
            }
        }
        // 道具消耗校验
        Map<String, Integer> tempResources = new HashMap<>();
        List<String> needGoodsIds = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(config.getItemId());
        int needCount = config.getItemCount();
        Object[] goldObj = new Object[2];
        for (String goodsId : needGoodsIds) {
            int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
            if (owerCount >= needCount) {
                tempResources.put(goodsId, needCount);
                needCount = 0;
                break;
            }else if(owerCount > 0){
                needCount = needCount - owerCount;
                tempResources.put(goodsId, owerCount);
            }
        }
        int newNeedGold = 0;// 消耗的元宝
        int newNeedBgold = 0;// 消耗的绑定元宝
        if (needCount > 0) {
            if (!isAutoGM) {
                busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.XM_MOSHEN_NO_ITEM);
                return;
            }
            int bPrice = config.getBgold();// 绑定元宝的价格
            if (bPrice < 1) {
                busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
                return;
            }
            int bCount = 0;
            int nowNeedBgold = 0;
            for (int i = 0; i < needCount; i++) {
                nowNeedBgold = (bCount + 1) * bPrice;
                Object[] bgoldError = roleBagExportService.isEnought(GoodsCategory.BGOLD, nowNeedBgold, userRoleId);
                if (null != bgoldError) {
                    break;
                }
                bCount++;
            }
            nowNeedBgold = bCount * bPrice;
            goldObj[0] = nowNeedBgold;
            newNeedBgold = nowNeedBgold;
            needCount = needCount - bCount;

            int price = config.getGold();// 元宝的价格
            if (price < 1) {
                busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
                return;
            }
            int nowNeedGold = needCount * price;
            Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, nowNeedGold, userRoleId);
            if (null != goldError) {
                busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.YB_ERROR);
                return;
            }
            goldObj[1] = nowNeedGold;
            newNeedGold = nowNeedGold;
            needCount = 0;
        }
        // 扣除银两
        if (money > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.CONSUME_XM_MOSHEN_SHENGJI, true, LogPrintHandle.CBZ_XM_MOSHEN_SHENGJI);
        }
        // 扣除元宝
        if (newNeedGold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId, LogPrintHandle.CONSUME_XM_MOSHEN_SHENGJI, true, LogPrintHandle.CBZ_XM_MOSHEN_SHENGJI);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                busMsgQueue.addBusMsg(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, newNeedGold, String.valueOf(LogPrintHandle.CONSUME_XM_MOSHEN_SHENGJI), QQXiaoFeiType.CONSUME_XM_MOSHEN_SHENGJI, 1 });
            }
        }
        // 扣除绑定元宝
        if (newNeedBgold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId, LogPrintHandle.CONSUME_XM_MOSHEN_SHENGJI, true, LogPrintHandle.CBZ_XM_MOSHEN_SHENGJI);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                busMsgQueue.addBusMsg(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_BYB, newNeedBgold, String.valueOf(LogPrintHandle.CONSUME_XM_MOSHEN_SHENGJI), QQXiaoFeiType.CONSUME_XM_MOSHEN_SHENGJI, 1 });
            }
        }
        // 扣除道具
        BagSlots errorCode = roleBagExportService.removeBagItemByGoods(tempResources, userRoleId, GoodsSource.XM_MOSHEN_SHENGJI, true, true);
        if (!errorCode.isSuccee()) {
            busMsgQueue.addMsg(userRoleId, requestCmd, errorCode.getErrorCode());
            return;
        }
        // 升级结果(true:升阶成功;false=升阶失败)
        boolean upLvlSuccess = false;
        int blessVal = getRoleXinmoMoshenBlessVal(roleXinmoMoshen, config);
        if (blessVal < config.getBlessMinVal()) {
            upLvlSuccess = false;
        } else if (blessVal >= config.getBlessMaxVal()) {
            upLvlSuccess = true;
        } else if (Lottery.roll(config.getSuccessRatio() / (float) Lottery.HUNDRED.getVal(), Lottery.HUNDRED)) {
            upLvlSuccess = true;
        }
        int xmConfigId = config.getId();
        int newXmConfigId = xmConfigId;
        long now_time = GameSystemTime.getSystemMillTime();
        if (upLvlSuccess) {
            blessVal = 0;
            ++rankVal;
            XinmoMoshenJiChuBiaoConfig nextConfig = xinmoMoshenConfigExport.loadJichuConfigByTypeAndRank(typeVal, rankVal);
            if (nextConfig == null) {
                busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
                return;
            }
            newXmConfigId = nextConfig.getId();
            roleXinmoMoshen.setBlessValue(blessVal);
            roleXinmoMoshen.setBlessClearTime(0L);
            roleXinmoMoshen.setXmMoshenRank(nextConfig.getRank());           
            roleXinmoMoshen.setUpdateTime(now_time);
            roleXinmoMoshenDao.cacheUpdate(roleXinmoMoshen, userRoleId);
            // 全服通告
            if(nextConfig.getNoticeCode() > 0){
                UserRole userRole = roleExportService.getUserRole(userRoleId);
                xinmoMoshenNotice(busMsgQueue, nextConfig.getNoticeCode(), new Object[]{userRole.getName(), nextConfig.getRank()});                
            }
            // 场景刷新属性变化
            notifyStageXinmoMoshenAttrChange(userRoleId, busMsgQueue);
        } else {// 升阶失败增加随机祝福值
            blessVal += Lottery.roll(config.getAddBlessMinVal(), config.getAddBlessMaxVal() + 1);
            if(roleXinmoMoshen.getBlessClearTime() ==0 && config.isReset())
                roleXinmoMoshen.setBlessClearTime(DatetimeUtil.addHours(now_time, config.getResetHour()));
            roleXinmoMoshen.setBlessValue(blessVal);
            roleXinmoMoshen.setUpdateTime(now_time);
            roleXinmoMoshenDao.cacheUpdate(roleXinmoMoshen, userRoleId);
        }
        // 日志记录
        recordXinmoMoshenLog(userRoleId, xmConfigId, newXmConfigId, LogPrintHandle.getLogGoodsParam(tempResources, null), money, newNeedGold, newNeedBgold, blessVal);
        busMsgQueue.addMsg(userRoleId, requestCmd, new Object[] { AppErrorCode.SUCCESS, typeVal, newXmConfigId, blessVal, roleXinmoMoshen.getBlessClearTime() });
    }

    /**
     *  心魔-魔神噬体
     * @param userRoleId
     * @param type
     * @param busMsgQueue
     */
    public void xinmoMoshenShiti(Long userRoleId, Integer type, BusMsgQueue busMsgQueue) {
        /* 在心魔斗场副本中不允许执行心魔噬体操作 */
        String stageId = stageControllExportService.getCurStageId(userRoleId);
        IStage stage = StageManager.getStage(stageId);
        if (null != stage && XinmoDouchangFubenStage.class.isInstance(stage)) {
            return;
        }
        short requestCmd = ClientCmdType.XM_MOSHEN_SHITI;
        if (null == type || type.intValue() <= 0) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.PARAMETER_ERROR);
            return;
        }
        int typeVal = type.intValue();
        RoleXinmoMoshen roleXinmoMoshen = getRoleXinmoMoshenByType(userRoleId, typeVal);
        if (null == roleXinmoMoshen) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.XM_MOSHEN_NOT_ACTITY);
            return;
        }
        XinmoMoshenJiChuBiaoConfig config = xinmoMoshenConfigExport.loadJichuConfigByTypeAndRank(roleXinmoMoshen.getXmMoshenType(), roleXinmoMoshen.getXmMoshenRank());
        if (config == null) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
            return;
        }
        XinmoMoshenPublicConfig xinmoPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_XINMO_MOSHEN);
        if (null == xinmoPublicConfig) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
            return;
        }
        int xinmoMoshenId = config.getId();
        long now_time = GameSystemTime.getSystemMillTime();
        RoleXinmoMoshenShiti roleXinmoMoshenShiti = roleXinmoMoshenShitiDao.cacheLoad(userRoleId, userRoleId);
        if(null == roleXinmoMoshenShiti){
            roleXinmoMoshenShiti = new RoleXinmoMoshenShiti();
            roleXinmoMoshenShiti.setUserRoleId(userRoleId);
            roleXinmoMoshenShiti.setXinmoMoshenId(xinmoMoshenId);
            roleXinmoMoshenShiti.setShitiTime(now_time);
            roleXinmoMoshenShiti.setCreateTime(now_time);
            roleXinmoMoshenShiti.setUpdateTime(now_time);
            roleXinmoMoshenShitiDao.cacheInsert(roleXinmoMoshenShiti, userRoleId);
        }else{
            if (roleXinmoMoshenShiti.getXinmoMoshenId() > 0) {
                long pass_time = now_time - roleXinmoMoshenShiti.getShitiTime();
                long xinmoCd = xinmoPublicConfig.getXinMoCd() * DatetimeUtil.SECOND_MILLISECOND;
                if (pass_time <= xinmoCd) {
                    busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.XM_MOSHEN_SHITI_CD);
                    return;
                }
            }
            roleXinmoMoshenShiti.setXinmoMoshenId(xinmoMoshenId);
            roleXinmoMoshenShiti.setShitiTime(now_time);
            roleXinmoMoshenShiti.setUpdateTime(now_time);
            roleXinmoMoshenShitiDao.cacheUpdate(roleXinmoMoshenShiti, userRoleId);
        }
        
        // 噬体成功,开启解体倒计时
        xinmoMoshenScheduleBegin(userRoleId, (int)(xinmoPublicConfig.getXinMoChiXuCd() * DatetimeUtil.SECOND_MILLISECOND));
        // 刷新场景噬体属性
        notifyStageXinmoMoshenShitiAttrChange(userRoleId, busMsgQueue);
        // 返回数据
        busMsgQueue.addMsg(userRoleId, requestCmd, new Object[]{AppErrorCode.SUCCESS, xinmoMoshenId});
    }

    /**
     *  请求心魔-魔神祝福值信息
     * @param userRoleId
     * @param type
     * @return
     */
    public Object[] getXinmoMoshenBlessInfo(Long userRoleId, Integer type) {
        if (null == type || type.intValue() <= 0) {
            return null;
        }
        int typeVal = type.intValue();
        RoleXinmoMoshen roleXinmoMoshen = getRoleXinmoMoshenByType(userRoleId, typeVal);
        if (null == roleXinmoMoshen) {
            return null;
        }
        XinmoMoshenJiChuBiaoConfig config = xinmoMoshenConfigExport.loadJichuConfigByTypeAndRank(roleXinmoMoshen.getXmMoshenType(), roleXinmoMoshen.getXmMoshenRank());
        if (config == null) {
            return null;
        }
        return new Object[] { typeVal, config.getId(), getRoleXinmoMoshenBlessVal(roleXinmoMoshen, config), roleXinmoMoshen.getBlessClearTime() };
    }
    
    /**
     * 心魔-魔神噬体cd持续时间到,解体心魔
     * @param userRoleId
     */
    public void xinmoMoshenShitiCdEnd(Long userRoleId) {
        RoleXinmoMoshenShiti roleXinmoMoshenShiti = roleXinmoMoshenShitiDao.cacheLoad(userRoleId, userRoleId);
        if (null == roleXinmoMoshenShiti) {
            return;
        }
        int xinmoShitiId = roleXinmoMoshenShiti.getXinmoMoshenId();
        if (xinmoShitiId <= 0) {
            return;
        }
        // 更新数据
        roleXinmoMoshenShiti.setXinmoMoshenId(0);
        roleXinmoMoshenShiti.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleXinmoMoshenShitiDao.cacheUpdate(roleXinmoMoshenShiti, userRoleId);
        // 刷新场景属性
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_XM_MOSHEN_SHITI_CHARGE, new Object[] { true, null });
        // 返回数据
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_MOSHEN_SNED_JIETI_SHITI_ID, new Object[] { AppErrorCode.SUCCESS, xinmoShitiId });
    }
    
    // *******************************************心魔:魔神技能*****************************************//
    /**
     * 获取玩家心魔魔神技能集合
     * 
     * @param userRoleId
     * @return
     */
    private List<RoleXinmoSkill> getRoleXmSkills(Long userRoleId) {
        return roleXinmoSkillDao.cacheLoadAll(userRoleId);
    }

    /**
     * 根据心魔类型获取玩家心魔魔神技能对象
     * 
     * @param userRoleId
     * @param type
     * @return
     * 
     */
    private RoleXinmoSkill getRoleXmSkillByType(Long userRoleId, int type) {
        List<RoleXinmoSkill> skill_list = roleXinmoSkillDao.cacheLoadAll(userRoleId, new XinmoSkillFilter(type));
        if (ObjectUtil.isEmpty(skill_list)) {
            return null;
        }
        return skill_list.get(0);
    }

    /**
     * 获取玩家心魔魔神技能信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getXmSkillInfo(Long userRoleId) {
        List<RoleXinmoSkill> roleXmSkills = getRoleXmSkills(userRoleId);
        if (!ObjectUtil.isEmpty(roleXmSkills)) {
            List<Integer> rsList = new ArrayList<>();
            for (RoleXinmoSkill roleXmSkill : roleXmSkills) {
                rsList.addAll(roleXmSkill.findXmSkillIdList());
            }
            return rsList.toArray();
        } else {
            return null;
        }
    }

    /**
     * 玩家升级心魔魔神技能
     * 
     * @param userRoleId
     * @param skillId 要升级的魔神技能配置id
     */
    public Object[] upLevelXmSkill(Long userRoleId, Integer skill_Id) {
        if(null == skill_Id){
            return AppErrorCode.PARAMETER_ERROR;
        }
        int skillId = skill_Id.intValue();
        XinMoJiNengBiaoConfig config = xinMoJiNengBiaoConfigExport.loadById(skillId);
        if (null == config) {
            return AppErrorCode.CONFIG_ERROR;
        }
        int xm_type = config.getXinmoType();
        RoleXinmoSkill roleXmSkill = getRoleXmSkillByType(userRoleId, xm_type);

        /* 技能有效性检验 */
        Integer oldSkillId = null;
        int xm_seq = config.getSeq();
        if (null != roleXmSkill) {
            oldSkillId = roleXmSkill.getXmSkillIdBySeq(xm_seq);
        }
        int oldLevel = 0;
        XinMoJiNengBiaoConfig config2 = xinMoJiNengBiaoConfigExport.loadById(oldSkillId);
        if (null != config2) {
            oldLevel = config2.getLevel();
        }
        /* 最大等级限制校验 */
        int skillMaxLevel = xinMoJiNengBiaoConfigExport.getXmSkillMaxLevel(xm_type, xm_seq);
        if (oldLevel >= skillMaxLevel) {
            return AppErrorCode.XM_SKILL_MAX_LEVEL;
        }
        ++oldLevel;
        int s_level = config.getLevel();
        if (s_level != oldLevel) {
            return AppErrorCode.XM_SKILL_NO_LEARN;
        }

        /* 技能心魔阶级限制校验 */
        RoleXinmoMoshen roleXinmoMoshen = getRoleXinmoMoshenByType(userRoleId, xm_type);
        int xinmoRank = null == roleXinmoMoshen ? 0 : roleXinmoMoshen.getXmMoshenRank();
        if (xinmoRank < config.getXinmoRank()) {
            return AppErrorCode.XM_SKILL_LIMIT_LEVEL;
        }

        /* 道具消耗校验 */
        List<String> goodsIdList = xinMoJiNengBiaoConfigExport.getConsumeIds(config.getItemId());
        if (ObjectUtil.isEmpty(goodsIdList)) {
            return AppErrorCode.CONFIG_ERROR;
        }
        Map<String, Integer> itemMap = new HashMap<>();
        int needCount = config.getItemCount();
        for (String goodsId : goodsIdList) {
            int itemCnt = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
            if (itemCnt >= needCount) {
                itemMap.put(goodsId, needCount);
                needCount = 0;
                break;
            } else if (itemCnt > 0) {
                itemMap.put(goodsId, itemCnt);
                needCount -= itemCnt;
            }
        }
        if (needCount > 0) {
            return AppErrorCode.XM_SKILL_NOT_ENOUGH;
        }

        /* 金币消耗校验 */
        int money = config.getNeedMoney();
        Object[] isOb = roleBagExportService.isEnought(GoodsCategory.MONEY, money, userRoleId);
        if (null != isOb) {
            return AppErrorCode.JB_ERROR;
        }

        /* 扣除金币 */
        if (money > 0) {
            Object[] result = roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.CONSUME_XINMO_SKILL_UPLEVEL, true, LogPrintHandle.CBZ_XINMO_SKILL_UPLEVEL);
            if (result != null) {
                return result;
            }
        }
        /* 扣除道具 */
        BagSlots errorCode = roleBagExportService.removeBagItemByGoods(itemMap, userRoleId, GoodsSource.CONSUME_XM_SKILL_UPLEVEL, true, true);
        if(!errorCode.isSuccee()){
            return errorCode.getErrorCode();
        }
        /* 更新玩家五行技能数据 */
        long nowTime = GameSystemTime.getSystemMillTime();
        if (null == roleXmSkill) {
            roleXmSkill = new RoleXinmoSkill();
            roleXmSkill.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
            roleXmSkill.setUserRoleId(userRoleId);
            roleXmSkill.setXinmoType(xm_type);
            roleXmSkill.updateXmSkillId(xm_seq, skillId);
            roleXmSkill.setCreateTime(nowTime);
            roleXmSkill.setUpdateTime(nowTime);
            roleXinmoSkillDao.cacheInsert(roleXmSkill, userRoleId);
        } else {
            roleXmSkill.updateXmSkillId(xm_seq, skillId);
            roleXmSkill.setUpdateTime(nowTime);
            roleXinmoSkillDao.cacheUpdate(roleXmSkill, userRoleId);
        }
        
        /* 刷新普通:玩家心魔魔神技能附带的属性 */
        int skill_type = config.getSkillType();
        if (XinmoConstants.XM_MOSHEN_SKILL_TYPE_SHITI == skill_type) {
            Map<String, Long> xm_shiti_attrMap = getXinmoMoshenShitiAttrs(userRoleId);
            if (!ObjectUtil.isEmpty(xm_shiti_attrMap)) {
                ObjectUtil.longMapAdd(xm_shiti_attrMap, getXinmoShitiSkillAttrs(userRoleId));
                BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_XM_MOSHEN_SHITI_CHARGE, new Object[] { false, xm_shiti_attrMap });
            }
        } else if (XinmoConstants.XM_MOSHEN_SKILL_TYPE_NORMAL == skill_type) {
            BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_XM_SKILL_CHARGE, getXinmoNormalSkillAttrs(userRoleId));
        }
        return new Object[] { AppErrorCode.SUCCESS, skillId };
    }
    
    /**
     * 初始化玩家心魔魔神技能数据
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleXinmoSkill> initRoleXinmoSkill(Long userRoleId) {
        return roleXinmoSkillDao.initRoleXinmoSkill(userRoleId);
    }

    /**
     * 获取心魔魔神普通技能增加的属性集合
     * @param userRoleId
     * @return
     */
    public Map<String, Long> getXinmoNormalSkillAttrs(Long userRoleId) {
        List<RoleXinmoSkill> roleXmSkillList = getRoleXmSkills(userRoleId);
        if(ObjectUtil.isEmpty(roleXmSkillList)){
            return null;
        }
        Map<String, Long> attrsMap = new HashMap<>();
        for(RoleXinmoSkill roleXmSkill : roleXmSkillList){
            List<Integer> roleXmSkillIds = roleXmSkill.findXmSkillIdList();
            if (!ObjectUtil.isEmpty(roleXmSkillIds)) {
                for (Integer xmSkillId : roleXmSkillIds) {
                    XinMoJiNengBiaoConfig skillConfig = xinMoJiNengBiaoConfigExport.loadById(xmSkillId);
                    if (null == skillConfig || skillConfig.getSkillType() != XinmoConstants.XM_MOSHEN_SKILL_TYPE_NORMAL) {
                        continue;
                    }
                    ObjectUtil.longMapAdd(attrsMap, skillConfig.getAttrsMap());
                }
            }
        }
        
        return attrsMap;
    }

    /**
     * 获取心魔魔神噬体技能增加的属性集合 
     * @param userRoleId
     * @return
     */
    public Map<String, Long> getXinmoShitiSkillAttrs(Long userRoleId) {
        RoleXinmoMoshenShiti roleXinmoMoshenShiti = roleXinmoMoshenShitiDao.cacheLoad(userRoleId, userRoleId);
        if (null == roleXinmoMoshenShiti) {
            return null;
        }
        XinmoMoshenJiChuBiaoConfig config = xinmoMoshenConfigExport.loadJichuConfigById(roleXinmoMoshenShiti.getXinmoMoshenId());
        if(null == config){
            return null;
        }
        RoleXinmoSkill roleXmSkill = getRoleXmSkillByType(userRoleId, config.getType());
        if(null == roleXmSkill){
            return null;
        }
        Map<String, Long> attrsMap = new HashMap<>();
        List<Integer> roleXmSkillIds = roleXmSkill.findXmSkillIdList();
        if (!ObjectUtil.isEmpty(roleXmSkillIds)) {
            for (Integer xmSkillId : roleXmSkillIds) {
                XinMoJiNengBiaoConfig skillConfig = xinMoJiNengBiaoConfigExport.loadById(xmSkillId);
                if (null == skillConfig || skillConfig.getSkillType() != XinmoConstants.XM_MOSHEN_SKILL_TYPE_SHITI) {
                    continue;
                }
                ObjectUtil.longMapAdd(attrsMap, skillConfig.getAttrsMap());
            }
        }
        return attrsMap;
    }

    // ********************************************心魔-洗练***************************************************//
    /**
     * 获取玩家所有心魔洗练数据集合
     * 
     * @param userRoleId
     * @return
     */
    private List<RoleXinmoXilian> getRoleXmXilianList(Long userRoleId) {
        return roleXinmoXilianDao.cacheLoadAll(userRoleId);
    }

    /**
     * 获取玩家单个心魔的洗练数据
     * 
     * @param userRoleId
     * @param xm_type
     * @return
     */
    private RoleXinmoXilian getRoleXmXilian(Long userRoleId, int xm_type) {
        final int xinmo_type = xm_type;
        List<RoleXinmoXilian> list = roleXinmoXilianDao.cacheLoadAll(userRoleId, new IQueryFilter<RoleXinmoXilian>() {
            @Override
            public boolean check(RoleXinmoXilian entity) {
                if (entity.getXinmoType() != null && entity.getXinmoType().equals(xinmo_type)) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean stopped() {
                return false;
            }
        });
        if (ObjectUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 分割数据
     * 
     * @param attr_str
     * @return Object={ 0(int):属性ID, 1(String):属性类型, 2(long):属性值, 3(int):属性品质 }
     */
    private Object[] splitAttrString(String attr_str) {
        if (ObjectUtil.strIsEmpty(attr_str)) {
            return null;
        }
        String[] data = attr_str.split(GameConstants.CONFIG_SPLIT_CHAR);
        return new Object[] { Integer.parseInt(data[0]), data[1], Long.parseLong(data[2]), Integer.parseInt(data[3]) };
    }

    /**
     * 拼接数据
     * 
     * @param attr_str
     * @return Object={ 0(int):属性ID, 1(String):属性类型, 2(long):属性值, 3(int):属性品质 }
     */
    private String mergeAttrString(int attr_id, String attr_type, long attr_val, int attr_rank) {
        return new StringBuilder().append(attr_id).append(XinmoConstants.XM_XILIAN_ATTR_SPLIT).append(attr_type).append(XinmoConstants.XM_XILIAN_ATTR_SPLIT).append(attr_val).append(XinmoConstants.XM_XILIAN_ATTR_SPLIT).append(attr_rank).toString();
    }

    /**
     * 根据索引获取永久base属性 
     * 
     * @param roleXmXilian
     * @param index
     * @return
     */
    public String getBaseAttrStringByIndex(RoleXinmoXilian roleXmXilian, int index) {
        if(null == roleXmXilian) return null;
        switch (index) {
        case 1:
            return roleXmXilian.getBaseAttr1();
        case 2:
            return roleXmXilian.getBaseAttr2();
        case 3:
            return roleXmXilian.getBaseAttr3();
        default:
            return null;
        }
    }
    
    /**
     * 按属性索引更新永久base属性
     * 
     * @param roleXmXilian
     * @param index
     * @param baseAttrString
     */
    public void setBaseAttrStringByIndex(RoleXinmoXilian roleXmXilian, int index, String baseAttrString){
        if(null == roleXmXilian) return ;
        switch (index) {
        case 1:
            roleXmXilian.setBaseAttr1(baseAttrString);
            break;
        case 2:
            roleXmXilian.setBaseAttr2(baseAttrString);
            break;
        case 3:
            roleXmXilian.setBaseAttr3(baseAttrString);
            break;
        default:
            break;
        }
    }
    
    /**
     * 根据索引获取备份back属性
     * 
     * @param roleXmXilian
     * @param index
     * @return
     */
    public String getBackAttrStringByIndex(RoleXinmoXilian roleXmXilian, int index) {
        if(null == roleXmXilian) return null;
        switch (index) {
        case 1:
            return roleXmXilian.getBackAttr1();
        case 2:
            return roleXmXilian.getBackAttr2();
        case 3:
            return roleXmXilian.getBackAttr3();
        default:
            return null;
        }
    }
    
    /**
     * 按属性索引更新备份back属性
     * 
     * @param roleXmXilian
     * @param index
     * @param backAttrString
     */
    public void setBackAttrStringByIndex(RoleXinmoXilian roleXmXilian, int index, String backAttrString){
        if(null == roleXmXilian) return ;
        switch (index) {
        case 1:
            roleXmXilian.setBackAttr1(backAttrString);
            break;
        case 2:
            roleXmXilian.setBackAttr2(backAttrString);
            break;
        case 3:
            roleXmXilian.setBackAttr3(backAttrString);
            break;
        default:
            break;
        }
    }
    
    /**
     * 当前洗练已获得的base永久属性数组对象
     * 
     * @param roleXmXilian
     * @return
     */
    private Object[] getXmXilianBaseAttrData(RoleXinmoXilian roleXmXilian) {
        if (null == roleXmXilian) {
            return null;
        }
        List<Object[]> dataList = new ArrayList<Object[]>();
        for (int idx = 0; idx < XinmoConstants.XM_XILIAN_MAX_BASE_ATTR_NUM; idx++) {
            Object[] baseAttr = splitAttrString(getBaseAttrStringByIndex(roleXmXilian, idx + 1));
            if (null != baseAttr) {
                dataList.add(baseAttr);
            }
        }
        return ObjectUtil.isEmpty(dataList) ? null : dataList.toArray();
    }

    /**
     * 当前洗练已获得的back备份属性数组对象
     * 
     * @param roleXmXilian
     * @return
     */
    private Object[] getXmXilianBackAttrData(RoleXinmoXilian roleXmXilian) {
        if (null == roleXmXilian) {
            return null;
        }
        List<Object[]> dataList = new ArrayList<Object[]>();
        for (int idx = 0; idx < XinmoConstants.XM_XILIAN_MAX_BACK_ATTR_NUM; idx++) {
            Object[] backAttr = splitAttrString(getBackAttrStringByIndex(roleXmXilian, idx + 1));
            if (null != backAttr) {
                dataList.add(backAttr);
            }
        }
        return ObjectUtil.isEmpty(dataList) ? null : dataList.toArray();
    }

    /**
     * 获取单个心魔,洗练附加永久属性map集合
     * 
     * @param roleXmXilian
     * @return
     */
    private Map<String, Long> getXmXilianBaseAttrMap(RoleXinmoXilian roleXmXilian) {
        if(null == roleXmXilian) return null;
        Map<String, Long> baseAttrMap = new HashMap<String, Long>();
        for (int idx = 0; idx < XinmoConstants.XM_XILIAN_MAX_BASE_ATTR_NUM; idx++) {
            Object[] baseAttrData = splitAttrString(getBaseAttrStringByIndex(roleXmXilian, idx + 1));
            if (null != baseAttrData) {
                baseAttrMap.put(String.valueOf(baseAttrData[1]), Long.parseLong(String.valueOf(baseAttrData[2])));
            }
        }
        return baseAttrMap;
    }

    /**
     * 获取单个心魔,洗练附加永久属性的战斗力
     * 
     * @param roleXmXilian
     * @return
     */
    private long getXmXilianZplus(RoleXinmoXilian roleXmXilian) {
        Map<String, Long> baseAttrMap = getXmXilianBaseAttrMap(roleXmXilian);
        if(ObjectUtil.isEmpty(baseAttrMap)) return 0l;
        Float zplus = 0f;
        for (Entry<String, Long> entry : baseAttrMap.entrySet()) {
            Float xs = zhanLiXiShuConfigExportService.getZLXS(entry.getKey());
            if (xs != null && xs.floatValue() > 0) {
                zplus += xs.floatValue() * entry.getValue();
            }
        }
        return zplus.longValue();

    }

    /**
     * 刷新心魔洗练的备份属性数据
     * 
     * @param roleXmXilian
     * @param index
     * @param configId
     */
    private void refreshXmXilianBackAttr(RoleXinmoXilian roleXmXilian, int index, int configId) {
        if (null == roleXmXilian) {
            return;
        }
        XinMoXiLianBiaoConfig config = xinMoXiLianBiaoConfigExport.getConfigById(configId);
        if (null == config) {
            return;
        }
        Object[] attrData = Lottery.getRandomKeyByInteger(config.getInfoMap());
        if (null == attrData) {
            return;
        }
        setBackAttrStringByIndex(roleXmXilian, index, mergeAttrString(config.getId(), config.getAttrType(), RandomUtil.getRondom(Integer.parseInt(String.valueOf(attrData[1])), Integer.parseInt(String.valueOf(attrData[2]))), Integer.parseInt(String.valueOf(attrData[0]))));
    }

    /**
     * 更新心魔洗练所有备份属性数据
     * 
     * @param roleXmXilian
     * @param checkIdList 保留的永久属性id集合(原样替换到对应顺序的备份属性)
     * @param randomIdList 新随机的备份属性id集合
     */
    private void upateXmXilianBackAttrData(RoleXinmoXilian roleXmXilian, List<Integer> checkIdList, List<Integer> randomIdList) {
        if (null == roleXmXilian) {
            return;
        }
        if (ObjectUtil.isEmpty(checkIdList)) {
            for (int idx = 0; idx < XinmoConstants.XM_XILIAN_MAX_BACK_ATTR_NUM; idx++) {
                refreshXmXilianBackAttr(roleXmXilian, idx + 1, randomIdList.get(idx));
            }
        } else {
            for (int idx = 0; idx < XinmoConstants.XM_XILIAN_MAX_BACK_ATTR_NUM; idx++) {
                String baseAttrString = getBaseAttrStringByIndex(roleXmXilian, idx + 1);
                Object[] baseAttrData = splitAttrString(baseAttrString);
                if (null == baseAttrData) {
                    refreshXmXilianBackAttr(roleXmXilian, idx + 1, randomIdList.get(0));
                    randomIdList.remove(0);
                } else {
                    int oldAttrId = Integer.parseInt(String.valueOf(baseAttrData[0]));
                    if (checkIdList.contains(oldAttrId)) {
                        setBackAttrStringByIndex(roleXmXilian, idx + 1, baseAttrString);
                    } else {
                        refreshXmXilianBackAttr(roleXmXilian, idx + 1, randomIdList.get(0));
                        randomIdList.remove(0);
                    }
                }
            }
        }
    }

    /**
     * 初始化心魔洗练数据到内存
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleXinmoXilian> initRoleXinmoXilian(Long userRoleId) {
        return roleXinmoXilianDao.initRoleXinmoXilian(userRoleId);
    }

    /**
     * 获取所有心魔洗练属性集合
     * 
     * @param userRoleId
     * @return
     */
    public Map<String, Long> getAllXinmoXilianAttrs(Long userRoleId) {
        Map<String, Long> baseAttrMap = null;
        List<RoleXinmoXilian> roleXmXilianList = getRoleXmXilianList(userRoleId);
        if (!ObjectUtil.isEmpty(roleXmXilianList)) {
            baseAttrMap = new HashMap<String, Long>();
            long zplus = 0l;
            for (RoleXinmoXilian roleXmXilian : roleXmXilianList) {
                zplus += getXmXilianZplus(roleXmXilian);
                ObjectUtil.longMapAdd(baseAttrMap, getXmXilianBaseAttrMap(roleXmXilian));
            }
            baseAttrMap.put(EffectType.zplus.name(), zplus);
        }
        return baseAttrMap;
    }

    /**
     * 获取心魔洗练面板信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getXinmoXilianInfo(Long userRoleId) {
        List<Object[]> dataList = null;
        List<RoleXinmoXilian> roleXmXilianList = getRoleXmXilianList(userRoleId);
        if (!ObjectUtil.isEmpty(roleXmXilianList)) {
            dataList = new ArrayList<Object[]>();
            for (RoleXinmoXilian roleXmXilian : roleXmXilianList) {
                dataList.add(new Object[] { roleXmXilian.getXinmoType(), getXmXilianZplus(roleXmXilian), getXmXilianBackAttrData(roleXmXilian) == null ? 1 : 2, getXmXilianBaseAttrData(roleXmXilian), getXmXilianBackAttrData(roleXmXilian) });
            }
        }
        return ObjectUtil.isEmpty(dataList) ? null : dataList.toArray();
    }

    /**
     * 心魔洗练
     * 
     * @param userRoleId
     * @param xm_type 心魔类型
     * @param checkAttrIds 已勾选保留的永久base属性ID集合
     * @return
     */
    public Object[] xinmoXilianBegin(Long userRoleId, Integer xm_type, Object checkAttrIds) {
        if (null == xm_type || xm_type.intValue() <= 0) {
            return AppErrorCode.PARAMETER_ERROR;
        }
        /* 数据类型校验,接受转化数据格式 */
        Integer[] checkIdArray = null;
        if (checkAttrIds != null) {
            if (checkAttrIds instanceof Object[]) {
                Object[] checkBaseAttrObj = (Object[]) checkAttrIds;
                checkIdArray = new Integer[checkBaseAttrObj.length];
                System.arraycopy(checkBaseAttrObj, 0, checkIdArray, 0, checkBaseAttrObj.length);
            } else {
                return AppErrorCode.PARAMETER_ERROR;
            }
        }

        int checkNum = 0;// 保留属性个数
        List<Integer> checkIdList = null;// 保留属性id集合
        RoleXinmoXilian roleXmXilian = getRoleXmXilian(userRoleId, xm_type);
        if (null == roleXmXilian) {
            /* 校验心魔是否激活 */
            if (getRoleXinmoMoshenByType(userRoleId, xm_type) == null)
                return AppErrorCode.XM_MOSHEN_NOT_ACTITY;
        } else {
            /* 检验洗练保留的属性 */
            Object[] baseAttrData = getXmXilianBaseAttrData(roleXmXilian);
            if (!ObjectUtil.isEmpty(baseAttrData) && !ObjectUtil.isEmpty(checkIdArray)) {
                /* 转化为集合便于处理 */
                checkIdList = Arrays.asList(checkIdArray);
                /* 检查保留永久属性个数上限 */
                checkNum = checkIdList == null ? 0 : checkIdList.size();
                if (checkNum >= XinmoConstants.XM_XILIAN_MAX_BASE_ATTR_NUM) {
                    return AppErrorCode.XM_XILIAN_BASE_ATTR_UPPER_LIMIT;
                }
                /* 玩家拥有的永久属性id集合 */
                Set<Integer> idHset = new HashSet<Integer>();
                for (Object baseVo : baseAttrData) {
                    if (baseVo instanceof Object[]) {
                        Object[] base = (Object[]) baseVo;
                        idHset.add(Integer.parseInt(String.valueOf(base[0])));
                    }
                }
                /* 保留的洗练属性不存在 */
                boolean checkError = false;
                for (Integer checkId : checkIdList) {
                    if (!idHset.contains(checkId)) {
                        checkError = true;
                        break;
                    }
                }
                if (checkError)
                    return AppErrorCode.XM_XILIAN_BASE_ATTR_NOT;
            }
        }
        /* 校验配置存在 */
        if (xinMoXiLianBiaoConfigExport.configNotExist()) {
            return AppErrorCode.CONFIG_ERROR;
        }
        XinmoXilianPublicConfig xmXilianPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_XINMO_XILIAN);
        if (null == xmXilianPublicConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        /* 校验道具消耗 */
        String itemStr = xmXilianPublicConfig.getItemByCheckNum(checkNum);
        if (ObjectUtil.strIsEmpty(itemStr)) {
            return AppErrorCode.CONFIG_ERROR;
        }
        String[] itemArray = itemStr.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
        String itemId = itemArray[0];// 消耗道具大类id
        int needCnt = Integer.parseInt(itemArray[1]);// 消耗道具数量
        Map<String, Integer> itemMap = new HashMap<String, Integer>();// 记录玩家真实扣除的道具集合
        List<String> goodsIdList = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(itemId);
        if(ObjectUtil.isEmpty(goodsIdList)){
            return AppErrorCode.CONFIG_ERROR;
        }
        for (String goodsId : goodsIdList) {
            int itemCnt = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
            if (itemCnt >= needCnt) {
                itemMap.put(goodsId, needCnt);
                needCnt = 0;
                break;
            } else if (itemCnt > 0) {
                itemMap.put(goodsId, itemCnt);
                needCnt -= itemCnt;
            }
        }
        if (needCnt > 0) {
            return AppErrorCode.XM_XILIAN_NO_ITEM;
        }
        /* 校验银两消耗 */
        int money = xmXilianPublicConfig.getMoney();
        if (money > 0) {
            Object[] isOb = roleBagExportService.isEnought(GoodsCategory.MONEY, money, userRoleId);
            if (null != isOb) {
                return AppErrorCode.JB_ERROR;
            }
        }
        /* 扣除银两 */
        if (money > 0) {
            Object[] result = roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.CONSUME_XINMO_XILIAN, true, LogPrintHandle.CBZ_XINMO_XILIAN);
            if (result != null) {
                return result;
            }
        }
        /* 扣除道具 */
        BagSlots errorCode = roleBagExportService.removeBagItemByGoods(itemMap, userRoleId, GoodsSource.XINMO_XILIAN, true, true);
        if(!errorCode.isSuccee()){
            return errorCode.getErrorCode();
        }
        // 生成随机的属性id
        List<Integer> randomIdList = new ArrayList<Integer>();
        int needNum = XinmoConstants.XM_XILIAN_MAX_BACK_ATTR_NUM - (null == checkIdList ? 0 : checkIdList.size());
        while (needNum > 0) {
            Integer configId = xinMoXiLianBiaoConfigExport.createRandomConfigId();
            if (configId == null) {
                continue;
            }
            // 过滤掉玩家保留的属性id
            if (checkIdList != null && checkIdList.contains(configId)) {
                continue;
            }
            // 过滤掉重复的属性id
            if (randomIdList.contains(configId)) {
                continue;
            }
            randomIdList.add(configId);
            needNum--;
        }
        // 更新数据
        long now_time = GameSystemTime.getSystemMillTime();
        if (null == roleXmXilian) {
            roleXmXilian = new RoleXinmoXilian();
            roleXmXilian.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
            roleXmXilian.setUserRoleId(userRoleId);
            roleXmXilian.setXinmoType(xm_type);
            upateXmXilianBackAttrData(roleXmXilian, checkIdList, randomIdList);
            roleXmXilian.setCreateTime(now_time);
            roleXmXilian.setUpdateTime(now_time);
            roleXinmoXilianDao.cacheInsert(roleXmXilian, userRoleId);
        } else {
            upateXmXilianBackAttrData(roleXmXilian, checkIdList, randomIdList);
            roleXmXilian.setUpdateTime(now_time);
            roleXinmoXilianDao.cacheUpdate(roleXmXilian, userRoleId);
        }
        return new Object[] { AppErrorCode.SUCCESS, xm_type, getXmXilianBackAttrData(roleXmXilian) };
    }

    /**
     * 心魔洗练替换
     * 
     * @param userRoleId
     * @param xm_type
     * @return
     */
    public Object[] xinmoXilianReplace(Long userRoleId, Integer xm_type) {
        if (null == xm_type || xm_type.intValue() <= 0) {
            return AppErrorCode.PARAMETER_ERROR;
        }
        RoleXinmoXilian roleXmXilian = getRoleXmXilian(userRoleId, xm_type);
        if (null == roleXmXilian) {
            return null;
        }
        /* 校验备份属性存在 */
        List<String> backAttrList = new ArrayList<String>();
        for (int idx = 0; idx < XinmoConstants.XM_XILIAN_MAX_BACK_ATTR_NUM; idx++) {
            String backAttr = getBackAttrStringByIndex(roleXmXilian, idx + 1);
            if (!ObjectUtil.strIsEmpty(backAttr)) {
                backAttrList.add(backAttr);
            }

        }
        if(ObjectUtil.isEmpty(backAttrList)){
            return AppErrorCode.XM_XILIAN_NOT_BACK_ATTR;
        }
        /* 更新数据 */
        for (int idx = 0; idx < XinmoConstants.XM_XILIAN_MAX_BACK_ATTR_NUM; idx++) {
            /* 一对一按照顺序将back备份属性替换到base永久属性 */
            setBaseAttrStringByIndex(roleXmXilian, idx + 1, backAttrList.get(idx));
            /* 清空back备份属性数据 */
            setBackAttrStringByIndex(roleXmXilian, idx + 1, "");
        }
        roleXmXilian.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleXinmoXilianDao.cacheUpdate(roleXmXilian, userRoleId);
        /* 刷新场景属性 */
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_XM_XILIAN_CHARGE, getAllXinmoXilianAttrs(userRoleId));
        return new Object[] { AppErrorCode.SUCCESS, xm_type, getXmXilianBaseAttrData(roleXmXilian), getXmXilianZplus(roleXmXilian) };
    }
    
    // --------------------------------------------心魔:斗场副本-------------------------------------------------//
    /**
     * 根据心魔类型获取心魔噬体和心魔技能噬体属性集合
     * 
     * @param userRoleId
     * @param xm_type
     * @return
     */
    public Map<String, Long> getXinmoOrSkillAttrByXmType(Long userRoleId, Integer xm_type) {
        if(null == xm_type) return null;
        Map<String, Long> attrMap = null;
        // 根据类型获取心魔魔神
        RoleXinmoMoshen roleXinmoMoshen = getRoleXinmoMoshenByType(userRoleId, xm_type);
        if (null != roleXinmoMoshen) {
            attrMap = new HashMap<String, Long>();
            // 该类型的心魔魔神的噬体属性
            XinmoMoshenJiChuBiaoConfig config = xinmoMoshenConfigExport.loadJichuConfigByTypeAndRank(roleXinmoMoshen.getXmMoshenType(), roleXinmoMoshen.getXmMoshenType());
            if (null != config) {
                XinmoMoshenShitiBiaoConfig shiti_config = xinmoMoshenConfigExport.loadShitiConfigById(config.getId());
                if (null != shiti_config) {
                    ObjectUtil.longMapAdd(attrMap, shiti_config.getAttrMap());
                }
            }
            // 该类型的心魔魔神技能的噬体属性
            RoleXinmoSkill roleXmSkill = getRoleXmSkillByType(userRoleId, xm_type);
            if (null != roleXmSkill) {
                List<Integer> roleXmSkillIds = roleXmSkill.findXmSkillIdList();
                if (!ObjectUtil.isEmpty(roleXmSkillIds)) {
                    for (Integer xmSkillId : roleXmSkillIds) {
                        XinMoJiNengBiaoConfig skillConfig = xinMoJiNengBiaoConfigExport.loadById(xmSkillId);
                        if (null == skillConfig || skillConfig.getSkillType() != XinmoConstants.XM_MOSHEN_SKILL_TYPE_SHITI) {
                            continue;
                        }
                        ObjectUtil.longMapAdd(attrMap, skillConfig.getAttrsMap());
                    }
                }
            }
        }
        return attrMap;
    }


    
}
