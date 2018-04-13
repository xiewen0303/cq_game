package com.junyou.bus.rechargefanli.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.rechargefanli.entity.RefabuRefanli;
import com.junyou.bus.rechargefanli.server.RechargeFanLiService;

/**
 * @author zhongdian
 * 2015-8-13 下午3:49:04
 */
@Service
public class RechargeFanliExportService {

	@Autowired
	private RechargeFanLiService rechargeFanLiService;
	
	public List<RefabuRefanli> initRefabuRefanli(Long userRoleId){
		return rechargeFanLiService.initRefabuRefanli(userRoleId);
	}
	
	
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		return rechargeFanLiService.getRefbInfo(userRoleId, subId);
	}
	
	public Object[] getRefbLingQuStatus(Long userRoleId, Integer subId){
		return rechargeFanLiService.getRefbLingQuStatus(userRoleId, subId);
	}
	
	
	public void rechargeYb(Long userRoleId,Long addVal){
		rechargeFanLiService.rechargeYb(userRoleId, addVal);
	}
}
