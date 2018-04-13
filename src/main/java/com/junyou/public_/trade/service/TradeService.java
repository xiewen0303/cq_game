package com.junyou.public_.trade.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.setting.export.RoleSettingExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.public_.trade.TradeData;
import com.junyou.public_.trade.TradeOutPutWrapper;
import com.junyou.public_.trade.filter.StageElementFilter;
import com.junyou.public_.tunnel.PublicMsgSender;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.stage.StageManager;
import com.kernel.spring.container.DataContainer;

/**
 * 交易
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-15 下午5:20:40
 */
@Component
//@PublicSyncClass(component = TradeConstants.COMPONENET_NAME)
public class TradeService {
	
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleBagExportService bagExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleSettingExportService roleSettingExportService;
	
//	/**
//	 * xx秒后解锁交易请求锁定
//	 */
//	private static final int PAST_TIME=25*1000;
	 
	/**
	 * 发起交易
	 * @param selfRoleId
	 * @param targetRoleId
	 * @return
	 */
	public Object[] launch(Long selfRoleId,Long otherRoleId){
		//验证在同场景和在线
		Object[] check = checkOnline(otherRoleId);
		if(check != null){
			return check;
		}
		//验证是否需要对同屏幕才可交易
		Object[] checkSame = checkSameScreen(selfRoleId, otherRoleId);
		if(checkSame!=null){
			return checkSame;
		}
		
		//验证交易状态
		Object[] checkStage = checkNoTradeState(selfRoleId, otherRoleId);
		if(checkStage != null){
			return checkStage;
		}
		//验证对方是否允许
		if(roleSettingExportService.isSetting(otherRoleId, GameConstants.SYSTEM_SETTING_TYPE_TRADE)){
			return AppErrorCode.OPERATE_ERROR_TYPE_TRADE;//对方禁止交易
		}
		
//		//验证对方是否有被人邀请交易
//		Object targetFlag = dataContainer.getData(GameConstants.COMPONENET_TREAD_TARGET,otherRoleId.toString());
//		if(targetFlag != null){
//			Object[] datas =  (Object[])targetFlag;
//			long beforeTime = (long)datas[0];
//			Long launchedRoleId = (Long)datas[1];
//		 
//			if(beforeTime +  PAST_TIME> GameSystemTime.getSystemMillTime()){
//				if(launchedRoleId.equals(selfRoleId)){
//					return new Object[]{false,new Object[]{AppErrorCode.TRADE_TARGET_LUNCHED}};
//				}else{
//					return  new Object[]{false, new Object[] {AppErrorCode.TRADE_TARGET_NO_MUCH}};
//				}
//			}
//		}
//		
//		dataContainer.putData(GameConstants.COMPONENET_TREAD_TARGET, otherRoleId.toString(),new Object[]{GameSystemTime.getSystemMillTime(),selfRoleId});
		
		RoleWrapper role = getStageRole(selfRoleId);
		Object[] outData = TradeOutPutWrapper.getTradeRoleBaseInfo(role);
		
		return new Object[]{true,outData};
	}
	
	/**
	 * 判断是否再同一屏幕上
	 * @param selfRoleId
	 * @param otherRoleId
	 * @return
	 */
	public Object[] checkSameScreen(long selfRoleId,long otherRoleId){
		String selfStageId =  publicRoleStateExportService.getRolePublicStageId(selfRoleId);
		IStage stage = StageManager.getStage(selfStageId);
		if(stage!=null){
			IStageElement selfStageElement = stage.getElement(selfRoleId, ElementType.ROLE);
			Point selfP1= selfStageElement.getPosition(); 
			Collection<IStageElement> stageElements = stage.getSurroundElements(selfP1, ElementType.ROLE, new StageElementFilter(otherRoleId));
			if(stageElements == null || stageElements.size()==0){
				return  new Object[]{false,AppErrorCode.TRADE_ROLE_NO_NEARBY};
			}
		}
		return null;
	}
	
