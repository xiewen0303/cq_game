package com.junyou.bus.huajuan2.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.huajuan2.configure.Huajuan2EquipBiaoConfig;
import com.junyou.bus.huajuan2.configure.Huajuan2EquipConfigExportService;
import com.junyou.bus.huajuan2.configure.Huajuan2LieBiaoConfig;
import com.junyou.bus.huajuan2.configure.Huajuan2LieBiaoConfigExportService;
import com.junyou.bus.huajuan2.configure.Huajuan2XinXiBiaoConfig;
import com.junyou.bus.huajuan2.configure.Huajuan2XinXiBiaoConfigExportService;
import com.junyou.bus.huajuan2.configure.JuanZhouXinXiConfig;
import com.junyou.bus.huajuan2.configure.JuanZhouXinXiConfigExportService;
import com.junyou.bus.huajuan2.constants.Huajuan2Constants;
import com.junyou.bus.huajuan2.dao.RoleHuajuan2Dao;
import com.junyou.bus.huajuan2.dao.RoleHuajuan2ExpDao;
import com.junyou.bus.huajuan2.entity.RoleHuajuan2;
import com.junyou.bus.huajuan2.entity.RoleHuajuan2Exp;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.HuaJuan2FenjieLogEvent;
import com.junyou.event.HuaJuan2UpgradeLogEvent;
import com.junyou.event.Huajuan2ActiveLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class Huajuan2Service implements IFightVal {

    @Override
    public long getZplus(long userRoleId, int fightPowerType) {
        RoleHuajuan2Exp roleHuajuanExp = getRoleHuajuan2Exp(userRoleId, false);
        float rate = 0;
        if (null != roleHuajuanExp) {
            Integer level = juanZhouXinXiConfigExportService.calcLevel(roleHuajuanExp.getExp());
            JuanZhouXinXiConfig config = juanZhouXinXiConfigExportService.loadById(level);
                // 等级 比例加成
            rate = (config.getPercent() + 10000) / 10000f;
        }

        if(fightPowerType == FightPowerType.TJ_JH){
            Map<String, Long> jh =  getHuajuan2ActiveAttr(userRoleId);
            return (long)(CovertObjectUtil.getZplus(jh) * rate);
        }else if(fightPowerType == FightPowerType.TJ_ZB){
            Map<String, Long> zb =  getHuajuan2EquipAttr(userRoleId);
            return  (long)(CovertObjectUtil.getZplus(zb) * rate);
        }else if(fightPowerType == FightPowerType.TJ_JD){
            Map<String, Long> fs =  getHuajuan2FullStarAttr(userRoleId);
            return (long)(CovertObjectUtil.getZplus(fs) * rate);
        }
        return 0;
    }

    @Autowired
    private RoleHuajuan2Dao roleHuajuan2Dao;
    @Autowired
    private RoleHuajuan2ExpDao roleHuajuan2ExpDao;
    @Autowired
    private JuanZhouXinXiConfigExportService juanZhouXinXiConfigExportService;
    @Autowired
    private Huajuan2EquipConfigExportService huajuan2EquipConfigExportService;
    @Autowired
    private Huajuan2XinXiBiaoConfigExportService huajuan2XinXiBiaoConfigExportService;
    @Autowired
    private Huajuan2LieBiaoConfigExportService huajuan2LieBiaoConfigExportService;

    @Autowired
    private RoleBagExportService roleBagExportService;
    @Autowired
    private GoodsConfigExportService goodsConfigExportService;
    @Autowired
    private RoleExportService roleExportService;

    /**
     * 获取所有画卷2的缓存数据集合
     * 
     * @param userRoleId
     * @return
     */
    private List<RoleHuajuan2> getRoleHuajuan2List(Long userRoleId) {
        return roleHuajuan2Dao.cacheLoadAll(userRoleId);
    }

    /**
     * 获取画卷2缓存数据
     * 
     * @param userRoleId 玩家编号
     * @param configId 配置编号
     * @return
     */
    private RoleHuajuan2 getRoleHuajuan2(Long userRoleId, final Integer configId) {
        List<RoleHuajuan2> list = roleHuajuan2Dao.cacheLoadAll(userRoleId, new IQueryFilter<RoleHuajuan2>() {
            private boolean stop = false;

            @Override
            public boolean check(RoleHuajuan2 roleYunTu) {
                if (roleYunTu.getConfigId().equals(configId)) {
                    stop = true;
                }
                return stop;
            }

            @Override
            public boolean stopped() {
                return stop;
            }
        });
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取画卷2经验缓存数据
     * 
     * @param userRoleId
     * @return
     */
    private RoleHuajuan2Exp getRoleHuajuan2Exp(Long userRoleId, boolean create) {
        RoleHuajuan2Exp roleHuajuanExp = roleHuajuan2ExpDao.cacheLoad(userRoleId, userRoleId);
        if (create && null == roleHuajuanExp) {
            long now_time = GameSystemTime.getSystemMillTime();
            roleHuajuanExp = new RoleHuajuan2Exp();
            roleHuajuanExp.setUserRoleId(userRoleId);
            roleHuajuanExp.setExp(0L);
            roleHuajuanExp.setCreateTime(now_time);
            roleHuajuanExp.setUpdateTime(now_time);
            roleHuajuan2ExpDao.cacheInsert(roleHuajuanExp, userRoleId);
        }
        return roleHuajuanExp;
    }

    /**
     * 画卷2场景属性刷新
     * 
     * @param userRoleId
     */
    private void noticeStageAttrChange(Long userRoleId) {
        // 推送内部场景 属性变化
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.I_HUANJUAN2_CHARGE, getHuajuan2Attr(userRoleId));
        noticeClientAttrChange(userRoleId);
    };

    /**
     * 推送通知客户端面板总属性变化
     * 
     * @param userRoleId
     */
    private void noticeClientAttrChange(Long userRoleId) {
        BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN2_ATTR_CHANGE, new Object[] { getHuajuan2EquipAttr(userRoleId), getHuajuan2ActiveAttr(userRoleId), getHuajuan2FullStarAttr(userRoleId) });
    }

    /**
     * 对原有属性按照百分比进行加成(速度属性不加成)
     * 
     * @param attrsMap
     * @param percent
     */
    private Map<String, Long> changeAttributeMap(Map<String, Long> attrsMap, Long userRoleId) {
        if (!ObjectUtil.isEmpty(attrsMap)) {
            RoleHuajuan2Exp roleHuajuanExp = getRoleHuajuan2Exp(userRoleId, false);
            if (null != roleHuajuanExp) {
                Integer level = juanZhouXinXiConfigExportService.calcLevel(roleHuajuanExp.getExp());
                JuanZhouXinXiConfig config = juanZhouXinXiConfigExportService.loadById(level);
                if (null != config) {
                    Long speed = attrsMap.remove(EffectType.x19.name());
                    // 等级 比例加成
                    ObjectUtil.longMapTimes(attrsMap, (config.getPercent() + 10000) / 10000F);
                    if (speed != null) {
                        attrsMap.put(EffectType.x19.name(), speed);
                    }
                }
            }
        }
        return attrsMap;
    }

    /**
     * 获取画卷2激活加成属性集合
     * 
     * @param userRoleId
     * @return
     */
    private Map<String, Long> getHuajuan2ActiveAttr(Long userRoleId) {
        List<RoleHuajuan2> huaJuan2List = getRoleHuajuan2List(userRoleId);
        if (ObjectUtil.isEmpty(huaJuan2List)) {
            return null;
        }
        Map<String, Long> attrMap = new HashMap<String, Long>();
        for (RoleHuajuan2 huaJuan2 : huaJuan2List) {
            Huajuan2XinXiBiaoConfig config = huajuan2XinXiBiaoConfigExportService.loadById(huaJuan2.getConfigId());
            if (config != null) {
                ObjectUtil.longMapAdd(attrMap, config.getAttrs());
            }
        }
        return changeAttributeMap(attrMap, userRoleId);
    }

    /**
     * 获取画卷2装备后的加成属性集合
     * 
     * @param userRoleId
     * @return
     */
    private Map<String, Long> getHuajuan2EquipAttr(Long userRoleId) {
        List<RoleHuajuan2> huaJuan2List = getRoleHuajuan2List(userRoleId);
        if (ObjectUtil.isEmpty(huaJuan2List)) {
            return null;
        }
        Map<String, Long> attrMap = new HashMap<String, Long>();
        for (RoleHuajuan2 huaJuan2 : huaJuan2List) {
            if (huaJuan2.getIsUp().equals(Huajuan2Constants.HUAJUAN2_UP)) {
                Huajuan2EquipBiaoConfig config = huajuan2EquipConfigExportService.loadById(huaJuan2.getConfigId());
                if (config != null) {
                    ObjectUtil.longMapAdd(attrMap, config.getAttrs());
                }
            }
        }
        return changeAttributeMap(attrMap, userRoleId);
    }

    /**
     * 获取画卷2满星级加成属性集合
     * 
     * @param userRoleId
     * @return
     */
    private Map<String, Long> getHuajuan2FullStarAttr(Long userRoleId) {
        List<RoleHuajuan2> huaJuan2List = getRoleHuajuan2List(userRoleId);
        if (ObjectUtil.isEmpty(huaJuan2List)) {
            return null;
        }
        Map<String, Long> attrMap = new HashMap<String, Long>();
        Map<Integer, Integer> groupStarMap = new HashMap<>();
        for (RoleHuajuan2 huaJuan2 : huaJuan2List) {
            groupStarMap.put(huaJuan2.getGroupId(), null == groupStarMap.get(huaJuan2.getGroupId()) ? huaJuan2.getStar() : groupStarMap.get(huaJuan2.getGroupId()) + huaJuan2.getStar());
        }
        Integer groupStarNum = null;
        for (Entry<Integer, Integer> entry : groupStarMap.entrySet()) {
            groupStarNum = huajuan2XinXiBiaoConfigExportService.getGroupCount(entry.getKey());
            if (entry.getValue().equals(groupStarNum)) {
                Huajuan2LieBiaoConfig config = huajuan2LieBiaoConfigExportService.loadById(entry.getKey());
                if (config != null) {
                    ObjectUtil.longMapAdd(attrMap, config.getAttrs());
                }
            }
        }
        return changeAttributeMap(attrMap, userRoleId);
    }

    /**
     * 上线清除重复的画卷错误数据
     * @param userRoleId
     */
    public void onlineHandle(Long userRoleId) {
        List<RoleHuajuan2> list = getRoleHuajuan2List(userRoleId);
        if (ObjectUtil.isEmpty(list)) {
            return;
        }
        Set<Integer> errorConfigId = new HashSet<Integer>();
        for (RoleHuajuan2 roleHuajuan : list) {
            if (!errorConfigId.contains(roleHuajuan.getConfigId())) {
                errorConfigId.add(roleHuajuan.getConfigId());
            } else {
                roleHuajuan2Dao.cacheDelete(roleHuajuan.getId(), userRoleId);
                ChuanQiLog.info("画卷2上线清除重复画卷业务, userRoleId={}, configId={}", userRoleId, roleHuajuan.getConfigId());
            }
        }
    }
    
    /**
     * 画卷2激活
     * 
     * @param userRoleId
     * @param configId 画卷配置id
     * @return
     */
    public Object[] huajuan2Active(Long userRoleId, Integer configId) {
        Huajuan2XinXiBiaoConfig config = huajuan2XinXiBiaoConfigExportService.loadById(configId);
        if (config == null || config.getStar() != 1) {
            return AppErrorCode.CONFIG_ERROR;
        }
        /* 校验是否拥有该画卷 */
        /* 画卷升星之后configId发生改变, 故需要遍历该画卷所有星级的配置, 只要找到玩家拥有， 则代表已经激活过此画卷 */
        for (Huajuan2XinXiBiaoConfig tmp = config;;) {
            if (null != getRoleHuajuan2(userRoleId, tmp.getId())) {
                return AppErrorCode.HUAJUAN_ALREADY_HAS;
            }
            if (null == tmp.getNextConfig())
                break;
            tmp = tmp.getNextConfig();
        }
        /* 检查道具消耗 */
        Map<String, Integer> needItem = config.getNeeditem();
        Object[] code = roleBagExportService.checkRemoveBagItemByGoodsId(needItem, userRoleId);
        if (code != null) {
            return code;
        }
        /* 扣除道具 */
        BagSlots errorCode = roleBagExportService.removeBagItemByGoods(needItem, userRoleId, GoodsSource.HUAJUAN2_ACTIVE, true, true);
        if (!errorCode.isSuccee()) {
            return errorCode.getErrorCode();
        }
        /* 更新数据 */
        RoleHuajuan2 roleHuajuan2 = new RoleHuajuan2();
        long now_time = GameSystemTime.getSystemMillTime();
        roleHuajuan2.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
        roleHuajuan2.setUserRoleId(userRoleId);
        roleHuajuan2.setConfigId(config.getId());
        roleHuajuan2.setGroupId(config.getLiebiaoId());
        roleHuajuan2.setIsUp(Huajuan2Constants.HUAJUAN2_DOWN);
        roleHuajuan2.setStar(config.getStar());
        roleHuajuan2.setCreateTime(now_time);
        roleHuajuan2.setUpdateTime(now_time);
        roleHuajuan2Dao.cacheInsert(roleHuajuan2, userRoleId);
        /* 场景属性刷新 */
        noticeStageAttrChange(userRoleId);
        /* 日志记录 */
        RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
        JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(needItem, null);
        GamePublishEvent.publishEvent(new Huajuan2ActiveLogEvent(userRoleId, roleWrapper.getName(), configId, jsonArray));
        return new Object[] { AppErrorCode.SUCCESS, configId };
    }

    /**
     * 请求画卷2基本信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] huajuan2GetInfo(Long userRoleId) {
        Integer level = 1;
        long exp = 0;
        RoleHuajuan2Exp roleHuajuan2Exp = getRoleHuajuan2Exp(userRoleId, true);
        if (roleHuajuan2Exp != null) {
            exp = roleHuajuan2Exp.getExp();
            level = (level = juanZhouXinXiConfigExportService.calcLevel(exp)) == null ? 1 : level;
        }
        List<RoleHuajuan2> huajuan2List = getRoleHuajuan2List(userRoleId);
        Object[] huajuan2VoList = new Object[huajuan2List.size()];
        for (int i = 0; i < huajuan2List.size(); i++) {
            RoleHuajuan2 roleHuaJuan2 = huajuan2List.get(i);
            huajuan2VoList[i] = new Object[] { roleHuaJuan2.getConfigId(), roleHuaJuan2.getIsUp().equals(Huajuan2Constants.HUAJUAN2_UP), roleHuaJuan2.getStar() };
        }
        noticeClientAttrChange(userRoleId);
        return new Object[] { level, exp, huajuan2VoList };
    }

    /**
     * 画卷2穿装
     * 
     * @param userRoleId
     * @param configId
     * @return
     */
    public Object[] huajuan2Up(Long userRoleId, Integer configId) {
        RoleHuajuan2 roleHuajuan2 = getRoleHuajuan2(userRoleId, configId);
        if (roleHuajuan2 == null) {
            return AppErrorCode.HUAJUAN_NOT_EXIT;
        }
        /* 画卷2装备状态校验 */
        if (roleHuajuan2.getIsUp().equals(Huajuan2Constants.HUAJUAN2_UP)) {
            return AppErrorCode.HUAJUAN2_UP_READY;
        }
        List<RoleHuajuan2> huajuan2list = getRoleHuajuan2List(userRoleId);
        int equipCount = 0;
        if (!ObjectUtil.isEmpty(huajuan2list)) {
            for (RoleHuajuan2 huajuan2 : huajuan2list) {
                if (huajuan2.getIsUp().equals(Huajuan2Constants.HUAJUAN2_UP)) {
                    equipCount++;
                }
            }
        }
        /* 画卷2装备最大件数校验 */
        if (equipCount >= Huajuan2Constants.HUAJUAN2_MAX_EQUIP) {
            return AppErrorCode.HUAJUAN_EQUIP_NUM_LIMIT;
        }
        // 更新数据
        roleHuajuan2.setIsUp(Huajuan2Constants.HUAJUAN2_UP);
        roleHuajuan2.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleHuajuan2Dao.cacheUpdate(roleHuajuan2, userRoleId);
        noticeStageAttrChange(userRoleId);
        return new Object[] { AppErrorCode.SUCCESS, configId };
    }

    /**
     * 画卷2卸装
     * 
     * @param userRoleId
     * @param configId
     * @return
     */
    public Object[] huajuan2down(Long userRoleId, Integer configId) {
        RoleHuajuan2 roleHuajuan2 = getRoleHuajuan2(userRoleId, configId);
        if (roleHuajuan2 == null) {
            return AppErrorCode.HUAJUAN_NOT_EXIT;
        }
        /* 画卷2装备状态校验 */
        if (roleHuajuan2.getIsUp().equals(Huajuan2Constants.HUAJUAN2_DOWN)) {
            return AppErrorCode.HUAJUAN2_DOWN_READY;
        }
        // 更新数据
        roleHuajuan2.setIsUp(Huajuan2Constants.HUAJUAN2_DOWN);
        roleHuajuan2.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleHuajuan2Dao.cacheUpdate(roleHuajuan2, userRoleId);
        noticeStageAttrChange(userRoleId);
        return new Object[] { AppErrorCode.SUCCESS, configId };
    }

    /**
     * 画卷2分解
     * 
     * @param userRoleId
     * @param ids
     * @return
     */
    public Object[] huajuan2Fenjie(Long userRoleId, Object[] ids) {
        if (ids == null || ids.length == 0) {
            return AppErrorCode.PARAMETER_ERROR;
        }
        RoleHuajuan2Exp roleHuajuan2Exp = getRoleHuajuan2Exp(userRoleId, false);
        long beforeExp = roleHuajuan2Exp.getExp();
        /* 校验最大的分解经验 */
        long maxExp = juanZhouXinXiConfigExportService.getMaxExp();
        if (beforeExp >= maxExp) {
            return AppErrorCode.HUAJUAN_SHENGJI_DAODING;
        }
        /* 校验分解的道具 */
        long fenjieValue = 0;
        Map<Long, Integer> consumeMap = new HashMap<Long, Integer>();
        Map<String, Integer> consumeItemMap = new HashMap<String, Integer>();
        for (Object id : ids) {
            Long e = CovertObjectUtil.object2Long(id);
            RoleItemExport roleItem = roleBagExportService.getBagItemByGuid(userRoleId, e);
            if (roleItem == null) {
                return AppErrorCode.PARAMETER_ERROR;
            }
            String goodsId = roleItem.getGoodsId();
            GoodsConfig config = goodsConfigExportService.loadById(goodsId);
            if (config == null || config.getCategory() != GoodsCategory.HUAJUAN2) {
                return AppErrorCode.PARAMETER_ERROR;
            }
            boolean full = false;
            int count = roleItem.getCount();
            int consumeCount = 0;
            for (int i = 0; i < count; i++) {
                consumeCount++;
                fenjieValue = fenjieValue + config.getData1();
                if ((beforeExp + fenjieValue) >= maxExp) {
                    fenjieValue = maxExp;
                    full = true;
                    break;
                }
            }
            consumeMap.put(e, consumeCount);
            consumeItemMap.put(goodsId, consumeCount);
            if (full) {
                break;
            }
        }
        if (fenjieValue <= 0) {
            return AppErrorCode.PARAMETER_ERROR;
        }
        // 消耗道具
        for (Long guid : consumeMap.keySet()) {
            Integer consumeCount = consumeMap.get(guid);
            if (consumeCount > 0) {
                BagSlots bagSlots = roleBagExportService.removeBagItemByGuid(guid, consumeCount, userRoleId, GoodsSource.HUAJUAN2_FENJIE, true, true);
                if (!bagSlots.isSuccee()) {
                    return bagSlots.getErrorCode();
                }
            }
        }
        /* 更新数据 */
        long afterExp = beforeExp + fenjieValue;
        roleHuajuan2Exp.setExp(afterExp);
        roleHuajuan2Exp.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleHuajuan2ExpDao.cacheUpdate(roleHuajuan2Exp, userRoleId);
        // 属性刷新
        Integer beforeLevel = juanZhouXinXiConfigExportService.calcLevel(beforeExp);
        Integer afterLevel = juanZhouXinXiConfigExportService.calcLevel(afterExp);
        if (null == beforeLevel || null == afterLevel) {
            return AppErrorCode.CONFIG_ERROR;
        }
        if (afterLevel > beforeLevel) {
            noticeStageAttrChange(userRoleId);
        }
        // 日志记录
        JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(consumeItemMap, null);
        RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
        GamePublishEvent.publishEvent(new HuaJuan2FenjieLogEvent(userRoleId, roleWrapper.getName(), jsonArray, beforeLevel, beforeExp, afterLevel, afterExp));
        return new Object[] { AppErrorCode.SUCCESS, afterLevel, afterExp };
    }

    /**
     * 画卷2升星
     * 
     * @param userRoleId
     * @param configId
     * @return
     */
    public Object[] huajuan2Upgrade(Long userRoleId, Integer configId) {
        RoleHuajuan2 roleHuajuan2 = getRoleHuajuan2(userRoleId, configId);
        if (roleHuajuan2 == null) {
            return AppErrorCode.HUAJUAN_NOT_EXIT;
        }
        Huajuan2XinXiBiaoConfig config = huajuan2XinXiBiaoConfigExportService.loadById(configId);
        if (config == null) {
            return AppErrorCode.CONFIG_ERROR;
        }
        Huajuan2XinXiBiaoConfig newConfig = config.getNextConfig();
        if (newConfig == null) {
            return AppErrorCode.HUAJUAN2_NOT_UPGRADE;
        }
        /* 检查道具消耗 */
        Map<String, Integer> needItem = config.getNeeditem();
        Object[] code = roleBagExportService.checkRemoveBagItemByGoodsId(needItem, userRoleId);
        if (code != null) {
            return code;
        }
        /* 扣除道具 */
        BagSlots errorCode = roleBagExportService.removeBagItemByGoods(needItem, userRoleId, GoodsSource.HUAJUAN2_UPGRADE, true, true);
        if (!errorCode.isSuccee()) {
            return errorCode.getErrorCode();
        }
        /* 更新数据 */
        roleHuajuan2.setConfigId(newConfig.getId());
        roleHuajuan2.setGroupId(newConfig.getLiebiaoId());
        roleHuajuan2.setStar(newConfig.getStar());
        roleHuajuan2.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleHuajuan2Dao.cacheUpdate(roleHuajuan2, userRoleId);
        noticeStageAttrChange(userRoleId);
        /* 日志记录 */
        RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
        JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(needItem, null);
        GamePublishEvent.publishEvent(new HuaJuan2UpgradeLogEvent(userRoleId, roleWrapper.getName(), jsonArray, config.getId(), newConfig.getId()));
        return new Object[] { AppErrorCode.SUCCESS, config.getId(), newConfig.getId(), newConfig.getStar() };
    }

    /**
     * 初始化玩家画卷2数据到缓存
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleHuajuan2> initRoleHuajuan2Data(Long userRoleId) {
        return roleHuajuan2Dao.initRoleHuajuan2(userRoleId);
    }

    /**
     * 初始化玩家画卷2经验数据到缓存
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleHuajuan2Exp> initRoleHuajuan2ExpData(Long userRoleId) {
        return roleHuajuan2ExpDao.initRoleHuajuan2Exp(userRoleId);
    }

    /**
     * 获取画卷2所有加成属性集合
     * 
     * @param userRoleId
     * @return
     */
    public Map<String, Long> getHuajuan2Attr(Long userRoleId) {
        Map<String, Long> attrsMap = new HashMap<String, Long>();
        ObjectUtil.longMapAdd(attrsMap, getHuajuan2ActiveAttr(userRoleId));
        ObjectUtil.longMapAdd(attrsMap, getHuajuan2EquipAttr(userRoleId));
        ObjectUtil.longMapAdd(attrsMap, getHuajuan2FullStarAttr(userRoleId));
        return attrsMap;
    }

    

}
