/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.gameconfig.publicconfig.configure.export;

/**
 * @Description 心魔-深渊副本公告配置数据
 * @Author Yang Gao
 * @Since 2016-7-27
 * @Version 1.1.0
 */
public class XinmoShenyuanFubenPublicConfig extends AdapterPublicConfig {
    /* 冷却cd时间:秒 */
    private int cd;
    /* 冷却cd减少单位时间 :秒 */
    private int cdTime;
    /* 冷却cd减少单位消耗元宝 */
    private int cdGold;
    
    public XinmoShenyuanFubenPublicConfig() {}

    /**
     * @param cd
     * @param cdTime
     * @param cdGold
     */
    public XinmoShenyuanFubenPublicConfig(int cd, int cdTime, int cdGold) {
        this.cd = cd;
        this.cdTime = cdTime;
        this.cdGold = cdGold;
    }

    public int getCd() {
        return cd;
    }

    public void setCd(int cd) {
        this.cd = cd;
    }

    public int getCdTime() {
        return cdTime;
    }

    public void setCdTime(int cdTime) {
        this.cdTime = cdTime;
    }

    public int getCdGold() {
        return cdGold;
    }

    public void setCdGold(int cdGold) {
        this.cdGold = cdGold;
    }

}
