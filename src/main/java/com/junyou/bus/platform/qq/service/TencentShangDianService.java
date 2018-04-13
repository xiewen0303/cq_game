package com.junyou.bus.platform.qq.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.qq.confiure.export.LanZuanZheKouShopConfig;
import com.junyou.bus.platform.qq.confiure.export.LanZuanZheKouShopConfigService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.dao.TencentShangdianDao;
import com.junyou.bus.platform.qq.entity.TencentShangdian;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author zhongdian
 * 2015-12-16 下午2:34:57
 */
@Service
public class TencentShangDianService {

	@Autowired
	private TencentShangdianDao tencentShangdianDao;
	@Autowired
	private LanZuanZheKouShopConfigService lanZuanZheKouShopConfigService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private QqService qqService;
	public List<TencentShangdian> initTencentShangdians(Long userRoleId){
		return tencentShangdianDao.initTencentShangdian(userRoleId);
	}
	
	private TencentShangdian getTencentShangdian(Long userRoleId){
		List<TencentShangdian> tsdList = tencentShangdianDao.cacheAsynLoadAll(userRoleId);
		if(tsdList == null || tsdList.size() <= 0){
			TencentShangdian sd = new TencentShangdian();
			sd.setUserRoleId(userRoleId);
			sd.setBuyId("");
			sd.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			sd.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			
			tencentShangdianDao.cacheInsert(sd, userRoleId);
			return sd;
		}
		
		TencentShangdian sd = tsdList.get(0);
		if(!DateUtils.isSameDay(sd.getUpdateTime(), new Timestamp(GameSystemTime.getSystemMillTime()))){
			sd.setBuyId("");
			tencentShangdianDao.cacheUpdate(sd, userRoleId);
		}
		return sd;
	}
	
	public Object[] getInfo(Long userRoleId){
		TencentShangdian  sd = getTencentShangdian(userRoleId);
		if(sd.getBuyId() == null || "".equals(sd.getBuyId())){
			return null;
		}
		List<Object[]> sdList = new ArrayList<>();
		String[] str = sd.getBuyId().split(",");
		for (int i = 0; i < str.length; i++) {
			sdList.add(str[i].split(":"));
		}
		return sdList.toArray();
		
	}
	
	public Object[] buy(Long userRoleId,int id){
		
		Map<Integer, Object> lanZuanInfo = qqService.getRoleQQInfo(userRoleId,true);
		if(lanZuanInfo == null){
			return AppErrorCode.QQ_ARGS_ERROR;
		}
		LanZuanZheKouShopConfig shangDian = lanZuanZheKouShopConfigService.loadById(id);
		if(shangDian == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(shangDian.getType().intValue() == 1){
			Integer lanzuanLevel = (Integer) lanZuanInfo.get(QqConstants.QQ_PLATFORM_INFO_LAN_ZUAN_LEVEL_KEY);
			if(lanzuanLevel == null || lanzuanLevel.intValue() == 0){
				return AppErrorCode.QQ_NO_LANZUAN_BUY;
			}
		}else if(shangDian.getType().intValue() == 2){
			boolean lanzuanLevel = (boolean) lanZuanInfo.get(QqConstants.QQ_PLATFORM_INFO_LAN_ZUAN_NIANFEI_KEY);
			if(!lanzuanLevel){
				return AppErrorCode.QQ_NO_LANZUAN_BUY;
			}
		}
		TencentShangdian smsd = getTencentShangdian(userRoleId);
		if(isBuyGoodByConfigId(smsd.getBuyId(), shangDian.getId(),shangDian.getCount())){
			return AppErrorCode.NOT_BUY_COUNT;
		}
		
		//元宝是否足够
		Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD,shangDian.getNeedgold(), userRoleId);
		if(null != goldError){ 
			return goldError;
		}
		
		Map<String, Integer> goodMap = new HashMap<String, Integer>();
		goodMap.put(shangDian.getItemid(), 1);
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(goodMap, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		//消耗货币
		if(shangDian.getNeedgold() > 0 ){
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, shangDian.getNeedgold(), userRoleId, LogPrintHandle.CONSUME_TENCENT_SHANGDIAN, true,LogPrintHandle.CBZ_TENCENT_SHANGDIAN);
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,shangDian.getNeedgold(),LogPrintHandle.CONSUME_TENCENT_SHANGDIAN,QQXiaoFeiType.CONSUME_LANZUAN_SHANGDIAN,1});
			}
		}
		//物品进背包
		roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.TENCENT_SHANGDIAN, LogPrintHandle.GET_TENCENT_SHANGDIAN, LogPrintHandle.GBZ_TENCENT_SHANGDIAN, true);
		
		//增加购买的物品
		smsd.setBuyId(setBuyGood(smsd.getBuyId(), shangDian.getId()));
		smsd.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		tencentShangdianDao.cacheUpdate(smsd, userRoleId);
		
		return new Object[]{1,id};
	}
	
	/**
	 * 判断购买过这个配置ID的物品及数量
	 * @return
	 */
	private boolean isBuyGoodByConfigId(String buyId,int configId,int count){
		if(buyId == null || "".equals(buyId)){
			return false;
		}
		String[] buy = buyId.split(",");
		for (int i = 0; i < buy.length; i++) {
			String[] goods = buy[i].split(":");
			if(configId == Integer.parseInt(goods[0]) && count <= Integer.parseInt(goods[1])){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 增加购买的物品
	 * @param buyId
	 * @param configId
	 * @return
	 */
	private String setBuyGood(String buyId,int configId){
		if(buyId == null || "".equals(buyId)){
			return configId+":"+1;
		}else{
			String[] buy = buyId.split(",");
			int cc = 0;
			List<String[]> list = new ArrayList<>();
			for (int i = 0; i < buy.length; i++) {
				String[] goods = buy[i].split(":");
				if(configId == Integer.parseInt(goods[0])){//存在记录，已经买过
					cc = 1;
					goods = new String[]{goods[0],Integer.parseInt(goods[1])+1+""};
					/*buyId = buyId.replace(buy[i], "");
					return buyId +","+configId + ":" + (Integer.parseInt(goods[1])+1);*/
				}
				list.add(goods);
			}
			if(cc == 0){
				list.add(new String[]{configId+"",1+""});
			}
			String re = "";
			for (int i = 0; i < list.size(); i++) {
				String[] g = list.get(i);
				if("".equals(re)){
					re += g[0]+":"+g[1];
				}else{
					re += ","+g[0]+":"+g[1];
				}
			}
			return re;
		}
		
	}
	
	
	
	
}
