package com.junyou.bus.platform.qq.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.qq.service.QQTgpNengLiangService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.QQ_PLATFORM_MODULE)
public class QqTGPNengLiangAction {
	@Autowired
	private QQTgpNengLiangService QQTgpNengLiangService;
	
	@EasyMapping(mapping = ClientCmdType.GET_TGP_ZHUANOAN_INFO)
	public void getTgpInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		if(!PlatformConstants.isQQ()){
			return;
		}
		Object[] s = QQTgpNengLiangService.getTgpInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TGP_ZHUANOAN_INFO,s);
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_TGP_ZSONG_HAOYOU)
	public void zengSongHaoYou(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		if(!PlatformConstants.isQQ()){
			return;
		}
		String name = inMsg.getData();
		Object[] s = QQTgpNengLiangService.zengSongHaoYou(userRoleId,name);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TGP_ZSONG_HAOYOU,s);
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_TGP_ZHUANA)
	public void zhuan(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		if(!PlatformConstants.isQQ()){
			return;
		}
		Object[] s = QQTgpNengLiangService.zhuan(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TGP_ZHUANA,s);
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_TGP_DUIHUAN)
	public void duihuan(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		if(!PlatformConstants.isQQ()){
			return;
		}
		Integer id = inMsg.getData();
		Object[] s = QQTgpNengLiangService.duihuan(userRoleId,id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TGP_DUIHUAN,s);
	}
	
	
}
