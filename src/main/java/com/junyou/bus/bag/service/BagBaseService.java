package com.junyou.bus.bag.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.BagContants;
import com.junyou.bus.bag.BagOutputWrapper;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.OperationType;
import com.junyou.bus.bag.OutputType;
import com.junyou.bus.bag.TradesRollback;
import com.junyou.bus.bag.configure.export.BagConfigExportService;
import com.junyou.bus.bag.configure.export.StorageConfigExportService;
import com.junyou.bus.bag.dao.RoleBagDao;
import com.junyou.bus.bag.dao.RoleItemDescDao;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.entity.RoleItemDesc;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.manage.AbstractContainer;
import com.junyou.bus.bag.manage.Bag;
import com.junyou.bus.bag.manage.BagItems;
import com.junyou.bus.bag.manage.BagManage;
import com.junyou.bus.bag.manage.ChongwuItems;
import com.junyou.bus.bag.manage.ShenQiItems;
import com.junyou.bus.bag.manage.SortRemoveComparator;
import com.junyou.bus.bag.manage.SortRoleItemByIDComparator;
import com.junyou.bus.bag.manage.SortRoleItemComparator;
import com.junyou.bus.bag.manage.StorageItems;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.bag.vo.RoleItemOperation;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.chongwu.entity.RoleChongwu;
import com.junyou.bus.chongwu.service.export.ChongwuExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.equip.configure.export.SuiJiShuXingConfig;
import com.junyou.bus.equip.configure.export.SuiJiShuXingConfigExportService;
import com.junyou.bus.kuafuarena1v1.service.export.KuafuArena1v1SourceExportService;
import com.junyou.bus.qiling.export.QiLingExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.role.export.TangbaoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.tianyu.export.TianYuExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wuqi.export.WuQiExportService;
import com.junyou.bus.xianjian.export.XianJianExportService;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.bus.zhanjia.export.ZhanJiaExportService;
import com.junyou.bus.zhuansheng.export.ZhuanshengExportService;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.err.HttpErrorCode;
import com.junyou.event.RoleItemLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.EquipTypeSlot;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;
import com.kernel.data.accessor.AccessType;
import com.kernel.sync.annotation.Sync;
 

/**
 * 仅bag底层调用的业务方法
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午3:38:12
 *@Description: 
 */
public class BagBaseService{
	
	@Autowired
	private RoleBagDao roleBagDao;
	@Autowired
	private RoleItemDescDao roleItemDescDao;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private GoodsUseLimitService goodsUseLimitService;
	@Autowired
	private BagConfigExportService bagConfigExportService;
	@Autowired
	private StorageConfigExportService storageConfigExportService;
	@Autowired
	protected EmailExportService emailExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private SuiJiShuXingConfigExportService suiJiShuXingConfigExportService;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private ChiBangExportService chiBangExportService;
	@Autowired
	private XianJianExportService xianJianExportService;
	@Autowired
	private ZhanJiaExportService zhanJiaExportService;
	@Autowired
	private WuQiExportService wuQiExportService;
	@Autowired
	private TangbaoExportService tangbaoExportService;
	@Autowired
	private GuildExportService guildExportService;
	@Autowired
	private KuafuArena1v1SourceExportService kuafuArena1v1SourceExportService;
	@Autowired
	private QiLingExportService qiLingExportService;
	@Autowired
	private TianYuExportService tianYuExportService;
	@Autowired
	private ChongwuExportService chongwuExportService;
	@Autowired
	private ZhuanshengExportService zhuanshengExportService;
	/**
	 * 登录时加载背包数据
	 * @param roleId
	 */
	public List<RoleItem> initAll(long userRoleId) {
		List<RoleItem> roleItems=roleBagDao.initAll(userRoleId);
		//过滤掉没有用的物品
		filterGoodsIdForCfg(roleItems,true);
		//加载数据到bagManage缓存中去
		initBagItem(userRoleId, roleItems);
 
		return roleItems;
	}
	
	/**
	 * 将不符合的物品移除队列
	 * @param roleItems
	 * @param isload 是否是启动时加载检查，启动时加载检查直接操作db数据库。
	 */
	private void filterGoodsIdForCfg(List<RoleItem> roleItems,boolean isload){
		Iterator<RoleItem> iterator=roleItems.iterator();
		while(iterator.hasNext()){
			RoleItem roleItem=iterator.next();
			GoodsConfig goodsConfig=goodsConfigExportService.loadById(roleItem.getGoodsId());
			if(goodsConfig==null){
				iterator.remove();
				roleItem.setIsDelete(BagContants.DELETE);
				if(isload){
					roleBagDao.update(roleItem,roleItem.getUserRoleId(), AccessType.getDirectDbType());
				}else{
					roleBagDao.cacheUpdate(roleItem,roleItem.getUserRoleId());
				}
			}
		}
	}
	
	/**
	 * 初始化背包描述信息
	 * @param userRoleId
	 * @return
	 */
	public  RoleItemDesc initDescAll(long userRoleId){
		//初始化背包描述信息
		RoleItemDesc roleItemDesc=roleItemDescDao.load(userRoleId, userRoleId, AccessType.getDirectDbType());
		if(roleItemDesc==null){
			roleItemDesc=new RoleItemDesc();
			int bagEndSlot=bagConfigExportService.getBagEndSlot();
			roleItemDesc.setBagKyTime(0l);
			roleItemDesc.setBagOpeningSlot(bagEndSlot+1);
			roleItemDesc.setBagUpdateTime(GameSystemTime.getSystemMillTime());
			
			int storageEndSlot=storageConfigExportService.getEndSlot();
			roleItemDesc.setStorageKyTime(0l); 
			roleItemDesc.setStorageUpdateTime(GameSystemTime.getSystemMillTime());
			roleItemDesc.setStorageOpeningSlot(storageEndSlot+1);
			
			roleItemDesc.setUserRoleId(userRoleId);
			//初始化数据库
			roleItemDescDao.insert(roleItemDesc, userRoleId, AccessType.getDirectDbType());
		}
		
		Bag bag=getBag(userRoleId);
		HashMap<Integer, Integer> data=new HashMap<>();
		int bagBSlot=bagConfigExportService.getBagStartSlot();
		int storageBSlot=storageConfigExportService.getStartSlot();
		data.put(BagContants.BAG_B_SLOT,bagBSlot);
		data.put(BagContants.BAG_E_SLOT,roleItemDesc.getBagOpeningSlot()-1);
		data.put(BagContants.Storage_B_SLOT,storageBSlot);
		data.put(BagContants.Stroage_E_SLOT,roleItemDesc.getStorageOpeningSlot()-1);
		
		for (AbstractContainer container : bag.getContainers()) {
			container.setContainerDesc(data);
		}
		
		bag.resetEmptySlot();
		
		return roleItemDesc;
	}
	
	/**
	 * 加载db中背包物品信息
	 * @param roleId
	 */
	private void initBagItem(Long roleId,List<RoleItem> roleItems){
		Bag  bag=new Bag();
		bag.setRoleId(roleId); 
		//容器初始化 
		for (ContainerType s : ContainerType.values()){
			AbstractContainer obj = ContainerType.getClassObj(s);
			if(obj==null) continue; 
			bag.addBagStores(obj); 
		}
		BagManage.addBag(bag);
		  
		for (RoleItem roleItem : roleItems){
			AbstractContainer stores = bag.getStore(roleItem.getContainerType());
			if(stores == null) continue;
			stores.addItem(roleItem);
		}
	}
	
	
	/**
	 * 发送添加新物品直接通知前端信息
	 */
	private void sendNotifyGoods(List<RoleItemOperation> roleItemVos,long userRoleId) {
		if(roleItemVos == null) {
			return;
		}
		
		List<RoleItemOperation> sendUpdates=new ArrayList<>();
		List<RoleItemOperation> sendAdds=new ArrayList<>();
		
		for (RoleItemOperation roleItemAction : roleItemVos) {
			int operationType=roleItemAction.getOperationType();
			if(operationType==OperationType.ADD){
				sendAdds.add(roleItemAction);
			}else if(operationType==OperationType.UPDATE||operationType==OperationType.DELETE){
				sendUpdates.add(roleItemAction);
			}
		}
		
		if(sendUpdates.size()>0){
			sendNotifyGoodsChanage(userRoleId, sendUpdates);
		}
		
		if(sendAdds.size()>0){
			sendNotifyGoodsAdd(sendAdds, userRoleId);
		}
	}
	
	private void sendNotifyGoodsAdd(List<RoleItemOperation> roleItemVos,long userRoleId) {
		if(null == roleItemVos || roleItemVos.size() ==0){
			return ;
		}
		Object[] result=new Object[roleItemVos.size()];
		 
		for (int i=0;i<roleItemVos.size();i++) {
			result[i]=BagOutputWrapper.getOutWrapperData(OutputType.ITEMVO, roleItemVos.get(i).getRoleItem());
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.PUT_IN_BAG, result);
	}
 
