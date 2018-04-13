package com.junyou.bus.platform.qq.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.configure.export.PtCommonPublicConfig;
import com.junyou.bus.platform.qq.constants.QQGuanjiaConstants;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.dao.RoleQqGuanjiaDao;
import com.junyou.bus.platform.qq.entity.RoleQqGuanjia;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.err.AppErrorCode;
import com.junyou.event.QqGuanjiaRewardLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.http.HttpClientMocker;
import com.junyou.utils.math.BitOperationUtil;
import com.kernel.spring.container.DataContainer;

@Service
public class QqGuanjiaService {

	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private PlatformGongGongShuJuBiaoConfigExportService platformGongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleQqGuanjiaDao roleQqGuanjiaDao;
	@Autowired
	private RoleBagExportService roleBagExportService;

	/**
	 * 获取信息
	 * 
	 * @param userRoleId
	 * @param type
	 * @return
	 */
	public Object[] getInfo(long userRoleId) {
		if (!PlatformConstants.isQQ()) {
			return AppErrorCode.QQ_PLATFORM_ERROR;
		}
		RoleQqGuanjia roleQqGuanjia = getRoleQqGuanjia(userRoleId);

		return new Object[] { 1, roleQqGuanjia.getState() };
	}

	/**
	 * 
	 * @param userRoleId
	 * @param type=0新手礼包|type=1每日礼包
	 * @return
	 */
	public Object[] getRewards(long userRoleId, int type) {
		if (!PlatformConstants.isQQ()) {
			return AppErrorCode.QQ_PLATFORM_ERROR;
		}
		int state = 0;
		String itemName = QQGuanjiaConstants.FIRST_ITEM;

		if (type == QQGuanjiaConstants.FLAG_1) {
			state = QQGuanjiaConstants.FLAG_1;
			itemName = QQGuanjiaConstants.DAY_ITEM;
		}

		Map<String, Integer> itemMap = getPublicItemConfig(itemName);
		if (itemMap == null) {
			return AppErrorCode.CONFIG_ERROR;
		}

		RoleQqGuanjia roleQqGuanjia = getRoleQqGuanjia(userRoleId);
		if (!BitOperationUtil.calState(roleQqGuanjia.getState(), state)) {
			return AppErrorCode.GET_ALREADY;
		}
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper == null) {
			return AppErrorCode.ROLE_IS_NOT_ONLINE;
		}

		Object[] ret = checkOnline(roleWrapper.getUserId());
		if(ret!=null){
			return ret;
		}
		// 判断背包是否已满
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(itemMap, userRoleId);
		// 背包空间不足
		if (code != null) {
			return code;
		}

		Integer newState = BitOperationUtil.chanageState(roleQqGuanjia.getState(), state);
		roleQqGuanjia.setState(newState);
		roleQqGuanjia.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleQqGuanjiaDao.cacheUpdate(roleQqGuanjia, userRoleId);

		// ****进背包****
		if (type == QQGuanjiaConstants.FLAG_1) {
			roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.TENCENT_GUANJIA_DAY, LogPrintHandle.GET_TENCENT_GUANJIA_DAY,
					LogPrintHandle.GBZ_TENCENT_GUANJIA_DAY, true);
		} else {
			roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.TENCENT_GUANJIA_FIRST, LogPrintHandle.GET_TENCENT_GUANJIA_FIRST,
					LogPrintHandle.GBZ_TENCENT_GUANJIA_FIRST, true);
		}
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(itemMap, null);
		GamePublishEvent.publishEvent(new QqGuanjiaRewardLogEvent(userRoleId, jsonArray, type));

		return AppErrorCode.OK;
	}
	/**
	 * 检查管家是否在线
	 * @param userId
	 * @return
	 */
	private Object[] checkOnline(String userId) {
		Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, userId);
		StringBuffer params = new StringBuffer();
		params.append("openid=").append(userId);
		params.append("&openkey=").append(keyMap.get("openkey"));
