package com.junyou.bus.wuqi;

import java.util.ArrayList;
import java.util.List;

import com.junyou.bus.wuqi.entity.WuQiInfo;
import com.junyou.bus.wuqi.manage.WuQi;
import com.junyou.constants.GameConstants;
import com.junyou.stage.model.element.role.business.Equip;

public class WuQiUtil {
	
	
	public static WuQi coverToWuQi(WuQiInfo wuQiInfo,List<Integer> zuoqiHuanHuaConfigIdList, Object[] zqEquips){
		WuQi zuoQi =new WuQi();
		zuoQi.setCzdCount(wuQiInfo.getCzdcount()); 
		
		List<Equip> equips =new ArrayList<>();
		 
		if(zqEquips != null){
			for (Object tmp : zqEquips) {
				equips.add(Equip.convert2Equip( (Object[])tmp )); 
			}
		}
		
		
		zuoQi.setEquips(equips);
		zuoQi.setGetOn(wuQiInfo.getIsGetOn() == GameConstants.ZQ_UP);
		zuoQi.setQndCount(wuQiInfo.getQndCount());
		zuoQi.setShowId(wuQiInfo.getShowId());
		zuoQi.setZplus(wuQiInfo.getZplus());
//		List<Integer> skillIds =zqInfo.getSkillIdDatas();
//		TODO 技能
//		zuoQi.setSkills(skills);
		zuoQi.setWuqiLevel(wuQiInfo.getZuoqiLevel());
		if(zuoqiHuanHuaConfigIdList != null){
			zuoQi.setHuanhuaList(zuoqiHuanHuaConfigIdList);
		}
		return zuoQi;
	}
}
