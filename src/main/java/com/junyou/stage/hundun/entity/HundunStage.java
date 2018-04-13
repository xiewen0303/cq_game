package com.junyou.stage.hundun.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.publicconfig.configure.export.HundunPublicConfig;
import com.junyou.stage.hundun.configure.export.HunDunWarGuiZeBiaoConfig;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.skill.buff.BuffFactory;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author LiuYu
 * 2015-9-1 下午4:58:31
 */
public class HundunStage extends PublicFubenStage{
	
	private StageScheduleExecutor scheduleExecutor;
	private long cengExpiredTime;//本层的过期检测时间
	private Map<Long, HundunRank> roleJFMap = new HashMap<>();//玩家积分Map<玩家Id, 玩家总积分>
	private Integer cengId;//层Id
	private long nextExpiredTime;//下一层的过期时间
	private HunDunWarGuiZeBiaoConfig hdConfig;
	private Set<Long> canEnterUsers;
	private List<HundunRank> list;//本层排名
	private long nextRefreshTime;//下一次刷新排行时间
	private HundunPublicConfig publicConfig;
	private Object[] enterSuccess;
	
	
//	@Override
//	public void enter(IStageElement element, int x, int y) {
//		super.enter(element, x, y);
//		if(ElementType.isRole(element.getElementType())){
//			Role role = (Role)element;
//			IBuff buff = BuffFactory.create(role, role, GameConstants.SF_BUFF);
//			role.getBuffManager().addBuff(buff);
//			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
//		}
//	}
	public HundunStage(String id, Integer mapId, Integer lineNo,AOIManager aoiManager, PathInfoConfig pathInfoConfig,HunDunWarGuiZeBiaoConfig hdConfig,DingShiActiveConfig config,HundunPublicConfig publicConfig) {
		super(id, mapId, lineNo, aoiManager, pathInfoConfig, StageType.CHAOS);
		this.scheduleExecutor = new StageScheduleExecutor(getId());
		this.cengId = hdConfig.getId();
		enterSuccess = new Object[]{1,cengId};
		long expireTime = config.getCalcEndSecondTime() + GameConstants.SPRING_DINGSHI_ERRER_TIME;
		this.hdConfig = hdConfig;
		this.publicConfig = publicConfig;
		long second = hdConfig.getSecond();
		if(second > 0){
			this.cengExpiredTime = hdConfig.getSecond();
			
			//定时检测开启
			long min = config.getCalcStartSecondTime();
			if(min > 0){
				if(second > min){
					second = second - min;
				}else{
					second = second - min%second;
				}
			}
			scheduleCheck(second);
		}else{
			this.cengExpiredTime = GameSystemTime.getSystemMillTime() + expireTime;
		}
		
	}
	@Override
	public void start(){
		if(!isOpen()){
			super.start();
			StageMsgSender.send2Bus(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DINGSHI_HUNDUN_ADD_EXP, getId());
		}
	}

	@Override
	public void enterNotice(Long userRoleId) {
		//玩家进入地图成功后，发送结束时间戳
		StageMsgSender.send2One(userRoleId, ClientCmdType.CHAOS_ENTER, enterSuccess);
		StageMsgSender.send2One(userRoleId, ClientCmdType.CHAOS_END_TIME, cengExpiredTime);
	}
	

	@Override
	public void exitNotice(Long userRoleId) {
		StageMsgSender.send2One(userRoleId, ClientCmdType.CHAOS_EXIT, AppErrorCode.OK);
	}

	@Override
	public boolean isFubenMonster() {
		return false;
	}
	public Integer getCengId() {
		return cengId;
	}
	@Override
	public Short getSameMoCmd(){
		if(cengId < 4){
			return ClientCmdType.AOI_HUNDUN_SAME_CMD;
		}else{
			return null;
		}
	}
	/**
	 * 设置下一层的开启时间
	 * @param nextExpiredTime
	 */
	public void setNextExpiredTime(long nextExpiredTime) {
		this.nextExpiredTime = nextExpiredTime;
	}
	
	public long getCengExpiredTime() {
		return cengExpiredTime;
	}
	/**
	 * 获取所有玩家的积分榜
	 * @return
	 */
	public Map<Long, HundunRank> getRoleJfs(){
		return roleJFMap;
	}
	/**
	 * 添加玩家积分
	 * @param userRoleId
	 * @param addJf
	 */
	public void addRoleJifen(Long userRoleId, ElementType type, String userName){
		if(!isOpen()){
			return;
		}
		int addJf = 0;
		if(ElementType.isRole(type)){
			addJf = publicConfig.getRoleJifen();
		}else if(ElementType.isMonster(type)){
			addJf = publicConfig.getMonsterJifen();
		}else{
			return;
		}
		
		if(roleJFMap == null){
			roleJFMap = new HashMap<>();
		}
		
		HundunRank rank = roleJFMap.get(userRoleId);
		if(rank == null){
			rank = new HundunRank();
			rank.setUserRoleId(userRoleId);
			rank.setUserName(userName);
			roleJFMap.put(userRoleId, rank);
		}else{
			rank.setJfVal(rank.getJfVal() + addJf);
		}
		rank.setUpdateTime(GameSystemTime.getSystemMillTime());
		
		StageMsgSender.send2One(userRoleId, ClientCmdType.CHAOS_NOTICE_SELF_SCORE_CHANGE, rank.getJfVal());
	}
	
