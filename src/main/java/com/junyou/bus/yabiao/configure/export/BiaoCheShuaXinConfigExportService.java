package com.junyou.bus.yabiao.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 押镖奖励配置
 * 
 * @author LiNing
 * @date 2015-03-13 17:05:04
 */
@Component
public class BiaoCheShuaXinConfigExportService extends
		AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "BiaoCheShuaXin.jat";
	private Map<Integer, Map<Integer, BiaoCheShuaXinConfig>> configs;
	private int maxVip;

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		configs = new HashMap<Integer, Map<Integer, BiaoCheShuaXinConfig>>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				BiaoCheShuaXinConfig config = createBiaoCheShuaXinConfig(tmp);
				if (config.getId() > maxVip) {
					maxVip = config.getId();
				}
				Map<Integer, BiaoCheShuaXinConfig> map = configs.get(config
						.getRarelevel());
				if (map == null) {
					map = new HashMap<Integer, BiaoCheShuaXinConfig>();
					configs.put(config.getRarelevel(), map);
				}
				map.put(config.getId(), config);
			}
		}
		for (Map<Integer, BiaoCheShuaXinConfig> e : configs.values()) {
			int preVip = 0;
			for (int i = 0; i <= maxVip; i++) {
				BiaoCheShuaXinConfig config = e.get(i);
				if (config == null) {
					e.put(i, e.get(preVip));
				} else {
					preVip = i;
				}
			}
		}
	}

	public BiaoCheShuaXinConfig createBiaoCheShuaXinConfig(
			Map<String, Object> tmp) {
		BiaoCheShuaXinConfig config = new BiaoCheShuaXinConfig();

		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setRarelevel(CovertObjectUtil.object2int(tmp.get("rarelevel")));

		Integer d1 = CovertObjectUtil.object2int(tmp.get("data1"));
		Integer d2 = CovertObjectUtil.object2int(tmp.get("data2"));
		Integer d3 = CovertObjectUtil.object2int(tmp.get("data3"));
		Integer d4 = CovertObjectUtil.object2int(tmp.get("data4"));
		Integer d5 = CovertObjectUtil.object2int(tmp.get("data5"));
		config.setTotal(d1 + d2 + d3 + d4 + d5);
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(1, d1);
		map.put(2, d2);
		map.put(3, d3);
		map.put(4, d4);
		map.put(5, d5);
		config.setMap(map);
		return config;
	}

	protected String getConfigureName() {
		return configureName;
	}

	public BiaoCheShuaXinConfig getByVipAndRarelevel(Integer vip,
			Integer rarelevel) {
		if (vip.intValue() >= maxVip) {
			return configs.get(rarelevel).get(maxVip);
		} else {
			return configs.get(rarelevel).get(vip);
		}
	}
}