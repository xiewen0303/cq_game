package com.junyou.bus.tuangou.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.rfbactivity.configure.export.AbstractRfbConfigService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.bus.tanbao.configure.TanSuoBaoZangConfigGroup;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 修仙礼包
 * @author DaoZheng Yuan
 * 2015年6月7日 下午2:29:11
 */
public class RfbTuangouConfigExportService  extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		TuanGouGroupConfig subCfg = TUANGOU.get(subId);
		return subCfg != null ? subCfg.getPic() : null;
	}
	
	private static final RfbTuangouConfigExportService INSTANCE = new RfbTuangouConfigExportService();
	
	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,TuanGouGroupConfig> TUANGOU = new HashMap<>();
	
	
	/**
	 * 私有构造方法(不允许外面创建实例)
	 */
	private RfbTuangouConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static RfbTuangouConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,TuanGouGroupConfig> loadAll(){
		return TUANGOU;
	}
	
	
	public TuanGouGroupConfig loadById(int subId){
		return TUANGOU.get(subId);
	}
	
	@Override
	public void analysisConfigureDataResolve(int subId, byte[] data) {
		if(data == null){
			ChuanQiLog.error(" tuangou 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" tuangou 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		TuanGouGroupConfig group = TUANGOU.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" ShouChong subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		
		TuanGouGroupConfig newGroup = new TuanGouGroupConfig();
		newGroup.setMd5Version(md5Value);
		
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
				Map<String,Object> tmpObjMap = (Map<String, Object>)JSONObject.parse(json1.getString(j));
				map.putAll(tmpObjMap);
			}
			list.add(map);
		}  
		
		for (Map<String,Object> map : list) {
			if (null != map) {
				newGroup.setName(CovertObjectUtil.object2String(map.get("name")));
				newGroup.setDes(CovertObjectUtil.object2String(map.get("des")));
				newGroup.setTips(CovertObjectUtil.object2String(map.get("tips")));
				newGroup.setPic(CovertObjectUtil.object2String(map.get("pic")));
				newGroup.setJiangitem(CovertObjectUtil.object2String(map.get("jiangitem")));
				String jt = CovertObjectUtil.object2String(map.get("jiangitem"));
				if(jt != null && !"".equals(jt)){
					String[] jj = jt.split(GameConstants.GOODS_CONFIG_SUB_SPLIT_CHAR);
					if(jj.length > 2){
						newGroup.setJianLiClientMap(null);
					}else{
						newGroup.setJianLiClientMap(new Object[]{jj[0],jj[1]});
					}
				}
				newGroup.setPrice(CovertObjectUtil.object2int(map.get("price")));
				newGroup.setJiangitem1(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(map.get("jiangitem1"))));
				newGroup.setJianLiClientMap1(ConfigAnalysisUtils.getConfigArray(CovertObjectUtil.object2String(map.get("jiangitem1"))));
				newGroup.setPrice1(CovertObjectUtil.object2int(map.get("price1")));
				newGroup.setPrice2(CovertObjectUtil.object2int(map.get("price2")));
				newGroup.setCount(CovertObjectUtil.object2int(map.get("count")));
				newGroup.setTime1(CovertObjectUtil.object2String(map.get("time1")));
				newGroup.setTime2(CovertObjectUtil.object2String(map.get("time2")));
				newGroup.setTime3(CovertObjectUtil.object2String(map.get("time3")));
				newGroup.setCishu(CovertObjectUtil.object2int(map.get("cishu")));
			}
		}
		//加入管理
		TUANGOU.put(subId, newGroup);
	}
	
}
