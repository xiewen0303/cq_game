package com.junyou.bus.bag.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.bag.BagContants;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.service.RoleBagService;
import com.junyou.bus.bag.service.UsePropService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-12-9下午6:37:57
 *@Description:
 */
@Controller
@EasyWorker(moduleName = GameModType.BAG_MODULE)
public class BagAction {

	@Autowired
	private RoleBagService roleBagService;
	@Autowired
	private UsePropService usePropService;
 
	@EasyMapping(mapping = ClientCmdType.GET_STORAGE_ITEMS)
	public void getStorageItems(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result=roleBagService.getStorageItems(userRoleId);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_STORAGE_ITEMS, result);	
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.BAG_ZL)
	public void clearUpBag(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result=roleBagService.clearUpBag(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.BAG_ZL, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.STORAGE_ZL)
	public void clearUpStorage(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result=roleBagService.clearUpStorage(userRoleId);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.STORAGE_ZL, result);
	}
	
	@EasyMapping(mapping=ClientCmdType.MOVE_SLOT_ID)
	public void moveGoods2Slot(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data=inMsg.getData(); 
		long goodsPkId=LongUtils.obj2long(data[0]);
		Integer targetSlot = (Integer)data[1];
		Object[] result=roleBagService.moveGoods2Slot(userRoleId,targetSlot,goodsPkId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.MOVE_SLOT_ID, result);
	}
	
	//绑定与非绑定的合并到同一个格位中区
	@EasyMapping(mapping=ClientCmdType.MOVE_SLOT_ID1)
	public void moveGoodsID1ToSlot(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data=inMsg.getData(); 
		long goodsPkId = LongUtils.obj2long(data[0]);
		Integer targetSlot = (Integer)data[1];
		Object[] result = roleBagService.moveGoodsID1ToSlot(userRoleId,targetSlot,goodsPkId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.MOVE_SLOT_ID1, result);
	}
 
	@EasyMapping(mapping=ClientCmdType.CHAIFEN_ITEM)
	public void chaifenItem(Message inMsg){
		Long userRoleId = inMsg.getRoleId(); 
		Object[] data=inMsg.getData(); 	
		long goodsPkId=LongUtils.obj2long(data[0]); 
		int targetCount=(Integer)data[1];
		int targetSlot=(Integer)data[2];
		Object[] result=roleBagService.splitGoods(userRoleId,goodsPkId,targetCount,targetSlot);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHAIFEN_ITEM, result);
	}
	
	@EasyMapping(mapping=ClientCmdType.USE_BAG_ITEM)
	public void useBagItem(Message inMsg){
		
		Long userRoleId = inMsg.getRoleId();
		Object[] data=inMsg.getData(); 	
		Long goodsPkId = CovertObjectUtil.obj2long(data[0]);
		Integer count=(Integer)data[1];
		
		Object[] result=usePropService.useBagItem(userRoleId, goodsPkId, count);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.USE_ITEM_FAIL, result);	
		} 
	}
	
	@EasyMapping(mapping=ClientCmdType.USE_BAG_ONE_ITEM)
	public void useBagOneItem(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data=inMsg.getData(); 	
		long goodsPkId=LongUtils.obj2long(data[0]); 
		
		Object[] result=usePropService.useBagItem(userRoleId, goodsPkId, 1);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.USE_ITEM_FAIL, result);	
		}
	}
	
	
	@EasyMapping(mapping=ClientCmdType.BAG_GIVE_UP,kuafuType = EasyKuafuType.KFING_STOP_TYPE)
	public void bagGiveUp(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		long guid=LongUtils.obj2long(inMsg.getData()); 
		
		Object[] result=roleBagService.bagGiveUp(userRoleId,guid);
		BusMsgSender.send2One(userRoleId, ClientCmdType.BAG_GIVE_UP, result);
	}
	
	@EasyMapping(mapping=ClientCmdType.BAG_DESTROY_ITEM)
	public void bagDestroyItem(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		long guid=LongUtils.obj2long(inMsg.getData()); 
		
		Object[] result=roleBagService.bagDestroyItem(userRoleId,guid);
		BusMsgSender.send2One(userRoleId, ClientCmdType.BAG_DESTROY_ITEM, result);
	}
	
	/**
	 * 使用元宝激活背包格位
	 * @param context
	 */
	@EasyMapping(mapping = ClientCmdType.BAG_SLOT_ACTIVATION)
	public void bagSlotActivation(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer openSlot = (Integer)data[0];
		Integer gold = (Integer)data[1];
		
		Object[] result = null;
		if(openSlot >= BagContants.START_BAG_SLOT &&openSlot <=BagContants.END_BAG_SLOT){
			 result = roleBagService.bagSlotActivation(userRoleId,openSlot,gold);
		}else if(openSlot <= BagContants.END_STORAGE_SLOT && openSlot >= BagContants.START_STORAGE_SLOT){
			result = roleBagService.storageSlotActivation(userRoleId,openSlot,gold);
		}
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.BAG_SLOT_ACTIVATION,result );
	}
	 
	@EasyMapping(mapping = InnerCmdType.TAKE_GOODS,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE) 
	public void takeGoods(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		RoleItemInput roleItemInput=(RoleItemInput)inMsg.getData();
		
		Object[] result = roleBagService.takeGoods(userRoleId,roleItemInput);
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.GOODS_PICKUP, result[1]);
		}
	}
	
	@EasyMapping(mapping = InnerCmdType.TAKE_GOODS_KUAFU,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE) 
	public void takeGoodsKuafu(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] goods = (Object[])inMsg.getData();
		
		roleBagService.takeGoodsKF(userRoleId,goods);
	}
	
	
	@EasyMapping(mapping = ClientCmdType.SELL_ITEM) 
	public void sellItem(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		long guid = CovertObjectUtil.obj2long(inMsg.getData());
		
		Object[] result = roleBagService.sellItem2Npc(userRoleId,guid);
		 
		BusMsgSender.send2One(userRoleId, ClientCmdType.SELL_ITEM, result);
	}
	
	/**
	 * 领取宝箱奖励
	 * @param msg
	 */
	@EasyMapping(mapping = ClientCmdType.LINGQU_LIXIANG) 
	public void lingQuBaoXiang(Message inMsg) {
		
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		Integer paixu = (Integer) data[0];
		Long guId = CovertObjectUtil.obj2long(data[1]);

		
		Object[] result = usePropService.lingQuBaoXiang(userRoleId,guId,paixu);
		
		BusMsgSender.send2One(userRoleId,ClientCmdType.LINGQU_LIXIANG,result);
	}
	
	/**
	 * 客户端请求物品展示
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GOODS_ZHANSHI) 
	public void goodsZhanshi(Message inMsg){
		Object[] data=inMsg.getData(); 
		Long looker = inMsg.getRoleId();
		Long id = CovertObjectUtil.obj2long(data[0]);
		Long userRoleId = CovertObjectUtil.obj2long(data[1]);
		
		Object[] result=roleBagService.goodsZhanshi(userRoleId,id);
		if(result!=null){
			BusMsgSender.send2One(looker, ClientCmdType.GOODS_ZHANSHI, result);
		}
	}
}