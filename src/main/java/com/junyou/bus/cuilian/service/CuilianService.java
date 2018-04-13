package com.junyou.bus.cuilian.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.cuilian.configure.export.CuilianConsumeConfig;
import com.junyou.bus.cuilian.configure.export.CuilianConsumeExportService;
import com.junyou.bus.cuilian.constant.CuilianConstants;
import com.junyou.bus.cuilian.dao.RoleCuilianDao;
import com.junyou.bus.cuilian.entity.RoleCuilian;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.entity.VipConfig;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.export.VipConfigExportService;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.CuilianPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class CuilianService {

	@Autowired
	private RoleCuilianDao roleCuilianDao;
	@Autowired
	private VipConfigExportService vipConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private CuilianConsumeExportService cuilianConsumeExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;

	public List<RoleCuilian> initRoleCuilian(Long userRoleId) {
		return roleCuilianDao.initRoleCuilian(userRoleId);
	}

	public CuilianPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_CUILIAN);
	}

	public void createCuilian(Long userRoleId, int moneyTimes,
			long lastMoneyTime, int bgoldTimes, long lastBgoldTime,
			int goldTimes, long lastGoldTime) {
		RoleCuilian po = new RoleCuilian();
		po.setUserRoleId(userRoleId);
		po.setMoneyTimes(moneyTimes);
		po.setLastMoneyTime(lastMoneyTime);
		po.setBgoldTimes(bgoldTimes);
		po.setLastBgoldTime(lastBgoldTime);
		po.setGoldTimes(goldTimes);
		po.setLastGoldTime(lastGoldTime);
		roleCuilianDao.cacheInsert(po, userRoleId);
	}

	public RoleCuilian getRoleCuilian(Long userRoleId) {
		RoleCuilian ret = roleCuilianDao.cacheLoad(userRoleId, userRoleId);
		if (ret != null) {
			long currentTime = GameSystemTime.getSystemMillTime();
			boolean update = false;
			if (!DatetimeUtil.dayIsToday(currentTime, ret.getLastMoneyTime())) {
				ret.setLastMoneyTime(currentTime);
				ret.setMoneyTimes(0);
				update = true;
			}
			if (!DatetimeUtil.dayIsToday(currentTime, ret.getLastBgoldTime())) {
				ret.setLastBgoldTime(currentTime);
				ret.setBgoldTimes(0);
				update = true;
			}
			if (!DatetimeUtil.dayIsToday(currentTime, ret.getLastGoldTime())) {
				ret.setLastGoldTime(currentTime);
				ret.setGoldTimes(0);
				update = true;
			}
			if (update) {
				roleCuilianDao.cacheUpdate(ret, userRoleId);
			}
		}
		return ret;
	}

	public Object[] getCuilianInfo(Long userRoleId) {
		RoleCuilian info = getRoleCuilian(userRoleId);
		if (info == null) {
			long currentTime = GameSystemTime.getSystemMillTime();
			createCuilian(userRoleId, 0, currentTime, 0, currentTime, 0,
					currentTime);
			info = getRoleCuilian(userRoleId);
		}
//		int vipLevel = roleVipInfoExportService.getRoleVipInfo(userRoleId)
//				.getVipLevel();
//		VipConfig vipConfig = vipConfigExportService
//				.getVipConfigByLevel(vipLevel);
		// int maxMoneyTimes = vipConfig.getCuilianMoneyTimes();
		// int maxBgoldTimes = vipConfig.getCuilianBgoldTimes();
		// int maxGoldTimes = vipConfig.getCuilianGoldTimes();
		return new Object[] { AppErrorCode.SUCCESS, new Object[]{info.getMoneyTimes(),
				info.getBgoldTimes(), info.getGoldTimes()} };
	}

	public Object[] cuilian(Long userRoleId, int type) {
		RoleCuilian info = getRoleCuilian(userRoleId);
		if (info == null) {
			long currentTime = GameSystemTime.getSystemMillTime();
			createCuilian(userRoleId, 0, currentTime, 0, currentTime, 0,
					currentTime);
			info = getRoleCuilian(userRoleId);
		}
		int vipLevel = roleVipInfoExportService.getRoleVipInfo(userRoleId)
				.getVipLevel();
		VipConfig vipConfig = vipConfigExportService
				.getVipConfigByLevel(vipLevel);
		int maxTimes = 0;
		int currentTimes = 0;
		if (type == CuilianConstants.CUILIAN_TYPE_1) {
			maxTimes = vipConfig.getCuilianMoneyTimes();
			currentTimes = info.getMoneyTimes();
		} else if (type == CuilianConstants.CUILIAN_TYPE_2) {
			maxTimes = vipConfig.getCuilianBgoldTimes();
			currentTimes = info.getBgoldTimes();
		} else if (type == CuilianConstants.CUILIAN_TYPE_3) {
			maxTimes = vipConfig.getCuilianGoldTimes();
			currentTimes = info.getGoldTimes();
		} else {
			return AppErrorCode.CUI_LIAN_TYPE_ERROR;
		}
		if (maxTimes <= currentTimes) {
			return AppErrorCode.CUI_LIAN_TIMES_LIMIT;
		}
		CuilianConsumeConfig consumeConfig = cuilianConsumeExportService
				.loadByTimes(currentTimes+1);
		if (consumeConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		CuilianPublicConfig publicConfig = getPublicConfig();
		int roleLevel = roleExportService.getLoginRole(userRoleId).getLevel();
		// 校验消耗,且加奖励
		if (type == CuilianConstants.CUILIAN_TYPE_1) {
			Object[] result = accountExportService.decrCurrencyWithNotify(
					GoodsCategory.MONEY, (int) consumeConfig.getNeedMoney(),
					userRoleId, LogPrintHandle.CONSUME_CUILIAN, true,
					LogPrintHandle.CBZ_CUILIAN);
			if (result != null) {
				return result;
			}
			long exp = (long) Math.rint(Math.pow(roleLevel, 0.8)
					* publicConfig.getMoneyExp());
			roleExportService.incrExp(userRoleId, exp);
			info.setMoneyTimes(currentTimes + 1);
			info.setLastMoneyTime(GameSystemTime.getSystemMillTime());
		}
		if (type == CuilianConstants.CUILIAN_TYPE_2) {
			Object[] result = accountExportService.decrCurrencyWithNotify(
					GoodsCategory.BGOLD, (int) consumeConfig.getNeedBgold(),
					userRoleId, LogPrintHandle.CONSUME_CUILIAN, true,
					LogPrintHandle.CBZ_CUILIAN);
			if (result != null) {
				return result;
			}else{
				//腾讯OSS消费上报
				if(PlatformConstants.isQQ()){
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,(int) consumeConfig.getNeedBgold(),LogPrintHandle.CONSUME_CUILIAN,QQXiaoFeiType.CONSUME_CUILIAN,1});
				}
			}
			long zhenqi = (long) Math.rint(Math.pow(roleLevel, 0.8)
					* publicConfig.getBgoldZhenqi());
			roleExportService.addZhenqi(userRoleId, zhenqi);
			info.setBgoldTimes(currentTimes + 1);
			info.setLastBgoldTime(GameSystemTime.getSystemMillTime());
		}
		if (type == CuilianConstants.CUILIAN_TYPE_3) {
			Object[] result = accountExportService.decrCurrencyWithNotify(
					GoodsCategory.GOLD, (int) consumeConfig.getNeedGold(),
					userRoleId, LogPrintHandle.CONSUME_CUILIAN, true,
					LogPrintHandle.CBZ_CUILIAN);
			if (result != null) {
				return result;
			}else{
				//腾讯OSS消费上报
				if(PlatformConstants.isQQ()){
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,(int) consumeConfig.getNeedGold(),LogPrintHandle.CONSUME_CUILIAN,QQXiaoFeiType.CONSUME_CUILIAN,1});
				}
			}
			long exp = (long) Math.rint(Math.pow(roleLevel, 0.8)
					* Math.pow(currentTimes+1, 0.5) * publicConfig.getGoldExp());
			roleExportService.incrExp(userRoleId, exp);
			long zhenqi =(long) Math.rint(Math.pow(roleLevel, 0.8)
					* Math.pow(currentTimes+1, 0.5) * publicConfig.getGoldZhenqi());
			roleExportService.addZhenqi(userRoleId, zhenqi);
			long money = (long) Math.rint(Math.pow(roleLevel, 0.8)
					* Math.pow(currentTimes+1, 0.5) * publicConfig.getGoldMoney());
			accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY,
					money, userRoleId, LogPrintHandle.GET_CUILIAN,
					LogPrintHandle.GBZ_CUILIAN);
			info.setGoldTimes(currentTimes + 1);
			info.setLastGoldTime(GameSystemTime.getSystemMillTime());
		}
		roleCuilianDao.cacheUpdate(info, userRoleId);
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.CUILIAN, null});
		return new Object[]{AppErrorCode.SUCCESS,type};
	}
}
