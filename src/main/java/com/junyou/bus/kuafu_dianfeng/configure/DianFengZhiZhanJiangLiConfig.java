package com.junyou.bus.kuafu_dianfeng.configure;

import com.kernel.data.dao.AbsVersion;

/**
 * @Description 巅峰之战奖励表
 * @Author Yang Gao
 * @Since 2016-5-28
 * @Version 1.1.0
 */
public class DianFengZhiZhanJiangLiConfig extends AbsVersion {

    // 奖励标示ID
    private int id;
    // 获得奖励的名次
    private int rank;
    // 礼包ID
    private int rewardId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

}
