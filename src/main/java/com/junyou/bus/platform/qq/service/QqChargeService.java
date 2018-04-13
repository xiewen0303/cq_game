package com.junyou.bus.platform.qq.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.qq.confiure.export.QqQDCountPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqQDGoodPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqQDJiaGePublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqRechargePublicConfig;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.dao.RoleQdianZhigouDao;
import com.junyou.bus.platform.qq.entity.RoleQdianZhigou;
import com.junyou.bus.platform.qq.json.JSONException;
import com.junyou.bus.platform.qq.json.JSONObject;
import com.junyou.bus.platform.qq.utils.QqUtil;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.spring.container.DataContainer;

@Service
public class QqChargeService {

	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private PlatformGongGongShuJuBiaoConfigExportService platformGongGongShuJuBiaoConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleQdianZhigouDao roleQdianZhigouDao;
	
	
	public Object[] buyChargeInfo(Integer type,String tupianUrl,Long userRoleId) {
		//openid  账号
		//openkey  
		//appid  应用的唯一ID
		//pf  平台
		//format 定义api返回格式
		//pfkey 平台key
		//amt  购买物品总价格
		//ts  时间戳 以秒为单位。
		//payitem  购买的物品信息
		//goodsmeta  物品信息 格式必须是“name*des”， name表示物品的名称，des表示物品的描述信息
		//goodsurl  物品图片url
		//zoneid  区服ID
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("openid", role.getUserId());
		params.put("openkey", keyMap.get("openkey"));
		params.put("appid", QqConstants.APP_ID);
		params.put("pf", keyMap.get("pf"));
		//params.put("pf", "qqgame-terminal");
		params.put("format", QqConstants.FORMAT);
		params.put("pfkey", keyMap.get("pfkey"));
		params.put("ts", (new Date().getTime() /1000)+"");
		//获取玩家黄钻等级
		/*HashMap<String, String> hParams = new HashMap<String, String>();
		hParams.put("openid", role.getUserId());
		hParams.put("openkey",keyMap.get("openkey"));
		hParams.put("appid", QqConstants.APP_ID+"");
		hParams.put("pf", keyMap.get("pf"));
		hParams.put("format", QqConstants.FORMAT);
		
		String hScriptName = "/v3/user/get_info";
		String hProtocol = "http";
		
		String huangzuan = QqUtil.api(hScriptName, hParams, hProtocol, QqConstants.APP_ID, QqConstants.APP_KEY, ChuanQiConfigUtil.getTencentUrl());
		ChuanQiLog.error("充值获取黄钻的返回值:"+huangzuan);
		int huangZuanLevel = 0;
		try {
			huangZuanLevel = QqUtil.huangZuanLevel(huangzuan);
		} catch (OpensnsException e1) {
			ChuanQiLog.error("huoqu huangzuan level error",e1);
		}*/
		
		QqRechargePublicConfig publicConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_RECHARGE);
		if(publicConfig == null){
			return AppErrorCode.NOT_FOUND_GOOODS;
		}
		//物品信息
		Map<String, String> chargeMap = publicConfig.getChargeMap();
		String goodsId = "gold"+type;
		//价格
		int jiage = Integer.parseInt(chargeMap.get(goodsId)) / Integer.parseInt(chargeMap.get("goldbi"));
		
