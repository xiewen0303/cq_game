package com.junyou.bus.xunbao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tongyong.dao.ActityCountLogDao;
import com.junyou.bus.tongyong.entity.ActityCountLog;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.xunbao.configure.RfbXunBaoConfig;
import com.junyou.bus.xunbao.configure.RfbXunBaoConfigExportService;
import com.junyou.bus.xunbao.configure.RfbXunBaoConfigGroup;
import com.junyou.bus.xunbao.configure.RfbXunBaoConfigGroup.AllPeopleRewardsConfig;
import com.junyou.bus.xunbao.dao.RefbXunBaoDao;
import com.junyou.bus.xunbao.dao.RefbXunBaoLogDao;
import com.junyou.bus.xunbao.dao.XunBaoBagDao;
import com.junyou.bus.xunbao.entity.RefbXunbao;
import com.junyou.bus.xunbao.entity.XunBaoBag;
import com.junyou.bus.xunbao.entity.XunBaoLog;
import com.junyou.bus.xunbao.filter.RefbXunBaoFilter;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.WuPinZuBaoConfigExportService;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.XunBaoPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * 
 * @author zhongdian
 * @email  zhongdian@junyougame.com
 * @date 2015-10-9 上午10:34:27
 */
@Service
public class RefbXunBaoService {
	
	@Autowired
	private RefbXunBaoDao refbXunBaoDao;
	@Autowired
	private RefbXunBaoLogDao refbXunBaoLogDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private WuPinZuBaoConfigExportService wuPinZuBiaoConfigExportService; 
	@Autowired
	private GongGongShuJuBiaoConfigExportService publicConfigExportService;
	@Autowired
	private XunBaoBagDao xunbaoBagDao;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private ActityCountLogDao actityCountLogDao;
	
	public List<RefbXunbao> initXunbao(Long userRoleId){
		return refbXunBaoDao.initRefbXunbao(userRoleId);
	}
	
	public Object[] getAllRefbXunbaoCount(Integer subId){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		RfbXunBaoConfigGroup group = RfbXunBaoConfigExportService.getInstance().getRfbXunBaoConfig(subId);
		if(null == configSong || null == group){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		return new Object[]{subId,getQFXBCount(group,subId)};
	}
	public Object[] getRefbXunBaoData(Long userRoleId, Integer subId){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		RfbXunBaoConfigGroup group = RfbXunBaoConfigExportService.getInstance().getRfbXunBaoConfig(subId);
		if(null == configSong || null == group){
			return null;
		}
		RefbXunbao xunbao = getRefbXunBao(userRoleId,subId);
		//获取全服奖励配置
		Map<Integer, AllPeopleRewardsConfig> configs = group.getAllPeopleRewardsConfigs();
		List<Object> allPersonRewards = new ArrayList<Object>();
		if(null != configs && configs.size() > 0){
			for(AllPeopleRewardsConfig config:configs.values()){
				int configId = config.getId();
				Map<Integer, Integer> map = xunbao.getLingquStatusMap();
				Integer stutas = map.get(configId);
				if(!new Integer(1).equals(stutas)){
					stutas = 0;
				}
				Object[] obj = new Object[]{config.getTotalCount(),config.getGoodsId(),config.getGoodsCount(),stutas,configId};
				allPersonRewards.add(obj);
			}
		}
		
		//获取全服寻宝日志 
		List<XunBaoLog> xunbaoLogs = refbXunBaoLogDao.getXunbaonfoByIdDb(subId);
		Object[] logs = null;
		if(xunbaoLogs != null && xunbaoLogs.size() > 0){
			logs = new Object[xunbaoLogs.size()];
			
			int index = 0;
			for (XunBaoLog xunbaoLog : xunbaoLogs) {
				logs[index++] = new Object[]{xunbaoLog.getRoleName(),xunbaoLog.getGoodsId(),xunbaoLog.getGoodsCount()};
			}
		}
		ActityCountLog log = actityCountLogDao.getActivityCountBySubIdAndUser(subId, userRoleId);
		int count = 0;
		if(log != null){
			long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
			long upTime = log.getUpdateTime();
			long dTime = GameSystemTime.getSystemMillTime();
			if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
				actityCountLogDao.cleanActivityCount(subId, userRoleId);
			}else{
				count = log.getCount();
			}
		}	
		return new Object[]{group.getPic(),group.getShowGoods(),group.getTypeCountClient().toArray(),allPersonRewards.toArray(),logs,getQFXBCount(group,subId),group.getDes(),new Object[]{count,group.getMaxCount()}};
	}
	