	/**
	 * 添加物品入背包
	 * @param goods
	 * @param userRoleId 	玩家id
	 * @param source 		物品来源
	 * @param isRecord 		是否记录
	 * @return
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {1})
	protected BagSlots putInBag(RoleItemInput goods,long userRoleId, int source, boolean isRecord){
		BagSlots bagSlots=new BagSlots();
		Object[] errorCode=checkPutInBag(goods.getGoodsId(),goods.getCount(),userRoleId);
		if(errorCode!=null){
			bagSlots.setErrorCode(errorCode);
			return bagSlots;
		}
		
		RoleItem roleItem=BagUtil.converInsert2RoleItem(goods, userRoleId);
		boolean isNewSlot=goods.isNewSlot();
		if(isNewSlot){
			RoleItemOperation vo=addItem(roleItem, OperationType.ADD, null,ContainerType.BAGITEM);
			bagSlots.addRoleItemVo(vo);
		}else{
			List<RoleItemOperation> vos= addItem2Bag(roleItem, OperationType.ADD);
			bagSlots.addRoleItemVos(vos);
		}
		sendNotifyGoods(bagSlots.getRoleItemVos(), userRoleId); 
		//打印日志
		if(isRecord){
			recordRoleItemLog(LogPrintHandle.CONTAINER_GET_GOODS, source, bagSlots.getRoleItemVos(), userRoleId, null);
		}
		return bagSlots;
	}
	
	
	public Object[] checkPutInBag(String goodsId,int count, long userRoleId) {
		//检查配置表中是否有该物品
		GoodsConfig  goodsConfig= goodsConfigExportService.loadById(goodsId);
		if(goodsConfig==null){
			return AppErrorCode.NO_FIND_CONFIG;
		}

		if(goodsConfig.getCategory() <=0 ){
			ChuanQiLog.error("数值类型不可入背包！！ goodsId:"+goodsId+"\t category:"+goodsConfig.getCategory());
			return AppErrorCode.GOODS_TYPE_ERROR;
		}
		
		if(count <= 0){
			return AppErrorCode.GOODSID_COUNT_ISNULL;
		}
		
		int repeatCount=BagUtil.getItemRepeatCount(goodsId);
		
		int needSlotCount=count/repeatCount+(count%repeatCount>0?1:0);
		
		int bagEmptyCount = getContainerEmptyCount(ContainerType.BAGITEM, userRoleId);

		if(needSlotCount>bagEmptyCount){
			return AppErrorCode.BAG_NOEMPTY;
		}
		
		return null;
	}
	
	/**
	 * 物品移动的处理接口 
	 * @param sourceGuid			源格位对应的物品唯一id
	 * @param targetSlot 			将要移动到的目标格位
	 * @param targetContainerType 	将要移动到的目标容器
	 * @param userRoleId			操作者(角色id)
	 * @return						BagSlots
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {3})
	protected BagSlots moveSlot(long sourceGuid,Integer targetSlot,Integer targetContainerType,long userRoleId, Integer configId){
		
		RoleItem roleItem=roleBagDao.cacheLoad(sourceGuid, userRoleId);
		if(roleItem==null){
			BagSlots bagSlots=new BagSlots();
			bagSlots.setErrorCode(AppErrorCode.NOT_FOUND_GOOODS); 
			return bagSlots;
		}
		int sourceContainerType=roleItem.getContainerType();
		
		//背包移动到背包
		if(sourceContainerType==ContainerType.BAGITEM.getType()&&targetContainerType==ContainerType.BAGITEM.getType()){
			return moveContainerSlot(sourceGuid, targetSlot, userRoleId, ContainerType.BAGITEM);
		}
		//仓库到仓库
		if(sourceContainerType==ContainerType.STORAGEITEM.getType()&&targetContainerType==ContainerType.STORAGEITEM.getType()){
			return moveContainerSlot(sourceGuid, targetSlot, userRoleId, ContainerType.STORAGEITEM);
		}
		//背包到仓库
		if(sourceContainerType==ContainerType.BAGITEM.getType()&&targetContainerType==ContainerType.STORAGEITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId, ContainerType.BAGITEM, ContainerType.STORAGEITEM);
		}
		//仓库到背包 
		if(sourceContainerType==ContainerType.STORAGEITEM.getType()&&targetContainerType==ContainerType.BAGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.STORAGEITEM,ContainerType.BAGITEM);
		}
		
		//身上-->背包
		if(sourceContainerType==ContainerType.BODYTITEM.getType()&&targetContainerType==ContainerType.BAGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId, ContainerType.BODYTITEM, ContainerType.BAGITEM);
		}
		//背包-->身上
		if(sourceContainerType==ContainerType.BAGITEM.getType()&&targetContainerType==ContainerType.BODYTITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.BAGITEM, ContainerType.BODYTITEM);
		}
		 
		//坐骑-->背包
		if(sourceContainerType==ContainerType.ZUOQIITEM.getType()&&targetContainerType==ContainerType.BAGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.ZUOQIITEM, ContainerType.BAGITEM);
		}
		//背包-->坐骑
		if(sourceContainerType == ContainerType.BAGITEM.getType()&&targetContainerType==ContainerType.ZUOQIITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.BAGITEM, ContainerType.ZUOQIITEM);
		}
		
		//翅膀-->背包
		if(sourceContainerType==ContainerType.CHIBANGITEM.getType()&&targetContainerType==ContainerType.BAGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.CHIBANGITEM, ContainerType.BAGITEM);
		}
		//背包-->翅膀
		if(sourceContainerType == ContainerType.BAGITEM.getType()&&targetContainerType==ContainerType.CHIBANGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.BAGITEM, ContainerType.CHIBANGITEM);
		}
		
		//糖宝-->背包
		if(sourceContainerType==ContainerType.TANGBAOITEM.getType()&&targetContainerType==ContainerType.BAGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.TANGBAOITEM, ContainerType.BAGITEM);
		}
		//背包-->糖宝
		if(sourceContainerType == ContainerType.BAGITEM.getType()&&targetContainerType==ContainerType.TANGBAOITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.BAGITEM, ContainerType.TANGBAOITEM);
		}
		
		//天工-->背包
		if(sourceContainerType==ContainerType.TIANGONGITEM.getType()&&targetContainerType==ContainerType.BAGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.TIANGONGITEM, ContainerType.BAGITEM);
		}
		//背包-->天工
		if(sourceContainerType == ContainerType.BAGITEM.getType()&&targetContainerType==ContainerType.TIANGONGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.BAGITEM, ContainerType.TIANGONGITEM);
		}
		
		//天裳-->背包
		if(sourceContainerType==ContainerType.TIANSHANGITEM.getType()&&targetContainerType==ContainerType.BAGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.TIANSHANGITEM, ContainerType.BAGITEM);
		}
		//背包-->天裳
		if(sourceContainerType == ContainerType.BAGITEM.getType()&&targetContainerType==ContainerType.TIANSHANGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.BAGITEM, ContainerType.TIANSHANGITEM);
		}
		
		//器灵-->背包
		if(sourceContainerType==ContainerType.QILINGITEM.getType()&&targetContainerType==ContainerType.BAGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.QILINGITEM, ContainerType.BAGITEM);
		}
		//背包-->器灵
		if(sourceContainerType == ContainerType.BAGITEM.getType()&&targetContainerType==ContainerType.QILINGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.BAGITEM, ContainerType.QILINGITEM);
		}
		//天羽-->背包
		if(sourceContainerType==ContainerType.TIANYUITEM.getType()&&targetContainerType==ContainerType.BAGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.TIANYUITEM, ContainerType.BAGITEM);
		}
		//背包-->天羽
		if(sourceContainerType == ContainerType.BAGITEM.getType()&&targetContainerType==ContainerType.TIANYUITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.BAGITEM, ContainerType.TIANYUITEM);
		}
		//宠物-->背包
		if(sourceContainerType==ContainerType.CHONGWUITEM.getType()&&targetContainerType==ContainerType.BAGITEM.getType()){
		    return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.CHONGWUITEM, ContainerType.BAGITEM);
		}
		//背包-->宠物
		if(sourceContainerType == ContainerType.BAGITEM.getType()&&targetContainerType==ContainerType.CHONGWUITEM.getType()){
		    return moveSource2TargetContainer2(sourceGuid, targetSlot, userRoleId,ContainerType.BAGITEM, ContainerType.CHONGWUITEM, configId);
		}
		//神器-->背包
		if(sourceContainerType==ContainerType.SHENQIITEM.getType()&&targetContainerType==ContainerType.BAGITEM.getType()){
		    return moveSource2TargetContainer3(sourceGuid, targetSlot, userRoleId,ContainerType.SHENQIITEM, ContainerType.BAGITEM,0);
		}
		//背包-->神器
		if(sourceContainerType == ContainerType.BAGITEM.getType()&&targetContainerType==ContainerType.SHENQIITEM.getType()){
			if(configId !=null && configId >=1){
				return moveSource2TargetContainer3(sourceGuid, targetSlot, userRoleId,ContainerType.BAGITEM, ContainerType.SHENQIITEM, configId);
			}
		}
		
		//圣剑-->背包
		if(sourceContainerType==ContainerType.WUQI.getType()&&targetContainerType==ContainerType.BAGITEM.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.WUQI, ContainerType.BAGITEM);
		}
		//背包-->圣剑
		if(sourceContainerType == ContainerType.BAGITEM.getType()&&targetContainerType==ContainerType.WUQI.getType()){
			return moveSource2TargetContainer(sourceGuid, targetSlot, userRoleId,ContainerType.BAGITEM, ContainerType.WUQI);
		}
		
		BagSlots bagSlots=new BagSlots(); 
		bagSlots.setErrorCode(AppErrorCode.NO_OPERATION_MOVE);
		ChuanQiLog.error("格位移动错误: sourceGuid:"+sourceGuid+"\t targetSlot:"+targetSlot+"\t sourceContainerType:"+sourceContainerType+"\t targetContainerType:"+targetContainerType+"\t userRoleId:"+userRoleId);
		return bagSlots;
	} 
	 
	
	 
	/**
	 * 消耗物品 消耗具体的物品
	 * @param goodsId  物品的表示Id
	 * @param count
	 * @param userRoleId
	 * @param resource
	 * @param isRecord
	 * @param isNotify  是否直接通知客服端物品变化信息
	 * @return
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {2})
	protected BagSlots removeBagItemByGoodsId(String goodsId,int count,long userRoleId,int source,boolean isRecord,boolean isNotify){
		BagSlots bagSlots=new BagSlots();
		Object[] flagCode=checkRemoveBagItemByGoodsId(goodsId, count, userRoleId);
		if(flagCode!=null){
			bagSlots.setErrorCode(flagCode);
			return bagSlots;
		}
		
		List<RoleItemOperation> roleItemVos=removeBagItemByItemId(goodsId, count, userRoleId,false);
		bagSlots.addRoleItemVos(roleItemVos);
		if(isNotify){
			sendNotifyGoodsChanage(userRoleId, bagSlots.getRoleItemVos());
		}
		if(isRecord){
			recordRoleItemLog(LogPrintHandle.CONTAINER_REMOVE_GOODS, source, bagSlots.getRoleItemVos(), userRoleId, null);
		}
		return bagSlots;
	}
	
	/**
	 * 通知客服端物品变化信息
	 * @param userRoleId
	 * @param bagSlots
	 */
	private void sendNotifyGoodsChanage(long userRoleId,List<RoleItemOperation> roleItemVos){
		Object[] result=new Object[roleItemVos.size()];
		for (int i=0;i<roleItemVos.size();i++) {
			result[i]=BagOutputWrapper.getOutWrapperData(OutputType.MODIFY,roleItemVos.get(i));
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.ITEM_COUNT_MODIFY, result);
	}
	
	
	/**
	 * 消耗物品,不指定消耗物品绑定或非绑定
	 * @param goodsType :配置中指定的是某类型的物品,其中可能包含了绑定和非绑定
	 * @param count
	 * @param userRoleId
	 * @param source
	 * @param isRecord
	 * @param isNotify
	 * @return
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {2})
	protected BagSlots removeBagItemByGoodsType(String goodsType,int count,long userRoleId,int source,boolean isRecord,boolean isNotify){
		BagSlots bagSlots=new BagSlots();
		Object[] flagCode=checkRemoveBagItemByGoodsType(goodsType,count,userRoleId);
		if(flagCode!=null){
			bagSlots.setErrorCode(flagCode);
			return bagSlots;
		}
		
		List<RoleItemOperation> roleItemVos=removeBagItemByItemId(goodsType, count, userRoleId,true);
		bagSlots.addRoleItemVos(roleItemVos);
		
		if(isNotify){
			sendNotifyGoodsChanage(userRoleId, bagSlots.getRoleItemVos());
		}
		if(isRecord){
			recordRoleItemLog(LogPrintHandle.CONTAINER_REMOVE_GOODS, source, bagSlots.getRoleItemVos(), userRoleId, null);
		}
		return bagSlots;
	}
	
	public Object[] checkRemoveBagItemByGoodsType(String goodsIdType, int count,long userRoleId) {
		int owerCount=0;
		List<String> goodsIds=goodsConfigExportService.loadIdsById1(goodsIdType);
		List<RoleItem> roleItems= getContainerItems(userRoleId, ContainerType.BAGITEM.getType());
		if(roleItems == null || roleItems.size() < 1){
			return AppErrorCode.ITEM_COUNT_ENOUGH;
		}
		for (RoleItem roleItem : roleItems) {
			if(goodsIds.contains(roleItem.getGoodsId()) && !roleItem.isExpireTimed()){
				owerCount+=roleItem.getCount();
			}
		}
		if(owerCount<count){
			return AppErrorCode.ITEM_COUNT_ENOUGH;
		}
		
		 
		return null;
	}

	private List<RoleItemOperation> removeBagItemByItemId(String goodsId,int count,long userRoleId,boolean isGoodsType){
		List<RoleItemOperation> itemVos=new ArrayList<RoleItemOperation>();
		List<RoleItem> roleItems=null;
		if(isGoodsType){
			roleItems=getBagItemByGoodsType(goodsId,userRoleId);
		}else{
			roleItems=getBagItemByGoodsId(goodsId,userRoleId);
		}
		
		/**  确定消耗规则  */
		Collections.sort(roleItems, new SortRemoveComparator());
		
		for (RoleItem roleItem : roleItems) { 
			long guid=roleItem.getId();
			int  itemCount=roleItem.getCount();
			
			int realRemoveCount=0;//真实需要移除的数量 
			if(count>=itemCount){
				realRemoveCount=itemCount;
				count-=realRemoveCount; 
			}else{
				realRemoveCount=count;
				count=0;
			}
			RoleItemOperation vo=removeItem(userRoleId, guid, realRemoveCount, OperationType.DELETE,ContainerType.BAGITEM);
			
			itemVos.add(vo);
			
			if(count==0)break;
		}
		return itemVos;
	}
	
	private Bag getBag(long userRoleId){
		return BagManage.getBag(userRoleId); 
	} 
	
	private <T extends AbstractContainer> T getContainer(Class<T> c, ContainerType containerType,long userRoleId) {
		Bag bag=getBag(userRoleId);
		if(bag == null){
			return null;
		}
		return bag.getStore(c, containerType.getType());
	}

	public AbstractContainer getContainer(ContainerType containerType,long userRoleId) {
		Bag bag=getBag(userRoleId);
		if(bag == null){
			return null;
		}
		return bag.getStore(containerType.getType());
	}

	/**
	 * 背包格位移动叠加
	 * @param sourceSlot: 需要移动的格位
	 * @param targetSlot: 要移动到的格位
	 * @param userRoleId
	 * @return
	 */
	private BagSlots moveContainerSlot(long sourceGuid,int targetSlot,long userRoleId,ContainerType containerType){
		
		BagSlots bagSlots=new BagSlots();
		Object[] flagCode=checkMoveSlot(sourceGuid, targetSlot, userRoleId,containerType);
		if(flagCode!=null){
			bagSlots.setErrorCode(flagCode);
			return bagSlots;
		}
		//加入到新的格位
		RoleItem sourceRoleItem=getItemByGuid(userRoleId, sourceGuid, containerType);
		if(sourceRoleItem == null){
			bagSlots.setErrorCode(AppErrorCode.NOT_FOUND_GOOODS);
			return bagSlots;
		}
		RoleItem targetRoleItem=getItemBySlot(targetSlot, userRoleId,containerType);
		int sourceSlot=sourceRoleItem.getSlot();
		
		//新格位上有物品
		if(targetRoleItem!=null){
			int sourceCount=sourceRoleItem.getCount();
			int targetCount=targetRoleItem.getCount();
			int repeatCount=BagUtil.getItemRepeatCount(sourceRoleItem.getGoodsId());
			
			//相同物品 且不属于直接互换位置的
			if(sourceRoleItem.getGoodsId().equals(targetRoleItem.getGoodsId())&&sourceCount!=repeatCount&&targetCount!=repeatCount&&sourceRoleItem.getExpireTime().equals(targetRoleItem.getExpireTime())){
				 
				if(sourceCount+targetCount>repeatCount){
					targetRoleItem.setCount(repeatCount);
					sourceRoleItem.setCount(sourceCount+targetCount-repeatCount); 
					
					roleBagDao.cacheUpdate(targetRoleItem, userRoleId); 
					RoleItemOperation targetVo=createRoleItemVo(targetRoleItem, containerType, OperationType.UPDATE,targetCount);
					bagSlots.addRoleItemVo(targetVo);
					
					roleBagDao.cacheUpdate(sourceRoleItem, userRoleId); 
					RoleItemOperation sourceVo=createRoleItemVo(sourceRoleItem, containerType, OperationType.UPDATE,sourceCount);
					bagSlots.addRoleItemVo(sourceVo); 
				}else{
					RoleItemOperation sourceVo=removeItem(userRoleId, sourceRoleItem.getId(), sourceRoleItem.getCount(), OperationType.DELETE,containerType);
					bagSlots.addRoleItemVo(sourceVo);
					
					targetRoleItem.setCount(sourceCount+targetCount);
					roleBagDao.cacheUpdate(targetRoleItem,userRoleId); 
					RoleItemOperation targetVo=createRoleItemVo(targetRoleItem, containerType, OperationType.UPDATE,targetCount);
					bagSlots.addRoleItemVo(targetVo);
				}
			}else{
				//直接互换位置即可
				sourceRoleItem.setSlot(targetSlot);
				RoleItemOperation sourceVo=createRoleItemVo(sourceRoleItem, containerType, OperationType.UPDATE,sourceCount);
				bagSlots.addRoleItemVo(sourceVo);
				
				targetRoleItem.setSlot(sourceSlot); 
				RoleItemOperation targetVo=createRoleItemVo(targetRoleItem,containerType, OperationType.UPDATE,targetCount);
				bagSlots.addRoleItemVo(targetVo);
				
				roleBagDao.cacheUpdate(sourceRoleItem, userRoleId); 
				roleBagDao.cacheUpdate(targetRoleItem, userRoleId);
			}
		}else{	 
				removeItem(userRoleId, sourceGuid, sourceRoleItem.getCount(), OperationType.NOOPERATION, containerType);
				RoleItemOperation sourceVo = addItem(sourceRoleItem, OperationType.UPDATE, targetSlot, containerType);
				bagSlots.addRoleItemVo(sourceVo);
		}
		return bagSlots;
	}
	
	private int getBagBeginSlot(long userRoleId){
		BagItems bagItems=getContainer(BagItems.class, ContainerType.BAGITEM, userRoleId);
		if(bagItems == null){
			return Integer.MAX_VALUE;
		}
		return bagItems.getBeginSlot();
	}
	
	private int getBeginSlot(long userRoleId,ContainerType containerType){
		AbstractContainer bagItems=getContainer(containerType, userRoleId);
		if(bagItems == null){
			return Integer.MAX_VALUE;
		}
		return bagItems.getBeginSlot();
	}
	
	private int getStorageBeginSlot(long userRoleId){
		StorageItems bagItems=getContainer(StorageItems.class, ContainerType.STORAGEITEM, userRoleId);
		if(bagItems == null){
			return Integer.MAX_VALUE;
		}
		return bagItems.getBeginSlot();
	}
	
	private int getEndSlot(long userRoleId,ContainerType cType){
		AbstractContainer container = getContainer(cType, userRoleId);
		if(container == null){
			return Integer.MIN_VALUE;
		}
		return container.getEndSlot();
	}
	 

	private RoleItemOperation createRoleItemVo(RoleItem roleItem,ContainerType type,byte operationType,int oldCount){
		return new RoleItemOperation(oldCount,roleItem,operationType,(byte)type.getType());
	} 

	private BagSlots moveSource2TargetContainer(long sourceGuid,int targetSlot,long userRoleId,ContainerType sourceCType,ContainerType targetCType){
	    return moveSource2TargetContainer2(sourceGuid, targetSlot, userRoleId, sourceCType, targetCType, 0);
	}
	
