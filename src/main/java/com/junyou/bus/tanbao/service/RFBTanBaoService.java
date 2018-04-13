package com.junyou.bus.tanbao.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tanbao.configure.TanSuoBaoZangConfig;
import com.junyou.bus.tanbao.configure.TanSuoBaoZangConfigExportService;
import com.junyou.bus.tanbao.configure.TanSuoBaoZangConfigGroup;
import com.junyou.bus.tanbao.dao.RefabuTanbaoDao;
import com.junyou.bus.tanbao.entity.RefabuTanbao;
import com.junyou.bus.tanbao.filter.TanSuoBaoZangFilter;
import com.junyou.bus.tongyong.dao.ActityCountLogDao;
import com.junyou.bus.tongyong.entity.ActityCountLog;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class RFBTanBaoService {

	@Autowired
	private RefabuTanbaoDao refabuTanbaoDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private ActityCountLogDao actityCountLogDao;
	
	public List<RefabuTanbao> initRefabuTanbao(Long userRoleId) {
		return refabuTanbaoDao.initRefabuTanbao(userRoleId);
	}

	private RefabuTanbao createTanbao(Long userRoleId,int subId){
		RefabuTanbao tanbao = new RefabuTanbao();
		tanbao.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		tanbao.setSubId(subId);
		tanbao.setUserRoleId(userRoleId);
		tanbao.setStatus(1);
		tanbao.setWangchengCount(0);
		tanbao.setCreateTime(new Timestamp(System.currentTimeMillis()));
		tanbao.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		refabuTanbaoDao.cacheInsert(tanbao, userRoleId);
		
		return tanbao;
	}
	
	private RefabuTanbao getTanBao(Long userRoleId,int subId){
		List<RefabuTanbao> tanbaoList = refabuTanbaoDao.cacheLoadAll(userRoleId, new TanSuoBaoZangFilter(subId));
		if(tanbaoList == null || tanbaoList.size() <= 0){
			RefabuTanbao tanbao = createTanbao(userRoleId,subId);
			
			return tanbao;
		}else{
			RefabuTanbao tanbao = tanbaoList.get(0);
			return tanbao;
		}
		
	}


	public Object[] tanbao(Long userRoleId, int suoyin,int subId,Integer version,BusMsgQueue busMsgQueue) {
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		
		//版本不一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[]{subId,configSong.getClientVersion(),newSubHandleData};
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
			return null;
		}
		TanSuoBaoZangConfigGroup configGroup = TanSuoBaoZangConfigExportService.getInstance().loadByMap(subId);
		
		if(configGroup == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		boolean insert = true;
		//如果配置了次数
		if(configGroup.getMaxCount() > 0){
			//判断玩家次数
			ActityCountLog log = actityCountLogDao.getActivityCountBySubIdAndUser(subId, userRoleId);
			if(log != null){
				insert = false;
				if(log.getCount() != null && log.getCount()+1 > configGroup.getMaxCount()){
					return AppErrorCode.ACTITY_MAX_COUNT;
				}
			}
		}
		TanSuoBaoZangConfig config = configGroup.getTanSuoBaoZangConfig(suoyin);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		RefabuTanbao tanbao = getTanBao(userRoleId,subId);
		//判断宝藏是否已经开启
		if(isOpen(tanbao.getStatus(), suoyin)){
			return AppErrorCode.BAOZANG_NOT_OPEN;
		}
		//开启宝藏需要的元宝
		int yb = config.getGold();
		//元宝是否足够
		if(yb > 0){
			Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD,yb, userRoleId);
			if(null != goldError){ 
				return goldError;
			}
		}
		
		//探宝
		String good = Lottery.getRandomKeyByInteger(config.getItemMap());
		if(good == null || "".equals(good)){
			return AppErrorCode.CONFIG_ERROR;
		}
		Object[] obj = good.split(":");
		if(obj == null || obj.length < 2){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		//消耗元宝
		roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, config.getGold(), userRoleId, LogPrintHandle.CONSUME_REFABU_TANBAO, true,LogPrintHandle.CBZ_REFABU_TANBAO);
		if(PlatformConstants.isQQ()){
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB, config.getGold(),LogPrintHandle.CONSUME_DAOMO,QQXiaoFeiType.CONSUME_TANSUOBAOZANG,1});
		}
		
		//发放奖励
		Map<String, Integer> goodMap = new HashMap<String, Integer>();
		
		goodMap.put(obj[0].toString(), Integer.parseInt(obj[1].toString()));
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(goodMap, userRoleId);
		if(bagCheck != null){
			return AppErrorCode.BAG_NOEMPTY;
			/*String content = EmailUtil.getCodeEmail(AppErrorCode.DAOMO_EMAIL);
			emailExportService.sendEmailToOne(userRoleId, content,GameConstants.EMAIL_TYPE_SINGLE, good);*/
		}else{
			//物品进背包
			roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.TAN_SUO_BAO_ZANG, LogPrintHandle.GET_REFABU_TANGBAO, LogPrintHandle.GBZ_REFABU_TANBAO, true);
		}
		//如果不是永不关闭的宝藏，则在探索之后关闭
		if(GameConstants.OPEN_ID != suoyin){
			tanbao.setStatus(downStatus(tanbao.getStatus(), suoyin));
		}
		//如果探索的是王城宝藏，则探索王城次数加1
		Integer pro = 0;
		if(GameConstants.WANG_CHENG_ID == suoyin){
			tanbao.setWangchengCount(tanbao.getWangchengCount()+1);
		}else{
			//几率开启下级宝藏
			Map<Integer, Float> map = getOdd(config.getJilv());//开启几率
			pro = Lottery.getRandomKey(map);
			//成功 开启下级宝藏，失败不处理
			if(pro.intValue() == GameConstants.SUCCESS){
				tanbao.setStatus(updateStatus(tanbao.getStatus(), suoyin+1));
			}
		}
		//更新数据
		refabuTanbaoDao.cacheUpdate(tanbao, userRoleId);
		
		//全服日志
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(obj[0].toString()); 
		if(goodsConfig.isNotify()){
			saveLogAndNotify(subId, userRoleId, good, configSong.getSkey(), busMsgQueue);
		}
		if(configGroup.getMaxCount() > 0){
			if(insert){
				ActityCountLog log = new ActityCountLog();
				log.setUserRoleId(userRoleId);
				log.setCount(1);
				log.setUpdateTime(GameSystemTime.getSystemMillTime());
				actityCountLogDao.insertDb(log, subId);
			}else{
				actityCountLogDao.addActivityCount(subId, userRoleId, 1);
			}
		}
		return new Object[]{1,subId,suoyin,good,pro.intValue(),tanbao.getWangchengCount()};
	}

	private void saveLogAndNotify(int subId,long userRoleId,String goods,String key,BusMsgQueue busMsgQueue){
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		String roleName = role.getName();
		if(role.isGm()){
			return;//GM不广播
		}
		
		busMsgQueue.addBroadcastMsg(ClientCmdType.TANBAO_TUISONG, new Object[]{subId,roleName,goods,key});
	}
	
	
	private RefabuTanbao updateJianCe(int subId,RefabuTanbao tanbao){
		//判断活动是否结束
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
		long upTime = tanbao.getUpdateTime().getTime();
		long dTime = GameSystemTime.getSystemMillTime();
		if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
			tanbao.setStatus(1);
			tanbao.setWangchengCount(0);
			tanbao.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			refabuTanbaoDao.cacheUpdate(tanbao, tanbao.getUserRoleId());
		}
		return tanbao;
	}
	

	public Object[] lingqu(Long userRoleId, int suoyin,int subId,Integer version) {
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		
		//版本不一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[]{subId,configSong.getClientVersion(),newSubHandleData};
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
			return null;
		}
		TanSuoBaoZangConfigGroup configGroup = TanSuoBaoZangConfigExportService.getInstance().loadByMap(subId);
		
		if(configGroup == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Object[] wangChengInfo = configGroup.getWangChengInfo(suoyin);
		if(wangChengInfo == null || wangChengInfo.length < 2){
			return AppErrorCode.CONFIG_ERROR;
		}
		int count = (int) wangChengInfo[0];
		String good = wangChengInfo[1].toString();
		RefabuTanbao tanbao = getTanBao(userRoleId,subId);
		//判断探索王城的次数是否到达要求
		if(tanbao.getWangchengCount().intValue() < count){
			return AppErrorCode.WC_NOT_COUNT;
		}
		//判断是否已经领取奖励
		if(!isOpen(tanbao.getStatus(), getSuoyinById(suoyin))){
			return AppErrorCode.WC_YI_LINGQU;
		}
		//领取奖励
		Map<String, Integer> goodMap = new HashMap<String, Integer>();
		Object[] obj = good.split(":");
		if(obj == null || obj.length < 2){
			return AppErrorCode.CONFIG_ERROR;
		}
		goodMap.put(obj[0].toString(), Integer.parseInt(obj[1].toString()));
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(goodMap, userRoleId);
		if(bagCheck != null){
			return AppErrorCode.BAG_NOEMPTY;
		}else{
			//物品进背包
			roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.TAN_SUO_BAO_ZANG_WANG, LogPrintHandle.GET_REFABU_TANGBAO_WANG, LogPrintHandle.GBZ_REFABU_TANBAO_WANG, true);
		}
		//修改状态
		tanbao.setStatus(updateStatus(tanbao.getStatus(), getSuoyinById(suoyin)));
		//更新
		refabuTanbaoDao.cacheUpdate(tanbao, userRoleId);
		
		return new Object[]{1,subId,suoyin};
	}

	
	/**
	 * 获取进阶几率   1：成功几率
	 * @param odd
	 * @return
	 */
	private Map<Integer, Float> getOdd(float odd){
		int i = 1;
		Map<Integer, Float> map = new HashMap<Integer, Float>();
		map.put(GameConstants.SUCCESS, odd);
		map.put(GameConstants.FAILURE, i-odd);
		
		return map;
	}
	
	/**
	 * 判断是否已经开启或领取过奖励
	 * @param state
	 * @return true:未开启  false:已开启
	 */
	public static Boolean isOpen(Integer state, Integer suoyin){
		if(!state.equals(0)){
			suoyin = suoyin.intValue()-1;
			
			return (state >> suoyin & 1) == 0;
		}
		return false;
	}
	
	/**
	 * 修改状态
	 * @param state
	 * @return
	 */
	public static Integer updateStatus(Integer state, Integer suoyin){
		suoyin = suoyin.intValue() - 1;
		
		return (1 << suoyin) | state;
	}
	/**
	 * 关闭
	 * @param state
	 * @return
	 */
	public static Integer downStatus(Integer state, Integer suoyin){
		suoyin = suoyin.intValue() - 1;
		
		return ~(1<<suoyin) & state;
	}
	
	/**
	 * 索引转成ID
	 * @param suoyin
	 * @return
	 */
	private Integer getSuoyinById(int suoyin){
		if(suoyin == GameConstants.tbOneCount){
			return 6;
		}else if(suoyin == GameConstants.tbTwoCount){
			return 7;
		}else if(suoyin == GameConstants.tbThreeCount){
			return 8;
		}
		return 0;
		
	}
	
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null || !configSong.isRunActivity()){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		TanSuoBaoZangConfigGroup configGroup = TanSuoBaoZangConfigExportService.getInstance().loadByMap(subId);
		
		if(configGroup == null){
			return null;
		}
		RefabuTanbao tanbao = getTanBao(userRoleId, subId);
		tanbao = updateJianCe(subId, tanbao);
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
		return new Object[]{
				configGroup.getPic(),
				configGroup.getDes(),
				tanbao.getWangchengCount(),
				tanbao.getStatus(),
				configGroup.getShowItem(),
				configGroup.getGoldList().toArray(),
				configGroup.getWangChengList().toArray(),
				new Object[]{count,configGroup.getMaxCount()}
				
		};
	}
	
}
