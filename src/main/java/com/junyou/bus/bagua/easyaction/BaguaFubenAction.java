package com.junyou.bus.bagua.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.bagua.service.BaguaFubenTeamService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.module.GameModType;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

@Controller
@EasyWorker(moduleName = GameModType.BAGUA, groupName = EasyGroup.BUS_CACHE)
public class BaguaFubenAction {
	@Autowired
	private BaguaFubenTeamService baguaService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	/**
	 * 请求个人信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_GET_MY_INFO)
	public void getMyInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = baguaService.getMyInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.BAGUA_GET_MY_INFO, obj);
	}
	
	/**
	 * 根据副本Id获取队伍列表
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_SELECT_TEAM)
	public void selectTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer fubenId =(Integer)data[0];
		Integer beginIndex =(Integer)data[1];
		Integer endIndex =(Integer)data[2];
		Object[] result = baguaService.selectTeamByFubenId(fubenId,beginIndex,endIndex);
		BusMsgSender.send2One(userRoleId, ClientCmdType.BAGUA_SELECT_TEAM, result);
	}
	
	/**
	 * 创建队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_CREATE_TEAM)
	public void createTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Object[] result = baguaService.createTeam(userRoleId,(Integer)data[0],CovertObjectUtil.obj2long(data[1]),(Boolean)data[2]);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.BAGUA_CREATE_TEAM, result);
		}
	}
	
	/**
	 * 加入队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_JOIN_TEAM)
	public void joinTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer teamId = inMsg.getData();
		Object[] obj = baguaService.joinTeam(userRoleId,teamId);
		if(obj != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.BAGUA_JOIN_TEAM, obj);
		}
	}
	/**
	 * 快速加入
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_FUBEN_QUICK_JOIN)
	public void quickJoinBaguaFubenTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer fubenId = inMsg.getData();
		Object[] obj = baguaService.quickJoin(userRoleId,fubenId);
		if(obj != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.BAGUA_JOIN_TEAM, obj);
		}
	}
	
	/**
	 * 队长踢人
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_KICK)
	public void kick(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long targetRoleId = CovertObjectUtil.object2Long(inMsg.getData());
		baguaService.kick(userRoleId,targetRoleId);
	}
	
	/**
	 * 队长更改队伍进入需求战力
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_CHANGE_STRENGTH)
	public void changeStrength(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long strength = CovertObjectUtil.object2Long(inMsg.getData());
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId, InnerCmdType.BAGUA_CHANGE_STRENGTH, new Object[]{userRoleId,ChuanQiConfigUtil.getServerId(),strength});
	}
	
	/**
	 * 队长更改队伍是否满员自动挑战
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_CHANGE_TEAM_AUTO_START)
	public void changeTeamAutoStart(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId, InnerCmdType.BAGUA_CHANGE_TEAM_AUTO_START, new Object[]{userRoleId,ChuanQiConfigUtil.getServerId()});
	}
	
	/**
	 * 退出队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_TEAM_LEAVE)
	public void leaveTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		baguaService.leaveTeam(userRoleId);
	}
	
	/**
	 *更改准备状态
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_CHANGE_PREPARE_STATUS)
	public void changePrepareStatus(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId, InnerCmdType.BAGUA_CHANGE_PREPARE_STATUS, new Object[]{userRoleId,ChuanQiConfigUtil.getServerId()});
	}
	
	/**
	 * 队长开始
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_START_TEAM)
	public void startTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId, InnerCmdType.BAGUA_START_TEAM, new Object[]{userRoleId,ChuanQiConfigUtil.getServerId()});
	}
	
//	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_QUICKLY_ADD)
//	public void quicklyAddFubenTeam(Message inMsg){
//		Long userRoleId = inMsg.getRoleId();
//		Integer fubenId = inMsg.getData();
//		Object[] obj = baguaService.quicklyAdd(userRoleId, fubenId);
//		BusMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_QUICKLY_ADD, obj);
//	}
	
	
	/**
	 * 通知BUS业务退出八卦副本	
	 * @param inMsg
	 */
	@TokenCheck(component = GameConstants.COMPONENT_BAGUA_FUBEN_FORCED_LEAVE)
	@EasyMapping(mapping = InnerCmdType.BAGUA_FORCE_EXIT_FUBEN)
	public void forceLeaveFubenKf(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = baguaService.leaveFubenKf(userRoleId);
		if(obj != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.BAGUA_LEAVE_FUBEN, obj);
		}
	}
	
	/**
	 * 倒计时结束
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_AFTER_DJS)
	public void afterDjs(Message inMsg){
		Object[] data = inMsg.getData();
		Integer teamId = (Integer)data[0];
		Integer ceng = (Integer)data[1];
		baguaService.afterDjsKf(teamId,ceng);
	}
	
	/**
	 * 请求退出八卦副本
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_LEAVE_FUBEN,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void leaveFubenKf(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = baguaService.leaveFubenKf(userRoleId);
		if(obj != null){
			KuafuMsgSender.send2One(userRoleId, ClientCmdType.BAGUA_LEAVE_FUBEN, obj);
		}
	}
	
	/**
	 * 领取副本奖励
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_PICK_REWARD)
	public void pickReward(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer fubenId = inMsg.getData();
		Object[] obj = baguaService.pickReward(userRoleId,fubenId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.BAGUA_PICK_REWARD, obj);
	}
	
	/**
	 * 多人副本完成（标记为副本完成状态）
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_FUBEN_FINISH_HANDLE,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void fubenFinishHandle(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer fubenId = inMsg.getData();
		baguaService.fubenFinishHandle(userRoleId,fubenId);
	}
	
	/**
	 * 跨服请求成员数据
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_FUBEN_ASK_ROLE_DATA,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void askRoleData(Message inMsg){
		Long userRoleId = LongUtils.obj2long(inMsg.getData());		
		baguaService.sendRoleData(userRoleId);
	}
	
	/**
	 * 离开队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_LEAVE_TEAM,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void leaveTeamKf(Message inMsg){
		Long userRoleId = LongUtils.obj2long(inMsg.getData());		
		baguaService.leaveTeam1(userRoleId);
	}
	/**
	 * 玩家已进入跨服
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_ENTER_XIAO_HEI_WU,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void enterXiaoheiwu(Message inMsg){
		Long userRoleId = LongUtils.obj2long(inMsg.getData());
		baguaService.enterXiaoheiwu(userRoleId);
	}
	/**
	 * 玩家离开多人副本
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_LEAVE_FUBEN_YF,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void leaveFubenYf(Message inMsg){
		Long userRoleId = inMsg.getRoleId();	
		baguaService.leaveFubenYf(userRoleId);
	}
	/**
	 * 玩家进入多人副本失败
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_ENTER_FUBEN_FAIL,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void enterFubenFail(Message inMsg){
		Long userRoleId = inMsg.getRoleId();	
		baguaService.enterFubenFail(userRoleId);
	}
	
	/**
	 * 多人副本进入队伍成功
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_JOIN_TEAM_SUCCESS,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void joinTeamSuccess(Message inMsg){
		Object[] data = inMsg.getData();
		if(data.length < 4){
			return;
		}
		Long userRoleId = LongUtils.obj2long(data[0]);
		Integer fubenId = (Integer)data[1];
		Integer teamId = (Integer)data[2];
		baguaService.joinTeamSuccess(userRoleId, teamId, fubenId);
	}
	
	/**
	 * 多人副本邀请
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BAGUA_FUBEN_YAOQING_OTHER)
	public void yaoqing(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		baguaService.yaoqing(userRoleId);
	}
	
	/**
	 * 幻境历练异步离线业务
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_FUBEN_OFFLINE_HANDLE_SYN)
	public void offlineHandleSyn(Message inMsg){
		Long userRoleId = LongUtils.obj2long(inMsg.getData());
		baguaService.offlineHandleSyn(userRoleId);
	}
	
	//---------------------跨服模块 TODO--------------------

	/**
	 * 创建队伍(跨服)
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_CREATE_TEAM_KF)
	public void createTeamKf(Message inMsg){
		Object[] data = inMsg.getData();
		Integer belongFubenId = (Integer)data[0];
		Long strength = CovertObjectUtil.obj2long(data[1]);
		Boolean isAuto = (Boolean)data[2];
		Object[] roleData = (Object[])data[3];
		Long userRoleId = LongUtils.obj2long(roleData[0]);
		String serverId = (String)data[4];
		
		Object[] result = baguaService.createTeamKf(belongFubenId,strength,isAuto,userRoleId,roleData);
		if(result[0].equals(AppErrorCode.SUCCESS)){
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.BAGUA_JOIN_TEAM_SUCCESS, new Object[]{userRoleId,result[1],result[2],result[3]});//成功创建队伍
		}else if(result != AppErrorCode.BAGUA_TEAM_EXISTS){//创建失败
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.BAGUA_LEAVE_TEAM, userRoleId);
		}
		
		Object[] msg = new Object[]{ClientCmdType.BAGUA_CREATE_TEAM,result,userRoleId};//封装转发数据
		KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
	}
	

//	/**
//	 * 请求八卦副本队伍信息
//	 * @param inMsg
//	 */
//	@EasyMapping(mapping = InnerCmdType.BAGUA_SELECT_TEAM)
//	public void selectTeamKf(Message inMsg){
//		Object[] data = inMsg.getData();
//		Integer fubenId = (Integer)data[0];
//		Long userRoleId = LongUtils.obj2long(data[1]);
//		
//		Object[] result = baguaService.selectTeamByFubenId(fubenId);
//		
//		Object[] msg = new Object[]{ClientCmdType.BAGUA_SELECT_TEAM,result,userRoleId};//封装转发数据
//		KuafuMsgSender.send2OneKuafuSource((String)data[2], InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
//	}
	
	/**
	 * 请求幻境历练副本队伍信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_FUBEN_OFFLINE_HANDLE)
	public void offlineHandle(Message inMsg){
		Long userRoleId = LongUtils.obj2long(inMsg.getData());
		baguaService.offlineHandleKF(userRoleId);
	}
	
	/**
	 * 多人副本申请加入队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_FUBEN_APPLY_ENTER_TEAM)
	public void joinTeamKf(Message inMsg){
		Object[] data = inMsg.getData();
		Integer teamId = (Integer)data[0];
		Object[] roleData = (Object[])data[1];
		String serverId = (String)data[2];
		Long userRoleId = LongUtils.obj2long(roleData[0]);
		
		Object[] result = baguaService.joinTeamKf(userRoleId, teamId, roleData);
		if(result[0].equals(AppErrorCode.SUCCESS)){//加入失败
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.BAGUA_JOIN_TEAM_SUCCESS, new Object[]{userRoleId,result[3],result[1],result[4]});//成功加入队伍
		}else{
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.BAGUA_LEAVE_TEAM, userRoleId);
		}
		Object[] msg = new Object[]{ClientCmdType.BAGUA_JOIN_TEAM,result,userRoleId};//封装转发数据
		KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
	}
	
	/**
	 * 多人副本退出副本
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_EXIT_FUBEN)
	public void exitFuben(Message inMsg){
		Object[] data = inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String serverId = (String)data[1];
		Object[] result = baguaService.leaveTeamKf(userRoleId);
		if(result != null){
			Object[] msg = new Object[]{ClientCmdType.BAGUA_TEAM_LEAVE,result,userRoleId};
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
	}
	
	/**
	 * 多人副本踢出副本
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_KICK)
	public void kickKf(Message inMsg){
		Object[] data = inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String serverId = (String)data[1];
		Long targetRoleId = LongUtils.obj2long(data[2]);
		
		Object[] result = baguaService.kickKf(userRoleId,targetRoleId);
		Object[] msg = new Object[]{ClientCmdType.BAGUA_KICK,result,userRoleId};
		KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
	}
	
	/**
	 * 队长更改幻境历练副本队伍进入需求战力
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_CHANGE_STRENGTH)
	public void changeStrengthKf(Message inMsg){
		Object[] data = inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String serverId = (String)data[1];
		Long zplus = LongUtils.obj2long(data[2]);
		
		Object[] result = baguaService.changeStrengthKf(userRoleId,zplus);
		if(result != null){
			Object[] msg = new Object[]{ClientCmdType.BAGUA_CHANGE_STRENGTH,result,userRoleId};
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
	}
	
	/**
	 * 队长更改幻境历练副本队伍进入需求战力
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_CHANGE_TEAM_AUTO_START)
	public void changeTeamAutoStartKf(Message inMsg){
		Object[] data = inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String serverId = (String)data[1];
		
		Object[] result = baguaService.changeTeamAutoStartKf(userRoleId);
		if(result != null){
			Object[] msg = new Object[]{ClientCmdType.BAGUA_CHANGE_TEAM_AUTO_START,result,userRoleId};
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
	}
	/**
	 * （副本预进入请求）通知队伍里所有的人在指定时间戳后进入副本(跨服)
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_START_TEAM)
	public void startTeamKf(Message inMsg){
		Object[] data = inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String serverId = (String)data[1];
		Object[] result = baguaService.startTeamKf(userRoleId);
		if(result != null){			
			Object[] msg = new Object[]{ClientCmdType.BAGUA_START_TEAM,result,userRoleId};
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
	}
	
	/**
	 * 队员更改准备状态(跨服)
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_CHANGE_PREPARE_STATUS)
	public void changePrepareStatusKf(Message inMsg){
		Object[] data = inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		String serverId = (String)data[1];
		Object[] obj = baguaService.changePrepareStatusKf(userRoleId);
		if(obj != null){
			Object[] msg = new Object[]{ClientCmdType.BAGUA_CHANGE_PREPARE_STATUS, obj,userRoleId};
			KuafuMsgSender.send2OneKuafuSource(serverId, InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
	}
	
	/**
	 * 接受源服传来的玩家数据(跨服)
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_SEND_ROLE_DATA)
	public void recieveRoleDataKf(Message inMsg){
		Object[] data = inMsg.getData();
		baguaService.recieveRoleDataKf(data);
	}
	
	/**
	 * 玩家正式进入副本场景(跨服)
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_ENTER_FUBEN)
	public void enterFubenKf(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		baguaService.enterFubenKf(userRoleId);
	}
	
	/**
	 * 退出跨服(跨服)
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_EXIT_KUAFU)
	public void exitKuafu(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		baguaService.exitKuafu(userRoleId);
	}
	
	@EasyMapping(mapping = ClientCmdType.BAGUA_FUBEN_ENTER_DOOR)
	public void enterDoor(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer position = inMsg.getData();
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId, InnerCmdType.BAGUA_ENTER_DOOR, new Object[]{userRoleId,position});
	}
	
	/**
	 * 进入门
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.BAGUA_ENTER_DOOR)
	public void enterDoorKf(Message inMsg){
		Object[] data = inMsg.getData();
		Long userRoleId = LongUtils.obj2long(data[0]);
		Integer position = (Integer)data[1];
		String stageId = publicRoleStateExportService.getRolePublicStageId(userRoleId);
		StageMsgSender.send2StageInner(userRoleId, stageId, InnerCmdType.BAGUA_ENTER_DOOR_KF, position);
	}
	/**
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.HANDLE_BAGUA_FUBEN_TIME_OUT_TEAM)
	public void handleTimeOutTeam(Message inMsg){
		baguaService.handleOutTimeTeam();
	}
}
