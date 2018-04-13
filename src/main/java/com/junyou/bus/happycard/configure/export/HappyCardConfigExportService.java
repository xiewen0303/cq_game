package com.junyou.bus.happycard.configure.export;

import java.util.ArrayList;
import java.util.Collections;
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
public class HappyCardConfigExportService  extends AbstractRfbConfigService {
	
	private static final HappyCardConfigExportService INSTANCE = new HappyCardConfigExportService();

	/**
	 * key:子活动id value:子活动数据
	 */
	private Map<Integer, HappyCardGroupConfig> KFPM_MAP = new HashMap<>();

	private HappyCardConfigExportService() {
	}

	/**
	 * 获取对象实例
	 * 
	 * @return
	 */
	public static HappyCardConfigExportService getInstance() {
		return INSTANCE;
	}

	public Map<Integer, HappyCardGroupConfig> getAllConfig() {
		return KFPM_MAP;
	}

	public HappyCardConfig loadByKeyId(int subId, Integer id) {
		HappyCardGroupConfig config = KFPM_MAP.get(subId);
		if (config != null) {
			return config.getConfigMap().get(id);
		}
		return null;
	}

	public HappyCardGroupConfig loadByMap(int subId) {
		return KFPM_MAP.get(subId);
	}

	@Override
	public void analysisConfigureDataResolve(int subId, byte[] data) {

		if (data == null) {
			ChuanQiLog.error(" happy card 1 data is error! ");
		}

		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if (json == null || json.isEmpty()) {
			ChuanQiLog.error("  happy card 2 data is error! ");
			return;
		}
		Map<Integer, HappyCardGroupConfig> tmpMap = new HashMap<>(KFPM_MAP);
		// 版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		HappyCardGroupConfig group = tmpMap.get(subId);
		if (group != null) {

			if (md5Value.equals(group.getMd5Version())) {
				// 版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(
						"  happy card subid={} version is same md5Value={}",
						subId, md5Value);
				return;
			}
		}
		group = new HappyCardGroupConfig();
		group.setMd5Version(md5Value);

		// 处理子活动的版本号
		ActivityConfigSon configSon = ActivityAnalysisManager.getInstance()
				.loadByZiId(subId);
		if (configSon != null) {
			configSon.setClientVersion(configSon.getClientVersion() + 1);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < json.size(); i++) {
			JSONArray json1 = json.getJSONArray(i);
			Map<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < json1.size(); j++) {
				@SuppressWarnings("unchecked")
				Map<String, Object> aa = (Map<String, Object>) JSONObject
						.parse(json1.getString(j));
				map.putAll(aa);
			}
			list.add(map);
		}
		Map<Integer, HappyCardConfig> tmpConfig = new HashMap<Integer, HappyCardConfig>();
		List<Integer> idList = new ArrayList<Integer>();
		for (Map<String, Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				// id为-1处理活动说明和底图处理
				if (id == -1) {
					group.setChongzhi(CovertObjectUtil.object2int(map
							.get("chongzhi")));
					group.setDesc(CovertObjectUtil.object2String(map
							.get("shuoming")));
				} else {
					HappyCardConfig config = createHappyCardConfig(map);
					tmpConfig.put(config.getId(), config);
					idList.add(config.getId());
				}
			}
		}
		Collections.sort(idList);
		group.setIdList(idList);
		// 设置数据
		group.setConfigMap(tmpConfig);
		// 最终根据子活动ID记录，当前子活动ID的首充活动数据
		tmpMap.put(subId, group);
		this.KFPM_MAP = tmpMap;
		Object[] ybArray = new Object[idList.size()];
		for (int i = 0; i < idList.size(); i++) {
			ybArray[i] = group.getConfig(idList.get(i)).getMoney();
		}
		group.setYbArray(ybArray);
	}
	public HappyCardConfig createHappyCardConfig(Map<String, Object> tmp) {
		HappyCardConfig config = new HappyCardConfig();

		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setMoney(CovertObjectUtil.object2int(tmp.get("money")));

		Map<Integer, HappyCardItem> cardItemMap = new HashMap<Integer, HappyCardItem>();
		Map<Integer, Integer> weightMap = new HashMap<Integer, Integer>();
		int totalWeight = 0;
		for (int i = 0; i <= 24; i++) {
			String itemStr = CovertObjectUtil
					.object2String(tmp.get("item" + i));
			if (itemStr == null || "".equals(itemStr.trim())) {
				continue;
			}
			HappyCardItem cardItem = new HappyCardItem();
			cardItem.setIndex(i);
			cardItem.setItemsStr(itemStr);
			if (itemStr.contains(":")) {
				cardItem.setType(HappyCardItem.HAPPY_CARD_ITEM_TYPE_1);
				Map<String, Integer> itemMap = ConfigAnalysisUtils
						.getConfigMap(itemStr);
				cardItem.setItems(itemMap);
			} else {
				cardItem.setType(HappyCardItem.HAPPY_CARD_ITEM_TYPE_2);
				cardItem.setMulti(Integer.parseInt(itemStr));
			}
			int itemodds = CovertObjectUtil.object2int(tmp.get("item" + i
					+ "odds"));
			cardItem.setWeight(itemodds);
			totalWeight = totalWeight + itemodds;
			cardItemMap.put(i, cardItem);
			weightMap.put(i, itemodds);
		}
		config.setWeightMap(weightMap);
		config.setTotalWeight(totalWeight);
		config.setItems(cardItemMap);
		return config;
	}
}
