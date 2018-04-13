package com.junyou.stage.model.hatred;
 
import java.util.List;
 
import com.junyou.bus.activityboss.manage.ActivityBoss;
import com.junyou.bus.activityboss.manage.BossHurtRank;
import com.junyou.stage.model.core.element.IFighter;  
import com.junyou.stage.model.core.hatred.IHatred;
import com.junyou.stage.model.core.hatred.IHatredRule;
import com.junyou.stage.model.core.stage.ElementType;

/**
 * 野外刷怪boss
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-28 上午11:14:40
 */
public class BossMonsterHatredManager extends MonsterHatredManager2 {
	
	public BossMonsterHatredManager(IFighter owner, IHatredRule hatredRule) {
		super(owner, hatredRule);
		
	}

	/**
	 * 获得最高仇恨者
	 */
	@Override
	public IHatred getHatredest() {
		//有伤害排行榜先取伤害排行榜最高的
		if(ElementType.isMonster(fighter.getElementType())){
			ActivityBoss aBoss = (ActivityBoss)fighter;
			List<BossHurtRank> ranks = aBoss.getRanks();
			if(ranks != null && ranks.size()>0){
				BossHurtRank  bRank = ranks.get(0);
				IHatred hatred = hatredRule.getHatred(bRank.getUserRoleId());
				if(hatred != null){
					return hatred;	
				}
			}
		} 
		return super.getHatredest();
	}
	 
}
