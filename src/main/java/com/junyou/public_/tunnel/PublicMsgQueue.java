package com.junyou.public_.tunnel;

import java.util.ArrayList;
import java.util.List;



/**
 * @description 公共消息发送队列
 * @author ShiJie Chi
 * @date 2012-4-19 下午4:08:37 
 */
public class PublicMsgQueue {
	
	private List<IMsg> msgs = new ArrayList<IMsg>();
	
	/**
	 * 增加发送消息
	 * @param busMsg
	 */
	private void addMsg(IMsg busMsg){
		msgs.add(busMsg);
	}
	
	
	/**
	 * 输出所有消息
	 */
	public void flush(){
		
		if(msgs.size() > 0){
			for(IMsg msg : msgs){
				msg.flush();
			}
		}
		
	}
	
	/**
	 * 增加发往客户端的单发消息
	 */
	public void addMsg(Long roleId, Short command,Object result){
		addMsg(new ClientMsg(roleId, command, result));
	}
	
	/**
	 * 增加发往客户端的多发消息
	 */
	public void addMsg(Long[] roleIds, Short command,Object result){
		
		addMsg(new MultiClientMsg(roleIds, command, result));
	}
	
	/**
	 * 增加发往场景的消息
	 */
	public void addStageMsg(Long roleId, Short command,Object result){
		addMsg(new StageMsg(roleId, command, result));
	}
	
	/**
	 * 增加发往公共内部的消息
	 */
	public void addGuildChatMsg(Long roleId, Short command,Object result){
		addMsg(new GuildChatMsg(roleId, command, result));
	}
	
	/**
	 * 增加发往场景控制的消息
	 */
	public void addStageControllMsg(Long roleId, Short command,Object result){
		addMsg(new StageControllMsg(roleId, command, result));
	}
	
	public void addBroadcastMsg(Short command,Object result){
		addMsg(new BroadcastMsg(command,result));
	}
	
	
	private static interface IMsg{
		public void flush();
	}
	
	private static class ClientMsg implements IMsg{
		
		private Long roleId;
		private Short command;
		private Object result;
		
		public ClientMsg(Long roleId, Short command,Object result) {
			this.roleId = roleId;
			this.command = command;
			this.result = result;
		}
		
		public void flush(){
			PublicMsgSender.send2One(roleId, command, result);
		}
		
	}
	
	private static class StageMsg implements IMsg{
		
		private Long roleId;
		private Short command;
		private Object result;
		
		public StageMsg(Long roleId, Short command,Object result) {
			this.roleId = roleId;
			this.command = command;
			this.result = result;
		}
		
		public void flush(){
			PublicMsgSender.send2Stage(roleId, command, result);
		}
		
	}
	
	private static class GuildChatMsg implements IMsg{
		
		private Long roleId;
		private Short command;
		private Object result;
		
		public GuildChatMsg(Long roleId, Short command,Object result) {
			this.roleId = roleId;
			this.command = command;
			this.result = result;
		}
		
		public void flush(){
			PublicMsgSender.send2One(roleId,command, result);
		}
		
	}
	
	private static class MultiClientMsg implements IMsg{
		
		private Long[] roleIds;
		private Short command;
		private Object result;
		
		public MultiClientMsg(Long[] roleIds, Short command,Object result) {
			this.roleIds = roleIds;
			this.command = command;
			this.result = result;
		}
		
		public void flush(){
			PublicMsgSender.send2Many(roleIds, command, result);
		}
		
	}
	
	private static class StageControllMsg implements IMsg{
		
		private Long roleId;
		private Short command;
		private Object result;
		
		public StageControllMsg(Long roleId, Short command,Object result) {
			this.roleId = roleId;
			this.command = command;
			this.result = result;
		}
		
		public void flush(){
			PublicMsgSender.send2GsStageControl(roleId,command, result);
		}
		
	}
	
	private static class BroadcastMsg implements IMsg{
		
		private Short command;
		private Object result;
		
		public BroadcastMsg(Short command,Object result) {
			this.command = command;
			this.result = result;
		}

		@Override
		public void flush() {
			PublicMsgSender.send2All(command, result);
		}
		
		
		
	}
	
}
