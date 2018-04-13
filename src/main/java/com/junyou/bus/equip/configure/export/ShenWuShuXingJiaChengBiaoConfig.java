package com.junyou.bus.equip.configure.export;

import java.util.Map;

/**
 * 
 * @description 神武星铸属性加成配置表
 * 
 * @author Yang Gao
 * @date 2016-04-11 14:24:39
 */
public class ShenWuShuXingJiaChengBiaoConfig {

    private Integer id;

    private Integer lv;

    private Integer type;

    private Map<String, Long> attrs;

    private Integer needNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Map<String, Long> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, Long> attrs) {
        this.attrs = attrs;
    }

    public Integer getNeedNum() {
        return needNum;
    }

    public void setNeedNum(Integer needNum) {
        this.needNum = needNum;
    }

}
