package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.KuafuYunGongStageService;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.KUAFU_YUNGONG, groupName = EasyGroup.STAGE)
public class KuafuYunGongStageAction {
    
    @Autowired
    private KuafuYunGongStageService kuafuYunGongStageService;
    
	// -----------------------------------源服转到跨服执行的指令-----------------------
	// 进入跨服云宫之巅活动,发送玩家数据到场景
    @EasyMapping(mapping = InnerCmdType.KUAFU_YUNGONG_SEND_ROLE_DATA)
    public void kuafuYunGongSendRoleData(Message inMsg) {
        Object[] data = inMsg.getData();
        Long userRoleId = (Long) data[0];//玩家编号
        String sourceServerId = (String)data[1];
        Object roleData = data[2];
        kuafuYunGongStageService.kuafuYunGongSendRoleData(userRoleId, sourceServerId, roleData);
    }
    
    // 执行采集旗子指令
    @EasyMapping(mapping = InnerCmdType.I_KUAFU_YUNGONG_COLLECT)
    public void kuafuYunGongCollect(Message inMsg) {
        String stageId = inMsg.getStageId();
        Long userRoleId = inMsg.getRoleId();
        Long qiziGuid = LongUtils.obj2long(inMsg.getData());
        kuafuYunGongStageService.kuafuYunGongCollect(stageId,userRoleId,qiziGuid);
    }
    
    // 执行拔起旗子指令
    @EasyMapping(mapping = InnerCmdType.I_KUAFU_YUNGONG_PULL)
    public void kuafuYunGongPull(Message inMsg) {
        String stageId = inMsg.getStageId();
        Long userRoleId = inMsg.getRoleId();
        kuafuYunGongStageService.kuafuYunGongPull(stageId,userRoleId);
    }
    
    // 跨服场景中门主及其仙侣切换外显
    @EasyMapping(mapping = InnerCmdType.I_KUAFU_YUNGONG_CHANGE_CLOTHES)
    public void kuafuYunGongChangeClothes(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        kuafuYunGongStageService.kuafuYunGongChangeClothes(userRoleId, stageId);
    }
    
    // 退出场景指令
    @EasyMapping(mapping = InnerCmdType.I_KUAFU_YUNGONG_EXIT)
    public void kuafuYunGongExit(Message inMsg) {
        String stageId = inMsg.getStageId();
        Long userRoleId = inMsg.getRoleId();
        kuafuYunGongStageService.kuafuYunGongExit(stageId,userRoleId);
    }
    
    // -----------------------------------跨服执行的指令-----------------------
    // 旗子占领时间到产生获胜公会,提前结束活动 
    @EasyMapping(mapping = InnerCmdType.I_KUAFU_YUNGONG_HAS_WINNER)
    public void kuafuYunGongHasWinner(Message inMsg) {
        String stageId = inMsg.getStageId();
        kuafuYunGongStageService.kuafuYunGongHasWinner(stageId);
    }
    
    // 旗子坐标同步
    @EasyMapping(mapping = InnerCmdType.I_KUAFU_YUNGONG_SYN_QIZI)
    public void kuafuYunGongSynPosition(Message inMsg) {
        String stageId = inMsg.getStageId();
        kuafuYunGongStageService.kuafuYunGongSynPosition(stageId);
    }
    
    // 增加经验和真气 
    @EasyMapping(mapping = InnerCmdType.I_KUAFU_YUNGONG_ADD_EXP_ZQ)
    public void kuafuYunGongAddExpZq(Message inMsg) {
        String stageId = inMsg.getStageId();
        Long userRoleId = inMsg.getRoleId();
        kuafuYunGongStageService.kuafuYunGongAddExpZq(stageId,userRoleId);
    }
    
    // 扛旗者死亡 
    @EasyMapping(mapping = InnerCmdType.I_KUAFU_YUNGONG_OWNER_UPDATE)
    public void kuafuYunGongOwnerUpdate(Message inMsg) {
        String stageId = inMsg.getStageId();
        Long userRoleId = inMsg.getRoleId();
        kuafuYunGongStageService.kuafuYunGongOwnerUpdate(stageId,userRoleId);
    }
    
    // 活动过期结束 
    @EasyMapping(mapping = InnerCmdType.I_KUAFU_YUNGONG_OVER)
    public void kuafuYunGongOver(Message inMsg) {
        String stageId = inMsg.getStageId();
        kuafuYunGongStageService.kuafuYunGongOver(stageId);
    }
    
    // 活动清人 
    @EasyMapping(mapping = InnerCmdType.I_KUAFU_YUNGONG_CLEAR)
    public void kuafuYunGongClear(Message inMsg) {
        String stageId = inMsg.getStageId();
        kuafuYunGongStageService.kuafuYunGongClearRole(stageId);
    }
}
