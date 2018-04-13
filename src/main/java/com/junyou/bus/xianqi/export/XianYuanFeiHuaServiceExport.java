package com.junyou.bus.xianqi.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.xianqi.entity.RoleXianyuanFeihua;
import com.junyou.bus.xianqi.service.XianYuanFeiHuaService;

@Service
public class XianYuanFeiHuaServiceExport {
	@Autowired
	private XianYuanFeiHuaService xianYuanFeiHuaService;
	
	public Map<String,Long> getXianYuanFeiHuaAttr(Long userRoleId){
		return xianYuanFeiHuaService.getXianYuanFeiHuaAttr(userRoleId);
	}
	
	public  List<RoleXianyuanFeihua> initRoleXianyuanFeihua(Long userRoleId){
		return xianYuanFeiHuaService.getRoleXianyuanFeihuaDB(userRoleId);
	}
}
