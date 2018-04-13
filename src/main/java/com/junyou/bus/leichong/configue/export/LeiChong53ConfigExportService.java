package com.junyou.bus.leichong.configue.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

@Service
public class LeiChong53ConfigExportService extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		LeiChong53ConfigGroup cofnig = this.KFPM_MAP.get(subId);
		return cofnig != null ? cofnig.getPic() : null;
	}
	
	private static final LeiChong53ConfigExportService INSTANCE = new LeiChong53ConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,LeiChong53ConfigGroup> KFPM_MAP = new HashMap<>();
	
	
	private LeiChong53ConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static LeiChong53ConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,LeiChong53ConfigGroup> getAllConfig(){
		return KFPM_MAP;
	}
	
	public LeiChong53Config loadByKeyId(int subId,Integer id){
		LeiChong53ConfigGroup config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public LeiChong53ConfigGroup loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" LeiChong 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" LeiChong 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		LeiChong53ConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" LeiChong subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new LeiChong53ConfigGroup();
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
		Map<Integer, LeiChong53Config> tmpConfig = new LinkedHashMap<>();
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				//id为-1处理活动说明和底图处理
				if(id == -1){
					group.setDes(CovertObjectUtil.object2String(map.get("des")));
					group.setPic(CovertObjectUtil.object2String(map.get("bg")));
					group.setMax(CovertObjectUtil.object2int(map.get("max")));
				}else{
					LeiChong53Config config = createConfig(map);
					tmpConfig.put(config.getId(), config);
				}
			}
		}
		group.setConfigMap(tmpConfig);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	
	
	public LeiChong53Config createConfig(Map<String, Object> tmp) {
		LeiChong53Config config = new LeiChong53Config();	
		int id = CovertObjectUtil.object2int(tmp.get("id"));
		config.setId(id);
		
		if(id>0 && id < 20){
			config.setItemcharge(CovertObjectUtil.object2int(tmp.get("itemcharge")));
			//通用奖励配置
			Map<String, Integer> goodsMap = CovertObjectUtil.object2Map(tmp.get("itemreward"));	
			config.setItemreward(goodsMap);	
		}else if(id >=20  && id < 30){
			config.setReturncharge(CovertObjectUtil.object2int(tmp.get("returncharge")));
			config.setReturnpercent(CovertObjectUtil.object2int(tmp.get("returnpercent")));
			config.setReturngold(CovertObjectUtil.object2int(tmp.get("returngold")));
		}
		return config;
	}
}
