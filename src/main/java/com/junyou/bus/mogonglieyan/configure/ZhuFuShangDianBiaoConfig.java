package com.junyou.bus.mogonglieyan.configure;

import java.util.Map;

/**
 * 
 * @Description 灵火祝福,祝福商店表
 * @Author Yang Gao
 * @Since 2016-10-20
 * @Version 1.1.0
 */
public class ZhuFuShangDianBiaoConfig {
    // 商店排序号
    private int order;
    // 最小等级
    private int minLevel;
    // 最大等级
    private int mzxLevel;
    // 兑换需要精华数量
    private long needJinghua;
    //属性增加百分比
    private int percent;
    
    public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public int getMzxLevel() {
		return mzxLevel;
	}

	public void setMzxLevel(int mzxLevel) {
		this.mzxLevel = mzxLevel;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public long getNeedJinghua() {
        return needJinghua;
    }

    public void setNeedJinghua(long needJinghua) {
        this.needJinghua = needJinghua;
    }

}
