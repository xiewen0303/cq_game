/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xianqi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xianqi.configure.XianQiJueXingConfig;
import com.junyou.bus.xianqi.configure.XianQiJueXingConfigExportService;
import com.junyou.bus.xianqi.configure.YunYaoXianDongConfig;
import com.junyou.bus.xianqi.configure.YunYaoXianDongConfigExportService;
import com.junyou.bus.xianqi.constants.XianqiConstants;
import com.junyou.bus.xianqi.dao.RoleXianqiDao;
import com.junyou.bus.xianqi.dao.RoleXianqiJuexingDao;
import com.junyou.bus.xianqi.entity.RoleXianqi;
import com.junyou.bus.xianqi.entity.RoleXianqiJuexing;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.XianqiJuexingLogEvent;
import com.junyou.event.XianqiLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * @Description 仙器觉醒业务处理类
 * @Author Yang Gao
 * @Since 2016-10-30
 * @Version 1.1.0
 */
@Service
public class XianqiService {

    @Autowired
    private RoleXianqiDao roleXianqiDao;
    @Autowired
    private RoleXianqiJuexingDao roleXianqiJuexingDao;

    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private RoleBagExportService roleBagExportService;
    @Autowired
    private GoodsConfigExportService goodsConfigExportService;
    @Autowired
    private YunYaoXianDongConfigExportService yunYaoXianDongConfigExportService;
    @Autowired
    private XianQiJueXingConfigExportService xianQiJueXingConfigExportService;

    /**
     * 获取缓存中的仙器对象
     * 
     * @param userRoleId
     * @return
     */
    private RoleXianqi getCacheRoleXianqi(Long userRoleId) {
        return roleXianqiDao.cacheLoad(userRoleId, userRoleId);
    }

    /**
     * 获取缓存中的仙器觉醒对象
     * 
     * @param userRoleId
     * @return
     */
    private RoleXianqiJuexing getCacheRoleXianqiJuexing(Long userRoleId, Integer xianqiType) {
        final Integer type = xianqiType;
        List<RoleXianqiJuexing> roleXianqiJuexingList = roleXianqiJuexingDao.cacheLoadAll(userRoleId, new IQueryFilter<RoleXianqiJuexing>() {
            private boolean stopFalg;

            @Override
            public boolean check(RoleXianqiJuexing entity) {
                if (entity.getXianqiType() != null && entity.getXianqiType().equals(type)) {
                    stopFalg = true;
                }
                return stopFalg;
            }

            @Override
            public boolean stopped() {
                return stopFalg;
            }
        });
        return ObjectUtil.isEmpty(roleXianqiJuexingList) ? null : roleXianqiJuexingList.get(0);
    }

    /**
     * 获取缓存中的所有仙器觉醒对象集合
     * 
     * @param userRoleId
     * @return
     */
    private List<RoleXianqiJuexing> getCacheRoleXianqiJuexingList(Long userRoleId) {
        return roleXianqiJuexingDao.cacheLoadAll(userRoleId);
    }

    /**
     * 创建仙器对象
     * 
     * @param userRoleId
     * @return
     */
    private RoleXianqi createRoleXianqi(Long userRoleId) {
        RoleXianqi roleXianqi = new RoleXianqi();
        roleXianqi.setUserRoleId(userRoleId);
        roleXianqi.setXiandongLvl(XianqiConstants.XD_INIT_LEVEL);
        roleXianqi.setXiandongExp(0L);
        roleXianqi.setCreateTime(GameSystemTime.getSystemMillTime());
        roleXianqi.setUpdateTime(roleXianqi.getCreateTime());
        roleXianqiDao.cacheInsert(roleXianqi, userRoleId);
        return roleXianqi;
    }

    /**
     * 创建仙器觉醒对象
     * 
     * @param userRoleId
     * @return
     */
    private RoleXianqiJuexing createRoleXianqiJuexing(Long userRoleId, Integer xianqiType) {
        RoleXianqiJuexing roleXianqiJuexing = new RoleXianqiJuexing();
        roleXianqiJuexing.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
        roleXianqiJuexing.setUserRoleId(userRoleId);
        roleXianqiJuexing.setXianqiType(xianqiType);
        roleXianqiJuexing.setJuexingLvl(XianqiConstants.XQJX_INIT_LEVEL);
        roleXianqiJuexing.setCreateTime(GameSystemTime.getSystemMillTime());
        roleXianqiJuexing.setUpdateTime(roleXianqiJuexing.getCreateTime());
        roleXianqiJuexingDao.cacheInsert(roleXianqiJuexing, userRoleId);
        return roleXianqiJuexing;
    }

