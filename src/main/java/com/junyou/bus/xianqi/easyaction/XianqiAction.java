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
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xianqi.service.XianqiService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * @Description 仙器觉醒指令交互处理
 * @Author Yang Gao
 * @Since 2016-10-30
 * @Version 1.1.0
 */
@Controller
@EasyWorker(moduleName = GameModType.XIANQIJUEXING, groupName = EasyGroup.BUS_CACHE)
public class XianqiAction {
    @Autowired
    private XianqiService xianqiService;

    @EasyMapping(mapping = ClientCmdType.XQJX_INFO)
    public void xianqiLoadInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = xianqiService.loadInfo(userRoleId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.XQJX_INFO, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.XQJX_UPLEVEL)
    public void xianqiUplevel(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        String goodsId = (String) inMsg.getData();
        Object[] result = xianqiService.upLevel(userRoleId, goodsId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.XQJX_UPLEVEL, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.XQJX_JUEXING)
    public void xianqiJueXing(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer xianqiType = (Integer) inMsg.getData();
        Object[] result = xianqiService.jueXing(userRoleId, xianqiType);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.XQJX_JUEXING, result);
        }
    }

}
