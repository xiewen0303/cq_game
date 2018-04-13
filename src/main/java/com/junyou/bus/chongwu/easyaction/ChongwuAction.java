package com.junyou.bus.chongwu.easyaction;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.chongwu.service.ChongwuService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.junyou.utils.parameter.RequestParameterUtil;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.CHONGWU, groupName = EasyGroup.BUS_CACHE)
public class ChongwuAction {
	@Autowired
	private ChongwuService chongwuService;

	/**
	 * 请求宠物左侧列表信息
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_CHONGWU_LIST)
	public void getChongwuList(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();

		Object[] result = chongwuService.getChongwuList(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_CHONGWU_LIST,
				result);
	}

	/**
	 * 激活宠物
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ACTIVATE_CHONGWU)
	public void activateChongwu(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Object[] items = (Object[]) data[0];
		Integer configId = (Integer) data[1];
		List<Long> itemIds = new ArrayList<Long>();
		for (int i = 0; i < items.length; i++) {
			Long id = LongUtils.obj2long(items[i]);
			if (!itemIds.contains(id)) {
				itemIds.add(id);
			}
		}
		Object[] result = chongwuService.activateChongwu(userRoleId, configId,
				itemIds);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.ACTIVATE_CHONGWU,
					result);
		}
	}

	    /**
     * 获得宠物具体信息
     * 
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.GET_CHONGWU_DETAIL)
    public void getChongwuDetail(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer configId = inMsg.getData();
        Object[] result = chongwuService.getChongwuDetail(userRoleId, configId);
        if (result != null) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.GET_CHONGWU_DETAIL, result);
        }
    }

	/**
	 * 宠物升阶
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.CHONGWU_UPGRADE_JIE)
	public void chongwuUpgrade(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Object[] items = (Object[]) data[0];
		Integer configId = (Integer) data[1];
		Boolean autoBuy = (Boolean) data[2];
		List<Long> itemIds = new ArrayList<Long>();
		if(items!=null){
			for (int i = 0; i < items.length; i++) {
				Long id = LongUtils.obj2long(items[i]);
				if (!itemIds.contains(id)) {
					itemIds.add(id);
				}
			}
		}
		Object[] result = chongwuService.upgradeJieChongwu(userRoleId,
				configId, itemIds,autoBuy);
		if (result != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.CHONGWU_UPGRADE_JIE, result);
		}
	}

	/**
	 * 宠物出战
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.CHONGWU_FIGHT)
	public void chongwuFight(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer configId = inMsg.getData();
		Object[] result = chongwuService.goFight(userRoleId, configId);
		if (result != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.CHONGWU_FIGHT,
					result);
		}
	}

	/**
     * 宠物升级技能
     * 
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.CHONGWU_SKILL_UPLEVEL)
    public void chongwuUplevelSkill(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        Integer  chongwu_id = RequestParameterUtil.object2Integer(data[0]);
        String  skill_id = RequestParameterUtil.object2String(data[1]);
        Integer  skill_index = RequestParameterUtil.object2Integer(data[2]);
        Boolean auto = RequestParameterUtil.object2Boolean(data[3]);
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        chongwuService.uplevelChongwuSkill(userRoleId, chongwu_id, skill_id, skill_index, auto, busMsgQueue);
        busMsgQueue.flushAndRemove();
    }
}
