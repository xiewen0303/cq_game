package com.junyou.bus.shizhuang.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.shizhuang.entity.RoleShiZhuangJinJie;
import com.junyou.bus.shizhuang.entity.RoleShizhuang;
import com.junyou.bus.shizhuang.service.RoleShiZhuangService;
import com.junyou.log.ChuanQiLog;

/**
 * @author LiuYu
 * 2015-8-7 下午7:36:10
 */
@Service
public class RoleShiZhuangExportService {

	@Autowired
	private RoleShiZhuangService roleShiZhuangService;
	
	public List<RoleShizhuang> initRoleShizhuang(Long userRoleId) {
		return roleShiZhuangService.initRoleShizhuang(userRoleId);
	}
	
	public List<RoleShiZhuangJinJie> initRoleShizhuangJinJie(Long userRoleId) {
		return roleShiZhuangService.initRoleShizhuangJinJie(userRoleId);
	}
	
	public Integer getAttribute(Long userRoleId,Map<String,Long> activeAtt,Map<String,Long> levelAtt,Map<String,Long> jinJieAtt){
		return roleShiZhuangService.getAttribute(userRoleId, activeAtt, levelAtt,jinJieAtt);
	}
	public void onlineHandle(Long userRoleId){
		try{
			roleShiZhuangService.onlineHandle(userRoleId);
		}catch (Exception e) {
			ChuanQiLog.error("上线业务异常，",e);
		}
	}
	
	public Object[] activeXianshiShizhuang(Long userRoleId,int time,int id){
		return roleShiZhuangService.activeXianshiShizhuang(userRoleId, time, id);
	}
	
	public Object[] zhuanZhiShiZhuangZhuanHuan(Long userRoleId){
		return roleShiZhuangService.zhuanZhiShiZhuangZhuanHuan(userRoleId);
	}
}
