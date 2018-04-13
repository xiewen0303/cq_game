package com.junyou.stage.model.stage.fuben;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.publicconfig.configure.export.CampPublicConfig;
import com.junyou.stage.campwar.entity.CampRank;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 阵营战场
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-4-8 上午4:01:15 
 */
public class CampStage extends PublicFubenStage{
	private StageScheduleExecutor scheduleExecutor;
	private int[] camp = new int[]{1,2};//阵营
	private Map<Long, Integer> campMap = null;//Map<玩家Id，所属阵营>
	private Map<Integer, List<CampRank>> rankMap = null;//Map<阵营，list<CampRank>>
	private Map<Integer, Integer> jifenMap = null;//Map<阵营， 总积分>
	private int index = 0;//默认阵营下标
	private Map<Integer, int[]> points = null;//Map<阵营， 默认出生点>
	private Map<Integer, List<Long>> roleMap = null;//Map<阵营，list<玩家Id>>
	private Map<Long,Integer> roleBatter = new HashMap<Long,Integer>();//玩家Id,连续杀人数量
	private long delayTime = 0;
	private long nextRefreshTime=-1;

	public CampStage(String id, Integer mapId, AOIManager aoiManager, PathInfoConfig pathInfoConfig, CampPublicConfig campConfig) {
		super(id, mapId, 1, aoiManager, pathInfoConfig,StageType.CAMP);
		
		this.scheduleExecutor = new StageScheduleExecutor(getId());
		if(campConfig != null){
			delayTime = campConfig.getTime();
			if(campConfig.getCamp() != null && campConfig.getCamp().length >=0){
				//阵营没有则为默认值
				this.camp = campConfig.getCamp();
			}
			
			if(campConfig.getPoint() != null && campConfig.getPoint().size() > 0){
				//阵营所进入的坐标，没有则是地图坐标
				this.points = campConfig.getPoint();
			}
		}
	}
	
	public long getNextRefreshTime() {
		return nextRefreshTime;
	}

	public void setNextRefreshTime(long nextRefreshTime) {
		this.nextRefreshTime = nextRefreshTime;
	}
	
	public void noticeMonsterStatus(){
		Map<Long, IStageElement> roleMap = getBaseStageRoles();
		if(roleMap != null && roleMap.size() > 0){
			Object[] roleIds = new Object[roleMap.size()];
			int i = 0;
			
			for (Long roleId : roleMap.keySet()) {
				roleIds[i++] = roleId;
			}
			long refreshTime = getNextRefreshTime();
			if(refreshTime !=-1 && refreshTime < GameSystemTime.getSystemMillTime()){
				refreshTime = -1;
			}
			StageMsgSender.send2Many(roleIds, ClientCmdType.CMAP_MONSTER_REFRESH, refreshTime);
		}
	}

