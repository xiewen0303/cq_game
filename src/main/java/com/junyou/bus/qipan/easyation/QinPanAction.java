package com.junyou.bus.qipan.easyation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.qipan.server.QiPanService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 热发布全民修仙运营活动
 * @description 
 * @author ZHONGDIAN
 * @date 2015-5-5 下午4:46:05
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class QinPanAction {

	@Autowired
	private QiPanService qiPanService;
	
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_QIPAN)
	public void lingquLevelLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		
		Object[] result = qiPanService.zhuan(userRoleId, version, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_QIPAN, result);
		}
	}
	
	
	
}
