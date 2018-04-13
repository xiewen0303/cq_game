package com.junyou.stage.model.stage.kuafuluandou;

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
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

public class KuafuLuanDouStage extends PublicFubenStage {

	private DaLuanDouHuoDongBiaoConfig daLuanDouHuoDongBiaoConfig;
	
	
	public DaLuanDouHuoDongBiaoConfig getDaLuanDouHuoDongBiaoConfig() {
		return daLuanDouHuoDongBiaoConfig;
	}

	public void setDaLuanDouHuoDongBiaoConfig(DaLuanDouHuoDongBiaoConfig daLuanDouHuoDongBiaoConfig) {
		this.daLuanDouHuoDongBiaoConfig = daLuanDouHuoDongBiaoConfig;
	}

	public KuafuLuanDouStage(String id, Integer mapId, Integer lineNo,
			AOIManager aoiManager, PathInfoConfig pathInfoConfig,
			KuafuLuanDouPublicConfig publicConfig) {
		super(id, mapId, lineNo, aoiManager, pathInfoConfig,StageType.KUAFUDALUANDOU);
		this.publicConfig = publicConfig;
		this.scheduleExecutor = new StageScheduleExecutor(getId());
		super.start();
	}

	private StageScheduleExecutor scheduleExecutor;
	private KuafuLuanDouPublicConfig publicConfig;


	public StageScheduleExecutor getStageScheduleExecutor() {
		return scheduleExecutor;
	}

