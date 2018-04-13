package com.junyou.stage.kuafu.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.stagecontroll.MapType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.StageMsgSender;

@Service
public class KuafuStageService {
	
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	/**
	 * 申请进入跨服
	 * @param userRoleId
	 * @param stageId
	 * @param activeId
	 */
	public void applyEnterKF(Long userRoleId,String stageId,Integer activeId, Object... toKuafuData){
		if(!KuafuConfigUtil.isKuafuAvailable()){
			//TODO 跨服尚未连通
			return;
		}
		if(!KuafuConfigUtil.isActiveKfing(activeId)){
			//TODO 跨服活动尚未开启
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			//TODO 场景不存在
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			//TODO 角色不存在
			return;
		}
		if(role.isChanging()){
			return;
		}
		
		Object roleData = RoleFactory.createKuaFuRoleData(role);
		List<Object> kuafuNeedData = new ArrayList<>();
		kuafuNeedData.add(roleData);
		kuafuNeedData.add(activeId);
		for (Object object : toKuafuData) {
			kuafuNeedData.add(object);
		}
		
		KuafuManager.startKuafu(userRoleId);
		KuafuMsgSender.send2KuafuServer(userRoleId,userRoleId, InnerCmdType.INNER_KUAFU_ENTER, kuafuNeedData.toArray());
		
		// 发送到场景控制中心进入小黑屋地图，以便从跨服服务器出来时走完整流程
		DiTuConfig config = diTuConfigExportService.loadSafeDiTu();
		int[] birthXy = config.getRandomBirth();
		Object[] applyEnterData = new Object[]{config.getId(),birthXy[0],birthXy[1], MapType.KUAFU_SAFE_MAP};
		//传送前加一个无敌状态
		role.getStateManager().add(new NoBeiAttack());
		role.setChanging(true);
		StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
	}
	
}
