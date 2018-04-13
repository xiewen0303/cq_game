package com.junyou.login.io.easyaction;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.jinfeng.export.JinFengExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.service.export.TencentYaoQingExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.UserRoleOutputWrapper;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.rolestage.entity.RoleStage;
import com.junyou.bus.rolestage.export.RoleStageExportService;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.event.RoleInOrOutLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.io.GameSession;
import com.junyou.io.global.GameSessionManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.module.GameModType;
import com.junyou.public_.nodecontrol.helper.LoginHelper;
import com.junyou.public_.nodecontrol.service.NodeControlService;
import com.junyou.public_.tunnel.PublicMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.codec.AmfUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.pool.executor.Message;
import com.kernel.spring.container.DataContainer;

/**
 * @description 处理客户端的接入与退出
 * @author hehj
 * 2011-11-9 下午12:48:51
 */
@Controller
@EasyWorker(moduleName = GameModType.LOGIN, groupName = EasyGroup.LOGING)
public class IOAction {

	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private NodeControlService nodeControlService;
	@Autowired
	private JinFengExportService jinFengExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private TencentYaoQingExportService tencentYaoQingExportService;
	@Autowired
	private RoleStageExportService roleStageExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	/**
	 * [0:String(平台用户id),
	 * 1:String(服务器id),
	 * 2:Booblean(limited该用户是否沉迷了),
	 * 3:String(md5验证信息:时间戳),
	 * 4:Boolean(是否使用登录器登录true为用登录器登录，false为没有用登录器登录的),
	 * 5:String(服务器需要的参数)]
	 * @param msg
	 */
	@EasyMapping(mapping = ClientCmdType.IN)
	public void in(Message msg){

		Object[] data = msg.getData();
		
		String userId = (String)data[0];
		String serverId = (String)data[1];
		String yzStr = CovertObjectUtil.obj2StrOrNull(data[3]);
		String[] yzData = yzStr == null ? null : yzStr.split(":");
		if(yzData!=null && yzData.length!=2){
			PublicMsgSender.send2OneBySessionId(msg.getSessionid(), userId, ClientCmdType.IN, AppErrorCode.CHECK_FAILED);
			return;
		}
		String verify = yzData == null ? "" : yzData[0];
		String timestamp = yzData == null ? "" : yzData[1];
		String otherParams = null;
		
		//判断是否有第4位元素(登入器登陆,不是一定有此元素),默认为不是登入器登陆。
		Boolean usedlq = false;
		if(data.length >=4 ){
			usedlq = (Boolean) data[4];
		}
		String gsVersion = ChuanQiConfigUtil.getGsVersion();
		String ip = (String) msg.getMsgSource()[6];
		//5:String(服务器需要的参数)
		if(data.length >= 5){
			otherParams = (String) data[5];
			if(otherParams != null && !"".equals(otherParams)){
				if(PlatformConstants.isQQ()){
					String[] params=otherParams.split(PlatformPublicConfigConstants.FLAG1);
					String pfkey=params[0];
					String pf=params[1];	
					String openkey=params[2];
					//保存QQ充值调用接口所需数据
					Map<String , String> map = new HashMap<String, String>();
					map.put("pfkey", pfkey);
					map.put("pfyuan", pf);
					map.put("openkey", openkey);
					if(params.length >= 4){
						String appCustom = params[3];
						map.put("appCustom", appCustom);
					}
					if(params.length >= 5){
						String sId=params[4];
						map.put("sId", sId);
					}
					if(params.length >= 6){
						map.put("pf", params[5]);
					}
					if(params.length >= 7){
						map.put("x5sig", params[6]);
					}
					if(params.length >= 8){
						map.put("via", params[7]);
					}
					if(params.length >= 9){
						String iopenId =  params[8];
						if(iopenId != null && !"".equals(iopenId)){
							tencentYaoQingExportService.insertTencentYaoQing(userId, iopenId);
						}
					}
					dataContainer.putData(QqConstants.COMPONENET_NAME,userId, map);
				}else{
					//平台参数暂时存在这里，需要用的时候解析出来
					dataContainer.putData(GameConstants.PLATFORM_PARAM_COMPONENT_NAME, userId, otherParams);
				}
			}
		}
		
		// 校验接入信息是否合法
		boolean isLegal = LoginHelper.checkSign(userId, timestamp, verify,ip);
		if( !isLegal ){
			PublicMsgSender.send2OneBySessionId(msg.getSessionid(), userId, ClientCmdType.IN, AppErrorCode.CHECK_FAILED);
			return;
		}
		boolean isTime = LoginHelper.checkTime(timestamp,ip);
		if( !isTime ){
			//ChuanQiLog.error("login error TODO---------------checkTime:"+timestamp+"\t nowTime:"+System.currentTimeMillis());
			PublicMsgSender.send2OneBySessionId(msg.getSessionid(), userId, ClientCmdType.IN, AppErrorCode.TIME_CHECK_FAILED);
			return;
		}
		
		//验证是否被封IP
		if(jinFengExportService.isFengIp(ip)){
			GameSession session = GameSessionManager.getInstance().getSession4UserId(userId ,serverId);
			byte[] clientBytes = AmfUtil.convertMsg2Bytes(ClientCmdType.IN, new Object[]{0,AppErrorCode.IS_FENG_IP,new Object[]{ip}});
			session.sendMsg(clientBytes);
			session.setJichu(true);
			session.getChannel().close();
			return;
		}
		
		//客户端不合法
		if(GameServerContext.getGameAppConfig().isDebug()){
			if(!ip.startsWith(LoginHelper.CHECK_IP)){
				PublicMsgSender.send2OneBySessionId(msg.getSessionid(), userId, ClientCmdType.IN, new Object[]{0, AppErrorCode.CLENT_ERROR,new Object[]{ip}});
			}
		}
		
		Timestamp serverTime = ServerInfoServiceManager.getInstance().getServerStartTime();
		Long serverTimeMillSecond = 0L;
		if(serverTime != null){
			serverTimeMillSecond = serverTime.getTime();
		}
		
		Timestamp heHuTime = ServerInfoServiceManager.getInstance().getServerHefuTime();
		Long serverHeHuMillSecond = null;
		if(heHuTime != null){
			serverHeHuMillSecond = heHuTime.getTime();
		}
		
		//取得时间的偏移量(单位：分钟)
		int zoneOffMinute = TimeZone.getDefault().getRawOffset() / 60000;
		
		//开服时间[0:服务器开服时间,1:服务器当前时间,2:服务器版本,3:服务器合服时间,4:服务器时区偏移量(单位：分钟)]
		Object[] serverData = new Object[]{serverTimeMillSecond,GameSystemTime.getSystemMillTime(),gsVersion,serverHeHuMillSecond, zoneOffMinute, ip};
		
		//玩家信息
		Object[] outputData = new Object[]{1,serverData,null,null,ip};
		UserRole role = roleExportService.getRoleFromDb(userId, serverId);
		//标记是否发送11
		boolean flagSend11 = false;
		
		if(role != null){
			flagSend11 = true;
			Long roleId = role.getId();
			GameSession session = GameSessionManager.getInstance().getSession4UserId(userId ,serverId);
			//验证是否封号
			if(jinFengExportService.isFengHao(roleId)){
				byte[] clientBytes = AmfUtil.convertMsg2Bytes(ClientCmdType.IN, AppErrorCode.IS_FENG);
				session.sendMsg(clientBytes);
				session.setJichu(true);
				session.getChannel().close();
				return;
			}
			outputData[2] = UserRoleOutputWrapper.chooseRoleVO(role);
			long chengmiTime = getChenmiAddOnline(role);
			outputData[3] = chengmiTime;
			
			//---------------添加会话到会话管理--绑定角色基本信息到session-------------
			GameSessionManager.getInstance().addSession4Role(session, roleId);
			try {
				session.setRoleName(role.getName());
				session.setConfigId(role.getConfigId());
				session.setLevel(role.getLevel());
				session.setCreateTime(role.getCreateTime().getTime());
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}else{
			//游戏进度到达创角界面
			int type = GameConstants.GAME_JINDU_TYPE_ROLE;
			if((userId.hashCode() & 1) == 1){
				type = GameConstants.GAME_JINDU_TYPE_ROLE_OTHER;
			}
			PublicMsgSender.send2PublicInner(InnerCmdType.GAME_JINDU, GameConstants.DEFAULT_ROLE_ID, type);
			try {
				if(PlatformConstants.isQQ()){
					BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.TENCENT_LUOPANLM_LOGIN_CHUAN, new Object[]{userId,ip});
				}
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			}
		}
		
		PublicMsgSender.send2OneBySessionId(msg.getSessionid(), userId, ClientCmdType.IN, outputData);
		
		if(flagSend11){
		   RoleStage roleStage = roleStageExportService.getRoleStageFromDb(role.getId());
		   if(roleStage != null){
			   PublicMsgSender.send2OneBySessionId(msg.getSessionid(), userId, ClientCmdType.ENTER_STAGE_OK, new Object[]{roleStage.getMapId(),roleStage.getMapX(),roleStage.getMapY()});	
		   }else{
			   //设置出生点
			   DiTuConfig config = diTuConfigExportService.loadBirthDiTu();
			   int[] birthXy = config.getRandomBirth();
			   ChuanQiLog.debug("RoleStage data is not exits, send init map!");
			   PublicMsgSender.send2OneBySessionId(msg.getSessionid(), userId, ClientCmdType.ENTER_STAGE_OK, new Object[]{config.getId(),birthXy[0],birthXy[1]});
		   }
		}
		//记录微端登录状态
		int sort = LogPrintHandle.ROLE_IO_ACCOUNT;
		if(usedlq){
			if(role != null){
				nodeControlService.addRoleWeiDuan(role.getId());
			}else{
				//标记微端状态
				GameSession session = GameSessionManager.getInstance().getSession4UserId(userId,serverId);
				session.setWeiduan(true);
			}
			sort = LogPrintHandle.ROLE_IO_ACCOUNT_DLQ;
		}
		
		//打印账号上线日志
		GamePublishEvent.publishEvent(new RoleInOrOutLogEvent(LogPrintHandle.ROLE_IN_OR_OUT, sort, userId,null, null, ip,null,null));
	}
	
	
	
