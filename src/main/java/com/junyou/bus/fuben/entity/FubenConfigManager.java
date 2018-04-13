package com.junyou.bus.fuben.entity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.bagua.configure.export.DuoRenTongYongBiaoExportService;
import com.junyou.bus.bagua.constants.BaguaConstant;
import com.junyou.bus.fuben.service.DayFubenConfigService;
import com.junyou.bus.fuben.service.JianzhongConfigService;
import com.junyou.bus.fuben.service.MoreFubenConfigService;
import com.junyou.bus.fuben.service.PataConfigService;
import com.junyou.bus.fuben.service.ShouhuFubenConfigService;
import com.junyou.bus.fuben.service.WuxingFubenConfigService;
import com.junyou.bus.fuben.service.WuxingShilianFubenConfigService;
import com.junyou.bus.fuben.service.WuxingSkillFubenConfigService;
import com.junyou.bus.fuben.service.XinmoDouchangFubenConfigService;
import com.junyou.bus.fuben.service.XinmoFubenConfigService;
import com.junyou.bus.fuben.service.XinmoShenyuanFubenConfigService;
import com.junyou.bus.lianyuboss.configure.LianyuBossConfigExportService;
import com.junyou.bus.maigu.configure.constants.MaiguConstant;
import com.junyou.bus.personal_boss.configure.RolePersonalBossConfigExportService;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.tafang.configure.TaFangFubenConfig;


@Component
public class FubenConfigManager {
	private static DayFubenConfigService dayFubenConfigService;
	private static ShouhuFubenConfigService shouhuFubenConfigService;
	private static JianzhongConfigService jianzhongConfigService;
	private static MoreFubenConfigService moreFubenConfigService;
	private static DuoRenTongYongBiaoExportService duoRenTongYongBiaoExportService;
	private static PataConfigService pataConfigService;
	private static LianyuBossConfigExportService lianyuBossConfigExportService;
	private static WuxingFubenConfigService wuxingFubenConfigService;
	private static WuxingSkillFubenConfigService wuxingSkillFubenConfigService;
	private static WuxingShilianFubenConfigService wuxingShilianFubenConfigService;
	private static XinmoFubenConfigService xinmoFubenConfigService;
	private static XinmoShenyuanFubenConfigService xinmoShenyuanFubenConfigService;
    private static XinmoDouchangFubenConfigService xinmoDouchangFubenConfigService;
    private static RolePersonalBossConfigExportService rolePersonalBossConfigExportService;
	
	@Autowired
	public void setLianyuBossConfigExportService(LianyuBossConfigExportService lianyuBossConfigExportService) {
		FubenConfigManager.lianyuBossConfigExportService = lianyuBossConfigExportService;
	}
	@Autowired
	public void setJianzhongConfigService(JianzhongConfigService jianzhongConfigService) {
		FubenConfigManager.jianzhongConfigService = jianzhongConfigService;
	}
	@Autowired
	public void setDuoRenTongYongBiaoExportService(DuoRenTongYongBiaoExportService duoRenTongYongBiaoExportService) {
		FubenConfigManager.duoRenTongYongBiaoExportService = duoRenTongYongBiaoExportService;
	}
	@Autowired
	public void setDayFubenConfigService(DayFubenConfigService dayFubenConfigService) {
		FubenConfigManager.dayFubenConfigService = dayFubenConfigService;
	}
	@Autowired
	public void setShouhuFubenConfigService(ShouhuFubenConfigService shouhuFubenConfigService) {
		FubenConfigManager.shouhuFubenConfigService = shouhuFubenConfigService;
	}
	@Autowired
	public void setMoreFubenConfigService(MoreFubenConfigService moreFubenConfigService) {
		FubenConfigManager.moreFubenConfigService = moreFubenConfigService;
	}
	@Autowired
	public void setPataConfigService(PataConfigService pataConfigService) {
		FubenConfigManager.pataConfigService = pataConfigService;
	}
	@Autowired
	public void setWuxingFubenConfigService(WuxingFubenConfigService wuxingFubenConfigService) {
	    FubenConfigManager.wuxingFubenConfigService = wuxingFubenConfigService;
	}
	@Autowired
	public void setWuxingSkillFubenConfigService(WuxingSkillFubenConfigService wuxingSkillFubenConfigService) {
	    FubenConfigManager.wuxingSkillFubenConfigService = wuxingSkillFubenConfigService;
	}
	@Autowired
	public void setWuxingShilianFubenConfigService(WuxingShilianFubenConfigService wuxingShilianFubenConfigService) {
        FubenConfigManager.wuxingShilianFubenConfigService = wuxingShilianFubenConfigService;
    }
	@Autowired
    public void setXinmoFubenConfigService(XinmoFubenConfigService xinmoFubenConfigService) {
        FubenConfigManager.xinmoFubenConfigService = xinmoFubenConfigService;
    }
	@Autowired
	public void setXinmoShenyuanFubenConfigService(XinmoShenyuanFubenConfigService xinmoShenyuanFubenConfigService) {
	    FubenConfigManager.xinmoShenyuanFubenConfigService = xinmoShenyuanFubenConfigService;
	}
	@Autowired
    public void setXinmoDouchangFubenConfigService(XinmoDouchangFubenConfigService xinmoDouchangFubenConfigService) {
        FubenConfigManager.xinmoDouchangFubenConfigService = xinmoDouchangFubenConfigService;
    }
	@Autowired
    public void setRolePersonalBossConfigExportService(RolePersonalBossConfigExportService rolePersonalBossConfigExportService) {
		FubenConfigManager.rolePersonalBossConfigExportService = rolePersonalBossConfigExportService;
	}
	
