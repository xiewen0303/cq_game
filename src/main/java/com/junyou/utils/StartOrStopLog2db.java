package com.junyou.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.junyou.analysis.GameAppConfig;
import com.junyou.analysis.ServerInfoConfigManager;
import com.junyou.context.GameServerContext;
import com.junyou.log.ChuanQiLog;

public class StartOrStopLog2db {
	
	private static String cover2ShellParams(String[] args){
		if(args == null) {
			return "";
		}
		StringBuffer result = new StringBuffer();
		for (String arg : args) {
			result.append(" ").append(arg);
		}
		return result.toString();
	}
	
	/**
	 * 启动log2db服务
	 * @param args 
	 */
	public static void start(String[] args){
		if(GameServerContext.getGameAppConfig().isUseLog2db()){
			String params = cover2ShellParams(args);
			String sourceDir = ChuanQiConfigUtil.getSourceDir();
			
			StringBuffer shellCommand = new StringBuffer();
			shellCommand.append("sh ")
			.append(sourceDir)
			.append("/")
			.append(ChuanQiConfigUtil.getProjectName())
			.append("_log2db_")
			.append(GameServerContext.getGameAppConfig().getPlatformId())
			.append("_")
			.append(GameServerContext.getGameAppConfig().getServerId())
			.append("/soft/start.sh")
			.append(params);
			
			executeShell(shellCommand.toString());
		}
	}
	
	/**
	 * 停止log2db服务
	 * @param args 
	 */
	public static void stop(){
		if(GameServerContext.getGameAppConfig().isUseLog2db()){
			String sourceDir = ChuanQiConfigUtil.getSourceDir();
			
			StringBuffer shellCommand = new StringBuffer();
			shellCommand.append("sh ")
			.append(sourceDir)
			.append("/")
			.append(ChuanQiConfigUtil.getProjectName())
			.append("_log2db_")
			.append(GameServerContext.getGameAppConfig().getServerId())
			.append("/soft/stop.sh");
			
			executeShell(shellCommand.toString());
		}
	}
	
	public static boolean executeShell(String shellCommand){
		ChuanQiLog.debug("执行shell脚本："+shellCommand.toString());
		
		BufferedReader infoReader = null;
		BufferedReader errorReader = null;
		
		try {
			Process pid = null;
			String[] cmd = {"/bin/sh", "-c", shellCommand};
			
			//执行Shell命令
			pid = Runtime.getRuntime().exec(cmd);
			if (pid != null) {
				infoReader = new BufferedReader(new InputStreamReader(pid.getInputStream(),"utf-8"));
				errorReader = new BufferedReader(new InputStreamReader(pid.getErrorStream(),"utf-8"));
				
				String line = null;
				//读取Shell的输出内容，并添加到stringBuffer中
				while ( (line = infoReader.readLine()) != null) {
					ChuanQiLog.info(line);
				}
			
				pid.waitFor();
			} else {
				ChuanQiLog.error("没有pid/r/n");
			}
			
			if(errorReader != null){
				if (errorReader.ready()) {
					String line = null;
					while ((line = errorReader.readLine()) != null) {
						ChuanQiLog.error(line);
					}
					return false;
				}
			}
		} catch (Exception e) {
			ChuanQiLog.error("执行Shell命令时发生异常",e);
		} finally {
			 try {
				if(infoReader != null){
					 infoReader.close();
				 }
				if(errorReader!= null){
					errorReader.close();
				}
			} catch (IOException e) {
				ChuanQiLog.error("",e);
				e.printStackTrace();
			}
		}
		return true;
	}
}
