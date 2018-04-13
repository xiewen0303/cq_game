package com.junyou.bus.xinwen.easyaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xinwen.service.XinwenService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Component
@EasyWorker(moduleName = GameModType.TANGBAO_XINWEN)
public class XinwenAction {
	
	@Autowired
	private  XinwenService xinwenService;
	
	@EasyMapping(mapping = ClientCmdType.XINWEN_INFO)
	public void getInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = xinwenService.getInfo(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.XINWEN_INFO,ret);
		}
	}
	@EasyMapping(mapping = ClientCmdType.XINWEN_SJ)
	public void yaoshenMowenUpgrade(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		List<Long> itemIds = new ArrayList<Long>();
		for (int i = 0; i < data.length; i++) {
			Long id = LongUtils.obj2long(data[i]);
			if(!itemIds.contains(id)){
				itemIds.add(id);
			}
		}
		Object[] ret = xinwenService.upgrade(userRoleId, itemIds);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.XINWEN_SJ,ret);
		}
	}
	/**
	 * 拉取属性值 ,切换不同等级的时候显示对应属性
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.XINWEN_ATTR_CHANGE)
	public void getXinwenAttrByJie(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int jie = inMsg.getData();
		Map<String, Long> ret = xinwenService.getXinwenAttributeByJie(userRoleId, jie);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.XINWEN_ATTR_CHANGE,ret);
		}
	}

	/**
	 * 使用潜能丹|万能丹
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.XINWEN_QND_USE_NUMBER)
	public void useQND(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		long guid  = LongUtils.obj2long(inMsg.getData());
		Object[] ret = xinwenService.useQND(userRoleId, guid,1);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.XINWEN_QND_USE_NUMBER,ret);
		}
	}
	/**
	 * 使用成长丹|万能丹
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.XINWEN_CZD_USE_NUMBER)
	public void useCZD(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		long guid  = LongUtils.obj2long(inMsg.getData());
		Object[] ret = xinwenService.useCZD(userRoleId, guid,1);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.XINWEN_CZD_USE_NUMBER,ret);
		}
	}

	/**
	 * 自己糖宝心纹阶数
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.XINWEN_JIE_TO_CLIENT)
	public void getJie(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int ret = xinwenService.getXinwenJie(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XINWEN_JIE_TO_CLIENT,ret);
	}
}
