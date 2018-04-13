package com.junyou.bus.serverinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.serverinfo.entity.ServerInfo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.accessor.GlobalIdentity;
import com.kernel.data.dao.AbsDao;
import com.kernel.data.dao.IDaoOperation;


@Repository
public class ServerInfoDao extends AbsDao<ServerInfo> implements IDaoOperation<ServerInfo> {

	public List<ServerInfo> loadAllServerInfosFromDb() {
		
		return getRecords(null, GlobalIdentity.get(), AccessType.getDirectDbType());
	}

	public void insertServerInfo(ServerInfo serverInfo) {
		 insert(serverInfo, serverInfo.getId().longValue(), AccessType.getDirectDbType());
	}

	public void updateServerInfo(ServerInfo serverInfo) {
		update(serverInfo, serverInfo.getId().longValue(), AccessType.getDirectDbType());
	}

}