package com.junyou.bus.platform.qq.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.qq.constants.QQGeZhongZuanMap;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.utils.OpensnsException;
import com.junyou.bus.platform.qq.utils.QqUtil;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.spring.container.DataContainer;

@Service
public class QqService {
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private SessionManagerExportService sessionManagerExportService;
	
	/**
	 * key=0 value = ${int:黄钻等级} key=1 value = ${Boolean:是否是年费会员}
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Map<Integer, Object> getRoleQQInfo(Long userRoleId,boolean notifyClient) {
		
		Map<Integer, Object> ret = QQGeZhongZuanMap.getZuanMapByUser(userRoleId);
		if(ret == null || ret.size() <= 0){
			if(!sessionManagerExportService.isOnline(userRoleId)){
				return null;
			}
			UserRole role = roleExportService.getUserRole(userRoleId);
			if (role == null) {
				return null;
			}
			
//			//----------------------------------------------------------------------
//			ret = new HashMap<Integer, Object>();
//			int max=9;
//	        int min=0;
//	        Random random = new Random();
//
//			ret.put(QqConstants.QQ_PLATFORM_INFO_HUANG_ZUAN_LEVEL_KEY,random.nextInt(max)%(max-min+1));
//			ret.put(QqConstants.QQ_PLATFORM_INFO_HUANG_ZUAN_NIANFEI_KEY,true);
//			ret.put(QqConstants.QQ_PLATFORM_INFO_LAN_ZUAN_LEVEL_KEY,random.nextInt(max)%(max-min+1));
//			ret.put(QqConstants.QQ_PLATFORM_INFO_LAN_ZUAN_NIANFEI_KEY,random.nextInt(max)%(max-min+1) > 4);
//			ret.put(QqConstants.QQ_PLATFORM_INFO_3366_BAOZI,random.nextInt(100)%(100-min+1));
//			ret.put(QqConstants.QQ_PLATFORM_INFO_HAOHUA_LAN_ZUAN,random.nextInt(max)%(max-min+1) > 4);
//			ret.put(QqConstants.QQ_PLATFORM_INFO_HAOHUA_HUANG_ZUAN,random.nextInt(max)%(max-min+1) > 4);
//			if (notifyClient) {
//				BusMsgSender.send2One(userRoleId,ClientCmdType.GET_QQ_PLATFORM_INFO, ret);
//				String stageId = publicRoleStateExportService.getRolePublicStageId(userRoleId);
//				if (stageId != null) {
//					IStage stage = StageManager.getStage(stageId);
//					if (stage != null) {
//						IRole irole = stage.getElement(userRoleId, ElementType.ROLE);
//						if (irole != null) {
//							irole.getBusinessData().setPlatformInfo(ret);
//						}
//					}
//				}
//			}
//			if(role.getLevel() > 30){
//				QQGeZhongZuanMap.setUserZuanMap(userRoleId, ret);
//			}
//			if(role.getLevel()>-1){
//				return ret;	
//			}
			
//			//----------------------------------------------------------------------
			Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());
			if(keyMap ==null){
				return null;
			}
			HashMap<String, String> hParams = new HashMap<String, String>();
			hParams.put("openid", role.getUserId());
			hParams.put("openkey", keyMap.get("openkey"));
			hParams.put("appid", QqConstants.APP_ID);
			hParams.put("pf", keyMap.get("pf"));
			hParams.put("format", QqConstants.FORMAT);
			String protocol = "http";
			String appid = QqConstants.APP_ID;
			String appkey = QqConstants.APP_KEY;
			String serverName = ChuanQiConfigUtil.getTencentUrl();
			long beginTime = GameSystemTime.getSystemMillTime();
			String scriptName = "/v3/user/get_info";
			String resp = null;
			try {
				resp = QqUtil.api(scriptName, hParams, protocol, appid, appkey,serverName);
			} catch (Exception e) {
				ChuanQiLog.error("", e);
				return null;
			}
			ChuanQiLog.info("get qq info time={}ms",(GameSystemTime.getSystemMillTime() - beginTime));
			int huangZuanLevel = 0;
			int nianFei = 0;
			int lanZuanLevel = 0;
			int nianFeiLan = 0;
			int baozi = 0;
			int haohuaLanzuan = 0;
			int haohuaHuangzuan = 0;
			try {
				huangZuanLevel = QqUtil.huangZuanLevel(resp);
				nianFei = QqUtil.nianHuangZuan(resp);
				lanZuanLevel = QqUtil.lanZuanLevel(resp);
				nianFeiLan = QqUtil.nianLanZuan(resp);
				baozi = QqUtil._3366BaoZiLevel(resp);
				haohuaLanzuan = QqUtil.haoHuaLanZuan(resp);
				haohuaHuangzuan = QqUtil.haoHuaHuangZuan(resp);
			} catch (OpensnsException e) {
				e.printStackTrace();
			}
			ret = new HashMap<Integer, Object>();
			ret.put(QqConstants.QQ_PLATFORM_INFO_HUANG_ZUAN_LEVEL_KEY,huangZuanLevel);
			ret.put(QqConstants.QQ_PLATFORM_INFO_HUANG_ZUAN_NIANFEI_KEY,nianFei == 1);
			ret.put(QqConstants.QQ_PLATFORM_INFO_LAN_ZUAN_LEVEL_KEY,lanZuanLevel);
			ret.put(QqConstants.QQ_PLATFORM_INFO_LAN_ZUAN_NIANFEI_KEY,nianFeiLan == 1);
			ret.put(QqConstants.QQ_PLATFORM_INFO_3366_BAOZI,baozi);
			ret.put(QqConstants.QQ_PLATFORM_INFO_HAOHUA_LAN_ZUAN,haohuaLanzuan == 1);
			ret.put(QqConstants.QQ_PLATFORM_INFO_HAOHUA_HUANG_ZUAN,haohuaHuangzuan == 1);
			
			if (notifyClient) {
				BusMsgSender.send2One(userRoleId,ClientCmdType.GET_QQ_PLATFORM_INFO, ret);
				String stageId = publicRoleStateExportService.getRolePublicStageId(userRoleId);
				if (stageId != null) {
					IStage stage = StageManager.getStage(stageId);
					if (stage != null) {
						IRole irole = stage.getElement(userRoleId, ElementType.ROLE);
						if (irole != null) {
							irole.getBusinessData().setPlatformInfo(ret);
						}
					}
				}
			}else{
				//蓝钻等级大于0,,推送客服端蓝钻过期时间
				long lanZuanTime = 0;
				if(lanZuanLevel > 0){
					String blueInfo = null;
					try {
						blueInfo = getBuleVipInfo(userRoleId);
					} catch (Exception e) {
						ChuanQiLog.error("", e);
					}
					try {
						if(blueInfo != null){
							lanZuanTime = QqUtil.getLanZuanGuoQiTime(blueInfo);
						}
					} catch (OpensnsException e) {
						e.printStackTrace();
					}
					QQGeZhongZuanMap.setDaoQiTime(userRoleId, lanZuanTime);
					//推送
					BusMsgSender.send2One(userRoleId,ClientCmdType.TUISONG_LANZUAN_GOUQI_SHIJIAN, lanZuanTime);
				}
			}
			QQGeZhongZuanMap.setUserZuanMap(userRoleId, ret);
			
			return ret;
		}else{
			if (!notifyClient) {
				Long lanZuanTime = QQGeZhongZuanMap.getDaoQiTime(userRoleId);
				if(lanZuanTime == null){
					lanZuanTime = 0l;
					String blueInfo = null;
					try {
						blueInfo = getBuleVipInfo(userRoleId);
					} catch (Exception e) {
						ChuanQiLog.error("", e);
					}
					try {
						lanZuanTime = QqUtil.getLanZuanGuoQiTime(blueInfo);
					} catch (OpensnsException e) {
						e.printStackTrace();
					}
					QQGeZhongZuanMap.setDaoQiTime(userRoleId, lanZuanTime);
				}
				//推送
				BusMsgSender.send2One(userRoleId,ClientCmdType.TUISONG_LANZUAN_GOUQI_SHIJIAN, lanZuanTime);
			}
			return ret;
		}
	}
	public Map<Integer, Object> getRoleQQPlatformInfoNeicun(Long userRoleId) {
		
		Map<Integer, Object> ret = QQGeZhongZuanMap.getZuanMapByUser(userRoleId);
		if(ret == null || ret.size() <= 0){
			return null;
		}else{
			return ret;
		}
	}
	/**
	 * 角色下线删除黄蓝钻信息
	 * @param userRoleId
	 */
	public void removeBuleVipInfo(Long userRoleId){
		QQGeZhongZuanMap.reZuanMapByUser(userRoleId);
	}
	/**
	 * 获取蓝钻信息
	 * @param userRoleId
	 * @return
	 */
	public String getBuleVipInfo(Long userRoleId) {
		
		if(!sessionManagerExportService.isOnline(userRoleId)){
			return null;
		}
		UserRole role = roleExportService.getUserRole(userRoleId);
		if (role == null) {
			return null;
		}
		Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());
		if(keyMap ==null){
			return null;
		}
		HashMap<String, String> hParams = new HashMap<String, String>();
		hParams.put("openid", role.getUserId());
		hParams.put("openkey", keyMap.get("openkey"));
		hParams.put("appid", QqConstants.APP_ID);
		hParams.put("pf", keyMap.get("pf"));
		hParams.put("format", QqConstants.FORMAT);
		String protocol = "http";
		String appid = QqConstants.APP_ID;
		String appkey = QqConstants.APP_KEY;
		String serverName = ChuanQiConfigUtil.getTencentUrl();
		long beginTime = GameSystemTime.getSystemMillTime();
		String scriptName = "/v3/user/blue_vip_info";
		String resp = null;
		try {
			resp = QqUtil.api(scriptName, hParams, protocol, appid, appkey,serverName);
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		}
		ChuanQiLog.info("get qq info time={}ms",(GameSystemTime.getSystemMillTime() - beginTime));
		return resp;
	}
}
