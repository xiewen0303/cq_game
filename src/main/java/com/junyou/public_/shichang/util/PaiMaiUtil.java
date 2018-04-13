package com.junyou.public_.shichang.util;

import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.public_.shichang.entity.PaiMaiInfo;
import com.junyou.public_.shichang.manage.PaiMai;

public class PaiMaiUtil {
	
	/**
	 * 转换成对应的数据
	 * @param paimaiInfo
	 * @param roleItem
	 * @return
	 */
	public static  PaiMai coverToPaiMai(PaiMaiInfo paimaiInfo,RoleItem roleItem){
		
		PaiMai paimai = new PaiMai(); 
		paimai.setPrice(paimaiInfo.getPrice());
		paimai.setRoleItem(roleItem);
		paimai.setRoleName(paimaiInfo.getRoleName());
		paimai.setSellType(paimaiInfo.getSellType());
		paimai.setSellTime(paimaiInfo.getSellTime());
		paimai.setUserRoleId(paimaiInfo.getUserRoleId());
		return paimai;
	}
}
