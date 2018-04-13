package com.junyou.bus.qiling.util;

import java.util.ArrayList;
import java.util.List;

import com.junyou.bus.qiling.entity.QiLingInfo;
import com.junyou.bus.qiling.manage.QiLing;
import com.junyou.constants.GameConstants;
import com.junyou.stage.model.element.role.business.Equip;

public class QiLingUtil {

	
	public static QiLing coverToQiLing(QiLingInfo info,List<Integer> zuoqiHuanHuaConfigIdList, Object[] zqEquips) {
		QiLing qiLing =new QiLing();
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
		qiLing.setQiLingLevel(info.getQilingLevel());
		if(zuoqiHuanHuaConfigIdList!=null){
			qiLing.setHuanhuaList(zuoqiHuanHuaConfigIdList);
		}
		return qiLing; 
	}

}
