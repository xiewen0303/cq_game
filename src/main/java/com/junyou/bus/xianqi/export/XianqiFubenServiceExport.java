/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xianqi.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.xianqi.entity.XianqiFuben;
import com.junyou.bus.xianqi.service.XianqiFubenService;

/**
 * 
 *@Description 仙器副本对外访问类
 *@Author Yang Gao
 *@Since 2016-11-1
 *@Version 1.1.0
 */
@Service
public class XianqiFubenServiceExport {

    @Autowired
    private XianqiFubenService xianqiFubenService;
 
    public List<XianqiFuben> initXianqiFubenData(Long userRoleId){
        return xianqiFubenService.initXianqiFubenData(userRoleId);
    }
    
    public int getRoleXianqiFubenRemainCount(Long userRoleId){
        return  xianqiFubenService.getRoleXianqiFubenRemainCount(userRoleId);
    }

    public void cutXianqiFubenCount(Long userRoleId) {
        xianqiFubenService.cutXianqiFubenCount(userRoleId);
    }
    
}
