package com.junyou.bus.equip.configure.export;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

@Service
public class TaoZhuangXiangWeiJieDuanConfigExportService extends AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "TaoZhuangXiangWeiJieDuan.jat";

	/**
	 * 所有配置数据集合
	 */
	private static Map<String, TaoZhuangXiangWeiJieDuanConfig> configMap = new HashMap<>();

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		if (data == null) {
			return;
		}

		Object[] dataList = GameConfigUtil.getResource(data);

		if (null != dataList) {
			try {
				for (Object obj : dataList) {
					Map<String, Object> tmp = (Map<String, Object>) obj;
					if (null != tmp) {
						createConfig(tmp);
					}
				}
			} catch (Exception e) {
				ChuanQiLog.error("TaoZhuangXiangWeiJieDuan.jat解析有问题");
			}
		}
	}

	private void createConfig(Map<String, Object> tmp) {
		TaoZhuangXiangWeiJieDuanConfig config = new TaoZhuangXiangWeiJieDuanConfig();
		int buWeiId = CovertObjectUtil.object2int(tmp.get("buweiid"));
		int jd = CovertObjectUtil.object2int(tmp.get("id"));
		int star = CovertObjectUtil.object2int(tmp.get("xingshu"));
		config.setBuWeiId(buWeiId);
		config.setJd(jd);
		config.setStar(star);
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(attrs);

		configMap.put(generateKey(buWeiId, jd, star), config);
	}

	private static String generateKey(int buWeiId, int jd, int star) {
		return String.format("%d_%d_%d", buWeiId, jd, star);
	}

	public TaoZhuangXiangWeiJieDuanConfig getConfigByCondition(int buWeiId, int jd, int star) {
		return configMap.get(generateKey(buWeiId, jd, star));
	}

	/**
	 * 
	 *@description: 得到单件装备四象特殊阶段属性
	 * @param buWeiId 部位Id
	 * @param jd 阶段
	 * @param star 星数
	 * @return
	 */
	public Map<String, Long> getTotalAttrs(int buWeiId, int jd, int star) {
		Map<String, Long> attrMap = new HashMap<>();
		for (Entry<String, TaoZhuangXiangWeiJieDuanConfig> entry : configMap.entrySet()) {
			TaoZhuangXiangWeiJieDuanConfig tmpConfig = entry.getValue();
			if (tmpConfig.getBuWeiId() != buWeiId || tmpConfig.getAttrs() == null) {
				continue;
			}

			if (jd > tmpConfig.getJd() || (jd == tmpConfig.getJd() && star >= tmpConfig.getStar())) {
				// 叠加满足条件的属性
				ObjectUtil.longMapAdd(attrMap, tmpConfig.getAttrs());
			}
		}

		return attrMap;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

}
