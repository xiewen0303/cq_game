/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.jueban.configure;

import java.util.Map;

/**
 *@Description 热发布-绝版礼包活动配置组
 *@Author Yang Gao
 *@Since 2016-8-1
 *@Version 1.1.0
 */
public class RefabuJuebanGroupConfig {
    /*所有配置数据集合*/
    private Map<Integer, RefabuJuebanConfig> configMap;
    /*文件加密信息*/
    private String md5Version;
    
    public Map<Integer, RefabuJuebanConfig> getConfigMap() {
        return configMap;
    }
    public void setConfigMap(Map<Integer, RefabuJuebanConfig> configMap) {
        this.configMap = configMap;
    }
    public String getMd5Version() {
        return md5Version;
    }
    public void setMd5Version(String md5Version) {
        this.md5Version = md5Version;
    }
}
