package com.junyou.bus.platform.qq.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.entity.RoleAccount;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.qq.confiure.export.QqLZKaiTongPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqLZXuFeiPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqLZchengzhangPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqLZdengjiPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqLZnianfeiPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqLZxinshouPublicConfig;
import com.junyou.bus.platform.qq.constants.QQGeZhongZuanMap;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.dao.RolePlatformQqLzDao;
import com.junyou.bus.platform.qq.dao.TencentLanzuanDao;
import com.junyou.bus.platform.qq.entity.RolePlatformQqLz;
import com.junyou.bus.platform.qq.entity.TencentLanzuan;
import com.junyou.bus.platform.qq.utils.OpensnsException;
import com.junyou.bus.platform.qq.utils.QqUtil;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.err.AppErrorCode;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class QqLanZuanGiftService {
	@Autowired
	private RolePlatformQqLzDao rolePlatformQqLzDao;
	@Autowired
	private PlatformGongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private QqService qqService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private TencentLanzuanDao tencentLanzuanDao;
	
	public List<TencentLanzuan> initTencentLanzuans(Long userRoleId){
		return tencentLanzuanDao.initTencentLanzuan(userRoleId);
	}
	
	private TencentLanzuan getTencentLanzuan(Long userRoleId){
		List<TencentLanzuan> list = tencentLanzuanDao.cacheAsynLoadAll(userRoleId);
		if(list == null || list.size() <= 0){
			TencentLanzuan lz = new TencentLanzuan();
			lz.setUserRoleId(userRoleId);
			lz.setStatus(0);
			lz.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			lz.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			
			tencentLanzuanDao.cacheInsert(lz, userRoleId);
			return lz;
		}
		return list.get(0);
	}
	
	public RolePlatformQqLz initRolePlatformQqLz(Long userRoleId) {
		List<RolePlatformQqLz> list = rolePlatformQqLzDao
				.initRolePlatformQqLz(userRoleId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private RolePlatformQqLz getRolePlatformQqLz(Long userRoleId) {
		return rolePlatformQqLzDao.cacheLoad(userRoleId, userRoleId);
	}

	private void createRolePlatformQqLz(Long userRoleId, Integer xinshouGift,Long meiriGiftUpdateTime, String chengzhangPickedGift) {
		RolePlatformQqLz platformQqLz = new RolePlatformQqLz();
		platformQqLz.setUserRoleId(userRoleId);
		platformQqLz.setXinshouGift(xinshouGift);
		platformQqLz.setMeiriGiftUpdateTime(meiriGiftUpdateTime);
		platformQqLz.setChengzhangPickedGift(chengzhangPickedGift);
		rolePlatformQqLzDao.cacheInsert(platformQqLz, userRoleId);
	}

	public Integer getGiftStatus(Long userRoleId) {
		QqLZchengzhangPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PlatformPublicConfigConstants.QQ_LZ_CHENGZHANG);
		if (publicConfig == null) {
			return null;
		}
		RolePlatformQqLz platformQqLz = getRolePlatformQqLz(userRoleId);
		int ret = 0;
		if (platformQqLz != null
				&& platformQqLz.getXinshouGift().intValue() == QqConstants.XINSHOU_GIFT_REWARDED) {
			ret = 1;
		}
		String[] rewardedArray = null;
		if (platformQqLz != null) {
			rewardedArray = platformQqLz.getChengzhangPickedGift().split(":");
		}
		List<Integer> nList = publicConfig.getnList();
		for (Integer e : nList) {
			boolean rewarded = false;
			if (platformQqLz != null) {
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
		QqLZxinshouPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PlatformPublicConfigConstants.QQ_LZ_XINSHOU);
		if (publicConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<Integer, Object> huangZuanInfo = qqService.getRoleQQInfo(userRoleId,true);
		if(huangZuanInfo == null){
			return AppErrorCode.QQ_ARGS_ERROR;
		}
		Integer huangzuanLevel = (Integer) huangZuanInfo.get(QqConstants.QQ_PLATFORM_INFO_LAN_ZUAN_LEVEL_KEY);
		if(huangzuanLevel == null || huangzuanLevel.intValue() == 0){
			return AppErrorCode.QQ_NOT_LANZUAN;
		}
		RolePlatformQqLz platformQqLz = getRolePlatformQqLz(userRoleId);
		if (platformQqLz != null) {
			if (platformQqLz.getXinshouGift().intValue() == QqConstants.XINSHOU_GIFT_REWARDED) {
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
        if (platformQqLz == null) {
            createRolePlatformQqLz(userRoleId,QqConstants.XINSHOU_GIFT_REWARDED, 0L, "");
        } else {
            platformQqLz.setXinshouGift(QqConstants.XINSHOU_GIFT_REWARDED);
            rolePlatformQqLzDao.cacheUpdate(platformQqLz, userRoleId);
        }
        // 发送奖励
		roleBagExportService.putGoodsAndNumberAttr(itemMap,userRoleId, GoodsSource.QQ_LZ_XINSHOU,LogPrintHandle.GET_QQ_LZ_XINSHOW,LogPrintHandle.GBZ_QQ_LZ_XINSHOW, true);
		/*JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(publicConfig.getXinshou(), null);
		GamePublishEvent.publishEvent(new QqHzXinshouLogEvent(userRoleId,jsonArray));*/
		return AppErrorCode.OK;
	}

	public Object[] getChengZhangGift(Long userRoleId, Integer n) {
		if(!PlatformConstants.isQQ()){
			return AppErrorCode.QQ_PLATFORM_ERROR;
		}
		QqLZchengzhangPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PlatformPublicConfigConstants.QQ_LZ_CHENGZHANG);
		if (publicConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<Integer, Object> lanZuanInfo = qqService.getRoleQQInfo(userRoleId,true);
		if(lanZuanInfo == null){
			return AppErrorCode.QQ_ARGS_ERROR;
		}
		Integer lanzuanLevel = (Integer) lanZuanInfo.get(QqConstants.QQ_PLATFORM_INFO_LAN_ZUAN_LEVEL_KEY);
		if(lanzuanLevel == null || lanzuanLevel.intValue() == 0){
			return AppErrorCode.QQ_NOT_LANZUAN;
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
		RolePlatformQqLz platformQqLz = getRolePlatformQqLz(userRoleId);
		if (platformQqLz != null) {
			String chengzhangGift = platformQqLz.getChengzhangPickedGift();
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
		if (platformQqLz == null) {
			createRolePlatformQqLz(userRoleId, 0, 0L, String.valueOf(n));
		} else {
			if(platformQqLz
					.getChengzhangPickedGift().equals("")){
				platformQqLz.setChengzhangPickedGift(String.valueOf(n));
			}else{
				platformQqLz.setChengzhangPickedGift(platformQqLz
						.getChengzhangPickedGift() + ":" + String.valueOf(n));
			}
			rolePlatformQqLzDao.cacheUpdate(platformQqLz, userRoleId);
		}
		// 发送奖励
		roleBagExportService.putGoodsAndNumberAttr(items, userRoleId, GoodsSource.QQ_LZ_CHENGZHANG, LogPrintHandle.GET_QQ_LZ_CHENGZHANG, LogPrintHandle.GBZ_QQ_LZ_CHENGZHANG, true);
		return new Object[]{AppErrorCode.SUCCESS,n};
	}

	public Boolean getMeiriGiftStatus(Long userRoleId) {
		RolePlatformQqLz platformQqLz = getRolePlatformQqLz(userRoleId);
		if (platformQqLz != null) {
			Long updateTime = platformQqLz.getMeiriGiftUpdateTime();
			if (updateTime != 0L
					&& DatetimeUtil.dayIsToday(updateTime,
							GameSystemTime.getSystemMillTime())) {
				return true;
			}
		}
		return false;
	}

    public Object[] getMeiriGift(Long userRoleId) {
        if (!PlatformConstants.isQQ()) {
            return AppErrorCode.QQ_PLATFORM_ERROR;
        }
        QqLZdengjiPublicConfig dengjiPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_LZ_DENGJI);
        if (dengjiPublicConfig == null) {
            return AppErrorCode.CONFIG_ERROR;
        }
        QqLZnianfeiPublicConfig nianfeiPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_LZ_NIANFEI);
        if (nianfeiPublicConfig == null) {
            return AppErrorCode.CONFIG_ERROR;
        }
        Map<Integer, Object> lanZuanInfo = qqService.getRoleQQInfo(userRoleId, true);
        if (lanZuanInfo == null) {
            return AppErrorCode.QQ_ARGS_ERROR;
        }
        Integer lanzuanLevel = (Integer) lanZuanInfo.get(QqConstants.QQ_PLATFORM_INFO_LAN_ZUAN_LEVEL_KEY);
        if (lanzuanLevel == null || lanzuanLevel.intValue() == 0) {
            return AppErrorCode.QQ_NOT_LANZUAN;
        }
        Long currentTime = GameSystemTime.getSystemMillTime();
        RolePlatformQqLz platformQqLz = getRolePlatformQqLz(userRoleId);
        if (platformQqLz != null) {
            if (platformQqLz.getMeiriGiftUpdateTime().longValue() != 0L) {
                if (DatetimeUtil.dayIsToday(platformQqLz.getMeiriGiftUpdateTime(), currentTime)) {
                    return AppErrorCode.QQ_MEIRI_GIFT_REWARED;
                }
            }
        }
        Map<String, Integer> itemAllMap = new HashMap<String, Integer>();
        // 等级奖励
        Map<String, Integer> itemMap1 = dengjiPublicConfig.getItemsByLevel(lanzuanLevel);
        ObjectUtil.mapAdd(itemAllMap, itemMap1);
        // 年费奖励
        Map<String, Integer> itemMap2 = null;
        Boolean nianfei = (Boolean) lanZuanInfo.get(QqConstants.QQ_PLATFORM_INFO_LAN_ZUAN_NIANFEI_KEY);
        if (nianfei) {
            itemMap2 = nianfeiPublicConfig.getNianfei();
            ObjectUtil.mapAdd(itemAllMap, itemMap2);
        }
        // 豪华奖励
        Map<String, Integer> itemMap3 = null;
        Boolean haohua = (Boolean) lanZuanInfo.get(QqConstants.QQ_PLATFORM_INFO_HAOHUA_LAN_ZUAN);
        if (haohua) {
            itemMap3 = nianfeiPublicConfig.getHaohua();
            ObjectUtil.mapAdd(itemAllMap, itemMap3);
        }
        // 背包空间不足
        Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(itemAllMap, userRoleId);
        if (code != null) {
            return code;
        }
        // 更新数据
        if (platformQqLz == null) {
            createRolePlatformQqLz(userRoleId, 0, currentTime, "");
        } else {
            platformQqLz.setMeiriGiftUpdateTime(currentTime);
            rolePlatformQqLzDao.cacheUpdate(platformQqLz, userRoleId);
        }
        // 发送奖励
        roleBagExportService.putGoodsAndNumberAttr(itemMap1, userRoleId, GoodsSource.QQ_LZ_DENGJI, LogPrintHandle.GET_QQ_LZ_DENGJI, LogPrintHandle.GBZ_QQ_LZ_DENGJI, true);
        if (nianfei) {
            roleBagExportService.putGoodsAndNumberAttr(itemMap2, userRoleId, GoodsSource.QQ_LZ_NIANFEI, LogPrintHandle.GET_QQ_LZ_NIANFEI, LogPrintHandle.GBZ_QQ_LZ_NIANFEI, true);
        }
        if (haohua) {
            roleBagExportService.putGoodsAndNumberAttr(itemMap3, userRoleId, GoodsSource.QQ_LZ_HAOHUA, LogPrintHandle.GET_QQ_LZ_HAOHUA, LogPrintHandle.GBZ_QQ_LZ_HAOHUA, true);
        }
        return new Object[] { AppErrorCode.SUCCESS, lanzuanLevel, nianfei };
    }
	
	public Object[] lingQuXuFei(Long userRoleId){
		//蓝钻等级大于0,,推送客服端蓝钻过期时间
		String blueInfo = null;
		try {
			blueInfo = qqService.getBuleVipInfo(userRoleId);
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		}
		
		int lanZuanLevel = 0;
		long lanZuanTime = 0;
		try {
			if(blueInfo != null){
				lanZuanLevel = QqUtil.lanZuanLevel(blueInfo);
				lanZuanTime = QqUtil.getLanZuanGuoQiTime(blueInfo);
			}
		} catch (OpensnsException e) {
			e.printStackTrace();
		}
		if(lanZuanLevel <= 0 || lanZuanTime <= 0){
			return AppErrorCode.QQ_NO_LANZUAN_XUFEI;
		}
		//保存的过期时间
		Long yuanTime = QQGeZhongZuanMap.getDaoQiTime(userRoleId);
		if(yuanTime >= lanZuanTime){
			return AppErrorCode.QQ_NO_LANZUAN_XUFEI;
		}
		QqLZXuFeiPublicConfig xufeiConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_LZ_XUFEI);
		if (xufeiConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(xufeiConfig.getXufei(), userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		//物品进背包
		roleBagExportService.putGoodsAndNumberAttr(xufeiConfig.getXufei(), userRoleId, GoodsSource.GOODS_GET_LZXF, LogPrintHandle.GET_RFB_LANZUANXUFEI, LogPrintHandle.GBZ_RFB_LANZUANXUFEI, true);
				
		
		QQGeZhongZuanMap.setDaoQiTime(userRoleId, lanZuanTime);
		return new Object[]{1,lanZuanTime};
	}
	
	public int updateTenCentLanZuan(String userId,String serverId){
		int reState = 0;
		//验证账号是否存在
		RoleAccount account = accountExportService.getRoleAccountFromDb(userId, serverId);
		if(account == null){
			reState = AppErrorCode.ACCOUNT_NOT_EXSIT;
			return reState;
		}
		//收到回调，如果是未开通就修改成开通，可领取状态
		TencentLanzuan lz = getTencentLanzuan(account.getUserRoleId());
		if(lz.getStatus() == QqConstants.WEI_KAITONG){
			lz.setStatus(QqConstants.YI_KAITONG);
			
			tencentLanzuanDao.cacheUpdate(lz, account.getUserRoleId());
		}
		
		return reState;
	}
	
	public Object[] getLanZuanInfo(Long userRoleId){
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		TencentLanzuan lz = getTencentLanzuan(userRoleId);
		int status = 0;
		if(lz.getStatus() == 2){
			status = 1;
		}
		return new Object[]{status,role.getServerId()};
	}
	
	public Object[] lingquLanZuan(Long userRoleId){
		TencentLanzuan lz = getTencentLanzuan(userRoleId);
		if(lz.getStatus() != QqConstants.YI_KAITONG){
			return AppErrorCode.QQ_NO_LANZUAN_XUFEI;
		}
		QqLZKaiTongPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_LZ_KAITONG);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(config.getItems(), userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		//物品进背包
		roleBagExportService.putGoodsAndNumberAttr(config.getItems(), userRoleId, GoodsSource.GOODS_GET_LZXF, LogPrintHandle.GET_RFB_LANZUANXUFEI, LogPrintHandle.GBZ_RFB_LANZUANXUFEI, true);
				
		lz.setStatus(QqConstants.YI_LINGQU);
		lz.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		
		tencentLanzuanDao.cacheUpdate(lz, userRoleId);
		
		return new Object[]{1};
	}
}
