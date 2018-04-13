package com.junyou.bus.lj.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.lj.configure.export.LianJinConfig;
import com.junyou.bus.lj.configure.export.LianJinConfigExportService;
import com.junyou.bus.lj.configure.export.LianJinRankConfig;
import com.junyou.bus.lj.configure.export.LianJinRankConfigExportService;
import com.junyou.bus.lj.constants.LJConstants;
import com.junyou.bus.lj.dao.RoleLjDao;
import com.junyou.bus.lj.entity.RoleLj;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.service.UserRoleService;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.lottery.RandomUtil;

@Service
public class LJService {
	
	@Autowired
	private RoleLjDao roleLjDao;
	@Autowired
	private LianJinConfigExportService lianJinConfigExportService;
	@Autowired
	private LianJinRankConfigExportService lianJinRankConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleBagExportService bagExpService;
	@Autowired
	private UserRoleService userRoleService;
	
	
	public RoleLj initRoleLJ(Long userRoleId) {
		return roleLjDao.initRoleLjInfo(userRoleId);
	}
	
	public Object[] getInfo(Long userRoleId) {
		RoleLj roleLj = getRoleLjInfo(userRoleId);
		return new Object[]{roleLj.getTypeCounts(),roleLj.getExp(),roleLj.getLevel(),roleLj.getLblqs().toArray()};
	}

	public Object[] ljGetAward(Long userRoleId,int id) {
		RoleLj roleLj = getRoleLjInfo(userRoleId);
		
		if(roleLj.getLblqs().contains(id)){
			return AppErrorCode.XIULIAN_YI_LINGQU_JIFEN;
		}
		
		LianJinRankConfig config = lianJinRankConfigExportService.loadByLevel(id);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		if(roleLj.getLevel() < config.getLvl()){
			return AppErrorCode.LEVEL_NOT_ENOUGH;
		}
		
		Map<String,Integer> awards = config.getAwards();
		if(awards != null){
			Object[] checkFlag = bagExpService.checkPutGoodsAndNumberAttr(awards, userRoleId);
			if(checkFlag != null){
				return checkFlag;
			}
			bagExpService.putGoodsAndNumberAttr(awards, userRoleId, GoodsSource.LJ, LogPrintHandle.GET_LJ, LogPrintHandle.GBZ_LJ, true);
		}
		roleLj.addLblq(id);
		roleLjDao.cacheUpdate(roleLj, userRoleId);
		
		return new Object[]{1,id};
	}
	
	private int getMoney(RoleLj roleLj) {
		LianJinRankConfig config = lianJinRankConfigExportService.loadByLevel(roleLj.getLevel());
		if(config == null){
			ChuanQiLog.error("LianJinRankConfig is null,id="+roleLj.getLevel());
			return 0;
		}
		
		UserRole userRole = userRoleService.getUserRole(roleLj.getUserRoleId());
		return userRole.getLevel() * config.getBasemoney() +  RandomUtil.getIntRandomValue(config.getExtramoney()+1);
	}

	private void addExp(RoleLj roleLj,int exp){
		int result = roleLj.getExp() + exp;
		
		int maxLevel = lianJinRankConfigExportService.getMaxLevel();
		if(roleLj.getLevel() >= maxLevel){
			return;
		}
		int addLevel = 0;
		for (int i = roleLj.getLevel();i < maxLevel;i++) {
			LianJinRankConfig config = lianJinRankConfigExportService.loadByLevel(i);
			if(config == null){
				ChuanQiLog.error("LianJinRankConfig is null,level="+i);
				break;
			}
			
			if(result >= config.getMaxexp()){
				addLevel++;
				result -= config.getMaxexp();
			}
		}
		
		if(roleLj.getLevel() >= maxLevel){
			LianJinRankConfig config = lianJinRankConfigExportService.loadByLevel(maxLevel-1);
			result = config.getMaxexp();
		}
		roleLj.setExp(result);
		roleLj.setLevel(roleLj.getLevel() + addLevel);
	}
	
	public Object[] optLjInfo(Long userRoleId, int type) {
		RoleLj roleLj = getRoleLjInfo(userRoleId);
		int maxCount = lianJinConfigExportService.getMax(type);			
		
		int count = CovertObjectUtil.object2int(roleLj.getTypeCounts().get(type));
		if(count >= maxCount){
			return AppErrorCode.COUNT_FINISH;
		}
		
		LianJinConfig config = lianJinConfigExportService.loadByTypeAndCount(type,count+1);
		if(config ==  null){
			return AppErrorCode.CONFIG_ERROR;
		}
		int needVal = config.getNeedcount();
		if(type == LJConstants.type2){
			Object[] checkFlags = accountExportService.decrCurrencyWithNotify(GoodsCategory.BGOLD, needVal, userRoleId, LogPrintHandle.GET_LJ,true, LogPrintHandle.GBZ_LJ);//(GoodsCategory.BGOLD, needVal, userRoleId);
			if(checkFlags != null){
				return checkFlags;
			}
		}else if(type == LJConstants.type3){
			Object[] checkFlags = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, needVal, userRoleId,LogPrintHandle.GET_LJ,true, LogPrintHandle.GBZ_LJ); //
			if(checkFlags != null){
				return checkFlags;
			}
		}
		
		int money = getMoney(roleLj);
		accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.GET_LJ, LogPrintHandle.GBZ_LJ);
		
		roleLj.getTypeCounts().put(type, count + 1);
	
		roleLj.setLastModifyTime(System.currentTimeMillis());
		addExp(roleLj, config.getExp());
		roleLjDao.cacheUpdate(roleLj, userRoleId);
		
		return new Object[]{1,type,money,roleLj.getExp(),roleLj.getLevel()};
	}
	
	
	public RoleLj getRoleLjInfo(long userRoleId){
		RoleLj roleLj = roleLjDao.cacheLoad(userRoleId, userRoleId);
		if(roleLj == null){
			roleLj = new RoleLj();
			roleLj.setUserRoleId(userRoleId);
			roleLj.setExp(0);
			roleLj.setLastModifyTime(0L);
			roleLj.setLevel(1);
			roleLjDao.cacheInsert(roleLj, userRoleId);
		}
		
		if(roleLj.getLastModifyTime() < DatetimeUtil.getDate00Time()){
			roleLj.setTypeCount("");
			roleLj.getTypeCounts().clear();
			roleLj.setLastModifyTime(System.currentTimeMillis());
		}
		return roleLj;
	}
}