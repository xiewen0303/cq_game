package com.junyou.http.processor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.chenghao.service.export.ChenghaoExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.jinfeng.export.JinFengExportService;
import com.junyou.bus.kuafuarena1v1.service.export.KuafuArena1v1SourceExportService;
import com.junyou.bus.platform._37.export._37ExportService;
import com.junyou.bus.platform.baidu.export.BaiDuRenWuJiShiExportService;
import com.junyou.bus.platform.common.service.PtCommonService;
import com.junyou.bus.platform.export.PlatformExportService;
import com.junyou.bus.platform.jy.service.JunYouJobManager;
import com.junyou.bus.platform.qq.service.export.QqExportService;
import com.junyou.bus.platform.qq.service.export.RenWuJiShiExportService;
import com.junyou.bus.recharge.export.RechargeExportService;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.bus.serverinfo.service.ServerInfoService;
import com.junyou.bus.xiaofei.export.RefabuXiaoFeiExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.schedule.RefreshableConfigureManager;
import com.junyou.configure.schedule.RefreshableNoticeConfigureManager;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.HttpErrorCode;
import com.junyou.event.LoginGateLogEvent;
import com.junyou.event.acitvity.GxRfbChangeEvent;
import com.junyou.event.acitvity.HandleRfbChangeEvent;
import com.junyou.event.acitvity.ZhiRfbDelEvent;
import com.junyou.event.acitvity.ZhuRfbChangeEvent;
import com.junyou.event.acitvity.ZhuRfbDelEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.http.key.HttpKeyType;
import com.junyou.http.key.KeyEnum;
import com.junyou.http.msg.CallBack_Msg;
import com.junyou.http.msg.HttpCallBackMsg;
import com.junyou.http.msg.HttpStopServer_Msg;
import com.junyou.http.msg.ModelFilter_Msg;
import com.junyou.http.msg.RoleData_Msg;
import com.junyou.http.msg.ServerData_Msg;
import com.junyou.http.msg.YunWeiServerData_Msg;
import com.junyou.io.export.ChatExportService;
import com.junyou.io.global.GsCountChecker;
import com.junyou.io.swap.IoMsgSender;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafu.manager.KuafuServerInfoManager;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.kuafumatch.util.KuafuMatchServerUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.rank.export.RankExportService;
import com.junyou.public_.service.MsgFilterService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.session.SessionConstants;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.aoi.AOI;
import com.junyou.stage.model.core.stage.aoi.AbsAoiStage;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stop.ChuanQiStopHelper;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.common.ConfigureUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.cache.redis.Redis;
 
/**
 * 方法名在调用时大小写不敏感,请在写业务时不要用同样的名字
 */
@Service
public class GameHttpProcessor {

