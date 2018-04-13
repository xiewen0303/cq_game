package com.junyou.io.easyaction;

import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.io.GameSession;
import com.junyou.io.Message.IoMessage;
import com.junyou.io.global.GameSessionManager;
import com.junyou.io.global.PingVo;
import com.junyou.log.ChuanQiLog;
import com.junyou.module.GameModType;
import com.junyou.utils.codec.AmfUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Component
@EasyWorker(moduleName = GameModType.PING_MODULE)
public class PingAction {
	
	/**
	 * 40 
	 * 时间间隔大于服务器记录的值1.1倍则踢先线
	 * 如果当前时间戳 - 上次时间戳 小于时间间隔乘以容差值  now - lastTimer < interval * ( 1- allowance)，如果此错误次数达到错误次数上限次
	 */
	@EasyMapping(mapping = ClientCmdType.PING_CMD)
	public void ping(IoMessage message){
		Object[] tmps = (Object[]) message.getMsgSource();
		Long roleId = message.getRoleId();
		Object cmd = tmps[0];
		
		GameSession session = GameSessionManager.getInstance().getSession4RoleId(roleId);
		if(session == null){
			return;
		}
		
		PingVo pingVo = session.getPingVo();
		if(null == pingVo){
			pingVo = new PingVo();
			session.setPingVo(pingVo);
		}else{
			boolean isSuccess = pingVo.checkInterval();
			if(!isSuccess){
				//回复客户端断线原因
				byte[] clientBytes = AmfUtil.convertMsg2Bytes(ClientCmdType.KICK_ROLE,new Object[]{GameConstants.SERVER_JS_OFFLINE});
				session.sendMsg(clientBytes);
				
				session.getChannel().close();
				
				StringBuffer msg = new StringBuffer();
				msg.append(roleId).append(",").append(pingVo.genInterval()).append(",").append(pingVo.getCalcMsg());
				//记录被踢下线日志
				ChuanQiLog.error("ping包异常:"+msg.toString());
				return;
			}
		}
		
		Integer interval = pingVo.genInterval();
		byte[] bytes = AmfUtil.convertMsg2Bytes(cmd, interval);
		session.sendMsg(bytes);
	}
	
	
	/**
	 * 客户端服务器端时间同步 
	 * @param message
	 */
	@EasyMapping(mapping = ClientCmdType.CLIENT_SYSTEM_TIME)
	public void serverTime(IoMessage message){
		Object[] tmps = (Object[]) message.getMsgSource();
		Object cmd = tmps[0];
		
		Long roleId = message.getRoleId();
		
		byte[] bytes = AmfUtil.convertMsg2Bytes(cmd,GameSystemTime.getSystemMillTime());
		GameSession session = GameSessionManager.getInstance().getSession4RoleId(roleId);
		if(session != null){
			session.sendMsg(bytes);
		}
	}
	
}
