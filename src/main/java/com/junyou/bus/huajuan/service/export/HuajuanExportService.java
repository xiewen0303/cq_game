package com.junyou.bus.huajuan.service.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.huajuan.entity.RoleHuajuan;
import com.junyou.bus.huajuan.entity.RoleHuajuanExp;
import com.junyou.bus.huajuan.service.HuajuanService;

@Service
public class HuajuanExportService {
	@Autowired
	private HuajuanService huajuanService;

	public List<RoleHuajuan> initRoleHuajuan(Long userRoleId) {
		return huajuanService.initRoleHuajuan(userRoleId);
	}

	public List<RoleHuajuanExp> initRoleHuajuanExp(Long userRoleId) {
		return huajuanService.initRoleHuajuanExp(userRoleId);
	}

	public void onlineHandle(Long userRoleId) {
		huajuanService.onlineHandle(userRoleId);
	}

	public Map<String, Long> getHuajuanAttr(Long userRoleId) {
		return huajuanService.getHuajuanAttr(userRoleId);
	}

	public Object[] activateHuajuan(Long userRoleId, Integer huajuanId) {
		return huajuanService.activateHuajuan(userRoleId, huajuanId);
	}
}
