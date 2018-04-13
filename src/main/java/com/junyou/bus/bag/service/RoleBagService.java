package com.junyou.bus.bag.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.BagOutputWrapper;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.OutputType;
import com.junyou.bus.bag.configure.export.BagConfig;
import com.junyou.bus.bag.configure.export.BagConfigExportService;
import com.junyou.bus.bag.configure.export.StorageConfig;
import com.junyou.bus.bag.configure.export.StorageConfigExportService;
import com.junyou.bus.bag.dao.RoleBagDao;
import com.junyou.bus.bag.dao.RoleItemDescDao;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.entity.RoleItemDesc;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.manage.AbstractContainer;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.bag.vo.RoleItemOperation;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.number.LongUtils;

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午3:38:12
 *@Description: 提供给BagAction使用的
 */

@Service
public class RoleBagService extends BagBaseService {

	@Autowired
	private RoleBagDao roleBagDao; 
	@Autowired
	private GoodsUseLimitService goodsUseLimitService;
	@Autowired
	private RoleItemDescDao roleItemDescDao;
	@Autowired
	private BagConfigExportService bagConfigExportService;
	@Autowired
	private StorageConfigExportService storageConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	/**
	 * 获得背包物品信息
	 * @param userRoleId
	 * @return
	 */
	public void loginHandle(Long userRoleId) {
		//处理格位信息
		onloginHandleSlot(userRoleId);
		
		List<Object> data=new ArrayList<>();
		/**背包信息**/
		getDataByContainerType(userRoleId,ContainerType.BAGITEM,data);
		 
		/**身上装备信息**/
		getDataByContainerType(userRoleId,ContainerType.BODYTITEM,data);
		/**坐骑装备信息**/
		getDataByContainerType(userRoleId,ContainerType.ZUOQIITEM,data);
		/**翅膀装备信息**/
		getDataByContainerType(userRoleId,ContainerType.CHIBANGITEM,data);
		/**器灵装备信息**/
		//getDataByContainerType(userRoleId,ContainerType.QILINGITEM,data);
		/**糖宝装备信息**/
		getDataByContainerType(userRoleId,ContainerType.TANGBAOITEM,data);
		/**天工装备信息**/
		getDataByContainerType(userRoleId,ContainerType.TIANGONGITEM,data);
		/**天裳装备信息**/
		getDataByContainerType(userRoleId,ContainerType.TIANSHANGITEM,data);
		/**器灵装备信息**/
		getDataByContainerType(userRoleId,ContainerType.QILINGITEM,data);
		/**天羽装备信息**/
		getDataByContainerType(userRoleId,ContainerType.TIANYUITEM,data);
		/**新圣剑**/
		getDataByContainerType(userRoleId,ContainerType.WUQI,data);
		
		/**  有限制的物品使用次数信息  */  
		Map<Integer,Integer> limitUseCount=goodsUseLimitService.getItemUseXzcsInfo(userRoleId);
		
		RoleItemDesc containerDesc =roleItemDescDao.cacheLoad(userRoleId, userRoleId);
		
		long bagKyTimeLong = getBagNoUseTime(userRoleId); 
		long storageKyTimeLong = getStorageNoUseTime(userRoleId); 
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_BAG_ITEMS, new Object[]{data.toArray(),limitUseCount,containerDesc.getBagOpeningSlot(),BagUtil.cover2Int(bagKyTimeLong),containerDesc.getStorageOpeningSlot(),BagUtil.cover2Int(storageKyTimeLong)});
 	}
	
	public void getDataByContainerType(long userRoleId,ContainerType type,List<Object> data){ 
		List<RoleItem> roleItems=getContainerItems(userRoleId, type.getType());
		if(roleItems!=null&&roleItems.size()>0){
			for (int i=0;i<roleItems.size();i++) {
					data.add(BagOutputWrapper.getOutWrapperData(OutputType.ITEMVO,roleItems.get(i))); 
			}
		}
	}
	
    /**
     * 获取宠物特有的附属装备vo数据
     * @param userRoleId
     * @param chongwuId
     */
    public List<Object> getChongwuEquipData(long userRoleId, Integer chongwuId) {
        if(null == chongwuId) return null;
        List<Object> data = null;
        List<RoleItem> roleItems = getContainerItems(userRoleId, ContainerType.CHONGWUITEM.getType());
        if (!ObjectUtil.isEmpty(roleItems)) {
            data = new ArrayList<Object>();
            for (RoleItem roleItem : roleItems) {
                if (null != roleItem.getChongwuId() && roleItem.getChongwuId().equals(chongwuId)) {
                    data.add(BagOutputWrapper.getOutWrapperData(OutputType.ITEMVO, roleItem));
                }
            }
        }
        return data;
    }
    
    /**
     * 获取宠物特有的附属装备vo数据
     * @param userRoleId
     * @param chongwuId
     */
    public Object[] getShenQiEquipData(long userRoleId, Integer shenQiId,Integer slot) {
        if(null == shenQiId) return null;
//        List<Object[]> data = null;
        List<RoleItem> roleItems = getContainerItems(userRoleId, ContainerType.SHENQIITEM.getType());
        if (!ObjectUtil.isEmpty(roleItems)) {
//            data = new ArrayList<>();
            for (RoleItem roleItem : roleItems) {
                if (null != roleItem.getShenqiId() && roleItem.getShenqiId().intValue()==shenQiId.intValue() && roleItem.getSlot().equals(slot)) {
                    return new Object[]{roleItem.getGoodsId(),roleItem.getId(),roleItem.getSlot()};
                }
            }
        }
        return null;
    }
	
	private Object[] getData(long userRoleId,ContainerType type){ 
		Object[] result=null;
		List<RoleItem> roleItems=getContainerItems(userRoleId, type.getType());
		if(roleItems!=null&&roleItems.size()>0){ 
			result=new Object[roleItems.size()];
			for (int i=0;i<roleItems.size();i++) { 
				result[i]=BagOutputWrapper.getOutWrapperData(OutputType.ITEMVO,roleItems.get(i)); 
			}
		}
		return result;
	}
	
	
	public Object[] getStorageItems(Long userRoleId) { 
		Object[] result=getData(userRoleId,  ContainerType.STORAGEITEM); 
		 
		RoleItemDesc containerDesc =roleItemDescDao.cacheLoad(userRoleId, userRoleId); 
		long kyTimeLong = getStorageNoUseTime(userRoleId); 
		
 		return new Object[]{result,containerDesc.getStorageOpeningSlot(),BagUtil.cover2Int(kyTimeLong)};
	}
	
	public Object[] clearUpBag(Long userRoleId) {
		BagSlots bagSlots=resetContainerSlot(ContainerType.BAGITEM, userRoleId);
		if(!bagSlots.isSuccee()){
			return null;
		}
		
		List<RoleItemOperation> roleItemVos=bagSlots.getRoleItemVos();
		
		Object[] result=null;
		if(roleItemVos!=null){
			result=new Object[roleItemVos.size()]; 
			for (int i=0;i<roleItemVos.size();i++) {
				result[i]=BagOutputWrapper.getOutWrapperData(OutputType.SLOTMODIFY, roleItemVos.get(i));
			}
		}
		
		return result;
	} 
	
	public Object[] clearUpStorage(Long userRoleId) {
		BagSlots bagSlots=resetContainerSlot(ContainerType.STORAGEITEM, userRoleId);
		if(!bagSlots.isSuccee()){
			return null;
		}
		
		List<RoleItemOperation> roleItemVos=bagSlots.getRoleItemVos();
		Object[] result=null;
		if(roleItemVos!=null){
			result=new Object[roleItemVos.size()];	
			for (int i=0;i<roleItemVos.size();i++) {
				result[i]=BagOutputWrapper.getOutWrapperData(OutputType.SLOTMODIFY, roleItemVos.get(i));
			}
		}
		return result;
	}
	
	
	public Object[] moveGoods2Slot(Long userRoleId, Integer targetSlot,long goodsPkId){
		RoleItem roleItem=roleBagDao.cacheLoad(goodsPkId, userRoleId);
		if(roleItem==null){
			return AppErrorCode.NOT_FOUND_GOOODS;
		}
		
		if(roleItem.getSlot().equals(targetSlot)){
			return AppErrorCode.NOT_MOVE;
		}
		int targetContainerType=getContainerTypeBySlot(targetSlot, userRoleId);
		
        BagSlots bagSolts = moveSlot(goodsPkId, targetSlot, targetContainerType, userRoleId, 0);
		if(!bagSolts.isSuccee()){
			return bagSolts.getErrorCode();
		}
		Object[] result=new Object[3];
		result[0]=1;
		List<RoleItemOperation> roleItemvos=bagSolts.getRoleItemVos();
		if(roleItemvos!=null){
			for (int i=0;i<roleItemvos.size();i++) { 
				result[i+1]=BagOutputWrapper.getOutWrapperData(OutputType.SLOTMODIFY,roleItemvos.get(i));
			}
		}
		return result;
	}
	
