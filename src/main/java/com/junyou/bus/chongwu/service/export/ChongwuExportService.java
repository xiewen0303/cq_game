package com.junyou.bus.chongwu.service.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.chongwu.configure.export.ChongWuJiHuoBiaoConfig;
import com.junyou.bus.chongwu.entity.RoleChongwu;
import com.junyou.bus.chongwu.entity.RoleChongwuSkill;
import com.junyou.bus.chongwu.service.ChongwuService;
import com.junyou.stage.model.element.chongwu.Chongwu;

@Service
public class ChongwuExportService {
	@Autowired
	private ChongwuService chongwuService;

	public List<RoleChongwu> initRoleChongwu(Long userRoleId) {
		return chongwuService.initRoleChongwu(userRoleId);
	}

	public Map<String, Long> getChongwuAttribute(Integer configId,
			Integer level, Integer jie, Integer ceng) {
		return chongwuService.getChongwuAttribute(configId, level, jie, ceng);
	}

	public Object[] addChongwuExp(Long userRoleId, Long levelExp) {
		return chongwuService.addChongwuExp(userRoleId, levelExp);
	}

	public Map<String, Long> getAllChongwuAttribute(Long userRoleId) {
		return chongwuService.getAllChongwuAttribute(userRoleId);
	}
	
	public Chongwu getFightCw(Long userRoleId){
		return chongwuService.getFightCw(userRoleId);
	}
	
	public void onlineHandle(Long userRoleId){
		chongwuService.onlineHandle(userRoleId);
	}
	
	
	public List<RoleChongwuSkill> initRoleChongwuSkill(Long userRoleId) {
        return chongwuService.initRoleChongwuSkill(userRoleId);
    }
	
	public Map<String, Long> getAllChongwuSkillAttribute(Long userRoleId){
	    return chongwuService.getRoleAllChongwuSkillAttrs(userRoleId);
	}

	public RoleChongwu getRoleChongwuById(Long userRoleId, Integer chongwuConfigId){
	    return chongwuService.getRoleChongwu(userRoleId, chongwuConfigId);
	}
	
	public ChongWuJiHuoBiaoConfig loadChongwuJiHuoConfigById(Integer configId){
	    return chongwuService.loadChongwuJiHuoConfigById(configId);
	}

}
