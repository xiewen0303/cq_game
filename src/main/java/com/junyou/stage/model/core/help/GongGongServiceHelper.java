package com.junyou.stage.model.core.help;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;

/**   
 */
@Component
public class GongGongServiceHelper {
	
	private static GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	
	private static PlatformGongGongShuJuBiaoConfigExportService platformGongGongShuJuBiaoConfigExportService;
	
	public static PlatformGongGongShuJuBiaoConfigExportService getPlatformGongGongShuJuBiaoConfigExportService() {
		return platformGongGongShuJuBiaoConfigExportService;
	}
	@Autowired
	public void setPlatformGongGongShuJuBiaoConfigExportService(
			PlatformGongGongShuJuBiaoConfigExportService platformGongGongShuJuBiaoConfigExportService) {
		GongGongServiceHelper.platformGongGongShuJuBiaoConfigExportService = platformGongGongShuJuBiaoConfigExportService;
	}
	
	
	public static GongGongShuJuBiaoConfigExportService getGongGongShuJuBiaoConfigExportService() {
		return gongGongShuJuBiaoConfigExportService;
	}
	@Autowired
	public void setGongGongShuJuBiaoConfigExportService(GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService) {
		GongGongServiceHelper.gongGongShuJuBiaoConfigExportService = gongGongShuJuBiaoConfigExportService;
	}
	
}