//		params.append("openid=").append("5EAC9DE5E53878EDF5B4CD3F75A4C4A0");
//		params.append("&openkey=").append("170F21F26C37D435D7A97AE2790A5589");
		params.append("&appid=").append(QqConstants.APP_ID); 
		                                                     
		ChuanQiLog.info("请求qq管家登录参数url={},params={}" , ChuanQiConfigUtil.getTencentGuanjiaUrl() ,params.toString());
		// 发起后台请求
		String result = HttpClientMocker.requestMockPost(ChuanQiConfigUtil.getTencentGuanjiaUrl(), params.toString());
		ChuanQiLog.info("请求qq管家登录信息={}", result);
		//jsonpCallback({"code":6,"msg":"输入参数有误"})
		result  = result.replace("jsonpCallback(", "").replace(")", "");
		JSONObject json = null;
		try {
			json = JSON.parseObject(result);
			ChuanQiLog.info("管家返回json={}", json);
		} catch (Exception e) {
			ChuanQiLog.error("管家返回数据转成json的时候出错");
		}
		if (json == null) {
			ChuanQiLog.error("腾讯管家没有返回数据");
			return AppErrorCode.TENCENT_GUANJIA_NO_DATA;
		}
		int qqStatus  = 1; //默认不在线
		int qqCode = (Integer)json.get("code");
		if (qqCode == 0) {
			// 请求成功
			qqStatus = (Integer) json.get("status");
		} else {
			if (qqCode == QQGuanjiaConstants.OPEN_ID_ERROR) {
				return AppErrorCode.TENCENT_GUANJIA_OPENID_ERROR;
			}
			return AppErrorCode.TENCENT_GUANJIA_DATA_RETURN_ERROR;
		}

		if (qqStatus != QQGuanjiaConstants.GUANJIA_STATUS_ONLINE) {
			if (qqStatus == QQGuanjiaConstants.GUANJIA_STATUS_OFFLINE) {
				// 管家不在线不能领取
				return AppErrorCode.TENCENT_GUANJIA_OFFLINE;
			}

			return AppErrorCode.TENCENT_GUANJIA_DATA_RETURN_ERROR;
		}
		
		return null;
	}

	/**
	 * 获取实体对象
	 * 
	 * @param userRoleId
	 * @return
	 */
	private RoleQqGuanjia getRoleQqGuanjia(long userRoleId) {
		RoleQqGuanjia roleQqGuanjia = roleQqGuanjiaDao.cacheAsynLoad(userRoleId, userRoleId);
		if (roleQqGuanjia == null) {
			roleQqGuanjia = new RoleQqGuanjia();
			roleQqGuanjia.setUserRoleId(userRoleId);
			roleQqGuanjia.setState(0);
			roleQqGuanjia.setUpdateTime(GameSystemTime.getSystemMillTime());
			roleQqGuanjia.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			roleQqGuanjiaDao.cacheInsert(roleQqGuanjia, userRoleId);
		}

		if (!DatetimeUtil.dayIsToday(roleQqGuanjia.getUpdateTime())) {
			//跨天了充值每日领取状态
			Integer newState = BitOperationUtil.chanageStateZero(roleQqGuanjia.getState(), 2);
			roleQqGuanjia.setState(newState);
			roleQqGuanjia.setUpdateTime(GameSystemTime.getSystemMillTime());
			roleQqGuanjiaDao.cacheUpdate(roleQqGuanjia, userRoleId);
		}

		return roleQqGuanjia;
	}

	/**
	 * 获取奖励配置
	 */
	private Map<String, Integer> getPublicItemConfig(String name) {
		PtCommonPublicConfig ptCommonPublicConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_GUANJIA);
		if (ptCommonPublicConfig != null) {
			Map<String, Integer> map = ptCommonPublicConfig.getItems().get(name);
			if (map != null) {
				return map;
			}
		}
		return null;
	}

}
