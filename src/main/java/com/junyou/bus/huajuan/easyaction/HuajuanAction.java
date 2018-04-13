package com.junyou.bus.huajuan.easyaction;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.huajuan.service.HuajuanService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.HUAJUAN, groupName = EasyGroup.BUS_CACHE)
public class HuajuanAction {

	@Autowired
	private HuajuanService huajuanService;

	/**
	 * 画卷合成
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.HUAJUAN_HECHENG)
	public void huajuanHecheng(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer id = CovertObjectUtil.object2Integer(data[0]);
		Integer num = CovertObjectUtil.object2Integer(data[1]);
		Object[] result = huajuanService.hecheng(userRoleId,id, num);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN_HECHENG,
					result);
		}
	}

	/**
	 * 获得画卷信息
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.HUAJUAN_GET_INFO)
	public void getHuajuanInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();

		Object[] result = huajuanService.getHuajuanInfo(userRoleId);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN_GET_INFO,
					result);
		}
	}

	/**
	 * 穿画卷
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.HUAJUAN_UP)
	public void huajuanUp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long huajuanId = CovertObjectUtil.object2Long(inMsg.getData());
		Object[] result = huajuanService.up(userRoleId, huajuanId);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN_UP, result);
		}
	}

	/**
	 * 卸载画卷
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.HUAJUAN_DOWN)
	public void huajuanDown(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long huajuanId = CovertObjectUtil.object2Long(inMsg.getData());
		Object[] result = huajuanService.down(userRoleId, huajuanId);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN_DOWN,
					result);
		}
	}

	/**
	 * 分解画卷
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.HUAJUAN_FENJIE)
	public void huajuanFenjie(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		List<Long> itemList = null;
		Object[] data = inMsg.getData();
		if (data != null && data.length > 0) {
			itemList = new ArrayList<Long>();
			for (Object e : data) {
				itemList.add(CovertObjectUtil.object2Long(e));
			}
		}
		Object[] result = huajuanService.fenjie(userRoleId, itemList);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN_FENJIE,
					result);
		}
	}
	/**
	 * 画卷升级	
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.HUAJUAN_SHENGJI)
	public void huajuanShengJi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Long huajuanId = CovertObjectUtil.object2Long(data[0]);
		Object[] item = (Object[]) data[1];
		List<Long> itemIds = null;
		if (item != null && item.length>0) {
			itemIds = new ArrayList<Long>();
			for (int i = 0; i < item.length; i++) {
				Long id = LongUtils.obj2long(item[i]);
				if (!itemIds.contains(id)) {
					itemIds.add(id);
				}
			}
		}
		Object[] ret = huajuanService.sjHuaJuan(userRoleId, huajuanId,itemIds);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN_SHENGJI,ret);
		}
	}
	/**
	 * 画卷库存经验值
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TUISONG_HUAJUAN_KUCUN_EXP)
	public void huajuanKuCun(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer exp = huajuanService.getKuCunExp(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TUISONG_HUAJUAN_KUCUN_EXP,exp);
	}
}
