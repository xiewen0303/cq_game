package com.junyou.bus.rfbflower.configue.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.daomoshouzha.configure.export.DaoMoShouZhaConfigGroup;
import com.junyou.bus.rfbactivity.configure.export.AbstractRfbConfigService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 热发布鲜花榜
 */
@Service
public class FlowerCharmRankConfigExportService  extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		FlowerCharmRankConfigGroup config = this.KFPM_MAP.get(subId);
		return config != null ? config.getPic() : null;
	}
	private static final FlowerCharmRankConfigExportService INSTANCE = new FlowerCharmRankConfigExportService();

	/**
	 * key:子活动id value:子活动数据
	 */
	private Map<Integer, FlowerCharmRankConfigGroup> KFPM_MAP = new HashMap<>();

	private FlowerCharmRankConfigExportService() {
	}

	/**
	 * 获取对象实例
	 * 
	 * @return
	 */
	public static FlowerCharmRankConfigExportService getInstance() {
		return INSTANCE;
	}

	public Map<Integer, FlowerCharmRankConfigGroup> getAllConfig() {
		return KFPM_MAP;
	}

	/**鲜花的价格**/
	public Integer getFlowerPriceByGoodId(int subId,String goodId){
		goodId = goodId.trim();
		FlowerCharmRankConfigGroup groups = KFPM_MAP.get(subId);
		if(groups!=null &&groups.getItemPrice()!=null &&  groups.getItemPrice().get(goodId)!=null){
			return groups.getItemPrice().get(goodId);
		}
		return null;
	}
	/**排行奖励的数据**/
	public FlowerCharmRankConfig loadByKeyId(int subId, Integer rank) {
		FlowerCharmRankConfigGroup groups = KFPM_MAP.get(subId);
		if (groups != null) {
			for (FlowerCharmRankConfig flowerRank : groups.getConfigs()) {
				if (rank>=flowerRank.getMinRank() && rank<=flowerRank.getMaxRank()) {
					return flowerRank;
				}
			}
		}
		return null;
	}

	public FlowerCharmRankConfigGroup loadConfigBySubId(int subId) {
		return KFPM_MAP.get(subId);
	}

	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId, byte[] data) {
		if (data == null) {
			ChuanQiLog.error(" FlowerCharmRank 1 data is error! ");
		}

		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if (json == null || json.isEmpty()) {
			ChuanQiLog.error(" FlowerCharmRank 2 data is error! ");
			return;
		}

		// 版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		FlowerCharmRankConfigGroup group = KFPM_MAP.get(subId);
		if (group != null) {

			if (md5Value.equals(group.getMd5Version())) {
				// 版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" FlowerCharmRank subid={} version is same md5Value={}", subId, md5Value);
				return;
			}
		}
		group = new FlowerCharmRankConfigGroup();
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
		List<FlowerCharmRankConfig> flowerRanks= new ArrayList<>();
		Map<String, Integer> itemPriceMap  = new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			int id = CovertObjectUtil.object2int(map.get("id"));
			if(id<0){
				if(id==-1){
					//图片资源
					group.setPic(CovertObjectUtil.object2String(map.get("res")));
					group.setDes(CovertObjectUtil.object2String(map.get("tips")));
				}else{
					//道具价格
					itemPriceMap.put(CovertObjectUtil.object2String(map.get("itemid")), CovertObjectUtil.object2int(map.get("needgold")));
				}
			} else{
				//排行的数据
				FlowerCharmRankConfig  flowerCharmRankConfig  = new FlowerCharmRankConfig();
				flowerCharmRankConfig.setConfigId(id);
				flowerCharmRankConfig.setMinRank(CovertObjectUtil.object2int(map.get("min")));
				flowerCharmRankConfig.setMaxRank(CovertObjectUtil.object2int(map.get("max")));
				flowerCharmRankConfig.setRewards(new ReadOnlyMap<>(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(map.get("jiangitem")))));
				if(flowerCharmRankConfig.getMaxRank()>group.getMaxRank()){
					//记录最大排名
					group.setMaxRank(flowerCharmRankConfig.getMaxRank());
				}
				
				flowerRanks.add(flowerCharmRankConfig);
			}
			
		}
		group.setItemPrice(itemPriceMap);
		group.setConfigs(flowerRanks);
		// 最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
}
