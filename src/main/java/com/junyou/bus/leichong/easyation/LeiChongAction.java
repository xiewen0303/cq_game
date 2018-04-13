package com.junyou.bus.leichong.easyation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.leichong.server.LeiChongService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 热发布全民修仙运营活动
 * @description 
 * @author ZHONGDIAN
 * @date 2015-5-5 下午4:46:05
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class LeiChongAction {

	@Autowired
	private LeiChongService leiChongService;
	
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_LEICHONG)
	public void lingquLevelLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = leiChongService.lingqu(userRoleId, version, subId, configId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_LEICHONG, result);
		}
	}
	
	
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_DAY_LEICHONG)
	public void lingquDay(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer day = (Integer) data[2];
		
		Object[] result = leiChongService.lingquDay(userRoleId, version, subId, day);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_DAY_LEICHONG, result);
		}
	}
	
	
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_DAY_LEICHONG53GOLD)
	public void lingqu53Gold(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Object[] targetIds = (Object[]) data[2];
		
		Object[] result = leiChongService.lingquFl53(userRoleId, version, subId, targetIds);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_DAY_LEICHONG53GOLD, new Object[]{result[0],result[1],targetIds});
		}
	}
	
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_DAY_LEICHONG53ITEM)
	public void lingqu53Item(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer targetIds =  (Integer)data[2];
		
		Object[] result = leiChongService.lingquFl53(userRoleId, version, subId, new Object[]{targetIds});
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_DAY_LEICHONG53ITEM, new Object[]{result[0],result[1],targetIds});
		}
	}
	
	/**
	 * 请求补签
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_BUQIAN_DAY_LEICHONG)
	public void buqianDay(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer day = (Integer) data[2];
		
		Object[] result = leiChongService.buqian(userRoleId, version, subId, day);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_BUQIAN_DAY_LEICHONG, result);
		}
	}
	
	
}
