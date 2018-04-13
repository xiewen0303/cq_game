package com.junyou.bus.fuben.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.JianzhongFubenConfig;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.YaoshenHunpoPublicConfig;

@Service
public class JianzhongConfigService  {
	 
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	/**
	 * 获取剑冢副本场景配置
	 */
	public JianzhongFubenConfig loadJianZhongConfig(){
		YaoshenHunpoPublicConfig yaoshenHunpoPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YAOSHEN_HUNPO);
		if(yaoshenHunpoPublicConfig != null){
			return yaoshenHunpoPublicConfig.getJianzhongFubenConfig();
		}else{
			return null;
		}
	}

}
