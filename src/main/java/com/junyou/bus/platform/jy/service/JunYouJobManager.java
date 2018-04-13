package com.junyou.bus.platform.jy.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.recharge.export.RechargeExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.DownloadPathUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author LiuYu
 * 2016-9-26 下午3:26:13
 */
@Component
public class JunYouJobManager {
	
//	private static final long JOB_TIME = 10 * 1000;//测试用定时间隔
	private static final long JOB_TIME = 8 * 60 * 60 * 1000;
	private JunYouJobRunnable task;
	
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RechargeExportService rechargeExportService;
	
	@PostConstruct
	public void init(){
		//判断是否要启动
		if(ChuanQiConfigUtil.isPrintJyLog()){
			startJob();
		}
	}
	
	public void printLog(){
		long startTime = DatetimeUtil.getDate00Time();
		long endTime = GameSystemTime.getSystemMillTime();
		
		String ptId = ChuanQiConfigUtil.getPlatfromId();
		String serverId = ChuanQiConfigUtil.getServerId();
		String ptServer = ChuanQiConfigUtil.getPlatformServerId();
		String ip = ChuanQiConfigUtil.getServerIp();
		int totalRegistCount = roleExportService.getTotalRegistCount();
		long totalRecharge = rechargeExportService.getTotalRechargeSum();
		if(endTime - startTime < JOB_TIME){
			//获取昨日数据时间点
			endTime = startTime;
			startTime = startTime - GameConstants.DAY_TIME;
		}
		int todayRegistCount = roleExportService.getTimeRegistCount(startTime, endTime);
		long todayRecharge = rechargeExportService.getTimeRechargeSum(startTime, endTime);
		JSONObject json = new JSONObject();
		json.put("logTime", endTime);
		json.put("ptId", ptId);
		json.put("serverId", serverId);
		json.put("ptServer", ptServer);
		json.put("ip", ip);
		json.put("totalRegistCount", totalRegistCount);
		json.put("todayRegistCount", todayRegistCount);
		json.put("totalRecharge", totalRecharge);
		json.put("todayRecharge", todayRecharge);
		String log = json.toJSONString();
		ChuanQiLog.jyLog(log);
		//上报
		StringBuilder urlSb = new StringBuilder();
		urlSb.append(ChuanQiConfigUtil.getJyLogUrl()).append("hqgGameHwReport.do?data=").append(log);
		getCallbackSign(urlSb.toString());
	}
	
	private String getCallbackSign(String urlSb){
		try{
			URL url = new URL(urlSb);
			byte[] data = DownloadPathUtil.download(url);
			if(data == null){
				return null;
			}
			InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(data));
			BufferedReader reader_ = new BufferedReader(reader);
			String sign = reader_.readLine();
			return sign;
		}catch (Exception e) {
			return null;
		}
	}
	
	public void startJob(){
		if(task == null){
			task = new JunYouJobRunnable(this);
		}else if(task.isRun()){
			return;
		}
		Thread thread = new Thread(task,"jygame_job_manager");
		thread.start();
	}
	
	public void stopJob(){
		if(task == null){
			return;
		}
		task.stopRun();
		task = null;
	}
	
	private class JunYouJobRunnable implements Runnable{
		private JunYouJobManager manager;
		private boolean run;
		public JunYouJobRunnable(JunYouJobManager manager){
			this.manager = manager;
		}
		public void stopRun(){
			run = false;
		}
		public boolean isRun(){
			return run;
		}
		@Override
		public void run() {
			run = true;
			while(true){
				if(!run){
					return;
				}
				try{
					//执行定时方法
					manager.printLog();
					Thread.sleep(JOB_TIME);
				}catch (Exception e) {
				}
			}
		}
		
	}
}
