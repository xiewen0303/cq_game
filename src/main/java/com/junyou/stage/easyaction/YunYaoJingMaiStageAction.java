package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.stage.service.YunYaoJingMaiStageService;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

@Controller
@EasyWorker(moduleName = GameModType.XIANQIFUBEN, groupName = EasyGroup.STAGE)
public class YunYaoJingMaiStageAction {
    
    @Autowired
    private YunYaoJingMaiStageService yunYaoJingMaiStageService;
    
    @EasyMapping(mapping = InnerCmdType.INNER_YYJM_EXIT_STAGE)
    public void innerExitStageHandler(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        yunYaoJingMaiStageService.innerExitHandler(userRoleId, stageId);
    }
    
    @TokenCheck(component = GameConstants.COMPONENT_YUNYAOJINGMAI_END_PRODUCE)
    @EasyMapping(mapping = InnerCmdType.INNER_YYJM_FORCE_KICK)
    public void innerForceKickHandler(Message inMsg){
        String stageId = inMsg.getStageId();
        yunYaoJingMaiStageService.innerForceKickHandler(stageId);
    }
    
    @EasyMapping(mapping = ClientCmdType.XQFUBEN_COLLECT)
    public void xqfubenCollect(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Long collectId = LongUtils.obj2long(inMsg.getData());
        Object[] result = yunYaoJingMaiStageService.xqfubenCollect(userRoleId, stageId, collectId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XQFUBEN_COLLECT, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.XQFUBEN_PULL)
    public void xqfubenPull(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Object[] result = yunYaoJingMaiStageService.xqfubenPull(userRoleId, stageId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XQFUBEN_PULL, result);
        }
    }
    
    
}
