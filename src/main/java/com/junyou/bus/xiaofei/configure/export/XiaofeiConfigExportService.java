package com.junyou.bus.xiaofei.configure.export;

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
import com.junyou.bus.smsd.configue.export.ShenMiShangDianConfigGroup;
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
public class XiaofeiConfigExportService extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		XiaofeiConfigGroup config = this.KFPM_MAP.get(subId);
		return config != null ? config.getPic() : null;
	}
	
	private static final XiaofeiConfigExportService INSTANCE = new XiaofeiConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,XiaofeiConfigGroup> KFPM_MAP = new HashMap<>();
	
	
	private XiaofeiConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static XiaofeiConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,XiaofeiConfigGroup> getAllConfig(){
		return KFPM_MAP;
	}
	
	public XiaofeiConfig loadByKeyId(int subId,Integer id){
		XiaofeiConfigGroup config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public XiaofeiConfigGroup loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" Xiaofei 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" Xiaofei 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		XiaofeiConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" Xiaofei subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new XiaofeiConfigGroup();
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
		Map<Integer, XiaofeiConfig> tmpConfig = new HashMap<Integer, XiaofeiConfig>();
		List<Object[]> dataList = new ArrayList<>();
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				//id为-1处理活动说明和底图处理
				if(id == -1){
					group.setDes(CovertObjectUtil.object2String(map.get("shuoming")));
					group.setPic(CovertObjectUtil.object2String(map.get("bg")));
				}else if(id == -2){
					
				}else{
					XiaofeiConfig config = createKaiFuChiBangConfig(map,dataList);
					tmpConfig.put(config.getId(), config);
					//设置最大人数
					if(group.getMaxPeople() < config.getMax()){
						group.setMaxPeople(config.getMax());
					}
				}
			}
		}
		
		group.setDataList(dataList);
		group.setConfigMap(tmpConfig);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	
	public XiaofeiConfig createKaiFuChiBangConfig(Map<String, Object> tmp,List<Object[]> dataList) {
		XiaofeiConfig config = new XiaofeiConfig();	
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));

		config.setMax(CovertObjectUtil.object2int(tmp.get("max")));
		config.setMin(CovertObjectUtil.object2int(tmp.get("min")));
		
		//奖励
		String jiangitem = CovertObjectUtil.object2String(tmp.get("item"));
		if(!CovertObjectUtil.isEmpty(jiangitem)){
			config.setJianLiMap(ConfigAnalysisUtils.getConfigVoMap(jiangitem));
			config.setJianLiClientMap(ConfigAnalysisUtils.getConfigArray(jiangitem));
			String eamilItem = jiangitem.replaceAll(GameConstants.GOODS_CONFIG_SPLIT_CHAR, ",");
			config.setEmailItem(eamilItem);
		}
		
		dataList.add(new Object[]{
						config.getMin(),
						config.getMax(),
						config.getJianLiClientMap()
					});
		
		return config;
	}
}
