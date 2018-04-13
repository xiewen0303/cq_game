/**
 * 
 */
package com.junyou.gameconfig.map.configure.export;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.DirType;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.constants.PathFinderType;
import com.junyou.gameconfig.constants.ResourceType;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.export.ResourceRefreshConfig;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @description 
 * @author ShiJie Chi
 * @created 2011-12-8下午5:06:43
 */
@Component
public class MapConfigExportService extends AbsClasspathConfigureParser{

	/**
	 * 配置名
	 */
	private String configureName = "maplist.bin";
	
	private Map<Integer, MapListConfig> configs;
	private Map<Integer, MapConfig> mapConfigs;
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] maplistData = GameConfigUtil.getResource(data);
		Map<Integer, MapListConfig> configs = new HashMap<>();
		Map<Integer, MapConfig> mapConfigs = new HashMap<>();
		for(Object obj : maplistData){
			Map<Object, Object> tmp = (Map<Object, Object>)obj;
			String id = (String)tmp.get("id");//id
			Integer mapId = Integer.parseInt(tmp.get("mapid").toString());//mapid
			String monster = (String)tmp.get("monster");//monster
			String path = (String)tmp.get("path");//path
			String collect = (String)tmp.get("collect");//collect
			Integer type = (Integer)tmp.get("type");//寻路类型
			
			MapListConfig mapListConfig = new MapListConfig();
			mapListConfig.setMapId(mapId);
			mapListConfig.setMonsterPath(monster);
			mapListConfig.setPathInfoPath(path);
			mapListConfig.setCollectPath(collect);
			configs.put(mapListConfig.getMapId(), mapListConfig);
			
			MapConfig mapConfig = new MapConfig();
			mapConfig.setMapId(mapId);
			
			if(null != path) {
				String pathInfoPath = id + "/" + path;
				Object[] signInfo = getLoader().loadSign(pathInfoPath, DirType.MAPRESOURCE);
				byte[] pathInfoData = getLoader().load((String)signInfo[1]);
				if (null != pathInfoData) {
					PathInfoConfig pathInfoConfig = new PathInfoConfig();
					try {
						ByteArrayInputStream bin = new ByteArrayInputStream(pathInfoData);
						DataInputStream d_in = new DataInputStream(bin);
						
						pathInfoConfig.setWidth(d_in.readInt());
						pathInfoConfig.setHeight(d_in.readInt());
						pathInfoConfig.setPathFinderType(null == type ? PathFinderType.A : type == 1 ? PathFinderType.AExtend : PathFinderType.A);
						pathInfoConfig.initXsWidthHeight();
						
						int x=0,y=0,value;
						while((value=d_in.read())!=-1){
							pathInfoConfig.addPathNode(x,y,value);
							
							// 检查坐标所在行
							if( ( ++x % (pathInfoConfig.getWidth()) ) == 0 ){
								x=0;
								++y;
							}
						}
						pathInfoConfig.preprocess();
						mapConfig.setPathInfoConfig(pathInfoConfig);
					} catch (Exception e) {
						throw new RuntimeException("can't initialize 'path.mm' at '"+mapId + "," + path+"',pls check it.");
					}
				}
			}
			
			List<ResourceRefreshConfig> configList = new ArrayList<ResourceRefreshConfig>();
			if(null != monster && !"".equals(monster)){
				String monsterPath = id + "/" + monster;
				Object[] signInfo = getLoader().loadSign(monsterPath, DirType.MAPRESOURCE);
				byte[] monsterData = getLoader().load((String)signInfo[1]);
				if (null != monsterData) {
					Object[] o = GameConfigUtil.getResource(monsterData);
					for (Object monsterObj:o) {
						Map configMap = (Map)monsterObj;
						ResourceRefreshConfig config = new ResourceRefreshConfig();
						config.setResourceType(ResourceType.RESOURCE_TYPE_MONSTER);
						config.setMaxCount((Integer) configMap.get("maxCount"));
						
						int mrTime = CovertObjectUtil.object2int(configMap.get("createTime"));
						if(mrTime < 500){
							//怪物配置最小容错值 ydz-2015-8-25
							mrTime = 500;
						}
						config.setRefreshInterval(mrTime);
						config.setTeamId(CovertObjectUtil.object2String(configMap.get("team")));
						config.setResourceId((String) configMap.get("id"));
						config.setResourceName(CovertObjectUtil.object2String(configMap.get("name")));
			
						Object[] points = (Object[])configMap.get("createPoints");
						List<Integer[]> pointList = new ArrayList<Integer[]>();
						for(Object point : points){
							Object[] pointO = (Object[]) point;
							
							int faceTo = -1;
							if(pointO.length > 2){
								faceTo = CovertObjectUtil.object2int(pointO[2]);
							}
							
							Integer[] p = new Integer[]{CovertObjectUtil.object2int(pointO[0]),CovertObjectUtil.object2int(pointO[1]),faceTo};
							pointList.add(p);
						}
						config.setPoint(pointList);
						if(config.getResourceId() != null){
							configList.add(config);
						}
						
						mapConfig.addMapAllMonsters(config.getResourceId(), config.getMaxCount());//添加地图内的怪物
					}
				}
			}
			
			mapConfig.setResourceRefreshConfigs(configList);
			mapConfigs.put(mapConfig.getMapId(), mapConfig);
		}			
		this.configs = configs;
		this.mapConfigs = mapConfigs;
	}

	
	public ResourceRefreshConfig createMapResourceRefreshConfig(String monsterId,int x,int y,int teamId){
		ResourceRefreshConfig config = new ResourceRefreshConfig();
		config.setResourceType(ResourceType.RESOURCE_TYPE_MONSTER);
		config.setMaxCount(1);
		config.setRefreshInterval(888888);
		config.setTeamId(teamId+"");
		config.setResourceId(monsterId);

		List<Integer[]> pointList = new ArrayList<Integer[]>();
		Integer[] p = new Integer[]{x,y};
		pointList.add(p);
		config.setPoint(pointList);
		
		return config;
	}
	
	public MapConfig load(int mapId) {
		return mapConfigs.get(mapId);
	}

	public List<MapListConfig> loadAll() {
		return new ArrayList<>(configs.values());
	}
}
