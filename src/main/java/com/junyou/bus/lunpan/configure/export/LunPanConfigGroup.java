package com.junyou.bus.lunpan.configure.export;

import java.util.List;
import java.util.Map;

/**
 * 
 * @description 七日开服活动配置表
 * 
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class LunPanConfigGroup {

    
    private String des;// 活动描述
    private String pic;// 背景图
    private Integer gold;// 充值金额
    private Integer count;// 达到充值金额赠送的次数
    private Integer maxCount; // 默认最大活动次数
    private Integer maxGe;// 最大格位

    private Map<Integer, LunPanConfig> configMap;
    
    private List<Object[]> duiHuanData;// 兑换数据

    private Map<Integer, Integer> zpMap;// 转盘Map

    public Map<Integer, Integer> getZpMap() {
        return zpMap;
    }

    public void setZpMap(Map<Integer, Integer> zpMap) {
        this.zpMap = zpMap;
    }

    public List<Object[]> getDuiHuanData() {
        return duiHuanData;
    }

    public void setDuiHuanData(List<Object[]> duiHuanData) {
        this.duiHuanData = duiHuanData;
    }

    public Integer getMaxGe() {
        return maxGe;
    }

    public void setMaxGe(Integer maxGe) {
        this.maxGe = maxGe;
    }

    public Map<Integer, LunPanConfig> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<Integer, LunPanConfig> configMap) {
        this.configMap = configMap;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    private String md5Version;

    public String getMd5Version() {
        return md5Version;
    }

    public void setMd5Version(String md5Version) {
        this.md5Version = md5Version;
    }

}
