package com.junyou.bus.marry.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.marry.entity.RoleMarryInfo;
import com.junyou.bus.marry.service.MarryService;
import com.junyou.log.ChuanQiLog;

/**
 * @author LiuYu
 * 2015-8-12 下午3:56:30
 */
@Service
public class MarryExportService {
	@Autowired
	private MarryService marryService;
	
	
	public List<RoleMarryInfo> initRoleMarryInfo(Long userRoleId) {
		return marryService.initRoleMarryInfo(userRoleId);
	}
	
	/**
	 * 填充结婚属性
	 * @param userRoleId
	 * @param xinwuAtt	信物属性
	 * @param lfAtt		龙凤属性
	 */
	public void putJieHunAtt(Long userRoleId,Map<String,Long> xinwuAtt,Map<String,Long> lfAtt){
		marryService.putJieHunAtt(userRoleId, xinwuAtt, lfAtt);
	}
	
	public void  onlineHandle(Long userRoleId) {
		try{
			marryService.onlineHandle(userRoleId);
		}catch (Exception e) {
			ChuanQiLog.error("上线业务异常，",e);
		}
	}
	public void  offlineHandle(Long userRoleId) {
		try{
			marryService.offlineHandle(userRoleId);
		}catch (Exception e) {
			ChuanQiLog.error("下线业务异常，",e);
		}
	}
	
	/**
	 * 获取配偶userRoleId，没有为null
	 * @param userRoleId
	 * @return
	 */
	public Long getPeiouUserRoleId(Long userRoleId){
		return marryService.getPeiouUserRoleId(userRoleId);
	}
	
	/**
	 * 获取婚姻情况，没有为null(当前玩家需在线)
	 * @param userRoleId
	 * @return [2]配偶名字， [7]结婚信物
	 */
	public Object[] getMarryInfo(Long userRoleId){
		return marryService.getMarryInfo(userRoleId);
	}
	
	public void zhuanZhiJieChuJieHun(Long userRoleId){
		marryService.zhuanZhiJieChuJieHun(userRoleId);
	}
	public void zhuanZhiCancelDing(Long userRoleId){
		marryService.zhuanZhiCancelDing(userRoleId);
	}
	public Integer getUserRoleJieHunStat(Long userRoleId){
		return marryService.getUserRoleJieHunStat(userRoleId);
	}
}