	public void startForceKickSchedule() {
		
		long cur = GameSystemTime.getSystemMillTime();
		String[] end = daLuanDouHuoDongBiaoConfig.getEndtime().split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		long endTime = DatetimeUtil.getCalcTimeCurTime(cur, Integer.parseInt(end[0]), Integer.parseInt(end[1]), Integer.parseInt(end[2]));
		StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(),InnerCmdType.KUAFULUANDOU_FORCE_KICK, null);
		//写死时间，方便测试
		//long time = 10*60*1000;
		//scheduleExecutor.schedule(getId(),GameConstants.COMPONENT_KUAFU_LUANDOU_FORCE_KICK, runable, endTime- cur, TimeUnit.MILLISECONDS);
		scheduleExecutor.schedule(getId(),GameConstants.COMPONENT_KUAFU_LUANDOU_FORCE_KICK, runable, endTime- cur, TimeUnit.MILLISECONDS);
		
	}

	/**
	 * 定时刷排行榜
	 */
	public void schedulerRefreshRank() {
		StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.KUAFU_LUANDOU_RANK, null);
		scheduleExecutor.schedule(this.getId().toString(),GameConstants.KUAFULUANDOU_REFRESH_RANK, runable,GameConstants.KUAFULUANDOU_RANK_DELAY, TimeUnit.MILLISECONDS);
	}

	
	/**
	 * 
	 */
	public void schedulerQuXiaoFuHou(Long userRoleId) {
		StageTokenRunable runable = new StageTokenRunable(userRoleId, getId(),InnerCmdType.KUAFULUANDOU_QUXIAO_WUDI, userRoleId);
		scheduleExecutor.schedule(userRoleId+"",GameConstants.KUAFULUANDOU_QUXIAO_FH, runable, 5000, TimeUnit.MILLISECONDS);
	}
	
	
	public void cancelForceKickSchedule() {
		scheduleExecutor.cancelSchedule(getId(),GameConstants.COMPONENT_KUAFU_LUANDOU_FORCE_KICK);
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
			/*ChuanQiLog.error("大乱斗发送进入指令1");
			KuafuMsgSender.send2One(element.getId(), ClientCmdType.ENTER_KUAFU_LUANDOU, AppErrorCode.SUCCESS);*/
		}
	}

	@Override
	public void leave(IStageElement element) {
		super.leave(element);
		/*if (ElementType.isRole(element.getElementType())) {
			ChuanQiLog.error("大乱斗发送退出指令1");
			KuafuMsgSender.send2One(element.getId(), ClientCmdType.EXIT_KUAFU_LUANDOU, AppErrorCode.SUCCESS);
		}*/
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
		String[] end = daLuanDouHuoDongBiaoConfig.getEndtime().split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		long endTime = DatetimeUtil.getCalcTimeCurTime(cur, Integer.parseInt(end[0]), Integer.parseInt(end[1]), Integer.parseInt(end[2]));
		
		KuafuMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_LUANDOU, new Object[]{AppErrorCode.SUCCESS,endTime});
	}

	public void exitNotice(Long userRoleId) {
		KuafuMsgSender.send2One(userRoleId, ClientCmdType.EXIT_KUAFU_LUANDOU,new Object[]{AppErrorCode.OK});
	}

	@Override
	public boolean isFubenMonster() {
		return false;
	}

	@Override
	public Short getBackFuHuoCmd() {
		return InnerCmdType.KUAFULUANDOU_AUTO_FUHUO;
	}

	@Override
	public Integer getQzFuhuoSecond() {
		return publicConfig.getFuhuotime();
	}

	private static Random random = new Random();

	public int[] getRevivePoint() {
		List<int[]> zuobiao = publicConfig.getZuobiao1();
		return zuobiao.get(random.nextInt(zuobiao.size()));
	}

	@Override
	public boolean isCanDazuo() {
		return false;
	}

	//private List<KuaFuLuanDouRank> rankList = new ArrayList<>();
	private Map<Long, KuaFuLuanDouRank> rankMap = new HashMap<>();
	/**
	 * 初始化角色积分
	 * @param userRoleId
	 */
	public void initUserScore(KuaFuLuanDouRank rank){
		if(rankMap.get(rank.getUserRoleId()) == null){
			rankMap.put(rank.getUserRoleId(), rank);
		}
	}
	
	public KuaFuLuanDouRank getMyKuaFuLuanDouRank(Long userRoleId){
		return rankMap.get(userRoleId);
	}
	
	/**
	 * 获取大乱斗排行
	 * @return
	 */
	public Object[] getKuaFuLuanDouRank(){
		List<Map.Entry<Long, KuaFuLuanDouRank>> list =  new LinkedList<Map.Entry<Long, KuaFuLuanDouRank>>(rankMap.entrySet() );  
        Collections.sort( list, new Comparator<Map.Entry<Long, KuaFuLuanDouRank>>(){  
	          public int compare( Map.Entry<Long, KuaFuLuanDouRank> o1, Map.Entry<Long, KuaFuLuanDouRank> o2 ){  
	                return (o2.getValue().getScore()).compareTo(o1.getValue().getScore());  
	          }  
        });  
        Object[] returnObj = new Object[5];//排行长度为6
    	for (int i = 0; i < list.size(); i++) {
    		KuaFuLuanDouRank rank = list.get(i).getValue();
    		if(rank == null){
    			break;
    		}
    		rank.setMingci(i+1);
    		if(i < returnObj.length){
        		returnObj[i] = new Object[]{rank.getMingci(),rank.getName(),rank.getScore()};
    		}
    		//推送每个玩家自己的排行
    		KuafuMsgSender.send2One(rank.getUserRoleId(), ClientCmdType.KUAFU_LUANDOU_USER_INFO, new Object[]{rank.getMingci(),rank.getName(),rank.getScore()});
		}
		return returnObj;
	}
	
	public static void main(String[] args) {
		KuaFuLuanDouRank rank1 = new KuaFuLuanDouRank();
		rank1.setName("第1名");
		rank1.setUserRoleId(1234l);
		rank1.setScore(6);
		KuaFuLuanDouRank rank2 = new KuaFuLuanDouRank();
		rank2.setName("第2名");
		rank2.setUserRoleId(234322l);
		rank2 .setScore(5);
		KuaFuLuanDouRank rank3 = new KuaFuLuanDouRank();
		rank3.setName("第3名");
		rank3.setUserRoleId(34234l);
		rank3.setScore(4);
		KuaFuLuanDouRank rank4 = new KuaFuLuanDouRank();
		rank4.setName("第4名");
		rank4.setUserRoleId(44322l);
		rank4.setScore(3);
		KuaFuLuanDouRank rank5 = new KuaFuLuanDouRank();
		rank5.setName("第5名");
		rank5.setUserRoleId(54335l);
		rank5.setScore(2);
		KuaFuLuanDouRank rank6 = new KuaFuLuanDouRank();
		rank6.setName("第6名");
		rank6.setUserRoleId(623432l);
		rank6.setScore(1);
		
		Map<Long, KuaFuLuanDouRank> rankMap = new HashMap<>();
		rankMap.put(rank5.getUserRoleId(),rank5);
		rankMap.put(rank4.getUserRoleId(),rank4);
		rankMap.put(rank6.getUserRoleId(),rank6);
		rankMap.put(rank3.getUserRoleId(),rank3);
		System.out.print("排序前：");
		for (Long id : rankMap.keySet()) {
			System.out.print(id);
			System.out.print(",");
		}
		System.out.println();
		
		List<Map.Entry<Long, KuaFuLuanDouRank>> list =  new LinkedList<Map.Entry<Long, KuaFuLuanDouRank>>(rankMap.entrySet() );  
        Collections.sort( list, new Comparator<Map.Entry<Long, KuaFuLuanDouRank>>(){  
	          public int compare( Map.Entry<Long, KuaFuLuanDouRank> o1, Map.Entry<Long, KuaFuLuanDouRank> o2 ){  
	                return (o2.getValue().getScore()).compareTo(o1.getValue().getScore());  
	          }  
         }); 
        System.out.print("排序后：");
        for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).getValue().getUserRoleId());
			System.out.print(",");
		}
		
        Object[] returnObj = new Object[6];//排行长度为6
       System.out.println(returnObj.length);
	}
	
}