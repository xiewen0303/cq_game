package com.junyou.bus.dati.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.dati.service.DaTiService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.DINGSHI_ACTIVE_MODULE)
public class DaTiAction {
	
	@Autowired
	private DaTiService daTiService;

	@EasyMapping(mapping = InnerCmdType.DINGSHI_DATI_STATRT)
	public void datiActivityStart(Message inMsg) {
		Integer activityId = inMsg.getData();
		daTiService.datiActivityStart(activityId);
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_DATI_STOP)
	public void datiActivityEnd(Message inMsg) {
		daTiService.datiActivityEnd();
	}
	
	@EasyMapping(mapping=ClientCmdType.GET_TIMU)
	public void getTimu(Message message){
		Long userRoleId=message.getRoleId();
		Object[] result=daTiService.getTimu(userRoleId);
		BusMsgSender.send2One(message.getRoleId(), ClientCmdType.GET_TIMU, result);
	}
	@EasyMapping(mapping=ClientCmdType.SUBMIT_OPT)
	public void submitOpt(Message message){
		Long userRoleId=message.getRoleId();
		Object[] data=message.getData();
		int titleId=(int) data[0];
		int opt=(int)data[1];
		int useDouble=(int)data[2];
		Object[] sendData= daTiService.submitOpt(userRoleId,titleId,opt,useDouble);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SUBMIT_OPT, sendData);
		
	}
	
	@EasyMapping(mapping=ClientCmdType.SHOW_RANK)
	public void showRank(Message message){
		Long userRoleId=message.getRoleId();
		int titleId=message.getData();
		Object[] sendData= daTiService.showRank(userRoleId,titleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SHOW_RANK, sendData);
		
	}
	
	@EasyMapping(mapping=ClientCmdType.USE_EXCLUDE_CARD)
	public void useExcludeCard(Message message){
		Long userRoleId=message.getRoleId();
		int titleId=message.getData();
		Object[] sendData= daTiService.useExcludeCard(userRoleId, titleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.USE_EXCLUDE_CARD, sendData);
		
	}
	
	@EasyMapping(mapping=ClientCmdType.CURRENT_ITEM)
	public void currentItem(Message message){
		Long userRoleId=message.getRoleId();
		Object[] sendData= daTiService.currentItem(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CURRENT_ITEM, sendData);
		
	}
	
/*
 * 
 * 	@EasyMapping(mapping = ClientCmdType.PROMPTLY_AWARD)
	public void promptlyAward(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] sendData=daTiService.promptlyAward(userRoleId);
		PublicMsgSender.send2One(userRoleId, ClientCmdType.PROMPTLY_AWARD, sendData);
		
	}*/
}