//	public Object[] moveGoods2Container(Long userRoleId, int containerType,long goodsPkId) {
//		BagSlots bagSlots=bagService.moveSlot(goodsPkId, null, containerType, userRoleId);
//		
//		if(!bagSlots.isSuccee()){
//			return bagSlots.getErrorCode();
//		}
//		Object[] result=new Object[3];
//		result[0]=1;
//		List<RoleItemVo> roleItemvos=bagSlots.getRoleItemVos();
//		for (int i=0;i<roleItemvos.size();i++) {
//			result[i+1]=BagOutputWrapper.formartModify(roleItemvos.get(i));
//		}
//		return result;
//	}
	 
	public Object[] useBagItem(Long userRoleId, long goodsPkId, int count) {
		BagSlots bagSlots=removeBagItemByGuid(goodsPkId, count, userRoleId, 0,true,true);
		if(!bagSlots.isSuccee()){
			return bagSlots.getErrorCode();
		}
		return null;
	}
	
	public Object[] bagDestroyItem(Long userRoleId, long goodsPkId) {
		BagSlots  bagSlots=destroyBagItemByGuid(goodsPkId, userRoleId, GoodsSource.GOODS_DROP_ITEMS, true,false);
		if(!bagSlots.isSuccee()){
			return bagSlots.getErrorCode();
		}
		return new Object[]{1,bagSlots.getRoleItemVos().get(0).getRoleItem().getId()};
	}
	
	public Object[] bagGiveUp(Long userRoleId, long guid) {
		RoleItem targetRoleItem = getItemByGuid(userRoleId, guid, ContainerType.BAGITEM);
		if(targetRoleItem == null){
			return AppErrorCode.NOT_FOUND_GOOODS;
		}
		
		//判定是否绑定物品
		GoodsConfig config = goodsConfigExportService.loadById(targetRoleItem.getGoodsId());
		if(config == null){
			ChuanQiLog.error("物品或装备配置表异常:goodsId="+targetRoleItem.getGoodsId());
			return AppErrorCode.NO_FIND_CONFIG;
		}
		
		if(config.isBind()){
			return AppErrorCode.NO_GIVE_BIND;
		}
		
		BagSlots  bagSlots=removeBagItemByGuid(guid, userRoleId, GoodsSource.GIVE_UP, true,false);
		if(!bagSlots.isSuccee()){
			return bagSlots.getErrorCode();
		}
		RoleItem roleItem=null;
		if(bagSlots.getRoleItemVos()!=null&&bagSlots.getRoleItemVos().size()>0){
			roleItem=bagSlots.getRoleItemVos().get(0).getRoleItem();
			if(roleItem == null){
				ChuanQiLog.error("丢弃物品异常,玩家背包没有找到对应的物品,userRoleId:"+userRoleId+"\t guid:"+guid);
				return AppErrorCode.BAG_GVIEUP_FAIL;
			}
			//丢入场景之中
			RoleItemExport roleItemExport = BagUtil.converRoleItemExport(roleItem); 
			Object[] dropGoods=new Object[]{ BagUtil.cover2Goods(roleItemExport) };
			Object[] data = new Object[]{userRoleId, null, null, null, dropGoods, 0, 0,null,userRoleId};
			roleBagDao.qzFlushCache(userRoleId);
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.DROP_GOODS, data);
		}else{
			ChuanQiLog.error("丢弃物品异常,userRoleId:"+userRoleId+"\t guid:"+guid);
			return AppErrorCode.BAG_GVIEUP_FAIL;
		}
		
		return new Object[]{1,roleItem.getId()};
	}
	
	/**
	 * 拆分物品
	 * @param userRoleId
	 * @param goodsPkId
	 * @param targetCount
	 * @return
	 */
	public Object[] splitGoods(Long userRoleId, long guid, int targetCount,int slot) {
		 BagSlots bagSlots=super.splitGoods(userRoleId, guid, targetCount, slot,GoodsSource.SPLIT_GOODS);
		 if(!bagSlots.isSuccee()){
			return bagSlots.getErrorCode();
		 } 
		 Object[] result=new Object[3];
		 result[0]=1;
		 List<RoleItemOperation> roleItemVos=bagSlots.getRoleItemVos();
		 for(int i=0;i<bagSlots.getRoleItemVos().size();i++) { 
			 result[i+1]=BagOutputWrapper.getOutWrapperData(OutputType.CHAIFEN, roleItemVos.get(i));
		 }
		return result;
	}
	
	/**
	 * 出售物品给npc
	 * @param userRoleId
	 * @param guid
	 * @return
	 */
	public Object[] sellItem2Npc(Long userRoleId, Long guid) {
		BagSlots bagSlots=removeBagItemByGuid(guid, userRoleId, GoodsSource.NPCSELL, true, true);
		if(!bagSlots.isSuccee()){
			return bagSlots.getErrorCode();
		}
		RoleItem roleItem=bagSlots.getRoleItemVos().get(0).getRoleItem();
	 
		/**
		 * 物品出售的价格
		 */
		int price = BagUtil.getSellPriceByItem(roleItem.getGoodsId());
		if(price <= 0){
			return AppErrorCode.GOODS_NO_SELL;
		}
		
		if(price*roleItem.getCount() > 0){
			accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, price*roleItem.getCount(), userRoleId, LogPrintHandle.GET_SELL_GOODS, LogPrintHandle.GBZ_SELL_GOODS);
		}
		
		return AppErrorCode.OK;
	}
	
	/**
	 * 绑定于非绑定合并
	 * @param userRoleId
	 * @param targetSlot
	 * @param goodsPkId
	 * @return
	 */
	public Object[] moveGoodsID1ToSlot(Long userRoleId, Integer targetSlot,long guid){
		BagSlots bagSlots=moveGoodsID1(userRoleId,targetSlot,guid);
		if(!bagSlots.isSuccee()){
			return bagSlots.getErrorCode();
		}
		Object[] result=new Object[3];
		result[0]=1;
		List<RoleItemOperation> itemVos=bagSlots.getRoleItemVos();
		for (int i=0;i<itemVos.size();i++){
			result[i+1]=BagOutputWrapper.getOutWrapperData(OutputType.SLOTMODIFY, itemVos.get(i));
		}
		//
//		if(result[2]==null){
//			result[2]=new Object[]{0,sourceSlot,0};
//		}
		return result;
	}
	
	public void clearBagData(long userRoleId){
		OfflineHandleSlot(userRoleId);
		offOnline(userRoleId);
	}
	
	 
	/**
	 * 激活背包各位
	 * @param userRoleId
	 * @param openSlot
	 * @return
	 */
	public Object[] bagSlotActivation(Long userRoleId, Integer openSlot,Integer gold) {
  
		RoleItemDesc containerDesc =roleItemDescDao.cacheLoad(userRoleId, userRoleId);
		
		int bagMax=bagConfigExportService.getMaxEndSlot();
		
		//判断是否已开启格位大于等于背包最大格位
		if(openSlot>bagMax){
			return AppErrorCode.BAG_ALL_SLOT_OPEN;
		}
		
		//即将要自动开启的格位
		int openingSlot= containerDesc.getBagOpeningSlot();
		if(openSlot<openingSlot){
			return AppErrorCode.BAG_OPEN_SLOT_ERROR;
		}
		 
		//所需消耗的元宝
		int costYb = 0;
		BagConfig openingSlotConfig=bagConfigExportService.getBagConfigBySlot(openingSlot);
		if(openingSlotConfig == null){
			ChuanQiLog.error("没有找到bagConfig的数据，openingSlot="+openingSlot);
			return AppErrorCode.BAG_SLOT_CONFIG_ERROR;
		}
		
		//开启中的格位
		long noUseTime = getBagNoUseTime(userRoleId);//角色未用开格时间
		long needTime = 0;
		if(noUseTime < openingSlotConfig.getTime()*1000 ) {
			costYb+=openingSlotConfig.getNeedMoney();
			needTime = openingSlotConfig.getTime()*1000 - noUseTime;
		}
		int gezi = 1;
		for (int i =openingSlot+1;i<=openSlot;i++){
			BagConfig config=bagConfigExportService.getBagConfigBySlot(i);
			costYb+=config.getNeedMoney();
			gezi++;
		}
	 	
		//判断元宝是否足够
		if(costYb > 0){
			if(gold != costYb){
				return new Object[]{2,needTime};
			}
			
			Object[] errorCode = accountExportService.isEnought(GoodsCategory.GOLD, costYb, userRoleId);
			if(errorCode != null){
				return errorCode;
			}
			
			//消耗元宝
			accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, costYb, userRoleId,LogPrintHandle.CONSUME_BAG_OPEN_SLOT,true,LogPrintHandle.CONSUME_BAG_OPEN_SLOT);
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,costYb,LogPrintHandle.CONSUME_BAG_OPEN_SLOT,QQXiaoFeiType.CONSUME_BAG_OPEN_SLOT,1});
			}
		}
		
		//修改数据
		containerDesc.setBagOpeningSlot(openSlot+1);
		containerDesc.setBagUpdateTime(GameSystemTime.getSystemMillTime());
		containerDesc.setBagKyTime(0l); 
		roleItemDescDao.cacheUpdate(containerDesc, userRoleId);
		
		//对背包容器修改一下
		modifyContainerEndSlot(userRoleId,openSlot,ContainerType.BAGITEM);
		
		notifyStage(userRoleId, getBagStorageAttrs(userRoleId));
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.OPEN_BAG, gezi});
		return new Object[]{1,openSlot};
	}
	
	/**
	 * 变化属性通知场景
	 * @param openSlot	已经开启的格位
	 */
	private void notifyStage(long userRoleId,Map<String,Long> attrs){
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_SLOT_ATTRS, attrs);
	}
	
	/**
	 * 激活仓库各位
	 * @param userRoleId
	 * @param openSlot
	 * @return
	 */
	public Object[] storageSlotActivation(Long userRoleId, Integer openSlot,Integer gold) {
  
		RoleItemDesc containerDesc =roleItemDescDao.cacheLoad(userRoleId, userRoleId);
		
		int bagMax=storageConfigExportService.getMaxEndSlot();
		
		//判断是否已开启格位大于等于背包最大格位
		if(openSlot>bagMax){
			return AppErrorCode.STORAGE_ALL_SLOT_OPEN;
		}
		
		//即将要自动开启的格位
		int openingSlot= containerDesc.getStorageOpeningSlot();
		if(openSlot<openingSlot){
			return AppErrorCode.BAG_OPEN_SLOT_ERROR;
		}
		 
		//所需消耗的元宝
		int costYb = 0;
		StorageConfig openingSlotConfig=storageConfigExportService.getStorageConfigBySlot(openingSlot);
		if(openingSlotConfig == null){
			ChuanQiLog.error("没有找到bagConfig的数据，openingSlot="+openingSlot);
			return AppErrorCode.BAG_SLOT_CONFIG_ERROR;
		}
		
		//开启中的格位
		long noUseTime = getStorageNoUseTime(userRoleId);//角色未用开格时间
		long needTime = 0;
		if(noUseTime < openingSlotConfig.getTime()*1000) {
			costYb+=openingSlotConfig.getNeedMoney();
			needTime = openingSlotConfig.getTime()*1000 - noUseTime;
		}
		
		for (int i =openingSlot+1;i<=openSlot;i++){
			StorageConfig config=storageConfigExportService.getStorageConfigBySlot(i);
			costYb+=config.getNeedMoney();
		}
	 	
		//判断元宝是否足够
		if(costYb > 0){
			if(gold != costYb){
				return new Object[]{2,needTime};
			}
			Object[] errorCode = accountExportService.isEnought(GoodsCategory.GOLD, costYb, userRoleId);
			if(errorCode != null){
				return errorCode;
			}
			//消耗元宝
			accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, costYb, userRoleId,LogPrintHandle.CONSUME_STORAGE_OPEN_SLOT,true,LogPrintHandle.CBZ_STORAGE_OPEN_SLOT);
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,costYb,LogPrintHandle.CONSUME_STORAGE_OPEN_SLOT,QQXiaoFeiType.CONSUME_STORAGE_OPEN_SLOT,1});
			}
		}
			
		//修改数据
		containerDesc.setStorageOpeningSlot(openSlot+1);
		containerDesc.setStorageUpdateTime(GameSystemTime.getSystemMillTime());
		containerDesc.setStorageKyTime(0l); 
		roleItemDescDao.cacheUpdate(containerDesc, userRoleId);
		
		//对背包容器修改一下
		modifyContainerEndSlot(userRoleId,openSlot,ContainerType.STORAGEITEM);
		
		notifyStage(userRoleId, getBagStorageAttrs(userRoleId));
		return new Object[]{1,openSlot};
	}
	
	public Map<String,Long> getBagStorageAttrs(long userRoleId){
		Map<String,Long> attrs =new HashMap<>();
		
		AbstractContainer storageContainer = getContainer(ContainerType.STORAGEITEM, userRoleId);
		if(storageContainer != null){
			int sSlot = storageContainer.getEndSlot();
			StorageConfig sConfig=storageConfigExportService.getStorageConfigBySlot(sSlot);
			if(sConfig != null){
				Map<String, Long> sAttrs = sConfig.getAttrs();
				ObjectUtil.longMapAdd(attrs, sAttrs);
			}
		}
		
		AbstractContainer bagContainer = getContainer(ContainerType.BAGITEM, userRoleId);
		if(bagContainer != null){
			int bSlot = bagContainer.getEndSlot();
			
			
			BagConfig bConfig=bagConfigExportService.getBagConfigBySlot(bSlot);
			if(bConfig != null){
				Map<String,Long> bAttrs = bConfig.getAttrs();
				ObjectUtil.longMapAdd(attrs, bAttrs);
			}
		}
		
		return attrs;
	}
 
	 
	/**
	 * 计算角色未使用的背包开格时间
	 * @return
	 */
	private long getBagNoUseTime(Long userRoleId) { 
		RoleItemDesc desc = roleItemDescDao.cacheLoad(userRoleId, userRoleId);
		//角色已保存未用开格时间 
		long noUseTime=desc.getBagKyTime();
		long time =GameSystemTime.getSystemMillTime(); 
		long beginRecordTime=desc.getBagUpdateTime();
		noUseTime +=(time-beginRecordTime);
		return  noUseTime;
	}
