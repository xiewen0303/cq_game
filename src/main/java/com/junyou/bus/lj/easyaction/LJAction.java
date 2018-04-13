package com.junyou.bus.lj.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.lj.service.LJService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.LJ, groupName = EasyGroup.BUS_CACHE)
public class LJAction {
	
	@Autowired
	private LJService ljService;
	
	/**请求信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.LJ_OPT)
	public void getInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int type = inMsg.getData();
		
		Object[] re = ljService.optLjInfo(userRoleId,type);
		if(re != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.LJ_OPT, re);
		}
	}
	 
	/**领奖
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.LJ_GET_AWARD)
	public void ljGetAward(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int id = inMsg.getData();
		
		Object[] re = ljService.ljGetAward(userRoleId,id);
		if(re != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.LJ_GET_AWARD, re);
		}
	}
}
