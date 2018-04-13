package com.junyou.bus.tanbao.configure;

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
 * 探索宝藏
 * @author ZHONGDIAN
 * 2015年5月19日 下午3:07:13
 */
@Service
public class TanSuoBaoZangConfigExportService  extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		TanSuoBaoZangConfigGroup subCfg = KFPM_MAP.get(subId);
		return subCfg != null ? subCfg.getPic() : null;
	}
	
	private static final TanSuoBaoZangConfigExportService INSTANCE = new TanSuoBaoZangConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,TanSuoBaoZangConfigGroup> KFPM_MAP = new HashMap<>();
	
	
	private TanSuoBaoZangConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static TanSuoBaoZangConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,TanSuoBaoZangConfigGroup> getAllConfig(){
		return KFPM_MAP;
	}
	
	public TanSuoBaoZangConfig loadByKeyId(int subId,Integer id){
		TanSuoBaoZangConfigGroup config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public TanSuoBaoZangConfigGroup loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" TanSuoBaoZang 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" TanSuoBaoZang 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		TanSuoBaoZangConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" TanSuoBaoZang subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new TanSuoBaoZangConfigGroup();
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
		List<Object[]> wangChengList = new ArrayList<>();
		Map<Integer, Object[]> wangChengItem = new HashMap<>();
		List<Object[]> goldList = new ArrayList<>();
		Map<Integer, TanSuoBaoZangConfig> tmpConfig = new HashMap<Integer, TanSuoBaoZangConfig>();
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				//id为-1处理活动说明和底图处理
				if(id == -1){
					wangChengList.add(new Object[]{-1,CovertObjectUtil.object2int(map.get("data1")),CovertObjectUtil.object2String(map.get("data2"))});
					wangChengItem.put(-1, new Object[]{CovertObjectUtil.object2int(map.get("data1")),CovertObjectUtil.object2String(map.get("data2"))});
				}else if(id == -2){
					wangChengList.add(new Object[]{-2,CovertObjectUtil.object2int(map.get("data1")),CovertObjectUtil.object2String(map.get("data2"))});
					wangChengItem.put(-2, new Object[]{CovertObjectUtil.object2int(map.get("data1")),CovertObjectUtil.object2String(map.get("data2"))});
				}else if(id == -3){
					wangChengList.add(new Object[]{-3,CovertObjectUtil.object2int(map.get("data1")),CovertObjectUtil.object2String(map.get("data2"))});
					wangChengItem.put(-3, new Object[]{CovertObjectUtil.object2int(map.get("data1")),CovertObjectUtil.object2String(map.get("data2"))});
				}else if(id == -4){
					group.setOpen(CovertObjectUtil.object2int(map.get("data1")));
					group.setOpenGold(CovertObjectUtil.object2int(map.get("data2")));
				}else if(id == -5){
					group.setPic(CovertObjectUtil.object2String(map.get("data1")));
					group.setDes(CovertObjectUtil.object2String(map.get("data2")));
				}else if(id == -6){
					group.setShowItem(CovertObjectUtil.object2String(map.get("data1")));
				}else if(id == -7){
					int count = CovertObjectUtil.object2int(map.get("data1"));
					if(count == 0){
						count = 9999;
					}
					group.setMaxCount(count);
				}else{
					TanSuoBaoZangConfig config = createShenMiShangDianConfig(map,goldList);
					tmpConfig.put(config.getId(), config);
				}
			}
		}
		group.setConfigMap(tmpConfig);
		group.setWangChengItem(wangChengItem);
		group.setWangChengList(wangChengList);
		group.setGoldList(goldList);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	
	
	public TanSuoBaoZangConfig createShenMiShangDianConfig(Map<String, Object> tmp,List<Object[]> goldList) {
		TanSuoBaoZangConfig config = new TanSuoBaoZangConfig();	
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setJilv(CovertObjectUtil.object2Float(tmp.get("odd")));
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		
		Map<String, Integer> itemMap = new HashMap<>();
		for (int i = 1; i < 201; i++) {
			String item = CovertObjectUtil.object2String(tmp.get("drop"+i));
			if(item != null && !"".equals(item)){
				Integer quan = CovertObjectUtil.object2int(tmp.get("drop"+i+"Odds"));
				itemMap.put(item, quan);
			}else{
				break;
			}
		}
		config.setItemMap(itemMap);
		
		goldList.add(new Object[]{CovertObjectUtil.object2int(tmp.get("id")),CovertObjectUtil.object2String(tmp.get("name")),CovertObjectUtil.object2int(tmp.get("gold"))});
		
		return config;
	}
	
	
}
