/**
 * 
 */
package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.stage.service.StageService;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

/**
 * @description 场景消息接入口
 * @author ShiJie Chi
 * @created 2011-11-24上午9:39:29
 */
@Controller
@EasyWorker(groupName = EasyGroup.STAGE, moduleName = GameModType.STAGE)
public class StageAction {

	@Autowired
	private StageService stageService;
	
	@EasyMapping(mapping = InnerCmdType.S_Enter_Stage_cmd)
	public void enterStage(Message inMsg){
		
		Object[] data = inMsg.getData(); 
		
		Long userRoleId = inMsg.getRoleId();
		String enterStageId = (String)data[0];
		int enterX = (Integer)data[1];
		int enterY = (Integer)data[2];
		
		//1、构造场景角色对象
		//2、进入场景
		stageService.enterStage(userRoleId,enterStageId, enterX, enterY);
	}
	
	@EasyMapping(mapping = InnerCmdType.S_Leave_Stage_cmd)
	public void leaveStage(Message inMsg){
		
		Object[] data = inMsg.getData();
		
		String leaveStageId = (String)data[0];
		Long userRoleId = inMsg.getRoleId();
		
		stageService.leaveStage(leaveStageId,userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.S_LEAVE_AFTER_ENTER_CMD)
	public void leaveAfterEnterStage(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData(); 
		String leaveStageId = (String)data[0];
		String enterStageId = (String)data[1];
		int enterX = (Integer)data[2];
		int enterY = (Integer)data[3];
		
		stageService.leaveAfterEnterStage(userRoleId, leaveStageId, enterStageId, enterX, enterY);
	}
	
	@EasyMapping(mapping = InnerCmdType.DROP_GOODS)
	public void dropGoods(Message inMsg){
		Object[] data = inMsg.getData();
		
		String stageId = inMsg.getStageId();
		Long ownerId = (Long)data[0];
		Integer teamId = (Integer)data[1];
		Integer x = (Integer)data[2];
		Integer y = (Integer)data[3];
		Object[] goods = (Object[])data[4];
		Integer protect = (Integer)data[5];
		Integer disappearDuration = (Integer)data[6];
		
		String monsterId = null;
		if(data.length > 7){
			monsterId = (String)data[7];
		}
		Long monsterGuId = null;
		if(data.length > 8){
			monsterGuId = CovertObjectUtil.object2Long(data[8]);
		}
		
		stageService.goodsEnterStage(stageId,ownerId,teamId,x,y,goods,protect,disappearDuration,monsterGuId,monsterId);
	}
	
	@EasyMapping(mapping = InnerCmdType.PK_DROP_GOODS)
	public void pkGoodsEnterStage(Message inMsg){
		String stageId = inMsg.getStageId();
		
		Object[] data = inMsg.getData();
		
		Long attackId = (Long)data[0];
		Long targetId = (Long)data[1];
		Boolean targetIsRed = (Boolean)data[2];
		
		stageService.pkGoodsEnterStage(stageId, attackId, targetId, targetIsRed);
	}
	
	@TokenCheck(component = GameConstants.COMPONENT_DROP_GOODS)
	@EasyMapping(mapping = InnerCmdType.GOODS_DISAPPEAR)
	public void goodsDisappear(Message inMsg){
		
		Long id = inMsg.getData();
		String stageId = inMsg.getStageId();
		
		stageService.goodsDisappear(id,stageId);
	}
	
}
