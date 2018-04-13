package com.junyou.stage.model.fight.statistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.gameconfig.constants.AttributeType;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.StageFightOutputWrapper;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.fight.IFightStatistic;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.role.DeadInfo;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.tunnel.IMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.datetime.GameSystemTime;


public abstract class AbsFightStatistic implements IFightStatistic {
	
	protected boolean dead;
	protected DeadInfo deadInfo;
	protected IHarm harm;
	
	protected boolean speedChange;
	
	/**
	 * 变化的属性集合(key:index,val:值)
	 */
	protected Map<Integer,Object> changedAttribute;
	
	/**
	 * 当前hp是否更改
	 */
	protected boolean hpChanged;
	/**
	 * 当前mp是否更改
	 */
	protected boolean mpChanged;
	
	/**
	 * 当前zy是否更改
	 */
	protected boolean zyChanged;
	
	/**
	 * buff相关 更改
	 */
	protected List<IBuff> addBuffs;
	protected List<IBuff> removeBuffs;
	
	/**
	 * 进入战斗状态
	 */
	protected boolean inFightState;
	
	/**
	 * 进入点头状态时间
	 */
	private Long inFightTime;
	
	/**
	 * 离开战斗状态
	 */
	protected boolean outFightState;
	
	protected IFighter fighter;
	
	public AbsFightStatistic(IFighter fighter) {
		this.fighter = fighter;
	}
	
	/**
	 * 获取进入战斗状态时间
	 * @return
	 */
	public Long getInFightTime(){
		return inFightTime;
	}
	
	@Override
	public void flushChanges(IMsgWriter msgWriter) {
		
		//分客户端，队伍，公会推送变化
		try{
		
			hpAttributeChange(msgWriter);
			
			attributeChanged(msgWriter);
			
			buffChange(msgWriter);
			//死亡
			deadChange(msgWriter);
			//战斗状态
			fightStateChange(msgWriter);
			//移动速度
			flushSpeedChange(msgWriter);
			
		}catch(Exception e){
			ChuanQiLog.error("flush error",e);
		}finally{
			clear();
		}
				
	}

	private void hpAttributeChange(IMsgWriter msgWriter){
		if(hpChanged){
			IStage stage = fighter.getStage();
			if(stage != null){
				//通知周围玩家血量生变化
				msgWriter.writeMsg(stage.getNoSelfSurroundRoleIds(fighter.getPosition(), fighter.getId()), ClientCmdType.HP_CHANGE, StageFightOutputWrapper.hpChange(fighter));
			}
			if(ElementType.isRole(fighter.getElementType())){
				msgWriter.writeMsg(fighter.getId(), ClientCmdType.HP_CHANGE, StageFightOutputWrapper.hpChange(fighter));
			}
		}
	}
	
	private void zyAttributeChange(IMsgWriter msgWriter){
		if(zyChanged){
			IStage stage = fighter.getStage();
			if(stage != null){
//				msgWriter.writeMsg(fighter.getId(), StageCommands.ZY_CHANGE, StageFightOutputWrapper.zyChange(fighter));
				
				//通知兼听自己的其他角色 我的真元变化
				if(ElementType.isRole(fighter.getElementType())){
					IRole role = (IRole) fighter;
					
					clearMpJt(role);
					
					Long[] notifyRoleIds = role.getBusinessData().getAttentionRoleIds();
					if(notifyRoleIds != null){
//						msgWriter.writeMsg(notifyRoleIds, StageCommands.QUERY_OTHER_MP, StageFightOutputWrapper.mpJtChange(fighter));
					}
				}
			}
		}
	}
	
	/**
	 * 清理监听
	 * @param role
	 * @param notifyRoleIds
	 */
	private void clearMpJt(IRole role){
		List<Long> dels = new ArrayList<Long>();
		IStage stage = role.getStage();
		
		Long[] notifyRoleIds = role.getBusinessData().getAttentionRoleIds();
		if(notifyRoleIds != null && notifyRoleIds.length > 0){
			
			for (Long nRoleId : notifyRoleIds) {
				IRole nRole = stage.getElement(nRoleId, ElementType.ROLE);
				if(nRole == null){
					dels.add(nRoleId);
				}
			}
			
			if(dels.size() > 0){
				for (Long dRoleId : dels) {
					role.getBusinessData().removeAttentionRoleId(dRoleId);
				}
			}
		}
	}
	
	private void buffChange(IMsgWriter msgWriter){
		if(null != addBuffs || null != removeBuffs){
			IStage stage = fighter.getStage();
			if(null == addBuffs && null != removeBuffs && removeBuffs.size() == 1){
				if(stage != null){
					StageMsgSender.send2Many(stage.getSurroundRoleIds(fighter.getPosition()), ClientCmdType.XIAOCHU_BUFF, new Object[]{fighter.getId(), removeBuffs.get(0).getId()});
				}
			}else{
				if(stage != null){
					//通知周围玩家buff改变
					msgWriter.writeMsg(stage.getSurroundRoleIds(fighter.getPosition()), ClientCmdType.ADD_BUFF,StageFightOutputWrapper.buffChange(fighter,addBuffs,removeBuffs));
				}else{
					msgWriter.writeMsg(fighter.getId(), ClientCmdType.ADD_BUFF,StageFightOutputWrapper.buffChange(fighter,addBuffs,removeBuffs));
				}
			}
		}
	}
	
