package com.junyou.bus.tafang.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.branchtask.BranchEnum;
import com.junyou.bus.branchtask.service.TaskBranchService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tafang.configure.TaFangExpConfig;
import com.junyou.bus.tafang.configure.TaFangLevelExpConfig;
import com.junyou.bus.tafang.configure.TaFangNpcConfig;
import com.junyou.bus.tafang.configure.TaFangNpcLevelConfig;
import com.junyou.bus.tafang.configure.TaFangPublicConfig;
import com.junyou.bus.tafang.configure.export.TaFangExpConfigServiceExport;
import com.junyou.bus.tafang.configure.export.TaFangLevelExpConfigServiceExport;
import com.junyou.bus.tafang.configure.export.TaFangNpcConfigExportService;
import com.junyou.bus.tafang.configure.export.TaFangNpcLevelConfigExportService;
import com.junyou.bus.tafang.dao.RoleTafangDao;
import com.junyou.bus.tafang.entity.RoleTafang;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.tafang.stage.TaFangNpc;
import com.junyou.stage.tafang.stage.TaFangStage;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;

/**
 * @author LiuYu
 * 2015-10-9 上午11:46:54
 */
@Service
public class RoleTaFangService {
	
	@Autowired
	private RoleTafangDao roleTafangDao;
	@Autowired
	private MonsterExportService monsterExportService;
	@Autowired
	private TaFangNpcConfigExportService taFangNpcConfigExportService;
	@Autowired
	private TaFangNpcLevelConfigExportService taFangNpcLevelConfigExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private TaFangExpConfigServiceExport taFangExpConfigServiceExport;
	@Autowired
	private TaFangLevelExpConfigServiceExport taFangLevelExpConfigServiceExport;
	
	public List<RoleTafang> initRoleTafang(Long userRoleId) {
		return roleTafangDao.initRoleTafang(userRoleId);
	}
	
	public void onlineHandle(Long userRoleId){
		RoleTafang roleTafang = roleTafangDao.cacheLoad(userRoleId, userRoleId);
		if(roleTafang != null && roleTafang.getExp() > 0 && roleTafang.getCal() == GameConstants.BOOLEAN_FALSE_TO_INT){
			calRoleTafang(roleTafang);
		}
	}
	
	private RoleTafang createRoleTafang(Long userRoleId){
		RoleTafang roleTafang = new RoleTafang();
		roleTafang.setUserRoleId(userRoleId);
		roleTafang.setExp(0l);
		roleTafang.setCal(0);
		roleTafang.setJoinTime(0);
		roleTafang.setUpdateTime(GameSystemTime.getSystemMillTime());
		return roleTafang;
	}
	
	private RoleTafang getRoleTafang(Long userRoleId){
		RoleTafang roleTafang = roleTafangDao.cacheLoad(userRoleId, userRoleId);
		if(roleTafang == null){
			roleTafang = createRoleTafang(userRoleId);
			roleTafangDao.cacheInsert(roleTafang, userRoleId);
		}else if(!DatetimeUtil.dayIsToday(roleTafang.getUpdateTime())){
			roleTafang.setJoinTime(0);
			roleTafang.setUpdateTime(GameSystemTime.getSystemMillTime());
			roleTafangDao.cacheUpdate(roleTafang, userRoleId);
		}
		return roleTafang;
	}
	
	public Object[] getTaFangInfo(Long userRoleId){
		RoleTafang roleTafang = getRoleTafang(userRoleId);
		return new Object[]{roleTafang.getJoinTime(),roleTafang.getExp()};
	}
	
