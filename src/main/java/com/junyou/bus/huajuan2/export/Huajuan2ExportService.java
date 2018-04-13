/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.huajuan2.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.huajuan2.entity.RoleHuajuan2;
import com.junyou.bus.huajuan2.entity.RoleHuajuan2Exp;
import com.junyou.bus.huajuan2.service.Huajuan2Service;

/**
 *@Description 画卷2对外访问类
 *@Author Yang Gao
 *@Since 2016-9-13
 *@Version 1.1.0
 */
@Service
public class Huajuan2ExportService {

    @Autowired
    private Huajuan2Service huaJuan2Service;
    
    public void onlineHandle(Long userRoleId){
        huaJuan2Service.onlineHandle(userRoleId);
    }

    public List<RoleHuajuan2> initRoleHuajuan2(Long userRoleId) {
        return huaJuan2Service.initRoleHuajuan2Data(userRoleId);
    }

    public List<RoleHuajuan2Exp> initRoleHuajuan2Exp(Long userRoleId) {
        return huaJuan2Service.initRoleHuajuan2ExpData(userRoleId);
    }

    public Map<String, Long> getHuajuan2Attr(Long userRoleId) {
        return huaJuan2Service.getHuajuan2Attr(userRoleId);
    }
    
}
