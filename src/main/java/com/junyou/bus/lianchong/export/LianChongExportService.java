package com.junyou.bus.lianchong.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.lianchong.entity.RoleLianchong;
import com.junyou.bus.lianchong.service.LianChongService;

@Service
public class LianChongExportService {
	@Autowired
	private LianChongService lianChongService;
	/**
	 * 玩家登陆加载进缓存
	 * @param userRoleId
	 * @return
	 */
	public List<RoleLianchong> initLeiChong(Long userRoleId){
		return lianChongService.initLeichong(userRoleId);
	}
	/**
	 * 充值后调用逻辑
	 */
	public void updateRecharge(Long userRoleId,Long addYb){
		lianChongService.rechargeYb(userRoleId, addYb);
	}
	/**
	 *  获取某个子活动的热发布某个活动信息
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		return lianChongService.getRefbInfo(userRoleId, subId);
	}
	/**
	 *  获取某个子活动的状态数据
	 */
	public Object[] getLianChongStates(Long userRoleId, Integer subId){
		return lianChongService.getLianChongStates(userRoleId, subId);
	}
	
	
	
}
