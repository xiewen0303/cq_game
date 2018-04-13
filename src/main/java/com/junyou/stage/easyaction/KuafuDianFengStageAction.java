package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.KuafuDianFengStageService;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.KUAFU_DIANFENG, groupName = EasyGroup.STAGE)
public class KuafuDianFengStageAction {

	@Autowired
	private KuafuDianFengStageService kuafuDianFengStageService;
	
	// 进入跨服巅峰之战房间,发送玩家数据到场景
	@EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_SEND_ROLE_DATA)
	public void kuafuDianFengSendRoleData(Message inMsg) {
		Object[] data = inMsg.getData();
		Integer loop = (Integer) data[0];//轮次编号
		Integer room = (Integer) data[1];//房间ID
		Long userRoleId = (Long) data[2];//玩家编号
		Object roleData = data[3];
		kuafuDianFengStageService.kuafuDianFengSendRoleData(loop, room, userRoleId,roleData);
	}

	// 跨服巅峰之战小场战斗开始
	@EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_START_PK)
	public void kuafuDianFengStartPk(Message inMsg) {
	    String stageId =  inMsg.getStageId();
		kuafuDianFengStageService.kuafuDianFengStartPk(stageId);
	}
	
    // 跨服巅峰之战小场战斗死亡
    @EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_DEATH_PK)
    public void kuafuDianFengDeathPk(Message inMsg) {
        String stageId =  inMsg.getStageId();
        Long winRoleId = inMsg.getRoleId();
        Long loseRoleId = (Long)inMsg.getData();
        kuafuDianFengStageService.kuafuDianFengDeathPk(stageId,winRoleId,loseRoleId);
    }
	
	// 跨服巅峰之战小场战斗结束
	@EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_END_PK)
	public void kuafuDianFengEndPk(Message inMsg) {
	    String stageId =  inMsg.getStageId();
	    kuafuDianFengStageService.kuafuDianFengEndPk(stageId);
	}
	
	// 跨服巅峰之战展示战斗结果
	@EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_RESULT_SHOW)
	public void kuafuDianFengShowResult(Message inMsg) {
	    String stageId =  inMsg.getStageId();
	    kuafuDianFengStageService.kuafuDianFengShowResult(stageId);
	}
	
	
	// 跨服巅峰之战结束,强踢人出场景
    @EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_FORCE_KICK)
    public void kuafubossForceKick(Message inMsg) {
        String stageId = (String)inMsg.getData();
        kuafuDianFengStageService.kuafuDianFengForceKick(stageId);
    }
    
    // 跨服巅峰玩家在小场比赛中下线处理
    @EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_OFFINE)
    public void kuafuDianfengOffine(Message inMsg) {
        Object[] data = inMsg.getData();
        long userRoleId = CovertObjectUtil.obj2long(data[0]);
        kuafuDianFengStageService.kuafuDianFengOffineHandle(userRoleId);
    }
	
}
