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
import com.junyou.stage.model.fight.SkillProcessHelper.ConfirmDraft;
import com.junyou.stage.utils.OffUtils;
import com.junyou.utils.lottery.MathUtils;

/**
 * @description 矩形路径验证
 * @author ShiJie Chi
 * @date 2012-6-12 下午4:28:07 
 */
public class RectangleSkillFirePath implements ISkillFirePath {

	
	public boolean inRange(ISkill skill, IStageElement attacker, IStageElement target,
			Object[] helpParam, IStage stage,ConfirmDraft draft) {
		
		/**
		 * author:ydz:
		 * 
		 * 问题：在主角用矩形AOI技能攻击时，有时会出现离主角非常近的目标无法被攻击到，服务器和客户端所算出来的扇形区域不一致，造成看似在可攻击区域内的目标无法被攻击。
		 * 
		 * 解决方法:在扇形算法前，优先做一个以主角为圆心一格的宽度为半径的小圆
		 */
//		int radius = OffUtils.areaRadius();//固定宽度为圆心
//		if(OffUtils.inScope(attacker.getPosition(), target.getPosition(),PathFinderType.AExtend, radius)){
//			return true;
//		}
		
		Point recA = null;
		Point recB = null;
		Point recC = null;
		Point recD = null;
		
		if(null != draft.getDraft()){
			Object[] tmp = draft.getDraft();
			recA = (Point)tmp[0];
			recB = (Point)tmp[1];
			recC = (Point)tmp[2];
			recD = (Point)tmp[3];
		}else{
			
			SkillConfig skillConfig = skill.getSkillConfig();
			float halfWidth = skillConfig.getPathWidth();
			Point startPoint = OffUtils.convert2XsPoint(attacker.getPosition(), PathFinderType.AExtend);
			
			float length = skillConfig.getRange();
			if(null != helpParam){
				Integer chooseX = (Integer) helpParam[0];
				Integer chooseY = (Integer) helpParam[1];
				float realLength = MathUtils.pointDistance(chooseX,chooseY,startPoint.getX(),startPoint.getY());
				length = (length > realLength) ? realLength : length;
			}
			
			double pi2 = Math.PI * 0.5;
			double angel = Double.parseDouble(String.valueOf(target.getPosition().getX()));
			
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
			double d = Math.sqrt(halfWidth * halfWidth + length * length); 
			double rd = Math.atan2(halfWidth,length);
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
			
			draft.setDraft(new Object[]{recA,recB,recC,recD});
		}		
		
		Point targetPoint = OffUtils.convert2XsPoint(target.getPosition(), PathFinderType.AExtend);
		return right(targetPoint,recA,recB) && right(targetPoint,recB,recC) && right(targetPoint,recC,recD) && right(targetPoint,recD,recA);
	}
	
	private boolean right(Point t, Point a, Point b) {
		return (t.getX() - a.getX()) * (t.getY() - b.getY()) < (t.getY() - a.getY()) * (t.getX() - b.getX());
	}

	@Override
	public Object[] createHelpParams(IStage stage, ISkill skill,
			IFighter attacker, IFighter target) {
		
		return null;
	}

}
