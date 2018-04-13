package com.junyou.gameconfig.map.configure.export;

import java.util.HashMap;
import java.util.Map;

public class NodeConfig {

	public static final String PUBLIC_NODE_NAME = "public";
	public static final String SWAP_NODE_NAME = "swap";
	
	private String name;
	private int localtion;
	private String ip;
	private String wsPort;
	private String tunnelPort;
	private Map<String, String> stages = new HashMap<String, String>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getLocaltion() {
		return localtion;
	}

	public void setLocaltion(String localtion) {
		this.localtion = Integer.valueOf(localtion);
	}

	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getWsPort() {
		return wsPort;
	}
	
	public void setWsPort(String wsPort) {
		this.wsPort = wsPort;
	}
	
	public String getTunnelPort() {
		return tunnelPort;
	}

	public void setTunnelPort(String tunnelPort) {
		this.tunnelPort = tunnelPort;
	}

	public Map<String, String> getStages() {
		return stages;
	}
	
	public void setStages(String stages) {
		if(null == stages || "".equals(stages)) return;
		String[] tmps = stages.split(",");
		if(null != tmps){
			for(String stage : tmps){
				this.stages.put(stage.trim(), stage.trim());
			}
		}
	}
	
	/**
	 * 配置解析完了后，重新设置 stages 数据是读取策划的配置
	 * @param _stages
	 */
	public void setStages(Map<String, String> _stages){
		this.stages.clear();
		
		this.stages = _stages;
	}
	
	public String getWsBaseAddress(){
		return this.ip+":"+this.wsPort;
	}
	
	public boolean contains(String stageid){
		return stages.containsKey(stageid);
	}
	
	public boolean isGs(){
		return !name.equals(PUBLIC_NODE_NAME) && !name.equals(SWAP_NODE_NAME); 
	}
	
	@Override
	public String toString() {
		return " {"+this.name+" "+this.ip+":"+this.tunnelPort +" }";
	}
}