	private RefbXunbao getRefbXunBao(Long userRoleId,int subId){
		
		List<RefbXunbao> list = refbXunBaoDao.cacheLoadAll(userRoleId, new RefbXunBaoFilter(subId));
		
		if(list == null || list.size() <= 0){
			RefbXunbao xunbao = new RefbXunbao();
			xunbao.setId(IdFactory.getInstance().generateId(ServerIdType.REFABU));
			xunbao.setFindCount(0);
			xunbao.setSubId(subId);
			xunbao.setUserRoleId(userRoleId);
			xunbao.setUpdateTime(GameSystemTime.getSystemMillTime());
			xunbao.setQmjlLingquStatus("");
			xunbao.setLingquStatusMap(new HashMap<Integer, Integer>());
			refbXunBaoDao.cacheInsert(xunbao, userRoleId);
			return xunbao;
		}
		return list.get(0);
	}
	
	/**
	 * 热发布寻宝
	 * @param userRoleId
	 * @param type
	 * @param busMsgQueue
	 * @return
	 */
	public Object[] xunbao(long userRoleId,int subId,int xunbaoId,BusMsgQueue busMsgQueue){
		RfbXunBaoConfigGroup group = RfbXunBaoConfigExportService.getInstance().getRfbXunBaoConfig(subId);
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null || group == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		//版本一样
//		if(configSong.getClientVersion() != version){
//			
//			//处理数据变化:
//			Object newSubHandleData = getRefbXunBaoData(userRoleId, subId);
//			Object[] data = new Object[]{subId,configSong.getClientVersion(),newSubHandleData};
//			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
//			return null;
//		}
		Map<Integer, Map<Integer, Integer>> xunbaoTypeMap = group.getTypeCount();
		if(null == xunbaoTypeMap || xunbaoTypeMap.size() < 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<Integer, Integer> xunbaoType = xunbaoTypeMap.get(xunbaoId);
		int times = 0;
		int needGold = 0;
		for(Map.Entry<Integer, Integer> entry : xunbaoType.entrySet()){
			times = entry.getKey();
			needGold = entry.getValue();
		}
		if(times <= 0 || needGold <= 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		boolean insert = true;
		//如果配置了次数
		if(group.getMaxCount() > 0){
			//判断玩家次数
			ActityCountLog log = actityCountLogDao.getActivityCountBySubIdAndUser(subId, userRoleId);
			if(log != null){
				insert = false;
				if(log.getCount() != null && log.getCount()+times > group.getMaxCount()){
					return AppErrorCode.ACTITY_MAX_COUNT;
				}
			}
		}
		//元宝验证
		if(needGold > 0 && null != accountExportService.isEnought(GoodsCategory.GOLD, needGold, userRoleId)){
			return AppErrorCode.XB_NO_GOLD;
		} 
		//获得物品
		Map<String,Integer> goodsMap = getXunBaoAwards(group,times,subId);
		int xunbaoCount = goodsMap.remove("xunbaoCount");
		Object[] checkBagFlag = checkPutIn(goodsMap,userRoleId);
		
		if(checkBagFlag != null){
			return checkBagFlag;
		}
		group.addAllXunbaoCount(xunbaoCount);
		
		//消耗元宝
		if(needGold > 0){
			accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, needGold, userRoleId, LogPrintHandle.CONSUME_REFABU_XUNBAO, true, LogPrintHandle.CBZ_REFABU_XUNBAO);
		}
		
		List<Object[]> outClients = new ArrayList<>();
		
		//物品进寻宝包裹
		putIn(userRoleId, goodsMap,outClients,busMsgQueue,LogPrintHandle.GET_REFABU_XUNBAO,subId);
		RefbXunbao xunbao = getRefbXunBao(userRoleId,subId);
		xunbao.setFindCount(xunbao.getFindCount()+times);
		refbXunBaoDao.cacheUpdate(xunbao, userRoleId);
		if(group.getMaxCount() > 0){
			if(insert){
				ActityCountLog log = new ActityCountLog();
				log.setUserRoleId(userRoleId);
				log.setCount(times);
				log.setUpdateTime(GameSystemTime.getSystemMillTime());
				actityCountLogDao.insertDb(log, subId);
			}else{
				actityCountLogDao.addActivityCount(subId, userRoleId, times);
			}
		}
		return new Object[]{1,subId,outClients.toArray(),times};
	}
	
	/**
	 *  获得寻宝对应的奖励
	 * @param findCount
	 * @param times
	 * @return
	 */
	private Map<String, Integer> getXunBaoAwards(RfbXunBaoConfigGroup group, int times, int subId) {
		Map<String, Integer> datas =new HashMap<String, Integer>();
		int xunbaoCount = 0;
		RfbXunBaoConfig config =null;
		for(int i = 0;i<times;i++){
			//获取全服寻宝次数并增加一次
			synchronized (GameConstants.REFB_QUANJU_XUNBAO_LOCK) {
				xunbaoCount++;
				//group.addAllXunbaoCount();
				//判断该次数是否在全服配置表内
				config = RfbXunBaoConfigExportService.getInstance().getXunBaoIdByCount(subId,getQFXBCount(group,subId));
			}
			if(config == null){
				ChuanQiLog.error("寻宝配置报异常：times:"+i);
				continue;
			}
			
			Object[] goods = Lottery.getRandomKeyByInteger(config.getGoodsOdds(), config.getAllOdds());
			if(goods == null){
				ChuanQiLog.error("寻宝配置表错误,权重获得物品为null:\t id="+config.getId()+"\t goodsOdds:"+config.getGoodsOdds()+"\t AllOdds:"+ config.getAllOdds());
				continue;
			}
			/**{goodsId,dropCount,isZB}*/ 
			String goodsId = (String)goods[0];
			int count = (Integer)goods[1];
			boolean isZB = (Boolean)goods[2];
			if(goods != null && goods.length == 3){
				if(isZB){ 
					//组包对应的物品信息 
					Map<String, Integer>  groupGoods = wuPinZuBiaoConfigExportService.componentRoll(goodsId);
					if(groupGoods != null){
						for (Entry<String, Integer> entry : groupGoods.entrySet()) {
							Integer oldCount = datas.get(entry.getKey());
							datas.put(entry.getKey(), entry.getValue()+(oldCount==null?0:oldCount));
						}
					} 
				}else{
					Integer oldCount = datas.get(goodsId);
					datas.put(goodsId, count+(oldCount==null?0:oldCount));
				}
			}
		}
		datas.put("xunbaoCount", xunbaoCount);
		return datas;
	}
	
	public int getXunbaoGoodsCount(long userRoleId){
		List<XunBaoBag> xunbaoBags = xunbaoBagDao.cacheAsynLoadAll(userRoleId);
		int bagCount = 0;
		if(xunbaoBags != null){
			bagCount = xunbaoBags.size();
		}
		
		return bagCount;
	}
	
	private Object[] checkPutIn(Map<String,Integer> datas,long userRoleId){
		if(datas== null || datas.size() == 0){
			return null;
		}
		
		int totalCount = 0;
		
		for (Entry<String, Integer> entry : datas.entrySet()) {
			String goodsId =entry.getKey();
			int count = entry.getValue();
			
			int maxStack =	BagUtil.getItemRepeatCount(goodsId);
			if(maxStack == 0) continue;
			totalCount+=count;
		}
		int nowBagSlotCount = getXunbaoGoodsCount(userRoleId);
		XunBaoPublicConfig publicConfig = publicConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_XUNBAO);
		if(nowBagSlotCount + totalCount > publicConfig.getMaxCapacity()){
			return AppErrorCode.XB_BAG_FULL;
		}
		return null;
	}
	
