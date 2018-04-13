package com.junyou.bus.fuben.easyation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.fuben.service.MoreFubenTeamService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.module.GameModType;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

/**
 * 多人副本Action
 * @author chenjianye
 * @date 2015-04-27
 */
@Controller
@EasyWorker(moduleName = GameModType.MORE_FUBEN_MODULE)
public class MoreFubenAction {
	@Autowired
	private MoreFubenTeamService moreFubenTeamService;
	
	/**
	 * 请求幻境历练副本信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_INFO)
	public void getMoreFubenInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = moreFubenTeamService.getMoreFubenInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_INFO, obj);
	}
	
	/**
	 * 请求幻境历练副本队伍信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_INIT_TEAM)
	public void initTeamInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer fubenId =(Integer)data[0];
		Integer beginIndex =(Integer)data[1];
		Integer endIndex =(Integer)data[2];
		Object[] result = moreFubenTeamService.initTeamInfo(fubenId,beginIndex,endIndex);
		BusMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_INIT_TEAM, result);
	}
	
	/**
	 * 请求创建幻境历练副本队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_TEAM_CREATE)
	public void createMoreFubenTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Object[] result = moreFubenTeamService.createMoreFubenTeam(userRoleId,(Integer)data[0],CovertObjectUtil.obj2long(data[1]),(Boolean)data[2]);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_TEAM_CREATE, result);
		}
	}
	
	/**
	 * 请求加入幻境历练副本队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_TEAM_JOIN)
	public void joinMoreFubenTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer teamId = inMsg.getData();
		Object[] obj = moreFubenTeamService.applyMoreFubenTeam(userRoleId,teamId);
		if(obj != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_TEAM_JOIN, obj);
		}
	}
	
	/**
	 * 快速加入
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_QUICK_JOIN)
	public void quickJoinMoreFubenTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer fubenId = inMsg.getData();
		Object[] obj = moreFubenTeamService.quickJoin(userRoleId,fubenId);
		if(obj != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_TEAM_JOIN, obj);
		}
	}
	
	/**
	 * 队长把谁踢出幻境历练副本队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_TEAM_REMOVE)
	public void removeMoreFubenTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long targetRoleId = CovertObjectUtil.object2Long(inMsg.getData());
		moreFubenTeamService.removeMoreFubenTeam(userRoleId,targetRoleId);
	}
	
	/**
	 * 队长更改幻境历练副本队伍进入需求战力
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_CHANGE_STRENGTH)
	public void teamChangeStrength(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long strength = CovertObjectUtil.object2Long(inMsg.getData());
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId, InnerCmdType.MORE_FUBEN_CHANGE_ZPLUS, new Object[]{userRoleId,ChuanQiConfigUtil.getServerId(),strength});
	}
	
	/**
	 * 队长更改幻境历练副本队伍是否满员自动挑战
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_SET_AUTO)
	public void teamChangeAuto(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId, InnerCmdType.MORE_FUBEN_SET_AUTO, new Object[]{userRoleId,ChuanQiConfigUtil.getServerId()});
	}
	
	/**
	 * 退出幻境历练副本队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_TEAM_LEAVE)
	public void leaveMoreFubenTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		moreFubenTeamService.leaveMoreFubenTeam(userRoleId);
	}
	
	/**
	 * 幻境历练副本队伍准备状态变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_PREPARE)
	public void moreFubenPrepare(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId, InnerCmdType.MORE_FUBEN_PREPARE, new Object[]{userRoleId,ChuanQiConfigUtil.getServerId()});
	}
	
	/**
	 * （副本预进入请求）通知队伍里所有的人在指定时间戳后进入副本
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_PRE_START)
	public void moreFubenStart(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId, InnerCmdType.MORE_FUBEN_PRE_START, new Object[]{userRoleId,ChuanQiConfigUtil.getServerId()});
	}
	
//	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_QUICKLY_ADD)
//	public void quicklyAddFubenTeam(Message inMsg){
//		Long userRoleId = inMsg.getRoleId();
//		Integer fubenId = inMsg.getData();
//		Object[] obj = moreFubenTeamService.quicklyAdd(userRoleId, fubenId);
//		BusMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_QUICKLY_ADD, obj);
//	}
	
	
	/**
	 * 通知BUS业务退出多人副本	
	 * @param inMsg
	 */
	@TokenCheck(component = GameConstants.COMPONENT_MORE_FUBEN_FORCED_LEAVE)
	@EasyMapping(mapping = InnerCmdType.B_EXIT_MORE_FUBEN)
	public void bMoreFubenExit(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = moreFubenTeamService.exitFuben(userRoleId);
		if(obj != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_EXIT, obj);
		}
	}
	
	/**
	 * 多人副本进入状态
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.B_ENTER_MORE_FUBEN)
	public void bEnterMoreFuben(Message inMsg){
		Integer teamId = inMsg.getData();
		moreFubenTeamService.enterMoreFuben(teamId);
	}
	
	/**
	 * 请求退出幻境历练副本
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_EXIT,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void moreFubenExit(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = moreFubenTeamService.exitFuben(userRoleId);
		if(obj != null){
			KuafuMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_EXIT, obj);
		}
	}
	
	/**
	 * 请求领取幻境历练副本奖励
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_FIRST_REWARD)
	public void getMoreTeamReward(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer fubenId = inMsg.getData();
		Object[] obj = moreFubenTeamService.getMoreFubenReward(userRoleId,fubenId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_FIRST_REWARD, obj);
	}
	
	/**
	 * 多人副本完成（标记为副本完成状态）
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.S_MORE_FUBEN_FINISH,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void exitMoreFubenFinish(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer fubenId = inMsg.getData();
		moreFubenTeamService.challengeOver(userRoleId,fubenId);
	}
	
	/**
	 * 跨服请求成员数据
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_ASK_ROLE_DATA,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void askRoleData(Message inMsg){
		Long userRoleId = LongUtils.obj2long(inMsg.getData());		
		moreFubenTeamService.sendRoleData(userRoleId);
	}
	
	/**
	 * 离开队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_LEAVE_TEAM,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void leaveTeam(Message inMsg){
		Long userRoleId = LongUtils.obj2long(inMsg.getData());		
		moreFubenTeamService.leaveTeam(userRoleId);
	}
	/**
	 * 玩家已进入跨服
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_ENTER_KUAFU,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void enterKuafu(Message inMsg){
		Long userRoleId = LongUtils.obj2long(inMsg.getData());		
		moreFubenTeamService.enterKuafu(userRoleId);
	}
	/**
	 * 玩家已进入跨服
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_SYNC,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void syncToSourceServer(Message inMsg){
		Long userRoleId = inMsg.getRoleId();	
		moreFubenTeamService.syncToSourceServer(userRoleId);
	}

	/**
	 * 玩家离开多人副本
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_LEAVE_FUBEN,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void leaveFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();	
		moreFubenTeamService.leaveFuben(userRoleId);
	}
	/**
	 * 玩家进入多人副本失败
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_ENTER_FAIL,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void enterFubenFail(Message inMsg){
		Long userRoleId = inMsg.getRoleId();	
		moreFubenTeamService.enterFubenFail(userRoleId);
	}
	
	/**
	 * 多人副本进入队伍成功
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_TEAM_SUCCESS,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void moreFubenTeamSuccess(Message inMsg){
		Object[] data = inMsg.getData();
		if(data.length < 4){
			return;
		}
		Long userRoleId = LongUtils.obj2long(data[0]);
		Integer fubenId = (Integer)data[1];
		Integer teamId = (Integer)data[2];
		moreFubenTeamService.enterTeamSuccess(userRoleId, teamId, fubenId);
	}
	
	/**
	 * 多人副本邀请
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_YAOQING_OTHER)
	public void moreFubenYaoqing(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		moreFubenTeamService.moreFubenYaoqing(userRoleId);
	}
	
	/**
	 * 幻境历练异步离线业务
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_OFFLINE_HANDLE_SYN)
	public void offlineHandleSyn(Message inMsg){
		Long userRoleId = LongUtils.obj2long(inMsg.getData());
		moreFubenTeamService.offlineHandleSyn(userRoleId);
	}
	
	/**
	 * 进入多人本（原服自己玩）
	 * @param inMsg
	 */
