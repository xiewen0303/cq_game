package com.junyou.bus.serverinfo.export;

import java.sql.Timestamp;

import com.junyou.bus.serverinfo.entity.ServerInfo;
import com.junyou.bus.serverinfo.service.ServerInfoService;
import com.junyou.context.GameServerContext;
import com.junyou.log.ChuanQiLog;
import com.junyou.share.StringAppContextShare;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 服务器信息管理类 
 * @author DaoZheng Yuan
 * 2014年11月22日 下午6:10:53
 */
public class ServerInfoServiceManager{
	
	private ServerInfoService serverInfoService;
	
	private ServerInfo serverInfo;
	
	private static final ServerInfoServiceManager INSTANCE = new ServerInfoServiceManager();
	
	/**
	 * 服务器开服时间
	 */
	private long openServerTime; 
	/**
	 * 服务器启动时间戳
	 */
	private long bootTime;
	
	private ServerInfoServiceManager(){
		bootTime = GameSystemTime.getSystemMillTime();
	}
	
	public static ServerInfoServiceManager getInstance() {
		return INSTANCE;
	}
	
	/**
	 * 获取服务器启动时间戳
	 * @return 
	 */
	public long getServerBootTime(){
		return bootTime;
	}
	
	public void setOpenServerTime(long openServerTime){
		this.openServerTime = openServerTime;
	}
	
//	public long getOpenServerTime(){
//		return openServerTime;
//	}
	
	private ServerInfoService getServerInfoService(){
		if(serverInfoService == null){
			serverInfoService = StringAppContextShare.getSpringContext().getBean(ServerInfoService.class);
		}
		
		return serverInfoService;
	}
	
	
	public ServerInfo init(){
		if(KuafuConfigPropUtil.isKuafuServer()){
			return null;
		}
		
		
		ServerInfo serInfo = getServerInfoService().loadServerInfo();
		if(serInfo == null){
			serInfo = new ServerInfo();
			serInfo.setId( Integer.parseInt(ChuanQiConfigUtil.getServerId()));
			serInfo.setStartTime(new Timestamp(openServerTime == 0l ? System.currentTimeMillis() : openServerTime));
			serInfo.setSendCmEmail(0);
			serInfo.setVersion(ChuanQiConfigUtil.getGsVersion());
			
			String roleNamePrefix = GameServerContext.getServerInfoConfig().getRoleNamePrefix();
			serInfo.setPrefixId(roleNamePrefix);
			serInfo.setPlatformId(GameServerContext.getGameAppConfig().getPlatformId());
			serInfo.setPtServerId(GameServerContext.getGameAppConfig().getPlatformServerId());
			serInfo.setStopTimes(1);
			serverInfoService.insertServerInfo(serInfo);
			
			this.serverInfo = serInfo;
		}else{
			serverInfo = serInfo;
			String roleNamePrefix = GameServerContext.getServerInfoConfig().getRoleNamePrefix();
			if( roleNamePrefix != null && !roleNamePrefix.equals(serverInfo.getPrefixId()) ){
				serverInfo.setPrefixId(roleNamePrefix);
			}
			if(serverInfo.getVersion() == null || "".equals(serverInfo.getVersion())){
				serverInfo.setVersion(ChuanQiConfigUtil.getGsVersion());
			}
			
			String platformId = GameServerContext.getGameAppConfig().getPlatformId();
			//平台id
			if(serverInfo.getPlatformId() == null || !platformId.equals(serverInfo.getPlatformId())){
				serverInfo.setPlatformId(platformId);
			}
			
			String ptServerId = GameServerContext.getGameAppConfig().getPlatformServerId();
			//平台服务器id
			if(serverInfo.getPlatformId() == null || !ptServerId.equals(serverInfo.getPtServerId())){
				serverInfo.setPtServerId(ptServerId);
			}
			
			//每次启动,停机次数加1
			serverInfo.setStopTimes(serverInfo.getStopTimes() + 1);
			
			//同步开服时间
			if(serInfo.getStartTime().getTime() != openServerTime && openServerTime != 0l){
				serInfo.setStartTime(new Timestamp(openServerTime));
			}
			serverInfoService.updateServerInfo(serverInfo);
		}
		
		return serverInfo;
	}
	
	/**
	 * 更新服务器的停机次数数据
	 * @return true:数据成功更新   false:没有数据需要更新
	 */
	public boolean updateStopTimes(){
		if(serverInfo.isUpdate()){
			ChuanQiLog._60MinuteLog("服务器的停机次数,因为ServerIdType:"+serverInfo.getUpdateStage()+" 类型超过2的22次方人为增加一次停机次数.");
			
			getServerInfoService().updateServerInfo(serverInfo);
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 获取服务器开服时间
	 * @return
	 */
	public Timestamp getServerStartTime(){
		return serverInfo.getStartTime();
	}
	/**
	 * 获取服务器合服时间
	 * @return
	 */
	public Timestamp getServerHefuTime(){
		return serverInfo.getHefuTime();
	}
	/**
	 * 获取服务器合服次数
	 * @return
	 */
	public int getServerHefuTimes(){
		return serverInfo.getHefuTimes();
	}
	
	
	/**
	 * 获取开服天数 当前日期和开服日期是同一天，开服天数返回值为 1
	 * @return
	 */
	public int getKaifuDays() {
		return DatetimeUtil.twoDaysDiffence(GameSystemTime.getSystemMillTime(), serverInfo.getStartTime().getTime()) + 1;
	}
	
	/**
	 * 获取合服天数 当前日期和合服日期是同一天，合服天数返回值为 1
	 * @return
	 */
	public int getHefuDays() {
		if(serverInfo.getHefuTime() == null){
			return 0;
		}
		
		return DatetimeUtil.twoDaysDiffence(GameSystemTime.getSystemMillTime(), serverInfo.getHefuTime().getTime()) + 1;
	}
}
