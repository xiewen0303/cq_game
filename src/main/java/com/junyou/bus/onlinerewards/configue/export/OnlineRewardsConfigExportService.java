package com.junyou.bus.onlinerewards.configue.export;

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
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.bus.rfbflower.configue.export.FlowerCharmRankConfigGroup;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 热发布在线奖励
 */
@Service
public class OnlineRewardsConfigExportService extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		OnlineRewardsConfigGroup config = this.KFPM_MAP.get(subId);
		return config != null ? config.getPic() : null;
	}
	
	private static final OnlineRewardsConfigExportService INSTANCE = new OnlineRewardsConfigExportService();

	/**
	 * key:子活动id value:子活动数据
	 */
	private Map<Integer, OnlineRewardsConfigGroup> KFPM_MAP = new HashMap<>();

	private OnlineRewardsConfigExportService() {
	}

	/**
	 * 获取对象实例
	 * 
	 * @return
	 */
	public static OnlineRewardsConfigExportService getInstance() {
		return INSTANCE;
	}

	public Map<Integer, OnlineRewardsConfigGroup> getAllConfig() {
		return KFPM_MAP;
	}

	public OnlineRewardsConfig loadByKeyId(int subId, Integer id) {
		OnlineRewardsConfigGroup groups = KFPM_MAP.get(subId);
		if (groups != null) {
			for (OnlineRewardsConfig lianChongConfig : groups.getConfigs()) {
				if (id == lianChongConfig.getId()) {
					return lianChongConfig;
				}
			}
		}
		return null;
	}

	public OnlineRewardsConfigGroup loadConfigBySubId(int subId) {
		return KFPM_MAP.get(subId);
	}

	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId, byte[] data) {
		if (data == null) {
			ChuanQiLog.error(" onlineRewards 1 data is error! ");
		}

		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if (json == null || json.isEmpty()) {
			ChuanQiLog.error(" onlineRewards 2 data is error! ");
			return;
		}

		// 版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		OnlineRewardsConfigGroup group = KFPM_MAP.get(subId);
		if (group != null) {

			if (md5Value.equals(group.getMd5Version())) {
				// 版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" onlineRewards subid={} version is same md5Value={}", subId, md5Value);
				return;
			}
		}
		group = new OnlineRewardsConfigGroup();
		group.setMd5Version(md5Value);

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
		List<OnlineRewardsConfig> rewardsConfigs= new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			OnlineRewardsConfig config = createLianChongConfig(map);
			if (config.getId().intValue()==-1) {
				group.setPic(config.getPic());
				group.setDes(config.getDesc());
			}else{
				rewardsConfigs.add(config);
			}
			
		}
		group.setConfigs(rewardsConfigs);
		// 最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	 
	public OnlineRewardsConfig createLianChongConfig(Map<String, Object> tmp) {
		OnlineRewardsConfig  config  = new OnlineRewardsConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		if(config.getId().intValue()==-1){
			config.setPic(CovertObjectUtil.object2String(tmp.get("item")));
			config.setDesc(CovertObjectUtil.object2String(tmp.get("des")));
		}else{
			config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
			config.setRewards(new ReadOnlyMap<>(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("item")))));	
		}
		
		return config;
	}
}
