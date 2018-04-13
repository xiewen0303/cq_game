package com.junyou.bus.huiyanshijin.configue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuYaoShenMoYinGroupConfig;
import com.junyou.bus.rfbactivity.configure.export.AbstractRfbConfigService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 老玩家回归
 * @author ZHONGDIAN
 * 2015年5月19日 下午3:07:13
 */
@Service
public class HuiYanShiJingConfigExportService  extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		HuiYanShiJingConfigGroup subCfg = KFPM_MAP.get(subId);
		return subCfg != null ? subCfg.getPic() : null;
	}
	
	private static final HuiYanShiJingConfigExportService INSTANCE = new HuiYanShiJingConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,HuiYanShiJingConfigGroup> KFPM_MAP = new HashMap<>();
	
	
	private HuiYanShiJingConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static HuiYanShiJingConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,HuiYanShiJingConfigGroup> getAllConfig(){
		return KFPM_MAP;
	}
	
	public HuiYanShiJingConfig loadByKeyId(int subId,Integer id){
		HuiYanShiJingConfigGroup config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public HuiYanShiJingConfigGroup loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" Huiyanshijin 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" Huiyanshijin 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		HuiYanShiJingConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" Huiyanshijin subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new HuiYanShiJingConfigGroup();
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
		Map<Integer, HuiYanShiJingConfig> tmpConfig = new HashMap<Integer, HuiYanShiJingConfig>();
		Map<Integer, Integer> goldMap = new HashMap<>();//MAP<挖矿级别，挖矿所需元宝>
		List<Object[]> goldList = new ArrayList<>();//每个级别消耗的元宝，给客服端的
		List<Object[]> iconList = new ArrayList<>();//每个矿石对应的icon，给客服端的
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));
				//id为-1处理活动说明和底图处理
				if(id == -1 || id == -2 || id == -3){
					Integer rank= CovertObjectUtil.object2int(map.get("rank"));
					Integer gold= CovertObjectUtil.object2int(map.get("needgold"));
					goldMap.put(rank, gold);
					goldList.add(new Object[]{rank,gold});
				}else if(id == -4 || id == -5 || id == -6 || id == -7){
					Integer rank= CovertObjectUtil.object2int(map.get("rank"));
					String icon= CovertObjectUtil.object2String(map.get("needgold"));
					iconList.add(new Object[]{rank,icon});
				}else if(id == -8){
					group.setPic(CovertObjectUtil.object2String(map.get("rank")));
				}else if(id == -9){
					group.setDes(CovertObjectUtil.object2String(map.get("rank")));
				}else if(id == -10){
					group.setMaxGold(CovertObjectUtil.object2int(map.get("rank")));
				}else if(id == -11){
					group.setDjCount(CovertObjectUtil.object2int(map.get("rank")));
				}else{
					HuiYanShiJingConfig config = createKaiFuChiBangConfig(map);
					tmpConfig.put(config.getId(), config);
					
				}
			}
		}
		group.setGoldList(goldList);
		group.setGoldMap(goldMap);
		group.setIconList(iconList);
		group.setConfigMap(tmpConfig);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	
	
	public HuiYanShiJingConfig createKaiFuChiBangConfig(Map<String, Object> tmp) {
		HuiYanShiJingConfig config = new HuiYanShiJingConfig();	
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setRank(CovertObjectUtil.object2int(tmp.get("rank")));
		config.setMinCount(CovertObjectUtil.object2int(tmp.get("count1")));
		config.setMaxCount(CovertObjectUtil.object2int(tmp.get("count2")));
		
		Map<Object[], Integer> jlMap = new HashMap<>();
		for (int i = 1; i < 100; i++) {
			String type = CovertObjectUtil.object2String(tmp.get("type"+i));
			if(type != null && !"".equals(type)){
				Integer jianggold = CovertObjectUtil.object2int(tmp.get("jianggold"+i));
				Integer gonggao = CovertObjectUtil.object2int(tmp.get("gonggao"+i));
				Integer gailv = CovertObjectUtil.object2int(tmp.get("gailv"+i));
				
				jlMap.put(new Object[]{type,jianggold,gonggao}, gailv);
				
			}else{
				break;
			}
		}
		config.setJlMap(jlMap);
		
		return config;
	}
}
