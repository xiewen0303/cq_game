package com.junyou.bus.xiuxian.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.xiuxian.entity.RefbXiuxian;

/**
 * 热发布全服数量记录管理
 * @author DaoZheng Yuan
 * 2015年6月7日 上午11:50:21
 */
public class RefbXiuXianManager {

	private static final RefbXiuXianManager INSTANCE = new RefbXiuXianManager();
			
	/**
	 * key:子活动id
	 * value:全服购买记录的数据
	 */
	private Map<Integer,Map<Integer,RefbXiuxian>> serverBuys = new HashMap<>();
	
	
	public static RefbXiuXianManager getInstance() {
		return INSTANCE;
	}
	
	/**
	 * 是否查询过 
	 * @param subId
	 * @return true:查询过,false:没查询过
	 */
	public boolean isQuery(int subId){
		return serverBuys.containsKey(subId);
	}
	
	/**
	 * 加入新的子活动id结果集
	 * @param list
	 * @param subId
	 */
	public void initSubIdList(int subId,List<RefbXiuxian> list){
		if(list == null){
			serverBuys.put(subId, null);
		}else{
			Map<Integer,RefbXiuxian> map = new HashMap<>();
			for (RefbXiuxian refbXiuxian : list) {
				map.put(refbXiuxian.getConfigId(), refbXiuxian);
			}
			serverBuys.put(subId, map);
		}
	}
	
	
	/**
	 * 记录加入管理 
	 * @param subId
	 * @param refbXiuxian
	 */
	public void addRefbXiuxianBySubId(int subId,RefbXiuxian refbXiuxian){
		Map<Integer,RefbXiuxian> map = serverBuys.get(subId);
		if(map == null){
			map = new HashMap<>();
			serverBuys.put(subId, map);
		}
		
		map.put(refbXiuxian.getConfigId(), refbXiuxian);
	}
	
	/**
	 * 获取全服出售记录
	 * @param subId 子活动id
	 * @param configId 物品配置id(非道具id) 
	 * @return
	 */
	public RefbXiuxian getRefbXiuxianBySubIdAndConfigId(int subId,int configId){
		Map<Integer,RefbXiuxian> map = serverBuys.get(subId);
		if(map != null){
			return map.get(configId);
		}else{
			return null;
		}
	}
}
