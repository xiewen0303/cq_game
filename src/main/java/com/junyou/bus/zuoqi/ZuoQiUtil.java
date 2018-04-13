package com.junyou.bus.zuoqi;

import java.util.ArrayList;
import java.util.List;

import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.bus.zuoqi.manage.ZuoQi;
import com.junyou.constants.GameConstants;
import com.junyou.stage.model.element.role.business.Equip;

public class ZuoQiUtil {
	
	
	public static ZuoQi coverToZuoQi(ZuoQiInfo zuoQiInfo,List<Integer> zuoqiHuanHuaConfigIdList, Object[] zqEquips){
		ZuoQi zuoQi =new ZuoQi();
		zuoQi.setCzdCount(zuoQiInfo.getCzdcount()); 
		
		List<Equip> equips =new ArrayList<>();
		 
		if(zqEquips != null){
			for (Object tmp : zqEquips) {
				equips.add(Equip.convert2Equip( (Object[])tmp )); 
			}
		}
		
		
		zuoQi.setEquips(equips);
		zuoQi.setGetOn(zuoQiInfo.getIsGetOn() == GameConstants.ZQ_UP);
		zuoQi.setQndCount(zuoQiInfo.getQndCount());
		zuoQi.setShowId(zuoQiInfo.getShowId());
		zuoQi.setZplus(zuoQiInfo.getZplus());
//		List<Integer> skillIds =zqInfo.getSkillIdDatas();
//		TODO 技能
//		zuoQi.setSkills(skills);
		zuoQi.setZuoQiLevel(zuoQiInfo.getZuoqiLevel());
		if(zuoqiHuanHuaConfigIdList!=null){
			zuoQi.setHuanhuaList(zuoqiHuanHuaConfigIdList);
		}
		return zuoQi;
	}
}
