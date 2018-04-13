package com.junyou.public_.shichang.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.public_.shichang.service.ShiChangService;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;
 
/**
 * 市场
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-15 下午5:15:01
 */
@Controller
@EasyWorker(moduleName = GameModType.SHI_CHANG,groupName=EasyGroup.PUBLIC)
public class ShiChangAction {
	 	
	@Autowired
	private ShiChangService shiChangSerivce;

	
	/**
	 * 吆喝
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.PM_GOODS_YH)
	public void pmGoodsYh(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		long guid = CovertObjectUtil.obj2long(inMsg.getData());
		
		Object[] result = shiChangSerivce.pmGoodsYh(userRoleId,guid);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.PM_GOODS_YH, result);
		 
	}
	
	/**
	 * 我的物品
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.PM_MY_GOODS_LIST)
	public void pmMyGoodsList(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = shiChangSerivce.pmMyGoodsList(userRoleId);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.PM_MY_GOODS_LIST, result);
		 
	}
	
	/**
	 * 市场列表
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.PM_GOODS_LIST)
	public void pmGoodsList(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] datas = inMsg.getData();
		int pmType = (int) datas[0];
		int bIndex = (int)datas[1];
		int eIndex = (int)datas[2];
		String selectTarget =(String) datas[3];
		int sortType = (int)datas[4];
		
		Object[] result = shiChangSerivce.pmGoodsList(userRoleId,bIndex,eIndex,selectTarget,sortType,pmType);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.PM_GOODS_LIST, result);
		 
	}
	
	
	@EasyMapping(mapping = ClientCmdType.PM_GOODS_UP)
	public void pmGoodsUp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] datas = inMsg.getData();
		long guid = CovertObjectUtil.obj2long(datas[0]);
		int count =(int)datas[1];
		int price = CovertObjectUtil.object2Integer(datas[2]);
		Object[] result=shiChangSerivce.pmGoodsUp(userRoleId,guid,count,price);
		BusMsgSender.send2One(userRoleId, ClientCmdType.PM_GOODS_UP, result);
	}
	
	
	@EasyMapping(mapping = ClientCmdType.PM_GOODS_DOWN)
	public void pmGoodsDown(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		 
		long guid = CovertObjectUtil.obj2long(inMsg.getData()); 
		
		Object[] result=shiChangSerivce.pmGoodsDown(userRoleId,guid);
		BusMsgSender.send2One(userRoleId, ClientCmdType.PM_GOODS_DOWN, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.PM_GOODS_GM)
	public void pmGoodsGm(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		 
		long guid = CovertObjectUtil.obj2long(inMsg.getData()); 
		
		Object[] result=shiChangSerivce.pmGoodsGm(userRoleId,guid);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.PM_GOODS_GM, result);
	}

	
	@EasyMapping(mapping = ClientCmdType.PM_GOODS_DOWN_ALL)
	public void pmGoodsDownAll(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result=shiChangSerivce.pmGoodsDownAll(userRoleId);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.PM_GOODS_DOWN_ALL, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.PM_GOODS_MODIFY)
	public void pmGoodsModify(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] datas = inMsg.getData();
		long guid = CovertObjectUtil.object2Long(datas[0]);
		int price = (int)datas[1];
		
		Object[] result = shiChangSerivce.pmGoodsModify(userRoleId,guid,price);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.PM_GOODS_MODIFY, result);
	}
}
