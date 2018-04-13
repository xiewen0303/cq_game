package com.junyou.bus.jingji.export;
//package com.junyou.bus.jingji.exoprt;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.stereotype.Service;
//
//import com.junyou.bus.jingji.entity.MingCiZhanShiConfig;
//import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
//import com.junyou.gameconfig.utils.GameConfigUtil;
//import com.junyou.utils.collection.ReadOnlyList;
//import com.junyou.utils.common.CovertObjectUtil;
//@Service
//public class MingCiZhanShiConfigExportService {//extends AbsClasspathConfigureParser{
//
//	private String configureName = "MingCiZhanShi.jat";
//	private Map<Integer, MingCiZhanShiConfig> configs;
//	private List<Integer> mingciList;
//
//	protected void configureDataResolve(byte[] data) {
//		Object[] dataList = GameConfigUtil.getResource(data);
//		Map<Integer, MingCiZhanShiConfig> configs = new HashMap<>();
//		List<Integer> mingciList = new ArrayList<>();
//		for (Object obj : dataList) {
//			Map<String, Object> tmp = (Map<String, Object>)obj;
//			if (null != tmp) {
//				MingCiZhanShiConfig config = createMingCiZhanShiConfig(tmp);
//				configs.put(config.getPaiming(), config);
//				mingciList.add(config.getPaiming());
//			}
//		}
//		this.configs = configs;
//		this.mingciList = new ReadOnlyList<>(mingciList);
//	}
//
//	protected String getConfigureName() {
//		return configureName;
//	}
//
//	private MingCiZhanShiConfig createMingCiZhanShiConfig(Map<String, Object> tmp){
//		MingCiZhanShiConfig config = new MingCiZhanShiConfig();
//		config.setPaiming(CovertObjectUtil.object2int(tmp.get("paim")));
//		config.setOne(CovertObjectUtil.object2int(tmp.get("paim1")));
//		config.setTwo(CovertObjectUtil.object2int(tmp.get("paim2")));
//		config.setThree(CovertObjectUtil.object2int(tmp.get("paim3")));
//		return config;
//	}
//	/**
//	 * list不可修改
//	 * @return
//	 */
//	public List<Integer> getMingCiList(){
//		return mingciList;
//	}
//
//	public MingCiZhanShiConfig loadByMingci(Integer mingci){
//		return configs.get(mingci);
//	}
//
//
//}
