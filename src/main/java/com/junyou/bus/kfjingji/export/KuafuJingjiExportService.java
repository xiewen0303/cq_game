package com.junyou.bus.kfjingji.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kfjingji.entity.KuafuJingji;
import com.junyou.bus.kfjingji.service.KuafuJingjiService;

/**
 * @author LiuYu
 * 2015-10-30 上午11:08:52
 */
@Service
public class KuafuJingjiExportService {
	
	@Autowired
	private KuafuJingjiService kuafuJingjiService;
	
	public List<KuafuJingji> initKuafuJingji(Long userRoleId) {
		return kuafuJingjiService.initKuafuJingji(userRoleId);
	}
}
