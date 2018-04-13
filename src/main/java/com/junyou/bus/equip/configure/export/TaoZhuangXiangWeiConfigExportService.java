package com.junyou.bus.equip.configure.export;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class TaoZhuangXiangWeiConfigExportService extends AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "TaoZhuangXiangWei.jat";

	/**
	 * 最大星数map,key为部位id
	 */
	private Map<Integer, Integer> maxStarMap = new HashMap<>();

	private Map<String, TaoZhuangXiangWeiConfig> configMap = new HashMap<>();
	
	private Set<Integer> buweiSet = new HashSet<>();

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
				ChuanQiLog.error("TaoZhuangXiangWei.jat解析有问题");
			}
		}
	}

	private void createConfig(Map<String, Object> tmp) {
		TaoZhuangXiangWeiConfig config = new TaoZhuangXiangWeiConfig();
		int buWeiId = CovertObjectUtil.object2int(tmp.get("buwei"));
		//容错
		if(buWeiId >=0){
			return ;
		}
		
		int jd = CovertObjectUtil.object2int(tmp.get("id"));
		int star = CovertObjectUtil.object2int(tmp.get("xingshu"));
		config.setBuWeiId(buWeiId);
		config.setJd(jd);
		config.setStar(star);
		config.setConsumeId(CovertObjectUtil.object2String(tmp.get("id1")));
		config.setMallId(CovertObjectUtil.object2String(tmp.get("mallid")));
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
		config.setNeedCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setNeedZsLevel(CovertObjectUtil.object2int(tmp.get("zhushen")));

		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(attrs);
		configMap.put(generateKey(buWeiId, jd, star), config);
		
		buweiSet.add(buWeiId);
		if (maxStarMap.get(buWeiId) == null || maxStarMap.get(buWeiId) < star) {
			maxStarMap.put(buWeiId, star);
		}
	}

	private String generateKey(int buWeiId, int jd, int star) {
		return String.format("%d_%d_%d", buWeiId, jd, star);
	}

	public TaoZhuangXiangWeiConfig getConfigByCondition(int buWeiId, int jd, int star) {
		return configMap.get(generateKey(buWeiId, jd, star));
	}
	
	public Map<String, TaoZhuangXiangWeiConfig> loadAllConfig(){
		return configMap;
	}
	
	public Set<Integer> loadAllBuweiIdSet(){
		return buweiSet;
	}
	
	/**
	 * 
	 *@description:根据传入条件,获取下一个条件的配置 
	 * @param buWeiId
	 * @param jd
	 * @param star
	 * @return
	 */
	public TaoZhuangXiangWeiConfig getNextConfigByCondition(int buWeiId, int jd, int star) {
		Integer maxStar = maxStarMap.get(buWeiId);
		if (maxStar == null) {
			// 没有此部位的配置
			return null;
		}
		
		if (star + 1 > maxStar) {
			// 如果下一个星数大于最大星数,返回下一个阶段的第一个配置
			return configMap.get(generateKey(buWeiId, jd + 1, 1));
		}

		// 返回下一个星数的配置
		return configMap.get(generateKey(buWeiId, jd, star + 1));
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
}
