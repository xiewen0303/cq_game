package com.junyou.bus.kuafu_dianfeng.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafu_dianfeng.service.KuafuDianFengService;

/**
 *@Description  巅峰之战对外访问类
 *@Author Yang Gao
 *@Since 2016-5-18
 *@Version 1.1.0
 */
@Service
public class KuafuDianFengExportService {

    @Autowired
    private KuafuDianFengService kuafuDianFengService;

    /**
     * 巅峰之战:定时器开始轮循比赛
     */
    public void startDianFengLoop() {
        kuafuDianFengService.loopDianFeng();
    }
    
    /**
     * 获取玩家跨服场景编号
     */
    public String getDianFengStageIdByRoleId(Long userRoleId){
        return kuafuDianFengService.getDianFengRoleStageId(userRoleId);
    }
    
    /**
     * 更新玩家巅峰之战结果数据
     */
    
    public void updateDianFengRedisData(int loop, int room, long winRoleId) {
        kuafuDianFengService.updateDianFengRedisData(loop, room, winRoleId);
    }
    
    /**
     * 清除巅峰之战结果数据
     */
    public void clearDianFengResultData(){
        kuafuDianFengService.clearAllDianFengData();
    }
    
    /**
     * 巅峰之战发送奖励
     */
    public void rewardDianFeng(){
        kuafuDianFengService.sendDianFengReward();
    }
    
}
