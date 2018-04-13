/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xianqi.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.xianqi.service.XianqiFubenService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 
 *@Description 仙器副本(云瑶晶脉公共副本场景)指令交互处理
 *@Author Yang Gao
 *@Since 2016-11-1
 *@Version 1.1.0
 */
@Controller
@EasyWorker(moduleName = GameModType.XIANQIFUBEN, groupName = EasyGroup.BUS_CACHE)
public class XianqiFubenAction {
    @Autowired
    private XianqiFubenService xianqiFubenService;

    @EasyMapping(mapping = ClientCmdType.XQFUBEN_ENTER)
    public void xqfubenEnter(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        xianqiFubenService.enterFubenStage(userRoleId, busMsgQueue);
        busMsgQueue.flushAndRemove();
    }
    
    @EasyMapping(mapping = ClientCmdType.XQFUBEN_EXIT)
    public void xqfubenExit(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        xianqiFubenService.exitFubenStage(userRoleId, busMsgQueue);
        busMsgQueue.flushAndRemove();
    }


}
