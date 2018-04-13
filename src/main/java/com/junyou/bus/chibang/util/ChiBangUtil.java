package com.junyou.bus.chibang.util;

import java.util.ArrayList;
import java.util.List;

import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.chibang.manage.ChiBang; 
import com.junyou.constants.GameConstants;
import com.junyou.stage.model.element.role.business.Equip;

public class ChiBangUtil {

	
	public static ChiBang coverToChiBang(ChiBangInfo info,List<Integer> zuoqiHuanHuaConfigIdList, Object[] zqEquips) {
		ChiBang chiBang =new ChiBang();
		chiBang.setCzdCount(info.getCzdcount()); 
		
		List<Equip> equips =new ArrayList<>();
		 
		if(zqEquips != null){
			for (Object tmp : zqEquips) {
				equips.add(Equip.convert2Equip( (Object[])tmp )); 
			}
		}
		
		chiBang.setEquips(equips);
		chiBang.setGetOn(info.getIsGetOn() == GameConstants.CB_UP);
		chiBang.setQndCount(info.getQndCount());
		chiBang.setShowId(info.getShowId());
		chiBang.setZplus(info.getZplus());
//		List<Integer> skillIds =zqInfo.getSkillIdDatas();
//		TODO 技能
//		zuoQi.setSkills(skills);
		chiBang.setZuoQiLevel(info.getChibangLevel());
		if(zuoqiHuanHuaConfigIdList!=null){
			chiBang.setHuanhuaList(zuoqiHuanHuaConfigIdList);
		}
		return chiBang; 
	}

}
