package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.stage.service.MoGongLieYanStageService;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

@Controller
@EasyWorker(moduleName = GameModType.MOGONGLIEYAN, groupName = EasyGroup.STAGE)
public class MoGongLieYanStageAction {
    
    @Autowired
    private MoGongLieYanStageService moGongLieYanStageService;
 
    @TokenCheck(component = GameConstants.COMPONENT_MGLY_ADD_EXP_ZHENQI)
    @EasyMapping(mapping = InnerCmdType.I_MGLY_ADD_EXP_ZHENQI)
    public void innerAddExpZhenqi(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        moGongLieYanStageService.innerAddExpZhenqi(userRoleId, stageId);
    }
    
    @TokenCheck(component = GameConstants.COMPONENT_MGLY_CUT_YUMOVAL)
    @EasyMapping(mapping = InnerCmdType.I_MGLY_CUT_YUMO_VAL)
    public void innerCutYumoVal(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        moGongLieYanStageService.innerCutYumoVal(userRoleId, stageId);
    }
    
    @EasyMapping(mapping = InnerCmdType.I_MGLY_ROLE_DEAD)
    public void innerRoleDeadHandler(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        moGongLieYanStageService.innerRoleDeadHandler(userRoleId, stageId);
    }
    
    @EasyMapping(mapping = InnerCmdType.I_MGLY_MONSTER_DEAD)
    public void innerMonsterDeadHandler(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Object[] data = inMsg.getData();
        String monsterId = (String) data[0];
        Integer monsterRank = (Integer) data[1];
        moGongLieYanStageService.innerMonsterDeadHandler(userRoleId, stageId, monsterId, monsterRank);
    }
    
    //------------------------------------------
    @EasyMapping(mapping = InnerCmdType.I_MGLY_EXIT_STAGE)
    public void innerExitStageHandler(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        moGongLieYanStageService.innerExitHandler(userRoleId, stageId);
    }
    @EasyMapping(mapping = InnerCmdType.I_MGLY_DELAY_EXIT)
    public void innerDelayExitHandler(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        moGongLieYanStageService.innerDelayExitHandler(userRoleId, stageId);
    }
}
