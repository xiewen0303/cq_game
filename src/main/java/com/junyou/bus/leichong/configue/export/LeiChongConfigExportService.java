package com.junyou.bus.leichong.configue.export;

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
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
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
public class LeiChongConfigExportService extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		LeiChongConfigGroup cofnig = this.KFPM_MAP.get(subId);
		return cofnig != null ? cofnig.getPic() : null;
	}
	
	private static final LeiChongConfigExportService INSTANCE = new LeiChongConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,LeiChongConfigGroup> KFPM_MAP = new HashMap<>();
	
	
	private LeiChongConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static LeiChongConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,LeiChongConfigGroup> getAllConfig(){
		return KFPM_MAP;
	}
	
	public LeiChongConfig loadByKeyId(int subId,Integer id){
		LeiChongConfigGroup config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public LeiChongConfigGroup loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" LeiChong 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" LeiChong 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		LeiChongConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" LeiChong subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new LeiChongConfigGroup();
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
		Map<Integer, LeiChongConfig> tmpConfig = new HashMap<Integer, LeiChongConfig>();
		Map<Integer, Map<String, Integer>> dayMap = new HashMap<>();
		List<Object[]> dayClient = new ArrayList<>();
		Integer day = 0;
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				//id为-1处理活动说明和底图处理
				if(id == -1){
					group.setDes(CovertObjectUtil.object2String(map.get("des")));
					group.setDes2(CovertObjectUtil.object2String(map.get("des2")));
					group.setPic(CovertObjectUtil.object2String(map.get("bg")));
					group.setBcGold(CovertObjectUtil.object2int(map.get("xfvalue")));
					String sevenItem = CovertObjectUtil.object2String(map.get("itema")).replace(GameConstants.CONFIG_SPLIT_CHAR, GameConstants.SQL_WHERE_INT_JOIN);
					group.setSevenItem(sevenItem);
					group.setSeverClient(ConfigAnalysisUtils.getConfigArray(CovertObjectUtil.object2String(map.get("itema"))));
				}else{
					LeiChongConfig config = createKaiFuChiBangConfig(map,day,dayMap,dayClient);
					if(config!= null && config.getXfValue() > 0){
						tmpConfig.put(config.getId(), config);
					}
				}
			}
		}
		group.setDayClient(dayClient.toArray());
		group.setDayMap(dayMap);
		group.setLcday(day);
		group.setConfigMap(tmpConfig);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	
	
	public LeiChongConfig createKaiFuChiBangConfig(Map<String, Object> tmp,int lcDay,Map<Integer, Map<String, Integer>> dayMap,List<Object[]> dayClient) {
		LeiChongConfig config = new LeiChongConfig();	
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setXfValue(CovertObjectUtil.object2int(tmp.get("xfvalue")));
		//config.setCount(CovertObjectUtil.object2int(tmp.get("count")));

		//通用奖励配置
		String tyGoodsItem = CovertObjectUtil.object2String(tmp.get("itema"));
		Map<String, GoodsConfigureVo> jobGoodsMap = ConfigAnalysisUtils.getConfigVoMap(tyGoodsItem);	
		config.setJianLiMapA(jobGoodsMap);
		Object[] tmpArr = ConfigAnalysisUtils.getConfigArray(tyGoodsItem);
		config.setJianLiClientMapA(tmpArr);
		
		Integer day = CovertObjectUtil.object2int(tmp.get("day"));
		if(day > 0){
			if(lcDay < day){
				lcDay = day;
			}
			Map<String, Integer> dayItem = ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("itemb")));
			dayMap.put(day, dayItem);
			dayClient.add(new Object[]{
					day,
					ConfigAnalysisUtils.getConfigArray(CovertObjectUtil.object2String(tmp.get("itemb")))
			});
		}
		return config;
	}
}
