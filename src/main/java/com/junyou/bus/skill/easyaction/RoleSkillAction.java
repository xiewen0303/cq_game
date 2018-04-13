package com.junyou.bus.skill.easyaction;

import com.junyou.bus.rfbactivity.service.ActivityServiceFacotry;
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.jingji.service.JingjiService;
import com.junyou.bus.shoplimit.service.ShopLimitService;
import com.junyou.bus.skill.service.RoleSkillService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.SKILL_MODULE)
public class RoleSkillAction {
	
	@Autowired
	private RoleSkillService roleSkillService;

	@Autowired
	private ShopLimitService shopLimitService;
	
	/**
	 * 技能被使用，增加熟练度
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.SKILL_USE_SKILL,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void skillUse(Message inMsg){
//		Long userRoleId = inMsg.getRoleId();
//		String skillId = inMsg.getData();
//		
//		roleSkillService.skillUse(userRoleId, skillId);
	}
	
	/**
	 * 学习技能
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SKILL_LEARN)
	public void learnSkill(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		String skillId = (String)data[0];
		Long guid = CovertObjectUtil.object2Long(data[1]);
		Object[] result = roleSkillService.learnSkill(userRoleId, skillId, guid);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SKILL_LEARN, result);
	}
	
	/**
	 * 等级升级触发
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_ROLE_LEVEL_UP)
	public void skillAutoLearn(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int level = (int)data[0];
		int roleConfig = (int)data[1];

		//自动学习技能
		roleSkillService.autoLearnSkill(userRoleId,roleConfig,level);
		
		//触发解锁
		shopLimitService.checkOpen(userRoleId);
		
		//提前添加到竞技场队列中
		if(level== 5){
			jingjiService.getJingjiInfo(userRoleId);	
		}

		//排行升级提醒活动角标
		ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.RFB_LEVEL_TYPE);
	}
	
	@Autowired
	private JingjiService jingjiService;
	
	
	/**
	 * 升级技能
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SKILL_LEVEL_UP)
	public void skillLevelUp(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		String skillId = (String)data[0];
		Long guid = CovertObjectUtil.object2Long(data[1]);
		Object[] result = roleSkillService.skillLevelUp(userRoleId, skillId, guid);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SKILL_LEVEL_UP, result);
	}
	/**
	 * 学习技能
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SKILL_BEIDONG_LEARN)
	public void learnBeiDongSkill(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		String skillId = (String)data[0];
		Long guid = CovertObjectUtil.object2Long(data[1]);
		Object[] result = roleSkillService.learnBeiDongSkill(userRoleId, skillId, guid);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SKILL_BEIDONG_LEARN, result);
	}
	/**
	 * 学习技能
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SKILL_TB_BEIDONG_LEARN)
	public void learnTangBaoSkill(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String skillId = inMsg.getData();
		Object[] result = roleSkillService.learnTangBaoSkill(userRoleId, skillId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SKILL_TB_BEIDONG_LEARN, result);
	}
	/**
	 * 升级技能
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SKILL_BEIDONG_LEVEL_UP)
	public void beiDongSkillLevelUp(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		String skillId = (String)data[0];
		Long guid = CovertObjectUtil.object2Long(data[1]);
		Object[] result = roleSkillService.beiDongSkillLevelUp(userRoleId, skillId, guid);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SKILL_BEIDONG_LEVEL_UP, result);
	}
	/**
	 * 升级糖宝被动技能
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SKILL_TB_BEIDONG_LEVEL_UP)
	public void tangBaoSkillLevelUp(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String skillId = inMsg.getData();
		Object[] result = roleSkillService.tangbaoSkillLevelUp(userRoleId, skillId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SKILL_TB_BEIDONG_LEVEL_UP, result);
	}
	/**
	 * 升级门派技能
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GUILD_SKILL_LEVEL_UP)
	public void guildBeiDongSkillLevelUp(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String skillId = (String)inMsg.getData();
		Object[] result = roleSkillService.guildBeiDongSkillLevelUp(userRoleId, skillId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GUILD_SKILL_LEVEL_UP, result);
	}
	/**
	 * 角色技能信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SKILL_TAB_INFO)
	public void getSkillInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer type = inMsg.getData();
		Object[] result = roleSkillService.getSkillsInfo(userRoleId, type);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SKILL_TAB_INFO, result);
	}
	/**
	 * 升级妖神技能
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_SKILL_LEVEL_UP)
	public void yaoshenSkillLevelUp(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String skillId = inMsg.getData();
		Object[] result = roleSkillService.yaoshenSkillLevelUp(userRoleId, skillId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_SKILL_LEVEL_UP, result);
	}
	/**
	 * 获取糖宝技能格位信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_TANGBAO_SKILL_GEWEI_INFO)
	public void getTangBaoGeWeiInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = roleSkillService.getTangBaoSkillGeWeiInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TANGBAO_SKILL_GEWEI_INFO, result);
	}
	/**
	 * 请求糖宝技能开孔
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_TANGBAO_SKILL_GEWEI_KAIKONG)
	public void openTangBaoGeWei(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer geweiId = inMsg.getData();
		Object[] result = roleSkillService.openTangBaoSkillGeWei(userRoleId, geweiId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TANGBAO_SKILL_GEWEI_KAIKONG, result);
	}
	/**
	 * 请求装备糖宝技能
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SKILL_TB_BEIDONG_ZHUANGBEI)
	public void zhuangBeiTangBaoSkill(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = inMsg.getData();
		String skillId = (String) obj[0];
		Integer gewei = (Integer) obj[1];
		Object[] result = roleSkillService.zhuangBeiJineng(userRoleId, skillId, gewei);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SKILL_TB_BEIDONG_ZHUANGBEI, result);
	}
}
