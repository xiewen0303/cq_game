package com.junyou.bus.xunbao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.branchtask.BranchEnum;
import com.junyou.bus.branchtask.service.TaskBranchService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.bus.xunbao.XunbaoOutputWrapper;
import com.junyou.bus.xunbao.configure.export.XunBaoConfig;
import com.junyou.bus.xunbao.configure.export.XunBaoConfig1ExportService;
import com.junyou.bus.xunbao.configure.export.XunBaoConfig2ExportService;
import com.junyou.bus.xunbao.configure.export.XunBaoConfigExportService;
import com.junyou.bus.xunbao.configure.export.XunBaoJiFenConfig;
import com.junyou.bus.xunbao.configure.export.XunBaoJiFenConfigExportService;
import com.junyou.bus.xunbao.configure.export.XunBaoZuBiaoConfigExportService;
import com.junyou.bus.xunbao.dao.XunBaoBagDao;
import com.junyou.bus.xunbao.dao.XunBaoDao;
import com.junyou.bus.xunbao.dao.XunbaoLogDao;
import com.junyou.bus.xunbao.entity.XunBao;
import com.junyou.bus.xunbao.entity.XunBaoBag;
import com.junyou.bus.xunbao.entity.XunBaoLog;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.XunBaoBagQCLogEvent;
import com.junyou.event.XunBaoJFChangeLogEvent;
import com.junyou.event.XunBaoLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.XunBaoPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.math.BitOperationUtil;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;
 
@Component
public class XunBaoService{
	 
