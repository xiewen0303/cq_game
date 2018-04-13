/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.gameconfig.publicconfig.configure.export;

/**
 * @Description 心魔-魔神公告配置数据
 * @Author Yang Gao
 * @Since 2016-7-20
 * @Version 1.1.0
 */
public class XinmoMoshenPublicConfig extends AdapterPublicConfig {
    /* 心魔-魔神噬体cd */
    private int xinMoCd;
    /* 心魔-魔神噬体持续cd */
    private int xinMoChiXuCd;

    public int getXinMoCd() {
        return xinMoCd;
    }

    public void setXinMoCd(int xinMoCd) {
        this.xinMoCd = xinMoCd;
    }

    public int getXinMoChiXuCd() {
        return xinMoChiXuCd;
    }

    public void setXinMoChiXuCd(int xinMoChiXuCd) {
        this.xinMoChiXuCd = xinMoChiXuCd;
    }

}
