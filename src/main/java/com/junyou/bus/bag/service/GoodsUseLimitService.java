package com.junyou.bus.bag.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.dao.RoleItemUseCsxzDao;
import com.junyou.bus.bag.entity.RoleItemUseCsxz;
import com.junyou.bus.bag.filter.PropGoodsIdFilter;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.DaoJuXzConfigExportService;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午3:38:12
 *@Description: 物品每日次数使用限制
 */

@Service
public class GoodsUseLimitService{
	@Autowired
	private RoleItemUseCsxzDao roleItemUseCsxzDao; 
	@Autowired 
	private DaoJuXzConfigExportService daoJuXzConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
//	@Autowired
//	private IVipInfoExportService vipInfoExportService;
	
	/**
	 * 初始化
	 * @param userRoleId
	 * @return
	 */
	public List<RoleItemUseCsxz> initAll(long userRoleId){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId); 
		return roleItemUseCsxzDao.getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	
	/**
	 * key: goodsId  value:已经使用次数
	 * @param userRoleId
	 * @return
	 */
	public Map<Integer,Integer> getItemUseXzcsInfo(long userRoleId){
		List<RoleItemUseCsxz> rolePropRemenbers = roleItemUseCsxzDao.cacheLoadAll(userRoleId);
		Map<Integer, Integer> result=null;
		if(rolePropRemenbers!=null){
			result=new HashMap<Integer, Integer>();
			for (RoleItemUseCsxz roleItemUseCsxz : rolePropRemenbers) {
				int useCount=getUseCount(roleItemUseCsxz);
				if(useCount==0)continue;
				result.put(roleItemUseCsxz.getXzId(), useCount);
			}
		}
		return result;
	}
	
	/**
	 * key: goodsId  value:已经使用次数
	 * @param userRoleId
	 * @return
	 */
	public Integer getItemUseXzcsInfo(long userRoleId,String goodsId){
		GoodsConfig goodsConfig=goodsConfigExportService.loadById(goodsId);
		int xianZhiId=goodsConfig.getXianZhiId();
		List<RoleItemUseCsxz> rolePropRemenbers = roleItemUseCsxzDao.cacheLoadAll(userRoleId,new PropGoodsIdFilter(xianZhiId));
		if(rolePropRemenbers!=null&&rolePropRemenbers.size()>0){
			RoleItemUseCsxz roleXzcs=rolePropRemenbers.get(0);
			return getUseCount(roleXzcs);
		}
		return 0;
	}
	
	/**
	 * 检测道具记录使用次数
	 * @param userRoleId
	 * @param goodsId 物品Id
	 * @param count 要使用的数量
	 * @return    null表示ok  其他的表示有错误errorCode
	 */
	public Object[] checkXzcsUse(long userRoleId,String goodsId,int count){
		int useCount=getItemUseXzcsInfo(userRoleId, goodsId);
		GoodsConfig goodsConfig=goodsConfigExportService.loadById(goodsId);
		int xianZhiId=goodsConfig.getXianZhiId();
		/**
		 *  测试代码，需要实际的vipLevel  TODO
		 */
		int vipLevel=0;
		
		if(xianZhiId!=0){
			Integer canUCount=daoJuXzConfigExportService.getLimitUseCount(xianZhiId, vipLevel);
//			canUCount=10;
			if(canUCount==null){
				return AppErrorCode.NO_FIND_CONFIG;
			}
			if(!canUCount.equals(0)&&canUCount<useCount+count){
				return AppErrorCode.NO_USE_COUNT;
			}
		}
		
		return null;
	}
	
	/**
	 * 使用道具记录使用次数
	 * @param userRoleId
	 * @param goodsId
	 * @param count
	 */
	public void useXzcsRecord(long userRoleId,String goodsId,int count){
		GoodsConfig goodsConfig=goodsConfigExportService.loadById(goodsId);
		int xzId = goodsConfig.getXianZhiId();
		if(xzId==0)return;
		List<RoleItemUseCsxz> roleItemUseCsxzs=roleItemUseCsxzDao.cacheLoadAll(userRoleId, new PropGoodsIdFilter(xzId));
		RoleItemUseCsxz roleItemUseCsxz=null;
		if(roleItemUseCsxzs!=null&&roleItemUseCsxzs.size()>0){
			roleItemUseCsxz=roleItemUseCsxzs.get(0);
			roleItemUseCsxz.setLastUseTime(GameSystemTime.getSystemMillTime());
			roleItemUseCsxz.setUseCount(count+getUseCount(roleItemUseCsxz));
			roleItemUseCsxzDao.cacheUpdate(roleItemUseCsxz, userRoleId);
		}else{
			roleItemUseCsxz=new RoleItemUseCsxz();
			roleItemUseCsxz.setXzId(xzId);
			long nowTime=GameSystemTime.getSystemMillTime();
			roleItemUseCsxz.setCreateTime(nowTime);
			roleItemUseCsxz.setUseCount(count);
			roleItemUseCsxz.setUserRoleId(userRoleId);
			roleItemUseCsxz.setLastUseTime(nowTime);
			roleItemUseCsxz.setId(BagUtil.getOhterIdentity());
			roleItemUseCsxzDao.cacheInsert(roleItemUseCsxz, userRoleId);
		}
		HashMap<Integer, Integer> result=new HashMap<>();
		result.put(roleItemUseCsxz.getXzId(), roleItemUseCsxz.getUseCount());
		BusMsgSender.send2One(userRoleId, ClientCmdType.NOTITY_CSXZ, result);
	}
	
	/**
	 * 获得今日已使用次数
	 * @param roleItemUseCsxz
	 * @return
	 */
	private int getUseCount(RoleItemUseCsxz roleItemUseCsxz){
		long dayBeginTime=DatetimeUtil.getDate00Time();
		if(roleItemUseCsxz.getLastUseTime()<=dayBeginTime){
			return 0;
		}
		return roleItemUseCsxz.getUseCount();
	}
}