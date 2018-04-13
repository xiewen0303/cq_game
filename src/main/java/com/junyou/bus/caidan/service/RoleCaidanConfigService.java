package com.junyou.bus.caidan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.caidan.entity.CaidanCategoryConfig;
import com.junyou.bus.caidan.entity.CaidanConfig;
import com.junyou.bus.rfbactivity.configure.export.AbstractRfbConfigService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;
@Component
public class RoleCaidanConfigService extends AbstractRfbConfigService {
	
	private static final RoleCaidanConfigService INSTANCE = new RoleCaidanConfigService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,CaidanCategoryConfig> CONFIG_MAP = new HashMap<>();
	
	private RoleCaidanConfigService(){
	}
	/**
	 * 获取对象实例
	 * @return
	 */
	public static RoleCaidanConfigService getInstance() {
		return INSTANCE;
	}
	
	public CaidanCategoryConfig loadByKeyId(int subId){
		return CONFIG_MAP.get(subId);
	}
	
	@Override
	public void analysisConfigureDataResolve(int subId, byte[] data) {
		if(data == null){
			ChuanQiLog.error(" Caidan 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" Caidan 2 data is error! ");
			return;
		}
		
		String md5Value = Md5Utils.md5Bytes(data);
		CaidanCategoryConfig group = CONFIG_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getVersion())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" Caidan subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new CaidanCategoryConfig();
		group.setVersion(md5Value);
		
		//处理子活动的版本号
		ActivityConfigSon configSon = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSon != null){
			configSon.setClientVersion(configSon.getClientVersion() + 1);
		}
		for(int i=0;i<json.size();i++){  
			JSONArray json1 = json.getJSONArray(i);
			Map<String,Object> map = new HashMap<String, Object>();
			for (int j = 0; j < json1.size(); j++) {
				@SuppressWarnings("unchecked")
				Map<String,Object> aa = (Map<String, Object>)JSONObject.parse(json1.getString(j));
				map.putAll(aa);
			}
			int id = CovertObjectUtil.object2int(map.get("id"));
			if(id > 0){
				CaidanConfig config = createCaidanConfig(map);
				config.setId(id);
				group.addCaidan(config);
				continue;
			}else if(id == -1){
				int gold = CovertObjectUtil.object2int(map.get("gold"));
				group.setRestetGold(gold);
			}else if(id == -2){
				String str = CovertObjectUtil.object2String(map.get("data1"));
				group.setShowItems(str.split(GameConstants.CONFIG_SPLIT_CHAR));
			}else if(id == -3){
				createDuihuanMap(group, map,subId);
			}else if(id == -4){
				group.setBg(CovertObjectUtil.object2String(map.get("data1")));
			}else if(id == -5){
				group.setInfo(CovertObjectUtil.object2String(map.get("data1")));
			}else if(id == -6){
				int count = CovertObjectUtil.object2int(map.get("data1"));
				if(count == 0){count = 9999;}
				group.setMaxCount(count);
			}
		}
		CONFIG_MAP.put(subId, group);
	}
	
	private CaidanConfig createCaidanConfig(Map<String, Object> map){
		CaidanConfig config = new CaidanConfig();
		config.setGold(CovertObjectUtil.object2int(map.get("gold")));
		config.setLucky(CovertObjectUtil.object2int(map.get("data1")));
		config.setScore(CovertObjectUtil.object2int(map.get("data2")));
		Map<GoodsConfigureVo, Integer> itemMap = new HashMap<>();
		for (int i = 1; i < 200; i++) {
			String goods = CovertObjectUtil.obj2StrOrNull(map.get("drop"+i));
			if(ObjectUtil.strIsEmpty(goods)){
				break;
			}
			String[] goodsInfo = goods.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			int count = 1;
			if(goodsInfo.length > 1){
				count = CovertObjectUtil.object2int(goodsInfo[1]);
			}
			GoodsConfigureVo vo = new GoodsConfigureVo(goodsInfo[0], count);
			int qz = CovertObjectUtil.object2int(map.get("drop"+i+"Odds"));
			itemMap.put(vo, qz);
		}
		config.setItemMap(itemMap);
		return config;
	}
	
	private void createDuihuanMap(CaidanCategoryConfig group,Map<String, Object> map,Integer subId){
		Map<String,Integer> duihuan = new HashMap<>();
		List<Object[]> duihuanInfo = new ArrayList<>();
		for (int i = 1; i < 200; i++) {
			String goods = CovertObjectUtil.obj2StrOrNull(map.get("drop"+i));
			if(ObjectUtil.strIsEmpty(goods)){
				break;
			}
			int cost = CovertObjectUtil.object2int(map.get("drop"+i+"Odds"));
			duihuan.put(goods, cost);
			duihuanInfo.add(new Object[]{goods,cost});
		}
		group.setDuihuanMap(duihuan);
		group.setGetDuihuanInfo(new Object[]{subId,duihuanInfo.toArray()});
	}
	@Override
	public Object getChildData(int subId) {
		CaidanCategoryConfig subCfg  = this.CONFIG_MAP.get(subId);
		return subCfg != null ? subCfg.getBg() : null;
	}

}
