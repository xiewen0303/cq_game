package com.junyou.bus.platform._37.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform._37.service._37Service;
/**
 * 37平台专属service
 * @author dsh
 * @date 2015-6-10
 */
@Service
public class _37ExportService {
	@Autowired
	private _37Service _37Service;
	/**
	 * 激活绑定手机奖励
	 * 
	 * @return 1:成功 -1：已绑定手机 -2:已绑定手机，且奖励已领取
	 */
	public Integer activatePhoneReward(String serverId,String userId) {
		return _37Service.activatePhoneReward(serverId,userId);
	}
}
