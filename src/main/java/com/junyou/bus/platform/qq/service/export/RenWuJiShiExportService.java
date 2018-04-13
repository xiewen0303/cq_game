package com.junyou.bus.platform.qq.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.qq.entity.Renwujishi;
import com.junyou.bus.platform.qq.service.QQRenWuJiShiService;



@Service
public class RenWuJiShiExportService  {

	@Autowired
	private QQRenWuJiShiService renWuJiShiService;
	
	public Object[] getRenWuStatus(Long roleId,String renWuId,int bu) {
		return renWuJiShiService.getRenWuStatus(roleId, renWuId, bu);
	}

	public List<Renwujishi> initAllRenWuJiShi(Long userRoleId) {
		return renWuJiShiService.initAllRenWuJiShi(userRoleId);
	}

	public Integer lingQu(Long userRoleId, String renWuId, int bu,String goodsId) {
		return renWuJiShiService.lingQu(userRoleId, renWuId, bu, goodsId);
	}

	 public Integer renWuJiShiLingQu(String openId,String serverId,String cmd,String renWuId,Integer step,String payItem){
		 return renWuJiShiService.renWuJiShiLingQu(openId, serverId, cmd, renWuId, step, payItem);
	 }

	
}
