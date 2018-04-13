package com.junyou.bus.oncechong.configure.export;

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
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 开服单笔充值
 */
@Service
public class OnceChongConfigExportService extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		OnceChongConfigGroup subCfg = KFPM_MAP.get(subId);
		return subCfg != null ? subCfg.getPic() : null;
	}
	
	private static final OnceChongConfigExportService INSTANCE = new OnceChongConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,OnceChongConfigGroup> KFPM_MAP = new HashMap<>();
	
	private OnceChongConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static OnceChongConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,OnceChongConfigGroup> getAllConfig(){
		return KFPM_MAP;
	}
	
	/**
	 * 通过id获得对应的配置表
	 * @param subId
	 * @param id
	 * @return
	 */
	public OnceChongConfig loadByKeyId(int subId,Integer id){
		OnceChongConfigGroup config = KFPM_MAP.get(subId);
		if(config != null){
			return config.getConfigMap().get(id);
		}
		return null;
	}
	
	
	public OnceChongConfigGroup loadByMap(int subId){
		return KFPM_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error("OnceChong 1 data is error! ");
			return;
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error("OnceChong 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		OnceChongConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error("OnceChong subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new OnceChongConfigGroup();
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
		Map<Integer, OnceChongConfig> tmpConfig = new HashMap<Integer, OnceChongConfig>();
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));

				//id为-1处理活动说明和底图处理
				if(id == -1){
					group.setDes(CovertObjectUtil.object2String(map.get("des")));
					group.setPic(CovertObjectUtil.object2String(map.get("bg")));
				}else{
					OnceChongConfig config = createConfig(map);
					tmpConfig.put(config.getId(), config);
				}
			}
		}
		
		group.setConfigMap(tmpConfig);
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	
	
	
	public OnceChongConfig createConfig(Map<String, Object> tmp) {
		OnceChongConfig config = new OnceChongConfig();	
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setBeginValue(CovertObjectUtil.object2int(tmp.get("minvalue")));
		config.setEndValue(CovertObjectUtil.object2int(tmp.get("maxvalue")));
//		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		
		config.setFanhuana(CovertObjectUtil.object2int(tmp.get("fanhuana")));
		config.setFanhuanb(CovertObjectUtil.object2int(tmp.get("fanhuanb")));
		
		String itemsName = "itemb";
		
		//奖励
		Map<Byte,Map<String,GoodsConfigureVo>> jianLiMap = new HashMap<>();
		config.setJianLiMap(new ReadOnlyMap<>(jianLiMap));
		
		Map<Byte,Object[]> jianLiClientMap = new HashMap<>();
		config.setJianLiClientMap(new ReadOnlyMap<>(jianLiClientMap));
		
		//通用奖励配置
		String tyGoodsItem = CovertObjectUtil.object2String(tmp.get(itemsName));
		if(!CovertObjectUtil.isEmpty(tyGoodsItem)){
			tyGoodsItem = "|" + tyGoodsItem;
		}
		
		//上仙(白子画)字段索引值
		String sxIndex = "1";
		//上仙(白子画)奖励
		
		String goodsItem = CovertObjectUtil.object2String(tmp.get(itemsName + sxIndex)) + tyGoodsItem;
		if(!CovertObjectUtil.isEmpty(goodsItem)){
			Map<String, GoodsConfigureVo> jobGoodsMap = ConfigAnalysisUtils.getConfigVoMap(goodsItem);
			if(jobGoodsMap != null){
				jianLiMap.put(GameConstants.JOB_BZH, jobGoodsMap);
			}
		}
		//处理客户端奖励数据
		if(jianLiMap.get(GameConstants.JOB_BZH) != null){
			Object[] tmpArr = ConfigAnalysisUtils.getConfigArray(goodsItem);
			jianLiClientMap.put(GameConstants.JOB_BZH, tmpArr);
		}
		
		//妖尊(花千骨)字段索引值
		String yzIndex = "2";
		//妖尊(花千骨)奖励
		goodsItem = CovertObjectUtil.object2String(tmp.get(itemsName+yzIndex)) + tyGoodsItem;
		if(!CovertObjectUtil.isEmpty(goodsItem)){
			Map<String, GoodsConfigureVo> jobGoodsMap = ConfigAnalysisUtils.getConfigVoMap(goodsItem);
			if(jobGoodsMap != null){
				jianLiMap.put(GameConstants.JOB_HQG, jobGoodsMap);
			}
		}
		//处理客户端奖励数据
		if(jianLiMap.get(GameConstants.JOB_HQG) != null){
			Object[] tmpArr = ConfigAnalysisUtils.getConfigArray(goodsItem);
			jianLiClientMap.put(GameConstants.JOB_HQG, tmpArr);
		}
		
		
		
		
		 
		
		
		String firstItemsName = "itema";
		
		//奖励
		Map<Byte,Map<String,GoodsConfigureVo>> firstJianLiMap = new HashMap<>();
		config.setFirstJianLiMap(new ReadOnlyMap<>(firstJianLiMap));
		
		Map<Byte,Object[]> firstJianLiClientMap = new HashMap<>();
		config.setFirstJianLiClientMap(new ReadOnlyMap<>(firstJianLiClientMap));
		
		//通用奖励配置
		String firstTyGoodsItem = CovertObjectUtil.object2String(tmp.get(firstItemsName));
		if(!CovertObjectUtil.isEmpty(firstTyGoodsItem)){
			firstTyGoodsItem = "|" + firstTyGoodsItem;
		}
		
		//上仙(白子画)字段索引值
		String firstSxIndex = "1";
		//上仙(白子画)奖励
		
		String firstGoodsItem = CovertObjectUtil.object2String(tmp.get(firstItemsName + firstSxIndex)) + firstTyGoodsItem;
		if(!CovertObjectUtil.isEmpty(firstGoodsItem)){
			Map<String, GoodsConfigureVo> jobGoodsMap = ConfigAnalysisUtils.getConfigVoMap( firstGoodsItem);
			if(jobGoodsMap != null){
				firstJianLiMap.put(GameConstants.JOB_BZH, jobGoodsMap);
			}
		}
		//处理客户端奖励数据
		if(firstJianLiMap.get(GameConstants.JOB_BZH) != null){
			Object[] tmpArr = ConfigAnalysisUtils.getConfigArray(firstGoodsItem);
			firstJianLiClientMap.put(GameConstants.JOB_BZH, tmpArr);
		}
		
		//妖尊(花千骨)字段索引值
		String firstYzIndex = "2";
		//妖尊(花千骨)奖励
		firstGoodsItem = CovertObjectUtil.object2String(tmp.get(firstItemsName+firstYzIndex)) + firstTyGoodsItem;
		if(!CovertObjectUtil.isEmpty(firstGoodsItem)){
			Map<String, GoodsConfigureVo> jobGoodsMap = ConfigAnalysisUtils.getConfigVoMap(firstGoodsItem);
			if(jobGoodsMap != null){
				firstJianLiMap.put(GameConstants.JOB_HQG, jobGoodsMap);
			}
		}
		//处理客户端奖励数据
		if(firstJianLiMap.get(GameConstants.JOB_HQG) != null){
			Object[] tmpArr = ConfigAnalysisUtils.getConfigArray(firstGoodsItem);
			firstJianLiClientMap.put(GameConstants.JOB_HQG, tmpArr);
		}
		
		
		
		
		
		
		
//		
//		//人皇(轩辕朗)字段索引值
//		String rhIndex = "3";
//		//人皇(轩辕朗)奖励
//		goodsItem = CovertObjectUtil.object2String(tmp.get(itemsNameA + rhIndex)) + tyGoodsItem;
//		if(!CovertObjectUtil.isEmpty(goodsItem)){
//			Map<String, GoodsConfigureVo> jobGoodsMap = ConfigAnalysisUtils.getConfigVoMap(goodsItem);
//			if(jobGoodsMap != null){
//				jianLiMapA.put(GameConstants.JOB_SQM, jobGoodsMap);
//			}
//		}
//		//处理客户端奖励数据
//		if(jianLiMapA.get(GameConstants.JOB_SQM) != null){
//			Object[] tmpArr = ConfigAnalysisUtils.getConfigArray(goodsItem);
//			jianLiClientMap.put(GameConstants.JOB_SQM, tmpArr);
//		}
//
//		//魔灵(夏紫熏)字段索引值
//		String mlIndex = "4";
//		//魔灵(夏紫熏)奖励
//		goodsItem = CovertObjectUtil.object2String(tmp.get(itemsNameA + mlIndex)) + tyGoodsItem;
//		if(!CovertObjectUtil.isEmpty(goodsItem)){
//			Map<String, GoodsConfigureVo> jobGoodsMap = ConfigAnalysisUtils.getConfigVoMap(goodsItem);
//			if(jobGoodsMap != null){
//				jianLiMapA.put(GameConstants.JOB_ZXQX, jobGoodsMap);
//			}
//		}
//		//魔灵处理客户端奖励数据
//		if(jianLiMap.get(GameConstants.JOB_ZXQX) != null){
//			Object[] tmpArr = ConfigAnalysisUtils.getConfigArray(goodsItem);
//			jianLiClientMap.put(GameConstants.JOB_ZXQX, tmpArr);
//		}
		
		
//		String itemsNameB = "itemb";
//		//通用奖励配置B
//		String tyGoodsItemB = CovertObjectUtil.object2String(tmp.get(itemsNameB));
//		if(!CovertObjectUtil.isEmpty(tyGoodsItemB)){
//			tyGoodsItemB = "|" + tyGoodsItemB;
//		}
//		
//		//上仙(白子画)奖励
//		String goodsItemB = CovertObjectUtil.object2String(tmp.get(itemsNameB + sxIndex)) + tyGoodsItemB;
//		if(!CovertObjectUtil.isEmpty(goodsItemB)){
//			Map<String, GoodsConfigureVo> jobGoodsMap = ConfigAnalysisUtils.getConfigVoMap(goodsItemB);
//			if(jobGoodsMap != null){
//				jianLiMapB.put(GameConstants.JOB_BZH, jobGoodsMap);
//			}
//		}
//		//处理客户端奖励数据
//		if(jianLiMapB.get(GameConstants.JOB_BZH) != null){
//			Object[] tmpArr = ConfigAnalysisUtils.getConfigArray(goodsItemB);
//			jianLiClientMapB.put(GameConstants.JOB_BZH, tmpArr);
//		}
//		
//		//妖尊(花千骨)奖励
//		goodsItemB = CovertObjectUtil.object2String(tmp.get(itemsNameB+yzIndex)) + tyGoodsItemB;
//		if(!CovertObjectUtil.isEmpty(goodsItemB)){
//			Map<String, GoodsConfigureVo> jobGoodsMap = ConfigAnalysisUtils.getConfigVoMap(goodsItemB);
//			if(jobGoodsMap != null){
//				jianLiMapB.put(GameConstants.JOB_HQG, jobGoodsMap);
//			}
//		}
//		//处理客户端奖励数据
//		if(jianLiMapB.get(GameConstants.JOB_HQG) != null){
//			Object[] tmpArr = ConfigAnalysisUtils.getConfigArray(goodsItemB);
//			jianLiClientMapB.put(GameConstants.JOB_HQG, tmpArr);
//		}
//		//人皇(轩辕朗)奖励
//		goodsItemB = CovertObjectUtil.object2String(tmp.get(itemsNameB + rhIndex)) + tyGoodsItemB;
//		if(!CovertObjectUtil.isEmpty(goodsItemB)){
//			Map<String, GoodsConfigureVo> jobGoodsMap = ConfigAnalysisUtils.getConfigVoMap(goodsItemB);
//			if(jobGoodsMap != null){
//				jianLiMapB.put(GameConstants.JOB_SQM, jobGoodsMap);
//			}
//		}
//		//处理客户端奖励数据
//		if(jianLiMapB.get(GameConstants.JOB_SQM) != null){
//			Object[] tmpArr = ConfigAnalysisUtils.getConfigArray(goodsItemB);
//			jianLiClientMapB.put(GameConstants.JOB_SQM, tmpArr);
//		}
//
//		//魔灵(夏紫熏)奖励
//		goodsItemB = CovertObjectUtil.object2String(tmp.get(itemsNameB + mlIndex)) + tyGoodsItemB;
//		if(!CovertObjectUtil.isEmpty(goodsItemB)){
//			Map<String, GoodsConfigureVo> jobGoodsMap = ConfigAnalysisUtils.getConfigVoMap(goodsItemB);
//			if(jobGoodsMap != null){
//				jianLiMapB.put(GameConstants.JOB_ZXQX, jobGoodsMap);
//			}
//		}
//		//处理客户端奖励数据
//		if(jianLiMapB.get(GameConstants.JOB_ZXQX) != null){
//			Object[] tmpArr = ConfigAnalysisUtils.getConfigArray(goodsItemB);
//			jianLiClientMapB.put(GameConstants.JOB_ZXQX, tmpArr);
//		}
		
		return config;
	}
}
