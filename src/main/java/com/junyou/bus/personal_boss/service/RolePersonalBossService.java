package com.junyou.bus.personal_boss.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.personal_boss.configure.RolePersonalBossConfig;
import com.junyou.bus.personal_boss.configure.RolePersonalBossConfigExportService;
import com.junyou.bus.personal_boss.dao.RolePersonalBossDao;
import com.junyou.bus.personal_boss.entity.RolePersonalBoss;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.util.DiTuConfigUtil;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PersonalBossStage;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class RolePersonalBossService {
	@Autowired
	private RolePersonalBossDao RolePersonalBossDao;
	@Autowired
	private RolePersonalBossConfigExportService rolePersonalBossConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private RoleExportService roleExportService;

	public List<RolePersonalBoss> initRolePersonalBoss(Long userRoleId) {
		return RolePersonalBossDao.initRolePersonalBoss(userRoleId);
	}

	private RolePersonalBoss get(Long userRoleId, final int configId) {
		List<RolePersonalBoss> entityList = RolePersonalBossDao.cacheLoadAll(userRoleId,
				new IQueryFilter<RolePersonalBoss>() {

					@Override
					public boolean check(RolePersonalBoss entity) {
						return entity.getConfigId().intValue() == configId;
					}

					@Override
					public boolean stopped() {
						return false;
					}
				});
		if (entityList == null || entityList.size() == 0) {
			entityList = new ArrayList<>();
			RolePersonalBoss entity = new RolePersonalBoss();

			entity.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			entity.setUserRoleId(userRoleId);
			entity.setConfigId(configId);
			entity.setCount(0);
			entity.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			RolePersonalBossDao.cacheInsert(entity, userRoleId);
			entityList.add(entity);
		}

		RolePersonalBoss entity = entityList.get(0);
		if(! DatetimeUtil.dayIsToday(entity.getUpdateTime().getTime())) {
			entity.setCount(0);
			entity.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			RolePersonalBossDao.cacheUpdate(entity, userRoleId);
		}
		return entity;
	}

	public Object[] info(Long userRoleId) {
		List<Object[]> result = new ArrayList<>();
		for(int configId : rolePersonalBossConfigExportService.loadAllConfigId()) {
			RolePersonalBoss entity = get(userRoleId, configId);
			if(entity.getCount().intValue() == 1){
				result.add(new Object[]{configId, entity.getCount()});
			}
//			RolePersonalBossConfig config = rolePersonalBossConfigExportService.loadById(configId);
		}
		return result.toArray();
	}
	
	public Object[] challenge(Long userRoleId, int configId) {
		// 剧情地图
		Integer curMapId = stageControllExportService.getCurMapId(userRoleId);
		if (curMapId == null) {
			return AppErrorCode.PERSONAL_BOSS_MAP_NOT_EXIST;
		}
		DiTuConfig curDitu = diTuConfigExportService.loadDiTu(curMapId);
		if (curDitu != null && DiTuConfigUtil.isBirthMap(curDitu.getType())) {
			return AppErrorCode.PERSONAL_BOSS_IN_JUQING_MAP;
		}
		// 判断是否在副本中
		if (stageControllExportService.inFuben(userRoleId)) {
			return AppErrorCode.PERSONAL_BOSS_IN_FUBEN;
		}
		if (stageControllExportService.isChanging(userRoleId)) {
			return AppErrorCode.PERSONAL_BOSS_IN_FUBEN;
		}
		RolePersonalBoss entity = get(userRoleId, configId);
		RolePersonalBossConfig config = rolePersonalBossConfigExportService.loadById(configId);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		// 进入
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config.getMap());
		if (dituCoinfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}

		if (roleExportService.getLoginRole(userRoleId).getLevel() < config.getLevel()) {
			return AppErrorCode.PERSONAL_BOSS_LEVEL_NO_MATCH;
		}
		
		if (entity.getCount() >= config.getCishu()) {
			return AppErrorCode.PERSONAL_BOSS_COUNT_OVER_LIMIT;
		}
		
		if(config.getItem()!=null) {
			Object[] checkResult = roleBagExportService.checkRemoveBagItemByGoodsId(config.getItem(), userRoleId);
			if (checkResult != null) {
				return checkResult;
			}
			
			BagSlots useResult = roleBagExportService.removeBagItemByGoods(config.getItem(), userRoleId,
					GoodsSource.PERSONAL_BOSS, true, true);
			if (!useResult.isSuccee()) {
				return useResult.getErrorCode();
			}
		}

		stageControllExportService.setChanging(userRoleId, true);

		int[] birthXy = dituCoinfig.getRandomBirth();
		Object[] applyEnterData = new Object[] { config.getMap(), birthXy[0], birthXy[1], MapType.PERSONAL_BOSS, configId};
		StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);

		return new Object[]{AppErrorCode.SUCCESS, config.getId(), GameSystemTime.getSystemMillTime()+config.getTztime()*1000};
	}
	/**杀死boss当天计数减1,奖励道具*/
	public Object[] reward(Long userRoleId, int configId) {
		Object[] obj = AppErrorCode.OK;
		// 剧情地图
		Integer curMapId = stageControllExportService.getCurMapId(userRoleId);
		if (curMapId == null) {
			obj = AppErrorCode.PERSONAL_BOSS_MAP_NOT_EXIST;
		}
		DiTuConfig curDitu = diTuConfigExportService.loadDiTu(curMapId);
		if (curDitu != null && DiTuConfigUtil.isBirthMap(curDitu.getType())) {
			obj = AppErrorCode.PERSONAL_BOSS_IN_JUQING_MAP;
		}
		// 判断是否在副本中
		if (!stageControllExportService.inFuben(userRoleId)) {
			obj = AppErrorCode.PERSONAL_BOSS_IN_FUBEN;
		}
		RolePersonalBossConfig config = rolePersonalBossConfigExportService.loadById(configId);
		if (config == null) {
			obj = AppErrorCode.CONFIG_ERROR;
		}
		// 判断背包是否已满
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(config.getJlitem(), userRoleId);
		if (code != null) {
			roleBagExportService.putInBagOrEmail(config.getJlitem(), userRoleId, GoodsSource.PERNONAL_BOSS_GET
					, true, EmailUtil.getCodeEmail(AppErrorCode.PERSONAL_BOSS_MAIL));
		}else{
			//****进背包****
			roleBagExportService.putGoodsAndNumberAttr(config.getJlitem(), userRoleId, GoodsSource.PERNONAL_BOSS_GET,
					LogPrintHandle.GET_PERNONAL_BOSS, LogPrintHandle.GBZ_PERNONAL_BOSS, true);
		}
		
		return obj;
	}
	
	public void exitStage(Long userRoleId,BusMsgQueue busMsgQueue){
		if (!stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.PERSONAL_BOSS_EXIT, AppErrorCode.FUBEN_NOT_IN_FUBEN);
            return;
        }
        /* 发送到场景内部处理:退出副本命令 */
        busMsgQueue.addStageMsg(userRoleId, InnerCmdType.S_EXIT_FUBEN, null);
        
	}

	public void exit(Long userRoleId) {
		BusMsgSender.send2One(userRoleId, ClientCmdType.PERSONAL_BOSS_EXIT, AppErrorCode.OK);
	}

	public void killHandler(Long userRoleId) {
		String stageId = stageControllExportService.getCurStageId(userRoleId);
		IStage istage = StageManager.getStage(stageId);
		PersonalBossStage stage = (PersonalBossStage)istage;
		
		RolePersonalBoss entity = get(userRoleId,stage.getFubenId());
		if(entity != null){
			entity.setCount(entity.getCount()+1);
			RolePersonalBossDao.cacheUpdate(entity, userRoleId);
		}
	}
	
//	public void chargeCount(Long userRoleId){
//		RolePersonalBoss entity = get(userRoleId, configId);
//		
//	}
}
