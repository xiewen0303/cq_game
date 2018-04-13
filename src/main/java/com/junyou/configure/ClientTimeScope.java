package com.junyou.configure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientTimeScope {

	private List<ClientTimeScopeNode> clientTimeScopeNodes;
	
	public void addTimeScopeNode(ClientTimeScopeNode node){
		if(null == clientTimeScopeNodes){
			clientTimeScopeNodes = new ArrayList<ClientTimeScopeNode>();
		}
		
		clientTimeScopeNodes.add(node);
	}
	
	/**
	 * 是否在区间
	 */
	public boolean inScope(Calendar calendar) {
		// TODO Auto-generated method stub
		
		if(null == clientTimeScopeNodes) return true;
		
		for(ClientTimeScopeNode node : clientTimeScopeNodes){
			
			if(node.inScope(calendar)) return true;
		}
		
		
		
		return false;
	}

}
