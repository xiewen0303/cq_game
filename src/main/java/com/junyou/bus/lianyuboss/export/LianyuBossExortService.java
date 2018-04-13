package com.junyou.bus.lianyuboss.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.lianyuboss.entity.GuildBossLianyu;
import com.junyou.bus.lianyuboss.service.LianyuBossService;

@Service
public   class LianyuBossExortService {

	@Autowired
	private LianyuBossService lianyuBossService;
	
	/**
	 * 初始化个人数据
	 * @param userRoleId
	 * @return
	 */
	public  List<GuildBossLianyu> initGuildBossLianyu(Long userRoleId){
		return lianyuBossService.initGuildBossLianyu(userRoleId);
	}
	/**
	 * 服务器启动统计所有帮派boss公共数据
	 */
	public void serverStartInitLianyuBossData(){
		
		lianyuBossService.initAllGuildLianyuBossData();
	}
	public boolean isReduceHalfBossHarm(long userRoleId,int configId){
		return lianyuBossService.isReduceHalfBossHarm(userRoleId, configId);
	}
	/**
	 * 整点清理
	 */
	public void clearMap(){
		lianyuBossService.clearData();
	}
}
