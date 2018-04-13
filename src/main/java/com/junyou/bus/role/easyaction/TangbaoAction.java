package com.junyou.bus.role.easyaction;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.role.service.TangbaoService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-5-11 下午7:51:04
 */
@Controller
@EasyWorker(moduleName = GameModType.ROLE_BUS_MODULE)
public class TangbaoAction {
	
	@Autowired
	private TangbaoService tangbaoService;
	
	/**
	 * 请求糖宝面板属性
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TANGBAO_ATTRIBUTE)
	public void tangbaoAttribute(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Map<Integer,Long> map = tangbaoService.getTangbaoAttribute(userRoleId);
		if(map != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_ATTRIBUTE, map);
		}
	}
	
	/**
	 * 一键食用
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TANGBAO_ONE_KEY_EAT)
	public void oneKeyEat(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		tangbaoService.oneKeyEat(userRoleId);
	}
	
	/**
	 * 更新糖宝进度
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.PET_ADD_PROGRESS,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void petAddProgress(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		tangbaoService.addTangbaoProgress(userRoleId);
	}
	/**
	 * 糖宝属性刷新
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.PET_ATTRIBUTE_REFRESH,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void petRefreshAttribute(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		tangbaoService.refreshAttribute(userRoleId);
	}
	
	
}
