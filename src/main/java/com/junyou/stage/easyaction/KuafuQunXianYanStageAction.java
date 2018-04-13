package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.KuafuQunXianYanStageService;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.KUAFU_QUNXIANYAN, groupName = EasyGroup.STAGE)
public class KuafuQunXianYanStageAction {

	@Autowired
	private KuafuQunXianYanStageService kuafuQunXianYanStageService;

	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_SEND_ROLE_DATA)
	public void kuafuBossSendRoleData(Message inMsg) {
		Object[] data = inMsg.getData();
		Long userRoleId = (Long) data[0];
		String name = (String) data[1];
		Object roleData = data[2];
		kuafuQunXianYanStageService.kuafuBossSendRoleData(userRoleId,name,roleData);
	}

	/**
	 * 跨服boss加经验
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_ADD_EXP_DINGSHI)
	public void kuafuBossAddExp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		String stageId = (String) data[0];
		Long exp = (Long) data[1];
		kuafuQunXianYanStageService.addExpDingshi(stageId, userRoleId, exp);
	}

	/**
	 * 跨服boss加经验
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_EXIT)
	public void kuafuBossExit(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		kuafuQunXianYanStageService.kuafuBossExit(userRoleId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFU_QUNXIANYAN_RANK)
	public void kuafuBossRank(Message inMsg) {
		String stageId = inMsg.getStageId();
		kuafuQunXianYanStageService.kuafuBossRank(stageId);
	}

	/**
	 * 跨服群仙宴强T人
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_FORCE_KICK)
	public void kuafubossForceKick(Message inMsg) {
		String stageId = inMsg.getStageId();
		kuafuQunXianYanStageService.kuafubossForceKick(stageId);
	}

	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_FUHUO)
	public void kuafuBossFuhuo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer mapId = inMsg.getData();
		String stageId = StageUtil.getStageId(mapId, 1);
		kuafuQunXianYanStageService.kuafuBossFuhuo( userRoleId,stageId);
	}
	
	/**
	 * 增加积分
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_ADD_JIFEN)
	public void kuafuBossAddJiFen(Message inMsg) {
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		String zyId = inMsg.getData();
		kuafuQunXianYanStageService.kuafuLuanDouAddJiFen(stageId, userRoleId,zyId);
	}
	/**
	 * 死亡积分
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_DEAD_JIFEN)
	public void deadJifen(Message inMsg) {
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		Long mbUserRoleId = inMsg.getData();//死亡的人的角色ID
		kuafuQunXianYanStageService.deadJifen(stageId, userRoleId,mbUserRoleId);
	}
	/**
	 * 刷新资源
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_ZIYUAN)
	public void shuaZiYuan(Message inMsg) {
		String stageId = inMsg.getStageId();
		Object[] obj = inMsg.getData();//死亡的人的角色ID
		Integer zyId = (Integer) obj[0];
		Integer x = (Integer) obj[1];
		Integer y = (Integer) obj[2];
		
		kuafuQunXianYanStageService.shuaZiYuan(stageId, zyId,x,y);
	}
	
	
	/**
	 * 请求排行榜
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_GET_RANK)
	public void getRankInner(Message inMsg) {
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		kuafuQunXianYanStageService.getRank(stageId,userRoleId);
	}
	
}
