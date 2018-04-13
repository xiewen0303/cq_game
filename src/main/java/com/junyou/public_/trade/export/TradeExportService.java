package com.junyou.public_.trade.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.junyou.public_.trade.service.TradeService;

/**
 * 交易对外接口
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-12-3下午6:37:34
 *@Description: 提供给外部使用的背包
 */
@Service
public class TradeExportService {
	 @Autowired
	 private TradeService tradeService;

	public void offlineHandle(Long userRoleId) {
		tradeService.offline(userRoleId);
	}
	 
	 
}