package com.junyou.bus.xiuxian.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.xiuxian.entity.RefbRoleXiuxian;
import com.junyou.bus.xiuxian.service.RfbXiuXianService;

/**
 * 修仙礼包
 * @author DaoZheng Yuan
 * 2015年6月7日 下午3:43:56
 */
@Component
public class RfbXiuXianExportService {

	@Autowired
	private RfbXiuXianService rfbXiuXianService;

	
	public List<RefbRoleXiuxian> initRefbRoleXiuxian(Long userRoleId){
		return rfbXiuXianService.initRefbRoleXiuxian(userRoleId);
	}
	
	public Object getXiuXianHandleDataBySubType(Long userRoleId,int subId){
		return rfbXiuXianService.getXiuXianInfoData(userRoleId,subId);
	}
	
	/**
	 * 获取活动的状态数据
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object getXiuXianStateDataBySubId(Long userRoleId,int subId){
		return rfbXiuXianService.getXiuXianStateDataBySubId(userRoleId, subId);
	}
}
