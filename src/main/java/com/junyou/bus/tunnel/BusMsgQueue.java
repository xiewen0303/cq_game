package com.junyou.bus.tunnel;

import java.util.ArrayList;
import java.util.List;



/**
 * @description 公共消息发送队列
 * @author ShiJie Chi
 * @date 2012-4-19 下午4:08:37 
 */
public class BusMsgQueue {
	
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
	 * 输出当前所有消息并删除
	 */
	public void flushAndRemove(){
		if(msgs.size() > 0){
			for(IMsg msg : msgs){
				msg.flush();
			}
		}
		
		msgs.clear();
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
	public void addMsg(String[] roleIds, Short command,Object result){
		
		addMsg(new MultiClientMsg(roleIds, command, result));
	}
	
	/**
	 * 增加发往场景的消息
	 */
	public void addStageMsg(Long roleId, Short command,Object result){
		addMsg(new StageMsg(roleId, command, result));
	}
	
	public void addBusMsg(Long roleId, Short command,Object result){
		addMsg(new BusMsg(roleId, command, result));
	}
	
	/**
	 * 增加广播消息
	 */
	public void addBroadcastMsg(Short command,Object result){
		addMsg(new BroadcastMsg(command, result));
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
			BusMsgSender.send2One(roleId, command, result);
		}
		
	}
	
	private static class BusMsg implements IMsg{
		
		private Long roleId;
		private Short command;
		private Object result;
		
		public BusMsg(Long roleId, Short command,Object result) {
			this.roleId = roleId;
			this.command = command;
			this.result = result;
		}
		
		public void flush(){
			BusMsgSender.send2BusInner(roleId, command, result);
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
			BusMsgSender.send2Stage(roleId, command, result);
		}
		
	}	
	
	private static class MultiClientMsg implements IMsg{
		
		private String[] roleIds;
		private Short command;
		private Object result;
		
		public MultiClientMsg(String[] roleIds, Short command,Object result) {
			this.roleIds = roleIds;
			this.command = command;
			this.result = result;
		}
		
		public void flush(){
			BusMsgSender.send2Many(roleIds, command, result);
		}
		
	}
	
	private static class BroadcastMsg implements IMsg{
		
		private Short command;
		private Object result;
		
		public BroadcastMsg(Short command,Object result) {
			this.command = command;
			this.result = result;
		}
		
		public void flush(){
			BusMsgSender.send2All(command, result);
		}
		
	}
	
}
