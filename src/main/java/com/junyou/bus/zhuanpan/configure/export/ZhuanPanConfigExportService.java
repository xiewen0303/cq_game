package com.junyou.bus.zhuanpan.configure.export;

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
import com.junyou.bus.xiaofei.configure.export.XiaofeiConfigGroup;
import com.junyou.constants.GameConstants;
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
public class ZhuanPanConfigExportService  extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		ZhuanPanConfigGroup config = this.KFPM_MAP.get(subId);
		return config != null ? config.getPic() : null;
	}
	private static final ZhuanPanConfigExportService INSTANCE = new ZhuanPanConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,ZhuanPanConfigGroup> KFPM_MAP = new HashMap<>();
	
	
	private ZhuanPanConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static ZhuanPanConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,ZhuanPanConfigGroup> getAllConfig(){
		return KFPM_MAP;
	}
	
	public ZhuanPanConfig loadByKeyId(int subId,Integer id){
		ZhuanPanConfigGroup config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public ZhuanPanConfigGroup loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" ZhuanPan 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" ZhuanPan 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		ZhuanPanConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" ZhuanPan subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new ZhuanPanConfigGroup();
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
		
		int ge = 0;
		List<Object[]> duiHuanList = new ArrayList<>();
		Map<Integer, Integer> zpMap = new HashMap<>();
		Map<Integer, ZhuanPanConfig> tmpConfig = new HashMap<Integer, ZhuanPanConfig>();
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				//id为-1处理活动说明和底图处理
				if(id == -1){
					//group.setDes(CovertObjectUtil.object2String(map.get("des")));
					group.setPic(CovertObjectUtil.object2String(map.get("item1")));
				}else if(id == -2){
					group.setNeeditem(CovertObjectUtil.object2String(map.get("needitem")));
				}else if(id == -3){
					String gold = CovertObjectUtil.object2String(map.get("gold"));
					String[] str = gold.split(GameConstants.CONFIG_SPLIT_CHAR);
					if(str.length <= 1){
						group.setGold(CovertObjectUtil.object2int(map.get("gold")));
					}else{
						List<Object[]> ybList = new ArrayList<>();
						for (int i = 0; i < str.length; i++) {
							String[] cc = str[i].split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
							ybList.add(cc);
						}
						group.setGoldList(ybList);
					}
				}else if(id == -4){
					int count = CovertObjectUtil.object2int(map.get("type"));
					if(count == 0){count = 9999;}
					group.setMaxCount(count);
				}else{
					ZhuanPanConfig config = createShenMiShangDianConfig(map,duiHuanList,zpMap);
					tmpConfig.put(config.getId(), config);
					if(config.getType().intValue() == 1){
						ge++;
					}
				}
			}
		}
		group.setZpMap(zpMap);
		group.setDuiHuanData(duiHuanList);
		group.setMaxGe(ge);
		group.setConfigMap(tmpConfig);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	
	
	public ZhuanPanConfig createShenMiShangDianConfig(Map<String, Object> tmp,List<Object[]> duiHuanList,Map<Integer, Integer> zpMap) {
		ZhuanPanConfig config = new ZhuanPanConfig();	
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		
		config.setJifen(CovertObjectUtil.object2int(tmp.get("needjifen")));
		
		config.setQuan(CovertObjectUtil.object2int(tmp.get("odds")));
		
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		
		if(config.getType() == 1){
			Map<String, Integer> itemMap = new HashMap<>();
			for (int i = 1; i < 50; i++) {
				String item = CovertObjectUtil.object2String(tmp.get("item"+i));
				if(item != null && !"".equals(item)){
					Integer quan = CovertObjectUtil.object2int(tmp.get("item"+i+"odds"));
					itemMap.put(item, quan);
				}else{
					break;
				}
			}
			config.setItemMap(itemMap);
			
			zpMap.put(config.getId(), config.getQuan());
		}
		if(config.getType() == 2){
			Map<String, Integer> itemMap = new HashMap<>();
			String item = CovertObjectUtil.object2String(tmp.get("item1"));
			if(item != null && !"".equals(item)){
				String[] str = item.split(":");
				
				itemMap.put(str[0], Integer.parseInt(str[1]));
				config.setDuiHuanMap(itemMap);
				config.setGoodId(str[0]);
				config.setGoodCount(Integer.parseInt(str[1]));
				duiHuanList.add(new Object[]{config.getId(),str[0],str[1],config.getJifen()});
			}
		}	
		
		
		
		return config;
	}
	
	
}