	private void putIn(long userRoleId,Map<String,Integer> goodsMap,List<Object[]> outClients,BusMsgQueue busMsgQueue,int type,int subId){
		if(goodsMap == null || goodsMap.size() == 0){
			return;
		}
		
		for (Map.Entry<String, Integer> entry : goodsMap.entrySet()){
			putInXunbaoBag(userRoleId, entry.getKey(), entry.getValue(),outClients,busMsgQueue,type,subId);
		}
	}
	
	private void putInXunbaoBag(long userRoleId,String goodsId,int count,List<Object[]> outClients,BusMsgQueue busMsgQueue,int type,int subId){
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
		if(goodsConfig == null){
			ChuanQiLog.debug("putInXunbaoBag goodsConfig is null goodsId:"+goodsId);
			return;
		}
		
		//保存广播日志并全服广播
		saveLogAndNotify(userRoleId, goodsConfig, count, busMsgQueue,type, subId);
		
		if(numberProp(goodsId, count,userRoleId)){
			return;
		}
		
		List<XunBaoBag> xunbaoBag = createXunbaoBag(userRoleId, goodsConfig, count);
		for (XunBaoBag goods : xunbaoBag) {
			xunbaoBagDao.cacheInsert(goods, userRoleId);
			outClients.add(new Object[]{goods.getId(),goods.getGoodsId(),goods.getGoodsCount()});
		}
	}
	/**
	 * 数值类型物品(游戏币,元宝,威望等)
	 * @param goodsConfig
	 * @param role
	 */
	private boolean numberProp(String moneyType,int count,long userRoleId){
		boolean flag = false; 
			if(moneyType.equals(ModulePropIdConstant.GOLD_GOODS_ID)
			||moneyType.equals(ModulePropIdConstant.MONEY_GOODS_ID)
			||moneyType.equals(ModulePropIdConstant.EXP_GOODS_ID)
			||moneyType.equals(ModulePropIdConstant.BIND_GOLD_GOODS_ID)) {
			
			flag=true;
			ChuanQiLog.error("寻宝出现了数值类型的奖励,moneyType:"+moneyType+"\t value:"+count);
		}
		return flag;
	}
	private List<XunBaoBag> createXunbaoBag(long userRoleId,GoodsConfig goodsConfig,int count){
		 List<XunBaoBag>  xunbaoBags = new ArrayList<>();
		
		int maxStack = goodsConfig.getMaxStack();
		int newCount = count/maxStack;
		
		for (int i=0; i<newCount ;i++) {
			xunbaoBags.add(createXunBaoGoods(userRoleId, goodsConfig.getId(), maxStack));
		}
		
		int lastCount = count%maxStack;
		for (int i=0; i<lastCount ;i++) {
			xunbaoBags.add(createXunBaoGoods(userRoleId, goodsConfig.getId(), 1));
		}
		
		return xunbaoBags;
	}
	private XunBaoBag createXunBaoGoods(long userRoleId,String goodsId,int count){
		XunBaoBag tmpBag = new XunBaoBag();
		tmpBag.setId(getGULIdentity());
		tmpBag.setUserRoleId(userRoleId);
		tmpBag.setGoodsId(goodsId);
		tmpBag.setGoodsCount(count); 
		tmpBag.setCreateTime(GameSystemTime.getSystemMillTime());
		return tmpBag;
	}
	
