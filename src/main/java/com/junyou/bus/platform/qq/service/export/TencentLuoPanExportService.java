package com.junyou.bus.platform.qq.service.export;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.entity.RoleAccount;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.online.export.RoleOnlineExportService;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.entity.TencentUserInfo;
import com.junyou.bus.platform.qq.service.TencentLuoPanOssService;
import com.junyou.bus.platform.qq.service.TencentLuoPanService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.io.global.GsCountChecker;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.spring.container.DataContainer;
/**
 * @author zhongdian
 * 2015-7-29 下午2:08:06
 */
@Service
public class TencentLuoPanExportService {

	
	@Autowired
	private TencentLuoPanService tencentLuoPanService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private TencentLuoPanOssService tencentLuoPanOssService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleOnlineExportService roleOnlineExportService;
	/**
	 * 腾讯高潜用户
	 * @param userRoleId
	 */
	public void gaoqianLog(Long userRoleId){
		tencentLuoPanService.gaoqianLog(userRoleId);
	}
	/**
	 * 发送罗盘请求
	 * @param map
	 * @param jiekou
	 */
	public void sendLuoPan(String luoPanUrl,Map<String, String> map,String jiekou){
		tencentLuoPanService.sendLuoPan(luoPanUrl,map, jiekou);
	}
	
	 public int pfChargePfId(String pf){
		 return tencentLuoPanService.pfChargePfId(pf);
	 }
	 /**
	  * 腾讯罗盘通用
	  * @param userRoleId
	  * @param lType  罗盘类型
	  */
	 public void tencentLuoPanTongYong(Long userRoleId,String lType){
		 RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);
		Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		if(keyMap == null){
			return;
		}
		//腾讯罗盘
			try {
				Map<String, String> cMap = new HashMap<>();
				cMap.put("appid", QqConstants.APP_ID);
				cMap.put("userip", Ip2Int(role.getLastLoginIp())+"");
				cMap.put("svrip", Ip2Int(ChuanQiConfigUtil.getServerIp())+"");
				cMap.put("time", GameSystemTime.getSystemMillTime()/1000+"");
				String pfYuan = keyMap.get("pf");
				if(pfYuan!=null && pfYuan.startsWith("union")){
					cMap.put("reserve_5", pfYuan);
					cMap.put("domain", "17");//pf含union  domain传17
				}else{
					cMap.put("domain", pfChargePfId(keyMap.get("pfyuan"))+"");
				}
				cMap.put("worldid", keyMap.get("sId"));
				cMap.put("opuid", userRoleId+"");
				cMap.put("opopenid", role.getUserId());
				
				
				sendLuoPan(ChuanQiConfigUtil.getTencentLuoPanUrl(),cMap, lType);
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}
	 
	 public void tencentRegisterLuoPan(Long userRoleId){
		 RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		 Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		 if(keyMap == null){
				return;
		  }
		 //腾讯罗盘
		 try {
			 Map<String, String> cMap = new HashMap<>();
			 cMap.put("appid", QqConstants.APP_ID);
			 cMap.put("userip", Ip2Int(role.getLastLoginIp())+"");
			 cMap.put("svrip", Ip2Int(ChuanQiConfigUtil.getServerIp())+"");
			 cMap.put("time", GameSystemTime.getSystemMillTime()/1000+"");
			 String pfYuan = keyMap.get("pf");
				if(pfYuan!=null && pfYuan.startsWith("union")){
					cMap.put("domain", "17");//pf含union  domain传17
				}else{
					cMap.put("domain", pfChargePfId(keyMap.get("pfyuan"))+"");
				}
			 cMap.put("worldid", keyMap.get("sId"));
			 cMap.put("opuid", userRoleId+"");
			 cMap.put("opopenid", role.getUserId());
			 //本次登陆在线时间
			 long curTime = GameSystemTime.getSystemMillTime() - role.getOnlineTime();
			 cMap.put("onlinetime", curTime+"");

			 sendLuoPan(ChuanQiConfigUtil.getTencentLuoPanUrl(),cMap, QqConstants.QUIT);
		 } catch (Exception e) {
			 ChuanQiLog.error("",e);
		 }
	 }
	 
