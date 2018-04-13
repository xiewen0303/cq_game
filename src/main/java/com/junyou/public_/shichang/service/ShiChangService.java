package com.junyou.public_.shichang.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.BagOutputWrapper;
import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.OutputType;
import com.junyou.bus.bag.dao.RoleBagDao;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.shichang.configure.export.ShiChangFenLeiConfigExportService;
import com.junyou.public_.shichang.dao.PaiMaiDao;
import com.junyou.public_.shichang.entity.PaiMaiInfo;
import com.junyou.public_.shichang.manage.PaiMai;
import com.junyou.public_.shichang.manage.PaiMaiManage;
import com.junyou.public_.shichang.manage.PriceComparator;
import com.junyou.public_.shichang.manage.SellPriceComparator;
import com.junyou.public_.shichang.manage.UpTimeComparator;
import com.junyou.public_.shichang.util.PaiMaiUtil;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午3:38:12
 *@Description: 市场
 */

@Service
public class ShiChangService{
	
	@Autowired
	private  RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private PaiMaiDao paiMaiDao;
	@Autowired
	private RoleBagDao roleBagDao;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private ShiChangFenLeiConfigExportService shiChangFenLeiConfigExportService;
	
	public void init(){
		if(KuafuConfigPropUtil.isKuafuServer()){//跨服服务器无需初始化
			return;
		} 
		List<PaiMaiInfo> paiMaiInfos = paiMaiDao.getRecords(new QueryParamMap<String, Object>());
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		
		queryParams.put("containerType", ContainerType.PAIMAI.getType());
		queryParams.put("isDelete", 0);
		List<RoleItem> roleItems = roleBagDao.getRecords(queryParams, null, AccessType.getDirectDbType()); 
		
		PaiMaiManage.getPaiMaiManage().init(roleItems, paiMaiInfos);
	}
	
	/**
	 * 下架
	 * @param userRoleId
	 * @param guid
	 * @return
	 */
	public Object[] pmGoodsDown(long userRoleId,long guid){
		
		PaiMai paiMai = PaiMaiManage.getPaiMaiManage().getPaiMaiByGuid(guid);
		if(paiMai == null || !paiMai.getUserRoleId().equals(userRoleId)){
			return AppErrorCode.PAIMAI_NO_EXIST; 
		}
	 
		Object[] errorCode = roleBagExportService.checkPutInBag(paiMai.getGoodsId(), paiMai.getCount(), userRoleId);
		if(errorCode != null) {
			return errorCode;
		}
		
		paiMai = removePaiMai(guid, userRoleId);
		
		RoleItem roleItem = paiMai.getRoleItem();
		RoleItemInput  roleItemInput = BagUtil.conver2RoleItemInput(roleItem);
		roleItemInput.setNewSlot(true);
		
		roleBagExportService.putInBag(roleItemInput, userRoleId, GoodsSource.PAI_MAI, true);

		return new Object[]{1};
	}
	
	private PaiMai removePaiMai(long guid,long userRoleId){
		PaiMai paiMai = PaiMaiManage.getPaiMaiManage().removePaiMai(guid); 
		paiMaiDao.delete(guid, userRoleId, AccessType.getDirectDbType());
		roleBagDao.delete(guid, null, AccessType.getDirectDbType());
		return paiMai;
	}
			
	
	/**
	 * 拍卖物品
	 * @param userRoleId
	 * @param guid
	 * @param count
	 * @return
	 */
	public Object[] pmGoodsUp(long userRoleId,long guid,int count,int price){
		RoleWrapper roleWrapper = getOnlineRole(userRoleId);
		if(roleWrapper == null){
			return AppErrorCode.ROLE_OFF;
		} 
		
		Object[] errorCode = checkPaiMaiUpGoods(guid, count, userRoleId,price);
		if(errorCode != null){
			return errorCode;
		}
		
		paiMaiUpGoods(userRoleId,guid, count,price,GoodsSource.PAI_MAI, true, true);
 
		return new Object[]{1};
	}
	
	
	private RoleItemExport paiMaiUpGoods(long userRoleId,long guid, int count,int price, int source, boolean isRecord, boolean isNotify) {
		RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, guid);
		
		RoleItem roleItem = conver2PaiMaiItem(roleItemExport, count, userRoleId);
		
		roleBagExportService.removeBagItemByGuid(guid, count, userRoleId, source, isRecord, isNotify);
		
		int sellType = getSellType(roleItem.getGoodsId());
		
		addPaiMaiGoods(roleItem, price,  count, sellType, userRoleId);
		
