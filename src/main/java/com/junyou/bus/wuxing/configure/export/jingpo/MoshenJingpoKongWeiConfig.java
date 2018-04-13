/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.wuxing.configure.export.jingpo;

import com.kernel.data.dao.AbsVersion;

/**
 * @Description 精魄孔位配置
 * @Author Yang Gao
 * @Since 2016-5-10
 * @Version 1.1.0
 */
public class MoshenJingpoKongWeiConfig extends AbsVersion {
    /* 孔位类型1=魔神身上7个孔位;2=魔神背包格位 */
    private int type;
    /* 孔位编号 */
    private int kong;
    /* 开启孔位需要条件类型1=魔神等阶;2=元宝 */
    private int needType;
    /* 开启孔位需要的条件数量 */
    private int needCount;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getKong() {
        return kong;
    }

    public void setKong(int kong) {
        this.kong = kong;
    }

    public int getNeedType() {
        return needType;
    }

    public void setNeedType(int needType) {
        this.needType = needType;
    }

    public int getNeedCount() {
        return needCount;
    }

    public void setNeedCount(int needCount) {
        this.needCount = needCount;
    }

}
