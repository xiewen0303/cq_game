package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.PropStageService;
import com.kernel.pool.executor.Message;

/**
 * 道具效果
 * @author LiuYu
 * @date 2015-4-14 下午2:18:53
 */
@Controller
@EasyWorker(groupName = EasyGroup.STAGE, moduleName = GameModType.PROP_MODULE)
public class PropAction {
	@Autowired
	private PropStageService propStageService;
	/**
	 * 血包效果生效
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.PROP_EFFECT_HP)
	public void hpRpopEffect(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		propStageService.hpRpopEffect(userRoleId, stageId);
	}
	/**
	 * 多倍经验到期
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.PROP_EFFECT_EXP_OVER)
	public void expPropOver(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		propStageService.expPropOver(userRoleId, stageId);
	}
	/**
	 * 通知跨服道具生效
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.PROP_EFFECT_NOTICE_KUAFU)
	public void noticeKfProp(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] data = inMsg.getData();
		String goodsId = (String)data[0];
		Integer count = (Integer)data[1];
		propStageService.noticeKfProp(userRoleId, stageId,goodsId,count);
	}
}