	/**
	 * http服务的所有对外服务方法,全部要通过MD5-16方式加密
	 * 关于内部keytype分三种(keytype = 1,2,3,9)
	 * 1.请求参数内不带keytype参数,为普通http对外服务
	 * 
	 * 2.充值类型 对外服务(例如  充值元宝等) 加注解@HttpKeyType(keyType = KeyEnum.CONFIDENTIAL)
	 * 
	 * 3.后台安全类型 （供后台发放游戏资源接口使用:充值，邮件）加注解@HttpKeyType(keyType = KeyEnum.GMTOOLS_SAFE)
	 * 
	 * 4.停机类型对外服务(安全为绝密)		加注解@HttpKeyType(keyType = KeyEnum.SECRET)
	 * 
	 */
	
	
	@Autowired
	private ServerInfoService serverInfoService;
	@Autowired
	private MsgFilterService msgFilterRemote;
	@Autowired
	private RechargeExportService rechargeExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private JinFengExportService jinFengExportService;
	@Autowired
	private _37ExportService _37ExportService;
	@Autowired
	private PtCommonService ptCommonSuperVipService;
	@Autowired
	private RenWuJiShiExportService renWuJiShiExportService;
	@Autowired
	private ChenghaoExportService chenghaoExportService;
	@Autowired
	private PlatformExportService platformExportService;
	@Autowired
	private KuafuArena1v1SourceExportService kuafuArena1v1SourceExportService;
	@Autowired
	private GuildExportService guildExportService;
	@Autowired
	private BaiDuRenWuJiShiExportService baiDuRenWuJiShiExportService;
	@Autowired
	private ChatExportService chatExportService;
	@Autowired
	private QqExportService qqExportService;
	@Autowired
	private RefabuXiaoFeiExportService refabuXiaoFeiExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private RankExportService rankExportService;
	@Autowired
	private JunYouJobManager junYouJobManager;
	/**
	 * 获取服务器信息根据类型 (通用数据返回无状态数据)
	 * @param params
	 *        type 类型
	 *        1:查询服务器在线人数
	 *        2:查询服务器版本号
	 *        3:查询服务器是否是跨服服务器
	 *        4:查询服务器时间
	 *        5:查询服务器停机次数
	 *        6:查询服务器启动时的时间
	 * @return
	 */
	public ServerData_Msg getServerInfoByType(Map<String,Object> params){
		ServerData_Msg msg = new ServerData_Msg();
		
		Object typeObj = params.get("type");
		if(typeObj == null){
			//没有type参数
			msg.setType(-1);
			return msg;
		}
		int type = Integer.parseInt(typeObj.toString());
		msg.setType(type);
		
		switch (type) {
		case GameConstants.HTTP_SERVER_DATA_TYPE_1:
			//查询服务器在线人数
			msg.setResult(GameServerContext.getSessionManagerExportService().getSessionCounts()+"");
			break;
		case GameConstants.HTTP_SERVER_DATA_TYPE_2:
			//查询服务器版本号
			msg.setResult(ChuanQiConfigUtil.getGsVersion());
			break;
		case GameConstants.HTTP_SERVER_DATA_TYPE_3:
			//查询服务器是否是跨服服务器
			msg.setResult(KuafuConfigPropUtil.isKuafuServer()+"");
			break;
		case GameConstants.HTTP_SERVER_DATA_TYPE_4:
			//查询服务器时间
			long curMill = GameSystemTime.getSystemMillTime();
			msg.setResult(DatetimeUtil.formatTime(curMill, "yyyy-MM-dd HH:mm:ss")+"@"+curMill);
			break;
		case GameConstants.HTTP_SERVER_DATA_TYPE_5:
			//查询服务器停机次数
			msg.setResult(serverInfoService.loadServerInfo().getStopTimes().toString());
			break;
		case GameConstants.HTTP_SERVER_DATA_TYPE_6:
			//查询服务器启动时的时间
			String bootTimeStr = DatetimeUtil.formatTime(ServerInfoServiceManager.getInstance().getServerBootTime(), "yyyy-MM-dd HH:mm:ss");
			msg.setResult(bootTimeStr);
			break;
		case GameConstants.HTTP_SERVER_DATA_TYPE_7:
			//查询跨服状态
			msg.setResult(KuafuConfigUtil.getConnectionState());
			break;
		case GameConstants.HTTP_SERVER_DATA_TYPE_8:
			//查询游戏内存中的允许的最高在线人数
			msg.setResult(GsCountChecker.getInstance().getMaxCount()+"");
			break;
		default:
			//没定义解析type
			msg.setType(0);
			break;
		}
		
		return msg;
	}
	
	
	/**
	 * gate取消沉迷状态
	 * @param params{roleId:角色Id,}
	 * @return
	 */
	public CallBack_Msg gateChenMiRe(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		Long userRoleId = CovertObjectUtil.obj2long(params.get("userId"));
		int result = roleExportService.reChenmi(userRoleId);
		msg.setResult(result + "");
		return msg;
	}
	
	
	/**
	 * 运维需要的几个参数一次性回调给运维
	 * @param params
	 * @return
	 */
	public YunWeiServerData_Msg getYunWeiServerData(Map<String,Object> params){
		YunWeiServerData_Msg msg = new YunWeiServerData_Msg();
		//在线人数
		msg.setOnlineCount(GameServerContext.getSessionManagerExportService().getSessionCounts());
		//查询服务器版本号
		msg.setVersion(ChuanQiConfigUtil.getGsVersion());
		//查询服务器是否是跨服服务器(true:是跨服务器,false:不是 是正常的源服务器;)
		msg.setKf(KuafuConfigPropUtil.isKuafuServer());
		//查询服务器当前时间
		long curMill = GameSystemTime.getSystemMillTime();
		msg.setCurTime(DatetimeUtil.formatTime(curMill, "yyyy-MM-dd HH:mm:ss")+"@"+curMill);

		if(!KuafuConfigPropUtil.isKuafuServer()){
			//开服时间
			Timestamp kfTime = serverInfoService.loadServerInfo().getStartTime();
			msg.setKfTime(DatetimeUtil.formatTime(kfTime.getTime(), "yyyy-MM-dd HH:mm:ss"));
			//合服时间
			Timestamp hfTime = serverInfoService.loadServerInfo().getHefuTime();
			if(hfTime != null){
				msg.setHfTime(DatetimeUtil.formatTime(hfTime.getTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			//查询服务器停机次数
			msg.setStopTimes(serverInfoService.loadServerInfo().getStopTimes());
		}else{
			//开服时间
			msg.setKfTime(null);
			//查询服务器停机次数
			msg.setStopTimes(0);
		}
		//查询服务器启动时的时间
		String bootTimeStr = DatetimeUtil.formatTime(ServerInfoServiceManager.getInstance().getServerBootTime(), "yyyy-MM-dd HH:mm:ss");
		msg.setBootTime(bootTimeStr);
		//查询跨服状态(-1:未读取到跨服配置，其他:已成功连接上跨服的连接数)
		msg.setKfCount(KuafuConfigUtil.getConnectionState());
		//查询游戏内存中的允许的最高在线人数
		msg.setMaxOnlineCount(GsCountChecker.getInstance().getMaxCount());
		//是否打开了redis  true:打开，false：关闭
		msg.setOpenRedis(ChuanQiConfigUtil.getGlobalRedisOn());
		
		return msg;
	}
	
	
	/**
	 * 获取配置文件的md5 sign值
	 * @param params
	 * @return
	 */
	public CallBack_Msg getGameConfigSignByName(Map<String,Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		Object typeObj = params.get("name");
		if(typeObj == null){
			msg.setCode(-1);
			return msg;
		}
		
		String sign = ConfigMd5SignManange.getConfigSignByFileName(typeObj.toString());
		if(sign != null){
			msg.setResult(sign);
		}else{
			msg.setCode(0);
		}
		
		return msg;
	}
	
	/**
	 * 查看已经关闭的模块
	 * @param params
	 * @return
	 */
	public ModelFilter_Msg queryClosedModels(Map<String,String> params){
		ModelFilter_Msg msg = new ModelFilter_Msg();
		
		Set<String> modSet = msgFilterRemote.queryCloseModelInfos();
		if(modSet == null || modSet.size() == 0){
			msg.setReslults("-");
		}else{
			StringBuffer buffStr = new StringBuffer();
			for (String modName : modSet) {
				buffStr.append(modName).append(",");
			}
			
			msg.setReslults(buffStr.substring(0,buffStr.length() -1).toString());
		}
		return msg;
	}
	
	/**
	 * 查看已经关闭的指令
	 * @param params
	 * @return
	 */
	public ModelFilter_Msg queryClosedCmds(Map<String,String> params){
		ModelFilter_Msg msg = new ModelFilter_Msg();
		
		Set<Short> modSet = msgFilterRemote.getCloseCmdInfos();
		if(modSet == null || modSet.size() == 0){
			msg.setReslults("-");
		}else{
			StringBuffer buffStr = new StringBuffer();
			for (Short modName : modSet) {
				buffStr.append(modName).append(",");
			}
			
			msg.setReslults(buffStr.substring(0,buffStr.length() -1).toString());
		}
		return msg;
	}
	
	
	/**
	 * 动态关闭模块
	 * @param params
	 * @return
	 */
	public ModelFilter_Msg closedModels(Map<String,String> params){
		String modelId=params.get("modName");
		ModelFilter_Msg msg=new ModelFilter_Msg();
		msg.setFlag(msgFilterRemote.closeModelMsgCode(modelId));
		return msg;
	}
	
	/**
	 * 动态开启模块
	 * @param params
	 * @return
	 */
	public ModelFilter_Msg openModels(Map<String,String> params){
		String modelId=params.get("modName");
		ModelFilter_Msg msg=new ModelFilter_Msg();
		msg.setFlag(msgFilterRemote.openModelMsgCode(modelId));
		return msg;
	}
	
	/**
	 * 动态关闭指令
	 * @param params
	 * @return
	 */
	public ModelFilter_Msg closedCmds(Map<String,String> params){
		Short cmd = Short.parseShort(params.get("cmd"));
		ModelFilter_Msg msg=new ModelFilter_Msg();
		msg.setFlag(msgFilterRemote.closeCmdMsgCode(cmd));
		return msg;
	}
	
	/**
	 * 动态开启指令
	 * @param params
	 * @return
	 */
	public ModelFilter_Msg openCmds(Map<String,String> params){
		Short cmd = Short.parseShort(params.get("cmd"));
		ModelFilter_Msg msg=new ModelFilter_Msg();
		msg.setFlag(msgFilterRemote.openCmdMsgCode(cmd));
		return msg;
	}
	
	/**
	 * gm后台更新跨服连接
	 * @param params
	 * @return
	 */
	public HttpCallBackMsg refreshKfConnection(Map<String,String> params){
//		KuafuConfigUtil.refreshKfConn();
		return new HttpCallBackMsg();
	}
	
	/**
	 * http倒计时停机对外服务
	 * @param params
	 * @return
	 */
	@HttpKeyType(keyType = KeyEnum.SECRET)
	public HttpStopServer_Msg stopGameServer(Map<String,String> params){
		HttpStopServer_Msg msg = new HttpStopServer_Msg();
		final String value = params.get("stoptime");
		if(value != null){
			msg.setState(1);//1成功
			
			Thread stopThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					ChuanQiStopHelper.httpStop(Integer.parseInt(value));
				}
			},"stopServerThread");
			
			stopThread.start();
			
		}else{
			msg.setState(0);//0失败
		}
		
		return msg;
	}
	
