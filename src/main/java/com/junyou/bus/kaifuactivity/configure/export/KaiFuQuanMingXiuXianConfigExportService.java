package com.junyou.bus.kaifuactivity.configure.export;

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
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 开服全民修仙
 */
@Service
public class KaiFuQuanMingXiuXianConfigExportService extends AbstractRfbConfigService {
	@Override
	public Object getChildData(int subId) {
		KaiFuQuanMingXiuXianGroupConfig subCfg = KFPM_MAP.get(subId);
		return subCfg != null ? subCfg.getPic() : null;
	}
	
	private static final KaiFuQuanMingXiuXianConfigExportService INSTANCE = new KaiFuQuanMingXiuXianConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,KaiFuQuanMingXiuXianGroupConfig> KFPM_MAP = new HashMap<>();
	
	
	private KaiFuQuanMingXiuXianConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static KaiFuQuanMingXiuXianConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,KaiFuQuanMingXiuXianGroupConfig> getAllConfig(){
		return KFPM_MAP;
	}
	
	public KaiFuQuanMingXiuXianConfig loadByKeyId(int subId,Integer id){
		KaiFuQuanMingXiuXianGroupConfig config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public KaiFuQuanMingXiuXianGroupConfig loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" quanmingxiuxian 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" quanmingxiuxian 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		KaiFuQuanMingXiuXianGroupConfig group = KFPM_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" quanmingxiuxian subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new KaiFuQuanMingXiuXianGroupConfig();
		group.setMd5Version(md5Value);
		
		//处理子活动的版本号
		ActivityConfigSon configSon = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSon != null){
			configSon.setClientVersion(configSon.getClientVersion() + 1);
		}
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();  
		for(int i=0;i<json.size();i++){  
			JSONArray json1 = json.getJSONArray(i);
			Map<String,Object> map = new HashMap<String, Object>();
			for (int j = 0; j < json1.size(); j++) {
				@SuppressWarnings("unchecked")
				Map<String,Object> aa = (Map<String, Object>)JSONObject.parse(json1.getString(j));
				map.putAll(aa);
			}
			list.add(map);
		}  
		//KaiFuPaiMingHuoDongGroupConfig vo = new KaiFuPaiMingHuoDongGroupConfig();
		Map<Integer, KaiFuQuanMingXiuXianConfig> tmpConfig = new HashMap<Integer, KaiFuQuanMingXiuXianConfig>();
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				//id为-1处理活动说明和底图处理
				if(id == -1){
					group.setDes(CovertObjectUtil.object2String(map.get("des")));
				}else{
					KaiFuQuanMingXiuXianConfig config = createKaiFuQuanMingXiuXianConfig(map);
					tmpConfig.put(config.getId(), config);
				}
			}
		}
		group.setConfigMap(tmpConfig);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	public KaiFuQuanMingXiuXianConfig createKaiFuQuanMingXiuXianConfig(Map<String, Object> tmp) {
		KaiFuQuanMingXiuXianConfig config = new KaiFuQuanMingXiuXianConfig();	
		
		config.setValue1(CovertObjectUtil.object2int(tmp.get("level")));
											
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setShuliang(CovertObjectUtil.object2int(tmp.get("quanfu")));
		
		String jiangitem = CovertObjectUtil.object2String(tmp.get("item"));
		if(!CovertObjectUtil.isEmpty(jiangitem)){
			config.setJiangitem(ConfigAnalysisUtils.getConfigMap(jiangitem));
			config.setItemClientMap(ConfigAnalysisUtils.getConfigArray(jiangitem));
		}	
		return config;
	}
	
	public int getGuid() {
		return ReFaBuUtil.RFB_LEVEL_TYPE;
	}

	
}
