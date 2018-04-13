package com.junyou.kuafu.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.junyou.kuafu.share.util.KuafuServerInfo;

public class KuafuRoleServerManager {
	private static KuafuRoleServerManager instance = new KuafuRoleServerManager();

	public static KuafuRoleServerManager getInstance() {
		return instance;
	}

	/**
	 * key：userRoleId ,value：KuafuServerConfig
	 */
	private Map<Long, KuafuServerInfo> roleServerMap = new ConcurrentHashMap<Long, KuafuServerInfo>();

	public KuafuServerInfo getBindServer(Long userRoleId) {
		return roleServerMap.get(userRoleId);
	}

	public void bindServer(Long userRoleId, KuafuServerInfo serverInfo) {
		if(serverInfo==null){
			return;
		}
		roleServerMap.put(userRoleId, serverInfo);
	}

	public void removeBind(Long userRoleId) {
		roleServerMap.remove(userRoleId);
	}
	
	public List<Long> getKuafuRoleIds(String serverId){
		List<Long> ret = new ArrayList<Long>();
		Set<Long> userRoleIdSet = roleServerMap.keySet();
		for(Long e:userRoleIdSet){
			KuafuServerInfo serverInfo = roleServerMap.get(e);
			if(serverInfo.getServerId().equals(serverId)){
				ret.add(e);
			}
		}
		return ret;
	}
}
