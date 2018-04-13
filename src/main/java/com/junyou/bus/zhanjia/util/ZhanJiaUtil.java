package com.junyou.bus.zhanjia.util;

import java.util.ArrayList;
import java.util.List;
import com.junyou.bus.zhanjia.entity.ZhanJiaInfo;
import com.junyou.bus.zhanjia.manage.ZhanJia;
import com.junyou.constants.GameConstants;
import com.junyou.stage.model.element.role.business.Equip;

public class ZhanJiaUtil {

	
	public static ZhanJia coverToXianjian(ZhanJiaInfo info,List<Integer> zuoqiHuanHuaConfigIdList, Object[] zqEquips) {
		ZhanJia xianjian =new ZhanJia();
		xianjian.setCzdCount(info.getCzdcount()); 
		
		List<Equip> equips =new ArrayList<>();
		 
		if(zqEquips != null){
			for (Object tmp : zqEquips) {
				equips.add(Equip.convert2Equip( (Object[])tmp )); 
			}
		}
		
		xianjian.setEquips(equips);
		xianjian.setGetOn(info.getIsGetOn() == GameConstants.ZJ_UP);
		xianjian.setQndCount(info.getQndCount());
		xianjian.setShowId(info.getShowId());
		xianjian.setZplus(info.getZplus()==null?0L:info.getZplus());
//		List<Integer> skillIds =zqInfo.getSkillIdDatas();
//		TODO 技能
//		zuoQi.setSkills(skills);
		xianjian.setXianjianLevel(info.getXianjianLevel());
		if(zuoqiHuanHuaConfigIdList!=null){
			xianjian.setHuanhuaList(zuoqiHuanHuaConfigIdList);
		}
		return xianjian; 
	}

}
