package com.junyou.bus.chengshen.export;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.chengshen.entity.RoleChengShen;
import com.junyou.bus.chengshen.service.ChengShenService;

@Service
public class ChengShenExportService {

	@Autowired
	private ChengShenService chengShenService;
	/**
	 * 初始化对象
	 * @param userRoleId
	 * @return
	 */
	public RoleChengShen initRoleChengShen(long userRoleId){
		
		return chengShenService.initRoleChengShen(userRoleId);
	}
	/**
	 * 初始化属性
	 * @param userRoleId
	 * @return
	 */
	public  Map<String, Long> initAttrMap(long userRoleId){
		
		return chengShenService.initAttrMap(userRoleId);
	}
	/**
	 * 玩家上线推送神魂值
	 */
	public void onlineHandlerToClientSHZ(long userRoleId){
		chengShenService.onlineHandlerToClientSHZ(userRoleId);
	}
	/**
	 * 使用神魂丹道具获得神魂值
	 * @param userRoleId
	 * @param data1
	 * @return
	 */
	public Object[] useChengShenSHD(long userRoleId, int data1,int count){
		return chengShenService.useShenHunDan(userRoleId, data1,count);
	}
	
	/**
	 * 获得成神称号属性
	 */
	public Map<String, Long> getAttrMapByLevel(int level){
		
		return chengShenService.getAttrMapByLevel(level);
	}
	
	/**
	 * 增加神魂值 
	 */
	public void addSHZ(long userRoleId,int addValue){
		chengShenService.addSHZ(userRoleId, addValue);
	}
	
	/**
	 * 获得玩家的神魂值--给其他模块调用
	 */
	public long getShenHunZhiByUserRoleId(long userRoleId){
		return chengShenService.getShenHunZhiByUserRoleId(userRoleId);
	}
	
}
