package com.junyou.stage.model.stage.kuafuquanxianyan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.junyou.bus.kuafuluandou.configure.DaLuanDouHuoDongBiaoConfig;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuLuanDouPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuQunXianYanPublicConfig;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

public class KuafuQunXianYanStage extends PublicFubenStage {


	public KuafuQunXianYanStage(String id, Integer mapId, Integer lineNo,
			AOIManager aoiManager, PathInfoConfig pathInfoConfig,
			KuafuQunXianYanPublicConfig publicConfig) {
		super(id, mapId, lineNo, aoiManager, pathInfoConfig,StageType.KUAFUQUNXIANYAN);
		this.publicConfig = publicConfig;
		this.scheduleExecutor = new StageScheduleExecutor(getId());
		super.start();
	}

	private StageScheduleExecutor scheduleExecutor;
	private KuafuQunXianYanPublicConfig publicConfig;
	private List<IMonster> monsters;

	public StageScheduleExecutor getStageScheduleExecutor() {
		return scheduleExecutor;
	}

	public void startForceKickSchedule() {
		
		long cur = GameSystemTime.getSystemMillTime();
		String[] end = publicConfig.getEndTime().split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		long endTime = DatetimeUtil.getCalcTimeCurTime(cur, Integer.parseInt(end[0]), Integer.parseInt(end[1]), Integer.parseInt(end[2]));
		StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(),InnerCmdType.KUAFUQUNXIANYAN_FORCE_KICK, null);
		scheduleExecutor.schedule(getId(),GameConstants.COMPONENT_KUAFU_QUNXIANYAN_FORCE_KICK, runable, endTime- cur, TimeUnit.MILLISECONDS);
		ChuanQiLog.error("启动跨服群仙宴结束定时，定时时间："+(endTime- cur));
	}

	/**
	 * 定时刷排行榜
	 */
	public void schedulerRefreshRank() {
		StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.KUAFU_QUNXIANYAN_RANK, null);
		scheduleExecutor.schedule(this.getId().toString(),GameConstants.KUAFUQUNXIANYAN_REFRESH_RANK, runable,publicConfig.getRankRefresh()*1000, TimeUnit.MILLISECONDS);
	}

	
	/**
	 * 定时刷资源
	 */
	public void schedulerRefreshZiYuan(int time,int zyId,int x,int y) {
		StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.KUAFUQUNXIANYAN_ZIYUAN, new Object[]{zyId,x,y});
		scheduleExecutor.schedule(zyId+"_"+x+"_"+y,GameConstants.KUAFUQUNXIANYAN_REFRESH_ZIYUAN, runable,time*1000, TimeUnit.MILLISECONDS);
	}

	private List<Long> roleIds = new ArrayList<Long>();

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public boolean isRegister(Long userRoleId) {
		return roleIds.contains(userRoleId);
	}

	@Override
	public void enter(IStageElement element, int x, int y) {
		super.enter(element, x, y);
		if (ElementType.isRole(element.getElementType())) {
			if (!roleIds.contains(element.getId())) {
				roleIds.add(element.getId());
			}
		}else if(ElementType.isMonster(element.getElementType())){
			if(null == monsters){
				this.monsters = new ArrayList<IMonster>();
			}
			
			IMonster monster = (IMonster)element;
			this.monsters.add(monster);
			
		}
	}

	@Override
	public void leave(IStageElement element) {
		super.leave(element);
		if (ElementType.isRole(element.getElementType())) {
			cancelAddExpSchedule(element.getId());
		}else if(ElementType.isMonster(element.getElementType())){
			this.monsters.remove(element);
		}
	}

	@Override
	public boolean isAddPk() {
		return false;
	}

	@Override
	public boolean isCanPk() {
		return true;
	}

	@Override
	public boolean isCanHasTangbao() {
		return true;
	}

	@Override
	public boolean isCanHasChongwu() {
		return true;
	}

	@Override
	public boolean isCanUseShenQi() {
		return true;
	}

	@Override
	public boolean isCanRemove() {
		return !isOpen() && getAllRoleIds().length < 1;
	}

	public void enterNotice(Long userRoleId) {
		long cur = GameSystemTime.getSystemMillTime();
		String[] end = publicConfig.getEndTime().split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		long endTime = DatetimeUtil.getCalcTimeCurTime(cur, Integer.parseInt(end[0]), Integer.parseInt(end[1]), Integer.parseInt(end[2]));
		
		KuafuMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_QUNXIANYAN, new Object[]{AppErrorCode.SUCCESS,endTime});
	}

	public void exitNotice(Long userRoleId) {
		KuafuMsgSender.send2One(userRoleId, ClientCmdType.EXIT_KUAFU_QUNXIANYAN,new Object[]{AppErrorCode.OK});
	}
	
	public void startAddExpSchedule(Long userRoleId) {
		StageTokenRunable runable = new StageTokenRunable(userRoleId, getId(),
				InnerCmdType.KUAFUQUNXIANYAN_ADD_EXP_DINGSHI, new Object[] { getId(),
						publicConfig.getJiangexp() });
		scheduleExecutor.schedule(userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_QUNXIANYAN_ADD_EXP, runable,
				publicConfig.getExptime(), TimeUnit.SECONDS);
	}

	private void cancelAddExpSchedule(Long userRoleId) {
		scheduleExecutor.cancelSchedule(userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_QUNXIANYAN_ADD_EXP);
	}

	@Override
	public boolean isFubenMonster() {
		return false;
	}

	@Override
	public Short getBackFuHuoCmd() {
		return InnerCmdType.KUAFUQUNXIANYAN_AUTO_FUHUO;
	}

	@Override
	public Integer getQzFuhuoSecond() {
		return 30;
	}


	@Override
	public boolean isCanDazuo() {
		return false;
	}

	//private List<KuaFuLuanDouRank> rankList = new ArrayList<>();
	private Map<Long, KuaFuQunXianYanRank> rankMap = new HashMap<>();
	/**
	 * 初始化角色积分
	 * @param userRoleId
	 */
	public void initUserScore(KuaFuQunXianYanRank rank){
		if(rankMap.get(rank.getUserRoleId()) == null){
			rankMap.put(rank.getUserRoleId(), rank);
		}
	}
	
	public KuaFuQunXianYanRank getMyKuaFuLuanDouRank(Long userRoleId){
		return rankMap.get(userRoleId);
	}
	
	/**
	 * 获取群仙宴排行
	 * @return
	 */
	public Object[] getKuaFuLuanDouRank(){
		List<Map.Entry<Long, KuaFuQunXianYanRank>> list =  new LinkedList<Map.Entry<Long, KuaFuQunXianYanRank>>(rankMap.entrySet() );  
        Collections.sort( list, new Comparator<Map.Entry<Long, KuaFuQunXianYanRank>>(){  
	          public int compare( Map.Entry<Long, KuaFuQunXianYanRank> o1, Map.Entry<Long, KuaFuQunXianYanRank> o2 ){  
	                return (o2.getValue().getScore()).compareTo(o1.getValue().getScore());  
	          }  
        });  
        Object[] returnObj = new Object[5];//排行长度为6
    	for (int i = 0; i < list.size(); i++) {
    		KuaFuQunXianYanRank rank = list.get(i).getValue();
    		if(rank == null){
    			break;
    		}
    		rank.setMingci(i+1);
    		if(i < returnObj.length){
        		returnObj[i] = new Object[]{rank.getMingci(),rank.getName(),rank.getScore(),rank.getDeadCount()};
    		}
    	}
		return returnObj;
	}
	
	/**获取总排行*/
	public Object[] getKuaFuLuanDouRankWz(){
		List<Map.Entry<Long, KuaFuQunXianYanRank>> list =  new LinkedList<Map.Entry<Long, KuaFuQunXianYanRank>>(rankMap.entrySet() );  
        Collections.sort( list, new Comparator<Map.Entry<Long, KuaFuQunXianYanRank>>(){  
	          public int compare( Map.Entry<Long, KuaFuQunXianYanRank> o1, Map.Entry<Long, KuaFuQunXianYanRank> o2 ){  
	                return (o2.getValue().getScore()).compareTo(o1.getValue().getScore());  
	          }  
        });  
		List<Object[]> relist = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
    		KuaFuQunXianYanRank rank = list.get(i).getValue();
    		if(rank == null){
    			break;
    		}
    		relist.add(new Object[]{rank.getMingci(),rank.getName(),rank.getScore(),rank.getDeadCount()});
		}
		return relist.toArray();
	}
	
	public Map<Long, Integer> getStageRankMap(){
		List<Map.Entry<Long, KuaFuQunXianYanRank>> list =  new LinkedList<Map.Entry<Long, KuaFuQunXianYanRank>>(rankMap.entrySet() );  
        Collections.sort( list, new Comparator<Map.Entry<Long, KuaFuQunXianYanRank>>(){  
	          public int compare( Map.Entry<Long, KuaFuQunXianYanRank> o1, Map.Entry<Long, KuaFuQunXianYanRank> o2 ){  
	                return (o2.getValue().getScore()).compareTo(o1.getValue().getScore());  
	          }  
        }); 
        Map<Long, Integer> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
    		KuaFuQunXianYanRank rank = list.get(i).getValue();
    		if(rank == null){
    			continue;
    		}
    		map.put(rank.getUserRoleId(), i+1);
		} 
        return map;
	}
	
	public String getUserServerId(Long userRoleId){
		KuaFuQunXianYanRank rank = rankMap.get(userRoleId);
		if(rank == null){
			return null;
		}
		return rank.getServerId();
	}
	
}