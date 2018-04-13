package com.junyou.bus.xiuxian.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.util.ActivityTimeType;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiuxian.configure.RfbXiuXianConfigExportService;
import com.junyou.bus.xiuxian.configure.XiuXianConfig;
import com.junyou.bus.xiuxian.configure.XiuXianGroupConfig;
import com.junyou.bus.xiuxian.dao.RefbRoleXiuxianDao;
import com.junyou.bus.xiuxian.dao.RefbXiuxianDao;
import com.junyou.bus.xiuxian.entity.RefbRoleXiuxian;
import com.junyou.bus.xiuxian.entity.RefbXiuxian;
import com.junyou.bus.xiuxian.filter.RefbXiuXianFilter;
import com.junyou.bus.xiuxian.manager.RefbXiuXianManager;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.RFBXXBuyLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * 热发布修仙礼包
 * @author DaoZheng Yuan
 * 2015年6月7日 下午2:47:12
 */
@Service
public class RfbXiuXianService {
	
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RefbRoleXiuxianDao refbRoleXiuxianDao;
	@Autowired
	private RefbXiuxianDao refbXiuxianDao;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	public List<RefbRoleXiuxian> initRefbRoleXiuxian(Long userRoleId){
		return refbRoleXiuxianDao.initRefbRoleXiuxian(userRoleId);
	}
	
	public Object getXiuXianInfoData(Long userRoleId,int subId){
		XiuXianGroupConfig config = RfbXiuXianConfigExportService.getInstance().loadById(subId);
		if(config == null){
			return null;
		}
		
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSong == null){
			return null;
		}
		
