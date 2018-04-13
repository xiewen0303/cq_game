package com.junyou.bus.xiulianzhilu.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.xiulianzhilu.entity.RoleXiulianJifen;
import com.junyou.bus.xiulianzhilu.entity.RoleXiulianTask;
import com.junyou.bus.xiulianzhilu.service.XiuLianService;

@Service
public class XiuLianExportService {
	
	@Autowired
	private XiuLianService xiuLianService;
	
	public List<RoleXiulianJifen> initRoleXiulianJifens(Long userRoleId){
		return xiuLianService.initRoleXiulianJifens(userRoleId);
	}
	
	public List<RoleXiulianTask> initRoleXiulianTasks(Long userRoleId){
		return xiuLianService.initRoleXiulianTasks(userRoleId);
	}
	
	
}
