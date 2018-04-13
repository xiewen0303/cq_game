package com.junyou.bus.platform._37.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform._37.configure.export._37ShoujilbPublicConfig;
import com.junyou.bus.platform._37.constants._37Constants;
import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.UserRoleConstants;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.Activate37PhoneRewardLogEvent;
import com.junyou.event.PickPhoneRewardLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.utils.ChuanQiConfigUtil;
/**
 * 
 * @author dsh
 * @date 2015-6-10
 */
@Service
public class _37Service {
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private PlatformGongGongShuJuBiaoConfigExportService _37GongGongShuJuBiaoConfigExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;

	/**
	 * 激活绑定手机奖励(gate通过http调用)
	 * 
	 * @param userRoleId
	 * @return 1:成功 -1：已绑定手机，奖励未领取 -2:已绑定手机，且奖励已领取 -3:用户不存在
	 */
	public Integer activatePhoneReward(String serverId, String userId) {
		if(!ChuanQiConfigUtil.getPlatfromId().equals(PlatformConstants.PLATFORM_37)){
			return _37Constants.ACTIVATE_PHONE_REWARD_WRONG_PLATFORM;
		}
		UserRole userRole = roleExportService.getRoleFromDb(userId, serverId);
		if (userRole == null) {
			return _37Constants.ACTIVATE_PHONE_REWARD_RESULT_3;
		}
		boolean isOnline = publicRoleStateExportService.isPublicOnline(userRole.getId());
		if(isOnline){
			userRole = roleExportService.getUserRole(userRole.getId());
		}
		GamePublishEvent.publishEvent(new Activate37PhoneRewardLogEvent(userRole.getId()));
		Integer status = userRole.getPhoneRewardStatus();
		if (status == null || status.intValue() == UserRoleConstants.USER_ROLE_PHONE_REWARD_STATUS_0) {
			roleExportService.updatePhoneReward(userRole.getId(),
					UserRoleConstants.USER_ROLE_PHONE_REWARD_STATUS_1);
			if(isOnline){
				BusMsgSender.send2One(userRole.getId(), ClientCmdType.GET_PHONE_REWARD_INFO,
						new Object[] {UserRoleConstants.USER_ROLE_PHONE_REWARD_STATUS_1 });
			}
			return 1;
		} else if (status.intValue() == UserRoleConstants.USER_ROLE_PHONE_REWARD_STATUS_1) {
			return _37Constants.ACTIVATE_PHONE_REWARD_RESULT_1;
		} else if (status.intValue() == UserRoleConstants.USER_ROLE_PHONE_REWARD_STATUS_2) {
			return _37Constants.ACTIVATE_PHONE_REWARD_RESULT_2;
		}
		return 1;
	}

	/**
	 * 获得绑定手机奖励状态
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object getPhoneRewardStatus(Long userRoleId) {
		if(!ChuanQiConfigUtil.getPlatfromId().equals(PlatformConstants.PLATFORM_37)){
			return AppErrorCode._37_PLATFORM_ERROR;
		}
		UserRole userRole = roleExportService.getUserRole(userRoleId);
		Integer status = userRole.getPhoneRewardStatus();
		if (status == null) {
			status = UserRoleConstants.USER_ROLE_PHONE_REWARD_STATUS_0;
		}
		return status;
	}

	/**
	 * 领取绑定手机奖励
	 * 
	 * @param userRoleId
	 */
	public Object[] pickPhoneReward(Long userRoleId) {
		if(!ChuanQiConfigUtil.getPlatfromId().equals(PlatformConstants.PLATFORM_37)){
			return AppErrorCode._37_PLATFORM_ERROR;
		}
		UserRole userRole = roleExportService.getUserRole(userRoleId);
		Integer status = userRole.getPhoneRewardStatus();
		if (status == null
				|| status.intValue() == UserRoleConstants.USER_ROLE_PHONE_REWARD_STATUS_0) {
			return AppErrorCode._37_PHONE_REWARD_CAN_NOT_PICK;
		} else if (status.intValue() == UserRoleConstants.USER_ROLE_PHONE_REWARD_STATUS_2) {
			return AppErrorCode._37_PHONE_REWARD_PICKED;
		}
		roleExportService.updatePhoneReward(userRoleId,
				UserRoleConstants.USER_ROLE_PHONE_REWARD_STATUS_2);
		_37ShoujilbPublicConfig publicConfig = getShoujilbPublicConfig();
		roleBagExportService.putGoodsAndNumberAttr(publicConfig.getItem(), userRoleId, GoodsSource._37_PHONE_REWARD, LogPrintHandle.GET_PHONE_REWARD, LogPrintHandle.GBZ_PHONE_REWARD, true);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(
				publicConfig.getItem(), null);
		GamePublishEvent.publishEvent(new PickPhoneRewardLogEvent(userRoleId,
				jsonArray));
		return AppErrorCode.OK;
	}

	/**
	 * 获取押镖公共配置
	 * 
	 * @return
	 */
	private _37ShoujilbPublicConfig getShoujilbPublicConfig() {
		return _37GongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PlatformPublicConfigConstants._37_SHOUJI_LB);
	}
}
