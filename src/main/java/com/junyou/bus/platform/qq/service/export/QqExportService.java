package com.junyou.bus.platform.qq.service.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.qq.entity.RolePlatformQqHz;
import com.junyou.bus.platform.qq.entity.RolePlatformQqLz;
import com.junyou.bus.platform.qq.entity.RoleQdianZhigou;
import com.junyou.bus.platform.qq.entity.RoleQqTgp;
import com.junyou.bus.platform.qq.entity.TencentLanzuan;
import com.junyou.bus.platform.qq.entity.TencentWeiduan;
import com.junyou.bus.platform.qq.service.QQTgpNengLiangService;
import com.junyou.bus.platform.qq.service.QqChargeService;
import com.junyou.bus.platform.qq.service.QqHuangZuanGiftService;
import com.junyou.bus.platform.qq.service.QqLanZuanGiftService;
import com.junyou.bus.platform.qq.service.QqService;
import com.junyou.bus.platform.qq.service.TencentWeiDuanService;

@Service
public class QqExportService {
	@Autowired
	private QqHuangZuanGiftService qqHuangZuanGiftService;
	@Autowired
	private QqLanZuanGiftService qqLanZuanGiftService;
	@Autowired
	private QqService qqService;
	@Autowired
	private TencentWeiDuanService tencentWeiDuanService;
	@Autowired
	private QQTgpNengLiangService qqTgpNengLiangService;
	@Autowired
	private QqChargeService qqChargeService;
	
	public RolePlatformQqHz initRolePlatformQqHz(Long userRoleId) {
		return qqHuangZuanGiftService.initRolePlatformQqHz(userRoleId);
	}
	public RolePlatformQqLz initRolePlatformQqLz(Long userRoleId) {
		return qqLanZuanGiftService.initRolePlatformQqLz(userRoleId);
	}
	public List<TencentLanzuan> initTencentLanzuans(Long userRoleId){
		return qqLanZuanGiftService.initTencentLanzuans(userRoleId);
	}
	public Map<Integer,Object> getRoleQQPlatformInfo(Long userRoleId,boolean notifyClient) {
		return qqService.getRoleQQInfo(userRoleId,notifyClient);
	}
	public Map<Integer,Object> getRoleQQPlatformInfoNeicun(Long userRoleId) {
		return qqService.getRoleQQPlatformInfoNeicun(userRoleId);
	}
	
	public List<TencentWeiduan> iniTencentWeiduan(Long userRoleId){
		return tencentWeiDuanService.initTencentWeiduan(userRoleId);
	}
	
	public List<RoleQqTgp> initRoleQqTgps(Long userRoleId){
		return qqTgpNengLiangService.initRoleQqTgps(userRoleId);
	}
	
	public List<RoleQdianZhigou> initRoleQdianZhigous(Long userRoleId){
		return qqChargeService.initRoleQdianZhigous(userRoleId);
	}
	public void setRoleQdianCount(Long userRoleId,String goodsId){
		qqChargeService.setUserBuyCount(userRoleId, goodsId);
	}
	/**
	 * 角色下线处理，删除角色蓝黄钻信息
	 * @param userRoleId
	 */
	public void offlineReVipInfo(Long userRoleId){
		qqService.removeBuleVipInfo(userRoleId);
	}
	/**
	 * 更新开通蓝钻信息
	 * @return
	 */
	public int updateTenCentLanZuan(String userId,String serverId){
		return qqLanZuanGiftService.updateTenCentLanZuan(userId, serverId);
	}
}
