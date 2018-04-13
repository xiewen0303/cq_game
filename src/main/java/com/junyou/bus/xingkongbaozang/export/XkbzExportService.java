package com.junyou.bus.xingkongbaozang.export;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.qipan.entity.Qipan;
import com.junyou.bus.xingkongbaozang.entity.RefabuXkbz;
import com.junyou.bus.xingkongbaozang.server.XkbzService;



@Service
public class XkbzExportService {

	@Autowired
	private XkbzService xkbzService;
	
	
	public List<RefabuXkbz> initRefabuXkbz(Long userRoleId){
		return xkbzService.initRefabuXkbz(userRoleId);
	}
	
	public Object[] getRefbXkbzInfo(Long userRoleId, Integer subId){
		return xkbzService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbXkbzLingQuStatus(Long userRoleId,Integer subId){
		return xkbzService.getRefbLingQuStatus(userRoleId, subId);
	}
	
	
	public void xiaofeiYb(Long userRoleId,Long addVal){
		xkbzService.xiaofeiYb(userRoleId, addVal,1);
	}
}
