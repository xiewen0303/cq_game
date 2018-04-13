package com.junyou.bus.friend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.friend.FriendOutputWrapper;
import com.junyou.bus.friend.dao.FriendDao;
import com.junyou.bus.friend.entity.Friend;
import com.junyou.bus.friend.util.FriendUtil;
import com.junyou.bus.friend.vo.FriendVo;
import com.junyou.bus.platform.qq.service.export.QqExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.setting.export.RoleSettingExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.FriendPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.number.LongUtils;
 
/**
 * 好友server
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-3-2 下午2:48:36
 */
@Service
public class FriendService{

	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private FriendDao friendDao;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private GuildExportService guildExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private RoleSettingExportService roleSettingExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private QqExportService qqExportService;
	/**
	 * 获取好友公共配置数据
	 * @description
	 */
	private FriendPublicConfig getFriendPublicConfig(){
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_FRIEND);
	}
	
	public Object[] getMembers(Long userRoleId, int type) {
		List<FriendVo> resultList=null;
		
		Friend friend = getFriend(userRoleId);
		
		switch (type) {
		case GameConstants.F_FRIEND:
			// 好友
			resultList = friendDao.getFriendsByParams(friend.getFriendIds());

			break;
		case GameConstants.F_COU:
			// 仇人
			resultList = friendDao.getFriendsByParams(friend.getEnemyDatas());

			break;
		case GameConstants.F_HEI:
			// 黑名单
			resultList = friendDao.getFriendsByParams(friend.getBlackIds());

			break;

		default:
			
			return AppErrorCode.FRIEND_TYPE_ERROR;
		}
		
		return getFriendsData(resultList,type,userRoleId);
	}
	
	public Object[] getFriendsData(List<FriendVo> resultList, Integer type,long userRoleId){
		Object[] friendDatas = new Object[]{};
		if(resultList != null){
			friendDatas = new Object[resultList.size()];
			int index = 0;
			for (FriendVo friendVo : resultList) {
				if(friendVo != null){
					friendDatas[index++] = getSingleFriend(friendVo,userRoleId);
				}
			}
		}
		
		return new Object[]{1,type, friendDatas};
	}
	
	/**
	 * 
	 * @param friendVo
	 * @param type  GameConstants 1:仇人,
	 * @return
	 */
	public Object[] getSingleFriend(FriendVo friendVo,long userRoleId){
		
		ArrayList<Object> result=new ArrayList<>();
		long friendId = friendVo.getId();
		result.add(friendVo.getConfigId());
		result.add(friendId);
		result.add(friendVo.getName());
		result.add(friendVo.getLevel());
		boolean online = publicRoleStateExportService.isPublicOnline(friendId);
		int vip = 0;
		try{
			RoleVipWrapper roleVipWrapper = null;
			if(online){
				roleVipWrapper = roleVipInfoExportService.getRoleVipInfo(friendId);
			}else{
				roleVipWrapper = roleVipInfoExportService.getRoleVipInfoFromDB(friendId);
			}
			vip = roleVipWrapper.getVipLevel();
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		result.add(vip);
		result.add(online);
		result.add(guildExportService.getGuildName(friendId));
		result.add(friendVo.getLastOfflineTime());

		Friend friend =	getFriend(userRoleId);
    	Map<Long, Integer> eDatas =	friend.getEnemyDatas();
    	if(eDatas!= null){
    		result.add(eDatas.get(friendId));	
    	}else{
    		result.add(null);
    	}
     
    	result.add(getZhanli(friendId,online)); 
    	if(PlatformConstants.isQQ()){
    		result.add(qqExportService.getRoleQQPlatformInfoNeicun(friendVo.getId()));
    	}
	    
	    return  result.toArray();
	}
	
	public long getZhanli(long userRoleId,boolean online){
		// 战力获取
		try{
			RoleBusinessInfoWrapper roleBusinessInfoWrapper = null;
			if(online){
				roleBusinessInfoWrapper = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
			}else{
				roleBusinessInfoWrapper = roleBusinessInfoExportService.getRoleBusinessInfoForDB(userRoleId);
			}
			if(roleBusinessInfoWrapper != null){
				return roleBusinessInfoWrapper.getCurFighter();
			}
		}catch (Exception e) {
		}
		
		return 0;
	}
	
	/**
	 * 通过userRoleId去缓存中取自己的好友数据
	 * @param userRoleId
	 * @return
	 */
	public Friend getFriend(Long userRoleId){
		Friend friend = friendDao.cacheAsynLoad(userRoleId, userRoleId);
		if(friend != null){
			return friend;
		}else{
			return friendCreate(userRoleId);
		}
	}
	/**
	 * 没有自己的好友数据就新建一条
	 * @param userRoleId
	 * @return
	 */
	private Friend friendCreate(Long userRoleId){
		Friend friend = new Friend();
		friend.setUserRoleId(userRoleId);
		friend.setCreateTime(GameSystemTime.getSystemMillTime());
		friend.setIsAccept(0);
		friend.setReplyState(GameConstants.F_NO_HUIFU);
		friendDao.cacheInsert(friend, userRoleId);
		return friend;
	}
	
/*	*//**
	 * 如果缓存中有数据就先从缓存中取，缓存中没有再去数据库取
	 * @param userRoleId
	 * @return
	 *//*
	private RoleWrapper getRoleWrapper(Long userRoleId){
		RoleWrapper roleWrapper = null;
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			roleWrapper = roleExportService.getLoginRole(userRoleId);
		}else{
			roleWrapper = roleExportService.getUserRoleFromDb(userRoleId);
		}
		
		return roleWrapper;
	}  
*/	

	public Object[] addMember(Long roleId, Integer type, Long targetRoleId,BusMsgQueue publicMsgQueue) {
		Friend friend = getFriend(roleId);
		String rStr =null;
		String cStr =null;
		int maxMember = 0; 
		
		switch (type) {
		case GameConstants.F_FRIEND:
			rStr = friend.getFriendIds();
			cStr = friend.getBlackIds();
			maxMember = getFriendPublicConfig().getMaxFirend();
			break;
			
		case GameConstants.F_HEI:
			rStr = friend.getBlackIds();
			cStr = friend.getFriendIds();
			maxMember = getFriendPublicConfig().getMaxBlack();
			break;

		default:
			return AppErrorCode.F_NO_SUPPORT;
		} 
		
		return	addFriendOrBlack(roleId, targetRoleId, publicMsgQueue, maxMember, rStr, cStr, type);
	}
	
	/**
	 * 添加好友
	 * @description
	 */
	private Object[] addFriendOrBlack(Long userRoleId, Long targetRoleId,BusMsgQueue publicMsgQueue,int maxMember,String rStr,String cStr,int type) {
		if(targetRoleId == null){
			return AppErrorCode.F_DATA_ERROR;
		}
		
		//判断对方是否在线
		if(!publicRoleStateExportService.isPublicOnline(targetRoleId)){
			return AppErrorCode.F_NO_ONLINE;
		}
		
		RoleWrapper targetRole =  roleExportService.getLoginRole(targetRoleId);
		if(targetRole == null){
			return AppErrorCode.F_NO_ONLINE;
		}
		
		//不能添加自己为好友
		if (userRoleId.equals(targetRoleId)) {
			return AppErrorCode.FRIEND_NOT_ADD;
		} 
		
		//是否已加为好友
		if(FriendUtil.isContainsFriendId(targetRoleId, rStr)){ //friend.getFriendIds())){
			if(type == GameConstants.F_FRIEND){
				return AppErrorCode.ROLE_EXISTS_FRIENDS;
			}else if(type == GameConstants.F_HEI){
				return AppErrorCode.ROLE_EXISTS_HEIS;
			}
			
		}
		
		//对方是否拒绝加为好友
		if(type == GameConstants.F_FRIEND && roleSettingExportService.isSetting(targetRoleId, GameConstants.SYSTEM_SETTING_TYPE_FIREND)){
			return AppErrorCode.OPERATE_ERROR_TYPE_FIREND;//对方禁止加为好友
		}
		
		//不能再添加好友了，您的已达到好友的最大人数
//		if(FriendUtil.getFriendCount(friend.getFriendIds()) >= getFriendPublicConfig().getMaxFirend()){
		if(FriendUtil.getMemberCount(rStr) >= maxMember){
			return AppErrorCode.FRIEND_MAX_COUNT;
		}
		
		Friend friend = getFriend(userRoleId);
		 
		if(FriendUtil.isContainsFriendId(targetRoleId, cStr)){//friend.getBlackIds())){
			String blackIds = FriendUtil.delMember(targetRoleId, cStr);
			
			if(type == GameConstants.F_FRIEND){
				friend.setBlackIds(blackIds);
			}else{
				friend.setFriendIds(blackIds);
			}
			
			publicMsgQueue.addMsg(userRoleId, ClientCmdType.DELETE_MEMBER, FriendOutputWrapper.del(targetRoleId,type));
		}
		
		String finalFriendIds = FriendUtil.addMember(targetRoleId, rStr);
		
		
		if(type == GameConstants.F_FRIEND){
			friend.setFriendIds(finalFriendIds);
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.ADD_FRIENDS, null});
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			}
		}else{
			friend.setBlackIds(finalFriendIds);
		}
 
		friendDao.cacheUpdate(friend, userRoleId);
		
		FriendVo friendVo = getOnlineFriendVo(targetRole);
		Object[] friendArray = getSingleFriend(friendVo, userRoleId);
		return new Object[]{1,type,friendArray};
	}
	
	
	
	private FriendVo getOnlineFriendVo(RoleWrapper targetRole){
		FriendVo friendVo =new FriendVo();
		friendVo.setId(targetRole.getId());
		friendVo.setConfigId(targetRole.getConfigId());
		friendVo.setLastOfflineTime(targetRole.getOfflineTime());
		friendVo.setLevel(targetRole.getLevel());
		friendVo.setName(targetRole.getName());
		return friendVo;
	}
	
	public Object[] delMember(Long userRoleId, Long targetRoleId, Integer type) {
		Friend friend = getFriend(userRoleId);
		String finalFriendIds =null;
		switch (type) {
		case GameConstants.F_FRIEND:
			//是否已不在好友列表内
			if(!FriendUtil.isContainsFriendId(targetRoleId, friend.getFriendIds())){
				return AppErrorCode.ROLE_NO_EXISTS_FRIENDS;
			}
			
			finalFriendIds = FriendUtil.delMember(targetRoleId, friend.getFriendIds());
			friend.setFriendIds(finalFriendIds);
			friendDao.cacheUpdate(friend, userRoleId);
			break;
			
		case GameConstants.F_COU:
			Map<Long,Integer> enemyDatas =  friend.getEnemyDatas();
			if(enemyDatas == null || !enemyDatas.containsKey(targetRoleId)){
				return AppErrorCode.ROLE_NO_EXISTS_ENEMYS;
			}
			 
			Map<Long,Integer> enemyDataMaps = FriendUtil.delMember(targetRoleId, friend.getEnemyDatas());
			friend.setEnemyDatas(enemyDataMaps);
			
			friendDao.cacheUpdate(friend, userRoleId);
			break;
			
		case GameConstants.F_HEI:
			//是否已不在好友列表内
			if(!FriendUtil.isContainsFriendId(targetRoleId, friend.getBlackIds())){
				return AppErrorCode.ROLE_NO_EXISTS_BLACKS;
			}
			
			finalFriendIds = FriendUtil.delMember(targetRoleId, friend.getBlackIds());
			friend.setBlackIds(finalFriendIds);
			friendDao.cacheUpdate(friend, userRoleId);
			break;

		default:
			return AppErrorCode.FRIEND_TYPE_ERROR;
		}
		
		return  FriendOutputWrapper.del(targetRoleId,type);
	}
	
	
	 
	
	/**
	 * 增加仇人
	 * @description
	 */
	public Object[] addEnemyLists(Long harmId, Long attackId) {
		if (attackId.equals(harmId)) {
			return AppErrorCode.ENEMYLIST_NOT_ADD;
		}
		
		//记录被干掉者的数据
		Friend harmFriend =	getFriend(harmId);
		Map<Long, Integer> harmEnemyDatas = harmFriend.getEnemyDatas();
		if(harmEnemyDatas == null){
			harmEnemyDatas =new HashMap<Long, Integer>();
			
		}
		
		Integer bs=harmEnemyDatas.get(attackId)==null?0:harmEnemyDatas.get(attackId);
		harmEnemyDatas.put(attackId, --bs);
		harmFriend.setEnemyDatas(harmEnemyDatas);
		
		friendDao.cacheUpdate(harmFriend, harmId);
		
		//记录攻击者的数据
		Friend attackFriend = getFriend(attackId);
		Map<Long, Integer> attackEnemyDatas = attackFriend.getEnemyDatas();
		
		if(attackEnemyDatas != null ){
			Integer attackCount = attackEnemyDatas.get(harmId);
			if(attackCount != null){
				attackEnemyDatas.put(harmId, ++attackCount);
				attackFriend.setEnemyDatas(attackEnemyDatas);
				
				friendDao.cacheUpdate(attackFriend, attackId);
			}
		}
		
		//该仇人是否在线
//		RoleWrapper attackRole = roleExportService.getLoginRole(attackId);
//		FriendVo attackRoleVo = getOnlineFriendVo(attackRole);
		
		FriendVo attackRoleVo = getFriendVo(attackId);
		
		return getSingleFriend(attackRoleVo,harmId);
	}
	
	public FriendVo getFriendVo(long userRoleId){
		
		FriendVo friendVo =null;
				
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper == null){
			friendVo = getOnlineFriendVo(roleWrapper);
		}else{
			 friendVo = friendDao.getFriendVoByParams(userRoleId);
		} 
		return friendVo;
	}
	
	
	
	
	public Object[] selectRoleInfo(String roleName,long userRoleId) {
		
		FriendVo friendVo = friendDao.getFriendVoByParams(roleName);
		if(friendVo == null){
			return AppErrorCode.F_NO_FIND;
		}
		
		Object[] result= getSingleFriend(friendVo,userRoleId);
		
		return new Object[]{1,result};
	}

	public Object[] delFriends(Long roleId, Object[] datas) {
		Friend friend = getFriend(roleId);
		String finalFriendIds =friend.getFriendIds();
		
		for (Object object : datas) {
			long targetRoleId = LongUtils.obj2long(object);
			//是否已不在好友列表内
			if(!FriendUtil.isContainsFriendId(targetRoleId, finalFriendIds)){
				return AppErrorCode.ROLE_NO_EXISTS_FRIENDS;
			}
			finalFriendIds = FriendUtil.delMember(targetRoleId, finalFriendIds);
		}
		
		friend.setFriendIds(finalFriendIds);
		friendDao.cacheUpdate(friend, roleId);
		
		return new Object[]{1};
	}

	public void friendSet(Long roleId, Boolean isAccept, Integer replyState) {
		
		Friend  friend = getFriend(roleId);
		friend.setIsAccept(isAccept==true?1:0); 
		friend.setReplyState(replyState);
		
		friendDao.cacheUpdate(friend, roleId);
	}

	/**
	 * 获得好友的设置默认信息
	 * @param roleId
	 * @return
	 */
	public Object[] getFriendSet(Long roleId) {
		
		Friend  friend = getFriend(roleId);
		 
		return new Object[]{friend.getIsAccept().intValue()==1,friend.getReplyState()};
	}

	/**
	 * 是否可以私聊
	 * @param roleId
	 * @return
	 */
	public boolean isPrivate(Long receiverId,long sendRoleId){
		Friend friend = getFriend(receiverId);
//		if(friend == null){
//			return false;
//		}
		 
		//对方的黑名单中有发送者 
		if(friend.getBlackIds()!=null && !"".equals(friend.getBlackIds())){
			if(friend.getBlackIds().contains(sendRoleId+GameConstants.F_SPLIT_CHAR)){
				return false;
			}
		}
		
		if(friend.getIsAccept().intValue() == 1){
			if(friend.getFriendIds() == null || !friend.getFriendIds().contains(sendRoleId+GameConstants.F_SPLIT_CHAR) ){
				return false;
			}
		} 
	 
		
		return true;
	}
	
	/**
	 * 只会从缓存或db中取出,不会创建新的
	 * @return
	 */
	private Friend getAsynLoadFriend(Long userRoleId){
		return  friendDao.cacheAsynLoad(userRoleId, userRoleId);
	}
}
