/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xianqi.configure;

import java.util.List;
import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * @Description 云瑶晶脉资源数据
 * @Author Yang Gao
 * @Since 2016-11-1
 * @Version 1.1.0
 */
public class YunYaoJingMaiConfig extends AbsVersion {

    /**
     * 资源id
     */
    private int id;
    /**
     * 资源刷新cd:秒
     */
    private int cd;
    /**
     * 资源刷新坐标集合,一个坐标一个晶脉
     */
    private List<Integer[]> zuobiao;
    /**
     * 资源采集时间:秒
     */
    private int time;
    /**
     * 采集资源奖励集合 key:道具小类id value:获得道具的概率(万分比)
     */
    private Map<String, Integer> itemMap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCd() {
        return cd;
    }

    public void setCd(int cd) {
        this.cd = cd;
    }

    public List<Integer[]> getZuobiao() {
        return zuobiao;
    }

    public void setZuobiao(List<Integer[]> zuobiao) {
        this.zuobiao = zuobiao;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Map<String, Integer> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, Integer> itemMap) {
        this.itemMap = itemMap;
    }

}