	public Object[] reciveExp(Long userRoleId,int id){
		RoleTafang roleTafang = getRoleTafang(userRoleId);
		if(roleTafang.getExp() < 1){
			return AppErrorCode.TAFANG_NO_EXP;//当前没有可领取经验值
		}
		int tq = roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_TAFANG_EXP_RATE);
		if(id > tq){
			return AppErrorCode.VIP_NOT_ENOUGH_LEVEL;
		}
		TaFangExpConfig config = taFangExpConfigServiceExport.getConfigById(id);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(config.getGold() > 0){
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, config.getGold(), userRoleId, LogPrintHandle.CONSUME_TAFANG, true, LogPrintHandle.CBZ_TAFANG_REVICE_EXP);
			if(result != null){
				return result;
			}
		}
		long exp = roleTafang.getExp() * config.getRate();
		roleTafang.setExp(0l);
		roleTafang.setCal(0);
		roleTafangDao.cacheUpdate(roleTafang, userRoleId);
		roleExportService.incrExp(userRoleId, exp);
		
		//支线任务
		taskBranchService.completeBranch(userRoleId, BranchEnum.B10,1);
		return new Object[]{1,id};
	}
	
	@Autowired
	private TaskBranchService taskBranchService;
	
	public void addExp(Long userRoleId,String monsterId){
		RoleTafang roleTafang = getRoleTafang(userRoleId);
		if(roleTafang.getCal().equals(GameConstants.BOOLEAN_TRUE_TO_INT)){
			return;//已结算
		}
		MonsterConfig config = monsterExportService.load(monsterId);
		if(config != null){
			roleTafang.setExp(roleTafang.getExp() + config.getBasicExp());
			roleTafangDao.cacheUpdate(roleTafang, userRoleId);
			BusMsgSender.send2One(userRoleId, ClientCmdType.TAFANG_EXP_CHANGE, config.getBasicExp());
		}
	}
	
	public Object[] enterTaFang(Long userRoleId){
		if(stageControllExportService.inFuben(userRoleId)){
			return AppErrorCode.FUBEN_IS_IN_FUBEN;//在副本中
		}
		TaFangPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TAFANG);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		RoleTafang roleTafang = getRoleTafang(userRoleId);
		if(roleTafang.getJoinTime() >= config.getCount()){
			return AppErrorCode.TAFANG_NO_COUNT;//进入次数已满
		}
		if(roleTafang.getExp() > 0){
			return AppErrorCode.TAFANG_NO_RECIVE_LAST_EXP;//上次经验尚未领取
		}
		Map<String,Integer> items = config.getSendGift();
		if(items != null){
			Object[] result = roleBagExportService.checkPutInBag(items, userRoleId);
			if(result != null){
				return result;
			}
		}
		roleTafang.setCal(GameConstants.BOOLEAN_FALSE_TO_INT);
		roleTafang.setJoinTime(roleTafang.getJoinTime() + 1);
		roleTafangDao.cacheUpdate(roleTafang, userRoleId);
		
		roleBagExportService.putInBag(items, userRoleId, GoodsSource.GOODS_TAFANG, true);
		
		// 进入塔防副本
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config.getMap());
		if (dituCoinfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		// 发送到场景进入地图
		int birthPoint[] = dituCoinfig.getRandomBirth();
		int x = birthPoint[0];
		int y = birthPoint[1];
		BusMsgSender.send2BusInner(userRoleId,InnerCmdType.S_APPLY_CHANGE_STAGE,new Object[] { dituCoinfig.getId(), x, y,MapType.TAFANG_FUBEN_MAP });
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.FUBEN_TAFANG, null});
		return null;
	}
	
	public Object[] putTa(Long userRoleId,int id){
		String stageId = publicRoleStateExportService.getRolePublicStageId(userRoleId);
		if(stageId == null){
			return AppErrorCode.STAGE_IS_NOT_EXIST;//场景不存在
		}
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.TAFANG_FUBEN.equals(iStage.getStageType())){
			return AppErrorCode.STAGE_IS_NOT_EXIST;//场景不存在
		}
		TaFangStage stage = (TaFangStage)iStage;
		TaFangNpc npc = stage.getTaFangNpc(id);
		if(npc != null){
			return AppErrorCode.TAFANG_HAS_NPC;//已有
		}
		TaFangNpcConfig config = taFangNpcConfigExportService.getConfig(id);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoodsId(config.getItem(), config.getItemCount(), userRoleId, GoodsSource.GOODS_TAFANG, true, true);
		if(!bagSlots.isSuccee()){
			return bagSlots.getErrorCode();
		}
		MonsterConfig monsterConfig = monsterExportService.load(config.getNpcId());
		npc = MonsterFactory.createTaFangNpc(IdFactory.getInstance().generateNonPersistentId(), monsterConfig);
		npc.setPositionId(id);
		npc.setAttLine(config.getAttLines());
		stage.enter(npc, config.getZuobiao()[0], config.getZuobiao()[1]);
		
		return new Object[]{1,id};
	}
	
	public Object[] npcLevelUp(Long userRoleId,Integer id,Integer type){
		String stageId = publicRoleStateExportService.getRolePublicStageId(userRoleId);
		if(stageId == null){
			return AppErrorCode.STAGE_IS_NOT_EXIST;//场景不存在
		}
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.TAFANG_FUBEN.equals(iStage.getStageType())){
			return AppErrorCode.STAGE_IS_NOT_EXIST;//场景不存在
		}
		TaFangStage stage = (TaFangStage)iStage;
		TaFangNpc npc = stage.getTaFangNpc(id);
		if(npc == null){
			return AppErrorCode.TAFANG_NO_NPC;//当前位置没有NPC
		}
		TaFangNpcLevelConfig config = taFangNpcLevelConfigExportService.getConfig(npc.getMonsterId());
		if(config == null){
			return AppErrorCode.TAFANG_NPC_IS_MAX_LEVEL;//NPC已到最高等级
		}
		if(GameConstants.TAFANG_COST_TYPE_MANEY.equals(type)){
			TaFangPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TAFANG);
			if(publicConfig == null){
				return AppErrorCode.CONFIG_ERROR;
			}
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			if(role == null || role.getLevel() < publicConfig.getMoneyLevel()){
				RoleVipWrapper roleVip = roleVipInfoExportService.getRoleVipInfo(userRoleId);
				if(roleVip == null || roleVip.getVipLevel() < publicConfig.getMoneyVip()){
					return AppErrorCode.VIP_NOT_ENOUGH_LEVEL;
				}
			}
			
			int value = config.getMoney();
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, value, userRoleId, LogPrintHandle.CONSUME_TAFANG, true, LogPrintHandle.CBZ_TAFANG_SJ);
			if(result != null){
				return result;
			}
		}else if(GameConstants.TAFANG_COST_TYPE_GOLD.equals(type)){
			int value = config.getGold();
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, value, userRoleId, LogPrintHandle.CONSUME_TAFANG, true, LogPrintHandle.CBZ_TAFANG_SJ);
			if(result != null){
				return result;
			}
		}else{
			return AppErrorCode.CONFIG_ERROR;//参数错误
		}
		
		MonsterConfig monsterConfig = monsterExportService.load(config.getNextId());
		TaFangNpc newNpc = MonsterFactory.createTaFangNpc(IdFactory.getInstance().generateNonPersistentId(), monsterConfig);
		if(newNpc == null){
			ChuanQiLog.error("塔防npc创建失败，npcId:{},升级前id:{}",config.getNextId(),config.getNpcId());
		}
		newNpc.setPositionId(npc.getPositionId());
		newNpc.setAttLine(npc.getAttLine());
		stage.leave(npc);
		stage.enter(newNpc, npc.getPosition().getX(), npc.getPosition().getY());
		
		return new Object[]{1,id,type};
	}
	
	public void exitTaFang(Long userRoleId){
		if(!stageControllExportService.inFuben(userRoleId)){
			//不在副本中
			BusMsgSender.send2One(userRoleId, ClientCmdType.LEAVE_FUBEN, AppErrorCode.FUBEN_NOT_IN_FUBEN);
		}
		RoleTafang roleTafang = getRoleTafang(userRoleId);
		if(roleTafang.getCal().equals(GameConstants.BOOLEAN_FALSE_TO_INT)){
			calRoleTafang(roleTafang);
		}
		
		//发送到场景处理退出副本
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.S_EXIT_TAFANG_FUBEN, null);
	}
	
	private void calRoleTafang(RoleTafang roleTafang){
		RoleWrapper role = roleExportService.getLoginRole(roleTafang.getUserRoleId());
		TaFangLevelExpConfig config = taFangLevelExpConfigServiceExport.getConfigByLevel(role.getLevel());
		if(config == null){
			return;
		}
		long exp = (long)(roleTafang.getExp() * config.getRate());
		roleTafang.setExp(exp);
		roleTafang.setCal(GameConstants.BOOLEAN_TRUE_TO_INT);
		roleTafangDao.cacheUpdate(roleTafang, roleTafang.getUserRoleId());
	}
}
