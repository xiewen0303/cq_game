package com.junyou.bus.map.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.login.configure.RefabuLoginConfigGroup;
import com.junyou.bus.map.entity.ActiveMapConfig;
import com.junyou.bus.rfbactivity.configure.export.AbstractRfbConfigService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.active.RefbActiveFubenStage;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 活动地图
 * @author LiuYu
 * @date 2015-7-29 下午2:54:10
 */
public class ActiveMapConfigExportService  extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		ActiveMapConfig subCfg = CONFIG_MAP.get(subId);
		return subCfg != null ? subCfg.getBg() : null;
	}
	

	private static final ActiveMapConfigExportService INSTANCE = new ActiveMapConfigExportService();

	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,ActiveMapConfig> CONFIG_MAP = new HashMap<>();
	
	
	private ActiveMapConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static ActiveMapConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,ActiveMapConfig> getAllConfig(){
		return CONFIG_MAP;
	}
	
	public ActiveMapConfig loadByMap(int subId){
		return CONFIG_MAP.get(subId);
	}
	
	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data) {
		if(data == null){
			ChuanQiLog.error(" ActiveMap 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" ActiveMap 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		ActiveMapConfig group = CONFIG_MAP.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getVersion())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" ActiveMap subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		Map<Integer,ActiveMapConfig> CONFIG_MAP = new HashMap<>(this.CONFIG_MAP);
		group = new ActiveMapConfig();
		group.setVersion(md5Value);
		group.setId(subId);
		
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
			group.setMapId(CovertObjectUtil.object2int(map.get("mapid")));
			group.setInfo(CovertObjectUtil.object2String(map.get("shuoming")));
			group.setBg(CovertObjectUtil.object2String(map.get("bg")));
			group.setBeizhu(CovertObjectUtil.obj2StrOrNull(map.get("beizhu")));
			String subids = CovertObjectUtil.obj2StrOrNull(map.get("subids"));
			if(!ObjectUtil.strIsEmpty(subids)){
				String[] ids = subids.split(",");
				if(ids.length > 0){
					List<Integer> mapids = new ArrayList<>();
					for (String id : ids) {
						mapids.add(CovertObjectUtil.object2int(id));
					}
					group.setSubMapIds(mapids);
				}
			}
			group.setStartTime(configSon.getStartTimeByMillSecond());
			group.setEndTime(configSon.getEndTimeByMillSecond());
		}
		//最终根据子活动ID记录，当前子活动ID的首充活动数据
		ActiveMapConfig oldConfig = CONFIG_MAP.get(subId);
		CONFIG_MAP.put(subId, group);
		this.CONFIG_MAP = CONFIG_MAP;
		if(oldConfig != null){
			if(oldConfig.getStartTime() < group.getStartTime() || oldConfig.getEndTime() > group.getEndTime()){
				kickHandle(oldConfig);
			}
		}
		if(group.getSubMapIds() != null && group.getSubMapIds().size() > 0){
			BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.INNER_REFB_ACTIVE_MAP_CREATE, subId);
		}
	}
	
	private void kickHandle(ActiveMapConfig config){
		String stageId = StageUtil.getStageId(config.getMapId(), config.getId());
		IStage stage = StageManager.getStage(stageId);
		if(StageType.isRefbActiveFuben(stage.getStageType())){
			RefbActiveFubenStage refbActiveFubenStage = (RefbActiveFubenStage)stage;
			refbActiveFubenStage.kickAll();
		}
	}
	
}
