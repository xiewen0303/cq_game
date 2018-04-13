package com.junyou.stage.model.element.monster;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.junyou.cmd.ClientCmdType;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.aoi.AoiPoint;
import com.junyou.stage.model.core.stage.aoi.AoiPointManager;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.skill.harm.Harm;
import com.junyou.stage.model.skill.harm.HarmType;
import com.junyou.stage.model.stage.tanbao.TanBaoStage;
import com.junyou.stage.model.state.AiFightState;
import com.junyou.stage.tunnel.BufferedMsgWriter;
import com.junyou.stage.tunnel.IMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * 仙宫巡逻怪
 * @author LiuYu
 * @date 2015-6-18 下午11:13:33
 */
public class XianGongXunLuoMonster extends FubenMonster{

	public XianGongXunLuoMonster(Long id,MonsterConfig monsterConfig,List<AoiPoint> lujing) {
		super(id, monsterConfig);
		this.lujing = lujing;
	}

	private int state = 1;
	private List<AoiPoint> lujing;
	
	public void move(){
		AoiPoint target = lujing.get(state);
		AoiPoint movePoint = getNextPoint(target);
		if(target.equals(movePoint)){
			state++;
			state = state % lujing.size();
		}
		moveHandle();
		getStage().moveTo(this, movePoint.getX(), movePoint.getY());
		if(getPosition().isReallyMove()){
			StageMsgSender.send2Many(getStage().getSurroundRoleIds(getPosition()), ClientCmdType.BEHAVIOR_MOVE, getMoveData());
		}
	}
	
	private void moveHandle(){
		TanBaoStage stage = (TanBaoStage)getStage();
		List<IRole> list = stage.getElementsByPoint(getPosition(), ElementType.ROLE);
		if(list != null && list.size() > 0){
			IMsgWriter msgWriter = new BufferedMsgWriter();
			for (IRole role : list) {
				if(!role.getStateManager().isDead() && !role.getStateManager().contains(StateType.NO_ATTACKED)){
					role.deadHandle(new Harm(null, this, role, HarmType.PUTONG, 1));
					role.getFightStatistic().flushChanges(msgWriter);
				}
			}
			msgWriter.flush();
		}
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
//	private AoiPoint getBstarFind(AoiPoint target){
//		Point from = getPosition();
//		Point[] points = BStarUtil.findPints(from, target);
//		
//		AoiPoint finalPoint = AoiPointManager.getAoiPoint(from.getX(), from.getY());
//		for (Point point : points) {
//			int faX = from.getX() + point.getX();
//			int faY = from.getY() + point.getY();
//			
//			if(getStage().isCanUseStagePoint(faX ,faY)){
//				finalPoint = AoiPointManager.getAoiPoint(faX, faY);
//				break;
//			}
//		}
//		
//		return finalPoint;
//	}
	
	@Override
	public void enterStageHandle(IStage stage) {
		
		getStateManager().add(new AiFightState());
		getAi().schedule(1000, TimeUnit.MILLISECONDS);
	}
}