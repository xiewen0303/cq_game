package com.junyou.stage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.prop.IProp;
import com.junyou.stage.model.prop.PropFactory;
import com.junyou.stage.model.prop.PropModel;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.tunnel.DirectMsgWriter;


/**
 * 场景道具效果
 * @author LiuYu
 * @date 2015-4-14 下午2:23:17
 */
@Service
public class PropStageService{
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	
	public void hpRpopEffect(Long userRoleId,String stageId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null){
			return;
		}
		
		//死亡状态不处理回血
		if(role.getStateManager().isDead()){
			return;
		}
		
		IFightAttribute fightAttribute = role.getFightAttribute();
		PropModel model = role.getPropModel();
		
		IProp prop = model.getPropByCategory(GoodsCategory.HP_POOL_TYPE);
		if(prop == null){
			return;
		}
		GoodsConfig goodsConfig = prop.getGoodsConfig();
		//消耗道具(可传任意值，方法已重写，不涉及参数)
		boolean over = prop.costValue(0);
		
		Long maxHp = fightAttribute.getMaxHp() * 1l;
		//主角回血
		if(fightAttribute.getCurHp() < maxHp){ 
			int val = goodsConfig.getData3() == null ? 0 : Integer.parseInt(goodsConfig.getData3());
			int ratio = goodsConfig.getData4() == null ? 0 : Integer.parseInt(goodsConfig.getData4());
			if(ratio > 0){
				val += maxHp * ratio / 100;
			}
			long cur = fightAttribute.getCurHp() + val;
			if(cur > maxHp){
				cur = maxHp.intValue();
			}
			fightAttribute.setCurHp(cur);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		}
		
		if(over){//道具效果已结束
			model.remove(prop);
		}else{
			prop.start();
		}
		
//		prop.notifyPropChange();
		
	}
	public void expPropOver(Long userRoleId,String stageId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null){
			return;
		}
		
		PropModel model = role.getPropModel();
		IProp prop = model.getPropByCategory(GoodsCategory.MANY_BEI_EXP);
		if(prop == null){
			return;
		}
		//消耗道具(可传任意值，方法已重写，不涉及参数)
		boolean over = prop.costValue(0);
		
		if(over){//道具效果已结束
			model.remove(prop);
		}else{
			prop.stop();
			prop.start();
		}
		
		prop.notifyPropChange();
	}
	
	/**
	 * 通知跨服使用了效果道具
	 * @param userRoleId
	 * @param goodsId
	 */
	public void noticeKfProp(Long userRoleId,String stageId,String goodsId,Integer count){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null){
			return;
		}
		GoodsConfig config = goodsConfigExportService.loadById(goodsId);
		if(config == null){
			return;
		}
		
		PropModel model = role.getPropModel();
		IProp prop = PropFactory.create(role, config,count);
		if(model.add(prop)){
			prop.start();
			prop.notifyPropChange();
		}
	}
}
