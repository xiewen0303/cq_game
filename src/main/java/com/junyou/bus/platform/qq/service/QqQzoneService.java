package com.junyou.bus.platform.qq.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.qq.confiure.export.QqQzoneEveryDayPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqQzoneLevelPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqQzoneXinShouPublicConfig;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.constants.QqQzoneConstants;
import com.junyou.bus.platform.qq.dao.RoleQqQzoneDao;
import com.junyou.bus.platform.qq.entity.RoleQqQzone;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.err.AppErrorCode;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.spring.container.DataContainer;

/**
 *@Description QQ游戏大厅业务处理
 *@Author Yang Gao
 *@Since 2016-7-8
 *@Version 1.1.0
 */
@Service
public class QqQzoneService {
    @Autowired
    private RoleQqQzoneDao roleQqQzoneDao;
    @Autowired
    private DataContainer dataContainer;
    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private RoleBagExportService roleBagExportService;
    @Autowired
    private PlatformGongGongShuJuBiaoConfigExportService platformGongGongShuJuBiaoConfigExportService;

    /**
     * 获取不为空的QQ空间对象
     * 
     * @param userRoleId
     * @return
     */
    private RoleQqQzone getRoleQqQzone(Long userRoleId) {
        return roleQqQzoneDao.cacheAsynLoad(userRoleId, userRoleId);
    }

    /**
     * 初始化数据对象
     * 
     * @param userRoleId
     * @return
     */
    private RoleQqQzone createNewRoleQqQzone(Long userRoleId) {
        RoleQqQzone RoleQqQzone = new RoleQqQzone();
        long nowTimestamp = GameSystemTime.getSystemMillTime();
        RoleQqQzone.setUserRoleId(userRoleId);
        RoleQqQzone.setOnceAwardStatus(QqQzoneConstants.REWARD_STATUS_NO);
        RoleQqQzone.setCreateTimestamp(nowTimestamp);
        RoleQqQzone.setUpdateTimestamp(nowTimestamp);
        roleQqQzoneDao.cacheInsert(RoleQqQzone, userRoleId);
        return RoleQqQzone;
    }

