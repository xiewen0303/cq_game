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
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 开服战力比拼
 * @author DaoZheng Yuan
 * 2015年5月19日 下午3:07:13
 */
@Service
public class KaiFuYuJianFeiXianConfigExportService extends AbstractRfbConfigService{

	private static final KaiFuYuJianFeiXianConfigExportService INSTANCE = new KaiFuYuJianFeiXianConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,KaiFuYuJianFeiXianGroupConfig> KFPM_MAP = new HashMap<>();
	
	
	private KaiFuYuJianFeiXianConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static KaiFuYuJianFeiXianConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,KaiFuYuJianFeiXianGroupConfig> getAllConfig(){
		return KFPM_MAP;
	}
	
	public KaiFuYuJianFeiXianConfig loadByKeyId(int subId,Integer id){
		KaiFuYuJianFeiXianGroupConfig config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public KaiFuYuJianFeiXianGroupConfig loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" KaiFuYuJianFeiXian 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" KaiFuYuJianFeiXian 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		KaiFuYuJianFeiXianGroupConfig group = KFPM_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" KaiFuYuJianFeiXian subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new KaiFuYuJianFeiXianGroupConfig();
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
		Map<Integer, KaiFuYuJianFeiXianConfig> tmpConfig = new HashMap<Integer, KaiFuYuJianFeiXianConfig>();
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				//id为-1处理活动说明和底图处理
				if(id == -1){
					group.setDes(CovertObjectUtil.object2String(map.get("des")));
					group.setPic(CovertObjectUtil.object2String(map.get("item")));
					group.setDes1(CovertObjectUtil.object2String(map.get("des1")));
					
				}else{
					KaiFuYuJianFeiXianConfig config = createKaiFuYuJianFeiXianConfig(map);
					tmpConfig.put(config.getId(), config);
				}
			}
		}
		
		//设置数据
		setData(tmpConfig, group);
		
		group.setConfigMap(tmpConfig);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	private void setData(Map<Integer, KaiFuYuJianFeiXianConfig> configMap,KaiFuYuJianFeiXianGroupConfig group){
	
		if(configMap != null && configMap.size() > 0){
			List<Object[]> jdData = new ArrayList<>();
			List<Object[]> pxData = new ArrayList<>();
			List<Object[]> tzData = new ArrayList<>();
			for (int i = 1; i <= 20; i++) {//阶段奖励（1-20）
				KaiFuYuJianFeiXianConfig zhanliConfig =  configMap.get(i);
				if(zhanliConfig == null){
					break;
				}
				Object[] zlData = new Object[]{
					i,
					zhanliConfig.getDes(),
					zhanliConfig.getItemClientMap(),
					zhanliConfig.getYuJianLevel()
				};
				jdData.add(zlData);
			}
			for (int i = 21; i <= 40; i++) {
				KaiFuYuJianFeiXianConfig zhanliConfig =  configMap.get(i);
				if(zhanliConfig == null){
					break;
				}
				Object[] zlData = new Object[]{
					i,
					zhanliConfig.getDes(),
					zhanliConfig.getItemClientMap()
				};
				pxData.add(zlData);
			}
			for (int i = 41; i <= 60; i++) {
				KaiFuYuJianFeiXianConfig zhanliConfig =  configMap.get(i);
				if(zhanliConfig == null){
					break;
				}
				Object[] zlData = new Object[]{
					zhanliConfig.getShowitem(),
					zhanliConfig.getDes()
				};
				tzData.add(zlData);
			}
			
			group.setJdData(jdData);
			group.setPxData(pxData);
			group.setTzData(tzData);
		}
	}
	
	
	public KaiFuYuJianFeiXianConfig createKaiFuYuJianFeiXianConfig(Map<String, Object> tmp) {
		KaiFuYuJianFeiXianConfig config = new KaiFuYuJianFeiXianConfig();	
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		
		config.setMax(CovertObjectUtil.object2int(tmp.get("max")));
		config.setMin(CovertObjectUtil.object2int(tmp.get("min")));
		config.setYuJianLevel(CovertObjectUtil.object2int(tmp.get("jieduan")));
		config.setShowitem(CovertObjectUtil.object2String(tmp.get("showitem")));
		config.setDes(CovertObjectUtil.object2String(tmp.get("des")));
		
		
		//奖励
		
		String jiangitem = CovertObjectUtil.object2String(tmp.get("item"));
		if(!CovertObjectUtil.isEmpty(jiangitem)){
			config.setJiangitem(ConfigAnalysisUtils.getConfigVoMap(jiangitem));
			config.setItemClientMap(ConfigAnalysisUtils.getConfigArray(jiangitem));
			String eamilItem = jiangitem.replaceAll(GameConstants.GOODS_CONFIG_SPLIT_CHAR, ",");
			config.setEmailItem(eamilItem);
		}
		
		
		
		return config;
	}

	@Override
	public Object getChildData(int subId) {
		KaiFuYuJianFeiXianGroupConfig subcfg = this.KFPM_MAP.get(subId);
		return subcfg != null ? subcfg.getPic() : null;
	}
}
