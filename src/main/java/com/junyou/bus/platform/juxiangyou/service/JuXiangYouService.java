package com.junyou.bus.platform.juxiangyou.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.common.service.PtCommonService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.utils.JuxiangYouUtil;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.http.HttpClientMocker;
import com.junyou.utils.md5.Md5Utils;
import com.kernel.spring.container.DataContainer;

/**
 * 聚享游平台
 * 
 * @author lxn
 * 
 */
@Service
public class JuXiangYouService {

	@Autowired
	private PtCommonService ptCommonService;
	@Autowired
	private DataContainer dataContainer;

	private static final String TOKEN_ID = "tokenID";

	/**
	 * 聚享游平台玩家成功登陆后回调地址 只回调一次，玩家第一次创角登陆回调。以后再登陆就不管了
	 */
	public void onlineHandle(long userRoleId) {
		if (ChuanQiConfigUtil.getPlatfromId().equals(PlatformPublicConfigConstants.PPS_PLAT_NAME)) {
			Long _userRoleId = dataContainer.getData(GameConstants.JUXIANGYOU_ROLE_FIRST_LOGIN, String.valueOf(userRoleId));
			if (_userRoleId == null) {
				return;// 没有值，不是第一次登陆
			}
			//清除标记
			dataContainer.removeData(GameConstants.JUXIANGYOU_ROLE_FIRST_LOGIN, String.valueOf(userRoleId));
			Map<String, String> info = ptCommonService.getRoleMap(userRoleId, PlatformPublicConfigConstants.PLATFORM_ARGS_MODEL);
			//只有从聚享游平台过来的用户后台传过来的参数里面才有tokenID值
			if (info != null && info.get(TOKEN_ID) != null) {
				String url = JuxiangYouUtil.getCallbackUrl();
				String tokenID = info.get("tokenID");
				String recordID = info.get("recordID");
				String tokenKey = info.get("tokenKey");
				String accessCode = String.valueOf(userRoleId);
				String accessKey = Md5Utils.md5To32(tokenID + recordID + accessCode + tokenKey);

				String[][] paramsBuf = new String[4][];

				paramsBuf[0] = new String[] { "tokenID", tokenID };
				paramsBuf[1] = new String[] { "recordID", recordID };
				paramsBuf[2] = new String[] { "accessCode", accessCode };
				paramsBuf[3] = new String[] { "accessKey", accessKey };
				
				String result = null;
				try {
					result = HttpClientMocker.requestMockGet(url, paramsBuf);
					ChuanQiLog.info("聚享游平台用户登陆游戏，tokenId={},回调返回值：{}", tokenID, result);
					if (result!=null && !result.contains("errcode")) {
						ChuanQiLog.info("回调'通知聚享游平台会员成功登陆游戏'的地址---成功！！！");
					} else {
						ChuanQiLog.info("回调'通知聚享游平台会员成功登陆游戏'的地址---失败.");
					}
				} catch (Exception e) {
					ChuanQiLog.info("回调聚享游平台地址报错！！！");
				}
			}
		}
	}
/**
 * 
	public static void main(String[] args) {
		String url = JuxiangYouUtil.getCallbackUrl();
		String tokenID = "10";
		String recordID = "4";
		String tokenKey = "b0cj391b90p421n8";
		String accessCode = String.valueOf(55554);
		String accessKey = Md5Utils.md5To32(tokenID + recordID + accessCode + tokenKey);

		String[][] paramsBuf = new String[4][];

		paramsBuf[0] = new String[] { "tokenID", tokenID };
		paramsBuf[1] = new String[] { "recordID", recordID };
		paramsBuf[2] = new String[] { "accessCode", accessCode };
		paramsBuf[3] = new String[] { "accessKey", accessKey };

		String result = HttpClientMocker.requestMockGet(url, paramsBuf);
		ChuanQiLog.info("聚享游平台用户登陆游戏，tokenId={},回调返回值：{}", tokenID, result);

		if (result.contains("errcode")) {
			ChuanQiLog.info("回调'通知聚享游平台会员成功登陆游戏'的地址失败！！！");
		} else {
			ChuanQiLog.info("回调'通知聚享游平台会员成功登陆游戏'的地址成功！！！");
		}
	}
 */
}
