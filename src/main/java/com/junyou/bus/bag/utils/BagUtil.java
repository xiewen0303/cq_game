package com.junyou.bus.bag.utils;
 
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.junyou.bus.bag.BagContants;
import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.gameconfig.constants.EquipTypeSlot;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.element.goods.Goods;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

 
/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-28上午9:44:36
 *@Description:
 */
public class BagUtil {
	
	public static ContainerType getContainerTypeBySlot(Integer targetSlot) {
		if(targetSlot>=BagContants.START_BAG_SLOT&&targetSlot<=BagContants.END_BAG_SLOT){
			return ContainerType.BAGITEM;
		}
		
		if(targetSlot>=BagContants.START_STORAGE_SLOT&&targetSlot<=BagContants.END_STORAGE_SLOT){
			return ContainerType.STORAGEITEM;
		}
		
		if(EquipTypeSlot.isZuoQiEquip(targetSlot)){
			return ContainerType.ZUOQIITEM;
		}
		
		if(EquipTypeSlot.isBodyEquip(targetSlot)){
			return ContainerType.BODYTITEM;
		}
		
		if(EquipTypeSlot.isChiBangEquip(targetSlot)){
			return ContainerType.CHIBANGITEM;
		}
		
		if(EquipTypeSlot.isTangBaoEquip(targetSlot)){
			return ContainerType.TANGBAOITEM;
		}
		
		if(EquipTypeSlot.isTianGongEquip(targetSlot)){
			return ContainerType.TIANGONGITEM;
		}
		
		if(EquipTypeSlot.isTianShangEquip(targetSlot)){
			return ContainerType.TIANSHANGITEM;
		}
		if(EquipTypeSlot.isQiLingEquip(targetSlot)){
			return ContainerType.QILINGITEM;
		}
		if(EquipTypeSlot.isTianYuEquip(targetSlot)){
			return ContainerType.TIANYUITEM;
		}
		if(EquipTypeSlot.isChongWuEquip(targetSlot)){
		    return ContainerType.CHONGWUITEM;
		}
		if(EquipTypeSlot.isShenQiEquip(targetSlot)){
			return ContainerType.SHENQIITEM;
		}
		if(EquipTypeSlot.isWuQiEquip(targetSlot)){
			return ContainerType.WUQI;
		}
		return null;
	}
	
	
	/** 
	 * 场景物品对象转换为背包物品对象
	 * @param roleItemExport
	 * @return
	 */ 
	public static RoleItemInput cover2RoleItemInput(Goods goods) {
		
		if(goods == null){
			return null;
		}
		GoodsConfig goodsConfig = BusConfigureHelper.getGoodsConfigExportService().loadById(goods.getGoodsId());
		if(goodsConfig==null)return null;
		
		RoleItemInput rItem=null;
		
		
		boolean isPersistence = IdFactory.getInstance().isGenerateId(goods.getId());
		//怪物掉落
		if(!isPersistence){
			rItem = createItem(goods.getGoodsId(), goods.getCount(), goods.getQianhuaLevel());
			rItem.setGuid(goods.getId());
			return rItem;
		}
		
		rItem = new RoleItemInput();
		
		rItem.setGuid(goods.getId());
		rItem.setGoodsId(goods.getGoodsId());
		rItem.setCount(goods.getCount());
		rItem.setExpireTime(goods.getExpireTime());
		rItem.setRandomAttrs(goods.getRandomAttrIds());
		rItem.setQhLevel(goods.getQianhuaLevel());
		
		//其他属性
		if(goods.getOtherData()!= null){
			String otherData = JSONObject.toJSONString(goods.getOtherData());
			rItem.setOtherData(otherData);
		}
		rItem.setItemCreateTime(goods.getItemCreateTime());
		
		/**
		 * 部分属性需要去配置表里面确认  TODO
		 */
		
		return rItem;
	}
	
	

	/** 
	 * 背包物品对象转换为场景的物品对象
	 * @param roleItemExport
	 * @return
	 */ 
	public static Goods cover2Goods(RoleItemExport roleItemExport) {
		Goods goods = new Goods(roleItemExport.getGuid(), roleItemExport.getGoodsId(),roleItemExport.getCount(), roleItemExport.getItemCreateTime());
		goods.setExpireTime(roleItemExport.getExpireTime()); 
		goods.setQianhuaLevel(roleItemExport.getQianhuaLevel());
		goods.setRandomAttrIds(roleItemExport.getRandomAttrs());
		goods.setOtherData(roleItemExport.getOtherData());
		goods.setItemCreateTime(roleItemExport.getItemCreateTime());
//		goods.setCurrentDurability(roleItemExport.getCurrentDurability());
		 
		return goods;
	}
	 
	
	@SuppressWarnings("unchecked")
	public static Goods cover2Goods(RoleItemInput roleItemInput){
		Goods goods = new Goods(roleItemInput.getGuid(), roleItemInput.getGoodsId(),roleItemInput.getCount(), roleItemInput.getItemCreateTime());
		goods.setExpireTime(roleItemInput.getExpireTime()); 
		goods.setQianhuaLevel(roleItemInput.getQhLevel());
		goods.setRandomAttrIds(roleItemInput.getRandomAttrs());
		goods.setItemCreateTime(roleItemInput.getItemCreateTime());
		
		Map<Integer, Object> otherData = null;
		if(roleItemInput.getOtherData() !=null && !"".equals(roleItemInput.getOtherData())){
			otherData = JSONObject.parseObject(roleItemInput.getOtherData(),Map.class);
		}
		goods.setOtherData(otherData);
		 
		return goods;
	}
	
