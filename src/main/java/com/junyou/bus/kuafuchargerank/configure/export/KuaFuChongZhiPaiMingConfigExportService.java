package com.junyou.bus.kuafuchargerank.configure.export;

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
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.share.StringAppContextShare;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

@Service
public class KuaFuChongZhiPaiMingConfigExportService extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		KuaFuChongZhiPaiMingGroupConfig subCfg = KFPM_MAP.get(subId);
		return subCfg != null ? subCfg.getBg() : null;
	}
	
	private static final KuaFuChongZhiPaiMingConfigExportService INSTANCE = new KuaFuChongZhiPaiMingConfigExportService();

	/**
	 * key:子活动id value:子活动数据
	 */
	private Map<Integer, KuaFuChongZhiPaiMingGroupConfig> KFPM_MAP = new HashMap<>();

	private KuaFuChongZhiPaiMingConfigExportService() {
	}

	/**
	 * 获取对象实例
	 * 
	 * @return
	 */
	public static KuaFuChongZhiPaiMingConfigExportService getInstance() {
		return INSTANCE;
	}

	public Map<Integer, KuaFuChongZhiPaiMingGroupConfig> getAllConfig() {
		return KFPM_MAP;
	}

	public KuaFuChongZhiPaiMingConfig loadByKeyId(int subId, Integer id) {
		KuaFuChongZhiPaiMingGroupConfig config = KFPM_MAP.get(subId);
		if (config != null) {
			return config.getConfigMap().get(id);
		}
		return null;
	}

	public KuaFuChongZhiPaiMingGroupConfig loadByMap(int subId) {
		return KFPM_MAP.get(subId);
	}

	@Override
	public void analysisConfigureDataResolve(int subId, byte[] data) {

		if (data == null) {
			ChuanQiLog.error(" KuaFuChongZhiPaiMing 1 data is error! ");
		}

		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if (json == null || json.isEmpty()) {
			ChuanQiLog.error(" KuaFuChongZhiPaiMing 2 data is error! ");
			return;
		}
		Map<Integer, KuaFuChongZhiPaiMingGroupConfig> tmpMap = new HashMap<>(
				KFPM_MAP);
		// 版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		KuaFuChongZhiPaiMingGroupConfig group = tmpMap.get(subId);
		if (group != null) {

			if (md5Value.equals(group.getMd5Version())) {
				// 版本号一致，不处理下面的业务直接跳出
				ChuanQiLog
						.error(" KuaFuChongZhiPaiMing subid={} version is same md5Value={}",
								subId, md5Value);
				return;
			}
		}
		group = new KuaFuChongZhiPaiMingGroupConfig();
		group.setMd5Version(md5Value);

		// 处理子活动的版本号
		ActivityConfigSon configSon = ActivityAnalysisManager.getInstance()
				.loadByZiId(subId);
		if (configSon != null) {
			configSon.setClientVersion(configSon.getClientVersion() + 1);
		}
		// configSon.isRunActivity();
		// if(configSon.getEndTimeByMillSecond() >
		// GameSystemTime.getSystemMillTime()){
		// openSchedule(subId,
		// configSon.getStartTimeByMillSecond(),configSon.getEndTimeByMillSecond());
		// }
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
		Map<Integer, KuaFuChongZhiPaiMingConfig> tmpConfig = new HashMap<Integer, KuaFuChongZhiPaiMingConfig>();
		List<KuaFuChongZhiPaiMingConfig> configList = new ArrayList<KuaFuChongZhiPaiMingConfig>();
		List<Integer> idList = new ArrayList<Integer>();
		int maxRank = 0;
		for (Map<String, Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				// id为-1处理活动说明和底图处理
				if (id == -1) {
					group.setBg(CovertObjectUtil.object2String(map.get("bg")));
					group.setDesc(CovertObjectUtil.object2String(map
							.get("shuoming")));
					group.setMinCharge(CovertObjectUtil.object2int(map
							.get("min_charge")));
					group.setTop1SpecilLimit(CovertObjectUtil.object2int(map
							.get("top1limit")));
					group.setSpecilDesc(CovertObjectUtil.object2String(map
							.get("specialdes")));
					String specialItem = CovertObjectUtil.object2String(map.get("specialitem"));
					if(!CovertObjectUtil.isEmpty(specialItem)){
						group.setSpecilItemMap(ConfigAnalysisUtils.getConfigVoMap(specialItem));
						group.setClientSpecilItem(ConfigAnalysisUtils.getConfigArray(specialItem));
						
						String[] itemArray = specialItem.split("\\|");
						for (String e : itemArray) {
							String[] obj = e.split(":");
							GoodsConfigureVo vo = new GoodsConfigureVo(obj[0],
									Integer.parseInt(obj[1]));
							group.getSpecilItem().add(vo);
						}
					}
				} else {
					idList.add(id);
					KuaFuChongZhiPaiMingConfig config = createKuaFuChongZhiPaiMingConfig(map);
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

	// private void openSchedule(int subId,long startTime,long endTime){
	// if(ActiveUtil.isKuafuChargeRankActiveShedule()){
	// return;
	// }
	// BusScheduleExportService busScheduleExportService =
	// StringAppContextShare.getSpringContext().getBean(BusScheduleExportService.class);
	// BusTokenRunable runable = new BusTokenRunable(
	// GameConstants.DEFAULT_ROLE_ID,
	// InnerCmdType.KUAFU_CHARGE_RANK_END_SCHEDULE, new
	// Object[]{subId,startTime,endTime});
	// busScheduleExportService.schedule(
	// GameConstants.DEFAULT_ROLE_ID.toString(),
	// GameConstants.KUAFU_CHARGE_RANK, runable,
	// (int)(endTime-GameSystemTime.getSystemMillTime()),
	// TimeUnit.MILLISECONDS);
	// ChuanQiLog.info("跨服充值排名活动结束定时器启动");
	// ActiveUtil.setKuafuChargeRankActiveShedule(true);
	// }

	private void cancelSchedule() {
		BusScheduleExportService busScheduleExportService = StringAppContextShare
				.getSpringContext().getBean(BusScheduleExportService.class);
		busScheduleExportService.cancelSchedule(
				GameConstants.DEFAULT_ROLE_ID.toString(),
				GameConstants.KUAFU_CHARGE_RANK);
	}

	private void setVo(KuaFuChongZhiPaiMingGroupConfig group,
			List<KuaFuChongZhiPaiMingConfig> configList) {
		Object[] vo = new Object[configList.size()];
		for (int i = 0; i < configList.size(); i++) {
			KuaFuChongZhiPaiMingConfig config = configList.get(i);
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

	public KuaFuChongZhiPaiMingConfig createKuaFuChongZhiPaiMingConfig(
			Map<String, Object> tmp) {
		KuaFuChongZhiPaiMingConfig config = new KuaFuChongZhiPaiMingConfig();

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
