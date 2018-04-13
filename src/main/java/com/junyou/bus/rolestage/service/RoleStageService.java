package com.junyou.bus.rolestage.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.rolestage.dao.PublicCdManagerDao;
import com.junyou.bus.rolestage.dao.RoleStageDao;
import com.junyou.bus.rolestage.entity.RoleStage;
import com.junyou.cmd.ClientCmdType;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.ExpConfig;
import com.junyou.gameconfig.export.ExpConfigExportService;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.NuqiPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.helper.PublicConfigureHelper;
import com.junyou.stage.model.skill.PublicCdManager;

/**
 * 场景
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-10 下午7:21:10 
 */
@Service
public class RoleStageService {
	@Autowired
	private RoleStageDao roleStageDao;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private ExpConfigExportService expConfigExportService;
	@Autowired
	private PublicCdManagerDao publicCdManagerDao;
	
	public void roleStageInit(Long userRoleId,Integer configId, Map<Short, Object> otherMsg) {
		RoleStage roleStage = new RoleStage();
		
		ExpConfig expConfig = expConfigExportService.loadById(1);
		
		Map<String,Long> attribute = expConfig.getAttribute();
		
		Long maxHp = attribute.get(EffectType.x1.name());
		
		roleStage.setHp(maxHp);
		
		roleStage.setUserRoleId(userRoleId);
		roleStage.setShenqi(0);
		NuqiPublicConfig nuqiPublicConfig = PublicConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_NUQI);
		if(nuqiPublicConfig != null){
			roleStage.setNuqi(nuqiPublicConfig.getMaxNq());
		}else{
			roleStage.setNuqi(0);
		}
		roleStage.setCdManager(new PublicCdManager());
		
		//设置出生点
		DiTuConfig config = diTuConfigExportService.loadBirthDiTu();
//		List<Integer[]> points = config.getZuobiaoChusheng();
//		int random = Lottery.roll(points.size());
//		Integer[] point = points.get(random);
		
		roleStage.setMapId(config.getId());
		
		int[] birthXy = config.getRandomBirth();
			
		roleStage.setMapX(birthXy[0]);
		roleStage.setMapY(birthXy[1]);
		
		roleStage.setLastMainMap(config.getId());
		
		otherMsg.put(ClientCmdType.ENTER_STAGE_OK, new Object[]{roleStage.getMapId(),roleStage.getMapX(),roleStage.getMapY()});
		
		roleStageDao.createRoleStageFromDb(roleStage, userRoleId);
	}
	
	/**
	 * 获取场景直接访问缓存
	 * @param userRoleId
	 * @return
	 */
	public RoleStage getRoleStage(Long userRoleId) {
		return roleStageDao.cacheLoad(userRoleId, userRoleId);
	}
	
	/**
	 * 获取场景直接访问库
	 * @param userRoleId
	 * @return
	 */
	public RoleStage getRoleStageFromDb(Long userRoleId){
		return roleStageDao.getRoleStageFromDb(userRoleId);
	}
	
	public void onlineHandle(Long userRoleId){
		RoleStage roleStage = getRoleStage(userRoleId);
		PublicCdManager publicCdManager = publicCdManagerDao.loadFile(userRoleId+"");
		if(publicCdManager == null){
			publicCdManager = new PublicCdManager();
		}
		roleStage.setCdManager(publicCdManager);
	}
	
	public void offlineHandle(Long userRoleId){
		RoleStage roleStage = getRoleStage(userRoleId);
		PublicCdManager publicCdManager = roleStage.getCdManager();
		publicCdManagerDao.writeFile(publicCdManager, userRoleId+"");
	}
	/**
	 * 更新玩家神器
	 * @param userRoleId
	 * @param shenqi
	 */
	public void updateShenqi(Long userRoleId,Integer shenqi){
		RoleStage cache = getRoleStage(userRoleId);
		cache.setShenqi(shenqi);
		roleStageDao.cacheUpdate(cache, userRoleId);
	}
}
