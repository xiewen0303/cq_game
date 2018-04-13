package com.junyou.bus.miaosha.configure;

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
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 连充
 */
@Service
public class MiaoShaConfigExportService  extends AbstractRfbConfigService {
	
	@Override
	public Object getChildData(int subId) {
		MiaoShaConfigGroup subCfg = KFPM_MAP.get(subId);
		return subCfg != null ? subCfg.getPic() : null;
	}
	
	private static final MiaoShaConfigExportService INSTANCE = new MiaoShaConfigExportService();

	/**
	 * key:子活动id value:子活动数据
	 */
	private Map<Integer, MiaoShaConfigGroup> KFPM_MAP = new HashMap<>();

	private MiaoShaConfigExportService() {
	}

	/**
	 * 获取对象实例
	 * 
	 * @return
	 */
	public static MiaoShaConfigExportService getInstance() {
		return INSTANCE;
	}

	public Map<Integer, MiaoShaConfigGroup> getAllConfig() {
		return KFPM_MAP;
	}

	public MiaoShaConfig loadByKeyId(int subId,long cur) {
		MiaoShaConfigGroup groups = KFPM_MAP.get(subId);
		if (groups == null) {
			return null;
		}
		return groups.getConfig(cur);
	}

	public MiaoShaConfigGroup loadByMap(int subId) {
		return KFPM_MAP.get(subId);
	}

	/**
	 * 解析数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId, byte[] data) {
		if (data == null) {
			ChuanQiLog.error(" LianChong 1 data is error! ");
		}

		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if (json == null || json.isEmpty()) {
			ChuanQiLog.error(" LianChong 2 data is error! ");
			return;
		}

		// 版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		MiaoShaConfigGroup group = KFPM_MAP.get(subId);
		if (group != null) {

			if (md5Value.equals(group.getMd5Version())) {
				// 版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" LianChong subid={} version is same md5Value={}", subId, md5Value);
				return;
			}
		}
		group = new MiaoShaConfigGroup();
		group.setMd5Version(md5Value);

		// 处理子活动的版本号
		ActivityConfigSon configSon = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if (configSon != null) {
			configSon.setClientVersion(configSon.getClientVersion() + 1);
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < json.size(); i++) {
			JSONArray json1 = json.getJSONArray(i);
			Map<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < json1.size(); j++) {
				@SuppressWarnings("unchecked")
				Map<String, Object> aa = (Map<String, Object>) JSONObject.parse(json1.getString(j));
				map.putAll(aa);
			}
			list.add(map);
		}
		for (Map<String, Object> map : list) {
			if (null != map) {
				Integer id = CovertObjectUtil.object2int(map.get("id"));
				// id为-1处理活动说明和底图处理
				if(id > 0){
					MiaoShaConfig config = createConfig(map);
					group.addConfig(config);
				}else if (id == -1) {
					group.setDes(CovertObjectUtil.object2String(map.get("endtime")));
				}else if (id == -2){
					group.setPic(CovertObjectUtil.object2String(map.get("endtime")));
				}else if (id == -3){
					group.setOpenTime(CovertObjectUtil.object2String(map.get("endtime")));
				}
			}
		}
		// 最终根据子活动ID记录，当前子活动ID的首充活动数据
		KFPM_MAP.put(subId, group);
	}
	 
	public MiaoShaConfig createConfig(Map<String, Object> tmp) {
		MiaoShaConfig  config  = new MiaoShaConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		
		String[] startStr = CovertObjectUtil.object2String(tmp.get("starttime")).split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		long startTime = (CovertObjectUtil.object2int(startStr[0]) * 60 + CovertObjectUtil.object2int(startStr[1])) * 60000;
		String[] endStr = CovertObjectUtil.object2String(tmp.get("endtime")).split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		long endTime = (CovertObjectUtil.object2int(endStr[0]) * 60 + CovertObjectUtil.object2int(endStr[1])) * 60000;
		MiaoShaTime time = new MiaoShaTime();
		time.setStartTime(startTime);
		time.setEndTime(endTime);
		config.setTime(time);
		
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		
		Map<MiaoShaGoodsVo,Integer> drop = new HashMap<>();
		int total = 0;
		for (int i = 1; i < 6; i++) {
			String goodsInfo = CovertObjectUtil.object2String(tmp.get("drop"+i));
			if(ObjectUtil.strIsEmpty(goodsInfo)){
				continue;
			}
			String[] goods = goodsInfo.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			if(goods.length < 2){
				continue;
			}
			MiaoShaGoodsVo vo = new MiaoShaGoodsVo();
			vo.setClient(goodsInfo);
			vo.setGoodsId(goods[0]);
			vo.setCount(CovertObjectUtil.object2int(goods[1]));
			if(i == 1){
				vo.setJipin(true);
				config.setJipinGoods(vo.getClient());
			}
			int odds = CovertObjectUtil.object2int(tmp.get("dropOdds"+i));
			drop.put(vo, odds);
			total += odds;
		}
		config.setDrop(drop);
		config.setTotal(total);
		
		String firstStr = CovertObjectUtil.object2String(tmp.get("item1"));
		Map<String,Integer> first = ConfigAnalysisUtils.getConfigMap(firstStr);
		config.setFirst(first);
		String top10Str = CovertObjectUtil.object2String(tmp.get("item2"));
		Map<String,Integer> top10 = ConfigAnalysisUtils.getConfigMap(top10Str);
		config.setTop10(top10);
		String joinStr = CovertObjectUtil.object2String(tmp.get("item3"));
		Map<String,Integer> join = ConfigAnalysisUtils.getConfigMap(joinStr);
		config.setJoin(join);
		config.setClientVo(new Object[]{firstStr,top10Str,joinStr});
		
		return config;
	}
}
