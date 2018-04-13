package com.junyou.stage.service;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.StageErrorCode;
import com.junyou.stage.StageOutputWrapper;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.state.NoAttack;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.StageMsgQueue;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.kernel.spring.container.DataContainer;

@Service
public class GmService{
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private DataContainer dataContainer;
//	@Autowired
//	private IRoleExportService roleExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
//	@Autowired
//	private IJinYanExportService jinYanExportService;
	
	public boolean gmYinshen(Long userRoleId, boolean xianshi, String stageId) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return true;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return true;
		}
		
		if(!role.isGm()){//不是GM不能隐身
			return true;
		}
		Long[] notifyRoleIds = role.getStage().getNoSelfSurroundRoleIds(role.getPosition(),userRoleId);
		if(xianshi && role.isYinshen()){//GM现身业务
			role.setYinshen(0);
			//设置GM隐身标记，表示GM已现身
			dataContainer.putData(GameConstants.COMPONENT_GM_HANLE_YS, userRoleId.toString(), true);
			
			if(notifyRoleIds != null){
				//推送创建role
				StageMsgSender.send2Many(notifyRoleIds, ClientCmdType.AOI_ROLES_ENTER, new Object[]{role.getMsgData()});
			}
			
			//TODO GM日志
//			printGmLog(userRoleId,LogChuanQiUtil.GM_OPERATION_XIANSHEN,null,stage.getMapId(),role.getPosition(),null,null);
		}else if(!xianshi && !role.isYinshen()){//GM隐身业务
			role.setYinshen(1);
			//移除GM隐身标记
			dataContainer.removeData(GameConstants.COMPONENT_GM_HANLE_YS, userRoleId.toString());
			if(notifyRoleIds != null){
				//推送删除role
//				StageMsgSender.send2Many(notifyRoleIds, StageCommands.AOI_ROLES_LEAVE, new Object[]{userRoleId});
			}
			
			//TODO GM日志
//			printGmLog(userRoleId,LogChuanQiUtil.GM_OPERATION_YINSHEN,null,stage.getMapId(),role.getPosition(),null,null);
		}
		
		return xianshi;
	}
	/**
	 * 打印日志
	 */
	private void printGmLog(Long userRoleId,int sort,Long targerRoleId,String fromMap,Point fromPoint,String targerMap,Point targerPoint){
		try{
			String spiltStr = "#";
			String left = "[";
			String right = "]";
//			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
//			RoleWrapper targerRole = null; 
//			if(targerRoleId != null){
//				if(publicRoleStateExportService.isPublicOnline(targerRoleId)){
//					targerRole = roleExportService.getLoginRole(targerRoleId);
//				}else{
//					targerRole = roleExportService.getUserRoleFromDb(targerRoleId);
//				}
//			}
			
			StringBuffer remark = new StringBuffer();
			if(fromMap != null && fromPoint != null){
				remark.append(fromMap);
				remark.append(spiltStr);
				remark.append(left);
				remark.append(fromPoint.getX()).append(",").append(fromPoint.getY());
				remark.append(right);
			}
			
			if(targerMap != null && targerPoint != null){
				if(remark.length() > 0){
					remark.append(spiltStr);
				}
				remark.append(targerMap);
				remark.append(spiltStr);
				remark.append(left);
				remark.append(targerPoint.getX()).append(",").append(targerPoint.getY());
				remark.append(right);
			}
//			String targer = null;
//			if(targerRole != null){
//				targer = targerRole.getId() + spiltStr + targerRole.getName();
//			}
//			LogChuanQiUtil.gmOperationLog(role, sort, targer, remark.toString());
		}catch (Exception e) {
			ChuanQiLog.error("", e);
		}
	}

	public Object gmTeleportMap(Long userRoleId, String stageId, Integer mapId) {

		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_MOVE_TO_MAP, new Object[]{0,StageErrorCode.NO_GM_QUANXIAN});
			return null;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(!role.isGm()){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_MOVE_TO_MAP, new Object[]{0,StageErrorCode.NO_GM_QUANXIAN});
			return null;
		}
		
		DiTuConfig config = diTuConfigExportService.loadDiTu(mapId);
		if(config == null){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_MOVE_TO_MAP, new Object[]{0,StageErrorCode.TELEPORT_ERROR});
			return null;
		}
		
		Object errorMsg = null;
		if(null != (errorMsg = commonLimit(stageId,userRoleId))){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_MOVE_TO_MAP, errorMsg);
			return null;
		}
		
		//在本地图不传送
		if(config.getId() == stage.getMapId()){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_MOVE_TO_MAP, new Object[]{0,StageErrorCode.IN_CUR_MAP});
			return null;
		}
		
		//地图传送配置验证 
		if(!config.isGmMove()){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_MOVE_TO_MAP, new Object[]{0,StageErrorCode.GM_CANNOT_TELEPORT_THIS_MAP});
			return null;
		}
		
		int[] birthXy = config.getRandomBirth();
		int x = birthXy[0];
		int y = birthXy[1];
		
		//TODO GM日志