		return getClientData(userRoleId, subId, config);
	}
	
	private void updateJianCe(int subId,Long userRoleId,int configId){
		
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		/*if(configSong.getTimeType() != ActivityTimeType.TIME_4_KAI_FU_LOOP && configSong.getTimeType() != ActivityTimeType.TIME_5_HE_FU_LOOP){
			return;
		}*/
		long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
		
		RefbXiuxian serverXiuxian = getRefbXiuxianBySubIdAndConfigId(subId, configId);
		if(serverXiuxian != null){
			long zTime = serverXiuxian.getUpdateTime();
			
			long dTime = GameSystemTime.getSystemMillTime();
			if(startTime  > zTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
				serverXiuxian.setBuyCount(0);
				serverXiuxian.setUpdateTime(System.currentTimeMillis());
				
				refbXiuxianDao.cacheUpdate(serverXiuxian, userRoleId);
			}
		}
		
		//判断活动是否结束
		RefbRoleXiuxian xiuxin = getRefbRoleXiuxian(userRoleId, subId, configId);
		if(xiuxin != null){
			long upTime = xiuxin.getUpdateTime();
			
			long dTime = GameSystemTime.getSystemMillTime();
			if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
				xiuxin.setBuyCount(0);
				xiuxin.setUpdateTime(System.currentTimeMillis());
				
				refbRoleXiuxianDao.cacheUpdate(xiuxin, userRoleId);
			}
		}
	}
	
	/**
	 * 获取活动的状态数据
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object getXiuXianStateDataBySubId(Long userRoleId,int subId){
		XiuXianGroupConfig config = RfbXiuXianConfigExportService.getInstance().loadById(subId);
		if(config == null){
			return null;
		}
		
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSong == null){
			return null;
		}
		
		return getClientStateDataBySubId(userRoleId, subId, config, configSong.getClientVersion());
	}
	
	
	private Object getClientStateDataBySubId(Long userRoleId,int subId,XiuXianGroupConfig config,int version){
		List<Integer> ids = config.getLbIds();
		if(ids != null){
			Object[] resultData = new Object[ids.size()];
			for (int i = 0; i < ids.size(); i++) {
				int roleBuyCount = 0;
				int serverBuyCount = 0;
				
				int id = ids.get(i);
				
				XiuXianConfig xiuXianConfig =  config.getXiuXianConfigById(id);
				RefbRoleXiuxian refbRoleXiuxian = getRefbRoleXiuxian(userRoleId, subId, xiuXianConfig.getId());
				//个人实际购买数量
				if(refbRoleXiuxian != null){
					roleBuyCount = refbRoleXiuxian.getBuyCount();
				}
				//单服出售记录数量
				RefbXiuxian serverXiuxian = getRefbXiuxianBySubIdAndConfigId(subId, xiuXianConfig.getId());
				if(serverXiuxian != null){
					serverBuyCount = serverXiuxian.getBuyCount();
				}
				
				resultData[i] = new Object[]{xiuXianConfig.getId(),roleBuyCount,serverBuyCount};
			}
			
			return new Object[]{subId,version ,resultData};
		}else{
			return null;
		}
	}
	
	
	/**
	 * 获取客户端需要的数据
	 * @param userRoleId
	 * @param subId
	 * @param config
	 * @return
	 */
	private Object getClientData(Long userRoleId,int subId,XiuXianGroupConfig config){
		List<Integer> ids = config.getLbIds();
		if(ids != null){
			Object[] resultData = new Object[ids.size()];
			for (int i = 0; i < ids.size(); i++) {
				int roleBuyCount = 0;
				int serverBuyCount = 0;
				
				int id = ids.get(i);
				
				//检测修仙循环更新
				updateJianCe(subId, userRoleId, id);
				
				XiuXianConfig xiuXianConfig =  config.getXiuXianConfigById(id);
				RefbRoleXiuxian refbRoleXiuxian = getRefbRoleXiuxian(userRoleId, subId, xiuXianConfig.getId());
				//个人实际购买数量
				if(refbRoleXiuxian != null){
					roleBuyCount = refbRoleXiuxian.getBuyCount();
				}
				//单服出售记录数量
				RefbXiuxian serverXiuxian = getRefbXiuxianBySubIdAndConfigId(subId, xiuXianConfig.getId());
				if(serverXiuxian != null){
					serverBuyCount = serverXiuxian.getBuyCount();
				}
				
				resultData[i] = getXiuXianConfigData(xiuXianConfig, roleBuyCount, serverBuyCount);
			}
			
			return new Object[]{config.getBgImageName(),resultData};
		}else{
			return null;
		}
	}
	
	/**
	 * 查询指定购买记录
	 * @param userRoleId
	 * @param subId
	 * @param configId
	 * @return
	 */
	private RefbRoleXiuxian getRefbRoleXiuxian(Long userRoleId,int subId,int configId){
		List<RefbRoleXiuxian> list = refbRoleXiuxianDao.cacheLoadAll(userRoleId, new RefbXiuXianFilter(subId, configId));
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	
	/**
	 * 根据子活动id和配置id获取单服出售记录
	 * @param subId
	 * @param configId
	 * @return
	 */
	private RefbXiuxian getRefbXiuxianBySubIdAndConfigId(int subId,int configId){
		//初始化单服出售记录根据子活动id
		initRefbXiuXianBySubId(subId);
		
		RefbXiuXianManager manager = RefbXiuXianManager.getInstance();
		return manager.getRefbXiuxianBySubIdAndConfigId(subId, configId);
	}
	
	/**
	 * 初始化单服出售记录根据子活动id
	 * @param subId
	 */
	private void initRefbXiuXianBySubId(int subId){
		RefbXiuXianManager manager = RefbXiuXianManager.getInstance();
		if(!manager.isQuery(subId)){
			List<RefbXiuxian> list = refbXiuxianDao.loadRefbXiuxianBySubId(subId);
			if(list != null && list.size() == 0){
				list = null;
			}
			RefbXiuXianManager.getInstance().initSubIdList(subId, list);
		}
	}
	

	
	/**
	 * 获取单个购买数据
	 * @param config
	 * @param roleBuyCount
	 * @param serverBuyCount
	 * @return
	 */
	private Object getXiuXianConfigData(XiuXianConfig config,int roleBuyCount ,int serverBuyCount){
		/**
		 * 	0	int	配置id
			1	int	货币类型
			2	int	物品id
			3	int	price(消费价格)
			4	int	showPrice(显示价格)
			5	int	danren(限购数量，0为无限制)
			6	int	quanfu(限购数量，0为无限制)
			7	int	buycount(已购买数量)
			8	int	quanfubuycount(全服已购买数量)
		 */
		return new Object[]{
				config.getId()
				,config.getMoneyType()
				,config.getGoodsId()
				,config.getGoodsNum()
				,config.getNeedValue()
				,config.getShowNeedValue()
				,config.getRoleMaxCount()
				,config.getServerMaxCount()
				,roleBuyCount
				,serverBuyCount
		};
	}
	
	/**
	 * 请求买并领取礼包
	 * @param userRoleId
	 * @param subId
	 * @param versionId
	 * @param buyId
	 * @return
	 */
	public Object[] buyXiuXianLiBao(Long userRoleId,int subId,int versionId,int buyId){
		XiuXianGroupConfig config = RfbXiuXianConfigExportService.getInstance().loadById(subId);
		if(config == null){
			return AppErrorCode.NO_XX_LIBAO;
		}
		
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_XX_LIBAO;
		}
		
		//版本号不一样，返回变更数据
		if(versionId != configSong.getClientVersion()){
			Object data = getClientData(userRoleId, subId, config);
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
			return null;
		}
		
		XiuXianConfig xiuXianConfig = config.getXiuXianConfigById(buyId);
		if(xiuXianConfig == null){
			return AppErrorCode.NO_XX_LIBAO;
		}
		
		//道具不存在
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(xiuXianConfig.getGoodsId());
		if(goodsConfig == null){
			return AppErrorCode.NO_LIBAO_GOODS;
		}
		
		//金钱判断
		Object[] moneyErr = accountExportService.isEnought(xiuXianConfig.getMoneyType(), xiuXianConfig.getNeedValue(),userRoleId);
		if(moneyErr != null){
			return moneyErr;
		}
		
		int goodsNum = xiuXianConfig.getGoodsNum();
		//背包放不下
		Object[] checkBag = roleBagExportService.checkPutInBag(xiuXianConfig.getGoodsId(), goodsNum, userRoleId);
		if(checkBag != null){
			return checkBag;
		}
		
		//判断个人购买数量
		if(xiuXianConfig.isCheckRoleCount()){
			RefbRoleXiuxian refbRoleXiuxian = getRefbRoleXiuxian(userRoleId, subId, buyId);
			
			if(refbRoleXiuxian != null && refbRoleXiuxian.getBuyCount() >= xiuXianConfig.getRoleMaxCount()){
				Object data = getClientStateDataBySubId(userRoleId, subId, config, configSong.getClientVersion());
				BusMsgSender.send2One(userRoleId, ClientCmdType.XIUXIAN_LIBAO_CHANGE, data);
				return null;
			}
		}
		
		//判断单服数量
		if(xiuXianConfig.isCheckServerCount()){
			RefbXiuxian refbXiuxian = getRefbXiuxianBySubIdAndConfigId(subId, buyId);
			
			if(refbXiuxian != null && refbXiuxian.getBuyCount() >= xiuXianConfig.getServerMaxCount()){
				Object data = getClientStateDataBySubId(userRoleId, subId, config, configSong.getClientVersion());
				BusMsgSender.send2One(userRoleId, ClientCmdType.XIUXIAN_LIBAO_CHANGE, data);
				return null;
			}
		}
		
		//1.先扣钱再发东西
		accountExportService.decrCurrencyWithNotify(xiuXianConfig.getMoneyType(), xiuXianConfig.getNeedValue(), userRoleId, LogPrintHandle.CONSUME_RFB_XIUXIAN, true, LogPrintHandle.CBZ_RFB_XIUXIAN);
		//腾讯OSS消费上报
		if(PlatformConstants.isQQ()){
			if(GoodsCategory.GOLD == xiuXianConfig.getMoneyType()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,xiuXianConfig.getNeedValue(),LogPrintHandle.CONSUME_RFB_XIUXIAN,QQXiaoFeiType.CONSUME_RFB_XIUXIAN,1});
			}else if(GoodsCategory.BGOLD == xiuXianConfig.getMoneyType()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,xiuXianConfig.getNeedValue(),LogPrintHandle.CONSUME_RFB_XIUXIAN,QQXiaoFeiType.CONSUME_RFB_XIUXIAN,1});
			}
		}
		//2.记录出售数据
		int curRoleCount = recodeBuyRoleCount(userRoleId, subId, xiuXianConfig);
		int curServerCount = recodeBuyServerCount(subId, xiuXianConfig);
		
		//3.添加物品到背包
		RoleItemInput goods = BagUtil.createItem(xiuXianConfig.getGoodsId(), goodsNum, 0);
		roleBagExportService.putInBag(goods, userRoleId, GoodsSource.RFB_XIUXIAN_LB, true);
		//日志打印 
		try {
			GamePublishEvent.publishEvent(new RFBXXBuyLogEvent(userRoleId, xiuXianConfig.getNeedValue(),xiuXianConfig.getMoneyType(),getRoleName(userRoleId), xiuXianConfig.getGoodsId(), goodsNum));
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
				
		// [0:int(1),1:int(子活动的全局ID),2:int(配置id),3:int(已购买的数量),4:int(全服已购买数量)]
		return new Object[]{1,subId,buyId,curRoleCount,curServerCount};
	}
	private String getRoleName(Long userRoleId){
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		return role.getName();
	}
	/**
	 * 记录个人购买记录
	 * @param userRoleId
	 * @param subId
	 * @param xiuXianConfig
	 * @return
	 */
	private int recodeBuyRoleCount(Long userRoleId,int subId,XiuXianConfig xiuXianConfig){
		if(!xiuXianConfig.isCheckRoleCount()){
			return 0;
		}
		
		int roleBuyCount = 0;
		
		//记录个人购买记录
		RefbRoleXiuxian refbRoleXiuxian = getRefbRoleXiuxian(userRoleId, subId, xiuXianConfig.getId());
		if(refbRoleXiuxian == null){
			roleBuyCount = 1;
			
			refbRoleXiuxian = new RefbRoleXiuxian();
			refbRoleXiuxian.setId(IdFactory.getInstance().generateId(ServerIdType.RFB_GOODS));
			refbRoleXiuxian.setUserRoleId(userRoleId);
			refbRoleXiuxian.setBuyCount(roleBuyCount);
			refbRoleXiuxian.setSubId(subId);
			refbRoleXiuxian.setConfigId(xiuXianConfig.getId());
			refbRoleXiuxian.setUpdateTime(GameSystemTime.getSystemMillTime());
			
			refbRoleXiuxianDao.cacheInsert(refbRoleXiuxian, userRoleId);
		}else{
			roleBuyCount = refbRoleXiuxian.getBuyCount() + 1;
			
			refbRoleXiuxian.setBuyCount(roleBuyCount);
			refbRoleXiuxian.setUpdateTime(GameSystemTime.getSystemMillTime());
			refbRoleXiuxianDao.cacheUpdate(refbRoleXiuxian, userRoleId);
		}
		
		return roleBuyCount;
	}
	
	/**
	 * 记录单服出售
	 * @param userRoleId
	 * @param subId
	 * @param xiuXianConfig
	 * @return
	 */
	private int recodeBuyServerCount(int subId,XiuXianConfig xiuXianConfig){
		if(!xiuXianConfig.isCheckServerCount()){
			return 0;
		}
		
		int serverBuyCount = 0;
		
		//同步数据
		synchronized (xiuXianConfig) {
			//单服出售记录数量
			RefbXiuxian serverXiuxian = getRefbXiuxianBySubIdAndConfigId(subId, xiuXianConfig.getId());
			if(serverXiuxian == null){
				serverBuyCount = 1;
				
				serverXiuxian = new RefbXiuxian();
				serverXiuxian.setId(IdFactory.getInstance().generateId(ServerIdType.RFB_GOODS));
				serverXiuxian.setBuyCount(serverBuyCount);
				serverXiuxian.setSubId(subId);
				serverXiuxian.setConfigId(xiuXianConfig.getId());
				serverXiuxian.setUpdateTime(GameSystemTime.getSystemMillTime());
				
				refbXiuxianDao.insertRefbXiuxian(serverXiuxian);
				
				//加入管理器管理
				RefbXiuXianManager.getInstance().addRefbXiuxianBySubId(subId, serverXiuxian);
			}else{
				serverBuyCount = serverXiuxian.getBuyCount() + 1;
				
				serverXiuxian.setBuyCount(serverBuyCount);
				serverXiuxian.setUpdateTime(GameSystemTime.getSystemMillTime());
				refbXiuxianDao.updateRefbXiuxian(serverXiuxian);
			}
		}
		
		return serverBuyCount;
	}
}
