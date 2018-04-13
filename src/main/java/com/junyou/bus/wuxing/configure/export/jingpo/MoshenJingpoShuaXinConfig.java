/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.wuxing.configure.export.jingpo;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * @Description 精魄刷新配置
 * @Author Yang Gao
 * @Since 2016-5-10
 * @Version 1.1.0
 */
public class MoshenJingpoShuaXinConfig extends AbsVersion {
    /* 法宝编号 */
    private int id;
    /* 法宝刷新概率配置key=法宝品质;value=权值 */
    private Map<Integer, Integer> proMap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, Integer> getProMap() {
        return proMap;
    }

    public void setProMap(Map<Integer, Integer> proMap) {
        this.proMap = proMap;
    }


}