package com.junyou.bus.xiulianzhilu.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.service.XiuLianService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.XIULIANRASK, groupName = EasyGroup.BUS_CACHE)
public class XiuLianAction {
	
	@Autowired
	private XiuLianService xiuLianService;	
	
	/**任务进度变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_XIULIAN_TASK_CHARGE)
	public void taskChaege(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = inMsg.getData();
		Integer type = (Integer) obj[0];//类型
		Object data = obj[1];//额外数据
		xiuLianService.taskCharge(userRoleId, type, data);
	}
	/**任务进度变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_XIULIAN_TASK_CHARGE_TS)
	public void taskChaegeTs(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = inMsg.getData();
		Integer type = (Integer) obj[0];//类型
		Object data = obj[1];//额外数据
		xiuLianService.taskChargeTxType(userRoleId, type, data);
	}
	
	/**请求信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.XIULIAN_GET_INFO)
	public void getInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] re = xiuLianService.getInfo(userRoleId);
		
		if(re != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.XIULIAN_GET_INFO, re);
		}
	}
	
	/**
	 * 积分兑换物品
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.XIULIAN_JIFEN_DUIHUAN)
	public void duihuan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = inMsg.getData();	
		Integer jcLevel = (Integer) obj[0];
		Integer few = (Integer) obj[1];
		Object[] re = xiuLianService.jifenDuihuan(userRoleId, jcLevel, few);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIULIAN_JIFEN_DUIHUAN, re);
	}
	/**
	 * 领取每日奖励
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.XIULIAN_LINGQU_DAY)
	public void lingquDay(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer day = inMsg.getData();	
		Object[] re = xiuLianService.lingquDay(userRoleId, day);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIULIAN_LINGQU_DAY, re);
	}
	
	/**
	 * 领取每日奖励
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.XIULIAN_LINGQU_JIFEN)
	public void lingquJifen(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id  = inMsg.getData();	
		Object[] re = xiuLianService.lingquJifen(userRoleId, id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIULIAN_LINGQU_JIFEN, re);
	}
	
	
	/**
	 * 领称号
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.XIULIAN_LINGQU_CHENGHAO)
	public void chenghao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] re = xiuLianService.getChengHao(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIULIAN_LINGQU_CHENGHAO, re);
	}
	
}
