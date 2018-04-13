package com.junyou.stage.model.core.stage.aoi;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.core.stage.IStageElement;

/**
 * @description 阵营关系辅助类
 * @author ShiJie Chi
 * @date 2012-3-12 下午2:55:22 
 */
public class CampRelationHelper {
	
	public Map<Integer,Map<Long, IStageElement>> enemyMap = new ConcurrentHashMap<>();
	
	public void add(IStageElement element){
		
		List<Integer> enemyCamps = StageConfigureHelper.getCampRelationExportService().getEnemyCamps(element.getCamp());
		if(null != enemyCamps){
			for(Integer tmp : enemyCamps){
				Map<Long,IStageElement> elementMap  = enemyMap.get(tmp);
				if(null == elementMap){
					elementMap = new ConcurrentHashMap<Long, IStageElement>();
					enemyMap.put(tmp, elementMap);
				}
				elementMap.put(element.getId(), element);
				
			}
		}
		
	}
	
	public Collection<IStageElement> getEnemies(Integer camp) {
		
		Map<Long, IStageElement> enemies = enemyMap.get(camp);
		if(null != enemies && enemies.size() > 0){
			return enemies.values();
		}
		
		return null;
	}

	public void remove(IStageElement element){
		
		List<Integer> enemyCamps = StageConfigureHelper.getCampRelationExportService().getEnemyCamps(element.getCamp());
		
		if(null != enemyCamps){
			for(Integer tmp : enemyCamps){
				Map<Long,IStageElement> elementMap  = enemyMap.get(tmp);
				if(null != elementMap){
					elementMap.remove(element.getId());
				}
			}
		}
	}
}
