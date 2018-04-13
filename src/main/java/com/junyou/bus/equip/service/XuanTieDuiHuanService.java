package com.junyou.bus.equip.service;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.equip.configure.export.XuanTieDuiHuanConfig;
import com.junyou.bus.equip.configure.export.XuanTieDuiHuanConfigExportService;
import com.junyou.bus.equip.dao.XuantieDuihuanDao;
import com.junyou.bus.equip.entity.XuantieDuihuan;
import com.junyou.bus.equip.filter.XuanTieDuiHuanIdFilter;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.err.AppErrorCode;
import com.junyou.event.XuanTieDuihuanLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * 玄铁兑换
 * @author wind
 * @email  18221610336@163.com
 * @date  2014-12-31 上午10:24:53
 */
@Service
public class XuanTieDuiHuanService{

	@Autowired
	private XuantieDuihuanDao xuantieDuihuanDao;
	@Autowired
	private XuanTieDuiHuanConfigExportService xuanTieDuiHuanConfigExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
	
	public List<XuantieDuihuan> initXuanTieHuiHuan(Long userRoleId){
		return xuantieDuihuanDao.initXuantieDuihuan(userRoleId);
		
	}
	
	public Object[] getDuiHuanInfo(Long userRoleId){
		List<XuantieDuihuan> list = xuantieDuihuanDao.cacheLoadAll(userRoleId);
		if(list == null || list.size() <= 0){
			return new Object[]{1,roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId).getXuanTieValue(),new Object[]{}};
		}
		List<Object[]> returnList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			XuantieDuihuan duihuan = list.get(i);
			if(!ObjectUtil.dayIsToday(duihuan.getUpdateTime())){//不是当前天  修改为0
				duihuan.setCount(0);
				duihuan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				
				xuantieDuihuanDao.cacheUpdate(duihuan, userRoleId);
			}else{
				Object[] obj = new Object[]{duihuan.getConfigId(),duihuan.getCount()};
				returnList.add(obj);
			}
			
		}
		
		
		return new Object[]{1,roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId).getXuanTieValue(),returnList.toArray()};
	}
	
	public Object[] duihuan(Long userRoleId,int id){
		XuanTieDuiHuanConfig config = xuanTieDuiHuanConfigExportService.loadById(id);
		if(config == null){
			return AppErrorCode.XUANTIE_CONFIG_ERROR;
		}
		//判断等级
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper.getLevel() < config.getNeedlevel()){
			return AppErrorCode.XUANTIE_NOT_LEVEL;
		}
		//判玄铁值
		int myXtz = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId).getXuanTieValue();
		if(myXtz < config.getNeedxt()){
			return AppErrorCode.XUANTIE_NOT_VALUE;
		}
		
		//检测背包
		Object[] result = roleBagExportService.checkPutInBag(config.getItemid(), 1, userRoleId);
		if(result != null){
			return result;
		}
		
		List<XuantieDuihuan> duihuanList =  xuantieDuihuanDao.cacheLoadAll(userRoleId, new XuanTieDuiHuanIdFilter(id));
		
		XuantieDuihuan duihuan = null;
		
		if(duihuanList != null && duihuanList.size() > 0){
			duihuan = duihuanList.get(0);
			if(!ObjectUtil.dayIsToday(duihuan.getUpdateTime())){//不是当前天  修改为0
				duihuan.setCount(0);
				duihuan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				
				xuantieDuihuanDao.cacheUpdate(duihuan, userRoleId);
			}
		}else{
			duihuan = new XuantieDuihuan();
			
			duihuan.setConfigId(id);
			duihuan.setCount(0);
			duihuan.setUserRoleId(userRoleId);
			duihuan.setCreateTime(new Timestamp(System.currentTimeMillis()));
			duihuan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			duihuan.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			
			xuantieDuihuanDao.cacheInsert(duihuan, userRoleId);
		}
		
		//兑换次数
		if(duihuan.getCount() >= config.getMaxcount()){
			return AppErrorCode.XUANTIE_NOT_COUNT;
		}
		
		//物品入背包
		RoleItemInput item = BagUtil.createItem(config.getItemid(), 1, 0);
		roleBagExportService.putInBag(item, userRoleId, GoodsSource.XUANTIE_DUIHUAN, true);
		//消耗玄铁值
		roleBusinessInfoExportService.costXuanTieVal(userRoleId, config.getNeedxt());
		//记录兑换次数
		duihuan.setCount(duihuan.getCount() +1);
		duihuan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		xuantieDuihuanDao.cacheUpdate(duihuan, userRoleId);
		
		//日志
		GamePublishEvent.publishEvent(new XuanTieDuihuanLogEvent(userRoleId, roleWrapper.getName(), config.getNeedxt(), config.getItemid(), 1));
		
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A10);
		return new Object[]{1,id,duihuan.getCount(),roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId).getXuanTieValue()};
	}
	
}