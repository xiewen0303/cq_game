package com.junyou.bus.platform.shunwang.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.platform.common.service.PtCommonService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.configure.export.PtCommonPublicConfig;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.err.AppErrorCode;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.http.HttpClientMocker;
import com.junyou.utils.md5.Md5Utils;
import com.kernel.spring.container.DataContainer;

@Service
public class ShunWangService {

	@Autowired
	private PtCommonService ptCommonService;
	@Autowired
	private DataContainer dataContainer;

	@Autowired
	private RoleExportService roleExportService;

	public Object[] getSuperVipInfo(Long userRoleId) {
		return ptCommonService.getSuperVipInfo(userRoleId);
	}

	public int isCloseActivity() {
		return ptCommonService.isCloseActivity();
	}

	public Object[] getState(Long userRoleId) {
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if (role == null) {
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		int currentLevel = 0;
		Map<String, String> info = dataContainer.getData(PlatformPublicConfigConstants.PLATFORM_ARGS_MODEL, userRoleId.toString());
		if (info == null) {
			JSONObject json = getLevelInfoFromPlatform(role.getUserId());
			if (json.get("status")!=null && (int) json.get("status") == 0) {
				String levelStr = String.valueOf(json.get("memberVip"));
				if(levelStr.contains("VIP")){
					currentLevel = Integer.parseInt(levelStr.substring(3, levelStr.length()));
				}else{
					currentLevel= Integer.parseInt(levelStr);
				}
				
			} else {
				// 获取等级失败
				ChuanQiLog.debug("***获取顺网会员等级错误，userId={},平台接口返回信息{}***", role.getUserId(),json.toString());
				return AppErrorCode.PLATFORM_GET_VIP_LEVEL_ERROR;
			}
			info = new HashMap<>();
			info.put(PlatformPublicConfigConstants.SHUNWANG_LEVEL_CACHE, currentLevel + "");
			dataContainer.putData(PlatformPublicConfigConstants.PLATFORM_ARGS_MODEL,userRoleId.toString(), info);
		}else{
			currentLevel = Integer.parseInt(info.get(PlatformPublicConfigConstants.SHUNWANG_LEVEL_CACHE));
		}
		
		
		int state = ptCommonService.getStateByRoleId(userRoleId);
		return new Object[] { AppErrorCode.SUCCESS, currentLevel, state };
	}

	/**
	 *玩家领取了一个奖励
	 * 
	 * @param userRoleId
	 * @param level
	 * @return
	 */
	public Object[] getReward(Long userRoleId, int level) {
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if (role == null) {
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}

		Map<String, String> info = dataContainer.getData(PlatformPublicConfigConstants.PLATFORM_ARGS_MODEL, userRoleId.toString());
		int currentLevel = 0;
		if (info != null && info.get(PlatformPublicConfigConstants.SHUNWANG_LEVEL_CACHE) != null) {
			currentLevel = Integer.parseInt(info.get(PlatformPublicConfigConstants.SHUNWANG_LEVEL_CACHE));
		}
		if (level > currentLevel) {
			// 超过会员等级
			return AppErrorCode._37_PLATFORM_GIFT_GET_NOT;
		}
		PtCommonPublicConfig publicConfig = getVipLevelPublicConfig();
		if (publicConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<String, Integer> items = publicConfig.getItems().get("level" + level);
		Object[] ret = ptCommonService.updateBagAndState(userRoleId, items, level - 1, PlatformPublicConfigConstants.SHUNWANG_VIP_LB, false);
		if (ret != null && ((Integer) ret[0] == 2||(Integer) ret[0] == 1)) {
			ret = new Object[] { ret[0], level };
		}
		return ret;
	}

	/**
	 * 通过平台接口获取玩家的VIp等级 存到缓存 不及时同步平台等级 上下线同步一次
	 * 34598944309920415484485842938613 
	 * result ="{status:0,memberVip:0}";
	 * @return
	 */
	private static JSONObject getLevelInfoFromPlatform(String userId) {
		String requestUrl = "http://hqg.swjoy.com/front/vip/getvip.do";
		String guid = userId;
		String domain = "hqg.swjoy.com";
		String key = "3219";
		StringBuffer str = new StringBuffer();
		str.append(guid).append(domain).append(key);
		String sign = Md5Utils.md5To32(str.toString());// 加密
		StringBuffer param = new StringBuffer();
		param.append("guid=").append(guid).append("&sign=").append(sign);
		String result = HttpClientMocker.requestMockPost(requestUrl, param.toString());
		ChuanQiLog.debug("获取顺网vip等级返回结果：" + result);
		return JSONObject.parseObject(result);
	}

	/**
	 * 获取顺网会员vip等级 测试
	 */
	public static void main(String[] args) {
		JSONObject json = getLevelInfoFromPlatform("44168599266239845301121203319645");
		 
		 System.out.println(json.get("status"));
	}

	/**
	 * 获取vip等级奖励道具
	 * 
	 * @return
	 */
	private PtCommonPublicConfig getVipLevelPublicConfig() {
		return ptCommonService.getConfigByModName(PlatformPublicConfigConstants.SHUNWANG_VIP_LB);
	}
}
