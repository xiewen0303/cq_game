package com.junyou.bus.laowanjia.configue;

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
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
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
public class LaoWanJiaConfigExportService extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		LaoWanJiaConfigGroup subCfg = KFPM_MAP.get(subId);
		return subCfg != null ? subCfg.getPic() : null;
	}
	
	private static final LaoWanJiaConfigExportService INSTANCE = new LaoWanJiaConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,LaoWanJiaConfigGroup> KFPM_MAP = new HashMap<>();
	
	
	private LaoWanJiaConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static LaoWanJiaConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,LaoWanJiaConfigGroup> getAllConfig(){
		return KFPM_MAP;
	}
	
	public LaoWanJiaConfig loadByKeyId(int subId,Integer id){
		LaoWanJiaConfigGroup config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public LaoWanJiaConfigGroup loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" LaoWanJia 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" LaoWanJia 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		LaoWanJiaConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" LaoWanJia subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new LaoWanJiaConfigGroup();
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
		Map<Integer, LaoWanJiaConfig> tmpConfig = new HashMap<Integer, LaoWanJiaConfig>();
		List<Object[]> voList = new ArrayList<>();
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));
				//id为-1处理活动说明和底图处理
				if(id == -1){
					group.setDes(CovertObjectUtil.object2String(map.get("des")));
					group.setPic(CovertObjectUtil.object2String(map.get("bg")));
				}else if(id == -2){
					group.setWeiLogin(CovertObjectUtil.object2int(map.get("day1")));
				}else{
					LaoWanJiaConfig config = createKaiFuChiBangConfig(map);
					tmpConfig.put(config.getId(), config);
					
					voList.add(new Object[]{config.getId(),config.getLoginDay(),config.getReGold(),config.getJianLiClientMapA(),config.getJianLiClientMapB()});
				}
			}
		}
		group.setDengLuVo(voList);
		group.setConfigMap(tmpConfig);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	
	
	public LaoWanJiaConfig createKaiFuChiBangConfig(Map<String, Object> tmp) {
		LaoWanJiaConfig config = new LaoWanJiaConfig();	
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setLoginDay(CovertObjectUtil.object2int(tmp.get("day1")));
		config.setReGold(CovertObjectUtil.object2int(tmp.get("goldb")));

		//奖励配置A
		String itemsNameA = "itema";
		String goodsItemA = CovertObjectUtil.object2String(tmp.get(itemsNameA));
		config.setJianLiMapA(ConfigAnalysisUtils.getConfigVoMap(goodsItemA));
		config.setJianLiClientMapA(ConfigAnalysisUtils.getConfigArray(goodsItemA));
		//奖励配置B
		String itemsNameB = "itemb";
		String goodsItemB = CovertObjectUtil.object2String(tmp.get(itemsNameB));
		config.setJianLiMapB(ConfigAnalysisUtils.getConfigVoMap(goodsItemB));
		config.setJianLiClientMapB(ConfigAnalysisUtils.getConfigArray(goodsItemB));
		
		return config;
	}
}
