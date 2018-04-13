package com.junyou.stop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.junyou.cmd.ClientCmdType;
import com.junyou.io.GameSession;
import com.junyou.io.global.GameSessionManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.codec.AmfUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.md5.Md5Utils;

/**
 * 关机处理
 */
public class ChuanQiStopHelper {
	
	private static final Logger logger = LogManager.getLogger(ChuanQiStopHelper.class);

	public static final String KEY = "QIANWANBUYAOLUANLAIO";
	
	
	public static String getStopSign(String from){
		return Md5Utils.md5To32(from + KEY);
	}
	
	public static boolean checkStopSign(String from,String sign){
		
		String md5Sign = getStopSign(from);
		if(md5Sign.equals(sign)){
			return true;
		}
		
		return true;
	}
	
	/**
	 * http停机
	 * @param stopSecond
	 */
	public static void httpStop(int stopSecond){
		//停止前通知客户端
		stopBeforeNotify(stopSecond);
		//执行停机操作	
		stop(" http stop ");
	}
	
	public static void stop(String stopReason){
		//正常退出
		System.exit(0);
		
		logger.error(" server stoped reason[{}]",stopReason);
	}
	
	
//	public static void stop(String stopReason){
//		
//		try{
//			/**
//			 * 通信模块关闭
//			 */
//			//关闭主端口消息入口
//			MsgFilterManage.changeCloseMsg();
//			
//			// 通信组件关闭
//			NetListener.stop();
//			// http组件关闭
//			GameHttpService.getInstence().stop();
//			//从redis里注销掉
//			if(KuafuConfigPropUtil.isKuafuServer()){
//				KuafuServerManager.quit();
//			}
//			
//			// redis关闭
//			if(GameServerContext.getRedis()!=null){
//				try{
//					GameServerContext.getRedis().destory();
//					ChuanQiLog.stopLog("断开与redis的连接!");
//				}catch (Exception e) {
//					ChuanQiLog.error("断开redis的连接失败",e);
//				}
//			}
//			
//			/**
//			 * 数据模块关闭
//			 */
//			
//			// 必要业务数据保存
//			// public 节点
//			for(Long roleid : GameServerContext.getPublicRoleStateService().getAllOnlineRoleids() ){
//				try{
//					GameServerContext.getNodeControlService().nodeExitOnServerClose(roleid);
//				}catch (Exception e) {
//					ChuanQiLog.error("", e);
//				}
//			}
//			// gs节点
//			for(Long roleid : GameServerContext.getRoleStateService().getAllOnlineRoleids()){
//				try{
//					GameServerContext.getIoService().roleOutOnServerClose(roleid);
//				}catch (Exception e) {
//					ChuanQiLog.error("", e);
//				}
//			}
//			
//			/**停服业务数据处理*/
//			GameBusStartStopHandle.stopHandle();
//			
//		}catch (Exception e) {
//			ChuanQiLog.error("",e);
//		}finally {
//			// 数据回写中心关闭
//			GameServerContext.getAsyncWriteManager().close();
//		}
//		
//		// 虚拟机关闭
//		System.exit(0);
//		
//		logger.error(" server stoped reason[{}]",stopReason);
//	}
	
	
	/**
	 * 停机前通知业务
	 * @param stopSecond
	 */
	private static void stopBeforeNotify(int stopSecond){
		if(stopSecond <= 0){
			return;
		}
		
		try {
			/*
			 * 通知客户端开始停机倒计时
			 */
			Long stopTime = GameSystemTime.getSystemMillTime() + stopSecond * 1000;
			byte[] bytes = AmfUtil.convertMsg2Bytes(ClientCmdType.STOP_SERVER,stopTime);
			
			for(GameSession session3 : GameSessionManager.getInstance().getRoleListSession()){
				session3.sendMsg(bytes);
			}
			
			//等待2秒后再执行停止游戏业务
			Thread.sleep(stopSecond * 1000 + 2000);
			
			
			ChuanQiLog.error("http stop stopTime:"+DatetimeUtil.formatTime(stopTime, "yyyy-MM-dd HH:mm:ss")+",curTime:"+DatetimeUtil.formatTime(GameSystemTime.getSystemMillTime(), "yyyy-MM-dd HH:mm:ss"));
		} catch (InterruptedException e) {
			ChuanQiLog.error("http ChuanQiStop error!",e);
		}
	}
}
