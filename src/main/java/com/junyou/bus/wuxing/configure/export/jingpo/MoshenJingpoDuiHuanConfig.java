/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.wuxing.configure.export.jingpo;

import com.kernel.data.dao.AbsVersion;

/**
 * @Description 精魄兑换配置
 * @Author Yang Gao
 * @Since 2016-5-10
 * @Version 1.1.0
 */
public class MoshenJingpoDuiHuanConfig extends AbsVersion {

    /* 配置编号 */
    private int id;
    /* 商城排序 */
    private int order;
    /* 需要的精华 */
    private int needjinghua;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getNeedjinghua() {
        return needjinghua;
    }

    public void setNeedjinghua(int needjinghua) {
        this.needjinghua = needjinghua;
    }

}
