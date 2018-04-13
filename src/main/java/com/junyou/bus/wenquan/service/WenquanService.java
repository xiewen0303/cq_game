package com.junyou.bus.wenquan.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.junyou.bus.role.data.ChenmiData;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.spring.container.DataContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wenquan.configure.export.YinLiangJiangLiConfig;
import com.junyou.bus.wenquan.configure.export.YinLiangJiangLiConfigExportService;
import com.junyou.bus.wenquan.constants.WenquanConstants;
import com.junyou.bus.wenquan.dao.RoleWenquanDao;
import com.junyou.bus.wenquan.entity.RoleWenquan;
import com.junyou.bus.wenquan.vo.WenquanRankVo;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.WenquanPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.wenquan.WenquanManager;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.accessor.AccessType;

@Service
public class WenquanService {
	@Autowired
	private RoleWenquanDao roleWenquanDao;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private YinLiangJiangLiConfigExportService yinLiangJiangLiConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private DataContainer dataContainer;
	
	public WenquanPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_WENQUAN);
	}

	public RoleWenquan initRoleWenquan(Long userRoleId) {
		List<RoleWenquan> list = roleWenquanDao.initRoleWenquan(userRoleId);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public void createRoleWenquan(Long userRoleId, Integer goldTimes,
			Integer moneyTimes, Integer playTimes) {
		Long currentTime = GameSystemTime.getSystemMillTime();

		RoleWenquan wenquan = new RoleWenquan();
		wenquan.setUserRoleId(userRoleId);
		wenquan.setGoldTimes(goldTimes);
		wenquan.setGoldUpdateTime(currentTime);
		wenquan.setMoneyTimes(moneyTimes);
		wenquan.setMoneyUpdateTime(currentTime);
		wenquan.setPlayTimes(playTimes);
		wenquan.setPlayUpdateTime(currentTime);
		roleWenquanDao.cacheInsert(wenquan, userRoleId);
	}

	public RoleWenquan getRoleWenquan(Long userRoleId) {
		RoleWenquan ret = roleWenquanDao.cacheLoad(userRoleId, userRoleId);
		if (ret == null) {
			return null;
		}
		Long currentTime = GameSystemTime.getSystemMillTime();
		boolean update = false;

		Long moneyUpdateTime = ret.getMoneyUpdateTime();
		if (!DatetimeUtil.dayIsToday(currentTime, moneyUpdateTime)) {
			ret.setMoneyTimes(0);
			ret.setMoneyUpdateTime(currentTime);
			update = true;
		}

		Long playUpdateTime = ret.getPlayUpdateTime();
		if (!DatetimeUtil.dayIsToday(currentTime, playUpdateTime)) {
			ret.setPlayTimes(0);
			ret.setPlayUpdateTime(currentTime);
			update = true;
		}
		if (update) {
			roleWenquanDao.cacheUpdate(ret, userRoleId);
		}
		return ret;
	}

	private ReentrantLock lock = new ReentrantLock();
	private static int TRY_LOCK_TIME = 1;
	private List<WenquanRankVo> rankList = null;

	public List<WenquanRankVo> getRank(int num) {
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			if (rankList == null) {
				return null;
			}

			updateTimes();

			if (rankList.size() <= num) {
				return rankList;
			} else {
				return rankList.subList(0, num);
			}
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
		return null;
	}

	public List<WenquanRankVo> getRank() {
		return rankList;
	}

	public void initRank() {
		WenquanPublicConfig publicConfig = getPublicConfig();
		if (publicConfig == null) {
			return;
		}
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			rankList = roleWenquanDao.getWenquanRankVo(publicConfig.getPaim());
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
	}

	/**
	 * -1:榜单未初始化;-2：未上榜 ;>0 代表名次
	 * 
	 * @param userRoleId
	 * @return
	 */
	public int getMyRank(Long userRoleId) {
		WenquanPublicConfig publicConfig = getPublicConfig();
		if (publicConfig == null) {
			return -1;
		}
		getRank(publicConfig.getPaim());
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			if (rankList == null) {
				return -1;
			} else {
				for (WenquanRankVo e : rankList) {
					if (e.getUserRoleId().longValue() == userRoleId.longValue()) {
						return e.getRank();
					}
				}
				return -2;
			}
		} catch (InterruptedException e1) {
			ChuanQiLog.error("", e1);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
		return -1;
	}

	/**
	 * 
	 前三名排行榜 我的排名 我的元宝聚灵次数 今天银两聚灵次数 今天互动次数 当前倍区 累计经验 累计真气
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] getWenquanInfo(Long userRoleId) {
		Object[] ret = new Object[9];
		ret[0] = AppErrorCode.SUCCESS;
		List<WenquanRankVo> top3 = getRank(3);
		if (top3 != null && top3.size() > 0) {
			Object[] top3Array = new Object[top3.size()];
			for (int i = 0; i < top3.size(); i++) {
				WenquanRankVo vo = top3.get(i);
				top3Array[i] = new Object[] { vo.getName(), vo.getUserRoleId(),
						vo.getRank(), vo.getGoldTimes() };
			}
			ret[1] = top3Array;
		} else {
			ret[1] = null;
		}
		ret[2] = WenquanManager.getManager().getZhenqi(userRoleId);
		ret[3] = WenquanManager.getManager().getJingyan(userRoleId);
		RoleWenquan wenquan = getRoleWenquan(userRoleId);
		if (wenquan == null) {
			ret[4] = 0;
			ret[5] = 0;
			ret[6] = -2;
			ret[7] = 0;
			ret[8] = 0;
		} else {
			ret[4] = wenquan.getMoneyTimes();
			ret[5] = wenquan.getPlayTimes();
			ret[6] = getMyRank(userRoleId);
			ret[7] = wenquan.getGoldTimes();
			ret[8] = WenquanManager.getManager().isInHighArea(userRoleId) ? 1
					: 0;
		}
		return ret;
	}

	/**
	 * 从0 开始
	 * 
	 * @param pageIndex
	 * @return
	 */

	public Object[] getRankList(int pageIndex, int recordsPerPage) {
		WenquanPublicConfig publicConfig = getPublicConfig();
		if (publicConfig == null) {
			return new Object[] { 0, null, 0 };
		}
		getRank(publicConfig.getPaim());
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			if (rankList == null || rankList.size() == 0) {
				return new Object[] { 0, null, 0 };
			}
			Object[] ret = new Object[3];
			ret[0] = rankList.size();
			int fromIndex = pageIndex * recordsPerPage;
			int toIndex = fromIndex + recordsPerPage;
			if (fromIndex > rankList.size() - 1) {
				toIndex = rankList.size();
				fromIndex = toIndex - recordsPerPage - 1;
				if (fromIndex < 0) {
					fromIndex = 0;
				}
			}
			if (toIndex > rankList.size() - 1) {
				toIndex = rankList.size();
			}
			List<WenquanRankVo> retList = rankList.subList(fromIndex, toIndex);
			Object[] rankArray = new Object[retList.size()];
			for (int i = 0; i < retList.size(); i++) {
				WenquanRankVo vo = retList.get(i);
				rankArray[i] = new Object[] { vo.getName(), vo.getUserRoleId(),
						vo.getRank(), vo.getGoldTimes() };
			}
			ret[1] = rankArray;
			int totalSize = rankList.size();
			int totalPage = (totalSize % recordsPerPage == 0) ? (totalSize / recordsPerPage)
					: (totalSize / recordsPerPage + 1);
			for (int i = 0; i < totalPage; i++) {
				if ((i + 1) * recordsPerPage > fromIndex) {
					pageIndex = i;
					break;
				}
			}
			ret[2] = pageIndex;
			return ret;
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
		return new Object[] { 0, null, 0 };
	}

	public Object[] julin(Long userRoleId, int type) {
		if (!ActiveUtil.isWenquan()) {
			return AppErrorCode.WENQUAN_ACTIVE_NOT_START;
		}
		WenquanPublicConfig publicConfig = getPublicConfig();
		if (publicConfig == null) {
			return null;
		}
		RoleWenquan wenquan = getRoleWenquan(userRoleId);
		Long jingyan = 0L;
		Long zhenqi = 0L;
		if (type == WenquanConstants.JULIN_TYPE_MONEY) {
			// 验证次数
			int maxTime = yinLiangJiangLiConfigExportService
					.getMaxYinLiangTimes();
			if (wenquan != null && wenquan.getMoneyTimes() >= maxTime) {
				return AppErrorCode.WENQUAN_MONEY_TIMES_LIMIT;
			}
			int currentTimes = wenquan == null ? 1
					: wenquan.getMoneyTimes() + 1;
			YinLiangJiangLiConfig config = yinLiangJiangLiConfigExportService
					.getConfig(currentTimes);
			int needMoney = config.getNeedmoney();
			// 验证银两
			Object[] result = accountExportService.decrCurrencyWithNotify(
					GoodsCategory.MONEY, needMoney, userRoleId,
					LogPrintHandle.CONSUME_MONEY_JU_LIN, true,
					LogPrintHandle.CBZ_MONEY_JU_LIN);
			if (result != null) {
				return result;
			}
			jingyan = config.getJingyan() * 1L;
			zhenqi = config.getZhenqi() * 1L;
			if (wenquan == null) {
				createRoleWenquan(userRoleId, 0, 1, 0);
			} else {
				wenquan.setMoneyTimes(currentTimes);
				wenquan.setMoneyUpdateTime(GameSystemTime.getSystemMillTime());
				roleWenquanDao.cacheUpdate(wenquan, userRoleId);
			}
			WenquanManager.getManager().addJulinSet(userRoleId);
		}
		if (type == WenquanConstants.JULIN_TYPE_GOLD) {
			// 验证元宝
			Object[] result = accountExportService.decrCurrencyWithNotify(
					GoodsCategory.GOLD, publicConfig.getNeedgold(), userRoleId,
					LogPrintHandle.CONSUME_GOLD_JU_LIN, true,
					LogPrintHandle.CBZ_GOLD_JU_LIN);
			if (result != null) {
				return result;
			}else{
				if(PlatformConstants.isQQ()){
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB, publicConfig.getNeedgold(),LogPrintHandle.CONSUME_GOLD_JU_LIN,QQXiaoFeiType.CONSUME_GOLD_JU_LIN,1});
				}
			}
			jingyan = publicConfig.getJingyan() * 1L;
			zhenqi = publicConfig.getZhenqi() * 1L;
			if (wenquan == null) {
				createRoleWenquan(userRoleId, 1, 0, 0);
			} else {
				wenquan.setGoldTimes(wenquan.getGoldTimes() + 1);
				wenquan.setGoldUpdateTime(GameSystemTime.getSystemMillTime());
				roleWenquanDao.cacheUpdate(wenquan, userRoleId);
			}
			WenquanManager.getManager().addJulinSet(userRoleId);
			WenquanManager.getManager().addConsumeGold(userRoleId,
					publicConfig.getNeedgold());
			if (!WenquanManager.getManager().isInHighArea(userRoleId)
					&& WenquanManager.getManager().getConsumeGold(userRoleId) >= getPublicConfig()
							.getNeedgold1()) {
				BusMsgSender.send2Stage(userRoleId,
						InnerCmdType.WENQUAN_GO_HIGH_AREA, null);
			}
			cacheUpdate(userRoleId);
		}
		// 加经验
		if (jingyan > 0) {
			roleExportService.incrExp(userRoleId, jingyan);
			WenquanManager.getManager().addJingyan(userRoleId, jingyan);
		}
		// 加真气
		if (zhenqi > 0) {
			roleExportService.addZhenqi(userRoleId, zhenqi);
			WenquanManager.getManager().addZhenqi(userRoleId, zhenqi);
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.WENQUAN_EXP_ZHENQI,
				new Object[] { jingyan, zhenqi });
		return new Object[] { AppErrorCode.SUCCESS, type };
	}

	public void cacheUpdate(Long userRoleId) {
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			boolean has = false;
			if (rankList == null) {
				rankList = new ArrayList<WenquanRankVo>();
			} else {
				for (WenquanRankVo vo : rankList) {
					if (vo.getUserRoleId().longValue() == userRoleId.longValue()) {
						has = true;
						vo.setGoldTimes(vo.getGoldTimes() + 1);
						vo.setGoldUpdateTime(GameSystemTime.getSystemMillTime());
					}
				}
			}
			if (!has) {
				String name = roleExportService.getUserRole(userRoleId).getName();
				WenquanRankVo vo = new WenquanRankVo();
				vo.setUserRoleId(userRoleId);
				vo.setName(name);
				vo.setRank(0);
				vo.setGoldTimes(1);
				vo.setGoldUpdateTime(GameSystemTime.getSystemMillTime());
				rankList.add(vo);
			}
			Collections.sort(rankList);
			for(int i=1;i<=rankList.size();i++){
				rankList.get(i-1).setRank(i);
			}
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
	}

	/**
	 * 清除跨天的元宝洗礼次数
	 * @param userRoleId
	 */
	public void updateTimes() {
		try {

			Long lastModifyTime = dataContainer.getData(GameConstants.COMPONENET_PAOZAO_RANK,GameConstants.COMPONENET_PAOZAO_RANK);
			if (ObjectUtil.isEmpty(lastModifyTime) || !ObjectUtil.dayIsToday(lastModifyTime)) {

				if (rankList == null) {
					rankList = new ArrayList<WenquanRankVo>();
				} else {
					long nowTime = System.currentTimeMillis();
					for (WenquanRankVo vo : rankList) {
						long lastUpdatetime = vo.getGoldUpdateTime();
						if(!ObjectUtil.dayIsToday(lastUpdatetime)){
							vo.setGoldTimes(0);
							vo.setGoldUpdateTime(nowTime);
						}
					}
				}

				Collections.sort(rankList);
				for(int i=1; i<=rankList.size(); i++){
					rankList.get(i-1).setRank(i);
				}

				dataContainer.putData(GameConstants.COMPONENET_PAOZAO_RANK,GameConstants.COMPONENET_PAOZAO_RANK,System.currentTimeMillis());
			}
		} catch (Exception e) {
			ChuanQiLog.error("update times error:", e);
		}
	}

	public Object[] rankSyn(Long userRoleId) {
		Object[] ret = new Object[3];
		List<WenquanRankVo> top3 = getRank(3);
		if (top3 != null && top3.size() > 0) {
			Object[] top3Array = new Object[top3.size()];
			for (int i = 0; i < top3.size(); i++) {
				WenquanRankVo vo = top3.get(i);
				top3Array[i] = new Object[] { vo.getName(), vo.getUserRoleId(),
						vo.getRank(), vo.getGoldTimes() };
			}
			ret[0] = top3Array;
		} else {
			ret[0] = null;
		}
		RoleWenquan wenquan = getRoleWenquan(userRoleId);
		if (wenquan == null) {
			ret[1] = 0;
			ret[2] = 0;
		} else {
			ret[1] = getMyRank(userRoleId);
			ret[2] = wenquan.getGoldTimes();
		}
		return ret;

	}

	public Object[] play(Long userRoleId, Long targetUserRoleId, int type) {
		if (!ActiveUtil.isWenquan()) {
			return AppErrorCode.WENQUAN_ACTIVE_NOT_START;
		}
		String stageId = publicRoleStateExportService
				.getRolePublicStageId(userRoleId);
		IStage stage = StageManager.getStage(stageId);
		if (stage.getStageType() != StageType.WENQUAN) {
			return AppErrorCode.ERR;
		}
		WenquanPublicConfig publicConfig = getPublicConfig();
		if (publicConfig == null) {
			return null;
		}
		RoleWenquan wenquan = getRoleWenquan(userRoleId);
		if (wenquan != null
				&& wenquan.getPlayTimes() >= publicConfig.getCishu()) {
			if (type == 0) {
				return AppErrorCode.PLAY_TIMES_LIMIT_1;
			} else {
				return AppErrorCode.PLAY_TIMES_LIMIT_2;
			}
		}
		UserRole userRole = roleExportService.getUserRole(userRoleId);
		if (userRole == null) {
			return AppErrorCode.ERR;
		}
		if (userRoleId.longValue() == targetUserRoleId.longValue()) {
			if (type == 0) {
				return AppErrorCode.WENQUAN_CAN_NOT_PLAY_MYSELF_2;
			} else {
				return AppErrorCode.WENQUAN_CAN_NOT_PLAY_MYSELF_1;
			}
		}
		UserRole targetUserRole = roleExportService
				.getUserRole(targetUserRoleId);
		if (targetUserRole == null) {
			return AppErrorCode.ERR;
		}
		Long jingyan = userRole.getLevel() * publicConfig.getJingyan1() * 1L;
		Long zhenqi = userRole.getLevel() * publicConfig.getZhenqi1() * 1L;
		// 加经验
		if (jingyan > 0) {
			roleExportService.incrExp(userRoleId, jingyan);
			WenquanManager.getManager().addJingyan(userRoleId, jingyan);
		}
		// 加真气
		if (zhenqi > 0) {
			roleExportService.addZhenqi(userRoleId, zhenqi);
			WenquanManager.getManager().addZhenqi(userRoleId, zhenqi);
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.WENQUAN_EXP_ZHENQI,
				new Object[] { jingyan, zhenqi });
		if (wenquan == null) {
			createRoleWenquan(userRoleId, 0, 0, 1);
		} else {
			wenquan.setPlayTimes(wenquan.getPlayTimes() + 1);
			wenquan.setPlayUpdateTime(GameSystemTime.getSystemMillTime());
			roleWenquanDao.cacheUpdate(wenquan, userRoleId);
		}
		wenquan = getRoleWenquan(userRoleId);
		BusMsgSender.send2Many(stage.getAllRoleIds(),
				ClientCmdType.WENQUAN_PLAY_NOTICE,
				new Object[] { type, userRoleId, targetUserRoleId, jingyan,
						zhenqi, userRole.getName(), targetUserRole.getName() });
		
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_XIZAOFEIZAO, 1});
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.WENQUAN, null});
		
		return new Object[] { AppErrorCode.SUCCESS, wenquan.getPlayTimes(),
				targetUserRole.getName() };
	}

	public boolean isTodayActive(Long userRoleId) {
		RoleWenquan wenquan = null;
		if (publicRoleStateExportService.isPublicOnline(userRoleId)) {
			wenquan = getRoleWenquan(userRoleId);
		} else {
			wenquan = roleWenquanDao.load(userRoleId, userRoleId,
					AccessType.getDirectDbType());
		}
		if (wenquan == null) {
			return false;
		} else {
			Long currentTime = GameSystemTime.getSystemMillTime();
			return DatetimeUtil.dayIsToday(currentTime,
					wenquan.getGoldUpdateTime());
		}
	}
	private static long one_day = 24*60*60*1000L;
	
	public void updateJulinUpdateTime(Long userRoleId){
		RoleWenquan wenquan = null;
		long currentTime = GameSystemTime.getSystemMillTime();
		if (publicRoleStateExportService.isPublicOnline(userRoleId)) {
			wenquan = getRoleWenquan(userRoleId);
		} else {
			wenquan = roleWenquanDao.load(userRoleId, userRoleId,
					AccessType.getDirectDbType());
		}
		if(wenquan != null ){
			if (wenquan.getGoldTimes()>0){
				wenquan.setGoldUpdateTime(currentTime -one_day );
			}
			if(wenquan.getMoneyTimes()>0){
				wenquan.setMoneyUpdateTime(currentTime -one_day );
			}
			if (publicRoleStateExportService.isPublicOnline(userRoleId)) {
				roleWenquanDao.cacheUpdate(wenquan, userRoleId);
			}else{
				roleWenquanDao.update(wenquan, userRoleId, AccessType.getDirectDbType());
			}
		}
	}
	public void handleUserModifyNameEvent(Long userRoleId, String afterName) {
		//更新排行榜
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			if (rankList == null || rankList.size() == 0) {
				return ;
			}
			for(WenquanRankVo e:rankList){
				if(e.getUserRoleId().equals(userRoleId)){
					e.setName(afterName);
				}
			}
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
	}
}