    /**
     * 获取登录的角色信息
     * 
     * @param userRoleId
     * @return
     */
    private RoleWrapper getRoleWrapper(Long userRoleId) {
        return roleExportService.getLoginRole(userRoleId);
    }

    /**
     * 获取所有仙器类型
     * 
     * @return
     */
    private List<Integer> getXianqiTypeList() {
        return Arrays.asList(XianqiConstants.XQJX_TYPE_1, XianqiConstants.XQJX_TYPE_2, XianqiConstants.XQJX_TYPE_3, XianqiConstants.XQJX_TYPE_4, XianqiConstants.XQJX_TYPE_5, XianqiConstants.XQJX_TYPE_6);
    }

    /**
     * 通知场景刷新仙洞属性变化
     * 
     * @param userRoleId
     */
    private void noticeStageRefreshXiandongAttrChange(Long userRoleId){
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_XIANDONG_CHANGE, getRoleXianqiAttrMap(userRoleId));
    }
    
    /**
     * 通知场景刷新仙器觉醒属性变化
     * 
     * @param userRoleId
     */
    private void noticeStageRefreshXianqiJuexingAttrChange(Long userRoleId){
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_XIANQIJUEXING_CHANGE, getRoleXianqiJuexingAttrMap(userRoleId));
    }
    
    
    /**
     * 初始化仙器数据到内存
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleXianqi> initRoleXianqiData(Long userRoleId) {
        return roleXianqiDao.initRoleXianqi(userRoleId);
    }

    /**
     * 初始化仙器觉醒数据到内存
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleXianqiJuexing> initRoleXianqiJuexingData(Long userRoleId) {
        return roleXianqiJuexingDao.initRoleXianqiJuexing(userRoleId);
    }

    /**
     * 加载仙器觉醒数据
     * 
     * @param userRoleId
     * @return
     */
    public Object[] loadInfo(Long userRoleId) {
        RoleXianqi roleXianqi = getCacheRoleXianqi(userRoleId);
        if (null == roleXianqi) {
            roleXianqi = createRoleXianqi(userRoleId);
            // 仙洞初始化数据,1级属性变化刷新
            noticeStageRefreshXiandongAttrChange(userRoleId);
        }
        
        List<Object[]> xianqiJuexingData = new ArrayList<Object[]>();
        boolean attrChangeFlag = false;
        for (Integer xianqiType : getXianqiTypeList()) {
            RoleXianqiJuexing roleXianqiJuexing = getCacheRoleXianqiJuexing(userRoleId, xianqiType);
            if (null == roleXianqiJuexing) {
                roleXianqiJuexing = createRoleXianqiJuexing(userRoleId, xianqiType);
                attrChangeFlag = true;
            }
            xianqiJuexingData.add(new Object[] { roleXianqiJuexing.getXianqiType(), roleXianqiJuexing.getJuexingLvl() });
        }
        // 仙器觉醒初始化数据,1级属性变化刷新
        if(attrChangeFlag)  noticeStageRefreshXianqiJuexingAttrChange(userRoleId);
        return new Object[] { AppErrorCode.SUCCESS, roleXianqi.getXiandongLvl(), roleXianqi.getXiandongExp(), xianqiJuexingData.toArray() };
    }

    /**
     * 获取云瑶仙洞属性加成
     * 
     * @param userRoleId
     * @return
     */
    public Map<String, Long> getRoleXianqiAttrMap(Long userRoleId) {
        RoleXianqi roleXianqi = getCacheRoleXianqi(userRoleId);
        if (null == roleXianqi) {
            return null;
        }
        YunYaoXianDongConfig config = yunYaoXianDongConfigExportService.loadByLvl(roleXianqi.getXiandongLvl());
        if (null == config) {
            return null;
        }
        return config.getAttrMap();
    }

    /**
     * 获取所有仙器觉醒的属性加成
     * 
     * @param userRoleId
     * @return
     */
    public Map<String, Long> getRoleXianqiJuexingAttrMap(Long userRoleId) {
        List<RoleXianqiJuexing> list = getCacheRoleXianqiJuexingList(userRoleId);
        if (ObjectUtil.isEmpty(list)) {
            return null;
        }
        Map<String, Long> attrMap = new HashMap<String, Long>();
        for (RoleXianqiJuexing roleXianqiJuexing : list) {
            XianQiJueXingConfig config = xianQiJueXingConfigExportService.loadByTypeAndLvl(roleXianqiJuexing.getXianqiType(), roleXianqiJuexing.getJuexingLvl());
            if (null != config) {
                ObjectUtil.longMapAdd(attrMap, config.getAttrMap());
            }
        }
        return attrMap;
    }
    
    public Long getRoleXianqiJuexingTotalZplus(Long userRoleId){
    	Map<String, Long> attr = getRoleXianqiJuexingAttrMap(userRoleId);
      	if(attr == null || attr.size() ==0 ||! attr.containsKey(EffectType.zplus.name())){
    		return 0l;
    	}
    	
    	return attr.get(EffectType.zplus.name());
    }

    /**
     * 升级云遥仙洞
     * 
     * @param userRoleId
     * @param goodsId 升级选择的道具id
     * @return
     */
    public Object[] upLevel(Long userRoleId, String goodsId) {
        if (null == goodsId) {
            return AppErrorCode.PAIMAI_ERROR;
        }
        // 校验功能是否开启
        RoleXianqi roleXianqi = getCacheRoleXianqi(userRoleId);
        if (null == roleXianqi) {
            return AppErrorCode.FUNCTION_NOT_OPEN;
        }
        // 校验配置是否存在
        int beforeLevel = roleXianqi.getXiandongLvl();
        if (beforeLevel >= yunYaoXianDongConfigExportService.getMaxLevel()) {
            return AppErrorCode.XQJX_UPLVL_LEVEL_LIMIT;
        }
        YunYaoXianDongConfig yunYaoXianDongConfig = yunYaoXianDongConfigExportService.loadByLvl(beforeLevel);
        if (null == yunYaoXianDongConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        // 校验升级道具是否正确
        List<String> itemList = yunYaoXianDongConfig.getNeeditem();
        if (ObjectUtil.isEmpty(itemList)) {
            return AppErrorCode.CONFIG_ERROR;
        } else if (!itemList.contains(goodsId)) {
            return AppErrorCode.ITEM_NOT_ERROR;
        }
        // 校验道具是否存在
        GoodsConfig config = goodsConfigExportService.loadById(goodsId);
        if (null == config) {
            return AppErrorCode.ITEM_NOT_ERROR;
        }
        int addExp = config.getData1();
        long uplevelExp = yunYaoXianDongConfig.getNeedexp();
        int haveCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
        if (haveCount <= 0) {
            return AppErrorCode.ITEM_NOT_ENOUGH;
        }
        // 消耗道具
        int useItemCnt = 1;
        BagSlots errorCode = roleBagExportService.removeBagItemByGoodsId(goodsId, useItemCnt, userRoleId, GoodsSource.XIANQI_XIANDONG_UPLVL, true, true);
        if (!errorCode.isSuccee()) {
            return errorCode.getErrorCode();
        }
        // 校验道具是否达到升级
        boolean uplevelFlag = false;
        int afterLevel = beforeLevel;
        long beforeExp = roleXianqi.getXiandongExp();
        long afterExp = beforeExp + addExp;
        if (afterExp >= uplevelExp) {// 直接升级
            afterLevel++;
            afterExp -= uplevelExp;
            uplevelFlag = true;
        }
        // 更新数据
        roleXianqi.setXiandongLvl(afterLevel);
        roleXianqi.setXiandongExp(afterExp);
        roleXianqi.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleXianqiDao.cacheUpdate(roleXianqi, userRoleId);
        // 升级刷新场景属性
        if (uplevelFlag) {
            noticeStageRefreshXiandongAttrChange(userRoleId);
        }
        // 日志记录
        Map<String, Integer> itemMap = new HashMap<String, Integer>();
        itemMap.put(goodsId, useItemCnt);
        JSONArray goods = LogPrintHandle.getLogGoodsParam(itemMap, null);
        RoleWrapper roleWrapper = getRoleWrapper(userRoleId);
        GamePublishEvent.publishEvent(new XianqiLogEvent(userRoleId, null == roleWrapper ? "" : roleWrapper.getName(), beforeLevel, afterLevel, beforeExp, afterExp, goods));
        return new Object[] { AppErrorCode.SUCCESS, afterExp, afterLevel };
    }

    /**
     * 仙器觉醒
     * 
     * @param userRoleId
     * @param xianqiType
     * @return
     */
    public Object[] jueXing(Long userRoleId, Integer xianqiType) {
        // 校验参数是否有效
        if (null == xianqiType) {
            return AppErrorCode.PAIMAI_ERROR;
        }
        // 校验仙器类型是否存在
        if (!getXianqiTypeList().contains(xianqiType)) {
            return AppErrorCode.XQJX_TYPE_ERROR;
        }
        // 校验功能是否开启
        RoleXianqi roleXianqi = getCacheRoleXianqi(userRoleId);
        RoleXianqiJuexing roleXianqiJuexing = getCacheRoleXianqiJuexing(userRoleId, xianqiType);
        if (null == roleXianqiJuexing || null == roleXianqi) {
            return AppErrorCode.FUNCTION_NOT_OPEN;
        }
        // 校验觉醒等级是否已达上限
        int beforeLevel = roleXianqiJuexing.getJuexingLvl();
        if (beforeLevel >= xianQiJueXingConfigExportService.getMaxByType(xianqiType)) {
            return AppErrorCode.XQJX_JUEXING_LEVEL_LIMIT;
        }
        // 校验觉醒等级是否超过仙洞等级
        if(beforeLevel >= roleXianqi.getXiandongLvl()){
            return AppErrorCode.XQJX_JUEXING_LEVEL_NOT_ENOUGN;
        }
        // 校验配置是否存在
        XianQiJueXingConfig config = xianQiJueXingConfigExportService.loadByTypeAndLvl(xianqiType, beforeLevel);
        if (null == config) {
            return AppErrorCode.CONFIG_ERROR;
        }
        // 检验道具是否足够
        List<String> itemIdList = config.getItemIdList();
        if (ObjectUtil.isEmpty(itemIdList)) {
            return AppErrorCode.CONFIG_ERROR;
        }
        Map<String, Integer> goodsMap = new HashMap<String, Integer>();
        for (String id1 : itemIdList) {
            List<String> needGoodsIds = goodsConfigExportService.loadIdsById1(id1);
            for (String goodsId : needGoodsIds) {
                if (roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId) < XianqiConstants.XQJX_JUEXING_ITEM_CNT) {
                    return AppErrorCode.ITEM_NOT_ENOUGH;
                }
                goodsMap.put(goodsId, XianqiConstants.XQJX_JUEXING_ITEM_CNT);
                break;
            }
        }
        // 消耗道具
        BagSlots errorCode = roleBagExportService.removeBagItemByGoods(goodsMap, userRoleId, GoodsSource.XIANQI_JUEXING_UPLVL, true, true);
        if (!errorCode.isSuccee()) {
            return errorCode.getErrorCode();
        }
        // 更新数据
        int afterLevel = beforeLevel + 1;
        roleXianqiJuexing.setJuexingLvl(afterLevel);
        roleXianqiJuexing.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleXianqiJuexingDao.cacheUpdate(roleXianqiJuexing, userRoleId);
        // 场景刷新数据
        noticeStageRefreshXianqiJuexingAttrChange(userRoleId);
        // 日志记录
        JSONArray goods = LogPrintHandle.getLogGoodsParam(goodsMap, null);
        RoleWrapper roleWrapper = getRoleWrapper(userRoleId);
        GamePublishEvent.publishEvent(new XianqiJuexingLogEvent(userRoleId, null == roleWrapper ? "" : roleWrapper.getName(), xianqiType, beforeLevel, afterLevel, goods));
        return new Object[] { AppErrorCode.SUCCESS, xianqiType, afterLevel };
    }

}
