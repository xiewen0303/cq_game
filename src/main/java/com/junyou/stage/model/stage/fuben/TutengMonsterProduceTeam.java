/**
 * 
 */
package com.junyou.stage.model.stage.fuben;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.core.element.IElement;
import com.junyou.stage.model.core.stage.AbsElementProduceTeam;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.kernel.gen.id.IdFactory;

/**
 * 
 * @author LiuYu
 * @date 2015-4-29 下午4:58:40
 */
public class TutengMonsterProduceTeam extends AbsElementProduceTeam {
	
	private StageScheduleExecutor elementScheduler;
	
	private boolean isDelay = false;
	private long guildId;
	private short cmd;
	/**
	 * 创建图腾怪物队伍
	 * @param guildId 图腾所属公会，同公会无法攻击，如果为0，则所有玩家无法攻击
	 * @param cmd 图腾死亡后通知指令，指令内发转发至业务层。如果为0，则标识该图腾死亡后无通知指令
	 */
	public TutengMonsterProduceTeam(String teamId,ElementType elementType,int limit,String elementId,List<Integer[]> xyPoints,Integer produceDelay,long guildId,short cmd) {
		super(teamId, elementType, limit, elementId, xyPoints,produceDelay);
		
		this.elementScheduler = new StageScheduleExecutor(teamId);
		this.guildId = guildId;
		this.cmd = cmd;
		if(produceDelay > 0){
			isDelay = true;
		}
		
	}

	protected IElement create(String teamId,String elementId) {
		
		MonsterConfig monsterConfig = StageConfigureHelper.getMonsterExportService().load(elementId);
		if(monsterConfig == null){
			ChuanQiLog.error("Fuben Monster is null monsterId:"+elementId);
			return null;
		}
		return MonsterFactory.createTuTengMonster(IdFactory.getInstance().generateNonPersistentId(), monsterConfig, guildId, cmd);
		
	}
	
	@Override
	public void schedule() {
		StageTokenRunable runable = new StageTokenRunable(null, stage.getId(), InnerCmdType.AI_PRODUCE, new Object[]{stage.getId(),teamId,elementType.getVal()});
		elementScheduler.schedule(new StringBuffer().append(stage.getId()).append("_").append(teamId).toString(), GameConstants.COMPONENT_PRODUCE_TEAM, runable, getDelay(), TimeUnit.MILLISECONDS);	
	}

	@Override
	public void randomXunlouSchedule() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getRandomOneElementId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDelayProduct() {
		return isDelay;
	}
}