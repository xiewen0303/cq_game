/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.kuafu_yungong.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafu_yungong.service.KuafuYunGongService;

/**
 *@Description 跨服云宫之巅对外访问类
 *@Author Yang Gao
 *@Since 2016-9-23
 *@Version 1.1.0
 */
@Service
public class KuafuYunGongExportService {

    @Autowired
    private KuafuYunGongService kuafuYunGongService;

    /**
     * 初始化
     */
    public void init() {
        kuafuYunGongService.initJob();
    }
    
    /**
     * 本服跨服云宫之巅获胜公会门主是否拥有外显
     * @param userRoleId
     * @return
     */
    public boolean kuafuYunGongIsShowCloth(Long userRoleId) {
        return kuafuYunGongService.kuafuYunGongIsShowCloth(userRoleId);
    }
}
