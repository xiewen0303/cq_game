package com.junyou.bus.platform.qq.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.qq.service.TencentWeiDuanService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.QQ_PLATFORM_MODULE)
public class QqWeiDuanAction {
	@Autowired
	private TencentWeiDuanService tencentWeiDuanService;
	
	@EasyMapping(mapping = ClientCmdType.GET_WEIDUAN_INFO)
	public void getWeiDuanInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		if(!PlatformConstants.isQQ()){
			return;
		}
		int s = tencentWeiDuanService.getWeiDuanInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_WEIDUAN_INFO,s);
	}
	
	@EasyMapping(mapping = ClientCmdType.LINGQU_WEIDUAN)
	public void lingquWeiDuan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id =inMsg.getData();
		if(!PlatformConstants.isQQ()){
			return;
		}
		Object[] re = tencentWeiDuanService.lingQuWeiduan(userRoleId, id);
		BusMsgSender.send2One(userRoleId,  ClientCmdType.LINGQU_WEIDUAN, re);
		
	}
	
	
	@EasyMapping(mapping = ClientCmdType.GET_TGP_INFO)
	public void getTgpInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		if(!PlatformConstants.isQQ()){
			return;
		}
		int s = tencentWeiDuanService.getTgpInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TGP_INFO,s);
	}
	
	@EasyMapping(mapping = ClientCmdType.LINGQU_TGP)
	public void lingQuTgp(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		if(!PlatformConstants.isQQ()){
			return;
		}
		Object[] re = tencentWeiDuanService.lingQuTgp(userRoleId);
		BusMsgSender.send2One(userRoleId,  ClientCmdType.LINGQU_TGP, re);
		
	}
	
	@EasyMapping(mapping = ClientCmdType.LINGQU_TGP_LOGIN)
	public void lingQuTgpLogin(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		if(!PlatformConstants.isQQ()){
			return;
		}
		Object[] re = tencentWeiDuanService.lingQuTgpLogin(userRoleId);
		BusMsgSender.send2One(userRoleId,  ClientCmdType.LINGQU_TGP_LOGIN, re);
		
	}
	
	@EasyMapping(mapping = ClientCmdType.LINGQU_TGP_LEVEL)
	public void lingQuTgpLevel(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		if(!PlatformConstants.isQQ()){
			return;
		}
		Integer id =inMsg.getData();
		Object[] re = tencentWeiDuanService.lingQuTgpLevel(userRoleId,id);
		BusMsgSender.send2One(userRoleId,  ClientCmdType.LINGQU_TGP_LEVEL, re);
		
	}
	
	
	@EasyMapping(mapping = ClientCmdType.GET_QQ_3366_BAOZI_MEIRI)
	public void get3366BaoZiStatus(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		if(!PlatformConstants.isQQ()){
			return;
		}
		boolean re = tencentWeiDuanService.get3366BaoZiInfo(userRoleId);
		BusMsgSender.send2One(userRoleId,  ClientCmdType.GET_QQ_3366_BAOZI_MEIRI, re);
		
	}
	@EasyMapping(mapping = ClientCmdType.GET_QQ_3366_BAOZI_MEIRI_LINGQU)
	public void lingQu3366BaoZi(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		if(!PlatformConstants.isQQ()){
			return;
		}
		Integer id =inMsg.getData();
		Object[] re = tencentWeiDuanService.lingQu3366BaoZiLevel(userRoleId,id);
		BusMsgSender.send2One(userRoleId,  ClientCmdType.GET_QQ_3366_BAOZI_MEIRI_LINGQU, re);
		
	}
	
	
}
