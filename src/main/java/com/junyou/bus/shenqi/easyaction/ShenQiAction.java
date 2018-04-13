package com.junyou.bus.shenqi.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.shenqi.service.ShenQiService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.SHEN_QI, groupName = EasyGroup.BUS_CACHE)
public class ShenQiAction {

	@Autowired
	private ShenQiService shenQiService;
	/**
	 * 获得神器激活信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_SHENQI_INFO)
	public void getShenQiInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();

		Object[] result = shenQiService.getShenqiInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_SHENQI_INFO, result);
	}
	/**
	 * 激活神器
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ACTIVATE_SHENQI)
	public void activateShenqi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer shenqiId = inMsg.getData();
		Object[] result = shenQiService.activateShenqi(userRoleId, shenqiId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ACTIVATE_SHENQI, result);
	}
	/**
	 * 佩戴神器
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.WEAR_SHENQI)
	public void wearShenqi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer shenqiId = inMsg.getData();
		Object[] result = shenQiService.wearShenqi(userRoleId, shenqiId);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.WEAR_SHENQI, result);
		}
	}
	/**
	 * 获取累计签到天数
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_TOTAL_ASSIGN_DAYS)
	public void getTotalAssignDays(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] result = shenQiService.getTotalAssignDays(userRoleId);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TOTAL_ASSIGN_DAYS, result);
		}
	}
	/**
	 * 获取累计在线
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_TOTAL_ONLINE_TIME)
	public void getTotalOnline(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		long ret = shenQiService.getTotalOnlineTime(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TOTAL_ONLINE_TIME, ret);
	}
	
	/**
	 * 是否已激活可购买神器
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.IS_ACITVE_BUY_SHENQI)
	public void isAcitveBuyShenQi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean ret = shenQiService.isAcitveBuyShenQi(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.IS_ACITVE_BUY_SHENQI, ret);
	}
	/**
	 * 购买开服神器
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.BUY_KAIFU_SHENQI)
	public void kfBuyShenQi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = shenQiService.kfBuyShenQi(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.BUY_KAIFU_SHENQI, ret);
	}
	/**
	 * 神器洗练
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SHENQI_XILIAN)
	public void shenQiXiLian(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data  = inMsg.getData();
		int id = (Integer)data[0];
		int consumeType =  (Integer)data[1];
		int autoType =  (Integer)data[2];
		Object[] lockAttr  = (Object[])data[3];
		Object[] ret = shenQiService.xiLian(userRoleId, id, consumeType,autoType, lockAttr);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SHENQI_XILIAN, ret);
	}
	/**
	 * 保存洗练属性
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SHENQI_XILIAN_SAVE)
	public void shenQiXiLianSave(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int id = inMsg.getData();
		Object[] ret = shenQiService.xiLianSaveOrGiveUp(userRoleId,id,1);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SHENQI_XILIAN_SAVE, ret);
	}
	/**
	 * 不保存洗练属性
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SHENQI_XILIAN_CANCEL)
	public void shenQiXiLianGiveUp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int id = inMsg.getData();
		Object[] ret = shenQiService.xiLianSaveOrGiveUp(userRoleId,id,0);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SHENQI_XILIAN_CANCEL, ret);
	}
	
	
	
}
