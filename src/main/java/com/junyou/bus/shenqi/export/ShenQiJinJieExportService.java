package com.junyou.bus.shenqi.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.shenqi.entity.ShenQiJinjie;
import com.junyou.bus.shenqi.service.ShenQiJinJieService;

@Service
public class ShenQiJinJieExportService {
	
	@Autowired
	private ShenQiJinJieService shenQiJinJieService;
	
	
	public Map<String,Long> getShenQiJinJieAttr(Long userRoleId){
		return shenQiJinJieService.getShenQiJinJieAttr(userRoleId);
	}
	public List<ShenQiJinjie> initShenQiJinJie(Long userRoleId) {
		return shenQiJinJieService.initShenQiJinJie(userRoleId);
	}
	public ShenQiJinjie getShenQiJinjieById(long userRoleId, Integer shenQiId){
		return shenQiJinJieService.getShenQiJinjieById(userRoleId, shenQiId);
		
	}
	
	public void notifyStageChange(long userRoleId){
		shenQiJinJieService.notifyStageChange(userRoleId);
	}
}
