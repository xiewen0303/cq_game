package com.junyou.bus.zhuansheng.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.zhuansheng.entity.Zhuansheng;
import com.junyou.bus.zhuansheng.service.ZhuanshengService;

/**
 * @author LiuYu
 * 2015-11-2 下午3:50:16
 */
@Service
public class ZhuanshengExportService {
	@Autowired
	private ZhuanshengService zhuanshengService;
	
	public List<Zhuansheng> initZhuansheng(Long userRoleId) {
		return zhuanshengService.initZhuansheng(userRoleId);
	}
	
	public Map<String,Long> getZhuangShengAttribute(Long userRoleId){
		return zhuanshengService.getZhuangShengAttribute(userRoleId);
	}

	public Integer getZhuanshengLevel(Long userRoleId){
		return zhuanshengService.getZhuanshengLevel(userRoleId);
	}
}
