package com.junyou.bus.platform.xunlei.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.common.entity.RolePlatformRecharge;
import com.junyou.bus.platform.common.service.PtCommonService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.configure.export.PtCommonPublicConfig;
import com.junyou.bus.platform.configure.export.SuperVipConfig;
import com.junyou.err.AppErrorCode;

@Service
public class XunleiService { 
	@Autowired
	private PtCommonService ptCommonService;

	/**
	 * 迅雷礼包领取状况
	 * @param userRoleId
	 * @return
	 */
	public Object[] getStateByRoleId(Long userRoleId) {
		try {
			Map<String, String> info = getRoleMap(userRoleId);
			Integer currentLevel = 0;
			if(info!=null && info.get(PlatformPublicConfigConstants.XUNLEI_GOLD_LEVEL)!=null){
				currentLevel = Integer.parseInt(info.get(PlatformPublicConfigConstants.XUNLEI_GOLD_LEVEL));
			}
			int state = ptCommonService.getStateByRoleId(userRoleId);
			return  new Object[]{currentLevel,state};
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * VIP礼包领取
	 * @param userRoleId
	 * @param type
	 * @param level
	 * @return
	 */
	public Object[] getVipReward(Long userRoleId,int level) {
		
		Map<String, String> info = getRoleMap(userRoleId);
		Integer currentLevel = 0;
		if(info!=null && info.get(PlatformPublicConfigConstants.XUNLEI_GOLD_LEVEL)!=null){
			currentLevel = Integer.parseInt(info.get(PlatformPublicConfigConstants.XUNLEI_GOLD_LEVEL));
		}
		if(info==null){
			return AppErrorCode.PLATFORM_DATA_ERROR;
		}
		if(level>currentLevel){
			return AppErrorCode.PLATFORM_VIP_NOT_ENOUGH;
		}
		PtCommonPublicConfig  commonPublicConfig  = this.getPublicConfig(2);
		if(commonPublicConfig==null){
			return AppErrorCode.PLATFORM_DATA_ERROR;
		}
		Map<String, Integer> itemMap  = commonPublicConfig.getItems().get("level"+level);
		Object[] result = ptCommonService.updateBagAndState(userRoleId, itemMap, level+1,PlatformPublicConfigConstants.XUNLEI_VIP_LB,true);
		if((Integer)result[0]==1){ //成功
			result[1] = (Integer)result[1]>>2; //给指定的状态 右移两位
		}
		return result;
	}
	//获取wen传来的参数
	private  Map<String, String> getRoleMap(Long userRoleId){
		
		return ptCommonService.getRoleMap(userRoleId,PlatformPublicConfigConstants.PLATFORM_ARGS_MODEL );
	}
	/**领取迅雷礼包
	 * @param userRoleId
	 * @param type
	 * type=0大厅，1特权
	 */
	public Object[] getReward(Long userRoleId, int type) {
 		PtCommonPublicConfig  commonPublicConfig =  getPublicConfig(type);
		if(commonPublicConfig == null){
			return AppErrorCode.PLATFORM_DATA_ERROR;
		} 
		if(type==0){ //游戏大厅
			Map<String, String> info = getRoleMap(userRoleId);
			if(info==null){
				return AppErrorCode.PLATFORM_DATA_ERROR;
			}
			Integer xlbox = 0;
			if(info.get(PlatformPublicConfigConstants.XUNLEI_XLBOX)!=null){
				xlbox = Integer.parseInt(info.get(PlatformPublicConfigConstants.XUNLEI_XLBOX));
			}
			if(type==0 && xlbox!=2){
				return AppErrorCode.PLATFORM_NOT_DATING;
			}
		}else{
			//判断充值是否达到要求
			RolePlatformRecharge rRecharge = ptCommonService.getPlatformRecharge(userRoleId);
			
			SuperVipConfig vipBaseInfo= ptCommonService.getSuperVipConfig();
			
			if(rRecharge==null || vipBaseInfo==null){return null;}
			
			if(rRecharge.getRechargeMonth()<vipBaseInfo.getAllRecharge() 
					&& rRecharge.getRechargeOnceMax()<vipBaseInfo.getOnceRecharge()){
				return AppErrorCode.PLATFORM_RECHARGE_NOT_ENOUGH; //没达到充值条件不能领取
			}
		}
		Map<String, Integer> itemMap  = commonPublicConfig.getItems().get("item");
		String giftType = type==0?PlatformPublicConfigConstants.XUNLEI_BOX_LB:PlatformPublicConfigConstants.XUNLEI_TEQUAN_LB;
		return  ptCommonService.updateBagAndState(userRoleId, itemMap, type,giftType,false);
	}
	/**
	 * type=0盒子，1特权，2vip礼包
	 * @param type
	 * @return
	 */
	private  PtCommonPublicConfig  getPublicConfig(int type) {
		String flag ="";
		switch (type) {
		case 0:
			flag = PlatformPublicConfigConstants.XUNLEI_BOX_LB;
			break;
		case 1:
			flag = PlatformPublicConfigConstants.XUNLEI_TEQUAN_LB;
			break;
		case 2:
			flag = PlatformPublicConfigConstants.XUNLEI_VIP_LB;
			break;
		}
		return  ptCommonService.getConfigByModName(flag);
		
	}
	
//*********************以下是迅雷特权 类似超级会员*****************************
	
	public Object[] getSuperVipInfo(Long userRoleId) {
		return ptCommonService.getSuperVipInfo(userRoleId);
	}

	public int isCloseActivity() {
		return ptCommonService.isCloseActivity();
	}
}
