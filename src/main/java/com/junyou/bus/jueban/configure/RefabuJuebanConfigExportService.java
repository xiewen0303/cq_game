/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.jueban.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.huiyanshijin.configue.HuiYanShiJingConfigGroup;
import com.junyou.bus.rfbactivity.configure.export.AbstractRfbConfigService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * @Description 热发布-绝版礼包活动配置解析
 * @Author Yang Gao
 * @Since 2016-8-1
 * @Version 1.1.0
 */
public class RefabuJuebanConfigExportService extends AbstractRfbConfigService {
	
    private static class InnerClassInstance {
        public static final RefabuJuebanConfigExportService instance = new RefabuJuebanConfigExportService();
    }

    private Map<Integer, RefabuJuebanGroupConfig> activityMap;

    private RefabuJuebanConfigExportService() {
    }

    public static RefabuJuebanConfigExportService getInstance() {
        return InnerClassInstance.instance;
    }

    @Override
    public void analysisConfigureDataResolve(int subId, byte[] data) {
        if (data == null) {
            ChuanQiLog.error(" RefabuJueban data is null ");
        }

        byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
        JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
        if (json == null || json.isEmpty()) {
            ChuanQiLog.error(" json data to RefabuJueban is error ");
            return;
        }
        RefabuJuebanGroupConfig configGroup = null;
        if (null == activityMap) {
            activityMap = new HashMap<>();
        } else {
            configGroup = activityMap.get(subId);
        }
        // 版本号比对
        String md5Value = Md5Utils.md5Bytes(data);
        if (configGroup != null && md5Value.equals(configGroup.getMd5Version())) {
            // 版本号一致，不处理下面的业务直接跳出
            ChuanQiLog.error(" RefabuJueban subid={} version is same md5Value={}", subId, md5Value);
            return;
        }

        configGroup = new RefabuJuebanGroupConfig();
        configGroup.setMd5Version(md5Value);

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
        Map<Integer, RefabuJuebanConfig> configMap = new HashMap<>();
        for (Map<String, Object> map : list) {
            if (null != map) {
                RefabuJuebanConfig config = new RefabuJuebanConfig();
                config.setId(CovertObjectUtil.object2int(map.get("id")));
                config.setRes(CovertObjectUtil.object2String(map.get("res")));
                config.setNeedGold(CovertObjectUtil.object2int(map.get("needgold")));
                String itemString = CovertObjectUtil.object2String(map.get("jiangitem"));
                config.setItemString(itemString);
                config.setItemMap(ConfigAnalysisUtils.getConfigMap(itemString));
                configMap.put(config.getId(), config);
            }
        }
        configGroup.setConfigMap(configMap);
        // 最终根据子活动ID记录，当前子活动ID的首充活动数据
        activityMap.put(subId, configGroup);
    }

    public RefabuJuebanGroupConfig loadBySubId(int subId) {
        if(activityMap == null){
            return null;
        }
        return activityMap.get(subId);
    }

    public RefabuJuebanConfig loadBySubAndId(int subId, int id) {
        RefabuJuebanGroupConfig group = loadBySubId(subId);
        if (group == null) {
            return null;
        }
        Map<Integer, RefabuJuebanConfig> configMap = group.getConfigMap();
        if (null == configMap) {
            return null;
        }
        return configMap.get(id);
    }

    public Map<Integer, RefabuJuebanGroupConfig> loadAll() {
        return activityMap;
    }
    
}
