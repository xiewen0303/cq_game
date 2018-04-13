package com.junyou.gameconfig.shuaguai.configure.export;
 
import java.util.ArrayList;
import java.util.Collection; 
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyList;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 定时刷怪表
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-24 下午5:25:43
 */

@Component
public class DingShiShuaGuaiConfigExportService extends AbsClasspathConfigureParser  {
	
	/**
	 * 默认解析随机坐标的数量
	 */
	private int MAXCOUNT = 200;
	
	private static final String KEY= "zuobiao";
	
	/**
	  * configFileName
	 */
	private String configureName = "DingShiShuaGuaiBiao.jat";
	
	private Map<Integer,DingShiShuaGuaiConfig> configs = null;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<Integer,DingShiShuaGuaiConfig> temp = new ConcurrentHashMap<>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				DingShiShuaGuaiConfig config = createConfig(tmp);
				temp.put(config.getId(), config);
			}
		}
		
		configs = temp;
	}
	 
	
	public DingShiShuaGuaiConfig createConfig(Map<String, Object> tmp) {
		
		DingShiShuaGuaiConfig config = new DingShiShuaGuaiConfig();	

		
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setLine(CovertObjectUtil.object2int(tmp.get("line")));
		config.setHuodong(CovertObjectUtil.object2int(tmp.get("huodong")));
		
		String time1 = CovertObjectUtil.object2String(tmp.get("time1"));
		if(!"".equals(time1)){
			String[] st = time1.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			config.setTime1(new int[]{Integer.parseInt(st[0]),Integer.parseInt(st[1])});
		}
		
		String time2 = CovertObjectUtil.object2String(tmp.get("time2"));
		if(!"".equals(time1)){
			String[] st = time2.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			config.setTime2(new int[]{Integer.parseInt(st[0]),Integer.parseInt(st[1])});
		}
		
		config.setGonggao(CovertObjectUtil.object2int(tmp.get("gonggao")));
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setMonsterId1(CovertObjectUtil.object2String(tmp.get("id1")));
		config.setMonsterId2(CovertObjectUtil.object2String(tmp.get("id2")));
		config.setType1(CovertObjectUtil.object2int(tmp.get("type1")));
		config.setMonsterId2(CovertObjectUtil.object2String(tmp.get("id2")));
		config.setQingguai(CovertObjectUtil.object2boolean(tmp.get("qingguai")));
		String mapStrs = CovertObjectUtil.object2String(tmp.get("map"));
		
		String[]  mapids = mapStrs.split("\\|");
		List<Integer> maps = new ArrayList<>(); 
		for (String mapId : mapids) {
			maps.add(Integer.valueOf(mapId));
		}
		config.setMapId( new ReadOnlyList<>(maps));
		
		String xunlu = CovertObjectUtil.object2String(tmp.get("xunlu"));
		if(!"".equals(xunlu)){
			config.setXlzb(analysisCoord(xunlu));
		}

		List<int[]> coordDatas = new ArrayList<>();
		for (int i = 1; i <= MAXCOUNT; i++) {
			String coordStr = CovertObjectUtil.object2String(tmp.get(KEY+i));
			int[]  coords = analysisCoord(coordStr);
			if(coords == null) {
				break;
			}
			coordDatas.add(coords);
		}
		config.setRandomCoord(new ReadOnlyList<int[]>(coordDatas));
		
		String bgTime = CovertObjectUtil.obj2StrOrNull(tmp.get("notrefresh"));
		if(bgTime != null){
			String[] bgDatas = bgTime.split("-");
			String[] beginDatas = bgDatas[0].split(":");
			String[] endDatas = bgDatas[1].split(":");
			config.setBeginTime(new int[]{CovertObjectUtil.object2Integer(beginDatas[0]),CovertObjectUtil.object2Integer(beginDatas[1])});
			config.setEndTime(new int[]{CovertObjectUtil.object2Integer(endDatas[0]),CovertObjectUtil.object2Integer(endDatas[1])});
		}
		
		return config;
	}
	
	private int[] analysisCoord(String coordStrTmp){
		 
		String[]  coordStr = coordStrTmp.split("\\|"); 
		if(coordStr.length <2) { return null;}
		return new int[]{Integer.valueOf(coordStr[0]),Integer.valueOf(coordStr[1])};
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public Collection<DingShiShuaGuaiConfig> getAllDingShiShuaGuaiConfigs() {
		if(configs == null){
			return null;
		}
		return configs.values();
	}
	
	public DingShiShuaGuaiConfig load(int id) {
		if(configs == null) {
			return null;
		}
		return configs.get(id);
	}


	public Collection<DingShiShuaGuaiConfig> loadAll() {
		return configs.values();
	}
}