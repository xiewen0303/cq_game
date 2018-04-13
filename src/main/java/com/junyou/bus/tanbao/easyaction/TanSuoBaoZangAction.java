package com.junyou.bus.tanbao.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tanbao.service.RFBTanBaoService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 探索宝藏
 * @description 
 * @author ZHONGDIAN
 * @email 891950886@qq.com
 * @date 2015-5-5 下午4:46:05
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class TanSuoBaoZangAction {

	@Autowired
	private RFBTanBaoService rFBTanBaoService;
	
	/**
	 * 请求追寻
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TANBAO_TANBAO)
	public void tanbao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer suoyin = (Integer) data[2];
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		Object[] result = rFBTanBaoService.tanbao(userRoleId, suoyin, subId, version,busMsgQueue);
		
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.TANBAO_TANBAO, result);
		}
		busMsgQueue.flush();
	}
	
	/**
	 * 领取王城奖励
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TANBAO_LINGQU_WANG)
	public void lingquWangCheng(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer suoyin = (Integer) data[2];
		
		Object[] result = rFBTanBaoService.lingqu(userRoleId, suoyin, subId, version);
		
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.TANBAO_LINGQU_WANG, result);
		}
	}
	
	
}
