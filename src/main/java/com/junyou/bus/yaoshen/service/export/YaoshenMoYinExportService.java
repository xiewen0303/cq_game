package com.junyou.bus.yaoshen.service.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.yaoshen.entity.RoleYaoshenMoyin;
import com.junyou.bus.yaoshen.service.YaoshenMoYinService;
import com.junyou.bus.yaoshen.vo.YaoShenMoyinRankVo;
import com.junyou.public_.rank.export.IYaoShenMoyinRankExportService;

@Service
public class YaoshenMoYinExportService  implements IYaoShenMoyinRankExportService<YaoShenMoyinRankVo>{
	@Autowired
	private YaoshenMoYinService yaoshenMoYinService;
	
	
	/**
	 * 初始化
	 * @param userRoleId
	 * @return
	 */
	public RoleYaoshenMoyin initRoleYaoshenMoYin(Long userRoleId) {
		
		return yaoshenMoYinService.initRoleYaoshenMoYin(userRoleId);
	}
	/**
	 *玩家升级到一定等级自动激活 
	 */
//	@Deprecated
//	public void roleUpLevel(Long userRoleId,int level){
//		yaoshenMoYinService.checkLevelAndActiveMoYin(userRoleId, level);
//	}
	/**
	 * 妖神魔印属性总和
	 */
	public Map<String, Long> getYaoshenMoYinAttribute(Long userRoleId){
		
		return yaoshenMoYinService.getYaoshenMoYinAttributeAfterLogin(userRoleId);
	}
	/**
	 * 妖神魔印属性
	 * @param jie
	 * @param ceng
	 * @param qndNum
	 * @param czdNum
	 * @return
	 */
	public Map<String, Long> getYaoshenHunpoAttribute(Integer jie,Integer ceng,Integer qndNum,Integer czdNum) {
		return yaoshenMoYinService.getYaoshenMoYinAttribute(jie,ceng,qndNum,czdNum);
	}
	public Object[] useQND(Long userRoleId, int count) {
		return yaoshenMoYinService.useQND(userRoleId, count);
	}
	
	public Object[] useCZD(Long userRoleId, int count) {
		return yaoshenMoYinService.useCZD(userRoleId, count);
	}
	
	public RoleYaoshenMoyin getRoleYaoshenMoyin(Long userRoleId){
		return yaoshenMoYinService.getRoleYaoshenMoYin(userRoleId);
	}
	
	@Override
	public List<YaoShenMoyinRankVo> getYaoShenMoyinRankVo(int limit) {
		return yaoshenMoYinService.getMoyinRankVo(limit);
	}
	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer addCeng){
		return yaoshenMoYinService.sjByItem(userRoleId, minLevel, maxLevel, addCeng);
	}
}
