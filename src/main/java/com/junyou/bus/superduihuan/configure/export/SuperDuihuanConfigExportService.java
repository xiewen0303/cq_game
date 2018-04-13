package com.junyou.bus.superduihuan.configure.export;

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
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

@Service
public class SuperDuihuanConfigExportService  extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		SuperDuihuanGroupConfig subCfg = KFPM_MAP.get(subId);
		return subCfg != null ? subCfg.getBg() : null;
	}
	
	private static final SuperDuihuanConfigExportService INSTANCE = new SuperDuihuanConfigExportService();

	/**
	 * key:子活动id value:子活动数据
	 */
	private Map<Integer, SuperDuihuanGroupConfig> KFPM_MAP = new HashMap<>();

	private SuperDuihuanConfigExportService() {
	}

	/**
	 * 获取对象实例
	 * 
	 * @return
	 */
	public static SuperDuihuanConfigExportService getInstance() {
		return INSTANCE;
	}

	public Map<Integer, SuperDuihuanGroupConfig> getAllConfig() {
		return KFPM_MAP;
	}

	public SuperDuihuanConfig loadByKeyId(int subId, Integer id) {
		SuperDuihuanGroupConfig config = KFPM_MAP.get(subId);
		if (config != null) {
			return config.getConfigMap().get(id);
		}
		return null;
	}

	public SuperDuihuanGroupConfig loadByMap(int subId) {
		return KFPM_MAP.get(subId);
	}

	@Override
	public void analysisConfigureDataResolve(int subId, byte[] data) {

		if (data == null) {
			ChuanQiLog.error(" SuperDuihuan 1 data is error! ");
		}

		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if (json == null || json.isEmpty()) {
			ChuanQiLog.error(" SuperDuihuan 2 data is error! ");
			return;
		}
		Map<Integer, SuperDuihuanGroupConfig> tmpMap = new HashMap<>(KFPM_MAP);
		// 版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		SuperDuihuanGroupConfig group = tmpMap.get(subId);
		if (group != null) {

			if (md5Value.equals(group.getMd5Version())) {
				// 版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(
						" SuperDuihuan subid={} version is same md5Value={}",
						subId, md5Value);
				return;
			}
		}
		group = new SuperDuihuanGroupConfig();
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
		Map<Integer, SuperDuihuanConfig> tmpConfig = new HashMap<Integer, SuperDuihuanConfig>();
		List<Integer> idList = new ArrayList<Integer>();
		for (Map<String, Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				// id为-1处理活动说明和底图处理
				if (id == -1) {
					group.setBg(CovertObjectUtil.object2String(map.get("bg")));
					group.setDesc(CovertObjectUtil.object2String(map
							.get("desc")));
				} else {
					idList.add(id);
					SuperDuihuanConfig config = createSuperDuihuanConfig(map);
					tmpConfig.put(config.getId(), config);
				}
			}
		}

		// 设置数据
		group.setConfigMap(tmpConfig);
		Collections.sort(idList);
		setVo(idList, group);
		// 最终根据子活动ID记录，当前子活动ID的首充活动数据
		tmpMap.put(subId, group);
		this.KFPM_MAP = tmpMap;
	}

	private void setVo(List<Integer> idList, SuperDuihuanGroupConfig group) {
		Map<Integer, SuperDuihuanConfig> configs = group.getConfigMap();
		Object[] vo = new Object[idList.size()];
		for (int i = 0; i < idList.size(); i++) {
			int id = idList.get(i);
			SuperDuihuanConfig config = configs.get(id);
			Object[] info = new Object[4];
			info[0] = id;
			info[1] = config.getCount();

			Object[] item1Obj = new Object[3];
			GoodsConfigureVo item1 = config.getItem1();
			item1Obj[0] = item1.getGoodsId();
			item1Obj[1] = item1.getGoodsCount();
			item1Obj[2] = item1.getQhLevel();

			Object[] item2Obj = new Object[3];
			GoodsConfigureVo item2 = config.getItem2();
			item2Obj[0] = item2.getGoodsId();
			item2Obj[1] = item2.getGoodsCount();
			item2Obj[2] = item2.getQhLevel();

			info[2] = new Object[] { item1Obj, item2Obj };

			Object[] item3Obj = new Object[3];
			GoodsConfigureVo item3 = config.getItem3();
			item3Obj[0] = item3.getGoodsId();
			item3Obj[1] = item3.getGoodsCount();
			item3Obj[2] = item3.getQhLevel();

			info[3] = new Object[] { item3Obj };

			vo[i] = info;
		}
		group.setVo(vo);
	}

	public SuperDuihuanConfig createSuperDuihuanConfig(Map<String, Object> tmp) {
		SuperDuihuanConfig config = new SuperDuihuanConfig();

		config.setId(CovertObjectUtil.object2int(tmp.get("id")));

		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));

		String item1 = CovertObjectUtil.object2String(tmp.get("item1"));
		Map<String, GoodsConfigureVo> needItem1 = ConfigAnalysisUtils
				.getConfigVoMap(item1);
		for (GoodsConfigureVo e : needItem1.values()) {
			config.setItem1(e);
			break;
		}

		String item2 = CovertObjectUtil.object2String(tmp.get("item2"));
		Map<String, GoodsConfigureVo> needItem2 = ConfigAnalysisUtils
				.getConfigVoMap(item2);
		for (GoodsConfigureVo e : needItem2.values()) {
			config.setItem2(e);
			break;
		}

		String item3 = CovertObjectUtil.object2String(tmp.get("item3"));
		Map<String, GoodsConfigureVo> needItem3 = ConfigAnalysisUtils
				.getConfigVoMap(item3);
		for (GoodsConfigureVo e : needItem3.values()) {
			config.setItem3(e);
			break;
		}

		return config;
	}
}
