package com.junyou.bus.linghuo.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.linghuo.entity.RoleLinghuoBless;
import com.junyou.bus.linghuo.entity.RoleLinghuoInfo;
import com.junyou.bus.linghuo.service.LingHuoService;

@Service
public class LingHuoExportService{
	
	@Autowired
	private LingHuoService lingHuoService;
	
	
	public List<RoleLinghuoInfo> initRoleLinghuoInfo(Long userRoleId){
		return lingHuoService.initRoleLinghuoInfo(userRoleId);
	}
	
	/**
	 * 获得灵火的属性
	 */
	public Map<String,Long> getLingHuoAttrs(Long userRoleId,int lingHuoId){
		return lingHuoService.getLingHuoAttrs(userRoleId,lingHuoId);
	}
	
	public Integer getLingHuoConfigId(Long userRoleId){
		return lingHuoService.getLingHuoConfigId(userRoleId);
	}
	
	
	/**
	 * 初始化灵火祝福数据
	 * 
	 * @param userRoleId
	 * @return
	 */
	public List<RoleLinghuoBless> initRoleLinghuoBlessList(Long userRoleId){
	    return lingHuoService.initRoleLinghuoBless(userRoleId);
	}
	
	/**
	 * 获取玩家灵火祝福所有加成属性
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getLinghuoBlessAllAttrsMap(Long userRoleId){
	    return lingHuoService.getLinghuoBlessAttrs(userRoleId);
	}
	
	
}