	/**
	 * 同意交易
	 * @param selfRoleId
	 * @param stageId
	 * @return
	 */
	public Object[] agree(Long selfRoleId,Long otherRoleId){
//		//移除自己处于被邀请交易的状态
//		dataContainer.removeData(GameConstants.COMPONENET_TREAD_TARGET, selfRoleId.toString());
		
		//验证在同场景和在线
		Object[] check = checkOnline(otherRoleId);
		if(check != null){
			return check;
		}
		
		//验证是否需要对同屏幕才可交易
		Object[] checkSame = checkSameScreen(selfRoleId, otherRoleId);
		if(checkSame!=null){
			return checkSame;
		}
		
		//验证交易状态
		Object[] checkStage = checkNoTradeState(selfRoleId, otherRoleId);
		if(checkStage != null){
			return checkStage;
		}
		
		/**
		 * 双方建立交易关系
		 */
		
		//同意者运行时数据
		TradeData selfData = new TradeData(selfRoleId);
		selfData.setOtherRoleId(otherRoleId);
		RoleWrapper selfRole = getStageRole(selfRoleId);
		if(selfRole != null){
			selfData.setSelfName(selfRole.getName());
		}
		dataContainer.putData(GameConstants.COMPONENET_TREAD_NAME, selfRoleId.toString(), selfData);
		
		
		//交易发起者的数据
		TradeData otherData = new TradeData(otherRoleId);
		otherData.setOtherRoleId(selfRoleId);
		RoleWrapper otherRole = getStageRole(otherRoleId);
		if(otherRole != null){
			otherData.setSelfName(otherRole.getName());
		} 
		dataContainer.putData(GameConstants.COMPONENET_TREAD_NAME, otherRoleId.toString(), otherData);
		 
		
		RoleWrapper role = getStageRole(selfRoleId); 
		Object[] outData = TradeOutPutWrapper.getTradeRoleBaseInfo(role); 
		
		return new Object[]{true,outData};
	}
	
	/**
	 * 拒绝交易请求
	 * @param selfRoleId
	 * @param otherRoleId
	 * @param stageId
	 * @return
	 */
	public Object[] reject(Long selfRoleId,Long otherRoleId){
		RoleWrapper selfRole = getStageRole(selfRoleId);
		
//		//移除自己处于被邀请交易的状态
//		dataContainer.removeData(GameConstants.COMPONENET_TREAD_TARGET, selfRoleId.toString());
		
		return new Object[]{AppErrorCode.TRADE_NAME_REJECT,selfRole.getName()};
	}
	
	public Object[] modifyMoney(Long userRoleId,Long yb){
		TradeData selfData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, userRoleId.toString());
		if(selfData == null){
			return new Object[]{false,AppErrorCode.TRADE_EXCEPTION};
		}
		
		Long otherRoleId = selfData.getOtherRoleId();
		//验证在同场景和在线
		Object[] check = checkOnline(otherRoleId);
		if(check != null){
			return check;
		}
		//验证交易状态
		Object[] checkStage = checkAppointTradeStateCheck(userRoleId, otherRoleId);
		if(checkStage != null){
			return checkStage;
		}
		
		// 判断是否有足够的元宝
		if(yb > 0){
			Object[] flag = accountExportService.isEnoughtValue(GoodsCategory.GOLD, yb, userRoleId);
		 	if(null != flag){
		 		return new Object[]{false,AppErrorCode.YB_ERROR[1]};
			}
		}
		
		//数据存入TradeData
		selfData.setYb(yb);
		