		String pf = keyMap.get("pf");
		if("myapp_pc".equals(pf)){
			pf = "myapp";
		}
		pf = pf.replace("*", "~");
		String payitem = getServerIdNum(role.getServerId())+"_"+0+"_"+pf+"_"+role.getLevel()+"_"+userRoleId+"_"+role.getLastLoginIp()+"_"+goodsId+"*"+jiage+"*"+1;
		params.put("payitem", payitem);
		String goodsmeta = chargeMap.get("name"+type)+"*"+chargeMap.get("des"+type);
		try {
			goodsmeta = URLEncoder.encode(goodsmeta.toString(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			ChuanQiLog.error("goodsmeta URLEncoder.encode error",e);
		}
		params.put("goodsmeta", goodsmeta);
		params.put("goodsurl", tupianUrl);
		params.put("zoneid", "0");
		
		
		String scriptName = "/v3/pay/buy_goods";
		String protocol = "https";
		String resp = QqUtil.api(scriptName, params, protocol, QqConstants.APP_ID, QqConstants.APP_KEY, ChuanQiConfigUtil.getTencentUrl());
		JSONObject jo = null;
		try {
			jo = new JSONObject(resp);
		} catch (JSONException e) {
			ChuanQiLog.error("api return json error",e);
		}
	    // 检测ret值
	    int rc = jo.optInt("ret", 0);
	    if(rc != 0){
	    	return AppErrorCode.FUWU_QI_FANMANG;
	    }else{
	    	String url = jo.optString("url_params");
	    	String token = jo.optString("token");
	    	ChuanQiLog.error("==============:"+type+"\n====="+url+"\n========"+token);
	    	return new Object[]{1,type,url,token};
	    }
	}
	/**
	 * 获取开通蓝钻token
	 * @param type
	 * @param tupianUrl
	 * @param userRoleId
	 * @return
	 */
	public Object[] getLanZuanToken(Long userRoleId) {
		//openid  账号
		//openkey  
		//appid  应用的唯一ID
		//pf  平台
		//format 定义api返回格式
		//pfkey 平台key
		//amt  购买物品总价格
		//ts  时间戳 以秒为单位。
		//payitem  购买的物品信息
		//goodsmeta  物品信息 格式必须是“name*des”， name表示物品的名称，des表示物品的描述信息
		//goodsurl  物品图片url
		//zoneid  区服ID
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("openid", role.getUserId());
		params.put("openkey", keyMap.get("openkey"));
		params.put("appid", QqConstants.APP_ID);
		params.put("pf", keyMap.get("pf"));
		params.put("format", QqConstants.FORMAT);
		params.put("pfkey", keyMap.get("pfkey"));
		params.put("ts", (new Date().getTime() /1000)+"");
		params.put("tokentype", "1");
		params.put("discountid", QqConstants.LAN_HOUDONG_HAO);
		params.put("zoneid", role.getServerId());
		params.put("version", "v3");
		
		
		String scriptName = "/v3/pay/get_token";
		String protocol = "http";
		String resp = QqUtil.api(scriptName, params, protocol, QqConstants.APP_ID, QqConstants.APP_KEY, ChuanQiConfigUtil.getTencentUrl());
		JSONObject jo = null;
		try {
			jo = new JSONObject(resp);
			ChuanQiLog.error("/v3/pay/get_token接口返回："+jo);
		} catch (JSONException e) {
			ChuanQiLog.error("api return json error",e);
		}
		// 检测ret值
		int rc = jo.optInt("ret", 0);
		if(rc != 0){
			return AppErrorCode.FUWU_QI_FANMANG;
		}else{
			String token = jo.optString("token");
			return new Object[]{1,token};
		}
	}
	
	public List<RoleQdianZhigou> initRoleQdianZhigous(Long userRoleId){
		return roleQdianZhigouDao.initRoleQdianZhigou(userRoleId);
	}
	
	private RoleQdianZhigou getRoleQdianZhigou(Long userRoleId){
		List<RoleQdianZhigou> list = roleQdianZhigouDao.cacheAsynLoadAll(userRoleId);
		if(list == null || list.size() <= 0){
			RoleQdianZhigou zhigou = new RoleQdianZhigou();
			zhigou.setUserRoleId(userRoleId);
			zhigou.setCount("");
			zhigou.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			zhigou.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			
			roleQdianZhigouDao.cacheInsert(zhigou, userRoleId);
			return zhigou;
		}
		RoleQdianZhigou zhigou = list.get(0);
		//不是同一天，清理数据
		if(!DateUtils.isSameDay(zhigou.getUpdateTime(), new Timestamp(GameSystemTime.getSystemMillTime()))){
			zhigou.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			zhigou.setCount("");
			
			roleQdianZhigouDao.cacheUpdate(zhigou, userRoleId);
		}
		return zhigou;
	}
	
	
	public Object[] buyGoodSInfo(String id,String tupianUrl,Long userRoleId) {
		//openid  账号
		//openkey  
		//appid  应用的唯一ID
		//pf  平台
		//format 定义api返回格式
		//pfkey 平台key
		//amt  购买物品总价格
		//ts  时间戳 以秒为单位。
		//payitem  购买的物品信息
		//goodsmeta  物品信息 格式必须是“name*des”， name表示物品的名称，des表示物品的描述信息
		//goodsurl  物品图片url0
		//zoneid  区服ID
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("openid", role.getUserId());
		params.put("openkey", keyMap.get("openkey"));
		params.put("appid", QqConstants.APP_ID);
		params.put("pf", keyMap.get("pf"));
		params.put("format", QqConstants.FORMAT);
		params.put("pfkey", keyMap.get("pfkey"));
		params.put("ts", (new Date().getTime() /1000)+"");
		
		QqQDGoodPublicConfig goodPublicConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QDIAN_ZHIGOU);
		QqQDJiaGePublicConfig jiagePublicConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QDIAN_JIAGE);
		QqQDCountPublicConfig countPublicConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QDIAN_COUNT);
		if(goodPublicConfig == null || jiagePublicConfig == null || countPublicConfig == null){
			return AppErrorCode.NOT_FOUND_GOOODS;
		}
		//物品信息
		Map<String, String> goodsMap = goodPublicConfig.getItems();
		Map<String, String> jiageMap = jiagePublicConfig.getItems();
		Map<String, Integer> countMap = countPublicConfig.getItems();
		//价格
		String goodsId = goodsMap.get(id);
		String jiage = jiageMap.get(id);
		Integer count = countMap.get(id);
		
		RoleQdianZhigou zhigou = getRoleQdianZhigou(userRoleId);
		//玩家次数
		int yiCount = getLingQuCount(zhigou.getCount(), goodsId);
		//判断是否次数已完
		if(yiCount >= count){
			return AppErrorCode.NOT_BUY_COUNT;
		}
		
		String pf = keyMap.get("pf");
		if("myapp_pc".equals(pf)){
			pf = "myapp";
		}
		pf = pf.replace("*", "~");
		String payitem = getServerIdNum(role.getServerId())+"_"+0+"_"+pf+"_"+role.getLevel()+"_"+userRoleId+"_"+role.getLastLoginIp()+"_"+goodsId+"*"+jiage+"*"+1;
		params.put("payitem", payitem);
		//String goodsmeta = chargeMap.get("name"+type)+"*"+chargeMap.get("des"+type);
		GoodsConfig goodConfig = goodsConfigExportService.loadById(goodsId);
		if(goodConfig == null){
			return AppErrorCode.NOT_FOUND_GOOODS;
		}
		String goodsmeta = goodConfig.getName()+"*null";
		try {
			goodsmeta = URLEncoder.encode(goodsmeta.toString(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			ChuanQiLog.error("goodsmeta URLEncoder.encode error",e);
		}
		params.put("appmode", "1");
		params.put("goodsmeta", goodsmeta);
		params.put("goodsurl", tupianUrl);
		params.put("zoneid", "0");
		
		
		String scriptName = "/v3/pay/buy_goods";
		String protocol = "https";
		String resp = QqUtil.api(scriptName, params, protocol, QqConstants.APP_ID, QqConstants.APP_KEY, ChuanQiConfigUtil.getTencentUrl());
		JSONObject jo = null;
		try {
			jo = new JSONObject(resp);
		} catch (JSONException e) {
			ChuanQiLog.error("api return json error",e);
		}
		// 检测ret值
		int rc = jo.optInt("ret", 0);
		if(rc != 0){
			return AppErrorCode.FUWU_QI_FANMANG;
		}else{
			String url = jo.optString("url_params");
			String token = jo.optString("token");
			
			return new Object[]{1,id,url,token};
		}
	}
	
	/**
	 * 设置玩家购买次数
	 */
	public void setUserBuyCount(Long userRoleId,String goodsId){
		RoleQdianZhigou zhigou = getRoleQdianZhigou(userRoleId);
		//玩家次数
		int yiCount = getLingQuCount(zhigou.getCount(), goodsId);
		
		zhigou.setCount(setLingQuStatus(zhigou.getCount(), goodsId, yiCount));
		zhigou.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		roleQdianZhigouDao.cacheUpdate(zhigou, userRoleId);
		QqQDGoodPublicConfig goodPublicConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QDIAN_ZHIGOU);
		//物品信息
		Map<String, String> goodsMap = goodPublicConfig.getItems();
		for (String id : goodsMap.keySet()) {
			if(goodsId.equals(goodsMap.get(id))){
				BusMsgSender.send2One(userRoleId, ClientCmdType.TUISONG_QDIAN,id);
			}
		}
	}
	
	
	public Object[] getGoodsCount(Long userRoleId){
		
		RoleQdianZhigou zhigou = getRoleQdianZhigou(userRoleId);
		if(zhigou.getCount() == null || "".equals(zhigou.getCount())){
			return null;
		}
		List<Object[]> list = new ArrayList<>();
		QqQDGoodPublicConfig goodPublicConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QDIAN_ZHIGOU);
		//物品信息
		Map<String, String> goodsMap = goodPublicConfig.getItems();
		String[] lingqu = zhigou.getCount().split("\\|");
		for (int i = 0; i < lingqu.length; i++) {
			if(lingqu[i] == null || "".equals(lingqu[i])){
				continue;
			}
			String[] status = lingqu[i].split(",");
			String goodsId = status[0];
			for (String id : goodsMap.keySet()) {
				if(goodsId.equals(goodsMap.get(id))){
					list.add(new Object[]{id,Integer.parseInt(status[1])});
				}
			}
		}
		
		return list.toArray();
	}
	
	private String setLingQuStatus(String lingquStatus,String configId,int count){
		int yiCount = count + 1;
		if(lingquStatus == null || "".equals(lingquStatus)){
			return configId+","+yiCount;
		}else{
			if(lingquStatus.contains(configId+","+count )){
				lingquStatus = lingquStatus.replace(configId+","+count, configId+","+yiCount);
			}else{
				lingquStatus = lingquStatus+"|"+configId+","+yiCount;
			}
			return lingquStatus;
		}
		
	}
	
	/**
	 * 获取购买次数
	 * @return
	 */
	private int getLingQuCount(String lingquStatus,String id){
		String[] lingqu = lingquStatus.split("\\|");
		if(lingqu == null || lingqu.length <= 0){
			return 0;
		}
		for (int i = 0; i < lingqu.length; i++) {
			if(lingqu[i] == null || "".equals(lingqu[i])){
				continue;
			}
			String[] status = lingqu[i].split(",");
			if(status[0].equals(id)){
				return Integer.parseInt(status[1]);
			}
		}
		return 0;
	}
	
	
	/**
	 * 获得服务器ID（数字）
	 * @param serverId
	 * @return
	 */
	private String getServerIdNum(String serverId){
		String regEx="[^0-9]"; 
		Pattern p = Pattern.compile(regEx);     
		Matcher m = p.matcher(serverId);     
		return m.replaceAll("").trim();  
	}

}
