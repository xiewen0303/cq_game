package com.junyou.bus.pic.configure.export;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.oncechong.configure.export.OnceChongConfigGroup;
import com.junyou.bus.rfbactivity.configure.export.AbstractRfbConfigService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 图片公告
 * @author LiuYu
 * @date 2015-7-29 下午2:54:10
 */
public class PicNoticeConfigExportService extends AbstractRfbConfigService {
	
	private static final PicNoticeConfigExportService INSTANCE = new PicNoticeConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,String> CONFIG_MAP = new HashMap<>();
	
	
	private PicNoticeConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static PicNoticeConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,String> getAllConfig(){
		return CONFIG_MAP;
	}
	
	public String loadByMap(int subId){
		return CONFIG_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" SuoYaoTa 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" SuoYaoTa 2 data is error! ");
			return;
		}
		
		//处理子活动的版本号
		ActivityConfigSon configSon = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSon != null){
			configSon.setClientVersion(configSon.getClientVersion() + 1);
		}
		Map<Integer,String> CONFIG_MAP = new HashMap<>(this.CONFIG_MAP);
		for(int i=0;i<json.size();i++){  
			JSONArray json1 = json.getJSONArray(i);
			Map<String,Object> map = new HashMap<String, Object>();
			for (int j = 0; j < json1.size(); j++) {
				@SuppressWarnings("unchecked")
				Map<String,Object> aa = (Map<String, Object>)JSONObject.parse(json1.getString(j));
				map.putAll(aa);
			}
			CONFIG_MAP.put(subId, CovertObjectUtil.object2String(map.get("bg")));
		}
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		this.CONFIG_MAP = CONFIG_MAP;
	}
	
}