//		printGmLog(userRoleId, LogChuanQiUtil.GM_OPERATION_MOVE, null, stage.getMapId(), role.getPosition(), mapId, new Point(x, y));
		
		return new Object[]{mapId,x,y};
	}
	
	/**
	 * 传送基本限制
	 * @return NULL:允许
	 */
	private Object commonLimit(String stageId, Long roleId) {
		
		IStage stage = StageManager.getStage(stageId);
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		
		//死亡状态不允许传送
		if(role.getStateManager().isDead()){
			return new Object[]{0,StageErrorCode.TELEPORT_IN_DEAD_ERROR};
		}
		/*//摆摊状态不能传送
		if(role.getBusinessData().isBooth()){
			return new Object[]{0,StageErrorCode.IN_BOOTH};
		}*/
		
		return null;
	}

	public Object gmTeleportMap(Long userRoleId, String stageId,Object[] point) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_MOVE_TO_THIS_MAP_OTHER, new Object[]{0,StageErrorCode.NO_GM_QUANXIAN});
			return null;
		}
		
		Integer mapId = stage.getMapId();
		
		Object result = checkMovePoint(point,mapId);
		if(result != null){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_MOVE_TO_THIS_MAP_OTHER, result);
			return null;
		}
		
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(!role.isGm()){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_MOVE_TO_THIS_MAP_OTHER, new Object[]{0,StageErrorCode.NO_GM_QUANXIAN});
			return null;
		}
		
		Object errorMsg = null;
		if(null != (errorMsg = commonLimit(stageId,userRoleId))){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_MOVE_TO_THIS_MAP_OTHER, errorMsg);
			return null;
		}
		
		Integer x = (Integer)point[0];
		Integer y = (Integer)point[1];
		
		//TODO GM日志
