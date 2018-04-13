package com.junyou.bus.xunbao.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.rfbactivity.configure.export.AbstractRfbConfigService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 
 * @author zhongdian  
 */
public class RfbXunBaoConfigExportService  extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		RfbXunBaoConfigGroup subCfg = KFPM_MAP.get(subId);
		return subCfg != null ? subCfg.getPic() : null;
	}
	
	private static final RfbXunBaoConfigExportService INSTANCE = new RfbXunBaoConfigExportService();
	
	private RfbXunBaoConfigExportService(){
	}

	/**
	 * 获取对象实例
	 * @return
	 */
	public static RfbXunBaoConfigExportService getInstance() {
		return INSTANCE;
	}
	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,RfbXunBaoConfigGroup> KFPM_MAP = new HashMap<>();
	
	private int defautId = -1;
	
	public RfbXunBaoConfigGroup getRfbXunBaoConfig(int subId){
		return KFPM_MAP.get(subId);
	}
	
	public RfbXunBaoConfig createXunBaoConfig(Map<String, Object> tmp) {
		int  allOdds=0;
		RfbXunBaoConfig config = new RfbXunBaoConfig();	
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setMincishu(CovertObjectUtil.object2int(tmp.get("data1")));
		config.setMaxcishu(CovertObjectUtil.object2int(tmp.get("data2")));
		
		int i=1;
		while(i<200){
			
			String drop = CovertObjectUtil.object2String(tmp.get(GameConstants.DROP+i));
			
			if("".equals(drop) ){
				break;
			}
			String[] dropGoods = drop.split(":");
			if(dropGoods.length < 2){
				break;
			}
			int dropOdds=CovertObjectUtil.object2int(tmp.get(GameConstants.DROP+i+GameConstants.ODDS));
			allOdds+=dropOdds;
			int index = drop.indexOf(GameConstants.ZB_FLAG);
			boolean isZB= index != -1 ? true:false;
			config.addGoodsOdds(new Object[]{dropGoods[0],Integer.valueOf(dropGoods[1]),isZB},dropOdds);
			
			i++;
		}
		
		config.setAllOdds(allOdds);
							
		return config;
	} 
	 
	public RfbXunBaoConfig getXunBaoIdByCount(int subId, long times) {
		RfbXunBaoConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			for (RfbXunBaoConfig  config : group.getXunBaoConfigs().values()) {
				if(config.getMincishu()<=times && times<=config.getMaxcishu()){
					return config;
				}
			}
			return group.getXunBaoConfigs().get(defautId);
		}
		return null;
		
	}

	@Override
	public void analysisConfigureDataResolve(int subId, byte[] data) {
		
		if(data == null){
			ChuanQiLog.error(" Xunbao 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" Xunbao 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		RfbXunBaoConfigGroup group = KFPM_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" Xunbao subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		group = new RfbXunBaoConfigGroup();
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
		
		Map<Integer, RfbXunBaoConfig> xunBaoConfigs = new HashMap<>();
		for (Map<String,Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));
				if(id>0){
					RfbXunBaoConfig config = createXunBaoConfig(map);
					
					xunBaoConfigs.put(config.getId(), config);
					
					if(defautId<1){
						defautId = config.getId();
					}
				}else{
					addXunBaoOtherConfig(id, map, group);
				}
			}
		}
		if(0 < xunBaoConfigs.size()){
			group.setXunBaoConfigs(xunBaoConfigs);
		}
		KFPM_MAP.put(subId, group);
	}
	/**
	 * 添加寻宝其他信息
	 * @param id
	 * @param tmp
	 * @param group
	 */
	public void addXunBaoOtherConfig(Integer id, Map<String, Object> tmp, RfbXunBaoConfigGroup group){
		if(-4 < id){
			int count = CovertObjectUtil.object2int(tmp.get("data1"));
			int needGold = CovertObjectUtil.object2int(tmp.get("data2"));
			group.addTypeCount(id, count, needGold);
			
		}else if(-4 == id){
			String xunbaoShow = CovertObjectUtil.object2String(tmp.get("data1"));
			String[] xunbaoArr = xunbaoShow.split(GameConstants.GOODS_CONFIG_SPLIT_CHAR);
			List<Object> list = new ArrayList<Object>();
			for(String goods : xunbaoArr){
				String[] goodsArr = goods.split(GameConstants.GOODS_CONFIG_SUB_SPLIT_CHAR);
				list.add(new Object[]{goodsArr[0],goodsArr.length>1 ? goodsArr[1]:1});
			}
			group.setShowGoods(list.toArray());
			String pic = CovertObjectUtil.object2String(tmp.get("data2"));
			group.setPic(pic);
			
			String des = CovertObjectUtil.object2String(tmp.get(GameConstants.DROP+1));
			group.setDes(des);
		}else if(-10 == id){
			int count = CovertObjectUtil.object2int(tmp.get("data1"));
			if(count == 0 ){count = 9999;}
			group.setMaxCount(count);
		}else{
			int totalCount = CovertObjectUtil.object2int(tmp.get("data1"));
			String rewardsStr = CovertObjectUtil.object2String(tmp.get("data2"));
			String[] rewardsArr = rewardsStr.split(GameConstants.GOODS_CONFIG_SUB_SPLIT_CHAR);
			group.addAllPeopleRewardsConfigs(id, totalCount, rewardsArr[0], Integer.parseInt(rewardsArr[1]));
		}
	}
}