package com.junyou.bus.bagua.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

@Component
public class DuoRenTongYongBiaoExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, Map<Integer, DuoRenTongYongBiaoConfig>> configs = null;

	/**
	 * configFileName
	 */
	private String configureName = "DuoRenTongYongBiao.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, Map<Integer, DuoRenTongYongBiaoConfig>>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				DuoRenTongYongBiaoConfig config = createDuoRenTongYongBiaoConfig(tmp);
				Map<Integer, DuoRenTongYongBiaoConfig> map = configs.get(config
						.getType());
				if (map == null) {
					map = new HashMap<Integer, DuoRenTongYongBiaoConfig>();
					configs.put(config.getType(), map);
				}
				map.put(config.getId(), config);
			}
		}
	}

	public DuoRenTongYongBiaoConfig createDuoRenTongYongBiaoConfig(
			Map<String, Object> tmp) {
		DuoRenTongYongBiaoConfig config = new DuoRenTongYongBiaoConfig();
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		Map<String, Integer> jiangli = ConfigAnalysisUtils
				.getConfigMap(CovertObjectUtil.object2String(tmp.get("shoucijiangli")));
		config.setJiangliMap(jiangli);
		config.setMapId(CovertObjectUtil.object2int(tmp.get("map")));
		config.setTuijian(CovertObjectUtil.object2int(tmp.get("tuijian")));
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
		config.setJiangexp(CovertObjectUtil.obj2long(tmp.get("jiangexp")));
		config.setJiangmoney(CovertObjectUtil.object2int(tmp.get("jiangmoney")));
		config.setXianzhizhanli(CovertObjectUtil.object2int(tmp
				.get("xianzhizhanli")));
		String data1 = CovertObjectUtil.object2String(tmp.get("data1"));
		String monster = null;
		if(data1.equals("1")){
			monster = CovertObjectUtil.object2String(tmp.get("data2"));
			if(!ObjectUtil.strIsEmpty(monster)){
				String[] monsterInfo = monster.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
				Map<String,Integer> map = new HashMap<>();
				for(String info : monsterInfo){				
					map.put(info, 1);
				}
				config.setWantedMap(map);
			}
		}
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public DuoRenTongYongBiaoConfig loadByOrder(int type, int id) {
		Map<Integer, DuoRenTongYongBiaoConfig> map = configs.get(type);
		if (map == null) {
			return null;
		}
		return map.get(id);
	}

}