//		printGmLog(userRoleId, LogChuanQiUtil.GM_OPERATION_MOVE, null, stage.getMapId(), role.getPosition(), mapId, new Point(x, y));
		
		return new Object[]{mapId,x,y};
	}
	
	/**
	 * 验证传送的坐标点是否合法
	 * @param point
	 * @param mapId
	 * @return
	 */
	private Object checkMovePoint(Object[] point,Integer mapId){
		if(point.length == 2){
			Integer x = (Integer)point[0];
			Integer y = (Integer)point[1];
			if(x >= 0 && y >= 0 ){
				MapConfig mapConfig = StageConfigureHelper.getMapExportService().load(mapId);
				if(x <= mapConfig.getPathInfoConfig().getXsWidth() && y <= mapConfig.getPathInfoConfig().getHeight()){
					return null;
				}
			}
		}
		return new Object[]{0,StageErrorCode.GM_TELEPORT_POINT_ERROR};
	}

	public Object gmSelectOtherRole(String userRoleId, String roleName) {
//		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
//		if(role == null){
//			return null;
//		}
//		if(!role.isGm()){
//			return null;
//		}
//		List<String> userRoleIds = roleStateExportService.getAllOnlineRoleidList(roleName, GmHandleConstants.GM_ONCE_SELECT_LIMIT);
//		if(userRoleIds.size() > 0){
//			
//			List<RoleWrapper> roleList = new ArrayList<RoleWrapper>();
//			for (String roleId : userRoleIds) {
//				RoleWrapper other_role = roleExportService.getLoginRole(roleId);
//				if(other_role != null){
//					roleList.add(other_role);
//				}
//			}
//			
//			if(roleList.size() > 0){
//				Object[] result = new Object[roleList.size()];
//				for (int i = 0 ;i <roleList.size(); i++) {
//					RoleWrapper other_role = roleList.get(i);
//					result[i] = new Object[]{other_role.getConfigId(),other_role.getId(),other_role.getName()};
//				}
//				//打印日志
//				printGmSelectLog(role,roleName,roleList);
//				return result;
//			}
//		}
		return null;
	}

	public void gmTeleportMapToOtherRole(Long userRoleId, String stageId,Long targerRoleId,StageMsgQueue stageMsgQueue) {
		if(!publicRoleStateExportService.isPublicOnline(targerRoleId)){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_MOVE_TO_OTHER_PLAYER, new Object[]{0,StageErrorCode.GM_TELEPORT_TARGER_ROLE_NOT_ONLINE});
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
//			stageMsgQueue.addMsg(userRoleId, GmHandleCommands.GM_MOVE_TO_OTHER_PLAYER, new Object[]{0,StageErrorCode.GM_TELEPORT_STAGE_ERROR});
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(!role.isGm()){
//			stageMsgQueue.addMsg(userRoleId, GmHandleCommands.GM_MOVE_TO_OTHER_PLAYER, new Object[]{0,StageErrorCode.NO_GM_QUANXIAN});
			return;
		}
		
		String targerStageId = publicRoleStateExportService.getRolePublicStageId(targerRoleId);
		if(targerStageId == null){
//			stageMsgQueue.addMsg(userRoleId, GmHandleCommands.GM_MOVE_TO_OTHER_PLAYER, new Object[]{0,StageErrorCode.GM_TELEPORT_STAGE_ERROR});
			return;
		}
		
		IStage targerStage = StageManager.getStage(targerStageId);
		if(targerStage == null){
//			stageMsgQueue.addMsg(userRoleId, GmHandleCommands.GM_MOVE_TO_OTHER_PLAYER, new Object[]{0,StageErrorCode.GM_TELEPORT_STAGE_ERROR});
			return;
		}
		
		IRole targerRole = targerStage.getElement(targerRoleId, ElementType.ROLE);
		if(targerRole == null){
//			stageMsgQueue.addMsg(userRoleId, GmHandleCommands.GM_MOVE_TO_OTHER_PLAYER, new Object[]{0,StageErrorCode.GM_TELEPORT_TARGER_ROLE_ERROR});
			return;
		}
		Integer x = targerRole.getPosition().getX();
		Integer y = targerRole.getPosition().getY();

		Integer mapId = targerStage.getMapId();
		DiTuConfig config = diTuConfigExportService.loadDiTu(mapId);
		if(config == null){
//			stageMsgQueue.addMsg(userRoleId, GmHandleCommands.GM_MOVE_TO_OTHER_PLAYER, new Object[]{0,StageErrorCode.TELEPORT_ERROR});
			return;
		}
		
		Object errorMsg = null;
		if(null != (errorMsg = commonLimit(stageId,userRoleId))){
//			stageMsgQueue.addMsg(userRoleId, GmHandleCommands.GM_MOVE_TO_OTHER_PLAYER, errorMsg);
			return;
		}
		//GM无法传送到玩家所在副本
		if(targerStage.isCopy()){
//			stageMsgQueue.addMsg(userRoleId, GmHandleCommands.GM_MOVE_TO_OTHER_PLAYER, new Object[]{0,StageErrorCode.GM_CANNOT_TELEPORT_TO_PLAYER_COPY});
			return;
		}
		
		//地图传送配置验证 
		if(!config.isGmMove()){
//			stageMsgQueue.addMsg(userRoleId, GmHandleCommands.GM_MOVE_TO_OTHER_PLAYER, new Object[]{0,StageErrorCode.GM_CANNOT_TELEPORT_THIS_MAP});
			return;
		}
		
//		stageMsgQueue.addStageControllMsg(userRoleId, StageTeleportCommands.INNER_APPLY_CHANGE_MAP, new Object[]{mapId,x,y,null});
		
		//TODO GM日志
//		printGmLog(userRoleId, LogChuanQiUtil.GM_OPERATION_MOVE, targerRole.getId(), stage.getMapId(), role.getPosition(), mapId, targerRole.getPosition());
	}
	public void changeGm(Long userRoleId, String stageId, int isGm) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		boolean roleIsGm = isGm == GameConstants.IS_GM;
		if(role.isGm() == roleIsGm){//GM权限无变更
			return;
		}
		Long[] notifyRoleIds = role.getStage().getNoSelfSurroundRoleIds(role.getPosition(),userRoleId);
		role.setIsGm(isGm);
