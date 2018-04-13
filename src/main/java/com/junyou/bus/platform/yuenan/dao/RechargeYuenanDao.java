package com.junyou.bus.platform.yuenan.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.platform.yuenan.entity.RechargeYuenan;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.err.AppErrorCode;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.accessor.GlobalIdentity;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RechargeYuenanDao extends BusAbsCacheDao<RechargeYuenan> implements IDaoOperation<RechargeYuenan> {

	/**创建充值记录直接访问库**/
	public void insertRechargeYuenanFromDb(RechargeYuenan recharge){
		insert(recharge, recharge.getId(), AccessType.getDirectDbType());
	}
	
	/**获取该订单编号的所有充值记录**/
	public List<RechargeYuenan> getRechargeFormDbByOrderId(String orderId){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("reState", AppErrorCode.RECHARGE_SUCCESS);
		queryParams.put("orderId", orderId);
		
		return getRecords(queryParams, GlobalIdentity.get(), AccessType.getDirectDbType());
	}
	
}