package com.junyou.bus.xianjian.util;

import java.util.ArrayList;
import java.util.List;

import com.junyou.bus.xianjian.entity.XianJianInfo;
import com.junyou.bus.xianjian.manage.XianJian;
import com.junyou.constants.GameConstants;
import com.junyou.stage.model.element.role.business.Equip;

public class XianjianUtil {

	
	public static XianJian coverToXianjian(XianJianInfo info,List<Integer> zuoqiHuanHuaConfigIdList, Object[] zqEquips) {
		XianJian xianjian =new XianJian();
		xianjian.setCzdCount(info.getCzdcount()); 
		
		List<Equip> equips =new ArrayList<>();
		 
		if(zqEquips != null){
			for (Object tmp : zqEquips) {
				equips.add(Equip.convert2Equip( (Object[])tmp )); 
			}
		}
		
		xianjian.setEquips(equips);
		xianjian.setGetOn(info.getIsGetOn() == GameConstants.XJ_UP);
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
