package com.junyou.bus.platform.qq.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.qq.confiure.export.YaoQingHaoYouConfig;
import com.junyou.bus.platform.qq.confiure.export.YaoQingHaoYouConfigExportService;
import com.junyou.bus.platform.qq.dao.TencentYaoqingDao;
import com.junyou.bus.platform.qq.dao.TencentYaoqingLingquDao;
import com.junyou.bus.platform.qq.entity.TencentYaoqing;
import com.junyou.bus.platform.qq.entity.TencentYaoqingLingqu;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.err.AppErrorCode;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.accessor.AccessType;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * @author zhongdian
 * 2015-12-30 下午3:05:23
 */
@Service
public class TencentYaoQingService {

	
	@Autowired
	private TencentYaoqingDao tencentYaoqingDao;
	@Autowired
	private TencentYaoqingLingquDao tencentYaoqingLingquDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private YaoQingHaoYouConfigExportService yaoQingHaoYouConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	public List<TencentYaoqingLingqu> initTencentYaoqingLingqus(Long userRoleId){
		return tencentYaoqingLingquDao.initTencentYaoqingLingqu(userRoleId);
	}
	
	private TencentYaoqingLingqu getTencentYaoqingLingqu(Long userRoleId){
		List<TencentYaoqingLingqu> list = tencentYaoqingLingquDao.cacheAsynLoadAll(userRoleId);
		if(list == null || list.size() <= 0){
			TencentYaoqingLingqu yaoqing = new TencentYaoqingLingqu();
			yaoqing.setUserRoleId(userRoleId);
			yaoqing.setLingquId("");
			yaoqing.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			yaoqing.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			
			tencentYaoqingLingquDao.cacheInsert(yaoqing, userRoleId);
			return yaoqing;
		}
		return list.get(0);
	}
	
	public void insertYaoQing(String userId,String iopenId){
		TencentYaoqing yaoqing = new TencentYaoqing();
		yaoqing.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		yaoqing.setUserId(userId);
		yaoqing.setIopenId(iopenId);
		yaoqing.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		
		tencentYaoqingDao.insert(yaoqing, null, AccessType.getDirectDbType());
	}
	
	
	public Object[] getLingQuInfo(Long userRoleId){
		TencentYaoqingLingqu yaoqing = getTencentYaoqingLingqu(userRoleId);
		if("".equals(yaoqing.getLingquId())){
			return null;
		}
		Object[] str = yaoqing.getLingquId().split(",");
		return str;
	}
	
	public int getYaoQingCount(Long userRoleId){
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			return 0;
		}
		YaoQingHaoYouConfig config = yaoQingHaoYouConfigExportService.loadById(-1);
		if(config == null){
			return 0;
		}
		int count = tencentYaoqingDao.getYaoQingCount(role.getUserId(), config.getLevel());
		return count;
	}
	
	public Object[] lingquYaoQing(Long userRoleId,int id){
		TencentYaoqingLingqu yaoqing = getTencentYaoqingLingqu(userRoleId);
		if(!isLingqu(yaoqing.getLingquId(), id)){
			return AppErrorCode.QQ_YAOQING_YI_LINGQU;
		}
		YaoQingHaoYouConfig config = yaoQingHaoYouConfigExportService.loadById(id);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(config.getItem(), userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		//修改状态
		yaoqing.setLingquId(setLingQuId(yaoqing.getLingquId(), id));
		yaoqing.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		
		tencentYaoqingLingquDao.cacheUpdate(yaoqing, userRoleId);
		//物品进背包
		roleBagExportService.putGoodsAndNumberAttr(config.getItem(), userRoleId, GoodsSource.TENCENT_YAOQING_HAOYOU, LogPrintHandle.GET_TENCENT_YAOQING, LogPrintHandle.GBZ_TENCENT_YAOQING, true);
				
		return new Object[]{1,id};
	}

	private String setLingQuId(String lingquId,int id){
		if(lingquId == null || "".equals(lingquId)){
			return id+"";
		}else{
			return lingquId+","+id;
		}
	}
	
	private boolean isLingqu(String lingquId,int id){
		if(lingquId == null || "".equals(lingquId)){
			return true;
		}
		Object[] ids = lingquId.split(",");
		for (int i = 0; i < ids.length; i++) {
			if(id == Integer.parseInt(ids[i].toString())){
				return false;
			}
		}
		return true;
	}
	
}