    /**
     * 检验QQ游大厅平台
     * 
     * @param userRoleId
     * @return
     */
    private boolean checkQqQzonePlatform(Long userRoleId) {
        RoleWrapper role = roleExportService.getLoginRole(userRoleId);
        if (null == role) {
            return false;
        }
        Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());
        if (ObjectUtil.isEmpty(keyMap)) {
            return false;
        }
        return QqConstants.QZONE.equals(keyMap.get("pfyuan"));
    }

    /**
     * 获取新手礼包信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getXinShouInfo(Long userRoleId) {
        if (!checkQqQzonePlatform(userRoleId)) {
            return AppErrorCode.QQ_PLATFORM_ERROR;
        }
        RoleQqQzone RoleQqQzone = getRoleQqQzone(userRoleId);
        if (null == RoleQqQzone) {
            RoleQqQzone = createNewRoleQqQzone(userRoleId);
        }
        return new Object[] { AppErrorCode.SUCCESS, RoleQqQzone.getOnceAwardStatus() == QqQzoneConstants.REWARD_STATUS_YES };
    }

    /**
     * 领取新手礼包
     * 
     * @param userRoleId
     * @return
     */
    public Object[] receiveXinShouGift(Long userRoleId) {
        if (!checkQqQzonePlatform(userRoleId)) {
            return AppErrorCode.QQ_PLATFORM_ERROR;
        }
        RoleQqQzone RoleQqQzone = getRoleQqQzone(userRoleId);
        if (null == RoleQqQzone) {
            return null;
        }
        if (QqQzoneConstants.REWARD_STATUS_YES == RoleQqQzone.getOnceAwardStatus()) {
            return AppErrorCode.GET_ALREADY;
        }
        QqQzoneXinShouPublicConfig xinshouConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_QZONE_XINSHOU);
        if (null == xinshouConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        Map<String, Integer> itemMap = xinshouConfig.getXinShouItem();
        if (ObjectUtil.isEmpty(itemMap)) {
            return AppErrorCode.CONFIG_ERROR;
        }
        // 背包空间不足
        Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(itemMap, userRoleId);
        if (code != null) {
            return code;
        }

        /* 更新数据 */
        RoleQqQzone.setOnceAwardStatus(QqQzoneConstants.REWARD_STATUS_YES);
        RoleQqQzone.setUpdateTimestamp(GameSystemTime.getSystemMillTime());
        roleQqQzoneDao.cacheUpdate(RoleQqQzone, userRoleId);

        /* 发送奖励 */
        roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.QqQzone_XINSHOU, LogPrintHandle.GET_QqQZone_XINSHOU, LogPrintHandle.GBZ_QqQZone_XINSHOU, true);

        return AppErrorCode.OK;
    }

    /**
     * 获取成长礼包信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getLevelInfo(Long userRoleId) {
        if (!checkQqQzonePlatform(userRoleId)) {
            return AppErrorCode.QQ_PLATFORM_ERROR;
        }
        RoleQqQzone RoleQqQzone = getRoleQqQzone(userRoleId);
        if (null == RoleQqQzone) {
            RoleQqQzone = createNewRoleQqQzone(userRoleId);
        }
        QqQzoneLevelPublicConfig levelConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_QZONE_LEVEL);
        if (null == levelConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        Map<Integer, Map<String, Integer>> levelRewardMap = levelConfig.getLevelItem();
        if (ObjectUtil.isEmpty(levelRewardMap)) {
            return AppErrorCode.CONFIG_ERROR;
        }
        List<Integer> levelArray = RoleQqQzone.getReceiveLevelArray();
        return new Object[] { AppErrorCode.SUCCESS, null == levelArray ? null : levelArray.toArray() };
    }

    /**
     * 领取成长礼包
     * 
     * @param userRoleId
     * @param level
     * @return
     */
    public Object[] receiveLevelGift(Long userRoleId, Integer level) {
        // 参数有效性校验
        int levelVal = null == level ? 0 : level.intValue();
        if (0 == levelVal) {
            return AppErrorCode.PARAMETER_ERROR;
        }
        // 平台校验
        if (!checkQqQzonePlatform(userRoleId)) {
            return AppErrorCode.QQ_PLATFORM_ERROR;
        }
        RoleQqQzone RoleQqQzone = getRoleQqQzone(userRoleId);
        if (null == RoleQqQzone) {
            return null;
        }
        // 领取状态校验
        List<Integer> levelArray = RoleQqQzone.getReceiveLevelArray();
        if (!ObjectUtil.isEmpty(levelArray) && levelArray.contains(levelVal)) {
            return AppErrorCode.GET_ALREADY;
        }
        // 奖励配置校验
        QqQzoneLevelPublicConfig levelConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_QZONE_LEVEL);
        if (null == levelConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        Map<Integer, Map<String, Integer>> levelRewardMap = levelConfig.getLevelItem();
        if (ObjectUtil.isEmpty(levelRewardMap)) {
            return AppErrorCode.CONFIG_ERROR;
        }
        // 参数有效性校验
        Map<String, Integer> rewardMap = levelRewardMap.get(levelVal);
        if (ObjectUtil.isEmpty(rewardMap)) {
            return AppErrorCode.PARAMETER_ERROR;
        }
        // 背包空间不足
        Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(rewardMap, userRoleId);
        if (code != null) {
            return code;
        }
        // 更新数据
        RoleQqQzone.updateLevelLog(levelVal);
        RoleQqQzone.setUpdateTimestamp(GameSystemTime.getSystemMillTime());
        roleQqQzoneDao.cacheUpdate(RoleQqQzone, userRoleId);
        /* 发送奖励 */
        roleBagExportService.putGoodsAndNumberAttr(rewardMap, userRoleId, GoodsSource.QqQzone_LEVEL, LogPrintHandle.GET_QqQZone_LEVEL, LogPrintHandle.GBZ_QqQZone_LEVEL, true);
        return new Object[] { AppErrorCode.SUCCESS, levelVal };
    }

    /**
     * 获取每日礼包信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getEveryInfo(Long userRoleId) {
        if (!checkQqQzonePlatform(userRoleId)) {
            return AppErrorCode.QQ_PLATFORM_ERROR;
        }
        RoleQqQzone RoleQqQzone = getRoleQqQzone(userRoleId);
        if (null == RoleQqQzone) {
            RoleQqQzone = createNewRoleQqQzone(userRoleId);
        }
        return new Object[] { AppErrorCode.SUCCESS, ObjectUtil.dayIsToday(RoleQqQzone.getEveryAwardTimestamp()) };
    }

    /**
     * 领取每日礼包
     * 
     * @param userRoleId
     * @return
     */
    public Object[] receiveEveryGift(Long userRoleId) {
        // 平台校验
        if (!checkQqQzonePlatform(userRoleId)) {
            return AppErrorCode.QQ_PLATFORM_ERROR;
        }
        RoleQqQzone RoleQqQzone = getRoleQqQzone(userRoleId);
        if (null == RoleQqQzone) {
            return null;
        }
        // 领取状态校验
        Long everyTimestamp = RoleQqQzone.getEveryAwardTimestamp();
        if (ObjectUtil.dayIsToday(everyTimestamp)) {
            return AppErrorCode.GET_ALREADY;
        }
        // 奖励配置校验
        QqQzoneEveryDayPublicConfig everyConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_QZONE_EVERY_DAY);
        if (null == everyConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        Map<String, Integer> itemMap = everyConfig.getEveryDayItem();
        if (ObjectUtil.isEmpty(itemMap)) {
            return AppErrorCode.CONFIG_ERROR;
        }
        // 背包空间不足
        Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(itemMap, userRoleId);
        if (code != null) {
            return code;
        }
        /* 更新数据 */
        long nowTimestamp = GameSystemTime.getSystemMillTime();
        RoleQqQzone.setEveryAwardTimestamp(nowTimestamp);
        RoleQqQzone.setUpdateTimestamp(nowTimestamp);
        roleQqQzoneDao.cacheUpdate(RoleQqQzone, userRoleId);
        /* 发送奖励 */
        roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.QqQzone_EVERY, LogPrintHandle.GET_QqQZone_EVERY, LogPrintHandle.GBZ_QqQZone_EVERY, true);
        return AppErrorCode.OK;
    }

}
