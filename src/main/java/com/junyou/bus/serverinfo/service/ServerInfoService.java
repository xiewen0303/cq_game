package com.junyou.bus.serverinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.serverinfo.dao.ServerInfoDao;
import com.junyou.bus.serverinfo.entity.ServerInfo;


@Service
public class ServerInfoService {

	@Autowired
	private ServerInfoDao serverInfoDao;
	
	
	private List<ServerInfo> loadAllServerInfo() {
		return serverInfoDao.loadAllServerInfosFromDb();
	}
	
	public ServerInfo loadServerInfo(){
		List<ServerInfo> serverInfos = loadAllServerInfo();
		if(serverInfos == null || serverInfos.size() == 0){
			return null;
		}else{
			return serverInfos.get(0);
		}
	}

	public void insertServerInfo(ServerInfo serverInfo) {
		 serverInfoDao.insertServerInfo(serverInfo);
	}
	
	
	public void updateServerInfo(ServerInfo serverInfo) {
		serverInfoDao.updateServerInfo(serverInfo);
		//更新完成(状态改成不需要更新)
		serverInfo.updateFinishState();
	}
	
}