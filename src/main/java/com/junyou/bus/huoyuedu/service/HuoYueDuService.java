package com.junyou.bus.huoyuedu.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.branchtask.BranchEnum;
import com.junyou.bus.branchtask.service.TaskBranchService;
import com.junyou.bus.huoyuedu.configure.HuoYueDuAwardConfig;
import com.junyou.bus.huoyuedu.configure.HuoYueDuAwardConfigExportService;
import com.junyou.bus.huoyuedu.configure.HuoYueDuContentConfig;
import com.junyou.bus.huoyuedu.configure.HuoYueDuContentConfigExportService;
import com.junyou.bus.huoyuedu.dao.RoleHuoyueduDao;
import com.junyou.bus.huoyuedu.entity.RoleHuoyuedu;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.HuoyueduRewardLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.nodecontrol.service.NodeControlService;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;

/**
 * 活跃度
 * 
 * @author lxn
 * 
 */
@Service
public class HuoYueDuService {
	@Autowired
	private RoleHuoyueduDao roleHuoyueduDao;

	@Autowired
	private HuoYueDuAwardConfigExportService huoYueDuAwardConfigExportService;
	@Autowired
	private HuoYueDuContentConfigExportService huoYueDuContentConfigExportService;

	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private NodeControlService nodeControlService;
	@Autowired
	private TaskBranchService taskBranchService;
	
	/**
	 * 上线后调用逻辑
	 * 
	 * @param userRoleId
	 * @throws InterruptedException
	 */
	public void onlineHandle(Long userRoleId) {

		// 如果是微端登陆完成A1任务
		if(nodeControlService.isWeiDuanLogin(userRoleId)){
			completeActivity(userRoleId, ActivityEnum.A1);
		}
		// 玩家登陆完成后如果VIP>=5自动完成 A13任务
		RoleVipWrapper roleVipWrapper = roleVipInfoExportService.getRoleVipInfo(userRoleId);
		HuoYueDuContentConfig contentConfig = this.huoYueDuContentConfigExportService.loadPublicConfig(ActivityEnum.A13.getActivityId());
		if(contentConfig==null){
			return ;
		}
		if (roleVipWrapper.getVipLevel() >= contentConfig.getData()) {
			completeActivity(userRoleId, ActivityEnum.A13);
		}

	}

	/**
	 * 获取玩家活跃度面板信息
	 */
	public Object[] getTaskInfo(Long userRoleId) {
		RoleHuoyuedu roleHuoyuedu = getRoleHuoyuedu(userRoleId);
		Object[] result = new Object[] { 1, null, 0 };
		JSONObject json = roleHuoyuedu.getJson();
		result[1] = json.size() == 0 ? null : json;
		result[2] = roleHuoyuedu.getAward();
 		return result;
	}

	/**
	 * 积分到了领取奖励
	 */
	public Object[] getAward(Long userRoleId, int awardId) {
		// 判断积分是否满了
		RoleHuoyuedu roleHuoyuedu = getRoleHuoyuedu(userRoleId);
		if (roleHuoyuedu == null) {
			return AppErrorCode.HUOYUEDU_DATA_ERROR; // 数据异常
		}
		HuoYueDuAwardConfig awardVo = this.huoYueDuAwardConfigExportService.loadPublicConfig(awardId);

		int jifen = this.countJifen(roleHuoyuedu);
		if (jifen < awardVo.getJifen()) {
			// 重新推一边数据给客户端 矫正
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_HUOYUEDU_INFO, getTaskInfo(userRoleId));
			return AppErrorCode.HUOYUEDU_JIFEN_NOT_ENOUGHT; // 数据异常活跃度积分不够不能领取
		}
		// 判断背包是否已满
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(awardVo.getItems(), userRoleId);
		// 背包空间不足
		if (code != null) {
			return code;
		}
		if (!BitOperationUtil.calState(roleHuoyuedu.getAward(), awardId - 1)) {
			// 已领取
			return AppErrorCode.HUOYUEDU_GET_AWARD_ERROR;
		}
		// 更新状态
		Integer newState = BitOperationUtil.chanageState(roleHuoyuedu.getAward(), awardId - 1);
		roleHuoyuedu.setAward(newState);
		roleHuoyueduDao.cacheUpdate(roleHuoyuedu, userRoleId);
		
		taskBranchService.completeBranch(userRoleId, BranchEnum.B6, 1);
		
		//****进背包****
		roleBagExportService.putGoodsAndNumberAttr(awardVo.getItems(), userRoleId, GoodsSource.GOODS_HUOYUEDU,
				LogPrintHandle.GET_HUOYUEDU_LB, LogPrintHandle.GBZ_HUOYUEDU_LB, true);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(awardVo.getItems(), null);
		GamePublishEvent.publishEvent(new HuoyueduRewardLogEvent(userRoleId, jsonArray));

