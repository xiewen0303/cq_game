package com.junyou.bus.xiaofei.easyation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiaofei.server.RefabuXiaoFeiService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 热发布神秘商店运营活动
 * @description 
 * @author ZHONGDIAN
 * @date 2015-5-5 下午4:46:05
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class XiaoFeiPaiMingAction {

	@Autowired
	private RefabuXiaoFeiService refabuXiaoFeiService;
	
	/**
	 * 请求购买
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_XIAOFEI_INFO)
	public void buy(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer begin = (Integer) data[2];
		Integer number = (Integer) data[3];
		
		Object[] result = refabuXiaoFeiService.getXiaoFeiInfo(userRoleId, version, subId, begin, number);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_XIAOFEI_INFO, result);
		}
	}
	
}
