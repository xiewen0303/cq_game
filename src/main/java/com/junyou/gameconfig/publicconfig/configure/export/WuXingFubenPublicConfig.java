/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.gameconfig.publicconfig.configure.export;


/**
 *@Description 五行副本公共数据配置信息
 *@Author Yang Gao
 *@Since 2016-4-20 下午3:50:21
 *@Version 1.1.0
 */
public class WuXingFubenPublicConfig extends AdapterPublicConfig {
    
    private int count;
    private int buyNeedGold;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getBuyNeedGold() {
        return buyNeedGold;
    }

    public void setBuyNeedGold(int buyNeedGold) {
        this.buyNeedGold = buyNeedGold;
    }
    
    
    
}
