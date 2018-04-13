package com.junyou.bus.shenqi.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.equip.configure.export.ShenQiGeWeiConfig;
import com.junyou.bus.shenqi.entity.ShenQiEquip;
import com.junyou.bus.shenqi.service.ShenQiEquipService;

@Service
public class ShenQiEquipExportService {
	
	@Autowired
	private ShenQiEquipService shenQiEquipService;
	
	public List<ShenQiEquip> initShenQiEquip(Long userRoleId) {
		return shenQiEquipService.initShenQiEquip(userRoleId);
	}
	
	public List<ShenQiEquip> getShenQiEquipByShenQiIdAndSlot(Long userRoleId,Integer shenQiId,Integer slot) {
		return shenQiEquipService.getShenQiEquipByShenQiIdAndSlot(userRoleId,shenQiId,slot);
	}
	
	public List<ShenQiEquip> getShenQiEquip(Long userRoleId) {
		return shenQiEquipService.getShenQiEquip(userRoleId);
	}
	
	public void checkGeWeiOpen(ShenQiGeWeiConfig shenQiGeWeiConfig,Long userRoleId){
		shenQiEquipService.checkGeWeiOpen(shenQiGeWeiConfig, userRoleId);
		
	}
}
