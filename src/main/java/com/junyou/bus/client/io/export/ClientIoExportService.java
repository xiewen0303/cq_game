package com.junyou.bus.client.io.export;

import org.springframework.stereotype.Service;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;

@Service
public class ClientIoExportService {
	
	/**
	 * 角色接入节点
	 * @param roleid
	 */
	public void roleIn(Long roleid,String ip) {
		BusMsgSender.send2BusInit(roleid, InnerCmdType.BUS_INIT_IN, ip);
	}

	/**
	 * 角色退出节点
	 * @param roleid
	 */
	public void roleOut(Long roleid) {
		BusMsgSender.send2BusInit(roleid, InnerCmdType.BUS_INIT_OUT, null);
	}
}
