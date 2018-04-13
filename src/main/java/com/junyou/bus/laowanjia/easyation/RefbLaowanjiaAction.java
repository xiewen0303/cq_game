package com.junyou.bus.laowanjia.easyation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.laowanjia.service.RefbLaoWanJiaService;
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
public class RefbLaowanjiaAction {

	@Autowired
	private RefbLaoWanJiaService refbLaoWanJiaService;
	
	/**
	 * 请求信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.LAOWANJIA_INFO)
	public void getLaowanjiaInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		
		Object[] result = refbLaoWanJiaService.getLaoWanJiaInfo(userRoleId, version, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.LAOWANJIA_INFO, result);
		}
	}
	/**
	 * 请求领取
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.LAOWANJIA_LINGQU)
	public void lingquLevelLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		Integer type = (Integer) data[3];
		
		Object[] result = refbLaoWanJiaService.lingqu(userRoleId, version, subId, configId, type);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.LAOWANJIA_LINGQU, result);
		}
	}
	
	
}
