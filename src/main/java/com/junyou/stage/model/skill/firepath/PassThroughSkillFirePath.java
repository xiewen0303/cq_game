package com.junyou.stage.model.skill.firepath;

import com.junyou.gameconfig.constants.PathFinderType;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.SkillConfig;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.skill.ISkillFirePath;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.fight.SkillProcessHelper.ConfirmDraft;
import com.junyou.stage.utils.OffUtils;

/**
 * @description 矩形路径验证
 * @author ShiJie Chi
 * @date 2012-6-12 下午4:28:07 
 */
public class PassThroughSkillFirePath implements ISkillFirePath {

	public boolean inRange(ISkill skill, IStageElement attacker, IStageElement target,
			Object[] helpParam, IStage stage,ConfirmDraft draft) {
		
		/**
		 * author:ydz:
		 * 
		 * 问题：在主角用扇形AOI技能攻击时，有时会出现离主角非常近的目标无法被攻击到，服务器和客户端所算出来的扇形区域不一致，造成看似在可攻击区域内的目标无法被攻击。
		 * 
		 * 解决方法:在扇形算法前，优先做一个以主角为圆心一格的宽度为半径的小圆
		 */
		int radius = OffUtils.areaRadius();//固定宽度为圆心
		if(OffUtils.inScope(attacker.getPosition(), target.getPosition(),PathFinderType.AExtend, radius)){
			return true;
		}
		
		Point recA = null;
		Point recB = null;
		Point recC = null;
		Point recD = null;
		
		Point startPoint = OffUtils.convert2XsPoint(attacker.getPosition(), PathFinderType.AExtend);
		Point endPoint = null;
		if(null != helpParam){
			endPoint = OffUtils.convert2XsPoint(new Point((Integer)helpParam[0],(Integer)helpParam[1]), PathFinderType.AExtend);
		}else{
			endPoint = new Point(startPoint.getX(),startPoint.getY());
		}
		
		if(null != draft.getDraft()){
			Point[] tmp = draft.getDraft();
			recA = tmp[0];
			recB = tmp[1];
			recC = tmp[2];
			recD = tmp[3];
		}else{
			
			SkillConfig skillConfig = skill.getSkillConfig();
			float halfWidth = skillConfig.getPathWidth();
			
			
//			float length = skillConfig.getRang();
			double cLength2 = Math.pow(endPoint.getX() - startPoint.getX(), 2) + Math.pow(endPoint.getY() - startPoint.getY(), 2);
			
			double pi2 = Math.PI * 0.5;
			//
			//Math.atan2(y, x);
			
			double angel = Math.atan2(endPoint.getY() - startPoint.getY(), endPoint.getX() - startPoint.getX());
			
			//recA
			double tr = angel - pi2;
			double cosRad = Math.cos(tr);
			double sinRad = Math.sin(tr);
			Integer aX = (int)(startPoint.getX() +  halfWidth * cosRad);
			Integer aY = (int)(startPoint.getY() + halfWidth * sinRad);
			recA = new Point(aX, aY);
			
			//recB
			tr = angel + pi2;
			cosRad = Math.cos(tr);
			sinRad = Math.sin(tr);
			Integer bX = (int)(startPoint.getX() +  halfWidth * cosRad);
			Integer bY = (int)(startPoint.getY() + halfWidth * sinRad);
			recB = new Point(bX,bY);
			
			//recC
			double d = Math.sqrt(halfWidth * halfWidth + cLength2); 
			double rd = Math.atan2(halfWidth,Math.sqrt(cLength2));
			tr = angel + rd;
			cosRad = Math.cos(tr);
			sinRad = Math.sin(tr);
			Integer cX = (int)(startPoint.getX() +  d * cosRad);
			Integer cY = (int)(startPoint.getY() + d * sinRad);
			recC = new Point(cX, cY);
			
			//recD
			tr = angel - rd;
			cosRad = Math.cos(tr);
			sinRad = Math.sin(tr);
			Integer dX = (int)(startPoint.getX() +  d * cosRad);
			Integer dY = (int)(startPoint.getY() + d * sinRad);
			recD = new Point(dX, dY);
			
			draft.setDraft(new Point[]{recA,recB,recC,recD});
		}		
		
		Point targetPoint = OffUtils.convert2XsPoint(target.getPosition(), PathFinderType.AExtend);
		if(targetPoint.equals(startPoint)){
			return true;
		}
		return right(targetPoint,recA,recB) && right(targetPoint,recB,recC) && right(targetPoint,recC,recD) && right(targetPoint,recD,recA);
	}
	
	private boolean right(Point t, Point a, Point b) {
		return (t.getX() - a.getX()) * (t.getY() - b.getY()) < (t.getY() - a.getY()) * (t.getX() - b.getX());
	}

	@Override
	public Object[] createHelpParams(IStage stage, ISkill skill,
			IFighter attacker, IFighter target) {

		Point startPoint = OffUtils.convert2XsPoint(attacker.getPosition(), PathFinderType.AExtend);
		Point endPoint = OffUtils.convert2XsPoint(target.getPosition(), PathFinderType.AExtend);
		
		int dx = endPoint.getX() - startPoint.getX();
		int dy = endPoint.getY() - startPoint.getY();
		double len = Math.sqrt(dx * dx + dy * dy);
		double px = endPoint.getX() + OffUtils.GAIL_PX * dx / len;
		double py = endPoint.getY() + OffUtils.GAIL_PX * dy / len;
		
		Point result = stage.getSurroundValidPoint(new Point((int)Math.round(px / OffUtils.GAIL_PX), (int)Math.round(py / OffUtils.GAIL_PX)), true, PointTakeupType.GOODS);
		if(null == result){
			result = stage.getSurroundValidPoint(target.getPosition(), false, PointTakeupType.GOODS);
		}
		
		return new Object[]{result.getX(),result.getY()};
		
	}

}
