package com.junyou.bus.huoyuedu.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.service.HuoYueDuService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 *活跃度
 * 
 * @author lxn
 */
@Component
@EasyWorker(moduleName = GameModType.HUO_YUE_DU)
public class HuoYueDuAction {
   
	@Autowired
	private HuoYueDuService huoYueDuService;
	
	/**
	 * 获取活跃度 面板信息
	 */
	@EasyMapping(mapping = ClientCmdType.GET_HUOYUEDU_INFO)
	public void getInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = huoYueDuService.getTaskInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_HUOYUEDU_INFO, result);
	}
	/**
	 * 领取奖励
	 */
	@EasyMapping(mapping = ClientCmdType.GET_HUOYUEDU_AWARD)
	public void getAward(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer awardId = inMsg.getData();
		Object[] result = huoYueDuService.getAward(userRoleId, awardId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_HUOYUEDU_AWARD, result);	
		}
	}
	/**
	 * 测试阶段模拟某个人物完成。
	 * 测试完注释掉 lxn:TODO 
	@EasyMapping(mapping = ClientCmdType.HUOYUEDU_TEST_ACTIVITY)
	public void testCompleteActivity(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		GamePublishEvent.publishEvent(new HuoyueduEvent(userRoleId, ActivityEnum.getEnumById(id)));
	}
	 */
	
	/**
	 *  跨服需要参与活跃度的都在这里处理
	 *  A18--参与1次幻境历练
	 * @param message
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_HUOYUEDU_HUANJING,kuafuType=EasyKuafuType.KF2S_HANDLE_TYPE)
	public void guildMemberOffline(Message inMsg){
		 Object[] data  = (Object[])inMsg.getData();
		 ActivityEnum  acEnum = (ActivityEnum)data[0];
		 Long  roleId  = (Long)data[1];
		this.huoYueDuService.completeActivity(roleId, acEnum);
	}
	/**
	 *  跨服需要参与活跃度的都在这里处理
	 *  A26--参与1次八卦阵副本
	 * @param message
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_HUOYUEDU_BAGUA,kuafuType=EasyKuafuType.KF2S_HANDLE_TYPE)
	public void innerHuoyueduBagua(Message inMsg){
		 Object[] data  = (Object[])inMsg.getData();
		 ActivityEnum  acEnum = (ActivityEnum)data[0];
		 Long  roleId  = (Long)data[1];
		this.huoYueDuService.completeActivity(roleId, acEnum);
	}
	/**
	 * 涉及到他人业务的统计活跃度统一以抛指令的方式处理
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_HUOYUEDU_COUNT)
	public void countHuoYueDu(Message inMsg){
		 Object[] data  = (Object[])inMsg.getData();
		 ActivityEnum  acEnum = (ActivityEnum)data[0];
		 Long  roleId  = (Long)data[1];
		this.huoYueDuService.completeActivity(roleId, acEnum);
	}
	/**
	 * 其他模块需要通过指令的方式统计活跃度 
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_HUOYUEDU)
	public void yewaiBossHuoyuedu(Message inMsg){
		 Long userRoleId = inMsg.getRoleId();
		 Object[] data  = (Object[])inMsg.getData(); 
		 ActivityEnum  acEnum = (ActivityEnum)data[0];
		  
		this.huoYueDuService.completeActivity(userRoleId,acEnum);
	}
	
	
	
	
	
}
