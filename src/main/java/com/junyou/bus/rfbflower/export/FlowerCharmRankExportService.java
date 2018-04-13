package com.junyou.bus.rfbflower.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.rfbflower.entity.RoleRfbFlower;
import com.junyou.bus.rfbflower.service.FlowerCharmRankService;

@Service
public class FlowerCharmRankExportService {

	@Autowired
	private FlowerCharmRankService flowerCharmRankService;
	/**
	 * 玩家登陆加载进缓存
	 * @param userRoleId
	 * @return
	 */
	public List<RoleRfbFlower> initRoleRfbFlower(Long userRoleId){
		return flowerCharmRankService.initRoleRfbFlower(userRoleId);
	}
	/**
	 *  初始化某个子活动的热发布某个活动信息
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		return flowerCharmRankService.getRefbInfo(userRoleId, subId);
	}
	/**
	 * 送鲜花触发
	 * @param userRoleId
	 * @param charmValue
	 */
	public void updateRedisFlowerCharmRank(Long userRoleId, int charmValue) {
		flowerCharmRankService.updateRedisFlowerCharmRank(userRoleId,charmValue);
	}
	/**
	 *  版本号不变的情况下获取某个子活动的状态数据,暂不需要
	 */
	@Deprecated
	public Object[] getOnlineRewardsStates(Long userRoleId, Integer subId){
		return flowerCharmRankService.getFlowerRankStates(userRoleId, subId);
	}
	
	/**
	 * 每隔这个点轮询一次
	 */
	public void flowerCharmRankJieSuan(){
		flowerCharmRankService.flowerCharmRankJieSuan();
	}
	
	
 
}
