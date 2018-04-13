package com.junyou.bus.huiyanshijin.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.huiyanshijin.service.HuiYanShiJingService;
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
public class HuiYanShiJinAction {

	@Autowired
	private HuiYanShiJingService huiYanShiJingService;
	
	/**
	 * 请求信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.HUIYAN_WA_KUANG)
	public void getLaowanjiaInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer level = (Integer) data[2];
		Integer kIndex = (Integer) data[3];
		
		Object[] result = huiYanShiJingService.waKuang(userRoleId, version, subId, level,kIndex);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.HUIYAN_WA_KUANG, result);
		}
	}
}
