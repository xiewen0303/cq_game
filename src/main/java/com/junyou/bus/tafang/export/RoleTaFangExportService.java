package com.junyou.bus.tafang.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.tafang.entity.RoleTafang;
import com.junyou.bus.tafang.service.RoleTaFangService;
import com.junyou.log.ChuanQiLog;

/**
 * @author LiuYu
 * 2015-10-19 下午1:50:15
 */
@Service
public class RoleTaFangExportService {
	
	@Autowired
	private RoleTaFangService roleTaFangService;
	
	public List<RoleTafang> initRoleTafang(Long userRoleId) {
		return roleTaFangService.initRoleTafang(userRoleId);
	}
	
	public void onlineHandle(Long userRoleId){
		try{
			roleTaFangService.onlineHandle(userRoleId);
		}catch (Exception e) {
			ChuanQiLog.error("塔防上线业务异常:",e);
		}
	}
}