		//双方交易状态处理  [交易中，只要修改金钱或物品，状态自动退回到 开始交易状态]
		if(selfData.getState() > GameConstants.START_TRADE){
			selfData.setState(GameConstants.START_TRADE);
		}
		TradeData targetData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, otherRoleId.toString());
		if(targetData.getState() > GameConstants.START_TRADE){
			targetData.setState(GameConstants.START_TRADE);
		} 
		
		return new Object[]{true,otherRoleId,yb};
	}
	
	public Object[] modifyGoods(Long userRoleId,int tradeSlot,Long guid,int count){
		TradeData selfData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, userRoleId.toString());
		if(selfData == null){
			return new Object[]{false,AppErrorCode.TRADE_EXCEPTION};
		}
		Long otherRoleId = selfData.getOtherRoleId();
		//验证在同场景和在线
		Object[] check = checkOnline(otherRoleId);
		if(check != null){
			return check;
		}
		//验证交易状态
		Object[] checkStage = checkAppointTradeStateCheck(userRoleId, otherRoleId);
		if(checkStage != null){
			return checkStage;
		}
		
		//验证交易物品
		Object[] checkGoods = checkTradeGoods(tradeSlot,guid, userRoleId, selfData,count);
		if(!(Boolean) checkGoods[0]){
			return checkGoods;
		}
		
		//双方交易状态处理  [交易中，只要修改金钱或物品，状态自动退回到 开始交易状态]
		if(selfData.getState() > GameConstants.START_TRADE){
			selfData.setState(GameConstants.START_TRADE);
		}
		TradeData targetData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, otherRoleId.toString());
		if(targetData.getState() > GameConstants.START_TRADE){
			targetData.setState(GameConstants.START_TRADE);
		}

		RoleItemExport goods = (RoleItemExport)checkGoods[1];
		Object[] result =TradeOutPutWrapper.modifyGoods(tradeSlot,goods,count);
		
		return new Object[]{true,otherRoleId,result};
	}
	
	public boolean isActive(long userRoleId){
		TradeData selfData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, userRoleId+"");
		if(selfData == null){
			return false;
		}
		return true;
	}
	
	
	public Long cancel(Long userRoleId){
		TradeData selfData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, userRoleId.toString());
		
		Long otherRoleId = null;
		if(selfData != null){
			otherRoleId = selfData.getOtherRoleId();
			//清理两个人的TradeData数据
			dataContainer.removeData(GameConstants.COMPONENET_TREAD_NAME, userRoleId.toString());
			dataContainer.removeData(GameConstants.COMPONENET_TREAD_NAME, otherRoleId.toString());
		}
		
		return otherRoleId;
	}
	
	
	public Object[] confirm1(Long userRoleId) {
		TradeData selfData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, userRoleId.toString()); 
		
		if(selfData == null){
			return new Object[]{false,AppErrorCode.TRADE_EXCEPTION};
		}
		
		Long otherRoleId = selfData.getOtherRoleId();
		TradeData targetData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, otherRoleId.toString());
		//验证在同场景和在线
		Object[] check = checkOnline(otherRoleId);
		if(check != null){
			return new Object[]{false,check};
		}
		//验证交易状态
		Object checkStage = checkAppointTradeState(userRoleId, otherRoleId,GameConstants.START_TRADE);
		if(checkStage != null){
			return new Object[]{false,checkStage};
		}
		 
		Long yb = selfData.getYb();
		 
		if(yb < 0){
			yb = 0l;
		} 
		
		Object[] enoughtCode = accountExportService.isEnoughtValue(GoodsCategory.GOLD, yb, userRoleId);
		//如果不足够,变为拥有的实际值
		if(yb > 0 && null != enoughtCode ){
			yb = accountExportService.getCurrency(GoodsCategory.GOLD, userRoleId);
			selfData.setYb(yb);
		}
		
		/**
		 * 验证对方获得这么多元宝时，是否超过上限
		 */
		Object[] checkGoldFlag = checkRoleAcceptGold(selfData.getOtherRoleId(), selfData.getYb(), getRoleName(selfData.getOtherRoleId()));
		if(checkGoldFlag !=null){
			return checkGoldFlag;
		}
		
		//物品验证
		Object[] checkGoods = checkBagExpChecks(targetData, userRoleId, otherRoleId);
		if(!(Boolean) checkGoods[0]){
			return checkGoods;
		}
		
		//交易状态修改为确认1
		selfData.setState(GameConstants.CONFIRM_TRADE);
		
		return new Object[]{true,otherRoleId};
	}
	
	/**
	 * 判断元宝最大值
	 * @param targetRoleId
	 * @param money
	 * @param roleName
	 * @return
	 */
	public Object[] checkRoleAcceptGold(long targetRoleId,long money,String roleName){
		long ownGold = accountExportService.getCurrency(GoodsCategory.GOLD, targetRoleId);
		if(ownGold +money >GameConstants.YB_MAX){
			return new Object[]{false,AppErrorCode.createError(AppErrorCode.TRADE_GOLD_MAX,roleName)};
		}
		return null;
	}
	
	
	public Object[] confirm2(Long userRoleId){
		TradeData selfData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, userRoleId.toString());
		if(selfData == null){
			return new Object[]{false,AppErrorCode.TRADE_EXCEPTION};
		}
		
		Long otherRoleId = selfData.getOtherRoleId();
		//验证在同场景和在线
		Object[] check = checkOnline(otherRoleId);
		if(check != null){
			return check;
		}
		
		//验证状态
		Object checkStage = checkAppointTradeState(userRoleId, otherRoleId, GameConstants.CONFIRM_TRADE);
		if(checkStage != null){
			return new Object[]{false,checkStage};
		}
		
		//交易状态修改为确认2
		selfData.setState(GameConstants.KNOW_OTHER_CONFIRM_TRADE);
		
		return new Object[]{true,otherRoleId};
	}
	
	public Object[] confirm3(Long userRoleId){
		TradeData selfData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, userRoleId.toString());
		if(selfData == null){
			return new Object[]{false,AppErrorCode.TRADE_EXCEPTION};
		}
		
		Long otherRoleId = selfData.getOtherRoleId();
		//验证在线
		Object[] check = checkOnline(otherRoleId);
		if(check != null){
			return check;
		}
		
		//验证状态
		Object checkStage = checkAppointTradeState(userRoleId, otherRoleId, GameConstants.KNOW_OTHER_CONFIRM_TRADE);
		if(checkStage != null){
			return new Object[]{false,checkStage};
		}
		 
		//判定身上的元宝是否足够
		long yb = selfData.getYb();
		Object[] enoughtCode = accountExportService.isEnoughtValue(GoodsCategory.GOLD, yb, userRoleId);
		//如果不足够,变为拥有的实际值
		if(yb > 0 &&null != enoughtCode ){
			return new Object[]{false,AppErrorCode.YB_ERROR};
		}
