package com.junyou.stage.collect.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.junyou.stage.collect.configure.DingShiShuaXiangConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class DingShiShuaXiangConifgExport extends AbsClasspathConfigureParser {
	/**
	 * 默认解析随机坐标的数量
	 */
	private int MAXCOUNT = 200;
	
	private static final String KEY= "zuobiao";
	
	private String configureName = "DingShiShuaXiangBiao.jat";
	
	private DingShiShuaXiangConfig dingShiShuaXiangConfig;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				this.dingShiShuaXiangConfig=createConfig(tmp);
				//只配一条
				break;
			}
		}
	}

	private DingShiShuaXiangConfig createConfig(Map<String, Object> tmp) {
		DingShiShuaXiangConfig config=new DingShiShuaXiangConfig();
		config.setId1(CovertObjectUtil.object2int(tmp.get("id1")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setLine(CovertObjectUtil.object2int(tmp.get("line")));
		config.setJiangItem(CovertObjectUtil.object2String(tmp.get("jiangitem")));
		
	/*	config.setHuodong(CovertObjectUtil.object2int(tmp.get("huodong")));*/
		
	/*	String time1 = CovertObjectUtil.object2String(tmp.get("time1"));
		if(!"".equals(time1)){
			String[] st = time1.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			config.setTime1(new int[]{Integer.parseInt(st[0]),Integer.parseInt(st[1])});
		}*/
		
		/*String time2 = CovertObjectUtil.object2String(tmp.get("time2"));
		if(!"".equals(time1)){
			String[] st = time2.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			config.setTime2(new int[]{Integer.parseInt(st[0]),Integer.parseInt(st[1])});
		}*/
		
		config.setGonggao(CovertObjectUtil.object2int(tmp.get("gonggao")));
		config.setJiange(CovertObjectUtil.object2int(tmp.get("jiange"))*1000);
		config.setTime3(CovertObjectUtil.object2int(tmp.get("time3"))*1000);
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setMap(CovertObjectUtil.object2int(tmp.get("map")));
		String xunlu = CovertObjectUtil.object2String(tmp.get("xunlu"));
		if(!"".equals(xunlu)){
			config.setXunlu(analysisCoord(xunlu));
		}
		List<Integer[]> coordDatas = new ArrayList<>();
		for (int i = 1; i <= MAXCOUNT; i++) {
			String coordStr = CovertObjectUtil.object2String(tmp.get(KEY+i));
			Integer[]  coords = analysisCoord(coordStr);
			if(coords == null) {
				break;
			}
			coordDatas.add(coords);
		}
		config.setZuobiaoList(coordDatas);
		return config;
	}
	
	private Integer[] analysisCoord(String coordStrTmp){
		 
		String[]  coordStr = coordStrTmp.split("\\|"); 
		if(coordStr.length <2) { return null;}
		return new Integer[]{Integer.valueOf(coordStr[0]),Integer.valueOf(coordStr[1])};
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public DingShiShuaXiangConfig getDingShiShuaXiangConfig() {
		return dingShiShuaXiangConfig;
	}

}
