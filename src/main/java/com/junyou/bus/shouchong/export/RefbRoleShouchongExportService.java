package com.junyou.bus.shouchong.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.shouchong.entity.RefbRoleShouchong;
import com.junyou.bus.shouchong.service.RefbRoleShouchongService;

/**
 * @author DaoZheng Yuan
 * 2015年5月19日 下午9:25:23
 */
@Component
public class RefbRoleShouchongExportService {
	
	@Autowired
	private RefbRoleShouchongService refbRoleShouchongService;
	
	public List<RefbRoleShouchong> initRefbRoleShouchong(Long userRoleId){
		return refbRoleShouchongService.initRefbRoleShouchong(userRoleId);
	}
	
	/**
	 * 根据子活动获取累计充值元宝金额
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object getRechargeValueBySubId(Long userRoleId,int subId){
		return refbRoleShouchongService.getRechargeValueBySubId(userRoleId, subId);
	}
	
	/**
	 * 获取首充数据
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object getShouChongRechargeInfo(Long userRoleId,int subId){
		return refbRoleShouchongService.getShouChongRechargeInfo(userRoleId, subId);
	}
	
	/**
	 * 获取当前是否有首充活动
	 * @return true:有首充,false:无首充
	 */
	public boolean huoquShouchong(Long userRoleId,int subId){
		return refbRoleShouchongService.huoquShouchong(userRoleId, subId);
	}
	
	/**
	 * 充值成功后，增加活动的累积元宝金额
	 * @param userRoleId
	 * @param addVal
	 */
	public void changeRecharge(Long userRoleId, Long addVal){
		refbRoleShouchongService.changeRecharge(userRoleId, addVal);
	}
	
	/**
	 * 获取每日充值数据
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object getLoopDayChongZhiRechargeInfo(Long userRoleId,int subId){
		return refbRoleShouchongService.getLoopDayChongZhiRechargeInfo(userRoleId, subId);
	}
}
