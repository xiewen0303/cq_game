package com.junyou.public_.email.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.public_.email.service.EmailService;
import com.junyou.public_.tunnel.PublicMsgQueue;

@Service
public class EmailExportService {
	
	@Autowired
	private EmailService emailService;
	
	public void init(){
		emailService.init();
	}
	
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
		return emailService.sendEmailGmtool(userRoleIds,title,content, emailType, min, max, attachment, checkTime, biaoshi);
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
		return emailService.sendEmailToOne(userRoleId,title, content, emailType, attachment);
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
		return emailService.sendEmailToOneExpire(userRoleId, title,content, emailType, attachment,expireTime);
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
		return emailService.sendEmailToMany(userRoleIds,title, content, emailType, attachment);
	}
	/**
	 * 游戏内批量发邮件
	 * @param roleIds
	 * @param content
	 * @param emailType
	 * @param attachment
	 * @param checkTime
	 * @return
	 */
	public boolean sendEmailToMany(List<Long> roleIds,String title,String content,Integer emailType,String attachment){
		return emailService.sendEmailToMany(roleIds, title,content, emailType, attachment);
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
		return emailService.sendEmailToAll(title,content, emailType, min, max, attachment, checkTime, biaoshi);
	}
	
	/**
	 * 定时处理邮件业务
	 * @param publicMsgQueue
	 * @return
	 */
	public void emailQuartzDelete(PublicMsgQueue publicMsgQueue){
		emailService.emailQuartzDelete(publicMsgQueue);
	}
	
	/**
	 * 后台删除邮件根据邮件id
	 * @param emailId
	 */
	public void gmToolsDelEmailByEmailId(Long emailId){
		emailService.gmToolsDelEmailByEmailId(emailId);
	}
	
	public int gmToolsDelEmailByContent(String content,String startTime,String endTime){
		return emailService.gmToolsDelEmailByContent(content, startTime, endTime);
	}
	
	public boolean sendEmailToOneByCreateTime(Long userRoleId,String title,String content,Integer emailType,String attachment,long createTime){
		return emailService.sendEmailToOneByCreateTime(userRoleId,title, content, emailType,attachment,createTime);
	}
}
