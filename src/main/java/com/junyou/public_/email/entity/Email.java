package com.junyou.public_.email.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("email")
public class Email extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("content")
	private String content;
	
	@Column("title")
	private String title;

	@Column("email_type")
	private Integer emailType;

	@Column("min")
	private Integer min;

	@Column("max")
	private Integer max;

	@Column("attachment")
	private String attachment;

	@Column("recive_time")
	private Long reciveTime;

	@Column("check_time")
	private Long checkTime;

	@Column("create_time")
	private Timestamp createTime;

	@Column("expire_time")
	private Long expireTime;

	@Column("biaoshi")
	private String biaoshi;
	
	@Column("gm_tools")
	private String gmTools;//标记是否是后台发的全服邮件


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public String getContent(){
		return content;
	}

	public  void setContent(String content){
		this.content = content;
	}
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getEmailType(){
		return emailType;
	}

	public  void setEmailType(Integer emailType){
		this.emailType = emailType;
	}

	public Integer getMin(){
		return min;
	}

	public  void setMin(Integer min){
		this.min = min;
	}

	public Integer getMax(){
		return max;
	}

	public  void setMax(Integer max){
		this.max = max;
	}

	public String getAttachment(){
		return attachment;
	}

	public  void setAttachment(String attachment){
		this.attachment = attachment;
	}

	public Long getReciveTime(){
		return reciveTime;
	}

	public  void setReciveTime(Long reciveTime){
		this.reciveTime = reciveTime;
	}

	public Long getCheckTime(){
		return checkTime;
	}

	public  void setCheckTime(Long checkTime){
		this.checkTime = checkTime;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	public Long getExpireTime(){
		return expireTime;
	}

	public  void setExpireTime(Long expireTime){
		this.expireTime = expireTime;
	}

	public String getBiaoshi(){
		return biaoshi;
	}

	public  void setBiaoshi(String biaoshi){
		this.biaoshi = biaoshi;
	}

	public String getGmTools() {
		return gmTools;
	}

	public void setGmTools(String gmTools) {
		this.gmTools = gmTools;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}
	/**
	 * 邮件是否有附件 (有金钱也算有附件)
	 * @return true:有
	 */
	public Boolean haveAttament(){
		if(getAttachment() == null || getAttachment().equals("")){
			return false;
		}else{
			return true;
		}
	}

	public Email copy(){
		Email result = new Email();
		result.setId(getId());
		result.setContent(getContent());
		result.setTitle(getTitle());
		result.setEmailType(getEmailType());
		result.setMin(getMin());
		result.setMax(getMax());
		result.setAttachment(getAttachment());
		result.setReciveTime(getReciveTime());
		result.setCheckTime(getCheckTime());
		result.setCreateTime(getCreateTime());
		result.setExpireTime(getExpireTime());
		result.setBiaoshi(getBiaoshi());
		result.setGmTools(getGmTools());
		return result;
	}
}
