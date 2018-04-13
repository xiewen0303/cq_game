/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.jueban.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.jueban.entity.RefabuJueban;
import com.junyou.bus.jueban.server.RefabuJuebanService;

/**
 *@Description 热发布-绝版礼包活动对外处理 
 *@Author Yang Gao
 *@Since 2016-8-1
 *@Version 1.1.0
 */
@Service
public class RefabuJuebanExportService {
    
    @Autowired
    private RefabuJuebanService refabuJuebanService;
    
    public List<RefabuJueban> initRefabuJueban(Long userRoleId){
        return refabuJuebanService.initRefabuJueban(userRoleId);
    }
    
    public Object getRefbInfo(Long userRoleId, Integer subId) {
        return refabuJuebanService.getRefabuInfo(userRoleId, subId);
    }

}
