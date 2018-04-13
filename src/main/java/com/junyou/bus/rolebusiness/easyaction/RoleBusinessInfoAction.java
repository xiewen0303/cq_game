package com.junyou.bus.rolebusiness.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.rolebusiness.service.RoleBusinessInfoService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

/**
 * 角色业务数据Action
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-13 下午3:12:20 
 */
@Component
@EasyWorker(moduleName = GameModType.ROLE_BUS_MODULE)
public class RoleBusinessInfoAction {
	@Autowired
	private RoleBusinessInfoService roleBusinessInfoService;
	
	// ---------------------PK---------------------------
	@EasyMapping(mapping = InnerCmdType.ROLE_HUIMING,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void pkHuiMing(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		roleBusinessInfoService.rolePkAddHm(userRoleId);
	}
	@EasyMapping(mapping = InnerCmdType.ROLE_PK)
	public void rolePk(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		roleBusinessInfoService.rolePkAdd(userRoleId);
	}
	
	@TokenCheck(component = GameConstants.COMPONENET_HM)
	@EasyMapping(mapping = InnerCmdType.HONGMING_DERC)
	public void quertzDecreaseHongMing(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		roleBusinessInfoService.quertzDecreasePkValue(userRoleId);
	}
	
	@TokenCheck(component = GameConstants.COMPONENET_HUIM)
	@EasyMapping(mapping = InnerCmdType.HUI_XS)
	public void quertzDissolveHuiMing(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		roleBusinessInfoService.quertzDissolveHuiMing(userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.ZPLUS_CHANGE_SAVE,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void zplusChangeSave(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long zplus = LongUtils.obj2long(inMsg.getData());
		
		roleBusinessInfoService.roleSaveFighter(userRoleId, zplus);
	}
	
	
}
