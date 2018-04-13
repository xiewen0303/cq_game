/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.wuxing.vo;

/**
 * @Description 五行魔神精魄数据传输对象
 * @Author Yang Gao
 * @Since 2016-5-10
 * @Version 1.1.0
 */
public class WuXingJingpoVO {

    /* guid */
    private long guid;
    /* 配置编号 */
    private long goodsId;
    /* 当前吞噬经验 */
    private int eatExp;
    /* 存放类型-1=魔神背包;1,2,3,4,5对应五位魔神 */
    private int type;
    /* 存放孔位-1=魔神背包;1,2,3,4,5,6,7对应的7个孔位 */
    private int kongwei;

    /**
     * @param guid
     * @param goodsId
     * @param eatExp
     * @param attrsMap
     * @param type
     * @param kongwei
     */
    public WuXingJingpoVO(long guid, long goodsId, int eatExp, int type, int kongwei) {
        super();
        this.guid = guid;
        this.goodsId = goodsId;
        this.eatExp = eatExp;
        this.type = type;
        this.kongwei = kongwei;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public long getGuid() {
        return guid;
    }

    public void setGuid(long guid) {
        this.guid = guid;
    }

    public int getEatExp() {
        return eatExp;
    }

    public void setEatExp(int eatExp) {
        this.eatExp = eatExp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getKongwei() {
        return kongwei;
    }

    public void setKongwei(int kongwei) {
        this.kongwei = kongwei;
    }

}
