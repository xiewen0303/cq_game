package com.junyou.bus.lianchong.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.lianchong.service.LianChongService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 连充奖励
 */
@Controller
@EasyWorker(moduleName = GameModType.LIAN_CHONG, groupName = EasyGroup.BUS_CACHE)
public class LianChongAction {
	@Autowired
	private LianChongService lianChongService;
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.LIANCHONG_REWARD_GET)
	public void lingquLevelLibao(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		Integer dayType = (Integer)data[3];
		Object[] result = lianChongService.getReward(userRoleId, subId,version,configId,dayType);
		if (result != null) {// 返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.LIANCHONG_REWARD_GET, result);
		}
	}
	/**
	 * 模拟充值测试
	 * @param inMsg
	@EasyMapping(mapping = ClientCmdType.LIANCHONG_TEST)
	public void moniChongzhi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer addY =  (Integer)inMsg.getData();
		lianChongService.rechargeYb(userRoleId,Long.parseLong(addY+""));
	}
	**/
}
