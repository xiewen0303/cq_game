package com.junyou.bus.mogonglieyan.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.mogonglieyan.service.RoleMoGongLieYanService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 
 * @Description 魔宫猎焰指令处理
 */
@Controller
@EasyWorker(moduleName = GameModType.MOGONGLIEYAN)
public class RoleMoGongLieYanAction {

    @Autowired
    private RoleMoGongLieYanService moGongLieYanService;

    @EasyMapping(mapping = ClientCmdType.MGLY_INIT_INFO)
    public void moGongLieYanInitInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = moGongLieYanService.initInfo(userRoleId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.MGLY_INIT_INFO, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.MGLY_BUY_LHZX)
    public void moGongLieYanBuyLhzx(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer order = (Integer) inMsg.getData();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        moGongLieYanService.buyLhzx(userRoleId, order, busMsgQueue);
        busMsgQueue.flushAndRemove();
    }

    @EasyMapping(mapping = ClientCmdType.MGLY_ENTER_STAGE)
    public void moGongLieYanEnterStage(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        moGongLieYanService.enterStage(userRoleId, busMsgQueue);
        busMsgQueue.flushAndRemove();
    }

    @EasyMapping(mapping = ClientCmdType.MGLY_EXIT_STAGE)
    public void moGongLieYanExitStage(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = moGongLieYanService.exitStage(userRoleId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.MGLY_EXIT_STAGE, result);
        }
    }

    @EasyMapping(mapping = InnerCmdType.I_MGLY_UPDATE_YUMO_VAL)
    public void updateMoGongLieYanYumoVal(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer cutYumoVal = inMsg.getData();
        moGongLieYanService.updateYumoVal(userRoleId, cutYumoVal);
    }

}
