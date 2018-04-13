package com.junyou.bus.shenqi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.equip.configure.export.ShenQiGeWeiConfig;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.shenqi.dao.ShenQiEquipDao;
import com.junyou.bus.shenqi.entity.ShenQiEquip;
import com.junyou.bus.shenqi.entity.ShenQiJinjie;
import com.junyou.bus.shenqi.export.ShenQiJinJieExportService;
import com.junyou.bus.shenqi.filter.ShenQiEquipFilter;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * 
 * @description:神器装备
 *
 *	@author chenxiaobing
 *
 * @date 2016-12-7
 */
@Service
public class ShenQiEquipService {
	@Autowired
	private ShenQiEquipDao shenQiEquipDao;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private ShenQiJinJieExportService shenQiJinJieExportService;
	
	
	private static final Integer DATA_LEVEL = 1;
	private static final Integer DATA_VIP = 2;
	private static final Integer DATA_SHENQI_LEVEL = 3;
	
	
	
	public List<ShenQiEquip> initShenQiEquip(Long userRoleId) {
		return shenQiEquipDao.initShenQiEquip(userRoleId);
	}
	
	public List<ShenQiEquip> getShenQiEquipByShenQiIdAndSlot(Long userRoleId,Integer shenQiId,Integer slot) {
		return shenQiEquipDao.cacheLoadAll(userRoleId, new ShenQiEquipFilter(shenQiId,slot));
	}
	
	public List<ShenQiEquip> getShenQiEquip(Long userRoleId) {
		return shenQiEquipDao.cacheLoadAll(userRoleId);
	}
	
	public void checkGeWeiOpen(ShenQiGeWeiConfig shenQiGeWeiConfig,Long userRoleId){
		
		if(shenQiGeWeiConfig.getData() == DATA_LEVEL){
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			if(role != null){
				if(role.getLevel() >= shenQiGeWeiConfig.getTiaojian()){
					ShenQiEquip entity = createShenQiEquip(shenQiGeWeiConfig.getId(),shenQiGeWeiConfig.getGeWei(),userRoleId);
					shenQiEquipDao.cacheInsert(entity, userRoleId);
				}
			}
		}else if(shenQiGeWeiConfig.getData() == DATA_VIP){
			RoleVipWrapper roleVip = roleVipInfoExportService.getRoleVipInfo(userRoleId);
			if(roleVip.getVipLevel() >= shenQiGeWeiConfig.getTiaojian()){
				ShenQiEquip entity = createShenQiEquip(shenQiGeWeiConfig.getId(),shenQiGeWeiConfig.getGeWei(),userRoleId);
				shenQiEquipDao.cacheInsert(entity, userRoleId);
			}
		}else if(shenQiGeWeiConfig.getData() == DATA_SHENQI_LEVEL){
			ShenQiJinjie shenJinJie = shenQiJinJieExportService.getShenQiJinjieById(userRoleId, shenQiGeWeiConfig.getId());
			if(shenJinJie != null){
				if(shenJinJie.getShenqiLevel() >= shenQiGeWeiConfig.getTiaojian()){
					ShenQiEquip entity = createShenQiEquip(shenQiGeWeiConfig.getId(),shenQiGeWeiConfig.getGeWei(),userRoleId);
					shenQiEquipDao.cacheInsert(entity, userRoleId);
				}
			}
		}
	}
	
	public ShenQiEquip createShenQiEquip(Integer shenQiId,Integer slot,Long userRoleId){
		ShenQiEquip shenQiEquip = new ShenQiEquip();
		shenQiEquip.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		shenQiEquip.setUserRoleId(userRoleId);
		shenQiEquip.setShenQiId(shenQiId);
		shenQiEquip.setSlot(slot);
		return shenQiEquip;
	}
	
	
}
