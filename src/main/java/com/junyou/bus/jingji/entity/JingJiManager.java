package com.junyou.bus.jingji.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;

public class JingJiManager {

	private static JingJiManager manager = new JingJiManager();

	private Map<Integer,RoleJingji> rankMap = new HashMap<>();
	private Map<Long,RoleJingji> roleMap = new HashMap<>();
	private static AtomicInteger rankAtomic;


	public static JingJiManager getManager(){
		return manager;
	}

	public Map<Integer,RoleJingji> getRankMaps(){
		return rankMap;
	}

    public Map<Long,RoleJingji> getRoleMaps(){
        return roleMap;
    }

	/**
	 * 初始化竞技排行(容错同排名)  排除已经隐藏掉的机器人
	 * @param list
	 */
	public List<RoleJingji> init(List<RoleJingji> list){
		List<RoleJingji> change = new ArrayList<>();
		int rank = 1;
		for (RoleJingji roleJingji : list) {
			if(roleJingji.getRank() != rank){
				roleJingji.setRank(rank);
				change.add(roleJingji);
			}
			roleMap.put(roleJingji.getUserRoleId(), roleJingji);
			if(roleJingji.getUsed() !=1){
				rankMap.put(rank, roleJingji);
				rank++;
			}
		}
		rankAtomic = new AtomicInteger(rank);
		return change;
	}
	/**
	 * 新增竞技角色
	 * @param roleJingji
	 */
	public void add(RoleJingji roleJingji){
		int rank = rankAtomic.getAndIncrement();
		roleJingji.setRank(rank);
		roleMap.put(roleJingji.getUserRoleId(), roleJingji);
		rankMap.put(roleJingji.getRank(), roleJingji);
	}

		/**
	 * 根据角色id获取竞技信息
	 * @param userRoleId
	 * @return
	 */
	public RoleJingji getRoleJingjiByRoleId(Long userRoleId){
		return roleMap.get(userRoleId);
	}
	/**
	 * 根据排名获取竞技信息
	 * @param rank
	 * @return
	 */
	public RoleJingji getRoleJingjiByRank(Integer rank){
		return rankMap.get(rank);
	}
	
	/**
	 * 根据排名获取竞技信息
	 * @param rank
	 * @return
	 */
	public int getTotalCount(){
		return rankMap.size();
	}
}
