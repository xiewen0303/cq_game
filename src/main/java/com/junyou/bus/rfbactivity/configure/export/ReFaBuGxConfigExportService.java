package com.junyou.bus.rfbactivity.configure.export;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.event.acitvity.ZhuRfbActivityEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.DownloadRemoteUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;

/**
 * 热发布活动关系解析(不用 spring管理实际，自己管理)
 * @author DaoZheng Yuan
 * 2015年5月18日 上午11:17:42
 */
public class ReFaBuGxConfigExportService {

	private String fileName = "refabuGanXi.jat";
	
	private Map<Integer,ReFaBuGxConfig> GUANG_XI_MAP;
	
	private static final ReFaBuGxConfigExportService INSTANCE = new ReFaBuGxConfigExportService();
	
	/**
	 * 解析全部关系
	 */
	public static final int ALL_GX = -1;
	
	private ReFaBuGxConfigExportService(){
	}
	
	public ReFaBuGxConfig getReFaBuGxConfig(int zuId){
		if(GUANG_XI_MAP == null){
			return null;
		}
		return GUANG_XI_MAP.get(zuId);
	}
	
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static ReFaBuGxConfigExportService getInstance() {
		return INSTANCE;
	}

	
	/**
	 * 初始化
	 */
	public void init(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			byte[] data = DownloadRemoteUtil.download(ChuanQiConfigUtil.getLoadDirectoryUrl(), GameConstants.REFABU_DIR_NAME,fileName);
			analysisConfigureDataResolve(data);
		}
	}
	

	/**
	 * 初始化解析配置
	 * @param data
	 */
	private void analysisConfigureDataResolve(byte[] data) {
		if(data == null){
			ChuanQiLog.errorConfig("ReFaBuGxConfigExportService error data is null!");
			return;
		}
		
		JSONArray json = JsonUtils.getJsonArrayByBytes(data);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" ReFaBuGxConfigExportService json data is error! ");
			return;
		}
		ConfigMd5SignManange.addConfigSign(fileName, data);
		
		//解析业务
		configureDataResolveCalc(json,ALL_GX,true);
	}
	
	/**
	 * 解析业务
	 * @param json 数据数组对象
	 * @param activity 主活动id,-1表示解析全部
	 * @param isInit 是否是初始化 true:是,false:不是
	 */
	private void configureDataResolveCalc(JSONArray json,int activity,boolean isInit){
		Map<Integer,ReFaBuGxConfig> tmpGuanXis = new HashMap<>();
		for(int i=0;i < json.size();i++){
			@SuppressWarnings("unchecked")
			Map<String,Object> tmp = (Map<String, Object>)JSONObject.parse(json.get(i).toString());
			ReFaBuGxConfig config = createReFaBuGxConfig(tmp);
			
			tmpGuanXis.put(config.getId(), config);
		}
		
		GUANG_XI_MAP = tmpGuanXis;
		
		
		/*
		 * 处理本服需要建立的主活动关系
		 */
		String serverId = GameServerContext.getGameAppConfig().getServerId();
		String platformId = GameServerContext.getGameAppConfig().getPlatformId();
		
		Map<Integer,String> zuIds = new HashMap<>();
		for(ReFaBuGxConfig gxConfig : GUANG_XI_MAP.values()){
			//已删除不需要加入管理
			if(gxConfig.isDel()){
				continue;
			}
			
			//如果不是解析全部关系，那就只解析指定关系主活动
			if(ALL_GX != activity && gxConfig.getId() != activity){
				continue;
			}
			
			//默认不下载
			boolean isAdd = false;
			if(gxConfig.isFull()){
				//如果全服开了，先暂时设定为加入
				isAdd = true;
			}
			if(gxConfig.isContainOnlyFu(serverId)){
				//只要包含在onlyFus服配置里，可以直接确定需要下载这个主活动
				isAdd = true;
			}else if(gxConfig.isContainNoFu(serverId)){
				//只要包含在NoFus服配置里，可以直接确定不需要下载这个主活动(OnlyFu的优先级高于Nofu,当两个值相等时以OnlyFu为准，无视Nofu)
				isAdd = false;
			}else if(gxConfig.isContainOnlyPt(platformId)){
				//平台OnlyPt优先级高于NoPt
				isAdd = true;
			}else if(gxConfig.isContainNoPt(platformId)){
				//最后NoPt
				isAdd = false;
			}
			
			//确认最后结果
			if(isAdd){
				zuIds.put(gxConfig.getId(),gxConfig.getActivityConfigName());
			}
		}
		
		/*
		 * 抛出事件，下载主活动配置表
		 */
		if(zuIds.size() > 0){
			GamePublishEvent.publishEvent(new ZhuRfbActivityEvent(zuIds,isInit));
		}else{
			ChuanQiLog.debug("================热发布流程已完成！");
		}
	}
	
	/**
	 * 关系全部解析
	 */
	public void changeAllConfigureDataResolve(){
		changeConfigureDataResolve(ALL_GX);
	}
	
	/**
	 * 关系变化
	 * @param activityId
	 * @param data
	 */
	public void changeConfigureDataResolve(int activityId){
		byte[] data = DownloadRemoteUtil.download(ChuanQiConfigUtil.getLoadDirectoryUrl(), GameConstants.REFABU_DIR_NAME,fileName);
		if(data == null){
			ChuanQiLog.errorConfig("changeConfigureDataResolve error data is null!");
			return;
		}
		
		JSONArray json = JsonUtils.getJsonArrayByBytes(data);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" changeConfigureDataResolve json data is error! ");
			return;
		}
		
		String oldMd5Value = ConfigMd5SignManange.getConfigSignByFileName(fileName);
		if(oldMd5Value == null){
			//原来没有热发布活动，重新走一下全部流程
			configureDataResolveCalc(json,activityId,false);
			return;
		}
		
		String newMd5Value = Md5Utils.md5Bytes(data);
		if(newMd5Value.equals(oldMd5Value)){
			//版号一致不处理业务
			return;
		}
		
		//解析流程
		configureDataResolveCalc(json,activityId,false);
	}
	
	
	private String SPLIT_CHAR = ",";
	
	private ReFaBuGxConfig createReFaBuGxConfig(Map<String, Object> tmp){
		ReFaBuGxConfig config = new ReFaBuGxConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		
		StringBuffer buf = new StringBuffer();
		buf.append(GameConstants.ZHU_RFB_PREFIX).append(config.getId()).append(GameConstants.RFB_SUFFIX);
		config.setActivityConfigName(buf.toString());
		
		config.setFull(CovertObjectUtil.object2Boolean(tmp.get("isFull")));
		config.setDel(CovertObjectUtil.object2Boolean(tmp.get("isDel")));
		
		String value = CovertObjectUtil.object2String(tmp.get("onlyFus"));
		config.setOnlyFus(calcMapByStr(value));
		
		value = CovertObjectUtil.object2String(tmp.get("onlyPts"));
		config.setOnlyPts(calcMapByStr(value));
		
		value = CovertObjectUtil.object2String(tmp.get("noFus"));
		config.setNoFus(calcMapByStr(value));
		
		value = CovertObjectUtil.object2String(tmp.get("noPts"));
		config.setNoPts(calcMapByStr(value));
		
		config.setDays(CovertObjectUtil.object2int(tmp.get("days")));
		
		return config;
	}

	
	private Map<String,Object> calcMapByStr(String tmpStr){
		if(!CovertObjectUtil.isEmpty(tmpStr)){
			Map<String,Object> tmpMap = new HashMap<>();
			String[] tmpArr = tmpStr.split(SPLIT_CHAR);
			
			for (String tmpId : tmpArr) {
				tmpMap.put(tmpId, null);
			}
			
			return tmpMap;
		}else{
			return null;
		}
	}

}
