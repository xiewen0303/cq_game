/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xianqi.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.xianqi.entity.RoleXianqi;
import com.junyou.bus.xianqi.entity.RoleXianqiJuexing;
import com.junyou.bus.xianqi.service.XianqiService;

/**
 *@Description 仙器觉醒对外访问类
 *@Author Yang Gao
 *@Since 2016-10-30
 *@Version 1.1.0
 */
@Service
public class XianqiServiceExport {

    @Autowired
    private XianqiService xianqiService;
 
    public List<RoleXianqi> initRoleXianqi(Long userRoleId){
        return xianqiService.initRoleXianqiData(userRoleId);
    }
    
    public List<RoleXianqiJuexing> initRoleXianqiJuexing(Long userRoleId){
        return xianqiService.initRoleXianqiJuexingData(userRoleId);
    }

    public Map<String, Long> getXiandongAttrMap(Long userRoleId) {
        return xianqiService.getRoleXianqiAttrMap(userRoleId);
    }
    
    public Map<String, Long> getXianqiJuexingAttrMap(Long userRoleId) {
        return xianqiService.getRoleXianqiJuexingAttrMap(userRoleId);
    }
    
    /**
     * 
     *@description:获取单个玩家仙觉战力总和 
     * @param userRoleId
     * @return
     */
    public Long getRoleXianqiJuexingTotalZplus(Long userRoleId){
    	return xianqiService.getRoleXianqiJuexingTotalZplus(userRoleId);
    }
    
    
}
