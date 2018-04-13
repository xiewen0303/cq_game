package com.junyou.bus.suoyaota.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.suoyaota.entity.RefbSuoyaota;
import com.junyou.bus.suoyaota.service.SuoYaoTaService;

/**
 * @author LiuYu
 * 2015-8-3 下午1:54:16
 */
@Service
public class SuoYaoTaExportService {
	
	@Autowired
	private SuoYaoTaService suoYaoTaService;
	
	public List<RefbSuoyaota> initRefbSuoyaota(Long userRoleId) {
		return suoYaoTaService.initRefbSuoyaota(userRoleId);
	}
	
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		return suoYaoTaService.getRefbInfo(userRoleId, subId);
	}
	
	public Object[] getRefbState(Long userRoleId, Integer subId){
		return suoYaoTaService.getRefbState(userRoleId, subId);
	}
}
