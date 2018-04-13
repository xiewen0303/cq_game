package com.junyou.bus.bag.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.bus.bag.configure.export.BagConfig;
import com.junyou.bus.bag.configure.export.BagConfigExportService;
import com.junyou.bus.bag.configure.export.StorageConfig;
import com.junyou.bus.bag.configure.export.StorageConfigExportService;
import com.junyou.bus.bag.manage.AbstractContainer;
import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.gameconfig.constants.EffectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.TradesRollback;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.service.BagBaseService;
import com.junyou.bus.bag.service.RoleBagService;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.kernel.sync.annotation.Sync;

/**
 * 背包对外接口
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-12-3下午6:37:34
 *@Description: 提供给外部使用的背包
 */
@Service
public class RoleBagExportService extends BagBaseService {

	@Autowired
	private AccountExportService accountExportService;
	
	@Autowired
	private RoleExportService roleExportService;
	
	@Autowired
	private RoleBagService roleBagService;

	
	/**
	 * 添加物品（可以包含数值类型的添加 ）
	 * @param goods 物品
	 * @param userRoleId
	 * @param source
	 * @param beizhu 
	 * @param isRecord
	 */
	public void putGoodsVoAndNumberAttr(Map<String,GoodsConfigureVo> goods,long userRoleId, int source,int logType,int beizhu, boolean isRecord){
		if(goods == null){
			return;
		}
		
		Map<String,GoodsConfigureVo> items = null;
		
		for (Entry<String, GoodsConfigureVo> entry : goods.entrySet()) {
			String goodsId  = entry.getKey();
			GoodsConfigureVo vo = entry.getValue();
			Integer numberType = GoodsCategory.getNumberType(goodsId);
			
			Integer count = vo.getGoodsCount();
			
			if(numberType != null){
				incrNumberWithNotify(numberType, count, userRoleId, logType, beizhu);
			}else{
				if(items == null) {
					items =  new HashMap<String, GoodsConfigureVo>();
				}
				items.put(goodsId,  vo);
			}
		}
		
		//添加物品进背包
		if(items!=null){
			putInBagVo(items, userRoleId, source, isRecord);
		} 
	}
	/**
	 * 物品进背包或邮件(物品包含数值类型)
	 * @param goods		获得物品
	 * @param userRoleId	角色id
	 * @param source	物品来源
	 * @param isRecord	是否记录
	 * @param content	邮件内容(需自行处理code)
	 * @return 1:背包，2：邮件
	 */
	@Sync(component = GameConstants.COMPONENT_BAG_SHARE,indexes = {1})
	public int putInBagOrEmailWithNumber(Map<String,Integer> goods,long userRoleId, int source, int logType,int beizhu,boolean isRecord,String content){
		if(checkPutGoodsAndNumberAttr(goods, userRoleId) == null){
			//背包未满进背包
			putGoodsAndNumberAttr(goods, userRoleId, source, logType, beizhu, isRecord);
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
	 * 添加物品（可以包含数值类型的添加 ）
	 * @param goods 物品
	 * @param userRoleId
	 * @param source
	 * @param beizhu 
	 * @param isRecord
	 */
	public void putGoodsAndNumberAttr(Map<String,Integer> goods,long userRoleId, int source,int logType,int beizhu, boolean isRecord){
		if(goods == null) return;
		
		Map<String,Integer> items = null;
		
		for (Entry<String, Integer> entry : goods.entrySet()) {
			String goodsId  = entry.getKey();
			Integer numberType = GoodsCategory.getNumberType(goodsId);
			
			if(numberType!= null){
				incrNumberWithNotify(numberType, entry.getValue(), userRoleId, logType, beizhu);
			}else{
				if(items == null) {
					items =  new HashMap<String, Integer>();
				}
				items.put(goodsId,  entry.getValue());
			}
		}
		
		//添加物品进背包
		if(items!=null){
			putInBag(items, userRoleId, source, isRecord);
		} 
	}
	
	/**
	 * 检查数字类型数据是否足够
	 * @param type   {@link GoodsCategory}
	 * @param value
	 * @param userRoleId
	 * @return null 表示足够,其他的表示errorCode
	 */
	public Object[] isEnought(int type,long value,Long userRoleId){
		Object[] result = null;
		switch (type) {
		case GoodsCategory.GOLD:
		case GoodsCategory.BGOLD:
		case GoodsCategory.MONEY:
			result = accountExportService.isEnought(type, value, userRoleId);
			break;
		case GoodsCategory.EXP:
			
			//TODO			roleExportService.
			break;
			
		case GoodsCategory.ZHENQI:
			boolean flagZhen =	roleExportService.isEnoughZhenqi(userRoleId, value);
			
			if(flagZhen){
				result = AppErrorCode.ZHEN_ERROR;
			}
			break;
 
			
		default:
			result =AppErrorCode.ENOUGHT_ERROR;
			ChuanQiLog.error("添加属性数据类型不支持,type:"+type);
			break;
		}
		
		return result;
	}
	   
	
	 
	/**
	 * 扣除数字类型
	 * @param type
	 * @param value
	 * @param userRoleId
	 * @param logType
	 * @param isNoXF  是否参与消费统计(true:是)
	 * @param beizhu
	 * @return
	 */
	public Object[] decrNumberWithNotify(int type, int value, Long userRoleId,int logType, boolean isNoXF,int beizhu) {
		Object[] result = null;
		//过滤掉负值
		if(value <=0)return AppErrorCode.MONEY_DATA_ERROR;
		
		switch (type) {
		case GoodsCategory.GOLD:
		case GoodsCategory.BGOLD:
		case GoodsCategory.MONEY:
			result =accountExportService.decrCurrencyWithNotify(type, value, userRoleId, logType,isNoXF, beizhu);
			break;
			
			
//		case GoodsCategory.EXP:
//			
//			//TODO		roleExportService.
//			break;
			
		case GoodsCategory.ZHENQI:
			roleExportService.costZhenqi(userRoleId, (int)value);
			break;

		default:
			result =AppErrorCode.ENOUGHT_ERROR;
			ChuanQiLog.error("消耗数据类型不支持,type:"+type);
			break;
		}
		return result; 
	}
	
	
	
	/**
	 * 获得身上所有装备
	 * @return
	 */
	public List<RoleItemExport> getBodyEquips(long userRoleId){
		List<RoleItemExport> roleItemExports=new ArrayList<>();
		List<RoleItem> equips=super.getContainerItems(userRoleId, ContainerType.BODYTITEM.getType());
		if(equips==null)return roleItemExports;
		for (RoleItem roleItem : equips) {
			roleItemExports.add(BagUtil.converRoleItemExport(roleItem));
		}
		return roleItemExports;
	}
	
	/**
	 * 获取玩家身上套装个数
	 * @param userRoleId
	 * @return
	 */
	public Integer getTaoZhuangCount(long userRoleId){
		int count = 0;
		List<RoleItem> equips=super.getContainerItems(userRoleId, ContainerType.BODYTITEM.getType());
		if(equips==null)return 0;
		for (RoleItem roleItem : equips) {
			boolean b = roleBagService.isTaoZhuang(roleItem.getGoodsId());
			if(b){
				count++;
			}
		}
		return count;
	}
	/**
	 * 获取玩家身上紫装个数
	 * @param userRoleId
	 * @return
	 */
	public Integer getZiZhuangCount(long userRoleId){
		int count = 0;
		List<RoleItem> equips=super.getContainerItems(userRoleId, ContainerType.BODYTITEM.getType());
		if(equips==null)return 0;
		for (RoleItem roleItem : equips) {
			boolean b = roleBagService.isZiZhuang(roleItem.getGoodsId());
			if(b){
				count++;
			}
		}
		return count;
	}
	/**
	 * 获取玩家身上橙装个数
	 * @param userRoleId
	 * @return
	 */
	public Integer getChengZhuangCount(long userRoleId){
		int count = 0;
		List<RoleItem> equips=super.getContainerItems(userRoleId, ContainerType.BODYTITEM.getType());
		if(equips==null)return 0;
		for (RoleItem roleItem : equips) {
			boolean b = roleBagService.isChengZhuang(roleItem.getGoodsId());
			if(b){
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 对多类型物品进行消耗(大类物品Id1)
	 * @param goods
	 * @param userRoleId
	 * @param source 来源{@Link GoodsSource}
	 * @param isRecord
	 * @param isNotify
	 * @return
	 */
	public BagSlots removeBagItemByGoodsTypes(Map<String,Integer> goods,long userRoleId,int source,boolean isRecord,boolean isNotify){
		return super.removeBagItemByGoodsTypes(goods, userRoleId,source,isRecord,isNotify);
	}
	
	/**
	 * 物品移动的处理接口 
	 * @param sourceGuid			源格位对应的物品唯一id
	 * @param targetSlot 			将要移动到的目标格位
	 * @param targetContainerType 	将要移动到的目标容器
	 * @param userRoleId			操作者(角色id)
	 * @return						BagSlots
	 */
	 public BagSlots moveSlot(long sourceGuid,Integer targetSlot,Integer targetContainerType,long userRoleId){
		return super.moveSlot(sourceGuid, targetSlot, targetContainerType, userRoleId, 0);
	}
	
    public BagSlots moveSlot2(long sourceGuid, Integer targetSlot, Integer targetContainerType, long userRoleId, Integer chongwuId) {
        return super.moveSlot(sourceGuid, targetSlot, targetContainerType, userRoleId, chongwuId);
    }
    
    public BagSlots moveSlot3(long sourceGuid, Integer targetSlot, Integer targetContainerType, long userRoleId, Integer shenQiId) {
        return super.moveSlot(sourceGuid, targetSlot, targetContainerType, userRoleId, shenQiId);
    }
	
	 
	/**
	 * 获得背包里面的一个物品
	 * @param guid
	 * @return
	 */
	public RoleItemExport getBagItemByGuid(long userRoleId,long guid){ 
		RoleItem roleItem=super.getItemByGuid(userRoleId,guid,ContainerType.BAGITEM);
		if(roleItem!=null){
			return	BagUtil.converRoleItemExport(roleItem);
		}
		return null;
	}
	
	/**
	 * 获得身上里面的一个物品
	 * @param guid
	 * @return
	 */
	public RoleItemExport getBodyGoodsByGuid(long userRoleId,long guid){
		RoleItem roleItem=getItemByGuid(userRoleId, guid,ContainerType.BODYTITEM);
		if(roleItem!=null){
			return	BagUtil.converRoleItemExport(roleItem);
		}
		return null;
	}
	
	/**
	 * 获得仓库里面的一个物品
	 * @param guid
	 * @return
	 */
	public RoleItemExport getStorageGoodsByGuid(long userRoleId,long guid){
		RoleItem roleItem=getItemByGuid(userRoleId, guid,ContainerType.STORAGEITEM);
		if(roleItem!=null){
			return	BagUtil.converRoleItemExport(roleItem);
		}
		return null;
	}
	
	/**
	 * 通过格子位获得对应的物品对象
	 * @param slot
	 * @param userRoleId
	 * @return
	 */
	public RoleItemExport getRoleGoodsBySlot(int slot,long userRoleId,ContainerType containerType){
		RoleItem roleItem =  getItemBySlot(slot, userRoleId,containerType);
		if(roleItem!=null){
			return	BagUtil.converRoleItemExport(roleItem);
		}
		return null;
	}
	
	/**
	 * 添加物品入背包
	 * @param goodsId 		商品Id
	 * @param count 		数量
	 * @param userRoleId 	玩家id
	 * @param source 		物品来源
	 * @param isRecord 		是否记录
	 * @return
	 */
	public BagSlots putInBag(Map<String,Integer> goods,long userRoleId, int source, boolean isRecord){
		return super.putInBag(goods, userRoleId, source, isRecord);
	}
	
	/**
	 * 添加物品入背包
	 * @param goodsId 		商品Id
	 * @param count 		数量
	 * @param userRoleId 	玩家id
	 * @param source 		物品来源
	 * @param isRecord 		是否记录
	 * @return
	 */
	public BagSlots putInBagVo(Map<String,GoodsConfigureVo> goods,long userRoleId, int source, boolean isRecord){
		return super.putInBagVo(goods, userRoleId, source, isRecord);
	}
	
	/**
	 * 添加物品进背包，（带状态信息的物品,如场景拾取物品等）
	 * @param goods 		商品
	 * @param userRoleId 	玩家id
	 * @param source 		物品来源
	 * @param isRecord 		是否记录
	 * @return
	 */
	public BagSlots putInBag(RoleItemInput goods,long userRoleId, int source, boolean isRecord){
		return super.putInBag(goods, userRoleId, source, isRecord);
	} 
	
	/**
	 * 移除指定goodsId的物品
	 * @param goodsId
	 * @param count
	 * @param userRoleId
	 * @param source
	 * @param isRecord
	 * @param isNofity  是否直接通知客服端物品变化信息 
	 * @return
	 */
	public BagSlots removeBagItemByGoodsId(String goodsId,int count,long userRoleId,int source,boolean isRecord,boolean isNofity){
		return super.removeBagItemByGoodsId(goodsId, count, userRoleId, source, isRecord,isNofity);
	}
	
	/**
	 * 移除指定guid的物品
	 * @param guid 物品主键ID
	 * @param count
	 * @param userRoleId
	 * @param resource
	 * @param isRecord
	 * @param isNotify:是否推送背包数量变化给前端
	 * @return
	 */ 
	public BagSlots removeBagItemByGuid(long guid,long userRoleId,int resource,boolean isRecord,boolean isNotify){
		return super.removeBagItemByGuid(guid, userRoleId, resource, isRecord,isNotify);
	}
	/**
	 * 对多类型物品进行消耗
	 * @param goods  goodsId=count
	 * @param userRoleId
	 * @param source
	 * @param isRecord
	 * @param isNofity  是否直接通知客服端物品变化信息 
	 * @return
	 */
	public BagSlots removeBagItemByGoods(Map<String,Integer> goods,long userRoleId,int source,boolean isRecord,boolean isNofity){
		return super.removeBagItemByGoods(goods, userRoleId, source, isRecord,isNofity);
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
	public BagSlots removeBagItemByGoodsType(String goodsType,int count,long userRoleId,int source,boolean isRecord,boolean isNotify){
		return super.removeBagItemByGoodsType(goodsType, count, userRoleId, source, isRecord, isNotify);
	}
	
	/**
	 * 检查消耗物品,不指定消耗物品绑定或非绑定
	 * @param goodsIdType(大类id)
	 * @param count
	 * @param userRoleId
	 * @return 结果为null即可以操作     其他：错误码
	 */
	public Object[] checkRemoveBagItemByGoodsType(String goodsIdType,int count,long userRoleId){
		return super.checkRemoveBagItemByGoodsType(goodsIdType, count, userRoleId);
	}

	/**
	 * 登陆时加载背包数据
	 * @param identity
	 * @return
	 */
	public List<RoleItem> initAll(Long identity) { 
		return super.initAll(identity);
	}
	
	/**
	 * 更行otherData
	 * @param data
	 */
	public void updateOtherData(Map<Integer, Object> data,long guid,long userRoleId) {
		super.updateOtherData(data,guid,userRoleId);
	}

	public void updateQHLevel(int nextQHLevel, long guid, Long userRoleId) {
		super.updateQHLevel(nextQHLevel,guid,userRoleId); 
	}
	public void updateZhushenLevel(int nextZhushenLevel, long guid, Long userRoleId) {
		super.updateZhushenLevel(nextZhushenLevel,guid,userRoleId); 
	}
	public void updateZhushenFailTimes(int failTimes, long guid, Long userRoleId) {
		super.updateZhushenFailTimes(failTimes,guid,userRoleId); 
	}
	public void updateZBSJ(String goodsId, long guid, Long userRoleId) {
		super.updateZBSJ(goodsId,guid,userRoleId); 
	}
	public void updateTiPinLevel(long guid, Long userRoleId,int tipin,String goodsId){
		super.updateTiPinLevel(guid, userRoleId, tipin, goodsId);
	}

	/**
	 * 通过guid,count来删除物品
	 * @param guid
	 * @param count
	 * @param userRoleId
	 * @param source  来源{@Link GoodsSource}
	 * @param isRecord
	 * @param isNotify
	 * @return
	 */ 
	public BagSlots removeBagItemByGuid(long guid,int count,long userRoleId,int source,boolean isRecord,boolean isNotify){
		return super.removeBagItemByGuid(guid, count, userRoleId, source, isRecord, isNotify);
	}
	
	/**
	  * 用于的两个玩家互相交换物品
	  * @param sourceGuids:发起交易者物品
	  * @param targetGuids:被交易者物品
	  * @param sourceRoleId:发起交易者的userRoleId
	  * @param targetRoleId:被交易者的userRoleId
	  * @return 交易者与被交易者需要通知客服端的操作
	  */ 
	public TradesRollback changeGoods(List<Long> sourceGuids,List<Long> targetGuids,long sourceRoleId,long targetRoleId,int source,boolean isRecord){
		return super.changeItemOwner(sourceGuids, targetGuids, sourceRoleId, targetRoleId, source, isRecord);
	} 
	
	/**
	 * 删除物品
	 * @param guids
	 * @param userRoleId
	 * @param source  来源{@Link GoodsSource}
	 * @param isRecord
	 * @param isNotify
	 * @return
	 */
	public BagSlots removeBagItemByGuids(List<Long> guids,long userRoleId,int source,boolean isRecord,boolean isNotify){
		return super.removeBagItemByGuids(guids, userRoleId, source, isRecord, isNotify);
	}

	/**
	 * 获得背包里面某个物品的数量
	 * @param goodsId
	 * @param userRoleId
	 * @return
	 */
	public int getBagItemCountByGoodsId(String goodsId, Long userRoleId) {
		return getItemCountByGoodsId(goodsId, userRoleId, ContainerType.BAGITEM.getType());
	}

	/**
	 * 获得身上的装备
	 * @param userRoleId
	 * @return
	 */
	public List<RoleItemExport> getEquip(long userRoleId,ContainerType type) {
		List<RoleItemExport> roleItemExports=new ArrayList<>();
		List<RoleItem> equips=super.getContainerItems(userRoleId, type.getType());
		if(equips==null)return roleItemExports;
		for (RoleItem roleItem : equips) {
			roleItemExports.add(BagUtil.converRoleItemExport(roleItem));
		}
		return roleItemExports;
	}
	
	/**
	 * 获得装备
	 * @param userRoleId
	 * @param guid
	 * @param 类型
	 * @return
	 */
	public RoleItemExport getOtherEquip(Long userRoleId, long guid,ContainerType type) {
		RoleItem roleItem =  super.getItemByGuid(userRoleId, guid, type);
		if(roleItem != null){
			return BagUtil.converRoleItemExport(roleItem);
		}
		return null;
	}
	
	/**
	 * 获得玩家背包仓库对角色增加的属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String,Long> getBagStorageAttrs(long userRoleId){
		return roleBagService.getBagStorageAttrs(userRoleId);
	}
	/**
	 * 获得背包剩余空间
	 * @param userRoleId
	 * @return
	 */
	public int getBagEmptySize(Long userRoleId) {
		return super.getContainerEmptyCount(ContainerType.BAGITEM, userRoleId);
	}
	
	/**
	 * 后台删除玩家背包物品
	 * @param userRoleId 角色ID
	 * @param guid 物品唯一ID
	 * @param count 需要删除的数量
	 * @return
	 */
	public String webDelRoleGoodsCount(Long userRoleId, Long guid, Integer count){
		return roleBagService.webDelRoleGoodsCount(userRoleId, guid, count);
	}
	
	/**
	 * 获取所有装备
	 * @param userRoleId
	 * @return
	 */
	public Object[] getAllEquips(Long userRoleId){
		List<Object> data = new ArrayList<>();
		roleBagService.getDataByContainerType(userRoleId,ContainerType.BODYTITEM,data);
		return data.toArray();
	}
	
	/**
	 * 获取角色的身上装备强化总等级
	 */
	public int getAllEquipsQHLevel(Long userRoleId){
		int qhLevel = 0;
		List<RoleItem> equips=super.getContainerItems(userRoleId, ContainerType.BODYTITEM.getType());
		if(equips==null){
			return 0;
		}
		for (RoleItem roleItem : equips) {
			qhLevel += roleItem.getQianhuaLevel();
		}
		return qhLevel;
	}
	
	/**
	 * 移除物品
	 * @param goods	（含数值类型）
	 * @param userRoleId
	 * @return
	 */
	public Object[] removeBagItemByGoodsIdAndNumber(Map<String, Integer> goods,long userRoleId, int source, boolean isRecord,boolean isNotify,int logType, boolean isNoXF,int beizhu){
		return roleBagService.removeBagItemByGoodsIdAndNumber(goods, userRoleId, source, isRecord, isNotify, logType, isNoXF, beizhu);
	}
	
	/**
	 * 获取宠物身上所有的附属装备vo集合
	 * 
	 * @param userRoleId
	 * @param chongwuId
	 * @return
	 */
	public Object[] getAllChongwuEquips(Long userRoleId, Integer chongwuId){
	    List<Object> data = roleBagService.getChongwuEquipData(userRoleId, chongwuId);
	    return data == null ? null : data.toArray();
	}
	
	/**
	 * 获取宠物身上所有的附属装备vo集合
	 * 
	 * @param userRoleId
	 * @param chongwuId
	 * @return
	 */
	public Object[] getAllShenQiEquips(Long userRoleId, Integer shenQiId,Integer slot){
	    Object[] obj = roleBagService.getShenQiEquipData(userRoleId, shenQiId,slot);
	    return obj;
	}
}
