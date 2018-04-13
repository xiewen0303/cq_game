package com.junyou.io;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.ExecutorService;

import com.junyou.io.global.PingVo;
import com.junyou.log.ChuanQiLog;
import com.junyou.session.SessionConstants;
import com.junyou.utils.codec.AmfUtil;

/**
 * 自定义session
 * @author ydz
 * @date 2015-7-13 下午3:33:04
 */
public class GameSession {

	/*********session基本信息区************/
	private String id;
	//session类型 0:正常客户服务器session  ,1:源服和跨服之间session
	private byte sessionType;
	private Channel channel;
	
	/**********玩家信息区***********/
	private String serverId;
	private int configId;
	private String userId;
	private long roleId = 0L;
	private String roleName;
	private int level = 0;
	private Long createTime;
	
	/**********状态区***********/
	private String ip;
	//是否沉迷 true:是 false:非
	private boolean chenmi;
	//是否是微端 true:是 false:非
	private boolean weiduan;
	//是否被挤出 true:是，false:非
	private boolean jichu;
	
	//是否在场景内 true:在  false:不在
	private boolean inStage;
	
	private PingVo pingVo;
	
	private ExecutorService executorService;
	
	
	public GameSession(byte type,Channel channel, String sessionId) {
		this.sessionType = type;
		this.channel = channel;
		this.id = sessionId;
	}


	public boolean isWeiduan() {
		return weiduan;
	}




	public void setWeiduan(boolean weiduan) {
		this.weiduan = weiduan;
	}




	public PingVo getPingVo() {
		return pingVo;
	}



	public void setPingVo(PingVo pingVo) {
		this.pingVo = pingVo;
	}



	/**
	 * 是否在场景内 
	 * @return true:在  false:不在
	 */
	public boolean isInStage() {
		return inStage;
	}


	public void setInStage(boolean inStage) {
		this.inStage = inStage;
	}



	/**
	 * 是否是跨服服务session
	 * @return true:跨服session,false:正常session
	 */
	public boolean isKuaFuSession(){
		if(sessionType == SessionConstants.NOMAL_SESSION_TYPE){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 是否是正常用户session
	 * @return true:正常
	 */
	public boolean isNomalSession(){
		if(sessionType == SessionConstants.NOMAL_SESSION_TYPE){
			return true;
		}else{
			return false;
		}
	}
	
	

	public boolean isChenmi() {
		return chenmi;
	}




	public void setChenmi(boolean chenmi) {
		this.chenmi = chenmi;
	}




	public String getIp() {
		return ip;
	}




	public void setIp(String ip) {
		this.ip = ip;
	}




	public String getServerId() {
		return serverId;
	}




	public void setServerId(String serverId) {
		this.serverId = serverId;
	}



	public int getConfigId() {
		return configId;
	}


	public void setConfigId(int configId) {
		this.configId = configId;
	}


	public Long getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public long getRoleId() {
		return roleId;
	}


	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public ExecutorService getExecutorService() {
		return executorService;
	}


	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}


	public String getId() {
		return id;
	}


	public Channel getChannel() {
		return channel;
	}


	/**
	 * 是否是被挤出的状态session
	 * @return true:是，false:不是
	 */
	public boolean isJichu() {
		return jichu;
	}


	public void setJichu(boolean jichu) {
		this.jichu = jichu;
	}
	
	public void sendMsg(final byte[] content) {
		
		if(executorService==null || sessionType == SessionConstants.KUAFU_SESSION_TYPE){
			if(channel.isActive()){
				channel.writeAndFlush(content);
			}
		}else{
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					if(channel.isActive()){
						channel.writeAndFlush(content);
					}
				}
			});
		}
	}

	public void sendMsg(Object[] msg) {
		byte[] content = AmfUtil.convertMsg2Bytes(msg);
		this.sendMsg(content);
	}
	
	/**
	 * 数据无需再次序列化，可直接发送(暂时给跨服用)
	 * @param msg
	 */
	public ChannelFuture sendDMsg(Object msg){
		if(!channel.isWritable()){
			if(MsgCountRecord.incrementAndGet()){
				ChuanQiLog.memInfo("gamesession isWritable:false,serverId:"+getServerId()+",name:"+getRoleName());
			}
		}
		
		if(channel.isActive()){
			return channel.writeAndFlush(msg);
		}else{
			return null;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o){
			return true;
		}
		
		if (!(o instanceof GameSession)){
			return false;
		}

		GameSession session = (GameSession) o;

		return id == session.id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
}