	@Autowired 
	private XunBaoDao xunbaoDao;
	@Autowired 
	private XunbaoLogDao xunbaoLogDao;
	@Autowired
	private XunBaoBagDao xunbaoBagDao;
	@Autowired 
	private XunBaoConfigExportService xunBaoConfigExportService; 
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private XunBaoJiFenConfigExportService xunBaoJiFenConfigExportService;
	@Autowired
	private XunBaoZuBiaoConfigExportService xunBaoZuBiaoConfigExportService;
	@Autowired
	private XunBaoConfig1ExportService xunBaoConfig1ExportService;
	@Autowired
	private XunBaoConfig2ExportService xunBaoConfig2ExportService;
	/**
	 * 打开寻宝面板
	 * @param userRoleId
	 * @param busMsgQueue
	 * @return
	 */
	public Object[] getXunbaoInfo(long userRoleId, BusMsgQueue busMsgQueue){
		int bagCount = getXunbaoGoodsCount(userRoleId);
		
		List<XunBaoLog> xunbaoLogs = null;
		try {
			xunbaoLogs = xunbaoLogDao.getXunbaonfoByIdDb();
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		return XunbaoOutputWrapper.getXunbaoInfo(xunbaoLogs, bagCount);
	}
	
	
	 
	/**
	 * 获得寻宝积分
	 * @param userRoleId
	 * @return
	 */
	public int getXunBaoJF(long userRoleId){
		XunBao xunbao =  getXunBao(userRoleId);
		if(xunbao == null)return 0;
		return xunbao.getFindJf();
	}
	
	private XunBao getXunBao(long userRoleId){
		XunBao xunbao = xunbaoDao.cacheAsynLoad(userRoleId, userRoleId);
		return xunbao;
	}
	
	private XunBao initXunBao(long userRoleId){
		XunBao xunbao = new XunBao();
		xunbao.setFindCount(0);
		xunbao.setFindJf(0);
		xunbao.setFindLastTime(GameSystemTime.getSystemMillTime());
		xunbao.setUserRoleId(userRoleId);
//		xunbao.setFindVersions(findVersions); //TODO 
		
		xunbaoDao.cacheInsert(xunbao, userRoleId);
		return xunbao;
	}
	
	public Object[] getXunbaoBagData(long userRoleId){
		List<XunBaoBag> xunbaoBags = getXunbaoBags(userRoleId);
		return XunbaoOutputWrapper.getXunbaoData(xunbaoBags);
	}
	
	

	private List<XunBaoBag> getXunbaoBags(long userRoleId){
		return xunbaoBagDao.cacheAsynLoadAll(userRoleId); 
	}
	
	
	public int getXunbaoGoodsCount(long userRoleId){
		List<XunBaoBag> xunbaoBags = getXunbaoBags(userRoleId);
		int bagCount = 0;
		if(xunbaoBags != null){
			bagCount = xunbaoBags.size();
		}
		
		return bagCount;
	}
	 
	/**
	 * 寻宝
	 * @param userRoleId
	 * @param type
	 * @param busMsgQueue
	 * @return
	 */
	public Object[] xunbao(long userRoleId,int type,String goodsId,BusMsgQueue busMsgQueue){
		
		XunBaoPublicConfig xunbaoPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_XUNBAO);
		if(xunbaoPublicConfig == null){
			return AppErrorCode.XB_NO_PUBLIC_CONFIG;
		}
		
		Integer times = xunbaoPublicConfig.getTimes(type);
		if(times == null ||times.equals(0)){
			return AppErrorCode.XB_TYPE_ERROR;
		}
		
		String needGoodsId = xunbaoPublicConfig.getNeedGoodsId();
		
		int needGoodsIdCount = 0;
		//类型一的寻宝  优先用钥匙,然后考虑元宝
		if(type == GameConstants.XB_TYPE1){
			needGoodsIdCount = roleBagExportService.getBagItemCountByGoodsId(needGoodsId, userRoleId);	
		}
				
		
		int needGold = 0;
		int needConsumeCount = 0; 
		
		if(needGoodsIdCount < times){
			needGold = (times-needGoodsIdCount)*xunbaoPublicConfig.getNeedGold();
			needConsumeCount=needGoodsIdCount;
		}else{
			needConsumeCount = times;
		}
		boolean isXhGoos = false;//是否消耗道具
		//类型2，判断打折道具
		if(type == GameConstants.XB_TYPE2){
			if(goodsId != null && !"".equals(goodsId)){
				GoodsConfig goodConfig = goodsConfigExportService.loadById(goodsId);
				if(goodConfig.getCategory() != GoodsCategory.XUNBAO_ZHEKOU){
					return AppErrorCode.GOODS_NOT_ENOUGH;
				}
				if(goodConfig != null){
					needGold = needGold * goodConfig.getData1() / 100;
					isXhGoos = true;
				}
			}
		}
		
		//元宝验证
		if(needGold > 0 && null != accountExportService.isEnought(GoodsCategory.GOLD, needGold, userRoleId)){
			return AppErrorCode.XB_NO_GOLD;
		} 
		
		
		
		XunBao xunbao =  getXunBao(userRoleId);
		if(xunbao == null){
			//只有第一次寻宝时才进行初始化
			xunbao=	initXunBao(userRoleId);
		}
		
		//验证背包是否可以存储物品
		int findCount = xunbao.getFindCount();
		//获得物品
		Map<String,Integer> goodsMap = getXunBaoAwards(findCount,times,type);
		//对不起,请清理一下,你的寻宝包裹
		
		Object[] checkBagFlag = checkPutIn(goodsMap, userRoleId);
		if(checkBagFlag != null){
			return checkBagFlag;
		}
		//消耗道具
		if(isXhGoos){
			//检查消耗物品
			Object[] error = roleBagExportService.checkRemoveBagItemByGoodsId(goodsId, 1, userRoleId);
			if(error != null){
				return error;
			}
			roleBagExportService.removeBagItemByGoodsId(goodsId, 1, userRoleId, GoodsSource.XUNBAO, true, true);
		}
		//消耗元宝
		if(needGold > 0){
			accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, needGold, userRoleId, LogPrintHandle.CONSUME_XUN_BAO, true, LogPrintHandle.CBZ_XUN_BAO);
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,needGold,LogPrintHandle.CONSUME_XUN_BAO,QQXiaoFeiType.CONSUME_XUN_BAO,1});
			}
		}
		//扣除钥匙
		if(needConsumeCount > 0){
			//检查消耗物品
			Object[] error = roleBagExportService.checkRemoveBagItemByGoodsId(needGoodsId, needConsumeCount, userRoleId);
			if(error != null){
				return error;
			}
			roleBagExportService.removeBagItemByGoodsId(needGoodsId, needConsumeCount, userRoleId, GoodsSource.XUNBAO, true, true);
		}
		
		List<Object[]> outClients = new ArrayList<Object[]>();
		
		//物品进寻宝包裹
		putIn(userRoleId, goodsMap,outClients,busMsgQueue,LogPrintHandle.XUNBAO_TYPE_PT);
		
		//寻宝后添加寻宝积分
		xunbao.setFindJf(xunbao.getFindJf()+times*xunbaoPublicConfig.getXbjifen());
		xunbao.setFindLastTime(GameSystemTime.getSystemMillTime());
		xunbao.setFindCount(xunbao.getFindCount()+times);
		xunbaoDao.cacheUpdate(xunbao, userRoleId);
		
		//推送寻宝积分:TODO
