package com.junyou.public_.rank.manage;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.public_.rank.export.RankExportService;


@Component
public class RankManage  {

	@Autowired
	private RankExportService rankExportService;
	
	/**刷新间隔*/
	public static final long INTERVAL_TIME=1000*60*60;
	
	/**排行统计上限*/
	public static final int STATIS_LIMIT=100;
	
	private static RankManage instance;
	
	private boolean isStarted=false;
	private RankManage(){
		
	}
	
	public static RankManage getInstance(){
		if(instance==null){
			instance=new RankManage();
		}
		return instance;
	}
	
	public void initRank(){
		if(isStarted){
			return;
		}
		rankExportService.refreshRank();
		
		isStarted=true;
	}

/*	public void startRefresh(){
		if(isStarted){
			return;
		}
		
		new Timer("RefreshRankTimer").schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				List<ILevelRankVo>  levelRankVos=levelRankExportService.getLevelRankVo(STATIS_LIMIT);
				List<IFightingRankVo> fightingRankVos=fightingRankExportService.getFightingRankVo(STATIS_LIMIT);
				List<IZuoqiRankVo> zuoqiRankVos=zuoqiRankExportService.getZuoqiRankVo(STATIS_LIMIT);
				
				
				
				rankExportService.refreshRank(RankType.LEVEL, levelRankVos);
				rankExportService.refreshRank(RankType.FIGHTING, fightingRankVos);
				rankExportService.refreshRank(RankType.ZUOQI, zuoqiRankVos);
				
			}
		}, 1, INTERVAL_TIME);//TODO setting time
		
		isStarted=true;

	}*/
	
	
}
