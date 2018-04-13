package com.junyou.kuafuio.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.constants.GameConstants;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.kernel.spring.container.DataContainer;

@Component
public class KuafuOnlineUtil {

	private static PublicRoleStateExportService publicRoleStateExportService;
	private static DataContainer dataContainer;
	
	
	@Autowired
	public void setDataContainer(DataContainer dataContainer) {
		KuafuOnlineUtil.dataContainer = dataContainer;
	}
	@Autowired
	public void setPublicRoleStateExportService(PublicRoleStateExportService publicRoleStateExportService) {
		KuafuOnlineUtil.publicRoleStateExportService = publicRoleStateExportService;
	}
	
	/**
	 * 更改玩家为在线状态
	 * @param roleid
	 */
	public static void changeSomeoneOnline(Long roleid){
		publicRoleStateExportService.change2PublicOnline(roleid);
	}
	
	/**
	 * 更改玩家为离线状态
	 * @param roleid
	 */
	public static void changeSomeoneOffline(Long roleid){
		publicRoleStateExportService.change2PublicOffline(roleid);
		dataContainer.removeData(GameConstants.DATA_CONTAINER_ROLE, roleid.toString());
		dataContainer.removeData(GameConstants.DATA_CONTAINER_ROLESTAGE, roleid.toString());
	}
}