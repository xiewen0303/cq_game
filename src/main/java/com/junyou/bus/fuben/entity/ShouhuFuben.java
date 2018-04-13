package com.junyou.bus.fuben.entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("shouhu_fuben")
public class ShouhuFuben extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("start_id")
	private Integer startId;

	@Column("now_id")
	private Integer nowId;

	@Column("max_id")
	private Integer maxId;

	@Column("restart_time")
	private Integer restartTime;

	@Column("update_time")
	private Long updateTime;

	@Column("gift_state")
	private String giftState;
	
	@EntityField
	private List<Integer> state;
	
	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getNowId(){
		return nowId;
	}

	public  void setNowId(Integer nowId){
		this.nowId = nowId;
	}

	public Integer getMaxId(){
		return maxId;
	}

	public  void setMaxId(Integer maxId){
		this.maxId = maxId;
	}

	public Integer getRestartTime(){
		return restartTime;
	}

	public  void setRestartTime(Integer restartTime){
		this.restartTime = restartTime;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public String getGiftState(){
		return giftState;
	}

	public Integer getStartId() {
		return startId;
	}

	public void setStartId(Integer startId) {
		this.startId = startId;
	}

	public  void setGiftState(String giftState){
		this.giftState = giftState;
		if(!ObjectUtil.strIsEmpty(giftState)){
			try {
				String[] states = giftState.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
				if(state == null){
					state = new ArrayList<>();
				}
				for (String string : states) {
					state.add(Integer.parseInt(string));
				}
			} catch (Exception e) {
				ChuanQiLog.error("守护副本礼包状态解析异常，user_role_id:" + userRoleId);
			}
		}
	}

	public List<Integer> getState() {
		if(state == null){
			state = new ArrayList<>();
		}
		return state;
	}
	
	/**
	 * 将内存中的状态数字转为需要持久化的字符串格式
	 */
	public void refreshState(){
		if(state != null && state.size() > 0){
			StringBuffer sb = new StringBuffer();
			for (Integer st : state) {
				sb.append(GameConstants.CONFIG_SUB_SPLIT_CHAR);
				sb.append(st);
			}
			giftState = sb.substring(GameConstants.CONFIG_SUB_SPLIT_CHAR.length());
		}
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public ShouhuFuben copy(){
		ShouhuFuben result = new ShouhuFuben();
		result.setUserRoleId(getUserRoleId());
		result.setNowId(getNowId());
		result.setMaxId(getMaxId());
		result.setRestartTime(getRestartTime());
		result.setUpdateTime(getUpdateTime());
		result.setGiftState(getGiftState());
		result.setStartId(getStartId());
		return result;
	}
}
