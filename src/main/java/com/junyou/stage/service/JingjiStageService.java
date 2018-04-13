package com.junyou.stage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.stagecontroll.MapType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.stage.tunnel.StageMsgSender;
/**
 * 竞技场景业务
 * @author LiuYu
 * @date 2015-3-25 上午10:44:14
 */
@Service
public class JingjiStageService {
	
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	
	public void enterSafeMap(Long userRoleId){
		DiTuConfig config = diTuConfigExportService.loadSafeDiTu();
		int[] birthXy = config.getRandomBirth();
		Object[] applyEnterData = new Object[]{config.getId(),birthXy[0],birthXy[1], MapType.JINGJI_SAFE_MAP};
		
		StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
	}
	
	public void enterSafeMapType(Long userRoleId,Integer mapType){
		DiTuConfig config = diTuConfigExportService.loadSafeDiTu();
		int[] birthXy = config.getRandomBirth();
		Object[] applyEnterData = new Object[]{config.getId(),birthXy[0],birthXy[1], mapType};
		
		StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
	}
	
}