//	
	/**
	 * 计算角色未使用的仓库开格时间
	 * @return
	 */
	private long getStorageNoUseTime(Long userRoleId) { 
		RoleItemDesc desc = roleItemDescDao.cacheLoad(userRoleId, userRoleId);
		//角色已保存未用开格时间 
		long noUseTime=desc.getStorageKyTime();
		long time =GameSystemTime.getSystemMillTime(); 
		long beginRecordTime=desc.getStorageUpdateTime();
		noUseTime +=(time-beginRecordTime);
		return  noUseTime;
	} 
	
	/**
	 * 下线处理可扩容累加时间信息
	 * @param userRoleId
	 */
	protected void OfflineHandleSlot(long userRoleId){
		RoleItemDesc desc = roleItemDescDao.cacheLoad(userRoleId, userRoleId);
		
		long bagNoUseTime=getBagNoUseTime(userRoleId);
		desc.setBagKyTime(bagNoUseTime);
		
		long storageNoUseTime=getStorageNoUseTime(userRoleId);
		desc.setStorageKyTime(storageNoUseTime);
		
		roleItemDescDao.cacheUpdate(desc, userRoleId);
	}
	
	/**
	 * 登陆时修改在线开始时间
	 * @param userRoleId
	 */
	public void onloginHandleSlot(long userRoleId){
		RoleItemDesc desc = roleItemDescDao.cacheLoad(userRoleId, userRoleId);
		long nowTime=GameSystemTime.getSystemMillTime();
		desc.setBagUpdateTime(nowTime);
		desc.setStorageUpdateTime(nowTime);
		roleItemDescDao.cacheUpdate(desc, userRoleId);
	}
	
	/**
	 * 拾取物品进背包
	 * @param userRoleId
	 * @param goods
	 * @return
	 */
	public Object[] takeGoods(Long userRoleId, RoleItemInput roleItemInput) {
		BagSlots bagSlots = putInBag(roleItemInput, userRoleId,GoodsSource.STAGE_TAKE_GOODS,true);
		
		if(bagSlots.getErrorCode() != null){
			//拾取一个物品失败,再次放回场景
//			Object[] dropGoods=new Object[]{BagUtil.cover2Goods(roleItemInput) };
//			Object[] data = new Object[]{userRoleId, null, null, null, dropGoods, 0, 0,null,userRoleId};
//			BusMsgSender.send2Stage(userRoleId, InnerCmdType.DROP_GOODS, data);
		}
		
		return bagSlots.getErrorCode();
	}
	
	/**
	 * 拾取数值类型进背包(来自跨服)
	 * @param userRoleId
	 * @param goods
	 * @return
	 */
	public void takeGoodsKF(Long userRoleId, Object[] goods) {
		String goodsId = (String)goods[0];
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
		if(goodsConfig.getCategory() >= 0){
			return;
		}
		incrNumberWithNotify(goodsConfig.getCategory(), LongUtils.obj2long(goods[1]), userRoleId, LogPrintHandle.GET_GOODS_PICKUP, LogPrintHandle.GBZ_GOODS_PICKUP);
	}

	/**
	 * 客户端请求物品展示
	 * @param userRoleId
	 * @param id
	 * @return
	 */
	public Object[] goodsZhanshi(Long userRoleId, Long id) {
		if(!publicRoleStateExportService.isPublicOnline(userRoleId)){
			return AppErrorCode.LINK_IS_OUTTIME;
		}
		
		RoleItem roleItem = null;
		for (ContainerType type : ContainerType.values()) {
			roleItem = getItemByGuid(userRoleId, id, type);
			if(roleItem != null){
				break;
			}
		}
		if(roleItem == null){
			return AppErrorCode.LINK_IS_OUTTIME;
		}
		return new Object[]{AppErrorCode.SUCCESS, BagOutputWrapper.formart(roleItem)};
	}
	
	/**
	 * 是否是套装
	 * @param goodsId
	 * @return
	 */
	public boolean isTaoZhuang(String goodsId){
		GoodsConfig config = goodsConfigExportService.loadById(goodsId);
		if(config.getSuit() != 0){
			return true;
		}
		return false;
	}
	/**
	 * 是否是紫装以上
	 * @param goodsId
	 * @return
	 */
	public boolean isZiZhuang(String goodsId){
		GoodsConfig config = goodsConfigExportService.loadById(goodsId);
		if(config.getRareLevel() >= 3){
			return true;
		}
		return false;
	}
	/**
	 * 是否是橙装以上
	 * @param goodsId
	 * @return
	 */
	public boolean isChengZhuang(String goodsId){
		GoodsConfig config = goodsConfigExportService.loadById(goodsId);
		if(config.getRareLevel() >= 4){
			return true;
		}
		return false;
	}
}