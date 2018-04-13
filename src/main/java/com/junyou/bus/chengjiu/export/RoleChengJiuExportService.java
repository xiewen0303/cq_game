package com.junyou.bus.chengjiu.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.chengjiu.entity.RoleChengjiu;
import com.junyou.bus.chengjiu.entity.RoleChengjiuData;
import com.junyou.bus.chengjiu.server.RoleChengJiuService;

/**
 * @author zhongdian
 * 2015-6-30 下午2:26:58
 */
@Service
public class RoleChengJiuExportService {

	
	@Autowired
	private RoleChengJiuService roleChengJiuService;
	
	
	public List<RoleChengjiu> initChengjiu(Long userRoleId){
		return roleChengJiuService.initRoleChengJiu(userRoleId);
	}
	public List<RoleChengjiuData> initRoleChengjiuData(Long userRoleId){
		return roleChengJiuService.initRoleChengjiuData(userRoleId);
	}
	
	/**
	 * 检测并激活成就及通知客服端成就激活
	 * @param userRoleId
	 * @param type
	 * @param cjValue
	 */
	public void tuisongChengJiu(Long userRoleId,int type,int cjValue){
		roleChengJiuService.jianCeAndJiHuoCheng(userRoleId, type, cjValue);
	}
	
	public void onlineHandle(Long userRoleId){
		roleChengJiuService.onlineHandle(userRoleId);
	}
	/**
	 * 根据成就ID 获取成就点数
	 * @return
	 */
	public int getChengJiuValue(Long userRoleId){
		return roleChengJiuService.getChengJiuValue(userRoleId);
	}
	/**
	 * 获取成就属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getChengJiuAttrs(Long userRoleId){
		return roleChengJiuService.getChengJiuAttrs(userRoleId);
	}
	/**
	 * 获取成就属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getChengJiuAttrsKf(Long userRoleId,String cjId){
		return roleChengJiuService.getChengJiuAttrsKf(userRoleId,cjId);
	}
	
}
