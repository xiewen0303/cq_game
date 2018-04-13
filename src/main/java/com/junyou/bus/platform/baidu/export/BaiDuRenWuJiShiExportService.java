package com.junyou.bus.platform.baidu.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.baidu.service.BaiDuRenWuJiShiService;



@Service
public class BaiDuRenWuJiShiExportService  {

	@Autowired
	private BaiDuRenWuJiShiService renWuJiShiService;
	
	/*public Object[] getRenWuStatus(Long roleId,String renWuId,int bu) {
		return renWuJiShiService.getRenWuStatus(roleId, renWuId, bu);
	}*/

/*	public List<Renwujishi> initAllRenWuJiShi(Long userRoleId) {
		return renWuJiShiService.initAllRenWuJiShi(userRoleId);
	}*/

/*	public Integer lingQu(Long userRoleId, String renWuId, int bu,String goodsId) {
		return renWuJiShiService.lingQu(userRoleId, renWuId, bu, goodsId);
	}*/

	 public Integer renWuJiShiLingQu(String openId,String serverId,String renWuId,Integer step){
		 return renWuJiShiService.renWuJiShiLingQu(openId, serverId,  renWuId, step);
	 }

	
}
