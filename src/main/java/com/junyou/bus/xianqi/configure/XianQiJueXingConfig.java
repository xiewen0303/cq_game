/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xianqi.configure;

import java.util.List;
import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * @Description 仙器觉醒配置表
 * @Author Yang Gao
 * @Since 2016-10-30
 * @Version 1.1.0
 */
public class XianQiJueXingConfig extends AbsVersion {
    /**
     * id编号
     */
    private int id;
    /**
     * 仙器类型 1、御剑 2、翅膀 3、器灵 4、天工 5、天裳 6、天羽
     */
    private int type;
    /**
     * 仙器等级
     */
    private int level;
    /**
     * 升级需要的道具大类id集合
     */
    private List<String> itemIdList;
    /**
     * 升级仙器获得的属性集合(包含zplus)
     */
    private Map<String, Long> attrMap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<String> getItemIdList() {
        return itemIdList;
    }

    public void setItemIdList(List<String> itemIdList) {
        this.itemIdList = itemIdList;
    }

    public Map<String, Long> getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map<String, Long> attrMap) {
        this.attrMap = attrMap;
    }

}
