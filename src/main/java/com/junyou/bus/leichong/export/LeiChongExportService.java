package com.junyou.bus.leichong.export;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.leichong.entity.Leichong;
import com.junyou.bus.leichong.server.LeiChongService;



@Service
public class LeiChongExportService {

	@Autowired
	private LeiChongService leiChongService;
	
	
	public List<Leichong> initLeiChong(Long userRoleId){
		return leiChongService.initLeichong(userRoleId);
	}
	
	public Leichong getLeiChong(Long userRoleId,int subId){
		return leiChongService.getLeiChong(userRoleId, subId);
	}
	
	public Object[] getRefbQiPanInfo(Long userRoleId, Integer subId){
		return leiChongService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbQiPanLingQuStatus(Long userRoleId,Integer subId){
		return leiChongService.getRefbLingQuStatus(userRoleId, subId);
	}
	
	
	public void rechargeYb(Long userRoleId,Long addVal){
		leiChongService.rechargeYb(userRoleId, addVal);
	}
	
	public void rechargeYb53(Long userRoleId,Long addVal){
		leiChongService.rechargeYb53(userRoleId, addVal);
	}

	public Object[] getFanLi53Info(Long userRoleId, Integer subId) {
		return leiChongService.getFanLi53Info(userRoleId, subId);
	}

	public Object[] getRefb53Status(Long userRoleId, int subId) {
		return leiChongService.getRefb53Status(userRoleId, subId);
	}
}
