package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.JingjiStageService;
import com.kernel.pool.executor.Message;
/**
 * 竞技场景
 * @author LiuYu
 * @date 2015-3-25 上午10:42:48
 */
@Controller
@EasyWorker(groupName = EasyGroup.STAGE, moduleName = GameModType.JINGJI_MODULE)
public class JingjiStageAction {
	@Autowired
	private JingjiStageService jingjiStageService;
	
	@EasyMapping(mapping = InnerCmdType.ENTER_SAFE_MAP)
	public void enterSafeMap(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		jingjiStageService.enterSafeMap(userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.ENTER_SAFE_MAP_KF)
	public void enterSafeMapType(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer mapType = inMsg.getData();
		jingjiStageService.enterSafeMapType(userRoleId,mapType);
	}
}
