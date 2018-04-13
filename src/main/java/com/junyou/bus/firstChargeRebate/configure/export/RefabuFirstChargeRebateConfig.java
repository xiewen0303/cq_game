package com.junyou.bus.firstChargeRebate.configure.export;

/**
 * 热发布首次100%返利活动配置信息
 * 
 * @Description
 * @Author Yang Gao
 * @Since 2016-6-6
 * @Version 1.1.0
 */
public class RefabuFirstChargeRebateConfig {

    /** 背景图片 **/
    private String pic;
    /** 规则说明描述 **/
    private String desc;
    /** 返回比例:百分比 **/
    private float rebateRatio;
    /** 配置文件加密信息 **/
    private String md5Version;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getRebateRatio() {
        return rebateRatio;
    }

    public void setRebateRatio(float rebateRatio) {
        this.rebateRatio = rebateRatio;
    }

    public String getMd5Version() {
        return md5Version;
    }

    public void setMd5Version(String md5Version) {
        this.md5Version = md5Version;
    }

}
