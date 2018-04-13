package com.junyou.public_.rank.entity;

/*import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.IEntity;
@Table("rank")
public class Rank  implements Serializable , IEntity{

	private static final long serialVersionUID = 1L;

	
	private Long ranKId;
	
	private Integer rank;
	
	private Long userRoleId;
	
	private Integer rankType;
	
	private Timestamp lastRefresh;
	
	
	
	public Long getRanKId() {
		return ranKId;
	}

	public void setRanKId(Long ranKId) {
		this.ranKId = ranKId;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getRankType() {
		return rankType;
	}

	public void setRankType(Integer rankType) {
		this.rankType = rankType;
	}

	public Timestamp getLastRefresh() {
		return lastRefresh;
	}

	public void setLastRefresh(Timestamp lastRefresh) {
		this.lastRefresh = lastRefresh;
	}

	@Override
	public String getPirmaryKeyName() {
		return "rankId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return getRanKId();
	}

	@Override
	public IEntity copy() {
		Rank copy=new Rank();
		copy.setRanKId(getRanKId());
		copy.setRank(getRank());
		copy.setRankType(getRankType());
		copy.setUserRoleId(getUserRoleId());
		copy.setLastRefresh(getLastRefresh());
		return copy;
	}

}

*/
public class Rank{//排行需要入库请用上
	
}