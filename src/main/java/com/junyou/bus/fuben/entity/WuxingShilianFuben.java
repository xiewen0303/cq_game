package com.junyou.bus.fuben.entity;
import java.io.Serializable;

import com.junyou.constants.GameConstants;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("wuxing_shilian_fuben")
public class WuxingShilianFuben extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("today_fight_num")
	private Integer todayFightNum;

	@Column("create_time")
	private Long createTime;

	@Column("update_time")
	private Long updateTime;

	@EntityField
    private int state = GameConstants.FUBEN_STATE_READY;

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getTodayFightNum(){
		return todayFightNum;
	}

	public  void setTodayFightNum(Integer todayFightNum){
		this.todayFightNum = todayFightNum;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Long createTime){
		this.createTime = createTime;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public WuxingShilianFuben copy(){
		WuxingShilianFuben result = new WuxingShilianFuben();
		result.setUserRoleId(getUserRoleId());
		result.setTodayFightNum(getTodayFightNum());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
	
	
}
