package com.junyou.bus.leihao.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.leihao.entity.RefabuLeihao;
import com.junyou.bus.leihao.service.LeiHaoService;

@Service
public class LeiHaoExportService {
	@Autowired
	private LeiHaoService leiHaoService;
	public List<RefabuLeihao> initRefabuLeihao(Long userRoleId) {
		return leiHaoService.initRefabuLeihao(userRoleId);
	}
	public void xiaofeiYb(Long userRoleId,Long yb){
		leiHaoService.xiaofeiYb(userRoleId, yb);
	}
	
	public Object[] getInfo(Long userRoleId, int subId) {
		return leiHaoService.getInfo(userRoleId, subId);
	}
}
