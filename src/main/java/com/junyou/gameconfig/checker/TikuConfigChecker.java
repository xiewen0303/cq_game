package com.junyou.gameconfig.checker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.dati.configure.DaTiPublicConfig;
import com.junyou.bus.dati.export.TiKuConfigExportService;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.log.ChuanQiLog;

@Component
public class TikuConfigChecker {
	
	private static GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	
	private static TiKuConfigExportService tiKuConfigExportService;
	
	@Autowired
	public void setDaTiPublicConfig(GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService){
		TikuConfigChecker.gongGongShuJuBiaoConfigExportService=gongGongShuJuBiaoConfigExportService;
	}
	
	@Autowired
	public void setTiKuConfigExportService(TiKuConfigExportService tiKuConfigExportService){
		TikuConfigChecker.tiKuConfigExportService=tiKuConfigExportService;
	}
	
	public static  boolean startCheck(){
		DaTiPublicConfig daTiPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_DATI);
		if(tiKuConfigExportService.getTiKuConfig().size()<daTiPublicConfig.getTiSum()){
			ChuanQiLog.error("题库配置异常,题库题目小于" + daTiPublicConfig.getTiSum());
			return false;
		}
		return true;
	}

}
