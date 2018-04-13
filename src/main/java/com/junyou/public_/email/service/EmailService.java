package com.junyou.public_.email.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.email.service.EmailRelationService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.constants.GameConstants;
import com.junyou.err.HttpErrorCode;
import com.junyou.public_.email.dao.EmailDao;
import com.junyou.public_.email.entity.Email;
import com.junyou.public_.email.entity.EmailManager;
import com.junyou.public_.tunnel.PublicMsgQueue;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.dao.QueryParamMap;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class EmailService {
	@Autowired
	private EmailDao emailDao;
	@Autowired
	private EmailRelationService emailRelationService;
	/**
	 * 后台发送邮件
	 * @param userRoleIds
	 * @param title
	 * @param content
	 * @param emailType
	 * @param min
	 * @param max
	 * @param attachment
	 * @param checkTime
	 * @param biaoshi
	 */
	public String sendEmailGmtool(String userRoleIds,String title,String content,Integer emailType,Integer min,Integer max,String attachment,Long checkTime,String biaoshi){
		if(emailType == null){
			return HttpErrorCode.EMAIL_TYPE_ERROR;
		}
		//如果是全服邮件，并且同一天内已经发过一份同样内容的邮件，则无视
		if(emailType >= GameConstants.EMAIL_TYPE_ALL){
			String createTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			List<Email> elist = emailDao.getEmailByTimeAndContent(content, createTime,emailType);
			if(elist != null && elist.size()>0){
				return HttpErrorCode.EMAIL_CHONGFU;
			}
		}
		List<Long> roleIds = null;
		if(emailType < GameConstants.EMAIL_TYPE_ALL){
			if(userRoleIds == null || "".equals(userRoleIds)){//个人邮件roleId不能为空
				return HttpErrorCode.EMAIL_NO_ROLEID;
			}
			String[] ids = userRoleIds.split(GameConstants.ROLEID_SPLIT);
			roleIds = new ArrayList<>();
			for (String id : ids) {
				roleIds.add(Long.parseLong(id));
			}
		}else if(emailType > GameConstants.EMAIL_TYPE_ALL){
			if(min == null || min.intValue() < 1){
				min = 1;
			}
			if(max != null && max.intValue() < min){
				max = min;
			}
		}
		Email email = createEmail(title,content, emailType, min, max, attachment, checkTime, biaoshi);
		
		
		//从后台发出的全服邮件并且有附件的才锁定
		if(emailType >= GameConstants.EMAIL_TYPE_ALL && email.haveAttament()){
			email.setGmTools(GameConstants.EMAIL_GMTOOL_STATUS);
			//email.setTitle(GameConstants.EMAIL_GMTOOL_TITLE);
		}
		
		emailDao.cacheInsert(email, null);
		EmailManager.getManager().addEmail(email);
		emailRelationService.sendEmail(roleIds, email);
		return HttpErrorCode.SUCCESS;
	}
	/**
	 * 游戏内单发邮件(限时)
	 * @param userRoleId
	 * @param content
	 * @param emailType
	 * @param attachment
	 * @param expireTime	截止时间
	 * @return
	 */
	public boolean sendEmailToOneExpire(Long userRoleId,String title,String content,Integer emailType,String attachment,long expireTime){
		if(emailType >= GameConstants.EMAIL_TYPE_ALL || userRoleId == null){
			return false;
		}
		Email email = createExpireEmail(content,title, emailType, null, null, attachment, GameSystemTime.getSystemMillTime(), null,expireTime);
		emailDao.cacheInsert(email, null);
		EmailManager.getManager().addEmail(email);
		
		PublicMsgQueue publicMsgQueue = new PublicMsgQueue();
		emailRelationService.sendEmail(userRoleId, email,publicMsgQueue);
		publicMsgQueue.flush();
		return true;
	}
	/**
	 * 游戏内单发邮件
	 * @param userRoleId
	 * @param title
	 * @param content
	 * @param emailType
	 * @param attachment
	 * @param checkTime
	 * @return
	 */
	public boolean sendEmailToOne(Long userRoleId,String title,String content,Integer emailType,String attachment){
		if(emailType >= GameConstants.EMAIL_TYPE_ALL || userRoleId == null){
			return false;
		}
		Email email = createEmail(title,content, emailType, null, null, attachment, GameSystemTime.getSystemMillTime(), null);
		emailDao.cacheInsert(email, null);
		EmailManager.getManager().addEmail(email);
		
		PublicMsgQueue publicMsgQueue = new PublicMsgQueue();
		emailRelationService.sendEmail(userRoleId, email,publicMsgQueue);
		publicMsgQueue.flush();
		return true;
	}
	/**
	 * 游戏内批量发邮件
	 * @param userRoleId
	 * @param title
	 * @param content
	 * @param emailType
	 * @param attachment
	 * @param checkTime
	 * @return
	 */
	public boolean sendEmailToMany(String userRoleIds,String title,String content,Integer emailType,String attachment){
		if(emailType >= GameConstants.EMAIL_TYPE_ALL || ObjectUtil.strIsEmpty(userRoleIds)){
			return false;
		}
		Email email = createEmail(title,content, emailType, null, null, attachment, GameSystemTime.getSystemMillTime(), null);
		emailDao.cacheInsert(email, null);
		EmailManager.getManager().addEmail(email);
		String[] ids = userRoleIds.split(",");
		List<Long> roleIds = new ArrayList<>();
		for (String id : ids) {
			roleIds.add(Long.parseLong(id));
		}
		emailRelationService.sendEmail(roleIds, email);
		return true;
	}
	/**
	 * 游戏内批量发邮件
	 * @param userRoleId
	 * @param title
	 * @param content
	 * @param emailType
	 * @param attachment
	 * @param checkTime
	 * @return
	 */
	public boolean sendEmailToMany(List<Long> roleIds,String title,String content,Integer emailType,String attachment){
		if(emailType >= GameConstants.EMAIL_TYPE_ALL || roleIds ==null ||roleIds.size() == 0){
			return false;
		}
		Email email = createEmail(title,content, emailType, null, null, attachment, GameSystemTime.getSystemMillTime(), null);
		emailDao.cacheInsert(email, null);
		EmailManager.getManager().addEmail(email);
		emailRelationService.sendEmail(roleIds, email);
		return true;
	}
	/**
	 * 游戏内群发邮件
	 * @param title
	 * @param content
	 * @param emailType
	 * @param min
	 * @param max
	 * @param attachment
	 * @param checkTime
	 * @param biaoshi
	 * @return
	 */
	public boolean sendEmailToAll(String title,String content,Integer emailType,Integer min,Integer max,String attachment,Long checkTime,String biaoshi){
		if(emailType < GameConstants.EMAIL_TYPE_ALL){
			return false;
		}else if(emailType > GameConstants.EMAIL_TYPE_ALL){
			if(min == null || min.intValue() < 1){
				min = 1;
			}
			if(max != null && max.intValue() < min){
				max = min;
			}
		}
		Email email = createEmail(title,content, emailType, min, max, attachment, checkTime, biaoshi);
		emailDao.cacheInsert(email, null);
		EmailManager.getManager().addEmail(email);
		List<Long> roleIds = null;
		emailRelationService.sendEmail(roleIds, email);
		return true;
	}
	/**
	 * 创建指定时间过期邮件
	 * @param content
	 * @param emailType
	 * @param min
	 * @param max
	 * @param attachment
	 * @param checkTime
	 * @param biaoshi
	 * @param expireTime	过期时间戳
	 * @return
	 */
	private Email createExpireEmail(String content,String title,Integer emailType,Integer min,Integer max,String attachment,Long checkTime,String biaoshi,long expireTime){
		Email email = new Email();
		email.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		email.setContent(content);
		email.setTitle(title);
		email.setEmailType(emailType);
		email.setMin(min);
		email.setMax(max);
		long createTime = GameSystemTime.getSystemMillTime();
		email.setCreateTime(new Timestamp(createTime));
		if(attachment != null && !attachment.equals("")){
			email.setAttachment(attachment);
		}
		email.setExpireTime(expireTime);
		
		email.setCheckTime(checkTime);
		if(biaoshi != null && !biaoshi.equals("")){
			email.setBiaoshi(biaoshi);
		}
		return email;
	}
	/**
	 * 创建普通邮件
	 * @param content
	 * @param emailType
	 * @param min
	 * @param max
	 * @param attachment
	 * @param checkTime
	 * @param biaoshi
	 * @return
	 */
	private Email createEmail(String title,String content,Integer emailType,Integer min,Integer max,String attachment,Long checkTime,String biaoshi){
		Email email = new Email();
		email.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		email.setTitle(title);
		email.setContent(content);
		email.setEmailType(emailType);
		email.setMin(min);
		email.setMax(max);
		long createTime = GameSystemTime.getSystemMillTime();
		email.setCreateTime(new Timestamp(createTime));
		long eTime = EmailUtil.getBaseGuoQi();
		if(attachment != null && !attachment.equals("")){
			email.setAttachment(attachment);
			eTime = EmailUtil.getGuoQi();
		}
		long expireTime = createTime + eTime;
		email.setExpireTime(expireTime);
		
		email.setCheckTime(checkTime);
		if(biaoshi != null && !biaoshi.equals("")){
			email.setBiaoshi(biaoshi);
		}
		return email;
	}
	/**
	 * 根据邮件id批量删除邮件以及邮件关系
	 * @param emailIds
	 */
	public void delEmailByIds(String emailIds){
		emailDao.delEmailByIds(emailIds);
		emailRelationService.delEmailRelationByIds(emailIds);
	}
	/**
	 * 初始化邮件数据
	 */
	public void init(){
		if(KuafuConfigPropUtil.isKuafuServer()){//跨服服务器无需初始化
			return;
		}
		List<Email> list = emailDao.getRecords(new QueryParamMap<>());
		String delIds = EmailManager.getManager().init(list);
		if(delIds != null){
			delEmailByIds(delIds);
		}
	}
	/**
	 * 定时处理邮件业务
	 * @param publicMsgQueue
	 * @return
	 */
	public void emailQuartzDelete(PublicMsgQueue publicMsgQueue){
		List<Long> delIds = EmailManager.getManager().getExpireEmailIds();
		if(delIds.size() > 0){
			StringBuilder ids = new StringBuilder();
			for (Long id : delIds) {
				ids.append(",").append(id);
			}
			delEmailByIds(ids.substring(1));

			emailRelationService.checkEmailRelation(delIds, publicMsgQueue);
		}
	}
	
	/**
	 * 后台删除邮件根据邮件id
	 * @param emailId
	 */
	public void gmToolsDelEmailByEmailId(Long emailId){
		EmailManager.getManager().delEmail(emailId);
		emailDao.cacheDelete(emailId, emailId);
	}
	
	public int gmToolsDelEmailByContent(String content,String startTime,String endTime){
		List<Email> list = emailDao.getEmailByTime(content, startTime, endTime);
		if(list == null || list.size() <=0){
			return -1;
		}
		for (int i = 0; i < list.size(); i++) {
			Email email = list.get(i);
			Long emailId = email.getId();
			EmailManager.getManager().delEmail(emailId);
			emailDao.cacheDelete(emailId, emailId);
		}
		return 1;
	}
	
	
	/**
	 * 创建普通邮件,包含创建时间
	 * @param content
	 * @param emailType
	 * @param min
	 * @param max
	 * @param attachment
	 * @param checkTime
	 * @param biaoshi
	 * @return
	 */
	private Email createEmailByCreateTime(String title,String content,Integer emailType,Integer min,Integer max,String attachment,String biaoshi,long createTime){
		Email email = new Email();
		email.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		email.setContent(content);
		email.setTitle(title);
		email.setEmailType(emailType);
		email.setMin(min);
		email.setMax(max);
		email.setCreateTime(new Timestamp(createTime));
		long eTime = EmailUtil.getBaseGuoQi();
		if(attachment != null && !attachment.equals("")){
			email.setAttachment(attachment);
			eTime = EmailUtil.getGuoQi();
		}
		long expireTime = createTime + eTime;
		email.setExpireTime(expireTime);
		
		email.setCheckTime(createTime-1000);
		if(biaoshi != null && !biaoshi.equals("")){
			email.setBiaoshi(biaoshi);
		}
		return email;
	}
	
	/**
	 * 游戏内单发邮件(可指定创建时间)
	 * @param userRoleId
	 * @param title
	 * @param content
	 * @param emailType
	 * @param attachment
	 * @param checkTime
	 * @return
	 */
	public boolean sendEmailToOneByCreateTime(Long userRoleId,String title,String content,Integer emailType,String attachment,long createTime){
		if(emailType >= GameConstants.EMAIL_TYPE_ALL || userRoleId == null){
			return false;
		}
		Email email = createEmailByCreateTime(title,content, emailType, null, null, attachment, null, createTime);
		emailDao.cacheInsert(email, null);
		EmailManager.getManager().addEmail(email);
		
		PublicMsgQueue publicMsgQueue = new PublicMsgQueue();
		emailRelationService.sendEmail(userRoleId, email,publicMsgQueue);
		publicMsgQueue.flush();
		return true;
	}
}
