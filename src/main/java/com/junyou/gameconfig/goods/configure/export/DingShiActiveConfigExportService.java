package com.junyou.gameconfig.goods.configure.export;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
 

/**
 * 定时活动配置表
 * @author LiuYu
 * @date 2015-4-22 下午8:19:16
 */
@Service
public class DingShiActiveConfigExportService extends AbsClasspathConfigureParser{
	 
	/**
	  * configFileName
	 */
	private String configureName = "DingShiHuoDongBiao.jat";
	
	
	private Map<Integer,DingShiActiveConfig> configs;
	
	private DingShiActiveConfig createDingShiActiveConfig(Map<String, Object> tmp) {
		DingShiActiveConfig config = new DingShiActiveConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setName(CovertObjectUtil.obj2StrOrNull(tmp.get("name")));
		config.setMinLevel(CovertObjectUtil.object2int(tmp.get("minlevel")));
		config.setTongji(CovertObjectUtil.object2boolean(tmp.get("tongji")));
		config.setOpen(CovertObjectUtil.object2boolean(tmp.get("open")));
		
		String startTime = CovertObjectUtil.object2String(tmp.get("starttime"));
		config.setStartTime(startTime);
		String[] st = startTime.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		config.setStartTime1(new int[]{Integer.parseInt(st[0]),Integer.parseInt(st[1])});
		
		String endTime = CovertObjectUtil.object2String(tmp.get("endtime"));
		config.setEndTime(endTime);
		String[] et = endTime.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		config.setEndTime1(new int[]{Integer.parseInt(et[0]),Integer.parseInt(et[1])});
		
		String week = CovertObjectUtil.object2String(tmp.get("week"));
		if(!CovertObjectUtil.isEmpty(week)){
			List<Integer> weekList = new ArrayList<>();
			for (String weekStr : week.split(GameConstants.CONFIG_WEEK_SPLIT_CHAR)) {
				weekList.add(Integer.parseInt(weekStr) + 1);
			}
			config.setWeeks(weekList);
		}
		
		String kfWeek = CovertObjectUtil.object2String(tmp.get("week1"));
		if(!CovertObjectUtil.isEmpty(kfWeek)){
			List<Integer> weekList = new ArrayList<>();
			for (String weekStr : kfWeek.split(GameConstants.CONFIG_WEEK_SPLIT_CHAR)) {
				weekList.add(Integer.parseInt(weekStr) + 1);
			}
			config.setKfWeeks(weekList);
		}
		
		String hfWeek = CovertObjectUtil.object2String(tmp.get("week"));
		if(!CovertObjectUtil.isEmpty(hfWeek)){
			List<Integer> weekList = new ArrayList<>();
			for (String weekStr : hfWeek.split(GameConstants.CONFIG_WEEK_SPLIT_CHAR)) {
				weekList.add(Integer.parseInt(weekStr) + 1);
			}
			config.setHfWeeks(weekList);
		}
		
		config.setOpenDay(CovertObjectUtil.object2int(tmp.get("day")));
		
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setData1(CovertObjectUtil.obj2StrOrNull(tmp.get("data1")));
		config.setData2(CovertObjectUtil.obj2StrOrNull(tmp.get("data2")));
		config.setData3(CovertObjectUtil.obj2StrOrNull(tmp.get("data3")));
		
		config.setMapId(CovertObjectUtil.object2int(tmp.get("map")));
		config.setX(CovertObjectUtil.object2int(tmp.get("x")));
		config.setY(CovertObjectUtil.object2int(tmp.get("y")));
		
		config.setExpBl(CovertObjectUtil.obj2float(tmp.get("expBl1")));
		config.setIsdrop(CovertObjectUtil.object2boolean(tmp.get("dropBl1")));
		
		String maps = CovertObjectUtil.object2String(tmp.get("maps"));
		if(!CovertObjectUtil.isEmpty(maps)){
			List<Integer> mapList = new ArrayList<>();
			for (String mapStr : maps.split(GameConstants.CONFIG_WEEK_SPLIT_CHAR)) {
				mapList.add(Integer.parseInt(mapStr));
			}
			config.setMaps(mapList);
		}
		
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	@Override
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,DingShiActiveConfig> configs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				DingShiActiveConfig config = createDingShiActiveConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
		
		this.configs = configs;
	}
	
	public DingShiActiveConfig getConfig(Integer id){
		return configs.get(id);
	}
	
	public List<DingShiActiveConfig> getAllConfigs(){
		return new ArrayList<>(configs.values());
	}
}