	/**
	 * http充值业务
	 * @param params{userId:账号Id,userRoleId:角色id}
	 * @return
	 */
	@HttpKeyType(keyType = KeyEnum.CONFIDENTIAL)
	public CallBack_Msg rechageAccount(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String userId = params.get("userId").toString();
		Long userRoleId = Long.parseLong(params.get("userRoleId").toString());
		String serverId = params.get("serverId").toString();
		String platformType = params.get("platformType").toString();
		String orderId = params.get("orderId").toString();
		Double rmb = Double.parseDouble(params.get("rmb").toString());
		Long yb = Long.parseLong(params.get("yb").toString());
		
		int result = rechargeExportService.rechage(userId, userRoleId, serverId, platformType, orderId, rmb, yb);
		msg.setResult(result+"");
		return msg;
	}
	
	/**
	 * http后台充值业务
	 * @param params{roleId:账号Id,}
	 * @return
	 */
	@HttpKeyType(keyType = KeyEnum.GMTOOLS_SAFE)
	public CallBack_Msg webRechageAccount(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		Long userRoleId = CovertObjectUtil.obj2long(params.get("userRoleId"));
		Integer reType = CovertObjectUtil.object2int(params.get("reType"));
		String orderId = CovertObjectUtil.object2String(params.get("orderId"));
		Double rmb = (double)CovertObjectUtil.obj2float(params.get("rmb"));
		Long yb = CovertObjectUtil.object2Long(params.get("yb"));
		int result = rechargeExportService.webRechage(userRoleId, reType, orderId, rmb, yb);
		msg.setResult(result + "");
		return msg;
	}
	
	/**
	 * 腾讯http充值业务
	 * @param params{userId:账号Id,}
	 * @return
	 */
	@HttpKeyType(keyType = KeyEnum.CONFIDENTIAL)
	public CallBack_Msg rechageAccountQQ(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String serverId = params.get("serverId").toString();
		Long userRoleId = Long.parseLong(params.get("userRoleId").toString());
		String billno = params.get("billno").toString();
		String platformType = params.get("platformType").toString();
		String itemXinxi = params.get("itemXinxi").toString();;
		String amt = params.get("amt").toString();
		String pubacctPayamtCoins = params.get("pubacctPayamtCoins").toString();
		
		int result = rechargeExportService.rechageQQ(serverId, userRoleId, billno, platformType, itemXinxi, amt, pubacctPayamtCoins);
		msg.setResult(result+"");
		return msg;
	}
	
