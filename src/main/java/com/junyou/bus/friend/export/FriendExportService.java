package com.junyou.bus.friend.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.friend.entity.Friend;
import com.junyou.bus.friend.service.FriendService;

/**
 * 好友
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-3-6 下午4:35:43
 */
@Service
public class FriendExportService {
	
	@Autowired
	private FriendService friendService;
	
	/**
	 * 获得好友
	 * @param userRoleId
	 * @return 
	 */
	public Friend getFriend(long userRoleId){
		return friendService.getFriend(userRoleId);
	}
	
	/**
	 * 是否开启了私聊
	 * @param receiverId
	 * @return
	 */
	public boolean isPrivate(long sendRoleId,Long receiverId) {
		return friendService.isPrivate(receiverId,sendRoleId);
	} 
	/**
	 * 获取列表
	 * @param userRoleId
	 * @param type
	 * @return
	 */
	public Object[] getMembers(Long userRoleId, int type) {
		return friendService.getMembers(userRoleId, type);
	}
}

