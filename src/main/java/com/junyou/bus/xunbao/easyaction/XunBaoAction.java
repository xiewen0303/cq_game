package com.junyou.bus.xunbao.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xunbao.service.XunBaoService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message; 

@Component
@EasyWorker(moduleName = GameModType.XUN_BAO)
public class XunBaoAction {

	@Autowired
	private XunBaoService xunbaoService;
	@Autowired
	private XunBaoService xunbaoJifenService;
	
	/**
	 * 打开寻宝界面请求寻宝信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_XB_INFO)
	public void getXunbaoInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		Object[] result = xunbaoService.getXunbaoInfo(userRoleId, busMsgQueue);
		BusMsgSender.send2One(userRoleId,ClientCmdType.GET_XB_INFO,result);
		
		busMsgQueue.flush();
	}
	
	/**
	 * 拉取全部寻宝包裹物品
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_XB_DATA)
	public void getXunbaoBagData(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = xunbaoService.getXunbaoBagData(userRoleId);
		BusMsgSender.send2One(userRoleId,ClientCmdType.GET_XB_DATA,result);
	}
	
	
	/**
	 * 寻宝
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.XUBAO)
	public void xunbao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] obj = inMsg.getData();
		Integer type = (Integer) obj[0];
		String goodsId = (String) obj[1];
		/*Integer type = inMsg.getData();
		String goodsId = "8000";*/
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result = xunbaoService.xunbao(userRoleId, type,goodsId,busMsgQueue);
		
		BusMsgSender.send2One(userRoleId,ClientCmdType.XUBAO,result);
		
		busMsgQueue.flush();
	}
	
	/**
	 * 领取单个物品
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TAKE_OUT_ONE)
	public void takeOutXunbaoBag(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long guid =CovertObjectUtil.obj2long(inMsg.getData());
		
		Object[] result = xunbaoService.takeOutXunbaoBag(userRoleId, guid);
		BusMsgSender.send2One(userRoleId,ClientCmdType.TAKE_OUT_ONE,result);
	}
	
	
	/**
	 * 一键领取
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TAKE_OUT_MANY)
	public void takeOutManyXunbaoBag(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] xbItemIds = inMsg.getData();
		
		Object[] result = xunbaoService.takeOutXunbaoMany(userRoleId,xbItemIds);
		BusMsgSender.send2One(userRoleId,ClientCmdType.TAKE_OUT_MANY,result);
	}
	
	/**
	 * 寻宝积分
	 * 
	 */
	@EasyMapping(mapping = ClientCmdType.GET_XB_JIFEN_FZ_INFO)
	public void getXunBaoJiFenFZInfo(Message inMsg){
		long userRoleId = inMsg.getRoleId();
		
		Object[] result = xunbaoJifenService.getXunBaoJiFenFZInfo(userRoleId);
		BusMsgSender.send2One( userRoleId, ClientCmdType.GET_XB_JIFEN_FZ_INFO, result);
	}
	 
	/**
	 * 寻宝积分
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_XB_JIFEN_INFO)
	public void getXunBaoJiFenInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer fzId = inMsg.getData();
		
		Object[] result = xunbaoJifenService.getXunBaoJiFenInfo(userRoleId, fzId);
		BusMsgSender.send2One( userRoleId, ClientCmdType.GET_XB_JIFEN_INFO, result);
	}
	
	/**
	 * 积分兑换
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.XB_CONVERT)
	public void xunbaoConvert(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		Object[] result = xunbaoJifenService.xunbaoConvert(userRoleId, id, busMsgQueue);
		BusMsgSender.send2One( userRoleId, ClientCmdType.XB_CONVERT, result);
		
		busMsgQueue.flush();
	}

	@EasyMapping(mapping = ClientCmdType.XB_JIFEN)
	public void getXunBaoJF(Message inMsg){
		Long userRoleId = inMsg.getRoleId(); 
		
		int xunBaoJF = xunbaoJifenService.getXunBaoJF(userRoleId);
		BusMsgSender.send2One( userRoleId, ClientCmdType.XB_JIFEN, xunBaoJF);
	} 
	
}
