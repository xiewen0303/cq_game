package com.junyou.bus.leihao.configure.export;

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

/**
 * 累计消费
 */
@Service
public class LeiHaoConfigExportService extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		LeiHaoGroupConfig subCfg = KFPM_MAP.get(subId);
		return subCfg != null ? subCfg.getBg() : null;
	}
	
	private static final LeiHaoConfigExportService INSTANCE = new LeiHaoConfigExportService();

	/**
	 * key:子活动id value:子活动数据
	 */
	private Map<Integer, LeiHaoGroupConfig> KFPM_MAP = new HashMap<>();

	private LeiHaoConfigExportService() {
	}

	/**
	 * 获取对象实例
	 * 
	 * @return
	 */
	public static LeiHaoConfigExportService getInstance() {
		return INSTANCE;
	}

	public Map<Integer, LeiHaoGroupConfig> getAllConfig() {
		return KFPM_MAP;
	}

	public LeiHaoConfig loadByKeyId(int subId, Integer id) {
		LeiHaoGroupConfig config = KFPM_MAP.get(subId);
		if (config != null) {
			return config.getConfigMap().get(id);
		}
		return null;
	}
	public LeiHaoBGoldConfig loadByKeyDayId(int subId, Integer id) {
		LeiHaoGroupConfig config = KFPM_MAP.get(subId);
		if (config != null) {
			return config.getbGoldConfigMap().get(id);
		}
		return null;
	}

	public LeiHaoGroupConfig loadByMap(int subId) {
		return KFPM_MAP.get(subId);
	}

	@Override
	public void analysisConfigureDataResolve(int subId, byte[] data) {

		if (data == null) {
			ChuanQiLog.error(" LeiHao 1 data is error! ");
		}

		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if (json == null || json.isEmpty()) {
			ChuanQiLog.error(" LeiHao 2 data is error! ");
			return;
		}
		Map<Integer, LeiHaoGroupConfig> tmpMap = new HashMap<>(KFPM_MAP);
		// 版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		LeiHaoGroupConfig group = tmpMap.get(subId);
		if (group != null) {

			if (md5Value.equals(group.getMd5Version())) {
				// 版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(
						" LeiHao subid={} version is same md5Value={}",
						subId, md5Value);
				return;
			}
		}
		group = new LeiHaoGroupConfig();
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
		Map<Integer, LeiHaoConfig> tmpConfig = new HashMap<Integer, LeiHaoConfig>();
		Map<Integer, LeiHaoBGoldConfig> bGoldConfigMap = new HashMap<Integer, LeiHaoBGoldConfig>();
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
					LeiHaoConfig config = createLeiHaoConfig(map);
					if(config != null){
						tmpConfig.put(config.getId(), config);
					}
					LeiHaoBGoldConfig bGoldConfig = createLeiHaoBGoldConfig(map);
					if(bGoldConfig!= null){
						bGoldConfigMap.put(bGoldConfig.getId(), bGoldConfig);
					}
				}
			}
		}

		// 设置数据
		group.setConfigMap(tmpConfig);
		group.setbGoldConfigMap(bGoldConfigMap);
		Collections.sort(idList);
		setVo(idList, group);
		setDayVo(idList, group);
		// 最终根据子活动ID记录，当前子活动ID的首充活动数据
		tmpMap.put(subId, group);
		this.KFPM_MAP = tmpMap;
	}

	private void setVo(List<Integer> idList, LeiHaoGroupConfig group) {
		Object[] vo = new Object[idList.size()];
		Map<Integer, LeiHaoConfig> configMap = group.getConfigMap();
		for(int i=0;i<idList.size();i++){
			Integer id = idList.get(i);
			LeiHaoConfig config = configMap.get(id);
			if(config != null){
				vo[i] = new Object[]{id,config.getXfvalue(),config.getCount(),config.getItemStr()};
			}
		}
		group.setVo(vo);
	}
	private void setDayVo(List<Integer> idList, LeiHaoGroupConfig group) {
		Object[] vo = new Object[idList.size()];
		Map<Integer, LeiHaoBGoldConfig> configMap = group.getbGoldConfigMap();
		for(int i=0;i<idList.size();i++){
			Integer id = idList.get(i);
			LeiHaoBGoldConfig config = configMap.get(id);
			if(config != null){
				vo[i] = new Object[]{id,config.getXfvalue(),config.getBgold(),config.getFhb()};
			}
		}
		group.setDayVo(vo);
	}

	public LeiHaoConfig createLeiHaoConfig(Map<String, Object> tmp) {
		LeiHaoConfig config = new LeiHaoConfig();
		int id = CovertObjectUtil.object2int(tmp.get("id"));
		if(id <= 0){
			return null;
		}
		config.setId(id);
		
		config.setXfvalue(CovertObjectUtil.object2int(tmp.get("xfvalue")));
		
		String itema = CovertObjectUtil.object2String(tmp.get("itema"));
		config.setItemStr(itema);
		
		Map<String, GoodsConfigureVo> item = ConfigAnalysisUtils.getConfigVoMap(itema);
		config.setItemMap(item);
		return config;
	}
	
	public LeiHaoBGoldConfig createLeiHaoBGoldConfig(Map<String, Object> tmp) {
		LeiHaoBGoldConfig config = new LeiHaoBGoldConfig();
		
		int id = CovertObjectUtil.object2int(tmp.get("id"));
		if(id <= 0){
			return null;
		}
		config.setId(id);
		
		config.setXfvalue(CovertObjectUtil.object2int(tmp.get("xfvalue2")));
		
		config.setBgold(CovertObjectUtil.object2int(tmp.get("itemb")));
		
		config.setFhb(CovertObjectUtil.object2int(tmp.get("fhb")));
		
		return config;
	}
}
