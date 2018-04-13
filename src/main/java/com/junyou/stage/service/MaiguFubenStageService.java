package com.junyou.stage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.MaiguFbStage;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * 多人副本场景Service
 * 
 * @author chenjianye
 * @date 2015-04-29
 */
@Service
public class MaiguFubenStageService {
	/**
	 * 主动退出多人副本
	 * 
	 * @param stageId
	 */
	public void selfLeaveFuben(String stageId, Long userRoleId) {
		MaiguFbStage maiguStage = (MaiguFbStage) StageManager.getStage(stageId);
		if (maiguStage == null) {
			return;
		}
		IRole role = maiguStage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		// 场景结束
		maiguStage.leave(role);
		// baguaStage.noticeClientExit(userRoleId);

		List<IRole> roles = maiguStage.getChallengers();
		if (roles == null || roles.isEmpty()) {
			// 场景无人，立马回收场景
			StageManager.removeCopy(maiguStage);
		}

		KuafuMsgSender.send2KuafuSource(userRoleId,
				InnerCmdType.MAIGU_LEAVE_FUBEN_YF, null);
		StageMsgSender
				.send2Bus(userRoleId, InnerCmdType.MAIGU_EXIT_KUAFU, null);

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
	public void challengeOver(String stageId, int result) {
		MaiguFbStage fubenStage = (MaiguFbStage) StageManager.getStage(stageId);

		if (fubenStage == null) {
			return;
		}

		// 通关
		fubenStage.tongGuanHandle();

		List<IRole> roles = fubenStage.getChallengers();
		for (IRole role : roles) {
			KuafuMsgSender.send2KuafuSource(role.getId(),
					ClientCmdType.MAIGU_RESULT_TIP, result);
			if(result==1){
				KuafuMsgSender.send2One(role.getId(),
						InnerCmdType.MAIGU_FUBEN_FINISH_HANDLE,
						fubenStage.getFubenId());
			}
		}
	}

	/**
	 * 获取多人副本伤害输出
	 * 
	 * @param stageId
	 * @return
	 */
	public Object[] getMaiguFubenShanghaiConsole(String stageId) {
		MaiguFbStage fubenStage = (MaiguFbStage) StageManager.getStage(stageId);

		if (fubenStage == null) {
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}

		return null;
	}
}