	 public void tencentRechageLuoPan(Long userRoleId,String lType,long yb){
		 RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		 Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		 if(keyMap == null){
				return;
			}
		 //腾讯罗盘
		 try {
			 Map<String, String> cMap = new HashMap<>();
			 cMap.put("appid", QqConstants.APP_ID);
			 cMap.put("userip", Ip2Int(role.getLastLoginIp())+"");
			 cMap.put("svrip", Ip2Int(ChuanQiConfigUtil.getServerIp())+"");
			 cMap.put("time", GameSystemTime.getSystemMillTime()/1000+"");
			 String pfYuan = keyMap.get("pf");
				if(pfYuan!=null && pfYuan.startsWith("union")){
					cMap.put("domain", "17");//pf含union  domain传17
				}else{
					cMap.put("domain", pfChargePfId(keyMap.get("pfyuan"))+"");
				}
			 cMap.put("worldid", keyMap.get("sId"));
			 cMap.put("opuid", userRoleId+"");
			 cMap.put("opopenid", role.getUserId());
			 cMap.put("modifyfee", yb+"");
			
			 sendLuoPan(ChuanQiConfigUtil.getTencentLuoPanUrl(),cMap, lType);
		 } catch (Exception e) {
			 ChuanQiLog.error("",e);
		 }
	 }
	 
	 /**
	  * IP转成int
	  * @param strIp
	  * @return
	  */
	 public static int Ip2Int(String strIp){
	        String[] ss = strIp.split("\\.");
	        if(ss.length != 4){
	            return 0;
	        }
	        int ip =  (int) (Integer.parseInt(ss[0])*Math.pow(256,3)+Integer.parseInt(ss[1])*Math.pow(256,2)+Integer.parseInt(ss[2])*Math.pow(256,1)+Integer.parseInt(ss[3])*Math.pow(256,0));
	        return ip;
	 }
	 
