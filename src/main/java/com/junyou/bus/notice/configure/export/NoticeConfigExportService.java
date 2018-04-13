package com.junyou.bus.notice.configure.export;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsRemoteRefreshAbleConfigureParserByPlatformAndServer;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 解析公告配置
 */
@Component
public class NoticeConfigExportService extends AbsRemoteRefreshAbleConfigureParserByPlatformAndServer {

	private static final String FILE_NAME = "Notice";
	
	private NoticeCategoryConfig noticeCategoryConfig = new NoticeCategoryConfig();
	
	/**
	 * 配置名  http://192.168.0.212/cqupload/
	 */
	private String configureName = "gameNotice/"+FILE_NAME;

	private static final String TYPE_KEY = "global";

//	@Override
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			ChuanQiLog.error(" Notice data is null! ");
			return;
		}
		JSONObject json = GameConfigUtil.getJsonByBytes(data);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" Notice data is error! ");
			return;
		}
		int type = -1;
		ConcurrentMap<Integer,NoticeConfig> map = new ConcurrentHashMap<Integer, NoticeConfig>();
		for (Object obj : json.keySet()) {
			String key = (String)obj;
			if(TYPE_KEY.equals(key)){
				type = CovertObjectUtil.object2int(json.get(key));
				continue;
			}
			JSONObject jobj = (JSONObject)json.get(key);
			NoticeConfig config = createNoticeConfig(jobj);
			if(GameSystemTime.getSystemMillTime() < config.getEndTime() && !"".equals(config.getText()) && null != config.getStep()){
				map.put(config.getId(), config);
			}
		}
		List<Object> change = noticeCategoryConfig.setNotices(map,type);
		if(change != null && change.size() > 0){
			//有公告变通，全局推送
			BusMsgSender.send2All(ClientCmdType.TUISONG_NOTICE, change.toArray());
		}
		ConfigMd5SignManange.addConfigSign(FILE_NAME, data);
	}

//	@Override
	protected void clearData() {
		
	}
	private NoticeConfig createNoticeConfig(JSONObject json){
		NoticeConfig config = new NoticeConfig();
		config.setId(CovertObjectUtil.object2Integer(json.get("id")));
		config.setStartTime(CovertObjectUtil.obj2long(json.get("startTime")));
		config.setEndTime(CovertObjectUtil.obj2long(json.get("endTime")));
		config.setText(CovertObjectUtil.object2String(json.get("content")));
		config.setStep(CovertObjectUtil.object2Integer(json.get("stepTime")));
		config.setLocation(CovertObjectUtil.object2Integer(json.get("location")));
		return config;
	}

//	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public NoticeCategoryConfig getNoticeCategoryConfig() {
		return noticeCategoryConfig;
	}

//	@Override
	public String getSortName() {
		return FILE_NAME;
	}
}