//	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_YAOQING_OTHER)
	public void enterMoreFuben(Message inMsg){
		//TODO
	}
	
	
	
	//---------------------跨服模块 TODO--------------------

	/**
	 * 请求创建幻境历练副本队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.APPLY_CREATE_MORE_FUBEN_TEAM)
	public void createMoreFubenTeamInner(Message inMsg){
		Object[] data = inMsg.getData();
		Integer belongFubenId = (Integer)data[0];
		Long strength = CovertObjectUtil.obj2long(data[1]);
		Boolean isAuto = (Boolean)data[2];
		Object[] roleData = (Object[])data[3];
		Long userRoleId = LongUtils.obj2long(roleData[0]);
		String serverId = (String)data[4];
		
		Object[] result = moreFubenTeamService.createMoreFubenTeamKuafu(belongFubenId,strength,isAuto,userRoleId,roleData);
		if(result[0].equals(AppErrorCode.SUCCESS)){
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.MORE_FUBEN_TEAM_SUCCESS, new Object[]{userRoleId,result[1],result[2],result[3]});//成功创建队伍
		}else if(result != AppErrorCode.MORE_TEAM_EXISTS){//创建失败
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.MORE_FUBEN_LEAVE_TEAM, userRoleId);
		}
		
		Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_TEAM_CREATE,result,userRoleId};//封装转发数据
		KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
	}
	

	/**
	 * 请求幻境历练副本队伍信息
	 * @param inMsg
	 */
