/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.xinmo.entity.RoleXinmo;
import com.junyou.bus.xinmo.entity.RoleXinmoLiandan;
import com.junyou.bus.xinmo.entity.RoleXinmoLiandanItem;
import com.junyou.bus.xinmo.entity.RoleXinmoMoshen;
import com.junyou.bus.xinmo.entity.RoleXinmoMoshenShiti;
import com.junyou.bus.xinmo.entity.RoleXinmoSkill;
import com.junyou.bus.xinmo.entity.RoleXinmoXilian;
import com.junyou.bus.xinmo.service.XinmoService;

/**
 *@Description 心魔系统对外访问数据
 *@Author Yang Gao
 *@Since 2016-6-28
 *@Version 1.1.0
 */
@Service
public class XinmoExportService {
    
    @Autowired
    private XinmoService xinmoService;

    /*********************************心魔****************************/
    public List<RoleXinmo> initRoleXinmo(Long userRoleId) {
        return xinmoService.initRoleXinmo(userRoleId);
    }

    public Map<String, Long> getXinmoAttrs(Long userRoleId) {
        return xinmoService.getXinmoAttrs(userRoleId);
    }
    
    public int getRoleXinmoRank(Long userRoleId){
        return xinmoService.getRoleXinmoRank(userRoleId);
    }
    
    public void offLineRoleXinmoHandle(Long userRoleId){
        xinmoService.saveRoleXinmoKillLog(userRoleId);
    }
    
    /*********************************心魔-天炉炼丹****************************/
    public List<RoleXinmoLiandan> initRoleXinmoLiandan(Long userRoleId) {
        return xinmoService.initRoleXinmoLiandan(userRoleId);
    }
    
    public List<RoleXinmoLiandanItem> initRoleXinmoLiandanItem(Long userRoleId) {
        return xinmoService.initRoleXinmoLiandanItemList(userRoleId);
    }
    
    public void onlineLiandanHandle(Long userRoleId) {
        xinmoService.xinmoLianDanScheduleBegin(userRoleId);
        xinmoService.xinmoLianDanNotifySolt(userRoleId);
    }
    
    public void offlineLiandanHandle(Long userRoleId) {
        xinmoService.xinmoLianDanScheduleEnd(userRoleId);
    }
    
    public void roleUpLevel(Long userRoleId){
        xinmoService.xinmoLianDanScheduleBegin(userRoleId);
    }
    /*********************************心魔-魔神****************************/
    public List<RoleXinmoMoshen> initRoleXinmoMoshen(Long userRoleId){
        return xinmoService.initRoleXinmoMoshen(userRoleId);
    }
    
    public List<RoleXinmoMoshenShiti> initRoleXinmoMoshenShiti(Long userRoleId){
        return xinmoService.initRoleXinmoMoshenShiti(userRoleId);
    }
    
    public void onlineXinmoMoshenShitiHandle(Long userRoleId){
        xinmoService.onlineSendXinmoMoshenShitiId(userRoleId);
    }
    
    public void offlineXinmoMoshenShitiHandle(Long userRoleId) {
        xinmoService.xinmoMoshenScheduleEnd(userRoleId);
    }
    
    public Map<String, Long> getXinmoMoshenAttrs(Long userRoleId){
        return xinmoService.getXinmoMoshenAttrs(userRoleId);
    }
    
    public Map<String, Long> getXinmoMoshenShitiAttrs(Long userRoleId){
        return xinmoService.getXinmoMoshenShitiAttrs(userRoleId);
    }
    
    public Long getRoleXinmoTotalZplus(Long userRoleId){
        return xinmoService.getRoleXinmoMoshenTotalZplus(userRoleId);
    }
    
    /*********************************心魔-魔神技能****************************/
    public List<RoleXinmoSkill> initRoleXinmoSkill(Long userRoleId){
        return xinmoService.initRoleXinmoSkill(userRoleId);
    }
    
    public Map<String, Long> getXinmoNormalSkillAttrs(Long userRoleId){
        return xinmoService.getXinmoNormalSkillAttrs(userRoleId);
    }
    
    public Map<String, Long> getXinmoShitiSkillAttrs(Long userRoleId){
        return xinmoService.getXinmoShitiSkillAttrs(userRoleId);
    }
    /*********************************心魔-洗练****************************/
    public List<RoleXinmoXilian> initRoleXinmoXilian(Long userRoleId){
        return xinmoService.initRoleXinmoXilian(userRoleId);
    }
    
    public Map<String, Long> getXinmoXilianAttrs(Long userRoleId){
        return xinmoService.getAllXinmoXilianAttrs(userRoleId);
    }
    
    // 获得指定类型心魔魔神的噬体属性和对应魔神技能的噬体属性
    public Map<String, Long> getXinmoOrSkillAttrByXmType(Long userRoleId, Integer xm_type) {
        return xinmoService.getXinmoOrSkillAttrByXmType(userRoleId, xm_type);
    }
   
}
