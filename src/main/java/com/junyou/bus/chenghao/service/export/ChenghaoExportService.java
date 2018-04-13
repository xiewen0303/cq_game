package com.junyou.bus.chenghao.service.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.chenghao.entity.RoleChenghao;
import com.junyou.bus.chenghao.service.ChenghaoService;

@Service
public class ChenghaoExportService {
	@Autowired
	private ChenghaoService chenghaoService;

	public List<RoleChenghao> initRoleChenghao(Long userRoleId) {
		return chenghaoService.initRoleChenghao(userRoleId);
	}

	public void onlineHandle(Long userRoleId) {
		chenghaoService.filterExpired(userRoleId);
		chenghaoService.noticeClient(userRoleId);
	}

	/**
	 * 返回null代表激活成功，否则不成功 为errorCode
	 * 
	 * @param userRoleId
	 * @param chenghaoId
	 * @return
	 */
	public Object[] activateChenghao(Long userRoleId, Integer chenghaoId) {
		return chenghaoService.activateChenghao(userRoleId, chenghaoId);
	}

	public Map<String, Long> getChenghaoAttribute(Long userRoleId) {
		return chenghaoService.getChenghaoAttribute(userRoleId);
	}

	public Map<Integer, String> getWearingChenghao(Long userRoleId) {
		return chenghaoService.getWearingChenghao(userRoleId);
	}

	public Integer dingshiChenghao(String serverId, String userId,
			Integer chenghaoId, String chenghaoName, String chenghaoRes) {
		return chenghaoService.dingshiChenghao(serverId, userId, chenghaoId,
				chenghaoName, chenghaoRes);
	}
	
	public void fetchDingZhiChenghao(){
		chenghaoService.fetchDingZhiChenghao();
	}

}
