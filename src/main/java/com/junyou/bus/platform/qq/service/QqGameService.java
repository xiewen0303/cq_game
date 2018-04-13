package com.junyou.bus.platform.qq.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.qq.confiure.export.QqGameEveryDayPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqGameLevelPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqGameXinShouPublicConfig;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.constants.QqGameConstants;
import com.junyou.bus.platform.qq.dao.RoleQqGameDao;
import com.junyou.bus.platform.qq.entity.RoleQqGame;
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
public class QqGameService {
    @Autowired
    private RoleQqGameDao roleQqGameDao;
    @Autowired
    private DataContainer dataContainer;
    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private RoleBagExportService roleBagExportService;
    @Autowired
    private PlatformGongGongShuJuBiaoConfigExportService platformGongGongShuJuBiaoConfigExportService;

    /**
     * 获取不为空的QQ游戏大厅对象
     * 
     * @param userRoleId
     * @return
     */
    private RoleQqGame getRoleQqGame(Long userRoleId) {
        return roleQqGameDao.cacheAsynLoad(userRoleId, userRoleId);
    }

    /**
     * 初始化数据对象
     * 
     * @param userRoleId
     * @return
     */
    private RoleQqGame createNewRoleQqGame(Long userRoleId) {
        RoleQqGame roleQqGame = new RoleQqGame();
        long nowTimestamp = GameSystemTime.getSystemMillTime();
        roleQqGame.setUserRoleId(userRoleId);
        roleQqGame.setOnceAwardStatus(QqGameConstants.REWARD_STATUS_NO);
        roleQqGame.setCreateTimestamp(nowTimestamp);
        roleQqGame.setUpdateTimestamp(nowTimestamp);
        roleQqGameDao.cacheInsert(roleQqGame, userRoleId);
        return roleQqGame;
    }