	/**
	 * 腾讯蓝钻开通活动业务
	 * @param params{userId:账号Id,}
	 * @return
	 */
	@HttpKeyType(keyType = KeyEnum.CONFIDENTIAL)
	public CallBack_Msg tencentLanZuan(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String serverId = params.get("serverId").toString();
		String userId = params.get("userId").toString();
		
		int result = qqExportService.updateTenCentLanZuan(userId, serverId);
		msg.setResult(result+"");
		return msg;
	}
	/**
	 * 越南http预充值业务
	 * @param params{userId:账号Id,}
	 * @return
	 */
	//@HttpKeyType(keyType = KeyEnum.CONFIDENTIAL)
	public CallBack_Msg rechageAccountYueNan(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String serverId = params.get("serverId").toString();//服务器ID
		String userId = params.get("userId").toString();//账号
		String billno = params.get("billno").toString();//订单号
		String platformType = params.get("platformType").toString();//平台ID
		Double rmb = Double.parseDouble(params.get("rmb").toString());//充值金额
		Long yb = Long.parseLong(params.get("yb").toString());//元宝数量
		
		int result = rechargeExportService.rechageYueNan(userId, serverId, platformType, billno, rmb, yb);
		msg.setResult(result+"");
		return msg;
	}
	/**
	 * 韩国http请求排行榜业务
	 * @param params{userId:账号Id,}
	 * @return
	 */
	//@HttpKeyType(keyType = KeyEnum.CONFIDENTIAL)
	public CallBack_Msg hanGuoRankInfo(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		//String userId = params.get("userId").toString();//账号
		String serverId = params.get("serverId").toString();//服务器ID
		Integer rankType = Integer.parseInt(params.get("rankType").toString());//请求的排行类型
		JSONObject obj = rankExportService.hangouGetRank(serverId, rankType);
		//long rank = rankExportService.getUserServerRank(userId, serverId,rankType);
		
		//obj.put("userRank", rank);
		msg.setResult(""+obj.toString());
		return msg;
	}
	/**
	 * 台湾http视频业务
	 * @param params{userId:账号Id,}
	 * @return
	 */
	//@HttpKeyType(keyType = KeyEnum.CONFIDENTIAL)
	public CallBack_Msg taiWanShiPin(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String type = params.get("type").toString();
		String serverId = params.get("server_id").toString();//服务器ID
		String zbUserId = params.get("senderid").toString();//主播游戏账号
		String zbUserName = params.get("sendername").toString();//主播游戏名字
		String userId = params.get("targetid").toString();//游戏账号
//		String userName = params.get("targetname").toString();//游戏名字
		String content = params.get("content").toString();//聊天内容
		try {
			if(content!=null){
				content = URLDecoder.decode(content, "utf-8");
			}
//			if(userName!=null){
//				userName = URLDecoder.decode(userName, "utf-8");
//			}
			if(zbUserName!=null){
				zbUserName = URLDecoder.decode(zbUserName, "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			ChuanQiLog.error("主播世界聊天解码错误",e);
		}
		
		int result = chatExportService.taiWanZhuBoLiaoTian(zbUserId,zbUserName,userId,serverId, type,content);
		
		msg.setResult(""+result);
		return msg;
	}
	
	/**
	 * 腾讯http任务集市
	 * @param params{userId:账号Id,}
	 * @return
	 */
	@HttpKeyType(keyType = KeyEnum.CONFIDENTIAL)
	public CallBack_Msg tencentRenWuJiShi(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String openId  = params.get("userId").toString();
		String serverId = params.get("serverId").toString();
		String cmd = params.get("cmd").toString();
		String renWuId  = params.get("renWuId").toString();
		Integer step  = Integer.parseInt(params.get("step").toString());
		String payItem  = params.get("payItem").toString();
		
		Integer result = renWuJiShiExportService.renWuJiShiLingQu(openId, serverId, cmd, renWuId, step, payItem);
		msg.setResult(result+"");
		return msg;
	}
	/**
	 * 百度http任务集市
	 * @param params{userId:账号Id,}
	 * @return
	 */
	@HttpKeyType(keyType = KeyEnum.CONFIDENTIAL)
	public CallBack_Msg baiduRenWuJiShi(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String openId  = params.get("userId").toString();
		String serverId = params.get("serverId").toString();
		String renWuId  = params.get("renWuId").toString();
		Integer step  = Integer.parseInt(params.get("step").toString());
		
		Integer result = baiDuRenWuJiShiExportService.renWuJiShiLingQu(openId, serverId,renWuId, step);
		msg.setResult(result+"");
		return msg;
	}

	/**
	 * 修改用户类型
	 * @param params
	 * @return
	 */
	public CallBack_Msg changeUserType(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
//		String userId = CovertObjectUtil.object2String(params.get("userId"));
//		String serverId = CovertObjectUtil.object2String(params.get("serverId"));
//		Integer type = CovertObjectUtil.object2int(params.get("type"));
//		String result = accountExportService.updateUserAccountType(userId, serverId, gm, type);
//		msg.setResult(result);
		return msg;
	}
	/**
	 * 修改用户类型
	 * @param params
	 * @return
	 */
	public CallBack_Msg changeGmType(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String userId = CovertObjectUtil.object2String(params.get("userId"));
		String serverId = CovertObjectUtil.object2String(params.get("serverId"));
		Integer type = CovertObjectUtil.object2int(params.get("type"));
		Long keepTime = CovertObjectUtil.object2Long(params.get("keepTime"));
		String result = roleExportService.changeGmType(userId, serverId, type, keepTime);
		msg.setResult(result);
		return msg;
	}
	
	/**
	 * 根据对应的号copy多个号,指定copy一些模块
	 */
	public CallBack_Msg copyRoleByRole(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		Map<String, String> result =  roleExportService.copyRoleByRole(params);
		msg.setResult(result.toString());
		return msg;
		
	}
	/**
	 * 后台删除游戏邮件业务
	 * @param params{userId:账号Id,}
	 * @return
	 */
	public CallBack_Msg webDeleteEmail(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String content = params.get("content").toString();;
		String startTime = params.get("startTime").toString();
		String endTime = params.get("endTime").toString();
		
		int re = emailExportService.gmToolsDelEmailByContent(content, startTime, endTime);
		msg.setResult(""+re);
		return msg;
	}
	/**
	 * 发送邮件
	 * @param params
	 * @return
	 */
	public CallBack_Msg sendEmail(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		try {
			String userRoleIds = CovertObjectUtil.obj2StrOrNull(params.get("userRoleIds"));
			String title = URLDecoder.decode(CovertObjectUtil.object2String(params.get("title")),"utf-8");
			if(title.startsWith(EmailUtil.CODE_GM_FLAG)){
				title = EmailUtil.getCodeEmail(title.substring(1));
			}else{
				title = EmailUtil.getNomalEmail(title);
			}
			
			String content = URLDecoder.decode(CovertObjectUtil.object2String(params.get("content")),"utf-8");
			if(content.startsWith(EmailUtil.CODE_GM_FLAG)){
				content = EmailUtil.getCodeEmail(content.substring(1));
			}else{
				content = EmailUtil.getNomalEmail(content);
			}
			
			Integer emailType = CovertObjectUtil.object2Integer(params.get("emailType"));
			Integer min = CovertObjectUtil.object2Integer(params.get("min"));
			Integer max = CovertObjectUtil.object2Integer(params.get("max"));
			String attachment = CovertObjectUtil.obj2StrOrNull(params.get("attachment"));
			Long checkTime = CovertObjectUtil.object2Long(params.get("checkTime"));
			String biaoshi = CovertObjectUtil.obj2StrOrNull(params.get("biaoshi"));
			String result = emailExportService.sendEmailGmtool(userRoleIds,title, content, emailType, min, max, attachment, checkTime, biaoshi);
			msg.setResult(result);
		} catch (UnsupportedEncodingException e) {
			ChuanQiLog.error("email decode err.");
			msg.setResult("email decode error");
		}
		return msg;
	}
	/**
	 * 打印登陆gate信息日志
	 * @param params
	 * @return
	 */
	public CallBack_Msg printGateLog(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String userId = CovertObjectUtil.obj2StrOrNull(params.get("userId"));
		String ip = CovertObjectUtil.obj2StrOrNull(params.get("ip"));
		String serverId = CovertObjectUtil.obj2StrOrNull(params.get("serverId"));
		String coopName = CovertObjectUtil.obj2StrOrNull(params.get("coopName"));
		String refererUrl = CovertObjectUtil.obj2StrOrNull(params.get("refererUrl"));
		GamePublishEvent.publishEvent(new LoginGateLogEvent(userId, ip, serverId, coopName, refererUrl));
		return msg;
	}


	/**
	 * 批量打印登陆gate信息日志
	 * @param params
	 * @return
	 */
	public CallBack_Msg printGateLogs(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String logs = CovertObjectUtil.obj2StrOrNull(params.get("logs"));
		if (logs != null) {
			JSONArray json = JSONArray.parseArray(logs);
			for (Object object : json) {
				JSONObject jObject = (JSONObject)object;
				String userId = CovertObjectUtil.obj2StrOrNull(jObject.get("userId"));
				String ip = CovertObjectUtil.obj2StrOrNull(jObject.get("ip"));
				String serverId = CovertObjectUtil.obj2StrOrNull(jObject.get("serverId"));
				String coopName = CovertObjectUtil.obj2StrOrNull(jObject.get("coopName"));
				String refererUrl = CovertObjectUtil.obj2StrOrNull(jObject.get("refererUrl"));
				GamePublishEvent.publishEvent(new LoginGateLogEvent(userId, ip, serverId, coopName, refererUrl));
			}
		}
		return msg;
	}
	
	/**
	 * 更新热发布商城配置--商城配置
	 * @param params
	 * @return
	 */
	public CallBack_Msg rfbRefreshConfig(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String fileName = CovertObjectUtil.object2String(params.get("fileName"));
		Integer result = RefreshableConfigureManager.refresh(fileName);
		msg.setResult(result.toString());
		return msg;
	}
	
	/**
	 * 更新热发布配置
	 * @param params
	 * @return
	 */
	public CallBack_Msg rfbYunYingRefushRelation(Map<String, Object> params){
		//获取主活动参数
		Integer activityId = CovertObjectUtil.object2int(params.get("id"));
		if(activityId != null){
			//主活动id,暂时用不到(后台有这个参数的支持)所有不用
			//Integer mainId = CovertObjectUtil.object2int(params.get("mainId"));
			
			GamePublishEvent.publishEvent(new GxRfbChangeEvent(activityId));
		}else{
			activityId = -1;
		}
		
		//返回结果
		CallBack_Msg msg = new CallBack_Msg();
		msg.setResult(activityId.toString());
		return msg;
	}
	
	/**
	 * 通知重读消费排名数据
	 * @param params
	 * @return
	 */
	public CallBack_Msg rfbXiaoFeiPaiMingDu(Map<String, Object> params){
		refabuXiaoFeiExportService.quartXiaoFei();
		CallBack_Msg msg = new CallBack_Msg();
		msg.setResult("1");
		return msg;
	}
	
	/**
	 * 更新运营热发布主活动
	 * @param params
	 * @return
	 */
	public CallBack_Msg rfbYunYingRefreshConfig(Map<String, Object> params){
		//获取主活动参数
		Integer activityId = CovertObjectUtil.object2int(params.get("id"));
		
		//抛出一个主活动变更事件
		if(activityId != null){
			GamePublishEvent.publishEvent(new ZhuRfbChangeEvent(activityId));
		}else{
			activityId = -1;
		}
		
		//返回结果
		CallBack_Msg msg = new CallBack_Msg();
		msg.setResult(activityId.toString());
		return msg;
	}
	
	/**
	 * 标记删除热发布主活动
	 * @param params
	 * @return
	 */
	public CallBack_Msg rfbYunYingDeleteConfig(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		Integer activityId = CovertObjectUtil.object2int(params.get("id"));
		if(activityId != null){
			//主活动删除
			GamePublishEvent.publishEvent(new ZhuRfbDelEvent(activityId));
		}else{
			activityId = -1;
		}
		
		msg.setResult(activityId.toString());
		return msg;
	}
	
	
	/**
	 * 热发布修改子活动
	 * @param params
	 * @return
	 */
	public CallBack_Msg rfbYunYingZhiConfigUpdate(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		Integer subId = CovertObjectUtil.object2int(params.get("subId"));
		if(subId != null){
			Integer mainId = CovertObjectUtil.object2int(params.get("mainId"));
			
			GamePublishEvent.publishEvent(new HandleRfbChangeEvent(subId, mainId));
		}else{
			subId = -1;
		}
		
		msg.setResult(subId.toString());
		return msg;
	}
	
	/**
	 * 标记删除热发布子活动
	 * @param params
	 * @return
	 */
	public CallBack_Msg rfbYunYingDeleteZhiConfig(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		Integer subId = CovertObjectUtil.object2int(params.get("id"));
		if(subId != null){
			GamePublishEvent.publishEvent(new ZhiRfbDelEvent(subId));
		}else{
			subId = -1;
		}
		
		msg.setResult(subId.toString());
		return msg;
	}
	
	/**
	 * 获取角色数据
	 * @param params
	 * @return
	 */
	public RoleData_Msg getRoleData(Map<String, Object> params){
		RoleData_Msg msg = new RoleData_Msg();
		String userId = CovertObjectUtil.object2String(params.get("userId"));
		String serverId = CovertObjectUtil.object2String(params.get("serverId"));
		UserRole role = roleExportService.getRoleFromDb(userId, serverId);
		if(role != null){
			msg.setRoleId(role.getId());
			msg.setName(role.getName());
			msg.setConfigId(role.getConfigId());
			msg.setLevel(role.getLevel());
			msg.setUserId(userId);
			msg.setCreateTime(role.getCreateTime().getTime());
		}else{
			msg.setCode(-1);
		}
		return msg;
	}
	
	/**
	 * 禁言封号
	 * @param params
	 * @return
	 */
	public CallBack_Msg jinfeng(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String userId = CovertObjectUtil.object2String(params.get("userId"));
		String serverId = CovertObjectUtil.object2String(params.get("serverId"));
		int type = CovertObjectUtil.object2int(params.get("type"));
		int keepTime = CovertObjectUtil.object2int(params.get("keepTime"));
		int sort = CovertObjectUtil.object2int(params.get("sort"));
		String reasons = CovertObjectUtil.object2String(params.get("reasons"));
		UserRole role = roleExportService.getRoleFromDb(userId, serverId);
		String result = HttpErrorCode.USER_NOT_EXIST;
		if(role != null){
			result = jinFengExportService.httpJinfeng(role.getId(), type, keepTime, sort, reasons);
		}
		msg.setResult(result);
		return msg;
	}
	/**
	 * 解封IP
	 * @param params
	 * @return
	 */
	public CallBack_Msg jieIp(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String ip = CovertObjectUtil.object2String(params.get("ip"));
		jinFengExportService.httpJieIp(ip);
		msg.setResult(HttpErrorCode.SUCCESS);
		return msg;
	}
	
	/**
	 * 后台删除背包内物品
	 * @param params 
	 * @return
	 */
	public CallBack_Msg webDelBagGoods(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		Long userRoleId = CovertObjectUtil.obj2long(params.get("userRoleId"));
		Long guid = CovertObjectUtil.obj2long(params.get("guid"));
		Integer count = CovertObjectUtil.object2Integer(params.get("count"));
		String result = roleBagExportService.webDelRoleGoodsCount(userRoleId, guid, count);
		msg.setResult(result);
		return msg;
	}
	
	
	/**
	 * 测试
	 * @param params{}
	 * @return
	 */
	public CallBack_Msg testApp(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg(); 
		Object[] data=new Object[]{97257521152l,9};
		IoMsgSender.swap(new Object[]{ClientCmdType.GET_STORAGE_ITEMS,data,0,1,1,0,0,94493474818l,null,null});
		
//		roleBagExportService.pkDropBodyGoods(77447823361l, 1, true);
		
		
		return msg;
	}
	
	/**
	 * 重置游戏最高在线人数 允许1~5000
	 * @param params
	 * @return
	 */
	public CallBack_Msg resetGameMaxOnineCount(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg(); 
		Integer count = CovertObjectUtil.object2Integer(params.get("count"));
		if(count == null){
			msg.setResult("-1");
		}else if(count <= 0){
			msg.setResult("-2");
		}else{
			GsCountChecker.resetGameMaxOnlineCount(count);
			msg.setResult("1");
		}
		
		return msg;
	}
	
	/**
	 * 更新公告配置
	 * @param params
	 * @return
	 */
	public CallBack_Msg noticeRefreshConfig(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String fileName =  CovertObjectUtil.object2String(params.get("fileName"));
		Integer result = RefreshableNoticeConfigureManager.refresh(fileName);
		msg.setResult(result+"");
		return msg;
	}
	/**
	 * 激活手机奖励
	 * @param params
	 * @return
	 */
	public CallBack_Msg activatePhoneReward(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String serverId = CovertObjectUtil.object2String(params.get("serverId"));
		String userId = CovertObjectUtil.object2String(params.get("userId"));
		Integer ret  = _37ExportService.activatePhoneReward(serverId,userId);
		msg.setResult(String.valueOf(ret));
		return msg;
	}
	

	/**
	 * 超级会员活动关闭开启或者信息变更通知 lxn
	 * @param params
	 * @return
	 */
	public CallBack_Msg platformSuperVip(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		try {
			ptCommonSuperVipService.webOrderUpdateConfig(params); //"state"
			msg.setResult("1");
		} catch (Exception e) {
			msg.setResult("0");
		}
		return  msg;
	}
	/**
	 * 360特权领取礼包压测接口
	 * @param params
	 * @return
	 */
	public CallBack_Msg web360TequanTest(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		
		try {
			ChuanQiLog.info("enter  web360TequanTest... ");
		   Object[] result = platformExportService.web360TequanTest(params); 
		   ChuanQiLog.info("end web360TequanTest...,result={} ",result);
		   if(result!=null && (Integer)result[0]==1){
			   msg.setResult("1");
		   }else{
			   msg.setResult("0");
		   }
		} catch (Exception e) {
			msg.setResult("0");
			ChuanQiLog.info("excute web360TequanTest error");
		}
		return  msg;
	}
	
	
	/**
	 * 后台工具删除指定邮件
	 * @param params
	 * @return
	 */
	public CallBack_Msg gmToolsDelEmailById(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		Long emailId = CovertObjectUtil.object2Long(params.get("emailId"));
		if(emailId != null){
			emailExportService.gmToolsDelEmailByEmailId(emailId);
			msg.setResult("1");
		}else{
			msg.setResult("-1");
		}
		return msg;
	}
	
	
	/**
	 * 激活手机奖励
	 * @param params
	 * @return
	 */
	public CallBack_Msg dingzhiChenghao(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String serverId = CovertObjectUtil.object2String(params.get("serverId"));
		String userId = CovertObjectUtil.object2String(params.get("userId"));
		Integer chenghaoId = CovertObjectUtil.object2int(params.get("chenghaoId"));
		String chenghaoName = CovertObjectUtil.object2String(params.get("chenghaoName"));
		String chenghaoRes = CovertObjectUtil.object2String(params.get("chenghaoRes"));
		Integer ret  = chenghaoExportService.dingshiChenghao(serverId, userId, chenghaoId, chenghaoName, chenghaoRes);
		msg.setResult(String.valueOf(ret));
		return msg;
	}
	
	/**
	 * 临时打开指令统计
	 * @param params
	 * @return
	 */
	public CallBack_Msg openTmpCmdTj(Map<String, String> params){
		CallBack_Msg msg = new CallBack_Msg();
		String open = params.get("open");
		if(open != null){
			SessionConstants.TMP_STATIC_MSG = "1".equals(open);
			msg.setResult("1");
		}else{
			msg.setResult("-1");
		}
		
		return msg;
	}
	
	public CallBack_Msg cleanKuafuArena1v1Jifen(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String userRoleId = CovertObjectUtil.object2String(params.get("userRoleId"));
		if(userRoleId.equals("all")){
			kuafuArena1v1SourceExportService.clean();
		}else {
			kuafuArena1v1SourceExportService.clean(Long.valueOf(userRoleId));
		}
		msg.setResult("1");
		return msg;
	}
	
	public CallBack_Msg redisOnOff(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String status = CovertObjectUtil.object2String(params.get("status"));
		if(status.equals("on")){
			if(GameServerContext.getRedis() == null && ChuanQiConfigUtil.getGlobalRedisOn()){
				String globalRedisIp = ChuanQiConfigUtil.getGlobalRedisIp();
				String globalRedisPort = ChuanQiConfigUtil.getGlobalRedisPort();
				int globalRedisDb = ChuanQiConfigUtil.getGlobalRedisDb();
				String globalRedisPwd = ChuanQiConfigUtil.getGlobalRedisPassword();
				
				Redis redis = new Redis(globalRedisIp, Integer.parseInt(globalRedisPort),globalRedisDb, globalRedisPwd);
				GameServerContext.setRedis(redis);
				msg.setResult("1");
			}
		}
		if(status.equals("off")){
			if(GameServerContext.getRedis()!=null){
				GameServerContext.getRedis().destory();
				GameServerContext.setRedis(null);
				msg.setResult("1");
			}
		}
		msg.setResult("-1");
		return msg;
	}
	
	public CallBack_Msg readPropertiesFile(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String fileName = CovertObjectUtil.object2String(params.get("fileName"));
		int status = ConfigureUtil.reloadProperties(fileName);
		msg.setResult(String.valueOf(status));
		return msg;
	}
	
	/**
	 * 测试当前源服是否能够连通指定跨服
	 * @param params
	 * @return -1:不能连接，1：可以连接
	 */
	public CallBack_Msg testKuafuServerConnect(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String serverId = CovertObjectUtil.object2String(params.get("serverId"));
		KuafuServerInfo serverInfo = KuafuServerInfoManager.getInstance()
				.getKuafuServerInfo(serverId);
		KuafuNetTunnel tunnel =	KuafuConfigUtil.getConnection(serverInfo);
		if(tunnel==null || !tunnel.isConnected()){
			msg.setResult("-1");
			KuafuConfigUtil.returnBrokenConnection(tunnel);
		}else{
			msg.setResult("1");
			KuafuConfigUtil.returnConnection(tunnel);
		}
		return msg;
	}
	/**
	 * 测试当前源服是否能够连通匹配服
	 * @param params
	 * @return -1:不能连接，1：可以连接
	 */
	public CallBack_Msg testMatchServerConnect(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		if(!KuafuMatchServerUtil.isMatchServerAvailable()){
			msg.setResult("-1");
		}else{
			msg.setResult("1");
		}
		return msg;
	}
	/**
	 * 测试当前源服是否能够连通匹配服
	 * @param params
	 * @return -2:未初始化 -1:不能连接，1：可以连接
	 */
	public CallBack_Msg testRedisConnect(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		Redis redis = GameServerContext.getRedis();
		if(redis==null){
			msg.setResult("-2");
		}
		if(!redis.ping()){
			msg.setResult("-1");
		}else{
			msg.setResult("1");
		}
		return msg;
	}
	
	/**
	 * 修改公会
	 * @param params
	 * @return
	 */
	public CallBack_Msg updateGuild(Map<String, String> params){
		CallBack_Msg msg = new CallBack_Msg();
		Long guildId = CovertObjectUtil.obj2long(params.get("guildId"));
		String info = params.get("info");
		Integer type = CovertObjectUtil.object2int(params.get("type"));
		String result = HttpErrorCode.ARGS_ERROR;
		if(type == 1){
			result = guildExportService.gmChangeGuildName(guildId, info);
		}else if(type == 2){
			result = guildExportService.gmChangeGuildNotice(guildId, info);
		}
		msg.setResult(result);
		return msg;
	}
	
	/**
	 * 修改公会会长
	 * @param params
	 * @return
	 */
	public CallBack_Msg updateGuildLeader(Map<String, String> params){
		CallBack_Msg msg = new CallBack_Msg();
		Long guildId = CovertObjectUtil.obj2long(params.get("guildId"));
		Long userRoleId = CovertObjectUtil.obj2long(params.get("userRoleId"));
		String result = guildExportService.gmChangeLeader(guildId, userRoleId);
		msg.setResult(result);
		return msg;
	}
	
	/**
	 * 将某人踢出公会
	 * @param params
	 * @return
	 */
	public CallBack_Msg kickGuildMember(Map<String, String> params){
		CallBack_Msg msg = new CallBack_Msg();
		Long guildId = CovertObjectUtil.obj2long(params.get("guildId"));
		Long userRoleId = CovertObjectUtil.obj2long(params.get("userRoleId"));
		String result = guildExportService.gmKickMember(guildId, userRoleId);
		msg.setResult(result);
		return msg;
	}
	
	public CallBack_Msg cleanServerInfo(Map<String, String> params){
		CallBack_Msg msg = new CallBack_Msg();
		String serverId = params.get("serverId");
		KuafuServerInfoManager.getInstance().removeServerInfo(serverId);
		msg.setResult("1");
		return msg;
	}
	public CallBack_Msg removeRoleServerBidding(Map<String, String> params){
		CallBack_Msg msg = new CallBack_Msg();
		Long userRoleId = CovertObjectUtil.obj2long(params.get("userRoleId"));
		KuafuRoleServerManager.getInstance().removeBind(userRoleId);
		msg.setResult("1");
		return msg;
	}
	
	public CallBack_Msg openKuafuDebugLog(Map<String, String> params){
		CallBack_Msg msg = new CallBack_Msg();
		Boolean status = Boolean.parseBoolean(params.get("status"));
		KuafuManager.OPEN_KUAFU_DEBUG_LOG = status;
		msg.setResult("1");
		return msg;
	}
	
	/**
	 * gate 拉取玩家是否有角色（包括删除的和未删除的）
	 * @param params 
	 * @return
	 */
	public CallBack_Msg checkRole(Map<String, Object> params){
		CallBack_Msg msg = new CallBack_Msg();
		String result = "0";
		
		try {
			String userId = CovertObjectUtil.object2String(params.get("userId"));
			String serverId = CovertObjectUtil.object2String(params.get("serverId"));
			UserRole userRole = roleExportService.getRoleFromDb(userId, serverId);
			
			if(userRole != null){
				result = "1";
			}
		} catch (Exception e) {
			result = "-1";
			ChuanQiLog.error("",e);
		}
		
		msg.setResult(result);
		return msg;
	}
	public CallBack_Msg getKuafuStaus(Map<String, String> params){
		CallBack_Msg msg = new CallBack_Msg();
		String userRoleId = params.get("userRoleId");
		msg.setResult(String.valueOf(KuafuManager.kuafuIng(Long.valueOf(userRoleId))));
		return msg;
	}
	public CallBack_Msg setIngKuafu(Map<String, String> params){
		CallBack_Msg msg = new CallBack_Msg();
		String userRoleId = params.get("userRoleId");
		KuafuManager.startKuafu(Long.valueOf(userRoleId));
		msg.setResult("1");
		return msg;
	}
	public CallBack_Msg clearRoleKuafu(Map<String, String> params){
		CallBack_Msg msg = new CallBack_Msg();
		String userRoleIdStr = params.get("userRoleId");
		Long userRoleId = Long.valueOf(userRoleIdStr);
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			KuafuManager.clearRoleKuafu(userRoleId);
			msg.setResult("1");
		}else{
			msg.setResult("0");
		}
		return msg;
	}
	/**
	 * 解决玩家卡长场景
	 * 需要在玩家下线后执行
	 * @param params
	 * @return
	 */
	public CallBack_Msg clearStageRole(Map<String, String> params){
		CallBack_Msg msg = new CallBack_Msg();
		String userRoleIdStr = params.get("userRoleId");
		Long userRoleId = Long.valueOf(userRoleIdStr);
		Collection<IStage> stages = StageManager.getAllStage();
		int count =0;
		if(stages!=null){
			for(IStage e:stages){
				IStageElement stageElement=	e.getElement(userRoleId, ElementType.ROLE);
				if(stageElement!=null){
					ChuanQiLog.error("clearStageRole( userRoleId:" + userRoleId +", stageId :" + e.getId() + ")");
					e.leave(stageElement);
					count++;
				}
				if(e instanceof AbsAoiStage ){
					AbsAoiStage aoiStage = (AbsAoiStage)e;
					 Collection<AOI> aois =	aoiStage.getAoiManager().getAoiMap().values();
					 if(aois!=null){
						 for(AOI aoi:aois){
							boolean exist1= aoi.removeElement(userRoleId, ElementType.ROLE);
							boolean exist2= aoi.removeElement(userRoleId, ElementType.FIGHTER);
							if(exist1 || exist2){
								count++;
							}
						 }
					 }
				}
			}
		}
		msg.setResult(String.valueOf(count));
		return msg;
	}
	
	
	public CallBack_Msg openClosePrintJyLog(Map<String, String> params){
		CallBack_Msg msg = new CallBack_Msg();
		boolean open = CovertObjectUtil.object2boolean(params.get("open"));
		if(open){
			junYouJobManager.startJob();
		}else{
			junYouJobManager.stopJob();
		}
		msg.setResult("1");
		return msg;
	}
}