//		RoleWrapper loginRole = roleExportService.getLoginRole(userRoleId);
//		PublicMsgSender.send2One(userRoleId, GmHandleCommands.GM_GET_OR_LOSE, loginRole.getGmType());
		if(roleIsGm){
			role.setYinshen(1);
			role.getStateManager().add(new NoBeiAttack());//无法被攻击
			role.getStateManager().add(new NoAttack());//无法攻击
			if(notifyRoleIds != null){
				//推送删除role
//				StageMsgSender.send2Many(notifyRoleIds, StageCommands.AOI_ROLES_LEAVE, new Object[]{userRoleId});
			}
		}else{
			role.setYinshen(0);
			role.getStateManager().remove(StateType.NO_ATTACKED);//无法被攻击
			role.getStateManager().remove(StateType.NO_ATTACK);//无法攻击
			dataContainer.removeData(GameConstants.COMPONENT_GM_HANLE_YS, userRoleId.toString());
			if(notifyRoleIds != null){
				//推送创建role
				StageMsgSender.send2Many(notifyRoleIds, ClientCmdType.AOI_ROLES_ENTER, new Object[]{role.getMsgData()});
			}
		}
		
	}
	public Object gmFenghao(Long userRoleId, Long targerRoleId,String reasons) {
		if(reasons == null || "".equals(reasons)){
			return StageOutputWrapper.gmOperationFail(StageErrorCode.REASONS_IS_NULL);
		}
		
//		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
//		if(role == null){
//			return null;
//		}
//		if(!role.isGm()){
//			return StageOutputWrapper.gmOperationFail(StageErrorCode.NO_GM_QUANXIAN);
//		}
		
//		RoleWrapper targerRole = null;
//		if(publicRoleStateExportService.isPublicOnline(targerRoleId)){
//			targerRole = roleExportService.getLoginRole(targerRoleId);
//		}else{
//			targerRole = roleExportService.getUserRoleFromDb(targerRoleId);
//		}
		
//		if(targerRole == null){
//			return StageOutputWrapper.gmOperationFail(StageErrorCode.TARGET_ROLE_IS_NULL);
//		}
//		if(targerRole.getIsGm() == 1){
//			return StageOutputWrapper.gmOperationFail(StageErrorCode.TARGET_ROLE_IS_GM);
//		}
		
//		String result = jinYanExportService.freeze(targerRole.getUserId(), targerRole.getServerId(), JinYanErrorCode.OTHER_SORT, reasons);
//		if(result != null){
//			return StageOutputWrapper.gmOperationFail(StageErrorCode.TARGET_ROLE_IS_NULL);
//		}
		
		//异步通知后台
//		new Thread(new NotifyGmTool(0,targerRole.getUserId(),targerRole.getServerId(),reasons,role.getServerId() + "_" + role.getName())).start();
				
		//TODO GM日志
//		printGmLog(userRoleId, LogChuanQiUtil.GM_OPERATION_FENGHAO, targerRoleId, null, null, null, null);
		
		return new Object[]{1};
	}
	public Object gmJinyan(Long userRoleId, Long targerRoleId,String reasons) {
		if(reasons == null || "".equals(reasons)){
			return StageOutputWrapper.gmOperationFail(StageErrorCode.REASONS_IS_NULL);
		}
		
//		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
//		if(role == null){
//			return null;
//		}
//		if(!role.isGm()){
//			return StageOutputWrapper.gmOperationFail(StageErrorCode.NO_GM_QUANXIAN);
//		}
//		
//		RoleWrapper targerRole = null;
//		if(publicRoleStateExportService.isPublicOnline(targerRoleId)){
//			targerRole = roleExportService.getLoginRole(targerRoleId);
//		}else{
//			targerRole = roleExportService.getUserRoleFromDb(targerRoleId);
//		}
		
//		if(targerRole == null){
//			return StageOutputWrapper.gmOperationFail(StageErrorCode.TARGET_ROLE_IS_NULL);
//		}
//		if(targerRole.getIsGm() == 1){
//			return StageOutputWrapper.gmOperationFail(StageErrorCode.TARGET_ROLE_IS_GM);
//		}
//		
//		Object result = jinYanExportService.shutUp(targerRole.getUserId(),targerRole.getServerId());
//		if(result != null){
//			return StageOutputWrapper.gmOperationFail(StageErrorCode.TARGET_ROLE_IS_NULL);
//		}
		
		//异步通知后台
//		new Thread(new NotifyGmTool(1,targerRole.getUserId(),targerRole.getServerId(),reasons,role.getServerId() + "_" + role.getName())).start();
		
		//TODO GM日志
//		printGmLog(userRoleId, LogChuanQiUtil.GM_OPERATION_JINYAN, targerRoleId, null, null, null, null);
		
		return new Object[]{1};
	}
	
	class NotifyGmTool implements Runnable{
		private int sort;
		private String userId;
		private String serverId;
		private String reasons;
		private String operator;
		
		public NotifyGmTool(int sort,String userId,String serverId,String reasons,String operator){
			this.sort = sort;
			this.userId = userId;
			this.serverId = serverId;
			try {
				this.reasons = URLEncoder.encode(reasons, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			this.operator = operator;
			
		}
		public void run() {
			StringBuffer buffer = new StringBuffer();
			buffer.append(ChuanQiConfigUtil.getManageToolUrl())
			.append("gm_fengjin.do?userId=")
			.append(userId)
			.append("&forbidType=")
			.append(sort)
			.append("&serverId=")
			.append(serverId)
			.append("&reason=")
			.append(reasons)
			.append("&operator=")
			.append(operator);
			try{
				URL url = new URL(buffer.toString());
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setConnectTimeout(10000);
				connection.setReadTimeout(15000);
				connection.connect();
				connection.getInputStream().close();
				connection.disconnect();
			}catch (Exception e) {
				ChuanQiLog.error("", e);
			}
		}
		
	}
}
