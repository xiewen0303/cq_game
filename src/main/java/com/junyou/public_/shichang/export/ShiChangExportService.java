package com.junyou.public_.shichang.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.public_.shichang.service.ShiChangService;

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午3:38:12
 *@Description: 市场
 */

@Service
public class ShiChangExportService{
	@Autowired
	private ShiChangService shiChangService;
	
	public void init(){
		shiChangService.init();
	}
}