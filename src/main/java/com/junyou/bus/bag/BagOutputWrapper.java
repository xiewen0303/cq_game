package com.junyou.bus.bag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.bag.vo.RoleItemOperation;
import com.junyou.bus.equip.configure.export.TaoZhuangZhuShenConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.GoodsCategory;

/**
 * 
  *@author: wind
  *@email: 18221610336@163.com
  *@version: 2014-12-4上午10:53:41
  *@Description:
 */
public class BagOutputWrapper {
	/**
	 * 物品配置id
	 */
	private static final String GOODS_ID="0";
	/**
	 * 物品guid
	 */
	private static final String GUID="1";
	/**
	 * 格位号
	 */
	private static final String SLOT="2";
	/**
	 * 物品数量
	 */
	private static final String GOODS_COUNT="3";
	/**
	 * 过期时间
	 */
	private static final String EXPIRE_TIME="4";
	/**
	 * 装备强化等级
	 */
	private static final String EQUIP_QH_LEVEL="5";
	/**
	 * 随机属性
	 */
	private static final String RANDOM_ATTR="6";
	/**
	 * 提品值
	 */
	private static final String TIPIN_VALUE="7";
	/**
	 * 铸神值
	 */
	private static final String ZHUSHEN_VALUE="8";
	/**
	 * 铸神概率值
	 */
	private static final String ZHUSHEN_RATE_VALUE="9";
//	/**
//	 * 耐久度
//	 */
//	private static final String CURRENT_DURABILITY="6";
//	/**
//	 * 装备祝福值
//	 */
//	private static final String EQUIP_QH_ZFZ="8";
	
	 
	public static Object[] erroInfo(int error, String id, Integer count){
		return new Object[]{0, error, id, count};
	}
	
	public static Object getOutWrapperData(int type,Object data){
		Object result =null;
		switch (type) {
		case OutputType.ITEMVO:
			result=formart((RoleItem)data);
			break;
		case OutputType.CHAIFEN:
			result=formartChaifen((RoleItemOperation)data);
			break;
		case OutputType.MODIFY:
			result=formartModify((RoleItemOperation)data);
			break;
		case OutputType.SLOTMODIFY:
			result=formartSlotModify((RoleItemOperation)data);
			break;
		case OutputType.MOVESLOT:
			result=formartMoveModify((RoleItemOperation)data);
			break;
		case OutputType.CHONGWUMOVESLOT:
		    result=formartChongwuMoveModify((RoleItemOperation)data);
		    break;
		case OutputType.SHENQIMOVESLOT:
		    result=formartShenQiMoveModify((RoleItemOperation)data);
		    break;
		case OutputType.EXPORT_TO_ITEMVO:
			result=formartByRoleItemExport((RoleItemExport)data);
			break;
		default:
			break;
		}
		return result;
	}
	
//	private static int getEquipZfz(RoleItemExport roleItem) {
//		if(roleItem == null) return 0;
//		
//		Object zfzObj=roleItem.getOtherData(OtherDataType.EQUIP_ZFZ);
//		int zfz=CovertObjectUtil.object2int(zfzObj);
//		
//		Object qhLastTimeObj=roleItem.getOtherData(OtherDataType.QH_LASTTIME); 
//		
//		Integer qhLevelObj=roleItem.getQianhuaLevel();
//		int qhLevel=qhLevelObj==null?0:qhLevelObj;
//		
//		int nextQHLevel=qhLevel+1;
//		QiangHuaBiaoConfig qhConfig=BusConfigureHelper.getQiangHuaBiaoConfigExportService().getQHConfig(nextQHLevel);
//		
//		long qhLastTime = 0l;
//		if(qhLastTimeObj!=null){
//			qhLastTime=Long.parseLong(qhLastTimeObj+"");
//		}
//		
//		boolean isClear=qhConfig.isClear();
//		zfz=getZfuValue(zfz, isClear, qhLastTime);
//		
//		return zfz;
//	}
	 
	
//	private static int getZfuValue(int zfzValue,boolean isClear,long qiLastTime){ 
//		if(qiLastTime<=DatetimeUtil.getDate00Time()&&isClear){
//			return 0;
//		}
//		return zfzValue;
//	}
	
	
	/**
	 * 物品属性输出,查询数据,不带操作状态的
	 * @param entity
	 * @return
	 */
	public static Map<String, Object> formart(RoleItem entity) {
		Map<String,Object> outputMap = new HashMap<String,Object>();
		outputMap.put(GOODS_ID, entity.getGoodsId());
		outputMap.put(GUID, entity.getId());
		outputMap.put(SLOT, entity.getSlot());
		outputMap.put(GOODS_COUNT, entity.getCount());
		outputMap.put(EXPIRE_TIME, entity.getExpireTime());
		outputMap.put(TIPIN_VALUE, entity.getTipinValue());
		Integer zhushenLevel = entity.getZhushenLevel();
		Integer zhushenFailTimes = entity.getZhushenFailTimes();
		if(zhushenLevel==null){
			zhushenLevel = 0;
		}
		if(zhushenFailTimes == null){
			zhushenFailTimes =0;
		}
		outputMap.put(ZHUSHEN_VALUE, zhushenLevel);
		int maxZhushenLevel = BusConfigureHelper.getTaoZhuangZhuShenConfigExportService().getMaxLevel();
		if(zhushenLevel>=maxZhushenLevel){
			outputMap.put(ZHUSHEN_RATE_VALUE, 0);
		}else{
			TaoZhuangZhuShenConfig taoZhuangZhuShenConfig = BusConfigureHelper.getTaoZhuangZhuShenConfigExportService().loadByLevel(zhushenLevel+1);
			int rate =(int)Math.floor((taoZhuangZhuShenConfig.getOdds() + zhushenFailTimes*taoZhuangZhuShenConfig.getAddodds())/100);
			outputMap.put(ZHUSHEN_RATE_VALUE, rate);
		}
		
		
		GoodsConfig  goodsConfig = BusConfigureHelper.getGoodsConfigExportService().loadById(entity.getGoodsId());
		
		if(GoodsCategory.EQUIP_TYPE == goodsConfig.getCategory()){
			
			outputMap.put(EQUIP_QH_LEVEL, entity.getQianhuaLevel());
			
//			int maxDurability = BagUtil.getGoodsMaxDurability(entity.getGoodsId());
//			if(maxDurability != 0){
//				int durability = maxDurability-entity.getCurrentDurability();  
//				 
//				outputMap.put(CURRENT_DURABILITY, durability<0?0:durability);
//			}
			
			Integer randomAttrs = entity.getRandomAttrs();
			if(randomAttrs != null){
				outputMap.put(RANDOM_ATTR, randomAttrs);
			}
			
//			RoleItemExport roleItemExport = BagUtil.converRoleItemExport(entity);
//			outputMap.put(EQUIP_QH_ZFZ,getEquipZfz(roleItemExport));
		}
		
		return outputMap;
	}
	
	
	
