package com.junyou.bus.recharge.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.danfuchargerank.vo.DanfuChargeRankVo;
import com.junyou.bus.recharge.service.RechargeService;

/**
 * 充值
 */
@Component
public class RechargeExportService {
	
	@Autowired
	private RechargeService rechargeService;
	
	/**
	 * 充值
	 * @param userId
	 * @param userRoleId
	 * @param serverId
	 * @param platformType
	 * @param orderId
	 * @param reRmb
	 * @param yb
	 */
	public int rechage(String userId, Long userRoleId, String serverId, String platformType, String orderId, Double reRmb, Long yb){
		return rechargeService.rechage(userId, userRoleId, serverId, platformType, orderId, reRmb, yb);
	}
	
	/**
	 * 越南预充值
	 * @param userId
	 * @param userRoleId
	 * @param serverId
	 * @param platformType
	 * @param orderId
	 * @param reRmb
	 * @param yb
	 */
	public int rechageYueNan(String userId, String serverId, String platformType, String orderId, Double reRmb, Long yb){
		return rechargeService.rechageYueNan(userId,  serverId, platformType, orderId, reRmb, yb);
	}
	
	/**
	 * 充值
	 * @param userId
	 * @param userRoleId
	 * @param serverId
	 * @param platformType
	 * @param orderId
	 * @param reRmb
	 * @param yb
	 */
	public int rechageQQ(String serverId,Long userRoleId,String billno,String platformType,String itemXinxi,String amt,String pubacctPayamtCoins){
		return rechargeService.rechageQQ(serverId, userRoleId, billno, platformType, itemXinxi, amt, pubacctPayamtCoins);
	}
	
	/**
	 * 越南预充值
	 * @param userId
	 * @param userRoleId
	 * @param serverId
	 * @param platformType
	 * @param orderId
	 * @param reRmb
	 * @param yb
	 */
	public int rechageYueNan(String serverId,Long userRoleId,String billno,String platformType,String itemXinxi,String amt,String pubacctPayamtCoins){
		return rechargeService.rechageQQ(serverId, userRoleId, billno, platformType, itemXinxi, amt, pubacctPayamtCoins);
	}
	/**
	 * 后台充值
	 * @param userId
	 * @param serverId
	 * @param reType
	 * @param orderId
	 * @param reRmb
	 * @param yb
	 */
	public int webRechage(Long userRoleId,Integer reType, String orderId, Double reRmb, Long yb){
		return rechargeService.webRechage(userRoleId, reType, orderId, reRmb, yb);
	}
	/**
	 * 上线处理充值到账
	 * @param userRoleId
	 */
	public void onlineCalRecharge(Long userRoleId){
		rechargeService.onlineHandlefinishRecharge(userRoleId);
	}
	
	/**
	 * 获取时间段内充值总和
	 * @param userRoleId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public int getTotalRechargesByTime(long userRoleId,long startTime,long endTime){
		return rechargeService.getTotalRechargesByTime(userRoleId, startTime, endTime);
	}
	
	/**
	 * 获取单服充值排行
	 * @param startTime
	 * @param endTime
	 * @param limit
	 * @param minCharge
	 * @return
	 */
	public List<DanfuChargeRankVo> getTotalRechargesByTime(long startTime,long endTime,int limit,int minCharge){
		return rechargeService.getTotalRechargesByTime( startTime, endTime,limit,minCharge);
	}
	/**
	 * 获取总充值额
	 * @return
	 */
	public long getTotalRechargeSum(){
		return rechargeService.getTotalRechargeSum();
	}
	/**
	 * 获取时间区间内总充值额
	 * @return
	 */
	public long getTimeRechargeSum(long startTime,long endTime){
		return rechargeService.getTimeRechargeSum(startTime, endTime);
	}
}
