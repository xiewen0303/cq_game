package com.junyou.bus.giftcard.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("gift_card")
public class GiftCard extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("cardno")
	private String cardno;

	@Column("use_time")
	private Long useTime;


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public String getCardno(){
		return cardno;
	}

	public  void setCardno(String cardno){
		this.cardno = cardno;
	}

	public Long getUseTime(){
		return useTime;
	}

	public  void setUseTime(Long useTime){
		this.useTime = useTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public GiftCard copy(){
		GiftCard result = new GiftCard();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setCardno(getCardno());
		result.setUseTime(getUseTime());
		return result;
	}
}
