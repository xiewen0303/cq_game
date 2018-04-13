package com.junyou.bus.xinwen.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 糖宝心纹
 * @author lxn
 *
 */
@Component
public class XinwenConfigExportService extends
		AbsClasspathConfigureParser {

	private String QND_ID = null;
	private String CZD_ID = null;
	private int MAX_JIE = 0;
	private int MAX_CENG = 0;
	private int ACTIVATE_MONEY = 0;
	private int ACTIVATE_ITEM_NUM = 0;
	private String ACTIVATE_ITEM_ID = null;
	/**
	 * configFileName
	 */
	private String configureName = "TangBaoXinWen.jat";

	private Map<Integer, Map<Integer, XinwenJichuConfig>> configMap;
	private Map<Integer, Map<String, Long>> attrMap=new HashMap<Integer, Map<String, Long>>();

	private XinwenJichuConfig createHunPoPeiZhiConfig(
			Map<String, Object> tmp) {
		XinwenJichuConfig config = new XinwenJichuConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setId1(CovertObjectUtil.object2int(tmp.get("id1")));
		config.setId2(CovertObjectUtil.object2int(tmp.get("id2")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setMoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setMallid(CovertObjectUtil.object2String(tmp.get("mallid")));
		config.setGgopen(CovertObjectUtil.object2int(tmp.get("ggopen")));
		config.setQndopen(CovertObjectUtil.object2int(tmp.get("qndopen")));
		config.setQndid(CovertObjectUtil.object2String(tmp.get("qndid")));
		config.setQndmax(CovertObjectUtil.object2int(tmp.get("qndmax")));
		config.setCzdopen(CovertObjectUtil.object2int(tmp.get("czdopen")));
		config.setCzdid(CovertObjectUtil.object2String(tmp.get("czdid")));
		config.setCzdmax(CovertObjectUtil.object2int(tmp.get("czdmax")));

		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));

		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);

		Map<Integer, Map<Integer, XinwenJichuConfig>> configMap = new HashMap<Integer, Map<Integer, XinwenJichuConfig>>();
		List<Integer> idList = new ArrayList<Integer>();
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				XinwenJichuConfig config = createHunPoPeiZhiConfig(tmp);
				QND_ID = config.getQndid();
				CZD_ID = config.getCzdid();
				if (config.getId1() > MAX_JIE) {
					MAX_JIE = config.getId1();
				}
				if (config.getId2() > MAX_CENG) {
					MAX_CENG = config.getId2();
				}
				if (config.getId1() == 0) {
					ACTIVATE_MONEY = config.getMoney();
					ACTIVATE_ITEM_NUM = config.getCount();
					ACTIVATE_ITEM_ID = config.getMallid();
				}
				Map<Integer, XinwenJichuConfig> map = configMap.get(config
						.getId1());
				if (map == null) {
					map = new HashMap<Integer, XinwenJichuConfig>();
					configMap.put(config.getId1(), map);
				}
				map.put(config.getId2(), config);
				attrMap.put(config.getId(), config.getAttrs());
				idList.add(config.getId());
			}
		}
		this.configMap = configMap;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public XinwenJichuConfig load(Integer jie, Integer ceng) {
		Map<Integer, XinwenJichuConfig> map = configMap.get(jie);
		if (map == null) {
			return null;
		}
		return map.get(ceng);
	}
	public Map<Integer, XinwenJichuConfig> loadByJie(Integer jie) {
		Map<Integer, XinwenJichuConfig> map = configMap.get(jie);
		if (map == null) {
			return null;
		}
		return map;
	}

	public String getQND_ID() {
		return QND_ID;
	}

	public String getCZD_ID() {
		return CZD_ID;
	}

	public int getMAX_JIE() {
		return MAX_JIE;
	}

	public int getMAX_CENG() {
		return MAX_CENG;
	}

	public int getACTIVATE_MONEY() {
		return ACTIVATE_MONEY;
	}

	public int getACTIVATE_ITEM_NUM() {
		return ACTIVATE_ITEM_NUM;
	}

	public String getACTIVATE_ITEM_ID() {
		return ACTIVATE_ITEM_ID;
	}

	public Map<String, Long> getAttribute(int jie, int ceng) {
		XinwenJichuConfig config = load(jie, ceng);
		return attrMap.get(config.getId());
	}

}
