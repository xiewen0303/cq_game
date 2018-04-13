package com.junyou.gameconfig.map.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 地图传送信息配置
 *@author  DaoZheng Yuan
 *@created 2013-10-16上午10:18:19
 */
@Component
public class ChuanSongConfigExportService extends AbsClasspathConfigureParser {

	private Map<String, ChuanSongConfig> configs;
	
	/**
	 * 配置名
	 */
	private String configureName = "ChuanSongBiao.jat";
	
	protected String getConfigureName() {
		return configureName;
	}
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<String, ChuanSongConfig> configs = new HashMap<>();
		
		for (Object obj:dataList) {
			Map<Object, Object> tmp = (Map<Object, Object>)obj;
			if (null != tmp) {
				
				String wayPointId1 = CovertObjectUtil.object2String(tmp.get("id1"));
				Integer mapId1 = CovertObjectUtil.object2int(tmp.get("map1"));
				Integer mapId2 = CovertObjectUtil.object2int(tmp.get("map2"));
				
				Integer x1 = CovertObjectUtil.object2int(tmp.get("x1"));
				Integer x2 = CovertObjectUtil.object2int(tmp.get("x2"));
				Integer y1 = CovertObjectUtil.object2int(tmp.get("y1"));
				Integer y2 = CovertObjectUtil.object2int(tmp.get("y2"));

				
				ChuanSongConfig chuanSongConfig1 = new ChuanSongConfig();
				chuanSongConfig1.setWayPointId(wayPointId1);
				chuanSongConfig1.setCurMapId(mapId1);
				chuanSongConfig1.setCurX(x1);
				chuanSongConfig1.setCurY(y1);
				//目标点
				chuanSongConfig1.setTargetMapId(mapId2);
				chuanSongConfig1.setTargetX(x2);
				chuanSongConfig1.setTargetY(y2);
				configs.put(chuanSongConfig1.getWayPointId(), chuanSongConfig1);
				
				//传送点  0为显示1 为不显示
				Boolean isHide = CovertObjectUtil.object2boolean(tmp.get("hide"));
				if(!isHide){
					ChuanSongConfig chuanSongConfig2 = new ChuanSongConfig();
					String wayPointId2 = CovertObjectUtil.object2String(tmp.get("id2"));
					chuanSongConfig2.setWayPointId(wayPointId2);
					
					chuanSongConfig2.setCurMapId(mapId2);
					chuanSongConfig2.setCurX(x2);
					chuanSongConfig2.setCurY(y2);
					//目标点
					chuanSongConfig2.setTargetMapId(mapId1);
					chuanSongConfig2.setTargetX(x1);
					chuanSongConfig2.setTargetY(y1);
					
					configs.put(chuanSongConfig2.getWayPointId(), chuanSongConfig2);
				}
				
			}
		}
		this.configs = configs;
	}

	

	/**
	 * 根据ID加载传送信息
	 * @param mapId
	 * @return
	 */
	public ChuanSongConfig loadById(String id){
		return configs.get(id);
	}
}