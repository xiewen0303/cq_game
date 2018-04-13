package com.junyou.bus.kuafu_qunxianyan.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafu_qunxianyan.service.KuafuQunXianYanService;

@Service
public class KuafuQunXianYanExportService {

	@Autowired
	private KuafuQunXianYanService kuafuQunXianYanService;
	
	public void clearQunXianYan(){
		kuafuQunXianYanService.clearQunXianYan();
	}
}