		return null;
	}
	
	
	
	private void addPaiMaiGoods(RoleItem roleItem ,int price, int count, int sellType,long userRoleId){
		 
		PaiMaiInfo paiMaiInfo =new PaiMaiInfo();
		paiMaiInfo.setGuid(roleItem.getId());
		paiMaiInfo.setPrice(price); 
		paiMaiInfo.setRoleName(getRoleName(userRoleId)); 
		paiMaiInfo.setSellType(getSellType(roleItem.getGoodsId()));
		paiMaiInfo.setUserRoleId(userRoleId);
		paiMaiInfo.setSellTime(GameSystemTime.getSystemMillTime());
		paiMaiDao.cacheInsert(paiMaiInfo, userRoleId);
		
		//将数据加入数据库
		roleBagDao.insert(roleItem, null, AccessType.getDirectDbType());
		
		PaiMai  paiMai =PaiMaiUtil.coverToPaiMai(paiMaiInfo, roleItem); 
		PaiMaiManage.getPaiMaiManage().addPaiMai(paiMai);
	}
	
	
	
	
	
	/**
	 * 转换成拍卖的物品
	 * @param roleItemExport
	 * @param count
	 * @param userRoleId
	 * @return
	 */
	public RoleItem conver2PaiMaiItem(RoleItemExport roleItemExport ,int count,long userRoleId){

		RoleItem  targetRoleItem = BagUtil.converExport2RoleItem(roleItemExport, userRoleId);
		targetRoleItem.setId(BagUtil.getIdentity());
		if(roleItemExport.getCount() > count){
			targetRoleItem.setCount(count);
		}
		targetRoleItem.setContainerType(ContainerType.PAIMAI.getType());
		return targetRoleItem;
	}


	private Object[] checkPaiMaiUpGoods(long guid, int count, long userRoleId,int price) {
		// 绑定物品不可拍卖
		RoleItemExport  roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, guid);
		if(roleItemExport == null){
			return AppErrorCode.PM_NO_UP;
		}
		GoodsConfig config = goodsConfigExportService.loadById(roleItemExport.getGoodsId());
		Integer sellType = getSellType(roleItemExport.getGoodsId());
		if(config.getBangding() == 1 || config.getDurationDay() !=0 || sellType == null|| count<=0 ){
			return AppErrorCode.PM_NO_UP;
		}
		
		if(price<= 0){
			return AppErrorCode.PM_PRICE_ERROR;
		}
		
		return roleBagExportService.checkRemoveBagItemByGuid(guid, count, userRoleId);
	}
  


	public Integer getSellType(String goodsId){ 
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
		return  shiChangFenLeiConfigExportService.getType(goodsConfig.getJob(), goodsConfig.getEqpart(), goodsConfig.getCategory());
	}
	
	private String getRoleName(long userRoleId){
		return getOnlineRole(userRoleId)==null?"":getOnlineRole(userRoleId).getName();
	}
	
	private RoleWrapper getOnlineRole(long userRoleId){
		return roleExportService.getLoginRole(userRoleId);
	}

	/**
	 * 拍卖行购买
	 * @param userRoleId
	 * @param guid
	 * @return
	 */
	public Object[] pmGoodsGm(Long userRoleId, long guid) {
		 PaiMai paiMai = PaiMaiManage.getPaiMaiManage().getPaiMaiByGuid(guid);
		 if(paiMai == null){
			 return AppErrorCode.PAIMAI_NO_EXIST;
		 }
		 /**
		  * 自己的物品，不可购买
		  */
		 if(paiMai.getUserRoleId().equals(userRoleId)){
			 return AppErrorCode.PAIMAI_SELF_GOODS;
		 }
		 
		 if(paiMai.getPrice() <= 0 ){
			 return AppErrorCode.PAIMAI_ERROR;
		 }
		 
		 Object[] errorCodeGold = roleBagExportService.isEnought(GoodsCategory.GOLD, paiMai.getPrice(), userRoleId);
		 if(errorCodeGold != null){
			 return errorCodeGold;
		 }
		 
		 RoleItemInput  roleItemInput = BagUtil.conver2RoleItemInput(paiMai.getRoleItem());
		 roleItemInput.setNewSlot(true);
		 
		 Object[] errorCodeGoods =  roleBagExportService.checkPutInBag(roleItemInput.getGoodsId(), roleItemInput.getCount(), userRoleId);
		 if(errorCodeGoods != null){
			 return errorCodeGoods;
		 }
		 //删除拍卖行数据
		 removePaiMai(guid, userRoleId);
		 
		 //加入获得者的背包中
		 roleBagExportService.putInBag(roleItemInput, userRoleId, GoodsSource.PAI_MAI_GM, true);
		 roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD,  paiMai.getPrice(), userRoleId, LogPrintHandle.CONSUME_PAIMAI_GM, false, LogPrintHandle.CBZ_PAIMAI_GM);
		//腾讯OSS消费上报
		if(PlatformConstants.isQQ()){
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,paiMai.getPrice(),LogPrintHandle.CONSUME_PAIMAI_GM,QQXiaoFeiType.CONSUME_PAIMAI_GM,1});
		}
		 //发送邮件通知获得了拍卖
		 Map<String, Integer> awards  = new HashMap<String, Integer>();
		 awards.put(ModulePropIdConstant.GOLD_GOODS_ID, paiMai.getPrice());
		  
		 String[] attachments = EmailUtil.getAttachments(awards);
		 GoodsConfig  goodsConfig = goodsConfigExportService.loadById(paiMai.getGoodsId());
		 //String goodsName =  goodsConfig.getName()==null?"":goodsConfig.getName();
		 String title = EmailUtil.getCodeEmail(GameConstants.PAIMAI_CONTENT_CODE_TITLE);
		 String content = EmailUtil.getCodeEmailPm(GameConstants.PAIMAI_CONTENT_CODE,goodsConfig.getId(),paiMai.getCount()+"",paiMai.getPrice()+"");
		 for (String attachment : attachments) {
			emailExportService.sendEmailToOne(paiMai.getUserRoleId(), title,content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
		 }
		try {
			BusMsgSender.send2BusInner(paiMai.getUserRoleId(), InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_SHICHANGMAI, 1});
			//roleChengJiuExportService.tuisongChengJiu(paiMai.getUserRoleId(), GameConstants.CJ_SHICHANGMAI, 1);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		 
		return new Object[]{1};
	}

	/**
	 *  获得拍卖行的物品列表
	 * @param userRoleId
	 * @param bIndex
	 * @param eIndex
	 * @return
	 */
	public Object[] pmGoodsList(Long userRoleId, int bIndex, int eIndex,String selectTarget,int sortType,int pmType) {
		List<PaiMai>  paimains = PaiMaiManage.getPaiMaiManage().getPaimais();
		if(eIndex<bIndex){
			return AppErrorCode.PAIMAI_INDEX_ERROR;
		}
		
		if(paimains == null || paimains.size() <= bIndex){
			return new Object[]{1,null,0};
		}
		
//		List<PaiMai> paimainTemps = new ArrayList<>();
		
		switch (sortType) {
		case GameConstants.PAIMAI_TIMESORT:
			Collections.sort(paimains, new UpTimeComparator());
			break;
		case GameConstants.PAIMAI_SELL_PRICE_UP:
			Collections.sort(paimains, new SellPriceComparator(1));
			break;
		case GameConstants.PAIMAI_SELL_PRICE_DOWN:
			Collections.sort(paimains, new SellPriceComparator(-1));
			break;
		case GameConstants.PAIMAI_PRICE_UP:
			Collections.sort(paimains, new PriceComparator(1));
			break;
		case GameConstants.PAIMAI_PRICE_DOWN:
			Collections.sort(paimains, new PriceComparator(-1));
			break;

		default:
			break;
		}
		
		List<PaiMai> results = filterPaiMais(paimains, selectTarget,bIndex,eIndex,pmType);
		int counts = filterPaiMaisCount(paimains, selectTarget, bIndex, eIndex, pmType);
		Object[]  result = conver2MarketGoodsVO(results);
		
		return new Object[]{1,result,counts};
	}
	
	private int filterPaiMaisCount(List<PaiMai> paiMais ,String selectTarget,int bIndex,int eIndex,int pmType){
		int result =0; 
		if(null ==  paiMais || paiMais.size() ==0 ){
			return result;
		}
		
		for (PaiMai paiMai : paiMais) {
			String goodsId = paiMai.getGoodsId();
			GoodsConfig  goodsConfig = goodsConfigExportService.loadById(goodsId);
			if(selectTarget != null && !selectTarget.trim().equals("") && !goodsConfig.getName().contains(selectTarget)){
				continue;
			}
			
			if(pmType != paiMai.getSellType().intValue()){
				continue;
			}
			result++; 
		}
		return result;
	}
	
	
	private List<PaiMai> filterPaiMais(List<PaiMai> paiMais ,String selectTarget,int bIndex,int eIndex,int pmType){
		List<PaiMai> result = new ArrayList<>();
		if(null ==  paiMais || paiMais.size() ==0 ){
			return result;
		}
		
		int rCount = 0;
		for (PaiMai paiMai : paiMais) {
			String goodsId = paiMai.getGoodsId();
			GoodsConfig  goodsConfig = goodsConfigExportService.loadById(goodsId);
			if(selectTarget != null && !selectTarget.trim().equals("") && !goodsConfig.getName().contains(selectTarget)){
				continue;
			}
			
			if(pmType != paiMai.getSellType().intValue()){
				continue;
			}
			
			if(rCount>=bIndex){
				result.add(paiMai);
			}
			 
			if(++rCount>eIndex){
				break;
			} 
		}
		return result;
	}

	public Object[] pmMyGoodsList(Long userRoleId) {
		
		RoleBusinessInfoWrapper roleBus = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
		if(roleBus == null){
			return AppErrorCode.PAIMAI_ERROR;
		}
		long  cdTime = roleBus.getLastYhTime()== null ||roleBus.getLastYhTime().intValue() == 0 ? 0: roleBus.getLastYhTime() + GameConstants.YH_CD;
		
		List<PaiMai> paimais  = PaiMaiManage.getPaiMaiManage().getPaiMaiByRoleId(userRoleId);
		Object[]  result = conver2MarketGoodsVO(paimais);
		return new Object[]{1,result,cdTime};
	}
	
 
	
	
	/**
	 * 
	 * @param paimais
	 * @return
	 */
	private Object[] conver2MarketGoodsVO(List<PaiMai> paimais){
		if(paimais == null || paimais.size() == 0)return null;
		Object[] result = new Object[paimais.size()];
		for (int i=0;i<paimais.size() ;i++) { 
			PaiMai	paiMai = paimais.get(i);
			result[i] = new Object[]{BagOutputWrapper.getOutWrapperData(OutputType.ITEMVO, paiMai.getRoleItem()),paiMai.getPrice(),paiMai.getRoleName()};
		}
		return result;
	}
	
	/**
	 * 下架所有物品
	 * @param userRoleId
	 * @return
	 */
	public Object[] pmGoodsDownAll(Long userRoleId) {
		
		List<PaiMai> paiMais = PaiMaiManage.getPaiMaiManage().getPaiMaiByRoleId(userRoleId);
		if(paiMais == null || paiMais.size() == 0){
			return AppErrorCode.PAIMAI_NO_EXIST; 
		}
		
		paiMais = new ArrayList<>(paiMais);
		int emptySize = roleBagExportService.getBagEmptySize(userRoleId);
		if(paiMais.size() > emptySize){
			return AppErrorCode.BAG_NOEMPTY;
		}
		
		for (PaiMai paiMai : paiMais) {
			paiMai = removePaiMai(paiMai.getGuid(), userRoleId);
			
			RoleItem roleItem = paiMai.getRoleItem();
			RoleItemInput  roleItemInput = BagUtil.conver2RoleItemInput(roleItem);
			roleItemInput.setNewSlot(true);
			roleBagExportService.putInBag(roleItemInput, userRoleId, GoodsSource.PAI_MAI, true);
		}
		
		return new Object[]{1};
	}
	
	/**
	 * 吆喝
	 * @param userRoleId
	 * @return
	 */
	public Object[] pmGoodsYh(Long userRoleId, long guid) {
		RoleBusinessInfoWrapper  roleWrapper = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
		if(roleWrapper != null){
			if(roleWrapper.getLastYhTime() != null && roleWrapper.getLastYhTime() +GameConstants.YH_CD >GameSystemTime.getSystemMillTime()){
				return AppErrorCode.PAIMAI_YH_CD;
			}
			roleBusinessInfoExportService.updateYHLastTime(userRoleId,GameSystemTime.getSystemMillTime());
		}
		
		//发送吆喝信息 TODO
		
		return new Object[]{1};
	}

	/**
	 * 修改物品价格
	 * @param userRoleId
	 * @param guid
	 * @param price
	 * @return
	 */
	public Object[] pmGoodsModify(Long userRoleId, long guid, int price) {
		 PaiMai paiMai = PaiMaiManage.getPaiMaiManage().getPaiMaiByGuid(guid);
		 if(paiMai == null){
		 	 return AppErrorCode.PAIMAI_NO_EXIST;
		 }
		 
		 /**自己的物品，不可购买 */
		 if(!paiMai.getUserRoleId().equals(userRoleId)){
			 return AppErrorCode.PAIMAI_NO_EXIST;
		 }
		 /**价格检查 */
		 if(price <= 0 ){
			 return AppErrorCode.PM_PRICE_ERROR;
		 }
		 
		 PaiMai newPaiMai = removePaiMai(guid, userRoleId);
		 RoleItem newRI = newPaiMai.getRoleItem();
		 newPaiMai.getRoleItem().setId(BagUtil.getIdentity());
		 newPaiMai.setSellTime(GameSystemTime.getSystemMillTime());
		 
		 addPaiMaiGoods(newRI, price, newRI.getCount(), newPaiMai.getSellType(), userRoleId);
		 		 
		 return new Object[]{1};
	}
}