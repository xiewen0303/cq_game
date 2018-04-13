package com.junyou.gameconfig.map.configure.export;

import java.util.HashMap;
import java.util.Map;

public class NodeConfigs {

	private Map<String, NodeConfig> nodeConfigs = new HashMap<String, NodeConfig>();
	
	public void addNodeConfig(NodeConfig nodeConfig){
		nodeConfigs.put(nodeConfig.getName(), nodeConfig);
	}
	
	public NodeConfig getNodeConfig(String nodename){
		return nodeConfigs.get(nodename);
	}
	
	public NodeConfig getNdoeConfigByStageId(String stageid){
		for(NodeConfig nodeConfig : nodeConfigs.values()){
			if(nodeConfig.contains(stageid)){
				return nodeConfig;
			}
		}
		return null;
	}
	
	public Map<String, NodeConfig> getAllNodeConfig(){
		return this.nodeConfigs;
	}
}
