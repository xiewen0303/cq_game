package com.junyou.bus.platform.yuenan.service;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.yuenan.confiure.export.YueNanYaoQingPublicConfig;
import com.junyou.bus.platform.yuenan.dao.YuenanYaoqingDao;
import com.junyou.bus.platform.yuenan.entity.YuenanYaoqing;
import com.junyou.err.AppErrorCode;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author zhongdian
 * 2015-12-3 上午11:15:52
 */
@Service
public class YuenanYaoqingService {

	@Autowired
	private YuenanYaoqingDao yuenanYaoqingDao;
	@Autowired
	private PlatformGongGongShuJuBiaoConfigExportService platformGongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	
	public List<YuenanYaoqing> initYuenanYaoqings(Long userRoleId){
		return yuenanYaoqingDao.initYuenanYaoqing(userRoleId);
	}
	
	
	public YuenanYaoqing getYuenanYaoqing(Long userRoleId){
		List<YuenanYaoqing> list = yuenanYaoqingDao.cacheAsynLoadAll(userRoleId);
		if(list == null || list.size() <= 0){
			YuenanYaoqing yaoqing = new YuenanYaoqing();
			yaoqing.setUserRoleId(userRoleId);
			yaoqing.setHaoyouCount(0);
			yaoqing.setLingquCount(0);
			yaoqing.setYaoqingId("");
			yaoqing.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			yaoqing.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			
			yuenanYaoqingDao.cacheInsert(yaoqing, userRoleId);
			
			return yaoqing;
		}
		YuenanYaoqing yaoqing = list.get(0);
		//不是同一天，清理数据
		if(!DateUtils.isSameDay(yaoqing.getUpdateTime(), new Timestamp(GameSystemTime.getSystemMillTime()))){
			yaoqing.setHaoyouCount(0);
			yaoqing.setLingquCount(0);
			yaoqing.setYaoqingId("");
			yaoqing.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			
			yuenanYaoqingDao.cacheUpdate(yaoqing, userRoleId);
		}
		return yaoqing;
	}
	

	public Object[] lingQu(Long userRoleId){
		YuenanYaoqing yaoqing = getYuenanYaoqing(userRoleId);
		YueNanYaoQingPublicConfig config = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.YUENAN_YAOQING_HAOYOU);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//领奖次数大于配置的次数
		if(yaoqing.getLingquCount() >= config.getLingQuCount()){
			return AppErrorCode.YUENAN_YAOQING_LINGQU_NOT_COUNT;
		}
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(config.getItemMap(), userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		//物品进背包
		roleBagExportService.putGoodsAndNumberAttr(config.getItemMap(), userRoleId, GoodsSource.YUENAN_YAOQING_HAOYOU, LogPrintHandle.GET_YN_YQ_HY, LogPrintHandle.GBZ_YN_YQ_HY, true);
				
		yaoqing.setLingquCount(yaoqing.getLingquCount() +1);
		
		yuenanYaoqingDao.cacheUpdate(yaoqing, userRoleId);
		
		return new Object[]{1};
	}
	
	public void cunchuYaoQingId(Long userRoleId,Object[] ids){
		YuenanYaoqing yaoqing = getYuenanYaoqing(userRoleId);
		String id = yaoqing.getYaoqingId();
		for (int i = 0; i < ids.length; i++) {
			if("".equals(id)){
				id = ids[i].toString();
			}else{
				id = id + "," + ids[i];
			}
		}
		String[] str = id.split(",");
		if(str.length > 15){
			return;
		}else{
			yaoqing.setYaoqingId(id);
		}
		
		yuenanYaoqingDao.cacheUpdate(yaoqing, userRoleId);
		return;
	}
	
	public String getYaoqingId(Long userRoleId){
		YuenanYaoqing yaoqing = getYuenanYaoqing(userRoleId);
		return yaoqing.getYaoqingId();
	}
	
	public int getLingQuCount(Long userRoleId){
		YuenanYaoqing yaoqing = getYuenanYaoqing(userRoleId);
		return yaoqing.getLingquCount();
	}
	
}
