/**
 * 
 */
package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.module.GameModType;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.service.FightService;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-5下午4:17:35
 */
@Controller
@EasyWorker(moduleName = GameModType.STAGE,groupName = EasyGroup.STAGE)
public class FightAction {
	
	@Autowired
	private FightService fightService;

	@EasyMapping(mapping = ClientCmdType.SKILL_FIRE,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void skillFire(Message inMsg){
		Object[] obj = inMsg.getData();
		
		String stageId = inMsg.getStageId();
		Long attackerId = inMsg.getRoleId();
		String skillCategory = (String)obj[0];
		Object[] targetIds = (Object[])obj[1];
		Object[] xys = null;
		if(obj.length > 3){
			xys = (Object[]) obj[3];
		}
		
		fightService.skillFire(stageId,attackerId,skillCategory,targetIds,new Object[]{skillCategory,targetIds,obj[2],attackerId,xys});
	}
	
	/**
	 * 内部施法执行(AOE)
	 * @param inMsg
	 */
//	@EasyMapping(mapping = StageCommands.INNER_SKILL_FIRE)
	public void innerSkillFire(Message inMsg){
		Object[] obj = inMsg.getData();
		
		String stageId = (String) obj[0];
		Long attackerId = (Long) obj[1];
		ElementType  attackerType = (ElementType) obj[2];
		String skillCategory = (String) obj[3];
		
		fightService.innerSkillFire(stageId, attackerId, attackerType,skillCategory);
	}
	
	/**
	 * 内部施法执行(单体)
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_SKILL_SINGLE_FIRE)
	public void innerSingleSkillFire(Message inMsg){
		Object[] obj = inMsg.getData();
		
		String stageId = (String) obj[0];
		Long targetId = (Long) obj[1];
		Long attackerId = (Long) obj[2];
		ElementType  attackerType = (ElementType) obj[3];
		String skillCategory = (String) obj[4];
		ElementType  targetType = (ElementType) obj[5];
		
		fightService.innerSkillSingleFire(stageId, targetId,attackerId, attackerType,skillCategory,targetType);
	}
	/**
	 * 内部施法执行(BUFF触发)
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_BUFF_SKILL_FIRE)
	public void innerBuffSkillFire(Message inMsg){
		Object[] obj = inMsg.getData();
		
		String stageId = (String) obj[0];
		Long targetId = (Long) obj[1];
		Long attackerId = (Long) obj[2];
		ElementType  attackerType = (ElementType) obj[3];
		String skillId = (String) obj[4];
		ElementType  targetType = (ElementType) obj[5];
		
		fightService.innerBuffSkillFire(stageId, targetId,attackerId, attackerType,targetType,skillId);
	}
	
	
	/**
	 * 道具复活
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.PROP_REVIVE)
	public void propRevive(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		
		fightService.propRevive(stageId, roleId);
	}
	
	/**
	 * 道具复活（通知到跨服执行复活操作）
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KF_PROP_REVIVE,kuafuType = EasyKuafuType.DIRECT_SWAP_TYPE)
	public void kfPropRevive(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		
		fightService.kfPropRevive(stageId, roleId);
	}
	
	/**
	 * 普通复活
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TOWN_REVIVE,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void townRevive(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		
		fightService.townRevive(stageId,roleId);
	}
	
	/**
	 * 普通复活
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.SHENMO_AUTO_FUHUO)
	public void shenmoRevive(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		
		fightService.shenmoRevive(stageId,roleId);
	}
	/**
	 * 普通复活
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUBOSS_AUTO_FUHUO)
	public void kuafuBossRevive(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		
		fightService.kuafuBossRevive(stageId,roleId);
	}
	/**
	 * 普通复活
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFUQUNXIANYAN_AUTO_FUHUO)
	public void kuafuQunXianYanRevive(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		
		fightService.kuafuQunXianYanRevive(stageId,roleId);
	}
	/**
	 * 普通复活
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFULUANDOU_AUTO_FUHUO)
	public void kuafuDaLuanDouRevive(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		fightService.kuafuDaluandouRevive(stageId,roleId);
	}
	

    // 跨服云宫之巅场景复活
    @EasyMapping(mapping = InnerCmdType.I_KUAFU_YUNGONG_AUTO_FUHUO)
    public void kuafuYunGongFuhuo(Message inMsg){
        String stageId = inMsg.getStageId();
        Long userRoleId = inMsg.getRoleId();
        fightService.kuafuYunGongFuhuo(stageId, userRoleId);
    }
    
	/**
	 * 装备复活
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.ZHUANGBEI_FUHOU)
	public void fuHuoYuPei(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		fightService.yuPeiRevive(stageId, roleId);
		
	}
	
	/**
	 * 取消复活无敌
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KUAFULUANDOU_QUXIAO_WUDI)
	public void kuafuQuxiaoWuDi(Message inMsg) {
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		ChuanQiLog.error("取消复活："+userRoleId);
		fightService.kuafuLuanDouQuXiaoWuDi(stageId, userRoleId);
	}
	/**
	 * 设置战斗模式
	 */
	@EasyMapping(mapping = ClientCmdType.BATTLE_MODE,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void setBattleMode(Message inMsg){
		
		int mode = (Integer)inMsg.getData();
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		fightService.setBattleMode(stageId,roleId,mode);
	}
	
	/**
	 * 请求兼听其他角色魔法变化[带开关]
	 */
//	@EasyMapping(mapping = StageCommands.QUERY_OTHER_MP)
	public void getOtherRoleMp(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		Object data = inMsg.getData();
		Long otherRoleId = null;
		if(data != null){
			otherRoleId = (Long) data;
		}
		
		Object result = fightService.getOtherRoleMp(stageId, roleId, otherRoleId);
		if(otherRoleId != null){
//			StageMsgSender.send2One(roleId, StageCommands.QUERY_OTHER_MP, result);
		}
	}

	
	/**
	 * 战斗状态检测
	 */
	@TokenCheck(component = GameConstants.COMPONENT_FIGHT_STATE)
	@EasyMapping(mapping = InnerCmdType.INNER_FIGHT_STATE_CHECK)
	public void fightStateCheck(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		fightService.fightStateCheck(stageId,roleId);
	}
	
	@TokenCheck(component = GameConstants.COMPONENT_BACK_FUHUO)
	@EasyMapping(mapping = InnerCmdType.S_CAMP_FUHUO)
	public void sCampFuhuo(Message inMsg){
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		
		fightService.campFuhuo(stageId, userRoleId);
	}
	
	@EasyMapping(mapping = ClientCmdType.CAMP_FUHUO)
	public void campFuhuo(Message inMsg){
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		
		fightService.campFuhuo(stageId, userRoleId);
	}
	
	
	@TokenCheck(component = GameConstants.COMPONENT_BACK_FUHUO)
	@EasyMapping(mapping = InnerCmdType.S_TERRITORY_FUHUO)
	public void sTerritoryFuhuo(Message inMsg){
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		
		fightService.territoryFuhuo(stageId, userRoleId);
	}
	
	@EasyMapping(mapping = ClientCmdType.TERRITORY_FUHUO)
	public void territoryFuhuo(Message inMsg){
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		
		fightService.territoryFuhuo(stageId, userRoleId);
	}
	
	@EasyMapping(mapping = ClientCmdType.BAGUA_FUHUO,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void baguaFuhuo(Message inMsg){
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		
		fightService.baguaFuhuo(stageId, userRoleId);
	}
	
	@EasyMapping(mapping = ClientCmdType.MAIGU_FUHUO,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void maiguFuhuo(Message inMsg){
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		
		fightService.maiguFuhuo(stageId, userRoleId);
	}
	
	
	@TokenCheck(component = GameConstants.COMPONENT_BACK_FUHUO)
	@EasyMapping(mapping = InnerCmdType.S_HCZBS_FUHUO)
	public void sHcZbsFuhuo(Message inMsg){
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		fightService.zbsFuhuo(stageId, userRoleId);
	}

	
	@EasyMapping(mapping = ClientCmdType.HC_ZBS_FUHUO)
	public void HcZbsFuhuo(Message inMsg){
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		fightService.zbsFuhuo(stageId, userRoleId);
	}
	
	
	/**
	 * 服务器内部回城复活
	 * @param inMsg
	 */
	@TokenCheck(component = GameConstants.COMPONENT_BACK_FUHUO)
	@EasyMapping(mapping = InnerCmdType.INNER_TOWN_REVIVE)
	public void innerTownRevive(Message inMsg){
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		
		fightService.townRevive(stageId,roleId);
	}
	
	
	
	/**
	 * 神器攻击
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.SHENQI_ATTACK,kuafuType = EasyKuafuType.DIRECT_SWAP_TYPE)
	public void shenqiAttack(Message inMsg){
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		fightService.shenqiAttack(stageId, userRoleId);
	}
}
