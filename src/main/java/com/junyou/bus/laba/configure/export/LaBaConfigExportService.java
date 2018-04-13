package com.junyou.bus.laba.configure.export;

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
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 拉霸
 */
@Service
public class LaBaConfigExportService  extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		LaBaConfigGroup subCfg = KFPM_MAP.get(subId);
		return subCfg != null ? subCfg.getBg() : null;
	}

	private static final LaBaConfigExportService INSTANCE = new LaBaConfigExportService();

	/**
	 * key:子活动id value:子活动数据
	 */
	private Map<Integer, LaBaConfigGroup> KFPM_MAP = new HashMap<>();

	private LaBaConfigExportService() {
	}

	/**
	 * 获取对象实例
	 * 
	 * @return
	 */
	public static LaBaConfigExportService getInstance() {
		return INSTANCE;
	}

	public Map<Integer, LaBaConfigGroup> getAllConfig() {
		return KFPM_MAP;
	}

	public LaBaConfigGroup loadByMap(int subId) {
		return KFPM_MAP.get(subId);
	}

	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId, byte[] data) {
		if (data == null) {
			ChuanQiLog.error(" LaBa 1 data is error! ");
		}

		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if (json == null || json.isEmpty()) {
			ChuanQiLog.error(" LaBa 2 data is error! ");
			return;
		}

		// 版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		LaBaConfigGroup group = KFPM_MAP.get(subId);
		if (group != null) {
			if (md5Value.equals(group.getMd5Version())) {
				// 版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" LaBa subid={} version is same md5Value={}", subId, md5Value);
				return;
			}
		}
		group = new LaBaConfigGroup();
		group.setMd5Version(md5Value);
		
		// 处理子活动的版本号
		ActivityConfigSon configSon = ActivityAnalysisManager.getInstance().loadByZiId(subId);
//		if(configSon==null || !configSon.isRunActivity()){
//			return;//过期活动不管
//		}
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
		
		int beginIndex = 0;
		for (Map<String, Object> map : list) {
			if (null != map) {
				int id = CovertObjectUtil.object2int(map.get("id"));
				if(id == -1){
					group.setBg(CovertObjectUtil.object2String(map.get("bg")));
					group.setDesc(CovertObjectUtil.object2String(map.get("des")));
					group.setRecordCount(CovertObjectUtil.object2int(map.get("display")));
				}else if (id > 0){
					LaBaConfig laBaConfig = createLaBaConfig(map);
					laBaConfig.setBeginIndex(beginIndex);
					beginIndex = beginIndex + laBaConfig.getCount();
					laBaConfig.setEndIndex(beginIndex+1);
					group.putIntoMap(id,laBaConfig);
					if(id > group.getMaxId()){
						group.setMaxId(id);
					}
				}
			}
		}
		KFPM_MAP.put(subId, group);
	}
	private LaBaConfig createLaBaConfig(Map<String, Object> tmp){
		LaBaConfig  laBaConfig  = new LaBaConfig();
		laBaConfig.setCount(CovertObjectUtil.object2int(tmp.get("cishu")));
		
		
		laBaConfig.setChargenum(CovertObjectUtil.object2int(tmp.get("chargenum")));
		laBaConfig.setId(CovertObjectUtil.object2int(tmp.get("id")));
		laBaConfig.setNeedGold(CovertObjectUtil.object2int(tmp.get("needgold")));
		laBaConfig.setVipLevel(CovertObjectUtil.object2int(tmp.get("viplvl")));
		laBaConfig.setMingain(CovertObjectUtil.object2int(tmp.get("mingain")));
		
		List<LaBaInfo> laBaInfos = new ArrayList<>();
		for(int i = 1;i <= 10 ; i++){
			LaBaInfo laBaInfo = new LaBaInfo();
			Object rangemax = CovertObjectUtil.obj2StrOrNull(tmp.get("rangemax"+i));
			if(rangemax == null){
				continue;
			}
			laBaInfo.setRangemax(CovertObjectUtil.object2int(tmp.get("rangemax"+i)));
			laBaInfo.setRangemin(CovertObjectUtil.object2int(tmp.get("rangemin"+i)));
			laBaInfo.setWeight(CovertObjectUtil.object2int(tmp.get("odds"+i)));
			laBaInfos.add(laBaInfo);
		}
		laBaConfig.setLaBaInfo(laBaInfos);
		return laBaConfig;
	}
	 
}
