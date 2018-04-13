/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.mogonglieyan.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.mogonglieyan.entity.RoleMogonglieyan;
import com.junyou.bus.mogonglieyan.service.RoleMoGongLieYanService;

/**
 *@Description 心魔系统对外访问数据
 *@Author Yang Gao
 *@Since 2016-6-28
 *@Version 1.1.0
 */
@Service
public class RoleMoGongLieYanExportService {
    
    @Autowired
    private RoleMoGongLieYanService moGongLieYanService;
    
    /**
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleMogonglieyan> initRoleMglyList(Long userRoleId) {
        return moGongLieYanService.initData2Cache(userRoleId);
    }

    /**
     * 使用道具添加魔焰精华数
     * @param addJinghua
     * @return
     */
    public Object[] addMglyJinghua(Long userRoleId, long addJinghua) {
        return moGongLieYanService.addJinghua(userRoleId, addJinghua);
    }

    
    
}