//		
//		TradeData targetData = dataContainer.getData(TradeConstants.COMPONENET_NAME, otherRoleId.toString());
 
		TradeData targetData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, otherRoleId.toString());
 
		
		
		//交易状态修改为  [回馈了双方都确认状态]
		selfData.setState(GameConstants.FREED_BACK_TRADE);
		
		/**
		 * 两方的状态都是[回馈了双方都确认状态]后 通知action可以物品交易
		 */
		if(selfData.getState() == GameConstants.FREED_BACK_TRADE && targetData.getState() == GameConstants.FREED_BACK_TRADE){
			return new Object[]{true,otherRoleId,null};
		}
		
		return new Object[]{true,otherRoleId};
	}
	
	private GoodsConfig getGoodsConfig(String goodsId){
		return goodsConfigExportService.loadById(goodsId);
	}
	
	/**
	 * 验证对方物品自己背包是否可以放得下
	 * @param goodsGuid 对方物品guid
	 * @param userRoleId 自己RoleId
	 * @param targetRoleId 对方RoleId
	 * @return true:可放得下
	 */
	private Object[] checkBagExpChecks(TradeData targetData,Long userRoleId,Long targetRoleId){
		 Map<Long, Integer> goodsGuid = targetData.getGoodsGuids();
		 Map<Long, Integer> goodsCounts =  targetData.getGoodsCounts();
		 
		Map<String,Integer> bagChecks=new HashMap<String, Integer>();
		 
		if(goodsGuid != null && goodsGuid.size() > 0){
			
			for (Long guid : goodsGuid.keySet()) {
				RoleItemExport entity = bagExportService.getBagItemByGuid(targetRoleId,guid);
				if(entity == null){
					return new Object[]{false,AppErrorCode.createError(AppErrorCode.TRADE_GOODS_NO_EXIST,targetData.getSelfName())};
				}
				
				GoodsConfig goodsConfig = getGoodsConfig(entity.getGoodsId());
				if(goodsConfig.isBind()){
					return new Object[]{false,AppErrorCode.createError(AppErrorCode.TRADE_GOODS_BIND,targetData.getSelfName())};
				}
				//过期物品处理
				if(entity.isExpireTime()){
					return new Object[]{false,AppErrorCode.createError(AppErrorCode.EXPIRE_ERROR,targetData.getSelfName())};
				}
				
				Integer count = goodsCounts.get(guid);
				if(count != null && count > 0){
					bagChecks.put(entity.getGoodsId(), count);
				}
			}
 
			if(bagExportService.checkPutInBag(bagChecks, userRoleId)==null){
				return new Object[]{true};
			}else{
				
				return new Object[]{false,AppErrorCode.createError(AppErrorCode.TRADE_BAG_NO_SLOT,getRoleName(userRoleId))};
			}
		}else{
			return new Object[]{true};
		}
	}
	
	public String getRoleName(long userRoleId){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper== null){
			return "";
		}
		return roleWrapper.getName();
	}
	
	private Object[] checkTradeGoods(int tradeIndex,Long guid,Long userRoleId,TradeData selfData,int count){
		RoleItemExport goods = null;
		//物品可交易验证
		boolean falg = tradeIndex >= 0 && tradeIndex < GameConstants.TRADE_SLOT_MAX_COUNT;
		if(!falg){
			return new Object[]{false,AppErrorCode.createError(AppErrorCode.TRADE_INDEX_ERROR, selfData.getSelfName())};
		}
		
		//物品数量小于零
		if(count < 0){
			return new Object[]{false,AppErrorCode.createError(AppErrorCode.TRADE_GOODS_COUNT_ERROR,selfData.getSelfName())};
		}
		
		if(guid != null && guid!=0){
			goods = bagExportService.getBagItemByGuid(userRoleId,guid);
			
			if(goods == null){
				return new Object[]{false,AppErrorCode.createError(AppErrorCode.TRADE_GOODS_NO_EXIST,selfData.getSelfName())};
			}
			GoodsConfig goodsConfig = getGoodsConfig(goods.getGoodsId());
			if(goodsConfig.isBind()){
				return new Object[]{false,AppErrorCode.createError(AppErrorCode.TRADE_GOODS_BIND,selfData.getSelfName())};
			}else if(goods.getCount() < count){
				return new Object[]{false,AppErrorCode.createError(AppErrorCode.TRADE_GOODS_COUNT_ERROR, selfData.getSelfName())};
			}
			
			//过期物品处理
			if(goods.isExpireTime()){
				return new Object[]{false,AppErrorCode.createError(AppErrorCode.EXPIRE_ERROR,selfData.getSelfName())};
			}
			
			//guid是否重复
			boolean isContains = selfData.isContains(guid);
			if(isContains){
				return new Object[]{false,AppErrorCode.createError(AppErrorCode.CONTAINS_ERROR,selfData.getSelfName())};
			}
			
			selfData.addGoodsGuids(guid, tradeIndex);
			selfData.addGoodsCount(guid, count);
		}else{
			selfData.removeGuid(tradeIndex);
		}
		
		return new Object[]{true,goods};
	}
	
	/**
	 * 验证角色是否在线
	 * @param selfRoleId
	 * @param targetRoleId
	 * @param stageId
	 * @return
	 */
	private Object[] checkOnline(Long targetRoleId){
		boolean isOnline = publicRoleStateExportService.isPublicOnline(targetRoleId);
		if(!isOnline){
			return new Object[]{false,AppErrorCode.TRADE_ROLE_NO_ONLINE};
		}
		
		return null;
	}
	
