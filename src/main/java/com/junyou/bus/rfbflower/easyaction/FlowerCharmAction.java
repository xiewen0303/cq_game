package com.junyou.bus.rfbflower.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.rfbflower.service.FlowerCharmRankService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * @description 
 */
@Controller
@EasyWorker(moduleName = GameModType.FLOWER_RANK)
public class FlowerCharmAction {

	
	@Autowired
	private FlowerCharmRankService flowerCharmRankService;
	
	/**
	 * 请求面板信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.FLOWER_RFB_PANEL_INFO)
	public void getPanelInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer startIndex = (Integer) data[2];
		Integer endIndex = (Integer) data[3];
		
		Object[] result = flowerCharmRankService.getPanelInfo(subId, version,userRoleId, startIndex, endIndex);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.FLOWER_RFB_PANEL_INFO, result);
		}
	}
	/**
	 * 购买
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.FLOWER_RFB_BUY)
	public void lingquLevelLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		String goodId = (String) data[2];
		Integer num = (Integer) data[3];
		
		Object[] result = flowerCharmRankService.buy(subId, version,userRoleId, goodId, num);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.FLOWER_RFB_BUY, result);
		}
	}
	
	
	
}
