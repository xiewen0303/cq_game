package com.junyou.bus.hongbao.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.hongbao.service.HongbaoService;

@Service
public class HongbaoExportService {

	@Autowired
	private HongbaoService hongbaoService;
	/**
	 * 玩家首冲触发红包
	 */
	@Deprecated
	public void  shouChongToHongbao(Long userRoleId){
		hongbaoService.sendHongbao(userRoleId);
	}
	/**
	 * 玩家下线
	 * @param userRoleId
	 */
	public void  offlineHandle(Long userRoleId){
		hongbaoService.offlineHandle(userRoleId);
	}

	/**
	 * 凌晨清理24小时过期红包
	 * @param userRoleId
	 */
	public void  clearExpireHongbao(){
		hongbaoService.clearExpireHongbao();
	}
	
	
}
