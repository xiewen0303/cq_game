package com.junyou.bus.onlinerewards.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.onlinerewards.entity.RoleOnlineRewards;
import com.junyou.bus.onlinerewards.service.OnlineRewardsService;

@Service
public class OnlineRewardsExportService {

	@Autowired
	private OnlineRewardsService onlineRewardsService;
	/**
	 * 玩家登陆加载进缓存
	 * @param userRoleId
	 * @return
	 */
	public List<RoleOnlineRewards> initOnlineRewards(Long userRoleId){
		return onlineRewardsService.initOnlineRewards(userRoleId);
	}
	/**
	 *  初始化某个子活动的热发布某个活动信息
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		return onlineRewardsService.getRefbInfo(userRoleId, subId);
	}
	/**
	 *  版本号不变的情况下获取某个子活动的状态数据
	 */
	public Object[] getOnlineRewardsStates(Long userRoleId, Integer subId){
		return onlineRewardsService.getOnlineRewardsStates(userRoleId, subId);
	}
	
	/**
	 * 玩家上线逻辑
	 */
	public void onlineHandle(Long userRoleId){
		onlineRewardsService.onlineHandle(userRoleId);
	}
	
	/**
	 * 玩家下线逻辑 
	 */
	public void offlineHandle(Long userRoleId){
		onlineRewardsService.offlineHandle(userRoleId);
	}
}
