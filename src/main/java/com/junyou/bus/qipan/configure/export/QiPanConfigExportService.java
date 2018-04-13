package com.junyou.bus.qipan.configure.export;

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
public class QiPanConfigExportService extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		QiPanConfigGroup cofnig = this.KFPM_MAP.get(subId);
		return cofnig != null ? cofnig.getPic() : null;
	}



	private static final QiPanConfigExportService INSTANCE = new QiPanConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,QiPanConfigGroup> KFPM_MAP = new HashMap<>();
	
	
	private QiPanConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static QiPanConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,QiPanConfigGroup> getAllConfig(){
		return KFPM_MAP;
	}
	
	public QiPanConfig loadByKeyId(int subId,Integer id){
		QiPanConfigGroup config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public QiPanConfigGroup loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" QiPan 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" QiPan 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		QiPanConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" QiPan subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new QiPanConfigGroup();
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
		Map<Integer, QiPanConfig> tmpConfig = new HashMap<Integer, QiPanConfig>();
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				//id为-1处理活动说明和底图处理
				if(id == -1){
					group.setDes(CovertObjectUtil.object2String(map.get("des")));
					group.setPic(CovertObjectUtil.object2String(map.get("bg")));
					group.setXfValue(CovertObjectUtil.object2int(map.get("xfvalue")));
					group.setMaxCount(CovertObjectUtil.object2int(map.get("maxcount")));
				}else{
					QiPanConfig config = createKaiFuChiBangConfig(map);
					tmpConfig.put(config.getId(), config);
				}
			}
		}
		
		group.setConfigMap(tmpConfig);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	
	
	public QiPanConfig createKaiFuChiBangConfig(Map<String, Object> tmp) {
		QiPanConfig config = new QiPanConfig();	
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setMaxCiShu(CovertObjectUtil.object2int(tmp.get("csmax")));
		config.setMaxGe(CovertObjectUtil.object2int(tmp.get("max")));

		//int zhuang = 1;
		Map<Integer, Float> geMap = new HashMap<>();
		List<Object[]> buList = new ArrayList<>();
		
		for (int zhuang = 1; zhuang <= 10; zhuang++) {
			Float l = CovertObjectUtil.object2Float(tmp.get("odds"+zhuang))/100;
			if(l == null || l == 0){
				break;
			}
			geMap.put(zhuang, l);
			buList.add(new Object[]{zhuang,l});
		}
		config.setZhuanMap(geMap);
		config.setClinetZM(buList);
		
		Map<Integer, Object[]> geWeiMap = new HashMap<>();
		List<Object[]> clientGw = new ArrayList<>();
		for (int ge = 1; ge < 100; ge++) {
			int l = CovertObjectUtil.object2int(tmp.get("gezi"+ge));
			String item = CovertObjectUtil.object2String(tmp.get("item"+ge));
			if(l == 0 || item == null || "".equals(item)){
				break;
			}
			geWeiMap.put(l, ConfigAnalysisUtils.getConfigArray(item));
			clientGw.add(new Object[]{l,item});
		}
		config.setClientGw(clientGw);
		config.setGeWeiMap(geWeiMap);
		
		return config;
	}
}
