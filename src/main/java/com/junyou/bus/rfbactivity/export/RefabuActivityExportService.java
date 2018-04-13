package com.junyou.bus.rfbactivity.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.rfbactivity.configure.export.ActivityConfig;
import com.junyou.bus.rfbactivity.entity.RoleYuanbaoRecord;
import com.junyou.bus.rfbactivity.service.RefabuActivityService;
import com.junyou.bus.rfbactivity.service.RoleYuanbaoRecordService;

/**
 * 热发布活动对外服务类
 * @author DaoZheng Yuan
 * 2015年5月21日 下午1:22:02
 */
@Component
public class RefabuActivityExportService {

	@Autowired
	private RefabuActivityService refabuActivityService;
	@Autowired
	private RoleYuanbaoRecordService roleYuanbaoRecordService;
	/**
	 * 获取子活动实例业务数据根据子活动类型
	 * @return
	 */
	public Object getSubHandleDataBySubType(Long userRoleId,Integer subId,int type){
		return refabuActivityService.getSubHandleDataBySubType(userRoleId, subId, type);
	}
	
	/**
	 * 充值元宝热发布监听器业务
	 * @param userRoleId
	 * @param subId
	 * @param addYb
	 * @param rechargeData 
	 */
	public void chargeYbRefbMonitorHandle(Long userRoleId,Long addYb, Map<String, Long[]> rechargeData){
		refabuActivityService.chargeYbRefbMonitorHandle(userRoleId, addYb,rechargeData);
	}
	/**
	 * 消费元宝热发布监听器业务
	 * @param userRoleId
	 * @param subId
	 * @param addYb
	 */
	public void xfYbRefbMonitorHandle(Long userRoleId,Long yb){
		refabuActivityService.xfYbRefbMonitorHandle(userRoleId, yb);
	}
	
	/**
	 * 获取今日的充值消费记录
	 * @param userRoleId
	 */
	public RoleYuanbaoRecord getRoleYuanbaoRecord(Long userRoleId){
		return roleYuanbaoRecordService.getRoleYuanBaoRecord(userRoleId);
	}
	
	public List<RoleYuanbaoRecord> initRoleYuanbaoRecords(Long userRoleId){
		return roleYuanbaoRecordService.initRoleYuanbaoRecords(userRoleId);
	}
	
	public boolean isShowActivity(Long userRoleId, ActivityConfig activity){
	    return refabuActivityService.isShowActivity(userRoleId, activity);
	}
}
