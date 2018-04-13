package com.junyou.bus.extremeRecharge.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.rfbactivity.configure.export.AbstractRfbConfigService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;


@Service
public class RfbExtremeRechargeConfigExportService  extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		RfbExtremeRechargeConfig config = this.activityMap.get(subId);
		return config != null ? config.getRes() : null;
	}
    /** key:子活动id;value:子活动数据 **/
    private Map<Integer, RfbExtremeRechargeConfig> activityMap = new HashMap<>();

    private static final RfbExtremeRechargeConfigExportService INSTANCE = new RfbExtremeRechargeConfigExportService();

    private RfbExtremeRechargeConfigExportService() {}

    public static RfbExtremeRechargeConfigExportService getInstance() {
        return INSTANCE;
    }

    public Map<Integer, RfbExtremeRechargeConfig> getAllConfig() {
        return activityMap;
    }

	public RfbExtremeRechargeConfig loadBySubId(int subId){
		return activityMap.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
    public void analysisConfigureDataResolve(int subId, byte[] data) {
        if (data == null) {
            ChuanQiLog.error(" RfbExtremeRecharge data is null ");
        }

        byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
        JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
        if (json == null || json.isEmpty()) {
            ChuanQiLog.error(" json data to RfbExtremeRecharge is error ");
            return;
        }
        
        RfbExtremeRechargeConfig config = activityMap.get(subId);
        
        // 版本号比对
        String md5Value = Md5Utils.md5Bytes(data);
        if (config != null) {
            if (md5Value.equals(config.getMd5Version())) {
                // 版本号一致，不处理下面的业务直接跳出
                ChuanQiLog.error(" RfbExtremeRecharge subid={} version is same md5Value={}", subId, md5Value);
                return;
            }
        }
        
        config = new RfbExtremeRechargeConfig();
        config.setMd5Version(md5Value);

        // 处理子活动的版本号
        ActivityConfigSon configSon = ActivityAnalysisManager.getInstance().loadByZiId(subId);
        if (configSon != null) {
            configSon.setClientVersion(configSon.getClientVersion() + 1);
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < json.size(); i++) {
            JSONArray json1 = json.getJSONArray(i);
            Map<String, Object> map = new HashMap<String, Object>();
            for (int j = 0; j < json1.size(); j++) {
                @SuppressWarnings("unchecked")
                Map<String, Object> aa = (Map<String, Object>) JSONObject.parse(json1.getString(j));
                map.putAll(aa);
            }
            list.add(map);
        }
        for (Map<String, Object> map : list) {
            if (null != map) {
            	String jiangitem = CovertObjectUtil.object2String(map.get("item"));
            	config.setYb(CovertObjectUtil.object2Integer(map.get("need")));
            	config.setRes(CovertObjectUtil.object2String(map.get("res")));
            	config.setAni(CovertObjectUtil.object2String(map.get("ani")));
            	config.setItem(jiangitem);
            	config.setJiangItem(ConfigAnalysisUtils.getConfigVoMap(jiangitem));
            	config.setZlpus(CovertObjectUtil.object2Long(map.get("zlpus")));
            }
        }
        
        activityMap.put(subId, config);
    }
	
}