	private void attributeChanged(IMsgWriter msgWriter){
		if(null != changedAttribute && ElementType.isRole(fighter.getElementType())){
			
			IStage stage = fighter.getStage();
			
			//属性变化
			msgWriter.writeMsg(new Long[]{fighter.getId()}, ClientCmdType.SELF_ATTRIBUTE, changedAttribute);
			if(changedAttribute.containsKey(AttributeType.ZplusIndex)){
				//如果战力变化,通知业务变更
				if(KuafuConfigPropUtil.isKuafuServer()){
					KuafuMsgSender.send2KuafuSource(fighter.getId(), InnerCmdType.ZPLUS_CHANGE_SAVE, changedAttribute.get(AttributeType.ZplusIndex));
				}else{
					StageMsgSender.send2Bus(fighter.getId(), InnerCmdType.ZPLUS_CHANGE_SAVE, changedAttribute.get(AttributeType.ZplusIndex));
				}
			}
		
			if(stage != null){
				//属性变化 战斗力业务
//				StageMsgSender.send2StageInner(fighter.getId(), stage.getId(), StageCommands.INNER_STAGE_FIGHTER_CHANGE, null);
			}
		}
	}
	
	private void fightStateChange(IMsgWriter msgWriter){
		
//		if(outFightState){
//			msgWriter.writeMsg(fighter.getId(), StageCommands.FIGHT_STATE_END, StageOutputWrapper.fightEnd(fighter));
//		}
//		
//		if(!dead){
//			if(inFightState){
//				msgWriter.writeMsg(fighter.getId(), StageCommands.FIGHT_STATE_START, StageFightOutputWrapper.inFight(fighter));
//				
//			}
//		}
	}
	
	 private void deadChange(IMsgWriter msgWriter){
		 
		if(dead){
			
			IStage stage = fighter.getStage(); 
			if(ElementType.isRole(fighter.getElementType())){
			    if(stage.getStageType() != StageType.KUAFUDIANFENG){
			        if(stage.getStageType() == StageType.KUAFU_ARENA_SINGLE){
			            BusMsgSender.send2BusInner(fighter.getId(), InnerCmdType.KUAFU_ARENA_1V1_DEAD_HANDLE, stage.getId());
			        }else{
			            //通知周围玩家死亡信息
			            msgWriter.writeMsg(fighter.getId(), ClientCmdType.ROLE_DEAD, StageFightOutputWrapper.dead(fighter,deadInfo));
			            //TODO 玩家死亡地图日志
			        }
			    }
			}
			
			if(stage != null){
				//服务端推送其他客户端或者怪物死亡的信息
				msgWriter.writeMsg(stage.getSurroundRoleIds(fighter.getPosition()), ClientCmdType.FIGHTER_DEAD, fighter.getId());
			}
//			else{
//				msgWriter.writeMsg(fighter.getId(), ClientCmdType.FIGHTER_DEAD, fighter.getId());
//			}
		}
	}
	
	/**
	 * 移动速度变化
	 * @param msgWriter
	 */
	private void flushSpeedChange(IMsgWriter msgWriter){
		if(speedChange){
			IStage stage = fighter.getStage();
			if(stage != null){
				msgWriter.writeMsg(fighter.getStage().getSurroundRoleIds(fighter.getPosition()), ClientCmdType.SPEED_CHANGE, new Object[]{fighter.getId(),fighter.getFightAttribute().getSpeed()});
			}else if(ElementType.isRole(fighter.getElementType())){
				msgWriter.writeMsg(fighter.getId(), ClientCmdType.SPEED_CHANGE, new Object[]{fighter.getId(),fighter.getFightAttribute().getSpeed()});
			}
		}
	}
	
	protected void clear() {
		
		this.hpChanged = false;
		this.mpChanged = false;
		this.zyChanged = false;
		
		this.changedAttribute = null;
		this.harm = null;
		this.dead = false;
		this.speedChange = false;
		
		this.addBuffs = null;
		this.removeBuffs = null;
		
		this.inFightState = false;
		
		this.outFightState = false;
	}

	@Override
	public void addHarm(IHarm harm) {
		this.harm = harm;
	}

	@Override
	public void setAttribute(Integer index, Object val) {
		
		if(null == changedAttribute){
			changedAttribute = new HashMap<Integer, Object>();
		}
		
		changedAttribute.put(index, val);
	}

	@Override
	public void hpChange() {
		this.hpChanged = true;
	}
	@Override
	public void mpChange() {
		this.mpChanged = true;
	}
	
	@Override
	public void zyChange(){
		this.zyChanged = true;
	}

	@Override
	public void dead() {
		this.dead = true;
	}
	
	@Override
	public void speedChange(){
		this.speedChange = true;
	}

	@Override
	public void removeBuff(IBuff buff) {
		
		if(null == removeBuffs){
			removeBuffs = new ArrayList<IBuff>();
		}
		removeBuffs.add(buff);
	}

	@Override
	public void addBuff(IBuff buff) {
		
		if(null == addBuffs){
			addBuffs = new ArrayList<IBuff>();
		}
		
		addBuffs.add(buff);
	}

	public void addState(IState state){
		if(StateType.FIGHT.equals(state.getType())){
			this.inFightState = true;
			this.inFightTime = GameSystemTime.getSystemMillTime();
			
		}else if(StateType.DEAD.equals(state.getType())){
			this.dead = true;
			this.deadInfo = state.getData();
		}
		
	}

	@Override
	public void replaceState(IState state) {
		
		if(StateType.FIGHT.equals(state.getType())){
			this.outFightState = true;
		}
		
	}

	@Override
	public void removeState(IState state) {

		if(StateType.FIGHT.equals(state.getType())){
			this.outFightState = true;
		}
		
	}

}
