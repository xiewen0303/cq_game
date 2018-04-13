package com.junyou.bus.laowanjia.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.laowanjia.entity.RefbLaowanjia;
import com.junyou.bus.laowanjia.service.RefbLaoWanJiaService;

/**
 * @author zhongdian
 * 2016-3-22 上午10:25:30
 */
@Service
public class RefbLaowanjiaExportService {

	
	@Autowired
	private RefbLaoWanJiaService refbLaoWanJiaService;
	
	public void onlineHandle(Long userRoleId){
		refbLaoWanJiaService.onlineHandle(userRoleId);
	}
	
	public List<RefbLaowanjia> initRefbLaowanjias(Long userRoleId){
		return refbLaoWanJiaService.initRefbLaowanjias(userRoleId);
	}
	
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		return refbLaoWanJiaService.getRefbInfo(userRoleId, subId);
	}
	public void rechargeYb(Long userRoleId,Long addVal){
		refbLaoWanJiaService.rechargeYb(userRoleId, addVal);
	}
	public boolean isXianShiActity(Long userRoleId,int subId){
		return refbLaoWanJiaService.isXianShiActity(userRoleId, subId);
	}
}
