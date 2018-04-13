package com.junyou.bus.platform.qq.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.qq.confiure.export.QqBaoZidengjiPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqTgpPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqTgpchengzhangPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqWeiDuanPublicConfig;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.dao.TencentWeiduanDao;
import com.junyou.bus.platform.qq.entity.TencentWeiduan;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.err.AppErrorCode;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author zhongdian
 * 2015-8-5 下午3:05:17
 */
@Service
public class TencentWeiDuanService {

	@Autowired
	private TencentWeiduanDao tencentWeiduanDao;
	@Autowired
	private PlatformGongGongShuJuBiaoConfigExportService platformGongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private QqService qqService;
	public List<TencentWeiduan> initTencentWeiduan(Long userRoleId){
		return tencentWeiduanDao.initTencentWeiduan(userRoleId);
	}
	
	public TencentWeiduan getTencentWeiDuan(Long userRoleId){
		List<TencentWeiduan> weiduans = tencentWeiduanDao.cacheAsynLoadAll(userRoleId);
		if(weiduans == null || weiduans.size() <= 0){
			TencentWeiduan weiduan = new TencentWeiduan();
			weiduan.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			weiduan.setUserRoleId(userRoleId);
			weiduan.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			weiduan.setTgpStatus(0);
			weiduan.setWeiduanStatus(0);
			weiduan.setDtgpStatus(0);
			weiduan.setTgpLevelStatus(0);
			weiduan.setBaoziStatus(0);
			tencentWeiduanDao.cacheInsert(weiduan, userRoleId);
			
			return weiduan;
		}
		TencentWeiduan weiduan = weiduans.get(0);
		//不是同一天，清理数据
		if(!DateUtils.isSameDay(weiduan.getUpdateTime(), new Timestamp(GameSystemTime.getSystemMillTime()))){
			weiduan.setTgpStatus(0);
			weiduan.setWeiduanStatus(0);
			weiduan.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			weiduan.setBaoziStatus(0);
			tencentWeiduanDao.cacheUpdate(weiduan, userRoleId);
		}
		
		return weiduan;
	}
	
	/**
	 * 获取微端领取状态
	 * @param userRoleId
	 * @return
	 */
	public Integer getWeiDuanInfo(Long userRoleId){
		TencentWeiduan weiduan = getTencentWeiDuan(userRoleId);
		
		return weiduan.getWeiduanStatus();
		
	}
	
	
	public Object[] lingQuWeiduan(Long userRoleId,int id){
		TencentWeiduan weiduan = getTencentWeiDuan(userRoleId);
		QqWeiDuanPublicConfig config  = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_WEIDUAN);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//判断是否已经领取过奖励
		if(!isCanRecevieState(weiduan.getWeiduanStatus().intValue(), id)){
			return new Object[]{2,id};
		}
		Map<String, Integer> goodMap = config.getWeiDuanMap().get(id);
		if(goodMap == null || goodMap.size() <= 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(goodMap, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		//修改状态
		weiduan.setWeiduanStatus(chanageState(weiduan.getWeiduanStatus(), id));
		//物品进背包
		roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.GOODS_QQ_WEIDUAN_LINGQU, LogPrintHandle.GET_QQ_WEIDUAN_GIFT, LogPrintHandle.GBZ_QQ_WENDUAN_GIFT, true);
		
		tencentWeiduanDao.cacheUpdate(weiduan, userRoleId);
		
		return new Object[]{1,id};
	}
	
	/**
	 * 获取TGP领取状态
	 * @param userRoleId
	 * @return
	 */
	public Integer getTgpInfo(Long userRoleId){
		TencentWeiduan weiduan = getTencentWeiDuan(userRoleId);
		
		return getStatus(weiduan.getTgpStatus(),weiduan.getDtgpStatus(), weiduan.getTgpLevelStatus());
		
	}
	
