package com.junyou.bus.danfuchargerank.configure.export;

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
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.bus.rfbflower.configue.export.FlowerCharmRankConfigGroup;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

@Service
public class DanFuChongZhiPaiMingConfigExportService extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		DanFuChongZhiPaiMingGroupConfig config = this.KFPM_MAP.get(subId);
		return config != null ? config.getBg() : null;
	}
	
	private static final DanFuChongZhiPaiMingConfigExportService INSTANCE = new DanFuChongZhiPaiMingConfigExportService();

	/**
	 * key:子活动id value:子活动数据
	 */
	private Map<Integer, DanFuChongZhiPaiMingGroupConfig> KFPM_MAP = new HashMap<>();

	private DanFuChongZhiPaiMingConfigExportService() {
	}

	/**
	 * 获取对象实例
	 * 
	 * @return
	 */
	public static DanFuChongZhiPaiMingConfigExportService getInstance() {
		return INSTANCE;
	}

	public Map<Integer, DanFuChongZhiPaiMingGroupConfig> getAllConfig() {
		return KFPM_MAP;
	}

	public DanFuChongZhiPaiMingConfig loadByKeyId(int subId, Integer id) {
		DanFuChongZhiPaiMingGroupConfig config = KFPM_MAP.get(subId);
		if (config != null) {
			return config.getConfigMap().get(id);
		}
		return null;
	}

	public DanFuChongZhiPaiMingGroupConfig loadByMap(int subId) {
		return KFPM_MAP.get(subId);
	}

	@Override
	public void analysisConfigureDataResolve(int subId, byte[] data) {

		if (data == null) {
			ChuanQiLog.error(" danFuChongZhiPaiMing 1 data is error! ");
		}

		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if (json == null || json.isEmpty()) {
			ChuanQiLog.error(" danFuChongZhiPaiMing 2 data is error! ");
			return;
		}
		Map<Integer, DanFuChongZhiPaiMingGroupConfig> tmpMap = new HashMap<>(
				KFPM_MAP);
		// 版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		DanFuChongZhiPaiMingGroupConfig group = tmpMap.get(subId);
		if (group != null) {

			if (md5Value.equals(group.getMd5Version())) {
				// 版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" danFuChongZhiPaiMing subid={} version is same md5Value={}",subId, md5Value);
				return;
			}
		}
		group = new DanFuChongZhiPaiMingGroupConfig();
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
				Map<String, Object> aa = (Map<String, Object>) JSONObject
						.parse(json1.getString(j));
				map.putAll(aa);
			}
			list.add(map);
		}
		Map<Integer, DanFuChongZhiPaiMingConfig> tmpConfig = new HashMap<Integer, DanFuChongZhiPaiMingConfig>();
		List<DanFuChongZhiPaiMingConfig> configList = new ArrayList<DanFuChongZhiPaiMingConfig>();
		List<Integer> idList = new ArrayList<Integer>();
		int maxRank = 0;
		for (Map<String, Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				// id为-1处理活动说明和底图处理
				if (id == -1) {
					group.setBg(CovertObjectUtil.object2String(map.get("bg")));
					group.setMinCharge(CovertObjectUtil.object2int(map.get("min_charge")));
					group.setDesc(CovertObjectUtil.object2String(map.get("shuoming")));
					group.setItemGold(CovertObjectUtil.object2int(map.get("itemgold")));
					group.setExtraRank(CovertObjectUtil.object2int(map.get("rank")));
					group.setGoldItem(CovertObjectUtil.object2String(map.get("item1")));
					group.setItemtx(CovertObjectUtil.object2String(map.get("itemtx")));
					group.setItemms(CovertObjectUtil.object2String(map.get("itemms")));
					group.setTxVo(new Object[]{group.getItemtx(),group.getItemms()});
				} else {
					idList.add(id);
					DanFuChongZhiPaiMingConfig config = createDanFuChongZhiPaiMingConfig(map);
					for (int i = config.getMin(); i <= config.getMax(); i++) {
						tmpConfig.put(i, config);
					}
					if (config.getMax() > maxRank) {
						maxRank = config.getMax();
					}
					configList.add(config);
				}
			}
		}

		group.setMaxRank(maxRank);
		// 设置数据
		group.setConfigMap(tmpConfig);
		Collections.sort(idList);
		setVo(group, configList);
		// 最终根据子活动ID记录，当前子活动ID的首充活动数据
		tmpMap.put(subId, group);
		this.KFPM_MAP = tmpMap;
	}

	private void setVo(DanFuChongZhiPaiMingGroupConfig group,
			List<DanFuChongZhiPaiMingConfig> configList) {
		Object[] vo = new Object[configList.size()];
		for (int i = 0; i < configList.size(); i++) {
			DanFuChongZhiPaiMingConfig config = configList.get(i);
			Object[] info = new Object[4];
			info[0] = config.getId();
			info[1] = config.getMin();
			info[2] = config.getMax();
			List<GoodsConfigureVo> tmpItems = config.getItem();
			Object[] itemArray = new Object[tmpItems.size()];
			int index = 0;
			for (GoodsConfigureVo e : tmpItems) {
				itemArray[index++] = new Object[] { e.getGoodsId(),
						e.getGoodsCount(), e.getQhLevel() };
			}
			info[3] = itemArray;
			vo[i] = info;
		}
		group.setVo(vo);
	}

	public DanFuChongZhiPaiMingConfig createDanFuChongZhiPaiMingConfig(
			Map<String, Object> tmp) {
		DanFuChongZhiPaiMingConfig config = new DanFuChongZhiPaiMingConfig();

		config.setId(CovertObjectUtil.object2int(tmp.get("id")));

		config.setMin(CovertObjectUtil.object2int(tmp.get("min")));
		config.setMax(CovertObjectUtil.object2int(tmp.get("max")));

		String item = CovertObjectUtil.object2String(tmp.get("item"));
		String[] itemStrArray = item.split("\\|");
		for (String e : itemStrArray) {
			String[] obj = e.split(":");
			GoodsConfigureVo vo = new GoodsConfigureVo(obj[0],
					Integer.parseInt(obj[1]));
			config.addItem(vo);
		}
		Map<String, GoodsConfigureVo> itemMap = ConfigAnalysisUtils
				.getConfigVoMap(item);
		config.setItemMap(itemMap);
		return config;
	}
}
