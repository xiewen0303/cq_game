package com.junyou.stage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bagua.configure.export.BaguaZhenMenConfig;
import com.junyou.bus.bagua.configure.export.BaguaZhenMenExportService;
import com.junyou.bus.bagua.configure.export.DuoRenTongYongBiaoConfig;
import com.junyou.bus.bagua.configure.export.DuoRenTongYongBiaoExportService;
import com.junyou.bus.bagua.constants.BaguaConstant;
import com.junyou.bus.fuben.entity.BaguaFubenTeam;
import com.junyou.bus.fuben.entity.BaguaFubenTeamManage;
import com.junyou.bus.fuben.entity.BaguaFubenTeamMember;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.BaguaPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.BaguaFbStage;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * 多人副本场景Service
 * 
 * @author chenjianye
 * @date 2015-04-29
 */
@Service
public class BaguaFubenStageService {

	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private BaguaZhenMenExportService baguaZhenMenExportService;
	@Autowired
	private DuoRenTongYongBiaoExportService duoRenTongYongBiaoExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;

	private BaguaPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_BAGUA);
	}

	/**
	 * 主动退出多人副本
	 * 
	 * @param stageId
	 */
	public void selfLeaveFuben(String stageId, Long userRoleId) {
		BaguaFbStage baguaStage = (BaguaFbStage) StageManager.getStage(stageId);
		if (baguaStage == null) {
			return;
		}
		IRole role = baguaStage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		// 场景结束
		baguaStage.leave(role);
//		baguaStage.noticeClientExit(userRoleId);

		List<IRole> roles = baguaStage.getChallengers();
		if (roles == null || roles.isEmpty()) {
			// 场景无人，立马回收场景
			StageManager.removeCopy(baguaStage);
		}

		KuafuMsgSender.send2KuafuSource(userRoleId,
				InnerCmdType.BAGUA_LEAVE_FUBEN_YF, null);
		StageMsgSender.send2Bus(userRoleId,
				InnerCmdType.BAGUA_EXIT_KUAFU, null);

		// 定时强制退出副本操作
		// StageMsgSender.send2StageControl(role.getId(),
		// InnerCmdType.S_APPLY_LEAVE_STAGE, role.getId());
	}

	/**
	 * 完成副本清场
	 * 
	 * @param stageId
	 * @param type
	 */
	public void challengeOver(String stageId) {
		BaguaFbStage fubenStage = (BaguaFbStage) StageManager.getStage(stageId);

		if (fubenStage == null) {
			return;
		}

		// 通关
		fubenStage.tongGuanHandle();

		List<IRole> roles = fubenStage.getChallengers();
		for (IRole role : roles) {
			KuafuMsgSender.send2KuafuSource(role.getId(), ClientCmdType.BAGUA_RESULT_TIP, null);
			KuafuMsgSender.send2One(role.getId(),
					InnerCmdType.BAGUA_FUBEN_FINISH_HANDLE, fubenStage.getFubenId());
		}
	}

	/**
	 * 获取多人副本伤害输出
	 * 
	 * @param stageId
	 * @return
	 */
	public Object[] getBaguaFubenShanghaiConsole(String stageId) {
		BaguaFbStage fubenStage = (BaguaFbStage) StageManager.getStage(stageId);

		if (fubenStage == null) {
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}

		// Map<Long,Integer> hurts = fubenStage.getHurts();
		// if(!hurts.isEmpty()){
		// List<Object[]> objs = new ArrayList();
		// for(Entry<Long,Integer> entry : hurts.entrySet()){
		// objs.add(new Object[]{entry.getKey(),entry.getValue()});
		// }
		// return new Object[]{objs.toArray()};
		// }

		return null;
	}

	public void enterDoor(String stageId, Long userRoleId, Integer position) {
		if (position < 0 || position > 7) {
			return;
		}
		BaguaFbStage baguaStage = (BaguaFbStage) StageManager.getStage(stageId);
		if (baguaStage == null) {
			return;
		}
		IRole role = baguaStage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		int ceng = baguaStage.getCeng();
		BaguaPublicConfig publicConfig = getPublicConfig();
		int maxCeng = publicConfig.getCeng();
		if (ceng == maxCeng) {
			return;
		}
		Integer teamId = BaguaFubenTeamManage.getTeamId(userRoleId);
		if (teamId == null) {
			return;
		}
		BaguaFubenTeam team = BaguaFubenTeamManage.getTeamByTeamID(teamId);
		if (team == null) {
			return;
		}
		String name  = team.getMember(userRoleId).getMemberName();
		boolean isOpen = team.isZhenMenOpen(ceng, position);
		int zhenmenId = team.getZhenmenId(ceng, position);
		BaguaZhenMenConfig baguaZhenMenConfig = baguaZhenMenExportService
				.loadByOrder(zhenmenId);
		int chuan = baguaZhenMenConfig.getChuan();
		int nextCeng = ceng + chuan;
		if (nextCeng < 1) {
			nextCeng = 1;
		}
		if (nextCeng > 8) {
			nextCeng = 8;
		}
		// 场景结束
//		baguaStage.leave(role);

//		List<IRole> roles = baguaStage.getChallengers();
//		if (roles == null || roles.isEmpty()) {
//			// 场景无人，立马回收场景
//			StageManager.removeCopy(baguaStage);
//		}

		if(nextCeng!=ceng){
			DuoRenTongYongBiaoConfig config = duoRenTongYongBiaoExportService
					.loadByOrder(BaguaConstant.BUAGUA_FUBEN,
							team.getBelongFubenId());
			if (config == null) {
				return;
			}
			// 发送到场景进入地图
			DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config
					.getMapId());
			int[] zuobiao = null;
			if (ceng < 8) {
				zuobiao = publicConfig.getZuobiao();
			} else {
				zuobiao = publicConfig.getFuhuo();
			}
			Object[] applyEnterData = new Object[] { dituCoinfig.getId(),
					zuobiao[0], zuobiao[1], MapType.BAGUA_FUBEN_MAP,
					team.getBelongFubenId(), team.getTeamId(), null, nextCeng };
			StageMsgSender.send2StageControl(role.getId(),
					InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		}
		// 将此门的状态标记为已打开
		team.openZhenmen(ceng, position);
		KuafuMsgSender.send2KuafuSource(
				userRoleId,
				ClientCmdType.BAGUA_FUBEN_ENTER_DOOR,
				new Object[] {
						AppErrorCode.SUCCESS,
						nextCeng,
						team.getOpenDoorInfo(nextCeng,
								baguaZhenMenExportService) });
		
		List<IRole> roles = baguaStage.getChallengers();
		for (IRole e : roles) {
			if (!e.getId().equals(userRoleId)) {
				KuafuMsgSender.send2KuafuSource(
						e.getId(),
						ClientCmdType.BAGUA_FUBEN_DOOR_NOTICE,
						new Object[] {
								ceng,
								team.getOpenDoorInfo(ceng,
										baguaZhenMenExportService) });
			}
		}
		List<BaguaFubenTeamMember> members = team.getMembers();
		if(zhenmenId == 1 && isOpen==false){
			for (BaguaFubenTeamMember e : members) {
				KuafuMsgSender.send2KuafuSource(
						e.getRoleId(),
						ClientCmdType.BAGUA_NOTICE_ENTER_SHENGMEN,
						new Object[] {
							name,
								ceng });
			}	
		}
	}
}
