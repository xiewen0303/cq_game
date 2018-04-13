package com.junyou.bus.jewel.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.jewel.entity.RoleJewel;
import com.junyou.bus.jewel.service.JewelService;

@Service
public class JewelExportService {

	
	@Autowired
	private JewelService jewelService;
	/**
	 * 玩家登陆加载进缓存
	 * @param userRoleId
	 * @return
	 */
	public List<RoleJewel> initJewel(Long userRoleId){
		return jewelService.initRoleJewel(userRoleId);
	}
	/**
	 * 统计宝石给玩家加的属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> countJewelAttr(Long userRoleId){
		
		return jewelService.countJewelAttr(userRoleId);
	}
}