	 /**
	  * 腾讯罗盘联盟注册
	  * @param userRoleId
	  * @param lType  罗盘类型
	  */
	 public void tencentLuoPanLmZhuCe(Long userRoleId){
		RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);
		Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		if(keyMap == null){
			return;
		}
		String x5sig = keyMap.get("x5sig");
		//if(x5sig != null && !"".equals(x5sig)){
			//腾讯罗盘
			try {
				Map<String, String> cMap = new HashMap<>();
				cMap.put("version", "1");
				cMap.put("appid", QqConstants.APP_ID);
				cMap.put("userip", Ip2Int(role.getLastLoginIp())+"");
				cMap.put("svrip", Ip2Int(ChuanQiConfigUtil.getServerIp())+"");
				cMap.put("time", GameSystemTime.getSystemMillTime()+"");
				cMap.put("worldid", keyMap.get("sId"));
				cMap.put("uid", userRoleId+"");
				cMap.put("openid", role.getUserId());
				cMap.put("pf", keyMap.get("pf"));
				cMap.put("openkey", keyMap.get("openkey"));
				cMap.put("pfkey", keyMap.get("pfkey"));
				cMap.put("x5sig", x5sig);
				
				tencentLuoPanService.sendLuoPanLM(ChuanQiConfigUtil.getTencentLuoPanLMUrl(), cMap, QqConstants.LM_CHUANGJIAO);
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		//}
		
	}
	 /**
	  * 腾讯罗盘联盟登陆
	  * @param userRoleId
	  * @param lType  罗盘类型
	  */
	 public void tencentLuoPanLmLogin(Long userRoleId){
		 RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);
		 Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		 if(keyMap == null){
			 return;
		 }
		 String x5sig = keyMap.get("x5sig");
		 //if(x5sig != null && !"".equals(x5sig)){
		 //腾讯罗盘
		 try {
			 Map<String, String> cMap = new HashMap<>();
			 cMap.put("version", "1");
			 cMap.put("appid", QqConstants.APP_ID);
			 cMap.put("userip", Ip2Int(role.getLastLoginIp())+"");
			 cMap.put("svrip", Ip2Int(ChuanQiConfigUtil.getServerIp())+"");
			 cMap.put("time", GameSystemTime.getSystemMillTime()+"");
			 cMap.put("worldid", keyMap.get("sId"));
			 cMap.put("uid", userRoleId+"");
			 cMap.put("level", role.getLevel()+""); 
			 cMap.put("openid", role.getUserId());
			 cMap.put("pf", keyMap.get("pf"));
			 cMap.put("openkey", keyMap.get("openkey"));
			 cMap.put("pfkey", keyMap.get("pfkey"));
			 cMap.put("x5sig", x5sig);
			 
			 tencentLuoPanService.sendLuoPanLM(ChuanQiConfigUtil.getTencentLuoPanLMUrl(), cMap, QqConstants.LM_LOGIN);
		 } catch (Exception e) {
			 ChuanQiLog.error("",e);
		 }
		 //}
	 }
	 /**
	  * 腾讯罗盘联盟到达创角页
	  * @param userRoleId
	  * @param lType  罗盘类型
	  */
	 public void tencentLuoPanLmLoginChuan(String userId,String ip){
		 Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, userId);	
		 if(keyMap == null){
			 return;
		 }
		 String x5sig = keyMap.get("x5sig");
		 //if(x5sig != null && !"".equals(x5sig)){
		 //腾讯罗盘
		 try {
			 Map<String, String> cMap = new HashMap<>();
			 cMap.put("version", "1");
			 cMap.put("appid", QqConstants.APP_ID);
			 cMap.put("userip", Ip2Int(ip)+"");
			 cMap.put("svrip", Ip2Int(ChuanQiConfigUtil.getServerIp())+"");
			 cMap.put("time", GameSystemTime.getSystemMillTime()+"");
			 cMap.put("worldid", keyMap.get("sId"));
			 cMap.put("uid", "");
			 cMap.put("level", "-1"); 
			 cMap.put("openid", userId);
			 cMap.put("pf", keyMap.get("pf"));
			 cMap.put("openkey", keyMap.get("openkey"));
			 cMap.put("pfkey", keyMap.get("pfkey"));
			 cMap.put("x5sig", x5sig);
			 
			 tencentLuoPanService.sendLuoPanLM(ChuanQiConfigUtil.getTencentLuoPanLMUrl(), cMap, QqConstants.LM_LOGIN);
		 } catch (Exception e) {
			 ChuanQiLog.error("",e);
		 }
		 //}
	 }
	 /**
	  * 腾讯罗盘联盟充值
	  * @param userRoleId
	  * @param lType  罗盘类型
	  */
	 public void tencentLuoPanLmRecharge(Long userRoleId,int yb,String billno){
		 RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		 Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		 if(keyMap == null){
				return;
		 }
		 String x5sig = keyMap.get("x5sig");
		 //if(x5sig != null && !"".equals(x5sig)){
			 //腾讯罗盘
			 try {
				 Map<String, String> cMap = new HashMap<>();
				 cMap.put("version", "1");
				 cMap.put("appid", QqConstants.APP_ID);
				 cMap.put("userip", Ip2Int(role.getLastLoginIp())+"");
				 cMap.put("svrip", Ip2Int(ChuanQiConfigUtil.getServerIp())+"");
				 cMap.put("time", GameSystemTime.getSystemMillTime()+"");
				 cMap.put("worldid", keyMap.get("sId"));
				 cMap.put("uid", userRoleId+"");
				 cMap.put("openid", role.getUserId());
				 cMap.put("pf", keyMap.get("pf"));
				 cMap.put("fee", yb+"");
				 cMap.put("serial", billno);
				 cMap.put("x5sig", x5sig);
				 cMap.put("paytype", "");
				 
				 tencentLuoPanService.sendLuoPanLMRecharge(ChuanQiConfigUtil.getTencentLuoPanLMUrl(), cMap, QqConstants.LM_RECHARGE,keyMap.get("pfkey"));
			 } catch (Exception e) {
				 ChuanQiLog.error("",e);
			 }
		// }
	 }
	 
	 /**
	  * 腾讯via用户绑定
	  * @param userRoleId
	  */
	 public void tencentViaUser(Long userRoleId){
		 tencentLuoPanService.tencentViaUser(userRoleId);
	 }
	 public List<TencentUserInfo> initTencentUserInfos(Long userRoleId){
		 return tencentLuoPanService.initTencentUserInfos(userRoleId);
	 }
	 public String getUserZhuCePf(Long userRoleId){
		 return tencentLuoPanService.getUserZhuCePf(userRoleId);
	 }
	 public String getUserZhuCeVia(Long userRoleId){
		 return tencentLuoPanService.getUserZhuCeVia(userRoleId);
	 }
	 
	 /**
	  * 腾讯罗盘OSS注册
	  * @param userRoleId
	  * @param lType  罗盘类型
	  */
	 public void tencentLuoPanOssZhuCe(Long userRoleId){
		RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);
		Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		if(keyMap == null){
			return;
		}
		//腾讯罗盘
		try {
			Map<String, String> cMap = new HashMap<>();
			cMap.put("iappid", QqConstants.APP_ID);
			String pfYuan = keyMap.get("pf");
			if(pfYuan!=null && pfYuan.startsWith("union")){
				cMap.put("idomain", "17");//pf含union  domain传17
			}else{
				cMap.put("idomain", pfChargePfId(keyMap.get("pfyuan"))+"");
			}
			cMap.put("ioptype", "0");
			cMap.put("iactionid", "0");
			cMap.put("iworldid", (Integer.parseInt(keyMap.get("sId"))+255)+"");//大区ID，由于1-255在有特殊的用途，所有上报worldid都+255，这个规则需要对所有上报的表生效。
			cMap.put("vuin", role.getUserId());
			cMap.put("iuserip", Ip2Int(role.getLastLoginIp())+"");
			cMap.put("ieventTime", GameSystemTime.getSystemMillTime()/1000+"");
			cMap.put("iroleId", userRoleId+"");
			cMap.put("ijobId", role.getConfigId()+"");
			
			tencentLuoPanOssService.sendLuoPan(ChuanQiConfigUtil.getTencentLuoPanOssUrl(), cMap);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
	}
	 /**
	  * 腾讯罗盘OSS登陆
	  * @param userRoleId
	  * @param lType  罗盘类型
	  */
	 public void tencentLuoPanOssLoign(Long userRoleId){
		 RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);
		 Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		 if(keyMap == null){
			 return;
		 }
		 //腾讯罗盘
		 try {
			 Map<String, String> cMap = new HashMap<>();
			 cMap.put("iappid", QqConstants.APP_ID);
			 String pfYuan = keyMap.get("pf");
			 if(pfYuan!=null && pfYuan.startsWith("union")){
			 	cMap.put("idomain", "17");//pf含union  domain传17
			 }else{
				cMap.put("idomain", pfChargePfId(keyMap.get("pfyuan"))+"");
		 	 }
			 cMap.put("ioptype", "1");
			 cMap.put("iactionid", "0");
			 cMap.put("iworldid", (Integer.parseInt(keyMap.get("sId"))+255)+"");//大区ID，由于1-255在有特殊的用途，所有上报worldid都+255，这个规则需要对所有上报的表生效。
			 cMap.put("vuin", role.getUserId());
			 cMap.put("iuserip", Ip2Int(role.getLastLoginIp())+"");
			 cMap.put("ieventTime", GameSystemTime.getSystemMillTime()/1000+"");
			 cMap.put("iroleId", userRoleId+"");
			 cMap.put("ijobId", role.getConfigId()+"");
			 cMap.put("ilevel", role.getLevel()+"");
			 cMap.put("vparam_1", role.getExp()+"");
			 //获取货币信息
			 RoleAccount account = accountExportService.getRoleAccount(userRoleId);
			 if(account == null){
				 cMap.put("iparam_16", "0");
				 cMap.put("iparam_17", "0");
				 cMap.put("iparam_18", "0");
			 }else{
				 cMap.put("iparam_16", account.getYb()+"");
				 cMap.put("iparam_17", account.getBindYb()+"");
				 cMap.put("iparam_18", account.getNoReYb()+"");
			 }
			 cMap.put("iparam_20", role.getCreateTime().getTime()/1000+"");
			 cMap.put("iparam_1", roleOnlineExportService.getTotalOnlineTime(userRoleId)+"");

			 tencentLuoPanOssService.sendLuoPan(ChuanQiConfigUtil.getTencentLuoPanOssUrl(), cMap);
		 } catch (Exception e) {
			 ChuanQiLog.error("",e);
		 }
		 
	 }
	 /**
	  * 腾讯罗盘OSS充值
	  * @param userRoleId
	  * @param lType  罗盘类型
	  */
	 public void tencentLuoPanOssRecharge(Long userRoleId,int qDian,int yb,String billno){
		 ChuanQiLog.error("OSS充值");
		 RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);
		 Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		 if(keyMap == null){
			 return;
		 }
		 //腾讯罗盘
		 try {
			 Map<String, String> cMap = new HashMap<>();
			 cMap.put("iappid", QqConstants.APP_ID);
			 String pfYuan = keyMap.get("pf");
			 if(pfYuan!=null && pfYuan.startsWith("union")){
				 cMap.put("idomain", "17");//pf含union  domain传17
			 }else{
				 cMap.put("idomain", pfChargePfId(keyMap.get("pfyuan"))+"");
			 }
			 cMap.put("ioptype", "1001");
			 cMap.put("iactionid", "0");
			 cMap.put("iworldid", (Integer.parseInt(keyMap.get("sId"))+255)+"");//大区ID，由于1-255在有特殊的用途，所有上报worldid都+255，这个规则需要对所有上报的表生效。
			 cMap.put("vuin", role.getUserId());
			 cMap.put("iuserip", Ip2Int(role.getLastLoginIp())+"");
			 cMap.put("ieventTime", GameSystemTime.getSystemMillTime()/1000+"");
			 
			 cMap.put("iparam_1", qDian+"");
			 cMap.put("iparam_2", yb+"");
			 cMap.put("vparam_1", "");
			 cMap.put("vparam_2", billno);
			 cMap.put("iparam_3", "1");
			 cMap.put("iparam_4", "0");
			 
			 tencentLuoPanOssService.sendLuoPan(ChuanQiConfigUtil.getTencentLuoPanOssUrl(), cMap);
		 } catch (Exception e) {
			 ChuanQiLog.error("",e);
		 }
		 
	 }
	 /**
	  * 腾讯罗盘OSS退出日志上报
	  * @param userRoleId
	  * @param lType  罗盘类型
	  */
	 public void tencentLuoPanOssTuichu(Long userRoleId){
		 RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);
		 Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		 if(keyMap == null){
			 return;
		 }
		 //腾讯罗盘
		 try {
			 Map<String, String> cMap = new HashMap<>();
			 cMap.put("iappid", QqConstants.APP_ID);
			 String pfYuan = keyMap.get("pf");
			 if(pfYuan!=null && pfYuan.startsWith("union")){
				 cMap.put("idomain", "17");//pf含union  domain传17
			 }else{
				 cMap.put("idomain", pfChargePfId(keyMap.get("pfyuan"))+"");
			 }
			 cMap.put("ioptype", "1");
			 cMap.put("iactionid", "1");
			 cMap.put("iworldid", (Integer.parseInt(keyMap.get("sId"))+255)+"");//大区ID，由于1-255在有特殊的用途，所有上报worldid都+255，这个规则需要对所有上报的表生效。
			 cMap.put("vuin", role.getUserId());
			 cMap.put("iuserip", Ip2Int(role.getLastLoginIp())+"");
			 cMap.put("ieventTime", GameSystemTime.getSystemMillTime()/1000+"");
			 cMap.put("iroleId", userRoleId+"");
			 cMap.put("ijobId", role.getConfigId()+"");
			 cMap.put("ilevel", role.getLevel()+"");
			 cMap.put("vparam_1", role.getExp()+"");
			 //获取货币信息
			 RoleAccount account = accountExportService.getRoleAccount(userRoleId);
			 if(account == null){
				 cMap.put("iparam_16", "0");
				 cMap.put("iparam_17", "0");
				 cMap.put("iparam_18", "0");
			 }else{
				 cMap.put("iparam_16", account.getYb()+"");
				 cMap.put("iparam_17", account.getBindYb()+"");
				 cMap.put("iparam_18", account.getNoReYb()+"");
			 }
			 cMap.put("iparam_20", role.getCreateTime().getTime()/1000+"");
			 //本次登陆在线时间
			 long curTime = GameSystemTime.getSystemMillTime() - role.getOnlineTime();
			 cMap.put("iparam_1", curTime+"");
			 
			 tencentLuoPanOssService.sendLuoPan(ChuanQiConfigUtil.getTencentLuoPanOssUrl(), cMap);
		 } catch (Exception e) {
			 ChuanQiLog.error("",e);
		 }
		 
	 }
	 /**
	  * 腾讯罗盘OSS在线人数日志上报
	  * @param userRoleId
	  * @param lType  罗盘类型
	  */
	 public void tencentLuoPanOssRenShu(Integer sessionCounts){
		 //腾讯罗盘
		 try {
			 Map<String, String> cMap = new HashMap<>();
			 cMap.put("iappid",  QqConstants.APP_ID);
			 cMap.put("idomain",  pfChargePfId(ChuanQiConfigUtil.getPlatfromId())+"");
			 cMap.put("ioptype", "2");
			 cMap.put("iactionid", "0");
			 cMap.put("iworldid", (Integer.parseInt(ChuanQiConfigUtil.getPlatformServerId())+255)+"");//大区ID，由于1-255在有特殊的用途，所有上报worldid都+255，这个规则需要对所有上报的表生效。
			 cMap.put("ieventTime", GameSystemTime.getSystemMillTime()/1000+"");
			 cMap.put("iparam_1", sessionCounts+"");
			 
			 tencentLuoPanOssService.sendLuoPan(ChuanQiConfigUtil.getTencentLuoPanOssUrl(), cMap);
		 } catch (Exception e) {
			 ChuanQiLog.error("",e);
		 }
		 
	 }
	 
	 /**
	  * 腾讯罗盘OSS元宝消费日志上报
	  * @param userRoleId
	  * @param lType  罗盘类型
	  */
	 public void tencentLuoPanOssXiaofei(Long userRoleId,String zhiFuType,int value,String goodsId,String goodName,int count){
		 RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);
		 Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		 if(keyMap == null){
			 return;
		 }
		 //腾讯罗盘
		 try {
			 Map<String, String> cMap = new HashMap<>();
			 cMap.put("iappid", QqConstants.APP_ID);
			 String pfYuan = keyMap.get("pf");
			 if(pfYuan!=null && pfYuan.startsWith("union")){
				 cMap.put("idomain", "17");//pf含union  domain传17
			 }else{
				 cMap.put("idomain", pfChargePfId(keyMap.get("pfyuan"))+"");
			 }
			 cMap.put("ioptype", "3");
			 cMap.put("iactionid", "0");
			 cMap.put("iworldid", (Integer.parseInt(keyMap.get("sId"))+255)+"");//大区ID，由于1-255在有特殊的用途，所有上报worldid都+255，这个规则需要对所有上报的表生效。
			 cMap.put("vuin", role.getUserId());
			 cMap.put("ilevel", role.getLevel()+"");
			 cMap.put("iuserip", Ip2Int(role.getLastLoginIp())+"");
			 cMap.put("ieventTime", GameSystemTime.getSystemMillTime()/1000+"");
			 cMap.put("iroleId", userRoleId+"");
			 cMap.put("iparam_1", zhiFuType);
			 cMap.put("iparam_2", value+"");
			 cMap.put("iparam_3", "1");//商品类型 写死为1
			 cMap.put("iparam_16", goodsId);
			 cMap.put("vparam_1", goodName);
			 cMap.put("iparam_4", count+"");
			 
			 
			 tencentLuoPanOssService.sendLuoPan(ChuanQiConfigUtil.getTencentLuoPanOssUrl(), cMap);
		 } catch (Exception e) {
			 ChuanQiLog.error("",e);
		 }
		 
	 }
	 /**
	  * 腾讯罗盘OSS大区日志上报（每日23点上报）
	  * @param userRoleId
	  * @param lType  罗盘类型
	  */
	 public void tencentLuoPanOssDaQu(){
		 //腾讯罗盘
		 try {
			 Map<String, String> cMap = new HashMap<>();
			 cMap.put("iappid",  QqConstants.APP_ID);
			 cMap.put("idomain",  "0");
			 cMap.put("ioptype", "4");
			 cMap.put("iactionid", "0");
			 cMap.put("ieventTime", GameSystemTime.getSystemMillTime()/1000+"");
			 cMap.put("iworldid", (Integer.parseInt(ChuanQiConfigUtil.getPlatformServerId())+255)+"");//大区ID，由于1-255在有特殊的用途，所有上报worldid都+255，这个规则需要对所有上报的表生效。
			 cMap.put("vparam_1", ChuanQiConfigUtil.getServerName());
			 cMap.put("iparam_1", GsCountChecker.getInstance().getMaxCount()+"");
			 cMap.put("iparam_2", "0");
			 cMap.put("vparam_2", "");
			 cMap.put("vparam_3", ServerInfoServiceManager.getInstance().getServerStartTime()+"");
			 
			 tencentLuoPanOssService.sendLuoPan(ChuanQiConfigUtil.getTencentLuoPanOssUrl(), cMap);
		 } catch (Exception e) {
			 ChuanQiLog.error("",e);
		 }
	 }
	 
}
