package com.junyou.bus.setting.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.junyou.bus.setting.RoleSettingOutputWrapper;
import com.junyou.bus.setting.dao.RoleSettingDao;
import com.junyou.bus.setting.entity.RoleSetting;
import com.junyou.public_.share.export.PublicRoleStateExportService;

/**
 * 角色设置（快捷键等）
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-16 下午6:46:06 
 */
@Service
public class RoleSettingService {
	@Autowired
	private RoleSettingDao roleSettingDao;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	/**
	 * 获取角色设置信息
	 * @param userRoleId
	 * @return 
	 */
	private RoleSetting getRoleSetting(Long userRoleId){
		return roleSettingDao.cacheAsynLoad(userRoleId, userRoleId);
	}
	
	/**
	 * 获取玩家快捷键信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getQuickbarInfo(Long userRoleId){
		RoleSetting roleSetting = getRoleSetting(userRoleId);
		if(roleSetting == null){
			return null;
		}
		
		return RoleSettingOutputWrapper.map2ObjQuickbarInfo(roleSetting.getQuickbarInfoMap());
	}
	/**
	 * 获取玩家挂机设置信息
	 * @param userRoleId
	 * @return
	 */
	public Object getGuajiInfo(Long userRoleId){
		RoleSetting roleSetting = getRoleSetting(userRoleId);
		if(roleSetting == null){
			return null;
		}
		
		return roleSetting.getGuajiInfoMap();
	}
	
	/**
	 * 设置快捷键
	 * @param userRoleId
	 * @param data
	 */
	public void changeQuickBar(Long userRoleId, String index, Integer type, String data){
		if(index == null || type == null){
			return;
		}
		
		RoleSetting roleSetting = getRoleSetting(userRoleId);
		if(roleSetting != null){
			Map<String, Map<String, Object>> qbInfoMap = roleSetting.getQuickbarInfoMap();
			String qbInfo = null;
			//如果类型为0，则服务端可以删除这个索引
			if(type.intValue() <= 0){
				qbInfoMap.remove(index);
				qbInfo = RoleSettingOutputWrapper.map2StrQuickbarInfo(qbInfoMap);
			//反之则修改
			}else{
				qbInfo = RoleSettingOutputWrapper.setQuickbarInfo(roleSetting.getQuickbarInfoMap(), index, type, data);
			}
			
			roleSetting.setQuickbarInfo(qbInfo);
			roleSettingDao.cacheUpdate(roleSetting, userRoleId);
		}else{
			//添加快捷键
			roleSetting = new RoleSetting();
			roleSetting.setUserRoleId(userRoleId);
			roleSetting.setQuickbarInfo(RoleSettingOutputWrapper.setQuickbarInfo(roleSetting.getQuickbarInfoMap(), index.toString(), type, data));
			
			roleSettingDao.cacheInsert(roleSetting, userRoleId);
		}
	}
	/**
	 * 设置挂机设置
	 * @param userRoleId
	 * @param data
	 */
	public void changeGuaji(Long userRoleId, Integer type, Object data){
		if(type == null){
			return;
		}
		
		RoleSetting roleSetting = getRoleSetting(userRoleId);
		if(roleSetting != null){
			Map<String, Object> gjInfoMap = roleSetting.getGuajiInfoMap();
			gjInfoMap.put(type.toString(), data);
			
			String gjInfo = JSON.toJSONString(gjInfoMap);
			roleSetting.setGuajiInfo(gjInfo);
			
			roleSettingDao.cacheUpdate(roleSetting, userRoleId);
		}else{
			//添加快捷键
			roleSetting = new RoleSetting();
			Map<String, Object> gjInfoMap = roleSetting.getGuajiInfoMap();
			gjInfoMap.put(type.toString(), data);
			
			String gjInfo = JSON.toJSONString(gjInfoMap);
			roleSetting.setUserRoleId(userRoleId);
			roleSetting.setGuajiInfo(gjInfo);
			
			roleSettingDao.cacheInsert(roleSetting, userRoleId);
		}
	}
//	/**		Liuyu:暂时无用
//	 * 设置系统设置
//	 * @param userRoleId
//	 * @param data
//	 */
//	public void changeSystem(Long userRoleId, Integer type, Object data){
//		if(type == null){
//			return;
//		}
//		
//		RoleSetting roleSetting = getRoleSetting(userRoleId);
//		if(roleSetting != null){
//			Map<String, Object> sysInfoMap = roleSetting.getSystemInfoMap();
//			sysInfoMap.put(type.toString(), data);
//			
//			String sysInfo = JSON.toJSONString(sysInfoMap);
//			roleSetting.setSystemInfo(sysInfo);
//			
//			roleSettingDao.cacheUpdate(roleSetting, userRoleId);
//		}else{
//			//添加快捷键
//			roleSetting = new RoleSetting();
//			Map<String, Object> sysInfoMap = roleSetting.getSystemInfoMap();
//			sysInfoMap.put(type.toString(), data);
//			
//			String sysInfo = JSON.toJSONString(sysInfoMap);
//			roleSetting.setSystemInfo(sysInfo);
//			
//			roleSettingDao.cacheInsert(roleSetting, userRoleId);
//		}
//	}
	
	/**
	 * 是否设置拒绝
	 * @param userRoleId
	 * @param type
	 * @return
	 */
	public boolean isSetting(Long userRoleId,String type){
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			RoleSetting roleSetting = getRoleSetting(userRoleId);
			Object ret = roleSetting.getGuajiInfoMap().get(type);
			if(ret != null){
				return (boolean)ret;
			}
		}
		return false;
	}
}
