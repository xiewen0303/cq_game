package com.junyou.bus.tianyu.export;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.tianyu.configure.export.TianYuJiChuConfig;
import com.junyou.bus.tianyu.configure.export.TianYuJiChuConfigExportService;
import com.junyou.bus.tianyu.entity.TianYuInfo;
import com.junyou.bus.tianyu.manage.TianYu;
import com.junyou.bus.tianyu.service.TianYuService;
import com.junyou.public_.share.export.PublicRoleStateExportService;

@Service
public class TianYuExportService{
	
	@Autowired
	private TianYuService tianYuService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	/**
	 * 获得天羽的属性（除移动速度）
	 */
	public Map<String,Long> getTianYuAttrs(Long userRoleId,TianYu tianyu){
		return tianYuService.getTianYuAttrs(userRoleId,tianyu);
	}
	/**
	 * 获得天羽的属性（除移动速度）
	 */
	public Map<String,Long> getTianYuAttr(Long userRoleId){
		return tianYuService.getTianYuAttr(userRoleId);
	}
	
	  
	public TianYuInfo getTianYuInfo(long userRoleId){
		return tianYuService.getTianYuInfo(userRoleId);
	}

	public Object[] getEquips(Long userRoleId) {
		return tianYuService.getEquips(userRoleId);
	}

	
	public Object[] useTianYuQND(Long userRoleId,int count) {
		  return tianYuService.cbUseQND(userRoleId,count); 
	}

	public Object[] addTianYuCZD(Long userRoleId, int consumeCount) {
		 return tianYuService.cbUseCZD(userRoleId,consumeCount);
	}
	/**
	 * 获取天羽等级
	 * @param userRoleId
	 * @return
	 */
	public Integer getTianYuLevel(long userRoleId){
		TianYuInfo tianyuInfo = tianYuService.getTianYuInfo(userRoleId);
		if(tianyuInfo == null){
			return -1;
		}
		return tianyuInfo.getTianyuLevel();
	}
	
	/**
	 * 获取天羽等级
	 * @param userRoleId
	 * @return
	 */
	public Integer getTianYuLevelOther(long userRoleId){
		TianYuInfo tianyuInfo = tianYuService.getTianYuInfo(userRoleId);
		if(tianyuInfo == null){
			return -1;
		}
		TianYuJiChuConfig  config = tianYuJiChuConfigExportService.loadById(tianyuInfo.getTianyuLevel());
		return config.getLevel();
	}
	
	@Autowired
	private TianYuJiChuConfigExportService tianYuJiChuConfigExportService;

	public TianYuInfo initTianYu(Long userRoleId) {
		return tianYuService.initTianYu(userRoleId);
	}
 
	public TianYuInfo getTianYuInfoOnOROff(long userRoleId){
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			TianYuInfo tianyuInfo = tianYuService.getTianYuInfo(userRoleId);
			
			return tianyuInfo;
		}else{
			TianYuInfo tianyuInfo = tianYuService.getTianYuInfoDB(userRoleId);
			if(tianyuInfo == null){
				return null;
			}
			return tianyuInfo;
		}
	}
	
	
	
	public void onlineHandle(Long userRoleId){
		tianYuService.onlineHandle(userRoleId);
	}
	
	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer bgold){
		return tianYuService.sjByItem(userRoleId, minLevel, maxLevel, bgold);
	}
	
	
	public int getSkillMaxCount(Long userRoleId){
		return tianYuService.getSkillMaxCount(userRoleId);
	}
}