		return new Object[] { 1, awardId };
	}
	

	/**
	 * 任务总数是1次的 活动触发事件处理逻辑,或者注入对应的模块
	 */
	public void completeActivity(Long userRoleId, ActivityEnum acEnum) {
		completeActivity(userRoleId, acEnum, 1);
	}

	/**
	 * 任务总数大于1的 活动触发事件处理逻辑,或者注入对应的模块
	 * 
	 * @param userRoleId
	 * @param acEnum
	 * @param num
	 *            完成了几次任务
	 */
	public void completeActivity(Long userRoleId, ActivityEnum acEnum, Integer num) {

		try {
			if(num == null || num <= 0){
				ChuanQiLog.error("***HuoYueDuService.completeActivity,ERROR:num={},ActivityId={},userRoleId={}***", num,acEnum.getActivityId(), userRoleId);
				return;
			}
			
			HuoYueDuContentConfig contentConfig = this.huoYueDuContentConfigExportService.loadPublicConfig(acEnum.getActivityId());
			if (contentConfig == null) {
				ChuanQiLog.error("***HuoYueDuService.completeActivity,ERROR:num={},ActivityId={},userRoleId={},contentConfig is null!***", num,acEnum.getActivityId(), userRoleId);
				return;
			}

			RoleHuoyuedu roleHuoyuedu = getRoleHuoyuedu(userRoleId);

			JSONObject roleJson = roleHuoyuedu.getJson();
			Integer roleCompleteNum = 0; // 检查玩家之前做了几次
			if (roleJson.get(acEnum.getActivityId().toString()) != null) {
				roleCompleteNum = (Integer) roleJson.get(acEnum.getActivityId().toString());
			}
			if (roleCompleteNum >= contentConfig.getNeedNum()) {
				return;// 这项任务今日已经完成
			}

			Integer countNum = roleCompleteNum + num;
			if (countNum > contentConfig.getNeedNum()) {
				countNum = contentConfig.getNeedNum(); // 结果不能超过任务总完成数
			}
			roleJson.put(acEnum.getActivityId().toString(), countNum);
			roleHuoyuedu.setJson(roleJson);
			roleHuoyueduDao.cacheUpdate(roleHuoyuedu, userRoleId);
			//一些前端拿不到数据的后端推送给客户端
			notice2client(acEnum,userRoleId);
			
		} catch (Exception e) {
			ChuanQiLog.error("***HuoYueDuService,ERROR:num={},ActivityId={},userRoleId={},errorInfo={}***", num,acEnum.getActivityId(), userRoleId,e.getMessage());
		}
	}

	/**
	 * 获取RoleHuoyuedu对象
	 * @param userRoleId
	 * @return
	 */
	private RoleHuoyuedu getRoleHuoyuedu(Long userRoleId) {
		RoleHuoyuedu roleHuoyuedu = roleHuoyueduDao.cacheAsynLoad(userRoleId, userRoleId);
		if(roleHuoyuedu == null){
			//创建
			roleHuoyuedu = new RoleHuoyuedu();
			Timestamp time = new Timestamp(GameSystemTime.getSystemMillTime());
			roleHuoyuedu.setCreateTime(time);
			roleHuoyuedu.setUpdateTime(GameSystemTime.getSystemMillTime());
			roleHuoyuedu.setUserRoleId(userRoleId);
			roleHuoyuedu.setAward(0);
			roleHuoyueduDao.cacheInsert(roleHuoyuedu, userRoleId);
		}else if (!DatetimeUtil.dayIsToday(roleHuoyuedu.getUpdateTime())) {// 跨天了
			roleHuoyuedu.setJson(null);// 纠正数据  
			roleHuoyuedu.setUpdateTime(GameSystemTime.getSystemMillTime());
			roleHuoyuedu.setAward(0);
			roleHuoyueduDao.cacheUpdate(roleHuoyuedu, userRoleId);
		}
		return roleHuoyuedu;
	}
	/**
	 * 推送客户端获取不到的活动数据
	 */
	private void notice2client(ActivityEnum activityEnum,Long roleId){
//		 if(activityEnum == ActivityEnum.A17){
			 BusMsgSender.send2One(roleId, ClientCmdType.GET_HUOYUEDU_INFO, getTaskInfo(roleId)); 
//		 }
	}
	 
	/**
	 * 计算积分
	 */
	private int countJifen(RoleHuoyuedu roleHuoyuedu) {
		if (roleHuoyuedu == null) {
			return 0;
		}
		JSONObject object = roleHuoyuedu.getJson();
		int jifen = 0;
		for (Map.Entry<String, Object> map : object.entrySet()) {
			Integer id = Integer.valueOf(map.getKey());
			Integer value = (Integer) map.getValue();
			HuoYueDuContentConfig vo = this.huoYueDuContentConfigExportService.loadPublicConfig(id); // 需要完成的总数
			if(vo==null){
				continue;
			}
			if (value == vo.getNeedNum()) {
				jifen += vo.getJifen(); // 累计积分
			}
		}
		return jifen;
	}

}