	/**
	 * 寻宝背包物品主键Id
	 * @return
	 */
	public static long getGULIdentity(){
		return IdFactory.getInstance().generateId(ServerIdType.XUNBAO_TEMP_GUID);
	}
	/*private XunBaoBag createXunBaoGoods(long userRoleId,String goodsId,int count, String randomAttrs){
		XunBaoBag tmpBag = new XunBaoBag();
		tmpBag.setId(IdFactory.getInstance().generateId(ServerIdType.XUNBAO_TEMP_GUID));
		tmpBag.setUserRoleId(userRoleId);
		tmpBag.setGoodsId(goodsId);
		tmpBag.setGoodsCount(count); 
		tmpBag.setRandomAttrs(randomAttrs); 
		tmpBag.setCreateTime(GameSystemTime.getSystemMillTime());
		return tmpBag;
	}*/
	/**
	 * 保存广播日志并全服广播
	 * @param userRoleId
	 * @param goodsConfig
	 * @param count
	 * @param busMsgQueue
	 */
	private void saveLogAndNotify(long userRoleId,GoodsConfig goodsConfig,int count,BusMsgQueue busMsgQueue,int type,int subId){
		//全局通知
		if(goodsConfig.isNotify()){
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			
			if(role.isGm()){
				return;//GM不广播
			}
			
			//记录通知
			String roleName = role.getName();
			if(type ==  LogPrintHandle.GET_REFABU_XUNBAO){
				busMsgQueue.addBroadcastMsg(ClientCmdType.NOTICE_XUNBAO_INFO, new Object[]{subId, new Object[]{roleName,goodsConfig.getId(),count}});
				
				XunBaoLog xunbaoLog = new XunBaoLog();
				xunbaoLog.setRoleName(roleName);
				xunbaoLog.setGoodsId(goodsConfig.getId());
				xunbaoLog.setGoodsCount(count);
				xunbaoLog.setCreateTime(GameSystemTime.getSystemMillTime());
				
				try {
					refbXunBaoLogDao.insertDb(xunbaoLog,subId);
				} catch (Exception e) {
					ChuanQiLog.error("",e);
				}
			}
		}
	}
	/**
	 * 领取全服寻宝奖励
	 * @param userRoleId
	 * @param subId
	 * @param configId
	 * @return
	 */
	public Object[] getQFXBRewards(long userRoleId, int subId, int configId){
		//判断配置是否正确
		RfbXunBaoConfigGroup group = RfbXunBaoConfigExportService.getInstance().getRfbXunBaoConfig(subId);
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(null == configSong || null == group){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		Map<Integer, AllPeopleRewardsConfig> configs = group.getAllPeopleRewardsConfigs();
		if(null == configs){
			return AppErrorCode.CONFIG_ERROR;
		}
		AllPeopleRewardsConfig config = configs.get(configId);
		if(null == config){
			return AppErrorCode.CONFIG_ERROR;
		}
		//获取全服寻宝次数
		if(0 > group.getAllXunbaoCount()){
			List<Integer> list = refbXunBaoDao.getAllXunbaoCount(subId);
			group.setAllXunbaoCount(list.get(0));
		}
		if(group.getAllXunbaoCount() < config.getTotalCount()){
			return AppErrorCode.KAIFU_TIAOJIAN_ERROR;
		}
		//判断是否领取过奖励
		RefbXunbao xunbao = getRefbXunBao(userRoleId,subId);
		Map<Integer, Integer> statusMap = xunbao.getLingquStatusMap();
		if(null == statusMap){
			statusMap = new HashMap<Integer, Integer>();
		}
		if(new Integer(1).equals(statusMap.get(configId))){
			return AppErrorCode.KAIFU_YI_LINGQU;
		}
		
		Map<String, Integer> goods = new HashMap<String, Integer>();
		goods.put(config.getGoodsId(), config.getGoodsCount());
		
		Object[] bagFlag = roleBagExportService.checkPutInBag(goods, userRoleId);
		if(bagFlag != null){
			return bagFlag;
		}
		//修改领取状态
		statusMap.put(configId, 1);
		xunbao.setLingquStatusMap(statusMap);
		refbXunBaoDao.cacheUpdate(xunbao, userRoleId);
		
		roleBagExportService.putInBag(goods, userRoleId, GoodsSource.QF_XUNBAO, true);
		
		return new Object[]{1,subId,configId};
	}
	private int getQFXBCount(RfbXunBaoConfigGroup group, Integer subId){
		int res = group.getAllXunbaoCount();
		if(0 > res){
			List<Integer> list = refbXunBaoDao.getAllXunbaoCount(subId);
			res = list.get(0) == null ? 0:list.get(0);
			group.setAllXunbaoCount(res);
		}
		return res;
	}
}
