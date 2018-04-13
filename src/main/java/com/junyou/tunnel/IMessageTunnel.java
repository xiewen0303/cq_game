package com.junyou.tunnel;

/**
 * @description 组件消息通道
 * @author hehj
 * 2011-11-24 下午3:08:26
 */
public interface IMessageTunnel {

	/**
	 * 发送给单个客户端
	 * @param userid
	 * @param command
	 * @param result
	 */
	public void send2One(String userid, String command,Object result);
	
	/**
	 * 发送给所有客户端
	 * @param command
	 * @param data
	 */
	public void send2All(String command, Object data);
	
	/**
	 * 发送给多个客户端
	 * @param userRoleIds
	 * @param command
	 * @param data
	 */
	public void send2Many(Object[] userRoleIds, String command,Object data);
	
	/**
	 * 发送给业务处理组件的单个目标
	 * @param userid
	 * @param command
	 * @param result
	 */
	public void send2Bus(String userid, String command,Object result);

	/**
	 * 发送给场景组件的单个目标
	 * @param userid
	 * @param command
	 * @param result
	 */
	public void send2Stage(String userid, String command,Object result);

	/**
	 * 发送给场景控制组件的单个目标
	 * @param userid
	 * @param command
	 * @param result
	 */
	public void send2StageControl(String userid, String command,Object result);

}
