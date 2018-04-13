package com.junyou.bus.miaosha.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.miaosha.entity.RefbMiaosha;
import com.junyou.bus.miaosha.service.RefbMiaoshaService;

/**
 * @author LiuYu
 * 2016-3-7 上午10:36:26
 */
@Service
public class RefbMiaoshaExportService {
	@Autowired
	private RefbMiaoshaService refbMiaoshaService;
	/**
	 * 玩家登陆加载进缓存
	 * @param userRoleId
	 * @return
	 */
	public List<RefbMiaosha> initRefbMiaosha(Long userRoleId){
		return refbMiaoshaService.initRefbMiaosha(userRoleId);
	}
	/**
	 *  获取某个子活动的热发布某个活动信息
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		return refbMiaoshaService.getRefbInfo(userRoleId, subId);
	}
	/**
	 *  获取某个子活动的状态数据
	 */
	public Object[] getRefbStates(Long userRoleId, Integer subId){
		return refbMiaoshaService.getRefbStates(userRoleId, subId);
	}
}
