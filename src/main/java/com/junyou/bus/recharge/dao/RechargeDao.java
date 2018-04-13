package com.junyou.bus.recharge.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.danfuchargerank.vo.DanfuChargeRankVo;
import com.junyou.bus.recharge.entity.Recharge;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.utils.datetime.DatetimeUtil;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.accessor.GlobalIdentity;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RechargeDao extends BusAbsCacheDao<Recharge> implements IDaoOperation<Recharge> {
	/**创建充值记录直接访问库**/
	public void insertRechargeFromDb(Recharge recharge){
		insert(recharge, recharge.getId(), AccessType.getDirectDbType());
	}
	
	/**获取该订单编号的所有充值记录**/
	public List<Recharge> getRechargeFormDbByOrderId(String orderId){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("reStates", AppErrorCode.RECHARGE_SUCCESS + GameConstants.SQL_WHERE_INT_JOIN + AppErrorCode.RECIVE_SUCCESS );
		queryParams.put("orderId", orderId);
		
		return getRecords(queryParams, GlobalIdentity.get(), AccessType.getDirectDbType());
	}
	
	/**获取该玩家的可领取的充值记录**/
	public List<Recharge> getEnableRecharges(Long userRoleId){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		queryParams.put("reState", AppErrorCode.RECHARGE_SUCCESS);
		
		return getRecords(queryParams, GlobalIdentity.get(), AccessType.getDirectDbType());
	}
	
	/**获取该玩家的可领取的充值记录**/
	public List<Recharge> getEnableRecharges(String userId,String serverId){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userId", userId);
		queryParams.put("serverId", serverId);
		queryParams.put("reState", AppErrorCode.RECHARGE_SUCCESS);
		
		return getRecords(queryParams, GlobalIdentity.get(), AccessType.getDirectDbType());
	}
	/**获取该玩家的一段时间的充值记录**/
	@SuppressWarnings("unchecked")
	public int getTotalRechargesByTime(long userRoleId,long startTime,long endTime) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		queryParams.put("reState1", AppErrorCode.RECIVE_SUCCESS);
		queryParams.put("reState2", AppErrorCode.RECHARGE_SUCCESS);
		String startTimeStr = DatetimeUtil.formatTime(startTime, DatetimeUtil.FORMART3);
		queryParams.put("startTime", startTimeStr);
		String endTimeStr = DatetimeUtil.formatTime(endTime, DatetimeUtil.FORMART3);
		queryParams.put("endTime", endTimeStr);
		List<Long> list = query("selectRechargeByTime", queryParams);
		if(list ==null || list.size() ==0){
			return 0;
		}
		if(list.get(0)==null){
			return 0;
		}
		return list.get(0).intValue();
	}
	/**获取一段时间的充值排行记录**/
	@SuppressWarnings("unchecked")
	public List<DanfuChargeRankVo> getTotalRechargesByTime(long startTime,long endTime,int limit,int minCharge) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("limit", limit);
		queryParams.put("reState1", AppErrorCode.RECIVE_SUCCESS);
		queryParams.put("reState2", AppErrorCode.RECHARGE_SUCCESS);
		String startTimeStr = DatetimeUtil.formatTime(startTime, DatetimeUtil.FORMART3);
		queryParams.put("startTime", startTimeStr);
		String endTimeStr = DatetimeUtil.formatTime(endTime, DatetimeUtil.FORMART3);
		queryParams.put("endTime", endTimeStr);
		queryParams.put("minCharge", minCharge);
		return query("selectAllRechargeByTime", queryParams);
	}
	
	/**
	 * 获取总充值额
	 * @return
	 */
	public long getTotalRechargeSum(){
		List<Long> list = query("allRechargeSum", null);
		if(list == null || list.size() == 0){
			return 0;
		}
		return list.get(0);
	}
	
	/**
	 * 获取时间区间内总充值额
	 * @return
	 */
	public long getTimeRechargeSum(long startTime,long endTime){
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("startTime", startTime);
		queryParams.put("endTime", endTime);
		List<Long> list = query("selectTimeRechargeSum", queryParams);
		if(list == null || list.size() == 0){
			return 0;
		}
		return list.get(0);
	}
}