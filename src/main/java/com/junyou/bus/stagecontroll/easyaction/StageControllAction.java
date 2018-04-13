package com.junyou.bus.stagecontroll.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.stagecontroll.service.StageControllService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

/**
 * @description 场景控制消息入口
 * 控制用户场景流程，并且保存用户登陆状态
 * 1、进入场景(登陆)
 * 2、退出场景
 * 3、切换场景
 * @author DaoZheng Yuan
 * 2014年11月27日 下午2:05:23
 */
@Controller
@EasyWorker(moduleName = GameModType.STAGE_CONTRAL, groupName = EasyGroup.STAGE_CONTROL)
public class StageControllAction {

	@Autowired
	private StageControllService stageControllService;
//	@Autowired
//	private IKuafuBusExportService kuafuBusExportService;
	

	@EasyMapping(mapping = InnerCmdType.S_Change_Stage_cmd)
	public void change(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer mapId = inMsg.getData();
		
		stageControllService.change(userRoleId,mapId);
	}
	
//	
//	/**
//	 * @description 申请切换地图
//	 * @param [roleId,stageId]
//	 * @return 
//	 */
////	@EasyMapping(mapping = StageControllCommands.APPLY_CHANGE_STAGE)
//		public void applyChangeMap(Message inMsg){
//		Long roleId = inMsg.getRoleId();
//		
//		Object result = stageControllService.applyChangeMap(roleId);
////		BusMsgSender.send2One(roleId, StageControllCommands.APPLY_CHANGE_STAGE, result);
//	}
//	
//	/**
//	 * @description 切换场景。操作步骤如下：
//	 * 1、拉去用户场景数据，并保存
//	 * 2、异步通知当前场景用户退出
//	 * 3、异步通知信场景用户进入，并记录用户登陆状况
//	 * 4、同步场景数据到客户端
//	 * 
//	 * @param [roleId,mapId]
//	 */
////	@EasyMapping(mapping = StageControllCommands.CHANGE_STAGE)
//	public void change(Message inMsg){
//		
//		Long roleId = inMsg.getRoleId();
//		String mapId = inMsg.getData();
//		
//		if(KuafuConfigPropUtil.isKuafuServer()){
//			stageControllService.kuafuChangeMap(roleId, mapId);
//		}else{
//			stageControllService.change(roleId,mapId);
//		}
//	}
	
	/**
	 * 内部申请切换场景：
	 * 在场景控制中登记准备切换的地图和坐标信息，并告知客户端可以 切换
	 * 
	 * @param [roleId,stageId,x,y,[mapType],[fubenId],[matchId],[ceng]]
	 */
	@EasyMapping(mapping = InnerCmdType.S_APPLY_CHANGE_STAGE)
	public void applyChange(Message inMsg){
		Object[] data = inMsg.getData();
		Long roleId = inMsg.getRoleId();
        stageControllService.innerApplyChangeMap(roleId, data);
	}
	
	
	@EasyMapping(mapping = InnerCmdType.S_APPLY_LEAVE_STAGE)
	public void applyLeave(Message inMsg){
		
		Long userRoleId = inMsg.getRoleId();
		
		stageControllService.applyLeaveStageMap(userRoleId);
	}
	
	
	
	/**
	 * 在跨服服务器内-申请进入跨服
	 * @param msg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_SC_KUAFU_COPY)
	public void applyEnterKuafu(Message msg){
		Long roleId = msg.getRoleId();
		Object[] data = msg.getData();
		
		Integer mapId = (Integer)data[0];
		int x = (Integer)data[1];
		int y = (Integer)data[2];
		int mapType = (Integer)data[3];
		
		stageControllService.kuafuApplyEnter(roleId, mapId, x, y,mapType);
		
	}
//	
//	
//
	@EasyMapping(mapping = InnerCmdType.INNER_T_LEAVEL_COPY,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void applyLeavelKuafu(Message msg){
		Long roleId = msg.getRoleId();
		
		stageControllService.kuafuApplyLeavel(roleId);
		BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.MORE_FUBEN_LEAVE_TEAM, roleId);
	}
	
	/**
	 * 跨服离线指令(第一步，离开地图)
	 * @param msg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFU_OFFLINE1)
	public void kuafuOffLine1(Message msg){
		Long roleId = msg.getRoleId();
		
		stageControllService.kuafuLogout(roleId);
	}
	
////	@EasyMapping(mapping = StageControllCommands.KUAFU_OFFLINE2)
//	public void kuafuOffLine2(Message msg){
////		String roleId = msg.getRoleId();
//		
////		kuafuBusExportService.leaveKuafu(roleId);
//	}
//	
//	/**
//	 * 进入跨服副本的本地地图
//	 * @param msg
//	 */
////	@EasyMapping(mapping = StageControllCommands.INNER_ENTER_KUAFU_LOCAL_COPY)
//	public void enterKuafuCopyLocalMap(Message msg){
//		Long roleId = msg.getRoleId();
//		
//		Object[] data = msg.getData();
//		String mapId = (String)data[0];
//		
//		stageControllService.enterKuafuLocalMap(roleId, mapId);
//	}
//	
//	
//	/**
//	 * 获取地图所有线路
//	 */
////	@EasyMapping(mapping = StageControllCommands.GET_MAP_LINES)
//	public void getMapLines(Message inMsg){
//		Long roleId = inMsg.getRoleId();
//		
//		Object result = stageControllService.getMapLines(roleId);
////		BusMsgSender.send2One(roleId, StageControllCommands.GET_MAP_LINES, result);
//	}
//	
//	
//	/**
//	 * @description 退出场景。操作步骤如下：
//	 * 1、拉去用户场景数据，并保存
//	 * 2、异步通知当前场景用户退出，并记录用户登陆状况
//	 */
//	@EasyMapping(mapping = InnerCmdType.EXIT_STAGE)
//	public void exit(Message inMsg){
//		Long roleId = inMsg.getRoleId();
//		stageControllService.exit(roleId);
//	}
//	
}