/**
 * 
 */
package com.junyou.stage.tunnel;

import java.util.ArrayList;
import java.util.List;




/**
 * @description 带缓存消息输出器（线程安全）
 * @author ShiJie Chi
 * @created 2011-12-6下午1:45:54
 */
public class BufferedMsgWriter implements IMsgWriter {
	
	private static ThreadLocal<IMsgWriter> localMsgWriters = new ThreadLocal<IMsgWriter>(){
		@Override
		protected IMsgWriter initialValue() {
			return new BufferedMsgWriter();
		}
		
	};
	
	private List<IMsg> msgs = new ArrayList<IMsg>();
	
	/**
	 * 增加发送消息
	 * @param busMsg
	 */
	private void addMsg(IMsg busMsg){
		msgs.add(busMsg);
	}
	
	/**
	 * 增加发往场景的消息
	 */
	public void writeMsg(Long roleId, Short command,Object result){
		addMsg(new ClientMsg(roleId, command, result));
	}
	
	/**
	 * 增加发往客户端的多发消息
	 */
	public void writeMsg(Long[] roleIds, Short command,Object result){
		
		addMsg(new MultiClientMsg(roleIds, command, result));
	}
	
	@Override
	public void writePublicMsg(Long roleId, Short command, Object result) {
		addMsg(new PublicMsg(roleId,command,result));
	}

	/**
	 * @param
	 */
	@Override
	public void flush() {
		
		for(IMsg msg : msgs){
			msg.flush();
		}
		
		msgs.clear();
	}
	
	/**
	 * 获取实例
	 * @param
	 */
	public static IMsgWriter getInstance() {
		return localMsgWriters.get();
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
			StageMsgSender.send2One(roleId, command, result);
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
			StageMsgSender.send2Many(roleIds, command, result);
		}
		
	}

	private static class PublicMsg implements IMsg{
		
		private Long roleId;
		private short command;
		private Object result;
		
		public PublicMsg(Long roleId, short command,Object result) {
			this.roleId = roleId;
			this.command = command;
			this.result = result;
		}
		
		public void flush(){
			StageMsgSender.send2Public(roleId, command, result);
		}
		
	}
}
