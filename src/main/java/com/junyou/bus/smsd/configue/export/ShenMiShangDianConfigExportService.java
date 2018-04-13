package com.junyou.bus.smsd.configue.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuTangBaoGroupConfig;
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
public class ShenMiShangDianConfigExportService extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		ShenMiShangDianConfigGroup config = this.KFPM_MAP.get(subId);
		return config != null ? config.getPic() : null;
	}
	
	private static final ShenMiShangDianConfigExportService INSTANCE = new ShenMiShangDianConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,ShenMiShangDianConfigGroup> KFPM_MAP = new HashMap<>();
	
	
	private ShenMiShangDianConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static ShenMiShangDianConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,ShenMiShangDianConfigGroup> getAllConfig(){
		return KFPM_MAP;
	}
	
	public ShenMiShangDianConfig loadByKeyId(int subId,Integer id){
		ShenMiShangDianConfigGroup config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public ShenMiShangDianConfigGroup loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" ShenMiShangDianConfig 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" ShenMiShangDianConfig 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		ShenMiShangDianConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" ShenMiShangDianConfig subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new ShenMiShangDianConfigGroup();
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
		Map<Integer, ShenMiShangDianConfig> tmpConfig = new HashMap<Integer, ShenMiShangDianConfig>();
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				//id为-1处理活动说明和底图处理
				if(id == -1){
					//group.setDes(CovertObjectUtil.object2String(map.get("des")));
					group.setPic(CovertObjectUtil.object2String(map.get("item")));
				}else if(id == -2){
					group.setSxTime(CovertObjectUtil.object2int(map.get("item")));
				}else if(id == -3){
					group.setSxGold(CovertObjectUtil.object2int(map.get("item")));
				}else if(id == -4){
					group.setXianshi(ConfigAnalysisUtils.getConfigArray(CovertObjectUtil.object2String(map.get("item"))));
				}else{
					ShenMiShangDianConfig config = createShenMiShangDianConfig(map);
					tmpConfig.put(config.getId(), config);
				}
			}
		}
		
		group.setConfigMap(tmpConfig);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	
	
	public ShenMiShangDianConfig createShenMiShangDianConfig(Map<String, Object> tmp) {
		ShenMiShangDianConfig config = new ShenMiShangDianConfig();	
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		
		config.setOld(CovertObjectUtil.object2int(tmp.get("old")));
											
		config.setNow(CovertObjectUtil.object2int(tmp.get("now")));
		
		String item = CovertObjectUtil.object2String(tmp.get("item"));
		if(item != null && !"".equals(item)){
			String[] i = item.split(":");
			config.setItem(i[0]);
			config.setCount(Integer.parseInt(i[1]));
		}
											
		config.setGuangbo(CovertObjectUtil.object2int(tmp.get("guangbo")));
											
		config.setLittleid(CovertObjectUtil.object2int(tmp.get("littleid")));
											
		config.setMoneytype(CovertObjectUtil.object2int(tmp.get("moneytype")));
											
		config.setOdds(CovertObjectUtil.obj2float(tmp.get("odds")));
		
		return config;
	}
	
	public List<ShenMiShangDianConfig> loadByLittleid(final Integer subId,final Integer littleid) {
		
		ShenMiShangDianConfigGroup  configVo = KFPM_MAP.get(subId);
		
		List<ShenMiShangDianConfig> list = new ArrayList<>();
		for (Integer key : configVo.getConfigMap().keySet()) {
			ShenMiShangDianConfig config = configVo.getConfigMap().get(key);
			if(config.getLittleid().intValue() == littleid){
				list.add(config);
			}
		}
		return list;
	}
	
}
