package com.junyou.bus.happycard.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.happycard.configure.export.HappyCardConfig;
import com.junyou.bus.happycard.configure.export.HappyCardConfigExportService;
import com.junyou.bus.happycard.configure.export.HappyCardGroupConfig;
import com.junyou.bus.happycard.configure.export.HappyCardItem;
import com.junyou.bus.happycard.dao.RefabuHappyCardDao;
import com.junyou.bus.happycard.dao.RefabuHappyCardItemDao;
import com.junyou.bus.happycard.entity.RefabuHappyCard;
import com.junyou.bus.happycard.entity.RefabuHappyCardItem;
import com.junyou.bus.recharge.export.RechargeExportService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class HappyCardService {
	@Autowired
	private RefabuHappyCardItemDao refabuHappyCardItemDao;
	@Autowired
	private RefabuHappyCardDao refabuHappyCardDao;
	@Autowired
	private RechargeExportService rechargeExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleExportService roleExportService;

	public List<RefabuHappyCard> initRefabuHappyCard(Long userRoleId) {
		return refabuHappyCardDao.initRefabuHappyCard(userRoleId);
	}

	private RefabuHappyCard getHappCardInfo(Long userRoleId, final Integer subId) {
		List<RefabuHappyCard> list = refabuHappyCardDao.cacheLoadAll(
				userRoleId, new IQueryFilter<RefabuHappyCard>() {
					private boolean stop = false;

					@Override
					public boolean check(RefabuHappyCard info) {
						if (subId.equals(info.getSubId())) {
							stop = true;
						}
						return stop;
					}

					@Override
					public boolean stopped() {
						return stop;
					}
				});
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private RefabuHappyCard createRefabuHappyCard(Long userRoleId,
			Integer subId, String items) {
		RefabuHappyCard refabuHappyCard = new RefabuHappyCard();
		refabuHappyCard.setId(IdFactory.getInstance().generateId(
				ServerIdType.COMMON));
		refabuHappyCard.setSubId(subId);
		refabuHappyCard.setUserRoleId(userRoleId);
		refabuHappyCard.setItems(items);
		refabuHappyCard.setItemIndex("");
		refabuHappyCard.setBeforeYb(0);
		refabuHappyCard.setFanTimes(0);
		refabuHappyCard.setMulti(1);
		refabuHappyCard.setUpdateTime(GameSystemTime.getSystemMillTime());
		refabuHappyCardDao.cacheInsert(refabuHappyCard, userRoleId);
		return getHappCardInfo(userRoleId, subId);
	}

	public List<RefabuHappyCardItem> initRefabuHappyCardItem(Long userRoleId) {
		return refabuHappyCardItemDao.initRefabuHappyCardItem(userRoleId);
	}

	private List<RefabuHappyCardItem> getRefabuHappyCardItem(Long userRoleId,
			final Integer subId) {
		List<RefabuHappyCardItem> list = refabuHappyCardItemDao.cacheLoadAll(
				userRoleId, new IQueryFilter<RefabuHappyCardItem>() {

					@Override
					public boolean check(RefabuHappyCardItem info) {
						return subId.equals(info.getSubId());
					}

					@Override
					public boolean stopped() {
						return false;
					}
				});
		if (list != null && list.size() > 0) {
			Collections.sort(list);
			return list;
		}
		return null;

	}

	private void createRefabuHappyCardItem(Long userRoleId, Integer subId,
			String items, int itemIndex) {
		RefabuHappyCardItem refabuHappyCardItem = new RefabuHappyCardItem();
		refabuHappyCardItem.setId(IdFactory.getInstance().generateId(
				ServerIdType.COMMON));
		refabuHappyCardItem.setSubId(subId);
		refabuHappyCardItem.setUserRoleId(userRoleId);
		refabuHappyCardItem.setItems(items);
		refabuHappyCardItem.setItemIndex(itemIndex);
		refabuHappyCardItem.setUpdateTime(GameSystemTime.getSystemMillTime());
		refabuHappyCardItemDao.cacheInsert(refabuHappyCardItem, userRoleId);
	}

	public Object[] getInfo(Long userRoleId, Integer subId) {
		HappyCardGroupConfig config = HappyCardConfigExportService
				.getInstance().loadByMap(subId);
		if (config == null) {
			return null;
		}
		ActivityConfigSon activity = ActivityAnalysisManager.getInstance()
				.loadRunByZiId(subId);
		if (activity == null) {
			return null;
		}
		RefabuHappyCard info = getHappCardInfo(userRoleId, subId);
		if (info == null) {
			String initItems = randomInitItems(config);
			info = createRefabuHappyCard(userRoleId, subId, initItems);
		}else{
			//判断活动循环数据
			info = updateJianCe(subId, info,config);
		}
		int totalTimes = config.getTimes(getUserChongzhi(userRoleId, subId));
		Object[] ret = new Object[7];
		ret[0] = totalTimes - info.getFanTimes();
		ret[1] = analysItemStr(userRoleId, subId, info.getItems(), true);
		ret[2] = getRewardRecord(userRoleId, subId);
		ret[3] = getResetTimes(userRoleId, subId);
		ret[4] = config.getYbArray();
		ret[5] = info.getFanTimes();
		ret[6] = config.getDesc();
		return ret;
	}
	
	private RefabuHappyCard updateJianCe(int subId,RefabuHappyCard info,HappyCardGroupConfig config){
		//判断活动是否结束
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
		long upTime = info.getUpdateTime();
		long dTime = GameSystemTime.getSystemMillTime();
		if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
			
			refabuHappyCardDao.cacheDelete(info.getId(), info.getUserRoleId());
			String initItems = randomInitItems(config);
			info = createRefabuHappyCard(info.getUserRoleId(), subId, initItems);
			
			List<RefabuHappyCardItem> list = getRefabuHappyCardItem(info.getUserRoleId(),subId);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					RefabuHappyCardItem item = list.get(i);
					refabuHappyCardItemDao.cacheDelete(item.getId(), info.getUserRoleId());
				}
			}
		}
		return info;
	}
	

	public Object[] getInfo2(Long userRoleId, int subId) {
		RefabuHappyCard info = getHappCardInfo(userRoleId, subId);
		if (info == null) {
			return new Object[] { subId, 0, 0 };
		}
		HappyCardGroupConfig config = HappyCardConfigExportService
				.getInstance().loadByMap(subId);
		if (config == null) {
			return new Object[] { subId, 0, 0 };
		}
		int totalTimes = config.getTimes(getUserChongzhi(userRoleId, subId));
		return new Object[] { subId, totalTimes - info.getFanTimes(),
				getResetTimes(userRoleId, subId) };
	}

	public String randomInitItems(HappyCardGroupConfig configGroup) {
		StringBuffer sb = new StringBuffer(500);
		List<Integer> idList = configGroup.getIdList();
		for (Integer e : idList) {
			HappyCardConfig config = configGroup.getConfig(e);
			Integer index = Lottery.getRandomKeyByInteger(
					config.getWeightMap(), config.getTotalWeight());
			HappyCardItem item = config.getItem(index);
			if (item.isItem()) {
				sb.append(item.getItemsStr());
			} else {
				sb.append(0).append(":").append(item.getMulti());
			}
			sb.append("|");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	private Object[] analysItemStr(Long userRoleId, int subId, String str,
			boolean sort) {
		if (str == null || str.trim().equals("")) {
			return null;
		}
		String[] strArray = str.split("\\|");
		Object[] ret = new Object[strArray.length];
		if (sort) {
			RefabuHappyCard info = getHappCardInfo(userRoleId, subId);
			if (info == null) {
				return null;
			}
			int fanTimes = info.getFanTimes();
			if (fanTimes > 0) {
				List<RefabuHappyCardItem> list = getRefabuHappyCardItem(
						userRoleId, subId);
				if (list == null || list.size() < fanTimes) {
					return null;
				}
				HappyCardGroupConfig config = HappyCardConfigExportService
						.getInstance().loadByMap(subId);
				List<Integer> indexList = new ArrayList<Integer>();
				for (int i = 0; i < config.getIdList().size(); i++) {
					indexList.add(i);
				}
				List<RefabuHappyCardItem> subList = list.subList(list.size()
						- fanTimes, list.size());
				for (int i = 0; i < subList.size(); i++) {
					RefabuHappyCardItem item = subList.get(i);
					indexList.remove(new Integer(item.getItemIndex()));
					String[] infoArray = item.getItems().split(":");
					ret[i] = new Object[] { infoArray[0],
							Integer.parseInt(infoArray[1]) };
				}
				int count = subList.size();
				for (Integer e : indexList) {
					String[] infoArray = strArray[e].split(":");
					ret[count] = new Object[] { infoArray[0],
							Integer.parseInt(infoArray[1]) };
					count = count + 1;
				}
				return ret;
			}
		}
		for (int i = 0; i < strArray.length; i++) {
			String[] infoArray = strArray[i].split(":");
			ret[i] = new Object[] { infoArray[0],
					Integer.parseInt(infoArray[1]) };
		}
		return ret;
	}

	private Map<Long, Integer> chongzhiMap = new HashMap<Long, Integer>();
	
	private Map<Long,Integer> chongzhiSubMap = new HashMap<Long,Integer>();

	public void handleUserRecharge(Long userRoleId, int num) {
		chongzhiMap.remove(userRoleId);
		chongzhiSubMap.remove(userRoleId);
	}

	public void onlineHandle(Long userRoleId) {
		chongzhiMap.remove(userRoleId);
		chongzhiSubMap.remove(userRoleId);
	}

	private int getUserChongzhi(Long userRoleId, int subId) {
		Integer chongzhiSubId = chongzhiSubMap.get(userRoleId);
		if(chongzhiSubId!=null){
			if(chongzhiSubId.intValue() != subId){
				chongzhiMap.remove(userRoleId);
			}
		}
		Integer num = chongzhiMap.get(userRoleId);
		if (num != null) {
			return num;
		}
		num = getActualChongzhi(userRoleId, subId);
		RefabuHappyCard info = getHappCardInfo(userRoleId, subId);
		if (info != null) {
			num = num - info.getBeforeYb();
		}
		chongzhiMap.put(userRoleId, Math.max(num, 0));
		chongzhiSubMap.put(userRoleId, subId);
		return num;
	}

	private int getActualChongzhi(Long userRoleId, int subId) {
		ActivityConfigSon activity = ActivityAnalysisManager.getInstance()
				.loadRunByZiId(subId);
		if (activity == null) {
			return 0;
		}
		long startTime = activity.getStartTimeByMillSecond();
		long endTime = activity.getEndTimeByMillSecond();
		return rechargeExportService.getTotalRechargesByTime(userRoleId,
				startTime, endTime);
	}

	private Object[] getRewardRecord(Long userRoleId, int subId) {
		List<RefabuHappyCardItem> list = getRefabuHappyCardItem(userRoleId,
				subId);
		if (list == null || list.size() == 0) {
			return null;
		}
		Object[] ret = new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			RefabuHappyCardItem e = list.get(i);
			String items = e.getItems();
			String[] itemArray = items.split(":");
			ret[i] = new Object[] { itemArray[0],
					Integer.parseInt(itemArray[1]) };
		}
		return ret;
	}

	private static Random random = new Random();

	public Object[] fanCard(Long userRoleId, int subId, int version, int index) {
		HappyCardGroupConfig config = HappyCardConfigExportService.getInstance().loadByMap(subId);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		ActivityConfigSon activity = ActivityAnalysisManager.getInstance()
				.loadRunByZiId(subId);
		if (activity == null) {
			return null;
		}
		// 版本不一样
		if (activity.getClientVersion() != version) {
			// 处理数据变化:
			Object newSubHandleData = getInfo(userRoleId, subId);
			Object[] data = new Object[] { subId, activity.getClientVersion(),
					newSubHandleData };
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
			return null;
		}
		RefabuHappyCard info = getHappCardInfo(userRoleId, subId);
		if (info == null) {
			return AppErrorCode.ERR;
		}
		int totalTimes = config.getTimes(getUserChongzhi(userRoleId, subId));
		int fanTimes = info.getFanTimes();
		if (totalTimes <= fanTimes) {
			return AppErrorCode.CARD_FAN_TIMES_LIMIT;
		}
		if (index != fanTimes + 1) {
			return AppErrorCode.CARD_INEDX_ERROR;
		}
		String itemsStr = info.getItems();
		Object[] itemArray = analysItemStr(userRoleId, subId, itemsStr, false);
		int randomIndex = randomIndex2(itemArray.length, info.getItemIndex());
		Object[] obj = (Object[]) itemArray[randomIndex];
		String goodsId = (String) obj[0];
		Integer num = (Integer) obj[1];
		int showNum = num;
		if (goodsId.equals("0")) {
			// 倍数
			if (info.getMulti() == 1) {
				info.setMulti(num);
			} else {
				info.setMulti(num + info.getMulti());
			}
			createRefabuHappyCardItem(userRoleId, subId, goodsId + ":" + num,
					randomIndex);
		} else {
			GoodsConfig goodsConfig = goodsConfigExportService
					.loadById(goodsId);
			if (goodsConfig == null) {
				return AppErrorCode.CONFIG_ERROR;
			}
			Object[] checkResult = roleBagExportService.checkPutInBag(goodsId,
					info.getMulti() * num, userRoleId);
			if (checkResult != null) {
				return checkResult;
			}
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put(goodsId, info.getMulti() * num);
			roleBagExportService.putInBag(map, userRoleId,
					GoodsSource.GOODS_HAPPY_CARD, true);
			createRefabuHappyCardItem(userRoleId, subId,
					goodsId + ":" + info.getMulti() * num, randomIndex);
			showNum = info.getMulti() * num;
			if (goodsConfig.isNotify()) {
				String name = roleExportService.getLoginRole(userRoleId)
						.getName();
				BusMsgSender.send2All(
						ClientCmdType.FAN_CARD_NOTICE,
						new Object[] {
								subId,
								new Object[] { goodsId, info.getMulti() * num,
										name }, activity.getSkey() });
			}
//			info.setMulti(1);
		}
		info.setItemIndex(info.getItemIndex() + String.valueOf(randomIndex)
				+ ":");
		info.setUpdateTime(GameSystemTime.getSystemMillTime());
		info.setFanTimes(info.getFanTimes() + 1);
		refabuHappyCardDao.cacheUpdate(info, userRoleId);
		ChuanQiLog.info("userRoleId={} fan happy card", userRoleId);
		return new Object[] { AppErrorCode.SUCCESS, subId, index,
				new Object[] { goodsId, showNum },
				totalTimes - info.getFanTimes() };
	}

	private int randomIndex(int length, String indexListStr) {
		List<Integer> left = new ArrayList<Integer>();
		String[] array = indexListStr.split(":");
		for (int i = 0; i < length; i++) {
			boolean flag = true;
			for (String e : array) {
				if (String.valueOf(i).equals(e)) {
					flag = false;
					break;
				}
			}
			if (flag) {
				left.add(i);
			}
		}
		int index = random.nextInt(left.size());
		return left.get(index);
	}

	private int randomIndex2(int length, String indexListStr) {
		if (indexListStr == null || indexListStr.trim().equals("")) {
			return 0;
		}
		String[] array = indexListStr.split(":");
		int max = 0;
		for (String e : array) {
			Integer index = Integer.parseInt(e);
			if (index > max) {
				max = index;
			}
		}
		return max + 1;
	}

	public int getResetTimes(Long userRoleId, int subId) {
		HappyCardGroupConfig config = HappyCardConfigExportService
				.getInstance().loadByMap(subId);
		if (config == null) {
			return 0;
		}
		int yb = getUserChongzhi(userRoleId, subId);
		if (yb >= config.getChongzhi()) {
			return 1;
		}
		return 0;
	}

	public Object[] reset(Long userRoleId, int subId, int version) {
		HappyCardGroupConfig config = HappyCardConfigExportService
				.getInstance().loadByMap(subId);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		ActivityConfigSon activity = ActivityAnalysisManager.getInstance()
				.loadRunByZiId(subId);
		if (activity == null) {
			return null;
		}
		// 版本不一样
		if (activity.getClientVersion() != version) {
			// 处理数据变化:
			Object newSubHandleData = getInfo(userRoleId, subId);
			Object[] data = new Object[] { subId, activity.getClientVersion(),
					newSubHandleData };
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
			return null;
		}
		RefabuHappyCard info = getHappCardInfo(userRoleId, subId);
		if (info == null) {
			return AppErrorCode.ERR;
		}
		if (getResetTimes(userRoleId, subId) <= 0) {
			return AppErrorCode.CARD_RESET_TIMES_LIMIT;
		}
		info.setBeforeYb(getActualChongzhi(userRoleId, subId));
		info.setFanTimes(0);
		info.setMulti(1);
		info.setItemIndex("");
		String newItems = randomInitItems(config);
		info.setItems(newItems);
		info.setUpdateTime(GameSystemTime.getSystemMillTime());
		refabuHappyCardDao.cacheUpdate(info, userRoleId);
		chongzhiMap.remove(userRoleId);
		chongzhiSubMap.remove(userRoleId);
		ChuanQiLog.info("userRoleId={} reset happy card", userRoleId);
		return new Object[] { AppErrorCode.SUCCESS, subId,
				analysItemStr(userRoleId, subId, newItems, false),
				getResetTimes(userRoleId, subId),
				config.getTimes(getUserChongzhi(userRoleId, subId)) };
	}
}
