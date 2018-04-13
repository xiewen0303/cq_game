package com.junyou.stage.easyaction;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.stage.service.MonsterService;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

@Controller
@EasyWorker(groupName = EasyGroup.STAGE, moduleName = GameModType.STAGE)
public class AiAction {
	
	@Autowired
	private MonsterService aiService;
	
	/**
	 * ai处理
	 */
	@TokenCheck(component = GameConstants.COMPONENT_AI_HEARTBEAT)
	@EasyMapping(mapping = InnerCmdType.AI_HANDLE)
	public void defaultHandle(Message inMsg){
		
		Object[] data = inMsg.getData();
		
		String stageId = (String)data[0];
		Long id = (Long)data[1];
		Integer elementType = (Integer)data[2];
		aiService.defaultHandle(stageId,id, elementType);
		
	}
	
	/**
	 * 仙宫巡逻AI
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.AI_XIANGONG_XUNLUO)
	public void xunLuoAi(Message inMsg){
		Object[] data = inMsg.getData();
		
		String stageId = (String)data[0];
		Long id = (Long)data[1];
		Integer elementType = (Integer)data[2];
		aiService.xunLuoAi(stageId,id, elementType);
	}
	
	/**
	 * 塔防怪物AI
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.AI_TAFANG_MONSTER)
	public void taFangMonsterAi(Message inMsg){
		Object[] data = inMsg.getData();
		
		String stageId = (String)data[0];
		Long id = (Long)data[1];
		Integer elementType = (Integer)data[2];
		aiService.tafangMonsterAi(stageId, id, elementType);
	}
	/**
	 * 塔防NPCAI
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.AI_TAFANG_NPC)
	public void taFangNPCAi(Message inMsg){
		Object[] data = inMsg.getData();
		
		String stageId = (String)data[0];
		Long id = (Long)data[1];
		Integer elementType = (Integer)data[2];
		aiService.taFangNPCAi(stageId, id, elementType);
	}
	
	/**
	 * 高级ai处理--释放技能
	 */
//	@EasyMapping(mapping = StageCommands.INNER_AI_ADVANCED_1)
	public void gaojiFireHandle(Message inMsg){
		
		Object[] data = inMsg.getData();
		
		Long fighterId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		Long targetId = (Long)data[0];
		String skillId = (String)data[1];
		
		int delay = 0;
		boolean repeat = false;
		if( data.length == 4 ){
			delay = (Integer)data[2];
			repeat = (Boolean)data[3];
		}
		
		aiService.gaojiAiSkill(stageId, fighterId, targetId, skillId, delay, repeat);
		
	}
	/**
	 * 高级ai处理--传送
	 */
//	@EasyMapping(mapping = StageCommands.INNER_AI_ADVANCED_2)
	public void gaojiSendHandle(Message inMsg){
		
		Object[] data = inMsg.getData();
		
		Long roleId = inMsg.getRoleId();
		String stageId = (String)data[0];
		int x = (Integer)data[1];
		int y = (Integer)data[2];
		
		aiService.gaojiAiTelport(roleId, stageId, x, y);
		
	}
	/**
	 * 高级ai处理--说话
	 */
//	@EasyMapping(mapping = StageCommands.INNER_AI_ADVANCED_3)
	public void gaojiSpeakHandle(Message inMsg){
		
		Object[] data = inMsg.getData();
		
		Long id = (Long)data[0];
		String stageId = (String)data[1];
		String words = (String)data[2];
		
		aiService.monsterSpeak(stageId, id, words);
		
	}
	
	@TokenCheck(component = GameConstants.COMPONENT_AI_RETRIEVE)
	@EasyMapping(mapping = InnerCmdType.AI_RETRIEVE)
	public void retrieve(Message inMsg){
		
		Object[] data = inMsg.getData();
		
		String stageId = inMsg.getStageId();
		Long id = (Long)data[0];
		Integer elementType = (Integer)data[1];
		String teamId = (String)data[2];
		
		aiService.retrieve(stageId,id,elementType,teamId);
	}
	
	@TokenCheck(component = GameConstants.COMPONENT_PRODUCE_TEAM)
	@EasyMapping(mapping = InnerCmdType.AI_PRODUCE)
	public void elementProduce(Message inMsg){
		
		Object[] data = inMsg.getData();
		
		String stageId = (String)data[0];
		String teamId = (String)data[1];
		Integer elementType = (Integer)data[2];
		
		aiService.produce(stageId,teamId,elementType);
	}
	
	/**
	 * AI消失
	 */
	@EasyMapping(mapping = InnerCmdType.AI_DISAPPEAR)
	public void elementDisapear(Message inMsg){
		
		Object[] data = inMsg.getData();
		
		String stageId = inMsg.getStageId();
		Long id = (Long)data[0];
		Integer elementType = (Integer)data[1];
		
		aiService.disapear(stageId, id, elementType);
	}
	
	/**
	 * 怪物死亡掉落
	 * @param [怪物编号 |stageId| 怪物坐标x | 怪物坐标y  | 仇恨最高者id | 仇恨最高者level | 仇恨最高者teamId]
	 * @return [roleId,teamId,x,y,[goods | goods]]
	 */
	@EasyMapping(mapping = InnerCmdType.AI_GOODS_DROP)
	public void monsterDeadDrop(Message inMsg){
		
		Object[] data = inMsg.getData();
		String monsterId = (String)data[0];
		String stageId = (String)data[1];
		Integer x = (Integer)data[2];
		Integer y = (Integer)data[3];
		Long hatredestRoleId = (Long)data[4];
		Integer level = (Integer)data[5];
		Integer teamId = (Integer)data[6];
		Long monsterGuid = CovertObjectUtil.object2Long(data[7]);
		
		aiService.monsterDeadDrop(stageId,monsterId,monsterGuid,x,y,hatredestRoleId,level,teamId);
	}
	
	
	/**
	 * 怪物经验掉落
	 * @param [monsterId,monsterHp,仇恨最高者id,仇恨最高者teamId,teamCount,map(key:roleId,val:harm)]
	 */
	@SuppressWarnings("unchecked")
	@EasyMapping(mapping = InnerCmdType.AI_EXP_DROP)
	public void monsterExpDrop(Message inMsg){
		String stageId = inMsg.getStageId();
		Object[] data = inMsg.getData();
		String monsterId = (String) data[0];
		long monsterMaxHp = (Long) data[1];
		Long hatredestRoleId = (Long) data[2];
		String teamId = (String) data[3];
		Map<Long, Integer> harms = (Map<Long, Integer>) data[5];
		
		aiService.monsterExpDrop(stageId, monsterId, monsterMaxHp, hatredestRoleId, teamId, harms);
	}
	
	/**
	 * 怪物自动AI巡逻
	 * @param inMsg
	 */
	@TokenCheck(component = GameConstants.COMPONENT_AUTO_TEAM_XUNLUO)
	@EasyMapping(mapping = InnerCmdType.AI_AUTO_XUNLOU)
	public void elementAiXunluo(Message inMsg){
		Object[] data = inMsg.getData();
		
		String stageId = (String)data[0];
		String teamId = (String)data[1];
		Integer elementType = (Integer)data[2];
		
		aiService.elementAiXunluo(stageId,teamId,elementType);
	}
}