//		busMsgQueue.addMsg(userRoleId, ClientCmdType.XB_JIFEN, xunbao.getFindJf());
		//成就
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_TIJUNBAOHE,  times});
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.XUNBAO, times});
			//支线
			taskBranchService.completeBranch(userRoleId, BranchEnum.B11, times);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		
		//日志打印 
		JSONArray receiveItems = new JSONArray();
		LogFormatUtils.parseJSONArray(goodsMap, receiveItems);
		GamePublishEvent.publishEvent(new XunBaoLogEvent(userRoleId, needGold, getRoleName(userRoleId), needGoodsId, needConsumeCount, receiveItems, LogPrintHandle.XUNBAO_TYPE_PT, type));
		
		
		return new Object[]{1,outClients.toArray()};
	}
	
	@Autowired
	private TaskBranchService taskBranchService;
	private String getRoleName(long userRoleId){
		RoleWrapper role =	roleExportService.getLoginRole(userRoleId);
		return role==null?"":role.getName();
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
			totalCount+=count/maxStack+(count%maxStack);
		}
		int nowBagSlotCount = getXunbaoGoodsCount(userRoleId);
		XunBaoPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_XUNBAO);
		if(nowBagSlotCount + totalCount > publicConfig.getMaxCapacity()){
			return AppErrorCode.XB_BAG_FULL;
		}
		return null; 
	}
	
	public Object[] isFull(long userRoleId){
		int nowBagSlotCount = getXunbaoGoodsCount(userRoleId);
		XunBaoPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_XUNBAO);
		if(nowBagSlotCount >= publicConfig.getMaxCapacity()){
			return AppErrorCode.XB_BAG_FULL;
		}
		return null; 
	}
	
	
	/**
	 *  获得寻宝对应的奖励
	 * @param findCount
	 * @param times
	 * @return
	 */
	private Map<String, Integer> getXunBaoAwards(int findCount, int times,int type) {
		Map<String, Integer> datas =new HashMap<String, Integer>();
		 
		int id = findCount+1;
		XunBaoConfig config = getXunBaoConfigByServerDay(id,type);
		if(config == null){
			ChuanQiLog.error("寻宝配置报异常：times:"+id);
		}
		for(int i = findCount+1;i<=findCount+times;i++){
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
					Map<String, Integer>  groupGoods = xunBaoZuBiaoConfigExportService.componentRoll(goodsId);
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
		return datas;
	}

	
	private XunBaoConfig getXunBaoConfigByServerDay(int id,int type){
		XunBaoConfig config = null;
		int kfDays = ServerInfoServiceManager.getInstance().getKaifuDays();
		if(kfDays >= 1 && kfDays <= 7){
			if(type == GameConstants.XB_TYPE1){
				config = xunBaoConfigExportService.getXunBaoById(999);
			}else{
				config = xunBaoConfigExportService.getXunBaoIdByCount(id);
			}
		}else if(kfDays >= 8 && kfDays <= 14){
			if(type == GameConstants.XB_TYPE1){
				config = xunBaoConfig1ExportService.getXunBaoById(999);
			}else{
				config = xunBaoConfig1ExportService.getXunBaoIdByCount(id);
			}
		}else{
			if(type == GameConstants.XB_TYPE1){
				config = xunBaoConfig2ExportService.getXunBaoById(999);
			}else{
				config = xunBaoConfig2ExportService.getXunBaoIdByCount(id);
			}
		}
		return config;
	}
	   
	
	public void putIn(long userRoleId,Map<String,Integer> goodsMap,List<Object[]> outClients,BusMsgQueue busMsgQueue,int type){
		if(goodsMap == null || goodsMap.size() == 0){
			return;
		}
		
		for (Map.Entry<String, Integer> entry : goodsMap.entrySet()){
			putInXunbaoBag(userRoleId, entry.getKey(), entry.getValue(),outClients,busMsgQueue,type);
		}
	}
	
	private void putInXunbaoBag(long userRoleId,String goodsId,int count,List<Object[]> outClients,BusMsgQueue busMsgQueue,int type){
		GoodsConfig goodsConfig = getGoodsConfig(goodsId);
		if(goodsConfig == null){
			ChuanQiLog.debug("putInXunbaoBag goodsConfig is null goodsId:"+goodsId);
			return;
		}
		
		//保存广播日志并全服广播
		saveLogAndNotify(userRoleId, goodsConfig, count, busMsgQueue,type);
		
		if(numberProp(goodsId, count,userRoleId)){
			return;
		}
		
		List<XunBaoBag> xunbaoBag = createXunbaoBag(userRoleId, goodsConfig, count);
		for (XunBaoBag goods : xunbaoBag) {
			xunbaoBagDao.cacheInsert(goods, userRoleId);
			outClients.add(new Object[]{goods.getId(),goods.getGoodsId(),goods.getGoodsCount()});
		}
	}
	
	private RoleWrapper getRoleWrapper(long userRoleId) {
		return roleExportService.getLoginRole(userRoleId);
	}
	
	/**
	 * 保存广播日志并全服广播
	 * @param userRoleId
	 * @param goodsConfig
	 * @param count
	 * @param busMsgQueue
	 */
	private void saveLogAndNotify(long userRoleId,GoodsConfig goodsConfig,int count,BusMsgQueue busMsgQueue,int type){
		//全局通知
		if(goodsConfig.isNotify()){
			RoleWrapper role = getRoleWrapper(userRoleId);
			
			if(role.isGm()){
				return;//GM不广播
			}
			
			//记录通知
			String roleName = role.getName();
			if(type ==  LogPrintHandle.XUNBAO_TYPE_PT){
				busMsgQueue.addBroadcastMsg(ClientCmdType.XUNBAO_SYSTEM_NOTIFY, new Object[]{roleName,goodsConfig.getId(),count});
				
				XunBaoLog xunbaoLog = new XunBaoLog();
				
				xunbaoLog.setRoleName(roleName);
				xunbaoLog.setGoodsId(goodsConfig.getId());
				xunbaoLog.setGoodsCount(count);
				xunbaoLog.setCreateTime(GameSystemTime.getSystemMillTime());
				
				try {
					xunbaoLogDao.insertDb(xunbaoLog);
				} catch (Exception e) {
					ChuanQiLog.error("",e);
				}
			}
		}
	}
	
	public XunBaoBag getXunBaoBag(long userRoleId,long guid){
		XunBaoBag xunbaoBag = xunbaoBagDao.cacheLoad(guid, userRoleId);
		if(xunbaoBag != null){
			return xunbaoBag;
		}
		getXunBaoBags(userRoleId);
		
		return xunbaoBagDao.cacheLoad(guid, userRoleId);
	}
	
	public List<XunBaoBag> getXunBaoBags(long userRoleId){
		return  xunbaoBagDao.cacheAsynLoadAll(userRoleId);
	}
	
	
	public Object[] takeOutXunbaoBag(long userRoleId,long guid){
		XunBaoBag xunbaoBag =getXunBaoBag(userRoleId, guid);
		if(xunbaoBag == null) {
			return AppErrorCode.XB_ITEM_NO_EXIST;
		}
		
		Object[] bagFlag = roleBagExportService.checkPutInBag(xunbaoBag.getGoodsId(),1,userRoleId);
		if(bagFlag != null){
			return bagFlag;
		}
		
		//清除寻宝包裹被取的物品
		xunbaoBagDao.cacheDelete(guid, userRoleId);
		
		//添加物品入背包
		RoleItemInput roleItem = BagUtil.createItem(xunbaoBag.getGoodsId(), xunbaoBag.getGoodsCount(), 0);
		BagSlots  result = roleBagExportService.putInBag(roleItem, userRoleId, GoodsSource.XUNBAO, true);
		
		//取出寻宝背包物品日志 
		JSONArray receiveItems = new JSONArray();
		LogFormatUtils.parseJSONArray(result, receiveItems); 
		GamePublishEvent.publishEvent(new XunBaoBagQCLogEvent(userRoleId, getRoleName(userRoleId), receiveItems)); 
		
		return new Object[]{1,guid}; 
	}
	
	
	public Object[] takeOutXunbaoMany(long userRoleId,Object[] xbItemIds){
		if(xbItemIds == null || xbItemIds.length <=0) {
			return AppErrorCode.XB_QCWP_ERROR;
		}
		
		List<XunBaoBag> xunbaoBags = getXunbaoBags(userRoleId);
		
		//没有可领取的物品
		if(xunbaoBags == null || xunbaoBags.size() == 0){
			return AppErrorCode.XB_BAG_NO_GOODS;
		}
		
		List<Long> delIds = new ArrayList<Long>();
		Map<String,Integer> inbagMap = new HashMap<String, Integer>();
		
		for (Object guidObj : xbItemIds) {
			Long guid = CovertObjectUtil.object2Long(guidObj);
			for (XunBaoBag xunBaoBag : xunbaoBags) {
				//寻宝背包中有 并且去重复
				if(xunBaoBag.getId().equals(guid) &&!delIds.contains(guid)){
					delIds.add(guid);
					
					int count = xunBaoBag.getGoodsCount();
					if(inbagMap.containsKey(xunBaoBag.getGoodsId())){
						count = count + inbagMap.get(xunBaoBag.getGoodsId());
					}
					inbagMap.put(xunBaoBag.getGoodsId(), count);
				}
			}
		}
		
		Object[] bagErrorCode = roleBagExportService.checkPutInBag(inbagMap, userRoleId);
		
		if(bagErrorCode != null){
			return bagErrorCode;
		}
		
		//删除寻宝物品
		for (long xbId : delIds) {
			xunbaoBagDao.cacheDelete(xbId, userRoleId);
		}
		
		BagSlots result = roleBagExportService.putInBag(inbagMap, userRoleId, GoodsSource.XUNBAO, true);
		
		//取出寻宝背包物品日志 
		JSONArray receiveItems = new JSONArray();
		LogFormatUtils.parseJSONArray(result, receiveItems); 
		GamePublishEvent.publishEvent(new XunBaoBagQCLogEvent(userRoleId, getRoleName(userRoleId), receiveItems)); 
		
		return new Object[]{1,delIds.toArray()};
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
	
	private GoodsConfig getGoodsConfig(String goodsId){
		return goodsConfigExportService.loadById(goodsId);
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
	
	public Object[] getXunBaoJiFenInfo(Long userRoleId, Integer fzId) {
		Object[] xbList = xunBaoJiFenConfigExportService.getXunBaoJiFenBiaoConfigList(fzId);
		if(xbList == null || xbList.length <= 0){
			return AppErrorCode.XB_CONFIG_ERROR;
		}
		
		return new Object[]{1,fzId,xbList};
	}

	/**
	 * 积分兑换物品
	 * @param userRoleId
	 * @param id
	 * @param busMsgQueue
	 * @return
	 */
	public Object[] xunbaoConvert(Long userRoleId, Integer id, BusMsgQueue busMsgQueue) {
	 		XunBaoJiFenConfig config = xunBaoJiFenConfigExportService.loadById(id);
			//配置为null
			if(config == null){
				return AppErrorCode.XB_CONFIG_ERROR;
			}
			
			int jf = getXunBaoJF(userRoleId);
			if(config.getNeedxbjifen() > jf){
				return AppErrorCode.XB_JIFEN_ERROR;
			}
			if(config.getNeeditem() != null && !"".equals(config.getNeeditem())){
				Object[] needItemFlag = roleBagExportService.checkRemoveBagItemByGoodsId(config.getNeeditem(), 1, userRoleId);
				if(needItemFlag != null){
					return needItemFlag;
				}
			}
			//判断兑换的物品是否是自己职业的物品
			GoodsConfig  goodsConfig=BusConfigureHelper.getGoodsConfigExportService().loadById(config.getItemid());
			RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
			if(roleWrapper == null){
				return AppErrorCode.NUMBER_ERROR;
			}
			//职业不能兑换
			int orleJob = roleWrapper.getConfigId();
			boolean jobFlag = BitOperationUtil.calState(goodsConfig.getJob(), orleJob);
			if(goodsConfig.getJob() != 0 && jobFlag){
				return AppErrorCode.XB_DUIHUAN_ERROR;
			}
			//判断背包空间是否足够
			Object[] addItemFlag = roleBagExportService.checkPutInBag(config.getItemid(), 1, userRoleId);
			if(addItemFlag != null){
				return addItemFlag;
			}
			
			//扣除物品
			if(config.getNeeditem() != null && !"".equals(config.getNeeditem())){
				 roleBagExportService.removeBagItemByGoodsId(config.getNeeditem(), 1, userRoleId, GoodsSource.XUNBAO, true, true);
			} 
			
			//扣除积分
			XunBao xunBao = getXunBao(userRoleId);
			xunBao.setFindJf(xunBao.getFindJf() - config.getNeedxbjifen());
			xunbaoDao.cacheUpdate(xunBao, userRoleId);
			
			//兑换后的物品放入背包
			RoleItemInput roleItemInput = BagUtil.createItem(config.getItemid(), 1, 0);
			BagSlots bagSlot = roleBagExportService.putInBag(roleItemInput, userRoleId, GoodsSource.XUNBAO, true);
			
			//寻宝日志
			JSONArray receiveItems = new JSONArray();
			LogFormatUtils.parseJSONArray(bagSlot, receiveItems);
			
			int consumeCount = "".equals(config.getNeeditem()) ? 0 : 1;
			//添加寻宝积分兑换日志
			GamePublishEvent.publishEvent(new XunBaoJFChangeLogEvent(userRoleId, getRoleName(userRoleId), config.getNeedxbjifen(),config.getNeeditem(), consumeCount, receiveItems));
			//推送寻宝积分:TODO
//			busMsgQueue.addMsg(userRoleId, ClientCmdType.XB_JIFEN, xunBao.getFindJf());
			return AppErrorCode.OK; 
	}
 
	public Object[] getXunBaoJiFenFZInfo(long userRoleId) {
		Object[] arr = xunBaoJiFenConfigExportService.getXunBaoJiFenFZConfig();
		if(arr == null || arr.length <= 0){
			return AppErrorCode.XB_CONFIG_ERROR;
		}
		
		return new Object[]{1,arr};
	}
	
}
