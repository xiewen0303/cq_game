package com.junyou.public_.nodecontrol.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.public_.nodecontrol.service.NodeControlService;

@Service
public class NodeControlExportService {

	@Autowired
	private NodeControlService nodeControlService;
	
	
	/**
	 * 是否微端登录
	 * @param userRoleId
	 * @return true:是
	 */
	public boolean isWeiDuanLogin(Long userRoleId){
		return nodeControlService.isWeiDuanLogin(userRoleId);
	}
	
	/**
	 * 设置一个角色为离线状态
	 * @param roleid
	 */
	public void change2offline(Long roleid){
		nodeControlService.change2offline(roleid);
	}
}
