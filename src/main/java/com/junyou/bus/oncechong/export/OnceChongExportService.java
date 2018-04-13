package com.junyou.bus.oncechong.export;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.oncechong.entity.RoleOncechong;
import com.junyou.bus.oncechong.service.OnceChongService;



@Service
public class OnceChongExportService {

	@Autowired
	private OnceChongService onceChongService;
	
	
	public List<RoleOncechong> initOnceChong(Long userRoleId){
		return onceChongService.initOncechong(userRoleId);
	}
	
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		return onceChongService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbStatus(Long userRoleId,Integer subId){
		return onceChongService.getLingQuStatus(userRoleId, subId);
	}
	
	public void rechargeYb(Long userRoleId,Long addVal){
		onceChongService.rechargeYb(userRoleId, addVal);
	}
}
