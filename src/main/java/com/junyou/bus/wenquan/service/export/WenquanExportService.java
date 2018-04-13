package com.junyou.bus.wenquan.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.wenquan.entity.RoleWenquan;
import com.junyou.bus.wenquan.service.WenquanService;
import com.junyou.bus.wenquan.vo.WenquanRankVo;

@Service
public class WenquanExportService {
	@Autowired
	private WenquanService wenquanService;

	public RoleWenquan initRoleWenquan(Long userRoleId) {
		return wenquanService.initRoleWenquan(userRoleId);
	}
	
	public void initRank(){
		wenquanService.initRank();
	}

	public List<WenquanRankVo> getRank() {
		return wenquanService.getRank();
	}
	
	public boolean isTodayActive(Long userRoleId) {
		return wenquanService.isTodayActive(userRoleId);
	}
	
	public void updateJulinUpdateTime(Long userRoleId){
		wenquanService.updateJulinUpdateTime(userRoleId);
	}
}
