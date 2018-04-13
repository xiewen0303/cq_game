package com.junyou.bus.vip.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.vip.service.RoleVipInfoService;
import com.junyou.bus.vip.util.RoleVipWrapper;

@Service
public class RoleVipInfoExportService {
	@Autowired
	private RoleVipInfoService roleVipInfoService;
	/**
	 * 上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		roleVipInfoService.onlineHandle(userRoleId);
	}
	
	public void createRoleVipInfo(Long userRoleId){
		roleVipInfoService.createRoleVipInfoDB(userRoleId);
	}
	
	public RoleVipWrapper getRoleVipInfo(Long userRoleId){
		return roleVipInfoService.getRoleVipInfo(userRoleId);
	}
	
	public RoleVipWrapper getRoleVipInfoFromDB(Long userRoleId){
		return roleVipInfoService.getRoleVipInfoFromDB(userRoleId);
	}
	/**
	 * 设置VIP等级
	 * @param userRoleId
	 * @param vipLevel
	 */
	public void setVipLevel(Long userRoleId,int vipLevel){
		roleVipInfoService.setVipLevel(userRoleId, vipLevel);
	}
	/**
	 * 获取特权信息
	 * @param userRoleId
	 * @param tequanType
	 * @return
	 */
	public int getVipTequan(Long userRoleId,String tequanType){
		return roleVipInfoService.getTequanInfo(userRoleId, tequanType);
	}
	/**
	 * 获取特权信息(直接访问数据库)
	 * @param userRoleId
	 * @param tequanType
	 * @return
	 */
	public int getVipTequanFromDb(Long userRoleId,String tequanType){
		return roleVipInfoService.getTequanInfoFromDb(userRoleId, tequanType);
	}
	/**
	 * 增加VIP经验
	 * @param userRoleId
	 * @param vipExp
	 */
	public void rechargeVipExp(Long userRoleId,long vipExp){
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		roleVipInfoService.rechargeVipExp(userRoleId, vipExp, busMsgQueue);
		
		busMsgQueue.flush();
	}
}
