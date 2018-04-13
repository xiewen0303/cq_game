package com.junyou.bus.jingji.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.jingji.entity.PaiMingJiangLiConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyList;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class PaiMingJiangLiConfigExportService extends AbsClasspathConfigureParser{

	private String configureName = "PaiMingJiangLi.jat";
	private Map<Integer,PaiMingJiangLiConfig> configs;
	private List<Integer> mingciList;
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, PaiMingJiangLiConfig> configs = new HashMap<>();
		List<Integer> mingciList = new ArrayList<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				PaiMingJiangLiConfig config = createPaiMingJiangLiConfig(tmp);
				configs.put(config.getRank(), config);
				mingciList.add(config.getRank());
			}
		}
		this.configs = configs;
		this.mingciList = new ReadOnlyList<>(mingciList);
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	private PaiMingJiangLiConfig createPaiMingJiangLiConfig(Map<String, Object> tmp){
		PaiMingJiangLiConfig config = new PaiMingJiangLiConfig();
		config.setRank(CovertObjectUtil.object2int(tmp.get("ranking")));
		config.setRongyu(CovertObjectUtil.object2int(tmp.get("jiangitem")));
		config.setExp(CovertObjectUtil.object2int(tmp.get("jiangitem1")));
		config.setMoney(CovertObjectUtil.object2int(tmp.get("jiangitem2")));
		return config;
	}
	/**
	 * list不可修改
	 * @return
	 */
	public List<Integer> getMingCiList(){
		return mingciList;
	}
	
	public PaiMingJiangLiConfig loadByRank(Integer rank){
		return configs.get(rank);
	}
	
}