//	private BagSlots moveSource2TargetContainer1(long sourceGuid,int targetSlot,long userRoleId,ContainerType sourceCType,ContainerType targetCType){
//	    return moveSource2TargetContainer3(sourceGuid, targetSlot, userRoleId, sourceCType, targetCType, 0);
//	}
	
	/**夸容器
	 * @param sourceGuid 
	 * @param targetSlot 
	 * @param userRoleId
	 * @param chongwuId 宠物编号
	 * @return
	 */
    private BagSlots moveSource2TargetContainer2(long sourceGuid, int targetSlot, long userRoleId, ContainerType sourceCType, ContainerType targetCType, Integer chongwuId) {

        BagSlots bagSlots = new BagSlots();

        Object[] flagCode = checkSource2TargetContainer(sourceGuid, targetSlot, userRoleId, targetCType, chongwuId);
        if (flagCode != null) {
            bagSlots.setErrorCode(flagCode);
            return bagSlots;
        }

        // 加入到新的格位
        RoleItem sourceRoleItem = getItemByGuid(userRoleId, sourceGuid, sourceCType);
        if (sourceRoleItem == null) {
            bagSlots.setErrorCode(AppErrorCode.NOT_FOUND_GOOODS);
            return bagSlots;
        }
        RoleItem targetRoleItem = ContainerType.CHONGWUITEM.equals(targetCType) ? getItemBySlot(targetSlot, userRoleId, targetCType, chongwuId) :  getItemBySlot(targetSlot, userRoleId, targetCType);

        int sourceSlot = sourceRoleItem.getSlot();

        // 新格位上有物品
        if (targetRoleItem != null) {
            int sourceCount = sourceRoleItem.getCount();
            int targetCount = targetRoleItem.getCount();
            int repeatCount = BagUtil.getItemRepeatCount(sourceRoleItem.getGoodsId());

            // 相同物品且 不属于直接交换位置的情况
            if (sourceRoleItem.getGoodsId().equals(targetRoleItem.getGoodsId()) && sourceCount != repeatCount && targetCount != repeatCount && sourceRoleItem.getExpireTime().equals(targetRoleItem.getExpireTime())) {

                if (sourceCount + targetCount > repeatCount) {

                    targetRoleItem.setCount(repeatCount);
                    RoleItemOperation targetVo = createRoleItemVo(targetRoleItem, targetCType, OperationType.UPDATE, targetCount);
                    bagSlots.addRoleItemVo(targetVo);

                    sourceRoleItem.setCount(sourceCount + targetCount - repeatCount);
                    RoleItemOperation sourceVo = createRoleItemVo(sourceRoleItem, sourceCType, OperationType.UPDATE, sourceCount);
                    bagSlots.addRoleItemVo(sourceVo);

                    roleBagDao.cacheUpdate(targetRoleItem, userRoleId);
                    roleBagDao.cacheUpdate(sourceRoleItem, userRoleId);

                } else {
                    RoleItemOperation sourceVo = removeItem(userRoleId, sourceRoleItem.getId(), sourceRoleItem.getCount(), OperationType.DELETE, ContainerType.BAGITEM);
                    bagSlots.addRoleItemVo(sourceVo);

                    targetRoleItem.setCount(sourceCount + targetCount);
                    roleBagDao.cacheUpdate(targetRoleItem, userRoleId);
                    RoleItemOperation targetVo = createRoleItemVo(targetRoleItem, targetCType, OperationType.UPDATE, targetCount);
                    bagSlots.addRoleItemVo(targetVo);
                }
            } else {
                int sourceChongwuId = null == sourceRoleItem.getChongwuId() ? 0 : sourceRoleItem.getChongwuId();
                int targetChongwuId = null == targetRoleItem.getChongwuId() ? 0 : targetRoleItem.getChongwuId();
                // 直接互换位置即可
                removeItem(userRoleId, sourceRoleItem.getId(), sourceRoleItem.getCount(), OperationType.NOOPERATION, sourceCType);
                removeItem(userRoleId, targetRoleItem.getId(), targetRoleItem.getCount(), OperationType.NOOPERATION, targetCType);

                // 将仓库物品移到背包
                RoleItemOperation addTargetItemVo = addItem2(targetRoleItem, OperationType.UPDATE, sourceSlot, sourceCType, sourceChongwuId);
                // addTargetItemVo.setOperationType(OperationType.UPDATE);
                bagSlots.addRoleItemVo(addTargetItemVo);

                // 添加背包物品到仓库
                RoleItemOperation addSourceItemVo = addItem2(sourceRoleItem, OperationType.UPDATE, targetSlot, targetCType, targetChongwuId);
                // addSourceItemVo.setOperationType(OperationType.UPDATE);
                bagSlots.addRoleItemVo(addSourceItemVo);

                // 对缓存进行更新
                // roleBagDao.cacheUpdate(targetRoleItem, userRoleId);
                // roleBagDao.cacheUpdate(sourceRoleItem, userRoleId);
            }
        } else {
            removeItem(userRoleId, sourceRoleItem.getId(), sourceRoleItem.getCount(), OperationType.NOOPERATION, sourceCType);

            RoleItemOperation addVo = addItem2(sourceRoleItem, OperationType.NOOPERATION, targetSlot, targetCType, chongwuId);
            addVo.setOperationType(OperationType.UPDATE);
            bagSlots.addRoleItemVo(addVo);
            roleBagDao.cacheUpdate(sourceRoleItem, userRoleId);
        }
        return bagSlots;
    }
    
    /**夸容器
	 * @param sourceGuid 
	 * @param targetSlot 
	 * @param userRoleId
	 * @param chongwuId 宠物编号
	 * @return
	 */
    private BagSlots moveSource2TargetContainer3(long sourceGuid, int targetSlot, long userRoleId, ContainerType sourceCType, ContainerType targetCType, Integer shenQiId) {

        BagSlots bagSlots = new BagSlots();
        Object[] flagCode = checkSource2TargetContainer1(sourceGuid, targetSlot, userRoleId, targetCType, shenQiId);
        if (flagCode != null) {
            bagSlots.setErrorCode(flagCode);
            return bagSlots;
        }

        // 加入到新的格位
        RoleItem sourceRoleItem = getItemByGuid(userRoleId, sourceGuid, sourceCType);
        if (sourceRoleItem == null) {
            bagSlots.setErrorCode(AppErrorCode.NOT_FOUND_GOOODS);
            return bagSlots;
        }
        RoleItem targetRoleItem = ContainerType.SHENQIITEM.equals(targetCType) ? getItemBySlot1(targetSlot, userRoleId, targetCType, shenQiId) :  getItemBySlot(targetSlot, userRoleId, targetCType);

        int sourceSlot = sourceRoleItem.getSlot();

        // 新格位上有物品
        if (targetRoleItem != null) {
            int sourceCount = sourceRoleItem.getCount();
            int targetCount = targetRoleItem.getCount();
            int repeatCount = BagUtil.getItemRepeatCount(sourceRoleItem.getGoodsId());

            // 相同物品且 不属于直接交换位置的情况
            if (sourceRoleItem.getGoodsId().equals(targetRoleItem.getGoodsId()) && sourceCount != repeatCount && targetCount != repeatCount && sourceRoleItem.getExpireTime().equals(targetRoleItem.getExpireTime())) {

                if (sourceCount + targetCount > repeatCount) {

                    targetRoleItem.setCount(repeatCount);
                    RoleItemOperation targetVo = createRoleItemVo(targetRoleItem, targetCType, OperationType.UPDATE, targetCount);
                    bagSlots.addRoleItemVo(targetVo);

                    sourceRoleItem.setCount(sourceCount + targetCount - repeatCount);
                    RoleItemOperation sourceVo = createRoleItemVo(sourceRoleItem, sourceCType, OperationType.UPDATE, sourceCount);
                    bagSlots.addRoleItemVo(sourceVo);

                    roleBagDao.cacheUpdate(targetRoleItem, userRoleId);
                    roleBagDao.cacheUpdate(sourceRoleItem, userRoleId);

                } else {
                    RoleItemOperation sourceVo = removeItem(userRoleId, sourceRoleItem.getId(), sourceRoleItem.getCount(), OperationType.DELETE, ContainerType.BAGITEM);
                    bagSlots.addRoleItemVo(sourceVo);

                    targetRoleItem.setCount(sourceCount + targetCount);
                    roleBagDao.cacheUpdate(targetRoleItem, userRoleId);
                    RoleItemOperation targetVo = createRoleItemVo(targetRoleItem, targetCType, OperationType.UPDATE, targetCount);
                    bagSlots.addRoleItemVo(targetVo);
                }
            } else {
                int sourceShenQiId = null == sourceRoleItem.getShenqiId() ? 0 : sourceRoleItem.getShenqiId();
                int targetShenQiId = null == targetRoleItem.getShenqiId() ? 0 : targetRoleItem.getShenqiId();
                // 直接互换位置即可
                removeItem(userRoleId, sourceRoleItem.getId(), sourceRoleItem.getCount(), OperationType.NOOPERATION, sourceCType);
                removeItem(userRoleId, targetRoleItem.getId(), targetRoleItem.getCount(), OperationType.NOOPERATION, targetCType);

                // 将仓库物品移到背包
                RoleItemOperation addTargetItemVo = addItem3(targetRoleItem, OperationType.UPDATE, sourceSlot, sourceCType, sourceShenQiId);
                // addTargetItemVo.setOperationType(OperationType.UPDATE);
                bagSlots.addRoleItemVo(addTargetItemVo);

                // 添加背包物品到仓库
                RoleItemOperation addSourceItemVo = addItem3(sourceRoleItem, OperationType.UPDATE, targetSlot, targetCType, targetShenQiId);
                // addSourceItemVo.setOperationType(OperationType.UPDATE);
                bagSlots.addRoleItemVo(addSourceItemVo);

                // 对缓存进行更新
                // roleBagDao.cacheUpdate(targetRoleItem, userRoleId);
                // roleBagDao.cacheUpdate(sourceRoleItem, userRoleId);
            }
        } else {
            removeItem(userRoleId, sourceRoleItem.getId(), sourceRoleItem.getCount(), OperationType.NOOPERATION, sourceCType);

            RoleItemOperation addVo = addItem3(sourceRoleItem, OperationType.NOOPERATION, targetSlot, targetCType, shenQiId);
            addVo.setOperationType(OperationType.UPDATE);
            bagSlots.addRoleItemVo(addVo);
            roleBagDao.cacheUpdate(sourceRoleItem, userRoleId);
        }
        return bagSlots;
    }
    
    
    
    
	
	/**
     * 添加物品到背包,指定格子号（包括对带有状态数据的添加）
     * @opterationType：操作类型 :1.update:则只需要更新对应的角色所属和格位即可 
     *                        2.add:则需要考虑叠加问题 （仓库移动到背包不考虑叠加，交易物品不考虑叠加）
     * @return
     */
    private RoleItemOperation addItem(RoleItem roleItem, byte operationType, Integer slot,ContainerType sourceCType) {
        return this.addItem2(roleItem, operationType, slot,sourceCType, 0);
    }
	
	/**
	 * 添加物品到背包,指定格子号（包括对带有状态数据的添加）
	 * @opterationType：操作类型 :1.update:则只需要更新对应的角色所属和格位即可 
	 * 						  2.add:则需要考虑叠加问题 （仓库移动到背包不考虑叠加，交易物品不考虑叠加）
	 * @param chongwuId  宠物编号
	 * @return
	 */
	private RoleItemOperation addItem2(RoleItem roleItem, byte operationType, Integer slot,ContainerType sourceCType, Integer chongwuId) {
		 
			long userRoleId=roleItem.getUserRoleId();
			 
			AbstractContainer container = getContainer(sourceCType, userRoleId);
			if(container == null) {
				ChuanQiLog.error("添加背包数据错误，容器不存在！");
				throw new RuntimeException();
			}
			
			roleItem.setContainerType(sourceCType.getType());
			
			if(roleItem.getId()==null||roleItem.getId()<=0){
				BagUtil.newRoleItemSetId(roleItem);
			}
			 
			if(!container.hasEmptySlot(slot)) {
				ChuanQiLog.error("添加背包数据错误，格位已经存在了！");
				throw new RuntimeException();
			}
			
			roleItem.setSlot(slot);
			roleItem.setChongwuId(chongwuId);
			container.addItem(roleItem);
			byte realOpt = OperationType.ADD;
			
			if(operationType==OperationType.ADD) {
				roleBagDao.cacheInsert(roleItem, userRoleId);
			}else if(operationType==OperationType.UPDATE) {
				roleBagDao.cacheUpdate(roleItem, userRoleId);
				realOpt = OperationType.UPDATE;
			}
	 
			return createRoleItemVo(roleItem, ContainerType.BAGITEM, realOpt,0);  
	}
	
	/**
	 * 添加物品到背包,指定格子号（包括对带有状态数据的添加）
	 * @opterationType：操作类型 :1.update:则只需要更新对应的角色所属和格位即可 
	 * 						  2.add:则需要考虑叠加问题 （仓库移动到背包不考虑叠加，交易物品不考虑叠加）
	 * @param shenQiId  宠物编号
	 * @return
	 */
	private RoleItemOperation addItem3(RoleItem roleItem, byte operationType, Integer slot,ContainerType sourceCType, Integer shenQiId) {
		 
			long userRoleId=roleItem.getUserRoleId();
			 
			AbstractContainer container = getContainer(sourceCType, userRoleId);
			if(container == null) {
				ChuanQiLog.error("添加背包数据错误，容器不存在！");
				throw new RuntimeException();
			}
			
			roleItem.setContainerType(sourceCType.getType());
			
			if(roleItem.getId()==null||roleItem.getId()<=0){
				BagUtil.newRoleItemSetId(roleItem);
			}
			 
			if(!container.hasEmptySlot(slot)) {
				ChuanQiLog.error("添加背包数据错误，格位已经存在了！");
				throw new RuntimeException();
			}
			
			roleItem.setSlot(slot);
			roleItem.setShenqiId(shenQiId);
			container.addItem(roleItem);
			byte realOpt = OperationType.ADD;
			
			if(operationType==OperationType.ADD) {
				roleBagDao.cacheInsert(roleItem, userRoleId);
			}else if(operationType==OperationType.UPDATE) {
				roleBagDao.cacheUpdate(roleItem, userRoleId);
				realOpt = OperationType.UPDATE;
			}
	 
			return createRoleItemVo(roleItem, ContainerType.BAGITEM, realOpt,0);  
	}
	
	/**
	 * 添加物品到背包,指定格子号（专给交易提供）
	 * @return
	 */
	private RoleItemOperation addItem2BagByTrade(RoleItem roleItem) {
		long userRoleId = roleItem.getUserRoleId();
		AbstractContainer container = getContainer(ContainerType.BAGITEM, userRoleId);
		if(container == null) {
			ChuanQiLog.error("添加背包数据错误，容器不存在！");
			throw new RuntimeException();
		}
		
		roleItem.setContainerType(ContainerType.BAGITEM.getType());
		
		if(roleItem.getId()==null||roleItem.getId()<=0){
			BagUtil.newRoleItemSetId(roleItem);
		}
		
		roleItem.setSlot(null); 
		container.addItem(roleItem);
		
		return createRoleItemVo(roleItem, ContainerType.BAGITEM, OperationType.ADD,0);  
	}

	/**
	 * 通过格子位获得对应的物品对象
	 * @param slot
	 * @param userRoleId
	 * @param containerType
	 * @return
	 */ 
	protected RoleItem getItemBySlot(int slot,long userRoleId,ContainerType cType) {
		
		List<RoleItem>  roleItems=getContainerItems(userRoleId, cType.getType());
		
		for (RoleItem roleItem : roleItems) {
			 if(roleItem.getSlot().equals(slot)){
				 return roleItem;
			 }
		}
		return null;
	}
	
    protected RoleItem getItemBySlot(int slot, long userRoleId, ContainerType cType, Integer chongwuId) {
    	ChongwuItems chongwuItems=getContainer(ChongwuItems.class, ContainerType.CHONGWUITEM, userRoleId);
    	return chongwuItems.getItemByCwIdAndSlot(chongwuId, slot);
/*        List<RoleItem> roleItems = getContainerItems(userRoleId, cType.getType());
        for (RoleItem roleItem : roleItems) {
            if (null != roleItem.getChongwuId() && roleItem.getChongwuId().equals(chongwuId) && roleItem.getSlot().equals(slot)) {
                return roleItem;
            }
        }
        return null;*/
    }
    
    public RoleItem getItemBySlot1(int slot, long userRoleId, ContainerType cType, Integer shenQiId) {
    	ShenQiItems whenQiItems=getContainer(ShenQiItems.class, ContainerType.SHENQIITEM, userRoleId);
    	return whenQiItems.getItemBySqIdAndSlot(shenQiId, slot);
//        List<RoleItem> roleItems = getContainerItems(userRoleId, cType.getType());
//
//        for (RoleItem roleItem : roleItems) {
//            if (null != roleItem.getShenqiId() && roleItem.getShenqiId().equals(shenQiId) && roleItem.getSlot().equals(slot)) {
//                return roleItem;
//            }
//        }
//        return null;
    }
	
	/**
	 * 判断对应的容器是否有这些格位
	 * @param sourceGuid
	 * @param targetSlot
	 * @param userRoleId
	 * @param targetCType
	 * @return
	 */
	private Object[] checkSource2TargetContainer(long sourceGuid, int targetSlot,long userRoleId,ContainerType targetCType, Integer chongwuId) {
		//背包或仓库
		if(targetSlot >= getBeginSlot(userRoleId,targetCType)  &&  targetSlot <= getEndSlot(userRoleId,targetCType)){
			return null;
		}
		//装备格位（人物、御剑、翅膀、糖宝、天工、天裳）
		if(EquipTypeSlot.isEquip(targetSlot)){
			RoleItem roleItem = getItemByGuid(userRoleId, sourceGuid, ContainerType.BAGITEM); 
			return checkBag2BodyOrOhter(userRoleId, roleItem, targetSlot,targetCType, chongwuId); 
		}
		
		return  AppErrorCode.NO_TARGET_SLOT;
	}
	
	/**
	 * 判断对应的容器是否有这些格位
	 * @param sourceGuid
	 * @param targetSlot
	 * @param userRoleId
	 * @param targetCType
	 * @return
	 */
	private Object[] checkSource2TargetContainer1(long sourceGuid, int targetSlot,long userRoleId,ContainerType targetCType, Integer shenQiId) {
		//背包或仓库
		if(targetSlot >= getBeginSlot(userRoleId,targetCType)  &&  targetSlot <= getEndSlot(userRoleId,targetCType)){
			return null;
		}
		//装备格位（人物、御剑、翅膀、糖宝、天工、天裳）
		if(EquipTypeSlot.isEquip(targetSlot)){
			RoleItem roleItem = getItemByGuid(userRoleId, sourceGuid, ContainerType.BAGITEM); 
			return checkBag2BodyOrOhter(userRoleId, roleItem, targetSlot,targetCType, shenQiId); 
		}
		
		return  AppErrorCode.NO_TARGET_SLOT;
	}
	
	public Object[] checkMoveSlot(long sourceGuid,int targetSlot,long userRoleId,ContainerType cType){
		/**
		 * 判定各位是否存在
		 */  
		if(targetSlot<getBeginSlot(userRoleId,cType)||targetSlot>getEndSlot(userRoleId,cType)){
			return AppErrorCode.NO_TARGET_SLOT;
		}
		return null;
	 }
	
	private Object[] checkBag2BodyOrOhter(long userRoleId, RoleItem roleItem,int slot,ContainerType type, Integer chongwuId){
		if(roleItem==null){
			return AppErrorCode.NOT_FOUND_GOOODS;
		}
		
		GoodsConfig goodsConfig=goodsConfigExportService.loadById(roleItem.getGoodsId());
		if(goodsConfig==null){
			return AppErrorCode.NO_FIND_CONFIG;
		}
		RoleWrapper  roleW=roleExportService.getLoginRole(userRoleId);
		//等级
		int levelReq=goodsConfig.getLevelReq();
		if(roleW.getLevel()<levelReq){
			return AppErrorCode.REQ_LEVEL_BZ;
		}
		//转生等级
		int zsLevel = goodsConfig.getNeedzhuansheng();
		if(zsLevel > 0){
			Integer zhuansheng = zhuanshengExportService.getZhuanshengLevel(userRoleId);
			if(zhuansheng == null || zhuansheng.intValue() < zsLevel){
				return AppErrorCode.REQ_ZS_LEVEL_BZ;
			}
		}
		
		Integer level = null;
		if(ContainerType.ZUOQIITEM.equals(type)){
			level = zuoQiExportService.getZuoQiInfoLevelOther(userRoleId);
		}else if(ContainerType.CHIBANGITEM.equals(type)){
			level = chiBangExportService.getChibangLevelOther(userRoleId);
		}else if(ContainerType.TIANGONGITEM.equals(type)){
			level = xianJianExportService.getXianJianLevelOther(userRoleId);
		}else if(ContainerType.TIANSHANGITEM.equals(type)){
			level = zhanJiaExportService.getXianJianLevelOther(userRoleId);
		}else if(ContainerType.TANGBAOITEM.equals(type)){
			if(!tangbaoExportService.hasTangbao(userRoleId)){
				return AppErrorCode.TB_NO_TANGBAO;//没有糖宝
			}
		}else if(ContainerType.QILINGITEM.equals(type)){
			level = qiLingExportService.getQiLingLevel(userRoleId);
		}else if(ContainerType.TIANYUITEM.equals(type)){
			level = tianYuExportService.getTianYuLevelOther(userRoleId);
		}else if(ContainerType.CHONGWUITEM.equals(type)){
		    RoleChongwu roleChongwu = chongwuExportService.getRoleChongwuById(userRoleId, chongwuId);
		    level = null == roleChongwu ? -1 : roleChongwu.getJie();
        }else if (ContainerType.WUQI.equals(type)) {
			level = wuQiExportService.getWuQiInfoLevelOther(userRoleId);
		}
		
		if(level != null && level + 1 < goodsConfig.getData1()){
			return AppErrorCode.EQUIP_NO_ENOUGH_LEVEL;//所需等阶不足
		}
		
		//判断职业是否正确,可能有多个职业
		int orleJob= roleW.getConfigId();
		int job=goodsConfig.getJob();
		boolean jobFlag=BitOperationUtil.calState(job, orleJob);
		if(jobFlag){
			return AppErrorCode.JOB_EQUIP_ERR;
		}
		
		//判断部位是否正确
		if(goodsConfig.getEqpart()!=EquipTypeSlot.getEquipPartBySlotNum(slot)){
			return AppErrorCode.POSITION_EQUIP_ERR;
		}
		//穿戴后绑定
		if(!ObjectUtil.strIsEmpty(goodsConfig.getEquipId())){
			roleItem.setGoodsId(goodsConfig.getEquipId());
			BusMsgSender.send2One(roleItem.getUserRoleId(), ClientCmdType.EQUIP_GOODS_ID_CHANGE, new Object[]{roleItem.getId(),roleItem.getGoodsId()});
		}
		
		return null;
	}
	

	/**
	 * 移除容器类物品
	 * @param userRoleId
	 * @param guid
	 * @param count
	 * @param operationType 实际需要对db做的操作
	 * @param containerType 容器类型
	 * @return
	 */
	private RoleItemOperation removeItem(long userRoleId,long guid,int count,byte operationType,ContainerType containerType){
		
		AbstractContainer container =getContainer(containerType, userRoleId);
		
		RoleItem roleItem=getItemByGuid(userRoleId, guid,containerType);
		
		if(roleItem == null){
			ChuanQiLog.error("{}移除物品guid:{},数量:{}时出错，物品不存在。",userRoleId,guid,count);
		}
		
		
		byte realOperationType=operationType;
		int oldCount = roleItem.getCount();
		
		if(oldCount == count){ 
			container.removeItem(guid);
			
			if(OperationType.DELETE==operationType){
				roleBagDao.cacheDelete(guid, userRoleId); 
			}
			realOperationType = OperationType.DELETE;
			
		}else if(oldCount > count){
			roleItem.setCount(roleItem.getCount()-count);
			if(OperationType.UPDATE==operationType||OperationType.DELETE==operationType){
				roleBagDao.cacheUpdate(roleItem, userRoleId);
			}
			realOperationType = OperationType.UPDATE;
		}
		
		return new RoleItemOperation(oldCount, roleItem, realOperationType, (byte)ContainerType.BAGITEM.getType());
	}
	
	/**
	 * 移除容器类物品-[交易专用]
	 * @param userRoleId
	 * @param guid
	 * @param count
	 * @param operationType 实际需要对db做的操作
	 * @param containerType 容器类型
	 * @return
	 */
	private RoleItemOperation removeItemByTrade(long userRoleId,long guid){
		RoleItem roleItem = getItemByGuid(userRoleId, guid,ContainerType.BAGITEM);
		
		AbstractContainer container = getContainer(ContainerType.BAGITEM, userRoleId);
		container.removeItem(guid);
		
		return new RoleItemOperation(roleItem.getCount(), roleItem, OperationType.DELETE, (byte)ContainerType.BAGITEM.getType());
	}
	
	protected RoleItem getItemByGuid(long userRoleId, long guid,ContainerType containerType) {
		AbstractContainer abstractContainer =  getContainer(containerType, userRoleId);
		if(abstractContainer == null){
			return null;
		}
		return abstractContainer.getItemByGuid(guid);
	}
 
	/**
	 * 记录物品操作
	 * @param type  {@Link LogPrintHandle   消耗:CONTAINER_REMOVE_GOODS,获得:CONTAINER_GET_GOODS } 
	 * @param source
	 * @param datas
	 * @param bz 来源描述
	 */
	private void recordRoleItemLog(int type,int source,List<RoleItemOperation> datas,long userRoleId,Object bz){
		if(datas!=null && datas.size()>0){
			for (RoleItemOperation roleItemAction : datas) {
				GoodsConfig  config = goodsConfigExportService.loadById(roleItemAction.getGoodsId());
				if(config == null){
					ChuanQiLog.error("[记录物品日志]物品配置Id不存在,goodsId:"+roleItemAction.getGoodsId());
					continue;
				}
				//配置表没有表示记录的数据不记录
				if(!config.getRecord()) 
					continue;
				
				//操作类型
				byte operationType = roleItemAction.getOperationType();
				
				//删除操作  数量为0的不记录
				int oldCount = roleItemAction.getOldCount();
				if(operationType == OperationType.DELETE && oldCount == 0){
					continue;
				}
				
				//更新操作  数量没变不记录
				int count = roleItemAction.getCount();				
				if(operationType == OperationType.UPDATE){
					if(oldCount == count){
						continue;
					} 
					count = Math.abs(count - oldCount);
				}
					
				GamePublishEvent.publishEvent(new RoleItemLogEvent(type, userRoleId,roleItemAction.getGuid(),getRoleName(userRoleId,true),roleItemAction.getGoodsId(),count,source,bz));
			}
		}
	}
	
	/**
	 * 获得玩家名字
	 * @param userRoleId
	 * @param isOnline	是否在线
	 * @return
	 */
	private String getRoleName(long userRoleId,boolean isOnline){ 
		RoleWrapper roleWrapper = null;
		if(isOnline){
			roleWrapper = roleExportService.getLoginRole(userRoleId);
		}else{
			roleWrapper = roleExportService.getUserRoleFromDb(userRoleId);
		}
		
		if(roleWrapper == null){
			return userRoleId+"";
		}
		return roleWrapper.getName();
	}
	
	/**
	 * 添加物品到背包
	 * @opterationType：操作类型 :1.update:则只需要更新对应的角色所属和格位即可 
	 * 						  2.add:则需要考虑叠加问题 （仓库移动到背包不考虑叠加，交易物品不考虑叠加）
	 * @return
	 */
	private List<RoleItemOperation> addItem2Bag(RoleItem roleItem,byte operationType){
		boolean isNewGuid=false;
		if(roleItem.getId()==null||roleItem.getId()<=0){
			isNewGuid=true;
		}
		List<RoleItemOperation> results=new ArrayList<RoleItemOperation>();
		long userRoleId=roleItem.getUserRoleId();
		int count=roleItem.getCount();
		String goodId=roleItem.getGoodsId();
		int repeatCount=BagUtil.getItemRepeatCount(goodId);
		
		if(operationType==OperationType.ADD){
			long timeOut=roleItem.getExpireTime();
			//先从叠加未满的开始算起
			List<RoleItem> items = getNoFullRepeat(goodId, userRoleId,timeOut);
			for (RoleItem ri : items) {
				if(count==0)break; 
				int changedCount=0;
 
				int oldCount =ri.getCount();
				if(ri.getCount()+count<repeatCount){
					changedCount=ri.getCount()+count;
					count=0; 
				}else{
					changedCount=repeatCount;
					count-=repeatCount-ri.getCount(); 
				}
					ri.setCount(changedCount); 
					roleBagDao.cacheUpdate(ri, userRoleId);  
					
					RoleItemOperation vo=createRoleItemVo(ri,ContainerType.BAGITEM, OperationType.UPDATE,oldCount);
					results.add(vo); 
			}
			 
			//开启新的格子
			if(count>0){
				int addNewFullCount=count/repeatCount;
				int addNewNoFullCount=count%repeatCount;
				//添加叠加满了的新物品
				for(int i=0;i<addNewFullCount;i++){
					RoleItem  rItem=roleItem.copy();
					rItem.setCount(repeatCount);
					if(isNewGuid){
						BagUtil.newRoleItemSetId(roleItem);
					}
					
					RoleItemOperation vo = addItem(rItem, OperationType.ADD, null, ContainerType.BAGITEM);
					results.add(vo); 
				}
				//添加未叠加满的新物品
				if(addNewNoFullCount>0){
					RoleItem  rItem=roleItem.copy();
					if(isNewGuid){
						rItem.setId(BagUtil.getIdentity());
					}
					rItem.setCount(addNewNoFullCount);
					
					RoleItemOperation vo = addItem(rItem, OperationType.ADD, null, ContainerType.BAGITEM);
					results.add(vo); 
 
				}
			}
			
		}else{
			RoleItemOperation vo = addItem(roleItem, operationType, roleItem.getSlot(), ContainerType.BAGITEM);
			results.add(vo);
		}
		//入背包检测修炼任务
		try {
			String goodsId = roleItem.getGoodsId();
			if("2400".equals(goodsId)){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.GET_TIANJIANG_BAOXIANG, null});
			}else{
				GoodsConfig config = goodsConfigExportService.loadById(goodsId);
				if(config != null){
					if(config.getCategory() != GoodsCategory.EQUIP_TYPE){//是装备
						if(config.getRareLevel() == 3){//紫装
							BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.EQUIP_ZISE_EQUIP, null});
						}
					}
				}
			}
		} catch (Exception e) {
			ChuanQiLog.error(""+e);
		}
	
		return results;
	}
	
	
	/** 
	 * 获得叠加未满的物品
	 * @param bind
	 * @param itemId
	 * @return
	 */
	private List<RoleItem> getNoFullRepeat(String goodsId,long userRoleId,long timeOut){
		List<RoleItem> results=new ArrayList<RoleItem>();
		List<RoleItem> owerItems=getBagItemByGoodId(goodsId,userRoleId);
		int repeatCount=BagUtil.getItemRepeatCount(goodsId);
		for (RoleItem roleItem : owerItems) {
			if(roleItem.getGoodsId().equals(goodsId)){ 
				if(roleItem.getCount()<repeatCount&&roleItem.getExpireTime().equals(timeOut)){
					results.add(roleItem);
				}
			}
		}
		return results;
	}
	
	/**  
	 * 查询容器里面的数据
	 * @param userRoleId 
	 * @param type  对应ContainerType
	 * @return
	 */
	protected List<RoleItem> getContainerItems(long userRoleId,int type){
		Bag  bag=getBag(userRoleId);
		if(bag == null){
			return null;
		}
		AbstractContainer stores=bag.getStore(type);
		if(stores == null){
			return null;
		}
		return stores.getItems();
	}

	
	/**
	 * 通过物品id和是否bind获得物品现有数量
	 * @param gooodId
	 * @param isBind   ture:绑定(BagContants.BIND)		false:非绑定(BagContants.NOBIND)
	 * @param userRoleId
	 * @return
	 */
	public List<RoleItem> getBagItemByGoodId(String goodId,long userRoleId){
		List<RoleItem> results=new ArrayList<RoleItem>();
		List<RoleItem>  roleItems=getContainerItems(userRoleId, ContainerType.BAGITEM.getType());
		if(roleItems == null || roleItems.size() < 1){
			return results;
		}
		for (RoleItem roleItem : roleItems) {
			 if(roleItem.getGoodsId().equals(goodId)){
				 results.add(roleItem);
			 }
		}
		
		//需要一个消耗规则排序
		return results;
	}
	
	/**
	 *  通过物品id获得物品现有数量
	 * @param gooodId
	 * @param userRoleId
	 * @return
	 */
	public int getItemCountByGoodsId(String goodsId,long userRoleId,int type){
		int result=0; 
		List<RoleItem>  roleItems=getContainerItems(userRoleId,type);
		for (RoleItem roleItem : roleItems) {
			 if(roleItem.getGoodsId().equals(goodsId) && !roleItem.isExpireTimed()){
				 result+=roleItem.getCount();
			 }
		}
		return result;
	}
	
	
	
	/**
	 *  通过物品id获得物品现有数量
	 * @param gooodId
	 * @param userRoleId
	 * @return
	 */
	public List<RoleItem> getBagItemByGoodsId(String gooodId,long userRoleId){
		List<RoleItem> results=new ArrayList<RoleItem>();
		List<RoleItem>  roleItems=getContainerItems(userRoleId, ContainerType.BAGITEM.getType());
		if(roleItems == null || roleItems.size() < 1){
			return results;
		}
		for (RoleItem roleItem : roleItems) {
			 if(roleItem.getGoodsId().equals(gooodId) && !roleItem.isExpireTimed()){
				 results.add(roleItem);
			 }
		}
		return results;
	}
	
	/**
	 *  通过物品id获得物品现有数量
	 * @param gooodId
	 * @param userRoleId
	 * @return
	 */
	public List<RoleItem> getBagItemByGoodsType(String goodsType,long userRoleId){
		List<RoleItem> results=new ArrayList<RoleItem>();
		List<RoleItem>  roleItems=getContainerItems(userRoleId, ContainerType.BAGITEM.getType());
		if(roleItems == null || roleItems.size() < 1){
			return results;
		}
		List<String> goodsIds=goodsConfigExportService.loadIdsById1(goodsType);
		
		for (RoleItem roleItem : roleItems) {
			 if(goodsIds.contains(roleItem.getGoodsId()) && !roleItem.isExpireTimed()){
				 results.add(roleItem);
			 }
		}
		return results;
	}
	
	public Object[] checkRemoveBagItemByGoodsId(String goodsId,int count,long userRoleId){ 
		int owerCount=getItemCountByGoodsId(goodsId, userRoleId,ContainerType.BAGITEM.getType());
		if(count>owerCount){
			 return AppErrorCode.ITEM_COUNT_ENOUGH;
		}
		Object[] errorCode=goodsUseLimitService.checkXzcsUse(userRoleId, goodsId, owerCount);
		if(errorCode!=null){
			return errorCode;
		}
		return null;
	}
	
	
	
	/**
	 * 消耗指定了guid的物品,不带数量，直接对整个格子的物品进行操作
	 * @param guid
	 * @param userRoleId
	 * @param resource
	 * @param isRecord
	 * @param isNotify 是否直接通知客服端物品变化的结果
	 * @return
	 */ 
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {1})
	protected BagSlots removeBagItemByGuid(long guid,long userRoleId,int source,boolean isRecord,boolean isNotify){
		BagSlots bagSlots=new BagSlots();
		Object[] flagCode=checkRemoveBagItemByGuid(guid,userRoleId);
		if(flagCode!=null){
			bagSlots.setErrorCode(flagCode);
			return bagSlots;
		}
		RoleItem roleItem = getItemByGuid(userRoleId, guid, ContainerType.BAGITEM);
		RoleItemOperation vo = removeItem(userRoleId, guid,roleItem.getCount(), OperationType.DELETE, ContainerType.BAGITEM);
		bagSlots.addRoleItemVo(vo);
		
		if(isNotify){
			sendNotifyGoodsChanage(userRoleId, bagSlots.getRoleItemVos());
		}
		
		//记录物品操作
		if(isRecord){ 
			recordRoleItemLog(LogPrintHandle.CONTAINER_REMOVE_GOODS, source, bagSlots.getRoleItemVos(), userRoleId,null);
		}
		
		return bagSlots;
	}
	/**
	 * 销毁指定了guid的物品,不带数量，直接对整个格子的物品进行操作
	 * @param guid
	 * @param userRoleId
	 * @param resource
	 * @param isRecord
	 * @param isNotify 是否直接通知客服端物品变化的结果
	 * @return
	 */ 
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {1})
	protected BagSlots destroyBagItemByGuid(long guid,long userRoleId,int source,boolean isRecord,boolean isNotify){
		BagSlots bagSlots=new BagSlots();
		Object[] flagCode=checkDestroyBagItemByGuid(guid,userRoleId);
		if(flagCode!=null){
			bagSlots.setErrorCode(flagCode);
			return bagSlots;
		}
		RoleItem roleItem = getItemByGuid(userRoleId, guid, ContainerType.BAGITEM);
		RoleItemOperation vo = removeItem(userRoleId, guid,roleItem.getCount(), OperationType.DELETE, ContainerType.BAGITEM);
		bagSlots.addRoleItemVo(vo);
		
		if(isNotify){
			sendNotifyGoodsChanage(userRoleId, bagSlots.getRoleItemVos());
		}
		
		//记录物品操作
		if(isRecord){ 
			recordRoleItemLog(LogPrintHandle.CONTAINER_REMOVE_GOODS, source, bagSlots.getRoleItemVos(), userRoleId,null);
		}
		
		return bagSlots;
	}
	
	
	/**
	 * 消耗指定了guid的物品,如掉物品到场景
	 * @param guid
	 * @param count
	 * @param userRoleId
	 * @param resource
	 * @param isRecord
	 * @param isNotify 是否直接通知客服端物品变化的结果
	 * @return
	 */ 
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {2})
	protected BagSlots removeBagItemByGuid(long guid,int count,long userRoleId,int source,boolean isRecord,boolean isNotify){
		BagSlots bagSlots=new BagSlots();
		Object[] flagCode=checkRemoveBagItemByGuid(guid,count,userRoleId);
		if(flagCode!=null){
			bagSlots.setErrorCode(flagCode);
			return bagSlots;
		}
		
		RoleItemOperation vo=removeItem(userRoleId, guid,count,OperationType.DELETE,ContainerType.BAGITEM);
		bagSlots.addRoleItemVo(vo);
		
		if(isNotify){
			sendNotifyGoodsChanage(userRoleId, bagSlots.getRoleItemVos());
		}
		
		//记录物品日志
		if(isRecord){
			recordRoleItemLog(LogPrintHandle.CONTAINER_REMOVE_GOODS, source, bagSlots.getRoleItemVos(), userRoleId,null);
		}
		return bagSlots;
	}
	
	public Object[] checkRemoveBagItemByGuid(long guid,long userRoleId){
		RoleItem roleItem=getItemByGuid(userRoleId, guid, ContainerType.BAGITEM);
		
		if(roleItem==null){
			return AppErrorCode.NOT_FOUND_GOOODS;
		}
		
		if(roleItem.isExpireTimed()){
			return AppErrorCode.IS_EXPIRE_GOODS;
		}
		
		Object[] errorCode=goodsUseLimitService.checkXzcsUse(userRoleId, roleItem.getGoodsId(), roleItem.getCount());
		if(errorCode!=null){
			return errorCode;
		}
		
		return null;
	}
	public Object[] checkDestroyBagItemByGuid(long guid,long userRoleId){
		RoleItem roleItem=getItemByGuid(userRoleId, guid, ContainerType.BAGITEM);
		
		if(roleItem==null){
			return AppErrorCode.NOT_FOUND_GOOODS;
		}
		
		return null;
	}
	
	public Object[] checkRemoveBagItemByGuid(long guid,int count,long userRoleId){
		RoleItem roleItem=getItemByGuid(userRoleId, guid, ContainerType.BAGITEM);
		
		if(roleItem==null){
			return AppErrorCode.NOT_FOUND_GOOODS;
		}
		
		if(roleItem.getCount()<count||count<1){
			return AppErrorCode.ITEM_COUNT_ENOUGH;
		}
		
		if(roleItem.isExpireTimed()){
			return AppErrorCode.IS_EXPIRE_GOODS;
		}
		
		Object[] errorCode=goodsUseLimitService.checkXzcsUse(userRoleId, roleItem.getGoodsId(), count);
		if(errorCode!=null){
			return errorCode;
		}
		
		return null;
	}
	
	/**
	 * 获取容器的空格子
	 * @param userRoleId
	 * @param type
	 * @return
	 */
	public int getContainerEmptyCount(ContainerType type,long userRoleId){
		Bag bag=getBag(userRoleId);
		BagItems bagItems=bag.getStore(BagItems.class, type.getType());
		return bagItems.getEmptyCount();
		
	}
	
	/**
	 * 下线时清理背包缓存数据
	 */
	public void offOnline(long roleId){
		BagManage.clear(roleId);
	}
	
	/**
	 * 整理容器	(暂时只对背包和仓库提供整理功能)
	 * @param containerType
	 * @return 
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {1})
	protected BagSlots resetContainerSlot(ContainerType containerType,long userRoleId){
		BagSlots bagSlots=new BagSlots();
		//暂时只对背包和仓库提供整理功能
		if(containerType!=ContainerType.BAGITEM&&containerType!=ContainerType.STORAGEITEM){
			 bagSlots.setErrorCode(AppErrorCode.NO_RESET_EMPTY);
			 return bagSlots;
		}
		Bag bag=getBag(userRoleId);
		AbstractContainer container=bag.getStore(AbstractContainer.class, containerType.getType());
		
		List<RoleItem> roleItems=getContainerItems(userRoleId, containerType.getType());
		if(roleItems == null ||roleItems.size() == 0){
			 bagSlots.setErrorCode(AppErrorCode.RESET_ERROR);
			 return bagSlots;
		}
		
		Map<Long,Integer> oldData = getOldData(roleItems);
		
		//1：整理出可以叠加的物品 
		merge(roleItems);
		
		//2:清除需要清除的物品
		removeEmptySlot(roleItems, bagSlots, containerType,container);
		
		//3:需要具体的排序需求 
		Collections.sort(roleItems,new SortRoleItemComparator());
		 //通过物品配置表的物品id排序
		Collections.sort(roleItems,new SortRoleItemByIDComparator());
 		
		//4：重置格子位 
		resetSlot(roleItems, bagSlots, containerType,userRoleId,oldData);
		
		//5:记录空格子位
		container.resetEmptySolt();
		
		return bagSlots;
	}
	
	private Map<Long,Integer> getOldData(List<RoleItem> roleItems){
		
		Map<Long,Integer> result =new HashMap<>();
		for (RoleItem roleItem : roleItems) {
			result.put(roleItem.getId(), roleItem.getCount());
		}
		
		return result;
	}
	
	private void resetSlot(List<RoleItem> roleItems,BagSlots bagSlots,ContainerType containerType,long userRoleId,Map<Long,Integer> oldData) {
		int beginIndex=0;
		if(containerType==ContainerType.BAGITEM){
			beginIndex=getBagBeginSlot(userRoleId);
		}
		if(containerType==ContainerType.STORAGEITEM){
			beginIndex=getStorageBeginSlot(userRoleId);
		}
		for (int i=0;i<roleItems.size();i++) {
			RoleItem roleItem=roleItems.get(i);
			int newSlot=i+beginIndex;
			if(roleItem.getSlot()!=newSlot || !roleItem.getCount().equals(oldData.get(roleItem.getId()))){ //与原来数量不相符的也需要发送update数据过去
				roleItem.setSlot(newSlot);
				roleBagDao.cacheUpdate(roleItem, roleItem.getUserRoleId());
				bagSlots.addRoleItemVo(createRoleItemVo(roleItem, containerType, OperationType.UPDATE,roleItem.getCount()));
			}
		}
	}
	/**
	 * 2:清除需要清除的物品
	 * @param bagSlots 
	 */
	private void removeEmptySlot(List<RoleItem> roleItems, BagSlots bagSlots,ContainerType containerType,AbstractContainer container){
		Iterator<RoleItem> iterator=roleItems.iterator();
		while(iterator.hasNext()){
			 RoleItem roleItem=iterator.next();
			 if(roleItem.getCount()==0){
				 iterator.remove();
				 container.removeItem(roleItem.getId());
				 roleBagDao.cacheDelete(roleItem.getPrimaryKeyValue(), roleItem.getUserRoleId());
				 bagSlots.addRoleItemVo(createRoleItemVo(roleItem, containerType, OperationType.DELETE,roleItem.getCount()));
			 }
		}
	}
 
	
	
	/**
	 * 同ID 绑定相同的合并  并且数量多的在前 少的在后
	 */
	private void merge(List<RoleItem> tmpGoods) {
		
		//排序
		for (int i = 0; i < tmpGoods.size()-1; i++) {
			RoleItem outGoods = tmpGoods.get(i);
			int outCount=outGoods.getCount();
			if(outCount==0)continue;
			int MAX_OVERLAY = BagUtil.getItemRepeatCount(outGoods.getGoodsId());
			
			for (int j = i+1; j < tmpGoods.size(); j++) {
				RoleItem innerGoods = tmpGoods.get(j);
				int innerCount=innerGoods.getCount();
				if(innerCount==0)continue;
				if(outGoods.getCount() == MAX_OVERLAY){
					break;
				}
				/**
				 * 綁定于非綁定若全在配置中決定了，那就不需要比較綁定非綁定了
				 */
				boolean outGoodBind=BagUtil.getIsBind(outGoods.getGoodsId());
				boolean innerGoodsBind=BagUtil.getIsBind(innerGoods.getGoodsId());
				
				if(outGoods.getGoodsId().equals(innerGoods.getGoodsId()) &&outGoodBind == innerGoodsBind && outGoods.getExpireTime().equals(innerGoods.getExpireTime())){
					int tmpCount =  (outGoods.getCount() + innerCount) >= MAX_OVERLAY ? MAX_OVERLAY - outGoods.getCount() : innerGoods.getCount();
					
					outGoods.setCount(outGoods.getCount() + tmpCount);
					
					innerGoods.setCount(innerCount - tmpCount);
				}
			}
		}
	}
	
	
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {1})
	protected BagSlots putInBagVo(Map<String, GoodsConfigureVo> goods, long userRoleId,int source, boolean isRecord) {
		BagSlots bagSlots=new BagSlots();
		Object[] flagCode=checkPutInBagVo(goods,userRoleId);
		if(flagCode!=null){
		    bagSlots.setErrorCode(flagCode);
			return bagSlots;
		}
		List<RoleItemOperation> vos = new ArrayList<RoleItemOperation>();
		if(goods!=null){
			for (Entry<String, GoodsConfigureVo> entry : goods.entrySet()) {
				String goodsId = entry.getKey();
				GoodsConfigureVo vo = entry.getValue();
				int count = vo.getGoodsCount();
				
				RoleItemInput roleItemInput=BagUtil.createItem(goodsId, count, vo.getQhLevel());
				RoleItem roleItem = BagUtil.converInsert2RoleItem(roleItemInput, userRoleId);
				List<RoleItemOperation> rvos= addItem2Bag(roleItem, OperationType.ADD);
				vos.addAll(rvos); 
			}
		}
		
		bagSlots.addRoleItemVos(vos); 
		//通知客服端
		sendNotifyGoods(bagSlots.getRoleItemVos(), userRoleId);
		
		//记录日志
		if(isRecord){
			recordRoleItemLog(LogPrintHandle.CONTAINER_GET_GOODS, source, bagSlots.getRoleItemVos(), userRoleId, null);
		}
		
		return bagSlots;
	}
	
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {1})
	protected BagSlots putInBag(Map<String, Integer> goods, long userRoleId,int source, boolean isRecord) {
		BagSlots bagSlots=new BagSlots();
		Object[] flagCode=checkPutInBag(goods,userRoleId);
		if(flagCode!=null){
		    bagSlots.setErrorCode(flagCode);
			return bagSlots;
		}
		List<RoleItemOperation> vos=new ArrayList<RoleItemOperation>();
		if(goods!=null){
			for (Entry<String, Integer> entry : goods.entrySet()) {
				String goodsId=entry.getKey();
				int count=entry.getValue();
				RoleItemInput roleItemInput=BagUtil.createItem(goodsId, count, 0);
				RoleItem roleItem = BagUtil.converInsert2RoleItem(roleItemInput, userRoleId);
				List<RoleItemOperation> rvos= addItem2Bag(roleItem, OperationType.ADD);
				vos.addAll(rvos); 
			}
		}
		
		bagSlots.addRoleItemVos(vos); 
		//通知客服端
		sendNotifyGoods(bagSlots.getRoleItemVos(), userRoleId);
		
		//记录日志
		if(isRecord){
			recordRoleItemLog(LogPrintHandle.CONTAINER_GET_GOODS, source, bagSlots.getRoleItemVos(), userRoleId, null);
		}
		
		return bagSlots;
	}

	
	public Object[] checkPutInBagVo(Map<String, GoodsConfigureVo> goods, long userRoleId) {
		int needSlotCount = 0;
		for (Entry<String, GoodsConfigureVo>  entry : goods.entrySet()) {
			String goodsId = entry.getKey();
			GoodsConfigureVo vo = entry.getValue();
			
			Integer count = vo.getGoodsCount();
			//判定背包空间是否足够 
			if(goodsId==null||goodsId.equals("")){
				return AppErrorCode.NOT_FOUND_GOOODS;
			}
			
			GoodsConfig goodsConfig=goodsConfigExportService.loadById(goodsId);
			if(goodsConfig==null){
				return AppErrorCode.NO_FIND_CONFIG;
			}
			
			if(goodsConfig.getCategory() <=0 ){
				ChuanQiLog.error("数值类型不可入背包！！ goodsId:"+goodsId+"\t category:"+goodsConfig.getCategory());
				return AppErrorCode.GOODS_TYPE_ERROR;
			}
			 
			if(count==0){
				return AppErrorCode.GOODSID_COUNT_ISNULL;
			}
			int repeatCount = BagUtil.getItemRepeatCount(goodsId);
			
			needSlotCount += count / repeatCount + (count % repeatCount > 0 ? 1 : 0);
		}
		  
		int bagEmptyCount = getContainerEmptyCount(ContainerType.BAGITEM, userRoleId);
		if(needSlotCount > bagEmptyCount){
			return AppErrorCode.BAG_NOEMPTY;
		}
		
		return null;
	}
	
	public Object[] checkPutInBag(Map<String, Integer> goods, long userRoleId) {
		 
		int needSlotCount=0;
		for (Entry<String, Integer>  entry : goods.entrySet()) {
			String goodsId=entry.getKey();
			Integer count=entry.getValue();
			//判定背包空间是否足够 
			if(goodsId==null||goodsId.equals("")){
				return AppErrorCode.NOT_FOUND_GOOODS;
			}
			
			GoodsConfig goodsConfig=goodsConfigExportService.loadById(goodsId);
			if(goodsConfig==null){
				return AppErrorCode.NO_FIND_CONFIG;
			}
			
			if(goodsConfig.getCategory() <=0 ){
				ChuanQiLog.error("数值类型不可入背包！！ goodsId:"+goodsId+"\t category:"+goodsConfig.getCategory());
				return AppErrorCode.GOODS_TYPE_ERROR;
			}
			 
			if(count==0){
				return AppErrorCode.GOODSID_COUNT_ISNULL;
			}
			int repeatCount=BagUtil.getItemRepeatCount(goodsId);
			
			needSlotCount+=count/repeatCount+(count%repeatCount>0?1:0);
		}
		  
		int bagEmptyCount= getContainerEmptyCount(ContainerType.BAGITEM, userRoleId);
		if(needSlotCount>bagEmptyCount){
			return AppErrorCode.BAG_NOEMPTY;
		}
		
		return null;
	}
	
	/**
	 * 检查添加物品与数值
	 * @param goods
	 * @param userRoleId
	 * @return null表示成功
	 */
	public Object[] checkPutGoodsAndNumberAttr(Map<String, Integer> goods, long userRoleId) {
		 
		int needSlotCount=0;
		for (Entry<String, Integer>  entry : goods.entrySet()) {
			String goodsId=entry.getKey();
			Integer count=entry.getValue();
			//判定背包空间是否足够 
			if(goodsId==null||goodsId.equals("")){
				return AppErrorCode.NOT_FOUND_GOOODS;
			}
			
			GoodsConfig goodsConfig=goodsConfigExportService.loadById(goodsId);
			if(goodsConfig==null){
				return AppErrorCode.NO_FIND_CONFIG;
			}
			
			if(goodsConfig.getCategory() <=0 ){
				 continue;
			}
			
			if(count==0){
				return AppErrorCode.GOODSID_COUNT_ISNULL;
			}
			int repeatCount=BagUtil.getItemRepeatCount(goodsId);
			
			needSlotCount+=count/repeatCount+(count%repeatCount>0?1:0);
		}
		  
		int bagEmptyCount= getContainerEmptyCount(ContainerType.BAGITEM, userRoleId);
		if(needSlotCount>bagEmptyCount){
			return AppErrorCode.BAG_NOEMPTY;
		}
		
		return null;
	}
	
	
	/**
	 * 对多类型物品进行消耗
	 * @param goods  goodsId=count
	 * @param userRoleId
	 * @param source
	 * @param isRecord
	 * @param isNotify
	 * @return
	 */ 
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {1}) 
	protected BagSlots removeBagItemByGoods(Map<String, Integer> goods, long userRoleId, int source, boolean isRecord,boolean isNotify) {
		BagSlots bagSlots=new BagSlots();
		Object[] errorCode=checkRemoveBagItemByGoodsId(goods,userRoleId);
		if(errorCode!=null){ 
			bagSlots.setErrorCode(errorCode);
			return bagSlots;
		}
		List<RoleItemOperation> itemvos=new ArrayList<RoleItemOperation>();
		for (Entry<String, Integer> entry : goods.entrySet()) {
			String goodsId=entry.getKey();
			int count=entry.getValue();
			List<RoleItemOperation> vos=removeBagItemByItemId(goodsId, count,userRoleId,false);
			itemvos.addAll(vos);
		}
		bagSlots.addRoleItemVos(itemvos);
		if(isNotify){
			sendNotifyGoodsChanage(userRoleId, bagSlots.getRoleItemVos());
		}
		if(isRecord){
			recordRoleItemLog(LogPrintHandle.CONTAINER_REMOVE_GOODS, source, bagSlots.getRoleItemVos(), userRoleId, null);
		}
		return bagSlots;
	}

	public Object[] checkRemoveBagItemByGoodsId(Map<String, Integer> goods,long userRoleId){
		Object[] flagCode=null;
		if(goods == null) return AppErrorCode.REMOVE_TARGET_NULL;
		for (Entry<String, Integer> entry : goods.entrySet()) {
			String goodsId=entry.getKey();
			int count=entry.getValue();
			flagCode=checkRemoveBagItemByGoodsId(goodsId, count, userRoleId);
			if(flagCode!=null) break;
		}
		return flagCode;
	}
	
	/**
	 * 检测是否够移除
	 * @param goods	（含数值类型）
	 * @param userRoleId
	 * @return
	 */
	public Object[] checkRemoveBagItemByGoodsIdAndNumber(Map<String, Integer> goods,long userRoleId){
		Object[] flagCode=null;
		if(goods == null) return AppErrorCode.REMOVE_TARGET_NULL;
		for (Entry<String, Integer> entry : goods.entrySet()) {
			String goodsId=entry.getKey();
			int count=entry.getValue();
			if(count < 0){
				return AppErrorCode.NUMBER_ERROR;
			}
			Integer numberType = GoodsCategory.getNumberType(goodsId);
			if(numberType != null){
				switch (numberType) {
				case GoodsCategory.GOLD:
				case GoodsCategory.BGOLD:
				case GoodsCategory.MONEY:
					flagCode = accountExportService.isEnought(numberType, count, userRoleId);
					break;
					
				case GoodsCategory.ZHENQI:
					if(!roleExportService.isEnoughZhenqi(userRoleId, count)){
						return AppErrorCode.ZHEN_ERROR;
					}
					break;
				case GoodsCategory.RONGYU:
					if(!roleBusinessInfoExportService.isEnoughRongyu(userRoleId, count)){
						return AppErrorCode.JINGJI_NO_ENOUGH_RONGYU;
					}
					break;
					// FIXME  需要添加新的类型可以在这里添加
				default:
					ChuanQiLog.error("消耗属性数据类型不支持,type:"+numberType);
					break;
				}
			}else{
				flagCode = checkRemoveBagItemByGoodsId(goodsId, count, userRoleId);
			}
			if(flagCode!=null) break;
		}
		return flagCode;
	}
	
	/**
	 * 移除物品
	 * @param goods	（含数值类型）
	 * @param userRoleId
	 * @return
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {1}) 
	public Object[] removeBagItemByGoodsIdAndNumber(Map<String, Integer> goods,long userRoleId, int source, boolean isRecord,boolean isNotify,int logType, boolean isNoXF,int beizhu){
		BagSlots bagSlots=new BagSlots();
		Object[] errorCode = checkRemoveBagItemByGoodsIdAndNumber(goods,userRoleId);
		if(errorCode!=null){ 
			return errorCode;
		}
		List<RoleItemOperation> itemvos=new ArrayList<RoleItemOperation>();
		for (Entry<String, Integer> entry : goods.entrySet()) {
			String goodsId=entry.getKey();
			int count=entry.getValue();
			Integer numberType = GoodsCategory.getNumberType(goodsId);
			if(numberType != null){
				switch (numberType) {
				case GoodsCategory.GOLD:
				case GoodsCategory.BGOLD:
				case GoodsCategory.MONEY:
					accountExportService.decrCurrencyWithNotify(numberType, count, userRoleId,logType, isNoXF,beizhu);
					break;
					
				case GoodsCategory.ZHENQI:
					roleExportService.costZhenqi(userRoleId, count);
					break;
				case GoodsCategory.RONGYU:
					roleBusinessInfoExportService.costRongyu(userRoleId, count);
					break;
					// FIXME  需要添加新的类型可以在这里添加
				default:
					ChuanQiLog.error("消耗时消耗的数据类型不支持,type:"+numberType);
					break;
				}
			}else{
				List<RoleItemOperation> vos=removeBagItemByItemId(goodsId, count,userRoleId,false);
				itemvos.addAll(vos);
			}
		}
		bagSlots.addRoleItemVos(itemvos);
		if(isNotify){
			sendNotifyGoodsChanage(userRoleId, bagSlots.getRoleItemVos());
		}
		if(isRecord){
			recordRoleItemLog(LogPrintHandle.CONTAINER_REMOVE_GOODS, source, bagSlots.getRoleItemVos(), userRoleId, null);
		}
		return null;
	}
	
	/**
	 * 拆分物品
	 * @param userRoleId
	 * @param guid
	 * @param targetCount
	 * @param slot
	 * @param source
	 * @return
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {0})
	protected BagSlots splitGoods(Long userRoleId, long guid, int targetCount,int slot,int source) {
		 BagSlots bagSlots=new BagSlots();
		 RoleItem roleItem=getItemByGuid(userRoleId, guid, ContainerType.BAGITEM);
		 if(roleItem==null){
			 bagSlots.setErrorCode(AppErrorCode.NOT_FOUND_GOOODS);
			 return bagSlots;
		 }
		 
		 if(roleItem.getCount()<=targetCount){
			 bagSlots.setErrorCode(AppErrorCode.ITEM_COUNT_ENOUGH);
			 return bagSlots;
		 }
		 if(targetCount<=0){
			 bagSlots.setErrorCode(AppErrorCode.CF_COUNT_ERROR);
			 return bagSlots;
		 }
		 
		 BagItems bagItems=getContainer(BagItems.class, ContainerType.BAGITEM, userRoleId);
		 if(!bagItems.hasEmptySlot(slot)){
			 bagSlots.setErrorCode(AppErrorCode.NO_EMPTY_SLOT);
			 return bagSlots;
		 }
		 
		 //删除源数量
		 RoleItemOperation removeVo=removeItem(userRoleId, guid, targetCount, OperationType.UPDATE,ContainerType.BAGITEM); 
		 bagSlots.addRoleItemVo(removeVo);
		 List<RoleItemOperation> logTempR = new ArrayList<>();
		 logTempR.add(removeVo);
		 recordRoleItemLog(LogPrintHandle.CONTAINER_REMOVE_GOODS, source, logTempR, userRoleId, null);
		 
		 //生成新的物品
		 RoleItem resultItem=createNewItem(removeVo.getRoleItem().copy(),targetCount);
		 RoleItemOperation addVo=addItem(resultItem, OperationType.ADD, slot,ContainerType.BAGITEM); 
		 bagSlots.addRoleItemVo(addVo);
		 List<RoleItemOperation> logTempA = new ArrayList<>();
		 logTempA.add(addVo);
		 recordRoleItemLog(LogPrintHandle.CONTAINER_GET_GOODS, source, logTempA, userRoleId, removeVo.getGuid());
		 
		 return bagSlots;
	}
	
	/**
	 * 创建新的物品(返回的对象与参数为同一对象)
	 * @param resultRoleItem
	 * @param targetCount
	 * @return
	 */
	private RoleItem createNewItem(RoleItem resultRoleItem, int targetCount) {
		resultRoleItem.setCount(targetCount);
		resultRoleItem.setId(BagUtil.getIdentity());
		return resultRoleItem;
	}
	
	/**
	 * 绑定于非绑定物品合并
	 * @param userRoleId
	 * @param targetSlot
	 * @param sourceGuid
	 * @return
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {0})
	protected BagSlots moveGoodsID1(Long userRoleId, Integer targetSlot, long sourceGuid) {
		BagSlots bagSlots=new BagSlots();
		
		RoleItem sourceRoleItem=roleBagDao.cacheLoad(sourceGuid, userRoleId);
		RoleItem targetRoleItem=getItemBySlot(targetSlot, userRoleId,ContainerType.BAGITEM);
		
		if(sourceRoleItem==null||targetRoleItem==null){
			//原物品不存在
			bagSlots.setErrorCode(AppErrorCode.NO_SOURCE_TARGET);
			return bagSlots;
		}
		//容错  目标格位于源格位相同
		if(sourceRoleItem.getSlot().equals(targetSlot)){
			bagSlots.setErrorCode(AppErrorCode.NOT_MOVE);
			return bagSlots;
		}
		
		if(!sourceRoleItem.getExpireTime().equals(targetRoleItem.getExpireTime())){
			//有效时间不同
			bagSlots.setErrorCode(AppErrorCode.NOT_SAME_EXPIRETIME);
			return bagSlots;
		}
		
		GoodsConfig  sourceGoodsConfig=goodsConfigExportService.loadById(sourceRoleItem.getGoodsId());
		GoodsConfig  targetGoodsConfig=goodsConfigExportService.loadById(targetRoleItem.getGoodsId()); 
		if(!sourceGoodsConfig.getId1().equals(targetGoodsConfig.getId1())){
			//不是同一类物品的绑定绑定非绑定合并
			bagSlots.setErrorCode(AppErrorCode.NOT_SAME_ID1);
			return bagSlots;
		}
		
		if(sourceGoodsConfig.isBind()){
			//都改为绑定的goodsId
			String  bindGoodsId=sourceGoodsConfig.getId();
			if(bindGoodsId!=null){
				//客服端格子位是没有变化的
				//将物品互换
				RoleItem temp=targetRoleItem;
				targetRoleItem=sourceRoleItem;
				sourceRoleItem=temp;
			}
		}
		
		int targetContainerType=getContainerTypeBySlot(targetSlot,userRoleId);
		int sourceContainerType=getContainerTypeBySlot(sourceRoleItem.getSlot(),userRoleId);
		
		int sourceCount=sourceRoleItem.getCount();
		int targetCount=targetRoleItem.getCount();
		int repeatCount=BagUtil.getItemRepeatCount(sourceRoleItem.getGoodsId());
		
		//bag 2 bag
		if(targetContainerType==ContainerType.BAGITEM.getType()&&sourceContainerType==ContainerType.BAGITEM.getType()){
			if(sourceCount+targetCount>repeatCount){
				sourceRoleItem.setCount(sourceCount+targetCount-repeatCount);
				roleBagDao.cacheUpdate(sourceRoleItem, userRoleId); 
				RoleItemOperation sourceVo=createRoleItemVo(sourceRoleItem, ContainerType.BAGITEM, OperationType.UPDATE,sourceCount);
				bagSlots.addRoleItemVo(sourceVo);
				
				targetRoleItem.setCount(repeatCount);
				roleBagDao.cacheUpdate(targetRoleItem, userRoleId);
				RoleItemOperation targetVo=createRoleItemVo(targetRoleItem, ContainerType.BAGITEM, OperationType.UPDATE,targetCount);
				bagSlots.addRoleItemVo(targetVo);
			}else{
				RoleItemOperation sourceVo=removeItem(userRoleId, sourceRoleItem.getId(), sourceRoleItem.getCount(), OperationType.DELETE,ContainerType.BAGITEM);
				bagSlots.addRoleItemVo(sourceVo);
				
				targetRoleItem.setCount(sourceCount+targetCount);
				roleBagDao.cacheUpdate(targetRoleItem,userRoleId); 
				RoleItemOperation targetVo=createRoleItemVo(targetRoleItem, ContainerType.BAGITEM, OperationType.UPDATE,targetCount);
				bagSlots.addRoleItemVo(targetVo);
			}
		}
		
		// storage 2 storage
		if(targetContainerType==ContainerType.STORAGEITEM.getType()&&sourceContainerType==ContainerType.STORAGEITEM.getType()){
			if(sourceCount+targetCount>repeatCount){ 
				sourceRoleItem.setCount(sourceCount+targetCount-repeatCount);
				roleBagDao.cacheUpdate(sourceRoleItem, userRoleId); 
				RoleItemOperation sourceVo=createRoleItemVo(sourceRoleItem, ContainerType.STORAGEITEM, OperationType.UPDATE,sourceCount);
				bagSlots.addRoleItemVo(sourceVo);
				
				targetRoleItem.setCount(repeatCount);
				roleBagDao.cacheUpdate(targetRoleItem, userRoleId);
				RoleItemOperation targetVo=createRoleItemVo(targetRoleItem, ContainerType.STORAGEITEM, OperationType.UPDATE,targetCount);
				bagSlots.addRoleItemVo(targetVo);
			}else{
				RoleItemOperation sourceVo=removeItem(userRoleId, sourceRoleItem.getId(), sourceRoleItem.getCount(), OperationType.DELETE, ContainerType.STORAGEITEM);//moveItem(userRoleId, sourceRoleItem.getId(), OperationType.DELETE,ContainerType.STORAGEITEM);
				bagSlots.addRoleItemVo(sourceVo);
				
				targetRoleItem.setCount(sourceCount+targetCount);
				roleBagDao.cacheUpdate(targetRoleItem,userRoleId); 
				RoleItemOperation targetVo=createRoleItemVo(targetRoleItem, ContainerType.STORAGEITEM, OperationType.UPDATE,targetCount);
				bagSlots.addRoleItemVo(targetVo);
			}
		}
			 
		//bag 2 storage
		if(targetContainerType==ContainerType.STORAGEITEM.getType()&&sourceContainerType==ContainerType.BAGITEM.getType()){
			if(sourceCount+targetCount>repeatCount){
				sourceRoleItem.setCount(sourceCount+targetCount-repeatCount); 
				RoleItemOperation sourceVo=createRoleItemVo(sourceRoleItem, ContainerType.BAGITEM, OperationType.UPDATE,sourceCount);
				bagSlots.addRoleItemVo(sourceVo);
				
				targetRoleItem.setCount(repeatCount);
				RoleItemOperation targetVo=createRoleItemVo(targetRoleItem, ContainerType.STORAGEITEM, OperationType.UPDATE,targetCount);
				bagSlots.addRoleItemVo(targetVo);
				
				roleBagDao.cacheUpdate(targetRoleItem, userRoleId); 
				roleBagDao.cacheUpdate(sourceRoleItem, userRoleId); 
				
			}else{
				RoleItemOperation sourceVo=removeItem(userRoleId, sourceRoleItem.getId(), sourceRoleItem.getCount(), OperationType.DELETE,ContainerType.BAGITEM);
				bagSlots.addRoleItemVo(sourceVo);
				
				targetRoleItem.setCount(sourceCount+targetCount);
				roleBagDao.cacheUpdate(targetRoleItem,userRoleId); 
				RoleItemOperation targetVo=createRoleItemVo(targetRoleItem, ContainerType.STORAGEITEM, OperationType.UPDATE,targetCount);
				bagSlots.addRoleItemVo(targetVo);
			}
		}
			//storage 2 bag
		if(targetContainerType==ContainerType.BAGITEM.getType()&&sourceContainerType==ContainerType.STORAGEITEM.getType()){
			if(sourceCount+targetCount>repeatCount){ 
				sourceRoleItem.setCount(sourceCount+targetCount-repeatCount);
				roleBagDao.cacheUpdate(sourceRoleItem, userRoleId); 
				RoleItemOperation sourceVo=createRoleItemVo(sourceRoleItem, ContainerType.STORAGEITEM, OperationType.UPDATE,sourceCount);
				bagSlots.addRoleItemVo(sourceVo);
				
				targetRoleItem.setCount(repeatCount);
				roleBagDao.cacheUpdate(targetRoleItem, userRoleId); 
				RoleItemOperation targetVo=createRoleItemVo(targetRoleItem, ContainerType.BAGITEM, OperationType.UPDATE,targetCount); 
				bagSlots.addRoleItemVo(targetVo);
			}else{
				RoleItemOperation sourceVo=removeItem(userRoleId, sourceRoleItem.getId(),sourceRoleItem.getCount(), OperationType.DELETE,ContainerType.STORAGEITEM);
				bagSlots.addRoleItemVo(sourceVo);
				
				targetRoleItem.setCount(sourceCount+targetCount); 
				RoleItemOperation targetVo=createRoleItemVo(targetRoleItem, ContainerType.BAGITEM, OperationType.UPDATE,targetCount); 
				bagSlots.addRoleItemVo(targetVo); 
				
				roleBagDao.cacheUpdate(targetRoleItem,userRoleId); 
			}
		}
		
		return bagSlots;
	}
	
	@SuppressWarnings("unchecked")
	public void updateOtherData(Map<Integer, Object> data,long guid,long userRoleId){
			if(data==null){
				return;
			}
			RoleItem roleItem=roleBagDao.cacheLoad(guid, userRoleId);
			String otherDataStr=roleItem.getOtherData();
			if(otherDataStr==null||"".equals(otherDataStr)){
				String  dataStr=JSONObject.toJSONString(data);
				roleItem.setOtherData(dataStr);
			}else{
				Map<Integer,Object> otherData=JSONObject.parseObject(otherDataStr, Map.class);
				otherData.putAll(data);
				
				String  dataStr=JSONObject.toJSONString(otherData);
				roleItem.setOtherData(dataStr);
			}
			roleBagDao.cacheUpdate(roleItem, userRoleId);
	}

	public void updateQHLevel(int nextQHLevel, long guid, Long userRoleId) { 
		RoleItem roleItem=roleBagDao.cacheLoad(guid, userRoleId);
		roleItem.setQianhuaLevel(nextQHLevel);
		roleBagDao.cacheUpdate(roleItem, userRoleId);
	}
	public void updateZhushenLevel(int nextZhushenLevel, long guid, Long userRoleId) { 
		RoleItem roleItem=roleBagDao.cacheLoad(guid, userRoleId);
		roleItem.setZhushenLevel(nextZhushenLevel);
		roleItem.setZhushenFailTimes(0);
		roleBagDao.cacheUpdate(roleItem, userRoleId);
	}
	public void updateZhushenFailTimes(int failTimes, long guid, Long userRoleId) { 
		RoleItem roleItem=roleBagDao.cacheLoad(guid, userRoleId);
		roleItem.setZhushenFailTimes(failTimes);
		roleBagDao.cacheUpdate(roleItem, userRoleId);
	}
	public void updateZBSJ(String goodsId, long guid, Long userRoleId) { 
		RoleItem roleItem=roleBagDao.cacheLoad(guid, userRoleId);
		roleItem.setGoodsId(goodsId);
		Integer suiji = roleItem.getRandomAttrs();
		if(suiji != null){//升级随机属性
			SuiJiShuXingConfig suiJiShuXingConfig = suiJiShuXingConfigExportService.getConfig(suiji);
			int count = 0;
			int max = suiJiShuXingConfigExportService.getMaxSize();
			GoodsConfig config = goodsConfigExportService.loadById(goodsId);
			while(count < max && suiJiShuXingConfig != null && suiJiShuXingConfig.getNextId() != null){
				count++;
				if(suiJiShuXingConfig.getLevel() < config.getLevelReq()){
					suiji = suiJiShuXingConfig.getNextId();
					suiJiShuXingConfig = suiJiShuXingConfigExportService.getConfig(suiji);
				}else{
					break;
				}
			}
			roleItem.setRandomAttrs(suiji);
		}
		roleBagDao.cacheUpdate(roleItem, userRoleId);
	}
	
	/**
	 * 通过格位获得容器类型
	 * @param targetSlot
	 * @param userRoleId
	 * @return
	 */
	protected int getContainerTypeBySlot(int targetSlot,long userRoleId){
		int bagStartSlot=bagConfigExportService.getBagStartSlot();
		BagItems bagItems=getContainer(BagItems.class, ContainerType.BAGITEM, userRoleId);
		int bagEndSlot=bagItems.getEndSlot();
		
		int storageStartSlot=storageConfigExportService.getStartSlot();
		StorageItems storageItems=getContainer(StorageItems.class, ContainerType.STORAGEITEM, userRoleId);
		int storageEndSlot=storageItems.getEndSlot();
		
		if(targetSlot>=bagStartSlot&&targetSlot<=bagEndSlot){
			return ContainerType.BAGITEM.getType();
		}
		
		if(targetSlot>=storageStartSlot&&targetSlot<=storageEndSlot){
			return ContainerType.STORAGEITEM.getType();
		}
		
		if(targetSlot<0){
			return ContainerType.BODYTITEM.getType();
		}
		return 0;
	}
	
	/**
	 * 修改仓背包/库格子最后格位
	 * @param userRoleId
	 * @param endSlot
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {0})
	protected void modifyContainerEndSlot(Long userRoleId, int endSlot,ContainerType cType) {
		AbstractContainer container = getContainer(cType, userRoleId);
		if(container == null){
			return;
		}
		int oldEndSlot=container.getEndSlot();
		if(endSlot<oldEndSlot)return ;
		
		container.setEndSlot(endSlot);
		
		for (int i=oldEndSlot+1; i<= endSlot;i++) {
			container.addSlot(i);
		}
	}
	

	/**
	  * 用于两个玩家互相交换物品
	  * @param sourceGuids:发起交易者物品
	  * @param targetGuids:被交易者物品
	  * @param sourceRoleId:发起交易者的userRoleId
	  * @param targetRoleId:被交易者的userRoleId
	  * @return 交易者与被交易者需要通知客服端的操作
	  */ 
	public TradesRollback changeItemOwner(List<Long> sourceGuids,List<Long> targetGuids,long sourceRoleId,long targetRoleId, int source, boolean isRecord){
		TradesRollback result=new TradesRollback();
		
		List<RoleItem> sourceItems=new ArrayList<>();
		List<RoleItem> targetItems=new ArrayList<>();
		if(sourceGuids!=null){
			for (long sourceGuid : sourceGuids) {
				RoleItem roleItem=getItemByGuid(sourceRoleId, sourceGuid, ContainerType.BAGITEM);
				sourceItems.add(roleItem);
			} 
		}
		
		if(targetGuids!=null){
			for (long targetGuid : targetGuids) {
				RoleItem roleItem=getItemByGuid(targetRoleId, targetGuid,ContainerType.BAGITEM);
				targetItems.add(roleItem);
			} 
		}
		
		//1:对玩家source操作
		if(sourceGuids!=null){
			deleteTradsGoods(sourceRoleId, sourceGuids, result);
			if(!result.isSuccee()){
				return result;
			}
		}
		
		
		//2:对于玩家target操作
		if(targetGuids!=null){
			deleteTradsGoods(targetRoleId, targetGuids, result);
			if(!result.isSuccee()){
				return result;
			}
		}
		
		//3:对玩家source操作
		if(targetItems.size()>0){
			addTradsGoods(sourceRoleId, targetItems, result);
			if(!result.isSuccee()){
				return result;
			}
		}
		
		//4:对于玩家target操作
		if(sourceItems.size()>0){
			addTradsGoods(targetRoleId, sourceItems, result);
			if(!result.isSuccee()){
				return result;
			}
		}
		
		//3:对db操作
		for (RoleItem roleItem : sourceItems) {
			roleBagDao.onlyUpdateDB(roleItem, sourceRoleId);
		}
		roleBagDao.qzFlushCache(sourceRoleId);
		
		for (RoleItem roleItem : targetItems) {
			roleBagDao.onlyUpdateDB(roleItem, targetRoleId);
		}
		roleBagDao.qzFlushCache(targetRoleId);
		
		Map<Long, List<RoleItemOperation>> roleItemActions = result.getItems();
		if(roleItemActions != null){
			for (Entry<Long, List<RoleItemOperation>> entry : roleItemActions.entrySet()) {
				long userRoleId = entry.getKey();
				List<RoleItemOperation> ras = entry.getValue();
				
				//通知物品信息变化
				sendNotifyGoods(entry.getValue(),userRoleId);
				
				//记录物品日志
				if(isRecord){ 
					String roleName = getRoleName(entry.getKey(),true);
					
					for (RoleItemOperation ra : ras) {
						if(ra.getOperationType()== OperationType.DELETE){
							
							RoleItem item=ra.getRoleItem();
							GamePublishEvent.publishEvent(new RoleItemLogEvent(LogPrintHandle.CONTAINER_REMOVE_GOODS, userRoleId,item.getId(),roleName,item.getGoodsId(),item.getCount(),source,null));
						}else if(ra.getOperationType() == OperationType.ADD){
							
							RoleItem item=ra.getRoleItem();
							GamePublishEvent.publishEvent(new RoleItemLogEvent(LogPrintHandle.CONTAINER_GET_GOODS, userRoleId,item.getId(),roleName,item.getGoodsId(),item.getCount(),source,null));
						}
					}
				}
			}
		}
		return result;
	}
 
	
	
	
	/**
	 * 仅交易使用 
	 * @param sourceRoleId
	 * @param sourceGuids
	 * @param result
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {0})
	private void deleteTradsGoods(long sourceRoleId,List<Long> sourceGuids,TradesRollback result){
		//删除自己的物品  
		for (Long sourceGuid : sourceGuids) {
			RoleItem roleItem = getItemByGuid(sourceRoleId, sourceGuid,ContainerType.BAGITEM);
			
			Object[] flagCode = checkRemoveBagItemByGuid(sourceGuid,sourceRoleId);
			if(flagCode == null){
				RoleItemOperation oldVo = removeItemByTrade(sourceRoleId, sourceGuid);
				result.addRoleItemVo(oldVo, sourceRoleId);
			}else{
				result.setSuccee(false);
				return;
			}
			
			
			//角色缓存操作
			boolean flag = roleBagDao.onlyDeleteCache(roleItem, sourceRoleId);
			if(flag){
				result.recordDeleteRoleCache(roleItem, sourceRoleId);
			}else{
				result.setSuccee(false);
				return;
			}
		}
		 
	}
	
	/**
	 * 仅交易使用  
	 * @param sourceRoleId
	 * @param targetItems
	 * @param result
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {0})
	private void addTradsGoods(long sourceRoleId,List<RoleItem> targetItems,TradesRollback result){
			
		for (RoleItem roleItem : targetItems) {
			
				//检查背包空间是否足够
				int bagEmptyCount = getContainerEmptyCount(ContainerType.BAGITEM, sourceRoleId);
				if(bagEmptyCount >=1){
					roleItem.setUserRoleId(sourceRoleId);
					RoleItemOperation newVo = addItem2BagByTrade(roleItem);
					result.addRoleItemVo(newVo, sourceRoleId);
				}else{
					result.setSuccee(false);
					return;
				}
 
				//角色缓存操作
				boolean flag = roleBagDao.onlyAddCache(roleItem, sourceRoleId);
				if(flag){
					result.recordUpdateRoleCache(roleItem, sourceRoleId);
				}else{
					result.setSuccee(false);
					return;
				}
		}
	}
	
	/**
	 * 数据回滚
	 * @param tradesRollBack
	 * @return
	 */
	public void rollBackTrades(TradesRollback tradesRollBack) {
		 Map<Long, List<RoleItemOperation>>  manageCacheDatas = tradesRollBack.getItems();
		 if(manageCacheDatas!=null){
			for (Entry<Long, List<RoleItemOperation>> entry : manageCacheDatas.entrySet()) {
				Long userRoleId = entry.getKey();
				List<RoleItemOperation> opterationDatas = entry.getValue();
				rollBack(userRoleId, opterationDatas);
			}
		 }
		 
		 //回滚删除物品
		 Map<Long, List<RoleItem>> addDatas = tradesRollBack.getAddDatas();
		 if(addDatas!=null){
			 for (Entry<Long, List<RoleItem>> entry : addDatas.entrySet()) {
				 rollBackDeleteData(entry.getKey(), entry.getValue());
			}
		 }
		 
		//回滚添加的数据
		 Map<Long, List<RoleItem>> deleteDatas = tradesRollBack.getDeleteDatas(); 
		 if(deleteDatas!=null){
			 for (Entry<Long, List<RoleItem>> entry : deleteDatas.entrySet()) {
				 rollBackAddData(entry.getKey(), entry.getValue());
			}
		 }
	}
	
	//回滚删除的数据
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {0})
	private void rollBackDeleteData(long userRoleId,List<RoleItem> roleItems){
		 for (RoleItem roleItem : roleItems) {
			 roleBagDao.onlyAddCache(roleItem, userRoleId);
		 }
	}
	
	//回滚添加的数据
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {0})
	private void rollBackAddData(long userRoleId,List<RoleItem> roleItems){
		 for (RoleItem roleItem : roleItems) {
			 roleBagDao.onlyDeleteCache(roleItem, userRoleId);
		 }
	}
	
	
	 
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {0})
	private void rollBack(long userRoleId,List<RoleItemOperation> opterationDatas ){
		
		for (RoleItemOperation roleItemAction : opterationDatas) {
			//删除掉(之前操作添加)物品 
			if(roleItemAction.getOperationType() == OperationType.ADD){
				RoleItem roleItem = roleItemAction.getRoleItem();
				removeItem(userRoleId, roleItem.getId(), roleItem.getCount(), OperationType.NOOPERATION,ContainerType.BAGITEM);
			}
			//添加(之前操作删除掉)物品 
			if(roleItemAction.getOperationType() == OperationType.DELETE){
				RoleItem roleItem = roleItemAction.getRoleItem();
				addItem(roleItem, OperationType.NOOPERATION, null, ContainerType.BAGITEM);
			}
		}
	}
	
	/**
	 * 消耗指定了guid的物品,不带数量，直接对整个格子的物品进行操作
	 * @param guid
	 * @param userRoleId
	 * @param resource
	 * @param isRecord
	 * @param isNotify 是否直接通知客服端物品变化的结果
	 * @return
	 */ 
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE, indexes = {1})
	protected BagSlots removeBagItemByGuids(List<Long> guids,long userRoleId,int source,boolean isRecord,boolean isNotify){
		BagSlots bagSlots=new BagSlots();
		Object[] flagCode = checkRemoveBagItemByGuids(guids,userRoleId);
		if(flagCode!=null){
			bagSlots.setErrorCode(flagCode);
			return bagSlots;
		}
		
		for (long guid : guids) {
			RoleItem roleItem=getItemByGuid(userRoleId, guid, ContainerType.BAGITEM);
			RoleItemOperation vo=removeItem(userRoleId, guid,roleItem.getCount(),OperationType.DELETE,ContainerType.BAGITEM);
			bagSlots.addRoleItemVo(vo);
		}
		
		//通知前端物品变更
		if(isNotify){
			sendNotifyGoodsChanage(userRoleId, bagSlots.getRoleItemVos());
		}
		
		//记录物品操作 
		if(isRecord){
			recordRoleItemLog(LogPrintHandle.CONTAINER_REMOVE_GOODS, source, bagSlots.getRoleItemVos(), userRoleId,null);
		}
		
		return bagSlots;
	}
	
	/**
	 * 
	 * @param guids
	 * @param userRoleId
	 * @return
	 */
	private Object[] checkRemoveBagItemByGuids(List<Long> guids, long userRoleId) {
		for (Long guid : guids) {
			Object[] errorCode = checkRemoveBagItemByGuid(guid, userRoleId);
			if(errorCode != null){
				return errorCode;
			}
		}
		
		return null;
	}
	
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {1})
	protected BagSlots removeBagItemByGoodsTypes(Map<String, Integer> goodsTypes,long userRoleId, int source, boolean isRecord, boolean isNotify) {
		BagSlots bagSlots=new BagSlots();
		Object[] flagCode=checkRemoveBagItemByGoodsTypes(goodsTypes,userRoleId);
		if(flagCode!=null){
			bagSlots.setErrorCode(flagCode);
			return bagSlots;
		}
		List<RoleItemOperation> roleItemVos=new ArrayList<>();
		
		for (Entry<String, Integer> entry : goodsTypes.entrySet()) {
			String goodsId=entry.getKey();
			int count=entry.getValue();
			roleItemVos.addAll(removeBagItemByItemId(goodsId, count, userRoleId,true));
		} 
		
		bagSlots.addRoleItemVos(roleItemVos);
		
		if(isNotify){
			sendNotifyGoodsChanage(userRoleId, bagSlots.getRoleItemVos());
		}
		
		if(isRecord){
			recordRoleItemLog(LogPrintHandle.CONTAINER_REMOVE_GOODS, source, bagSlots.getRoleItemVos(), userRoleId, null);
		}
		
		return bagSlots;
	 
	}
	
	public Object[] checkRemoveBagItemByGoodsTypes(Map<String, Integer> goods, long userRoleId) {
		if(goods == null) return AppErrorCode.REMOVE_TARGET_NULL;
		for (Entry<String, Integer> entry : goods.entrySet()) {
			Object[] errorCode=checkRemoveBagItemByGoodsType(entry.getKey(), entry.getValue(),userRoleId);
			if(errorCode!=null){
				return errorCode;
			}
		}
		return null;
	}
	
	public int getBagItemCountByGoodsType(String goodsType,long userRoleId){
		int result =0;
		List<RoleItem>  roleItems=getContainerItems(userRoleId, ContainerType.BAGITEM.getType());
		if(roleItems == null || roleItems.size() < 1){
			return 0;
		}
		List<String> goodsIds=goodsConfigExportService.loadIdsById1(goodsType);
		for (RoleItem roleItem : roleItems) {
			 if(goodsIds.contains(roleItem.getGoodsId())){
				 result+=roleItem.getCount();
			 }
		}
		return result;
	}
	/**
	 * 物品进背包或邮件
	 * @param goods		获得物品
	 * @param userRoleId	角色id
	 * @param source	物品来源
	 * @param isRecord	是否记录
	 * @param content	邮件内容(需自行处理code)
	 * @return 1:背包，2：邮件
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {1})
	public int putInBagOrEmail(Map<String,Integer> goods,long userRoleId, int source, boolean isRecord,String content){
		if(checkPutInBag(goods, userRoleId) == null){
			//背包未满进背包
			putInBag(goods, userRoleId, source, isRecord);
			return 1;
		}else{
			//背包已满走邮件
			String[] attachments = EmailUtil.getAttachments(goods);
			String title = EmailUtil.getCodeEmail(AppErrorCode.EMAIL_SERVER_TITLE);
			for (String attachment : attachments) {
				emailExportService.sendEmailToOne(userRoleId,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
			}
			return 2;
		}
	}
	/**
	 * 物品进背包或邮件(限时邮件)
	 * @param goods		获得物品
	 * @param userRoleId	角色id
	 * @param source	物品来源
	 * @param isRecord	是否记录
	 * @param content	邮件内容(需自行处理code)
	 * @param expireTime	过期时间
	 * @return 1:背包，2：邮件
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {1})
	public int putInBagOrEmailExpire(Map<String,Integer> goods,long userRoleId, int source, boolean isRecord,String title,String content,long expireTime){
		if(checkPutInBag(goods, userRoleId) == null){
			//背包未满进背包
			putInBag(goods, userRoleId, source, isRecord);
			return 1;
		}else{
			//背包已满走邮件
			String[] attachments = EmailUtil.getAttachments(goods);
			for (String attachment : attachments) {
				emailExportService.sendEmailToOneExpire(userRoleId,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment,expireTime);
			}
			return 2;
		}
	}
	
	/**
	 * 后台删除玩家背包物品
	 * @param userRoleId 角色ID
	 * @param guid 物品唯一ID
	 * @param count 需要删除的数量
	 * @return
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {0})
	public String webDelRoleGoodsCount(Long userRoleId, Long guid, Integer count){
		//判断数值
		if(null==userRoleId || null==guid || null==count){
			return HttpErrorCode.ARGS_ERROR;
		}
		//拉取数据(玩家在线则拉取缓存，反之拉库)
		boolean isOnline = publicRoleStateExportService.isPublicOnline(userRoleId);
		
		RoleItem item = null;
		if(isOnline){
			item = roleBagDao.cacheLoad(guid, userRoleId);
		}else{
			item = roleBagDao.load(guid, userRoleId, AccessType.getDirectDbType());
		}
		
		if(item == null){
			return HttpErrorCode.NO_GOODS_ERROR;
		}
		
		if(item.getIsDelete() != GameConstants.GOODS_NO_DELETE){
			return HttpErrorCode.NO_GOODS_ERROR;
		}
			
		// 判断数量
		if(item.getCount() < count || count == 0){
			return HttpErrorCode.COUNT_ERROR;
		}
		//物品删除后数量
		Integer beCount = item.getCount()-count;
		//物品唯一ID
		Long goodGuid = guid;
		//大于物品原有数量则全部删除
		if(item.getCount() > count){
			item.setCount(beCount);
			RoleItem delItem = createNewItem(item.copy(), count);
			delItem.setIsDelete(GameConstants.GOODS_GMTOOLS_DELETE);
			if(isOnline){
				roleBagDao.cacheUpdate(item, userRoleId);
			}else{
				roleBagDao.onlyUpdateDB(item, userRoleId);
			}
			roleBagDao.insert(delItem, userRoleId,AccessType.getDirectDbType());
			goodGuid = delItem.getId();
		}else{
			item.setIsDelete(GameConstants.GOODS_GMTOOLS_DELETE);
			if(isOnline){
				roleBagDao.cacheUpdate(item, userRoleId);
			}else{
				roleBagDao.onlyUpdateDB(item, userRoleId);
			}
		}
		//玩家在线则通知客户端物品变化
		if(isOnline){
			BusMsgSender.send2One(userRoleId, ClientCmdType.ITEM_COUNT_MODIFY, new Object[]{new Object[]{guid, beCount }});
		}
		
		//打印日志
		GamePublishEvent.publishEvent(new RoleItemLogEvent(LogPrintHandle.CONTAINER_REMOVE_GOODS, userRoleId,goodGuid,getRoleName(userRoleId,isOnline),item.getGoodsId(), count,GoodsSource.GM_TOOLS_DELETE,null));
		return HttpErrorCode.SUCCESS;
	}

	/**
	 * 添加数值类型的数据   包含添加后的消息推送
	 * @param type {@link GoodsCategory 这个类中的负数  {金币，元宝,绑定元宝等各种数值类型的添加}}
	 * @param inVal
	 * @param userRoleId
	 * @param logType{@link LogPrintHandle}
	 * @return 实际添加的值
	 */
	public long incrNumberWithNotify(int type, long inVal, long userRoleId, int logType, int  beizhu){
		long result =0;
		//过滤掉负值
		if(inVal <=0)return result;
		int value = (int)inVal;
		if(value < 0){
			value = 0;
		}
		switch (type) {
		case GoodsCategory.GOLD:
		case GoodsCategory.BGOLD:
		case GoodsCategory.MONEY:
			result = accountExportService.incrCurrencyWithNotify(type, inVal, userRoleId, logType, beizhu);
			break;
		case GoodsCategory.EXP:
			roleExportService.incrExp(userRoleId, inVal);
			break;
			
		case GoodsCategory.ZHENQI:
			roleExportService.addZhenqi(userRoleId, value);
			
			break;
		case GoodsCategory.RONGYU:
			roleBusinessInfoExportService.addRongyu(userRoleId, value);
			break;
		case GoodsCategory.GONGXIAN:
			guildExportService.addGongxian(userRoleId, value);
			break;
		case GoodsCategory.GONGXUN:
			kuafuArena1v1SourceExportService.addGongxun(userRoleId, value);
			break;
			// FIXME  需要添加新的类型可以在这里添加
		default:
			ChuanQiLog.error("添加属性数据类型不支持,type:"+type);
			break;
		}
		
		return result;
	}
	
	public void updateTiPinLevel(long guid, Long userRoleId,int tipin,String goodsId) { 
		RoleItem roleItem=roleBagDao.cacheLoad(guid, userRoleId);
		roleItem.setTipinValue(tipin);
		if(goodsId != null){
			roleItem.setGoodsId(goodsId);
			Integer rollAtt = roleItem.getRandomAttrs();
			if(rollAtt != null){
				SuiJiShuXingConfig suiJiShuXingConfig = suiJiShuXingConfigExportService.getConfig(rollAtt);
				if(suiJiShuXingConfig != null && suiJiShuXingConfig.getTpId() != null){
					roleItem.setRandomAttrs(suiJiShuXingConfig.getTpId());
				}
			}
		}
		roleBagDao.cacheUpdate(roleItem, userRoleId);
	}
}