	/**
	 * 活动结束，排名统计，发放奖励
	 */
	public void startEndRankSchedule(Long expireDelay) {
		if(expireDelay == null) return;
		start();
		StageTokenRunable runable = new StageTokenRunable(null, getId(), InnerCmdType.S_CAMP_RANK, getId());
		scheduleExecutor.schedule(getId(), GameConstants.COMPONENT_CAMP_RANK, runable, expireDelay, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 强制退出（活动结束后，排名已结束）
	 */
	public void startEndLevelSchedule(){
		long expireDelay = 1 * 60 * 1000;//活动结束后，一分钟
		
		StageTokenRunable runable = new StageTokenRunable(null, getId(), InnerCmdType.S_LEVEL_CAMP, getId());
		scheduleExecutor.schedule(getId(), GameConstants.COMPONENT_CAMP_CHECK, runable, expireDelay, TimeUnit.MILLISECONDS);
	}

	@Override
	public void enter(IStageElement element, int x, int y) {
		
		if(ElementType.isRole(element.getElementType())){
			Role role = (Role)element;
			//设置玩家阵营
			if(isCamp(role.getZyCamp())){
				Integer camp = getRoleCamp(role.getId());
				role.setZyCamp(camp);
				
				//进入创建处理
				if(roleMap == null){
					roleMap = new HashMap<>();
				}
				
				List<Long> roles = roleMap.get(camp);
				if(roles == null){
					roles = new ArrayList<>();
				}
				
				roles.remove(role.getId());
				roles.add(role.getId());
				roleMap.put(camp, roles);
			}
			
			StageMsgSender.send2One(role.getId(), ClientCmdType.NOTICE_CAMP, role.getZyCamp());
			
			long refreshTime = getNextRefreshTime();
			if(refreshTime !=-1 && refreshTime < GameSystemTime.getSystemMillTime()){
				refreshTime = -1;
			}
			StageMsgSender.send2One(role.getId(), ClientCmdType.CMAP_MONSTER_REFRESH, refreshTime);
			
		}
		super.enter(element, x, y);
		if(ElementType.isRole(element.getElementType())){
			Role role = (Role)element;
			role.startCampAddExpSchedule(delayTime);
		}
	}
	/**
	 * 获取双方出生点位
	 * @param camp
	 * @return
	 */
	public int[] getPoint(Integer camp){
		if(points != null && points.size() > 0){
			int[] point = points.get(camp);
			if(point != null && point.length > 0){
				return point;
			}
		}
		return null;
	}
	
	/**
	 * 获取玩家所属阵营（没有则创建，只在玩家进入场景中调用）
	 * @return
	 */
	public int getRoleCamp(Long userRoleId){
		if(campMap == null || campMap.size() <= 0 || !campMap.containsKey(userRoleId)){
			int camp = getNextCamp();
			if(campMap == null){
				campMap = new HashMap<>();
			}
			campMap.put(userRoleId, camp);
			return camp;
		}
		return campMap.get(userRoleId);
	}
	
	/**
	 * 获取玩家的下一阵营
	 * @return
	 */
	private int getNextCamp(){
		int campIndex = 0;
		if(camp.length <= index){
			index = 0;
		}else {
			campIndex = index;
		}
		index++;
		
		return camp[campIndex];
	}
	
	/**
	 * 是否有所属阵营
	 * @param camp 阵营
	 * @return
	 */
	private boolean isCamp(Integer camp){
		return camp == null;
	}
	
	public void leave(IStageElement element) {
		super.leave(element);
		
		if(ElementType.isRole(element.getElementType())){
			Role role = (Role)element;
			role.cancelCampAddExpSchedule();
		}
	}
	
	/**
	 * 添加玩家积分
	 * @param userRoleId
	 * @param addJf
	 */
	public void addRoleJifen(Long userRoleId, int addJf,String name){
		if(addJf <= 0 || campMap == null || campMap.size() <= 0){
			return;
		}
		
		if(rankMap == null){
			rankMap = new HashMap<>();
		}
		
		if(jifenMap == null){
			jifenMap = new HashMap<>();
		}
		
		Integer camp = campMap.get(userRoleId);
		List<CampRank> rankList = rankMap.get(camp);
		
		CampRank campRank = null;
		if(rankList != null && rankList.size() > 0){
			
			for (CampRank cr : rankList) {
				if(cr.getUserRoleId().equals(userRoleId)){
					campRank = cr;
					break;
				}
			}
		}else{
			rankList = new ArrayList<>();
		}
		
		if(campRank == null){
			campRank = new CampRank();
			campRank.setUserRoleId(userRoleId);
			campRank.setName(name);
			campRank.setJifen(addJf);
			campRank.setCamp(camp);
		}else{
			rankList.remove(campRank);
			campRank.setJifen(campRank.getJifen() + addJf);
		}
		
		rankList.add(campRank);
		rankMap.put(camp, rankList);
		
		//积分榜
		jifenMap.put(camp, CovertObjectUtil.object2int(jifenMap.get(camp)) + addJf);
	}
	
	/**
	 * 获取排行数据
	 * @return
	 */
	public Map<Integer, List<CampRank>> getRankMap(){
		return rankMap;
	}
	
	/**
	 * 获取该阵营的总积分
	 * @param camp
	 * @return
	 */
	public Integer getCampJifens(Integer camp){
		return jifenMap.get(camp);
	}
	
	/**
	 * 排名后设置排行榜数据
	 * @param list
	 */
	public void setRankList(Integer camp, List<CampRank> list){
		if(rankMap == null){
			rankMap = new HashMap<>();
		}
		
		rankMap.put(camp, list);
	}
	
	/**
	 * 获取玩家的所属阵营
	 * @param userRoleId
	 * @return
	 */
	public Integer getRoleCampById(Long userRoleId){
		return campMap.get(userRoleId);
	}
	
	/**
	 * 在最后排名的时候拉取
	 * @return
	 */
	public List<CampRank> getCampRankList(){
		if(rankMap == null || rankMap.size() <= 0){
			return null;
		}
		
		List<CampRank> list = new ArrayList<>();
		for (Map.Entry<Integer, List<CampRank>> entry : rankMap.entrySet()) {
			for (CampRank campRank : entry.getValue()) {
				list.add(campRank);
			}
		}
		return list;
	}
	public int[] getCamps(){
		return camp;
	}
	/**
	 * 获取赢的阵营
	 * @return null:则没有胜方
	 */
	public Integer getWinCamp(){
		if(jifenMap == null || jifenMap.size() <= 0){
			return null;
		}
		
		//值的第一名的key
		Integer key = null;
		//值的第一名
		int fristVal = 0;
		//值的第一名出现的次数
		int maxValueTimes = 0;
		
		for (Map.Entry<Integer, Integer> entry : jifenMap.entrySet()) {
					
			if(entry.getValue() > fristVal){
				//recode new value
				fristVal = entry.getValue();
				
				key = entry.getKey();
				
				maxValueTimes = 1;
			}else if(entry.getValue() == fristVal){
				maxValueTimes = maxValueTimes + 1;
			}
		}
		
		//is win
		if(maxValueTimes == 1){
			return key;
		}else if(maxValueTimes > 1){
			return -1;
		}
		
		return null;
	}
	
	public Integer getLoseCamp(Integer winCamp){
		if(winCamp.intValue() == camp[0]){
			return camp[1];
		}
		if(winCamp.intValue() == camp[1]){
			return camp[0];
		}
		//-1代表平均
		return -1;
	}
	
	/**
	 * 获得阵营中所有的阵营积分信息
	 * @param camp
	 * @return
	 */
	public List<CampRank> getCampRanks(Integer camp){
		if(rankMap == null || rankMap.size() <= 0){
			return null;
		}
		
		return rankMap.get(camp);
	}
	
	/**
	 * 获取该阵营所有的玩家Id
	 * @param camp
	 * @return
	 */
	public List<Long> getCampRoles(Integer camp){
		if(roleMap == null || roleMap.size() <= 0){
			return null;
		}
		List<Long> list =  roleMap.get(camp);
		if(list == null){
			return new ArrayList<>();
		}
		return list;
	}
	
	public void addRoleBatter(Long userRoleId){
		Integer current = roleBatter.get(userRoleId);
		if(current == null ){
			roleBatter.put(userRoleId, 1);
		}else{
			roleBatter.put(userRoleId, current + 1);
		}
	}
	public void clearRoleBatter(Long userRoleId){
		roleBatter.remove(userRoleId);
	}
	public Integer getRoleBatter(Long userRoleId){
		Integer ret= roleBatter.get(userRoleId);
		if(ret == null){
			return 0;
		}
		return ret;
	}
	/**
	 * 清除阵营记录
	 */
	public void clear() {
		this.camp = new int[]{1,2};//阵营
		this.campMap = null;//Map<玩家Id，所属阵营>
		this.rankMap = null;//Map<阵营，list<CampRank>>
		this.jifenMap = null;//Map<阵营， 总积分>
		this.index = 0;//默认阵营下标
		this.points = null;
		this.roleMap = null;//Map<阵营，list<玩家Id>>
		this.roleBatter.clear();
	}

	@Override
	public boolean isAddPk() {
		return false;
	}

	@Override
	public boolean isCanRemove(){
		return !isOpen() && getAllElements(ElementType.ROLE).size() < 1;
	}

	public void enterNotice(Long userRoleId) {
		StageMsgSender.send2One(userRoleId, ClientCmdType.ENTER_CAMP_WAR, AppErrorCode.OK);
	}
	
	public void exitNotice(Long userRoleId) {
		StageMsgSender.send2One(userRoleId, ClientCmdType.LEVEL_CAMP_WAR, AppErrorCode.OK);
	}
	
	@Override
	public boolean isCanHasTangbao() {
		return false;
	}
	/**
	 * 是否可以携带宠物
	 * @return
	 */
	public boolean isCanHasChongwu(){
		return false;
	}


	@Override
	public boolean isFubenMonster() {
		return false;
	}

	@Override
	public boolean isCanUseShenQi() {
		return false;
	}
}