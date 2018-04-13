package com.junyou.bus.lianchong.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.onlinerewards.configue.export.OnlineRewardsConfigGroup;
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
 * 连充
 */
@Service
public class LianChongConfigExportService extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		LianChongConfigGroup config = this.KFPM_MAP.get(subId);
		return config != null ? config.getPic() : null;
	}
	private static final LianChongConfigExportService INSTANCE = new LianChongConfigExportService();

	/**
	 * key:子活动id value:子活动数据
	 */
	private Map<Integer, LianChongConfigGroup> KFPM_MAP = new HashMap<>();

	private LianChongConfigExportService() {
	}

	/**
	 * 获取对象实例
	 * 
	 * @return
	 */
	public static LianChongConfigExportService getInstance() {
		return INSTANCE;
	}

	public Map<Integer, LianChongConfigGroup> getAllConfig() {
		return KFPM_MAP;
	}

	public LianChongConfig loadByKeyId(int subId, Integer id) {
		LianChongConfigGroup groups = KFPM_MAP.get(subId);
		if (groups != null) {
			for (LianChongConfig lianChongConfig : groups.getConfigs()) {
				if (id == lianChongConfig.getId()) {
					return lianChongConfig;
				}
			}
		}
		return null;
	}

	public LianChongConfigGroup loadByMap(int subId) {
		return KFPM_MAP.get(subId);
	}

	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId, byte[] data) {
		if (data == null) {
			ChuanQiLog.error(" LianChong 1 data is error! ");
		}

		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if (json == null || json.isEmpty()) {
			ChuanQiLog.error(" LianChong 2 data is error! ");
			return;
		}

		// 版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		LianChongConfigGroup group = KFPM_MAP.get(subId);
		if (group != null) {

			if (md5Value.equals(group.getMd5Version())) {
				// 版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" LianChong subid={} version is same md5Value={}", subId, md5Value);
				return;
			}
		}
		group = new LianChongConfigGroup();
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
		List<LianChongConfig>  tmpConfig  = new ArrayList<>();
		for (Map<String, Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));
				// id为-1处理活动说明和底图处理
				if (id == -1) {
					group.setPic(CovertObjectUtil.object2String(map.get("bg")));
					group.setDes(CovertObjectUtil.object2String(map.get("shuoming")));
				} else {
					LianChongConfig config = createLianChongConfig(map);
					tmpConfig.add(config);
				}
			}
		}
		group.setConfigs(tmpConfig);
		// 最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	 
	public LianChongConfig createLianChongConfig(Map<String, Object> tmp) {
		LianChongConfig  config  = new LianChongConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		int  loop = config.getType()==1?1:3;
		List<LianChongConfig.RewardVo> rewardVos  = new ArrayList<>();
		for (int i = 1; i <=loop; i++) {
			LianChongConfig.RewardVo   rewardVo = config.getInnerRewardVo();
			String item  =  CovertObjectUtil.object2String(tmp.get("item"+i));
			int dayType = CovertObjectUtil.object2int(tmp.get("day"+i));
			Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
			rewardVo.setDayType(dayType);
			rewardVo.setItemMap(itemMap);
			rewardVos.add(rewardVo);
		}
		config.setRewardVos(rewardVos);
		return config;
	}
}
