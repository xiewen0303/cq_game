package com.junyou.bus.xunbao.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.xunbao.entity.RefbXunbao;
import com.junyou.bus.xunbao.service.RefbXunBaoService;

/**
 * 
 * @author zhubo 
 * @email  zhubo@junyougame.com
 * @date 2015-10-9 下午5:00:08
 */
@Service
public class RefbXunBaoExportService {
	@Autowired
	private RefbXunBaoService xunBaoService;
	
	public List<RefbXunbao> initRefbXunbao(Long userRoleId){
		return xunBaoService.initXunbao(userRoleId);
	}
	
	public Object[] getRefbXunBaoInfo(Long userRoleId, Integer subId){
		return xunBaoService.getRefbXunBaoData(userRoleId, subId);
	}
	
	public Object[] getAllRefbXunbaoCount(Integer subId){
		return xunBaoService.getAllRefbXunbaoCount(subId);
	}
}
