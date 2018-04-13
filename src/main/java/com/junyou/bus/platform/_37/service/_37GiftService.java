package com.junyou.bus.platform._37.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.common.entity.RolePlatformInfo;
import com.junyou.bus.platform.common.service.PtCommonService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.configure.export.PtCommonPublicConfig;
import com.junyou.err.AppErrorCode;
import com.junyou.utils.math.BitOperationUtil;

@Service
public class _37GiftService {

	@Autowired
	private PtCommonService ptCommonService;
	/**
	 * 获取玩家在37平台领取礼包情况
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] getStateByRoleId(Long userRoleId) {
		Map<String, String> info = getRoleMap(userRoleId);
		int currentLevel  = 0 ;
		if(info!=null && info.get(PlatformPublicConfigConstants.PLATFORM_37_VIP_PTLV)!=null ){
			currentLevel = Integer.parseInt(info.get(PlatformPublicConfigConstants.PLATFORM_37_VIP_PTLV));
		}
		int state =   ptCommonService.getStateByRoleId(userRoleId);
		return new Object[] { AppErrorCode.SUCCESS, currentLevel,state };
	}
	//获取web传来的参数
	private  Map<String, String> getRoleMap(Long userRoleId){
		return ptCommonService.getRoleMap(userRoleId,PlatformPublicConfigConstants.PLATFORM_ARGS_MODEL);
	}
	/**
	 * 37wan玩家领取了一个奖励
	 * @param userRoleId
	 * @param level
	 * @return
	 */
	public Object[] getReward(Long userRoleId, int level) {
		
		Map<String, String> info = getRoleMap(userRoleId);
		int currentLevel = 0;
		if(info!=null && info.get(PlatformPublicConfigConstants.PLATFORM_37_VIP_PTLV)!=null){
			 currentLevel = Integer.parseInt(info.get(PlatformPublicConfigConstants.PLATFORM_37_VIP_PTLV));
		}
		if (level > currentLevel) {
			// 超过会员等级
			return AppErrorCode._37_PLATFORM_GIFT_GET_NOT;
		}
		// 判断背包是否已满
		PtCommonPublicConfig publicConfig = getVipLevelPublicConfig();
		if(publicConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<String, Integer> items = publicConfig.getItems().get("level" + level);
		if(items==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Object[] ret  = ptCommonService.updateBagAndState(userRoleId, items, level-1,PlatformPublicConfigConstants._37_LEVEL_LB,true);
		if(ret!=null && (Integer)ret[0]==2){
			ret = new Object[]{ret[0], ptCommonService.getStateByRoleId(userRoleId)};
		}
		return  ret;
	}
	/**
	 * 获取vip等级奖励道具
	 * 
	 * @return
	 */
	private PtCommonPublicConfig getVipLevelPublicConfig() {
		return ptCommonService.getConfigByModName(PlatformPublicConfigConstants._37_LEVEL_LB);
	}
	
	//============================================令牌==============================================

	/**获取令牌奖励状态
	 * @param userRoleId
	 * @param level
	 * @return
	 */
	public  boolean getLingpaiRewardState(Long userRoleId) {
		RolePlatformInfo rolePlatformInfo =this.ptCommonService.getRolePlatformInfo(userRoleId);
			if(rolePlatformInfo==null || BitOperationUtil.calState(rolePlatformInfo.getGiftStateStandby(), 0)){
				return  false;
			}
		return true;
	}
	/**领取令牌奖励
	 * @param userRoleId
	 * @param level
	 * @return
	 */
	public Object[] getLingpaiReward(Long userRoleId) {
		
		Map<String, String> info = getRoleMap(userRoleId);
		int lingpai = 0;
		if(info!=null && info.get(PlatformPublicConfigConstants.PLATFORM_37_LINGPAI)!=null){
			lingpai = Integer.parseInt(info.get(PlatformPublicConfigConstants.PLATFORM_37_LINGPAI));
		}
		if (lingpai==0) {
			// 未绑定 不能领取
			return AppErrorCode.PLATFORM_360_ERROR_5;
		}
		PtCommonPublicConfig publicConfig = ptCommonService.getConfigByModName(PlatformPublicConfigConstants._37_LINGPAI_LB);
		if(publicConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<String, Integer> items = publicConfig.getItems().get("item");
		return ptCommonService.updateBagAndState(userRoleId, items, 0,PlatformPublicConfigConstants._37_LINGPAI_LB,false);
	}

}
