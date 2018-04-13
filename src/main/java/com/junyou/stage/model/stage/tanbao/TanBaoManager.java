package com.junyou.stage.model.stage.tanbao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 探宝活动管理器
 * @author LiuYu
 * 2015-6-16 下午5:54:42
 */
public class TanBaoManager {
	private static TanBaoManager manager = new TanBaoManager();
	private Map<Long,TanBaoRoleVo> roleVoMap = new HashMap<>();
	private List<TanBaoRoleVo> rankList = new ArrayList<>();
	private List<TanBaoStage> activeStages = new ArrayList<>();
	
	private long updateTime;
	private boolean start;
	private int level;
	private Integer mapId;
	private Object[] enterSucessMsg;
	private Long stopTime;
	
	public static TanBaoManager getManager(){
		return manager;
	}
	
	public void activeStart(int level){
		if(start){
			return;
		}else if(activeStages.size() < 0){
			return;
		}
		start = true;
		this.level = level;
	}
	
	public void setStopTime(Long stopTime) {
		this.stopTime = stopTime;
	}

	public void addStage(TanBaoStage stage){
		activeStages.add(stage);
		StageManager.addStageCopy(stage);
		mapId = stage.getMapId();
	}
	
	public boolean isStarting(){
		return start;
	}
	
	public void activeStop(){
		start = false;
		for (TanBaoStage stage : activeStages) {
			stage.stop();
		}
	}
	
	public Integer getMapId(){
		return mapId;
	}
	
	public void clear(){
		roleVoMap.clear();
		rankList.clear();
		for (TanBaoStage stage : activeStages) {
			if(stage.isCanRemove()){
				StageManager.removeCopy(stage);
			}
		}
		mapId = null;
		activeStages.clear();
	}
	
	public void checkRoleVo(RoleWrapper role){
		if(!roleVoMap.containsKey(role.getId())){
			TanBaoRoleVo roleVo = new TanBaoRoleVo();
			roleVo.setUserRoleId(role.getId());
			roleVo.setName(role.getName());
			roleVo.setExp(0);
			roleVo.setDeadTime(0);
			roleVo.setScore(0);
			roleVo.setEnterTime(GameSystemTime.getSystemMillTime());
			roleVoMap.put(role.getId(), roleVo);
			roleVo.setRank(roleVoMap.size());
			rankList.add(roleVo);
		}
	}
	
	public TanBaoRoleVo getRoleVo(Long userRoleId){
		return roleVoMap.get(userRoleId);
	}
	
	private void sort(){
		if(updateTime > GameSystemTime.getSystemMillTime()){
			return;
		}
		synchronized (rankList) {
			if(updateTime > GameSystemTime.getSystemMillTime()){
				return;
			}
			Collections.sort(rankList,new Comparator<TanBaoRoleVo>() {
				
				@Override
				public int compare(TanBaoRoleVo o1, TanBaoRoleVo o2) {
					int result = o2.getScore() - o1.getScore();
					if(result != 0){
						return result;
					}
					result = o1.getDeadTime() - o2.getDeadTime();
					return 0;
				}
			});
			int rank = 1;
			for (TanBaoRoleVo roleVo : rankList) {
				roleVo.setRank(rank++);
			}
			updateTime = GameSystemTime.getSystemMillTime() + 1000;
		}
	}

	public Object[] getRank(int start,int count){
		if(!this.start || count < 1 || start >= rankList.size()){
			return null;
		}
		int max = start + count;
		if(max > rankList.size()){
			max = rankList.size();
		}
		sort();
		List<Object[]> list = new ArrayList<>();
		for (; start < max; start++) {
			TanBaoRoleVo roleVo = rankList.get(start);
			list.add(roleVo.getMsgData());
		}
		return list.toArray();
	}
	
	public List<TanBaoRoleVo> getRankList(){
		if(start){
			return null;
		}
		sort();
		return rankList;
	}

	public int getLevel() {
		return level;
	}
	
	/**
	 * 获取总人数
	 * @return
	 */
	public int getSize(){
		return rankList.size();
	}

	public Object[] getEnterSucessMsg() {
		if(enterSucessMsg == null){
			enterSucessMsg = new Object[]{1,0};
		}
		enterSucessMsg[1] = stopTime - GameSystemTime.getSystemMillTime();
		return enterSucessMsg;
	}

	/**
	 * 判定玩家是否在探宝场景中
	 * @param userRoleId
	 * @return
	 */
	public boolean checkInStarge(long userRoleId){
		for (TanBaoStage stage : activeStages ) {
			if(null != stage.getElement(userRoleId, ElementType.ROLE)){
				return true;
			}
		}
		return false;
	}
}