	/**
	 * 登陆游戏
	 * @param msg
	 */
	@EasyMapping(mapping = ClientCmdType.CLIENT_APPLY_ENTER_GAME)
	public void login(Message msg){
		Long userRoleId = msg.getRoleId();
		
		PublicMsgSender.send2PublicInner(InnerCmdType.NODE_INIT_IN, userRoleId, (String) msg.getMsgSource()[6]);
	}
	
//	/**
//	 * 登陆进入场景
//	 * @param msg
//	 */
//	@EasyMapping(mapping = ClientCmdType.ENTER_STAGE_OK)
//	public void loginEnterStage(Message msg){
//		Long userRoleId = msg.getRoleId();
//		BusMsgSender.send2BusInit(userRoleId, InnerCmdType.BUS_ENTER_INIT_IN, (String) msg.getMsgSource()[6]);
//	}
	
	
	
	private long getChenmiAddOnline(UserRole userRole){
		if(!userRole.isFangchenmi()){
			return 0l;
		}
		
		long sysTime = GameSystemTime.getSystemMillTime(); 
		long offlineTime =CovertObjectUtil.obj2long(userRole.getOfflineTime());
		if(offlineTime != 0l){
			long addOfflineTime = sysTime - userRole.getOfflineTime();
			int offline = (int) (userRole.getChenmiAddOffline() + addOfflineTime);
			
			// 下线时间超过5小时，清除沉迷在线和离线时间
			if (offline >= GameConstants.CHENMI_TIME) {
				return 0l; 
			}
		}

		return  userRole.getChenmiAddOnline();
	}
	
