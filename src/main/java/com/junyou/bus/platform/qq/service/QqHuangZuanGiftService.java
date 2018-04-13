package com.junyou.bus.platform.qq.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.qq.confiure.export.QqHZchengzhangPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqHZdengjiPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqHZnianfeiPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqHZxinshouPublicConfig;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.dao.RolePlatformQqHzDao;
import com.junyou.bus.platform.qq.entity.RolePlatformQqHz;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.err.AppErrorCode;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class QqHuangZuanGiftService {
	@Autowired
	private RolePlatformQqHzDao rolePlatformQqHzDao;
	@Autowired
	private PlatformGongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private QqService qqService;

	public RolePlatformQqHz initRolePlatformQqHz(Long userRoleId) {
		List<RolePlatformQqHz> list = rolePlatformQqHzDao
				.initRolePlatformQqHz(userRoleId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private RolePlatformQqHz getRolePlatformQqHz(Long userRoleId) {
		return rolePlatformQqHzDao.cacheLoad(userRoleId, userRoleId);
	}

	private void createRolePlatformQqHz(Long userRoleId, Integer xinshouGift,
			Long meiriGiftUpdateTime, String chengzhangPickedGift) {
		RolePlatformQqHz platformQqHz = new RolePlatformQqHz();
		platformQqHz.setUserRoleId(userRoleId);
		platformQqHz.setXinshouGift(xinshouGift);
		platformQqHz.setMeiriGiftUpdateTime(meiriGiftUpdateTime);
		platformQqHz.setChengzhangPickedGift(chengzhangPickedGift);
		rolePlatformQqHzDao.cacheInsert(platformQqHz, userRoleId);
	}

	public Integer getGiftStatus(Long userRoleId) {
		QqHZchengzhangPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PlatformPublicConfigConstants.QQ_HZ_CHENGZHANG);
		if (publicConfig == null) {
			return null;
		}
		RolePlatformQqHz platformQqHz = getRolePlatformQqHz(userRoleId);
		int ret = 0;
		if (platformQqHz != null
				&& platformQqHz.getXinshouGift().intValue() == QqConstants.XINSHOU_GIFT_REWARDED) {
			ret = 1;
		}
		String[] rewardedArray = null;
		if (platformQqHz != null) {
			rewardedArray = platformQqHz.getChengzhangPickedGift().split(":");
		}
		List<Integer> nList = publicConfig.getnList();
		for (Integer e : nList) {
			boolean rewarded = false;
			if (platformQqHz != null) {
				for (String s : rewardedArray) {
					if (s.equals(String.valueOf(e))) {
						rewarded = true;
						break;
					}
				}
			}
			ret = ret | ((rewarded ? 1 : 0) << e);
		}
		return ret;
	}

	public Object[] getXinShouGift(Long userRoleId) {
		if(!PlatformConstants.isQQ()){
			return AppErrorCode.QQ_PLATFORM_ERROR;
		}
		QqHZxinshouPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PlatformPublicConfigConstants.QQ_HZ_XINSHOU);
		if (publicConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<Integer, Object> huangZuanInfo = qqService.getRoleQQInfo(userRoleId,true);
		if(huangZuanInfo == null){
			return AppErrorCode.QQ_ARGS_ERROR;
		}
		Integer huangzuanLevel = (Integer) huangZuanInfo.get(QqConstants.QQ_PLATFORM_INFO_HUANG_ZUAN_LEVEL_KEY);
		if(huangzuanLevel == null || huangzuanLevel.intValue() == 0){
			return AppErrorCode.QQ_NOT_HUANGZUAN;
		}
		RolePlatformQqHz platformQqHz = getRolePlatformQqHz(userRoleId);
		if (platformQqHz != null) {
			if (platformQqHz.getXinshouGift().intValue() == QqConstants.XINSHOU_GIFT_REWARDED) {
				return AppErrorCode.QQ_XINSHOU_GIFT_REWARED;
			}
		}
		Map<String, Integer> itemMap = publicConfig.getXinshou();
		 // 背包空间不足
        Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(itemMap, userRoleId);
        if (code != null) {
            return code;
        }
		// 更新数据
		if (platformQqHz == null) {
		    createRolePlatformQqHz(userRoleId,
		            QqConstants.XINSHOU_GIFT_REWARDED, 0L, "");
		} else {
		    platformQqHz.setXinshouGift(QqConstants.XINSHOU_GIFT_REWARDED);
		    rolePlatformQqHzDao.cacheUpdate(platformQqHz, userRoleId);
		}
		// 发送奖励
        roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.QQ_HZ_XINSHOU, LogPrintHandle.GET_QQ_HZ_XINSHOW, LogPrintHandle.GBZ_QQ_HZ_XINSHOW, true);
        /*
         * JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(
         * publicConfig.getXinshou(), null); GamePublishEvent.publishEvent(new
         * QqHzXinshouLogEvent(userRoleId, jsonArray));
         */
		return AppErrorCode.OK;
	}

	public Object[] getChengZhangGift(Long userRoleId, Integer n) {
		if(!PlatformConstants.isQQ()){
			return AppErrorCode.QQ_PLATFORM_ERROR;
		}
		QqHZchengzhangPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PlatformPublicConfigConstants.QQ_HZ_CHENGZHANG);
		if (publicConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<Integer, Object> huangZuanInfo = qqService.getRoleQQInfo(userRoleId,true);
		if(huangZuanInfo == null){
			return AppErrorCode.QQ_ARGS_ERROR;
		}
		Integer huangzuanLevel = (Integer) huangZuanInfo.get(QqConstants.QQ_PLATFORM_INFO_HUANG_ZUAN_LEVEL_KEY);
		if(huangzuanLevel == null || huangzuanLevel.intValue() == 0){
			return AppErrorCode.QQ_NOT_HUANGZUAN;
		}
		UserRole userRole = roleExportService.getUserRole(userRoleId);
		if (userRole == null) {
			return AppErrorCode.ERR;
		}
		Integer dengji = publicConfig.getLevelByN(n);
		if (dengji == null) {
			return AppErrorCode.QQ_CHENGZHANG_GIFT_NOT_EXSITS;
		}
		Map<String, Integer> items = publicConfig.getItemsByN(n);
		if (items == null) {
			return AppErrorCode.QQ_CHENGZHANG_GIFT_NOT_EXSITS;
		}
		if (userRole.getLevel() < dengji) {
			return AppErrorCode.QQ_CHENGZHANG_GIFT_LEVEL_LIMIT;
		}
		RolePlatformQqHz platformQqHz = getRolePlatformQqHz(userRoleId);
		if (platformQqHz != null) {
			String chengzhangGift = platformQqHz.getChengzhangPickedGift();
			if (!chengzhangGift.equals("")) {
				String[] dengjiArray = chengzhangGift.split(":");
				for (String e : dengjiArray) {
					if (e.equals(String.valueOf(n))) {
						return AppErrorCode.QQ_CHENGZHANG_GIFT_REWARED;
					}
				}
			}
        }
        // 背包空间不足
        Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(items, userRoleId);
        if (code != null) {
            return code;
        }
        // 更新数据
        if (platformQqHz == null) {
            createRolePlatformQqHz(userRoleId, 0, 0L, String.valueOf(n));
        } else {
            if (platformQqHz.getChengzhangPickedGift().equals("")) {
                platformQqHz.setChengzhangPickedGift(String.valueOf(n));
            } else {
                platformQqHz.setChengzhangPickedGift(platformQqHz.getChengzhangPickedGift() + ":" + String.valueOf(n));
            }
            rolePlatformQqHzDao.cacheUpdate(platformQqHz, userRoleId);
        }
        // 发送奖励
        roleBagExportService.putGoodsAndNumberAttr(items, userRoleId, GoodsSource.QQ_HZ_CHENGZHANG, LogPrintHandle.GET_QQ_HZ_CHENGZHANG, LogPrintHandle.GBZ_QQ_HZ_CHENGZHANG, true);
        return new Object[] { AppErrorCode.SUCCESS, n };
	}

	public Boolean getMeiriGiftStatus(Long userRoleId) {
		RolePlatformQqHz platformQqHz = getRolePlatformQqHz(userRoleId);
		if (platformQqHz != null) {
			Long updateTime = platformQqHz.getMeiriGiftUpdateTime();
			if (updateTime != 0L
					&& DatetimeUtil.dayIsToday(updateTime,
							GameSystemTime.getSystemMillTime())) {
				return true;
			}
		}
		return false;
	}

	public Object[] getMeiriGift(Long userRoleId) {
		if(!PlatformConstants.isQQ()){
			return AppErrorCode.QQ_PLATFORM_ERROR;
		}
		QqHZdengjiPublicConfig dengjiPublicConfig = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PlatformPublicConfigConstants.QQ_HZ_DENGJI);
		if (dengjiPublicConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		QqHZnianfeiPublicConfig nianfeiPublicConfig = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PlatformPublicConfigConstants.QQ_HZ_NIANFEI);
		if (nianfeiPublicConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<Integer, Object> huangZuanInfo = qqService.getRoleQQInfo(userRoleId,true);
		if(huangZuanInfo == null){
			return AppErrorCode.QQ_ARGS_ERROR;
		}
		Integer huangzuanLevel = (Integer) huangZuanInfo.get(QqConstants.QQ_PLATFORM_INFO_HUANG_ZUAN_LEVEL_KEY);
		if(huangzuanLevel == null || huangzuanLevel.intValue() == 0){
			return AppErrorCode.QQ_NOT_HUANGZUAN;
		}
		Long currentTime = GameSystemTime.getSystemMillTime();
		RolePlatformQqHz platformQqHz = getRolePlatformQqHz(userRoleId);
		if (platformQqHz != null) {
			if (platformQqHz.getMeiriGiftUpdateTime().longValue() != 0L) {
				if (DatetimeUtil.dayIsToday(
						platformQqHz.getMeiriGiftUpdateTime(), currentTime)) {
					return AppErrorCode.QQ_MEIRI_GIFT_REWARED;
				}
			}
		}
		Map<String, Integer> itemAllMap = new HashMap<String, Integer>();
		// 等级礼包
		Map<String, Integer> itemMap1 = dengjiPublicConfig.getItemsByLevel(huangzuanLevel);
		ObjectUtil.mapAdd(itemAllMap, itemMap1);
		// 年费礼包
		Map<String, Integer> itemMap2 =null;
        Boolean nianfei = (Boolean) huangZuanInfo.get(QqConstants.QQ_PLATFORM_INFO_HUANG_ZUAN_NIANFEI_KEY);
        if (nianfei) {
            itemMap2 = nianfeiPublicConfig.getNianfei();
            ObjectUtil.mapAdd(itemAllMap, itemMap2);
        }
		// 背包空间不足
        Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(itemAllMap, userRoleId);
        if (code != null) {
            return code;
        }
       // 更新数据
        if (platformQqHz == null) {
            createRolePlatformQqHz(userRoleId, 0, currentTime, "");
        } else {
            platformQqHz.setMeiriGiftUpdateTime(currentTime);
            rolePlatformQqHzDao.cacheUpdate(platformQqHz, userRoleId);
        }
        // 发送奖励
        roleBagExportService.putGoodsAndNumberAttr(itemMap1, userRoleId, GoodsSource.QQ_HZ_DENGJI, LogPrintHandle.GET_QQ_HZ_DENGJI, LogPrintHandle.GBZ_QQ_HZ_DENGJI, true);
        if (nianfei) {
            roleBagExportService.putGoodsAndNumberAttr(itemMap2, userRoleId, GoodsSource.QQ_HZ_NIANFEI, LogPrintHandle.GET_QQ_HZ_NIANFEI, LogPrintHandle.GBZ_QQ_HZ_NIANFEI, true);
        }
        return new Object[] { AppErrorCode.SUCCESS, huangzuanLevel, nianfei };
	}
}
