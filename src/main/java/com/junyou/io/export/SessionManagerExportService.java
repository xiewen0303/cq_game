package com.junyou.io.export;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.cmd.ClientCmdType;
import com.junyou.io.GameSession;
import com.junyou.io.global.GameSessionManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.codec.AmfUtil;

@Component
public class SessionManagerExportService{

	
	public Object[] getRoleIdsByIp(String ip){
		Map<Long,Object> ipMap = GameSessionManager.getInstance().getRoleIdsByIp(ip);
		if(ipMap == null || ipMap.size() == 0){
			return null;
		}else{
			return ipMap.keySet().toArray();
		}
	}
	
	public void addJunQingState(Long roleId){
		
	}
	
	public void delJunQingState(Long roleId){
		
	}
	
	
	public void tiChuOfflineSingle(Long roleId,Object errorMsg){
		try {
			
			// 看该角色是否已经上线，如果在线先踢下线
			GameSession inSession = GameSessionManager.getInstance().getSession4RoleId(roleId);
			if(null != inSession){
				//回复客户端断线原因
				byte[] clientBytes = AmfUtil.convertMsg2Bytes(ClientCmdType.KICK_ROLE,errorMsg);
				inSession.sendMsg(clientBytes);
				
				inSession.setJichu(true);
				// 通知业务组件角色下线
				inSession.getChannel().close();
				
				// 从会话管理中移除该会话
//				GameSessionManager.getInstance().removeSession(inSession.getChannel());
			}else{
				ChuanQiLog.error("将用户踢下线时未找到对应session，role id:{}",roleId);
			}
		
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	public Boolean isOnline(Long userRoleId){
		return GameSessionManager.getInstance().isOnline(userRoleId);
	}
	
	
	public boolean isChenmi(Long userRoleId){
		GameSession session = GameSessionManager.getInstance().getSession4RoleId(userRoleId);
		if(session != null){
			return session.isChenmi();
		}else{
			return false;
		}
	}
	
	/**
	 * 获取在线人数
	 */
	public int getSessionCounts(){
		return GameSessionManager.getInstance().getOnlineCount();
	}
	
	public GameSession getSessionById(String sessinId){
		return GameSessionManager.getInstance().getSession4Id(sessinId);
	}
	
	
	/**
	 * 绑定角色基本信息到session
	 * @param roleId
	 * @param name
	 * @param configId
	 * @param level
	 * @param createTime
	 */
	public void saveSessin4RoleBaseInfo(Long roleId,String name,Integer configId,int level,Long createTime){
		GameSessionManager.getInstance().saveSessin4RoleBaseInfo(roleId, name, configId, level, createTime);
	}
	
	/**
	 * 保存角色名称
	 * @param name
	 * @param level
	 * @param name
	 */
	public void saveSessin4Name(Long roleId,String name){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			GameSessionManager.getInstance().saveSessin4Name(roleId, name);
		}
	}
	
	/**
	 * 保存角色等级
	 * @param name
	 * @param level
	 * @param level
	 */
	public void saveSessin4Level(Long roleId,int level){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			GameSessionManager.getInstance().saveSessin4Level(roleId, level);
		}
	}
}