	public Object[] lingQuTgp(Long userRoleId){
		TencentWeiduan weiduan = getTencentWeiDuan(userRoleId);
		QqTgpPublicConfig config  = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_TGP);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//判断是否已经领取过奖励
		if(weiduan.getTgpStatus().intValue() != 0){
			return new Object[]{2};
		}
		Map<String, Integer> goodMap = config.getTgpMap();
		if(goodMap == null || goodMap.size() <= 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(goodMap, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		//修改状态
		weiduan.setTgpStatus(1);
		//物品进背包
		roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.GOODS_QQ_TGP_LINGQU, LogPrintHandle.GET_QQ_TGP_GIFT, LogPrintHandle.GBZ_QQ_TGP_GIFT, true);
		
		tencentWeiduanDao.cacheUpdate(weiduan, userRoleId);
		
		return new Object[]{1};
	}
	
	
	public Object[] lingQuTgpLogin(Long userRoleId){
		TencentWeiduan weiduan = getTencentWeiDuan(userRoleId);
		QqTgpPublicConfig config  = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_TGP_LOGIN);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//判断是否已经领取过奖励
		if(weiduan.getDtgpStatus().intValue() != 0){
			return new Object[]{2};
		}
		Map<String, Integer> goodMap = config.getTgpMap();
		if(goodMap == null || goodMap.size() <= 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(goodMap, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		//修改状态
		weiduan.setDtgpStatus(1);
		//物品进背包
		roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.GOODS_QQ_TGP_LOGIN_LINGQU, LogPrintHandle.GET_QQ_TGP_LOGIN_GIFT, LogPrintHandle.GBZ_QQ_TGP_LOGIN_GIFT, true);
		
		tencentWeiduanDao.cacheUpdate(weiduan, userRoleId);
		
		return new Object[]{1};
	}
	
	
	public Object[] lingQuTgpLevel(Long userRoleId,int id){
		TencentWeiduan weiduan = getTencentWeiDuan(userRoleId);
		QqTgpchengzhangPublicConfig config  = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_TGP_CHENGZHANG);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//判断是否已经领取过奖励
		if(!isCanRecevieState(weiduan.getTgpLevelStatus().intValue(), id)){
			return new Object[]{2,id};
		}
		Map<Integer, Integer> levelMap = config.getLevels();
		if(levelMap == null || levelMap.size() <= 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		//需求等级
		int level = levelMap.get(id);
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role.getLevel() < level){
			return AppErrorCode.QQ_CHENGZHANG_GIFT_LEVEL_LIMIT;
		}
		Map<String, Integer> goodMap = config.getItems().get(level);
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(goodMap, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		//修改状态
		weiduan.setTgpLevelStatus(chanageState(weiduan.getTgpLevelStatus(), id));
		//物品进背包
		roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.GOODS_QQ_TGP_LEVEL_LINGQU, LogPrintHandle.GET_QQ_TGP_LEVEL_GIFT, LogPrintHandle.GBZ_QQ_TGP_LEVEL_GIFT, true);
		
		tencentWeiduanDao.cacheUpdate(weiduan, userRoleId);
		
		return new Object[]{1,id};
	}
	
	
	public boolean get3366BaoZiInfo(Long userRoleId){
		TencentWeiduan weiduan = getTencentWeiDuan(userRoleId);
		
		return weiduan.getBaoziStatus() == 1;
		
	}
	
	public Object[] lingQu3366BaoZiLevel(Long userRoleId,int id){
		TencentWeiduan weiduan = getTencentWeiDuan(userRoleId);
		QqBaoZidengjiPublicConfig config  = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_3366_LEVEL);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//判断是否已经领取过奖励
		if(weiduan.getBaoziStatus().intValue() != 0){
			return new Object[]{2};
		}
		Map<Integer, Map<String, Integer>> baoziMap = config.getItems();
		if(baoziMap == null || baoziMap.size() <= 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<Integer, Object> lanZuanInfo = qqService.getRoleQQInfo(userRoleId,true);
		if(lanZuanInfo == null){
			return AppErrorCode.QQ_ARGS_ERROR;
		}
		//包子等级
		Integer baoziLevel = (Integer) lanZuanInfo.get(QqConstants.QQ_PLATFORM_INFO_3366_BAOZI);
		Map<String, Integer> goodMap = getGoodMapByBaoZi(baoziMap, baoziLevel);
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(goodMap, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		//修改状态
		weiduan.setBaoziStatus(1);
		//物品进背包
		roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.GOODS_QQ_3366_BAOZI_LINGQU, LogPrintHandle.GET_QQ_3366_BAOZI, LogPrintHandle.GBZ_QQ_3366_BAOZI, true);
		
		tencentWeiduanDao.cacheUpdate(weiduan, userRoleId);
		
		return new Object[]{1,id};
	}
	/**
	 * 根据包子等级获取配置物品
	 * @return
	 */
	private Map<String, Integer> getGoodMapByBaoZi(Map<Integer, Map<String, Integer>> baoziMap,int baoziLevel){
		Map<String, Integer> map = null;
		int level = 0;
		for (Integer lv : baoziMap.keySet()) {
			if(baoziLevel >= lv && lv >= level){
				map = baoziMap.get(lv);
				level = lv;
			}
		}
		
		return map;
	}
	
	/**
	 * 判断是否已经领取奖励
	 * @param state
	 * @param day
	 * @return true:可领取奖励
	 */
	public static Boolean isCanRecevieState(Integer state, Integer day){
		if(!state.equals(0)){
			day = day.intValue() - 1;
			
			return (state >> day & 1) == 0;
		}
		return true;
	}
	
	/**
	 * 修改状态
	 * @param state
	 * @return
	 */
	public static Integer chanageState(Integer state, Integer day){
		day = day.intValue() - 1;
		
		return (1 << day) | state;
	}
	
	/**
	 * 
	 * @param meiri  每日礼包
	 * @param di     第一次登陆礼包
	 * @param level  等级礼包
	 * @return
	 */
	public static Integer getStatus(Integer meiri,Integer di,Integer level){
		Integer state = 0;
		if(meiri > 0){
			meiri = meiri.intValue() - 1;
			state = (1 << meiri) | state;
		}
		if(di > 0){
			di = di.intValue();
			state = (1 << di) | state;
		}
		if(level > 0){
			state = (level << 2) | state;
		}
		return state;
	}
	
}
