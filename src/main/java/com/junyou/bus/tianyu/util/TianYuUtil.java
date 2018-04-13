package com.junyou.bus.tianyu.util;

import java.util.ArrayList;
import java.util.List;

import com.junyou.bus.tianyu.entity.TianYuInfo;
import com.junyou.bus.tianyu.manage.TianYu;
import com.junyou.constants.GameConstants;
import com.junyou.stage.model.element.role.business.Equip;

public class TianYuUtil {

	
	public static TianYu coverToTianYu(TianYuInfo info,List<Integer> zuoqiHuanHuaConfigIdList, Object[] zqEquips) {
		TianYu qiLing =new TianYu();
		qiLing.setCzdCount(info.getCzdcount()); 
		
		List<Equip> equips =new ArrayList<>();
		 
		if(zqEquips != null){
			for (Object tmp : zqEquips) {
				equips.add(Equip.convert2Equip( (Object[])tmp )); 
			}
		}
		
		qiLing.setEquips(equips);
		qiLing.setGetOn(info.getIsGetOn() == GameConstants.Ql_UP);
		qiLing.setQndCount(info.getQndCount());
		qiLing.setShowId(info.getShowId());
		qiLing.setZplus(info.getZplus());
//		List<Integer> skillIds =zqInfo.getSkillIdDatas();
//		TODO 技能
//		zuoQi.setSkills(skills);
		qiLing.setTianYuLevel(info.getTianyuLevel());
		if(zuoqiHuanHuaConfigIdList!=null){
			qiLing.setHuanhuaList(zuoqiHuanHuaConfigIdList);
		}
		return qiLing; 
	}

}
