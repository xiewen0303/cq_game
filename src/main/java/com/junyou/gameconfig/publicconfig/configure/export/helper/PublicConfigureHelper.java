package com.junyou.gameconfig.publicconfig.configure.export.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;

@Component
public class PublicConfigureHelper {
 
	private static GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
 	
	@Autowired
	public void setGongGongShuJuBiaoConfigExportService(GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService) {
		PublicConfigureHelper.gongGongShuJuBiaoConfigExportService = gongGongShuJuBiaoConfigExportService;
	}
	  
	public static GongGongShuJuBiaoConfigExportService getGongGongShuJuBiaoConfigExportService() {
		return gongGongShuJuBiaoConfigExportService;
	}
}
