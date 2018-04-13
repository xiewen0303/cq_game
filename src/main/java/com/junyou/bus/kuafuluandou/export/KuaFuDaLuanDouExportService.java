package com.junyou.bus.kuafuluandou.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafuluandou.service.KuaFuDaLuanDouService;

/**
 * @author zhongdian
 * 2016-2-18 下午4:41:15
 */
@Service
public class KuaFuDaLuanDouExportService {

	
	@Autowired
	private KuaFuDaLuanDouService kuaFuDaLuanDouService;
	
	/**
	 * 海选赛，发送邮件
	 */
	public void luanDouHaiXuanSai(){
		kuaFuDaLuanDouService.haixuansai();
	}
	
	public void luanDouJieSuanEmail(){
		kuaFuDaLuanDouService.jiesuanEmail();
	}
	
	//清除大乱斗排名数据
	public void luanDouClearRankData(){
	    kuaFuDaLuanDouService.clearDaLuanDouRankData();
	}
	
}