	/**
	 * 通过配表生成对应的数据
	 * @param itemId	物品id
	 * @param count		物品数量
	 * @param qhLevel	强化等级
	 * @return
	 */
	public static RoleItemInput createItem(String itemId,int count,int qhLevel){
		
		boolean isNumber = GoodsCategory.isNumberType(itemId);
		if(isNumber){
			ChuanQiLog.error("数据错误,\titemId:"+itemId);
			return null;
		}
		
		GoodsConfig goodsConfig = BusConfigureHelper.getGoodsConfigExportService().loadById(itemId);
		if(goodsConfig == null) return null; 
		
		RoleItemInput rItem = new RoleItemInput();
		rItem.setGoodsId(itemId);
		rItem.setCount(count);
		if(GoodsCategory.EQUIP_TYPE == goodsConfig.getCategory()){
			rItem.setQhLevel(qhLevel);
//			String randomAttrs=createRandomAttrs(itemId);  TODO 需要随机属性时解开注释即可
//			rItem.setRandomAttrs(randomAttrs);
		}
		
		
		long expireTime=BagUtil.getTimeOut(itemId);
		rItem.setExpireTime(expireTime);
		rItem.setItemCreateTime(GameSystemTime.getSystemMillTime());
		
		/**
		 * 部分属性需要去配置表里面确认  TODO
		 */
		return rItem;
	}
	
	/**
	 * 生成随机属性
	 * @param goodsId
	 * @return
	 */
	private static Integer createRandomAttrs(String goodsId){
		GoodsConfig goodsConfig = BusConfigureHelper.getGoodsConfigExportService().loadById(goodsId);
		if(!GoodsCategory.isEquip(goodsConfig.getCategory())){
			return null;
		}
		
		int fenzu=goodsConfig.getFenzu();
		if(fenzu==0){
			return null;
		}
		
		Map<Integer, Float> randomGroups= BusConfigureHelper.getSuiJiShuXingConfigExportService().getRandomGroup(fenzu);
		
		return Lottery.getRandomNewKey(randomGroups);
	}
	
	/**
	 * 对内的转换成db对象
	 * @param goods
	 * @return
	 */
	public static RoleItem converInsert2RoleItem(RoleItemInput goods,long userRoleId){
		RoleItem rItem=new RoleItem();
		rItem.setId(goods.getGuid());
		rItem.setGoodsId(goods.getGoodsId());
		rItem.setCount(goods.getCount());
		rItem.setUserRoleId(userRoleId);
		rItem.setExpireTime(goods.getExpireTime());
		rItem.setQianhuaLevel(goods.getQhLevel());
		rItem.setCreateTime(goods.getItemCreateTime());
		rItem.setRandomAttrs(goods.getRandomAttrs());
		rItem.setOtherData(goods.getOtherData());
		/**
		 * 部分其他数据在这里填充   TODO
		 */
		return rItem;
	}
	
	/**
	 * 对内的转换成db对象
	 * @param goods
	 * @return
	 */
	public static RoleItemInput conver2RoleItemInput(RoleItem roleItem){
		RoleItemInput rItem=new RoleItemInput();
		rItem.setGuid(roleItem.getId());
		rItem.setGoodsId(roleItem.getGoodsId());
		rItem.setCount(roleItem.getCount()); 
		rItem.setExpireTime(roleItem.getExpireTime());
		rItem.setQhLevel(roleItem.getQianhuaLevel());
		rItem.setItemCreateTime(roleItem.getCreateTime());
		rItem.setRandomAttrs(roleItem.getRandomAttrs());
		rItem.setOtherData(roleItem.getOtherData());
		
		/**
		 * 部分其他数据在这里填充   TODO
		 */
		return rItem;
	}
	
