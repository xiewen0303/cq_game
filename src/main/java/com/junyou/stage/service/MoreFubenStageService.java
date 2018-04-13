package com.junyou.stage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.MoreFbStage;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * 多人副本场景Service
 * 
 * @author chenjianye
 * @date 2015-04-29
 */
@Service
public class MoreFubenStageService {
	/**
	 * 退出多人副本
	 * 
	 * @param stageId
	 */
	public void exitFuben(String stageId, Long userRoleId) {
		MoreFbStage moreStage = (MoreFbStage) StageManager.getStage(stageId);
		if (moreStage == null) {
			return;
		}
		IRole role = moreStage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		// 场景结束
		moreStage.leave(role);

		List<IRole> roles = moreStage.getChallengers();
		if (roles == null || roles.isEmpty()) {
			// 场景无人，立马回收场景
			StageManager.removeCopy(moreStage);
		}

		KuafuMsgSender.send2KuafuSource(userRoleId,
				InnerCmdType.MORE_FUBEN_LEAVE_FUBEN, null);
		StageMsgSender.send2Bus(userRoleId,
				InnerCmdType.MORE_FUBEN_LEAVE_FUBEN_KUAFU, null);

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
		MoreFbStage fubenStage = (MoreFbStage) StageManager.getStage(stageId);

		if (fubenStage == null) {
			return;
		}

		// 通关
		fubenStage.tongGuanHandle();

		List<IRole> roles = fubenStage.getChallengers();
		for (IRole role : roles) {
			KuafuMsgSender.send2One(role.getId(),
					InnerCmdType.S_MORE_FUBEN_FINISH, fubenStage.getFubenId());
		}
	}

	/**
	 * 获取多人副本伤害输出
	 * 
	 * @param stageId
	 * @return
	 */
	public Object[] getMoreFubenShanghaiConsole(String stageId) {
		MoreFbStage fubenStage = (MoreFbStage) StageManager.getStage(stageId);

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

	public void startSynSourceServer(Long userRoleId, String stageId) {
		MoreFbStage fubenStage = (MoreFbStage) StageManager.getStage(stageId);
		if(fubenStage==null){
			return;
		}
		IRole role = fubenStage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		KuafuMsgSender.send2KuafuSource(userRoleId,
				InnerCmdType.MORE_FUBEN_SYNC, null);
		fubenStage.startSyncToSourceServer(userRoleId);
	}
	
	public void removeRoleFromKuafu(Long userRoleId, String stageId){
		MoreFbStage moreStage = (MoreFbStage) StageManager.getStage(stageId);
		if(moreStage==null){
			return;
		}
		IRole role = moreStage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		// 场景结束
		moreStage.leave(role);

		List<IRole> roles = moreStage.getChallengers();
		if (roles == null || roles.isEmpty()) {
			// 场景无人，立马回收场景
			StageManager.removeCopy(moreStage);
		}

		StageMsgSender.send2Bus(userRoleId,
				InnerCmdType.MORE_FUBEN_LEAVE_FUBEN_KUAFU, null);
	}
}