	/**
	 * 减少积分
	 * @param role
	 */
	public void desrJifen(Role role){
		if(!isOpen()){
			return;
		}
		HundunRank rank = roleJFMap.get(role.getId());
		if(rank == null){
			return;
		}
		rank.setJfVal((int)(rank.getJfVal() * publicConfig.getShengyu()));
		rank.setUpdateTime(GameSystemTime.getSystemMillTime());
		StageMsgSender.send2One(role.getId(), ClientCmdType.CHAOS_NOTICE_SELF_SCORE_CHANGE, rank.getJfVal());
		//判断积分是否可用，不可用则删除，反之则重置
		if(rank.getJfVal() <= 0){
			roleJFMap.remove(role.getId());
		}
	}
	
	public void leave(IStageElement element) {
		super.leave(element);
		
		if(ElementType.isRole(element.getElementType())){
			roleJFMap.remove(element.getId());
		}
	}
	
	/**
	 * 定时验证
	 * @param time
	 */
	public void scheduleCheck(long time){
		if(time > 0){
			cengExpiredTime = GameSystemTime.getSystemMillTime() + time;
			StageTokenRunable runable = new StageTokenRunable(null, getId(), InnerCmdType.DINGSHI_HUNDUN_CHECK, getId());
			scheduleExecutor.schedule(getId(), GameConstants.COMPONENT_CHAOS_CHECK, runable, time, TimeUnit.MILLISECONDS);
		}
	}
	
	public int getExpRate() {
		return hdConfig.getData1();
	}
	public HunDunWarGuiZeBiaoConfig getHdConfig() {
		return hdConfig;
	}
	public boolean isOpenEnterNext(){
		return GameSystemTime.getSystemMillTime() < nextExpiredTime;
	}
	public boolean isCanEnter(Long userRoleId) {
		return canEnterUsers != null && canEnterUsers.contains(userRoleId);
	}
	public void setCanEnterUsers(Set<Long> canEnterUsers) {
		this.canEnterUsers = canEnterUsers;
	}
	
	/**
	 * 重新排序
	 * @param roleRankMap 玩家未排序的积分
	 * @param minNeed
	 * @param mustRefresh	是否强制刷新
	 * @return
	 */
	public List<HundunRank> reRankIngChaos(boolean mustRefresh){
		if(roleJFMap != null && roleJFMap.size() > 0){
			if(!mustRefresh && GameSystemTime.getSystemMillTime() <= nextRefreshTime){
				return list;
			}
			List<HundunRank> list = new ArrayList<>();
			for (Map.Entry<Long, HundunRank> entry : new ArrayList<>(roleJFMap.entrySet())) {
				
				//验证上榜最低积分要求
				HundunRank rank = entry.getValue();
				if(hdConfig.getMinneed() < rank.getJfVal()){
					list.add(rank);
				}else{
					rank.setRank(0);
				}
			}
			
			if(list.size() <= 0){
				return null;
			}
			nextRefreshTime = GameSystemTime.getSystemMillTime() + GameConstants.CHAOS_REFRESH_TIME;
			//再排序
			Collections.sort(list, new Comparator<HundunRank>(){
				@Override
				public int compare(HundunRank o1, HundunRank o2) {
					if(o2.getJfVal() > o1.getJfVal()){
						return 1;
					}else if(o2.getJfVal() == o1.getJfVal() && o2.getUpdateTime() > o1.getUpdateTime()){
						return 1;
					}else if(o2.getJfVal() == o1.getJfVal() && o2.getUpdateTime() == o1.getUpdateTime()){
						return 0;
					}else{
						return -1;
					}
				}
			});
			for (int i = 0; i < list.size(); i++) {
				HundunRank rank = list.get(i);
				rank.setRank(i + 1);
			}
			this.list = list;
			return list;
		}
		return null;
	}
	
	public int getRank(Long userRoleId){
		HundunRank rank = roleJFMap.get(userRoleId);
		if(rank != null){
			return rank.getRank();
		}
		return 0;
	}
	@Override
	public boolean isAddPk() {
		return false;
	}
	@Override
	public boolean isCanFeijian() {
		return false;
	}
	@Override
	public boolean isCanDazuo() {
		return false;
	}
	@Override
	public boolean isCanJump() {
		return false;
	}
	@Override
	public boolean isCanHasTangbao() {
		return false;
	}
	@Override
	public boolean isCanHasChongwu() {
		return false;
	}
	@Override
	public boolean isCanUseShenQi() {
		return false;
	}
	public Short getBackFuHuoCmd(){
		return ClientCmdType.CHAOS_PUTONG_FUHUO;
	}
	
	
	
}
