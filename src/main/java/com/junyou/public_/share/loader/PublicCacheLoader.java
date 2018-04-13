package com.junyou.public_.share.loader;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.hczbs.entity.Zhengbasai;
import com.junyou.bus.hczbs.entity.ZhengbasaiDayReward;
import com.junyou.bus.hczbs.export.HcZhengBaSaiExportService;
import com.junyou.bus.lianyuboss.export.LianyuBossExortService;
import com.junyou.bus.territory.entity.Territory;
import com.junyou.bus.territory.export.TerritoryExportService;
import com.junyou.public_.jindu.entity.GameJindu;
import com.junyou.public_.jindu.export.GameJinduExportService;
import com.junyou.utils.KuafuConfigPropUtil;
import com.kernel.data.cache.EntityCache;
import com.kernel.data.cache.IEntityCache;
import com.kernel.data.cache.IEntityCacheLoader;

@Component("publicCacheLoader")
public class PublicCacheLoader implements IEntityCacheLoader {

	@Autowired
	private TerritoryExportService territoryExport;
	@Autowired
	private GameJinduExportService gameJinduExportService;
	@Autowired
	private HcZhengBaSaiExportService hcZhengBaSaiExportService;
	@Autowired
	private LianyuBossExortService LianyuBossExortService;
	
	
	
	@Override
	public IEntityCache loadEntityCache(Long identity) {
		IEntityCache entityCache = new EntityCache(identity);
		
		//非跨服初始化
		if(!KuafuConfigPropUtil.isKuafuServer()){
			//领地数据
			List<Territory> territory = territoryExport.initTerritory();
			if(territory != null){
				entityCache.addModelData(territory, Territory.class);
			}
			//天宫之战
			List<Zhengbasai>  zhengbasai  = hcZhengBaSaiExportService.initZhengbasai();
			if(zhengbasai!=null){
				entityCache.addModelData(zhengbasai, Zhengbasai.class);
			}
			//天宫之战--奖励名单
			List<ZhengbasaiDayReward>  zhengbasaiDayRewards  = hcZhengBaSaiExportService.initZhengbasaiDayReward();
			if(zhengbasaiDayRewards!=null){
				entityCache.addModelData(zhengbasaiDayRewards, ZhengbasaiDayReward.class);
			}
			List<GameJindu> gameJindu = gameJinduExportService.initGameJindu();
			if(gameJindu != null){
				entityCache.addModelData(gameJindu, GameJindu.class);
			}
			//门派boss炼狱
			LianyuBossExortService.serverStartInitLianyuBossData();
		}
		
		return entityCache;
	}

}