	/**
	 * 物品属性输出,查询数据,不带操作状态的
	 * @param entity
	 * @return
	 */
	
	public static Object formart(Map<String,Integer> entity) {
		Object[] result = new Object[entity.size()];
		int i=0;
		for (Entry<String,Integer> entry : entity.entrySet()) {
			Map<String,Object> outputMap = new HashMap<String,Object>();
			outputMap.put(GOODS_ID, entry.getKey());
			outputMap.put(GOODS_COUNT, entry.getValue());
			result[i++] = outputMap;
		}
		return result;
	}
	
	/**
	 * 物品属性输出,查询数据,不带操作状态的
	 * @param entity
	 * @return
	 */
	private static Map<String, Object> formartByRoleItemExport(RoleItemExport entity) {
		Map<String,Object> outputMap = new HashMap<String,Object>();
		outputMap.put(GOODS_ID, entity.getGoodsId());
		outputMap.put(GUID, entity.getGuid());
		outputMap.put(SLOT, entity.getSlot());
		outputMap.put(GOODS_COUNT, entity.getCount());
		outputMap.put(EXPIRE_TIME, entity.getExpireTime());
		
		GoodsConfig  goodsConfig = BusConfigureHelper.getGoodsConfigExportService().loadById(entity.getGoodsId());
		
		if(GoodsCategory.EQUIP_TYPE == goodsConfig.getCategory()){
			outputMap.put(EQUIP_QH_LEVEL, entity.getQianhuaLevel());
//			int maxDurability = BagUtil.getGoodsMaxDurability(entity.getGoodsId());
//			if(maxDurability != 0){
//				int durability = maxDurability-entity.getCurrentDurability();
//				outputMap.put(CURRENT_DURABILITY, durability<0?0:durability);
//			}
//			
			Integer randomAttrs = entity.getRandomAttrs();
			if(randomAttrs != null){
				outputMap.put(RANDOM_ATTR, randomAttrs);
			}
//			
//			outputMap.put(EQUIP_QH_ZFZ,getEquipZfz(entity));
		}
		return outputMap;
	}
	 
	
	/**
	 * 拆分后的产物
	 * @param entity
	 * @return
	 */
	private static Object formartChaifen(RoleItemOperation entity) {
		Object[] outputMap =new Object[3];
		if(entity.getOperationType()==OperationType.ADD){
			outputMap[0]=entity.getGuid();
			outputMap[1]=entity.getSlot();
			outputMap[2]=entity.getCount(); 
		}
		if(entity.getOperationType()==OperationType.UPDATE){ 
			outputMap[0]=entity.getGuid(); 
			outputMap[1]=entity.getCount(); 
		}
		return outputMap;
	}
	
	
	
	
	private static Object formartModify(RoleItemOperation entity) {
		Object[] outputMap=new Object[2]; 
		outputMap[0]=entity.getGuid();
		if(entity.getOperationType()==OperationType.ADD||entity.getOperationType()==OperationType.UPDATE){
			outputMap[1]=entity.getCount();
		} else if(entity.getOperationType()==OperationType.DELETE){
			outputMap[1]=0;
		}
		return outputMap;
	}
	
	
	private static Object formartSlotModify(RoleItemOperation entity) {
		Object[] outputMap=new Object[3];
		outputMap[0]=entity.getGuid();
		outputMap[1]=entity.getSlot();
	 
		if(entity.getOperationType()==OperationType.ADD||entity.getOperationType()==OperationType.UPDATE){
			outputMap[2]=entity.getCount();
		} else if(entity.getOperationType()==OperationType.DELETE){
			outputMap[2]=0;
		}
		return outputMap;
	}
	
	private static Object formartMoveModify(RoleItemOperation entity) {
		Object[] outputMap = new Object[2];
		outputMap[0]=entity.getGuid();
		outputMap[1]=entity.getSlot();
		return outputMap;
	}
	
	private static Object formartChongwuMoveModify(RoleItemOperation entity) {
	    Object[] outputMap = new Object[3];
	    outputMap[0]=entity.getGuid();
	    outputMap[1]=entity.getSlot();
	    outputMap[2]=entity.getChongwuId();
	    return outputMap;
	}
	private static Object formartShenQiMoveModify(RoleItemOperation entity) {
	    Object[] outputMap = new Object[3];
	    outputMap[0]=entity.getGuid();
	    outputMap[1]=entity.getSlot();
	    outputMap[2]=entity.getShenQiId();
	    return outputMap;
	}
}
