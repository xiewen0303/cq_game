package com.junyou.public_.guild.entity;


/**
 * 公会权限 
 * @author LiuYu
 * @date 2015-1-20 上午9:32:40
 */
public class GuildQuanXianConfig{
	/**职位id*/
	private int id;
	/**职位人数上限，0为无上限*/
	private int postMaxCount;
	/**任命权限*/
	private boolean appoint;
	/**开除权限*/
	private boolean expel;
	/**审批权限*/
	private boolean recruit;
	/**修改公告权限*/
	private boolean setNotice;
	/**修改招收条件权限*/
	private boolean reviseterm;
	/**门派升级权限*/
	private boolean guildUp;
	/**阁楼升级权限*/
	private boolean schoolUp;
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPostMaxCount() {
		return postMaxCount;
	}

	public void setPostMaxCount(int postMaxCount) {
		this.postMaxCount = postMaxCount;
	}

	public boolean isAppoint() {
		return appoint;
	}

	public void setAppoint(boolean appoint) {
		this.appoint = appoint;
	}

	public boolean isExpel() {
		return expel;
	}

	public void setExpel(boolean expel) {
		this.expel = expel;
	}

	public boolean isRecruit() {
		return recruit;
	}

	public void setRecruit(boolean recruit) {
		this.recruit = recruit;
	}

	public boolean isSetNotice() {
		return setNotice;
	}

	public void setSetNotice(boolean setNotice) {
		this.setNotice = setNotice;
	}

	public boolean isReviseterm() {
		return reviseterm;
	}

	public void setReviseterm(boolean reviseterm) {
		this.reviseterm = reviseterm;
	}

	public boolean isGuildUp() {
		return guildUp;
	}

	public void setGuildUp(boolean guildUp) {
		this.guildUp = guildUp;
	}

	public boolean isSchoolUp() {
		return schoolUp;
	}

	public void setSchoolUp(boolean schoolUp) {
		this.schoolUp = schoolUp;
	}

	
}
