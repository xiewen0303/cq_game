package com.junyou.bus.xiaofei.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.xiaofei.server.RefabuXiaoFeiService;

@Component
public class XiaoFeiInit {

	@Autowired
	private RefabuXiaoFeiService refabuXiaoFeiService;
	
	
	public void init(){
		//初始化消费
		initXiaoFei();
	}
	
	/**
	 * 初始化消费
	 */
	private void initXiaoFei(){
		refabuXiaoFeiService.quartXiaoFei();
	}

	
	/**
	 * 定时执行方法
	 */
	/*public void doIt(){
		//定时发送结算邮件并清空旧数据
		quartzEmailAndClear();
	}*/
	
	/**
	 * 定时发送结算邮件
	 */
	/*private void quartzEmailAndClear(){
		try {
			
			refabuXiaoFeiService.xfJiangLiEmail();
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}*/
	
	
	
}