    /**
     * 检验QQ游大厅平台
     * 
     * @param userRoleId
     * @return
     */
    private boolean checkQqGamePlatform(Long userRoleId) {
        RoleWrapper role = roleExportService.getLoginRole(userRoleId);
        if (null == role) {
            return false;
        }
        Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());
        if (ObjectUtil.isEmpty(keyMap)) {
            return false;
        }
        return QqConstants.QQGAME.equals(keyMap.get("pfyuan"));
    }

    /**
     * 获取新手礼包信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getXinShouInfo(Long userRoleId) {
        if (!checkQqGamePlatform(userRoleId)) {
            return AppErrorCode.QQ_PLATFORM_ERROR;
        }
        RoleQqGame roleQqGame = getRoleQqGame(userRoleId);
        if (null == roleQqGame) {
            roleQqGame = createNewRoleQqGame(userRoleId);
        }
        return new Object[] { AppErrorCode.SUCCESS, roleQqGame.getOnceAwardStatus() == QqGameConstants.REWARD_STATUS_YES };
    }

    /**
     * 领取新手礼包
     * 
     * @param userRoleId
     * @return
     */
    public Object[] receiveXinShouGift(Long userRoleId) {
        if (!checkQqGamePlatform(userRoleId)) {
            return AppErrorCode.QQ_PLATFORM_ERROR;
        }
        RoleQqGame roleQqGame = getRoleQqGame(userRoleId);
        if (null == roleQqGame) {
            return null;
        }
        if (QqGameConstants.REWARD_STATUS_YES == roleQqGame.getOnceAwardStatus()) {
            return AppErrorCode.GET_ALREADY;
        }
        QqGameXinShouPublicConfig xinshouConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_GAME_XINSHOU);
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
        roleQqGame.setOnceAwardStatus(QqGameConstants.REWARD_STATUS_YES);
        roleQqGame.setUpdateTimestamp(GameSystemTime.getSystemMillTime());
        roleQqGameDao.cacheUpdate(roleQqGame, userRoleId);

        /* 发送奖励 */
        roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.QQGAME_XINSHOU, LogPrintHandle.GET_QQGAME_XINSHOU, LogPrintHandle.GBZ_QQGAME_XINSHOU, true);

        return AppErrorCode.OK;
    }

    /**
     * 获取成长礼包信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getLevelInfo(Long userRoleId) {
        if (!checkQqGamePlatform(userRoleId)) {
            return AppErrorCode.QQ_PLATFORM_ERROR;
        }
        RoleQqGame roleQqGame = getRoleQqGame(userRoleId);
        if (null == roleQqGame) {
            roleQqGame = createNewRoleQqGame(userRoleId);
        }
        QqGameLevelPublicConfig levelConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_GAME_LEVEL);
        if (null == levelConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        Map<Integer, Map<String, Integer>> levelRewardMap = levelConfig.getLevelItem();
        if (ObjectUtil.isEmpty(levelRewardMap)) {
            return AppErrorCode.CONFIG_ERROR;
        }
        List<Integer> levelArray = roleQqGame.getReceiveLevelArray();
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
        if (!checkQqGamePlatform(userRoleId)) {
            return AppErrorCode.QQ_PLATFORM_ERROR;
        }
        RoleQqGame roleQqGame = getRoleQqGame(userRoleId);
        if (null == roleQqGame) {
            return null;
        }
        // 领取状态校验
        List<Integer> levelArray = roleQqGame.getReceiveLevelArray();
        if (!ObjectUtil.isEmpty(levelArray) && levelArray.contains(levelVal)) {
            return AppErrorCode.GET_ALREADY;
        }
        // 奖励配置校验
        QqGameLevelPublicConfig levelConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_GAME_LEVEL);
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
        roleQqGame.updateLevelLog(levelVal);
        roleQqGame.setUpdateTimestamp(GameSystemTime.getSystemMillTime());
        roleQqGameDao.cacheUpdate(roleQqGame, userRoleId);
        /* 发送奖励 */
        roleBagExportService.putGoodsAndNumberAttr(rewardMap, userRoleId, GoodsSource.QQGAME_LEVEL, LogPrintHandle.GET_QQGAME_LEVEL, LogPrintHandle.GBZ_QQGAME_LEVEL, true);
        return new Object[] { AppErrorCode.SUCCESS, levelVal };
    }

    /**
     * 获取每日礼包信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getEveryInfo(Long userRoleId) {
        if (!checkQqGamePlatform(userRoleId)) {
            return AppErrorCode.QQ_PLATFORM_ERROR;
        }
        RoleQqGame roleQqGame = getRoleQqGame(userRoleId);
        if (null == roleQqGame) {
            roleQqGame = createNewRoleQqGame(userRoleId);
        }
        return new Object[] { AppErrorCode.SUCCESS, ObjectUtil.dayIsToday(roleQqGame.getEveryAwardTimestamp()) };
    }

    /**
     * 领取每日礼包
     * 
     * @param userRoleId
     * @return
     */
    public Object[] receiveEveryGift(Long userRoleId) {
        // 平台校验
        if (!checkQqGamePlatform(userRoleId)) {
            return AppErrorCode.QQ_PLATFORM_ERROR;
        }
        RoleQqGame roleQqGame = getRoleQqGame(userRoleId);
        if (null == roleQqGame) {
            return null;
        }
        // 领取状态校验
        Long everyTimestamp = roleQqGame.getEveryAwardTimestamp();
        if (ObjectUtil.dayIsToday(everyTimestamp)) {
            return AppErrorCode.GET_ALREADY;
        }
        // 奖励配置校验
        QqGameEveryDayPublicConfig everyConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_GAME_EVERY_DAY);
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
        roleQqGame.setEveryAwardTimestamp(nowTimestamp);
        roleQqGame.setUpdateTimestamp(nowTimestamp);
        roleQqGameDao.cacheUpdate(roleQqGame, userRoleId);
        /* 发送奖励 */
        roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.QQGAME_EVERY, LogPrintHandle.GET_QQGAME_EVERY, LogPrintHandle.GBZ_QQGAME_EVERY, true);
        return AppErrorCode.OK;
    }

}
