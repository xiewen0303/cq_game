package com.junyou.bus.jinfeng.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.jinfeng.dao.JinYanDao;
import com.junyou.bus.jinfeng.entity.JinYan;
import com.junyou.bus.jinfeng.utils.FengHaoUtil;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.HttpErrorCode;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.utils.datetime.GameSystemTime;

@Component
public class JinYanService{

	@Autowired
	private JinYanDao jinYanDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	private ConcurrentMap<Long, JinYan> jinyanList = new ConcurrentHashMap<>();

	public String shutUp(Long userRoleId, BusMsgQueue msgQueue,int keepTime) {
		// userId为空
		if (userRoleId == null) {
			return HttpErrorCode.USER_NOT_EXIST;
		}

		// 查询结果为空
		RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);
		if (role == null) {
			return HttpErrorCode.ROLE_NOT_EXIST;
		}

		JinYan jinYan = null;
		boolean isOnline = publicRoleStateExportService.isPublicOnline(userRoleId);
		// 在线则走缓存，不在线直接走数据库
		if (isOnline) {
			jinYan = jinyanList.get(userRoleId);
		}else{
			jinYan = jinYanDao.selectFromDB(userRoleId);
		}
		long expTime = FengHaoUtil.getExpireTime(keepTime);
		if(jinYan == null){
			jinYan = new JinYan();
			jinYan.setUserRoleId(userRoleId);
			jinYan.setExpireTime(expTime);
			jinYanDao.insert2DB(jinYan);
		}else{
			jinYan.setExpireTime(expTime);
			jinYanDao.updateFromDB(jinYan);
		}
		if(isOnline){
			jinyanList.put(userRoleId, jinYan);
		}
		
		//通知禁言通知客户端静默禁言
		msgQueue.addMsg(userRoleId, ClientCmdType.JING_YAN_JM, new Object[]{true,jinYan.getExpireTime()});
		
		return HttpErrorCode.SUCCESS;
	}

	public String removeShutUp(Long userRoleId,BusMsgQueue msgQueue) {

		// userId为空
		if (userRoleId == null) {
			return HttpErrorCode.USER_NOT_EXIST;
		}

		RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);
		if (role == null) {
			return HttpErrorCode.ROLE_NOT_EXIST;
		}

		if (publicRoleStateExportService.isPublicOnline(userRoleId)) {
			//移除内存数据
			jinyanList.remove(userRoleId);
		}
		jinYanDao.deleteFromDB(userRoleId);
		
		//通知禁言通知客户端静默禁言
		msgQueue.addMsg(userRoleId, ClientCmdType.JING_YAN_JM, new Object[]{false});
		
		return HttpErrorCode.SUCCESS;
	}

	public JinYan getJinYan(Long roleId) {
		JinYan jinYan = jinYanDao.selectFromDB(roleId);
		if (jinYan == null)
			return null;
		if (jinYan.getExpireTime() > 0&& jinYan.getExpireTime() < GameSystemTime.getSystemMillTime()) {
			jinYanDao.deleteFromDB(roleId);
			return null;
		}
		return jinYan;
	}
	
	public void onlineHandle(Long roleId) {
		JinYan jinYan = getJinYan(roleId);
		if(jinYan != null){
			//加入内存管理
			jinyanList.put(roleId,jinYan);
//			BusMsgSender.send2One(roleId, ClientCmdType.JINYAN_STATE, new Object[]{GameConstants.JINYAN_OUT,jinYan.getExpireTime()});
		}
	}
	
	public void offlineHandle(Long roleId) {
		//移除内存管理
		jinyanList.remove(roleId);
	}

	public boolean isJinYan(Long roleId) {
		if(!publicRoleStateExportService.isPublicOnline(roleId)){
			return true;
		}
		JinYan jinYan = jinyanList.get(roleId);
		if (jinYan == null)
			return false;
		if (jinYan.getExpireTime() > 0 && jinYan.getExpireTime() < GameSystemTime.getSystemMillTime()) {
			//先移除内存数据
			jinyanList.remove(roleId);
			jinYanDao.deleteFromDB(jinYan.getUserRoleId());
			return false;
		}
		return true;
	}

	/**
	 * 获取客户端需要的禁言数据
	 * @param roleId
	 * @return
	 */
	public Object getJinYanData(Long roleId){
		if(!publicRoleStateExportService.isPublicOnline(roleId)){
			return null;
		}
		
		JinYan jinYan = jinyanList.get(roleId);
		if (jinYan == null){
			return null;
		}
		
		if (jinYan.getExpireTime() > 0 && jinYan.getExpireTime() < GameSystemTime.getSystemMillTime()) {
			//先移除内存数据
			jinyanList.remove(roleId);
			jinYanDao.deleteFromDB(jinYan.getUserRoleId());
			return null;
		}
			
		//[Boolean(true:已被禁言;false:取消禁言),Number(解封的时间戳,0就是永久禁言)]
		return new Object[]{true,jinYan.getExpireTime()};
	}
	
}