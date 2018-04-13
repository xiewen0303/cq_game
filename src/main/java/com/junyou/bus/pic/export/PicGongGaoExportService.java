package com.junyou.bus.pic.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.pic.service.PicGongGaoService;

/**
 * @author zhongdian
 * 2016-3-15 下午3:57:59
 */
@Service
public class PicGongGaoExportService {

	@Autowired
	private PicGongGaoService picGongGaoService;
	
	public void onlineHandle(Long userRoleId){
		picGongGaoService.onlineHandle(userRoleId);
	}
}