//	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_TEAM_INFO)
//	public void getMoreFubenTeamInfoKF(Message inMsg){
//		Object[] data = inMsg.getData();
//		Integer fubenId = (Integer)data[0];
//		Long userRoleId = LongUtils.obj2long(data[1]);
//		
//		Object[] result = moreFubenTeamService.initTeamInfo(fubenId);
//		
//		Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_INIT_TEAM,result,userRoleId};//封装转发数据
//		KuafuMsgSender.send2OneKuafuSource((String)data[2], InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
//	}
	
	/**
	 * 请求幻境历练副本队伍信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_OFFLINE_HANDLE)
	public void offlineHandle(Message inMsg){
		Long userRoleId = LongUtils.obj2long(inMsg.getData());
		moreFubenTeamService.offlineHandleKF(userRoleId);
	}
	
	/**
	 * 多人副本申请加入队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_APPLY_ENTER_TEAM)
	public void applyEnterTeam(Message inMsg){
		Object[] data = inMsg.getData();
		Integer teamId = (Integer)data[0];
		Object[] roleData = (Object[])data[1];
		String serverId = (String)data[2];
		Long userRoleId = LongUtils.obj2long(roleData[0]);
		
		Object[] result = moreFubenTeamService.applyMoreFubenTeamKF(userRoleId, teamId, roleData);
		if(result[0].equals(AppErrorCode.SUCCESS)){//加入失败
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.MORE_FUBEN_TEAM_SUCCESS, new Object[]{userRoleId,result[3],result[1],result[4]});//成功加入队伍
		}else{
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.MORE_FUBEN_LEAVE_TEAM, userRoleId);
		}
		Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_TEAM_JOIN,result,userRoleId};//封装转发数据
		KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
	}
	
	/**
	 * 多人副本退出副本
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_EXIT_FUBEN)
	public void exitFuben(Message inMsg){
		Object[] data = inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String serverId = (String)data[1];
		Object[] result = moreFubenTeamService.leaveMoreFubenTeamKF(userRoleId);
		if(result != null){
			Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_TEAM_LEAVE,result,userRoleId};
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
	}
	
	/**
	 * 多人副本踢出副本
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_KICK_FUBEN)
	public void kickFuben(Message inMsg){
		Object[] data = inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String serverId = (String)data[1];
		Long targetRoleId = LongUtils.obj2long(data[2]);
		
		Object[] result = moreFubenTeamService.removeMoreFubenTeamKF(userRoleId,targetRoleId);
		Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_TEAM_REMOVE,result,userRoleId};
		KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
	}
	
	/**
	 * 队长更改幻境历练副本队伍进入需求战力
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_CHANGE_ZPLUS)
	public void changeZplus(Message inMsg){
		Object[] data = inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String serverId = (String)data[1];
		Long zplus = LongUtils.obj2long(data[2]);
		
		Object[] result = moreFubenTeamService.teamChangeStrength(userRoleId,zplus);
		if(result != null){
			Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_CHANGE_STRENGTH,result,userRoleId};
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
	}
	
	/**
	 * 队长更改幻境历练副本队伍进入需求战力
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_SET_AUTO)
	public void setAuto(Message inMsg){
		Object[] data = inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String serverId = (String)data[1];
		
		Object[] result = moreFubenTeamService.teamSetAuto(userRoleId);
		if(result != null){
			Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_SET_AUTO,result,userRoleId};
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
	}
	/**
	 * （副本预进入请求）通知队伍里所有的人在指定时间戳后进入副本(跨服)
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_PRE_START)
	public void moreFubenStartKF(Message inMsg){
		Object[] data = inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String serverId = (String)data[1];
		Object[] result = moreFubenTeamService.startMoreFuben(userRoleId);
		if(result != null){			
			Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_PRE_START,result,userRoleId};
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
	}
	
	/**
	 * 幻境历练副本队伍准备状态变化(跨服)
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_PREPARE)
	public void moreFubenPrepareKF(Message inMsg){
		Object[] data = inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String serverId = (String)data[1];
		Object[] obj = moreFubenTeamService.prepareTeamState(userRoleId);
		if(obj != null){
			Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_PREPARE, obj,userRoleId};
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
	}
	
	/**
	 * 幻境历练副本队伍准备状态变化(跨服)
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_SEND_ROLE_DATA)
	public void sendRoleData(Message inMsg){
		Object[] data = inMsg.getData();
		moreFubenTeamService.reciveRoleData(data);
	}
	
	/**
	 * 通知跨服进入场景(跨服)
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_CAN_ENTER_STAGE)
	public void canEnterStage(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		moreFubenTeamService.enterKuafuMap(userRoleId);
	}
	
	/**
	 * 退出副本(跨服)
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_LEAVE_FUBEN_KUAFU)
	public void leaveFubenKF(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		moreFubenTeamService.leaveFubenKF(userRoleId);
	}
	
	/**
	 * 请求幻境历练副本队伍信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.HANDLE_MORE_FUBEN_TIME_OUT_TEAM)
	public void handleTimeOutTeam(Message inMsg){
		moreFubenTeamService.handleOutTimeTeam();
	}
	/**
	 * 玩家已在跨服已经切场景后再次向源服告知 标记状态（容错
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_AFTER_ENTER_STAGE,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void afterEnterKuafuStage(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		KuafuManager.startKuafu(userRoleId);
	}
	
}
