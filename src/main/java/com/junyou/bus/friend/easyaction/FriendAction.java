package com.junyou.bus.friend.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.friend.service.FriendService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.public_.tunnel.PublicMsgSender;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

/**
 * 好友功能
 * @description 
 * @author wind
 * @date 2015-1-6 下午6:33:04
 */
@Component
@EasyWorker(moduleName = GameModType.FRIEND_MODULE)
public class FriendAction{

	@Autowired
	private FriendService friendService;
	
	
	@EasyMapping(mapping = ClientCmdType.GET_MEMBERS)
	public void getMembers(Message inMsg) {
		Long roleId = inMsg.getRoleId();
		Integer type = inMsg.getData();
		Object[] result = friendService.getMembers(roleId,type);
		BusMsgSender.send2One(roleId, ClientCmdType.GET_MEMBERS, result);
	}
	
	
	@EasyMapping(mapping = ClientCmdType.ADD_MEMBER)
	public void addMember(Message inMsg) {
		Long roleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer type = (Integer)data[0];
		Long targetRoleId = LongUtils.obj2long(data[1]);
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		Object[] result = friendService.addMember(roleId,type,targetRoleId,busMsgQueue);
		
		busMsgQueue.flush();
		
		BusMsgSender.send2One(roleId, ClientCmdType.ADD_MEMBER, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.DELETE_MEMBER)
	public void delMember(Message inMsg) {
		Long roleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer type = (Integer)data[0];
		Long targetRoleId = LongUtils.obj2long(data[1]);
		
		Object[] result = friendService.delMember(roleId, targetRoleId,type);
		
		BusMsgSender.send2One(roleId, ClientCmdType.DELETE_MEMBER, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.F_SELECT_ROLE)
	public void selectRoleInfo(Message inMsg) {
		Long roleId = inMsg.getRoleId();
		String roleName = inMsg.getData(); 
		
		Object[] result = friendService.selectRoleInfo(roleName,roleId);
		 
		BusMsgSender.send2One(roleId, ClientCmdType.F_SELECT_ROLE, result);
	}
	
	@EasyMapping(mapping = InnerCmdType.ADD_ENEMY)
	public void addEnemylist(Message inMsg) {
		Long roleId = inMsg.getRoleId(); //被干掉的人
		Long enemylistId = CovertObjectUtil.obj2long(inMsg.getData()); //攻击者
		
		Object[] result = friendService.addEnemyLists(roleId, enemylistId);
		if(result != null){
			PublicMsgSender.send2One(roleId, ClientCmdType.ADD_ENEMY, result);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.DEL_FRIENDS)
	public void delFriends(Message inMsg) {
		Long roleId = inMsg.getRoleId();
		
		Object[] datas = inMsg.getData();
		
		Object[] result = friendService.delFriends(roleId, datas);
		PublicMsgSender.send2One(roleId, ClientCmdType.DEL_FRIENDS, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.FRIEND_SET)
	public void friendSet(Message inMsg) {
		Long roleId = inMsg.getRoleId();
		
		Object[] data = (Object[])inMsg.getData();
		
		Boolean isAccept = (Boolean)data[0];
		
		Integer replyState = (Integer)data[1];
		
		
		friendService.friendSet(roleId, isAccept,replyState); 
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_FRIEND_SET)
	public void getFriendSet(Message inMsg) {
		Long roleId = inMsg.getRoleId();
		
		Object[] result = friendService.getFriendSet(roleId);
		
		PublicMsgSender.send2One(roleId, ClientCmdType.GET_FRIEND_SET, result);
	}
}
