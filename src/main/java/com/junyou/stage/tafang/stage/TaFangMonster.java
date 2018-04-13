package com.junyou.stage.tafang.stage;

import java.util.concurrent.TimeUnit;

import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.aoi.AoiPoint;
import com.junyou.stage.model.core.stage.aoi.AoiPointManager;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * @author LiuYu
 * 2015-10-10 下午3:38:29
 */
public class TaFangMonster extends Monster{

	public TaFangMonster(Long id, MonsterConfig monsterConfig) {
		super(id, null,monsterConfig);
	}
	
	/**所在移动线*/
	private int moveLine;
	/**终点坐标*/
	private AoiPoint target;
	/**行动步数*/
	private int step;
	
	public int getMoveLine() {
		return moveLine;
	}

	public void setMoveLine(int moveLine) {
		this.moveLine = moveLine;
	}

	public AoiPoint getTarget() {
		return target;
	}

	public void setStopZb(int[] stopZb) {
		target = AoiPointManager.getAoiPoint(stopZb[0], stopZb[1]);
	}

	public int getStep() {
		return step;
	}

	public void addStep() {
		this.step++;
	}
	
	@Override
	public boolean isTaFangMonster(){
		return true;
	}

	@Override
	protected void scheduleDisappearHandle(IHarm harm) {
		//启动消失
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.AI_DISAPPEAR, new Object[]{getId(),getElementType().getVal()});
		getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, disappearDuration, TimeUnit.SECONDS);
	}
	
	public boolean move(){
		AoiPoint movePoint = getNextPoint(target);
		if(target.equals(movePoint)){
			getAi().stop();
			//该怪已到终点
			getStage().leave(this);
			return false;
		}
		getStage().moveTo(this, movePoint.getX(), movePoint.getY());
		if(getPosition().isReallyMove()){
			StageMsgSender.send2Many(getStage().getSurroundRoleIds(getPosition()), ClientCmdType.BEHAVIOR_MOVE, getMoveData());
		}
		return true;
	}
	
	private AoiPoint getNextPoint(AoiPoint target){
		Point from = getPosition();
		int x = from.getX();
		int y = from.getY();
		if(target.getX() > x){
			x++;
		}else if(target.getX() < x){
			x--;
		}
		
		if(target.getY() > y){
			y++;
		}else if(target.getY() < y){
			y--;
		}
		return AoiPointManager.getAoiPoint(x, y);
	}
	
}
