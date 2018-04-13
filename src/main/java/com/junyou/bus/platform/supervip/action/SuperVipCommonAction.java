package com.junyou.bus.platform.supervip.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.supervip.service.SuperVipCommonService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 超级会员公用
 * @author lxn
 *
 */
@Component
@EasyWorker(moduleName = GameModType.PLATFORM_SOGOU)
public class SuperVipCommonAction {

	@Autowired
	private SuperVipCommonService superVipCommonService;
	
	/**
	 * 超级会员请求信息
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_SUPER_QQ_STATE)
	public void get37WanGiftInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  result = superVipCommonService.getSuperVipInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_SUPER_QQ_STATE, result);
	}
	/**
	 * 超级会员活动是否关闭
	 */
	@EasyMapping(mapping = ClientCmdType.GET_PLATFORM_SUPER_QQ_CLOSE)
	public void isCloseActivity(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int result = superVipCommonService.isCloseActivity();
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PLATFORM_SUPER_QQ_CLOSE, result);
	}
	/**
	 * 其他模块模拟充值都可以再这里打开测试
	 * 模拟充值  打版本的时候删掉lxn:TODO
	@EasyMapping(mapping = ClientCmdType.RECHARGE_TEST)
	public void testRecharge(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long money  =Long.parseLong(inMsg.getData()+""); //一人民币等于10元宝
		superVipCommonService.sendRechargeToClient(userRoleId,money*10);
	}
	***/	 
}
