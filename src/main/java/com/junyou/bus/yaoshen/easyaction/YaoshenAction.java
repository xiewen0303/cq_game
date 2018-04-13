package com.junyou.bus.yaoshen.easyaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.yaoshen.service.YaoshenHunPoService;
import com.junyou.bus.yaoshen.service.YaoshenMoYinService;
import com.junyou.bus.yaoshen.service.YaoshenService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.YAOSHEN_MODULE)
public class YaoshenAction {
	@Autowired
	private YaoshenService yaoshenService;
	@Autowired
	private YaoshenHunPoService yaoshenHunPoService;
	@Autowired
	private YaoshenMoYinService yaoshenMoYinService;
	
	@EasyMapping(mapping = ClientCmdType.GET_YAOSHEN_INFO)
	public void getYaoshenInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = yaoshenService.getYaoshenInfo(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_YAOSHEN_INFO,
					ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.ACTIVATE_YAOSHEN)
	public void activateYaoshen(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Object[] items = (Object[]) data[0];
		Boolean useGold = (Boolean) data[1];
		List<Long> itemIds = new ArrayList<Long>();
		for (int i = 0; i < items.length; i++) {
			Long id = LongUtils.obj2long(items[i]);
			if(!itemIds.contains(id)){
				itemIds.add(id);
			}
		}
		Object[] ret = yaoshenService.activeYaoshen(userRoleId, itemIds,
				useGold);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.ACTIVATE_YAOSHEN,
					ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.YAOSHEN_UPGRADE)
	public void yaoshenUpgrade(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		List<Long> itemIds = new ArrayList<Long>();
		for (int i = 0; i < data.length; i++) {
			Long id = LongUtils.obj2long(data[i]);
			if(!itemIds.contains(id)){
				itemIds.add(id);
			}
		}
		Object[] ret = yaoshenService.upgrade(userRoleId, itemIds);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_UPGRADE,ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.YAOSHEN_CHANGE_SHOW)
	public void yaoshenChangeShow(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = yaoshenService.changeYaoshenShow(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,ClientCmdType.YAOSHEN_CHANGE_SHOW, ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.YAOSHEN_ATTR_CHANGE)
	public void yaoshenGetAttr(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Map<String, Long> ret = yaoshenService.getYaoshenAttribute(userRoleId,true);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.YAOSHEN_ATTR_CHANGE, ret);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_MOWEN_INFO)
	public void yaoshenMowenInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = yaoshenService.getMowenInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOWEN_INFO,ret);
	}
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_MOWEN_UP)
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
		Object[] ret = yaoshenService.mowenUpgrade(userRoleId, itemIds);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOWEN_UP,ret);
		}
	}
	
	/**
	 * 拉取属性值 切页
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_MOWEN_ATTR_CHANGE)
	public void getMowenAttr(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int jie = inMsg.getData();
		Map<String, Long> ret = yaoshenService.getYaoshenMowenAttributeByJie(userRoleId, jie);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOWEN_ATTR_CHANGE,ret);
		}
	}
	
	/**
	 * 使用妖神霸体成长丹
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_BATI_CZ)
	public void batiCzd(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long guid = LongUtils.obj2long(inMsg.getData());
		Object[] result  = yaoshenService.batiUseCzdNew(userRoleId, guid);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_BATI_CZ, result);
		}
	}
	
	/**
	 * 使用妖神霸体潜能丹
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_BATI_QN)
	public void  batiQnd(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long guid = LongUtils.obj2long(inMsg.getData());
		Object[] result  = yaoshenService.batiUseQndNew(userRoleId, guid);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_BATI_QN, result);
		}
	}
	
	
	/**
	 * 使用妖神魔纹成长丹
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_MOWEN_CZ)
	public void mowenCzd(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long guid = LongUtils.obj2long(inMsg.getData());
		Object[] result  = yaoshenService.mowenUseCzdNew(userRoleId, guid);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOWEN_CZ, result);
		}
	}
	
	/**
	 * 使用妖神魔纹潜能丹
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_MOWEN_QN)
	public void mowenQnd(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long guid = LongUtils.obj2long(inMsg.getData());
		Object[] result  = yaoshenService.mowenUseQndNew(userRoleId, guid);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOWEN_QN, result);
		}
	}
	
	
	/**
	 * 使用妖神魂魄成长丹
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_HUNPO_CZ)
	public void hunpoCzd(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long guid = LongUtils.obj2long(inMsg.getData());
		Object[] result  = yaoshenHunPoService.hunpoUseCzdNew(userRoleId, guid);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_CZ, result);
		}
	}
	
	/**
	 * 使用妖神魂魄潜能丹
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_HUNPO_QN)
	public void hunpoQnd(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long guid = LongUtils.obj2long(inMsg.getData());
		Object[] result  = yaoshenHunPoService.hunpoUseQndNew(userRoleId, guid);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_QN, result);
		}
	}
	
	/**
	 * 使用妖神魔印成长丹
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_MOYING_CZ)
	public void moyingCzd(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long guid = LongUtils.obj2long(inMsg.getData());
		Object[] result  = yaoshenMoYinService.moyingUseCzdNew(userRoleId, guid);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOYING_CZ, result);
		}
	}
	
	/**
	 * 使用妖神魔印潜能丹
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_MOYING_QN)
	public void moyingQnd(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long guid = LongUtils.obj2long(inMsg.getData());
		Object[] result  = yaoshenMoYinService.moyingUseQndNew(userRoleId, guid);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOYING_QN, result);
		}
	}
}
