package com.junyou.bus.email.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.email.dao.EmailRelationDao;
import com.junyou.bus.email.entity.EmailInfo;
import com.junyou.bus.email.entity.EmailRelation;
import com.junyou.bus.email.utils.EmailOutPutWarpper;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.EmailDelLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.dao.EmailDao;
import com.junyou.public_.email.entity.Email;
import com.junyou.public_.email.entity.EmailManager;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.public_.tunnel.PublicMsgQueue;
import com.junyou.public_.tunnel.PublicMsgSender;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.number.LongUtils;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class EmailRelationService {
	@Autowired
	private EmailRelationDao emailRelationDao;
	@Autowired
	private EmailDao emailDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	
	
	public List<EmailRelation> initEmailRelation(Long userRoleId){
		return emailRelationDao.initEmailRelation(userRoleId);
	}
	
	/**
	 * 玩家上线检测可建立关系的邮件
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		Long offline = role.getOfflineTime();
		//第一次创号则不处理关系
		if(role.isFirstLogin()){
			offline = 0l;
		}
		long createTime = role.getCreateTime().getTime();
		List<Email> list = emailDao.needCheckEmails(createTime, offline/1000);
		if(list != null && list.size() > 0){
			List<EmailRelation> emailRelations = emailRelationDao.cacheLoadAll(userRoleId);
			for (Email email : list) {
				if(!checkEmail(role, email,true)){
					continue;
				}
				if(!hasRelation(emailRelations, email)){
					continue;
				}
				EmailRelation relation = createEmailRelation(userRoleId, email.getId());
				emailRelationDao.cacheInsert(relation, userRoleId);
			}
		}
		
		syncBakEmail(userRoleId,false);
	}
	/**
	 * 检测该邮件是否已建立关系
	 * @param emailRelations
	 * @param email
	 * @return
	 */
	private boolean hasRelation(List<EmailRelation> emailRelations,Email email){
		if(emailRelations == null || emailRelations.size() < 1)return true;
		for (EmailRelation emailRelation : emailRelations) {
			if(email.getId().equals(emailRelation.getEmailId()))return false;
		}
		return true;
	}
	
	private EmailRelation createEmailRelation(Long userRoleId,Long emailId){
		EmailRelation relation = new EmailRelation();
		relation.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		relation.setUserRoleId(userRoleId);
		relation.setEmailId(emailId);
		relation.setIsDelete(GameConstants.EMAIL_BAK);
		relation.setStatus(GameConstants.EMAIL_STATUS_WEIDU);
		return relation;
	}
//	/**
//	 * 获取当前邮件总数量
//	 * @param userRoleId
//	 * @param db	是否查询db
//	 * @return
//	 */
//	private int getEmailCount(Long userRoleId,boolean db){
//		if(db){
//			return emailRelationDao.selectEmailCount(userRoleId);
//		}else{
//			List<EmailRelation> list = emailRelationDao.cacheLoadAll(userRoleId, new IQueryFilter<EmailRelation>() {
//				@Override
//				public boolean check(EmailRelation entity) {
//					if(entity.getIsDelete() == GameConstants.EMAIL_NOT_DEL){
//						return true;
//					}
//					return false;
//				}
//				@Override
//				public boolean stopped() {
//					return false;
//				}
//			});
//			return list == null ? 0 : list.size();
//		}
//	}
	/**
	 * 检测玩家是否满足邮件的关系建立条件
	 * @param role
	 * @param email
	 * @return
	 */
	private boolean checkEmail(RoleWrapper role,Email email,boolean online){
		if(email.getEmailType() == GameConstants.EMAIL_TYPE_ALL){
			return true;
		}else if(email.getEmailType() == GameConstants.EMAIL_TYPE_LEVEL){
			Integer minLevel = email.getMin();
			Integer maxLevel = email.getMax();
			if(minLevel != null && minLevel > role.getLevel()){
				return false;
			}
			if(maxLevel != null && maxLevel < role.getLevel()){
				return false;
			}
			return true;
		}else if(email.getEmailType() == GameConstants.EMAIL_TYPE_VIP){
			Integer minLevel = email.getMin();
			Integer maxLevel = email.getMax();
			RoleVipWrapper vip = null;
			if(online){
				vip = roleVipInfoExportService.getRoleVipInfo(role.getId());
			}else{
				vip = roleVipInfoExportService.getRoleVipInfoFromDB(role.getId());
			}
			if(vip == null){
				return false;
			}
			if(minLevel != null && minLevel > vip.getVipLevel()){
				return false;
			}
			if(maxLevel != null && maxLevel < vip.getVipLevel()){
				return false;
			}
			return true;
		}
		return false;
	}
//	/**
//	 * 发送邮件
//	 * @param roleIds	收件人列表，全服邮件传null
//	 * @param email
//	 */
//	public void sendEmailbak(List<Long> roleIds,Email email){
//		PublicMsgQueue publicMsgQueue = new PublicMsgQueue();
//		if(roleIds == null){
//			roleIds = publicRoleStateExportService.getAllOnlineRoleids();
//			List<Long> noticeRoleids = new ArrayList<>();
//			for (Long roleId : roleIds) {
//				try{
//					RoleWrapper role = roleExportService.getLoginRole(roleId);
//					boolean online = true;
//					if(role == null){
//						role = roleExportService.getUserRoleFromDb(roleId);
//						online = false;
//					}
//					if(role.getCreateTime().getTime() > email.getCheckTime() || !checkEmail(role, email,online)){
//						continue;
//					}
//					EmailRelation relation = createEmailRelation(roleId, email.getId());
//					int mailCount = getEmailCount(roleId, false);
//					if(mailCount >= EmailUtil.getEmailMaxCount()){
//						relation.setIsDelete(GameConstants.EMAIL_IS_DEL);
//					}
//					if(online){
//						emailRelationDao.cacheInsert(relation, roleId);
//						noticeRoleids.add(roleId);
//						publicMsgQueue.addMsg(roleId, ClientCmdType.NEW_MESSAGE, EmailOutPutWarpper.email2TitleInfoVo(email, relation));
//					}else{
//						emailRelationDao.insert(relation, roleId, AccessType.getDirectDbType());
//					}
//				}catch (Exception e) {
//					ChuanQiLog.error("玩家{}建立邮件{}关系时异常",roleId,email.getId());
//				}
//			}
//		}else{
//			for (Long roleId : roleIds) {
//				sendEmail(roleId, email,publicMsgQueue);
//			}
//		}
//		publicMsgQueue.flush();
//	}
	
//	/**
//	 * 单发邮件
//	 * @param roleId
//	 * @param email
//	 * @param publicMsgQueue
//	 */
//	public void sendEmail(Long roleId,Email email,PublicMsgQueue publicMsgQueue){
//		EmailRelation relation = createEmailRelation(roleId, email.getId());
//		boolean isOnline = publicRoleStateExportService.isPublicOnline(roleId);
//		int emailCount = getEmailCount(roleId, !isOnline);
//		if(emailCount >= EmailUtil.getEmailMaxCount()){
//			relation.setIsDelete(GameConstants.EMAIL_IS_DEL);
//		}
//		if(isOnline){
//			emailRelationDao.cacheInsert(relation, roleId);
//			publicMsgQueue.addMsg(roleId, ClientCmdType.NEW_MESSAGE, EmailOutPutWarpper.email2TitleInfoVo(email, relation));
//		}else{
//			emailRelationDao.insert(relation, null, AccessType.getDirectDbType());
//		}
//	}
	
	/**
	 * 发送邮件
	 * @param roleIds	收件人列表，全服邮件传null
	 * @param email
	 */
	public void sendEmail(List<Long> roleIds,Email email){
		PublicMsgQueue publicMsgQueue = new PublicMsgQueue();
		if(roleIds == null){
			roleIds = publicRoleStateExportService.getAllOnlineRoleids();
		}
		
		if(roleIds != null){
			for (Long roleId : roleIds) {
				sendEmail(roleId, email,publicMsgQueue);
			}
		}
		
		publicMsgQueue.flush();
	}
	
	/**
	 * 单发邮件
	 * @param roleId
	 * @param email
	 * @param publicMsgQueue
	 */
	public void sendEmail(Long roleId,Email email,PublicMsgQueue publicMsgQueue){
		EmailRelation relation = createEmailRelation(roleId, email.getId());
//		boolean isOnline = publicRoleStateExportService.isPublicOnline(roleId);
		RoleWrapper role = roleExportService.getLoginRole(roleId);
		if(role != null){
			List<EmailRelation> emailRelations = getEmailsByType(roleId,GameConstants.EMAIL_NOT_DEL);
			if(emailRelations == null || emailRelations.size() < EmailUtil.getEmailMaxCount()){
				relation.setIsDelete(GameConstants.EMAIL_NOT_DEL);
				publicMsgQueue.addMsg(roleId, ClientCmdType.NEW_MESSAGE, EmailOutPutWarpper.email2TitleInfoVo(email, relation));
			}else{
				publicMsgQueue.addBroadcastMsg(ClientCmdType.NOTIFY_CLIENT_ALERT, new Object[]{AppErrorCode.EMAIL_FULL});
			}
			emailRelationDao.cacheInsert(relation, roleId);
		}else{
			emailRelationDao.insert(relation, null, AccessType.getDirectDbType());
		}
	}
	
	/**
	 * 
	 * @param roleId
	 * @param deleteState  GameConstants.EMAIL_NOT_DEL
	 * @return
	 */
	private List<EmailRelation>  getEmailsByType(long roleId,final int deleteState){
		return emailRelationDao.cacheLoadAll(roleId, new IQueryFilter<EmailRelation>() {
			
			@Override
			public boolean check(EmailRelation entity) {
				return entity.getIsDelete() == deleteState;
			}

			@Override
			public boolean stopped() {
				return false;
			}
		});
	}
	
	
	/**
	 * 读邮件
	 * @param roleId
	 * @param relationId
	 * @return
	 */
	public Object[] readEmail(Long roleId,Long relationId){
		EmailRelation relation = emailRelationDao.cacheLoad(relationId, roleId);
		if(relation == null){
			return AppErrorCode.EMAIL_NO;
		}
		if(relation.getIsDelete().equals(GameConstants.EMAIL_IS_DEL)){
			return AppErrorCode.EMAIL_IS_DELETE;
		}
		Email email = EmailManager.getManager().getEmail(relation.getEmailId());
		if(email == null){
			relation.setIsDelete(GameConstants.IS_DEL);
			emailRelationDao.cacheUpdate(relation, roleId);
			BusMsgSender.send2One(roleId, ClientCmdType.DELETE_EMAIL, new Object[]{1,new Object[]{relationId}});
			return AppErrorCode.EMAIL_NO;
		}
		if(relation.getStatus() == GameConstants.EMAIL_STATUS_WEIDU){//未读的邮件改成已读
			relation.setStatus(GameConstants.EMAIL_STATUS_YIDU);
			emailRelationDao.cacheUpdate(relation, roleId);
		}
		if(email.getExpireTime() < GameSystemTime.getSystemMillTime()){
			return AppErrorCode.EMAIL_IS_EXPIRE;
		}
		return new Object[]{1,EmailOutPutWarpper.email2InfoVo(email, relation)};
	}
	/**
	 * 领取附件
	 * @param roleId
	 * @param relationId
	 * @return
	 */
	public Object[] reciveEmail(Long roleId,Long relationId){
		EmailRelation relation = emailRelationDao.cacheLoad(relationId, roleId);
		if(relation == null){
			return AppErrorCode.EMAIL_NO;
		}
		if(relation.getIsDelete().equals(GameConstants.EMAIL_IS_DEL)){
			return AppErrorCode.EMAIL_IS_DELETE;
		}
		Email email = EmailManager.getManager().getEmail(relation.getEmailId());
		if(email == null){
			relation.setIsDelete(GameConstants.IS_DEL);
			emailRelationDao.cacheUpdate(relation, roleId);
			BusMsgSender.send2One(roleId, ClientCmdType.DELETE_EMAIL, new Object[]{1,new Object[]{relationId}});
			return AppErrorCode.EMAIL_NO;
		}
		if(email.getExpireTime() < GameSystemTime.getSystemMillTime()){
			return AppErrorCode.EMAIL_IS_EXPIRE;
		}
		
		if(relation.getStatus() == GameConstants.EMAIL_STATUS_YILINGQU){
			return AppErrorCode.EMAIL_FUJIAN_YI_LINGQU;
		}
		
		//判断后台发过来的有附件的邮件锁死24小时
		if(GameConstants.EMAIL_GMTOOL_STATUS.equals(email.getGmTools())){
			//锁死邮件判断 
			Object[] errorCode = null;
			Long calcValue = 0L;
			if(PlatformConstants.is37()){
				//只有37平台需要处理锁定24小时
				calcValue = DatetimeUtil.addHours(email.getCreateTime().getTime(), GameConstants.EMAIL_GMTOOL_LOCK_HOUR);
				errorCode = AppErrorCode.EMAIL_LOCK_24;
			}else{
				//pps联运锁死1小时判断 
				calcValue = DatetimeUtil.addHours(email.getCreateTime().getTime(), GameConstants.EMAIL_GMTOOL_LOCK_HOUR_PPS);
				errorCode = AppErrorCode.EMAIL_LOCK_1;
			}
			
			if(calcValue > GameSystemTime.getSystemMillTime()){
				return errorCode;
			}
		}
		
		List<String[]> items = EmailUtil.atta2Items(email.getAttachment());
		if(items == null){//无附件，直接删除邮件
//			relation.setIsDelete(GameConstants.EMAIL_IS_DEL);
//			emailRelationDao.cacheUpdate(relation, roleId);
			//emailRelationDao.cacheDelete(relation.getId(), roleId);
			return new Object[]{1,relationId};
		}
		Map<String,Integer> map = new HashMap<>();
		Map<String,Integer> checkMap = new HashMap<>();
		List<String[]> equips = new ArrayList<>();
		for (String[] item : items) {
			String goodsId = item[0];
			Integer checkValue = checkMap.get(goodsId);
			if(checkValue == null){
				checkValue = Integer.parseInt(item[1]);
			}else{
				checkValue += Integer.parseInt(item[1]);
			}
			if(item.length < 3){
				Integer value = map.get(goodsId);
				if(value == null){
					value = Integer.parseInt(item[1]);
				}else{
					value += Integer.parseInt(item[1]);
				}
				map.put(goodsId, value);
			}else{
				equips.add(item);
			}
			checkMap.put(goodsId, checkValue);
		}
		Object[] result = roleBagExportService.checkPutGoodsAndNumberAttr(checkMap, roleId);
		if(result != null){
			return result;
		}
		//relation.setIsDelete(GameConstants.EMAIL_IS_DEL);
		relation.setStatus(GameConstants.EMAIL_STATUS_YILINGQU);//标记状态为已领取
		emailRelationDao.cacheUpdate(relation, roleId);
		//emailRelationDao.cacheDelete(relation.getId(), roleId);
//		if(email.getEmailType() == GameConstants.EMAIL_TYPE_SINGLE){//个人邮件领取后直接删除
//			EmailManager.getManager().delEmail(email.getId());
//			emailDao.cacheDelete(email.getId(), GameConstants.DEFAULT_ROLE_ID);
//		}
		for (String[] item : equips) {
			String goodsId = item[0];
			int count = Integer.parseInt(item[1]);
			int qh = Integer.parseInt(item[2]);
			RoleItemInput bag = BagUtil.createItem(goodsId, count,qh);
			roleBagExportService.putInBag(bag, roleId, GoodsSource.EMAIL_FUJIAN, true);
		}
		if(map.size() > 0){
			roleBagExportService.putGoodsAndNumberAttr(map, roleId, GoodsSource.EMAIL_FUJIAN,LogPrintHandle.GET_EMAIL,LogPrintHandle.GBZ_EMAIL, true);
		}
		return new Object[]{1,relationId};
	}
	/**
	 * 一键领取
	 * @param userRoleId
	 * @return
	 */
	public Object[] onceReciveEmail(Long userRoleId){
		List<EmailRelation> list = emailRelationDao.cacheLoadAll(userRoleId, new IQueryFilter<EmailRelation>() {
			@Override
			public boolean check(EmailRelation entity) {
				return entity.getIsDelete() == GameConstants.EMAIL_NOT_DEL && entity.getStatus() != GameConstants.EMAIL_STATUS_YILINGQU;
			}
			@Override
			public boolean stopped() {
				return false;
			}
		});
		if(list == null || list.size() < 1){
			//无可领取邮件
			return AppErrorCode.EMAIL_NO_ATT;
		}
		List<EmailInfo> emailList = new ArrayList<>();
		for (EmailRelation emailRelation : list) {
			Email email = EmailManager.getManager().getEmail(emailRelation.getEmailId());
			if(email != null){
				
				//判断后台发过来的有附件的邮件锁死24小时
				if(GameConstants.EMAIL_GMTOOL_STATUS.equals(email.getGmTools())){
					
					Long calcValue = 0L;
					if(PlatformConstants.is37()){
						//只有37平台需要处理锁定24小时
						calcValue = DatetimeUtil.addHours(email.getCreateTime().getTime(), GameConstants.EMAIL_GMTOOL_LOCK_HOUR);
					}else{
						//pps联运锁死1小时判断 
						calcValue = DatetimeUtil.addHours(email.getCreateTime().getTime(), GameConstants.EMAIL_GMTOOL_LOCK_HOUR_PPS);
					}
					
					if(calcValue > GameSystemTime.getSystemMillTime()){
						continue;
					}
				}
				
				emailList.add(new EmailInfo(email, emailRelation));
			}
		}
		//无可领取邮件
		if(emailList.size() < 1){
			return AppErrorCode.EMAIL_NO_ATT;
		}
		List<Long> emailRelationIds = new ArrayList<>();
		Map<String,Integer> map = new HashMap<>();
		List<String[]> equips = new ArrayList<>();
		Map<String,Integer> showItems = new HashMap<>();
		Map<String,Integer> checkMap = new HashMap<>();
		for (EmailInfo email : emailList) {
			EmailRelation emailRelation = email.getEmailRelation();
			if(email.getEmail().getExpireTime() < GameSystemTime.getSystemMillTime()){
				//已过期邮件直接删除
				emailRelationIds.add(emailRelation.getId());
//				emailRelation.setIsDelete(GameConstants.EMAIL_IS_DEL);
//				emailRelationDao.cacheUpdate(emailRelation, userRoleId);
				emailRelationDao.cacheDelete(emailRelation.getId(), userRoleId);
				continue;
			}
			if(emailRelation.getStatus() == GameConstants.EMAIL_STATUS_YILINGQU){
				continue;
			}
			List<String[]> items = EmailUtil.atta2Items(email.getEmail().getAttachment());
			if(items == null){
				//无附件邮件直接删除
//				emailRelationIds.add(emailRelation.getId());
//				emailRelation.setIsDelete(GameConstants.EMAIL_IS_DEL);
//				emailRelationDao.cacheUpdate(emailRelation, userRoleId);
				//emailRelationDao.cacheDelete(emailRelation.getId(), userRoleId);
				continue;
			}
			for (String[] item : items) {
				Integer checkValue = checkMap.get(item[0]);
				if(checkValue == null){
					checkValue = Integer.parseInt(item[1]);
				}else{
					checkValue += Integer.parseInt(item[1]);
				}
				checkMap.put(item[0], checkValue);
			}
			//检测这封邮件的附件物品是否可以放下
			Object[] result = roleBagExportService.checkPutGoodsAndNumberAttr(checkMap, userRoleId);
			if(result != null){
				return result;
			}
			//修改邮件状态
			emailRelationIds.add(emailRelation.getId());
			//emailRelation.setIsDelete(GameConstants.EMAIL_IS_DEL);
			emailRelation.setStatus(GameConstants.EMAIL_STATUS_YILINGQU);
			emailRelationDao.cacheUpdate(emailRelation, userRoleId);
			//emailRelationDao.cacheDelete(emailRelation.getId(), userRoleId);
			
			for (String[] item : items) {
				if(item.length < 3){
					Integer value = map.get(item[0]);
					if(value == null){
						value = Integer.parseInt(item[1]);
					}else{
						value += Integer.parseInt(item[1]);
					}
					map.put(item[0], value);
				}else{
					equips.add(item);
				}
				showItems.put(item[0], checkMap.get(item[0]));
			}
			
//			if(email.getEmail().getEmailType() == GameConstants.EMAIL_TYPE_SINGLE){//个人邮件领取后直接删除
//				EmailManager.getManager().delEmail(email.getEmail().getId());
//				emailDao.cacheDelete(email.getEmail().getId(), GameConstants.DEFAULT_ROLE_ID);
//			}
		}
		for (String[] item : equips) {
			String goodsId = item[0];
			int count = Integer.parseInt(item[1]);
			int qh = Integer.parseInt(item[2]);
			RoleItemInput bag = BagUtil.createItem(goodsId, count,qh);
			roleBagExportService.putInBag(bag, userRoleId, GoodsSource.EMAIL_FUJIAN, true);
		}
		if(map.size() > 0){
			roleBagExportService.putGoodsAndNumberAttr(map, userRoleId, GoodsSource.EMAIL_FUJIAN,LogPrintHandle.GET_EMAIL,LogPrintHandle.GBZ_EMAIL, true);
		}
		return new Object[]{1,emailRelationIds.toArray(),showItems};
	}
	
	public void delEmailRelationByIds(String emailIds){
		emailRelationDao.delEmailRelationByIds(emailIds);
	}
	/**
	 * 检测玩家邮件关系中是否有过期邮件，删除内存并通知客户端
	 * @param delIds
	 * @param publicMsgQueue
	 */
	public void checkEmailRelation(List<Long> delIds,PublicMsgQueue publicMsgQueue){
		for (Long roleId : publicRoleStateExportService.getAllOnlineRoleids()) {
			try{
				List<Long> noticeIds = new ArrayList<>();
				List<EmailRelation> relations = emailRelationDao.cacheLoadAll(roleId);
				for (EmailRelation emailRelation : relations) {
					Long id = emailRelation.getEmailId();
					if(delIds.contains(id)){
						noticeIds.add(id);
					}
				}
				if(noticeIds.size() > 1){
					Object[] msg = new Object[]{1,noticeIds.toArray()};
					publicMsgQueue.addMsg(roleId, ClientCmdType.DELETE_EMAIL, msg);
					syncBakEmail(roleId, true);
				}
			}catch (Exception e) {
				ChuanQiLog.error("玩家{}检测过期邮件时出错",roleId);
			}
		}
	}
	/**
	 * 玩家手动删除邮件
	 * @param emailIds
	 * @param roleId
	 * @return
	 */
	public Object[] playerDelEmail(Long roleId,Object[] relationIds){
		List<Long> deleteIds = new ArrayList<>();
		if(relationIds == null || relationIds.length <= 0){
			return AppErrorCode.EMAIL_NO_SELECT_DEL;
		}
		for (int i = 0; i < relationIds.length; i++) {
			long relationId =LongUtils.obj2long(relationIds[i]);
			EmailRelation emailRelation = emailRelationDao.cacheLoad(relationId, roleId);
			if(emailRelation == null){
				return AppErrorCode.EMAIL_NO_DELETE;
			}
			if(emailRelation.getStatus() != GameConstants.EMAIL_STATUS_YILINGQU){
				try {
					//获取邮件
					Email email = EmailManager.getManager().getEmail(emailRelation.getEmailId());
					if(email != null && !ObjectUtil.strIsEmpty(email.getAttachment())){
						return AppErrorCode.EMAIL_FUJIAN_NO_DELETE;
					}
				} catch (Exception e) {
					ChuanQiLog.error("",e);
				}
			}
		}
		
		for (int i = 0; i < relationIds.length; i++) {
			long relationId = LongUtils.obj2long(relationIds[i]);
			EmailRelation emailRelation = emailRelationDao.cacheLoad(relationId, roleId);
			if(emailRelation != null){
				emailRelation.setIsDelete(GameConstants.IS_DEL);
				emailRelationDao.cacheUpdate(emailRelation, roleId);
				deleteIds.add(relationId);
				try {
					//获取邮件
					Email email = EmailManager.getManager().getEmail(emailRelation.getEmailId());
					if(email != null){
						//抛出删除带有附件邮件事件打印日志
						if(email.haveAttament()){
							RoleWrapper role = roleExportService.getLoginRole(roleId);
							String roleName = role == null ? "-" : role.getName();
							//抛出日志事件
							GamePublishEvent.publishEvent(new EmailDelLogEvent(roleId, roleName, email.getAttachment()));
						}
					}
				} catch (Exception e) {
					ChuanQiLog.error("",e);
				}
			}
		}
		
		//处理将仓库中的邮件拉取出来
		if(deleteIds.size() > 0){
			syncBakEmail(roleId,true);
		}
		return new Object[]{1,deleteIds.toArray()};
	}
	
	/**
	 * 处理将仓库中的邮件拉取出来
	 * @param userRoleId
	 */
	private void syncBakEmail(Long userRoleId,boolean isSend) {
		List<EmailRelation> emailRelations = getEmailsByType(userRoleId,GameConstants.EMAIL_NOT_DEL);
		int oldCount = emailRelations == null ? 0:emailRelations.size();
		int needCount = EmailUtil.getEmailMaxCount() - oldCount;
		if(needCount > 0){
			List<EmailRelation> bakEmailRelations = getEmailsByType(userRoleId, GameConstants.EMAIL_BAK);
			if(bakEmailRelations != null && bakEmailRelations.size() > 0){
				//邮件仓库排序
				Collections.sort(bakEmailRelations, new Comparator<EmailRelation>() {
					@Override
					public int compare(EmailRelation o1, EmailRelation o2) {
						long t1 = 0;
						long t2 = 0;
						//获取邮件
						Email email1 = EmailManager.getManager().getEmail(o1.getEmailId());
						if(email1 != null){
							t1 = email1.getCreateTime().getTime();
						}
						
						Email email2 = EmailManager.getManager().getEmail(o2.getEmailId());
						if(email2 != null){
							t2 = email2.getCreateTime().getTime();
						}
						if(t1>t2){
							return 1;
						}else if(t1 == t2){
							return 0;
						}else{
							return -1;
						}
					}
				});
				
				for (EmailRelation emailRelation : bakEmailRelations) {
					Email email1 = EmailManager.getManager().getEmail(emailRelation.getEmailId());
					if(email1 == null){
						 continue;
					}
					emailRelation.setIsDelete(GameConstants.EMAIL_NOT_DEL);
					emailRelationDao.cacheUpdate(emailRelation,userRoleId);
					if(isSend){
						PublicMsgSender.send2One(userRoleId, ClientCmdType.NEW_MESSAGE, EmailOutPutWarpper.email2TitleInfoVo(email1, emailRelation));
					}
					if(--needCount <= 0){
						break;
					}
				}
			}
		}
	}
	/**
	 * 玩家手动删除邮件
	 * @param emailIds
	 * @param roleId
	 * @return
	 */
	public Object[] playerDelEmail(Long roleId,long relationId){
		EmailRelation emailRelation = emailRelationDao.cacheLoad(relationId, roleId);
		if(emailRelation != null){
			emailRelation.setIsDelete(GameConstants.IS_DEL);
			emailRelationDao.cacheUpdate(emailRelation, roleId);
			
			try {
				//获取邮件
				Email email = EmailManager.getManager().getEmail(emailRelation.getEmailId());
				if(email != null){
					//抛出删除邮件事件打印日志
					if(email.haveAttament()){
						String roleName = "-";
						RoleWrapper role = roleExportService.getLoginRole(roleId);
						if(role != null){
							roleName = role.getName();
						}
						
						//抛出日志事件
						GamePublishEvent.publishEvent(new EmailDelLogEvent(roleId, roleName, email.getAttachment()));
					}
				}
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}
		
		return new Object[]{1,new Object[]{relationId}};
	}
	/**
	 * 获取所有邮件
	 * @param roleId
	 * @return
	 */
	public Object[] loadAllEmails(Long roleId){
		List<EmailRelation> list = emailRelationDao.cacheLoadAll(roleId);
		if(list != null && list.size() > 0){
			List<Object[]> emailList = new ArrayList<>();
			for (EmailRelation emailRelation : list) {
				if(emailRelation.getIsDelete().equals(GameConstants.EMAIL_NOT_DEL)){
					Email email = EmailManager.getManager().getEmail(emailRelation.getEmailId());
					if(email == null){
						emailRelation.setIsDelete(GameConstants.IS_DEL);
						emailRelationDao.cacheUpdate(emailRelation, roleId);
					}else{
						emailList.add(EmailOutPutWarpper.email2TitleInfoVo(email, emailRelation));
					}
				}
			}
			if(emailList.size() > 0){
				Collections.sort( emailList, new Comparator<Object[]>(){  
					@Override
					public int compare(Object[] o1, Object[] o2) {
						long time1 = ((Date)o1[1]).getTime();
						long time2 = ((Date)o2[1]).getTime();
						if(time1 > time2){
							return -1;
						}else if(time1 == time2){
							return 0;
						}else{
							return 1;
						}
					}
					}  
		      );  
				return emailList.toArray();
			}
		}
		return null;
	}
}
