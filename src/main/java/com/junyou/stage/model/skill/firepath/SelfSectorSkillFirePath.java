package com.junyou.stage.model.skill.firepath;

import com.junyou.gameconfig.constants.PathFinderType;
import com.junyou.stage.configure.SkillFirePathType;
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

public class SelfSectorSkillFirePath implements ISkillFirePath {
	
	
	public boolean inRange(ISkill skill, IStageElement attacker, IStageElement target,
			Object[] helpParam, IStage stage,ConfirmDraft draft) {
		
		if(helpParam == null){
			//如果 参数为空,说明是服务器的AI 技能 那服务器直接园形处理(扇形服务器无法得到角度)
			return SkillFirePathFactory.getSkillFirePath(SkillFirePathType.SELF_CIRCLE).inRange(skill, attacker, target, helpParam, stage, draft);
		}
		
		if(attacker.getPosition().getX() == target.getPosition().getY() && attacker.getPosition().getY() == target.getPosition().getY()){
			return true;
		}
		
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
		
		
		SkillConfig skillConfig = skill.getSkillConfig();
		float length = skillConfig.getPathRadius();
		float halfRad = skillConfig.getPathSectorRad();
		
		Point startPoint = OffUtils.convert2XsPoint(attacker.getPosition(), PathFinderType.AExtend);

		double angel = Double.parseDouble(String.valueOf(helpParam[0]));
		double sqRadius = length * length;
		
		//先根据角度，将初始化的点，重新计算
		Integer sx = startPoint.getX();
		Integer sy= startPoint.getY();

		Point a = null;
		Point b = null;
		
		if(null != draft.getDraft()){
			Object[] tmp = draft.getDraft();
			a = (Point)tmp[0];
			b = (Point)tmp[1];
		}else{
			//计算pta
			double tr = angel - halfRad;
			Integer aX = (int)(sx + length * Math.cos(tr));
			Integer aY = (int)(sy + length * Math.sin(tr));
			a = new Point(aX, aY);
			
			//计算ptb
			tr = angel + halfRad;
			Integer bX = (int)(sx + length * Math.cos(tr));
			Integer bY = (int)(sy + length * Math.sin(tr));
			b = new Point(bX, bY);
			
			draft.setDraft(new Object[]{a,b});
		}
		
		Point t = OffUtils.convert2XsPoint(target.getPosition(), PathFinderType.AExtend);
		Integer dx = sx - t.getX();
		Integer dy = sy - t.getY();
		
		boolean tmp  = right(t,startPoint,a);
		boolean tmp2 = right(t,startPoint,b);
		boolean tmp3 = true;
		if(halfRad < Math.PI){
			if(halfRad > Math.PI * 0.5){
				tmp3 = !tmp || tmp2;
			}else{
				tmp3 = !tmp && tmp2;
			}
		}
		
		return tmp3 && dx * dx + dy * dy <= sqRadius;
	}
	

	private boolean right(Point t, Point a, Point b) {
		return (t.getX() - a.getX()) * (t.getY() - b.getY()) - (t.getY() - a.getY()) * (t.getX() - b.getX()) <0;
	}


	@Override
	public Object[] createHelpParams(IStage stage, ISkill skill,
			IFighter attacker, IFighter target) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
