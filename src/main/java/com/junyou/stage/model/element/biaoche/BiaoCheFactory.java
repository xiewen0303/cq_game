package com.junyou.stage.model.element.biaoche;

import org.springframework.stereotype.Component;

import com.junyou.bus.yabiao.configure.export.YaBiaoConfig;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.attribute.BaseFightAttribute;
import com.junyou.stage.model.core.state.StateManager;
import com.junyou.stage.model.element.componentlistener.FightListener;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.skill.buff.BuffManager;

/**
 * 镖车工厂
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-16 下午4:06:16 
 */
@Component
public class BiaoCheFactory {

	/**
	 * 创建镖车
	 * @param ybConfig
	 * @param owner
	 * @param expireTimes 过期时间戳（毫秒）
	 */
	public static Biaoche createBiaoche(IRole role,YaBiaoConfig ybConfig, long maxTime){
		Biaoche biaoche = new Biaoche(ybConfig, role, maxTime);
		
		//状态管理器相关
		biaoche.setStateManager(new StateManager());
		//AI设置
		biaoche.setAi(new BiaoCheAi(biaoche));
		//属性管理器
		BaseFightAttribute fightAttribute = new BiaoCheFightAttribute(biaoche);
		biaoche.setFightAttribute(fightAttribute);
		//填充属性
		fightAttribute.setBaseAttribute(BaseAttributeType.LEVEL,ybConfig.getAttributeMap());
		//战斗变化统计器
		biaoche.setFightStatistic(new BiaoCheFightStatistic(biaoche));
		//仇恨管理器
		biaoche.setHatredManager(new BiaoCheHatredManager(biaoche));
		//buff管理器
		biaoche.setBuffManager(new BuffManager());
		
		//组建绑定监听器
		FightListener fightListener = new FightListener(biaoche.getFightStatistic());
		biaoche.getFightAttribute().addListener(fightListener);
		biaoche.getStateManager().addListener(fightListener);
		
		//填充血量
		biaoche.getFightAttribute().resetHpMp();

		biaoche.setBiaoceState(1);
		
		return biaoche;
	}
}