//	/**
//	 * 检查是否再同一场景中，
//	 * @return
//	 */
//	private Object[] checkState(long selfRoleId,long otherRoleId){
//		String selfStageId = publicRoleStateExportService.getRolePublicStageId(selfRoleId); 
//		String otherStageId = publicRoleStateExportService.getRolePublicStageId(otherRoleId);
//		
//	}
	
	/**
	 * 获取角色
	 * @param userRoleId
	 * @param stageId
	 * @return
	 */
	private RoleWrapper getStageRole(Long userRoleId){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		return roleWrapper;
	}
	
	/**
	 * 验证不在交易状态
	 * @param selfRoleId
	 * @param targetRoleId
	 * @param stageId
	 * @return
	 */
	private Object[] checkNoTradeState(Long selfRoleId,Long targetRoleId){
		TradeData selfData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, selfRoleId.toString());
		TradeData targetData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, targetRoleId.toString());
		
		if(selfData != null ){
			return new Object[]{false,new Object[]{AppErrorCode.ROLE_SELF_TRADEING}};
		}
		if(targetData != null){
			return new Object[]{false,new Object[]{AppErrorCode.ROLE_TRADEING,targetData.getSelfName()}};
		}
		
		return null;
	}
	
	/**
	 * 验证指定的交易状态
	 * @param selfRoleId
	 * @param targetRoleId
	 * @param stageId
	 * @param tradeStage
	 * @return
	 */
	private Object checkAppointTradeState(Long selfRoleId,Long targetRoleId,int tradeStage){
		TradeData selfData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, selfRoleId.toString());
		TradeData targetData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, targetRoleId.toString());
		
		if(selfData == null || targetData == null){
			return AppErrorCode.TRADE_EXCEPTION;
		}
		
		if(selfData.getState() != tradeStage){
			return AppErrorCode.createError(AppErrorCode.ROLE_STATE_TRADE_ERROR, selfData.getSelfName());
		}
		if(targetData.getState() < tradeStage){
			return AppErrorCode.createError(AppErrorCode.ROLE_STATE_TRADE_ERROR,targetData.getSelfName());
		}
		
		return null;
	}

	/**
	 * 验证指定的交易状态
	 * @param selfRoleId
	 * @param targetRoleId
	 * @param stageId
	 * @param tradeStage
	 * @return
	 */
	private Object[] checkAppointTradeStateCheck(Long selfRoleId,Long targetRoleId){
		TradeData selfData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, selfRoleId.toString());
		TradeData targetData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, targetRoleId.toString());
		
		if(selfData == null) {
			return new Object[]{false,AppErrorCode.TRADE_EXCEPTION};
		}
		if(targetData == null) {
			return new Object[]{false,AppErrorCode.TRADE_EXCEPTION};
		}
		
		if(selfData.getState() != GameConstants.START_TRADE && selfData.getState() != GameConstants.CONFIRM_TRADE){
			return new Object[]{false,AppErrorCode.createError(AppErrorCode.ROLE_STATE_TRADE_ERROR,selfData.getSelfName())};
		}
		if(targetData.getState() != GameConstants.START_TRADE && targetData.getState() != GameConstants.CONFIRM_TRADE){
			return new Object[]{false,AppErrorCode.createError(AppErrorCode.ROLE_STATE_TRADE_ERROR,targetData.getSelfName())};
		}
		
		return null;
	}
	
	public void offline(long userRoleId){
		TradeData selfData = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, userRoleId+"");
		if(selfData != null){
			//通知取消交易 
			long otherRoleId = selfData.getOtherRoleId();
			String roleName = getRoleName(userRoleId);
			
			PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_CANCEL,AppErrorCode.createError(AppErrorCode.ROLE_OFF_LINE,roleName));
			
			dataContainer.removeData(GameConstants.COMPONENET_TREAD_NAME, userRoleId+"");
			dataContainer.removeData(GameConstants.COMPONENET_TREAD_NAME, otherRoleId+"");
		}
		
//		//清除交易前邀请状态
//		Object targetFlag = dataContainer.getData(GameConstants.COMPONENET_TREAD_TARGET, userRoleId+"");
//		if(targetFlag!=null){
//			dataContainer.removeData(GameConstants.COMPONENET_TREAD_TARGET, userRoleId+"");
//		}
	}
}
