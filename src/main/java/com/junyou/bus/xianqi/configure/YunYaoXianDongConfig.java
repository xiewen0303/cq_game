/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xianqi.configure;

import java.util.List;
import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * @Description 云遥仙洞配置信息
 * @Author Yang Gao
 * @Since 2016-10-25
 * @Version 1.1.0
 */
public class YunYaoXianDongConfig extends AbsVersion {

    /**
     * 仙洞等级
     */
    private int lv;
    /**
     * 升一级需要的经验
     */
    private long needexp;
    /**
     * 升级使用的道具大类id集合
     */
    private List<String> needitem;
    /**
     * 升阶仙洞获得基础属性集合
     */
    private Map<String, Long> attrMap;

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public long getNeedexp() {
        return needexp;
    }

    public void setNeedexp(long needexp) {
        this.needexp = needexp;
    }

    public List<String> getNeeditem() {
        return needitem;
    }

    public void setNeeditem(List<String> needitem) {
        this.needitem = needitem;
    }

    public Map<String, Long> getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map<String, Long> attrMap) {
        this.attrMap = attrMap;
    }

}