	/**
	 * 对外的转换成db对象
	 * @param goods
	 * @return
	 */
	public static RoleItem converExport2RoleItem(RoleItemExport goods,long userRoleId){
		RoleItem rItem=new RoleItem();
		rItem.setId(goods.getGuid());
		rItem.setGoodsId(goods.getGoodsId());
		rItem.setCount(goods.getCount());
		rItem.setUserRoleId(userRoleId);
		rItem.setExpireTime(goods.getExpireTime());
		rItem.setQianhuaLevel(goods.getQianhuaLevel());
		rItem.setCreateTime(goods.getItemCreateTime());
		rItem.setRandomAttrs(goods.getRandomAttrs());
		rItem.setOtherData(goods.getOtherDataStr());
		
		/**
		 * 部分其他数据在这里填充   TODO
		 */
		return rItem;
	}
	
	 
	
	/**
	 * 获得物品叠加数，或格子叠加数
	 * @param goodId
	 * @return
	 */
	public static int getItemRepeatCount(String goodsId){
		GoodsConfig  goodsConfig=BusConfigureHelper.getGoodsConfigExportService().loadById(goodsId);
		if(goodsConfig!=null){
			return goodsConfig.getMaxStack();
		}
		return 0;
	}
	
	/**
	 * 物品主键
	 * @return
	 */
	public static long getIdentity(){
		return IdFactory.getInstance().generateId(ServerIdType.GOODS);
	}
	
	/**
	 * 给新物品设置id（并处理相关属性：随机属性）
	 * @return
	 */
	public static void newRoleItemSetId(RoleItem roleItem){
		roleItem.setId(getIdentity());
		roleItem.setRandomAttrs(createRandomAttrs(roleItem.getGoodsId()));
	}
	
	/**
	 * 物品相关主键
	 * @return
	 */
	public static long getOhterIdentity(){
		return IdFactory.getInstance().generateId(ServerIdType.GOODS_OTHER);
	}
	

	public static boolean getIsBind(String goodsId) {
		GoodsConfig  goodsConfig = BusConfigureHelper.getGoodsConfigExportService().loadById(goodsId);
		if(goodsConfig!=null){
			return goodsConfig.isBind();
		}
		return false;
	}


	public static long getTimeOut(String goodsId) {
		GoodsConfig  goodsConfig=BusConfigureHelper.getGoodsConfigExportService().loadById(goodsId);
		if(goodsConfig==null||goodsConfig.getDurationDay() == 0){
			return 0;
		}
		return DatetimeUtil.getCalaDay24Time(goodsConfig.getDurationDay() - 1);
	}
	
	/**
	 * 转换鉴定属性
	 */
	public Object[] converJDAttr(String jiandingStr){
		if(jiandingStr==null||jiandingStr.equals("")){
			return null;
		}
		HashMap<String,Object> jianding= JSONObject.parseObject(jiandingStr, new TypeReference<HashMap<String,Object>>(){});
		Object[] result=new Object[jianding.size()];
		 
		for (Entry<String, Object>  e : jianding.entrySet()) {
				e.getKey();
				
		}
//		Object[] jianding = new Object[]{entity.getPtJianding1(),entity.getPtJianding2(),entity.getGjJianding1(),entity.getGjJianding2(),entity.getZzJianding1(),entity.getZzJianding2()};
		
		return result;
	}


	/**
	 * 物品出售的价格
	 * @param goodsId
	 * @return
	 */
	public static int getSellPriceByItem(String goodsId) {
		GoodsConfig config = BusConfigureHelper.getGoodsConfigExportService().loadById(goodsId);
		if(config == null ) return 0;
		
		return config.getRecycle(); 
	}


	//创建新的物品
	public static RoleItem createNewItem(RoleItem resultRoleItem, int targetCount) {
		resultRoleItem.setCount(targetCount);
		resultRoleItem.setId(getIdentity());
		return resultRoleItem;
	}
	
	
	public static int getMaxValueByNumber(int number){
		switch (number) {
		case 4:
			return 9999;
		case 3:
			return 999;
		case 2:
			return 99; 
		default:
			break;
		}
		return 0;
	}
	
	
	public static RoleItemExport converRoleItemExport(RoleItem roleItem) {
		return new RoleItemExport(roleItem);
	}



	public static int cover2Int(long kyTimeLong) {
		int kyTime = 0;
		if(kyTimeLong > Integer.MAX_VALUE){
			kyTime=Integer.MAX_VALUE;
		}else{
			kyTime=(int)kyTimeLong;
		}
		return kyTime;
	}
	
	/**
	 * 获得物品最大耐久度
	 * @param goodsId
	 * @return
	 */
	public static int getGoodsMaxDurability(String goodsId){
		GoodsConfig goodsConfig = BusConfigureHelper.getGoodsConfigExportService().loadById(goodsId);
		if(goodsConfig==null)return 0; 
		return goodsConfig.getMaxDurability();
	}
}
