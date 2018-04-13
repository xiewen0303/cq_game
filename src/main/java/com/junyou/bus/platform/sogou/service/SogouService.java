package com.junyou.bus.platform.sogou.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.common.service.PtCommonService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.configure.export.PtCommonPublicConfig;
import com.junyou.err.AppErrorCode;

@Service
public class SogouService {


	@Autowired
	private PtCommonService ptCommonService;
	
	/**
	 * 礼包领取情况
	 * @param roleId
	 * @return
	 */
	public int getStateByRoleId(Long roleId){
		 
		 return ptCommonService.getStateByRoleId(roleId);
	}
	/**
	 * @param roleId
	 * @param type 0大厅，1皮肤
	 * @return
	 */
	public Object[] getReward(Long roleId,String type){
		if(!type.equals(this.getLoginTyep(roleId))){
			//异常请求
			return null;
		}
		int position = 0;
		String modName =PlatformPublicConfigConstants.SOGOU_DATING_LB;
		if(type.equals(PlatformPublicConfigConstants.SOGOU_WANCLIENT_SKIN)){
			position = 1 ;
			modName =PlatformPublicConfigConstants.SOGOU_PIFU_LB;
		} 
		PtCommonPublicConfig commonConfig = ptCommonService.getConfigByModName(modName);
		String giftType = PlatformPublicConfigConstants.SOGOU_DATING_LB;
		if(commonConfig==null){
			return AppErrorCode.PLATFORM_DATA_ERROR; 
		}
		if(type.equals(PlatformPublicConfigConstants.SOGOU_WANCLIENT_SKIN)){
			giftType = PlatformPublicConfigConstants.SOGOU_PIFU_LB;
		}
		 return ptCommonService.updateBagAndState(roleId, commonConfig.getItems().get("item"), position,giftType, false);
	}
	//获取搜狗用户是大厅还是皮肤登陆
	private String getLoginTyep(Long userRoleId){
		 Map<String, String> info =ptCommonService.getRoleMap(userRoleId, PlatformPublicConfigConstants.PLATFORM_ARGS_MODEL);
		 if(info==null){
			 return null; //网页登陆
		 }
		return info.get(PlatformPublicConfigConstants.SOGOU_WANCLIENT);
	}
	/**
	 * 判断活动是否关闭
	 */
	public int isCloseActivity(){
		
		return ptCommonService.isCloseActivity();
	}
	/**
	 * 打开超级会员面板请求的信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getSuperVipInfo(Long userRoleId) {
		 
		return ptCommonService.getSuperVipInfo(userRoleId);
	}
	
}
