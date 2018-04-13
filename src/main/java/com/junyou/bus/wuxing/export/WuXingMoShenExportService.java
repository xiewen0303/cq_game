package com.junyou.bus.wuxing.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.wuxing.entity.RoleWuxing;
import com.junyou.bus.wuxing.entity.RoleWuxingFuti;
import com.junyou.bus.wuxing.entity.RoleWuxingJingpo;
import com.junyou.bus.wuxing.entity.RoleWuxingJingpoItem;
import com.junyou.bus.wuxing.entity.RoleWuxingSkill;
import com.junyou.bus.wuxing.entity.TangbaoWuxing;
import com.junyou.bus.wuxing.entity.TangbaoWuxingSkill;
import com.junyou.bus.wuxing.service.WuXingMoShenService;

/**
 * @author zhongdian
 * 2016-4-13 下午2:19:32
 */
@Service
public class WuXingMoShenExportService {

	@Autowired
	private WuXingMoShenService wuXingMoShenService;
	
	public Integer getFuTiType(Long userRoleId){
		return wuXingMoShenService.getFuTiType(userRoleId);
	}
	
	public Integer getFuTiConfigId(Long userRoleId){
		return wuXingMoShenService.getFuTiConfigId(userRoleId);
	}
	
	public Map<String, Long> getWuXingAttrs(Long userRoleId,List<Integer> list) {
		return wuXingMoShenService.getWuXingAttrs(userRoleId, list);
	}
	
	public void onlineTuiSong(Long userRoleId){
		wuXingMoShenService.onlineTuiSong(userRoleId);
	}
	
	public List<RoleWuxing> initRoleWuxing(Long userRoleId){
		return wuXingMoShenService.initRoleWuxing(userRoleId);
	}
	public List<RoleWuxingFuti> initRoleWuxingFutis(Long userRoleId){
		return wuXingMoShenService.initRoleWuxingFutis(userRoleId);
	}
	
    public List<Integer> getRoleWuXings(Long userRoleId) {
        return wuXingMoShenService.getRoleWuXings(userRoleId);
    }
    
    /**************************五行魔神技能**********************************/
    public List<RoleWuxingSkill> initRoleWuxingSkills(Long userRoleId) {
        return wuXingMoShenService.initRoleWuxingSkill(userRoleId);
    }

    public Map<String, Long> getWuXingSkillAttrs(Long userRoleId) {
        return wuXingMoShenService.getWuXingSkillAttrs(userRoleId);
    }
    /**************************五行魔神精魄**********************************/
    public List<RoleWuxingJingpo> initRoleBodyWxJpList(Long userRoleId){
        return wuXingMoShenService.initRoleBodyWxJpList(userRoleId);
    }
    
    public List<RoleWuxingJingpoItem> initRoleWxJpItemList(Long userRoleId){
        return wuXingMoShenService.initRoleWxJpItemList(userRoleId);
    }
    
    public Map<String, Long> getWuXingJpAttrs(Long userRoleId) {
        return wuXingMoShenService.getWuXingJpAttrs(userRoleId);
    }

    /**
     * GM指令设置魔神精华数目
     * @param userRoleId
     * @param count
     */
    public void setWuxingMoshenJinghua(Long userRoleId, Long count) {
        wuXingMoShenService.setMoshenJinghua(userRoleId, count);
    }
    
    /**************************糖宝五行魔神精魄**********************************/
    
    public List<TangbaoWuxing> initTangbaoWuxing(Long userRoleId){
        return wuXingMoShenService.initTangbaoWuxing(userRoleId);
    }
    
    public Map<String, Long> getTbWuXingAttrs(Long userRoleId) {
        return wuXingMoShenService.getTbWuXingAttrs(userRoleId);
    }
    
    public List<TangbaoWuxing> getTbWuXings(Long userRoleId) {
        return wuXingMoShenService.getTbWuXings(userRoleId);
    }
    
    public List<TangbaoWuxingSkill> initTbWuxingSkills(Long userRoleId) {
        return wuXingMoShenService.initTbWuxingSkill(userRoleId);
    }
    
    public Map<String, Long> getTbWuXingSkillAttrs(Long userRoleId) {
        return wuXingMoShenService.getTbWuXingSkillAttrs(userRoleId);
    }
    
    public Long getRoleWuXingTotallZplus(Long userRoleId){
    	return wuXingMoShenService.getRoleWuXingTotallZplus(userRoleId);
    }
    
    public Object[] sjByItem(Long userRoleId,Integer type,Integer level,Integer rate,String items){
		return wuXingMoShenService.sjByItem(userRoleId, type, level,rate, items);
	} 
	
}
