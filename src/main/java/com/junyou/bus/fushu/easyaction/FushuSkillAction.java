package com.junyou.bus.fushu.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.fushu.service.FushuSkillService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

/**
 * @author LiuYu
 * 2015-8-13 下午4:46:20
 */
@Controller
@EasyWorker(moduleName = GameModType.FUSHU_SKILL, groupName = EasyGroup.BUS_CACHE)
public class FushuSkillAction {

	@Autowired
	private FushuSkillService fushuSkillService;
	
	@EasyMapping(mapping = ClientCmdType.GET_YUJIAN_SKILL_INFO)
	public void getYujianSkillInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = fushuSkillService.getFushuSkillInfo(userRoleId, GameConstants.FUSHU_SKILL_TYPE_ZUOQI);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_YUJIAN_SKILL_INFO, result);
	}
	@EasyMapping(mapping = ClientCmdType.GET_CHIBANG_SKILL_INFO)
	public void getChibangSkillInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = fushuSkillService.getFushuSkillInfo(userRoleId, GameConstants.FUSHU_SKILL_TYPE_CHIBANG);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_CHIBANG_SKILL_INFO, result);
	}
	@EasyMapping(mapping = ClientCmdType.GET_TIANSHANG_SKILL_INFO)
	public void getTianshangSkillInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = fushuSkillService.getFushuSkillInfo(userRoleId, GameConstants.FUSHU_SKILL_TYPE_TIANSHANG);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TIANSHANG_SKILL_INFO, result);
	}
	@EasyMapping(mapping = ClientCmdType.GET_TIANGONG_SKILL_INFO)
	public void getTiangongSkillInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = fushuSkillService.getFushuSkillInfo(userRoleId, GameConstants.FUSHU_SKILL_TYPE_TIANGONG);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TIANGONG_SKILL_INFO, result);
	}
	@EasyMapping(mapping = ClientCmdType.GET_QILING_SKILL_INFO)
	public void getQilingSkillInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = fushuSkillService.getFushuSkillInfo(userRoleId, GameConstants.FUSHU_SKILL_TYPE_QILING);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_QILING_SKILL_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_TIANYU_SKILL_INFO)
	public void getTianYuSkillInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = fushuSkillService.getFushuSkillInfo(userRoleId, GameConstants.FUSHU_SKILL_TYPE_TIANYU);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TIANYU_SKILL_INFO, result);
	}
	
	
	@EasyMapping(mapping = ClientCmdType.LEARN_YUJIAN_SKILL)
	public void learnYujianSkill(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Long guid = CovertObjectUtil.object2Long(data[0]);
		int lockInfo = CovertObjectUtil.object2int(data[1]);
		Object[] result = fushuSkillService.xuexiSkill(userRoleId, guid, GameConstants.FUSHU_SKILL_TYPE_ZUOQI, lockInfo);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LEARN_YUJIAN_SKILL, result);
	}
	@EasyMapping(mapping = ClientCmdType.LEARN_CHIBANG_SKILL)
	public void learnChibangSkill(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Long guid = CovertObjectUtil.object2Long(data[0]);
		int lockInfo = CovertObjectUtil.object2int(data[1]);
		Object[] result = fushuSkillService.xuexiSkill(userRoleId, guid, GameConstants.FUSHU_SKILL_TYPE_CHIBANG, lockInfo);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LEARN_CHIBANG_SKILL, result);
	}
	@EasyMapping(mapping = ClientCmdType.LEARN_TIANSHANG_SKILL)
	public void learnTianshangSkill(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Long guid = CovertObjectUtil.object2Long(data[0]);
		int lockInfo = CovertObjectUtil.object2int(data[1]);
		Object[] result = fushuSkillService.xuexiSkill(userRoleId, guid, GameConstants.FUSHU_SKILL_TYPE_TIANSHANG, lockInfo);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LEARN_TIANSHANG_SKILL, result);
	}
	@EasyMapping(mapping = ClientCmdType.LEARN_TIANGONG_SKILL)
	public void learnTiangongSkill(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Long guid = CovertObjectUtil.object2Long(data[0]);
		int lockInfo = CovertObjectUtil.object2int(data[1]);
		Object[] result = fushuSkillService.xuexiSkill(userRoleId, guid, GameConstants.FUSHU_SKILL_TYPE_TIANGONG, lockInfo);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LEARN_TIANGONG_SKILL, result);
	}
	@EasyMapping(mapping = ClientCmdType.LEARN_QILING_SKILL)
	public void learnQilingSkill(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Long guid = CovertObjectUtil.object2Long(data[0]);
		int lockInfo = CovertObjectUtil.object2int(data[1]);
		Object[] result = fushuSkillService.xuexiSkill(userRoleId, guid, GameConstants.FUSHU_SKILL_TYPE_QILING, lockInfo);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LEARN_QILING_SKILL, result);
	}
	@EasyMapping(mapping = ClientCmdType.LEARN_TIANYU_SKILL)
	public void learnTianYuSkill(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Long guid = CovertObjectUtil.object2Long(data[0]);
		int lockInfo = CovertObjectUtil.object2int(data[1]);
		Object[] result = fushuSkillService.xuexiSkill(userRoleId, guid, GameConstants.FUSHU_SKILL_TYPE_TIANYU, lockInfo);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LEARN_TIANYU_SKILL, result);
	}
	@EasyMapping(mapping = ClientCmdType.GET_WUQI_SKILL_INFO)
	public void getWuQiSkillInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = fushuSkillService.getFushuSkillInfo(userRoleId, GameConstants.FUSHU_SKILL_TYPE_WUQI);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_WUQI_SKILL_INFO, result);
	}
	@EasyMapping(mapping = ClientCmdType.LEARN_WUQI_SKILL)
	public void learnWuQiSkill(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Long guid = CovertObjectUtil.object2Long(data[0]);
		int lockInfo = CovertObjectUtil.object2int(data[1]);
		Object[] result = fushuSkillService.xuexiSkill(userRoleId, guid, GameConstants.FUSHU_SKILL_TYPE_WUQI, lockInfo);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LEARN_WUQI_SKILL, result);
	}
}
