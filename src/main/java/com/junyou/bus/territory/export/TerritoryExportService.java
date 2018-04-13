package com.junyou.bus.territory.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.territory.entity.Territory;
import com.junyou.bus.territory.entity.TerritoryDayReward;
import com.junyou.bus.territory.service.TerritoryService;
@Service
public class TerritoryExportService {
	@Autowired
	private TerritoryService territoryService;
	
	/**
	 * 初始化
	 * @return
	 */
	public List<Territory> initTerritory() {
		return territoryService.initTerritory();
	}
	
	public Map<String, Long> getGuildXunZhangAttr(Long userRoleId){
		return territoryService.getGuildXunZhangAttr(userRoleId);
	}
	/**
	 * 根据地图id获取领地信息(可能为null)
	 * @param mapId
	 * @return
	 */
	public Territory loadTerritoryByMapId(Integer mapId){
		return territoryService.loadTerritoryByMapId(mapId);
	}
	
	public List<TerritoryDayReward> initDayReward(Long userRoleId){
		return territoryService.initDayReward(userRoleId);
	}
	
	public void createTerritory(Long mapId,Long guildId){
		territoryService.createTerritory(mapId,guildId);
	}
	
	public void updateTerritory(Long mapId,long guildId){
		territoryService.updateTerritory(mapId,guildId);
	}
	
	public Object[] getTerritoryRewardInfo(Long userRoleId){
		return territoryService.getTerritoryRewardInfo(userRoleId);
	}
	
	public int getTerritoryRewardState(Long userRoleId){
		return territoryService.territoryHasReward(userRoleId)?1:0; 
	}
	
	public Integer[] getTerritoryMapByGuildId(Long guildId){
		List<Integer> list =territoryService.getTerritories(guildId);
		if(list ==null){
			return null;
		}
		Integer[] ret = new Integer[list.size()];
		for(int i=0;i<list.size();i++){
			ret[i] = list.get(i);
		}
		return ret;
	}
	public void removeGuildXunZhang(List<Long> leaderIds){
		 territoryService.removeGuildXunZhang(leaderIds);
	}
	public void addGuildXunZhang(){
		 territoryService.addGuildXunZhang();
	}
	public void onlineHandle(Long userRoleId){
		territoryService.onlineHandle(userRoleId);
	}
	
	public void resetTerritory(long guildId){
		List<Integer> list = territoryService.getTerritories(guildId); 
		if(list!=null){
			for(Integer e:list){
				territoryService.updateTerritory(e*1L, 0L);
			}
		}
	}
}
