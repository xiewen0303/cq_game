package com.junyou.bus.platform.export;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.platform._360.service._360Service;
import com.junyou.bus.platform._360.service._360VplanService;
import com.junyou.bus.platform._37.service._37VplanService;
import com.junyou.bus.platform.common.service.PtCommonService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.log.ChuanQiLog;
import com.kernel.spring.container.DataContainer;

/**
 * 平台相关对外业务
 * @author lxn
 *
 */
@Component
public class PlatformExportService {

	@Autowired
	private DataContainer dataContainer;
	@Autowired 
	private PtCommonService ptCommonSuperVipService;
	
	@Autowired
	private _360Service _360Service;
	@Autowired
	private _360VplanService _360VplanService;
	@Autowired
	private _37VplanService _37VplanService;
	
	/**
	 * 玩家下线后平台清空放入dataContainer容器里面的值
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId){
		//平台缓存数据
		dataContainer.removeData(PlatformPublicConfigConstants.PLATFORM_ARGS_MODEL, userRoleId.toString());
		//360V计划
		dataContainer.removeData(PlatformPublicConfigConstants._360_VPLAN_INFO_MODEL, userRoleId.toString());	
		
	}
	/**
	 * 平台需求跟充值业务相关的。 
	 * 比如XX超级会员累充或者单充达到一定数量显示qq号享受美女专人服务！
	 * addYb增加的元宝
	 */
	public void roleRecharge(Long userRoleId, Long addYb){
		ptCommonSuperVipService.sendRechargeToClient(userRoleId, addYb);
	}
	/**
	 * 360V计划消费礼包记录
	 */
	public void _360VplanRoleRecharge(Long userRoleId, Long addYb){
		_360VplanService.sendRechargeToClient(userRoleId, addYb);
	}
	/**
	 * 360特权压测
	 */
	public Object[] web360TequanTest(Map<String, Object> params){
		return _360Service.web360TequanTest(params);
	}
	/**
	 * 360V计划会员信息初始化
	 */
	public void onlineHandle(Long userRoleId){
		try {
			_360VplanService.init360Vinfo(userRoleId);
		} catch (Exception e) {
			ChuanQiLog.error("请求360V计划用户请求接口信息报错，登陆玩家roleId={}，error={}",userRoleId,e);
		}
		
	}
	/**
	 * 37V计划消费礼包记录
	 */
	public void _37VplanRoleRecharge(Long userRoleId, Long addYb){
		_37VplanService.sendRechargeToClient(userRoleId, addYb);
	}
	
	
	
}
