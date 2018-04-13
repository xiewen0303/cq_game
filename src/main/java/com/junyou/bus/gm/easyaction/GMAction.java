package com.junyou.bus.gm.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.gm.service.GMService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.GM_MODULE)
public class GMAction {
	
	@Autowired
	private GMService gmService;
	
	@EasyMapping(mapping = ClientCmdType.GM_CREATE_ITEM)
	public void createItem(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data=inMsg.getData();
		String goodsId=(String)data[0];
		Integer count=(Integer)data[1];
		Integer qhLevel=(Integer)data[2];
		gmService.createItem(userRoleId,goodsId,count,qhLevel); 
	}
	/**
	 * 设置金币
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GM_SET_GOLD)
	public void setGold(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long count = CovertObjectUtil.object2Long(inMsg.getData());
		gmService.setGold(userRoleId, count);
	}
	/**
	 * 设置元宝
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GM_SET_YUANBAO)
	public void setYB(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long count = CovertObjectUtil.object2Long(inMsg.getData());
		gmService.setYB(userRoleId, count);
	}
	/**
	 * 设置绑定元宝
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GM_SET_BANG_YUANBAO)
	public void setBangYB(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long count = CovertObjectUtil.object2Long(inMsg.getData());
		gmService.setBangYB(userRoleId, count);
	}
	
	/**
	 * 增加经验
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GM_ADD_EXP)
	public void addExp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long count = CovertObjectUtil.object2Long(inMsg.getData());
		gmService.addExp(userRoleId, count);
	}
	
	/**
	 * 增加真气
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GM_ADD_ZHENQI)
	public void addZhenqi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Long value = CovertObjectUtil.object2Long(inMsg.getData());
		gmService.addZhenqi(userRoleId, value);
	}
	
	/**
	 * 地图跳转
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GM_GOTO)
	public void gmGoto(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer toMapId = inMsg.getData();
		
		gmService.gmGoto(userRoleId, toMapId);
	}
	/**
	 * 修改任务
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GM_CHANGE_TASK)
	public void changeTask(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer taskId = inMsg.getData();
		
		gmService.changeTask(userRoleId, taskId);
	}
	
	/**
	 * 修改等级
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GM_CHANGE_LEVEL)
	public void changeLevel(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer level = inMsg.getData();
		
		gmService.changeLevel(userRoleId, level);
	}
	/**
	 * 修改VIP等级
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GM_SET_VIP_LEVEL)
	public void setVipLevel(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer level = inMsg.getData();
		
		gmService.setVipLevel(userRoleId, level);
	}
	/**
	 * 增加怒气
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GM_SET_NUQI)
	public void setNuqi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer count = CovertObjectUtil.object2Integer(inMsg.getData());
		gmService.setNuqi(userRoleId, count);
	}
	

    /**
     * 设置魔神精华
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.GM_SET_JINGHUA)
    public void setJinghua(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Long count = CovertObjectUtil.object2Long(inMsg.getData());
        gmService.setJinghua(userRoleId, count);
    }
    
    /**
     * 设置魔神精华
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.GM_ADD_CHARGE)
    public void addCharge(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Long count = CovertObjectUtil.object2Long(inMsg.getData());
        
        gmService.addCharge(userRoleId, count);
    }
	
}