	@EasyMapping(mapping = ClientCmdType.CREATE_ROLE)
	public void createRole(Message inMsg){
		
		Object[] data = inMsg.getData();
		
		//[0:String(账号),1:String(服务器id),2:Array[0:String(角色名字),1:int(角色配置ID)]]
		String userId = (String)data[0];
		String serverId = (String)data[1];
		Object[] role = (Object[])data[2];
		String name = (String)role[0];
		Integer configId = (Integer)role[1];
		//2:boolean(true:自动创号玩家，false:正常玩家) 默认是false:正常玩家
		Boolean isAutoCreate = false;
		if(role.length == 3){
			isAutoCreate = (Boolean)role[2];
		}
		
		GameSession session = GameSessionManager.getInstance().getSession4UserId(userId,serverId);
		boolean isChenmi = session.isChenmi();
		String ip = session.getIp();
		
		//场景数据
		Map<Short,Object> otherMessage = new HashMap<>();
				
		Object[] result = roleExportService.createRoleFromDb(userId, name, configId, isChenmi, serverId, ip,isAutoCreate,otherMessage);
		PublicMsgSender.send2OneBySessionId(inMsg.getSessionid(),userId, ClientCmdType.CREATE_ROLE, result);
		if((int) result[0] == 1 ){
			Object[] finalRes = (Object[])result[1];
			Long roleId = (Long) finalRes[1];
			//---------------添加会话到会话管理
			GameSessionManager.getInstance().addSession4Role(session, roleId);

			//如果是微端登陆则标记微端状态
			if(session.isWeiduan()){
				nodeControlService.addRoleWeiDuan(roleId);
			}
		}
		
		//推送
		for (Entry<Short, Object> entry : otherMessage.entrySet()) {
			PublicMsgSender.send2OneBySessionId(inMsg.getSessionid(),userId, entry.getKey(), entry.getValue());
		}
	}
	
}
