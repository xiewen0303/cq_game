package com.junyou.bus.shouchong.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 热发布首充礼包表配置 (不让spring管理对象 ，业务自己管理实例对象)
 */
public class RfbShouChongConfigExportService  extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		ShouChongGroupConfig subCfg = SHOUCHONGS.get(subId);
		return subCfg != null ? subCfg.getBgContent() : null;
	}
	
	private static final RfbShouChongConfigExportService INSTANCE = new RfbShouChongConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,ShouChongGroupConfig> SHOUCHONGS = new HashMap<>();
	
	/**
	 * 私有构造方法(不允许外面创建实例)
	 */
	private RfbShouChongConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static RfbShouChongConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,ShouChongGroupConfig> loadAll(){
		return SHOUCHONGS;
	}
	
	public ShouChongActivityConfig loadById(String subId,Integer index){
		ShouChongGroupConfig config = SHOUCHONGS.get(subId);
		if(config != null){
			return config.getShouChongDangByIndex(index);
		}
		return null;
	}
	
	public ShouChongGroupConfig loadById(int subId){
		return SHOUCHONGS.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" ShouChong 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" ShouChong 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		ShouChongGroupConfig group = SHOUCHONGS.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" ShouChong subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		
		ShouChongGroupConfig newGroup = new ShouChongGroupConfig();
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
				ShouChongActivityConfig shouChongConfig = createShouChongActivityConfig(map);
				newGroup.addShouChongActivityConfig(shouChongConfig);
				if(shouChongConfig.getConfigId() == -1){
					newGroup.setDesc(shouChongConfig.getName());
					newGroup.setBgContent(shouChongConfig.getBtn0());
				}
			}
		}
		
		//手动排序一次
		newGroup.sortNeedYbList();
		//加入管理
		SHOUCHONGS.put(subId, newGroup);
	}
	
	private ShouChongActivityConfig createShouChongActivityConfig(Map<String, Object> tmp) {
		ShouChongActivityConfig config = new ShouChongActivityConfig();
		config.setConfigId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setNeedYb(CovertObjectUtil.object2int(tmp.get("need")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setBtn0(CovertObjectUtil.object2String(tmp.get("btn0")));
		config.setBtn1(CovertObjectUtil.object2String(tmp.get("btn1")));
		config.setGongGao(CovertObjectUtil.object2int(tmp.get("code")));
		String itemsName = "item";
		String resName = "res";
		String bgName = "bg";
		String showName = "show";
		
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
		
		Map<Byte, String> resClientMap = new HashMap<>();
		config.setResClientMap(new ReadOnlyMap<>(resClientMap));
		Map<Byte, String> bgClientMap = new HashMap<>();
		config.setBgClientMap(new ReadOnlyMap<>(bgClientMap));
		Map<Byte, String> showClientMap = new HashMap<>();
		config.setShowMap(showClientMap);
		
		//男主角字段索引值
		String sxIndex = "1";
		//男主角奖励
		String resValue = CovertObjectUtil.object2String(tmp.get(resName + sxIndex));
		String bgValue = CovertObjectUtil.object2String(tmp.get(bgName + sxIndex));
		String showValue = CovertObjectUtil.object2String(tmp.get(showName + sxIndex));
		resClientMap.put((byte)GameConstants.JOB_BZH, resValue);
		bgClientMap.put((byte)GameConstants.JOB_BZH, bgValue);
		showClientMap.put((byte)GameConstants.JOB_BZH, showValue);
		
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
		
		//女主角字段索引值
		String yzIndex = "2";
		//女主角奖励
		resValue = CovertObjectUtil.object2String(tmp.get(resName + yzIndex));
		bgValue = CovertObjectUtil.object2String(tmp.get(bgName + yzIndex));
		showValue = CovertObjectUtil.object2String(tmp.get(showName + yzIndex));
		resClientMap.put((byte)GameConstants.JOB_HQG, resValue);
		bgClientMap.put((byte)GameConstants.JOB_HQG, bgValue);
		showClientMap.put((byte)GameConstants.JOB_HQG, showValue);
		
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
		
//		//人皇(轩辕朗)字段索引值
//		String rhIndex = "3";
//		//人皇(轩辕朗)奖励
//		resValue = CovertObjectUtil.object2String(tmp.get(resName + rhIndex));
//		bgValue = CovertObjectUtil.object2String(tmp.get(bgName + rhIndex));
//		showValue = CovertObjectUtil.object2String(tmp.get(showName + rhIndex));
//		resClientMap.put((byte)GameConstants.JOB_SQM, resValue);
//		bgClientMap.put((byte)GameConstants.JOB_SQM, bgValue);
//		showClientMap.put((byte)GameConstants.JOB_SQM, showValue);
//		
//		goodsItem = CovertObjectUtil.object2String(tmp.get(itemsName + rhIndex)) + tyGoodsItem;
//		if(!CovertObjectUtil.isEmpty(goodsItem)){
//			Map<String, GoodsConfigureVo> jobGoodsMap = ConfigAnalysisUtils.getConfigVoMap(goodsItem);
//			if(jobGoodsMap != null){
//				jianLiMap.put(GameConstants.JOB_SQM, jobGoodsMap);
//			}
//		}
//		//处理客户端奖励数据
//		if(jianLiMap.get(GameConstants.JOB_SQM) != null){
//			Object[] tmpArr = ConfigAnalysisUtils.getConfigArray(goodsItem);
//			jianLiClientMap.put(GameConstants.JOB_SQM, tmpArr);
//		}
//
//		//魔灵(夏紫熏)字段索引值
//		String mlIndex = "4";
//		//魔灵(夏紫熏)奖励
//		resValue = CovertObjectUtil.object2String(tmp.get(resName + mlIndex));
//		bgValue = CovertObjectUtil.object2String(tmp.get(bgName + mlIndex));
//		showValue = CovertObjectUtil.object2String(tmp.get(showName + mlIndex));
//		resClientMap.put((byte)GameConstants.JOB_ZXQX, resValue);
//		bgClientMap.put((byte)GameConstants.JOB_ZXQX, bgValue);
//		showClientMap.put((byte)GameConstants.JOB_ZXQX, showValue);
//		
//		goodsItem = CovertObjectUtil.object2String(tmp.get(itemsName + mlIndex)) + tyGoodsItem;
//		if(!CovertObjectUtil.isEmpty(goodsItem)){
//			Map<String, GoodsConfigureVo> jobGoodsMap = ConfigAnalysisUtils.getConfigVoMap(goodsItem);
//			if(jobGoodsMap != null){
//				jianLiMap.put(GameConstants.JOB_ZXQX, jobGoodsMap);
//			}
//		}
		//处理客户端奖励数据
		if(jianLiMap.get(GameConstants.JOB_ZXQX) != null){
			Object[] tmpArr = ConfigAnalysisUtils.getConfigArray(goodsItem);
			jianLiClientMap.put(GameConstants.JOB_ZXQX, tmpArr);
		}
		
		return config;
	}
}