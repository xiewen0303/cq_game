package com.junyou.bus.laba.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.laba.service.LaBaService;

@Service
public class LaBaExportService {
	@Autowired
	private LaBaService laBaService;
	/**
	 *  获取某个子活动的热发布某个活动信息
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		return laBaService.getRefbInfo(userRoleId, subId);
	}
	/**
	 *  获取某个子活动的状态数据
	 */
	public Object[] getLaBaStates(Long userRoleId, Integer subId){
		return laBaService.getLianChongStates(userRoleId, subId);
	}
	
	/**
	 * @param userRoleId
	 * @param addVal
	 */
	public void rechargeYb(Long userRoleId,Long addVal){
		laBaService.rechargeYb(userRoleId,addVal);
	}
}
