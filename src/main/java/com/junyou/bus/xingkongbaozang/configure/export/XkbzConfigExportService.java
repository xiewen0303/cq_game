package com.junyou.bus.xingkongbaozang.configure.export;

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
 * 开服战力比拼
 */
@Service
public class XkbzConfigExportService extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		XkbzConfigGroup cofnig = this.KFPM_MAP.get(subId);
		return cofnig != null ? cofnig.getPic() : null;
	}
	
	private static final XkbzConfigExportService INSTANCE = new XkbzConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,XkbzConfigGroup> KFPM_MAP = new HashMap<>();
	
	
	private XkbzConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static XkbzConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,XkbzConfigGroup> getAllConfig(){
		return KFPM_MAP;
	}
	
	public XkbzConfig loadByKeyId(int subId,Integer id){
		XkbzConfigGroup config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public XkbzConfigGroup loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" xkbz 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" xkbz 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		XkbzConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" xkbz subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new XkbzConfigGroup();
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
		Map<Integer, XkbzConfig> tmpConfig = new HashMap<Integer, XkbzConfig>();
		List<Object> voList = new ArrayList<>();
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));
				//id为-1处理活动说明和底图处理
				if(id == -1){
					group.setDes(CovertObjectUtil.object2String(map.get("des")));
					group.setDes2(CovertObjectUtil.object2String(map.get("des1")));
					group.setPic(CovertObjectUtil.object2String(map.get("bg")));
					group.setGoldJifen(CovertObjectUtil.obj2float(map.get("goldrate")));
					group.setbGoldJifen(CovertObjectUtil.obj2float(map.get("bgoldrate")));
					group.setClean(CovertObjectUtil.object2int(map.get("clean")));
				}else{
					XkbzConfig config = createKaiFuChiBangConfig(map);
					if(config!= null){
						tmpConfig.put(config.getId(), config);
						voList.add(config.getVo());
					}
				}
			}
		}
		group.setConfigVo(voList.toArray());
		group.setConfigMap(tmpConfig);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	
	
	public XkbzConfig createKaiFuChiBangConfig(Map<String, Object> tmp) {
		XkbzConfig config = new XkbzConfig();	
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setJifen(CovertObjectUtil.object2int(tmp.get("consum")));
		
		config.setJianLiClient(CovertObjectUtil.object2String(tmp.get("reward")));
		config.setItemMap(ConfigAnalysisUtils.getConfigVoMap(CovertObjectUtil.object2String(tmp.get("reward"))));
		return config;
	}
}
