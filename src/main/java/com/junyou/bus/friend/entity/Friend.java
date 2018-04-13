package com.junyou.bus.friend.entity;
import java.io.Serializable;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("friend")
public class Friend extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("friend_ids")
	private String friendIds;

	@Column("black_ids")
	private String blackIds;

	@Column("enemy_ids")
	private String enemyIds;

	@Column("create_time")
	private Long createTime;

	@Column("is_accept")
	private Integer isAccept; //  0:接受,1:不接受

	@Column("reply_state")
	private Integer replyState;

	/**
	 * 玩家Id=仇恨击杀次数
	 */
	@EntityField
	private transient Map<Long,Integer> enemyDatas;

	public Map<Long,Integer> getEnemyDatas(){
		return enemyDatas;
	}
	
	public void setEnemyDatas(Map<Long,Integer> enemyDatas){
		this.enemyDatas = enemyDatas;
		this.enemyIds = JSONObject.toJSONString(enemyDatas);
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public String getFriendIds(){
		return friendIds;
	}

	public  void setFriendIds(String friendIds){
		this.friendIds = friendIds;
	}

	public String getBlackIds(){
		return blackIds;
	}

	public  void setBlackIds(String blackIds){
		this.blackIds = blackIds;
	}

	public String getEnemyIds(){
		return enemyIds;
	}

	public  void setEnemyIds(String enemyIds){
		this.enemyIds = enemyIds;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Long createTime){
		this.createTime = createTime;
	}
	/**
	 * 0:接受,1:不接受
	 * @return
	 */
	public Integer getIsAccept(){
		return isAccept;
	}

	public  void setIsAccept(Integer isAccept){
		this.isAccept = isAccept;
	}

	public Integer getReplyState(){
		return replyState;
	}

	public  void setReplyState(Integer replyState){
		this.replyState = replyState;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public Friend copy(){
		Friend result = new Friend();
		result.setUserRoleId(getUserRoleId());
		result.setFriendIds(getFriendIds());
		result.setBlackIds(getBlackIds());
		result.setEnemyIds(getEnemyIds());
		result.setCreateTime(getCreateTime());
		result.setIsAccept(getIsAccept());
		result.setReplyState(getReplyState());
		return result;
	}
}
