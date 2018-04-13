package com.junyou.bus.sevenlogin.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.sevenlogin.service.SevenLoginService;

/**
 * @author DaoZheng Yuan
 * 2015年5月29日 下午5:25:30
 */
@Service
public class SevenLoginExportService {

	@Autowired
	private SevenLoginService sevenLoginService;
	
	/**
	 * 获取七登是否有奖励
	 * @param userRoleId
	 * @return 返回状态值：没有奖励 = 0 ，有奖励 = 2的3次方
	 */
	public int getSevenLoginStateValue(Long userRoleId){
		return sevenLoginService.getSevenLoginStateValue(userRoleId);
	}
}
