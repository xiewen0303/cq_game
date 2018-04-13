package com.junyou.bus.fushu.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.fushu.entity.FushuSkill;
import com.junyou.bus.fushu.service.FushuSkillService;
import com.junyou.stage.model.core.attribute.BaseAttributeType;

/**
 * @author LiuYu
 * 2015-8-27 上午10:12:04
 */
@Service
public class FushuSkillExportService {
	
	@Autowired
	private FushuSkillService fushuSkillService;
	
	public List<FushuSkill> initFushuSkill(Long userRoleId) {
		return fushuSkillService.initFushuSkill(userRoleId);
	}
	
	public Map<BaseAttributeType,Map<String,Long>> getAllSkillAttribute(Long userRoleId){
		return fushuSkillService.getAllSkillAttribute(userRoleId);
	}
	
}
