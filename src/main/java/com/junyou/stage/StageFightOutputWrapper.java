package com.junyou.stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.role.DeadInfo;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.fight.statistic.IMonsterFightStatistic;
import com.junyou.stage.model.skill.harm.HarmPrintUtils;

public class StageFightOutputWrapper {

	public static Object hpChange(IFighter owner) {
		
		IFightAttribute attribute = owner.getFightAttribute();
		
		return new Object[]{owner.getId(),attribute.getCurHp()};
	}
	
	
	public static Object dead(IFighter owner, IHarm harm) {
		return HarmPrintUtils.deadMsg(harm);
	}

	public static Object dead(IFighter owner, DeadInfo deadInfo) {
		return HarmPrintUtils.deadMsg(owner, deadInfo);
	}

	public static Object inFight(IFighter owner) {
		return owner.getId();
	}

	public static Object buffChange(IFighter owner, List<IBuff> addBuffs,
			List<IBuff> removeBuffs) {
		
		List<Object> addBuffMsg = new ArrayList<Object>();
		if(null != addBuffs){
			
			for(int i = 0 ; i < addBuffs.size() ; i++){
				Object msg = addBuffs.get(i).getClientMsg();
				if(msg != null){
					addBuffMsg.add(msg);
				}
//				System.out.println("add-----"+addBuffs.get(i).getOwner().getName()+",id:"+addBuffs.get(i).getId()+",buffId:"+addBuffs.get(i).getBuffId()+",category:"+addBuffs.get(i).getBuffCategory());
			}
		}
		
		Object[] removeBuffMsg = null;
		if(null != removeBuffs){
			removeBuffMsg = new Object[removeBuffs.size()];
			for(int i = 0 ; i < removeBuffs.size() ; i++){
				removeBuffMsg[i] = removeBuffs.get(i).getId();
//				System.out.println("remove-----"+removeBuffs.get(i).getOwner().getName()+",id:"+removeBuffs.get(i).getId()+",buffId:"+removeBuffs.get(i).getBuffId()+",category:"+removeBuffs.get(i).getBuffCategory());
			}
		}
		
		Object[] addOut = null;
		if(addBuffMsg.size() > 0){
			addOut = addBuffMsg.toArray();
		}
		
//		System.out.println("-------------end-----");
		
		return new Object[]{owner.getId(),addOut,removeBuffMsg};
	}

	public static Object hpMpChange2Team(IRole owner) {
		IFightAttribute attribute = owner.getFightAttribute();
		return new Object[]{owner.getId(),attribute.getCurHp(),attribute.getMaxHp(),0};
	}

	public static Object dead2Team(IRole owner,boolean flag) {
		return new Object[]{owner.getId(),flag};
	}

	public static Object dead2Friend(IRole owner, String killerId) {
		return new Object[]{killerId,owner.getId(),owner.getStage().getMapId(),owner.getPosition().getX(),owner.getPosition().getY()};
	}
	
	/**
	 * 公会成员死亡
	 * @param isRole 战胜方是否是角色
	 * @param winRoleId 战胜方编号
	 * @param winRoleName 战胜方名称
	 * @param mapId 地图编号
	 * @return
	 */
	public static Object dead2Guild(boolean isRole, String winRoleId, String winRoleName, String mapId) {
		return new Object[]{isRole, winRoleId, winRoleName, mapId};
	}
	
	/**
	 * 公会成员战胜
	 * @param isRole 失败方是否是角色
	 * @param failId 失败方编号
	 * @param failName 失败方名称
	 * @param mapId 地图编号
	 * @return
	 */
	public static Object beat2Guild(boolean isRole, String failId, String failName, String mapId) {
		return new Object[]{isRole, failId, failName, mapId};
	}

	public static Object attributeChange(IFighter owner,
			Map<String, Object> attributes) {
		return attributes;
	}

	public static Object monsterJlzExpDrop(IRole benefitRole, IMonster monster) {
		return new Object[]{
				benefitRole.getId(),
				monster.getMonsterId(),
				monster.getLevel()
				};
	}
	public static Object monsterGoodDrop(IRole benefitRole, IMonster monster) {
		return new Object[]{
				monster.getMonsterId(),
				monster.getStage().getId(),
				monster.getPosition().getX(),
				monster.getPosition().getY(),
				benefitRole.getId(),
				benefitRole.getLevel(),
				benefitRole.getBusinessData().getTeamId(),
//				null,//TODO
				monster.getId()
				};
	}

	public static Object monsterExpDrop(IRole benefitRole, IMonster monster,Map<Long, Integer> harmMap) {
		return new Object[]{
				monster.getMonsterId(),
				monster.getFightAttribute().getMaxHp(),
				benefitRole.getId(),
//				benefitRole.getBusinessData().getTeamId(),
				null,//TODO
//				benefitRole.getBusinessData().getTeamCount(),
				null,//TODO
				harmMap};
	}

}
