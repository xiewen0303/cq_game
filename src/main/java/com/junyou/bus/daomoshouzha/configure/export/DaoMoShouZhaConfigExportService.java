package com.junyou.bus.daomoshouzha.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuZhanJiaGroupConfig;
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
 * 开服战力比拼
 * @author DaoZheng Yuan
 * 2015年5月19日 下午3:07:13
 */
@Service
public class DaoMoShouZhaConfigExportService  extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		DaoMoShouZhaConfigGroup config = this.KFPM_MAP.get(subId);
		return config != null ? config.getPic() : null;
	}
	
	private static final DaoMoShouZhaConfigExportService INSTANCE = new DaoMoShouZhaConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,DaoMoShouZhaConfigGroup> KFPM_MAP = new HashMap<>();
	
	
	private DaoMoShouZhaConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static DaoMoShouZhaConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,DaoMoShouZhaConfigGroup> getAllConfig(){
		return KFPM_MAP;
	}
	
	public DaoMoShouZhaConfig loadByKeyId(int subId,Integer id){
		DaoMoShouZhaConfigGroup config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public DaoMoShouZhaConfigGroup loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" DaoMoShouZha 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" DaoMoShouZha 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		DaoMoShouZhaConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" DaoMoShouZha subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new DaoMoShouZhaConfigGroup();
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
		Map<Integer, Integer> zpMap = new HashMap<>();
		List<Object[]> showList = new ArrayList<>();
		Map<Integer, DaoMoShouZhaConfig> tmpConfig = new HashMap<Integer, DaoMoShouZhaConfig>();
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				//id为-1处理活动说明和底图处理
				if(id == -1){
					//group.setDes(CovertObjectUtil.object2String(map.get("des")));
					group.setPic(CovertObjectUtil.object2String(map.get("show")));
				}else if(id == -2){
					group.setGold(CovertObjectUtil.object2int(map.get("gold")));
				}else if(id == -3){
					int count = CovertObjectUtil.object2int(map.get("nmae"));
					if(count == 0){
						count = 9999;
					}
					group.setMaxCount(count);
				}else{
					DaoMoShouZhaConfig config = createShenMiShangDianConfig(map,zpMap,showList);
					tmpConfig.put(config.getId(), config);
				}
			}
		}
		group.setShowList(showList);
		group.setZpMap(zpMap);
		group.setConfigMap(tmpConfig);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	
	
	public DaoMoShouZhaConfig createShenMiShangDianConfig(Map<String, Object> tmp,Map<Integer, Integer> zpMap,List<Object[]> showList) {
		DaoMoShouZhaConfig config = new DaoMoShouZhaConfig();	
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setQuan(CovertObjectUtil.object2int(tmp.get("typeodds")));
		
		String show = CovertObjectUtil.object2String(tmp.get("show"));
		String name = CovertObjectUtil.object2String(tmp.get("nmae"));
		if(config.getId() < 50){
			showList.add(new Object[]{name,show});
		}
		
		Map<String, Integer> itemMap = new HashMap<>();
		for (int i = 1; i < 101; i++) {
			String item = CovertObjectUtil.object2String(tmp.get("drop"+i));
			if(item != null && !"".equals(item)){
				Integer quan = CovertObjectUtil.object2int(tmp.get("drop"+i+"odds"));
				itemMap.put(item, quan);
			}else{
				break;
			}
		}
		config.setItemMap(itemMap);
		
		zpMap.put(config.getId(), config.getQuan());
	
		
		return config;
	}
	
	
}
