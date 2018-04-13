package com.junyou.bus.tongtian.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 升阶表
 */
@Component
public class TongtianRoadConfigExportService extends AbsClasspathConfigureParser {


	private Map<Integer,TongtianRaodConfig > dataMap =new HashMap<>();
	private int maxId; //最大顺序
	private int maxCuilian; //淬炼上限值
	/**
	 * configFileName
	 */
	private String configureName = "ZengJiaShuXing.jat";

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if (data == null) {
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				TongtianRaodConfig tongtianLaodConfig = createConfig(tmp);
				if(tongtianLaodConfig!=null){
					dataMap.put(tongtianLaodConfig.getId(), tongtianLaodConfig);
				}
			}
			
		}
	}

	public int getMaxCuilian() {
		return maxCuilian;
	}
	public void setMaxCuilian(int maxCuilian) {
		this.maxCuilian = maxCuilian;
	}
	public int getMaxId() {
		return maxId;
	}
	public void setMaxId(int maxId) {
		this.maxId = maxId;
	}
	private TongtianRaodConfig createConfig(Map<String, Object> tmp) {
		TongtianRaodConfig config = null;
		String pro = CovertObjectUtil.object2String(tmp.get("pro1"));
		try {
			EffectType.checkContains(pro);//校验属性
			int id = CovertObjectUtil.object2int(tmp.get("id"));
			if(id>maxId){
				maxId = id;//记录最大顺序
			}
			long provalue1 = CovertObjectUtil.object2Long(tmp.get("provalue1"));
			long zplus = CovertObjectUtil.object2Long(tmp.get("zplus"));
			int cuilian = CovertObjectUtil.object2int(tmp.get("cuilian"));
			long maxAttr   = CovertObjectUtil.object2Long(tmp.get("max"));
			long maxZplus = CovertObjectUtil.object2Long(tmp.get("zplusmax"));

			maxCuilian = cuilian;//淬炼值都是一样
			config = new TongtianRaodConfig();
			config.setId(id);
			config.setCuilianValue(cuilian);
			config.setMaxAttr(maxAttr);
			config.setAttrName(pro);
			config.setMaxZplus(maxZplus);
			Map<String, Long> attr  = new HashMap<>();
			attr.put(pro, provalue1);
			attr.put(EffectType.zplus.name(), zplus);
			config.setAttrMap(new ReadOnlyMap<>(attr));
		} catch (Exception e) {
			ChuanQiLog.error("***通天之路jat文件属性名称配置不对***,proName={},e={}",pro,e);
		}
		return config;
	}
     /**
      * 一个索引对应一个map
      * @param tmp
      * @return
      */
	public  Map<Integer, Map<String, Long>> getMap(Map<String, Object> tmp) {
		Map<Integer, Map<String, Long>> intMap = new HashMap<Integer, Map<String, Long>>();
		Map<String, Long> map = null;
		for (EffectType effectType : EffectType.values()) {
			String str = (String) tmp.get(effectType.name());
			if(str==null){
				continue;
			}
			map = new HashMap<>();
			Object[] arr = str.split(GameConstants.CONFIG_SPLIT_CHAR);
			int index = CovertObjectUtil.object2int(arr[0]);
			long val = CovertObjectUtil.object2Long(arr[1]);
			if (val > 0) {
				map.put(effectType.name(), val);
				intMap.put(index, new ReadOnlyMap<>(map));
			}
		}
		return intMap;
	}

	protected String getConfigureName() {
		return configureName;
	}
	
	public Map<Integer,TongtianRaodConfig > loadAllConfig(){
		return dataMap;
	}
	public TongtianRaodConfig loadConfig(int index){
		return dataMap.get(index);
	}
}