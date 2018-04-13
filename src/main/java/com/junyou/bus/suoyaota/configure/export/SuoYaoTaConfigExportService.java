package com.junyou.bus.suoyaota.configure.export;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.rfbactivity.configure.export.AbstractRfbConfigService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.bus.shouchong.configure.ShouChongGroupConfig;
import com.junyou.bus.suoyaota.entity.SuoYaoTaCengConfig;
import com.junyou.bus.suoyaota.entity.SuoYaoTaConfig;
import com.junyou.bus.suoyaota.entity.SuoYaoTaSlotConfig;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 锁妖塔
 * @author LiuYu
 * @date 2015-7-29 下午2:54:10
 */
public class SuoYaoTaConfigExportService extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		SuoYaoTaConfig subCfg = CONFIG_MAP.get(subId);
		return subCfg != null ? subCfg.getBg() : null;
	}
	
	private static final SuoYaoTaConfigExportService INSTANCE = new SuoYaoTaConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,SuoYaoTaConfig> CONFIG_MAP = new HashMap<>();
	
	
	private SuoYaoTaConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static SuoYaoTaConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,SuoYaoTaConfig> getAllConfig(){
		return CONFIG_MAP;
	}
	
	public SuoYaoTaConfig loadByMap(int subId){
		return CONFIG_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" SuoYaoTa 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" SuoYaoTa 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		SuoYaoTaConfig group = CONFIG_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getVersion())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" SuoYaoTa subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		Map<Integer,SuoYaoTaConfig> CONFIG_MAP = new HashMap<>(this.CONFIG_MAP);
		group = new SuoYaoTaConfig();
		group.setVersion(md5Value);
		group.setId(subId);
		
		//处理子活动的版本号
		ActivityConfigSon configSon = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSon != null){
			configSon.setClientVersion(configSon.getClientVersion() + 1);
		}
		Map<Integer,SuoYaoTaCengConfig> cengConfigs = new HashMap<>();
		Map<Integer,SuoYaoTaSlotConfig> slotConfigs = new HashMap<>();
		Map<Integer,Integer> slotOdds = new HashMap<>();
		int index = 0;
		int ceng = 1;
		int slotAllOdd = 0;
		SuoYaoTaCengConfig suoYaoTaCengConfig = new SuoYaoTaCengConfig();
		cengConfigs.put(ceng, suoYaoTaCengConfig);
		Map<Integer,Integer> maxLuckyMap = new HashMap<>();
		for(int i=0;i<json.size();i++){  
			JSONArray json1 = json.getJSONArray(i);
			Map<String,Object> map = new HashMap<String, Object>();
			for (int j = 0; j < json1.size(); j++) {
				@SuppressWarnings("unchecked")
				Map<String,Object> aa = (Map<String, Object>)JSONObject.parse(json1.getString(j));
				map.putAll(aa);
			}
			int id = CovertObjectUtil.object2int(map.get("id"));
			if(id > 0){
				SuoYaoTaSlotConfig slotConfig = createSuoYaoTaSlotConfig(map);
				if(slotConfig.getCeng() > ceng){
					suoYaoTaCengConfig.setSlotAllOdd(slotAllOdd);
					suoYaoTaCengConfig.setSlotInfo(slotConfigs);
					suoYaoTaCengConfig.setSlotOdds(slotOdds);
					Integer maxLucky = maxLuckyMap.get(ceng);
					if(maxLucky == null || maxLucky < 0){
						maxLucky = 0;
					}
					suoYaoTaCengConfig.setMaxLucky(maxLucky);
					
					ceng = slotConfig.getCeng();
					suoYaoTaCengConfig = new SuoYaoTaCengConfig();
					cengConfigs.put(ceng, suoYaoTaCengConfig);
					index = 0;
					slotAllOdd = 0;
					slotOdds = new HashMap<>();
					slotConfigs = new HashMap<>();
				}
				slotConfigs.put(index, slotConfig);
				slotOdds.put(index, slotConfig.getOdd());
				slotAllOdd += slotConfig.getOdd();
				index++;
			}else{
				if(id == -2){
					group.setCost(CovertObjectUtil.object2int(map.get("gold")));
					group.setInfo(CovertObjectUtil.object2String(map.get("shuoming")));
					group.setBg(CovertObjectUtil.object2String(map.get("bg")));
				}else if(id == -8){
					int count = CovertObjectUtil.object2int(map.get("cengshu"));
					if(count == 0){count = 9999;}
					group.setMaxCount(count);
				}else if(id < -2 && id > -15){
					int maxLucky = CovertObjectUtil.object2int(map.get("chanzhufu"));
					if(maxLucky > 0){
						int key = -2 - id;
						maxLuckyMap.put(key, maxLucky);
					}else{
						break;
					}
				}
			}
		}  
		suoYaoTaCengConfig.setSlotAllOdd(slotAllOdd);
		suoYaoTaCengConfig.setSlotInfo(slotConfigs);
		suoYaoTaCengConfig.setSlotOdds(slotOdds);
		Integer maxLucky = maxLuckyMap.get(ceng);
		if(maxLucky == null || maxLucky < 0){
			maxLucky = 0;
		}
		suoYaoTaCengConfig.setMaxLucky(maxLucky);
		group.setCengInfo(cengConfigs);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		CONFIG_MAP.put(subId, group);
		this.CONFIG_MAP = CONFIG_MAP;
	}
	
	public SuoYaoTaSlotConfig createSuoYaoTaSlotConfig(Map<String,Object> map){
		SuoYaoTaSlotConfig config = new SuoYaoTaSlotConfig();
		config.setCeng(CovertObjectUtil.object2int(map.get("cengshu")));
		int allOdds = 0;
		Map<GoodsConfigureVo,Integer> slotItems = new HashMap<>();
		for (int i = 1; i < 100; i++) {
			String goodsStr = CovertObjectUtil.obj2StrOrNull(map.get("item"+i));
			if(goodsStr == null){
				break;
			}
			try {
				Map<String,Integer> goods = ConfigAnalysisUtils.getConfigMap(goodsStr);
				int odds = CovertObjectUtil.object2int(map.get("odds"+i));
				for (Entry<String, Integer> entry : goods.entrySet()) {
					GoodsConfigureVo goodsVo = new GoodsConfigureVo(entry.getKey(), entry.getValue());
					slotItems.put(goodsVo, odds);
				}
				allOdds += odds;
			} catch (Exception e) {
				ChuanQiLog.error("{}",goodsStr);
			}
		}
		config.setItemAllOdd(allOdds);
		config.setItemOdds(slotItems);
		config.setOdd(CovertObjectUtil.object2int(map.get("quanz")));//格位权重
		return config;
	}
}
