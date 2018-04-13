/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.wuxing.configure.export.jingpo;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * @Description 精魄掉落配置
 * @Author Yang Gao
 * @Since 2016-5-10
 * @Version 1.1.0
 */
public class MoshenJingpoDiaoLuoConfig extends AbsVersion {
    /* 编号 */
    private int id;
    /* 获得魔神精华 */
    private int jinghua;
    /* 需要的银两 */
    private long needMoney;
    /* 获取精魄的概率集合key=精魄编号;value=权值 */
    private Map<Integer, Integer> proMap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJinghua() {
        return jinghua;
    }

    public void setJinghua(int jinghua) {
        this.jinghua = jinghua;
    }

    public long getNeedMoney() {
        return needMoney;
    }

    public void setNeedMoney(long needMoney) {
        this.needMoney = needMoney;
    }

    public Map<Integer, Integer> getProMap() {
        return proMap;
    }

    public void setProMap(Map<Integer, Integer> proMap) {
        this.proMap = proMap;
    }

}
