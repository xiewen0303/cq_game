package com.junyou.bus.equip.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.equip.entity.XuantieDuihuan;
import com.junyou.bus.equip.service.XuanTieDuiHuanService;
 

@Service
public class XuanTieDuiHuanExportService {
	
	@Autowired
	private XuanTieDuiHuanService xuanTieDuiHuanService;
	
	public List<XuantieDuihuan> initXuanTieDuiHuan(Long	userRoleId){
		return xuanTieDuiHuanService.initXuanTieHuiHuan(userRoleId);
	}

}
