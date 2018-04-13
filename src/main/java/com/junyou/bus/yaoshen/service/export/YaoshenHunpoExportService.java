package com.junyou.bus.yaoshen.service.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.yaoshen.entity.RoleYaoshenHunpo;
import com.junyou.bus.yaoshen.service.YaoshenHunPoService;
import com.junyou.bus.yaoshen.vo.YaoShenHunpoRankVo;
import com.junyou.public_.rank.export.IYaoShenHunpoRankExportService;

@Service
public class YaoshenHunpoExportService implements IYaoShenHunpoRankExportService<YaoShenHunpoRankVo>{
	@Autowired
	private YaoshenHunPoService yaoshenHunPoService;
	
	
	
	public RoleYaoshenHunpo initRoleYaoshenHunpo(Long userRoleId) {
		
		return yaoshenHunPoService.initRoleYaoshenHunpo(userRoleId);
	}
	/**
	 *玩家升级到一定等级自动激活 
	 */
	public void roleUpLevel(Long userRoleId,int level){
		
		yaoshenHunPoService.checkLevelAndActiveHunpo(userRoleId,level);
	}
	/**
	 * 妖神魂魄属性总和
	 */
	public Map<String, Long> getYaoshenHunpoAttribute(Long userRoleId){
		
		return yaoshenHunPoService.getYaoshenHunpoAttributeAfterLogin(userRoleId);
	}
	
	
	/**
	 * 妖神魂魄属性总和
	 * @param jie
	 * @param ceng
	 * @param qndNum
	 * @param czdNum
	 * @return
	 */
	public Map<String, Long> getYaoshenHunpoAttribute(Integer jie,Integer ceng,Integer qndNum,Integer czdNum) {
		return yaoshenHunPoService.getYaoshenHunpoAttribute(jie,ceng,qndNum,czdNum);
	}
	/**
	 * 妖神魂魄--魄神属性总和 
	 * @return
	 */
	public Map<String, Long> getHunpoAllPoshenAttribute(Long userRoleId) {
		return yaoshenHunPoService.getHunpoAllPoshenAttribute(userRoleId);
	}
	public Object[] useHunpoQND(Long userRoleId, int count) {
		return yaoshenHunPoService.useQND(userRoleId, count);
	}
	public RoleYaoshenHunpo getRoleYaoshenHunpo(Long userRoleId) {
		return yaoshenHunPoService.getRoleYaoshenHunpo(userRoleId);
	}
	public Object[] useHunpoCZD(Long userRoleId, int count) {
		return yaoshenHunPoService.useCZD(userRoleId, count);
	}
	@Override
	public List<YaoShenHunpoRankVo> getYaoShenHunpoRankVo(int limit) {
		return yaoshenHunPoService.getYaoShenHunpoRankVo(limit);
	}
	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer addCeng){
		return yaoshenHunPoService.sjByItem(userRoleId, minLevel, maxLevel, addCeng);
	} 
}
