package com.junyou.bus.caidan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.caidan.entity.RefbCaidan;

/**
 * @author LiuYu
 * 2015-9-16 下午6:40:57
 */
@Service
public class RoleCaidanExportService {
	@Autowired
	private RoleCaidanService roleCaidanService;
	
	public List<RefbCaidan> initRefbCaidan(Long userRoleId) {
		return roleCaidanService.initRefbCaidan(userRoleId);
	}
	
	public Object[] getInfo(Long userRoleId, int subId) {
		return roleCaidanService.getRefbInfo(userRoleId, subId);
	}
	
	public Object[] getRefbState(Long userRoleId, Integer subId){
		return roleCaidanService.getRefbState(userRoleId, subId);
	}
}
