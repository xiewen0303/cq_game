package com.junyou.bus.xunbao.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.xunbao.service.XunBaoService;

/**
 * @author LiuYu
 * 2015-8-5 下午5:57:50
 */
@Service
public class XunBaoExportService {
	@Autowired
	private XunBaoService xunBaoService;

	public Object[] isFull(long userRoleId){
		return xunBaoService.isFull( userRoleId);
	}
	
	public void putIn(long userRoleId,Map<String,Integer> goodsMap,List<Object[]> outClients,BusMsgQueue busMsgQueue,int type){
		xunBaoService.putIn(userRoleId, goodsMap, outClients, busMsgQueue, type);
	}
}