	public static void setConfigs(Map<String, AbsFubenConfig> configs) {
		FubenConfigManager.configs = configs;
	}



	private static Map<String,AbsFubenConfig> configs = new HashMap<>();
	
	public static String getId(int type,int id){
		return type + "_" + id;
	}
	private static int[] splitId(String id){
		if(id == null)return null;
		String[] ids = id.split("_");
		if(ids.length < 2)return null;
		return new int[]{Integer.parseInt(ids[0]),Integer.parseInt(ids[1])};
	}
	public static AbsFubenConfig getConfig(String id){
		AbsFubenConfig config = configs.get(id);
		if(config == null){
			int[] ids = splitId(id);
			if(ids == null || ids.length < 2)return null;
			config = initConfig(id, ids[0], ids[1]);
		}
		return config;
	}
	public static AbsFubenConfig getConfig(int type,int id){
		String configId = getId(type, id);
		AbsFubenConfig config = configs.get(configId);
		if(config == null){
			config = initConfig(configId, type, id);
		}
		return config;
	}
	
	private static AbsFubenConfig initConfig(String key,int type,int id){
		AbsFubenConfig config = null;
		if(type == MapType.FUBEN_MAP){
			config = dayFubenConfigService.loadById(id);
			configs.put(key, config);
		}else if(type == MapType.SAVE_FUBEN_MAP){
			config = shouhuFubenConfigService.loadById(id);
			configs.put(key, config);
		}else if(type == MapType.JIANZHONG_FUBEN_MAP){
			config = jianzhongConfigService.loadJianZhongConfig();
			configs.put(key, config);
		}else if(type == MapType.MORE_FUBEN_MAP){
			config = moreFubenConfigService.loadById(id);
			configs.put(key, config);
		}else if(type == MapType.BAGUA_FUBEN_MAP){
			config = duoRenTongYongBiaoExportService.loadByOrder(BaguaConstant.BUAGUA_FUBEN, id);
			configs.put(key, config);
		}else if(type == MapType.MAIGU_FUBEN_MAP){
			config = duoRenTongYongBiaoExportService.loadByOrder(MaiguConstant.MAIGU_FUBEN, id);
			configs.put(key, config);
		}else if(type == MapType.PATA_FUBEN_MAP){
			config = pataConfigService.loadById(id);
			configs.put(key, config);
		}else if(type == MapType.GUILD_LIANYU_BOSS){
			config = lianyuBossConfigExportService.loadById(id);
			configs.put(key, config);
		}else if(type == MapType.TAFANG_FUBEN_MAP){
			config = new TaFangFubenConfig();
			configs.put(key, config);
		}else if(type == MapType.WUXING_FUBEN_MAP){
		    config = wuxingFubenConfigService.loadById(id);
		    configs.put(key, config);
		}else if(type == MapType.WUXING_SKILL_FUBEN_MAP){
		    config = wuxingSkillFubenConfigService.loadByLayer(id);
		    configs.put(key, config);
		}else if(type == MapType.WUXING_SHILIAN_FUBEN_MAP){
            config = wuxingShilianFubenConfigService.loadByConfig();
            configs.put(key, config);
        }else if(type == MapType.XINMO_FUBEN_MAP){
            config = xinmoFubenConfigService.loadFubenConfigById(id);
            configs.put(key, config);
        }else if(type == MapType.XINMO_SHENYUAN_FUBEN_MAP){
            config = xinmoShenyuanFubenConfigService.loadById(id);
            configs.put(key, config);
    	}else if(type == MapType.XINMO_DOUCHANG_FUBEN_MAP){
    	    config = xinmoDouchangFubenConfigService.loadByConfig();
    	    configs.put(key, config);
    	}else if(type == MapType.PERSONAL_BOSS){
    	    config = rolePersonalBossConfigExportService.loadById(id);
    	    configs.put(key, config);
    	}
		return config;
	}
	
}
